package com.dianping.membercard.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicAdapter;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicCardAdapter<T> extends BasicAdapter
{
  private boolean isEnd;
  private boolean isPagination;
  private Context mContext;
  private List<T> mData;
  private String mEmptyString;
  private String mErrorString;

  public BasicCardAdapter(Context paramContext, List<T> paramList, boolean paramBoolean)
  {
    this.mContext = paramContext;
    if (paramList == null);
    for (paramContext = new ArrayList(); ; paramContext = new ArrayList(paramList))
    {
      this.mData = paramContext;
      this.isPagination = paramBoolean;
      return;
    }
  }

  private View handleSpecialStatusView(Object paramObject, View paramView, ViewGroup paramViewGroup)
  {
    if (paramObject == ERROR)
      return getFailedView(this.mEmptyString, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          BasicCardAdapter.this.reset();
        }
      }
      , paramViewGroup, paramView);
    if (paramObject == EMPTY)
      return getEmptyView(this.mEmptyString, "数据为空", paramViewGroup, paramView);
    if (this.mErrorString == null)
      loadNextPage();
    return getLoadingView(paramViewGroup, paramView);
  }

  private boolean isSpecialStatus(Object paramObject)
  {
    return (paramObject == EMPTY) || (paramObject == LOADING) || (paramObject == ERROR);
  }

  public void addAll(List<T> paramList)
  {
    this.mData.addAll(paramList);
    notifyDataSetChanged();
  }

  public int getCount()
  {
    int i = this.mData.size();
    if (!this.isPagination);
    do
    {
      return i;
      if (this.mEmptyString != null)
        return i + 1;
    }
    while (this.isEnd);
    return i + 1;
  }

  public Object getItem(int paramInt)
  {
    if (!this.isPagination)
      return this.mData.get(paramInt);
    if ((this.mEmptyString != null) && (paramInt == 0))
      return EMPTY;
    int i = paramInt;
    if (this.mEmptyString != null)
      i = paramInt - 1;
    if (i < this.mData.size())
      return this.mData.get(i);
    if (this.mErrorString == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public abstract int getItemResource();

  public abstract View getItemView(int paramInt, View paramView, BasicCardAdapter<T>.ViewHolder paramBasicCardAdapter);

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if (isSpecialStatus(localObject))
      return handleSpecialStatusView(localObject, paramView, paramViewGroup);
    if (paramView == null)
    {
      paramViewGroup = new Object();
      if ((paramView != null) && (paramViewGroup.getClass() == ViewHolder.class))
        break label94;
      paramView = View.inflate(this.mContext, getItemResource(), null);
      paramViewGroup = new ViewHolder(paramView);
      paramView.setTag(paramViewGroup);
    }
    while (true)
    {
      return getItemView(paramInt, paramView, paramViewGroup);
      paramViewGroup = paramView.getTag();
      break;
      label94: paramViewGroup = (ViewHolder)paramView.getTag();
    }
  }

  public void loadNextPage()
  {
  }

  public void remove(int paramInt)
  {
    this.mData.remove(paramInt);
    notifyDataSetChanged();
  }

  public void remove(T paramT)
  {
    this.mData.remove(paramT);
    notifyDataSetChanged();
  }

  public void replaceAll(List<T> paramList)
  {
    this.mData.clear();
    this.mData.addAll(paramList);
    notifyDataSetChanged();
  }

  public void reset()
  {
    this.mEmptyString = null;
    this.mErrorString = null;
    this.isEnd = false;
    this.mData = new ArrayList();
    notifyDataSetChanged();
  }

  public void setEmptyString(String paramString)
  {
    this.mEmptyString = paramString;
  }

  public void setErrorString(String paramString)
  {
    this.mErrorString = paramString;
  }

  public void updateEndStatus(boolean paramBoolean)
  {
    this.isEnd = paramBoolean;
  }

  public class ViewHolder
  {
    private View convertView;
    private SparseArray<View> views = new SparseArray();

    public ViewHolder(View arg2)
    {
      Object localObject;
      this.convertView = localObject;
    }

    public <T extends View> T getView(int paramInt)
    {
      View localView2 = (View)this.views.get(paramInt);
      View localView1 = localView2;
      if (localView2 == null)
      {
        localView1 = this.convertView.findViewById(paramInt);
        this.views.put(paramInt, localView1);
      }
      return localView1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.BasicCardAdapter
 * JD-Core Version:    0.6.0
 */