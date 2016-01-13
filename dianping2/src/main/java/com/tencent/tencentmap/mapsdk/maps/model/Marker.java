package com.tencent.tencentmap.mapsdk.maps.model;

import com.tencent.tencentmap.mapsdk.maps.a.gr;

public final class Marker
{
  private MarkerOptions a = null;
  private String b = "";
  private boolean c = false;
  private gr d = null;

  public Marker(MarkerOptions paramMarkerOptions, gr paramgr, String paramString)
  {
    this.b = paramString;
    this.a = paramMarkerOptions;
    this.d = paramgr;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Marker))
      return false;
    paramObject = (Marker)paramObject;
    return this.b.equals(paramObject.b);
  }

  public float getAlpha()
  {
    return this.a.getAlpha();
  }

  public String getId()
  {
    return this.b;
  }

  public LatLng getPosition()
  {
    LatLng localLatLng2 = this.d.b(this.b);
    LatLng localLatLng1 = localLatLng2;
    if (localLatLng2 == null)
      localLatLng1 = this.a.getPosition();
    return localLatLng1;
  }

  public float getRotateAngle()
  {
    if (this.d == null)
      return 0.0F;
    return this.d.f(this.b);
  }

  public float getRotation()
  {
    if (this.d == null)
      return 0.0F;
    return this.d.f(this.b);
  }

  public String getSnippet()
  {
    return this.a.getSnippet();
  }

  public String getTitle()
  {
    return this.a.getTitle();
  }

  public float getZIndex()
  {
    if (this.d == null)
      return 0.0F;
    return this.d.i(this.b);
  }

  public int hashCode()
  {
    return this.b.hashCode();
  }

  public void hideInfoWindow()
  {
    if (this.d == null)
      return;
    this.d.d(this.b);
  }

  public boolean isClickable()
  {
    if (this.d == null)
      return false;
    return this.d.h(this.b);
  }

  public boolean isDraggable()
  {
    return this.a.isDraggable();
  }

  public boolean isInfoWindowEnable()
  {
    return this.a.isInfoWindowEnable();
  }

  public boolean isInfoWindowShown()
  {
    if (this.d == null)
      return false;
    return this.d.e(this.b);
  }

  @Deprecated
  public boolean isNaviState()
  {
    return this.c;
  }

  public boolean isVisible()
  {
    if (this.d == null)
      return false;
    return this.a.isVisible();
  }

  public void remove()
  {
    if (this.d == null)
      return;
    this.d.a(this.b);
  }

  public void setAlpha(float paramFloat)
  {
    if (this.d == null)
      return;
    this.d.b(this.b, paramFloat);
    this.a.alpha(paramFloat);
  }

  public void setAnchor(float paramFloat1, float paramFloat2)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramFloat1, paramFloat2);
    this.a.anchor(paramFloat1, paramFloat2);
  }

  public void setAnimation(Animation paramAnimation)
  {
    if ((this.d == null) || (paramAnimation == null))
      return;
    this.d.a(this.b, paramAnimation);
  }

  public void setAnimationListener(AnimationListener paramAnimationListener)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramAnimationListener);
  }

  public void setClickable(boolean paramBoolean)
  {
    if (this.d == null)
      return;
    this.d.c(this.b, paramBoolean);
  }

  public void setDraggable(boolean paramBoolean)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramBoolean);
    this.a.draggable(paramBoolean);
  }

  public void setIcon(BitmapDescriptor paramBitmapDescriptor)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramBitmapDescriptor);
    this.a.icon(paramBitmapDescriptor);
  }

  public void setInfoWindowEnable(boolean paramBoolean)
  {
    if (this.d == null)
      return;
    this.a.infoWindowEnable(paramBoolean);
  }

  public void setMarkerOptions(MarkerOptions paramMarkerOptions)
  {
    if (paramMarkerOptions == null)
      return;
    this.d.a(this.b, paramMarkerOptions);
    this.a.position(paramMarkerOptions.getPosition());
    this.a.anchor(paramMarkerOptions.getAnchorU(), paramMarkerOptions.getAnchorV());
    this.a.title(paramMarkerOptions.getTitle());
    this.a.snippet(paramMarkerOptions.getSnippet());
    this.a.draggable(paramMarkerOptions.isDraggable());
    this.a.visible(paramMarkerOptions.isVisible());
    this.a.rotation(paramMarkerOptions.getRotation());
    this.a.icon(paramMarkerOptions.getIcon());
    this.a.alpha(paramMarkerOptions.getAlpha());
    this.a.zIndex(paramMarkerOptions.getZIndex());
  }

  @Deprecated
  public void setNaviState(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramBoolean1, paramBoolean2);
    this.c = paramBoolean1;
  }

  public void setPosition(LatLng paramLatLng)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramLatLng);
    this.a.position(paramLatLng);
  }

  public void setRotateAngle(float paramFloat)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramFloat);
    this.a.rotateAngle(paramFloat);
  }

  public void setRotation(float paramFloat)
  {
    if (this.d == null)
      return;
    this.d.a(this.b, paramFloat);
    this.a.rotation(paramFloat);
  }

  public void setSnippet(String paramString)
  {
    if (this.d == null)
      return;
    this.a.snippet(paramString);
    this.d.a(this.b, paramString);
  }

  public void setTitle(String paramString)
  {
    if (this.d == null)
      return;
    this.a.title(paramString);
    this.d.b(this.b, paramString);
  }

  public void setVisible(boolean paramBoolean)
  {
    if (this.d == null)
      return;
    this.d.b(this.b, paramBoolean);
    this.a.visible(paramBoolean);
  }

  public void setZIndex(float paramFloat)
  {
    this.d.c(this.b, paramFloat);
    this.a.zIndex(paramFloat);
  }

  public void showInfoWindow()
  {
    if (this.d == null)
      return;
    this.d.c(this.b);
  }

  public boolean startAnimation()
  {
    if (this.d == null)
      return false;
    return this.d.g(this.b);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.Marker
 * JD-Core Version:    0.6.0
 */