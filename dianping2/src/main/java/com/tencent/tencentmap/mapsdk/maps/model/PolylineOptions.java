package com.tencent.tencentmap.mapsdk.maps.model;

import com.tencent.tencentmap.mapsdk.maps.a.eg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class PolylineOptions
{
  private final List<LatLng> a = new ArrayList();
  private float b = 12.0F;
  private int c = eg.n;
  private float d = 0.0F;
  private boolean e = true;
  private boolean f = false;
  private boolean g;
  private float h = 1.0F;

  PolylineOptions a(boolean paramBoolean)
  {
    this.f = paramBoolean;
    return this;
  }

  public PolylineOptions add(LatLng paramLatLng, LatLng[] paramArrayOfLatLng)
  {
    this.a.add(paramLatLng);
    if (paramArrayOfLatLng != null)
      add(paramArrayOfLatLng);
    return this;
  }

  public PolylineOptions add(LatLng[] paramArrayOfLatLng)
  {
    this.a.addAll(Arrays.asList(paramArrayOfLatLng));
    return this;
  }

  public PolylineOptions addAll(Iterable<LatLng> paramIterable)
  {
    if (paramIterable != null)
    {
      paramIterable = paramIterable.iterator();
      while (paramIterable.hasNext())
        add((LatLng)paramIterable.next(), new LatLng[0]);
    }
    return this;
  }

  @Deprecated
  public PolylineOptions arrow(boolean paramBoolean)
  {
    this.g = paramBoolean;
    return this;
  }

  public PolylineOptions color(int paramInt)
  {
    this.c = paramInt;
    return this;
  }

  public int getColor()
  {
    return this.c;
  }

  public List<LatLng> getPoints()
  {
    return this.a;
  }

  public float getWidth()
  {
    return this.b;
  }

  public float getZIndex()
  {
    return this.d;
  }

  @Deprecated
  public boolean isArrow()
  {
    return this.g;
  }

  public boolean isGeodesic()
  {
    return this.f;
  }

  public boolean isVisible()
  {
    return this.e;
  }

  public void setLatLngs(List<LatLng> paramList)
  {
    this.a.clear();
    if (paramList == null)
      return;
    this.a.addAll(paramList);
  }

  public PolylineOptions visible(boolean paramBoolean)
  {
    this.e = paramBoolean;
    return this;
  }

  public PolylineOptions width(float paramFloat)
  {
    this.b = paramFloat;
    return this;
  }

  public PolylineOptions zIndex(float paramFloat)
  {
    this.d = paramFloat;
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions
 * JD-Core Version:    0.6.0
 */