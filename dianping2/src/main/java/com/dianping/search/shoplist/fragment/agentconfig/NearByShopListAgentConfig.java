package com.dianping.search.shoplist.fragment.agentconfig;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.model.Location;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent;
import com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent;
import com.dianping.search.shoplist.agent.ShopListNoKeywordSearchAgent;
import com.dianping.search.shoplist.agent.ShopListTextTitleAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.util.LinkedHashMap;
import java.util.Map;

public class NearByShopListAgentConfig extends NShopListAgentConfig
{
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();
  ShopListAgentFragment shopListAgentFragment;

  static
  {
    map.put("shoplist/texttitle", ShopListTextTitleAgent.class);
    map.put("shoplist/searchtitle", ShopListNoKeywordSearchAgent.class);
    map.put("shoplist/navifilter", ShopListNaviAdvanceFilterAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
    map.put("shoplist/emptyresultheader", ShopListEmptyResultHeaderAgent.class);
  }

  public NearByShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
    this.shopListAgentFragment = paramShopListAgentFragment;
  }

  protected void buildExtraRequestParameter(Uri.Builder paramBuilder)
  {
    int j = 0;
    DPObject localDPObject;
    label60: int i;
    if (this.shopListAgentFragment.getDataSource().nearbyShopId > 0)
    {
      paramBuilder.appendQueryParameter("shopid", String.valueOf(this.shopListAgentFragment.getDataSource().nearbyShopId));
      if (!this.shopListAgentFragment.getDataSource().isMetro())
        break label146;
      localDPObject = this.shopListAgentFragment.getDataSource().curMetro();
      if (localDPObject != null)
        break label160;
      i = 0;
      label66: if (localDPObject != null)
        break label170;
    }
    while (true)
    {
      if ((localDPObject != null) && (j == -1) && (i != -1))
        paramBuilder.appendQueryParameter("range", String.valueOf(i));
      return;
      paramBuilder.appendQueryParameter("lat", String.valueOf(((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).shopLat));
      paramBuilder.appendQueryParameter("lng", String.valueOf(((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).shopLng));
      break;
      label146: localDPObject = this.shopListAgentFragment.getDataSource().curRegion();
      break label60;
      label160: i = localDPObject.getInt("ID");
      break label66;
      label170: j = localDPObject.getInt("ParentID");
    }
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return map;
  }

  public void parseExtraUrl(Activity paramActivity, BaseShopListDataSource paramBaseShopListDataSource)
  {
    if ((paramActivity == null) || (paramBaseShopListDataSource == null));
    do
    {
      return;
      paramActivity = paramActivity.getIntent().getData();
    }
    while (paramActivity == null);
    if (this.shopListAgentFragment.isCurrentCity())
    {
      paramBaseShopListDataSource.setShowDistance(true);
      paramBaseShopListDataSource.setNeedLocalRegion(true);
    }
    while (true)
    {
      this.shopListAgentFragment.GATag = "nearbyshop";
      String str1 = paramActivity.getQueryParameter("shopid");
      if ((!TextUtils.isEmpty(str1)) && (TextUtils.isDigitsOnly(str1)))
        paramBaseShopListDataSource.nearbyShopId = Integer.parseInt(str1);
      str1 = paramActivity.getQueryParameter("cityid");
      if ((!TextUtils.isEmpty(str1)) && (TextUtils.isDigitsOnly(str1)))
        paramBaseShopListDataSource.setCityId(Integer.parseInt(str1));
      if (location() != null)
        paramBaseShopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
      str1 = paramActivity.getQueryParameter("lat");
      String str2 = paramActivity.getQueryParameter("lng");
      paramBaseShopListDataSource = (NewShopListDataSource)paramBaseShopListDataSource;
      if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)))
      {
        paramBaseShopListDataSource.shopLat = Double.valueOf(str1).doubleValue();
        paramBaseShopListDataSource.shopLng = Double.valueOf(str2).doubleValue();
      }
      paramBaseShopListDataSource.title = paramActivity.getQueryParameter("title");
      paramBaseShopListDataSource.needAdvFilter = true;
      return;
      paramBaseShopListDataSource.setShowDistance(false);
      paramBaseShopListDataSource.setNeedLocalRegion(false);
    }
  }

  public boolean shouldShow()
  {
    int j = 0;
    int i = j;
    try
    {
      if (this.shopListAgentFragment.getActivity() != null)
      {
        boolean bool = this.shopListAgentFragment.getActivity().getIntent().getData().getHost().equals("nearbyshoplist");
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
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.NearByShopListAgentConfig
 * JD-Core Version:    0.6.0
 */