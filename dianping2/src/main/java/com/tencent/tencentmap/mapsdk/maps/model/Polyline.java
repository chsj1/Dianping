package com.tencent.tencentmap.mapsdk.maps.model;

import com.tencent.tencentmap.mapsdk.maps.a.gw;
import java.util.List;

public final class Polyline
{

  @Deprecated
  public static final int DARK_BLUE = 6;

  @Deprecated
  public static final int DASHED = 33;

  @Deprecated
  public static final int GREY = 0;

  @Deprecated
  public static final int LIGHT_BLUE = 1;

  @Deprecated
  public static final int RED = 2;

  @Deprecated
  public static final int WHITE_BLUE = 19;

  @Deprecated
  public static final int YELLOW = 3;
  private PolylineOptions a = null;
  private String b = "";
  private gw c = null;

  public Polyline(PolylineOptions paramPolylineOptions, gw paramgw, String paramString)
  {
    this.b = paramString;
    this.a = paramPolylineOptions;
    this.c = paramgw;
  }

  @Deprecated
  public void addTurnArrow(int paramInt1, int paramInt2)
  {
    this.c.a(this.b, paramInt1, paramInt2);
  }

  @Deprecated
  public void cleanTurnArrow()
  {
    this.c.b(this.b);
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Polyline))
      return false;
    paramObject = (Polyline)paramObject;
    return this.b.equals(paramObject.b);
  }

  public int getColor()
  {
    return this.a.getColor();
  }

  public String getId()
  {
    return this.b;
  }

  public List<LatLng> getPoints()
  {
    return this.a.getPoints();
  }

  public float getWidth()
  {
    return this.a.getWidth();
  }

  public float getZIndex()
  {
    return this.a.getZIndex();
  }

  public int hashCode()
  {
    return this.b.hashCode();
  }

  public boolean isGeodesic()
  {
    return this.a.isGeodesic();
  }

  public boolean isVisible()
  {
    return this.a.isVisible();
  }

  public void remove()
  {
    if (this.c == null)
      return;
    this.c.a(this.b);
  }

  @Deprecated
  public void setArrow(boolean paramBoolean)
  {
    this.c.c(this.b, paramBoolean);
    this.a.arrow(paramBoolean);
  }

  public void setColor(int paramInt)
  {
    this.c.a(this.b, paramInt);
    this.a.color(paramInt);
  }

  public void setColors(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.c.a(this.b, paramArrayOfInt1, paramArrayOfInt2);
  }

  public void setGeodesic(boolean paramBoolean)
  {
    this.c.a(this.b, paramBoolean);
    this.a.a(paramBoolean);
  }

  public void setPoints(List<LatLng> paramList)
  {
    this.c.a(this.b, paramList);
    this.a.setLatLngs(paramList);
  }

  public void setVisible(boolean paramBoolean)
  {
    this.c.b(this.b, paramBoolean);
    this.a.visible(paramBoolean);
  }

  public void setWidth(float paramFloat)
  {
    this.c.a(this.b, paramFloat);
    this.a.width(paramFloat);
  }

  public void setZIndex(float paramFloat)
  {
    this.c.b(this.b, paramFloat);
    this.a.zIndex(paramFloat);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.Polyline
 * JD-Core Version:    0.6.0
 */