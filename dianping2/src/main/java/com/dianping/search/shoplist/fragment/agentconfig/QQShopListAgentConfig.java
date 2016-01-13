package com.dianping.search.shoplist.fragment.agentconfig;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.TuanShopAggregationListAdapter;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.search.shoplist.agent.NShopListContentAgent;
import com.dianping.search.shoplist.agent.ShopListKeywordSuggestAgent;
import com.dianping.search.shoplist.agent.ShopListQQTitleAgent;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.ShopListDataModelAdapter;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class QQShopListAgentConfig extends NShopListAgentConfig
{
  private static final String SHOP_QQ_REQUEST_URI = "http://m.api.dianping.com/searchshop.api";
  private static final String TUAN_QQ_REQUEST_URI = "http://app.t.dianping.com/searchshopdealgn.bin";
  private String mGATag = "qqaio6";

  public QQShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
    if (paramShopListAgentFragment.getActivityHost() != null)
    {
      if (!paramShopListAgentFragment.getActivityHost().equals("qqshoplist"))
        break label37;
      this.mGATag = "qqaio6";
    }
    label37: 
    do
      return;
    while (!paramShopListAgentFragment.getActivityHost().equals("wxshoplist"));
    this.mGATag = "weixinplus6";
  }

  public MApiRequest createListRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/searchshop.api").buildUpon();
    int i;
    if (cityId() <= 0)
      i = 1;
    while (true)
    {
      localBuilder.appendQueryParameter("cityid", String.valueOf(i));
      Object localObject = location();
      if (localObject == null)
      {
        localBuilder.appendQueryParameter("mylat", "0");
        localBuilder.appendQueryParameter("mylng", "0");
        label58: if (this.shopListAgentFragment.getDataSource().curCategory() != null)
          break label398;
        i = 0;
        label74: if (i > 0)
          localBuilder.appendQueryParameter("categoryid", String.valueOf(i));
        if (this.shopListAgentFragment.getDataSource().curRegion() != null)
          break label418;
        i = 0;
        label107: if (i > 0)
          localBuilder.appendQueryParameter("categoryid", String.valueOf(i));
        if (this.shopListAgentFragment.getDataSource().curSort() != null)
          break label438;
        localObject = null;
        label139: if (localObject != null)
          localBuilder.appendQueryParameter("sortid", String.valueOf(localObject));
        if (TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().suggestKeyword()));
      }
      try
      {
        localBuilder.appendQueryParameter("keyword", URLEncoder.encode(this.shopListAgentFragment.getDataSource().suggestKeyword(), "UTF-8"));
        label192: if (!TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().suggestValue()));
        try
        {
          localBuilder.appendQueryParameter("value", URLEncoder.encode(this.shopListAgentFragment.getDataSource().suggestValue(), "UTF-8"));
          label230: localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
          if (this.shopListAgentFragment.getDataSource().currentTabIndex() == 0)
            if (this.shopListAgentFragment.getDataSource().curSelectNav() == null)
            {
              paramInt = 0;
              label269: if (paramInt > 0)
                localBuilder.appendQueryParameter("filterid", String.valueOf(paramInt));
            }
          while (true)
          {
            localBuilder.appendQueryParameter("pagemodule", "qqshoplist");
            this.shopListRequest = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
            return this.shopListRequest;
            i = cityId();
            break;
            localBuilder.appendQueryParameter("mylat", Location.FMT.format(((Location)localObject).latitude()));
            localBuilder.appendQueryParameter("mylng", Location.FMT.format(((Location)localObject).longitude()));
            localBuilder.appendQueryParameter("myacc", String.valueOf(((Location)localObject).accuracy()));
            localBuilder.appendQueryParameter("locatecityid", String.valueOf(((Location)localObject).city().id()));
            break label58;
            label398: i = this.shopListAgentFragment.getDataSource().curCategory().getInt("ID");
            break label74;
            label418: i = this.shopListAgentFragment.getDataSource().curRegion().getInt("ID");
            break label107;
            label438: localObject = this.shopListAgentFragment.getDataSource().curSort().getString("ID");
            break label139;
            paramInt = this.shopListAgentFragment.getDataSource().curSelectNav().getInt("FilterId");
            break label269;
            if (this.shopListAgentFragment.getDataSource().currentTabIndex() != 1)
              continue;
            localBuilder.appendQueryParameter("filterid", "3");
          }
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException1)
        {
          break label230;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException2)
      {
        break label192;
      }
    }
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("shoplist/searchtabtitle", ShopListQQTitleAgent.class);
    localLinkedHashMap.put("shoplist/keyword", ShopListKeywordSuggestAgent.class);
    localLinkedHashMap.put("shoplist/contentlist", NShopListContentAgent.class);
    return localLinkedHashMap;
  }

  public ShopListAdapter getListAdapter(Context paramContext, ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    if (this.lastAdapter == null)
      if (this.shopListAgentFragment.getDataSource().currentTabIndex() == 0)
        this.lastAdapter = new ShopListDataModelAdapter(paramShopListReloadHandler);
    while (true)
    {
      return this.lastAdapter;
      this.lastAdapter = new TuanShopAggregationListAdapter((DPActivity)paramContext, paramShopListReloadHandler);
      continue;
      if ((this.shopListAgentFragment.getDataSource().currentTabIndex() == 0) && ((this.lastAdapter instanceof TuanShopAggregationListAdapter)))
      {
        this.lastAdapter = new ShopListDataModelAdapter(paramShopListReloadHandler);
        continue;
      }
      if ((this.shopListAgentFragment.getDataSource().currentTabIndex() != 1) || ((this.lastAdapter instanceof TuanShopAggregationListAdapter)))
        continue;
      this.lastAdapter = new TuanShopAggregationListAdapter((DPActivity)paramContext, paramShopListReloadHandler);
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
        String str = this.shopListAgentFragment.getActivity().getIntent().getData().getHost();
        if (!str.equals("qqshoplist"))
        {
          boolean bool = str.equals("wxshoplist");
          i = j;
          if (!bool);
        }
        else
        {
          i = 1;
        }
      }
      return i;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  protected void startShopInfoActivity(DPObject paramDPObject)
  {
    if (TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().suggestKeyword()));
    for (Object localObject = "list"; ; localObject = "search")
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID") + "&type=" + (String)localObject));
      ((Intent)localObject).putExtra("shopId", paramDPObject.getInt("ID"));
      ((Intent)localObject).putExtra("shop", paramDPObject);
      if (this.shopListAgentFragment.getDataSource().hasSearchDate())
      {
        ((Intent)localObject).putExtra("checkinTime", this.shopListAgentFragment.getDataSource().hotelCheckinTime());
        ((Intent)localObject).putExtra("checkoutTime", this.shopListAgentFragment.getDataSource().hotelCheckoutTime());
      }
      this.shopListAgentFragment.startActivity((Intent)localObject);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.QQShopListAgentConfig
 * JD-Core Version:    0.6.0
 */