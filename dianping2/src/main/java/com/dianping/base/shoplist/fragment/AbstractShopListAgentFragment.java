package com.dianping.base.shoplist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.dianping.app.CityConfig;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.base.shoplist.data.DataSource.OnDataChangeListener;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.util.Log;
import com.dianping.widget.view.GAHelper;

public abstract class AbstractShopListAgentFragment extends AgentFragment
  implements DataSource.DataLoader, FullRequestHandle<MApiRequest, MApiResponse>, DataSource.OnDataChangeListener
{
  protected static final String TAG = AbstractShopListAgentFragment.class.getSimpleName();
  public String GATag;
  protected String host;
  protected String requestId = null;
  protected BaseShopListDataSource shopListDataSource;
  protected MApiRequest shopListRequest;
  protected String targetPage;

  protected abstract BaseShopListDataSource createDataSource();

  protected abstract MApiRequest createRequest(int paramInt);

  public String getActivityHost()
  {
    return this.host;
  }

  public abstract ShopListAgentConfig getCurrentAgentListConfig();

  public BaseShopListDataSource getDataSource()
  {
    return this.shopListDataSource;
  }

  public String getGATag()
  {
    return this.GATag;
  }

  public boolean hasLocation()
  {
    return locationService().city() != null;
  }

  public boolean isCurrentCity()
  {
    return (locationService().city() != null) && (locationService().city().getInt("ID") == cityConfig().currentCity().id());
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.shopListRequest != null)
    {
      mapiService().abort(this.shopListRequest, this, true);
      this.shopListRequest = null;
    }
    this.shopListRequest = createRequest(paramInt);
    Log.d("debug_request", this.shopListRequest.toString());
    if (paramBoolean)
      cacheService().remove(this.shopListRequest);
    mapiService().exec(this.shopListRequest, this);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.shopListDataSource = createDataSource();
    this.shopListDataSource.setDataLoader(this);
    this.shopListDataSource.addDataChangeListener(this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopListRequest)
    {
      this.shopListRequest = null;
      Log.d("debug_resp", paramMApiResponse.statusCode() + " " + paramMApiResponse.message());
      this.shopListDataSource.setError("网络连接失败 点击重新加载");
    }
  }

  public abstract void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse);

  public void shopListSendNewPV()
  {
    NovaActivity localNovaActivity;
    boolean bool;
    if (((getActivity() instanceof AbstractTabListActivity)) && ((((AbstractTabListActivity)getActivity()).getCurrentFragment() instanceof AbstractShopListAgentFragment)))
    {
      localNovaActivity = (NovaActivity)getActivity();
      bool = GAHelper.instance().getUtmAndMarketingSource(localNovaActivity.gaExtra, getActivity().getIntent().getData());
      localNovaActivity.gaExtra.query_id = this.shopListDataSource.queryId();
      localNovaActivity.gaExtra.keyword = this.shopListDataSource.suggestKeyword();
      localNovaActivity.gaExtra.index = Integer.valueOf(this.shopListDataSource.startIndex());
      if (this.shopListDataSource.curCategory() != null)
        localNovaActivity.gaExtra.category_id = Integer.valueOf(this.shopListDataSource.curCategory().getInt("ID"));
      if (this.shopListDataSource.curRegion() == null)
        break label240;
      localNovaActivity.gaExtra.region_id = Integer.valueOf(this.shopListDataSource.curRegion().getInt("ID"));
      if (this.shopListDataSource.curSort() != null)
        localNovaActivity.gaExtra.sort_id = Integer.valueOf(this.shopListDataSource.curSort().getString("ID"));
      localNovaActivity.gaExtra.shop_id = null;
      if (!this.shopListDataSource.isTopShopNearBy)
        break label275;
      GAHelper.instance().setGAPageName("nearby_headlines");
    }
    while (true)
    {
      GAHelper.instance().setRequestId(localNovaActivity, this.requestId, localNovaActivity.gaExtra, bool);
      return;
      label240: if (this.shopListDataSource.curRange() == null)
        break;
      localNovaActivity.gaExtra.region_id = Integer.valueOf(this.shopListDataSource.curRange().getInt("ID"));
      break;
      label275: GAHelper.instance().setGAPageName("shoplist");
    }
  }

  public abstract boolean updateWedTab();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment
 * JD-Core Version:    0.6.0
 */