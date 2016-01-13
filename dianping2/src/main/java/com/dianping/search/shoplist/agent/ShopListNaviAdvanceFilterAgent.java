package com.dianping.search.shoplist.agent;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.data.model.NavTree;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.util.ShopListUtils;
import com.dianping.search.view.ShopFilterNaviView;
import com.dianping.search.view.ShopFilterNaviView.FilterListener;
import com.dianping.search.view.ShopFilterView;
import com.dianping.search.view.ShopFilterView.FilterListener;
import com.dianping.search.widget.AdvanceFilterBar;
import com.dianping.search.widget.SearchTwinListFilterDialog;
import com.dianping.search.widget.SearchTwinListFilterDialogWithRegionAndSubway;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopListNaviAdvanceFilterAgent extends ShopListAgent
  implements FilterDialog.OnFilterListener, ShopFilterView.FilterListener
{
  private static final String CELL_NAVI_FILTER = "020Navi";
  private NewShopListDataSource dataSource;
  protected boolean isFloor = false;
  protected AdvanceFilterBar mFilterBar;
  private DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener()
  {
    public void onDismiss(DialogInterface paramDialogInterface)
    {
      if (ShopListNaviAdvanceFilterAgent.this.dataSource == null);
      label102: label105: 
      while (true)
      {
        return;
        int i = 0;
        if (ShopListNaviAdvanceFilterAgent.this.dataSource.filterNavi != null)
        {
          paramDialogInterface = ShopListNaviAdvanceFilterAgent.this.dataSource.filterNavi.getArray("FilterGroups");
          if ((paramDialogInterface == null) || (paramDialogInterface.length <= 0))
            break label102;
        }
        for (i = 1; ; i = 0)
        {
          if (i == 0)
            break label105;
          paramDialogInterface = (ShopFilterNaviView)ShopListNaviAdvanceFilterAgent.this.mFilterBar.getItem(4).findViewById(R.id.shoplist_grid_filter);
          if (paramDialogInterface == null)
            break;
          paramDialogInterface.setNavList(ShopListNaviAdvanceFilterAgent.this.dataSource.filterNavi.getArray("FilterGroups"));
          return;
        }
      }
    }
  };

  public ShopListNaviAdvanceFilterAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateFilterItems()
  {
    if (this.dataSource == null)
      return;
    if (this.dataSource.regionNavTree == null)
      this.dataSource.regionNavTree = NavTree.from(ShopListUtils.genDefaultFilter(2));
    if ((this.isFloor) && (this.dataSource.floorNavList == null))
      this.dataSource.floorNavList = ShopListUtils.genDefaultFilter(4);
    this.isFloor = updateRegionDialog();
    if (this.dataSource.categoryNavTree == null)
      this.dataSource.categoryNavTree = NavTree.from(ShopListUtils.genDefaultFilter(1));
    Object localObject2 = this.mFilterBar.getItem(2);
    Object localObject1 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = new SearchTwinListFilterDialog(getActivity(), "category");
      ((SearchTwinListFilterDialog)localObject1).setHeaderItem(ShopListConst.ALL_CATEGORY);
      ((SearchTwinListFilterDialog)localObject1).setHasAll(false);
      ((FilterDialog)localObject1).setOnFilterListener(this);
      this.mFilterBar.addItem(2, "category", (FilterDialog)localObject1);
    }
    ((SearchTwinListFilterDialog)localObject1).setNavTree(this.dataSource.categoryNavTree);
    localObject2 = (SearchTwinListFilterDialog)localObject1;
    label292: int i;
    label415: int j;
    if (this.dataSource.curCategory() == null)
    {
      localObject1 = ShopListConst.ALL_CATEGORY;
      ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      if (this.dataSource.filterSorts() == null)
        this.dataSource.setFilterSorts(ShopListUtils.genDefaultFilter(3));
      localObject2 = this.mFilterBar.getItem(3);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new ListFilterDialog(getActivity(), "sort_select");
        ((FilterDialog)localObject1).setOnFilterListener(this);
        this.mFilterBar.addItem(3, "rank", (FilterDialog)localObject1);
      }
      ((ListFilterDialog)localObject1).setItems(this.dataSource.filterSorts());
      localObject2 = (ListFilterDialog)localObject1;
      if (this.dataSource.curSort() != null)
        break label595;
      localObject1 = ShopListConst.DEFAULT_SORT;
      ((ListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      if (this.dataSource.needAdvFilter)
      {
        localObject2 = this.mFilterBar.getItem(4);
        localObject1 = localObject2;
        if (localObject2 == null)
        {
          localObject1 = new FilterDialog(getActivity());
          ((FilterDialog)localObject1).setFilterView(((FilterDialog)localObject1).getLayoutInflater().inflate(R.layout.search_filter_viewstub, ((FilterDialog)localObject1).getFilterViewParent(), false));
          this.mFilterBar.addItem(4, "framefilter", (FilterDialog)localObject1);
          this.mFilterBar.getItem(4).setOnDismissListener(this.onDismissListener);
        }
        i = 0;
        if (this.dataSource.filterNavi != null)
        {
          localObject2 = this.dataSource.filterNavi.getArray("FilterGroups");
          if ((localObject2 == null) || (localObject2.length <= 0))
            break label606;
          i = 1;
        }
        if ((i != 0) || (this.dataSource.filterSelectNavs() != null))
        {
          if (i == 0)
            break label611;
          j = R.id.grid_viewstub;
          label438: localObject2 = (ViewStub)((FilterDialog)localObject1).findViewById(j);
          if (localObject2 != null)
            ((ViewStub)localObject2).inflate();
          if (i == 0)
            break label619;
          localObject1 = (ShopFilterNaviView)((FilterDialog)localObject1).findViewById(R.id.shoplist_grid_filter);
          ((ShopFilterNaviView)localObject1).setClickable(true);
          ((ShopFilterNaviView)localObject1).setNavList(this.dataSource.filterNavi.getArray("FilterGroups"));
          ((ShopFilterNaviView)localObject1).setFilterListener(new ShopFilterNaviView.FilterListener()
          {
            public void onfilterList(String paramString, boolean paramBoolean)
            {
              if (ShopListNaviAdvanceFilterAgent.this.dataSource == null)
                return;
              if (paramBoolean)
              {
                ShopListNaviAdvanceFilterAgent.this.dataSource.filters = paramString;
                ShopListNaviAdvanceFilterAgent.this.dataSource.reset(true);
                ShopListNaviAdvanceFilterAgent.this.dataSource.reload(false);
              }
              if (!TextUtils.isEmpty(ShopListNaviAdvanceFilterAgent.this.dataSource.filters))
                ShopListNaviAdvanceFilterAgent.this.mFilterBar.setItemEffect("framefilter", true);
              while (true)
              {
                ShopListNaviAdvanceFilterAgent.this.mFilterBar.getItem(4).dismiss();
                return;
                ShopListNaviAdvanceFilterAgent.this.mFilterBar.setItemEffect("framefilter", false);
              }
            }
          });
        }
      }
      label505: i = 0;
      label507: if (i >= this.mFilterBar.getChildCount())
        break label689;
      if ((this.mFilterBar.getItem(i) != null) && ((this.mFilterBar.getItem(i) instanceof SearchTwinListFilterDialog)))
      {
        localObject1 = (SearchTwinListFilterDialog)this.mFilterBar.getItem(i);
        if (TextUtils.isEmpty(this.dataSource.suggestKeyword()))
          break label683;
      }
    }
    label683: for (boolean bool = true; ; bool = false)
    {
      ((SearchTwinListFilterDialog)localObject1).setDialogHeightAdaptive(bool);
      i += 1;
      break label507;
      localObject1 = this.dataSource.curCategory();
      break;
      label595: localObject1 = this.dataSource.curSort();
      break label292;
      label606: i = 0;
      break label415;
      label611: j = R.id.list_viewstub;
      break label438;
      label619: localObject1 = (ShopFilterView)((FilterDialog)localObject1).findViewById(R.id.shoplist_filter);
      ((ShopFilterView)localObject1).setClickable(true);
      ((ShopFilterView)localObject1).setPriceLow(this.dataSource.minPrice());
      ((ShopFilterView)localObject1).setPriceHigh(this.dataSource.maxPrice());
      ((ShopFilterView)localObject1).setListFilter(this.dataSource.filterSelectNavs(), this.dataSource.curSelectNav());
      ((ShopFilterView)localObject1).setFilterListener(this);
      break label505;
    }
    label689: updateNavs(this.isFloor);
  }

  private boolean updateRegionDialog()
  {
    Object localObject1 = this.mFilterBar.getItem(1);
    if ((this.dataSource.floorNavList != null) || (this.isFloor))
    {
      localObject2 = localObject1;
      if (!(localObject1 instanceof ListFilterDialog))
      {
        localObject2 = new ListFilterDialog(getActivity(), "floor_right");
        ((FilterDialog)localObject2).setOnFilterListener(this);
        this.mFilterBar.addItem(1, "floor", (FilterDialog)localObject2);
      }
      ((ListFilterDialog)localObject2).setItems(this.dataSource.floorNavList);
      ((ListFilterDialog)localObject2).setSelectedItem(this.dataSource.curFloor);
      return true;
    }
    if ("globalshoplist".equals(this.dataSource.targetPage))
    {
      localObject2 = localObject1;
      if (!(localObject1 instanceof ListFilterDialog))
      {
        localObject2 = new ListFilterDialog(getActivity(), "region_right");
        ((FilterDialog)localObject2).setOnFilterListener(this);
        this.mFilterBar.addItem(1, "region", (FilterDialog)localObject2);
      }
      ((ListFilterDialog)localObject2).setItems(this.dataSource.filterRegions());
      localObject2 = (ListFilterDialog)localObject2;
      if (this.dataSource.curRegion() != null)
        localObject1 = this.dataSource.curRegion();
      while (true)
      {
        ((ListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
        return false;
        if (this.dataSource.filterRegions() != null)
        {
          localObject1 = this.dataSource.filterRegions()[0];
          continue;
        }
        localObject1 = ShopListConst.ALL_REGION;
      }
    }
    if ((this.dataSource.regionNavTree != null) && (this.dataSource.metroNavTree != null))
    {
      if (this.dataSource.isMetro())
      {
        localObject2 = localObject1;
        if (!(localObject1 instanceof SearchTwinListFilterDialogWithRegionAndSubway))
        {
          localObject2 = new SearchTwinListFilterDialogWithRegionAndSubway(getActivity(), "subway", this.dataSource);
          ((SearchTwinListFilterDialog)localObject2).setHasAll(false);
          ((FilterDialog)localObject2).setOnFilterListener(this);
          this.mFilterBar.addItem(1, "region", (FilterDialog)localObject2);
        }
        ((SearchTwinListFilterDialogWithRegionAndSubway)localObject2).setNavTree(this.dataSource.metroNavTree);
        ((SearchTwinListFilterDialogWithRegionAndSubway)localObject2).onSubwayClick();
        localObject2 = (SearchTwinListFilterDialog)localObject2;
        if (this.dataSource.curMetro() == null);
        for (localObject1 = ShopListConst.ALL_REGION; ; localObject1 = this.dataSource.curMetro())
        {
          ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
          return false;
        }
      }
      localObject2 = localObject1;
      if (!(localObject1 instanceof SearchTwinListFilterDialogWithRegionAndSubway))
      {
        localObject2 = new SearchTwinListFilterDialogWithRegionAndSubway(getActivity(), "distance", this.dataSource);
        ((SearchTwinListFilterDialog)localObject2).setHasAll(false);
        ((FilterDialog)localObject2).setOnFilterListener(this);
        this.mFilterBar.addItem(1, "region", (FilterDialog)localObject2);
      }
      ((SearchTwinListFilterDialogWithRegionAndSubway)localObject2).setNavTree(this.dataSource.regionNavTree);
      ((SearchTwinListFilterDialogWithRegionAndSubway)localObject2).onRegionClick();
      localObject2 = (SearchTwinListFilterDialog)localObject2;
      if (this.dataSource.curRegion() == null);
      for (localObject1 = ShopListConst.ALL_REGION; ; localObject1 = this.dataSource.curRegion())
      {
        ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
        break;
      }
    }
    Object localObject2 = localObject1;
    if (!(localObject1 instanceof SearchTwinListFilterDialog))
    {
      localObject2 = new SearchTwinListFilterDialog(getActivity(), "distance_right");
      ((SearchTwinListFilterDialog)localObject2).setHasAll(false);
      ((FilterDialog)localObject2).setOnFilterListener(this);
      this.mFilterBar.addItem(1, "region", (FilterDialog)localObject2);
    }
    ((SearchTwinListFilterDialog)localObject2).setNavTree(this.dataSource.regionNavTree);
    localObject2 = (SearchTwinListFilterDialog)localObject2;
    if (this.dataSource.curRegion() == null);
    for (localObject1 = ShopListConst.ALL_REGION; ; localObject1 = this.dataSource.curRegion())
    {
      ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      return false;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getDataSource() instanceof NewShopListDataSource))
    {
      this.dataSource = ((NewShopListDataSource)getDataSource());
      if (this.mFilterBar == null)
      {
        paramBundle = inflater().inflate(R.layout.filter_layout_advance, getParentView(), false);
        this.mFilterBar = ((AdvanceFilterBar)paramBundle.findViewById(R.id.filterBar));
        addCell("020Navi", paramBundle);
      }
      updateFilterItems();
    }
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    if ((!(paramObject instanceof DPObject)) || (this.dataSource == null));
    while (true)
    {
      return;
      if ("region".equals(paramFilterDialog.getTag()))
      {
        if (!(paramFilterDialog instanceof SearchTwinListFilterDialogWithRegionAndSubway))
          break label239;
        if (((SearchTwinListFilterDialogWithRegionAndSubway)paramFilterDialog).currentTadId() != 0)
          break label174;
        if (this.dataSource.filterRegions() == null)
          continue;
        if (this.dataSource.isMetro())
          this.dataSource.setCurRegion(this.dataSource.curMetro());
        if (!this.dataSource.setCurRegion((DPObject)paramObject))
        {
          paramFilterDialog.dismiss();
          return;
        }
        this.dataSource.setIsMetro(false);
      }
      label174: label239: 
      do
      {
        while (true)
        {
          if (!"floor".equals(paramFilterDialog.getTag()))
            break label279;
          if (this.dataSource.floorNavList == null)
            break;
          if ((this.dataSource.curFloor == null) || (!this.dataSource.curFloor.getString("ID").equals(((DPObject)paramObject).getString("ID"))))
            break label268;
          paramFilterDialog.dismiss();
          return;
          if (this.dataSource.filterMetros() == null)
            break;
          if (!this.dataSource.isMetro())
            this.dataSource.setCurMetro(this.dataSource.curRegion());
          if (!this.dataSource.setCurMetro((DPObject)paramObject))
          {
            paramFilterDialog.dismiss();
            return;
          }
          this.dataSource.setIsMetro(true);
        }
        if (this.dataSource.filterRegions() == null)
          break;
      }
      while (this.dataSource.setCurRegion((DPObject)paramObject));
      paramFilterDialog.dismiss();
      return;
      label268: this.dataSource.curFloor = ((DPObject)paramObject);
      label279: if ("category".equals(paramFilterDialog.getTag()))
      {
        String str = ((DPObject)paramObject).getString("Schema");
        if (!TextUtils.isEmpty(str))
        {
          getActivity().startActivity(str);
          paramFilterDialog.dismiss();
          return;
        }
        if (this.dataSource.filterCategories() == null)
          continue;
        if (!this.dataSource.setCurCategory((DPObject)paramObject))
        {
          paramFilterDialog.dismiss();
          return;
        }
      }
      if (!"rank".equals(paramFilterDialog.getTag()))
        break;
      if (this.dataSource.filterSorts() == null)
        continue;
      if (!this.dataSource.setCurSort((DPObject)paramObject))
      {
        paramFilterDialog.dismiss();
        return;
      }
      if (ShopListUtils.checkFilterable(getActivity(), (DPObject)paramObject))
        break;
      paramFilterDialog.dismiss();
      return;
    }
    paramObject = ShopListUtils.optimizeFilterTitle(((DPObject)paramObject).getString("Name"));
    this.mFilterBar.setItemTitle(paramFilterDialog.getTag(), paramObject);
    paramFilterDialog.dismiss();
    this.dataSource.reset(true);
    this.dataSource.reload(false);
  }

  public void onfilterList(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if (this.dataSource == null)
      return;
    if ((this.dataSource.setCurSelectNav(paramDPObject)) || (paramInt1 != this.dataSource.minPrice()) || (paramInt2 != this.dataSource.maxPrice()))
    {
      this.dataSource.setMinPrice(paramInt1);
      this.dataSource.setMaxPrice(paramInt2);
      this.dataSource.reset(true);
      this.dataSource.reload(false);
    }
    if (!TextUtils.isEmpty(this.dataSource.filters))
      this.mFilterBar.setItemEffect("framefilter", true);
    while (true)
    {
      this.mFilterBar.getItem(4).dismiss();
      return;
      this.mFilterBar.setItemEffect("framefilter", false);
    }
  }

  public void updateNavs(boolean paramBoolean)
  {
    if (this.dataSource == null)
      return;
    Object localObject3 = this.dataSource.curRegion();
    Object localObject1 = this.dataSource.curFloor;
    Object localObject4 = this.dataSource.curMetro();
    Object localObject2 = this.dataSource.curCategory();
    DPObject localDPObject = this.dataSource.curSort();
    String str = "全部楼层";
    if (localObject1 != null)
      str = ((DPObject)localObject1).getString("Name");
    localObject1 = ShopListUtils.getDefaultRegionName(this.dataSource.targetPage);
    if ((localObject3 == null) || (TextUtils.isEmpty(((DPObject)localObject3).getString("Name"))))
    {
      localObject3 = ShopListUtils.optimizeFilterTitle((String)localObject1);
      localObject1 = "全部商区";
      if (localObject4 != null)
        localObject1 = ((DPObject)localObject4).getString("Name");
      if ((localObject2 != null) && (!TextUtils.isEmpty(((DPObject)localObject2).getString("Name"))))
        break label269;
      localObject2 = "全部分类";
      label146: localObject4 = ShopListUtils.optimizeFilterTitle((String)localObject2);
      if ((localDPObject != null) && (!TextUtils.isEmpty(localDPObject.getString("Name"))))
        break label282;
      localObject2 = "智能排序";
      label177: localObject2 = ShopListUtils.optimizeFilterTitle((String)localObject2);
      if (!paramBoolean)
        break label295;
      this.mFilterBar.setItemTitle("floor", str);
    }
    while (true)
    {
      this.mFilterBar.setItemTitle("category", (String)localObject4);
      this.mFilterBar.setItemTitle("rank", (String)localObject2);
      this.mFilterBar.setItemTitle("framefilter", "筛选");
      if (TextUtils.isEmpty(this.dataSource.filters))
        break label334;
      this.mFilterBar.setItemEffect("framefilter", true);
      return;
      localObject1 = ((DPObject)localObject3).getString("Name");
      break;
      label269: localObject2 = ((DPObject)localObject2).getString("Name");
      break label146;
      label282: localObject2 = localDPObject.getString("Name");
      break label177;
      label295: if (this.dataSource.isMetro())
      {
        this.mFilterBar.setItemTitle("region", (String)localObject1);
        continue;
      }
      this.mFilterBar.setItemTitle("region", (String)localObject3);
    }
    label334: this.mFilterBar.setItemEffect("framefilter", false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent
 * JD-Core Version:    0.6.0
 */