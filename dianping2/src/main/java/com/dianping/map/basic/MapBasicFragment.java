package com.dianping.map.basic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.dianping.base.widget.NovaFragment;
import com.dianping.locationservice.LocationService;
import com.dianping.map.utils.ThirdpartyMapUtils;
import com.dianping.model.Location;
import com.dianping.v1.R.drawable;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public abstract class MapBasicFragment extends NovaFragment
  implements SensorEventListener
{
  private boolean mIsLoaded;
  private long mLastSensorChangeTime = 0L;
  private TencentMap mMap;
  private MapView mMapView;
  private Marker mMyPosition;
  private SensorManager mSensorManager;
  private Marker mStartPosition;

  private void initMapAndLocClient()
  {
    this.mMap = this.mMapView.getMap();
    this.mMap.setMyLocationEnabled(true);
    this.mMap.getUiSettings().setZoomControlsEnabled(false);
    this.mMap.setOnMapLoadedCallback(new MapBasicFragment.1(this));
    setUpMap();
  }

  private float normalizeDegree(float paramFloat)
  {
    return (720.0F + paramFloat) % 360.0F;
  }

  protected TencentMap getBasicMap()
  {
    return this.mMap;
  }

  protected MapView getBasicMapView()
  {
    return this.mMapView;
  }

  protected LatLng getMapCenter()
  {
    return getBasicMap().getCameraPosition().target;
  }

  protected LatLng getMyPosition()
  {
    if (locationService().hasLocation())
    {
      Location localLocation = location();
      if (localLocation != null)
        return new LatLng(localLocation.offsetLatitude(), localLocation.offsetLongitude());
    }
    return null;
  }

  protected Marker getMyPositionMarker()
  {
    return this.mMyPosition;
  }

  protected LatLng gotoMyLocation()
  {
    Object localObject = locationService();
    if (((LocationService)localObject).status() == 1)
      Toast.makeText(getActivity(), "正在定位，请稍候...", 1).show();
    do
    {
      return null;
      if (((LocationService)localObject).status() != -1)
        continue;
      Toast.makeText(getActivity(), "暂时无法定位，请检查您手机的系统定位开关是否打开", 1).show();
      return null;
    }
    while (((LocationService)localObject).status() != 2);
    localObject = getMyPosition();
    ThirdpartyMapUtils.moveToLatLng(getBasicMap(), (LatLng)localObject, true);
    return (LatLng)localObject;
  }

  protected boolean isLoaded()
  {
    return this.mIsLoaded;
  }

  public void onAccuracyChanged(Sensor paramSensor, int paramInt)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mSensorManager = ((SensorManager)getActivity().getSystemService("sensor"));
  }

  public void onDestroy()
  {
    if (this.mMapView != null)
      this.mMapView.onDestroy();
    this.mMapView = null;
    super.onDestroy();
  }

  public void onPause()
  {
    this.mSensorManager.unregisterListener(this);
    if (this.mMapView != null)
      this.mMapView.onPause();
    super.onPause();
  }

  public void onResume()
  {
    super.onResume();
    if (this.mMapView != null)
    {
      initMapAndLocClient();
      this.mMapView.onResume();
    }
    this.mSensorManager.registerListener(this, this.mSensorManager.getDefaultSensor(3), 1);
  }

  public void onSensorChanged(SensorEvent paramSensorEvent)
  {
    if (getMyPositionMarker() != null)
    {
      long l = System.currentTimeMillis();
      if (l - this.mLastSensorChangeTime > 1000L)
      {
        this.mLastSensorChangeTime = l;
        if (paramSensorEvent.accuracy <= 1)
          break label71;
        getMyPositionMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_self));
        getMyPositionMarker().setRotateAngle(normalizeDegree(paramSensorEvent.values[0] * -1.0F));
      }
    }
    return;
    label71: getMyPositionMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_my_location_circle));
  }

  protected void setBasicMap(TencentMap paramTencentMap)
  {
    this.mMap = paramTencentMap;
  }

  protected void setBasicMapView(MapView paramMapView)
  {
    this.mMapView = paramMapView;
  }

  protected abstract void setUpMap();

  protected double stringLatLng(String paramString)
  {
    return Double.valueOf(paramString).doubleValue();
  }

  protected void updateMyLocationMarker()
  {
    LatLng localLatLng = getMyPosition();
    if (localLatLng == null)
      return;
    if (this.mMyPosition != null)
      this.mMyPosition.setPosition(localLatLng);
    while (true)
    {
      localLatLng = new LatLng(localLatLng.latitude + 2.E-005D, localLatLng.longitude);
      if (this.mStartPosition == null)
        break;
      this.mStartPosition.setPosition(localLatLng);
      return;
      this.mMyPosition = this.mMap.addMarker(new MarkerOptions().position(localLatLng).anchor(0.5F, 0.5F).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_my_location_circle)));
    }
    this.mStartPosition = this.mMap.addMarker(new MarkerOptions().position(localLatLng).anchor(0.5F, 0.5F).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_start)));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.basic.MapBasicFragment
 * JD-Core Version:    0.6.0
 */