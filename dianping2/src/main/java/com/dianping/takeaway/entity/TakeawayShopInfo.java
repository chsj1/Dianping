package com.dianping.takeaway.entity;

import com.dianping.archive.DPObject;

public class TakeawayShopInfo
{
  public DPObject[] activity;
  public String announceText;
  public String carrier;
  public String deliveryFee;
  public String errorMessage;
  public boolean isAvailable;
  public boolean isShowReviewed;
  public double minAmount;
  public String minFeeText;
  public DPObject notice;
  public String openingTimeTip;
  public String reviewCount;
  public String reviewStr;
  public int shopPower;

  public TakeawayShopInfo(DPObject paramDPObject)
  {
    this.minAmount = paramDPObject.getDouble("MinAmount");
    if (paramDPObject.getInt("Status") == 0);
    for (boolean bool = true; ; bool = false)
    {
      this.isAvailable = bool;
      this.announceText = paramDPObject.getString("AnnounceText");
      this.errorMessage = paramDPObject.getString("ErrorMsg");
      this.carrier = paramDPObject.getString("CarrierName");
      this.minFeeText = paramDPObject.getString("MinFeeText");
      this.deliveryFee = paramDPObject.getString("DeliveryFee");
      this.activity = paramDPObject.getArray("Activity");
      this.shopPower = paramDPObject.getInt("ShopPower");
      this.reviewStr = paramDPObject.getString("ReviewStr");
      this.reviewCount = paramDPObject.getString("ReviewCount");
      this.isShowReviewed = paramDPObject.getBoolean("IsShowReviewed");
      this.openingTimeTip = paramDPObject.getString("OpeningTimeTip");
      this.notice = paramDPObject.getObject("Notice");
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayShopInfo
 * JD-Core Version:    0.6.0
 */