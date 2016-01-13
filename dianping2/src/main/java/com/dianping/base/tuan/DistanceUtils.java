package com.dianping.base.tuan;

import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;

public class DistanceUtils
{
  public static String calculateDistance(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    double d = ConfigHelper.configDistanceFactor;
    if (d <= 0.0D);
    do
    {
      do
        return null;
      while ((paramDouble1 == 0.0D) || (paramDouble2 == 0.0D) || (paramDouble3 == 0.0D) || (paramDouble4 == 0.0D));
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(paramDouble3, paramDouble4)) * d;
    }
    while ((Double.isNaN(paramDouble1)) || (paramDouble1 <= 0.0D));
    return getDistanceText((int)Math.round(paramDouble1 / 10.0D) * 10, paramDouble3, paramDouble4);
  }

  protected static String getDistanceText(long paramLong, double paramDouble1, double paramDouble2)
  {
    if ((paramDouble1 == 0.0D) && (paramDouble2 == 0.0D))
      return "";
    String str;
    if (paramLong <= 100L)
      str = "<100m";
    while (true)
    {
      return str;
      if (paramLong < 1000L)
      {
        str = paramLong + "m";
        continue;
      }
      if (paramLong < 10000L)
      {
        paramLong /= 100L;
        str = paramLong / 10L + "." + paramLong % 10L + "km";
        continue;
      }
      if (paramLong < 100000L)
      {
        str = paramLong / 1000L + "km";
        continue;
      }
      str = "";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.DistanceUtils
 * JD-Core Version:    0.6.0
 */