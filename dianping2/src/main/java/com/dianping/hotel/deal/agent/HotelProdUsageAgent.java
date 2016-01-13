package com.dianping.hotel.deal.agent;

import com.dianping.hotel.deal.agent.common.BaseHotelPariAgent;

public class HotelProdUsageAgent extends BaseHotelPariAgent
{
  public HotelProdUsageAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected int getPariType()
  {
    return 15;
  }

  public void setHeaderText(String paramString)
  {
    setHeaderTextWithAccordions(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdUsageAgent
 * JD-Core Version:    0.6.0
 */