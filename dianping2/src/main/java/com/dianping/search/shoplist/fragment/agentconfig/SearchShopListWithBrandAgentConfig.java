package com.dianping.search.shoplist.fragment.agentconfig;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.search.shoplist.agent.ShopListContentWithBrandAgent;
import com.dianping.search.shoplist.agent.ShopListSearchAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchShopListWithBrandAgentConfig extends NShopListAgentConfig
{
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();
  private NewShopListDataSource dataSource;

  static
  {
    map.put("shoplist/contentlistbrand", ShopListContentWithBrandAgent.class);
    map.put("shoplist/searchtitle", ShopListSearchAgent.class);
  }

  public SearchShopListWithBrandAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return map;
  }

  public boolean shouldShow()
  {
    if ((this.shopListAgentFragment.getDataSource() instanceof NewShopListDataSource))
    {
      this.dataSource = ((NewShopListDataSource)this.shopListAgentFragment.getDataSource());
      if (this.dataSource.targetType == 4)
      {
        this.dataSource.needAdvFilter = true;
        return true;
      }
    }
    else
    {
      return false;
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.SearchShopListWithBrandAgentConfig
 * JD-Core Version:    0.6.0
 */