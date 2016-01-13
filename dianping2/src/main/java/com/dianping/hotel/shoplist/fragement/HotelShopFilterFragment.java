package com.dianping.hotel.shoplist.fragement;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterBarClickListener;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterItemClickListener;
import com.dianping.base.shoplist.activity.NShopListActivity;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.ThreeFilterFragment;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HotelShopFilterFragment extends ThreeFilterFragment
{
  protected final FilterDialog.OnFilterListener filterListener = new FilterDialog.OnFilterListener()
  {
    public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
    {
      if ("category".equals(paramFilterDialog.getTag()))
        if (HotelShopFilterFragment.this.categoryNavs != null);
      do
      {
        while (true)
        {
          do
            return;
          while (!(paramObject instanceof DPObject));
          HotelShopFilterFragment.access$102(HotelShopFilterFragment.this, (DPObject)paramObject);
          HotelShopFilterFragment.this.updateNavs(HotelShopFilterFragment.this.currentCategory, null, null);
          if (!"rank".equals(paramFilterDialog.getTag()))
            break;
          if ((HotelShopFilterFragment.this.filterNavs == null) || (!(paramObject instanceof DPObject)))
            continue;
          if (!HotelShopFilterFragment.this.checkFilterable((DPObject)paramObject))
          {
            paramFilterDialog.dismiss();
            return;
          }
          HotelShopFilterFragment.access$502(HotelShopFilterFragment.this, (DPObject)paramObject);
          HotelShopFilterFragment.this.updateNavs(null, null, HotelShopFilterFragment.this.currentFilter);
        }
        paramFilterDialog.dismiss();
      }
      while (HotelShopFilterFragment.this.onFilterItemClickListener == null);
      HotelShopFilterFragment.this.onFilterItemClickListener.onFilterItemClick(HotelShopFilterFragment.this.currentCategory, null, HotelShopFilterFragment.this.currentFilter);
    }
  };
  private boolean hasSelectedCategory;

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("category".equals(paramObject))
    {
      if (this.categoryNavs == null)
        return;
      this.hasSelectedCategory = true;
      localObject = getActivity();
      if (((localObject instanceof NShopListActivity)) && (!TextUtils.isEmpty(((NShopListActivity)localObject).getParaKeyword())))
        statisticsEvent("hotelkwlist5", "hotelkwlist5_category_click", "", 0);
      localListFilterDialog = new ListFilterDialog((Activity)localObject);
      localListFilterDialog.setTag(paramObject);
      localListFilterDialog.setItems(this.categoryNavs);
      if (this.currentCategory == null)
      {
        localObject = ALL_CATEGORY;
        localListFilterDialog.setSelectedItem((DPObject)localObject);
        localListFilterDialog.setOnFilterListener(this.filterListener);
        localListFilterDialog.show(paramView);
      }
    }
    label115: 
    do
    {
      if (this.onFilterBarClickListener == null)
        break label211;
      this.onFilterBarClickListener.onFilterBarClick(paramObject);
      return;
      localObject = this.currentCategory;
      break;
    }
    while (!"rank".equals(paramObject));
    ListFilterDialog localListFilterDialog = new ListFilterDialog(getActivity());
    localListFilterDialog.setTag(paramObject);
    localListFilterDialog.setItems(this.filterNavs);
    if (this.currentFilter == null);
    for (Object localObject = DEFAULT_FILTER; ; localObject = this.currentFilter)
    {
      localListFilterDialog.setSelectedItem((DPObject)localObject);
      localListFilterDialog.setOnFilterListener(this.filterListener);
      localListFilterDialog.show(paramView);
      break label115;
      label211: break;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.filter_layout, paramViewGroup, false);
    if (paramLayoutInflater != null)
    {
      this.filterBar = ((FilterBar)paramLayoutInflater.findViewById(R.id.filterBar));
      this.filterBar.addItem("category", "全部分类");
      this.filterBar.addItem("rank", "默认排序");
      this.filterBar.setOnItemClickListener(this);
    }
    return paramLayoutInflater;
  }

  public void onDetach()
  {
    onFragmentDetach();
    dismissProgressDialog();
  }

  public void updateNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (paramDPObject1 != null)
      this.currentCategory = paramDPObject1;
    if (paramDPObject3 != null)
      this.currentFilter = paramDPObject3;
    paramDPObject2 = "星级分类";
    paramDPObject1 = paramDPObject2;
    if (this.currentCategory != null)
    {
      if (this.currentCategory.getInt("ID") != 60)
        break label96;
      paramDPObject1 = paramDPObject2;
      if (this.hasSelectedCategory)
        paramDPObject1 = this.currentCategory.getString("Name");
    }
    this.filterBar.setItem("category", paramDPObject1);
    paramDPObject2 = this.filterBar;
    if (this.currentFilter == null);
    for (paramDPObject1 = "默认排序"; ; paramDPObject1 = this.currentFilter.getString("Name"))
    {
      paramDPObject2.setItem("rank", paramDPObject1);
      return;
      label96: paramDPObject1 = this.currentCategory.getString("Name");
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.fragement.HotelShopFilterFragment
 * JD-Core Version:    0.6.0
 */