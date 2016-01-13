package com.dianping.selectdish;

public class NumberUtils
{
  public static String convertDoubleToIntegerIfNecessary(double paramDouble)
  {
    if (paramDouble % 1.0D == 0.0D)
      return String.valueOf(()paramDouble);
    return String.valueOf(paramDouble);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.NumberUtils
 * JD-Core Version:    0.6.0
 */