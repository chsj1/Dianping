package com.dianping.search.shoplist.fragment.agentconfig.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.search.shoplist.data.model.DealModel;
import com.dianping.search.shoplist.data.model.ExtSearchModel;
import com.dianping.search.shoplist.fragment.ShopListDataModelAdapter;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;

public abstract class NShopListAgentConfig extends ShopListAgentConfig
{
  public NShopListAgentConfig(AbstractShopListAgentFragment paramAbstractShopListAgentFragment)
  {
    super(paramAbstractShopListAgentFragment);
  }

  protected void buildRequestParameter(int paramInt, Uri.Builder paramBuilder)
  {
    super.buildRequestParameter(paramInt, paramBuilder);
    BaseShopListDataSource localBaseShopListDataSource = this.shopListAgentFragment.getDataSource();
    DPObject localDPObject = localBaseShopListDataSource.curRegion();
    if ((!localBaseShopListDataSource.isMetro()) && (localDPObject != null) && (localDPObject.getInt("ParentID") != -1))
      paramBuilder.appendQueryParameter("regiontype", String.valueOf(localDPObject.getInt("RegionType")));
    if (localBaseShopListDataSource.attributes != null)
      paramBuilder.appendQueryParameter("attributes", localBaseShopListDataSource.attributes);
  }

  public String getContentAgentKey()
  {
    return "shoplist/contentlist";
  }

  public ShopListAdapter getListAdapter(Context paramContext, ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    if (!(this.lastAdapter instanceof ShopListDataModelAdapter))
      this.lastAdapter = new ShopListDataModelAdapter(paramShopListReloadHandler);
    return this.lastAdapter;
  }

  protected void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Object localObject = paramAdapterView.getItemAtPosition(paramInt);
    if ((localObject instanceof ExtSearchModel))
    {
      paramAdapterView = ((ExtSearchModel)localObject).titleUrl;
      if (!TextUtils.isEmpty(paramAdapterView))
        this.shopListAgentFragment.startActivity(paramAdapterView);
      return;
    }
    if ((localObject instanceof DealModel))
    {
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
      paramAdapterView.putExtra("deal", ((DealModel)localObject).dealObj);
      paramView = ((DealModel)localObject).shopObj;
      if (paramView != null)
      {
        paramAdapterView.putExtra("buyLink", paramView.getObject("Deals").getString("BuyLink"));
        paramAdapterView.putExtra("selectLink", paramView.getObject("Deals").getString("SelectLink"));
        paramAdapterView.putExtra("detailLink", paramView.getObject("Deals").getString("DetailLink"));
      }
      this.shopListAgentFragment.startActivity(paramAdapterView);
      return;
    }
    super.onItemClick(paramAdapterView, paramView, paramInt, paramLong);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((getListView() != null) && (getListView().isPullToRefreshing()))
    {
      if (paramMApiRequest == this.shopListRequest)
        this.shopListRequest = null;
      while (true)
      {
        getListView().onRefreshComplete();
        if ((this.shopListAgentFragment.getContext() != null) && (this.shopListAgentFragment.getDataSource().shops() != null) && (!this.shopListAgentFragment.getDataSource().shops().isEmpty()))
          Toast.makeText(this.shopListAgentFragment.getContext(), "网络不可用，请检查你的网络设置", 0).show();
        return;
        if (paramMApiRequest != this.shopEventRequest)
          continue;
        this.shopEventRequest = null;
      }
    }
    super.onRequestFailed(paramMApiRequest, paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    if (getListView() != null)
      getListView().onRefreshComplete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig
 * JD-Core Version:    0.6.0
 */