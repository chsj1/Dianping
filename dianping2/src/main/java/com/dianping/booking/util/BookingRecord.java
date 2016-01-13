package com.dianping.booking.util;

import com.dianping.archive.DPObject;

public class BookingRecord
{
  public DPObject activityInfo;
  public String appealInfo;
  public int bookerGender;
  public String bookerName;
  public String bookerPhone;
  public int bookingId;
  public final DPObject bookingRecord;
  public long bookingTime;
  public String cancelTip;
  public DPObject flowList;
  public DPObject grouponDetail;
  public boolean isActive;
  public boolean isExpand;
  public boolean isInsteadRecord;
  public DPObject lotteryInfo;
  public DPObject onlinePay;
  public int orderStatus;
  public DPObject paidCashInfo;
  public int peopleNum;
  public int positionType;
  public DPObject preDepositeInfo;
  public DPObject recordButtonStatus;
  public String serializedId;
  public String shopAddress;
  public String shopContact;
  public int shopId;
  public String shopName;
  public String shopUrl;

  public BookingRecord(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      throw new NullPointerException();
    this.bookingRecord = paramDPObject;
    setBookingRecord();
  }

  private void setBookingRecord()
  {
    this.bookingId = this.bookingRecord.getInt("ID");
    this.serializedId = this.bookingRecord.getString("SerialNumber");
    this.shopId = this.bookingRecord.getInt("ShopID");
    this.shopName = this.bookingRecord.getString("ShopName");
    this.isInsteadRecord = this.bookingRecord.getBoolean("InsteadRecord");
    this.bookerName = this.bookingRecord.getString("BookerName");
    this.bookerGender = this.bookingRecord.getInt("BookerGender");
    this.bookerPhone = this.bookingRecord.getString("BookerPhone");
    this.peopleNum = this.bookingRecord.getInt("PeopleNumber");
    this.positionType = this.bookingRecord.getInt("PositionType");
    this.bookingTime = this.bookingRecord.getTime("BookingTime");
    this.orderStatus = this.bookingRecord.getInt("Status");
    this.shopAddress = this.bookingRecord.getString("ShopAddress");
    this.shopContact = this.bookingRecord.getString("ShopContact");
    this.shopUrl = this.bookingRecord.getString("ShopUrl");
    this.isActive = this.bookingRecord.getBoolean("IsActive");
    this.lotteryInfo = this.bookingRecord.getObject("LotteryInfo");
    this.grouponDetail = this.bookingRecord.getObject("GrouponDetail");
    this.onlinePay = this.bookingRecord.getObject("OnlinePay");
    this.paidCashInfo = this.bookingRecord.getObject("PaidCashInfo");
    this.recordButtonStatus = this.bookingRecord.getObject("RecordButton");
    this.appealInfo = this.bookingRecord.getString("AppealInfo");
    this.activityInfo = this.bookingRecord.getObject("ActivityInfo");
    this.isExpand = this.bookingRecord.getBoolean("IsExpand");
    this.flowList = this.bookingRecord.getObject("BookingRecordFlowList");
    this.preDepositeInfo = this.bookingRecord.getObject("PrepayInfo");
    this.cancelTip = this.bookingRecord.getString("CancelTip");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.BookingRecord
 * JD-Core Version:    0.6.0
 */