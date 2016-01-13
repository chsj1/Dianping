package com.dianping.locationservice.impl286.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil
{
  private static final String COMMON_DATE = "yyyy-MM-dd-HH-mm-ss";

  public static int dip2px(Context paramContext, float paramFloat)
  {
    return (int)(paramFloat * paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  public static double format(double paramDouble, int paramInt)
  {
    double d = Math.pow(10.0D, paramInt);
    return Math.round(paramDouble * d) / d;
  }

  public static String getCurrentTime()
  {
    return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
  }

  public static String intToIpAddress(int paramInt)
  {
    return (paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24);
  }

  public static int px2dip(Context paramContext, float paramFloat)
  {
    return (int)(paramFloat / paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.util.CommonUtil
 * JD-Core Version:    0.6.0
 */