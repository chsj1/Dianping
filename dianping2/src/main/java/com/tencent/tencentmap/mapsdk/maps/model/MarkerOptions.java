package com.tencent.tencentmap.mapsdk.maps.model;

import com.tencent.tencentmap.mapsdk.maps.a.fv;

public final class MarkerOptions
{
  private LatLng a;
  private String b = "";
  private String c;
  private BitmapDescriptor d;
  private float e = 0.5F;
  private float f = 1.0F;
  private boolean g;
  private boolean h = true;
  private float i = 0.0F;
  private float j = 1.0F;
  private float k = 0.0F;
  private boolean l = true;
  private boolean m = false;

  public MarkerOptions alpha(float paramFloat)
  {
    this.j = paramFloat;
    return this;
  }

  public MarkerOptions anchor(float paramFloat1, float paramFloat2)
  {
    this.e = paramFloat1;
    this.f = paramFloat2;
    return this;
  }

  public MarkerOptions draggable(boolean paramBoolean)
  {
    this.g = paramBoolean;
    return this;
  }

  public float getAlpha()
  {
    return this.j;
  }

  public float getAnchorU()
  {
    return this.e;
  }

  public float getAnchorV()
  {
    return this.f;
  }

  public BitmapDescriptor getIcon()
  {
    if (this.d == null)
      this.d = new BitmapDescriptor(new fv(5));
    return this.d;
  }

  public LatLng getPosition()
  {
    return this.a;
  }

  public float getRotateAngle()
  {
    return this.i;
  }

  public float getRotation()
  {
    return this.i;
  }

  public String getSnippet()
  {
    return this.c;
  }

  public String getTitle()
  {
    return this.b;
  }

  public float getZIndex()
  {
    return this.k;
  }

  public MarkerOptions icon(BitmapDescriptor paramBitmapDescriptor)
  {
    this.d = paramBitmapDescriptor;
    return this;
  }

  public MarkerOptions infoWindowEnable(boolean paramBoolean)
  {
    this.l = paramBoolean;
    return this;
  }

  public MarkerOptions is3D(boolean paramBoolean)
  {
    this.m = paramBoolean;
    return this;
  }

  public boolean is3D()
  {
    return this.m;
  }

  public boolean isDraggable()
  {
    return this.g;
  }

  public boolean isInfoWindowEnable()
  {
    return this.l;
  }

  public boolean isVisible()
  {
    return this.h;
  }

  public MarkerOptions position(LatLng paramLatLng)
  {
    this.a = paramLatLng;
    return this;
  }

  public MarkerOptions rotateAngle(float paramFloat)
  {
    this.i = paramFloat;
    return this;
  }

  public MarkerOptions rotation(float paramFloat)
  {
    this.i = paramFloat;
    return this;
  }

  public MarkerOptions snippet(String paramString)
  {
    this.c = paramString;
    return this;
  }

  public MarkerOptions title(String paramString)
  {
    this.b = paramString;
    return this;
  }

  public MarkerOptions visible(boolean paramBoolean)
  {
    this.h = paramBoolean;
    return this;
  }

  public MarkerOptions zIndex(float paramFloat)
  {
    this.k = paramFloat;
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions
 * JD-Core Version:    0.6.0
 */