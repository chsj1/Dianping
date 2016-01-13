package com.dianping.takeaway.entity;

import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.util.TakeawayCarCacheManager;
import com.dianping.takeaway.util.TakeawayCarCacheManager.MenuCache;
import com.dianping.takeaway.util.TakeawayGAManager;
import com.dianping.util.network.NetworkUtils;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TakeawayDishMenuDataSource
{
  private final int FLAG_ERROR_TOAST = 1;
  private final int FLAG_SHOP_MENU_OUTOFRANGE = 2;
  private final int FLAG_SHOP_MENU_UNKNOWNADDRESS = 3;
  public final int REQUEST_CODE_CHANGEADDRESS = 2;
  public final int REQUEST_CODE_DELIVERYDETAIL = 3;
  public final int REQUEST_CODE_GETSUITABLEADDRESS = 4;
  public final int REQUEST_CODE_INPUTADDRESS = 1;
  public final int RESULT_CODE_DELIVERYDETAIL_ADDRESSCHANGED = 3;
  public final int RESULT_CODE_DELIVERYDETAIL_CLOSEMENU = 6;
  public final int RESULT_CODE_DELIVERYDETAIL_MENUCHANGED = 5;
  public final int RESULT_CODE_DELIVERYDETAIL_MENUREFRESH = 9;
  private final int SHOP_STATUS_BUSY = 2;
  private final int SHOP_STATUS_REST = 1;
  public final int SOURCE_ADDRESS = 2;
  public final int SOURCE_INVALID = -1;
  private NovaActivity activity;
  public HashMap<String, Integer> categorySelectedNumMap = new HashMap();
  public String curCategoryName = "";
  private DataLoadListener dataLoadlistener;
  public List<TakeawayDishInfo> dishInfoList = new ArrayList();
  public HashMap<Integer, TakeawayDishInfo> dishInfoMap = new HashMap();
  public String[] errorMsg;
  public String inputAddress;
  public String lat;
  public String lng;
  public List<TakeawayMenuCategory> mCategories = new ArrayList();
  public TakeawayShopInfo mShopInfo;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new FullRequestHandle()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      int i;
      Object localObject;
      double d1;
      label106: double d2;
      if (TakeawayDishMenuDataSource.this.shopDishRequest == paramMApiRequest)
      {
        TakeawayDishMenuDataSource.access$002(TakeawayDishMenuDataSource.this, null);
        if ((paramMApiResponse != null) && ((paramMApiResponse.error() instanceof SimpleMsg)))
        {
          paramMApiRequest = (SimpleMsg)paramMApiResponse.error();
          i = paramMApiRequest.flag();
          paramMApiResponse = paramMApiRequest.content();
          localObject = TakeawayDishMenuDataSource.this;
          if (paramMApiResponse != null)
          {
            paramMApiRequest = paramMApiResponse.split("\\|");
            ((TakeawayDishMenuDataSource)localObject).errorMsg = paramMApiRequest;
            if (i != 2)
              break label211;
            paramMApiRequest = TakeawayDishMenuDataSource.this.activity.location();
            if (paramMApiRequest == null)
              break label200;
            d1 = paramMApiRequest.latitude();
            if (paramMApiRequest == null)
              break label205;
            d2 = paramMApiRequest.longitude();
            label116: TakeawayGAManager.statistics_takeaway6_dishtips_ood(TakeawayDishMenuDataSource.this.activity, TakeawayDishMenuDataSource.this.shopID, String.valueOf(d1), String.valueOf(d2));
            paramMApiRequest = TakeawayDishMenuDataSource.this.getGAUserInfo();
            GAHelper.instance().contextStatisticsEvent(TakeawayDishMenuDataSource.this.activity, "ood", paramMApiRequest, "view");
            if (TakeawayDishMenuDataSource.this.dataLoadlistener != null)
              TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishFinsh(TakeawayDishMenuDataSource.DataStatus.ERROR_OUT_OF_RANGE, null);
          }
        }
      }
      label200: label205: label211: label244: 
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                return;
                paramMApiRequest = null;
                break;
                d1 = 0.0D;
                break label106;
                d2 = 0.0D;
                break label116;
                if (i != 3)
                  break label244;
              }
              while (TakeawayDishMenuDataSource.this.dataLoadlistener == null);
              TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishFinsh(TakeawayDishMenuDataSource.DataStatus.ERROR_UNKNOWN_ADDRESS, null);
              return;
              if (i != 1)
                break label277;
            }
            while (TakeawayDishMenuDataSource.this.dataLoadlistener == null);
            TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishFinsh(TakeawayDishMenuDataSource.DataStatus.ERROR_TOAST, paramMApiResponse);
            return;
          }
          while (TakeawayDishMenuDataSource.this.dataLoadlistener == null);
          TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishFinsh(TakeawayDishMenuDataSource.DataStatus.ERROR_NETWORK, null);
          return;
          boolean bool = NetworkUtils.isWIFIConnection(TakeawayDishMenuDataSource.this.activity);
          paramMApiResponse = TakeawayDishMenuDataSource.this.activity;
          localObject = TakeawayDishMenuDataSource.this.shopID;
          if (bool);
          for (paramMApiRequest = "1"; ; paramMApiRequest = "2")
          {
            TakeawayGAManager.statistics_takeaway6_dishtips_retry(paramMApiResponse, (String)localObject, paramMApiRequest);
            return;
          }
        }
        while (TakeawayDishMenuDataSource.this.suitableAddressRequest != paramMApiRequest);
        TakeawayDishMenuDataSource.access$502(TakeawayDishMenuDataSource.this, null);
      }
      while (TakeawayDishMenuDataSource.this.dataLoadlistener == null);
      label277: TakeawayDishMenuDataSource.this.dataLoadlistener.loadSuitableAddress(TakeawayNetLoadStatus.STATUS_FAILED, null);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      int i;
      if (TakeawayDishMenuDataSource.this.shopDishRequest == paramMApiRequest)
      {
        TakeawayDishMenuDataSource.access$002(TakeawayDishMenuDataSource.this, null);
        TakeawayDishMenuDataSource.this.takeawayResult = ((DPObject)paramMApiResponse.result());
        if ((TakeawayDishMenuDataSource.this.source == 3) || (TakeawayDishMenuDataSource.this.source == 5))
        {
          paramMApiRequest = TakeawayDishMenuDataSource.this.takeawayResult.getString("Address");
          if ((!TextUtils.isEmpty(paramMApiRequest)) && (TextUtils.isEmpty(TakeawayDishMenuDataSource.this.inputAddress)))
          {
            TakeawayDishMenuDataSource.this.inputAddress = paramMApiRequest;
            TakeawayDishMenuDataSource.this.lat = String.valueOf(TakeawayDishMenuDataSource.this.takeawayResult.getDouble("Lat"));
            TakeawayDishMenuDataSource.this.lng = String.valueOf(TakeawayDishMenuDataSource.this.takeawayResult.getDouble("Lng"));
          }
        }
        i = TakeawayDishMenuDataSource.this.takeawayResult.getInt("Status");
        TakeawayDishMenuDataSource.access$202(TakeawayDishMenuDataSource.this, TakeawayCarCacheManager.readCacheMenu(TakeawayDishMenuDataSource.this.activity));
        if ((TakeawayDishMenuDataSource.this.menuCache.shopId.equals(TakeawayDishMenuDataSource.this.shopID)) && ((i == 1) || (i == 2)))
        {
          TakeawayCarCacheManager.clearCacheMenu(TakeawayDishMenuDataSource.this.activity);
          TakeawayDishMenuDataSource.access$202(TakeawayDishMenuDataSource.this, TakeawayCarCacheManager.readCacheMenu(TakeawayDishMenuDataSource.this.activity));
        }
        TakeawayDishMenuDataSource.this.parseShopInfo(TakeawayDishMenuDataSource.this.takeawayResult);
        if (TakeawayDishMenuDataSource.this.dataLoadlistener != null)
          TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishMenu(TakeawayNetLoadStatus.STATUS_SUCCESS, null);
        if (i == 1)
          TakeawayDishMenuDataSource.this.activity.statisticsEvent("takeaway6", "takeaway6_dishtips_rest", "", 0);
      }
      do
      {
        do
        {
          do
            return;
          while (i != 2);
          TakeawayDishMenuDataSource.this.activity.statisticsEvent("takeaway6", "takeaway6_dishtips_busy", "", 0);
          return;
        }
        while (TakeawayDishMenuDataSource.this.suitableAddressRequest != paramMApiRequest);
        TakeawayDishMenuDataSource.access$502(TakeawayDishMenuDataSource.this, null);
      }
      while (TakeawayDishMenuDataSource.this.dataLoadlistener == null);
      TakeawayDishMenuDataSource.this.dataLoadlistener.loadSuitableAddress(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiResponse.result());
    }

    public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
    {
    }

    public void onRequestStart(MApiRequest paramMApiRequest)
    {
      if ((TakeawayDishMenuDataSource.this.shopDishRequest == paramMApiRequest) && (TakeawayDishMenuDataSource.this.dataLoadlistener != null))
        TakeawayDishMenuDataSource.this.dataLoadlistener.loadDishMenu(TakeawayNetLoadStatus.STATUS_START, null);
    }
  };
  private TakeawayCarCacheManager.MenuCache menuCache;
  public String queryId = "";
  public TakeawayMenuCategory recomCategory;
  public List<TakeawayDishInfo> recomDishInfoList = new ArrayList();
  private MApiRequest shopDishRequest;
  public String shopID;
  public int source;
  private MApiRequest suitableAddressRequest;
  public DPObject takeawayResult;

  public TakeawayDishMenuDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
  }

  private void checkCacheAndApplyIt(List<TakeawayDishInfo> paramList)
  {
    if (!this.shopID.equals(this.menuCache.shopId));
    Object localObject1;
    int j;
    int i;
    do
    {
      do
      {
        return;
        localObject1 = this.menuCache.dishMap;
        j = ((HashMap)localObject1).size();
      }
      while (j == 0);
      i = 0;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Object localObject2 = (TakeawayDishInfo)localIterator.next();
        TakeawayDishInfo localTakeawayDishInfo = (TakeawayDishInfo)((HashMap)localObject1).get(Integer.valueOf(((TakeawayDishInfo)localObject2).id));
        if (localTakeawayDishInfo == null)
          continue;
        int k = localTakeawayDishInfo.selectedNum;
        int m = ((TakeawayDishInfo)localObject2).maxAvailableNum();
        if ((((TakeawayDishInfo)localObject2).isSoldout) || (((TakeawayDishInfo)localObject2).isInShortSupply()) || ((m < k) && (m != -1)))
          continue;
        i += 1;
        ((TakeawayDishInfo)localObject2).selectedNum = k;
        localObject2 = ((TakeawayDishInfo)localObject2).dishGroup;
        ((TakeawayDishGroup)localObject2).currentAmount += k;
      }
    }
    while (i == j);
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      localObject1 = (TakeawayDishInfo)paramList.next();
      ((TakeawayDishInfo)localObject1).selectedNum = 0;
      ((TakeawayDishInfo)localObject1).dishGroup.currentAmount = 0;
    }
    TakeawayCarCacheManager.clearCacheMenu(this.activity);
  }

  private void parseShopInfo(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.mShopInfo = new TakeawayShopInfo(paramDPObject);
    this.recomDishInfoList.clear();
    this.dishInfoList.clear();
    this.dishInfoMap.clear();
    Object localObject1 = paramDPObject.getArray("DishSetList");
    int i;
    Object localObject3;
    int j;
    int k;
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      this.mCategories.clear();
      i = 0;
      if (i < localObject1.length)
      {
        DPObject[] arrayOfDPObject = localObject1[i];
        localObject2 = arrayOfDPObject.getString("Name");
        int m = arrayOfDPObject.getInt("MaxCount");
        localObject3 = new TakeawayDishGroup((String)localObject2);
        arrayOfDPObject = arrayOfDPObject.getArray("List");
        if (arrayOfDPObject != null);
        for (j = arrayOfDPObject.length; ; j = 0)
        {
          this.dishInfoList.add(TakeawayDishInfo.Builder.buildTitleItemInfo((TakeawayDishGroup)localObject3, (String)localObject2));
          k = 0;
          while (k < j)
          {
            Object localObject4 = arrayOfDPObject[k];
            localObject4 = TakeawayDishInfo.Builder.buildContentItemInfo((TakeawayDishGroup)localObject3, (String)localObject2, ((DPObject)localObject4).getInt("ID"), ((DPObject)localObject4).getString("Name"), ((DPObject)localObject4).getString("SalesVolume"), ((DPObject)localObject4).getDouble("OrigPrice"), ((DPObject)localObject4).getDouble("CurPrice"), ((DPObject)localObject4).getInt("MinFeeCalType"), ((DPObject)localObject4).getBoolean("IsSoldOut"), ((DPObject)localObject4).getInt("Status"), ((DPObject)localObject4).getString("HotNum"), ((DPObject)localObject4).getString("BigImageUrl"), ((DPObject)localObject4).getString("LittleImageUrl"), ((DPObject)localObject4).getString("DishIntroduction"), ((DPObject)localObject4).getInt("LimitNum"), ((DPObject)localObject4).getInt("TotalNum"), ((DPObject)localObject4).getString("Tip"));
            ((TakeawayDishGroup)localObject3).dishes.add(localObject4);
            this.dishInfoList.add(localObject4);
            this.dishInfoMap.put(Integer.valueOf(((TakeawayDishInfo)localObject4).id), localObject4);
            k += 1;
          }
        }
        if (m != 0);
        for (((TakeawayDishGroup)localObject3).maxAmount = m; ; ((TakeawayDishGroup)localObject3).maxAmount = -1)
        {
          this.mCategories.add(new TakeawayMenuCategory((String)localObject2, j));
          if (i - 1 >= 0)
          {
            localObject2 = (TakeawayMenuCategory)this.mCategories.get(i);
            j = ((TakeawayMenuCategory)this.mCategories.get(i - 1)).startLine;
            ((TakeawayMenuCategory)localObject2).startLine = (((TakeawayMenuCategory)this.mCategories.get(i - 1)).amount + j + 1);
          }
          i += 1;
          break;
        }
      }
      if (!this.mCategories.isEmpty())
        this.curCategoryName = ((TakeawayMenuCategory)this.mCategories.get(0)).categoryName;
    }
    Object localObject2 = paramDPObject.getObject("RecommendDishSet");
    if (localObject2 != null)
    {
      paramDPObject = ((DPObject)localObject2).getString("Name");
      k = ((DPObject)localObject2).getInt("MaxCount");
      localObject1 = new TakeawayDishGroup(paramDPObject);
      localObject2 = ((DPObject)localObject2).getArray("List");
      if (localObject2 != null);
      for (i = localObject2.length; ; i = 0)
      {
        this.recomDishInfoList.add(TakeawayDishInfo.Builder.buildTitleItemInfo((TakeawayDishGroup)localObject1, paramDPObject));
        j = 0;
        while (j < i)
        {
          localObject3 = (TakeawayDishInfo)this.dishInfoMap.get(Integer.valueOf(localObject2[j].getInt("ID")));
          if (localObject3 != null)
          {
            ((TakeawayDishGroup)localObject1).dishes.add(localObject3);
            this.recomDishInfoList.add(localObject3);
          }
          j += 1;
        }
      }
      if (k == 0)
        break label686;
    }
    label686: for (((TakeawayDishGroup)localObject1).maxAmount = k; ; ((TakeawayDishGroup)localObject1).maxAmount = -1)
    {
      this.recomCategory = new TakeawayMenuCategory(paramDPObject, i);
      checkCacheAndApplyIt(this.dishInfoList);
      return;
    }
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.shopID));
      localGAUserInfo.query_id = this.queryId;
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      while (true)
        localGAUserInfo.shop_id = Integer.valueOf(0);
    }
  }

  public String[] getUnavailableMessage()
  {
    if (this.mShopInfo.errorMessage != null)
      return this.mShopInfo.errorMessage.split("\\|");
    return null;
  }

  public void loadData()
  {
    if (this.shopDishRequest != null);
    do
    {
      return;
      this.menuCache = TakeawayCarCacheManager.readCacheMenu(this.activity);
      this.errorMsg = null;
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("shopid");
      localArrayList.add(this.shopID);
      if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
      {
        localArrayList.add("lat");
        localArrayList.add(this.lat);
        localArrayList.add("lng");
        localArrayList.add(this.lng);
      }
      while (true)
      {
        if (!TextUtils.isEmpty(this.inputAddress))
        {
          localArrayList.add("address");
          localArrayList.add(this.inputAddress);
        }
        localArrayList.add("cityid");
        localArrayList.add(String.valueOf(this.activity.cityId()));
        localArrayList.add("source");
        localArrayList.add(String.valueOf(this.source));
        this.shopDishRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/dishlist.ta?", (String[])localArrayList.toArray(new String[0]));
        this.activity.mapiService().exec(this.shopDishRequest, this.mapiHandler);
        return;
        Location localLocation = this.activity.location();
        if (localLocation == null)
          break;
        DecimalFormat localDecimalFormat = Location.FMT;
        localArrayList.add("lat");
        localArrayList.add(localDecimalFormat.format(localLocation.latitude()));
        localArrayList.add("lng");
        localArrayList.add(localDecimalFormat.format(localLocation.longitude()));
        localArrayList.add("locatecityid");
        localArrayList.add(String.valueOf(localLocation.city().id()));
      }
    }
    while (this.dataLoadlistener == null);
    this.dataLoadlistener.loadDishFinsh(DataStatus.ERROR_LOCATE, null);
  }

  public void loadSuitableAddress()
  {
    if (this.suitableAddressRequest != null)
      return;
    StringBuilder localStringBuilder1 = new StringBuilder("http://waimai.api.dianping.com/getsuitableaddress.ta?");
    localStringBuilder1.append("shopid=").append(this.shopID);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("&lat=");
    if (TextUtils.isEmpty(this.lat))
    {
      str = "0.0";
      localStringBuilder2.append(str);
      localStringBuilder2 = localStringBuilder1.append("&lng=");
      if (!TextUtils.isEmpty(this.lng))
        break label133;
    }
    label133: for (String str = "0.0"; ; str = this.lng)
    {
      localStringBuilder2.append(str);
      this.suitableAddressRequest = BasicMApiRequest.mapiGet(localStringBuilder1.toString(), CacheType.DISABLED);
      this.activity.mapiService().exec(this.suitableAddressRequest, this.mapiHandler);
      return;
      str = this.lat;
      break;
    }
  }

  public void onDestroy()
  {
    if (this.shopDishRequest != null)
    {
      this.activity.mapiService().abort(this.shopDishRequest, null, true);
      this.shopDishRequest = null;
    }
    if (this.suitableAddressRequest != null)
    {
      this.activity.mapiService().abort(this.suitableAddressRequest, null, true);
      this.suitableAddressRequest = null;
    }
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.source = paramBundle.getInt("source", -1);
    this.shopID = paramBundle.getString("shopid");
    this.queryId = paramBundle.getString("queryid");
    this.inputAddress = paramBundle.getString("address");
    this.lat = paramBundle.getString("lat");
    this.lng = paramBundle.getString("lng");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("source", this.source);
    paramBundle.putString("shopid", this.shopID);
    paramBundle.putString("queryid", this.queryId);
    paramBundle.putString("address", this.inputAddress);
    paramBundle.putString("lat", this.lat);
    paramBundle.putString("lng", this.lng);
  }

  public void reloadData()
  {
    loadData();
    boolean bool = NetworkUtils.isWIFIConnection(this.activity);
    NovaActivity localNovaActivity = this.activity;
    String str2 = this.shopID;
    if (bool);
    for (String str1 = "1"; ; str1 = "2")
    {
      TakeawayGAManager.statistics_takeaway6_dishtips_retryclk(localNovaActivity, str2, str1);
      return;
    }
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadlistener = paramDataLoadListener;
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadDishFinsh(TakeawayDishMenuDataSource.DataStatus paramDataStatus, Object paramObject);

    public abstract void loadDishMenu(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);

    public abstract void loadSuitableAddress(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }

  public static enum DataStatus
  {
    static
    {
      ERROR_NETWORK = new DataStatus("ERROR_NETWORK", 2);
      ERROR_LOCATE = new DataStatus("ERROR_LOCATE", 3);
      ERROR_OUT_OF_RANGE = new DataStatus("ERROR_OUT_OF_RANGE", 4);
      ERROR_UNKNOWN_ADDRESS = new DataStatus("ERROR_UNKNOWN_ADDRESS", 5);
      ERROR_TOAST = new DataStatus("ERROR_TOAST", 6);
      $VALUES = new DataStatus[] { INITIAL, NORMAL, ERROR_NETWORK, ERROR_LOCATE, ERROR_OUT_OF_RANGE, ERROR_UNKNOWN_ADDRESS, ERROR_TOAST };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayDishMenuDataSource
 * JD-Core Version:    0.6.0
 */