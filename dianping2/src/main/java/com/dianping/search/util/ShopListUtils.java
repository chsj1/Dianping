package com.dianping.search.util;

import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.data.ShopListConst;

public class ShopListUtils extends com.dianping.base.shoplist.util.ShopListUtils
{
  public static final DPObject DEFAULT_ALL_FLOOR;
  public static final DPObject DEFAULT_ALL_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).generate();
  public static final int FILTER_TYPE_CATEGORY = 1;
  public static final int FILTER_TYPE_FLOOR = 4;
  public static final int FILTER_TYPE_REGION = 2;
  public static final int FILTER_TYPE_SORT = 3;

  static
  {
    DEFAULT_ALL_FLOOR = new DPObject("Pair").edit().putInt("ID", -1000).putString("Name", "全部楼层").putInt("Type", 4).generate();
  }

  public static DPObject[] genDefaultFilter(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return new DPObject[] { ShopListConst.ALL_CATEGORY };
    case 2:
      return new DPObject[] { DEFAULT_ALL_REGION };
    case 3:
      return new DPObject[] { ShopListConst.DEFAULT_SORT };
    case 4:
    }
    return new DPObject[] { DEFAULT_ALL_FLOOR };
  }

  public static String getFloorStrName(int paramInt)
  {
    if (paramInt >= 0)
      return paramInt + "F";
    return "B" + -paramInt;
  }

  public static boolean shouldShowDeal(DPObject paramDPObject)
  {
    return (paramDPObject.getObject("Deals") != null) && (paramDPObject.getObject("Deals").getArray("List") != null) && (paramDPObject.getObject("Deals").getArray("List").length > 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.util.ShopListUtils
 * JD-Core Version:    0.6.0
 */