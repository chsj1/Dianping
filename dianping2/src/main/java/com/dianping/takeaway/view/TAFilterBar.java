package com.dianping.takeaway.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;

public class TAFilterBar extends FilterBar
  implements FilterBar.OnItemClickListener
{
  private Context context;
  private FilterDialog dlg;
  private FilterChangeListener filterChangeListener;
  private FilterDialog.OnFilterListener filterListener;
  private TakeawaySampleShoplistDataSource taDataSource;

  public TAFilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    setOnItemClickListener(this);
  }

  private FilterDialog.OnFilterListener getFilterListener()
  {
    if (this.filterListener != null)
      return this.filterListener;
    this.filterListener = new FilterDialog.OnFilterListener()
    {
      public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
      {
        int j = 0;
        int i;
        if ("catagory".equals(paramFilterDialog.getTag()))
        {
          i = j;
          if (TAFilterBar.this.taDataSource.setCurCategory((DPObject)paramObject))
          {
            i = 1;
            paramObject = TAFilterBar.this.taDataSource.getGAUserInfo();
            paramObject.title = String.valueOf(TAFilterBar.this.taDataSource.curCategory().getInt("ID"));
            GAHelper.instance().contextStatisticsEvent(TAFilterBar.this.taDataSource.getActivity(), "categoryselect", paramObject, "tap");
          }
        }
        while (true)
        {
          if ((i != 0) && (TAFilterBar.this.filterChangeListener != null))
            TAFilterBar.this.filterChangeListener.filterChange();
          paramFilterDialog.dismiss();
          return;
          if ("sort".equals(paramFilterDialog.getTag()))
          {
            i = j;
            if (!TAFilterBar.this.taDataSource.setCurSort((DPObject)paramObject))
              continue;
            i = 1;
            paramObject = TAFilterBar.this.taDataSource.getGAUserInfo();
            paramObject.title = TAFilterBar.this.taDataSource.curSort().getString("ID");
            GAHelper.instance().contextStatisticsEvent(TAFilterBar.this.taDataSource.getActivity(), "sortselect", paramObject, "tap");
            continue;
          }
          i = j;
          if (!"multifilter".equals(paramFilterDialog.getTag()))
            continue;
          i = j;
          if (!TAFilterBar.this.taDataSource.setCurMultiFilterIds((String)paramObject))
            continue;
          i = 1;
          TAFilterBar.this.taDataSource.noShopReason = 0;
        }
      }
    };
    return this.filterListener;
  }

  public NovaLinearLayout addItem(Object paramObject, boolean paramBoolean)
  {
    if (super.getChildCount() > 0)
    {
      localObject = new ImageView(this.context);
      ((ImageView)localObject).setImageDrawable(getResources().getDrawable(R.drawable.filter_bar_divider));
      ((ImageView)localObject).setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
      super.addView((View)localObject);
    }
    Object localObject = (NovaLinearLayout)LayoutInflater.from(this.context).inflate(R.layout.takeaway_multifilter_bar_item, this, false);
    ((NovaLinearLayout)localObject).setTag(paramObject);
    ((NovaLinearLayout)localObject).setOnClickListener(this.handler);
    super.addView((View)localObject);
    super.setItem(paramObject, "筛选");
    setHightlight(paramObject, paramBoolean);
    return (NovaLinearLayout)localObject;
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (this.taDataSource == null);
    do
      while (true)
      {
        return;
        if ("catagory".equals(paramObject))
        {
          if (this.taDataSource.filterCategories() == null)
            continue;
          this.dlg = new TATwinListFilterDialog((Activity)this.context);
          this.dlg.setTag(paramObject);
          ((TATwinListFilterDialog)this.dlg).setHasAll(false);
          ((TATwinListFilterDialog)this.dlg).setDisplayIcon(false);
          ((TATwinListFilterDialog)this.dlg).setItems(this.taDataSource.filterCategories());
          ((TATwinListFilterDialog)this.dlg).setSelectedItem(this.taDataSource.curCategory());
          this.dlg.setOnFilterListener(getFilterListener());
          this.dlg.show(paramView);
          GAHelper.instance().contextStatisticsEvent(this.taDataSource.getActivity(), "category", null, "tap");
          return;
        }
        if (!"sort".equals(paramObject))
          break;
        if (this.taDataSource.filterSorts() == null)
          continue;
        this.dlg = new ListFilterDialog((Activity)this.context);
        this.dlg.setTag(paramObject);
        ((ListFilterDialog)this.dlg).setItems(this.taDataSource.filterSorts());
        ((ListFilterDialog)this.dlg).setSelectedItem(this.taDataSource.curSort());
        this.dlg.setOnFilterListener(getFilterListener());
        this.dlg.show(paramView);
        GAHelper.instance().contextStatisticsEvent(this.taDataSource.getActivity(), "sort", null, "tap");
        return;
      }
    while ((!"multifilter".equals(paramObject)) || (this.taDataSource.multiFilterNavs() == null));
    this.dlg = new TAMultiFilterDialog((Activity)this.context);
    this.dlg.setTag(paramObject);
    ((TAMultiFilterDialog)this.dlg).setTAOptionsData(this.taDataSource.multiFilterNavs(), this.taDataSource.curMultiFilterIds());
    this.dlg.setOnFilterListener(getFilterListener());
    this.dlg.show(paramView);
    GAHelper.instance().contextStatisticsEvent(this.taDataSource.getActivity(), "filter", null, "tap");
  }

  public void setFilterChangeListener(FilterChangeListener paramFilterChangeListener)
  {
    this.filterChangeListener = paramFilterChangeListener;
  }

  public void setHightlight(Object paramObject, boolean paramBoolean)
  {
    paramObject = super.findViewWithTag(paramObject);
    if (paramObject == null)
      return;
    paramObject = paramObject.findViewById(R.id.item_highlight);
    if (paramBoolean);
    for (int i = 0; ; i = 4)
    {
      paramObject.setVisibility(i);
      return;
    }
  }

  public void setTakeawayDataSource(TakeawaySampleShoplistDataSource paramTakeawaySampleShoplistDataSource)
  {
    this.taDataSource = paramTakeawaySampleShoplistDataSource;
  }

  public void updateFilter()
  {
    setItem("catagory", this.taDataSource.curCategory().getString("Name"));
    setItem("sort", this.taDataSource.curSort().getString("Name"));
    if (!TextUtils.isEmpty(this.taDataSource.curMultiFilterIds()));
    for (boolean bool = true; ; bool = false)
    {
      setHightlight("multifilter", bool);
      return;
    }
  }

  public static abstract interface FilterChangeListener
  {
    public abstract void filterChange();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAFilterBar
 * JD-Core Version:    0.6.0
 */