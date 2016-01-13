package com.dianping.search.shoplist.fragment.agentconfig;

import android.net.Uri.Builder;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.agent.ShopListBannerAgent;
import com.dianping.base.shoplist.agent.ShopListLocalBarAgent;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent;
import com.dianping.search.shoplist.agent.ShopListGuideKeywordBarAgent;
import com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent;
import com.dianping.search.shoplist.agent.ShopListRedirectBarAgent;
import com.dianping.search.shoplist.agent.ShopListRewriteWordAgent;
import com.dianping.search.shoplist.agent.ShopListSearchAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultShopListAgentConfig extends NShopListAgentConfig
{
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();

  static
  {
    map.put("shoplist/searchtitle", ShopListSearchAgent.class);
    map.put("shoplist/navifilter", ShopListNaviAdvanceFilterAgent.class);
    map.put("shoplist/rewritewordbar", ShopListRewriteWordAgent.class);
    map.put("shoplist/banner", ShopListBannerAgent.class);
    map.put("shoplist/redirectbar", ShopListRedirectBarAgent.class);
    map.put("shoplist/guidewordbar", ShopListGuideKeywordBarAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
    map.put("shoplist/localbar", ShopListLocalBarAgent.class);
    map.put("shoplist/emptyresultheader", ShopListEmptyResultHeaderAgent.class);
  }

  public DefaultShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
  }

  protected void buildExtraRequestParameter(Uri.Builder paramBuilder)
  {
    if ((this.shopListAgentFragment.getDataSource() instanceof NewShopListDataSource))
      paramBuilder.appendQueryParameter("disablerewrite", String.valueOf(((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).disableRewrite));
    super.buildExtraRequestParameter(paramBuilder);
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
      ((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).needAdvFilter = true;
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.DefaultShopListAgentConfig
 * JD-Core Version:    0.6.0
 */