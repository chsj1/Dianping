package com.dianping.membercard.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewHolderAdapter extends BasicAdapter
{
  private List<ViewHolder> mHolderList = new ArrayList();

  public void add(ViewHolder paramViewHolder)
  {
    if (paramViewHolder == null)
      return;
    this.mHolderList.add(paramViewHolder);
    notifyDataSetChanged();
  }

  public void add(List<ViewHolder> paramList)
  {
    if (paramList == null)
      return;
    this.mHolderList.addAll(paramList);
    notifyDataSetChanged();
  }

  public int getCount()
  {
    return this.mHolderList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.mHolderList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ViewHolder localViewHolder = (ViewHolder)getItem(paramInt);
    Context localContext = null;
    if (paramViewGroup != null)
      localContext = paramViewGroup.getContext();
    while (true)
    {
      return localViewHolder.inflate(localContext, paramView, paramViewGroup);
      if (paramView == null)
        continue;
      localContext = paramView.getContext();
    }
  }

  public void reset()
  {
    this.mHolderList.clear();
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ViewHolderAdapter
 * JD-Core Version:    0.6.0
 */