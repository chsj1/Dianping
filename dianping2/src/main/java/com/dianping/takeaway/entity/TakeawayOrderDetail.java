package com.dianping.takeaway.entity;

import com.dianping.archive.DPObject;

public class TakeawayOrderDetail
{
  public boolean ableToShareHongBao;
  public DPObject[] activityList;
  public String address;
  public String announcement;
  public String comment;
  public String contact;
  public int deliverStatus;
  public String discountAmount;
  public DPObject[] dishList;
  public DPObject[] feeList;
  public String hongBaoPromotion;
  public int interval;
  public String invoice;
  public int leftSeconds;
  public String orderTime;
  public String orderViewId;
  public String originalAmount;
  public String overtimePaymentDesc;
  public String payStatus;
  public DPObject popUpObj;
  public DPObject shareObj;
  public int shopId;
  public String shopName;
  public String[] shopPhone;
  public final DPObject takeawayOrderDetail;
  public String tips;

  public TakeawayOrderDetail(DPObject paramDPObject)
  {
    this.takeawayOrderDetail = paramDPObject;
    setTakeawayOrderDetail();
  }

  private void setTakeawayOrderDetail()
  {
    this.orderViewId = this.takeawayOrderDetail.getString("OrderViewId");
    this.orderTime = this.takeawayOrderDetail.getString("OrderTime");
    this.tips = this.takeawayOrderDetail.getString("Tips");
    this.deliverStatus = this.takeawayOrderDetail.getInt("DeliverStatus");
    this.shopId = this.takeawayOrderDetail.getInt("ShopId");
    this.shopName = this.takeawayOrderDetail.getString("ShopName");
    this.shopPhone = this.takeawayOrderDetail.getStringArray("ShopPhone");
    this.dishList = this.takeawayOrderDetail.getArray("DishList");
    this.feeList = this.takeawayOrderDetail.getArray("FeeList");
    this.activityList = this.takeawayOrderDetail.getArray("Activity");
    this.payStatus = this.takeawayOrderDetail.getString("PayStatus");
    this.originalAmount = this.takeawayOrderDetail.getString("Amount");
    this.discountAmount = this.takeawayOrderDetail.getString("DiscountPrice");
    this.address = this.takeawayOrderDetail.getString("Address");
    this.contact = this.takeawayOrderDetail.getString("Contact");
    this.announcement = this.takeawayOrderDetail.getString("Announcement");
    this.overtimePaymentDesc = this.takeawayOrderDetail.getString("OvertimePaymentDesc");
    this.comment = this.takeawayOrderDetail.getString("Comment");
    this.invoice = this.takeawayOrderDetail.getString("Invoice");
    this.shareObj = this.takeawayOrderDetail.getObject("Share");
    this.popUpObj = this.takeawayOrderDetail.getObject("PopUp");
    this.ableToShareHongBao = this.takeawayOrderDetail.getBoolean("AbleToShareHongBao");
    this.hongBaoPromotion = this.takeawayOrderDetail.getString("HongBaoPromotion");
    this.leftSeconds = this.takeawayOrderDetail.getInt("LeftSeconds");
    this.interval = this.takeawayOrderDetail.getInt("Interval");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayOrderDetail
 * JD-Core Version:    0.6.0
 */