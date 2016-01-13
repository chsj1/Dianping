package com.dianping.selectdish.entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.selectdish.model.SuggestDishInfo;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;

public class SelectDishSuggestAdapter extends BasicAdapter
{
  private SelectDishSuggestDataSource dataSource;

  public SelectDishSuggestAdapter(SelectDishSuggestDataSource paramSelectDishSuggestDataSource)
  {
    this.dataSource = paramSelectDishSuggestDataSource;
  }

  private void loadNewPage()
  {
    this.dataSource.errorMsg = null;
    this.dataSource.reqSuggestDish();
  }

  public int getCount()
  {
    if ((!this.dataSource.isEnd) || (this.dataSource.emptyMsg != null))
      return this.dataSource.suggestDishList.size() + 1;
    return this.dataSource.suggestDishList.size();
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.dataSource.suggestDishList.size())
      return this.dataSource.suggestDishList.get(paramInt);
    if (this.dataSource.emptyMsg != null)
      return EMPTY;
    if (this.dataSource.errorMsg == null)
      return LOADING;
    return ERROR;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof SuggestDishInfo))
    {
      localObject = (SuggestDishInfo)localObject;
      if ((paramView == null) || (paramView.getTag() != this))
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_suggest_item, paramViewGroup, false);
        paramView.setTag(this);
      }
      while (true)
      {
        ((NetworkImageView)paramView.findViewById(R.id.dish_pic)).setImage(((SuggestDishInfo)localObject).picUrl);
        ((TextView)paramView.findViewById(R.id.dish_name)).setText(((SuggestDishInfo)localObject).name);
        ((TextView)paramView.findViewById(R.id.dish_desc)).setText(((SuggestDishInfo)localObject).desc);
        ((CheckBox)paramView.findViewById(R.id.check)).setChecked(((SuggestDishInfo)localObject).checked);
        return paramView;
      }
    }
    if (localObject == LOADING)
    {
      loadNewPage();
      return getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == EMPTY)
      return getEmptyView(this.dataSource.emptyMsg, null, paramViewGroup, paramView);
    return (View)getFailedView(this.dataSource.errorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        SelectDishSuggestAdapter.this.loadNewPage();
      }
    }
    , paramViewGroup, paramView);
  }

  public boolean hasStableIds()
  {
    return true;
  }

  public void reset()
  {
    this.dataSource.errorMsg = null;
    this.dataSource.emptyMsg = null;
    this.dataSource.suggestDishList.clear();
    this.dataSource.isEnd = false;
    this.dataSource.start = 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishSuggestAdapter
 * JD-Core Version:    0.6.0
 */