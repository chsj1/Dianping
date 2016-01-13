package com.dianping.search.deallist.config;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.search.deallist.agent.DealListTabAgent;
import com.dianping.search.deallist.agent.NaviFilterBarTabAgent;
import com.dianping.search.deallist.agent.SearchDealTabAgent;
import java.util.HashMap;
import java.util.Map;

public class DealListTabConfig
  implements AgentListConfig
{
  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("deallist/search", SearchDealTabAgent.class);
    localHashMap.put("deallist/filter", NaviFilterBarTabAgent.class);
    localHashMap.put("deallist/deallist", DealListTabAgent.class);
    return localHashMap;
  }

  public boolean shouldShow()
  {
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.config.DealListTabConfig
 * JD-Core Version:    0.6.0
 */