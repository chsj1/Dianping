package com.dianping.selectdish.model;

import java.io.Serializable;

public class CartFreeItem
  implements Serializable
{
  public boolean expired = false;
  public final GiftInfo giftInfo;
  public boolean soldout = false;
  public boolean use = false;

  public CartFreeItem(GiftInfo paramGiftInfo)
  {
    this.giftInfo = paramGiftInfo;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.CartFreeItem
 * JD-Core Version:    0.6.0
 */