package com.dianping.takeaway.entity;

public class TakeawayAddress
{
  private static final String ADDRESS_DIVIDER = "#-#";
  public String address;
  public String addressDetail;
  public double lat;
  public double lng;

  public TakeawayAddress(String paramString, double paramDouble1, double paramDouble2)
  {
    this.address = paramString;
    this.lat = paramDouble1;
    this.lng = paramDouble2;
  }

  public TakeawayAddress(String paramString1, String paramString2, double paramDouble1, double paramDouble2)
  {
    this.address = paramString1;
    this.addressDetail = paramString2;
    this.lat = paramDouble1;
    this.lng = paramDouble2;
  }

  public String toString()
  {
    return this.address + "#-#" + this.lat + "#-#" + this.lng;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayAddress
 * JD-Core Version:    0.6.0
 */