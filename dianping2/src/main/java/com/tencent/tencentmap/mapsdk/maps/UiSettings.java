package com.tencent.tencentmap.mapsdk.maps;

import com.tencent.tencentmap.mapsdk.maps.a.hb;

public final class UiSettings
{
  private hb uiControl = null;

  UiSettings(hb paramhb)
  {
    this.uiControl = paramhb;
  }

  public boolean isCompassEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.d();
    return false;
  }

  public boolean isMyLocationButtonEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.e();
    return false;
  }

  public boolean isRotateGesturesEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.i();
    return false;
  }

  public boolean isScrollGesturesEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.f();
    return false;
  }

  public boolean isTiltGesturesEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.h();
    return false;
  }

  public boolean isZoomControlsEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.c();
    return false;
  }

  public boolean isZoomGesturesEnabled()
  {
    if (this.uiControl != null)
      return this.uiControl.g();
    return false;
  }

  public void setAllGesturesEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.h(paramBoolean);
  }

  public void setCompassEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.b(paramBoolean);
  }

  public void setMyLocationButtonEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.c(paramBoolean);
  }

  public void setRotateGesturesEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.g(paramBoolean);
  }

  public void setScaleViewPosition(int paramInt)
  {
    if (this.uiControl != null)
      this.uiControl.a(paramInt);
  }

  public void setScrollGesturesEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.d(paramBoolean);
  }

  public void setTiltGesturesEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.f(paramBoolean);
  }

  public void setZoomControlsEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.a(paramBoolean);
  }

  public void setZoomGesturesEnabled(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.e(paramBoolean);
  }

  public void showScaleView(boolean paramBoolean)
  {
    if (this.uiControl != null)
      this.uiControl.i(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.UiSettings
 * JD-Core Version:    0.6.0
 */