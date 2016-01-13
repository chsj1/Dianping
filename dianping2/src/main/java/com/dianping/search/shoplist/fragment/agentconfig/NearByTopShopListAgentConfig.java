package com.dianping.search.shoplist.fragment.agentconfig;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListSearchAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class NearByTopShopListAgentConfig extends NShopListAgentConfig
{
  protected static final String GETTINGAROUND_TOPSHOP_REQUEST_URI = "http://m.api.dianping.com/headlineshoplist.bin";
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();

  static
  {
    map.put("shoplist/searchtitle", ShopListSearchAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
  }

  public NearByTopShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
    this.shopListAgentFragment = paramShopListAgentFragment;
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/headlineshoplist.bin").buildUpon();
    buildRequestParameter(paramInt, localBuilder);
    buildExtraRequestParameter(localBuilder);
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

  public void setDataTag()
  {
    Object localObject = this.shopListAgentFragment.getDataSource();
    if ((localObject == null) || (!(localObject instanceof NewShopListDataSource)));
    while (true)
    {
      return;
      ((BaseShopListDataSource)localObject).isTopShopNearBy = true;
      ArrayList localArrayList = ((NewShopListDataSource)localObject).nearbyHeadlineShops;
      if (localArrayList != null)
      {
        i = 0;
        while (i < localArrayList.size())
        {
          ((ShopDataModel)localArrayList.get(i)).tag = ShopListItem.SHOP_TITLE_STYLE;
          i += 1;
        }
      }
      localObject = ((NewShopListDataSource)localObject).shopModels;
      if (localArrayList == null)
        continue;
      int i = 0;
      while (i < ((ArrayList)localObject).size())
      {
        ((ShopDataModel)((ArrayList)localObject).get(i)).tag = ShopListItem.SHOP_SIMPLE_STYLE;
        i += 1;
      }
    }
  }

  public boolean shouldShow()
  {
    int j = 0;
    try
    {
      int i;
      if ((this.shopListAgentFragment.getActivity() == null) || (!this.shopListAgentFragment.getActivity().getIntent().getData().getHost().equals("nearbyheadlineshoplist")))
      {
        i = j;
        if (this.shopListAgentFragment.getDataSource().targetPage != null)
        {
          boolean bool = "nearbyheadlineshoplist".equals(this.shopListAgentFragment.getDataSource().targetPage);
          i = j;
          if (!bool);
        }
      }
      else
      {
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
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.NearByTopShopListAgentConfig
 * JD-Core Version:    0.6.0
 */