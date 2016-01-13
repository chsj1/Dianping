package com.dianping.mall.nearby;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;

public class MallNearByFilterBar extends FilterBar
  implements FilterBar.OnItemClickListener
{
  private Context context;
  private FilterDialog dlg;
  private FilterChangeListener filterChangeListener;
  private FilterDialog.OnFilterListener filterListener;
  private MallNearByDataSource mallNearByDataSource;

  public MallNearByFilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    setOnItemClickListener(this);
  }

  private FilterDialog.OnFilterListener getFilterListener()
  {
    if (this.filterListener != null)
      return this.filterListener;
    this.filterListener = new MallNearByFilterBar.1(this);
    return this.filterListener;
  }

  public FilterChangeListener getFilterChangeListener()
  {
    return this.filterChangeListener;
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (this.mallNearByDataSource == null);
    do
      while (true)
      {
        return;
        if ("catagory".equals(paramObject))
        {
          if (this.mallNearByDataSource.filterCategories() == null)
            continue;
          this.dlg = new ListFilterDialog((Activity)this.context, "category_right");
          this.dlg.setTag(paramObject);
          ((ListFilterDialog)this.dlg).setItems(this.mallNearByDataSource.filterCategories());
          ((ListFilterDialog)this.dlg).setSelectedItem(this.mallNearByDataSource.curCategory());
          this.dlg.setOnFilterListener(getFilterListener());
          this.dlg.show(paramView);
          return;
        }
        if (!"sort".equals(paramObject))
          break;
        if (this.mallNearByDataSource.filterSorts() == null)
          continue;
        this.dlg = new ListFilterDialog((Activity)this.context, "sort_select");
        this.dlg.setTag(paramObject);
        ((ListFilterDialog)this.dlg).setItems(this.mallNearByDataSource.filterSorts());
        ((ListFilterDialog)this.dlg).setSelectedItem(this.mallNearByDataSource.curSort());
        this.dlg.setOnFilterListener(getFilterListener());
        this.dlg.show(paramView);
        return;
      }
    while ((!"floor".equals(paramObject)) || (this.mallNearByDataSource.floorNavs() == null));
    this.dlg = new ListFilterDialog((Activity)this.context, "floor_select");
    this.dlg.setTag(paramObject);
    ((ListFilterDialog)this.dlg).setItems(this.mallNearByDataSource.floorNavs());
    ((ListFilterDialog)this.dlg).setSelectedItem(this.mallNearByDataSource.curFloor());
    this.dlg.setOnFilterListener(getFilterListener());
    this.dlg.show(paramView);
  }

  public void setFilterChangeListener(FilterChangeListener paramFilterChangeListener)
  {
    this.filterChangeListener = paramFilterChangeListener;
  }

  public void setMallNearByDataSource(MallNearByDataSource paramMallNearByDataSource)
  {
    this.mallNearByDataSource = paramMallNearByDataSource;
  }

  public void updateFilter()
  {
    setItem("catagory", this.mallNearByDataSource.curCategory().getString("Name"));
    setItem("sort", this.mallNearByDataSource.curSort().getString("Name"));
    setItem("floor", this.mallNearByDataSource.curFloor().getString("Name"));
  }

  public static abstract interface FilterChangeListener
  {
    public abstract void filterChange();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.MallNearByFilterBar
 * JD-Core Version:    0.6.0
 */