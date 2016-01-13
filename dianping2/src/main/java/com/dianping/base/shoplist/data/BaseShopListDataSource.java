package com.dianping.base.shoplist.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.data.model.NavTree;
import com.dianping.base.shoplist.data.model.SimpleShopListModel;
import com.dianping.base.shoplist.util.OTAPriceLoad;
import com.dianping.base.shoplist.util.ShopListUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BaseShopListDataSource extends AbstractDataSource
  implements SimpleShopListModel
{
  public String aroundCitiesSearchValue;
  public String attributes;
  public boolean bannerIsShown = false;
  private ArrayList<DPObject> bannerList = new ArrayList();
  public NavTree categoryNavTree;
  private int cityId;
  private DPObject curCategory;
  private DPObject curMetro;
  private DPObject curRange;
  private DPObject curRegion;
  private DPObject curSelectNav = null;
  private DPObject curSort;
  private int currentTabIndex = 0;
  private double customLatitude;
  private double customLongitude;
  private String emptyMsg;
  private String errorMsg;
  private String externalUrl;
  private DPObject[] filterCategories;
  private DPObject[] filterMetros;
  public DPObject filterNavi;
  private DPObject[] filterRanges;
  private DPObject[] filterRegions;
  private DPObject[] filterSelectNavs;
  private DPObject[] filterSorts;
  public String filters;
  protected int firstPageSize;
  private boolean hasSearchDate;
  private long hotelCheckinTime;
  private long hotelCheckoutTime;
  private int hotelFilterType;
  public int hotelNearbyShopId = 0;
  public OTAPriceLoad hotelPricesLoad;
  public int hotelTabIndex = 0;
  private ArrayList<DPObject> hotelTuanList = new ArrayList();
  private boolean isEnd;
  private boolean isFromHome;
  private boolean isMetro;
  public boolean isShopNearBy = false;
  public boolean isTopShopNearBy;
  private String keywordInfo;
  private View lastExtraView;
  public SparseBooleanArray mGaRecordTables = new SparseBooleanArray();
  private int maxPrice = -1;
  public NavTree metroNavTree;
  private int minPrice = -1;
  public DPObject moreHotelsEntrance;
  public int nearbyShopId = 0;
  public boolean needLocalRegion;
  private int nextStartIndex;
  private double offsetLatitude;
  private double offsetLongitude;
  public DPObject[] operatingLocation;
  public String pageModule;
  private int placeType = 0;
  private String queryId = "";
  public int recResultCount = 0;
  public int recordCount;
  public NavTree regionNavTree;
  private boolean reloadFlag = false;
  public boolean searchAroundCities = false;
  private String searchValue;
  public DPObject selectedDeal;
  public String selectedListUrl;
  private ArrayList<DPObject> shops = new ArrayList();
  private boolean showDistance;
  private int startIndex;
  private String suggestKeyword;
  private String suggestValue;
  public String targetInfo;
  public String targetPage;
  public int targetType;
  public int wedProduct = 0;

  private void addLocalRegion()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DPObject("Region").edit().putInt("ID", -1).putString("Name", "附近").putInt("ParentID", 0).generate());
    DPObject[] arrayOfDPObject = this.filterRanges;
    int j = arrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = arrayOfDPObject[i];
      localArrayList.add(new DPObject("Region").edit().putInt("ID", ShopListUtils.parsePairIntId(localDPObject)).putString("Name", localDPObject.getString("Name")).putInt("ParentID", -1).generate());
      i += 1;
    }
    if (this.filterRegions != null)
      localArrayList.addAll(Arrays.asList(this.filterRegions));
    this.filterRegions = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    if ((this.curRegion == null) && (this.curRange != null))
      this.curRegion = new DPObject("Region").edit().putInt("ID", ShopListUtils.parsePairIntId(this.curRange)).putString("Name", this.curRange.getString("Name")).putInt("ParentID", -1).generate();
  }

  private void storeNearByShops(List<DPObject> paramList)
  {
    if (paramList == null);
    while (true)
    {
      return;
      int i = 0;
      while (i < paramList.size())
      {
        storeNearByShop(((DPObject)paramList.get(i)).edit().putInt("ListPosition", i).generate());
        i += 1;
      }
    }
  }

  private void storeShops(List<DPObject> paramList)
  {
    if (paramList == null);
    while (true)
    {
      return;
      int j = getDeviation();
      int i = 0;
      while (i < paramList.size())
      {
        storeOneShop(((DPObject)paramList.get(i)).edit().putInt("ListPosition", this.nextStartIndex + j + i).putString("ShopQueryId", this.queryId).generate());
        i += 1;
      }
    }
  }

  private void syncFilters()
  {
    this.filters = "";
    if (this.filterNavi != null)
    {
      DPObject[] arrayOfDPObject1 = this.filterNavi.getArray("FilterGroups");
      if (arrayOfDPObject1 != null)
      {
        StringBuilder localStringBuilder = new StringBuilder("");
        int i = 0;
        while (i < arrayOfDPObject1.length)
        {
          DPObject[] arrayOfDPObject2 = arrayOfDPObject1[i].getArray("Items");
          if (arrayOfDPObject2 != null)
          {
            int j = 0;
            while (j < arrayOfDPObject2.length)
            {
              if (arrayOfDPObject2[j].getBoolean("Selected"))
                localStringBuilder.append(arrayOfDPObject2[j].getString("ID")).append(",");
              j += 1;
            }
          }
          i += 1;
        }
        if (localStringBuilder.length() > 1)
          localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
        this.filters = localStringBuilder.toString();
      }
    }
  }

  public void appendResult(DPObject paramDPObject)
  {
    this.targetType = paramDPObject.getInt("TargetType");
    this.targetInfo = paramDPObject.getString("TargetInfo");
    if (this.targetInfo == null)
      this.targetInfo = "";
    if ((this.targetType == 1) && (!TextUtils.isEmpty(this.targetInfo)))
    {
      this.externalUrl = this.targetInfo;
      publishDataChange(22);
      return;
    }
    appendShops(paramDPObject);
    updateNavData(paramDPObject);
    changeStatus(2);
    this.keywordInfo = paramDPObject.getString("KeywordInfo");
  }

  public void appendShops(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") != this.nextStartIndex)
      return;
    this.queryId = paramDPObject.getString("QueryID");
    this.reloadFlag = true;
    if (this.nextStartIndex == 0)
    {
      this.bannerIsShown = false;
      clearShops(paramDPObject);
      this.firstPageSize = (paramDPObject.getInt("NextStartIndex") - this.startIndex);
    }
    DPObject[] arrayOfDPObject = paramDPObject.getArray("NearbyHeadlineShops");
    if (arrayOfDPObject != null)
      storeNearByShops(Arrays.asList(arrayOfDPObject));
    arrayOfDPObject = paramDPObject.getArray("List");
    if (arrayOfDPObject != null)
      storeShops(Arrays.asList(arrayOfDPObject));
    this.recordCount = paramDPObject.getInt("RecordCount");
    this.startIndex = paramDPObject.getInt("StartIndex");
    this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
    this.isEnd = paramDPObject.getBoolean("IsEnd");
    setEmptyMsg(paramDPObject.getString("EmptyMsg"));
    this.errorMsg = null;
    this.hasSearchDate = paramDPObject.getBoolean("HasSearchDate");
    this.bannerList.clear();
    arrayOfDPObject = paramDPObject.getArray("BannerList");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
      this.bannerList.addAll(Arrays.asList(arrayOfDPObject));
    this.hotelTuanList.clear();
    arrayOfDPObject = paramDPObject.getArray("HotelTuanInfoList");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
      this.hotelTuanList.addAll(Arrays.asList(arrayOfDPObject));
    this.recResultCount = paramDPObject.getInt("RecResultCount");
    if (this.startIndex == 0)
    {
      publishDataChange(10);
      return;
    }
    publishDataChange(12);
  }

  public ArrayList<DPObject> bannerList()
  {
    return this.bannerList;
  }

  public int cityId()
  {
    return this.cityId;
  }

  public void clearFilterFlags()
  {
    this.isMetro = false;
    this.curSelectNav = null;
    this.minPrice = -1;
    this.maxPrice = -1;
    setFilterNavi(null);
  }

  public void clearListHeaderInfo()
  {
    this.hotelTuanList.clear();
    this.targetType = -1;
    this.recResultCount = 0;
  }

  protected void clearShops()
  {
    this.shops.clear();
  }

  protected void clearShops(DPObject paramDPObject)
  {
    this.shops.clear();
  }

  public DPObject curCategory()
  {
    return this.curCategory;
  }

  public DPObject curMetro()
  {
    return this.curMetro;
  }

  public DPObject curRange()
  {
    return this.curRange;
  }

  public DPObject curRegion()
  {
    return this.curRegion;
  }

  public DPObject curSelectNav()
  {
    return this.curSelectNav;
  }

  public DPObject curSort()
  {
    return this.curSort;
  }

  public int currentTabIndex()
  {
    return this.currentTabIndex;
  }

  public double customLatitude()
  {
    return this.customLatitude;
  }

  public double customLongitude()
  {
    return this.customLongitude;
  }

  public String errorMsg()
  {
    return this.errorMsg;
  }

  public DPObject[] filterCategories()
  {
    return this.filterCategories;
  }

  public DPObject[] filterMetros()
  {
    return this.filterMetros;
  }

  public DPObject[] filterRanges()
  {
    return this.filterRanges;
  }

  public DPObject[] filterRegions()
  {
    return this.filterRegions;
  }

  public DPObject[] filterSelectNavs()
  {
    return this.filterSelectNavs;
  }

  public DPObject[] filterSorts()
  {
    return this.filterSorts;
  }

  public int firstPageSize()
  {
    return this.firstPageSize;
  }

  protected int getDeviation()
  {
    return 0;
  }

  public String getExternalUrl()
  {
    return this.externalUrl;
  }

  public int getHotelFilterType()
  {
    return this.hotelFilterType;
  }

  public boolean getReloadFlag()
  {
    return this.reloadFlag;
  }

  public boolean hasSearchDate()
  {
    return this.hasSearchDate;
  }

  public long hotelCheckinTime()
  {
    if ((float)this.hotelCheckinTime == 0.0F)
      return System.currentTimeMillis();
    return this.hotelCheckinTime;
  }

  public long hotelCheckoutTime()
  {
    if ((float)this.hotelCheckoutTime == 0.0F)
    {
      if ((float)this.hotelCheckinTime == 0.0F)
        return System.currentTimeMillis() + 86400000L;
      return this.hotelCheckinTime + 86400000L;
    }
    return this.hotelCheckoutTime;
  }

  public ArrayList<DPObject> hotelTuanList()
  {
    return this.hotelTuanList;
  }

  public boolean isEnd()
  {
    return this.isEnd;
  }

  public boolean isFromHome()
  {
    return this.isFromHome;
  }

  public boolean isMetro()
  {
    return this.isMetro;
  }

  public String keyWordInfo()
  {
    return this.keywordInfo;
  }

  public View lastExtraView()
  {
    return this.lastExtraView;
  }

  public int maxPrice()
  {
    return this.maxPrice;
  }

  public int minPrice()
  {
    return this.minPrice;
  }

  public int nextStartIndex()
  {
    return this.nextStartIndex;
  }

  public double offsetLatitude()
  {
    return this.offsetLatitude;
  }

  public double offsetLongitude()
  {
    return this.offsetLongitude;
  }

  public int placeType()
  {
    return this.placeType;
  }

  public void pullToRefresh(boolean paramBoolean)
  {
    this.nextStartIndex = 0;
    if (this.mDataLoader == null)
      throw new RuntimeException("DataSource should set a DataLoader!");
    this.mDataLoader.loadData(this.nextStartIndex, paramBoolean);
  }

  public String queryId()
  {
    return this.queryId;
  }

  public void reload(boolean paramBoolean)
  {
    if (this.nextStartIndex == 0)
      clearListHeaderInfo();
    changeStatus(1);
    if (this.mDataLoader == null)
      throw new RuntimeException("DataSource should set a DataLoader!");
    this.mDataLoader.loadData(this.nextStartIndex, paramBoolean);
  }

  public void reset(boolean paramBoolean)
  {
    this.nextStartIndex = 0;
    if (paramBoolean)
    {
      clearShops();
      this.bannerList.clear();
      this.mGaRecordTables.clear();
      this.errorMsg = null;
      this.emptyMsg = null;
      this.hotelNearbyShopId = 0;
    }
    clearListHeaderInfo();
    publishDataChange(10);
  }

  public String searchValue()
  {
    return this.searchValue;
  }

  public void setCityId(int paramInt)
  {
    this.cityId = paramInt;
  }

  public boolean setCurCategory(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      this.curCategory = null;
      return true;
    }
    if (this.curCategory == null)
    {
      this.curCategory = paramDPObject;
      return true;
    }
    if ((paramDPObject.getInt("ID") == this.curCategory.getInt("ID")) && (paramDPObject.getInt("ParentID") == curCategory().getInt("ParentID")))
      return false;
    this.curCategory = paramDPObject;
    return true;
  }

  public boolean setCurMetro(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.curMetro == null))
    {
      this.curMetro = paramDPObject;
      return true;
    }
    if ((paramDPObject.getInt("ID") == this.curMetro.getInt("ID")) && (paramDPObject.getInt("ParentID") == this.curMetro.getInt("ParentID")) && (paramDPObject.getInt("RegionType") == this.curMetro.getInt("RegionType")))
      return false;
    this.curMetro = paramDPObject;
    return true;
  }

  public boolean setCurRange(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      this.curRange = null;
      return true;
    }
    if (this.curRange == null)
    {
      this.curRange = paramDPObject;
      return true;
    }
    if (paramDPObject.getString("ID") == null)
      return false;
    if (paramDPObject.getString("ID").equals(this.curRange.getString("ID")))
      return false;
    this.curRange = paramDPObject;
    return true;
  }

  public boolean setCurRegion(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      this.curRegion = null;
    do
    {
      return false;
      if (this.curRegion != null)
        continue;
      this.curRegion = paramDPObject;
      return true;
    }
    while ((paramDPObject.getInt("ID") == this.curRegion.getInt("ID")) && (paramDPObject.getInt("ParentID") == this.curRegion.getInt("ParentID")) && (paramDPObject.getInt("RegionType") == this.curRegion.getInt("RegionType")));
    DPObject localDPObject = paramDPObject;
    if (paramDPObject.getString("Name") != null)
    {
      localDPObject = paramDPObject;
      if (paramDPObject.getString("Name").contains("（智能范围）"))
        localDPObject = new DPObject("Pair").edit().putInt("ID", paramDPObject.getInt("ID")).putString("Name", paramDPObject.getString("Name").replace("（智能范围）", "")).putInt("ParentID", paramDPObject.getInt("ParentID")).generate();
    }
    this.curRegion = localDPObject;
    return true;
  }

  public boolean setCurSelectNav(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      this.curSelectNav = null;
      return true;
    }
    if (this.curSelectNav == null)
    {
      this.curSelectNav = paramDPObject;
      return true;
    }
    if (paramDPObject.getInt("FilterId") == this.curSelectNav.getInt("FilterId"))
      return false;
    this.curSelectNav = paramDPObject;
    return true;
  }

  public boolean setCurSort(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      this.curSort = null;
      return true;
    }
    if (this.curSort == null)
    {
      this.curSort = paramDPObject;
      return true;
    }
    if (paramDPObject.getString("ID").equals(this.curSort.getString("ID")))
      return false;
    this.curSort = paramDPObject;
    return true;
  }

  public void setCurrentTabIndex(int paramInt)
  {
    this.currentTabIndex = paramInt;
  }

  public void setCustomGPS(double paramDouble1, double paramDouble2)
  {
    this.customLatitude = paramDouble1;
    this.customLongitude = paramDouble2;
  }

  public void setEmptyMsg(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    this.emptyMsg = paramString;
  }

  public void setError(String paramString)
  {
    this.errorMsg = paramString;
    changeStatus(3);
  }

  public void setFilterNavi(DPObject paramDPObject)
  {
    this.filterNavi = paramDPObject;
    syncFilters();
  }

  public void setFilterSorts(DPObject[] paramArrayOfDPObject)
  {
    this.filterSorts = paramArrayOfDPObject;
  }

  public void setHotelCheckinTime(long paramLong)
  {
    this.hotelCheckinTime = paramLong;
  }

  public void setHotelCheckoutTime(long paramLong)
  {
    this.hotelCheckoutTime = paramLong;
  }

  public void setHotelFilterType(int paramInt)
  {
    this.hotelFilterType = paramInt;
  }

  public void setIsFromHome(boolean paramBoolean)
  {
    this.isFromHome = paramBoolean;
  }

  public void setIsMetro(boolean paramBoolean)
  {
    this.isMetro = paramBoolean;
  }

  public void setLastExtraView(int paramInt, View.OnClickListener paramOnClickListener, Context paramContext)
  {
    if ((paramInt > 0) && (paramOnClickListener != null))
    {
      this.lastExtraView = LayoutInflater.from(paramContext).inflate(paramInt, null, false);
      this.lastExtraView.setOnClickListener(paramOnClickListener);
    }
  }

  public void setMaxPrice(int paramInt)
  {
    this.maxPrice = paramInt;
  }

  public void setMinPrice(int paramInt)
  {
    this.minPrice = paramInt;
  }

  public void setNeedLocalRegion(boolean paramBoolean)
  {
    this.needLocalRegion = paramBoolean;
  }

  public void setOffsetGPS(double paramDouble1, double paramDouble2)
  {
    this.offsetLatitude = paramDouble1;
    this.offsetLongitude = paramDouble2;
    publishDataChange(11);
  }

  public void setPlaceType(int paramInt)
  {
    this.placeType = paramInt;
  }

  public void setReloadFlag(boolean paramBoolean)
  {
    this.reloadFlag = paramBoolean;
  }

  public void setSearchValue(String paramString)
  {
    this.searchValue = paramString;
  }

  public void setShowDistance(boolean paramBoolean)
  {
    this.showDistance = paramBoolean;
  }

  public void setSuggestKeyword(String paramString)
  {
    this.suggestKeyword = paramString;
  }

  public void setSuggestValue(String paramString)
  {
    this.suggestValue = paramString;
  }

  public ArrayList<DPObject> shops()
  {
    return this.shops;
  }

  public boolean showDistance()
  {
    return this.showDistance;
  }

  public int startIndex()
  {
    return this.startIndex;
  }

  protected void storeNearByShop(DPObject paramDPObject)
  {
  }

  protected void storeOneShop(DPObject paramDPObject)
  {
    this.shops.add(paramDPObject);
  }

  public String suggestKeyword()
  {
    return this.suggestKeyword;
  }

  public String suggestValue()
  {
    return this.suggestValue;
  }

  protected void updateEventText(int paramInt, DPObject paramDPObject)
  {
    this.shops.set(paramInt, paramDPObject);
  }

  protected void updateNavData(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") != 0)
      return;
    this.filterRegions = new DPObject[0];
    this.filterMetros = new DPObject[0];
    Object localObject = paramDPObject.getObject("CurrentSort");
    if (localObject != null)
      this.curSort = ((DPObject)localObject);
    localObject = paramDPObject.getObject("CurrentSelect");
    if (localObject != null)
      this.curSelectNav = ((DPObject)localObject);
    localObject = paramDPObject.getArray("RegionNavs");
    DPObject[] arrayOfDPObject = paramDPObject.getArray("MetroNavs");
    if ((localObject != null) && (!this.isShopNearBy))
      this.filterRegions = ((DPObject)localObject);
    if ((arrayOfDPObject != null) && (!this.isShopNearBy))
      this.filterMetros = arrayOfDPObject;
    localObject = paramDPObject.getArray("CategoryNavs");
    if (localObject != null)
      this.filterCategories = ((DPObject)localObject);
    int j;
    int i;
    if ((this.filterCategories != null) && (this.curCategory != null))
    {
      j = this.curCategory.getInt("ID");
      localObject = this.filterCategories;
      int k = localObject.length;
      i = 0;
      if (i < k)
      {
        arrayOfDPObject = localObject[i];
        if ((arrayOfDPObject.getInt("ID") != j) || (arrayOfDPObject.getInt("ParentID") <= 0))
          break label772;
        this.curCategory = arrayOfDPObject;
      }
    }
    localObject = paramDPObject.getArray("RangeNavs");
    if (localObject != null)
    {
      this.filterRanges = ((DPObject)localObject);
      if ((this.needLocalRegion) || (this.isShopNearBy))
        addLocalRegion();
    }
    this.regionNavTree = NavTree.from(this.filterRegions);
    this.categoryNavTree = NavTree.from(this.filterCategories);
    this.metroNavTree = NavTree.from(this.filterMetros);
    localObject = paramDPObject.getArray("SortNavs");
    if (localObject != null)
      this.filterSorts = ((DPObject)localObject);
    localObject = paramDPObject.getArray("SelectNavs");
    if (localObject != null)
      this.filterSelectNavs = ((DPObject)localObject);
    localObject = paramDPObject.getObject("CurrentRegion");
    if (localObject != null)
      this.curRegion = ((DPObject)localObject);
    localObject = paramDPObject.getObject("CurrentRange");
    if (localObject != null)
    {
      this.curRange = ((DPObject)localObject);
      this.curRegion = new DPObject("Region").edit().putString("Name", this.curRange.getString("Name")).putInt("ID", ShopListUtils.parsePairIntId(this.curRange)).putInt("ParentID", -1).generate();
    }
    if ((!this.isMetro) && (this.curRegion != null) && ("".equals(this.curRegion.getString("Name"))) && (this.filterRegions != null))
    {
      localObject = this.filterRegions;
      j = localObject.length;
      i = 0;
      label445: if (i < j)
      {
        arrayOfDPObject = localObject[i];
        if (this.curRegion.getInt("ID") != arrayOfDPObject.getInt("ID"))
          break label781;
        this.curRegion = arrayOfDPObject;
      }
    }
    if ((this.isMetro) && (this.curMetro != null) && ("".equals(this.curMetro.getString("Name"))) && (this.filterMetros != null))
    {
      localObject = this.filterMetros;
      j = localObject.length;
      i = 0;
      label530: if (i < j)
      {
        arrayOfDPObject = localObject[i];
        if (this.curMetro.getInt("ID") != arrayOfDPObject.getInt("ID"))
          break label790;
        this.curMetro = arrayOfDPObject;
      }
    }
    if ((this.curCategory != null) && (-1 == this.curCategory.getInt("ParentID")) && (this.filterCategories != null))
    {
      if (this.curCategory.getInt("ID") == 0)
        this.curCategory = this.curCategory.edit().putInt("ParentID", 0).generate();
      localObject = this.filterCategories;
      j = localObject.length;
      i = 0;
      label640: if (i < j)
      {
        arrayOfDPObject = localObject[i];
        if (this.curCategory.getInt("ID") != arrayOfDPObject.getInt("ID"))
          break label799;
        this.curCategory = arrayOfDPObject;
      }
    }
    if ((this.curSort != null) && ("".equals(this.curSort.getString("Name"))) && (this.filterSorts != null))
    {
      localObject = this.filterSorts;
      j = localObject.length;
      i = 0;
    }
    while (true)
    {
      if (i < j)
      {
        arrayOfDPObject = localObject[i];
        if (this.curSort.getString("ID").equals(arrayOfDPObject.getString("ID")))
          this.curSort = arrayOfDPObject;
      }
      else
      {
        this.filterNavi = paramDPObject.getObject("FilterNavi");
        syncFilters();
        return;
        label772: i += 1;
        break;
        label781: i += 1;
        break label445;
        label790: i += 1;
        break label530;
        label799: i += 1;
        break label640;
      }
      i += 1;
    }
  }

  public void updateNavs(DPObject paramDPObject)
  {
    updateNavData(paramDPObject);
    publishDataChange(20);
  }

  public void updateShopListEvent(DPObject paramDPObject)
  {
    monitorenter;
    while (true)
    {
      int i;
      try
      {
        Object localObject = this.shops;
        if ((localObject == null) || (paramDPObject == null))
          return;
        if (paramDPObject.getArray("EventList") == null)
          continue;
        localObject = Arrays.asList(paramDPObject.getArray("EventList"));
        if (((List)localObject).size() == 0)
          continue;
        paramDPObject = new SparseArray();
        localObject = ((List)localObject).iterator();
        if (!((Iterator)localObject).hasNext())
          continue;
        DPObject localDPObject = (DPObject)((Iterator)localObject).next();
        if (localDPObject != null)
          continue;
        i = this.startIndex;
        if (i < this.shops.size())
        {
          localObject = (String)paramDPObject.get(((DPObject)this.shops.get(i)).getInt("ID"));
          if (TextUtils.isEmpty((CharSequence)localObject))
            break label209;
          updateEventText(i, ((DPObject)this.shops.get(i)).edit().putString("EventText", (String)localObject).generate());
          break label209;
          paramDPObject.put(localDPObject.getInt("ShopId"), localDPObject.getString("EventTag"));
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      publishDataChange(12);
      continue;
      label209: i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.BaseShopListDataSource
 * JD-Core Version:    0.6.0
 */