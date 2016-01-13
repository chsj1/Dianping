package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;

public class DealModel
{
  public DPObject dealObj;
  public String dealTitle;
  public double originalPrice;
  public double price;
  public DPObject shopObj;
  public int soldCount;

  public DealModel(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    this.shopObj = paramDPObject2;
    this.dealObj = paramDPObject1;
    this.originalPrice = paramDPObject1.getDouble("OriginalPrice");
    this.price = paramDPObject1.getDouble("Price");
    this.dealTitle = paramDPObject1.getString("DealTitle");
    this.soldCount = paramDPObject1.getInt("Count");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.DealModel
 * JD-Core Version:    0.6.0
 */