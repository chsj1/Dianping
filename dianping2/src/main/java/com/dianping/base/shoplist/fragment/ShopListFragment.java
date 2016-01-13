package com.dianping.base.shoplist.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.TuanShopAggregationListAdapter;
import com.dianping.base.shoplist.activity.NShopListActivity;
import com.dianping.base.shoplist.data.DataSource.OnDataChangeListener;
import com.dianping.base.shoplist.data.DataSource.OnDataSourceStatusChangeListener;
import com.dianping.base.shoplist.data.DefaultSearchShopListDataSource;
import com.dianping.base.shoplist.data.DefaultShopListDataSource;
import com.dianping.base.shoplist.data.LocalAndRegionShopListDataSource;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.BaseBannerView;
import com.dianping.base.widget.MainBannerView;
import com.dianping.base.widget.NovaFragment;
import com.dianping.model.Location;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopListFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, DataSource.OnDataChangeListener, ShopListAdapter.ShopListReloadHandler, DataSource.OnDataSourceStatusChangeListener
{
  static final int REQUEST_HOTEL_BOOKING = 111;
  private static final int REQUEST_HOTEL_PLACE = 112;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd", Locale.getDefault());
  private static final String TAG = ShopListFragment.class.getSimpleName();
  private int adapterType = 0;
  MainBannerView bannerView;
  private long checkinTimeMills;
  long checkoutTimeMills;
  private boolean enablePullToRefresh;
  private View hotelTuanInfoView;
  private boolean isPullToRefresh;
  private int lastShopsSize;
  private View likedActivityView;
  protected PullToRefreshListView listView;
  protected OnShopItemClickListener mShopItemClickListner;
  protected ShopListFragment.IShopListIndexMapping mapper;
  private boolean needDefaultGA = true;
  private boolean needShowLoading;
  private BroadcastReceiver receiver = new ShopListFragment.1(this);
  private View searchDateHeaderView;
  protected ShopListAdapter shopListAdapter;
  protected DefaultShopListDataSource shopListDataSource;
  protected boolean shouldShowImage;
  private View takeawayView;

  private void configSearchDateHeaderView(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    if (getActivity() == null)
      return;
    ShopListFragment.5 local5;
    if (this.searchDateHeaderView == null)
    {
      this.searchDateHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.shoplist_search_date_header_view, paramViewGroup, false);
      this.searchDateHeaderView.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(paramViewGroup.getContext(), 0.0F)));
      this.listView.setHeaderDividersEnabled(false);
      this.listView.addHeaderView(this.searchDateHeaderView, null, false);
      local5 = null;
    }
    try
    {
      Object localObject = Class.forName("com.dianping.hotel.shoplist.fragement.HotelShopFilterFragment").newInstance();
      paramViewGroup = local5;
      if ((localObject instanceof Fragment))
        paramViewGroup = (Fragment)localObject;
      if (paramViewGroup != null)
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.hotel_filter_fragment, paramViewGroup).commitAllowingStateLoss();
      paramViewGroup = new ShopListFragment.4(this);
      local5 = new ShopListFragment.5(this);
      this.searchDateHeaderView.findViewById(R.id.hotel_date_layout).setOnClickListener(paramViewGroup);
      this.searchDateHeaderView.findViewById(R.id.hotel_place_layout).setOnClickListener(local5);
      updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
      this.searchDateHeaderView.findViewById(R.id.head_view).setVisibility(8);
      this.searchDateHeaderView.findViewById(R.id.hotel_filter_fragment).setVisibility(8);
      return;
    }
    catch (ClassNotFoundException paramViewGroup)
    {
      while (true)
      {
        paramViewGroup.printStackTrace();
        paramViewGroup = local5;
      }
    }
    catch (InstantiationException paramViewGroup)
    {
      while (true)
      {
        paramViewGroup.printStackTrace();
        paramViewGroup = local5;
      }
    }
    catch (IllegalAccessException paramViewGroup)
    {
      while (true)
      {
        paramViewGroup.printStackTrace();
        paramViewGroup = local5;
      }
    }
  }

  private JSONArray getJsonArray(ArrayList<DPObject> paramArrayList)
  {
    if ((paramArrayList != null) && (paramArrayList.size() > 0))
    {
      JSONArray localJSONArray = new JSONArray();
      int i = 0;
      while (true)
      {
        Object localObject1 = localJSONArray;
        if (i >= paramArrayList.size())
          break label130;
        localObject1 = (DPObject)paramArrayList.get(i);
        if (localObject1 != null);
        try
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("ID", ((DPObject)localObject1).getInt("ID"));
          localJSONObject.put("Image", ((DPObject)localObject1).getString("Image"));
          localJSONObject.put("Url", ((DPObject)localObject1).getString("Url"));
          localJSONArray.put(localJSONObject);
          i += 1;
        }
        catch (JSONException localJSONException)
        {
          while (true)
            localJSONException.printStackTrace();
        }
      }
    }
    Object localObject2 = null;
    label130: return (JSONArray)localObject2;
  }

  private void pullToRefresh()
  {
    this.isPullToRefresh = true;
    if (this.shopListDataSource != null)
    {
      this.shopListDataSource.reset(false);
      this.shopListDataSource.reload(true);
      if (this.shopListAdapter != null)
        this.shopListAdapter.setIsPullToRefresh(true);
    }
  }

  private void showBanner(ViewGroup paramViewGroup)
  {
    if (this.bannerView == null)
    {
      this.bannerView = new MainBannerView(getActivity());
      this.bannerView.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(paramViewGroup.getContext(), 0.0F)));
      this.bannerView.setBannerCloseListener(new ShopListFragment.3(this));
      this.listView.setHeaderDividersEnabled(false);
      this.listView.addHeaderView(this.bannerView);
    }
    paramViewGroup = getJsonArray(this.shopListDataSource.bannerList());
    if (paramViewGroup == null)
    {
      setBannerViewVisible(8);
      return;
    }
    SharedPreferences localSharedPreferences = getActivity().getSharedPreferences("hotellistbanner", 0);
    MainBannerView localMainBannerView = this.bannerView;
    if (MainBannerView.shouldShowAnnounces(paramViewGroup, localSharedPreferences))
    {
      this.bannerView.setAnnounce(paramViewGroup, localSharedPreferences);
      setBannerViewVisible(0);
      return;
    }
    setBannerViewVisible(8);
  }

  private void updateHotelBookingTime(Intent paramIntent)
  {
    if (paramIntent != null)
    {
      long l1 = paramIntent.getLongExtra("checkin_time", System.currentTimeMillis());
      long l2 = paramIntent.getLongExtra("checkout_time", 86400000L + l1);
      if ((l1 != this.checkinTimeMills) || (l2 != this.checkoutTimeMills))
      {
        this.checkinTimeMills = l1;
        this.checkoutTimeMills = l2;
        updateSearchDateheaderView(l1, l2);
        this.shopListDataSource.reset(true);
        this.shopListDataSource.reload(true);
      }
    }
  }

  private void updateSearchDateheaderView(long paramLong1, long paramLong2)
  {
    if (this.searchDateHeaderView != null)
    {
      this.shopListDataSource.setHotelCheckinTime(paramLong1);
      this.shopListDataSource.setHotelCheckoutTime(paramLong2);
      ((TextView)this.searchDateHeaderView.findViewById(16908308)).setText("入住 " + SDF.format(Long.valueOf(paramLong1)));
      ((TextView)this.searchDateHeaderView.findViewById(16908309)).setText("退房 " + SDF.format(Long.valueOf(paramLong2)));
    }
  }

  public long checkinTimeMills()
  {
    return this.checkinTimeMills;
  }

  public long checkoutTimeMills()
  {
    return this.checkoutTimeMills;
  }

  protected ShopListAdapter creatShopListAdapter()
  {
    return new ShopListAdapter(this);
  }

  public PullToRefreshListView getListView()
  {
    return this.listView;
  }

  public String getShopTraceInfo(DPObject paramDPObject)
  {
    Location localLocation = location();
    double d1 = 0.0D;
    double d2 = 0.0D;
    if (localLocation != null)
    {
      d1 = localLocation.latitude();
      d2 = localLocation.longitude();
    }
    return paramDPObject.getInt("ID") + "," + d1 + "/" + d2;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    NovaConfigUtils.showDialogInMobileNetworkWhenFirstStart((DPActivity)getActivity());
    this.listView.setFastScrollEnabled(true);
    if (this.needShowLoading)
      onDataSourceStatusChange(1);
    registerReceiver(this.receiver, new IntentFilter("com.dianping.action.HOTEL_BOOKING_TIME_CHANGE"));
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 111)
      updateHotelBookingTime(paramIntent);
    while (true)
    {
      return;
      if (paramInt1 != 112)
        break;
      if (paramInt2 != -1)
        continue;
      paramInt1 = paramIntent.getIntExtra("type", 0);
      double d1;
      double d2;
      switch (paramInt1)
      {
      default:
        return;
      case 1:
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        if (paramIntent == null)
          continue;
        ((DefaultSearchShopListDataSource)this.shopListDataSource).setCurRange(paramIntent);
        if (!(getActivity() instanceof NShopListActivity))
          continue;
        ((NShopListActivity)getActivity()).setPlaceName("我");
        ((NShopListActivity)getActivity()).setPlaceType(paramInt1);
        ((NShopListActivity)getActivity()).setPlaceRange(paramIntent.getString("ID"));
        ((NShopListActivity)getActivity()).isPlaceSetted = true;
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_name)).setText(((NShopListActivity)getActivity()).getPlaceName());
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_range)).setText(((NShopListActivity)getActivity()).getPlaceRange());
        this.shopListDataSource.reset(true);
        this.shopListDataSource.reload(true);
        return;
      case 2:
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        if (paramIntent == null)
          continue;
        ((DefaultSearchShopListDataSource)this.shopListDataSource).setCurRegion(paramIntent);
        if (!(getActivity() instanceof NShopListActivity))
          continue;
        ((NShopListActivity)getActivity()).setPlaceName(paramIntent.getString("Name"));
        ((NShopListActivity)getActivity()).setPlaceType(paramInt1);
        ((NShopListActivity)getActivity()).setPlaceRange("");
        ((NShopListActivity)getActivity()).isPlaceSetted = true;
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_name)).setText(((NShopListActivity)getActivity()).getPlaceName());
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_range)).setText(((NShopListActivity)getActivity()).getPlaceRange());
        this.shopListDataSource.reset(true);
        this.shopListDataSource.reload(true);
        return;
      case 3:
        ((DefaultSearchShopListDataSource)this.shopListDataSource).setCurRange(new DPObject().edit().putString("ID", "").generate());
        d1 = paramIntent.getDoubleExtra("lat", 0.0D);
        d2 = paramIntent.getDoubleExtra("lng", 0.0D);
        paramIntent = paramIntent.getStringExtra("address");
        if (!(getActivity() instanceof NShopListActivity))
          continue;
        ((NShopListActivity)getActivity()).uriLat = String.valueOf(Double.valueOf(d1));
        ((NShopListActivity)getActivity()).uriLng = String.valueOf(Double.valueOf(d2));
        ((NShopListActivity)getActivity()).setPlaceName(paramIntent);
        ((NShopListActivity)getActivity()).setPlaceType(paramInt1);
        ((NShopListActivity)getActivity()).setPlaceRange("附近");
        ((NShopListActivity)getActivity()).isPlaceSetted = true;
        if ((this.shopListDataSource instanceof LocalAndRegionShopListDataSource))
          ((LocalAndRegionShopListDataSource)this.shopListDataSource).setFirst(true);
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_name)).setText(((NShopListActivity)getActivity()).getPlaceName());
        ((TextView)this.searchDateHeaderView.findViewById(R.id.place_range)).setText(((NShopListActivity)getActivity()).getPlaceRange());
        this.shopListDataSource.reset(true);
        this.shopListDataSource.reload(true);
        return;
      case 4:
        paramIntent = (DPObject)paramIntent.getParcelableExtra("result");
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder().append("region is null ? ");
        if (paramIntent == null);
        for (boolean bool = true; ; bool = false)
        {
          Log.d(str, bool);
          if (paramIntent == null)
            break;
          ((DefaultSearchShopListDataSource)this.shopListDataSource).setCurRange(new DPObject().edit().putString("ID", "").generate());
          str = paramIntent.getString("Name");
          d1 = paramIntent.getDouble("Lat");
          d2 = paramIntent.getDouble("Lng");
          if (!(getActivity() instanceof NShopListActivity))
            break;
          ((NShopListActivity)getActivity()).uriLat = String.valueOf(Double.valueOf(d1));
          ((NShopListActivity)getActivity()).uriLng = String.valueOf(Double.valueOf(d2));
          ((NShopListActivity)getActivity()).setPlaceName(str);
          ((NShopListActivity)getActivity()).setPlaceType(paramInt1);
          ((NShopListActivity)getActivity()).isPlaceSetted = true;
          if ((this.shopListDataSource instanceof LocalAndRegionShopListDataSource))
            ((LocalAndRegionShopListDataSource)this.shopListDataSource).setFirst(true);
          ((TextView)this.searchDateHeaderView.findViewById(R.id.place_name)).setText(((NShopListActivity)getActivity()).getPlaceName());
          ((TextView)this.searchDateHeaderView.findViewById(R.id.place_range)).setText(((NShopListActivity)getActivity()).getPlaceRange());
          this.shopListDataSource.reset(true);
          this.shopListDataSource.reload(true);
          return;
        }
      }
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.listView = new PullToRefreshListView(getActivity());
    this.listView.setBackgroundColor(getResources().getColor(R.color.white));
    this.listView.setOnRefreshListener(new ShopListFragment.2(this));
    paramViewGroup = this.listView;
    if (this.enablePullToRefresh);
    for (paramLayoutInflater = PullToRefreshBase.Mode.PULL_FROM_START; ; paramLayoutInflater = PullToRefreshBase.Mode.DISABLED)
    {
      paramViewGroup.setMode(paramLayoutInflater);
      this.listView.setOnItemClickListener(this);
      this.listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
      this.listView.setSelector(R.drawable.home_listview_bg);
      this.checkinTimeMills = System.currentTimeMillis();
      this.checkoutTimeMills = (this.checkinTimeMills + 86400000L);
      return this.listView;
    }
  }

  public void onDataChanged(int paramInt)
  {
    if (this.listView == null);
    while (true)
    {
      return;
      if (paramInt != 11)
        continue;
      double d1 = this.shopListDataSource.offsetLatitude();
      double d2 = this.shopListDataSource.offsetLongitude();
      paramInt = 0;
      while (paramInt < this.listView.getChildCount())
      {
        View localView = this.listView.getChildAt(paramInt);
        if ((localView instanceof ShopListItem))
          ((ShopListItem)localView).refreshDistance(d1, d2);
        paramInt += 1;
      }
    }
  }

  public void onDataSourceStatusChange(int paramInt)
  {
    if ((this.isPullToRefresh) && ((this.shopListDataSource.status() == 2) || (this.shopListDataSource.status() == 3)))
    {
      this.isPullToRefresh = false;
      this.listView.onRefreshComplete();
      if (this.shopListAdapter != null)
        this.shopListAdapter.setIsPullToRefresh(false);
    }
    showBanner(this.listView);
    configSearchDateHeaderView(this.listView, this.shopListDataSource.hasSearchDate());
    if ((getActivity() != null) && (this.likedActivityView == null))
    {
      this.likedActivityView = LayoutInflater.from(getActivity()).inflate(R.layout.keyword_tuan_item, null, false);
      this.listView.addHeaderView(this.likedActivityView, null, false);
    }
    if (this.shopListAdapter == null)
    {
      this.shopListAdapter = creatShopListAdapter();
      this.shopListAdapter.setShouldShowImage(this.shouldShowImage);
      this.listView.setAdapter(this.shopListAdapter);
    }
    this.shopListAdapter.setShopList(this.shopListDataSource);
  }

  public void onDestroy()
  {
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.bannerView != null);
    try
    {
      this.bannerView.onDetachedFromWindow();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    int i = paramInt;
    if (this.mapper != null)
      i = this.mapper.mapping(paramInt, this.listView.getHeaderViewsCount());
    Object localObject = paramAdapterView.getItemAtPosition(i);
    i = this.listView.getHeaderViewsCount();
    if ((localObject instanceof DPObject))
    {
      localObject = (DPObject)localObject;
      if (this.mShopItemClickListner != null)
        this.mShopItemClickListner.onShopItemClick(paramAdapterView, paramView, paramInt - i, paramLong, (DPObject)localObject);
    }
    else
    {
      return;
    }
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + ((DPObject)localObject).getInt("ID")));
    paramAdapterView.putExtra("shopId", ((DPObject)localObject).getInt("ID"));
    paramAdapterView.putExtra("shop", (Parcelable)localObject);
    if (this.shopListDataSource.hasSearchDate())
    {
      paramAdapterView.putExtra("checkinTime", this.checkinTimeMills);
      paramAdapterView.putExtra("checkoutTime", this.checkoutTimeMills);
    }
    startActivity(paramAdapterView);
  }

  public void onPause()
  {
    super.onPause();
    if (this.shopListDataSource != null)
      this.shopListDataSource.removeDataChangeListener(this);
  }

  public void onResume()
  {
    super.onResume();
    boolean bool = NovaConfigUtils.isShowImageInMobileNetwork();
    if (this.shouldShowImage != bool)
      this.shouldShowImage = bool;
    if (this.shopListAdapter != null)
    {
      this.shopListAdapter.setShouldShowImage(this.shouldShowImage);
      this.shopListAdapter.notifyDataSetChanged();
    }
    if (this.shopListDataSource != null)
      this.shopListDataSource.addDataChangeListener(this);
    updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    if (this.shopListAdapter != null)
      this.listView.setAdapter(this.shopListAdapter);
  }

  public void reload(boolean paramBoolean)
  {
    if (this.shopListDataSource != null)
    {
      if (this.shopListDataSource.shops().size() > this.lastShopsSize)
      {
        this.shopListDataSource.incLoadMoreCount();
        this.lastShopsSize = this.shopListDataSource.shops().size();
      }
      this.shopListDataSource.reload(paramBoolean);
    }
  }

  void setBannerViewVisible(int paramInt)
  {
    View localView = this.bannerView.findViewById(BaseBannerView.ANNOUNCELAY_HEAD_ID);
    if (paramInt == 0)
    {
      localView.setVisibility(0);
      return;
    }
    localView.setPadding(0, 0, 0, 0);
    localView.setVisibility(8);
  }

  public void setHotelCheckDate(long paramLong1, long paramLong2)
  {
    if ((paramLong1 != 0L) && (paramLong2 != 0L))
    {
      this.checkinTimeMills = paramLong1;
      this.checkoutTimeMills = paramLong2;
    }
  }

  public void setNeedDefaultGA(boolean paramBoolean)
  {
    this.needDefaultGA = paramBoolean;
  }

  public void setOnShopItemClickListener(OnShopItemClickListener paramOnShopItemClickListener)
  {
    this.mShopItemClickListner = paramOnShopItemClickListener;
  }

  public void setShopAdapter(ShopListAdapter paramShopListAdapter)
  {
    this.adapterType = 0;
    this.shopListAdapter = paramShopListAdapter;
    if (this.shopListAdapter != null)
    {
      this.listView.setAdapter(this.shopListAdapter);
      this.shopListAdapter.setShouldShowImage(this.shouldShowImage);
    }
    this.shopListDataSource.reset(true);
    this.shopListDataSource.reload(false);
  }

  public void setShopListDataSource(DefaultShopListDataSource paramDefaultShopListDataSource)
  {
    this.shopListDataSource = paramDefaultShopListDataSource;
    this.shopListDataSource.setShowDistance(DeviceUtils.isCurrentCity());
    if (this.shopListDataSource != null)
    {
      this.shopListDataSource.setDataSourceStatusChangeListener(this);
      if (getActivity() != null)
        onDataSourceStatusChange(1);
    }
    else
    {
      return;
    }
    this.needShowLoading = true;
  }

  public void setShowDistance(boolean paramBoolean)
  {
    if (this.shopListAdapter != null)
      this.shopListAdapter.setShowDistance(paramBoolean);
  }

  public void setTuanAdapter(TuanShopAggregationListAdapter paramTuanShopAggregationListAdapter)
  {
    this.adapterType = 1;
    this.shopListAdapter = paramTuanShopAggregationListAdapter;
    this.listView.setAdapter(this.shopListAdapter);
    this.shopListAdapter.setShouldShowImage(this.shouldShowImage);
    this.shopListDataSource.reset(true);
    this.shopListDataSource.reload(false);
  }

  public static abstract interface OnShopItemClickListener
  {
    public abstract void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.fragment.ShopListFragment
 * JD-Core Version:    0.6.0
 */