package com.dianping.base.widget;

import java.util.HashMap;
import java.util.Map;

public enum ViewItemType
{
  private static final Map<Integer, ViewItemType> _map;
  private int value;

  static
  {
    TUAN_DEAL = new ViewItemType("TUAN_DEAL", 1, 1);
    HUI = new ViewItemType("HUI", 2, 2);
    AGGRATION_ITEMS = new ViewItemType("AGGRATION_ITEMS", 3, 3);
    SHOP = new ViewItemType("SHOP", 4, 4);
    RIGHT_TAG = new ViewItemType("RIGHT_TAG", 5, 5);
    TITLE_TIP = new ViewItemType("TITLE_TIP", 6, 6);
    BANNER = new ViewItemType("BANNER", 7, 7);
    RECOMMEND_DEAL = new ViewItemType("RECOMMEND_DEAL", 8, 8);
    WARNING_TITLE_TIP = new ViewItemType("WARNING_TITLE_TIP", 9, 9);
    MARKET_AGG = new ViewItemType("MARKET_AGG", 10, 10);
    HOTEL_ZEUS = new ViewItemType("HOTEL_ZEUS", 11, 11);
    $VALUES = new ViewItemType[] { UNKNOWN, TUAN_DEAL, HUI, AGGRATION_ITEMS, SHOP, RIGHT_TAG, TITLE_TIP, BANNER, RECOMMEND_DEAL, WARNING_TITLE_TIP, MARKET_AGG, HOTEL_ZEUS };
    _map = new HashMap();
    ViewItemType[] arrayOfViewItemType = values();
    int j = arrayOfViewItemType.length;
    int i = 0;
    while (i < j)
    {
      ViewItemType localViewItemType = arrayOfViewItemType[i];
      _map.put(Integer.valueOf(localViewItemType.value), localViewItemType);
      i += 1;
    }
  }

  private ViewItemType(int paramInt)
  {
    this.value = paramInt;
  }

  public static ViewItemType parseFromValue(int paramInt)
  {
    return (ViewItemType)_map.get(Integer.valueOf(paramInt));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ViewItemType
 * JD-Core Version:    0.6.0
 */