package com.dianping.tuan.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.tuan.dialog.filter.NaviFilterDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog.ScreeningDialogListener;
import com.dianping.base.tuan.widget.TuanBannerView;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.ShopAndDealListItem.OnDealItemClickListener;
import com.dianping.base.widget.ShopAndDealListItem.OnShopItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.tuan.widget.TuanFilterBar;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class DealListFragmentWithFilter extends BaseTuanPtrListFragment
  implements TuanScreeningDialog.ScreeningDialogListener, FilterBar.OnItemClickListener, CityConfig.SwitchListener
{
  public static final DPObject ALL_CATEGORY;
  public static final DPObject ALL_REGION;
  public static final DPObject DEFAULT_SORT;
  protected static final DecimalFormat FMT = new DecimalFormat("#.00000");
  protected static final int HEADER_VIEW_TYPE_BANNER = 2;
  protected static final int HEADER_VIEW_TYPE_HOTEL = 1;
  protected static final int HEADER_VIEW_TYPE_NONE = 0;
  private static final int REQUEST_HOTEL_BOOKING = 1143;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd", Locale.getDefault());
  protected int accuracy;
  protected TuanBannerView bannerHeaderView;
  protected DPObject catagoryNavi = ALL_CATEGORY;
  protected int categoryId = -1;
  protected String channel;
  protected DPObject currentCategoryNavi = ALL_CATEGORY;
  protected DPObject currentRegion = ALL_REGION;
  protected DPObject currentSort = DEFAULT_SORT;
  protected DealListFragmentWithFilter.DealAdapter dealAdapter;
  protected View dealListEmptyView;
  protected FilterDialog dlg;
  protected DPObject[] dpNaviTags;
  protected String extraFilterStr;
  protected TuanFilterBar filterBar;
  final FilterDialog.OnFilterListener filterListener = new DealListFragmentWithFilter.1(this);
  protected boolean filterReset = true;
  protected String from_title;
  protected View headerView;
  protected boolean headerViewInited;
  protected int headerViewType;
  protected boolean isFirstPage = true;
  protected String keyword;
  protected double latitude;
  protected double longitude;
  protected TextView naviScreeningView;
  protected final ShopAndDealListItem.OnDealItemClickListener onDealItemClickListener = new DealListFragmentWithFilter.3(this);
  protected final ShopAndDealListItem.OnShopItemClickListener onShopClickListener = new DealListFragmentWithFilter.2(this);
  protected String queryId;
  protected int regionId = -1;
  protected DPObject regionNavi = ALL_REGION;
  protected String requestId;
  protected String sortId;
  protected DPObject sortNavi = DEFAULT_SORT;

  static
  {
    ALL_CATEGORY = new DPObject("Navi").edit().putString("ID", "0").putString("Name", "全部分类").putString("EnName", "0").putInt("Type", 1).generate();
    ALL_REGION = new DPObject("Navi").edit().putString("ID", "0").putString("Name", "全部商区").putString("EnName", "0").putInt("Type", 2).generate();
    DEFAULT_SORT = new DPObject("Navi").edit().putString("ID", "0").putString("Name", "智能排序").putString("EnName", "0").putInt("Type", 4).generate();
  }

  public static boolean hasTag(String[] paramArrayOfString, String paramString)
  {
    boolean bool2 = false;
    try
    {
      int j = paramArrayOfString.length;
      int i = 0;
      while (true)
      {
        boolean bool1 = bool2;
        if (i < j)
        {
          bool1 = paramArrayOfString[i].equals(paramString);
          if (bool1)
            bool1 = true;
        }
        else
        {
          return bool1;
        }
        i += 1;
      }
    }
    catch (java.lang.Exception paramArrayOfString)
    {
    }
    return false;
  }

  protected void addFilterItems()
  {
  }

  public boolean checkFilterable(DPObject paramDPObject)
  {
    if ((paramDPObject != null) && ("2".equals(paramDPObject.getString("ID"))) && (DPApplication.instance().locationService().location() == null))
    {
      Toast.makeText(getActivity(), "正在定位，此功能暂不可用", 0).show();
      return false;
    }
    return true;
  }

  protected DealListFragmentWithFilter.DealAdapter createDealAdapter()
  {
    return new DealListFragmentWithFilter.DealAdapter(this, getActivity());
  }

  protected ShopAndDealListItem.OnDealItemClickListener createOnDealItemClickListener()
  {
    return this.onDealItemClickListener;
  }

  protected ShopAndDealListItem.OnShopItemClickListener createOnShopClickListener()
  {
    return this.onShopClickListener;
  }

  public DPObject findSelectedNavi(DPObject paramDPObject)
  {
    if ((paramDPObject.getBoolean("Selected")) && (paramDPObject.getArray("Subs") != null) && (paramDPObject.getArray("Subs").length != 0))
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("Subs");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject.getBoolean("Selected"))
        {
          paramDPObject = localDPObject;
          if (localDPObject.getArray("Subs") != null)
          {
            paramDPObject = localDPObject;
            if (localDPObject.getArray("Subs").length != 0)
              paramDPObject = findSelectedNavi(localDPObject);
          }
          return paramDPObject;
        }
        i += 1;
      }
    }
    return paramDPObject;
  }

  public DPObject getCurrentCategory()
  {
    if (this.currentCategoryNavi == null)
      return ALL_CATEGORY;
    return this.currentCategoryNavi;
  }

  public DPObject getCurrentRegion()
  {
    if (this.currentRegion == null)
      return ALL_REGION;
    return this.currentRegion;
  }

  public DPObject getCurrentSort()
  {
    if (this.currentSort == null)
      return DEFAULT_SORT;
    return this.currentSort;
  }

  public DPObject getCurrentTopCategory()
  {
    Object localObject;
    if ((this.currentCategoryNavi == null) || (this.currentCategoryNavi == ALL_CATEGORY))
    {
      localObject = ALL_CATEGORY;
      return localObject;
    }
    DPObject[] arrayOfDPObject = this.catagoryNavi.getArray("Subs");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      int j = arrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label83;
        DPObject localDPObject = arrayOfDPObject[i];
        localObject = localDPObject;
        if (localDPObject.getBoolean("Selected"))
          break;
        i += 1;
      }
    }
    label83: return (DPObject)null;
  }

  public DealListFragmentWithFilter.DealAdapter getDealAdapter()
  {
    return this.dealAdapter;
  }

  public String getExtraFilterStr()
  {
    return this.extraFilterStr;
  }

  protected void hideHeaderView()
  {
    if (!this.headerViewInited)
      return;
    this.headerView.setVisibility(8);
  }

  protected void initHeaderView()
  {
    if (!isAdded());
    do
      return;
    while (this.headerViewInited);
    this.headerViewInited = true;
    this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.deal_list_header_view, this.listView, false);
    this.listView.setHeaderDividersEnabled(false);
    this.listView.addHeaderView(this.headerView, null, false);
    this.bannerHeaderView = ((TuanBannerView)this.headerView.findViewById(R.id.ad_header_view));
    this.bannerHeaderView.setBtnOnCloseListener(new DealListFragmentWithFilter.4(this));
    setHeadViewType(this.headerViewType);
  }

  protected boolean isListEmpty(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
      return true;
    while ((paramDPObject.getArray("List") == null) || (paramDPObject.getArray("List").length == 0));
    return false;
  }

  protected boolean isListEmpty(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null);
    do
      return true;
    while (paramArrayOfDPObject.length == 0);
    return false;
  }

  public DealListFragmentWithFilter.ListType listType()
  {
    return DealListFragmentWithFilter.ListType.COMMON;
  }

  public abstract MApiRequest loadDealList(int paramInt);

  public boolean locationCare()
  {
    return true;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setTitle("团购列表");
    if (this.filterBar.getChildCount() == 0)
      this.filterBar.setVisibility(8);
    this.from_title = getStringParam("banner_title");
    if (TextUtils.isEmpty(this.from_title))
      this.from_title = getStringParam("category_title");
    if (TextUtils.isEmpty(this.from_title))
      this.from_title = getStringParam("operation_title");
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity1.id() == paramCity2.id());
    do
      return;
    while ((!isAdded()) || (this.dealAdapter == null));
    this.dealAdapter.reset();
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("region".equals(paramObject))
      if ((this.regionNavi != null) && (this.regionNavi.getArray("Subs") != null) && (this.regionNavi.getArray("Subs").length != 0));
    do
      while (true)
      {
        return;
        this.dlg = new NaviFilterDialog(getActivity(), this.regionNavi, this.filterListener);
        this.dlg.setTag(paramObject);
        this.dlg.show(paramView);
        return;
        if ("category".equals(paramObject))
        {
          if ((this.catagoryNavi == null) || (this.catagoryNavi.getArray("Subs") == null) || (this.catagoryNavi.getArray("Subs").length == 0))
            continue;
          this.dlg = new NaviFilterDialog(getActivity(), this.catagoryNavi, this.filterListener);
          this.dlg.setTag(paramObject);
          this.dlg.show(paramView);
          return;
        }
        if (!"rank".equals(paramObject))
          break;
        if ((this.sortNavi == null) || (this.sortNavi.getArray("Subs") == null) || (this.sortNavi.getArray("Subs").length == 0))
          continue;
        this.dlg = new NaviFilterDialog(getActivity(), this.sortNavi, this.filterListener);
        this.dlg.setTag(paramObject);
        this.dlg.show(paramView);
        return;
      }
    while ((!"screening".equals(paramObject)) || (this.dpNaviTags == null) || (Arrays.asList(this.dpNaviTags).size() <= 0));
    this.dlg = new TuanScreeningDialog(getActivity(), this.dpNaviTags, this.extraFilterStr);
    this.dlg.setTag(paramObject);
    ((TuanScreeningDialog)this.dlg).setListener(this);
    this.dlg.show(paramView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    cityConfig().addListener(this);
    if (paramBundle != null)
    {
      this.currentRegion = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_currentregion"));
      this.currentCategoryNavi = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_currentcategory"));
      this.currentSort = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_currentfilter"));
      this.isFirstPage = paramBundle.getBoolean("DealListFragmentWithFilter_isfirstpage");
      if (paramBundle.getParcelableArrayList("DealListFragmentWithFilter_regionnavs") != null)
        this.regionNavi = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_regionnavs"));
      if (paramBundle.getParcelable("DealListFragmentWithFilter_categorynavs") != null)
        this.catagoryNavi = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_categorynavs"));
      if (paramBundle.getParcelableArrayList("DealListFragmentWithFilter_filternavs") != null)
        this.sortNavi = ((DPObject)paramBundle.getParcelable("DealListFragmentWithFilter_filternavs"));
      this.extraFilterStr = paramBundle.getString("DealListFragmentWithFilter_extrafilterstr");
      this.filterReset = paramBundle.getBoolean("DealListFragmentWithFilter_filterreset");
      if (paramBundle.getParcelableArrayList("DealListFragmentWithFilter_dpnavitags") != null)
        this.dpNaviTags = ((DPObject[])paramBundle.getParcelableArrayList("DealListFragmentWithFilter_dpnavitags").toArray(new DPObject[0]));
      this.headerViewType = paramBundle.getInt("DealListFragmentWithFilter_headerviewtype");
    }
    if (location() != null)
    {
      this.latitude = location().latitude();
      this.longitude = location().longitude();
      this.accuracy = location().accuracy();
    }
    this.dealAdapter = createDealAdapter();
    if ((paramBundle != null) && (this.dealAdapter != null))
      this.dealAdapter.onRestoreInstanceState(paramBundle);
  }

  public void onDestroy()
  {
    this.dealAdapter.cancelLoad();
    cityConfig().removeListener(this);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramAdapterView == this.listView)
    {
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      if (isDPObjectof(paramAdapterView, "Deal"))
      {
        paramAdapterView = (DPObject)paramAdapterView;
        if (paramAdapterView.getInt("DealType") != 5)
          break label90;
        paramAdapterView = paramAdapterView.getString("Link");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(paramAdapterView))));
      }
    }
    return;
    label90: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
    paramView.putExtra("deal", paramAdapterView);
    startActivity(paramView);
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (location() != null)
    {
      this.latitude = location().latitude();
      this.longitude = location().longitude();
      this.accuracy = location().accuracy();
    }
  }

  protected void onPullToRefresh()
  {
    this.dealAdapter.pullToReset(true);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.regionNavi != null)
      paramBundle.putParcelable("DealListFragmentWithFilter_regionnavs", this.regionNavi);
    if (this.catagoryNavi != null)
      paramBundle.putParcelable("DealListFragmentWithFilter_categorynavs", this.catagoryNavi);
    ArrayList localArrayList;
    if (this.sortNavi != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(new DPObject[] { this.sortNavi }));
      paramBundle.putParcelableArrayList("DealListFragmentWithFilter_filternavs", localArrayList);
    }
    if (this.dpNaviTags != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.dpNaviTags));
      paramBundle.putParcelableArrayList("DealListFragmentWithFilter_dpnavitags", localArrayList);
    }
    paramBundle.putBoolean("DealListFragmentWithFilter_isfirstpage", this.isFirstPage);
    paramBundle.putParcelable("DealListFragmentWithFilter_currentregion", this.currentRegion);
    paramBundle.putParcelable("DealListFragmentWithFilter_currentcategory", this.currentCategoryNavi);
    paramBundle.putParcelable("DealListFragmentWithFilter_currentfilter", this.currentSort);
    paramBundle.putString("DealListFragmentWithFilter_extrafilterstr", this.extraFilterStr);
    paramBundle.putBoolean("DealListFragmentWithFilter_filterreset", this.filterReset);
    paramBundle.putInt("DealListFragmentWithFilter_headerviewtype", this.headerViewType);
    if (this.dealAdapter != null)
      this.dealAdapter.onSaveInstanceState(paramBundle);
  }

  protected View onSetContentView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return paramLayoutInflater.inflate(R.layout.deal_list_with_filter, paramViewGroup, false);
  }

  public void onSubmit(String paramString)
  {
    String str = paramString;
    if (TextUtils.isEmpty(paramString))
      str = "";
    if (str.equals(this.extraFilterStr));
    do
    {
      return;
      this.extraFilterStr = str;
      toggleNaviButtonState();
    }
    while (this.dealAdapter == null);
    this.dealAdapter.reset();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    initHeaderView();
    this.dealListEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.deal_list_empty_view, this.emptyView, false);
    setEmptyView(this.dealListEmptyView);
    this.filterBar = ((TuanFilterBar)paramView.findViewById(R.id.filterBar));
    addFilterItems();
    this.filterBar.setOnItemClickListener(this);
    toggleNaviButtonState();
    setNavs(this.catagoryNavi, this.regionNavi, this.sortNavi);
    if (this.dealAdapter != null)
      this.listView.setAdapter(this.dealAdapter);
  }

  public void requestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void requestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void resetExtraFilter()
  {
    this.extraFilterStr = null;
    this.dpNaviTags = null;
    this.filterReset = true;
    toggleNaviButtonState();
  }

  public void resetFilter()
  {
    this.currentCategoryNavi = ALL_CATEGORY;
    this.currentRegion = ALL_REGION;
    this.currentSort = DEFAULT_SORT;
  }

  public void setBannerViewData(DPObject[] paramArrayOfDPObject)
  {
    if (!this.headerViewInited)
      return;
    this.bannerHeaderView.setBanner(paramArrayOfDPObject);
  }

  public void setChannel(String paramString)
  {
    this.channel = paramString;
  }

  public void setCurrentCategory(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Navi"))
      throw new IllegalArgumentException("argument must be Navi");
    this.currentCategoryNavi = paramDPObject;
  }

  public void setCurrentRegion(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Navi"))
      throw new IllegalArgumentException("argument must be region");
    this.currentRegion = paramDPObject;
  }

  public void setCurrentSort(DPObject paramDPObject)
  {
    if (!isDPObjectof(paramDPObject, "Navi"))
      throw new IllegalArgumentException("argument must be Navi.");
    this.currentSort = paramDPObject;
  }

  protected void setEmpty(String paramString)
  {
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).setText(paramString);
  }

  public boolean setExtraFilterString(String paramString)
  {
    int k = 0;
    int i = 0;
    int j = k;
    try
    {
      if (TextUtils.isEmpty(paramString))
        return false;
      String str = paramString;
      j = k;
      if (paramString.startsWith("screening="))
      {
        j = k;
        str = paramString.substring("screening=".length());
      }
      j = k;
      if (!str.equals(this.extraFilterStr))
        i = 1;
      j = i;
      this.extraFilterStr = str;
      j = i;
      toggleNaviButtonState();
      return i;
    }
    catch (java.lang.Exception paramString)
    {
      while (true)
        i = j;
    }
  }

  public void setHeadViewType(int paramInt)
  {
    if (!this.headerViewInited)
      return;
    this.headerViewType = paramInt;
    if (paramInt == 0)
    {
      this.headerView.setVisibility(8);
      if (paramInt != 2)
        break label71;
      this.headerView.findViewById(R.id.section).setVisibility(0);
    }
    while (true)
    {
      if ((paramInt & 0x2) == 0)
        break label89;
      this.bannerHeaderView.setVisibility(0);
      return;
      this.headerView.setVisibility(0);
      break;
      label71: this.headerView.findViewById(R.id.section).setVisibility(8);
    }
    label89: this.bannerHeaderView.setVisibility(8);
  }

  public void setKeyword(String paramString)
  {
    this.keyword = paramString;
  }

  public void setNaviTags(DPObject[] paramArrayOfDPObject)
  {
    if (!this.filterReset)
      return;
    this.filterReset = false;
    this.dpNaviTags = paramArrayOfDPObject;
    toggleNaviButtonState();
  }

  protected void setNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (paramDPObject1 != null)
      this.catagoryNavi = paramDPObject1;
    if (paramDPObject2 != null)
      this.regionNavi = paramDPObject2;
    if (paramDPObject3 != null)
      this.sortNavi = paramDPObject3;
    if ((this.catagoryNavi != null) && (!isDPObjectof(this.catagoryNavi, "Navi")))
      throw new IllegalArgumentException("argument {0} must be Navi.");
    if ((this.regionNavi != null) && (!isDPObjectof(this.regionNavi, "Navi")))
      throw new IllegalArgumentException("argument {1} must be Navi.");
    if ((this.sortNavi != null) && (!isDPObjectof(this.sortNavi, "Navi")))
      throw new IllegalArgumentException("argument {2} must be Navi.");
    if (paramDPObject1 != null)
      this.currentCategoryNavi = findSelectedNavi(paramDPObject1);
    if (this.currentCategoryNavi == null)
      this.currentCategoryNavi = ALL_CATEGORY;
    if (paramDPObject2 != null)
      this.currentRegion = findSelectedNavi(paramDPObject2);
    if (this.currentRegion == null)
      this.currentRegion = ALL_REGION;
    if (paramDPObject3 != null)
      this.currentSort = findSelectedNavi(paramDPObject3);
    if (this.currentSort == null)
      this.currentSort = DEFAULT_SORT;
    updateNavs(this.currentCategoryNavi, this.currentRegion, this.currentSort);
  }

  protected void setSearchEmpty(String paramString)
  {
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).setText("找不到与");
    paramString = new SpannableString(paramString);
    paramString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 0, paramString.length(), 33);
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).append(paramString);
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).append("相关的团购");
    this.dealListEmptyView.findViewById(R.id.ic_tips).setVisibility(0);
    this.dealListEmptyView.findViewById(R.id.ic_fail_message).setVisibility(0);
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).setText("或暂时没有与");
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).append(paramString);
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).append("相关的团购");
  }

  protected boolean setSelectedNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    int j = 0;
    int i = j;
    if (paramDPObject1 != null)
    {
      i = j;
      if (isDPObjectof(paramDPObject1, "Navi"))
      {
        i = j;
        if (paramDPObject1 != getCurrentCategory())
        {
          setCurrentCategory(paramDPObject1);
          i = 1;
        }
      }
    }
    j = i;
    if (paramDPObject2 != null)
    {
      j = i;
      if (isDPObjectof(paramDPObject2, "Navi"))
      {
        j = i;
        if (paramDPObject2.getInt("ID") != getCurrentRegion().getInt("ID"))
        {
          setCurrentRegion(paramDPObject2);
          j = 1;
        }
      }
    }
    i = j;
    if (paramDPObject3 != null)
    {
      i = j;
      if (isDPObjectof(paramDPObject3, "Navi"))
      {
        i = j;
        if (paramDPObject3.getString("ID") != getCurrentSort().getString("ID"))
        {
          if (!"2".equals(paramDPObject3.getString("ID")))
            break label177;
          i = j;
          if (location() != null)
          {
            setCurrentSort(paramDPObject3);
            i = 1;
          }
        }
      }
    }
    return i;
    label177: setCurrentSort(paramDPObject3);
    return true;
  }

  protected final void toggleNaviButtonState()
  {
    if (this.naviScreeningView == null)
      return;
    if (TextUtils.isEmpty(this.extraFilterStr))
      this.naviScreeningView.setSelected(false);
    while ((this.dpNaviTags != null) && (this.dpNaviTags.length != 0))
    {
      this.naviScreeningView.setEnabled(true);
      return;
      this.naviScreeningView.setSelected(true);
    }
    this.naviScreeningView.setEnabled(false);
  }

  protected void updateHotelHeaderView(long paramLong1, long paramLong2)
  {
    if (this.headerViewInited)
    {
      ((TextView)this.headerView.findViewById(16908308)).setText("  入住 " + SDF.format(Long.valueOf(paramLong1)));
      ((TextView)this.headerView.findViewById(16908309)).setText("  退房 " + SDF.format(Long.valueOf(paramLong2)));
    }
  }

  public void updateNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (paramDPObject1 != null)
    {
      this.currentCategoryNavi = paramDPObject1;
      if (paramDPObject2 == null)
        break label61;
      this.currentRegion = paramDPObject2;
      label18: if (paramDPObject3 == null)
        break label71;
    }
    label61: label71: for (this.currentSort = paramDPObject3; ; this.currentSort = DEFAULT_SORT)
    {
      if (isDPObjectof(this.currentCategoryNavi, "Navi"))
        break label81;
      throw new IllegalArgumentException("argument {0} must be Navi");
      this.currentCategoryNavi = ALL_CATEGORY;
      break;
      this.currentRegion = ALL_REGION;
      break label18;
    }
    label81: if (!isDPObjectof(this.currentRegion, "Navi"))
      throw new IllegalArgumentException("argument {1} must be Navi");
    if (!isDPObjectof(this.currentSort, "Navi"))
      throw new IllegalArgumentException("argument {2} must be Navi");
    this.filterBar.setItem("region", this.currentRegion.getString("Name"));
    this.filterBar.setItem("category", this.currentCategoryNavi.getString("Name"));
    this.filterBar.setItem("rank", this.currentSort.getString("Name"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.DealListFragmentWithFilter
 * JD-Core Version:    0.6.0
 */