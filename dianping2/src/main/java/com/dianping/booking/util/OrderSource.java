package com.dianping.booking.util;

public enum OrderSource
{
  public final String eventLabel;
  public final int fromType;

  static
  {
    SceneBooking = new OrderSource("SceneBooking", 3, 3, "channel_scene");
    RecommendedShops = new OrderSource("RecommendedShops", 4, 4, "channel_shoplist");
    Others = new OrderSource("Others", 5, -1, "others");
    $VALUES = new OrderSource[] { KeyWordSearch, SearchBtn, WholeShopsSearch, SceneBooking, RecommendedShops, Others };
  }

  private OrderSource(int paramInt, String paramString)
  {
    this.fromType = paramInt;
    this.eventLabel = paramString;
  }

  public static String getEventLabelByFromeType(int paramInt)
  {
    OrderSource[] arrayOfOrderSource = values();
    int j = arrayOfOrderSource.length;
    int i = 0;
    while (i < j)
    {
      OrderSource localOrderSource = arrayOfOrderSource[i];
      if (localOrderSource.fromType == paramInt)
        return localOrderSource.eventLabel;
      i += 1;
    }
    return "";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.OrderSource
 * JD-Core Version:    0.6.0
 */