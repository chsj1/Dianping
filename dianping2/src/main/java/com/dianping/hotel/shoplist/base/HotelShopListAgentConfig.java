package com.dianping.hotel.shoplist.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.TuanShopAggregationListAdapter;
import com.dianping.base.shoplist.agent.ShopListContentAgent;
import com.dianping.base.shoplist.agent.ShopListLocalBarAgent;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.base.shoplist.util.OTAPriceLoad;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ShopAndDealListItem;
import com.dianping.base.widget.ShopAndDealListItem.OnDealItemClickListener;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.hotel.shoplist.adapt.HotelShopListAdapter;
import com.dianping.hotel.shoplist.agent.HotelShopListContentAgent;
import com.dianping.hotel.shoplist.agent.HotelShopListSearchAgent;
import com.dianping.hotel.shoplist.agent.ShopListHotelBannerAgent;
import com.dianping.hotel.shoplist.agent.ShopListHotelInfoAgent;
import com.dianping.hotel.shoplist.agent.ShopListHotelTitleAgent;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelShopListAgentConfig extends ShopListAgentConfig
{
  private static final String AD_SHOP_LOC_KEY = "__HotelShopListShopLocKey";
  public static final String BANNER = "shoplist/hotelbanner";
  public static final String CONTENT_LIST = "shoplist/contentlist";
  public static final String HOTEL_CONTENT_LIST = "shoplist/hotelcontentlist";
  public static final String HOTEL_INFO = "shoplist/hotelinfo";
  private static final String HOTEL_LIST_REQUEST_URI = "http://m.api.dianping.com/hotelsearch/searchshop.hotel";
  public static final String HOTEL_SEARCH = "shoplist/hotelsearch";
  public static final String HOTEL_TITLE = "shoplist/hoteltitle";
  public static final String LOCAL_BAR = "shoplist/localbar";
  private static final String SHOP_TAG_KEY = "__HotelShopListShopType";
  private static final int TYPE_AD = 2;
  private static final int TYPE_HOTEL = 0;
  private static final int TYPE_SEP = 1;
  private boolean isFromKeywordSearch;
  private HotelTuanShopAggregationListAdapter tuanListAdapter;

  public HotelShopListAgentConfig(AbstractShopListAgentFragment paramAbstractShopListAgentFragment)
  {
    super(paramAbstractShopListAgentFragment);
  }

  private void baseItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = paramAdapterView.getItemAtPosition(paramInt);
    paramAdapterView = paramView;
    if ((paramView instanceof ShopDataModel))
      paramAdapterView = ((ShopDataModel)paramView).shopObj;
    DPObject localDPObject;
    if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).getInt("ID") > 0))
    {
      localDPObject = (DPObject)paramAdapterView;
      startShopInfoActivity(localDPObject);
      new ArrayList().add(new BasicNameValuePair("shopid", String.valueOf(localDPObject.getInt("ID"))));
      if (!localDPObject.getBoolean("IsAdShop"));
    }
    try
    {
      if (localDPObject.getInt("__HotelShopListShopType") == 2)
      {
        AdClientUtils.sendAdGa(localDPObject, "2", String.valueOf(localDPObject.getInt("__HotelShopListShopLocKey")));
        ShopListUtils.logAdGa(localDPObject, "2", String.valueOf(localDPObject.getInt("__HotelShopListShopLocKey")));
      }
      while (true)
      {
        if (this.shopListAgentFragment.getDataSource().hasSearchDate())
        {
          if (localDPObject.getInt("MarketPrice") <= 0)
            break;
          if (localDPObject.getBoolean("HotelBooking"))
          {
            paramView = this.shopListAgentFragment.getDataSource().queryId();
            paramAdapterView = paramView;
            if (localDPObject.getBoolean("IsHotelFull"))
              paramAdapterView = paramView + "|满房";
            new StringBuilder().append(paramAdapterView).append("|").append(localDPObject.getString("HotelPromoTag")).toString();
          }
        }
        return;
        if (localDPObject.getInt("__HotelShopListShopType") != 0)
          continue;
        AdClientUtils.sendAdGa(localDPObject, "2", String.valueOf(localDPObject.getInt("ListPosition") + 1));
        ShopListUtils.logAdGa(localDPObject, "2", String.valueOf(localDPObject.getInt("ListPosition") + 1));
      }
    }
    catch (Exception paramAdapterView)
    {
      while (true)
      {
        continue;
        if (localDPObject.getBoolean("HotelBooking"))
          continue;
      }
    }
  }

  private DPObject createListContainAdShop(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      localObject = null;
    DPObject[] arrayOfDPObject1;
    do
    {
      do
      {
        return localObject;
        arrayOfDPObject1 = paramDPObject.getArray("AdShops");
        localObject = paramDPObject;
      }
      while (arrayOfDPObject1 == null);
      localObject = paramDPObject;
    }
    while (arrayOfDPObject1.length == 0);
    Object localObject = new ArrayList();
    DPObject[] arrayOfDPObject2 = paramDPObject.getArray("List");
    if ((arrayOfDPObject2 != null) && (arrayOfDPObject2.length > 0))
    {
      int j = arrayOfDPObject2.length;
      i = 0;
      while (i < j)
      {
        ((List)localObject).add(arrayOfDPObject2[i].edit().putInt("__HotelShopListShopType", 0).generate());
        i += 1;
      }
      ((List)localObject).add(new DPObject().edit().putInt("__HotelShopListShopType", 1).generate());
    }
    int i = 0;
    while (i < arrayOfDPObject1.length)
    {
      ((List)localObject).add(arrayOfDPObject1[i].edit().putInt("__HotelShopListShopType", 2).putInt("__HotelShopListShopLocKey", i + 1).generate());
      i += 1;
    }
    localObject = (DPObject[])((List)localObject).toArray(new DPObject[((List)localObject).size()]);
    return (DPObject)paramDPObject.edit().putArray("List", localObject).generate();
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/hotelsearch/searchshop.hotel").buildUpon();
    localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("begindate", String.valueOf(this.shopListAgentFragment.getDataSource().hotelCheckinTime()));
    localBuilder.appendQueryParameter("enddate", String.valueOf(this.shopListAgentFragment.getDataSource().hotelCheckoutTime()));
    if (!TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().aroundCitiesSearchValue))
      localBuilder.appendQueryParameter("regionsearchkey", String.valueOf(this.shopListAgentFragment.getDataSource().aroundCitiesSearchValue));
    if (this.shopListAgentFragment.getDataSource().hotelNearbyShopId > 0)
      localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopListAgentFragment.getDataSource().hotelNearbyShopId));
    while (true)
    {
      Location localLocation = location();
      label164: label179: Object localObject;
      if (localLocation == null)
      {
        localBuilder.appendQueryParameter("mylat", "0");
        localBuilder.appendQueryParameter("mylng", "0");
        if (this.shopListAgentFragment.getDataSource().curCategory() != null)
          break label729;
        paramInt = 0;
        if (paramInt > 0)
          localBuilder.appendQueryParameter("categoryid", String.valueOf(paramInt));
        if (localLocation != null)
          localBuilder.appendQueryParameter("locatecityid", String.valueOf(localLocation.city().id()));
        if (this.shopListAgentFragment.getDataSource().curSort() != null)
          break label748;
        localObject = null;
        label234: if (localObject != null)
          localBuilder.appendQueryParameter("sortid", String.valueOf(localObject));
        if (this.shopListAgentFragment.getDataSource().minPrice() != -1)
          localBuilder.appendQueryParameter("minprice", String.valueOf(this.shopListAgentFragment.getDataSource().minPrice()));
        if (this.shopListAgentFragment.getDataSource().maxPrice() != -1)
          localBuilder.appendQueryParameter("maxprice", String.valueOf(this.shopListAgentFragment.getDataSource().maxPrice()));
        if (TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().suggestKeyword()));
      }
      try
      {
        localBuilder.appendQueryParameter("keyword", URLEncoder.encode(this.shopListAgentFragment.getDataSource().suggestKeyword(), "UTF-8"));
        label360: if (this.shopListAgentFragment.getDataSource().hotelTabIndex == 0)
          if (this.shopListAgentFragment.getDataSource().curSelectNav() == null)
          {
            paramInt = 0;
            if (paramInt > 0)
              localBuilder.appendQueryParameter("filterid", String.valueOf(paramInt));
            if (TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().suggestValue()));
          }
        try
        {
          label388: label404: localBuilder.appendQueryParameter("value", URLEncoder.encode(this.shopListAgentFragment.getDataSource().suggestValue(), "UTF-8"));
          label444: if (!TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().searchValue()));
          try
          {
            localBuilder.appendQueryParameter("hotelsearchvalue", URLEncoder.encode(this.shopListAgentFragment.getDataSource().searchValue(), "UTF-8"));
            label484: if (this.shopListAgentFragment.getDataSource() != null)
              localBuilder.appendQueryParameter("searcharoundcities", String.valueOf(this.shopListAgentFragment.getDataSource().searchAroundCities));
            if ((this.shopListAgentFragment.getDataSource().placeType() == 0) && (this.isFromKeywordSearch))
              this.shopListAgentFragment.getDataSource().setPlaceType(2);
            switch (this.shopListAgentFragment.getDataSource().placeType())
            {
            default:
            case 1:
            case 2:
            case 3:
            case 4:
            }
            while (true)
            {
              this.shopListRequest = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
              return this.shopListRequest;
              if (this.shopListAgentFragment.getDataSource().cityId() > 0)
              {
                localBuilder.appendQueryParameter("cityid", String.valueOf(this.shopListAgentFragment.getDataSource().cityId()));
                break;
              }
              localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
              break;
              localBuilder.appendQueryParameter("mylat", Location.FMT.format(localLocation.latitude()));
              localBuilder.appendQueryParameter("mylng", Location.FMT.format(localLocation.longitude()));
              localBuilder.appendQueryParameter("myacc", String.valueOf(localLocation.accuracy()));
              break label164;
              label729: paramInt = this.shopListAgentFragment.getDataSource().curCategory().getInt("ID");
              break label179;
              label748: localObject = this.shopListAgentFragment.getDataSource().curSort().getString("ID");
              break label234;
              paramInt = this.shopListAgentFragment.getDataSource().curSelectNav().getInt("FilterId");
              break label388;
              localBuilder.appendQueryParameter("filterid", "3");
              break label404;
              if ((localLocation != null) && (this.shopListAgentFragment.getDataSource().hotelNearbyShopId <= 0))
              {
                localBuilder.appendQueryParameter("lat", Location.FMT.format(localLocation.latitude()));
                localBuilder.appendQueryParameter("lng", Location.FMT.format(localLocation.longitude()));
              }
              if (this.shopListAgentFragment.getDataSource().curRange() != null)
                localBuilder.appendQueryParameter("range", this.shopListAgentFragment.getDataSource().curRange().getString("ID"));
              while (true)
              {
                localBuilder.appendQueryParameter("maptype", "0");
                localBuilder.appendQueryParameter("islocalsearch", "1");
                break;
                localBuilder.appendQueryParameter("range", "2000");
              }
              localObject = this.shopListAgentFragment.getDataSource().curRegion();
              if (localObject == null);
              for (paramInt = 0; ; paramInt = ((DPObject)localObject).getInt("ID"))
              {
                localBuilder.appendQueryParameter("regionid", String.valueOf(paramInt));
                break;
              }
              if ((this.shopListAgentFragment.getDataSource().customLatitude() != 0.0D) && (this.shopListAgentFragment.getDataSource().customLongitude() != 0.0D))
              {
                localBuilder.appendQueryParameter("lat", Location.FMT.format(this.shopListAgentFragment.getDataSource().customLatitude()));
                localBuilder.appendQueryParameter("lng", Location.FMT.format(this.shopListAgentFragment.getDataSource().customLongitude()));
              }
              if (this.shopListAgentFragment.getDataSource().curRange() != null)
                localBuilder.appendQueryParameter("range", this.shopListAgentFragment.getDataSource().curRange().getString("ID"));
              localBuilder.appendQueryParameter("maptype", "0");
            }
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException1)
          {
            break label484;
          }
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException2)
        {
          break label444;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException3)
      {
        break label360;
      }
    }
  }

  protected boolean fetchListView()
  {
    int i = 0;
    if (this.listView != null)
      i = 1;
    while (true)
    {
      return i;
      try
      {
        Object localObject = this.shopListAgentFragment.findAgent("shoplist/hotelcontentlist");
        if (localObject == null)
          continue;
        this.listView = ((PullToRefreshListView)localObject.getClass().getMethod("getListView", new Class[0]).invoke(localObject, new Object[0]));
        localObject = this.listView;
        if (localObject != null);
        for (i = 1; ; i = 0)
          return i;
      }
      catch (Exception localException)
      {
      }
    }
    return false;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("shoplist/hotelsearch", HotelShopListSearchAgent.class);
    localLinkedHashMap.put("shoplist/hoteltitle", ShopListHotelTitleAgent.class);
    localLinkedHashMap.put("shoplist/hotelbanner", ShopListHotelBannerAgent.class);
    localLinkedHashMap.put("shoplist/hotelinfo", ShopListHotelInfoAgent.class);
    if (Build.VERSION.SDK_INT > 10)
      localLinkedHashMap.put("shoplist/hotelcontentlist", HotelShopListContentAgent.class);
    while (true)
    {
      localLinkedHashMap.put("shoplist/localbar", ShopListLocalBarAgent.class);
      return localLinkedHashMap;
      localLinkedHashMap.put("shoplist/contentlist", ShopListContentAgent.class);
    }
  }

  public String getContentAgentKey()
  {
    return "shoplist/hotelcontentlist";
  }

  public ShopListAdapter getListAdapter(Context paramContext, ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    int i = this.shopListAgentFragment.getDataSource().hotelTabIndex;
    this.shopListAgentFragment.getDataSource().setCurrentTabIndex(i);
    if (i == 0)
    {
      if (this.lastAdapter == null)
        this.lastAdapter = new HotelShopListAdapter(paramShopListReloadHandler);
      return this.lastAdapter;
    }
    if (this.tuanListAdapter == null)
      this.tuanListAdapter = new HotelTuanShopAggregationListAdapter((DPActivity)paramContext, paramShopListReloadHandler);
    return this.tuanListAdapter;
  }

  public AdapterView.OnItemClickListener getOnItemClickListener()
  {
    return super.getOnItemClickListener();
  }

  protected void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    baseItemClick(paramAdapterView, paramView, paramInt, paramLong);
    paramView = new GAUserInfo();
    paramView.query_id = this.shopListAgentFragment.getDataSource().queryId();
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).getInt("ID") > 0))
      paramView.shop_id = Integer.valueOf(((DPObject)paramAdapterView).getInt("ID"));
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.shopListRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      Object localObject = createListContainAdShop(localDPObject);
      this.shopListAgentFragment.getDataSource().appendResult((DPObject)localObject);
      if ((localDPObject != null) && (localDPObject.getArray("List") != null))
      {
        localObject = this.shopListAgentFragment.getDataSource();
        List localList = Arrays.asList(localDPObject.getArray("List"));
        if (localList.size() > 0)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          int i = 0;
          while (i < localList.size())
          {
            localStringBuilder.append(((DPObject)localList.get(i)).getInt("ID")).append(",");
            i += 1;
          }
          if (localObject != null)
          {
            if (((BaseShopListDataSource)localObject).hotelPricesLoad == null)
              ((BaseShopListDataSource)localObject).hotelPricesLoad = new OTAPriceLoad();
            if (localStringBuilder.length() > 0)
            {
              localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
              ((BaseShopListDataSource)localObject).hotelPricesLoad.requestOTAPrices(localStringBuilder.toString(), cityId(), this.shopListAgentFragment.getDataSource().hotelCheckinTime(), this.shopListAgentFragment.getDataSource().hotelCheckoutTime());
            }
          }
        }
        ((BaseShopListDataSource)localObject).moreHotelsEntrance = localDPObject.getObject("MoreHotelsEntrance");
        ((BaseShopListDataSource)localObject).selectedDeal = localDPObject.getObject("SelectedDeal");
        ((BaseShopListDataSource)localObject).selectedListUrl = localDPObject.getString("SelectedListUrl");
        ((BaseShopListDataSource)localObject).operatingLocation = localDPObject.getArray("OperatingLocation");
      }
    }
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
  }

  public void parseExtraUrl(Activity paramActivity, BaseShopListDataSource paramBaseShopListDataSource)
  {
    if ((paramActivity == null) || (paramBaseShopListDataSource == null));
    do
    {
      return;
      paramActivity = paramActivity.getIntent().getData();
    }
    while (paramActivity == null);
    if (this.shopListAgentFragment.isCurrentCity())
    {
      paramBaseShopListDataSource.setShowDistance(true);
      paramBaseShopListDataSource.setNeedLocalRegion(true);
    }
    while (true)
    {
      String str = paramActivity.getQueryParameter("shopid");
      if ((!TextUtils.isEmpty(str)) && (TextUtils.isDigitsOnly(str)))
        paramBaseShopListDataSource.hotelNearbyShopId = Integer.parseInt(str);
      str = paramActivity.getQueryParameter("cityid");
      if ((!TextUtils.isEmpty(str)) && (TextUtils.isDigitsOnly(str)))
        paramBaseShopListDataSource.setCityId(Integer.parseInt(str));
      str = paramActivity.getQueryParameter("value");
      if (!TextUtils.isEmpty(str))
        paramBaseShopListDataSource.setSuggestValue(str);
      str = paramActivity.getQueryParameter("searchValue");
      if (!TextUtils.isEmpty(str))
        paramBaseShopListDataSource.setSearchValue(str);
      str = paramActivity.getQueryParameter("placetype");
      if ((!TextUtils.isEmpty(str)) && (TextUtils.isDigitsOnly(str)))
        paramBaseShopListDataSource.setPlaceType(Integer.parseInt(str));
      str = paramActivity.getQueryParameter("hoteltab");
      if ((!TextUtils.isEmpty(str)) && (TextUtils.isDigitsOnly(str)))
      {
        paramBaseShopListDataSource.setCurrentTabIndex(Integer.parseInt(str));
        paramBaseShopListDataSource.hotelTabIndex = Integer.parseInt(str);
      }
      str = paramActivity.getQueryParameter("searcharoundcities");
      if (!TextUtils.isEmpty(str))
        paramBaseShopListDataSource.searchAroundCities = Boolean.parseBoolean(str);
      str = paramActivity.getQueryParameter("regionsearchkey");
      if (!TextUtils.isEmpty(str))
        paramBaseShopListDataSource.aroundCitiesSearchValue = str;
      if (location() != null)
        paramBaseShopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
      paramActivity = paramActivity.getQueryParameter("isFromSearch");
      if ((TextUtils.isEmpty(paramActivity)) || (!paramActivity.equals("true")))
        break;
      this.isFromKeywordSearch = true;
      return;
      paramBaseShopListDataSource.setShowDistance(false);
      paramBaseShopListDataSource.setNeedLocalRegion(false);
    }
  }

  public boolean shouldShow()
  {
    int j = 0;
    Object localObject = this.shopListAgentFragment.getDataSource();
    if (localObject == null);
    do
    {
      return false;
      if (((BaseShopListDataSource)localObject).hasSearchDate())
        return true;
      localObject = ((BaseShopListDataSource)localObject).curCategory();
    }
    while (localObject == null);
    int i = ((DPObject)localObject).getInt("ID");
    if ((i == 60) || (i == 168) || (i == 169) || (i == 170) || (i == 171) || (i == 172) || (i == 173) || (i == 174) || (i == 6714) || (i == 6693) || (i == 25842))
      j = 1;
    return j;
  }

  protected void startShopInfoActivity(DPObject paramDPObject)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
    localIntent.putExtra("shopId", paramDPObject.getInt("ID"));
    localIntent.putExtra("shop", paramDPObject);
    if (paramDPObject.getBoolean("IsAdShop"))
    {
      paramDPObject = paramDPObject.getString("ExtraJson");
      if (TextUtils.isEmpty(paramDPObject));
    }
    try
    {
      paramDPObject = new JSONObject(paramDPObject).optString("Feedback");
      if (!TextUtils.isEmpty(paramDPObject))
        localIntent.putExtra("_fb_", paramDPObject);
      localIntent.putExtra("query_id", this.shopListAgentFragment.getDataSource().queryId());
      if (this.shopListAgentFragment.getDataSource().hasSearchDate())
      {
        localIntent.putExtra("checkinTime", this.shopListAgentFragment.getDataSource().hotelCheckinTime());
        localIntent.putExtra("checkoutTime", this.shopListAgentFragment.getDataSource().hotelCheckoutTime());
      }
      this.shopListAgentFragment.startActivityForResult(localIntent, 113);
      return;
    }
    catch (JSONException paramDPObject)
    {
      while (true)
        paramDPObject.printStackTrace();
    }
  }

  private class HotelTuanShopAggregationListAdapter extends TuanShopAggregationListAdapter
  {
    protected final ShopAndDealListItem.OnDealItemClickListener onDealItemClickListener = new ShopAndDealListItem.OnDealItemClickListener()
    {
      public void onDealItemClick(DPObject paramDPObject1, DPObject paramDPObject2, int paramInt1, int paramInt2)
      {
        Object localObject1 = paramDPObject1.getObject("HotelDealList");
        if (localObject1 != null)
          localObject1 = ((DPObject)localObject1).getArray("List");
        while (true)
        {
          Object localObject3 = null;
          Object localObject4 = localObject3;
          if (localObject1 != null)
            localObject4 = localObject3;
          try
          {
            if (localObject1.length > paramInt2)
              localObject4 = localObject1[paramInt2].getString("DetailUrl");
            Object localObject7 = null;
            Object localObject5 = null;
            localObject1 = localObject5;
            localObject3 = localObject4;
            DPObject[] arrayOfDPObject;
            label102: Object localObject6;
            if (TextUtils.isEmpty((CharSequence)localObject4))
            {
              localObject1 = paramDPObject1.getObject("Deals");
              if (localObject1 == null)
                break label486;
              arrayOfDPObject = ((DPObject)localObject1).getArray("List");
              localObject6 = null;
              localObject1 = localObject5;
              localObject3 = localObject6;
              if (arrayOfDPObject != null)
              {
                localObject1 = localObject5;
                localObject3 = localObject6;
                localObject4 = localObject7;
              }
            }
            try
            {
              if (arrayOfDPObject.length > paramInt2)
              {
                localObject1 = arrayOfDPObject[paramInt2];
                localObject4 = localObject1;
                localObject3 = ((DPObject)localObject1).getString("DetailUrl");
              }
              if (!TextUtils.isEmpty((CharSequence)localObject3))
              {
                localObject4 = new GAUserInfo();
                ((GAUserInfo)localObject4).query_id = HotelShopListAgentConfig.this.shopListAgentFragment.getDataSource().queryId();
                ((GAUserInfo)localObject4).shop_id = Integer.valueOf(paramDPObject1.getInt("ID"));
                ((GAUserInfo)localObject4).utm = (paramInt2 + "");
                if (localObject1 != null)
                  ((GAUserInfo)localObject4).butag = Integer.valueOf(((DPObject)localObject1).getInt("DealID"));
                GAHelper.instance().setGAPageName("shoptglist");
                GAHelper.instance().contextStatisticsEvent(HotelShopListAgentConfig.this.shopListAgentFragment.getActivity(), "jiudian_item", (GAUserInfo)localObject4, "tap");
                localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode((String)localObject3)));
                HotelShopListAgentConfig.HotelTuanShopAggregationListAdapter.this.dpActivity.startActivity((Intent)localObject1);
              }
              while (true)
              {
                localObject1 = new ArrayList();
                ((ArrayList)localObject1).add(new BasicNameValuePair("queryid", HotelShopListAgentConfig.HotelTuanShopAggregationListAdapter.this.queryId()));
                ((ArrayList)localObject1).add(new BasicNameValuePair("rowindex", paramInt1 + "-" + paramInt2));
                ((ArrayList)localObject1).add(new BasicNameValuePair("groupid", String.valueOf(paramDPObject2.getInt("ID"))));
                ((ArrayList)localObject1).add(new BasicNameValuePair("shopid", String.valueOf(paramDPObject1.getInt("ID"))));
                ((ArrayList)localObject1).add(new BasicNameValuePair("dealid", String.valueOf(paramDPObject2.getInt("DealID"))));
                return;
                localObject1 = null;
                break;
                label486: arrayOfDPObject = null;
                break label102;
                localObject3 = new GAUserInfo();
                ((GAUserInfo)localObject3).query_id = HotelShopListAgentConfig.this.shopListAgentFragment.getDataSource().queryId();
                ((GAUserInfo)localObject3).shop_id = Integer.valueOf(paramDPObject1.getInt("ID"));
                ((GAUserInfo)localObject3).utm = (paramInt2 + "");
                if (localObject1 != null)
                  ((GAUserInfo)localObject3).dealgroup_id = Integer.valueOf(((DPObject)localObject1).getInt("ID"));
                GAHelper.instance().setGAPageName("shoptglist");
                GAHelper.instance().contextStatisticsEvent(HotelShopListAgentConfig.this.shopListAgentFragment.getActivity(), "item", (GAUserInfo)localObject3, "tap");
                localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
                ((Intent)localObject1).putExtra("deal", paramDPObject2);
                HotelShopListAgentConfig.HotelTuanShopAggregationListAdapter.this.dpActivity.startActivity((Intent)localObject1);
              }
            }
            catch (Exception localObject2)
            {
              while (true)
              {
                Object localObject2 = localObject4;
                localObject3 = localObject6;
              }
            }
          }
          catch (Exception localException2)
          {
            while (true)
              localObject4 = localObject3;
          }
        }
      }
    };

    public HotelTuanShopAggregationListAdapter(DPActivity paramShopListReloadHandler, ShopListAdapter.ShopListReloadHandler arg3)
    {
      super(localShopListReloadHandler);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject1, "Shop"))
      {
        DPObject localDPObject = (DPObject)localObject1;
        localObject1 = localDPObject.getObject("Deals");
        int i;
        label50: Object localObject2;
        GAUserInfo localGAUserInfo;
        if (localObject1 != null)
        {
          localObject1 = ((DPObject)localObject1).getArray("List");
          i = 0;
          if (i >= localObject1.length)
            break label284;
          if ((ShopAndDealListItem.records.contains(Integer.valueOf(localDPObject.getInt("ID")))) || (i < 2))
          {
            localObject2 = localObject1[i];
            String str = localObject2.getString("DetailUrl");
            localGAUserInfo = new GAUserInfo();
            localGAUserInfo.query_id = HotelShopListAgentConfig.this.shopListAgentFragment.getDataSource().queryId();
            localGAUserInfo.shop_id = Integer.valueOf(localDPObject.getInt("ID"));
            localGAUserInfo.utm = (i + "");
            GAHelper.instance().setGAPageName("shoptglist");
            if (!TextUtils.isEmpty(str))
              break label239;
            if (localObject2 != null)
              localGAUserInfo.dealgroup_id = Integer.valueOf(localObject2.getInt("ID"));
            GAHelper.instance().contextStatisticsEvent(HotelShopListAgentConfig.this.shopListAgentFragment.getActivity(), "jiudian_item", localGAUserInfo, "view");
          }
        }
        while (true)
        {
          i += 1;
          break label50;
          localObject1 = null;
          break;
          label239: if (localObject2 != null)
            localGAUserInfo.butag = Integer.valueOf(localObject2.getInt("DealID"));
          GAHelper.instance().contextStatisticsEvent(HotelShopListAgentConfig.this.shopListAgentFragment.getActivity(), "item", localGAUserInfo, "view");
        }
        label284: localDPObject = new DPObject("ShopAggDealGroup").edit().putObject("Shop", localDPObject).putArray("Deals", localObject1).generate();
        if ((paramView instanceof ShopAndDealListItem));
        for (paramView = (ShopAndDealListItem)paramView; ; paramView = null)
        {
          localObject1 = paramView;
          if (paramView == null)
          {
            localObject1 = (ShopAndDealListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_and_deal_list_item, paramViewGroup, false);
            ((ShopAndDealListItem)localObject1).setOnShopItemClickListener(this.onShopClickListener);
            ((ShopAndDealListItem)localObject1).setOnDealItemClickListener(this.onDealItemClickListener);
          }
          ((ShopAndDealListItem)localObject1).setShopAggDealGroup(localDPObject, this.offsetLatitude, this.offsetLongitude, this.shouldShowImage, paramInt, "tuan5_shopjuhe_expand");
          ((ShopAndDealListItem)localObject1).setMaxDealCount(2);
          return localObject1;
        }
      }
      if (localObject1 == LOADING)
      {
        if ((this.shopListDataSource == null) || (this.shopListDataSource.nextStartIndex() != 0))
          this.reloadHandler.reload(false);
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject1 == LAST_EXTRA)
        return this.lastExtraView;
      if (localObject1 == EMPTY)
        return getShopEmptyView(paramViewGroup, paramView);
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          HotelShopListAgentConfig.HotelTuanShopAggregationListAdapter.this.reloadHandler.reload(false);
        }
      }
      , paramViewGroup, paramView);
    }

    public void setShopList(BaseShopListDataSource paramBaseShopListDataSource)
    {
      LinkedList localLinkedList2 = ShopAndDealListItem.records;
      LinkedList localLinkedList1 = null;
      if (!localLinkedList2.isEmpty())
        localLinkedList1 = new LinkedList(localLinkedList2);
      super.setShopList(paramBaseShopListDataSource);
      if (localLinkedList1 != null)
        localLinkedList2.addAll(localLinkedList1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.base.HotelShopListAgentConfig
 * JD-Core Version:    0.6.0
 */