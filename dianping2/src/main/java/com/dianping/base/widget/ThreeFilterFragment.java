package com.dianping.base.widget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractFilterFragment;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterBarClickListener;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ThreeFilterFragment extends AbstractFilterFragment
  implements FilterBar.OnItemClickListener
{
  protected final FilterDialog.OnFilterListener filterListener = new FilterDialog.OnFilterListener()
  {
    public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
    {
      if ("region".equals(paramFilterDialog.getTag()))
        if (ThreeFilterFragment.this.regionNavs != null);
      label22: 
      do
      {
        while (true)
        {
          break label22;
          do
            return;
          while (!(paramObject instanceof DPObject));
          ThreeFilterFragment.access$102(ThreeFilterFragment.this, (DPObject)paramObject);
          ThreeFilterFragment.this.updateNavs(null, ThreeFilterFragment.this.currentRegion, null);
          if ("category".equals(paramFilterDialog.getTag()))
          {
            if ((ThreeFilterFragment.this.categoryNavs == null) || (!(paramObject instanceof DPObject)))
              continue;
            ThreeFilterFragment.access$402(ThreeFilterFragment.this, (DPObject)paramObject);
            ThreeFilterFragment.this.updateNavs(ThreeFilterFragment.this.currentCategory, null, null);
          }
          if (!"rank".equals(paramFilterDialog.getTag()))
            break;
          if ((ThreeFilterFragment.this.filterNavs == null) || (!(paramObject instanceof DPObject)))
            continue;
          if (!ThreeFilterFragment.this.checkFilterable((DPObject)paramObject))
          {
            paramFilterDialog.dismiss();
            return;
          }
          ThreeFilterFragment.access$802(ThreeFilterFragment.this, (DPObject)paramObject);
          ThreeFilterFragment.this.updateNavs(null, null, ThreeFilterFragment.this.currentFilter);
        }
        paramFilterDialog.dismiss();
      }
      while (ThreeFilterFragment.this.onFilterItemClickListener == null);
      ThreeFilterFragment.this.onFilterItemClickListener.onFilterItemClick(ThreeFilterFragment.this.currentCategory, ThreeFilterFragment.this.currentRegion, ThreeFilterFragment.this.currentFilter);
    }
  };

  protected void addFilterItems()
  {
    this.filterBar.addItem("region", "全部商区");
    this.filterBar.addItem("category", "全部分类");
    this.filterBar.addItem("rank", "默认排序");
  }

  protected boolean displayIcon()
  {
    return true;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    updateNavs(null, null, null);
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("region".equals(paramObject))
    {
      if (this.regionNavs == null)
        return;
      localObject = new TwinListFilterDialog(getActivity());
      ((TwinListFilterDialog)localObject).setTag(paramObject);
      ((TwinListFilterDialog)localObject).setOnFilterListener(this.filterListener);
      ((TwinListFilterDialog)localObject).setHeaderItem(ALL_REGION);
      ((TwinListFilterDialog)localObject).setItems(this.regionNavs);
      if (this.currentRegion == null)
      {
        localDPObject = ALL_REGION;
        label73: ((TwinListFilterDialog)localObject).setSelectedItem(localDPObject);
        ((TwinListFilterDialog)localObject).show(paramView);
      }
    }
    label85: 
    do
    {
      if (this.onFilterBarClickListener == null)
        break label285;
      this.onFilterBarClickListener.onFilterBarClick(paramObject);
      return;
      localDPObject = this.currentRegion;
      break label73;
      if (!"category".equals(paramObject))
        continue;
      if (this.categoryNavs == null)
        break;
      localObject = new TwinListFilterDialog(getActivity());
      ((TwinListFilterDialog)localObject).setTag(paramObject);
      ((TwinListFilterDialog)localObject).setOnFilterListener(this.filterListener);
      ((TwinListFilterDialog)localObject).setHeaderItem(ALL_CATEGORY);
      ((TwinListFilterDialog)localObject).setItems(this.categoryNavs);
      if (this.currentCategory == null);
      for (localDPObject = ALL_CATEGORY; ; localDPObject = this.currentCategory)
      {
        ((TwinListFilterDialog)localObject).setSelectedItem(localDPObject);
        ((TwinListFilterDialog)localObject).setDisplayIcon(displayIcon());
        ((TwinListFilterDialog)localObject).show(paramView);
        break;
      }
    }
    while (!"rank".equals(paramObject));
    Object localObject = new ListFilterDialog(getActivity());
    ((ListFilterDialog)localObject).setTag(paramObject);
    ((ListFilterDialog)localObject).setItems(this.filterNavs);
    if (this.currentFilter == null);
    for (DPObject localDPObject = DEFAULT_FILTER; ; localDPObject = this.currentFilter)
    {
      ((ListFilterDialog)localObject).setSelectedItem(localDPObject);
      ((ListFilterDialog)localObject).setOnFilterListener(this.filterListener);
      ((ListFilterDialog)localObject).show(paramView);
      break label85;
      label285: break;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.filter_layout, paramViewGroup, false);
    this.filterBar = ((FilterBar)paramLayoutInflater.findViewById(R.id.filterBar));
    addFilterItems();
    this.filterBar.setOnItemClickListener(this);
    return paramLayoutInflater;
  }

  public void setNavs(DPObject[] paramArrayOfDPObject1, DPObject[] paramArrayOfDPObject2, DPObject[] paramArrayOfDPObject3)
  {
    this.categoryNavs = paramArrayOfDPObject1;
    this.regionNavs = paramArrayOfDPObject2;
    this.filterNavs = paramArrayOfDPObject3;
    updateNavs(this.currentCategory, this.currentRegion, this.currentFilter);
  }

  public void updateNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (paramDPObject2 != null)
      this.currentRegion = paramDPObject2;
    if (paramDPObject1 != null)
      this.currentCategory = paramDPObject1;
    if (paramDPObject3 != null)
      this.currentFilter = paramDPObject3;
    if (this.currentRegion != null)
    {
      paramDPObject2 = this.currentRegion.getString("Name");
      paramDPObject1 = paramDPObject2;
      if (!TextUtils.isEmpty(paramDPObject2))
      {
        paramDPObject1 = paramDPObject2;
        if (paramDPObject2.contains("（智能范围）"))
          paramDPObject1 = paramDPObject2.replace("（智能范围）", "");
      }
      paramDPObject3 = this.filterBar;
      paramDPObject2 = paramDPObject1;
      if (paramDPObject1 == null)
        paramDPObject2 = "全部商区";
      paramDPObject3.setItem("region", paramDPObject2);
      paramDPObject2 = this.filterBar;
      if (this.currentCategory != null)
        break label144;
      paramDPObject1 = "全部分类";
      label109: paramDPObject2.setItem("category", paramDPObject1);
      paramDPObject2 = this.filterBar;
      if (this.currentFilter != null)
        break label157;
    }
    label144: label157: for (paramDPObject1 = "默认排序"; ; paramDPObject1 = this.currentFilter.getString("Name"))
    {
      paramDPObject2.setItem("rank", paramDPObject1);
      return;
      paramDPObject2 = null;
      break;
      paramDPObject1 = this.currentCategory.getString("Name");
      break label109;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ThreeFilterFragment
 * JD-Core Version:    0.6.0
 */