package com.dianping.main.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.RecyclerAdapterAgentFreagment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.locationservice.LocationService;
import com.dianping.main.guide.MainActivity;
import com.dianping.main.guide.SkinManager;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.DPUrl;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MainHomeFragment extends RecyclerAdapterAgentFreagment
  implements RequestHandler<MApiRequest, MApiResponse>, CityConfig.SwitchListener
{
  public static final String BANNER = "home/HomeBannerSection";
  public static final String CATEGORY = "home/HomeCategorSection";
  public static final String COMPETITIVE = "home/HomeRecommendSection";
  public static final String FAMOUS = "home/HomePromotionBarSection";
  public static final String FEATURE = "home/HomeTravelSection";
  public static final String GUESSLIKE = "home/HomeGuesslikeSection";
  private static final String HOST = "home";
  private static final String HOST_ = "home/";
  public static final int MAX_GUESSLIKE_ITEM = 15;
  public static final String MODESWITCH = "home/HomeModeSwitchSection";
  public static final String NEWCUSTOM = "home/HomeNewCustomSection";
  public static final String PROMOTION = "home/HomePromotionSection";
  public static final int REQUEST_FAIL = 3;
  public static final int REQUEST_SUCCESS = 2;
  public static final String RETRY = "home/HomeRetrySection";
  public static final String RICHBUTTON = "home/HomeRichSection";
  public static final String SCENEMODE = "home/HomeSceneModeAgentSection";
  public static final String SEPARATOR = ";";
  public static final String SPECIALCONTENT = "home/HomeSpecialContentSection";
  public static final String TITLE = "home/HomeTitleSection";
  public static final String TRIP = "home/HomeTimerSection";
  public static final String VERTICAL = "home/HomeVerticalchannelSection";
  static HashMap<String, Integer> agentAdpaterTypeMap;
  static HashMap<String, Class<? extends CellAgent>> agentClassMap = new HashMap();
  boolean dataChanged;
  private DecimalFormat df = new DecimalFormat("000");
  public LinkedHashMap<String, JSONObject> homeData = new LinkedHashMap();
  public LinkedHashMap<String, JSONObject> homeDataClone;
  private boolean isOneVerticalChannel = true;
  private boolean isRefresh;
  private int isRequestStatus = 0;
  private MApiRequest mDeviceInfoReq;
  private MApiRequest opsRequest;
  PullToRefreshRecyclerView pullToRefreshListView;
  final PullToRefreshRecyclerView.OnRefreshListener refreshListener = new PullToRefreshRecyclerView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshRecyclerView paramPullToRefreshRecyclerView)
    {
      DPApplication.instance().tunnelConfigService().refresh();
      MainHomeFragment.this.onRefresh();
    }
  };
  private int requestCount = 0;
  public boolean requestRetrieved;

  static
  {
    agentClassMap.put("home/HomeModeSwitchSection", HomeModeSwitchAgent.class);
    agentClassMap.put("home/HomeBannerSection", HomeBannerAgent.class);
    agentClassMap.put("home/HomeRichSection", HomeHotAdAgent.class);
    agentClassMap.put("home/HomePromotionSection", HomePromotionAdAgent.class);
    agentClassMap.put("home/HomeNewCustomSection", HomeNewUserAdAgent.class);
    agentClassMap.put("home/HomeTravelSection", HomeFeatureAgent.class);
    agentClassMap.put("home/HomePromotionBarSection", HomeFamousShoppingAgent.class);
    agentClassMap.put("home/HomeTimerSection", HomeFeaturedShoppingAgent.class);
    agentClassMap.put("home/HomeRecommendSection", HomeCompetitiveAgent.class);
    agentClassMap.put("home/HomeSpecialContentSection", HomeSpecialContentAgent.class);
    agentClassMap.put("home/HomeTitleSection", HomeTitleBarAgent.class);
    agentClassMap.put("home/HomeCategorSection", HomeCategoryAgent.class);
    agentClassMap.put("home/HomeSceneModeAgentSection", SceneModeAgent.class);
    agentClassMap.put("home/HomeVerticalchannelSection", VerticalChannelAgent.class);
    agentClassMap.put("home/HomeGuesslikeSection", HomeGuessLikeAgent.class);
    agentClassMap.put("home/HomeRetrySection", HomeRefreshAgent.class);
    agentAdpaterTypeMap = new HashMap();
  }

  private void cleanCateCache(int paramInt)
  {
    ((DPActivity)getActivity()).mapiCacheService().remove(getOpsRequest(paramInt));
  }

  private MApiRequest getOpsRequest(int paramInt)
  {
    DPUrl localDPUrl = new DPUrl("http://m.api.dianping.com/operating/indexopsmodules.bin");
    localDPUrl.putParam("cityid", paramInt);
    Location localLocation = location();
    if (localLocation != null)
    {
      localDPUrl.putParam("lng", Location.FMT.format(localLocation.latitude()));
      localDPUrl.putParam("lat", Location.FMT.format(localLocation.longitude()));
      if (localLocation.city() != null)
        localDPUrl.putParam("loccityid", localLocation.city().id());
    }
    return BasicMApiRequest.mapiGet(localDPUrl.toString(), CacheType.DISABLED);
  }

  private boolean hasSectionChanged()
  {
    if (this.homeData.size() != this.homeDataClone.size())
      return true;
    Object localObject2 = this.homeData.keySet();
    Object localObject1 = this.homeDataClone.keySet();
    localObject2 = ((Set)localObject2).iterator();
    localObject1 = ((Set)localObject1).iterator();
    while (((Iterator)localObject2).hasNext())
      if (!((String)((Iterator)localObject2).next()).equals(((Iterator)localObject1).next()))
        return true;
    return false;
  }

  private void sendDeviceInfoReq()
  {
    if (this.mDeviceInfoReq != null)
    {
      mapiService().abort(this.mDeviceInfoReq, this, true);
      this.mDeviceInfoReq = null;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("deviceinfo");
    localArrayList.add(DeviceUtils.cxInfo("open"));
    this.mDeviceInfoReq = BasicMApiRequest.mapiPost("http://mapi.dianping.com/cd/deviceinfo.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.mDeviceInfoReq, this);
  }

  private void stopRequest()
  {
    if (this.opsRequest != null)
    {
      mapiService().abort(this.opsRequest, this, true);
      this.opsRequest = null;
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      private final int STEP = 5;
      private int counter = 0;
      LinkedHashMap<String, AgentInfo> defaultAgents = new LinkedHashMap();

      private void generaterAgentInfoItem(String paramString)
      {
        generaterAgentInfoItem(paramString, paramString);
      }

      private void generaterAgentInfoItem(String paramString1, String paramString2)
      {
        this.counter += 5;
        this.defaultAgents.put(paramString1, new AgentInfo((Class)MainHomeFragment.agentClassMap.get(paramString2), MainHomeFragment.this.df.format(this.counter)));
      }

      private void reset()
      {
        this.counter = 0;
        this.defaultAgents.clear();
      }

      public Map<String, AgentInfo> getAgentInfoList()
      {
        reset();
        generaterAgentInfoItem("home/HomeTitleSection");
        generaterAgentInfoItem("home/HomeModeSwitchSection");
        generaterAgentInfoItem("home/HomeBannerSection");
        generaterAgentInfoItem("home/HomeCategorSection");
        generaterAgentInfoItem("home/HomeSceneModeAgentSection");
        Iterator localIterator = MainHomeFragment.this.homeData.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str1 = (String)localIterator.next();
          if ("home/HomeBannerSection".equals(str1))
            continue;
          String str2 = str1.substring(0, str1.indexOf(";"));
          if (str2.equals("home/HomeRecommendSection"))
          {
            if (MainHomeFragment.this.isOneVerticalChannel)
            {
              generaterAgentInfoItem("home/HomeVerticalchannelSection");
              MainHomeFragment.access$102(MainHomeFragment.this, false);
            }
            generaterAgentInfoItem(str1, str2);
            continue;
          }
          if (!MainHomeFragment.agentClassMap.containsKey(str2))
            continue;
          generaterAgentInfoItem(str1, str2);
        }
        if (MainHomeFragment.this.isOneVerticalChannel)
          generaterAgentInfoItem("home/HomeVerticalchannelSection");
        generaterAgentInfoItem("home/HomeGuesslikeSection");
        generaterAgentInfoItem("home/HomeRetrySection");
        MainHomeFragment.access$102(MainHomeFragment.this, true);
        return this.defaultAgents;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        return null;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public ArrayList<HomeAgent.HomeAgentAdapter> getMergeAdapter()
  {
    return ((TypeMergeRecyclerAdapter)this.mergeAdapter).mTypeAdaptes;
  }

  public int getRequestStatus()
  {
    return this.isRequestStatus;
  }

  public boolean getShowRefresh()
  {
    Iterator localIterator;
    if (this.isRequestStatus == 3)
    {
      i = 1;
      localIterator = this.agentList.iterator();
    }
    Object localObject;
    do
    {
      if (localIterator.hasNext())
      {
        localObject = (String)localIterator.next();
        if (i != 0);
      }
      else
      {
        return i;
        i = 0;
        break;
      }
      localObject = (CellAgent)this.agents.get(localObject);
    }
    while ((!(localObject instanceof HomeAgent)) || (!((HomeAgent)localObject).showRetry()));
    if (((HomeAgent)localObject).isRequestStatus == 3);
    for (int i = 1; ; i = 0)
      break;
  }

  public boolean locationCare()
  {
    return true;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    sendDeviceInfoReq();
    cityConfig().addListener(this);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity1 == null)
      return;
    this.homeData.clear();
    stopRequest();
    sendOpsRequest();
    configService().refresh();
    dispatchCellChanged(null);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.requestRetrieved = paramBundle.getBoolean("requestRetrieved");
    while (true)
    {
      resetAgentHashMap();
      return;
      setHost("home");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.main_home_fragment, paramViewGroup, false);
    this.pullToRefreshListView = ((PullToRefreshRecyclerView)paramLayoutInflater.findViewById(R.id.main_listview));
    this.pullToRefreshListView.getRecycledViewPool().setMaxRecycledViews(((Integer)agentAdpaterTypeMap.get("home/HomeGuesslikeSection")).intValue(), 15);
    this.pullToRefreshListView.setItemAnimator(null);
    this.pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.pullToRefreshListView.setOnRefreshListener(this.refreshListener);
    setAgentContainerRecyclerView(this.pullToRefreshListView);
    setAgentContainerView((LinearLayout)paramLayoutInflater.findViewById(R.id.title_bar_layout));
    SkinManager.setHomeListSkin(getActivity(), paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    cityConfig().removeListener(this);
    super.onDestroy();
    stopRequest();
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if (!paramBoolean)
    {
      dispatchAgentChanged("home/HomeSceneModeAgentSection", null);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          if ((MainHomeFragment.this.getActivity() instanceof DPActivity))
            ((DPActivity)MainHomeFragment.this.getActivity()).showGAView("home");
        }
      }
      , 500L);
    }
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if ((getActivity() instanceof MainActivity))
      ((MainActivity)getActivity()).checkShouldShowSwitchCityDialog(true);
  }

  public final void onRefresh()
  {
    if (getActivity() == null)
      return;
    this.isRefresh = true;
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (String)localIterator.next();
      localObject = (CellAgent)this.agents.get(localObject);
      if (!(localObject instanceof HomeAgent.OnCellRefreshListener))
        continue;
      ((HomeAgent.OnCellRefreshListener)localObject).onRefresh();
    }
    cleanCateCache(cityId());
    stopRequest();
    sendOpsRequest();
    onRefreshRequest();
  }

  public void onRefreshComplete()
  {
    onRefreshCompleteRequest();
    onRefreshListComplete();
  }

  public void onRefreshCompleteRequest()
  {
    this.requestCount -= 1;
  }

  public void onRefreshListComplete()
  {
    if (this.requestCount <= 0)
    {
      this.isRefresh = false;
      this.pullToRefreshListView.onRefreshComplete();
    }
  }

  public void onRefreshRequest()
  {
    this.requestCount += 1;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mDeviceInfoReq)
    {
      this.mDeviceInfoReq = null;
      DPApplication.instance().getSharedPreferences("cx", 0).edit().putString("cdpid", "0").commit();
    }
    do
      return;
    while (paramMApiRequest != this.opsRequest);
    this.opsRequest = null;
    this.dataChanged = true;
    this.isRequestStatus = 3;
    paramMApiRequest = new Bundle();
    paramMApiRequest.putString("flag", "refreshflag");
    dispatchAgentChanged("home/HomeRetrySection", paramMApiRequest);
    dispatchCellChanged(null);
    if (this.isRefresh)
      onRefreshComplete();
    this.dataChanged = false;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mDeviceInfoReq)
    {
      this.mDeviceInfoReq = null;
      if (paramMApiResponse != null);
    }
    do
    {
      do
      {
        return;
        paramMApiRequest = (DPObject)paramMApiResponse.result();
      }
      while ((paramMApiRequest == null) || (!"ok".equalsIgnoreCase(paramMApiRequest.getString("Title"))));
      paramMApiRequest = paramMApiRequest.getString("CreditID");
      DPApplication.instance().getSharedPreferences("cx", 0).edit().putString("cdpid", paramMApiRequest).commit();
      return;
    }
    while (paramMApiRequest != this.opsRequest);
    this.opsRequest = null;
    if ((paramMApiResponse.result() instanceof DPObject))
      try
      {
        this.homeDataClone = ((LinkedHashMap)this.homeData.clone());
        this.homeData.clear();
        JSONArray localJSONArray = new JSONObject(((DPObject)paramMApiResponse.result()).getString("OpsModuleList")).getJSONArray("homeSections");
        int i = 0;
        while (i < localJSONArray.length())
        {
          JSONObject localJSONObject = (JSONObject)localJSONArray.get(i);
          paramMApiResponse = localJSONObject.getString("sectionType");
          paramMApiRequest = paramMApiResponse;
          if (!"home/HomeBannerSection".equals(paramMApiResponse))
            paramMApiRequest = localJSONObject.getString("sectionType") + ";" + i;
          this.homeData.put(paramMApiRequest, localJSONObject);
          i += 1;
        }
      }
      catch (Exception paramMApiRequest)
      {
        paramMApiRequest.printStackTrace();
      }
    this.dataChanged = true;
    this.isRequestStatus = 2;
    paramMApiRequest = new Bundle();
    paramMApiRequest.putString("flag", "refreshflag");
    dispatchAgentChanged("home/HomeRetrySection", paramMApiRequest);
    if (hasSectionChanged())
      resetAgents(null);
    while (true)
    {
      if (this.isRefresh)
        onRefreshComplete();
      this.dataChanged = false;
      return;
      dispatchCellChanged(null);
    }
  }

  protected void onRequestGpsCanceled()
  {
  }

  public void onResume()
  {
    super.onResume();
    sendOpsRequest();
    if (!isHidden())
      ((DPActivity)getActivity()).showGAView("home");
  }

  public final void onRetry()
  {
    if ((getActivity() == null) || (this.isRefresh))
      return;
    Iterator localIterator = this.agentList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (String)localIterator.next();
      localObject = (CellAgent)this.agents.get(localObject);
      if (!(localObject instanceof HomeAgent.OnCellRetryListener))
        continue;
      ((HomeAgent.OnCellRetryListener)localObject).onRetry();
    }
    sendOpsRequest();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("requestRetrieved", this.requestRetrieved);
  }

  public void resetAdapter()
  {
    this.mergeAdapter = new TypeMergeRecyclerAdapter();
  }

  public void resetAgentHashMap()
  {
    int i = 0;
    Iterator localIterator = agentClassMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      agentAdpaterTypeMap.put(localEntry.getKey(), Integer.valueOf(i));
      try
      {
        int j = ((Class)localEntry.getValue()).getField("adapterTypeCount").getInt(null);
        i += j;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public void sendOpsRequest()
  {
    if (this.opsRequest != null)
      return;
    this.opsRequest = getOpsRequest(cityId());
    mapiService().exec(this.opsRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.MainHomeFragment
 * JD-Core Version:    0.6.0
 */