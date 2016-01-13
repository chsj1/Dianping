package com.dianping.search.shoplist.fragment.agentconfig;

import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.shoplist.agent.ShopListBannerAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent;
import com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent;
import com.dianping.search.shoplist.agent.ShopListRedirectBarAgent;
import com.dianping.search.shoplist.agent.ShopListSearchAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.GetAroundSuggestFragment;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetAroundShopListAgentConfig extends NShopListAgentConfig
{
  protected static final String GETTINGAROUND_SHOP_REQUEST_URI = "http://m.api.dianping.com/gettingaround.bin";
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();

  static
  {
    map.put("shoplist/searchtitle", ShopListSearchAgent.class);
    map.put("shoplist/navifilter", ShopListNaviAdvanceFilterAgent.class);
    map.put("shoplist/banner", ShopListBannerAgent.class);
    map.put("shoplist/redirectbar", ShopListRedirectBarAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
    map.put("shoplist/emptyresultheader", ShopListEmptyResultHeaderAgent.class);
  }

  public GetAroundShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/gettingaround.bin").buildUpon();
    buildRequestParameter(paramInt, localBuilder);
    buildExtraRequestParameter(localBuilder);
    this.shopListRequest = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
    return this.shopListRequest;
  }

  public AbstractSearchFragment createSuggestFragment(FragmentActivity paramFragmentActivity, BaseShopListDataSource paramBaseShopListDataSource)
  {
    return GetAroundSuggestFragment.newInstance(paramFragmentActivity, ShopListUtils.getCategoryIdFromDataSource(paramBaseShopListDataSource));
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
    if (((this.shopListAgentFragment.getDataSource() instanceof NewShopListDataSource)) && ("gettingaround".equals(this.shopListAgentFragment.getDataSource().targetPage)))
    {
      ((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).needAdvFilter = true;
      return true;
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.GetAroundShopListAgentConfig
 * JD-Core Version:    0.6.0
 */