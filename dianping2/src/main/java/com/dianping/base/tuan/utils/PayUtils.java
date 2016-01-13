package com.dianping.base.tuan.utils;

public class PayUtils
{
  public static final String RMB = "￥";

  public static String generateGAPrepayInfos(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null))
      return null;
    return generatePrepayInfos(paramString1.split(","), paramString2.split(","));
  }

  public static String generateGAPrepayInfos(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return null;
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < paramArrayOfString.length - 1)
    {
      localStringBuilder.append("#").append(paramArrayOfString[i]).append("|");
      i += 1;
    }
    localStringBuilder.append("#").append(paramArrayOfString[(paramArrayOfString.length - 1)]);
    return localStringBuilder.toString();
  }

  public static String generateGAPrepayInfos(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    return generatePrepayInfos(paramArrayOfString1, paramArrayOfString2);
  }

  public static String generatePrepayInfos(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if ((paramArrayOfString1 == null) || (paramArrayOfString1.length == 0) || (paramArrayOfString2 == null) || (paramArrayOfString2.length == 0) || (paramArrayOfString1.length != paramArrayOfString2.length))
      return null;
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < paramArrayOfString1.length - 1)
    {
      localStringBuilder.append(paramArrayOfString1[i]).append("#").append(paramArrayOfString2[i]).append("|");
      i += 1;
    }
    localStringBuilder.append(paramArrayOfString1[(paramArrayOfString1.length - 1)]).append("#").append(paramArrayOfString2[(paramArrayOfString1.length - 1)]);
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.utils.PayUtils
 * JD-Core Version:    0.6.0
 */