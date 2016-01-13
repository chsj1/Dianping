package com.dianping.base.shoplist.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaLoadActivity;
import com.dianping.base.basic.AbstractFilterFragment;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterBarClickListener;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterItemClickListener;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.base.shoplist.data.DataSource.OnDataChangeListener;
import com.dianping.base.shoplist.data.DefaultSearchShopListDataSource;
import com.dianping.base.shoplist.data.LocalAndRegionShopListDataSource;
import com.dianping.base.shoplist.fragment.ShopListFragment;
import com.dianping.base.shoplist.fragment.ShopListFragment.OnShopItemClickListener;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

@Deprecated
public abstract class NShopListActivity extends NovaLoadActivity
  implements DataSource.DataLoader, RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, DataSource.OnDataChangeListener, ShopListFragment.OnShopItemClickListener, AbstractFilterFragment.OnFilterItemClickListener, AbstractFilterFragment.OnFilterBarClickListener, AbstractSearchFragment.OnSearchFragmentListener, CityConfig.SwitchListener
{
  private static final String TAG = NShopListActivity.class.getSimpleName();
  protected static final int TITLE_BAR_TYPE_HOTEL = 1;
  protected static final int TITLE_BAR_TYPE_NON_HOTEL = 0;
  protected DecimalFormat FMT = Location.FMT;
  protected long checkinDate;
  protected long checkoutDate;
  protected AbstractFilterFragment filterFragment;
  protected int firstAcc = 0;
  protected double firstLat = 0.0D;
  protected double firstLng = 0.0D;
  public boolean fromHome;
  protected boolean hasChangedCity;
  public boolean isHotelPlace;
  public boolean isHotelRequest;
  public boolean isHotelShopListFragment;
  public boolean isPlaceSetted;
  private View mLastExtrView;
  protected ButtonSearchBar mSearchBar;
  public boolean mbSearched;
  public boolean needRange;
  protected int paraCategoryId = -1;
  protected int paraCityId = -1;
  protected String paraKeyword;
  protected int paraMaxPrice = -1;
  protected int paraMinPrice = -1;
  protected int paraRange = -1;
  protected int paraRegionId = -1;
  protected int paraShopId;
  protected String paraSortId;
  protected String placeName;
  protected String placeRange;
  protected int placeType;
  protected DefaultSearchShopListDataSource shopListDataSource;
  protected ShopListFragment shopListFragment;
  protected MApiRequest shopRequest;
  protected int tabIndex;
  protected int titleBarType = -1;
  protected int tuanMaxPrice = -1;
  protected int tuanMinPrice = -1;
  public String uriLat;
  public String uriLng;
  protected String value = null;

  private void refreshSearchResult(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    if (paramString.equals("清空搜索记录"))
    {
      new SearchRecentSuggestions(this, "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
      finish();
      return;
    }
    this.paraKeyword = paramString;
    getIntent().putExtra("keyword", paramString);
    new SearchRecentSuggestions(this, "com.dianping.app.DianpingSuggestionProvider", 3).saveRecentQuery(paramString, null);
    if (this.mSearchBar != null)
      this.mSearchBar.setKeyword(paramString);
    this.shopListDataSource.reset(true);
    this.shopListDataSource.reload(false);
  }

  private void startAddShopActivity()
  {
    String str3 = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!";
    String str1 = str3;
    String str4;
    if (getIntent() != null)
    {
      str4 = getIntent().getStringExtra("keyword");
      str1 = str3;
      if (TextUtils.isEmpty(str4));
    }
    try
    {
      str1 = URLEncoder.encode(str4, "UTF-8");
      str1 = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!" + "&shopName=" + str1;
      startActivity(str1);
      return;
    }
    catch (Exception str2)
    {
      while (true)
      {
        localException.printStackTrace();
        String str2 = str3;
      }
    }
  }

  protected DefaultSearchShopListDataSource createDataSource()
  {
    return new DefaultSearchShopListDataSource(this);
  }

  protected MApiRequest createHotelRequest(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("searchshop.api?");
    localStringBuilder.append("start=").append(paramInt);
    localStringBuilder.append("&begindate=").append(this.shopListFragment.checkinTimeMills());
    localStringBuilder.append("&enddate=").append(this.shopListFragment.checkoutTimeMills());
    Object localObject2 = location();
    if ((localObject2 != null) && (this.firstLat == 0.0D) && (this.firstLng == 0.0D))
    {
      this.firstLat = ((Location)localObject2).latitude();
      this.firstLng = ((Location)localObject2).longitude();
      this.firstAcc = ((Location)localObject2).accuracy();
    }
    if ((this.firstLat == 0.0D) && (this.firstLng == 0.0D))
    {
      localStringBuilder.append("&mylat=0");
      localStringBuilder.append("&mylng=0");
    }
    while (true)
    {
      localStringBuilder.append("&myacc=").append(this.firstAcc);
      if (localObject2 != null)
        localStringBuilder.append("&locatecityid=").append(((Location)localObject2).city().id());
      label204: Object localObject1;
      if (this.shopListDataSource.curCategory() == null)
      {
        paramInt = 0;
        if (paramInt > 0)
          localStringBuilder.append("&categoryid=").append(paramInt);
        if (this.shopListDataSource.curFilterId() != null)
          break label717;
        localObject1 = null;
        label232: if (localObject1 != null)
          localStringBuilder.append("&sortid=").append((String)localObject1);
        if (this.tabIndex == 0)
        {
          if (this.paraMinPrice != -1)
            localStringBuilder.append("&minprice=").append(this.paraMinPrice);
          if (this.paraMaxPrice != -1)
            localStringBuilder.append("&maxprice=").append(this.paraMaxPrice);
        }
        if (this.tabIndex == 1)
        {
          if (this.tuanMinPrice != -1)
            localStringBuilder.append("&minprice=").append(this.tuanMinPrice);
          if (this.tuanMaxPrice != -1)
            localStringBuilder.append("&maxprice=").append(this.tuanMaxPrice);
        }
        if (this.tabIndex != 1)
          break label734;
        localStringBuilder.append("&filterid=3");
        if (TextUtils.isEmpty(this.paraKeyword));
      }
      try
      {
        localStringBuilder.append("&keyword=").append(URLEncoder.encode(this.paraKeyword, "UTF-8"));
        this.shopListDataSource.setGATag("keyword");
        label410: if (!TextUtils.isEmpty(this.value))
          localStringBuilder.append("&value=").append(this.value);
        if (getPlaceType() == 1)
        {
          localStringBuilder.append("&islocalsearch=").append(1);
          if (localObject2 != null)
          {
            DecimalFormat localDecimalFormat = Location.FMT;
            localObject1 = localDecimalFormat.format(((Location)localObject2).latitude());
            localObject2 = localDecimalFormat.format(((Location)localObject2).longitude());
            if ((localObject1 == null) || (localObject2 == null))
              break label784;
            localStringBuilder.append("&lat=").append((String)localObject1);
            localStringBuilder.append("&lng=").append((String)localObject2);
            if ((this.shopListDataSource.curRange() != null) && (!TextUtils.isEmpty(this.shopListDataSource.curRange().getString("ID"))))
              localStringBuilder.append("&range=").append(this.shopListDataSource.curRange().getString("ID"));
            localStringBuilder.append("&maptype=0");
            localStringBuilder.append("&cityid=").append(getCityId());
          }
        }
        while (true)
        {
          paramInt = locateAccuracy();
          if (paramInt >= 0)
            localStringBuilder.append("&myacc=" + paramInt);
          return new BasicMApiRequest(localStringBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
          localStringBuilder.append("&mylat=").append(this.FMT.format(this.firstLat));
          localStringBuilder.append("&mylng=").append(this.FMT.format(this.firstLng));
          break;
          paramInt = this.shopListDataSource.curCategory().getInt("ID");
          break label204;
          label717: localObject1 = this.shopListDataSource.curFilterId().getString("ID");
          break label232;
          label734: if (this.shopListDataSource.curSelectNavs() == null);
          for (paramInt = 0; paramInt > 0; paramInt = this.shopListDataSource.curSelectNavs().getInt("FilterId"))
          {
            localStringBuilder.append("&filterid=").append(paramInt);
            break;
          }
          return null;
          label784: return null;
          if (getPlaceType() == 2)
          {
            localObject1 = this.shopListDataSource.curRegion();
            if (localObject1 == null);
            for (paramInt = 0; ; paramInt = ((DPObject)localObject1).getInt("ID"))
            {
              if (paramInt > 0)
                localStringBuilder.append("&regionid=").append(paramInt);
              localStringBuilder.append("&cityid=").append(getCityId());
              break;
            }
          }
          if ((getPlaceType() != 3) && (getPlaceType() != 4))
            break label973;
          if ((this.uriLat != null) && (this.uriLng != null))
          {
            localStringBuilder.append("&lat=").append(this.uriLat);
            localStringBuilder.append("&lng=").append(this.uriLng);
          }
          if (this.shopListDataSource.curRange() != null)
            localStringBuilder.append("&range=").append(this.shopListDataSource.curRange().getString("ID"));
          localStringBuilder.append("&maptype=0");
          localStringBuilder.append("&cityid=").append(getCityId());
        }
        label973: return null;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        break label410;
      }
    }
  }

  protected abstract MApiRequest createRequest(int paramInt);

  protected String customIntentParam()
  {
    return "";
  }

  protected void fetchParametersFromURL()
  {
    Uri localUri = getIntent().getData();
    if (localUri != null);
    try
    {
      Object localObject = localUri.getQueryParameter("categoryid");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        this.paraCategoryId = Integer.parseInt((String)localObject);
      try
      {
        label35: localObject = localUri.getQueryParameter("regionid");
        if (!TextUtils.isEmpty((CharSequence)localObject))
          this.paraRegionId = Integer.parseInt((String)localObject);
        try
        {
          label58: localObject = localUri.getQueryParameter("range");
          if (!TextUtils.isEmpty((CharSequence)localObject))
            this.paraRange = Integer.parseInt((String)localObject);
          try
          {
            label81: localObject = localUri.getQueryParameter("shopid");
            if (!TextUtils.isEmpty((CharSequence)localObject))
              this.paraShopId = Integer.parseInt((String)localObject);
            localObject = localUri.getQueryParameter("cityid");
            if (!TextUtils.isEmpty((CharSequence)localObject))
              this.paraCityId = Integer.parseInt((String)localObject);
            try
            {
              label127: localObject = localUri.getQueryParameter("begindate");
              if (!TextUtils.isEmpty((CharSequence)localObject))
                this.checkinDate = Long.valueOf((String)localObject).longValue();
              localObject = localUri.getQueryParameter("enddate");
              if (!TextUtils.isEmpty((CharSequence)localObject))
                this.checkoutDate = Long.valueOf((String)localObject).longValue();
              label179: this.paraSortId = localUri.getQueryParameter("sort");
              try
              {
                localObject = localUri.getQueryParameter("minprice");
                if (!TextUtils.isEmpty((CharSequence)localObject))
                  this.paraMinPrice = Integer.parseInt((String)localObject);
                try
                {
                  label213: localObject = localUri.getQueryParameter("maxprice");
                  if (!TextUtils.isEmpty((CharSequence)localObject))
                    this.paraMaxPrice = Integer.parseInt((String)localObject);
                  label236: this.paraKeyword = localUri.getQueryParameter("keyword");
                  if (!TextUtils.isEmpty(this.paraKeyword))
                  {
                    getIntent().putExtra("keyword", this.paraKeyword);
                    this.shopListDataSource.setGATag("keyword");
                  }
                  this.uriLat = localUri.getQueryParameter("latitude");
                  this.uriLng = localUri.getQueryParameter("longitude");
                  this.placeName = localUri.getQueryParameter("placename");
                  this.placeRange = localUri.getQueryParameter("placerange");
                  try
                  {
                    localObject = localUri.getQueryParameter("placetype");
                    if (!TextUtils.isEmpty((CharSequence)localObject))
                      this.placeType = Integer.parseInt((String)localObject);
                    try
                    {
                      label346: localObject = localUri.getQueryParameter("isplacesetted");
                      if (!TextUtils.isEmpty((CharSequence)localObject))
                        this.isPlaceSetted = Boolean.parseBoolean((String)localObject);
                      try
                      {
                        label369: localObject = localUri.getQueryParameter("ishotelrequest");
                        if (!TextUtils.isEmpty((CharSequence)localObject))
                          this.isHotelRequest = Boolean.parseBoolean((String)localObject);
                        try
                        {
                          label392: localObject = localUri.getQueryParameter("fromhome");
                          if (!TextUtils.isEmpty((CharSequence)localObject))
                            this.fromHome = Boolean.parseBoolean((String)localObject);
                          label415: if (this.paraCategoryId >= 0)
                          {
                            String str = localUri.getQueryParameter("categoryname");
                            localObject = str;
                            if (str == null)
                              localObject = "";
                            if (this.paraCategoryId == 0)
                              localObject = "全部分类";
                            this.shopListDataSource.setCurCategory(new DPObject("Category").edit().putInt("ID", this.paraCategoryId).putString("Name", (String)localObject).putInt("ParentID", -1).putInt("Distance", 500).generate());
                          }
                          if (this.paraRegionId >= 0)
                          {
                            localObject = localUri.getQueryParameter("regionName");
                            this.shopListDataSource.setCurRegion(new DPObject("Region").edit().putInt("ID", this.paraRegionId).putString("Name", (String)localObject).putInt("ParentID", 0).generate());
                          }
                          if (this.paraRange >= 0)
                            this.shopListDataSource.setCurRange(new DPObject("Pair").edit().putString("ID", String.valueOf(this.paraRange)).putString("Name", "" + this.paraRange + "米").generate());
                          while (true)
                          {
                            if (!TextUtils.isEmpty(this.paraSortId))
                              this.shopListDataSource.setCurFilterId(new DPObject("Pair").edit().putString("ID", this.paraSortId).putString("Name", "").generate());
                            localObject = (DPObject)getIntent().getParcelableExtra("region");
                            if (localObject != null)
                              this.shopListDataSource.setCurRegion((DPObject)localObject);
                            localObject = (DPObject)getIntent().getParcelableExtra("category");
                            if (localObject != null)
                              this.shopListDataSource.setCurCategory((DPObject)localObject);
                            localObject = (DPObject)getIntent().getParcelableExtra("sort");
                            if (localObject != null)
                              this.shopListDataSource.setCurFilterId((DPObject)localObject);
                            return;
                            if (this.paraRange != -1)
                              continue;
                            this.shopListDataSource.setCurRange(new DPObject("Pair").edit().putString("ID", String.valueOf(this.paraRange)).putString("Name", "全城").generate());
                          }
                        }
                        catch (Exception localException1)
                        {
                          break label415;
                        }
                      }
                      catch (Exception localException2)
                      {
                        break label392;
                      }
                    }
                    catch (Exception localException3)
                    {
                      break label369;
                    }
                  }
                  catch (Exception localException4)
                  {
                    break label346;
                  }
                }
                catch (Exception localException5)
                {
                  break label236;
                }
              }
              catch (Exception localException6)
              {
                break label213;
              }
            }
            catch (Exception localException7)
            {
              break label179;
            }
          }
          catch (Exception localException8)
          {
            break label127;
          }
        }
        catch (Exception localException9)
        {
          break label81;
        }
      }
      catch (Exception localException10)
      {
        break label58;
      }
    }
    catch (Exception localException11)
    {
      break label35;
    }
  }

  public abstract void generateFilterFragment();

  protected int getCityId()
  {
    if (this.paraCityId == -1)
      return cityId();
    return this.paraCityId;
  }

  public String getOriginPlaceName()
  {
    return "";
  }

  public String getOriginPlaceRange()
  {
    return "";
  }

  public int getOriginPlaceType()
  {
    return 0;
  }

  public String getParaKeyword()
  {
    return this.paraKeyword;
  }

  public String getPlaceName()
  {
    if (this.isPlaceSetted)
      return this.placeName;
    return getOriginPlaceName();
  }

  public String getPlaceRange()
  {
    if (this.isPlaceSetted)
      return this.placeRange;
    return getOriginPlaceRange();
  }

  public int getPlaceType()
  {
    if (this.isPlaceSetted)
      return this.placeType;
    return getOriginPlaceType();
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.shopRequest != null)
      Log.w(TAG, "already requesting");
    while (true)
    {
      return;
      if (this.isHotelRequest);
      for (this.shopRequest = createHotelRequest(paramInt); this.shopRequest != null; this.shopRequest = createRequest(paramInt))
      {
        if (paramBoolean)
          mapiCacheService().remove(this.shopRequest);
        if (paramInt == 0)
        {
          View localView = findViewById(R.id.filter_view);
          if (localView != null)
            localView.setVisibility(8);
          if (this.rightTitleButton != null)
            this.rightTitleButton.setVisibility(8);
          localView = findViewById(R.id.review_list_frame);
          if (localView != null)
          {
            localView = localView.findViewById(R.id.right_title_button);
            if (localView != null)
              localView.setVisibility(8);
          }
        }
        mapiService().exec(this.shopRequest, this);
        return;
      }
    }
  }

  protected int locateAccuracy()
  {
    Location localLocation = location();
    if (localLocation != null)
      return localLocation.accuracy();
    return -1;
  }

  public void onBackPressed()
  {
    if (this.hasChangedCity)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
      localIntent.addFlags(268468224);
      startActivity(localIntent);
      finish();
      return;
    }
    super.onBackPressed();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    int i;
    if (this.isHotelPlace)
    {
      this.hasChangedCity = true;
      paramCity1 = locationService().city();
      if (paramCity1 == null)
        break label201;
      i = 1;
      if ((i == 0) || (paramCity1.getInt("ID") != cityConfig().currentCity().id()))
        break label206;
      i = 1;
      label54: if (i == 0)
        break label211;
      this.shopListDataSource.setShowDistance(true);
      this.shopListDataSource.setCurRange(new DPObject().edit().putString("ID", "").generate());
      if ((this.shopListDataSource instanceof LocalAndRegionShopListDataSource))
        ((LocalAndRegionShopListDataSource)this.shopListDataSource).setFirst(true);
      this.placeType = 1;
      setPlaceName("我");
      setPlaceRange("");
      this.isPlaceSetted = true;
      ((TextView)findViewById(R.id.place_name)).setText(getPlaceName());
      ((TextView)findViewById(R.id.place_range)).setText(getPlaceRange());
    }
    for (this.needRange = true; ; this.needRange = false)
    {
      this.shopListDataSource.reset(true);
      this.shopListDataSource.reload(true);
      return;
      label201: i = 0;
      break;
      label206: i = 0;
      break label54;
      label211: this.shopListDataSource.setShowDistance(false);
      this.shopListDataSource.setCurRegion(new DPObject().edit().putInt("ID", 0).generate());
      this.placeType = 2;
      setPlaceName("全部商区");
      setPlaceRange("");
      this.isPlaceSetted = true;
      ((TextView)findViewById(R.id.place_name)).setText(getPlaceName());
      ((TextView)findViewById(R.id.place_range)).setText(getPlaceRange());
    }
  }

  public void onClick(View paramView)
  {
  }

  @TargetApi(11)
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (Build.VERSION.SDK_INT >= 11)
      getWindow().setSoftInputMode(48);
    this.shopListDataSource = createDataSource();
    this.shopListDataSource.setDataLoader(this);
    this.shopListDataSource.setLastExtraView(R.layout.add_shop_item, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NShopListActivity.this.startAddShopActivity();
      }
    }
    , this);
    this.shopListDataSource.setEmptyMsg("没有找到任何商户");
    if (paramBundle != null)
      this.shopListDataSource.onRestoreInstanceState(paramBundle);
    this.shopListDataSource.setShowDistance(true);
    paramBundle = location();
    if (paramBundle != null)
      this.shopListDataSource.setOffsetGPS(paramBundle.offsetLatitude(), paramBundle.offsetLongitude());
    fetchParametersFromURL();
    paramBundle = getSupportFragmentManager();
    generateFilterFragment();
    if (this.filterFragment != null)
    {
      this.filterFragment.setOnFilterItemClickListener(this);
      this.filterFragment.setOnFilterBarClickListener(this);
    }
    this.shopListFragment = ((ShopListFragment)paramBundle.findFragmentById(R.id.shop_list_fragment));
    if ((this.checkinDate != 0L) && (this.checkoutDate != 0L))
      this.shopListFragment.setHotelCheckDate(this.checkinDate, this.checkoutDate);
    this.shopListFragment.setShopListDataSource(this.shopListDataSource);
    this.shopListFragment.setOnShopItemClickListener(this);
    cityConfig().addListener(this);
  }

  public void onDataChanged(int paramInt)
  {
    if (paramInt == 1)
      if (this.shopListDataSource.status() == 3)
      {
        if (this.shopListDataSource.shops().isEmpty())
        {
          showError(this.shopListDataSource.errorMsg());
          updateTitle(false);
          return;
        }
      }
      else
      {
        int i = this.shopListDataSource.status();
        DefaultSearchShopListDataSource localDefaultSearchShopListDataSource = this.shopListDataSource;
        if (i == 2)
          updateTitleBar();
      }
    if (paramInt == 10)
    {
      if ((!this.shopListDataSource.isEnd()) || (!this.shopListDataSource.shops().isEmpty()))
        break label113;
      setRightTitleButton(-1, null);
    }
    while (true)
    {
      showContent();
      updateTitle(false);
      return;
      label113: setRightTitleButton(R.drawable.ic_loc_in_map, this);
    }
  }

  protected void onDestroy()
  {
    cityConfig().removeListener(this);
    super.onDestroy();
  }

  public void onFilterBarClick(Object paramObject)
  {
  }

  public void onFilterItemClick(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if ((this.shopListDataSource.setCurCategory(paramDPObject1)) || (this.shopListDataSource.setCurRegion(paramDPObject2)) || (this.shopListDataSource.setCurFilterId(paramDPObject3)))
    {
      this.shopListDataSource.reset(true);
      this.shopListDataSource.reload(false);
    }
  }

  protected void onLeftTitleButtonClicked()
  {
    onBackPressed();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    paramLocationService = location();
    if (paramLocationService != null)
      this.shopListDataSource.setOffsetGPS(paramLocationService.offsetLatitude(), paramLocationService.offsetLongitude());
  }

  public void onPause()
  {
    super.onPause();
    this.shopListDataSource.removeDataChangeListener(this);
    locationService().removeListener(this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      this.shopRequest = null;
      paramMApiRequest = paramMApiResponse.error().toString();
      paramMApiResponse = paramMApiResponse.message();
      if (paramMApiResponse != null)
      {
        Log.i(TAG, paramMApiResponse.toString());
        paramMApiRequest = paramMApiResponse.toString();
      }
      this.shopListDataSource.setError(paramMApiRequest);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      if (!(paramMApiResponse.result() instanceof DPObject))
        break label300;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (this.shopListDataSource == null)
        break label300;
      if (((this.shopListDataSource instanceof LocalAndRegionShopListDataSource)) && (paramMApiRequest.getBoolean("HasSearchDate")) && (!this.isHotelShopListFragment))
        ((LocalAndRegionShopListDataSource)this.shopListDataSource).setFirst(true);
      if ((getPlaceType() != 3) && (getPlaceType() != 4))
        break label289;
      paramMApiResponse = paramMApiRequest.getArray("List");
      DPObject[] arrayOfDPObject = new DPObject[paramMApiResponse.length];
      int i = 0;
      while (i < paramMApiResponse.length)
      {
        arrayOfDPObject[i] = paramMApiResponse[i].edit().putString("placeLat", this.uriLat).putString("placeLng", this.uriLng).putString("placeAddress", this.placeName).generate();
        i += 1;
      }
      this.shopListDataSource.appendShops(paramMApiRequest.edit().putArray("List", arrayOfDPObject).generate());
    }
    while (true)
    {
      if (this.filterFragment != null)
        updateNavs();
      this.shopRequest = null;
      if (!this.shopListDataSource.hasSearchDate())
      {
        paramMApiRequest = findViewById(R.id.filter_view);
        if (paramMApiRequest != null)
          paramMApiRequest.setVisibility(0);
      }
      if (this.rightTitleButton != null)
        this.rightTitleButton.setVisibility(0);
      paramMApiRequest = findViewById(R.id.review_list_frame);
      if (paramMApiRequest != null)
      {
        paramMApiRequest = paramMApiRequest.findViewById(R.id.right_title_button);
        if (paramMApiRequest != null)
          paramMApiRequest.setVisibility(0);
      }
      return;
      label289: this.shopListDataSource.appendShops(paramMApiRequest);
    }
    label300: this.shopListDataSource.setError("错误");
    this.shopRequest = null;
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
  }

  public void onResume()
  {
    super.onResume();
    this.shopListDataSource.addDataChangeListener(this);
    locationService().addListener(this);
    updateTitle(false);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.shopListDataSource.onSaveInstanceState(paramBundle);
  }

  public void onSearchFragmentDetach()
  {
    if (this.titleBarType != 1)
      setTitleVisibility(0);
  }

  public void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject)
  {
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID") + customIntentParam()));
    paramAdapterView.putExtra("shopId", paramDPObject.getInt("ID"));
    paramAdapterView.putExtra("shop", paramDPObject);
    if (this.shopListDataSource.hasSearchDate())
    {
      paramAdapterView.putExtra("hotelBooking", true);
      paramAdapterView.putExtra("checkinTime", this.shopListFragment.checkinTimeMills());
      paramAdapterView.putExtra("checkoutTime", this.shopListFragment.checkoutTimeMills());
    }
    startActivity(paramAdapterView);
  }

  protected void reloadContent()
  {
    this.shopListDataSource.reload(false);
    showLoading(null);
  }

  public void setPlaceName(String paramString)
  {
    this.placeName = paramString;
  }

  public void setPlaceRange(String paramString)
  {
    if ("500".equals(paramString))
    {
      this.placeRange = "附近500m";
      return;
    }
    if ("1000".equals(paramString))
    {
      this.placeRange = "附近1km";
      return;
    }
    if ("2000".equals(paramString))
    {
      this.placeRange = "附近2km";
      return;
    }
    if ("5000".equals(paramString))
    {
      this.placeRange = "附近5km";
      return;
    }
    if ("附近".equals(paramString))
    {
      this.placeRange = "附近";
      return;
    }
    this.placeRange = "";
  }

  public void setPlaceType(int paramInt)
  {
    this.placeType = paramInt;
  }

  protected abstract void setupView();

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.mbSearched = true;
    Object localObject = paramDPObject.getString("Keyword");
    this.value = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(this.value))
    {
      DefaultSearchShopListDataSource localDefaultSearchShopListDataSource1 = this.shopListDataSource;
      DefaultSearchShopListDataSource localDefaultSearchShopListDataSource2 = this.shopListDataSource;
      localDefaultSearchShopListDataSource1.setCurRegion(DefaultSearchShopListDataSource.TOP_REGION);
      setPlaceType(2);
      localDefaultSearchShopListDataSource1 = this.shopListDataSource;
      setPlaceName(DefaultSearchShopListDataSource.TOP_REGION.getString("Name"));
      setPlaceRange("");
    }
    if ((this.mSearchBar != null) && (localObject != null))
      this.mSearchBar.setKeyword((String)localObject);
    localObject = new Bundle();
    ((Bundle)localObject).putBoolean("dontStartResultActivity", true);
    ((Bundle)localObject).putString("source", "com.dianping.action.SHOPLIST");
    refreshSearchResult(paramDPObject.getString("Keyword"));
  }

  protected void switchTitle(boolean paramBoolean)
  {
  }

  protected void updateNavs()
  {
    this.filterFragment.updateNavs(this.shopListDataSource.curCategory(), this.shopListDataSource.curRegion(), this.shopListDataSource.curFilterId());
    this.filterFragment.setNavs(this.shopListDataSource.filterCategories(), this.shopListDataSource.filterRegions(), this.shopListDataSource.filterIds());
  }

  protected void updateTitle(boolean paramBoolean)
  {
    setTitle("商户列表");
    if (paramBoolean)
    {
      setSubtitle("");
      return;
    }
    if (this.shopListDataSource.recordCount() > 0)
    {
      setSubtitle(" 【共" + this.shopListDataSource.recordCount() + "家】");
      return;
    }
    setSubtitle("");
  }

  protected void updateTitleBar()
  {
    if (this.shopListDataSource.hasSearchDate());
    for (int i = 1; ; i = 0)
    {
      if (i != this.titleBarType)
      {
        this.titleBarType = i;
        switchTitle(this.shopListDataSource.hasSearchDate());
      }
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.activity.NShopListActivity
 * JD-Core Version:    0.6.0
 */