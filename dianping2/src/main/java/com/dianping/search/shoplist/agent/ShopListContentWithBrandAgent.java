package com.dianping.search.shoplist.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.data.model.NavTree;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.util.ShopListUtils;
import com.dianping.search.view.ShopFilterView;
import com.dianping.search.view.ShopFilterView.FilterListener;
import com.dianping.search.widget.AdvanceFilterBar;
import com.dianping.search.widget.SearchTwinListFilterDialog;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopListContentWithBrandAgent extends NShopListContentAgent
  implements FilterDialog.OnFilterListener, ShopFilterView.FilterListener
{
  private static final String CELL_SHOP_LIST = "051ShopList";
  private boolean brandBoardShown = true;
  private NovaLinearLayout brandContent;
  private TextView brandDesc;
  private NovaRelativeLayout brandInfo;
  private LinearLayout brandLayout;
  private NetworkImageView brandLogo;
  private JSONObject brandObject;
  private TextView brandTitle;
  private RelativeLayout contentView;
  NewShopListDataSource dataSource;
  private AdvanceFilterBar floatFilterBar;
  private GAUserInfo gaUserInfo;
  private boolean headerRemove = false;
  private View.OnClickListener onBoardItemClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView.getTag() != null)
        ShopListContentWithBrandAgent.this.startActivity(paramView.getTag().toString());
    }
  };
  private AdvanceFilterBar pinHeaderFilterBar;
  private String queryId;

  public ShopListContentWithBrandAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setBrandContent()
  {
    if ((this.brandInfo == null) || (this.brandContent == null) || (this.brandObject == null));
    int j;
    Object localObject1;
    int i;
    int k;
    Object localObject2;
    while (true)
    {
      return;
      this.brandContent.removeAllViews();
      if (this.brandLogo == null)
        this.brandLogo = ((NetworkImageView)this.brandLayout.findViewById(R.id.logo));
      if (this.brandTitle == null)
        this.brandTitle = ((TextView)this.brandLayout.findViewById(R.id.name));
      if (this.brandDesc == null)
        this.brandDesc = ((TextView)this.brandLayout.findViewById(R.id.desc));
      this.brandLogo.setImage(this.brandObject.optString("BrandLogo"));
      this.brandTitle.setText(this.brandObject.optString("BrandTitle"));
      this.brandDesc.setText(this.brandObject.optString("BrandDesc"));
      this.brandInfo.setTag(this.brandObject.optString("BrandUrl"));
      this.brandInfo.setOnClickListener(this.onBoardItemClickListener);
      j = this.brandObject.optInt("BoardType");
      this.brandInfo.setGAString("brandzone_style" + j + "_title");
      this.brandInfo.gaUserInfo.query_id = this.queryId;
      try
      {
        JSONObject localJSONObject = new JSONObject(this.brandObject.optString("BoardContent"));
        localObject1 = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject1);
        i = ((DisplayMetrics)localObject1).widthPixels;
        k = ViewUtils.dip2px(getContext(), 7.0F);
        switch (j)
        {
        default:
        case 0:
          while (this.headerRemove)
          {
            this.shopListView.addHeaderView(this.brandLayout);
            this.shopListView.addHeaderView(this.floatFilterBar);
            return;
            this.brandContent.setOrientation(0);
            localObject1 = (NovaLinearLayout)inflater().inflate(R.layout.shoplist_brand_image, null);
            m = ViewUtils.dip2px(getContext(), 3.0F);
            localObject2 = new LinearLayout.LayoutParams(0, (i - k * 2) * 13 / 48, 3.0F);
            ((LinearLayout.LayoutParams)localObject2).setMargins(m, 0, m, 0);
            ((NovaLinearLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
            ((NetworkImageView)((NovaLinearLayout)localObject1).findViewById(R.id.image)).setImage(localJSONObject.optString("PicUrl"));
            ((NovaLinearLayout)localObject1).setTag(localJSONObject.optString("PicClickUrl"));
            ((NovaLinearLayout)localObject1).setOnClickListener(this.onBoardItemClickListener);
            ((NovaLinearLayout)localObject1).setGAString("brandzone_style" + j + "_activity");
            ((NovaLinearLayout)localObject1).gaUserInfo.query_id = this.queryId;
            this.brandContent.addView((View)localObject1);
          }
        case 1:
        case 2:
        }
      }
      catch (JSONException localJSONException)
      {
        while (true)
          localJSONException.printStackTrace();
        this.brandContent.setOrientation(0);
        int m = ViewUtils.dip2px(getContext(), 3.0F);
        localObject1 = new LinearLayout.LayoutParams(0, ((i - k * 2) / 3 - m * 2) * 3 / 4, 1.0F);
        ((LinearLayout.LayoutParams)localObject1).setMargins(m, 0, m, 0);
        i = 0;
        while (i < 3)
        {
          localObject2 = "Pic" + i + "Url";
          String str1 = "Pic" + i + "ClickUrl";
          String str2 = "Pic" + i + "Name";
          NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)inflater().inflate(R.layout.shoplist_brand_image, null);
          NetworkImageView localNetworkImageView = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.image);
          TextView localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.intro);
          localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject1);
          localNetworkImageView.setImage(localJSONException.optString((String)localObject2));
          localTextView.setText(localJSONException.optString(str2));
          localTextView.setVisibility(0);
          localNovaLinearLayout.setTag(localJSONException.optString(str1));
          localNovaLinearLayout.setOnClickListener(this.onBoardItemClickListener);
          localNovaLinearLayout.setGAString("brandzone_style" + j + "_activity");
          localNovaLinearLayout.gaUserInfo.index = Integer.valueOf(i);
          localNovaLinearLayout.gaUserInfo.query_id = this.queryId;
          this.brandContent.addView(localNovaLinearLayout);
          i += 1;
        }
        this.brandContent.setOrientation(1);
        k = ViewUtils.dip2px(getContext(), 3.0F);
        i = 0;
      }
    }
    label865: if (i < 2)
    {
      localObject1 = (NovaTextView)inflater().inflate(R.layout.shoplist_brand_text, null);
      if (i >= 1)
        break label1083;
      localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
      ((LinearLayout.LayoutParams)localObject2).setMargins(k, 0, k, ViewUtils.dip2px(getContext(), 10.0F));
      ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
    }
    while (true)
    {
      ((NovaTextView)localObject1).setText(localJSONException.optString("Board" + i + "Text"));
      ((NovaTextView)localObject1).setTag(localJSONException.optString("Board" + i + "Url"));
      ((NovaTextView)localObject1).setOnClickListener(this.onBoardItemClickListener);
      ((NovaTextView)localObject1).setGAString("brandzone_style" + j + "_activity");
      ((NovaTextView)localObject1).gaUserInfo.index = Integer.valueOf(i);
      ((NovaTextView)localObject1).gaUserInfo.query_id = this.queryId;
      this.brandContent.addView((View)localObject1);
      i += 1;
      break label865;
      break;
      label1083: localObject2 = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 36.0F));
      ((LinearLayout.LayoutParams)localObject2).setMargins(k, 0, k, 0);
      ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
    }
  }

  private void updateFilterItems()
  {
    if (getDataSource() == null)
      return;
    this.dataSource = ((NewShopListDataSource)getDataSource());
    if (this.dataSource.regionNavTree == null)
      this.dataSource.regionNavTree = NavTree.from(ShopListUtils.genDefaultFilter(2));
    Object localObject2 = this.floatFilterBar.getItem(1);
    Object localObject1 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = new SearchTwinListFilterDialog(getActivity(), "distance_right");
      ((SearchTwinListFilterDialog)localObject1).setHasAll(false);
      ((FilterDialog)localObject1).setOnFilterListener(this);
      this.floatFilterBar.addItem(1, "region", (FilterDialog)localObject1);
      this.pinHeaderFilterBar.addItem(1, "region", (FilterDialog)localObject1);
    }
    ((SearchTwinListFilterDialog)localObject1).setNavTree(this.dataSource.regionNavTree);
    localObject2 = (SearchTwinListFilterDialog)localObject1;
    if (this.dataSource.curRegion() == null)
    {
      localObject1 = ShopListConst.ALL_REGION;
      ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      if (this.dataSource.categoryNavTree == null)
        this.dataSource.categoryNavTree = NavTree.from(ShopListUtils.genDefaultFilter(1));
      localObject2 = this.floatFilterBar.getItem(2);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new SearchTwinListFilterDialog(getActivity(), "category_right");
        ((SearchTwinListFilterDialog)localObject1).setHeaderItem(ShopListConst.ALL_CATEGORY);
        ((SearchTwinListFilterDialog)localObject1).setHasAll(false);
        ((FilterDialog)localObject1).setOnFilterListener(this);
        this.floatFilterBar.addItem(2, "category", (FilterDialog)localObject1);
        this.pinHeaderFilterBar.addItem(2, "category", (FilterDialog)localObject1);
      }
      ((SearchTwinListFilterDialog)localObject1).setNavTree(this.dataSource.categoryNavTree);
      localObject2 = (SearchTwinListFilterDialog)localObject1;
      if (this.dataSource.curCategory() != null)
        break label565;
      localObject1 = ShopListConst.ALL_CATEGORY;
      label282: ((SearchTwinListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      if (this.dataSource.filterSorts() == null)
        this.dataSource.setFilterSorts(ShopListUtils.genDefaultFilter(3));
      localObject2 = this.floatFilterBar.getItem(3);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new ListFilterDialog(getActivity(), "sort_select");
        ((FilterDialog)localObject1).setOnFilterListener(this);
        this.floatFilterBar.addItem(3, "rank", (FilterDialog)localObject1);
        this.pinHeaderFilterBar.addItem(3, "rank", (FilterDialog)localObject1);
      }
      ((ListFilterDialog)localObject1).setItems(this.dataSource.filterSorts());
      localObject2 = (ListFilterDialog)localObject1;
      if (this.dataSource.curSort() != null)
        break label576;
    }
    label565: label576: for (localObject1 = ShopListConst.DEFAULT_SORT; ; localObject1 = this.dataSource.curSort())
    {
      ((ListFilterDialog)localObject2).setSelectedItem((DPObject)localObject1);
      if (this.dataSource.needAdvFilter)
      {
        localObject2 = this.floatFilterBar.getItem(4);
        localObject1 = localObject2;
        if (localObject2 == null)
        {
          localObject1 = new FilterDialog(getActivity());
          ((FilterDialog)localObject1).setFilterView((ShopFilterView)((FilterDialog)localObject1).getLayoutInflater().inflate(R.layout.shop_list_filter, ((FilterDialog)localObject1).getFilterViewParent(), false));
          this.floatFilterBar.addItem(4, "framefilter", (FilterDialog)localObject1);
          this.pinHeaderFilterBar.addItem(4, "framefilter", (FilterDialog)localObject1);
        }
        localObject1 = (ShopFilterView)((FilterDialog)localObject1).findViewById(R.id.shoplist_filter);
        ((ShopFilterView)localObject1).setClickable(true);
        ((ShopFilterView)localObject1).setPriceLow(this.dataSource.minPrice());
        ((ShopFilterView)localObject1).setPriceHigh(this.dataSource.maxPrice());
        ((ShopFilterView)localObject1).setListFilter(this.dataSource.filterSelectNavs(), this.dataSource.curSelectNav());
        ((ShopFilterView)localObject1).setFilterListener(this);
      }
      updateNavs();
      return;
      localObject1 = this.dataSource.curRegion();
      break;
      localObject1 = this.dataSource.curCategory();
      break label282;
    }
  }

  protected void addCell()
  {
    addCell("051ShopList", this.contentView);
  }

  protected void inflateViews()
  {
    this.contentView = ((RelativeLayout)inflater().inflate(R.layout.shop_list_view_with_float_filter, (ViewGroup)getFragment().contentView(), false));
    this.contentView.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
    this.shopListView = ((PullToRefreshListView)this.contentView.findViewById(R.id.shoplist));
    this.shopListView.setPullRefreshEnable(1);
    this.shopListView.setPullLoadEnable(0);
    this.shopListView.setSelector(R.drawable.home_listview_bg);
    this.pinHeaderFilterBar = ((AdvanceFilterBar)this.contentView.findViewById(R.id.filter_bar));
    this.floatFilterBar = ((AdvanceFilterBar)inflater().inflate(R.layout.filter_layout_advance, getParentView(), false));
    this.brandLayout = ((LinearLayout)inflater().inflate(R.layout.shoplist_brand_layout, getParentView(), false));
    this.brandLayout.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 0.0F)));
    this.brandInfo = ((NovaRelativeLayout)this.brandLayout.findViewById(R.id.brand_info));
    this.brandContent = ((NovaLinearLayout)this.brandLayout.findViewById(R.id.brand_content));
    this.floatFilterBar.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 45.0F)));
    updateFilterItems();
    this.shopListView.addHeaderView(this.brandLayout);
    this.shopListView.addHeaderView(this.floatFilterBar);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    this.dataSource = ((NewShopListDataSource)getDataSource());
    if ((this.dataSource == null) || (this.dataSource.targetInfo == null) || (this.dataSource.targetType != 4))
    {
      super.onAgentChanged(paramBundle);
      if (this.brandLayout != null)
        this.shopListView.removeHeaderView(this.brandLayout);
      if (this.floatFilterBar != null)
        this.shopListView.removeHeaderView(this.floatFilterBar);
      this.headerRemove = true;
      return;
    }
    try
    {
      this.brandObject = new JSONObject(this.dataSource.targetInfo);
      if ((this.dataSource.startIndex() == 0) && (this.dataSource.status() == 2))
      {
        this.queryId = this.dataSource.queryId();
        getActivity().gaExtra.query_id = this.queryId;
        if (this.gaUserInfo == null)
          this.gaUserInfo = new GAUserInfo();
        this.gaUserInfo.query_id = this.queryId;
        this.gaUserInfo.keyword = this.dataSource.suggestKeyword();
        this.gaUserInfo.index = Integer.valueOf(this.brandObject.optInt("BoardType"));
        GAHelper.instance().contextStatisticsEvent(getContext(), "brandzone_display", this.gaUserInfo, "view");
      }
      super.onAgentChanged(paramBundle);
      return;
    }
    catch (JSONException paramBundle)
    {
    }
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    if (!(paramObject instanceof DPObject));
    while (true)
    {
      return;
      if ("region".equals(paramFilterDialog.getTag()))
      {
        if (this.dataSource.filterRegions() == null)
          continue;
        if (!this.dataSource.setCurRegion((DPObject)paramObject))
        {
          paramFilterDialog.dismiss();
          return;
        }
      }
      if ("category".equals(paramFilterDialog.getTag()))
      {
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
    this.floatFilterBar.setItemTitle(paramFilterDialog.getTag(), paramObject);
    this.pinHeaderFilterBar.setItemTitle(paramFilterDialog.getTag(), paramObject);
    paramFilterDialog.dismiss();
    this.dataSource.reset(true);
    this.dataSource.reload(false);
  }

  protected void onListScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = this.shopListView.getHeaderViewsCount();
    if (i > 1)
    {
      if (paramInt1 >= i - 1)
        break label47;
      this.pinHeaderFilterBar.setVisibility(8);
    }
    for (this.brandBoardShown = true; ; this.brandBoardShown = false)
    {
      super.onListScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
      return;
      label47: this.pinHeaderFilterBar.setVisibility(0);
    }
  }

  public void onResume()
  {
    super.onResume();
    if ((this.brandBoardShown) && (this.dataSource != null) && (this.dataSource.status() == 2) && (this.gaUserInfo != null))
      GAHelper.instance().contextStatisticsEvent(getContext(), "brandzone_display", this.gaUserInfo, "view");
  }

  public void onfilterList(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if ((this.dataSource.setCurSelectNav(paramDPObject)) || (paramInt1 != this.dataSource.minPrice()) || (paramInt2 != this.dataSource.maxPrice()))
    {
      this.dataSource.setMinPrice(paramInt1);
      this.dataSource.setMaxPrice(paramInt2);
      this.dataSource.reset(true);
      this.dataSource.reload(false);
    }
    this.floatFilterBar.getItem(4).dismiss();
    this.pinHeaderFilterBar.getItem(4).dismiss();
  }

  public void updateListView()
  {
    setBrandContent();
    super.updateListView();
    updateFilterItems();
  }

  public void updateNavs()
  {
    Object localObject1 = this.dataSource.curRegion();
    Object localObject2 = this.dataSource.curCategory();
    DPObject localDPObject = this.dataSource.curSort();
    String str;
    if ((localObject1 == null) || (TextUtils.isEmpty(((DPObject)localObject1).getString("Name"))))
    {
      localObject1 = "全部商区";
      str = ShopListUtils.optimizeFilterTitle((String)localObject1);
      if ((localObject2 != null) && (!TextUtils.isEmpty(((DPObject)localObject2).getString("Name"))))
        break label199;
      localObject1 = "全部分类";
      label74: localObject2 = ShopListUtils.optimizeFilterTitle((String)localObject1);
      if (localDPObject != null)
        break label211;
    }
    label199: label211: for (localObject1 = "智能排序"; ; localObject1 = localDPObject.getString("Name"))
    {
      localObject1 = ShopListUtils.optimizeFilterTitle((String)localObject1);
      this.floatFilterBar.setItemTitle("region", str);
      this.floatFilterBar.setItemTitle("category", (String)localObject2);
      this.floatFilterBar.setItemTitle("rank", (String)localObject1);
      this.floatFilterBar.setItemTitle("framefilter", "筛选");
      this.pinHeaderFilterBar.setItemTitle("region", str);
      this.pinHeaderFilterBar.setItemTitle("category", (String)localObject2);
      this.pinHeaderFilterBar.setItemTitle("rank", (String)localObject1);
      this.pinHeaderFilterBar.setItemTitle("framefilter", "筛选");
      return;
      localObject1 = ((DPObject)localObject1).getString("Name");
      break;
      localObject1 = ((DPObject)localObject2).getString("Name");
      break label74;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListContentWithBrandAgent
 * JD-Core Version:    0.6.0
 */