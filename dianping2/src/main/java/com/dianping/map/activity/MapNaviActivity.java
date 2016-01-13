package com.dianping.map.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.map.elements.ElementsController;
import com.dianping.map.elements.OnRouteChangeListener;
import com.dianping.map.utils.MapUtils;
import com.dianping.map.utils.ThirdpartyMapUtils;
import com.dianping.model.Location;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class MapNaviActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, SensorEventListener, OnRouteChangeListener
{
  private static final int DELAY_TIME = 30000;
  private static final String K_BUS_DURATION = "key_bus_duration";
  private static final String K_CAR_DURATION = "key_car_duration";
  private static final String K_SHOP = "shop";
  private static final String K_WALK_DURATION = "key_walk_duration";
  private static final int MSG_REQUEST_TIMEOUT = 5;
  private boolean isFinalRouteModeSelected;
  private ProgressDialog loadingDialog;
  private LinearLayout mBusBtn;
  private TextView mBusDurationView;
  private LinearLayout mCarBtn;
  private TextView mCarDurationView;
  private Context mContext;
  private int mCurrentRoute = -1;
  private FrameLayout mCustomViewContainer;
  private LatLng mEndPosition;
  private View mFailedView;
  private Handler mHandle = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 5:
      }
      do
        return;
      while ((!MapNaviActivity.this.mLoadingView.isShown()) || (MapNaviActivity.this.isFinalRouteModeSelected));
      MapNaviActivity.this.mLoadingView.setVisibility(8);
      MapNaviActivity.this.mFailedView.setVisibility(0);
      ((LoadingErrorView)MapNaviActivity.this.mFailedView).setCallBack(new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MapNaviActivity.this.mFailedView.setVisibility(8);
          MapNaviActivity.this.mLoadingView.setVisibility(0);
          ElementsController.getInstance().init(MapNaviActivity.this, MapNaviActivity.this.mMap, MapNaviActivity.this.mShopObj);
          MapNaviActivity.this.mHandle.sendEmptyMessageDelayed(5, 30000L);
        }
      });
    }
  };
  private boolean mIsGotoMyLocation = true;
  private long mLastSensorChangeTime = 0L;
  private View mLoadingView;
  private TencentMap mMap;
  private MapView mMapView;
  private LatLng mMyLatLng;
  private ImageButton mMyLocationBtn;
  private Marker mMyPositionMarker;
  private SensorManager mSensorManager;
  private DPObject mShopObj;
  private MApiRequest mShopRequest;
  private LinearLayout mWalkBtn;
  private TextView mWalkDurationView;
  private String typeCarDuration = null;
  private String typeMinBusDuration = null;
  private String typeWalkDuration = null;

  private void btnSelector(int paramInt)
  {
    if (paramInt == 3)
    {
      this.mWalkDurationView.setTextColor(getResources().getColor(R.color.light_red));
      this.mCarDurationView.setTextColor(getResources().getColor(R.color.gray));
      this.mBusDurationView.setTextColor(getResources().getColor(R.color.gray));
      this.mWalkBtn.setSelected(true);
      this.mCarBtn.setSelected(false);
      this.mBusBtn.setSelected(false);
    }
    do
    {
      return;
      if (paramInt != 2)
        continue;
      this.mCarDurationView.setTextColor(getResources().getColor(R.color.light_red));
      this.mWalkDurationView.setTextColor(getResources().getColor(R.color.gray));
      this.mBusDurationView.setTextColor(getResources().getColor(R.color.gray));
      this.mCarBtn.setSelected(true);
      this.mWalkBtn.setSelected(false);
      this.mBusBtn.setSelected(false);
      return;
    }
    while (paramInt != 1);
    this.mBusDurationView.setTextColor(getResources().getColor(R.color.light_red));
    this.mWalkDurationView.setTextColor(getResources().getColor(R.color.gray));
    this.mCarDurationView.setTextColor(getResources().getColor(R.color.gray));
    this.mBusBtn.setSelected(true);
    this.mCarBtn.setSelected(false);
    this.mWalkBtn.setSelected(false);
  }

  private LatLng getMyLatLng()
  {
    Object localObject = locationService();
    if (((LocationService)localObject).status() == 1)
      Toast.makeText(this, "正在定位，请稍候...", 1).show();
    do
    {
      do
      {
        return null;
        if (((LocationService)localObject).status() != -1)
          continue;
        Toast.makeText(this, "暂时无法定位，请检查您手机的系统定位开关是否打开", 1).show();
        return null;
      }
      while ((((LocationService)localObject).status() != 2) || (!locationService().hasLocation()));
      localObject = location();
    }
    while (localObject == null);
    return (LatLng)new LatLng(((Location)localObject).offsetLatitude(), ((Location)localObject).offsetLongitude());
  }

  private int getShopId()
  {
    int i = -1;
    Object localObject = getIntent().getData();
    if (localObject != null)
    {
      localObject = ((Uri)localObject).getQueryParameter("shopid");
      if (!TextUtils.isEmpty((CharSequence)localObject));
    }
    try
    {
      i = Integer.parseInt((String)localObject);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return -1;
  }

  private LatLng gotoMyLocation()
  {
    Object localObject1 = locationService();
    if (((LocationService)localObject1).status() == 1)
      Toast.makeText(this, "正在定位，请稍候...", 1).show();
    do
    {
      return null;
      if (((LocationService)localObject1).status() != -1)
        continue;
      Toast.makeText(this, "暂时无法定位，请检查您手机的系统定位开关是否打开", 1).show();
      return null;
    }
    while (((LocationService)localObject1).status() != 2);
    Object localObject2 = null;
    localObject1 = localObject2;
    if (locationService().hasLocation())
    {
      Location localLocation = location();
      localObject1 = localObject2;
      if (localLocation != null)
        localObject1 = new LatLng(localLocation.offsetLatitude(), localLocation.offsetLongitude());
    }
    ThirdpartyMapUtils.moveToLatLng(this.mMap, (LatLng)localObject1, true);
    return (LatLng)localObject1;
  }

  private void initData(Bundle paramBundle)
  {
    if (paramBundle == null)
      this.mShopObj = ((DPObject)super.getIntent().getParcelableExtra("shop"));
    while (true)
    {
      this.mContext = this;
      this.mSensorManager = ((SensorManager)this.mContext.getSystemService("sensor"));
      this.mSensorManager.registerListener(this, this.mSensorManager.getDefaultSensor(3), 1);
      this.mMyLatLng = getMyLatLng();
      if (this.mShopObj != null)
        this.mEndPosition = new LatLng(this.mShopObj.getDouble("Latitude"), this.mShopObj.getDouble("Longitude"));
      return;
      this.mShopObj = ((DPObject)paramBundle.getParcelable("shop"));
      this.typeCarDuration = paramBundle.getString("key_car_duration");
      this.typeWalkDuration = paramBundle.getString("key_walk_duration");
      this.typeMinBusDuration = paramBundle.getString("key_bus_duration");
    }
  }

  private void initView()
  {
    super.getTitleBar().setCustomContentView(getLayoutInflater().inflate(R.layout.title_bar_navi, null));
    this.mCustomViewContainer = ((FrameLayout)findViewById(R.id.id_custom_view_container));
    this.mWalkDurationView = ((TextView)findViewById(R.id.btn1_name));
    this.mCarDurationView = ((TextView)findViewById(R.id.btn2_name));
    this.mBusDurationView = ((TextView)findViewById(R.id.btn3_name));
    this.mLoadingView = findViewById(R.id.id_loading);
    this.mLoadingView.setVisibility(0);
    this.mFailedView = findViewById(R.id.id_loading_fail);
    this.mWalkBtn = ((LinearLayout)findViewById(R.id.map_walk));
    this.mWalkBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (!MapNaviActivity.this.isDigit(MapNaviActivity.this.mWalkDurationView.getText().toString()))
          Toast.makeText(MapNaviActivity.this.getApplicationContext(), "未获取到步行线路", 0).show();
        MapNaviActivity.this.btnSelector(3);
        MapNaviActivity.access$902(MapNaviActivity.this, 3);
        MapNaviActivity.this.mCustomViewContainer.removeAllViews();
        MapNaviActivity.access$1102(MapNaviActivity.this, null);
        ElementsController.getInstance().show(3);
      }
    });
    this.mCarBtn = ((LinearLayout)findViewById(R.id.map_car));
    this.mCarBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MapNaviActivity.this.btnSelector(2);
        MapNaviActivity.access$902(MapNaviActivity.this, 2);
        MapNaviActivity.this.mCustomViewContainer.removeAllViews();
        MapNaviActivity.access$1102(MapNaviActivity.this, null);
        ElementsController.getInstance().show(2);
      }
    });
    this.mBusBtn = ((LinearLayout)findViewById(R.id.map_metrobus));
    this.mBusBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (!MapNaviActivity.this.isDigit(MapNaviActivity.this.mBusDurationView.getText().toString()))
          Toast.makeText(MapNaviActivity.this.getApplicationContext(), "未获取到公交地铁信息", 0).show();
        MapNaviActivity.this.btnSelector(1);
        MapNaviActivity.access$902(MapNaviActivity.this, 1);
        MapNaviActivity.access$1102(MapNaviActivity.this, null);
        ElementsController.getInstance().show(1);
        if (ElementsController.getInstance().haveCustomView(1))
        {
          paramView = ElementsController.getInstance().getCustomView(1).entrySet().iterator();
          while (paramView.hasNext())
          {
            Map.Entry localEntry = (Map.Entry)paramView.next();
            MapNaviActivity.this.mCustomViewContainer.removeAllViews();
            MapNaviActivity.this.mCustomViewContainer.addView((View)localEntry.getKey(), (ViewGroup.LayoutParams)localEntry.getValue());
          }
        }
      }
    });
    findViewById(R.id.map_others).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MapUtils.showSupportedMaps(MapNaviActivity.this, MapNaviActivity.this.mShopObj, MapNaviActivity.this.mCurrentRoute);
      }
    });
    this.mMapView = ((MapView)findViewById(R.id.map));
    this.mMapView.setVisibility(4);
    this.mMap = this.mMapView.getMap();
    this.mMyLocationBtn = ((ImageButton)findViewById(R.id.my_location));
    this.mMyLocationBtn.setVisibility(4);
    this.mMyLocationBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MapNaviActivity.this.mIsGotoMyLocation)
        {
          MapNaviActivity.this.gotoMyLocation();
          MapNaviActivity.access$1302(MapNaviActivity.this, false);
          return;
        }
        ThirdpartyMapUtils.moveToLatLng(MapNaviActivity.this.mMap, MapNaviActivity.this.mEndPosition, true);
        MapNaviActivity.access$1302(MapNaviActivity.this, true);
      }
    });
    this.mHandle.sendEmptyMessageDelayed(5, 30000L);
  }

  private boolean isDigit(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return false;
    int i = paramString.length();
    int j;
    do
    {
      j = i - 1;
      if (j < 0)
        break;
      i = j;
    }
    while (!Character.isDigit(paramString.charAt(j)));
    return true;
  }

  @TargetApi(12)
  private void setViewGone(View paramView)
  {
    if (paramView == null)
      return;
    if (Build.VERSION.SDK_INT >= 11)
    {
      paramView.animate().translationY(0.0F).alpha(0.0F).setListener(new AnimatorListenerAdapter(paramView)
      {
        public void onAnimationEnd(Animator paramAnimator)
        {
          super.onAnimationEnd(paramAnimator);
          this.val$view.setVisibility(8);
        }
      });
      return;
    }
    paramView.setVisibility(8);
  }

  public void OnFinalRouteModeSelected(int paramInt, boolean paramBoolean)
  {
    this.isFinalRouteModeSelected = true;
    setViewGone(this.mLoadingView);
    setViewGone(this.mFailedView);
    this.mMapView.setVisibility(0);
    this.mMyLocationBtn.setVisibility(0);
    btnSelector(paramInt);
  }

  public void OnRouteChanged(int paramInt, boolean paramBoolean, HashMap<View, FrameLayout.LayoutParams> paramHashMap, String paramString)
  {
    if (paramInt == 2)
      this.mCarDurationView.setText(paramString);
    do
    {
      return;
      if (paramInt != 3)
        continue;
      this.mWalkDurationView.setText(paramString);
      return;
    }
    while (paramInt != 1);
    this.mBusDurationView.setText(paramString);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean needTitleBarShadow()
  {
    return true;
  }

  public void onAccuracyChanged(Sensor paramSensor, int paramInt)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.map_navi);
    initData(paramBundle);
    initView();
    ElementsController.getInstance().setRouteChangeListener(this);
    if ((this.mShopObj == null) && (getShopId() > 0))
    {
      this.loadingDialog = new ProgressDialog(this);
      this.loadingDialog.setMessage("正在加载...");
      this.loadingDialog.show();
      paramBundle = (MApiService)getService("mapi");
      StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
      localStringBuffer.append("shopid=").append(getShopId());
      this.mShopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
      paramBundle.exec(this.mShopRequest, this);
      return;
    }
    if ((this.mShopObj == null) && (getShopId() < 0))
    {
      MapUtils.showNoAddressToast(this);
      finish();
      return;
    }
    if (MapUtils.isMapNOTSupported())
    {
      Toast.makeText(this, "该手机不支持腾讯地图", 1).show();
      finish();
      return;
    }
    ElementsController.getInstance().init(this, this.mMap, this.mShopObj);
  }

  protected void onDestroy()
  {
    if (this.mMapView != null)
      this.mMapView.onDestroy();
    this.mMapView = null;
    this.mSensorManager.unregisterListener(this);
    ElementsController.getInstance().destroyRequest();
    super.onDestroy();
  }

  public void onPause()
  {
    if (this.mMapView != null)
      this.mMapView.onPause();
    super.onPause();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopRequest)
    {
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      MapUtils.showNoAddressToast(this);
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopRequest);
    try
    {
      this.mShopObj = ((DPObject)paramMApiResponse.result());
      if (this.mShopObj == null)
      {
        MapUtils.showNoAddressToast(this);
        finish();
        return;
      }
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      ElementsController.getInstance().init(this, this.mMap, this.mShopObj);
      this.mEndPosition = new LatLng(this.mShopObj.getDouble("Latitude"), this.mShopObj.getDouble("Longitude"));
      this.mHandle.sendEmptyMessageDelayed(5, 30000L);
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.mShopObj = ((DPObject)paramBundle.getParcelable("shop"));
    this.typeCarDuration = paramBundle.getString("key_car_duration");
    this.typeWalkDuration = paramBundle.getString("key_walk_duration");
    this.typeMinBusDuration = paramBundle.getString("key_bus_duration");
  }

  protected void onResume()
  {
    if (this.mMapView != null)
    {
      this.mMap = this.mMapView.getMap();
      this.mMap.setMyLocationEnabled(true);
      this.mMap.getUiSettings().setZoomControlsEnabled(false);
      this.mMapView.onResume();
    }
    super.onResume();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("shop", this.mShopObj);
    paramBundle.putString("key_car_duration", this.typeCarDuration);
    paramBundle.putString("key_walk_duration", this.typeWalkDuration);
    paramBundle.putString("key_bus_duration", this.typeMinBusDuration);
  }

  public void onSensorChanged(SensorEvent paramSensorEvent)
  {
    long l = System.currentTimeMillis();
    if (l - this.mLastSensorChangeTime > 2000L)
    {
      this.mLastSensorChangeTime = l;
      if (this.mMyPositionMarker == null)
        break label90;
      if (paramSensorEvent.accuracy > 1)
      {
        this.mMyPositionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_self));
        this.mMyPositionMarker.setRotateAngle((paramSensorEvent.values[0] * -1.0F + 720.0F) % 360.0F);
      }
    }
    else
    {
      return;
    }
    this.mMyPositionMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_my_location_circle));
    return;
    label90: this.mMyPositionMarker = this.mMap.addMarker(new MarkerOptions().position(this.mMyLatLng).anchor(0.5F, 0.5F).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_my_location_circle)));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.activity.MapNaviActivity
 * JD-Core Version:    0.6.0
 */