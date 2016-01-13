package com.dianping.main.city;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import com.dianping.adapter.MergeAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.MainBannerView;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.content.AllCityStore;
import com.dianping.content.CityStore.LoadLocalCitiesListener;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.util.SearchUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.AlphabetBar;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.model.City;>;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import org.json.JSONObject;

public class CityListPickerActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener, ShopListTabView.TabChangeListener, View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, CityStore.LoadLocalCitiesListener, AbstractSearchFragment.OnSearchFragmentListener
{
  private static final String ACTION_OVERSEA_SWITCH_CITY = "oversea:switchcity";
  static final Object BANNER;
  private static final int CATEGORY_ROW_NUM = 3;
  static final Object GPS_CITY = new Object();
  protected static final String SELECT_ALL_CITY = "select_all_city";
  protected static final String SELECT_FOREIGN_CITY = "select_foreign_city";
  static final int TYPE_ALL = 0;
  static final int TYPE_FOREIGN = 1;
  CategoryViewInfo CATEGORY_FOREIGN_HOT_CITY = new CategoryViewInfo("其他海外热门目的地", 12, "select_city_hot", 0);
  CategoryViewInfo CATEGORY_FOREIGN_HOT_T5_CITY = new CategoryViewInfo("海外热门目的地", 6, "select_city_hot", 2);
  CategoryViewInfo CATEGORY_LOCAL_HOT_CITY = new CategoryViewInfo("国内热门城市", 6, "select_city_hot", 1);
  CategoryViewInfo CATEGORY_NEARBY_CITY = new CategoryViewInfo("周边热门城市", 6, "select_city_nearby", 2);
  CategoryViewInfo CATEGORY_NEARBY_FOREIGN_CITY = new CategoryViewInfo("周边热门目的地", 6, "select_city_nearby", 2);
  CategoryViewInfo CATEGORY_SELECT_ALL_CITY = new CategoryViewInfo("历史访问城市", 3, "select_city_recent", 2);
  CategoryViewInfo CATEGORY_SELECT_FOREIGN_CITY = new CategoryViewInfo("历史访问海外目的地", 3, "select_city_recent", 2);
  Object[] all_city = { GPS_CITY, this.CATEGORY_SELECT_ALL_CITY, this.CATEGORY_NEARBY_CITY, this.CATEGORY_LOCAL_HOT_CITY, this.CATEGORY_FOREIGN_HOT_T5_CITY };
  protected DPObject bannerForeign;
  MApiRequest cityBannerRequest;
  private String cityKeyword;
  MApiRequest cityListRequest;
  int city_type = 0;
  Object[] foreign_city = { BANNER, GPS_CITY, this.CATEGORY_SELECT_FOREIGN_CITY, this.CATEGORY_NEARBY_FOREIGN_CITY, this.CATEGORY_FOREIGN_HOT_CITY };
  protected City gpsCity;
  protected HotAdapter hotAdapter;
  protected CityAdapter mAdapter;
  protected ArrayList<Object> mCities = new ArrayList();
  protected ArrayList<Object> mForeignCities = new ArrayList();
  protected AlphabetBar mIndexBar;
  protected SharedPreferences mPref;
  protected ButtonSearchBar mSearchBar;
  protected ShopListTabView mTabBar;
  protected SectionIndexerMergeAdapter mergeAdapter;
  MApiRequest nearbyRequest;
  BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("oversea:switchcity".equals(paramIntent.getAction()))
      {
        paramContext = paramIntent.getStringExtra("data");
        if (TextUtils.isEmpty(paramContext));
      }
      try
      {
        paramContext = new JSONObject(paramContext);
        int i = paramContext.optInt("cityId");
        paramContext = paramContext.optString("cityName");
        if ((i > 0) && (!TextUtils.isEmpty(paramContext)))
          CityListPickerActivity.this.updateCityFromWeb(i, paramContext);
        return;
      }
      catch (java.lang.Exception paramContext)
      {
        Log.e("CityListPickerActivity", "extract parameters from json failed", paramContext);
      }
    }
  };
  Object selectedItem;
  private boolean shouldShowTitle = true;

  static
  {
    BANNER = new Object();
  }

  private ArrayList<City> dpArrayToCityList(DPObject[] paramArrayOfDPObject)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramArrayOfDPObject != null)
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      if (i < j)
      {
        Object localObject = paramArrayOfDPObject[i];
        if (TextUtils.isEmpty(((DPObject)localObject).getString("Url")));
        for (localObject = City.fromDPObject((DPObject)localObject); ; localObject = City.fromDPObjectWithUrl((DPObject)localObject))
        {
          localArrayList.add(localObject);
          i += 1;
          break;
        }
      }
    }
    return (ArrayList<City>)localArrayList;
  }

  private void updateLocalCities()
  {
    if (CityUtils.getCities() == null)
      return;
    getCityFirstCharList(this.mCities, getDomesticCities());
    getCityFirstCharList(this.mForeignCities, getForeignCities());
  }

  protected View creatBannerView(DPObject paramDPObject, View paramView)
  {
    Object localObject;
    if ((paramView instanceof MainBannerView))
    {
      paramView = (MainBannerView)paramView;
      localObject = paramView;
      if (paramView == null)
      {
        paramView = new MainBannerView(this);
        paramView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
        paramView.hideCloseButton();
        if (paramDPObject != null)
          break label62;
        paramView.hide();
        localObject = paramView;
      }
    }
    label62: 
    do
    {
      return localObject;
      paramView = null;
      break;
      paramDPObject = paramDPObject.getArray("BannerItemList");
      if ((paramDPObject == null) || (paramDPObject.length <= 0))
        break label121;
      localObject = new ArrayList();
      ((ArrayList)localObject).addAll(Arrays.asList(paramDPObject));
      paramView.setAnnounce((ArrayList)localObject, preferences());
      paramView.show();
      localObject = paramView;
    }
    while (paramDPObject.length <= 1);
    paramView.startAutoFlip();
    return paramView;
    label121: paramView.hide();
    return (View)paramView;
  }

  protected NovaTextView createCity(Spannable paramSpannable, View paramView, ViewGroup paramViewGroup, int paramInt1, int paramInt2)
  {
    paramView = createCity(paramView, paramViewGroup, paramInt1, paramInt2);
    paramView.setText(paramSpannable);
    return paramView;
  }

  protected NovaTextView createCity(View paramView, ViewGroup paramViewGroup, int paramInt1, int paramInt2)
  {
    if ((paramView instanceof NovaTextView));
    for (paramView = (NovaTextView)paramView; ; paramView = null)
    {
      Object localObject = paramView;
      if (paramView == null)
        localObject = (NovaTextView)getLayoutInflater().inflate(R.layout.city_item, paramViewGroup, false);
      int i = ((NovaTextView)localObject).getPaddingBottom();
      int j = ((NovaTextView)localObject).getPaddingTop();
      int k = ((NovaTextView)localObject).getPaddingRight();
      int m = ((NovaTextView)localObject).getPaddingLeft();
      ((NovaTextView)localObject).setBackgroundResource(paramInt1);
      ((NovaTextView)localObject).setPadding(m, j, k, i);
      ((NovaTextView)localObject).setLayoutParams(new AbsListView.LayoutParams(-1, paramInt2));
      return localObject;
    }
  }

  protected NovaTextView createCity(String paramString, View paramView, ViewGroup paramViewGroup, int paramInt1, int paramInt2, int paramInt3)
  {
    paramView = createCity(paramView, paramViewGroup, paramInt1, paramInt3);
    paramView.setText(paramString);
    paramView.setTextColor(getResources().getColor(paramInt2));
    return paramView;
  }

  protected View createHotCategoryLayout(String paramString1, ArrayList<City> paramArrayList, View.OnClickListener paramOnClickListener, View paramView, ViewGroup paramViewGroup, int paramInt1, String paramString2, int paramInt2)
  {
    if ((paramView instanceof CityGridLayout));
    for (paramView = (CityGridLayout)paramView; ; paramView = null)
    {
      Object localObject = paramView;
      if (paramView == null)
        localObject = new CityGridLayout(paramViewGroup.getContext());
      ((CityGridLayout)localObject).setItems(paramString1, paramArrayList, paramOnClickListener, paramInt1, paramString2, paramInt2);
      return localObject;
    }
  }

  protected void doSwitch(City paramCity)
  {
    if (paramCity == null)
      return;
    Intent localIntent = new Intent();
    localIntent.putExtra("city", paramCity);
    setResult(-1, localIntent);
    finish();
  }

  protected City getCityById(int paramInt)
  {
    return CityUtils.getCityById(paramInt);
  }

  public void getCityFirstCharList(ArrayList<Object> paramArrayList, City[] paramArrayOfCity)
  {
    paramArrayList.clear();
    if (paramArrayOfCity == null);
    while (true)
    {
      return;
      Object localObject1 = "";
      int j = paramArrayOfCity.length;
      int i = 0;
      while (i < j)
      {
        City localCity = paramArrayOfCity[i];
        Object localObject2 = localObject1;
        if (!localCity.firstChar().equals(localObject1))
        {
          localObject2 = localCity.firstChar();
          paramArrayList.add(localObject2);
        }
        paramArrayList.add(localCity);
        i += 1;
        localObject1 = localObject2;
      }
    }
  }

  protected City[] getDomesticCities()
  {
    ArrayList localArrayList = new ArrayList();
    City[] arrayOfCity = getSortBy1stChar();
    int j = arrayOfCity.length;
    int i = 0;
    while (i < j)
    {
      City localCity = arrayOfCity[i];
      if (!localCity.isForeign())
        localArrayList.add(localCity);
      i += 1;
    }
    return (City[])localArrayList.toArray(new City[localArrayList.size()]);
  }

  protected City[] getForeignCities()
  {
    ArrayList localArrayList = new ArrayList();
    City[] arrayOfCity = getSortBy1stChar();
    int j = arrayOfCity.length;
    int i = 0;
    while (i < j)
    {
      City localCity = arrayOfCity[i];
      if (localCity.isForeign())
        localArrayList.add(localCity);
      i += 1;
    }
    return (City[])localArrayList.toArray(new City[localArrayList.size()]);
  }

  protected ArrayList<City> getForeignFiveCities(ArrayList<City> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    paramArrayList = paramArrayList.iterator();
    do
    {
      if (!paramArrayList.hasNext())
        break;
      localArrayList.add((City)paramArrayList.next());
    }
    while (localArrayList.size() != 5);
    localArrayList.add(new City(-1, "全部海外", null, false, false, 0.0D, 0.0D, 0, false, false));
    return localArrayList;
  }

  protected City[] getLocalCities()
  {
    return getSortBy1stChar();
  }

  public String getPageName()
  {
    if (this.city_type == 0)
      return getMyUrl();
    return getMyUrl() + "_overseas";
  }

  protected ArrayList<City> getSelectCities(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    paramString = this.mPref.getString(paramString, "");
    if (!TextUtils.isEmpty(paramString))
    {
      paramString = paramString.split(",");
      int i = paramString.length - 1;
      while (true)
        if (i >= 0)
          try
          {
            City localCity = getCityById(Integer.valueOf(paramString[i]).intValue());
            if (localCity != null)
              localArrayList.add(localCity);
            i -= 1;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            while (true)
              localNumberFormatException.printStackTrace();
          }
    }
    return localArrayList;
  }

  protected City[] getSortBy1stChar()
  {
    return CityUtils.getSortBy1stChar();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadBannerServer()
  {
    this.cityBannerRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/operating/getbannerinfo.bin").buildUpon().appendQueryParameter("pos", Integer.toString(4)).appendQueryParameter("categoryid", "1").appendQueryParameter("cityid", "" + cityId()).toString(), CacheType.DISABLED);
    mapiService().exec(this.cityBannerRequest, this);
  }

  public void loadFromServer()
  {
    if (CityUtils.getCities() == null)
      showProgressDialog("正在加载城市列表请稍候...");
    Object localObject = location();
    if (localObject == null)
    {
      localObject = null;
      if (localObject == null)
        break label64;
    }
    label64: for (this.cityListRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/citylist.bin", CacheType.SERVICE); ; this.cityListRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/citylist.bin", CacheType.SERVICE))
    {
      mapiService().exec(this.cityListRequest, this);
      return;
      localObject = ((Location)localObject).city();
      break;
    }
  }

  public void loadNearbyServer()
  {
    this.nearbyRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/common/getrelatedcities.bin?cityid=" + cityId() + "&v=" + Environment.versionName(), CacheType.DAILY);
    mapiService().exec(this.nearbyRequest, this);
  }

  public void onAfterLoad()
  {
    updateLocalCities();
    this.mAdapter.notifyDataSetChanged();
    dismissDialog();
  }

  public void onClick(View paramView)
  {
    this.selectedItem = paramView.getTag();
    if ((this.selectedItem instanceof City))
    {
      if (TextUtils.isEmpty(((City)this.selectedItem).url()))
      {
        paramView = new Intent();
        paramView.putExtra("city", (City)this.selectedItem);
        setResult(-1, paramView);
        finish();
      }
    }
    else
      return;
    startActivity(((City)this.selectedItem).url());
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.shouldShowTitle = paramBundle.getBoolean("shouldShowTitle");
    int i;
    if (this.shouldShowTitle)
    {
      i = 0;
      setTitleVisibility(i);
      AllCityStore.addLoadLocalCityListener(this);
      this.mPref = preferences();
      this.mIndexBar = ((AlphabetBar)findViewById(R.id.sideBar));
      super.setTitle("城市列表");
      this.mAdapter = new CityAdapter();
      this.hotAdapter = new HotAdapter();
      this.mergeAdapter.addAdapter(this.hotAdapter);
      this.mergeAdapter.addAdapter(this.mAdapter);
      this.listView.setAdapter(this.mergeAdapter);
      updateLocalCities();
      this.mAdapter.notifyDataSetChanged();
      this.listView.setOnItemClickListener(this);
      this.mIndexBar.setListView(this.listView);
      this.mIndexBar.setSectionIndexter(this.mergeAdapter);
      this.mSearchBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
      i = SearchUtils.getHintId(SearchUtils.getSearchableInfo(this, getComponentName()));
      if (i <= 0)
        break label374;
      this.mSearchBar.setHint(i);
    }
    while (true)
    {
      this.mSearchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
      {
        public void onSearchRequested()
        {
          CitySearchFragment.newInstance(CityListPickerActivity.this, CityListPickerActivity.this.city_type + 1).setOnSearchFragmentListener(CityListPickerActivity.this);
          CityListPickerActivity.this.setTitleVisibility(8);
          CityListPickerActivity.access$002(CityListPickerActivity.this, false);
        }
      });
      this.mTabBar = ((ShopListTabView)LayoutInflater.from(this).inflate(R.layout.shoplist_tab_layout, null));
      this.mTabBar.setTabChangeListener(this);
      this.mTabBar.setLeftTitleText("全部");
      this.mTabBar.setRightTitleText("海外");
      ((NovaLinearLayout)this.mTabBar.findViewById(R.id.tab1)).setGAString("tab", "全部");
      ((NovaLinearLayout)this.mTabBar.findViewById(R.id.tab2)).setGAString("tab", "海外");
      super.getTitleBar().setCustomContentView(this.mTabBar);
      this.mTabBar.setCurIndex(0);
      paramBundle = new IntentFilter("oversea:switchcity");
      registerReceiver(this.receiver, paramBundle);
      setEmptyMsg("未找到匹配的城市", false);
      loadFromServer();
      loadBannerServer();
      loadNearbyServer();
      return;
      i = 8;
      break;
      label374: this.mSearchBar.setHint(R.string.city_search_hint);
    }
  }

  protected void onDestroy()
  {
    AllCityStore.removeLoadLocalCityListener();
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    this.selectedItem = this.listView.getItemAtPosition(paramInt);
    if ((this.selectedItem instanceof City))
    {
      paramAdapterView = new Intent();
      paramAdapterView.putExtra("city", (City)this.selectedItem);
      setResult(-1, paramAdapterView);
      finish();
    }
    if ((this.selectedItem == GPS_CITY) && (this.gpsCity != null))
    {
      paramAdapterView = new Intent();
      paramAdapterView.putExtra("city", this.gpsCity);
      setResult(-1, paramAdapterView);
      finish();
    }
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    this.mergeAdapter.notifyDataSetChanged();
  }

  public void onPreLoad()
  {
    showProgressDialog("正在加载数据");
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cityListRequest)
    {
      dismissDialog();
      this.cityListRequest = null;
    }
    if (paramMApiRequest == this.cityBannerRequest)
      this.cityBannerRequest = null;
    if (paramMApiRequest == this.nearbyRequest)
      this.nearbyRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cityListRequest)
      dismissDialog();
    try
    {
      setCities(((DPObject)paramMApiResponse.result()).getArray("Cities"), paramMApiResponse.rawData());
      updateLocalCities();
      label41: this.cityListRequest = null;
      this.mAdapter.notifyDataSetChanged();
      label253: 
      do
      {
        do
        {
          return;
          if (paramMApiRequest != this.nearbyRequest)
            continue;
          ArrayList localArrayList;
          if ((paramMApiResponse.result() instanceof DPObject))
          {
            paramMApiRequest = (DPObject)paramMApiResponse.result();
            paramMApiResponse = paramMApiRequest.getArray("RelatedCities");
            if (paramMApiResponse != null)
            {
              localArrayList = new ArrayList();
              int i = 0;
              while (i < paramMApiResponse.length - paramMApiResponse.length % 3)
              {
                localArrayList.add(City.fromDPObject(paramMApiResponse[i]));
                i += 1;
              }
              if (!city().isForeign())
                break label253;
              CategoryViewInfo.access$102(this.CATEGORY_NEARBY_FOREIGN_CITY, localArrayList);
            }
          }
          while (true)
          {
            CategoryViewInfo.access$102(this.CATEGORY_SELECT_ALL_CITY, getSelectCities("select_all_city"));
            CategoryViewInfo.access$102(this.CATEGORY_SELECT_FOREIGN_CITY, getSelectCities("select_foreign_city"));
            CategoryViewInfo.access$102(this.CATEGORY_LOCAL_HOT_CITY, dpArrayToCityList(paramMApiRequest.getArray("HotLocalCities")));
            paramMApiRequest = dpArrayToCityList(paramMApiRequest.getArray("HotOverSeaCities"));
            CategoryViewInfo.access$102(this.CATEGORY_FOREIGN_HOT_T5_CITY, getForeignFiveCities(paramMApiRequest));
            CategoryViewInfo.access$102(this.CATEGORY_FOREIGN_HOT_CITY, paramMApiRequest);
            this.hotAdapter.notifyDataSetChanged();
            this.nearbyRequest = null;
            return;
            CategoryViewInfo.access$102(this.CATEGORY_NEARBY_CITY, localArrayList);
          }
        }
        while (paramMApiRequest != this.cityBannerRequest);
        this.cityBannerRequest = null;
      }
      while (!(paramMApiResponse.result() instanceof DPObject));
      this.bannerForeign = ((DPObject)paramMApiResponse.result());
      this.hotAdapter.notifyDataSetChanged();
      return;
    }
    catch (java.lang.Exception paramMApiRequest)
    {
      break label41;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putBoolean("shouldShowTitle", this.shouldShowTitle);
    super.onSaveInstanceState(paramBundle);
  }

  public void onSearchFragmentDetach()
  {
    setTitleVisibility(0);
    this.shouldShowTitle = true;
  }

  public void onTabChanged(int paramInt)
  {
    this.city_type = paramInt;
    GAHelper.instance().setGAPageName(getPageName());
    GAHelper.instance().setRequestId(this, UUID.randomUUID().toString(), null, false);
    this.mAdapter.notifyDataSetChanged();
    this.hotAdapter.notifyDataSetChanged();
    if (paramInt == 0);
    for (String str = "全部"; ; str = "海外")
    {
      statisticsEvent("index5", "index5_city_filter", str, 0);
      return;
    }
  }

  protected void setCities(DPObject[] paramArrayOfDPObject, byte[] paramArrayOfByte)
  {
    CityUtils.setCities(paramArrayOfDPObject, paramArrayOfByte);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.city_list);
    this.mergeAdapter = new SectionIndexerMergeAdapter();
  }

  public void startSearch(DPObject paramDPObject)
  {
  }

  protected void updateCityFromWeb(int paramInt, String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("cityId", paramInt);
    localIntent.putExtra("cityName", paramString);
    setResult(-1, localIntent);
    finish();
  }

  protected class CategoryViewInfo
  {
    private int categoryID;
    private ArrayList<City> cityList;
    private String elementName;
    private int limit;
    private String title;

    public CategoryViewInfo(String paramInt1, int paramString1, String paramInt2, int arg5)
    {
      this.title = paramInt1;
      this.limit = paramString1;
      this.elementName = paramInt2;
      int i;
      this.categoryID = i;
    }
  }

  protected class CityAdapter extends BaseAdapter
  {
    public ArrayList<Object> list = new ArrayList();

    public CityAdapter()
    {
      resetData();
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof City))
      {
        if ((paramInt < getCount() - 1) && ((getItem(paramInt + 1) instanceof City)))
        {
          paramInt = R.drawable.city_middle_background;
          paramView = CityListPickerActivity.this.createCity(((City)localObject).name(), paramView, paramViewGroup, paramInt, R.color.black, ViewUtils.dip2px(CityListPickerActivity.this, 50.0F));
          paramView.setGAString("select_city_list", ((City)localObject).name());
          paramViewGroup = paramView.gaUserInfo;
          if (CityListPickerActivity.this.city_type != 0)
            break label121;
        }
        label121: for (paramInt = 3; ; paramInt = 1)
        {
          paramViewGroup.category_id = Integer.valueOf(paramInt);
          return paramView;
          paramInt = R.drawable.city_bottom_background;
          break;
        }
      }
      CityListPickerActivity localCityListPickerActivity = CityListPickerActivity.this;
      if ((localObject instanceof String));
      for (localObject = (String)localObject; ; localObject = "")
        return localCityListPickerActivity.createCity((String)localObject, paramView, paramViewGroup, R.drawable.city_title_background, R.color.light_gray, ViewUtils.dip2px(CityListPickerActivity.this, 32.0F));
    }

    public boolean isEnabled(int paramInt)
    {
      return (getItem(paramInt) instanceof City);
    }

    public void notifyDataSetChanged()
    {
      resetData();
      super.notifyDataSetChanged();
    }

    public void resetData()
    {
      if (CityListPickerActivity.this.city_type == 0)
      {
        this.list = CityListPickerActivity.this.mCities;
        return;
      }
      this.list = CityListPickerActivity.this.mForeignCities;
    }
  }

  class HotAdapter extends BaseAdapter
  {
    ArrayList<Object> list = new ArrayList();

    public HotAdapter()
    {
      resetData();
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof City))
        return ((City)localObject).id();
      return paramInt << 32;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (localObject == CityListPickerActivity.BANNER)
        return 0;
      if (localObject == CityListPickerActivity.GPS_CITY)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = getItem(paramInt);
      if (localObject1 == CityListPickerActivity.GPS_CITY)
      {
        localObject1 = CityListPickerActivity.this.location();
        Object localObject2 = CityListPickerActivity.this;
        if (localObject1 == null)
        {
          localObject1 = null;
          ((CityListPickerActivity)localObject2).gpsCity = ((City)localObject1);
          if (CityListPickerActivity.this.locationService().status() != -1)
            break label125;
          localObject1 = "定位失败，请点击重试";
          paramViewGroup = CityListPickerActivity.this.createCity("定位失败，请点击重试", paramView, paramViewGroup, R.drawable.city_bottom_background, R.color.light_gray, ViewUtils.dip2px(CityListPickerActivity.this, 50.0F));
          paramView = (View)localObject1;
        }
        while (true)
        {
          paramViewGroup.setGAString("select_city_gps", paramView);
          paramViewGroup.gaUserInfo.category_id = Integer.valueOf(0);
          return paramViewGroup;
          localObject1 = ((Location)localObject1).city();
          break;
          label125: if (CityListPickerActivity.this.gpsCity == null)
          {
            localObject1 = "正在定位城市...";
            paramViewGroup = CityListPickerActivity.this.createCity("正在定位城市...", paramView, paramViewGroup, R.drawable.city_bottom_background, R.color.light_gray, ViewUtils.dip2px(CityListPickerActivity.this, 50.0F));
            paramView = (View)localObject1;
            continue;
          }
          localObject1 = CityListPickerActivity.this.gpsCity.name();
          localObject2 = (String)localObject1 + "  GPS定位";
          SpannableString localSpannableString = new SpannableString((CharSequence)localObject2);
          localSpannableString.setSpan(new ForegroundColorSpan(CityListPickerActivity.this.getResources().getColor(R.color.black)), 0, CityListPickerActivity.this.gpsCity.name().length(), 17);
          localSpannableString.setSpan(new ForegroundColorSpan(CityListPickerActivity.this.getResources().getColor(R.color.light_gray)), CityListPickerActivity.this.gpsCity.name().length() + 1, ((String)localObject2).length(), 17);
          paramViewGroup = CityListPickerActivity.this.createCity(localSpannableString, paramView, paramViewGroup, R.drawable.city_bottom_background, ViewUtils.dip2px(CityListPickerActivity.this, 50.0F));
          paramView = (View)localObject1;
        }
      }
      if ((localObject1 instanceof CityListPickerActivity.CategoryViewInfo))
      {
        localObject1 = (CityListPickerActivity.CategoryViewInfo)localObject1;
        return CityListPickerActivity.this.createHotCategoryLayout(((CityListPickerActivity.CategoryViewInfo)localObject1).title, ((CityListPickerActivity.CategoryViewInfo)localObject1).cityList, CityListPickerActivity.this, paramView, paramViewGroup, ((CityListPickerActivity.CategoryViewInfo)localObject1).limit, ((CityListPickerActivity.CategoryViewInfo)localObject1).elementName, ((CityListPickerActivity.CategoryViewInfo)localObject1).categoryID);
      }
      if (localObject1 == CityListPickerActivity.BANNER)
        return CityListPickerActivity.this.creatBannerView(CityListPickerActivity.this.bannerForeign, paramView);
      return (View)(View)null;
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean hasStableIds()
    {
      return true;
    }

    public boolean isEnabled(int paramInt)
    {
      return getItem(paramInt) == CityListPickerActivity.GPS_CITY;
    }

    public void notifyDataSetChanged()
    {
      resetData();
      super.notifyDataSetChanged();
    }

    public void resetData()
    {
      this.list.clear();
      if (CityListPickerActivity.this.city_type == 0)
      {
        this.list.addAll(Arrays.asList(CityListPickerActivity.this.all_city));
        return;
      }
      this.list.addAll(Arrays.asList(CityListPickerActivity.this.foreign_city));
    }
  }

  class SectionIndexerMergeAdapter extends MergeAdapter
    implements SectionIndexer
  {
    protected final String[] SECTIONS = { "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    SectionIndexerMergeAdapter()
    {
    }

    public int getPositionForSection(int paramInt)
    {
      int i = 0;
      if (CityListPickerActivity.this.hotAdapter != null)
        i = CityListPickerActivity.this.hotAdapter.getCount();
      if ((paramInt <= 0) || (paramInt >= this.SECTIONS.length))
        return 0;
      String str = this.SECTIONS[paramInt];
      if (CityListPickerActivity.this.mAdapter.list != null)
      {
        paramInt = 0;
        while (paramInt < CityListPickerActivity.this.mAdapter.list.size())
        {
          Object localObject = CityListPickerActivity.this.mAdapter.list.get(paramInt);
          if ((((localObject instanceof City)) && (((City)localObject).firstChar().equals(str))) || (((localObject instanceof String)) && (((String)localObject).equals(str))))
            return paramInt + i;
          paramInt += 1;
        }
      }
      return -1;
    }

    public int getSectionForPosition(int paramInt)
    {
      return 0;
    }

    public Object[] getSections()
    {
      return this.SECTIONS;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.city.CityListPickerActivity
 * JD-Core Version:    0.6.0
 */