package com.dianping.search.deallist.config;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.search.deallist.agent.DealListAgent;
import com.dianping.search.deallist.agent.NaviFilterBarAgent;
import com.dianping.search.deallist.agent.SearchDealAgent;
import java.util.HashMap;
import java.util.Map;

public class DefaultDealListConfig
  implements AgentListConfig
{
  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("deallist/search", SearchDealAgent.class);
    localHashMap.put("deallist/filter", NaviFilterBarAgent.class);
    localHashMap.put("deallist/deallist", DealListAgent.class);
    return localHashMap;
  }

  public boolean shouldShow()
  {
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.config.DefaultDealListConfig
 * JD-Core Version:    0.6.0
 */