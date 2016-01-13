package com.dianping.base.share.util;

import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;

public class ShopUtil
{
  public static String getShopAddress(DPObject paramDPObject)
  {
    return paramDPObject.getString("Address");
  }

  public static String getShopCategory(DPObject paramDPObject)
  {
    return paramDPObject.getString("CategoryName");
  }

  public static String getShopImageUrl(DPObject paramDPObject)
  {
    return paramDPObject.getString("DefaultPic");
  }

  public static String getShopName(DPObject paramDPObject)
  {
    return DPObjectUtils.getShopFullName(paramDPObject);
  }

  public static String getShopPrice(DPObject paramDPObject)
  {
    return paramDPObject.getString("PriceText");
  }

  public static String getShopRegion(DPObject paramDPObject)
  {
    return paramDPObject.getString("RegionName");
  }

  public static String getShopShareContent(DPObject paramDPObject)
  {
    return paramDPObject.getString("ShareContent");
  }

  public static String getShopStar(DPObject paramDPObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramDPObject.getInt("ShopPower");
    int k = j / 10;
    int i = 0;
    while (i < k)
    {
      localStringBuilder.append("★");
      i += 1;
    }
    if (j % 10 > 0)
      localStringBuilder.append("☆");
    return localStringBuilder.toString();
  }

  public static String getShopUrl(DPObject paramDPObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://dpurl.cn/m/s");
    localStringBuilder.append(paramDPObject.getInt("ID"));
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.util.ShopUtil
 * JD-Core Version:    0.6.0
 */