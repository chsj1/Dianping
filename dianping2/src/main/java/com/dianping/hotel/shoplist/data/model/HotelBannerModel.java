package com.dianping.hotel.shoplist.data.model;

public class HotelBannerModel
{
  private String name;
  private String picUrl;
  private String smallPicUrl;
  private String url;

  public String getName()
  {
    return this.name;
  }

  public String getPicUrl(int paramInt)
  {
    if (paramInt == 2)
      return this.smallPicUrl;
    return this.picUrl;
  }

  public String getUrl()
  {
    return this.url;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setPicUrl(String paramString)
  {
    this.picUrl = paramString;
  }

  public void setSmallPicUrl(String paramString)
  {
    this.smallPicUrl = paramString;
  }

  public void setUrl(String paramString)
  {
    this.url = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.data.model.HotelBannerModel
 * JD-Core Version:    0.6.0
 */