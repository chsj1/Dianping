package com.dianping.hotel.deal.agent;

import com.dianping.hotel.deal.agent.common.BaseHotelPariAgent;

public class HotelProdPurchasingNoticeAgent extends BaseHotelPariAgent
{
  public HotelProdPurchasingNoticeAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected int getPariType()
  {
    return 16;
  }

  public void setHeaderText(String paramString)
  {
    setHeaderTextWithAccordions(paramString, Boolean.valueOf(true));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdPurchasingNoticeAgent
 * JD-Core Version:    0.6.0
 */