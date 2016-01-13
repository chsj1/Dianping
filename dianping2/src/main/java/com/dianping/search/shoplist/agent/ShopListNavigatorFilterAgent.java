package com.dianping.search.shoplist.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.search.widget.SearchTwinListFilterDialog;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class ShopListNavigatorFilterAgent extends ShopListAgent
  implements FilterBar.OnItemClickListener, FilterDialog.OnFilterListener
{
  private static final String CELL_NAVI_FILTER = "020Navi";
  protected FilterBar filterBar;
  SearchTwinListFilterDialog mCategoryDlg;
  SearchTwinListFilterDialog mRegionDlg;
  ListFilterDialog mSortDlg;

  public ShopListNavigatorFilterAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void addFilterItems()
  {
    this.filterBar.addItem("region", "全部商区").setGAString("distance");
    this.filterBar.addItem("category", "全部分类").setGAString("category");
    this.filterBar.addItem("rank", "智能排序").setGAString("sort");
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getDataSource() == null)
      return;
    if (this.filterBar == null)
    {
      paramBundle = inflater().inflate(R.layout.filter_layout, getParentView(), false);
      this.filterBar = ((FilterBar)paramBundle.findViewById(R.id.filterBar));
      addFilterItems();
      this.filterBar.setOnItemClickListener(this);
      addCell("020Navi", paramBundle);
    }
    updateNavs();
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("region".equals(paramObject))
      if (getDataSource().regionNavTree != null);
    do
      while (true)
      {
        return;
        if (this.mRegionDlg == null)
          this.mRegionDlg = new SearchTwinListFilterDialog(getActivity(), "distance_right");
        this.mRegionDlg.setTag(paramObject);
        this.mRegionDlg.setOnFilterListener(this);
        this.mRegionDlg.setHasAll(false);
        this.mRegionDlg.setNavTree(getDataSource().regionNavTree);
        localObject = this.mRegionDlg;
        if (getDataSource().curRegion() == null);
        for (paramObject = ShopListConst.ALL_REGION; ; paramObject = getDataSource().curRegion())
        {
          ((SearchTwinListFilterDialog)localObject).setSelectedItem(paramObject);
          this.mRegionDlg.show(paramView);
          return;
        }
        if (!"category".equals(paramObject))
          break;
        if (getDataSource().categoryNavTree == null)
          continue;
        if (this.mCategoryDlg == null)
          this.mCategoryDlg = new SearchTwinListFilterDialog(getActivity(), "category_right");
        this.mCategoryDlg.setTag(paramObject);
        this.mCategoryDlg.setOnFilterListener(this);
        this.mCategoryDlg.setHeaderItem(ShopListConst.ALL_CATEGORY);
        this.mCategoryDlg.setHasAll(false);
        this.mCategoryDlg.setNavTree(getDataSource().categoryNavTree);
        localObject = this.mCategoryDlg;
        if (getDataSource().curCategory() == null);
        for (paramObject = ShopListConst.ALL_CATEGORY; ; paramObject = getDataSource().curCategory())
        {
          ((SearchTwinListFilterDialog)localObject).setSelectedItem(paramObject);
          this.mCategoryDlg.show(paramView);
          return;
        }
      }
    while (!"rank".equals(paramObject));
    if (this.mSortDlg == null)
      this.mSortDlg = new ListFilterDialog(getActivity(), "sort_select");
    this.mSortDlg.setTag(paramObject);
    this.mSortDlg.setItems(getDataSource().filterSorts());
    Object localObject = this.mSortDlg;
    if (getDataSource().curSort() == null);
    for (paramObject = ShopListConst.DEFAULT_SORT; ; paramObject = getDataSource().curSort())
    {
      ((ListFilterDialog)localObject).setSelectedItem(paramObject);
      this.mSortDlg.setOnFilterListener(this);
      this.mSortDlg.show(paramView);
      return;
    }
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    if ("region".equals(paramFilterDialog.getTag()))
      if (getDataSource().filterRegions() != null);
    while (true)
    {
      do
        return;
      while (!(paramObject instanceof DPObject));
      if (!getDataSource().setCurRegion((DPObject)paramObject))
      {
        paramFilterDialog.dismiss();
        return;
      }
      if ("category".equals(paramFilterDialog.getTag()))
      {
        if ((getDataSource().filterCategories() == null) || (!(paramObject instanceof DPObject)))
          continue;
        if (!getDataSource().setCurCategory((DPObject)paramObject))
        {
          paramFilterDialog.dismiss();
          return;
        }
      }
      if (!"rank".equals(paramFilterDialog.getTag()))
        break;
      if ((getDataSource().filterSorts() == null) || (!(paramObject instanceof DPObject)))
        continue;
      if (!getDataSource().setCurSort((DPObject)paramObject))
      {
        paramFilterDialog.dismiss();
        return;
      }
      if (ShopListUtils.checkFilterable(getActivity(), (DPObject)paramObject))
        break;
      paramFilterDialog.dismiss();
      return;
    }
    updateNavs();
    paramFilterDialog.dismiss();
    getDataSource().reset(true);
    getDataSource().reload(false);
  }

  public void updateNavs()
  {
    Object localObject1 = null;
    DPObject localDPObject2 = null;
    DPObject localDPObject1 = null;
    if (getDataSource().curRegion() != null)
      localObject1 = getDataSource().curRegion();
    if (getDataSource().curCategory() != null)
      localDPObject2 = getDataSource().curCategory();
    if (getDataSource().curSort() != null)
      localDPObject1 = getDataSource().curSort();
    Object localObject2;
    if (localObject1 != null)
    {
      localObject2 = ((DPObject)localObject1).getString("Name");
      localObject1 = localObject2;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject1 = localObject2;
        if (((String)localObject2).contains("（智能范围）"))
          localObject1 = ((String)localObject2).replace("（智能范围）", "");
      }
      FilterBar localFilterBar = this.filterBar;
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "全部商区";
      localFilterBar.setItem("region", (String)localObject2);
      localObject2 = this.filterBar;
      if (localDPObject2 != null)
        break label174;
      localObject1 = "全部分类";
      label141: ((FilterBar)localObject2).setItem("category", (String)localObject1);
      localObject2 = this.filterBar;
      if (localDPObject1 != null)
        break label185;
    }
    label174: label185: for (localObject1 = "默认排序"; ; localObject1 = localDPObject1.getString("Name"))
    {
      ((FilterBar)localObject2).setItem("rank", (String)localObject1);
      return;
      localObject2 = null;
      break;
      localObject1 = localDPObject2.getString("Name");
      break label141;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListNavigatorFilterAgent
 * JD-Core Version:    0.6.0
 */