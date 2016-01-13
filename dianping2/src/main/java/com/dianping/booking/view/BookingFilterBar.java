package com.dianping.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.booking.BookingShoplistActvity;
import com.dianping.booking.util.BookingShoplistDataSource;

public class BookingFilterBar extends FilterBar
  implements FilterBar.OnItemClickListener
{
  private BookingShoplistActvity activity;
  private BookingShoplistDataSource datasource;
  private FilterDialog dlg;
  private FilterDialog.OnFilterListener filterListener;

  public BookingFilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.activity = ((BookingShoplistActvity)paramContext);
    setOnItemClickListener(this);
  }

  private FilterDialog.OnFilterListener getFilterListener()
  {
    if (this.filterListener != null)
      return this.filterListener;
    this.filterListener = new BookingFilterBar.1(this);
    return this.filterListener;
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (this.datasource == null);
    do
      while (true)
      {
        return;
        if ("region".equals(paramObject))
        {
          if (this.datasource.filterRegions() == null)
            continue;
          this.dlg = new TwinListFilterDialog(this.activity);
          ((TwinListFilterDialog)this.dlg).setHasAll(false);
          this.dlg.setTag(paramObject);
          ((TwinListFilterDialog)this.dlg).setItems(this.datasource.filterRegions());
          ((TwinListFilterDialog)this.dlg).setSelectedItem(this.datasource.curRegion());
          this.dlg.setOnFilterListener(getFilterListener());
          this.dlg.show(paramView);
          this.activity.statisticsEvent("booking6", "booking6_channel_filter_region", "", 0);
          return;
        }
        if (!"category".equals(paramObject))
          break;
        if (this.datasource.filterCategories() == null)
          continue;
        this.dlg = new TwinListFilterDialog(this.activity);
        ((TwinListFilterDialog)this.dlg).setHasAll(false);
        this.dlg.setTag(paramObject);
        ((TwinListFilterDialog)this.dlg).setItems(this.datasource.filterCategories());
        ((TwinListFilterDialog)this.dlg).setSelectedItem(this.datasource.curCategory());
        this.dlg.setOnFilterListener(getFilterListener());
        this.dlg.show(paramView);
        this.activity.statisticsEvent("booking6", "booking6_channel_filter_food", "", 0);
        return;
      }
    while ((!"rank".equals(paramObject)) || (this.datasource.filterSorts() == null));
    this.dlg = new ListFilterDialog(this.activity);
    this.dlg.setTag(paramObject);
    ((ListFilterDialog)this.dlg).setItems(this.datasource.filterSorts());
    ((ListFilterDialog)this.dlg).setSelectedItem(this.datasource.curSort());
    this.dlg.setOnFilterListener(getFilterListener());
    this.dlg.show(paramView);
    this.activity.statisticsEvent("booking6", "booking6_channel_filter_sort", "", 0);
  }

  public void setBookingShopListDataSource(BookingShoplistDataSource paramBookingShoplistDataSource)
  {
    this.datasource = paramBookingShoplistDataSource;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.BookingFilterBar
 * JD-Core Version:    0.6.0
 */