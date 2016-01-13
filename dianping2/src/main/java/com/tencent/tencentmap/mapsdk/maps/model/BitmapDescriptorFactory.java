package com.tencent.tencentmap.mapsdk.maps.model;

import android.graphics.Bitmap;
import com.tencent.tencentmap.mapsdk.maps.a.eg;
import com.tencent.tencentmap.mapsdk.maps.a.fv;

public final class BitmapDescriptorFactory
{
  public static final float HUE_AZURE = 210.0F;
  public static final float HUE_BLUE = 240.0F;
  public static final float HUE_CYAN = 180.0F;
  public static final float HUE_GREEN = 120.0F;
  public static final float HUE_MAGENTA = 300.0F;
  public static final float HUE_ORANGE = 30.0F;
  public static final float HUE_RED = 0.0F;
  public static final float HUE_ROSE = 330.0F;
  public static final float HUE_VIOLET = 270.0F;
  public static final float HUE_YELLOW = 60.0F;

  public static BitmapDescriptor defaultMarker()
  {
    return new BitmapDescriptor(new fv(5));
  }

  public static BitmapDescriptor defaultMarker(float paramFloat)
  {
    fv localfv = new fv(6);
    localfv.a(paramFloat);
    return new BitmapDescriptor(localfv);
  }

  public static BitmapDescriptor fromAsset(String paramString)
  {
    fv localfv = new fv(2);
    localfv.a(paramString);
    return new BitmapDescriptor(localfv);
  }

  public static BitmapDescriptor fromBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return null;
    paramBitmap = eg.a(paramBitmap);
    fv localfv = new fv(7);
    localfv.a(paramBitmap);
    return new BitmapDescriptor(localfv);
  }

  public static BitmapDescriptor fromFile(String paramString)
  {
    fv localfv = new fv(3);
    localfv.b(paramString);
    return new BitmapDescriptor(localfv);
  }

  public static BitmapDescriptor fromPath(String paramString)
  {
    fv localfv = new fv(4);
    localfv.c(paramString);
    return new BitmapDescriptor(localfv);
  }

  public static BitmapDescriptor fromResource(int paramInt)
  {
    fv localfv = new fv(1);
    localfv.a(paramInt);
    return new BitmapDescriptor(localfv);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory
 * JD-Core Version:    0.6.0
 */