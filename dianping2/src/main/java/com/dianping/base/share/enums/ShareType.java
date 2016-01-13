package com.dianping.base.share.enums;

public enum ShareType
{
  static
  {
    DEAL = new ShareType("DEAL", 1);
    WEB = new ShareType("WEB", 2);
    APP = new ShareType("APP", 3);
    Pay = new ShareType("Pay", 4);
    LuckyMoney = new ShareType("LuckyMoney", 5);
    HotelProd = new ShareType("HotelProd", 6);
    MultiShare = new ShareType("MultiShare", 7);
    $VALUES = new ShareType[] { SHOP, DEAL, WEB, APP, Pay, LuckyMoney, HotelProd, MultiShare };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.enums.ShareType
 * JD-Core Version:    0.6.0
 */