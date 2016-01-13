package com.dianping.takeaway.entity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.v1.R.layout;
import java.util.List;

public class TakeawayIconPagerAdapter extends PagerAdapter
{
  private TakeawayMainDataSource dataSource;
  private SparseArray<MeasuredGridView> gridViewsContainer = new SparseArray();
  private Context mContext;

  public TakeawayIconPagerAdapter(Context paramContext, TakeawayMainDataSource paramTakeawayMainDataSource)
  {
    this.mContext = paramContext;
    this.dataSource = paramTakeawayMainDataSource;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    paramViewGroup.removeView((View)paramObject);
  }

  public int getCount()
  {
    return this.dataSource.iconParser.pagerIconObjs.size();
  }

  public int getItemPosition(Object paramObject)
  {
    return -2;
  }

  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    MeasuredGridView localMeasuredGridView;
    if (this.gridViewsContainer.get(paramInt) == null)
    {
      localMeasuredGridView = (MeasuredGridView)LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_icon_gridview, paramViewGroup, false);
      localMeasuredGridView.setNumColumns(this.dataSource.iconParser.perLineIconNum);
      localMeasuredGridView.setAdapter(new TakeawayIconGridViewAdapter());
      this.gridViewsContainer.put(paramInt, localMeasuredGridView);
    }
    while (true)
    {
      TakeawayIconGridViewAdapter localTakeawayIconGridViewAdapter = (TakeawayIconGridViewAdapter)localMeasuredGridView.getAdapter();
      localTakeawayIconGridViewAdapter.setData(this.mContext, (DPObject[])this.dataSource.iconParser.pagerIconObjs.get(paramInt));
      localTakeawayIconGridViewAdapter.notifyDataSetChanged();
      paramViewGroup.addView(localMeasuredGridView);
      return localMeasuredGridView;
      localMeasuredGridView = (MeasuredGridView)this.gridViewsContainer.get(paramInt);
    }
  }

  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    return paramView == paramObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayIconPagerAdapter
 * JD-Core Version:    0.6.0
 */