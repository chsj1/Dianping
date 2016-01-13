package com.dianping.hotel.shoplist.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.agent.ShopListBaseAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.content.CityUtils;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ShopListHotelInfoAgent extends ShopListBaseAgent
  implements FilterBar.OnItemClickListener, CityConfig.SwitchListener
{
  public static final DPObject ALL_CATEGORY = new DPObject("Category").edit().putInt("ID", 0).putString("Name", "全部分类").putInt("ParentID", 0).generate();
  public static final DPObject ALL_REGION;
  public static final String CELL_HOTEL_FILTER = "030HotelFilter";
  public static final DPObject DEFAULT_SORT = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "智能排序").putInt("Type", 3).generate();
  private static final int REQUEST_HOTEL_BOOKING = 111;
  private static final int REQUEST_HOTEL_PLACE = 112;
  public static final int REQUEST_HOTE_SHOP_INFO = 113;
  private static final SimpleDateFormat SDF;
  private static final String URL_LOCATION_TAG = "UrlLocTag";
  private BaseShopListDataSource dataSource;
  private FilterBar filterBar1;
  private FilterBar filterBar2;
  final FilterDialog.OnFilterListener filterListener = new FilterDialog.OnFilterListener()
  {
    public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
    {
      if ("category".equals(paramFilterDialog.getTag()))
        if (ShopListHotelInfoAgent.this.dataSource.filterCategories() != null);
      while (true)
      {
        do
          return;
        while (!(paramObject instanceof DPObject));
        ShopListHotelInfoAgent.this.dataSource.setCurCategory((DPObject)paramObject);
        ShopListHotelInfoAgent.this.updateNavs();
        if (!"sort".equals(paramFilterDialog.getTag()))
          break;
        if ((ShopListHotelInfoAgent.this.dataSource.filterSorts() == null) || (!(paramObject instanceof DPObject)))
          continue;
        ShopListHotelInfoAgent.this.dataSource.setCurSort((DPObject)paramObject);
        if (!ShopListHotelInfoAgent.this.checkFilterable((DPObject)paramObject))
        {
          paramFilterDialog.dismiss();
          return;
        }
        ShopListHotelInfoAgent.this.updateNavs();
      }
      paramFilterDialog.dismiss();
      ShopListHotelInfoAgent.this.onFilterItemClick(paramFilterDialog.getTag());
      ShopListHotelInfoAgent.this.dataSource.reset(true);
      ShopListHotelInfoAgent.this.dataSource.reload(false);
    }
  };
  private boolean hasSelectedCategory;
  private int[] hotelRegionSelectedIndexPath;
  private PullToRefreshListView listView;
  private TextView placeNameAndRangeView;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.HOTEL_BOOKING_TIME_CHANGE".equals(paramIntent.getAction()))
      {
        long l1 = paramIntent.getLongExtra("checkin_time", ShopListHotelInfoAgent.this.dataSource.hotelCheckinTime());
        long l2 = paramIntent.getLongExtra("checkout_time", ShopListHotelInfoAgent.this.dataSource.hotelCheckinTime());
        ShopListHotelInfoAgent.this.dataSource.setHotelCheckinTime(l1);
        ShopListHotelInfoAgent.this.dataSource.setHotelCheckoutTime(l2);
        ShopListHotelInfoAgent.this.updateHotelDateView();
      }
    }
  };
  private boolean searchAroundCities;
  private View searchDateHeaderView;

  static
  {
    ALL_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).generate();
    SDF = new SimpleDateFormat("MM-dd");
  }

  public ShopListHotelInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private String formatRange(DPObject paramDPObject)
  {
    boolean bool = paramDPObject.getBoolean("UrlLocTag");
    paramDPObject = paramDPObject.getString("ID");
    if ("500".equals(paramDPObject))
      return "500m";
    if ("1000".equals(paramDPObject))
      return "1km";
    if ("2000".equals(paramDPObject))
    {
      if (bool)
        return "2km";
      return "我附近";
    }
    if ("5000".equals(paramDPObject))
      return "5km";
    if ("-1".equals(paramDPObject))
      return "我附近";
    return "";
  }

  private void onFilterBarClick(Object paramObject)
  {
    if ("category".equals(paramObject))
      if (!TextUtils.isEmpty(this.dataSource.suggestKeyword()))
        statisticsEvent("hotelkwlist5", "hotelkwlist5_category_click", this.dataSource.curCategory().getString("Name"), 0);
    do
    {
      return;
      statisticsEvent("hotellist5", "hotellist5_category_click", this.dataSource.curCategory().getString("Name"), 0);
      return;
    }
    while (!"sort".equals(paramObject));
    if (!TextUtils.isEmpty(this.dataSource.suggestKeyword()))
    {
      statisticsEvent("hotelkwlist5", "hotelkwlist5_sort_click", this.dataSource.curSort().getString("Name"), 0);
      return;
    }
    statisticsEvent("hotellist5", "hotellist5_sort_click", this.dataSource.curSort().getString("Name"), 0);
  }

  private void onFilterItemClick(Object paramObject)
  {
    if ("category".equals(paramObject))
    {
      if (!TextUtils.isEmpty(this.dataSource.suggestKeyword()))
      {
        statisticsEvent("hotelkwlist5", "hotelkwlist5_category", this.dataSource.curCategory().getString("Name"), 0);
        GAUserInfo localGAUserInfo = new GAUserInfo();
        localGAUserInfo.category_id = Integer.valueOf(this.dataSource.curCategory().getInt("ID"));
        localGAUserInfo.query_id = this.dataSource.queryId();
        GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_category", localGAUserInfo, "tap");
      }
    }
    else if ("sort".equals(paramObject))
    {
      if (TextUtils.isEmpty(this.dataSource.suggestKeyword()))
        break label274;
      statisticsEvent("hotelkwlist5", "hotelkwlist5_sort", this.dataSource.curSort().getString("Name"), 0);
    }
    while (true)
    {
      paramObject = new GAUserInfo();
      paramObject.category_id = Integer.valueOf(this.dataSource.curCategory().getInt("ID"));
      paramObject.query_id = this.dataSource.queryId();
      paramObject.sort_id = Integer.valueOf(this.dataSource.curSort().getInt("ID"));
      GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_sort", paramObject, "tap");
      return;
      statisticsEvent("hotellist5", "hotellist5_category", this.dataSource.curCategory().getString("Name"), 0);
      if (!this.dataSource.isFromHome())
        break;
      statisticsEvent("hotellist5", "hotellist5_category_nearby", this.dataSource.curCategory().getString("Name"), 0);
      break;
      label274: statisticsEvent("hotellist5", "hotellist5_sort", this.dataSource.curSort().getString("Name"), 0);
      if (!this.dataSource.isFromHome())
        continue;
      statisticsEvent("hotellist5", "hotellist5_sort_nearby", this.dataSource.curSort().getString("Name"), 0);
    }
  }

  private void setupFilterBar()
  {
    this.filterBar1 = ((FilterBar)this.searchDateHeaderView.findViewById(R.id.hotel_filter1));
    this.filterBar2 = ((FilterBar)this.searchDateHeaderView.findViewById(R.id.hotel_filter2));
    Object localObject2;
    if ((this.dataSource.curCategory() != null) && (this.dataSource.curCategory().getInt("ID") == 60))
    {
      localObject1 = this.filterBar1.addItem("category", "星级分类");
      localObject2 = (TextView)((NovaLinearLayout)localObject1).findViewById(16908308);
    }
    for (Object localObject1 = (ImageView)((NovaLinearLayout)localObject1).findViewById(R.id.btn_filter_img); ; localObject1 = (ImageView)((NovaLinearLayout)localObject1).findViewById(R.id.btn_filter_img))
    {
      ((TextView)localObject2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      localLayoutParams.weight = 1.0F;
      ((TextView)localObject2).setLayoutParams(localLayoutParams);
      localObject2 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 3.0F);
      ((LinearLayout.LayoutParams)localObject2).rightMargin = ViewUtils.dip2px(getContext(), 6.0F);
      ((LinearLayout.LayoutParams)localObject2).weight = 0.0F;
      ((ImageView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((TextView)this.filterBar2.addItem("sort", "智能排序").findViewById(16908308)).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      this.filterBar1.setOnItemClickListener(this);
      this.filterBar2.setOnItemClickListener(this);
      return;
      localObject1 = this.filterBar1.addItem("category", this.dataSource.curCategory().getString("Name"));
      localObject2 = (TextView)((NovaLinearLayout)localObject1).findViewById(16908308);
    }
  }

  private void setupPlaceAndDate()
  {
    3 local3 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopListHotelInfoAgent.this.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingpicktime?checkin_time=" + ShopListHotelInfoAgent.this.dataSource.hotelCheckinTime() + "&checkout_time=" + ShopListHotelInfoAgent.this.dataSource.hotelCheckoutTime())), 111);
        ShopListHotelInfoAgent.this.getActivity().overridePendingTransition(R.anim.popup_up_in, R.anim.booking_push_up_out);
        if (!TextUtils.isEmpty(ShopListHotelInfoAgent.this.dataSource.suggestKeyword()))
          ShopListHotelInfoAgent.this.statisticsEvent("hotelkwlist5", "hotelkwlist5_hoteldate_click", "", 0);
        while (true)
        {
          GAHelper.instance().contextStatisticsEvent(ShopListHotelInfoAgent.this.getContext(), "hotellist_date_click", null, "tap");
          return;
          ShopListHotelInfoAgent.this.statisticsEvent("hotellist5", "hotellist5_hoteldate_click", "", 0);
        }
      }
    };
    4 local4 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopListHotelInfoAgent.access$302(ShopListHotelInfoAgent.this, false);
        try
        {
          paramView = ShopListHotelInfoAgent.this.getActivity().getIntent().getData().getQueryParameter("searcharoundcities");
          if (!TextUtils.isEmpty(paramView))
            ShopListHotelInfoAgent.access$302(ShopListHotelInfoAgent.this, Boolean.parseBoolean(paramView));
          label47: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelregion?searcharoundcities=" + ShopListHotelInfoAgent.this.searchAroundCities));
          ArrayList localArrayList = new ArrayList(Arrays.asList(ShopListHotelInfoAgent.this.dataSource.filterRanges()));
          if ((localArrayList.size() > 0) && (localArrayList.get(0) != null) && ("-1".equals(((DPObject)localArrayList.get(0)).getString("ID"))))
            localArrayList.remove(0);
          paramView.putExtra("HOTEL_REGION_SelectedIndexPath", ShopListHotelInfoAgent.this.hotelRegionSelectedIndexPath);
          paramView.putExtra("rangeNavs", localArrayList);
          if (ShopListHotelInfoAgent.this.dataSource.placeType() == 1)
            paramView.putExtra("curRange", ShopListHotelInfoAgent.this.dataSource.curRange());
          ShopListHotelInfoAgent.this.startActivityForResult(paramView, 112);
          ShopListHotelInfoAgent.this.getActivity().overridePendingTransition(R.anim.popup_up_in, R.anim.booking_push_up_out);
          if (!TextUtils.isEmpty(ShopListHotelInfoAgent.this.dataSource.suggestKeyword()))
            ShopListHotelInfoAgent.this.statisticsEvent("hotelkwlist5", "hotelkwlist5_area_click", ShopListHotelInfoAgent.this.placeNameAndRangeView.getText().toString(), 0);
          while (true)
          {
            GAHelper.instance().contextStatisticsEvent(ShopListHotelInfoAgent.this.getContext(), "hotellist_area_click", null, "tap");
            return;
            ShopListHotelInfoAgent.this.statisticsEvent("hotellist5", "hotellist5_area_click", ShopListHotelInfoAgent.this.placeNameAndRangeView.getText().toString(), 0);
          }
        }
        catch (Exception paramView)
        {
          break label47;
        }
      }
    };
    this.searchDateHeaderView.findViewById(R.id.hotel_date_layout).setOnClickListener(local3);
    this.searchDateHeaderView.findViewById(R.id.hotel_place_layout).setOnClickListener(local4);
  }

  private void setupSearchDateHeader()
  {
    this.searchDateHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.shoplist_hotel_header_view, getParentView(), false);
    this.placeNameAndRangeView = ((TextView)this.searchDateHeaderView.findViewById(R.id.place_name_range));
    addCell("030HotelFilter", this.searchDateHeaderView);
    this.dataSource.setCurSelectNav(null);
    this.dataSource.setMinPrice(-1);
    this.dataSource.setMaxPrice(-1);
    setupPlaceAndDate();
    setupFilterBar();
    updateHotelPlaceView();
    updateHotelDateView();
  }

  private void updateHotelBookingTime(Intent paramIntent)
  {
    if (paramIntent != null)
    {
      long l1 = paramIntent.getLongExtra("checkin_time", System.currentTimeMillis());
      long l2 = paramIntent.getLongExtra("checkout_time", 86400000L + l1);
      if ((l1 != this.dataSource.hotelCheckinTime()) || (l2 != this.dataSource.hotelCheckoutTime()))
      {
        this.dataSource.setHotelCheckinTime(l1);
        this.dataSource.setHotelCheckoutTime(l2);
        updateHotelDateView();
        this.dataSource.reset(true);
        this.dataSource.reload(true);
      }
    }
  }

  private void updateHotelDateView()
  {
    if (this.searchDateHeaderView != null)
    {
      ((TextView)this.searchDateHeaderView.findViewById(16908308)).setText("入住 " + SDF.format(Long.valueOf(this.dataSource.hotelCheckinTime())));
      ((TextView)this.searchDateHeaderView.findViewById(16908309)).setText("退房 " + SDF.format(Long.valueOf(this.dataSource.hotelCheckoutTime())));
    }
  }

  private void updateHotelPlaceView()
  {
    boolean bool3 = false;
    try
    {
      String str1 = getActivity().getIntent().getData().getQueryParameter("searcharoundcities");
      bool2 = bool3;
      if (!TextUtils.isEmpty(str1))
        bool2 = Boolean.parseBoolean(str1);
      try
      {
        str1 = getActivity().getIntent().getData().getQueryParameter("isFromSearch");
        if ((!TextUtils.isEmpty(str1)) && (str1.equals("true")))
        {
          this.placeNameAndRangeView.setText("全城");
          this.dataSource.setPlaceType(2);
          return;
        }
      }
      catch (Exception localObject1)
      {
        String str2;
        Object localObject2;
        label138: boolean bool1;
        if (!isCurrentCity())
        {
          str2 = "";
          if ((getDataSource() != null) && (getDataSource().cityId() > 0))
          {
            localObject2 = CityUtils.getCityById(getDataSource().cityId());
            if (localObject2 != null)
              str2 = ((City)localObject2).name();
            localObject2 = this.placeNameAndRangeView;
            if (TextUtils.isEmpty(str2))
              break label271;
            bool1 = true;
            label152: if (!(bool1 & bool2))
              break label276;
            label159: ((TextView)localObject2).setText(str2);
            this.dataSource.setPlaceType(2);
            if ((this.dataSource.curRange() == null) || (!isCurrentCity()))
              break label326;
            this.dataSource.setPlaceType(1);
            localObject2 = this.placeNameAndRangeView;
            if (bool2)
              break label319;
            str2 = formatRange(this.dataSource.curRange());
            label219: ((TextView)localObject2).setText(str2);
          }
        }
        while (true)
        {
          try
          {
            str2 = getActivity().getIntent().getData().getQueryParameter("shopname");
            if (TextUtils.isEmpty(str2))
              break;
            this.placeNameAndRangeView.setText(str2);
            return;
          }
          catch (Exception localException2)
          {
            return;
          }
          if (!bool2)
            break label138;
          Object localObject1 = "周边目的地";
          break label138;
          label271: bool1 = false;
          break label152;
          label276: localObject1 = "全城";
          break label159;
          localObject2 = this.placeNameAndRangeView;
          if (!bool2);
          for (localObject1 = "我附近"; ; localObject1 = "周边目的地")
          {
            ((TextView)localObject2).setText((CharSequence)localObject1);
            this.dataSource.setPlaceType(1);
            break;
          }
          label319: localObject1 = "周边目的地";
          break label219;
          label326: if (this.dataSource.curRegion() == null)
            continue;
          this.dataSource.setPlaceType(2);
          if (!TextUtils.isEmpty(this.dataSource.curRegion().getString("Name")))
          {
            if (this.dataSource.curRegion().getString("Name").contains("附近"))
            {
              localObject2 = this.placeNameAndRangeView;
              if (!bool2);
              for (localObject1 = "我附近"; ; localObject1 = "周边目的地")
              {
                ((TextView)localObject2).setText((CharSequence)localObject1);
                this.dataSource.setPlaceType(1);
                break;
              }
            }
            this.placeNameAndRangeView.setText(this.dataSource.curRegion().getString("Name"));
            continue;
          }
          if ((getActivity() == null) || (getActivity().getIntent() == null) || (getActivity().getIntent().getData() == null))
            continue;
          localObject1 = getActivity().getIntent().getData();
          if (TextUtils.isEmpty(((Uri)localObject1).getQueryParameter("regionName")))
            continue;
          this.placeNameAndRangeView.setText(((Uri)localObject1).getQueryParameter("regionName"));
          this.dataSource.setPlaceType(2);
        }
      }
    }
    catch (Exception localException3)
    {
      while (true)
        boolean bool2 = bool3;
    }
  }

  private void updateSearchDateHeaderView()
  {
    if (this.dataSource.hasSearchDate())
    {
      if (this.searchDateHeaderView == null)
        setupSearchDateHeader();
      this.searchDateHeaderView.findViewById(R.id.head_view).setVisibility(0);
    }
    do
      return;
    while (this.searchDateHeaderView == null);
    this.searchDateHeaderView.findViewById(R.id.head_view).setVisibility(8);
  }

  protected boolean checkFilterable(DPObject paramDPObject)
  {
    if ((paramDPObject != null) && (paramDPObject.getString("Name").contains("距离")) && (getActivity().locationService().location() == null))
    {
      Toast.makeText(getActivity(), "正在定位，此功能暂不可用", 0).show();
      return false;
    }
    return true;
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
        if (Build.VERSION.SDK_INT > 10);
        for (Object localObject = "shoplist/hotelcontentlist"; ; localObject = "shoplist/contentlist")
        {
          localObject = getFragment().findAgent((String)localObject);
          if (localObject == null)
            break;
          this.listView = ((PullToRefreshListView)localObject.getClass().getDeclaredMethod("getListView", new Class[0]).invoke(localObject, new Object[0]));
          if (this.listView == null)
            break label86;
          i = 1;
          break label94;
        }
        label86: i = 0;
      }
      catch (Exception localException)
      {
        return false;
      }
    }
    label94: return i;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Object localObject;
    if ((paramInt1 == 111) || (paramInt1 == 113))
      if (paramInt2 == -1)
      {
        updateHotelBookingTime(paramIntent);
        if (getActivity() != null)
        {
          localObject = getActivity().getIntent();
          long l1 = paramIntent.getLongExtra("checkin_time", System.currentTimeMillis());
          long l2 = paramIntent.getLongExtra("checkout_time", 86400000L + l1);
          ((Intent)localObject).putExtra("checkin_time", l1);
          ((Intent)localObject).putExtra("checkout_time", l2);
          getActivity().setResult(-1, (Intent)localObject);
        }
      }
    while (true)
    {
      return;
      if (paramInt1 != 112)
        break;
      if (paramInt2 != -1)
        continue;
      this.hotelRegionSelectedIndexPath = paramIntent.getIntArrayExtra("HOTEL_REGION_SelectedIndexPath");
      paramInt1 = paramIntent.getIntExtra("type", 0);
      this.dataSource.setPlaceType(paramInt1);
      if (!isCurrentCity())
        this.dataSource.setShowDistance(false);
      if (paramIntent.getParcelableExtra("result") != null)
        switch (paramInt1)
        {
        case 0:
        default:
        case -1:
        case 1:
        case 2:
        case 3:
        case 4:
        }
      while (!TextUtils.isEmpty(this.dataSource.suggestKeyword()))
      {
        statisticsEvent("hotelkwlist5", "hotelkwlist5_area", this.placeNameAndRangeView.getText().toString(), 0);
        return;
        double d1 = paramIntent.getDoubleExtra("lat", 0.0D);
        double d2 = paramIntent.getDoubleExtra("lng", 0.0D);
        paramIntent = paramIntent.getStringExtra("address");
        this.dataSource.setCustomGPS(Double.valueOf(d1).doubleValue(), Double.valueOf(d2).doubleValue());
        this.dataSource.setPlaceType(3);
        this.dataSource.setHotelFilterType(-1);
        if (this.placeNameAndRangeView != null)
          this.placeNameAndRangeView.setText(paramIntent);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        continue;
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        if (paramIntent == null)
          continue;
        paramIntent = paramIntent.edit().putBoolean("UrlLocTag", true).generate();
        this.dataSource.setCurRange(paramIntent);
        if (this.placeNameAndRangeView != null)
          this.placeNameAndRangeView.setText(formatRange(paramIntent));
        this.dataSource.setHotelFilterType(1);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        continue;
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        if (paramIntent == null)
          continue;
        this.dataSource.setCurRegion(paramIntent);
        this.dataSource.aroundCitiesSearchValue = paramIntent.getString("RegionSearchKey");
        if ((this.placeNameAndRangeView != null) && (paramIntent != null))
          this.placeNameAndRangeView.setText(paramIntent.getString("Name"));
        this.dataSource.setHotelFilterType(2);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        if (getActivity() == null)
          continue;
        localObject = getActivity().getIntent();
        ((Intent)localObject).putExtra("region_name", paramIntent.getString("Name"));
        ((Intent)localObject).putExtra("region_id", paramIntent.getInt("ID"));
        getActivity().setResult(-1, (Intent)localObject);
        continue;
        d1 = paramIntent.getDoubleExtra("lat", 0.0D);
        d2 = paramIntent.getDoubleExtra("lng", 0.0D);
        paramIntent = paramIntent.getStringExtra("address");
        this.dataSource.setCustomGPS(Double.valueOf(d1).doubleValue(), Double.valueOf(d2).doubleValue());
        this.dataSource.setHotelFilterType(3);
        if (this.placeNameAndRangeView != null)
          this.placeNameAndRangeView.setText(paramIntent);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        this.dataSource.setShowDistance(true);
        continue;
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        if (paramIntent == null)
          continue;
        localObject = paramIntent.getString("Name");
        d1 = paramIntent.getDouble("Lat");
        d2 = paramIntent.getDouble("Lng");
        this.dataSource.setCustomGPS(Double.valueOf(d1).doubleValue(), Double.valueOf(d2).doubleValue());
        this.dataSource.setHotelFilterType(4);
        if (this.placeNameAndRangeView != null)
          this.placeNameAndRangeView.setText((CharSequence)localObject);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        this.dataSource.setShowDistance(true);
      }
      if (this.placeNameAndRangeView == null)
        continue;
      statisticsEvent("hotellist5", "hotellist5_area", this.placeNameAndRangeView.getText().toString(), 0);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.dataSource = getDataSource();
    if (this.dataSource == null);
    while (true)
    {
      return;
      if (this.dataSource.hotelTabIndex == 2)
        if (this.searchDateHeaderView != null)
          this.searchDateHeaderView.setVisibility(8);
      while ((this.dataSource.startIndex() == 0) && (fetchListView()))
      {
        updateSearchDateHeaderView();
        return;
        if (this.searchDateHeaderView == null)
          continue;
        this.searchDateHeaderView.setVisibility(0);
      }
    }
  }

  // ERROR //
  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore 4
    //   3: invokestatic 799	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
    //   6: invokevirtual 800	com/dianping/app/DPApplication:locationService	()Lcom/dianping/locationservice/LocationService;
    //   9: invokeinterface 803 1 0
    //   14: astore_1
    //   15: aload_1
    //   16: ifnull +254 -> 270
    //   19: iconst_1
    //   20: istore_3
    //   21: iload_3
    //   22: ifeq +253 -> 275
    //   25: aload_1
    //   26: ldc 72
    //   28: invokevirtual 243	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   31: aload_0
    //   32: invokevirtual 804	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:cityId	()I
    //   35: if_icmpne +240 -> 275
    //   38: iload 4
    //   40: istore_3
    //   41: aload_0
    //   42: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   45: aload_0
    //   46: invokevirtual 804	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:cityId	()I
    //   49: invokevirtual 807	com/dianping/base/shoplist/data/BaseShopListDataSource:setCityId	(I)V
    //   52: iload_3
    //   53: ifeq +86 -> 139
    //   56: aload_0
    //   57: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   60: iconst_1
    //   61: invokevirtual 697	com/dianping/base/shoplist/data/BaseShopListDataSource:setShowDistance	(Z)V
    //   64: aload_0
    //   65: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   68: new 60	com/dianping/archive/DPObject
    //   71: dup
    //   72: invokespecial 808	com/dianping/archive/DPObject:<init>	()V
    //   75: invokevirtual 70	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   78: ldc 72
    //   80: ldc 182
    //   82: invokeinterface 86 3 0
    //   87: invokeinterface 92 1 0
    //   92: invokevirtual 748	com/dianping/base/shoplist/data/BaseShopListDataSource:setCurRange	(Lcom/dianping/archive/DPObject;)Z
    //   95: pop
    //   96: aload_0
    //   97: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   100: iconst_1
    //   101: invokevirtual 550	com/dianping/base/shoplist/data/BaseShopListDataSource:setPlaceType	(I)V
    //   104: aload_0
    //   105: getfield 156	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:placeNameAndRangeView	Landroid/widget/TextView;
    //   108: aload_0
    //   109: aload_0
    //   110: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   113: invokevirtual 575	com/dianping/base/shoplist/data/BaseShopListDataSource:curRange	()Lcom/dianping/archive/DPObject;
    //   116: invokespecial 577	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:formatRange	(Lcom/dianping/archive/DPObject;)Ljava/lang/String;
    //   119: invokevirtual 514	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   122: aload_0
    //   123: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   126: iconst_1
    //   127: invokevirtual 486	com/dianping/base/shoplist/data/BaseShopListDataSource:reset	(Z)V
    //   130: aload_0
    //   131: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   134: iconst_1
    //   135: invokevirtual 489	com/dianping/base/shoplist/data/BaseShopListDataSource:reload	(Z)V
    //   138: return
    //   139: aload_0
    //   140: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   143: iconst_0
    //   144: invokevirtual 697	com/dianping/base/shoplist/data/BaseShopListDataSource:setShowDistance	(Z)V
    //   147: aload_0
    //   148: invokevirtual 405	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:getActivity	()Lcom/dianping/base/app/loader/AgentActivity;
    //   151: invokevirtual 525	com/dianping/base/app/loader/AgentActivity:getIntent	()Landroid/content/Intent;
    //   154: invokevirtual 529	android/content/Intent:getData	()Landroid/net/Uri;
    //   157: ldc_w 531
    //   160: invokevirtual 536	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   163: astore_1
    //   164: aload_1
    //   165: invokestatic 209	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   168: ifne +11 -> 179
    //   171: aload_0
    //   172: aload_1
    //   173: invokestatic 541	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   176: putfield 146	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:searchAroundCities	Z
    //   179: aload_0
    //   180: getfield 146	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:searchAroundCities	Z
    //   183: ifne +14 -> 197
    //   186: aload_0
    //   187: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   190: getstatic 110	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:ALL_REGION	Lcom/dianping/archive/DPObject;
    //   193: invokevirtual 751	com/dianping/base/shoplist/data/BaseShopListDataSource:setCurRegion	(Lcom/dianping/archive/DPObject;)Z
    //   196: pop
    //   197: aload_0
    //   198: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   201: iconst_2
    //   202: invokevirtual 550	com/dianping/base/shoplist/data/BaseShopListDataSource:setPlaceType	(I)V
    //   205: aload_0
    //   206: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   209: invokevirtual 584	com/dianping/base/shoplist/data/BaseShopListDataSource:curRegion	()Lcom/dianping/archive/DPObject;
    //   212: astore_1
    //   213: aload_0
    //   214: getfield 156	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:placeNameAndRangeView	Landroid/widget/TextView;
    //   217: astore_2
    //   218: aload_1
    //   219: ifnonnull +37 -> 256
    //   222: ldc 194
    //   224: astore_1
    //   225: aload_2
    //   226: aload_1
    //   227: invokevirtual 514	android/widget/TextView:setText	(Ljava/lang/CharSequence;)V
    //   230: aload_0
    //   231: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   234: iconst_1
    //   235: invokevirtual 486	com/dianping/base/shoplist/data/BaseShopListDataSource:reset	(Z)V
    //   238: aload_0
    //   239: getfield 133	com/dianping/hotel/shoplist/agent/ShopListHotelInfoAgent:dataSource	Lcom/dianping/base/shoplist/data/BaseShopListDataSource;
    //   242: iconst_1
    //   243: invokevirtual 489	com/dianping/base/shoplist/data/BaseShopListDataSource:reload	(Z)V
    //   246: return
    //   247: astore_1
    //   248: aload_1
    //   249: invokevirtual 809	java/lang/Exception:toString	()Ljava/lang/String;
    //   252: invokestatic 814	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   255: return
    //   256: aload_1
    //   257: ldc 80
    //   259: invokevirtual 166	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   262: astore_1
    //   263: goto -38 -> 225
    //   266: astore_1
    //   267: goto -88 -> 179
    //   270: iconst_0
    //   271: istore_3
    //   272: goto -251 -> 21
    //   275: iconst_0
    //   276: istore_3
    //   277: goto -236 -> 41
    //
    // Exception table:
    //   from	to	target	type
    //   56	138	247	java/lang/Exception
    //   139	147	247	java/lang/Exception
    //   179	197	247	java/lang/Exception
    //   197	218	247	java/lang/Exception
    //   225	246	247	java/lang/Exception
    //   256	263	247	java/lang/Exception
    //   147	179	266	java/lang/Exception
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("category".equals(paramObject))
    {
      if (this.dataSource.filterCategories() == null)
        return;
      this.hasSelectedCategory = true;
      localListFilterDialog = new ListFilterDialog(getActivity());
      localListFilterDialog.setTag(paramObject);
      localListFilterDialog.setItems(this.dataSource.filterCategories());
      if (this.dataSource.curCategory() == null)
      {
        localDPObject = ALL_CATEGORY;
        localListFilterDialog.setSelectedItem(localDPObject);
        localListFilterDialog.setOnFilterListener(this.filterListener);
        localListFilterDialog.show(paramView);
        ((NovaLinearLayout)paramView).setGAString("hotellist_category_click");
      }
    }
    do
    {
      onFilterBarClick(paramObject);
      return;
      localDPObject = this.dataSource.curCategory();
      break;
    }
    while (!"sort".equals(paramObject));
    ListFilterDialog localListFilterDialog = new ListFilterDialog(getActivity());
    localListFilterDialog.setTag(paramObject);
    localListFilterDialog.setItems(this.dataSource.filterSorts());
    if (this.dataSource.curSort() == null);
    for (DPObject localDPObject = DEFAULT_SORT; ; localDPObject = this.dataSource.curSort())
    {
      localListFilterDialog.setSelectedItem(localDPObject);
      localListFilterDialog.setOnFilterListener(this.filterListener);
      localListFilterDialog.show(paramView);
      ((NovaLinearLayout)paramView).setGAString("hotellist_sort_click");
      break;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.HOTEL_BOOKING_TIME_CHANGE");
    getActivity().registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    DPApplication.instance().cityConfig().removeListener(this);
    getActivity().unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void updateNavs()
  {
    Object localObject2 = "星级分类";
    Object localObject1 = localObject2;
    if (this.dataSource.curCategory() != null)
    {
      if (this.dataSource.curCategory().getInt("ID") != 60)
        break label163;
      localObject1 = localObject2;
      if (this.hasSelectedCategory)
        localObject1 = this.dataSource.curCategory().getString("Name");
    }
    this.filterBar1.setItem("category", (String)localObject1);
    ((TextView)(TextView)this.filterBar1.findViewWithTag("category").findViewById(16908308)).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localObject1 = "智能排序";
    localObject2 = this.filterBar2;
    if (this.dataSource.curSort() == null);
    while (true)
    {
      ((FilterBar)localObject2).setItem("sort", (String)localObject1);
      ((TextView)(TextView)this.filterBar2.findViewWithTag("sort").findViewById(16908308)).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      return;
      label163: localObject1 = this.dataSource.curCategory().getString("Name");
      break;
      localObject1 = this.dataSource.curSort().getString("Name");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.agent.ShopListHotelInfoAgent
 * JD-Core Version:    0.6.0
 */