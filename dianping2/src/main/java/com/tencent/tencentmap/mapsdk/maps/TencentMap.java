package com.tencent.tencentmap.mapsdk.maps;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import com.tencent.tencentmap.mapsdk.maps.a.eg;
import com.tencent.tencentmap.mapsdk.maps.a.fx;
import com.tencent.tencentmap.mapsdk.maps.a.fy;
import com.tencent.tencentmap.mapsdk.maps.a.ga;
import com.tencent.tencentmap.mapsdk.maps.a.gb;
import com.tencent.tencentmap.mapsdk.maps.a.gm;
import com.tencent.tencentmap.mapsdk.maps.a.gn;
import com.tencent.tencentmap.mapsdk.maps.a.gp;
import com.tencent.tencentmap.mapsdk.maps.a.gr;
import com.tencent.tencentmap.mapsdk.maps.a.gs;
import com.tencent.tencentmap.mapsdk.maps.a.gu;
import com.tencent.tencentmap.mapsdk.maps.a.gv;
import com.tencent.tencentmap.mapsdk.maps.a.gw;
import com.tencent.tencentmap.mapsdk.maps.a.gx;
import com.tencent.tencentmap.mapsdk.maps.a.hb;
import com.tencent.tencentmap.mapsdk.maps.a.hc;
import com.tencent.tencentmap.mapsdk.maps.a.hc.a;
import com.tencent.tencentmap.mapsdk.maps.a.hd;
import com.tencent.tencentmap.mapsdk.maps.a.hf;
import com.tencent.tencentmap.mapsdk.maps.a.hp;
import com.tencent.tencentmap.mapsdk.maps.a.hq;
import com.tencent.tencentmap.mapsdk.maps.a.hr;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.maps.model.TencentMapGestureListener;

public final class TencentMap
{
  public static final int MAP_TYPE_NONE = 0;
  public static final int MAP_TYPE_NORMAL = 1;
  public static final int MAP_TYPE_SATELLITE = 2;
  private fx circleControl = null;
  private fy circleManager = null;
  private ga glMapControl = null;
  private gb heatOverlayManager = null;
  private final hc.a locUiSettingControler = new TencentMap.1(this);
  private gm locationControl = null;
  private gn locationManager = null;
  private boolean mapDestroyed = false;
  private gp mapManager = null;
  private Projection mapProjection = null;
  private MapView mapV = null;
  private gr markerControl = null;
  private gs markerManager = null;
  private gw polyLineControl = null;
  private gu polygonControl = null;
  private gv polygonManager = null;
  private gx polylineManager = null;
  private UiSettings uiSetting;
  private hb uiSettingControl = null;
  private hc uiSettingManager = null;
  private hd viewControl = null;
  private hr worldMapOverlayManager = null;

  protected TencentMap()
  {
    this.mapManager = new gp(null, null);
  }

  protected TencentMap(MapView paramMapView, Context paramContext)
  {
    eg.b(paramContext);
    eg.a(paramContext);
    this.mapV = paramMapView;
    this.mapManager = new gp(this.mapV, paramContext);
    if (this.uiSettingControl == null)
    {
      if (this.uiSettingManager == null)
      {
        paramMapView = getViewDelegate().b();
        this.uiSettingManager = new hc(this.mapV, paramMapView);
        this.uiSettingManager.a(this.locUiSettingControler);
      }
      this.uiSettingControl = new hb(this.uiSettingManager);
    }
    this.uiSettingControl.a();
  }

  private void initForMylocation()
  {
    if (this.markerControl == null)
    {
      if (this.markerManager == null)
        this.markerManager = new gs(this.mapV, this.viewControl.b());
      this.markerControl = new gr(this.markerManager);
    }
    if (this.circleManager == null)
      this.circleManager = new fy(this.viewControl.b());
    if (this.circleControl == null)
      this.circleControl = new fx(this.circleManager);
    if (this.locationManager == null)
      this.locationManager = new gn(this.markerControl, this.circleControl, this.mapManager.q());
    if (this.locationControl == null)
      this.locationControl = new gm(this.locationManager);
  }

  private void releaseControlers()
  {
    if (this.uiSettingControl != null)
    {
      this.uiSettingControl.b();
      this.uiSettingControl = null;
    }
    if (this.locationControl != null)
    {
      this.locationControl.a();
      this.locationControl = null;
    }
    if (this.markerControl != null)
    {
      this.markerControl.a();
      this.markerControl = null;
    }
    if (this.polygonControl != null)
    {
      this.polygonControl.a();
      this.polygonControl = null;
    }
    if (this.polyLineControl != null)
    {
      this.polyLineControl.a();
      this.polyLineControl = null;
    }
    if (this.circleControl != null)
    {
      this.circleControl.a();
      this.circleControl = null;
    }
    if (this.glMapControl != null)
    {
      this.glMapControl.a();
      this.glMapControl = null;
    }
    if (this.mapProjection != null)
    {
      this.mapProjection.exit();
      this.mapProjection = null;
    }
    if (this.viewControl != null)
    {
      this.viewControl.a();
      this.viewControl = null;
    }
  }

  private void releaseManagers()
  {
    if (this.uiSettingManager != null)
    {
      this.uiSettingManager.i();
      this.uiSettingManager = null;
    }
    if (this.locationManager != null)
    {
      this.locationManager.e();
      this.locationManager = null;
    }
    if (this.markerManager != null)
    {
      this.markerManager.c();
      this.markerManager = null;
    }
    if (this.polygonManager != null)
    {
      this.polygonManager.b();
      this.polygonManager = null;
    }
    if (this.polylineManager != null)
    {
      this.polylineManager.b();
      this.polylineManager = null;
    }
    if (this.circleManager != null)
    {
      this.circleManager.a();
      this.circleManager = null;
    }
    if (this.mapManager != null)
    {
      this.mapManager.p();
      this.mapManager = null;
    }
  }

  static void setMapDataCachFolderName(String paramString)
  {
  }

  public final Circle addCircle(CircleOptions paramCircleOptions)
  {
    if (this.mapDestroyed)
      return null;
    if (this.circleManager == null)
      this.circleManager = new fy(this.viewControl.b());
    if (this.circleControl == null)
      this.circleControl = new fx(this.circleManager);
    return this.circleControl.a(paramCircleOptions);
  }

  public final HeatOverlay addHeatOverlay(HeatOverlayOptions paramHeatOverlayOptions)
  {
    if (this.mapDestroyed);
    while (true)
    {
      return null;
      if (this.heatOverlayManager != null)
        break;
      if (this.viewControl == null)
        continue;
      this.heatOverlayManager = new gb(this.viewControl.b());
    }
    return this.heatOverlayManager.a(paramHeatOverlayOptions);
  }

  public final Marker addMarker(MarkerOptions paramMarkerOptions)
  {
    if (this.mapDestroyed);
    while (true)
    {
      return null;
      if (this.markerManager != null)
        break;
      if ((this.mapV == null) || (this.viewControl == null))
        continue;
      this.markerManager = new gs(this.mapV, this.viewControl.b());
    }
    if (this.markerControl == null)
      this.markerControl = new gr(this.markerManager);
    return this.markerControl.a(paramMarkerOptions, this.markerControl);
  }

  public final Polygon addPolygon(PolygonOptions paramPolygonOptions)
  {
    if (this.mapDestroyed)
      return null;
    if (this.polygonManager == null)
      this.polygonManager = new gv(this.viewControl.b());
    if (this.polygonControl == null)
      this.polygonControl = new gu(this.polygonManager);
    return this.polygonControl.a(paramPolygonOptions);
  }

  public final Polyline addPolyline(PolylineOptions paramPolylineOptions)
  {
    if (this.mapDestroyed);
    while (true)
    {
      return null;
      if (this.polylineManager != null)
        break;
      if (this.viewControl == null)
        continue;
      this.polylineManager = new gx(this.viewControl.b());
    }
    if (this.polyLineControl == null)
      this.polyLineControl = new gw(this.polylineManager);
    return this.polyLineControl.a(paramPolylineOptions);
  }

  hq addWorldMapOverlay(hp paramhp)
  {
    if (this.mapDestroyed);
    while (true)
    {
      return null;
      if (this.worldMapOverlayManager != null)
        break;
      if (this.viewControl == null)
        continue;
      this.worldMapOverlayManager = new hr(this.viewControl.b());
    }
    return this.worldMapOverlayManager.a(paramhp);
  }

  public final void animateCamera(CameraUpdate paramCameraUpdate)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramCameraUpdate, 500L, null);
  }

  public final void animateCamera(CameraUpdate paramCameraUpdate, long paramLong, TencentMap.CancelableCallback paramCancelableCallback)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramCameraUpdate, paramLong, paramCancelableCallback);
  }

  public final void animateCamera(CameraUpdate paramCameraUpdate, TencentMap.CancelableCallback paramCancelableCallback)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramCameraUpdate, 500L, paramCancelableCallback);
  }

  public final void animateToNaviPosition(LatLng paramLatLng, float paramFloat1, float paramFloat2)
  {
    if (this.mapDestroyed);
    do
      return;
    while (this.mapDestroyed);
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramLatLng, paramFloat1, paramFloat2, true);
  }

  public final void animateToNaviPosition(LatLng paramLatLng, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramLatLng, paramFloat1, paramFloat2, paramFloat3, true);
  }

  public final void animateToNaviPosition(LatLng paramLatLng, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramLatLng, paramFloat1, paramFloat2, paramFloat3, paramBoolean);
  }

  public final void animateToNaviPosition2(LatLng paramLatLng, float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.b(paramLatLng, paramFloat1, paramFloat2, paramFloat3, paramBoolean);
  }

  @Deprecated
  public float calNaviLevel(LatLngBounds paramLatLngBounds, float paramFloat, int paramInt, boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramLatLngBounds, paramFloat, paramInt, paramBoolean);
  }

  @Deprecated
  public float calNaviLevel2(LatLng paramLatLng1, LatLng paramLatLng2, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramLatLng1, paramLatLng2, paramFloat1, paramFloat2, paramInt, paramBoolean);
  }

  @Deprecated
  public float calculateZoomToSpanLevel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, LatLng paramLatLng1, LatLng paramLatLng2, LatLng paramLatLng3)
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramInt1, paramInt2, paramInt3, paramInt4, paramLatLng1, paramLatLng2, paramLatLng3);
  }

  public final void clear()
  {
    if (this.mapDestroyed);
    do
    {
      return;
      if (this.polyLineControl != null)
        this.polyLineControl.b();
      if (this.polygonControl != null)
        this.polygonControl.b();
      if (this.circleControl == null)
        continue;
      this.circleControl.b();
    }
    while (this.markerControl == null);
    this.markerControl.b();
  }

  void clearWorldMapCache()
  {
    if (this.mapDestroyed);
    while (true)
    {
      return;
      if (this.worldMapOverlayManager != null)
        break;
      if (this.viewControl == null)
        continue;
      this.worldMapOverlayManager = new hr(this.viewControl.b());
    }
    this.worldMapOverlayManager.b();
  }

  public final CameraPosition getCameraPosition()
  {
    if (this.mapDestroyed)
      return null;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.b();
  }

  public final String getCityName(LatLng paramLatLng)
  {
    if (this.mapDestroyed)
      return "";
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramLatLng);
  }

  final hf getInfoWindowAnimationManager()
  {
    if (this.mapDestroyed)
      return null;
    if (this.markerManager == null)
      this.markerManager = new gs(this.mapV, this.viewControl.b());
    if (this.markerControl == null)
      this.markerControl = new gr(this.markerManager);
    return this.markerControl.c();
  }

  float getLogoMarginRate(int paramInt)
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.b(paramInt);
  }

  public final int getMapType()
  {
    if (this.mapDestroyed)
      return -1;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.f();
  }

  public MapView getMapView()
  {
    return this.mapV;
  }

  public final float getMaxZoomLevel()
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.c();
  }

  public final float getMinZoomLevel()
  {
    if (this.mapDestroyed)
      return 0.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.d();
  }

  public final Location getMyLocation()
  {
    if (this.mapDestroyed)
      return null;
    initForMylocation();
    return this.locationControl.b();
  }

  public final Projection getProjection()
  {
    if (this.mapDestroyed)
      return null;
    if (this.mapProjection == null)
      this.mapProjection = new Projection(this.mapManager);
    return this.mapProjection;
  }

  void getScreenShot(Handler paramHandler, Bitmap.Config paramConfig)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramHandler, paramConfig);
  }

  public void getScreenShot(TencentMap.ScreenShotReadyCallback paramScreenShotReadyCallback)
  {
    if (this.mapDestroyed)
    {
      paramScreenShotReadyCallback.onScreenShotReady(null);
      return;
    }
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramScreenShotReadyCallback, Bitmap.Config.ARGB_8888);
  }

  public void getScreenShot(TencentMap.ScreenShotReadyCallback paramScreenShotReadyCallback, Bitmap.Config paramConfig)
  {
    if (this.mapDestroyed)
    {
      paramScreenShotReadyCallback.onScreenShotReady(null);
      return;
    }
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramScreenShotReadyCallback, paramConfig);
  }

  public final UiSettings getUiSettings()
  {
    if (this.mapDestroyed)
      return null;
    if (this.uiSetting == null)
    {
      if (this.uiSettingControl == null)
      {
        if (this.uiSettingManager == null)
        {
          View localView = getViewDelegate().b();
          this.uiSettingManager = new hc(this.mapV, localView);
        }
        this.uiSettingControl = new hb(this.uiSettingManager);
      }
      this.uiSetting = new UiSettings(this.uiSettingControl);
    }
    return this.uiSetting;
  }

  public String getVersion()
  {
    if (this.mapDestroyed)
      return "";
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.n();
  }

  hd getViewDelegate()
  {
    if (this.viewControl == null)
      this.viewControl = new hd(this.mapManager);
    return this.viewControl;
  }

  final float getZoomToSpanLevel(LatLng paramLatLng1, LatLng paramLatLng2)
  {
    if (this.mapDestroyed)
      return 0.0F;
    if ((paramLatLng1 == null) || (paramLatLng2 == null))
      return -1.0F;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramLatLng1, paramLatLng2);
  }

  public boolean isDestroyed()
  {
    return this.mapDestroyed;
  }

  public final boolean isMyLocationEnabled()
  {
    if (this.mapDestroyed)
      return false;
    initForMylocation();
    return this.locationControl.e();
  }

  public final boolean isTrafficEnabled()
  {
    if (this.mapDestroyed)
      return false;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.g();
  }

  public final void moveCamera(CameraUpdate paramCameraUpdate)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramCameraUpdate);
  }

  void onDestroy()
  {
    if (this.glMapControl != null)
      this.glMapControl.m();
    releaseControlers();
    releaseManagers();
    this.mapDestroyed = true;
  }

  void onPause()
  {
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.l();
  }

  void onRestart()
  {
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.h();
  }

  void onResume()
  {
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.i();
  }

  void onStart()
  {
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.j();
  }

  void onStop()
  {
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.k();
  }

  @Deprecated
  public void setCompassExtraPadding(int paramInt)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.f(paramInt);
  }

  void setDrawPillarWith2DStyle(boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    this.mapManager.e(paramBoolean);
  }

  public final void setInfoWindowAdapter(TencentMap.InfoWindowAdapter paramInfoWindowAdapter)
  {
    if (this.mapDestroyed)
      return;
    if (this.markerManager == null)
      this.markerManager = new gs(this.mapV, this.viewControl.b());
    if (this.markerControl == null)
      this.markerControl = new gr(this.markerManager);
    this.markerControl.a(paramInfoWindowAdapter);
  }

  final void setInfoWindowStillVisible(boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.c(paramBoolean);
  }

  public final void setLocationSource(LocationSource paramLocationSource)
  {
    if (this.mapDestroyed)
      return;
    initForMylocation();
    this.locationControl.a(paramLocationSource);
  }

  final void setLogoAnchor(int paramInt)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.c(paramInt);
  }

  final void setLogoBottomMargin(int paramInt)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.e(paramInt);
  }

  final void setLogoLeftMargin(int paramInt)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.d(paramInt);
  }

  final void setLogoMarginRate(int paramInt, float paramFloat)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramInt, paramFloat);
  }

  final void setLogoVisible(boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.b(paramBoolean);
  }

  @Deprecated
  public void setMapScreenCenterProportion(float paramFloat1, float paramFloat2)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.c(paramFloat1, paramFloat2);
  }

  public final void setMapType(int paramInt)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramInt);
  }

  public final void setMyLocationEnabled(boolean paramBoolean)
  {
    if (this.mapDestroyed);
    do
    {
      return;
      initForMylocation();
      if (paramBoolean)
        continue;
      this.locationControl.d();
      return;
    }
    while (isMyLocationEnabled() == true);
    this.locationControl.c();
  }

  @Deprecated
  public void setNaviFixingProportion(float paramFloat1, float paramFloat2)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramFloat1, paramFloat2);
  }

  @Deprecated
  public void setNaviFixingProportion2D(float paramFloat1, float paramFloat2)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.b(paramFloat1, paramFloat2);
  }

  public final void setOnCameraChangeListener(TencentMap.OnCameraChangeListener paramOnCameraChangeListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnCameraChangeListener);
  }

  public final void setOnCompassClickedListener(TencentMap.OnCompassClickedListener paramOnCompassClickedListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnCompassClickedListener);
  }

  public final void setOnInfoWindowClickListener(TencentMap.OnInfoWindowClickListener paramOnInfoWindowClickListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnInfoWindowClickListener);
  }

  public final void setOnMapClickListener(TencentMap.OnMapClickListener paramOnMapClickListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnMapClickListener);
  }

  public final void setOnMapLoadedCallback(TencentMap.OnMapLoadedCallback paramOnMapLoadedCallback)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnMapLoadedCallback);
  }

  public final void setOnMapLongClickListener(TencentMap.OnMapLongClickListener paramOnMapLongClickListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnMapLongClickListener);
  }

  public final void setOnMarkerClickListener(TencentMap.OnMarkerClickListener paramOnMarkerClickListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramOnMarkerClickListener);
  }

  public final void setOnMarkerDragListener(TencentMap.OnMarkerDragListener paramOnMarkerDragListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.markerManager == null)
      this.markerManager = new gs(this.mapV, this.viewControl.b());
    if (this.markerControl == null)
      this.markerControl = new gr(this.markerManager);
    this.markerControl.a(paramOnMarkerDragListener);
  }

  public final void setOnMyLocationButtonClickListener(TencentMap.OnMyLocationButtonClickListener paramOnMyLocationButtonClickListener)
  {
    if ((this.mapDestroyed) || (this.uiSettingManager == null))
      return;
    this.uiSettingManager.a(paramOnMyLocationButtonClickListener);
  }

  public final void setOnMyLocationChangeListener(TencentMap.OnMyLocationChangeListener paramOnMyLocationChangeListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.locationManager == null)
      initForMylocation();
    this.locationManager.a(paramOnMyLocationChangeListener);
  }

  public final void setOnPolylineClickListener(TencentMap.OnPolylineClickListener paramOnPolylineClickListener)
  {
    if (this.mapDestroyed);
    while (true)
    {
      return;
      if (this.polylineManager != null)
        break;
      if (this.viewControl == null)
        continue;
      this.polylineManager = new gx(this.viewControl.b());
    }
    if (this.polyLineControl == null)
      this.polyLineControl = new gw(this.polylineManager);
    this.polyLineControl.a(paramOnPolylineClickListener);
  }

  void setOnTop(boolean paramBoolean)
  {
    this.mapManager.d(paramBoolean);
  }

  public final void setTencentMapGestureListener(TencentMapGestureListener paramTencentMapGestureListener)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramTencentMapGestureListener);
  }

  final int setTrafficData(byte[] paramArrayOfByte, String paramString)
  {
    if (this.mapDestroyed)
      return -1;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    return this.glMapControl.a(paramArrayOfByte, paramString);
  }

  public final void setTrafficEnabled(boolean paramBoolean)
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.a(paramBoolean);
  }

  public final void stopAnimation()
  {
    if (this.mapDestroyed)
      return;
    if (this.glMapControl == null)
      this.glMapControl = new ga(this.mapManager);
    this.glMapControl.e();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.TencentMap
 * JD-Core Version:    0.6.0
 */