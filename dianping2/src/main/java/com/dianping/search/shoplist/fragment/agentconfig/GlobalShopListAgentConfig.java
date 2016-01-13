package com.dianping.search.shoplist.fragment.agentconfig;

import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent;
import com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent;
import com.dianping.search.shoplist.agent.ShopListTextTitleAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import com.dianping.util.Log;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalShopListAgentConfig extends NShopListAgentConfig
{
  protected static final String GLOBAL_SHOP_REQUEST_URI = "http://m.api.dianping.com/globalshopsearch.bin";
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();
  ShopListAgentFragment shopListAgentFragment;

  static
  {
    map.put("shoplist/texttitle", ShopListTextTitleAgent.class);
    map.put("shoplist/navifilter", ShopListNaviAdvanceFilterAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
    map.put("shoplist/emptyresultheader", ShopListEmptyResultHeaderAgent.class);
  }

  public GlobalShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
    this.shopListAgentFragment = paramShopListAgentFragment;
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/globalshopsearch.bin").buildUpon();
    buildRequestParameter(paramInt, localBuilder);
    buildExtraRequestParameter(localBuilder);
    Log.d("debug_global_shop_request", localBuilder.toString());
    this.shopListRequest = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
    return this.shopListRequest;
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
    int j = 0;
    int i = j;
    try
    {
      if ((this.shopListAgentFragment.getDataSource() instanceof NewShopListDataSource))
      {
        boolean bool = "globalshoplist".equals(this.shopListAgentFragment.getActivityHost());
        i = j;
        if (bool)
          i = 1;
      }
      return i;
    }
    catch (Exception localException)
    {
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.GlobalShopListAgentConfig
 * JD-Core Version:    0.6.0
 */