package com.dianping.shopinfo.district.config;

import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.common.AddressAgent;
import com.dianping.shopinfo.dish.DishRankAgent;
import com.dianping.shopinfo.district.DistrictEnjoyAgent;
import com.dianping.shopinfo.district.DistrictRankAgent;
import com.dianping.shopinfo.district.DistrictTopAgent;
import com.dianping.shopinfo.district.HotMarketAgent;
import java.util.HashMap;
import java.util.Map;

public class BusinessDistrictAgentListConfig
  implements AgentListConfig
{
  private DPObject shop;

  public BusinessDistrictAgentListConfig(DPObject paramDPObject)
  {
    this.shop = paramDPObject;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopinfo/districttop", new AgentInfo(DistrictTopAgent.class, "0200Basic.01Top"));
    localHashMap.put("shopinfo/rank", new AgentInfo(DistrictRankAgent.class, "0200Basic.02Rank"));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "0200Basic.10Address"));
    localHashMap.put("shopinfo/hotmarket", new AgentInfo(HotMarketAgent.class, "0300District.01HotMarket"));
    localHashMap.put("shopinfo/dishrank", new AgentInfo(DishRankAgent.class, "0400District.02DishRank"));
    localHashMap.put("shopinfo/districtshop", new AgentInfo(DistrictEnjoyAgent.class, "0500District.01shop"));
    return localHashMap;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public boolean shouldShow()
  {
    return (this.shop != null) && (this.shop.getObject("ClientShopStyle") != null) && ("bd_shop".equalsIgnoreCase(this.shop.getObject("ClientShopStyle").getString("ShopView")));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.district.config.BusinessDistrictAgentListConfig
 * JD-Core Version:    0.6.0
 */