package com.dianping.selectdish.model;

import com.dianping.archive.DPObject;

public class UpdatedGiftInfo
{
  public final int giftId;
  public final int id;

  public UpdatedGiftInfo(DPObject paramDPObject)
  {
    this.id = paramDPObject.getInt("DishId");
    this.giftId = paramDPObject.getInt("GiftId");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.UpdatedGiftInfo
 * JD-Core Version:    0.6.0
 */