package com.dianping.search.shoplist.fragment.agentconfig;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListEmptyResultHeaderAgent;
import com.dianping.search.shoplist.agent.ShopListNaviAdvanceFilterAgent;
import com.dianping.search.shoplist.agent.ShopListSearchTitleAgent;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import com.dianping.search.util.ShopListUtils;
import java.util.LinkedHashMap;
import java.util.Map;

public class MallShopListAgentConfig extends NShopListAgentConfig
{
  protected static final String MALL_SHOP_REQUEST_URI = "http://m.api.dianping.com/inmallsearch.bin";
  static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();
  String lastKeyword = "";
  int mallId = -1;

  static
  {
    map.put("shoplist/searchtitle", ShopListSearchTitleAgent.class);
    map.put("shoplist/navifilter", ShopListNaviAdvanceFilterAgent.class);
    map.put("shoplist/contentlist", NShopListContentAgent.class);
    map.put("shoplist/emptyresultheader", ShopListEmptyResultHeaderAgent.class);
  }

  public MallShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
  }

  protected void buildExtraRequestParameter(Uri.Builder paramBuilder)
  {
    super.buildExtraRequestParameter(paramBuilder);
    paramBuilder.appendQueryParameter("mallid", String.valueOf(this.mallId));
    Object localObject = this.shopListAgentFragment.getDataSource();
    String str = ((BaseShopListDataSource)localObject).suggestKeyword();
    if ((!TextUtils.isEmpty(str)) && ((this.lastKeyword == null) || (!this.lastKeyword.equals(str))));
    for (boolean bool = true; ; bool = false)
    {
      Log.d("debug_floor", "curkw=" + str + " lastkw=" + this.lastKeyword + "clear=" + bool);
      this.lastKeyword = str;
      if (((localObject instanceof NewShopListDataSource)) && (!bool))
      {
        localObject = ((NewShopListDataSource)localObject).curFloor;
        if (localObject != null)
          paramBuilder.appendQueryParameter("floor", ((DPObject)localObject).getString("ID"));
      }
      return;
    }
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/inmallsearch.bin").buildUpon();
    buildRequestParameter(paramInt, localBuilder);
    buildExtraRequestParameter(localBuilder);
    Log.d("debug_shop_request", localBuilder.toString());
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

  public void parseExtraUrl(Activity paramActivity, BaseShopListDataSource paramBaseShopListDataSource)
  {
    String str = paramActivity.getIntent().getData().getQueryParameter("mallid");
    if ((!TextUtils.isEmpty(str)) && (TextUtils.isDigitsOnly(str)))
      this.mallId = Integer.valueOf(str).intValue();
    paramActivity = paramActivity.getIntent().getData().getQueryParameter("floor");
    if (!TextUtils.isEmpty(paramActivity));
    try
    {
      int i = Integer.parseInt(paramActivity);
      if ((paramBaseShopListDataSource instanceof NewShopListDataSource))
        ((NewShopListDataSource)paramBaseShopListDataSource).curFloor = new DPObject("Pair").edit().putString("ID", paramActivity).putString("Name", ShopListUtils.getFloorStrName(i)).putInt("Type", 4).generate();
      return;
    }
    catch (NumberFormatException paramActivity)
    {
      paramActivity.printStackTrace();
    }
  }

  public boolean shouldShow()
  {
    try
    {
      if ((this.shopListAgentFragment.getDataSource() instanceof NewShopListDataSource))
        ((NewShopListDataSource)this.shopListAgentFragment.getDataSource()).needAdvFilter = true;
      if (this.shopListAgentFragment.getActivity() != null)
      {
        boolean bool = this.shopListAgentFragment.getActivity().getIntent().getData().getHost().equals("inmallshoplist");
        if (bool)
          return true;
      }
      return false;
    }
    catch (Exception localException)
    {
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.MallShopListAgentConfig
 * JD-Core Version:    0.6.0
 */