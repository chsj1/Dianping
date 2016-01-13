package com.dianping.membercard.utils;

import com.dianping.archive.DPObject;

public enum ProductType
{
  private int value;

  static
  {
    DISCOUNT = new ProductType("DISCOUNT", 1, 1);
    FREE_SEND = new ProductType("FREE_SEND", 2, 2);
    CARD_SUSPEND = new ProductType("CARD_SUSPEND", 3, 5);
    BADGE = new ProductType("BADGE", 4, 6);
    POINT = new ProductType("POINT", 5, 7);
    SAVING = new ProductType("SAVING", 6, 8);
    TIMES = new ProductType("TIMES", 7, 9);
    SCORE = new ProductType("SCORE", 8, 10);
    WELIFE_PRIVILEGE = new ProductType("WELIFE_PRIVILEGE", 9, 11);
    WELIFE_OPEN_CARD = new ProductType("WELIFE_OPEN_CARD", 10, 12);
    FEED = new ProductType("FEED", 11, MCUtils.FEED_PRODUCT_TYPE);
    $VALUES = new ProductType[] { NULL, DISCOUNT, FREE_SEND, CARD_SUSPEND, BADGE, POINT, SAVING, TIMES, SCORE, WELIFE_PRIVILEGE, WELIFE_OPEN_CARD, FEED };
  }

  private ProductType(int paramInt)
  {
    this.value = paramInt;
  }

  public static ProductType valueOf(int paramInt)
  {
    if (DISCOUNT.equals(paramInt))
      return DISCOUNT;
    if (FREE_SEND.equals(paramInt))
      return FREE_SEND;
    if (CARD_SUSPEND.equals(paramInt))
      return CARD_SUSPEND;
    if (SAVING.equals(paramInt))
      return SAVING;
    if (TIMES.equals(paramInt))
      return TIMES;
    if (BADGE.equals(paramInt))
      return BADGE;
    if (POINT.equals(paramInt))
      return POINT;
    if (FEED.equals(paramInt))
      return FEED;
    if (SCORE.equals(paramInt))
      return SCORE;
    if (WELIFE_PRIVILEGE.equals(paramInt))
      return WELIFE_PRIVILEGE;
    if (WELIFE_OPEN_CARD.equals(paramInt))
      return WELIFE_OPEN_CARD;
    return NULL;
  }

  public static ProductType valueOf(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return NULL;
    if (BADGE.equals(paramDPObject))
      return BADGE;
    if (POINT.equals(paramDPObject))
      return POINT;
    if (FEED.equals(paramDPObject))
      return FEED;
    return valueOf(paramDPObject.getInt("ProductType"));
  }

  public boolean equals(int paramInt)
  {
    return this.value == paramInt;
  }

  public boolean equals(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      while (true)
      {
        return false;
        if (!paramDPObject.isClass("Product"))
          break;
        if (this.value == paramDPObject.getInt("ProductType"))
          return true;
      }
      if (paramDPObject.isClass("CardPoint"))
        return POINT.equals(this);
    }
    while (!paramDPObject.isClass("Feed"));
    return FEED.equals(this);
  }

  public boolean equals(ProductType paramProductType)
  {
    return this.value == paramProductType.value;
  }

  public int value()
  {
    return this.value;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ProductType
 * JD-Core Version:    0.6.0
 */