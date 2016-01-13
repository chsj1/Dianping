package com.dianping.hotel.mainsearch.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.mainsearch.agent.HotelHomeAgent;
import com.dianping.model.City;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelSearchMainFragment extends AgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public DPObject adsObj;
  private City city;
  private ViewGroup contentView;
  private boolean haveShowNearby = false;
  private MApiRequest indexAdsRequest;
  private FrameLayout mFragmentView;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.RESIDENCE_CHANGE".equals(paramIntent.getAction()))
      {
        int i = paramIntent.getIntExtra("oldCityId", -1);
        int j = paramIntent.getIntExtra("cityId", -1);
        if (i > 0)
          HotelSearchMainFragment.this.cleanHotAdCache(i);
        if (j > 0)
          HotelSearchMainFragment.this.cleanHotAdCache(j);
        HotelSearchMainFragment.this.requestAds();
      }
    }
  };

  private MApiRequest buildHotAdRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/hotel/eventads.hotel").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(paramInt));
    return mapiGet(this, localBuilder.build().toString(), CacheType.DISABLED);
  }

  private void cleanHotAdCache(int paramInt)
  {
    cacheService().remove(buildHotAdRequest(paramInt));
  }

  public void dispatchAdAgentChanged()
  {
    if (this.indexAdsRequest == null)
    {
      dispatchAgentChanged("main/ad", null);
      return;
    }
    this.haveShowNearby = true;
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("main/title", HotelHomeAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public boolean isConnecting()
  {
    Object localObject = (ConnectivityManager)getActivity().getApplicationContext().getSystemService("connectivity");
    if (localObject == null);
    do
    {
      return false;
      localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
    }
    while ((localObject == null) || (!((NetworkInfo)localObject).isAvailable()));
    return true;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.city = city();
    super.onActivityCreated(paramBundle);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.RESIDENCE_CHANGE");
    getActivity().registerReceiver(this.receiver, paramBundle);
    requestAds();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFragmentView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.hotel_search_main, null, false));
    this.contentView = ((ViewGroup)this.mFragmentView.findViewById(16908290));
    setAgentContainerView(this.contentView);
    return this.mFragmentView;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.receiver != null)
      unregisterReceiver(this.receiver);
    if (this.indexAdsRequest != null)
    {
      mapiService().abort(this.indexAdsRequest, this, true);
      this.indexAdsRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.indexAdsRequest)
    {
      this.indexAdsRequest = null;
      this.adsObj = null;
      if (this.haveShowNearby)
        dispatchAgentChanged("main/ad", null);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.indexAdsRequest)
    {
      this.adsObj = ((DPObject)paramMApiResponse.result());
      this.indexAdsRequest = null;
      if (this.haveShowNearby)
        dispatchAgentChanged("main/ad", null);
    }
  }

  public void requestAds()
  {
    if (this.city.id() == 0)
      return;
    if (this.indexAdsRequest != null)
      mapiService().abort(this.indexAdsRequest, null, true);
    this.indexAdsRequest = buildHotAdRequest(cityId());
    mapiService().exec(this.indexAdsRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.mainsearch.fragment.HotelSearchMainFragment
 * JD-Core Version:    0.6.0
 */