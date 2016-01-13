package com.dianping.hotel.deal.config;

import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;

public abstract class BaseHotelProdInfoConfig
  implements AgentListConfig
{
  protected HotelProdInfoAgentFragment mFragment;

  public BaseHotelProdInfoConfig(HotelProdInfoAgentFragment paramHotelProdInfoAgentFragment)
  {
    this.mFragment = paramHotelProdInfoAgentFragment;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.config.BaseHotelProdInfoConfig
 * JD-Core Version:    0.6.0
 */