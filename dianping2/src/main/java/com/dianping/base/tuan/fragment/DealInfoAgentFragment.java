package com.dianping.base.tuan.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.agent.DealInfoAgentClassMap;
import com.dianping.base.tuan.agent.DealInfoStructExtraAgent;
import com.dianping.base.tuan.agent.TuanGroupCellAgent.OnCellRefreshListener;
import com.dianping.base.tuan.config.BaseDealinfoConfig;
import com.dianping.base.tuan.config.BeautyDealinfoConfig;
import com.dianping.base.tuan.config.DefaultDealinfoConfig;
import com.dianping.base.tuan.config.FunDealinfoConfig;
import com.dianping.base.tuan.config.HotelDealInfoConfig;
import com.dianping.base.tuan.config.ScenicDealInfoConfig;
import com.dianping.base.tuan.config.SelectDealInfoConfig;
import com.dianping.base.tuan.config.TraveDealInfoConfig;
import com.dianping.base.util.PurchaseResultHelper;
import com.dianping.base.util.PurchaseResultHelper.PayFailTo;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.moduleconfig.AgentHelper;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.State;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map<Ljava.lang.String;Lcom.dianping.base.app.loader.AgentInfo;>;

public class DealInfoAgentFragment extends GroupAgentFragment
  implements AccountListener, TuanAgentFragment.Styleable, AgentFragment.CellStable, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String DEAL_PAGE_HOST = "tuandeal/";
  private static final String DEAL_VIEW_PREFIX = "tuandeal_";
  ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  private int agentStyle;
  protected ViewGroup bottomCellContainer;
  protected ViewGroup bottomView;
  protected MApiRequest dealDetailRequest;
  protected boolean dealDetailRetrieved;
  public int dealId;
  protected MApiRequest dealRequest;
  protected boolean dealRetrieved;
  protected DPObject dpDeal;
  protected DPObject dpDealDetail;
  protected boolean isDealSplit = true;
  protected boolean isFirstSlide = true;
  public int isGoodShop = 0;
  protected PullToRefreshScrollView pullToRefreshScrollView;
  protected MyResources res;
  protected FrameLayout rootView;
  protected MyScrollView scrollView;
  public int shopId = 0;
  protected CellAgent topCellAgent;
  protected ViewGroup topCellContainer;

  private void dispatchDealChanged(Parcelable paramParcelable)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("deal", this.dpDeal);
    localBundle.putInt("dealid", this.dealId);
    localBundle.putInt("shopid", this.shopId);
    localBundle.putInt("status", getDealStatus());
    if (paramParcelable != null)
      localBundle.putParcelable("extra", paramParcelable);
    dispatchAgentChanged(null, localBundle);
  }

  private String formatNumber(int paramInt)
  {
    return String.format("%05d", new Object[] { Integer.valueOf(paramInt) });
  }

  private Map<String, AgentInfo> getDealinfoAgentList()
  {
    Object localObject1;
    if ((this.dpDeal == null) || (this.dpDeal.getObject("DealStyle") == null) || (android.text.TextUtils.isEmpty(this.dpDeal.getObject("DealStyle").getString("ModuleKey"))))
      localObject1 = null;
    while (true)
    {
      return localObject1;
      localObject1 = "tuan_deal";
      if (this.dpDeal == null)
        break;
      Object localObject2 = this.dpDeal.getObject("DealStyle");
      if (localObject2 == null)
        break;
      localObject2 = ((DPObject)localObject2).getString("ModuleKey");
      if (!android.text.TextUtils.isEmpty((CharSequence)localObject2))
        localObject1 = localObject2;
      localObject2 = null;
      List localList = AgentHelper.getInstance().getAgentList(getActivity(), "tuandeal_" + (String)localObject1);
      localObject1 = localObject2;
      if (localList == null)
        continue;
      localObject1 = localObject2;
      if (localList.size() <= 0)
        continue;
      localObject2 = new HashMap();
      int j = 0;
      int k = 0;
      int i;
      if (localList != null)
        i = localList.size();
      while (true)
      {
        localObject1 = localObject2;
        if (k >= i)
          break;
        localObject1 = (ArrayList)localList.get(k);
        int i1 = 0;
        int n = 0;
        int m;
        label195: String str;
        if (localObject1 != null)
        {
          m = ((ArrayList)localObject1).size();
          if (n >= m)
            break label471;
          str = (String)((ArrayList)localObject1).get(n);
          if ((android.text.TextUtils.isEmpty(str)) || (DealInfoAgentClassMap.getAgentClass(str) == null))
            break label443;
          Log.i("DealConfig", "find class = " + DealInfoAgentClassMap.getAgentClass(str) + ", it's index = " + formatNumber(j) + "." + formatNumber(i1));
          if (DealInfoAgentClassMap.getAgentClass(str) != DealInfoStructExtraAgent.class)
            break label368;
          ((Map)localObject2).put("tuandeal/" + str, new AgentInfo(DealInfoAgentClassMap.getAgentClass(str), formatNumber(j)));
          label340: i1 += 10;
        }
        while (true)
        {
          n += 1;
          break label195;
          i = 0;
          break;
          m = 0;
          break label195;
          label368: ((Map)localObject2).put("tuandeal/" + str, new AgentInfo(DealInfoAgentClassMap.getAgentClass(str), formatNumber(j) + "." + formatNumber(i1)));
          break label340;
          label443: Log.i("DealConfig", "couldn't find class = " + str);
        }
        label471: j += 10;
        k += 1;
      }
    }
    return (Map<String, AgentInfo>)(Map<String, AgentInfo>)null;
  }

  private void resolveArguments(Bundle paramBundle)
  {
    this.dealId = getIntParam("id");
    String str;
    if (this.dealId == 0)
      str = getStringParam("id");
    try
    {
      this.dealId = Integer.parseInt(str);
      label32: str = getStringParam("_fb_");
      if (!com.dianping.util.TextUtils.isEmpty(str))
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("act", String.valueOf(4));
        AdClientUtils.report(str, localHashMap);
      }
      if (paramBundle == null)
        this.dpDeal = getObjectParam("deal");
      for (this.dealRetrieved = false; ; this.dealRetrieved = paramBundle.getBoolean("dealRetrieved"))
      {
        if ((this.dealId == 0) && (this.dpDeal != null))
          this.dealId = this.dpDeal.getInt("ID");
        this.shopId = getIntParam("shopId", 0);
        this.isGoodShop = getIntParam("isgoodshop", 0);
        return;
        this.dpDeal = ((DPObject)paramBundle.getParcelable("deal"));
      }
    }
    catch (Exception localException)
    {
      break label32;
    }
  }

  private void slideGa()
  {
    if (this.isFirstSlide)
      statisticsEvent("tuan5", "tuan5_detail_slide", null, 0);
    this.isFirstSlide = false;
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    if (paramAgentMessage != null)
    {
      if ("dealInfoShop".equals(paramAgentMessage.what))
      {
        if ((paramAgentMessage.body == null) || (paramAgentMessage.body.getParcelable("shop") == null))
          break label148;
        this.shopId = ((DPObject)paramAgentMessage.body.getParcelable("shop")).getInt("ID");
        if ((this.dealRetrieved) && (this.shopId != 0) && (!this.dealDetailRetrieved))
          sendDealDetailRequest();
      }
      if ("dealInfoDealDetailResult".equals(paramAgentMessage.what))
      {
        if (!paramAgentMessage.body.getBoolean("dealDetailRetrieved"))
          break label163;
        this.dpDealDetail = ((DPObject)paramAgentMessage.body.getParcelable("dpDealDetail"));
        this.dealDetailRetrieved = true;
        mergeDetailToDeal();
        dispatchDealChanged(null);
      }
    }
    while (true)
    {
      super.dispatchMessage(paramAgentMessage);
      return;
      label148: if (this.shopId != 0)
        break;
      this.shopId = -1;
      break;
      label163: this.dealDetailRetrieved = false;
      dispatchDealChanged(null);
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    this.agentListConfigs.clear();
    Map localMap = getDealinfoAgentList();
    if (localMap != null)
      this.agentListConfigs.add(new DealInfoAgentFragment.3(this, this, localMap));
    while (true)
    {
      this.isDealSplit = getCurrentAgentListConfig().isDealSplit();
      getCurrentAgentListConfig().onCreateViewGA();
      return this.agentListConfigs;
      this.agentListConfigs.add(new FunDealinfoConfig(this));
      this.agentListConfigs.add(new BeautyDealinfoConfig(this));
      this.agentListConfigs.add(new HotelDealInfoConfig(this));
      this.agentListConfigs.add(new TraveDealInfoConfig(this));
      this.agentListConfigs.add(new ScenicDealInfoConfig(this));
      this.agentListConfigs.add(new SelectDealInfoConfig(this));
      this.agentListConfigs.add(new DefaultDealinfoConfig(this));
    }
  }

  protected BaseDealinfoConfig getCurrentAgentListConfig()
  {
    Iterator localIterator = this.agentListConfigs.iterator();
    while (localIterator.hasNext())
    {
      AgentListConfig localAgentListConfig = (AgentListConfig)localIterator.next();
      if (localAgentListConfig.shouldShow())
        return (BaseDealinfoConfig)localAgentListConfig;
    }
    return null;
  }

  public DPObject getDeal()
  {
    return this.dpDeal;
  }

  public int getDealStatus()
  {
    if (this.dealRetrieved)
      return 1;
    if (this.dealRequest != null)
      return 2;
    return -1;
  }

  public GAUserInfo getGAUserInfo()
  {
    return getCurrentAgentListConfig().getGAUserInfo();
  }

  public int getStyle()
  {
    return this.agentStyle;
  }

  public boolean hasChannelTag(String paramString)
  {
    List localList;
    if ((this.dpDeal != null) && (this.dpDeal.getStringArray("DealChannelTags") != null))
      localList = Arrays.asList(this.dpDeal.getStringArray("DealChannelTags"));
    return (localList != null) && (localList.contains(paramString));
  }

  protected void mergeDetailToDeal()
  {
    if ((this.isDealSplit) && (this.dealRetrieved) && (this.dealDetailRetrieved) && (this.dpDeal.getInt("ID") == this.dpDealDetail.getInt("ID")))
      this.dpDeal = this.dpDeal.edit().putBoolean("Interested", this.dpDealDetail.getBoolean("Interested")).putString("ReviewRatio", this.dpDealDetail.getString("ReviewRatio")).putString("TotalReviewRecommend", this.dpDealDetail.getString("TotalReviewRecommend")).putString("TotalReview", this.dpDealDetail.getString("TotalReview")).putArray("DealComments", this.dpDealDetail.getArray("DealComments")).putArray("StructedDetails", this.dpDealDetail.getArray("StructedDetails")).putArray("DetailInfo", this.dpDealDetail.getArray("DetailInfo")).putInt("TagType", this.dpDealDetail.getInt("TagType")).putInt("ReviewType", this.dpDealDetail.getInt("ReviewType")).putArray("Extra", this.dpDealDetail.getArray("Extra")).putObject("HotelDealGroupDetailInfo", this.dpDealDetail.getObject("HotelDealGroupDetailInfo")).generate();
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    this.dealRetrieved = false;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (!this.dealRetrieved)
      sendDealRequest();
    dispatchDealChanged(null);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new FunDealinfoConfig(this));
    this.agentListConfigs.add(new BeautyDealinfoConfig(this));
    this.agentListConfigs.add(new HotelDealInfoConfig(this));
    this.agentListConfigs.add(new TraveDealInfoConfig(this));
    this.agentListConfigs.add(new ScenicDealInfoConfig(this));
    this.agentListConfigs.add(new SelectDealInfoConfig(this));
    this.agentListConfigs.add(new DefaultDealinfoConfig(this));
    this.res = MyResources.getResource(DealInfoAgentFragment.class);
    resolveArguments(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.deal_info, paramViewGroup, false);
    this.rootView = ((FrameLayout)paramLayoutInflater.findViewById(R.id.root));
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.scroll));
    this.scrollView = ((MyScrollView)this.pullToRefreshScrollView.getRefreshableView());
    this.bottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.pullToRefreshScrollView.setScrollingWhileRefreshingEnabled(false);
    this.pullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
    paramViewGroup = this.pullToRefreshScrollView.getLoadingLayoutProxy(true, false);
    paramViewGroup.setLoadingLayoutBackground(getResources().getDrawable(R.drawable.transparent));
    paramViewGroup.setLoadingDrawable(getResources().getDrawable(R.drawable.dropdown_anim_00));
    paramViewGroup.setBackgroundColor(0);
    this.pullToRefreshScrollView.setOnRefreshListener(new DealInfoAgentFragment.1(this));
    this.scrollView.setOnScrollListener(new DealInfoAgentFragment.2(this));
    this.topCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.topCellContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 48));
    this.topCellContainer.setVisibility(4);
    this.rootView.addView(this.topCellContainer);
    this.bottomCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.setVisibility(8);
    this.bottomView.addView(this.bottomCellContainer);
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.dealRequest != null)
      mapiService().abort(this.dealRequest, this, true);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public final void onRefresh()
  {
    if (getActivity() == null);
    while (true)
    {
      return;
      Iterator localIterator = this.agentList.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = (CellAgent)this.agents.get(localObject);
        if (!(localObject instanceof TuanGroupCellAgent.OnCellRefreshListener))
          continue;
        ((TuanGroupCellAgent.OnCellRefreshListener)localObject).onRefresh();
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.dealRequest)
    {
      this.dealRetrieved = false;
      this.dealRequest = null;
      resetAgents(null);
      dispatchDealChanged(null);
      if (getActivity() != null)
      {
        if ((this.pullToRefreshScrollView.getState() != PullToRefreshBase.State.REFRESHING) || (getActivity() == null))
          break label100;
        Toast.makeText(getActivity(), "刷新失败", 0).show();
      }
    }
    while (true)
    {
      this.pullToRefreshScrollView.onRefreshComplete();
      if (paramMApiRequest == this.dealDetailRequest)
      {
        this.dealDetailRetrieved = false;
        this.dealDetailRequest = null;
        dispatchDealChanged(null);
      }
      return;
      label100: if (paramMApiResponse.message() == null)
        continue;
      Toast.makeText(getActivity(), paramMApiResponse.message().content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.dealRequest)
    {
      this.dpDeal = ((DPObject)paramMApiResponse.result());
      this.dealRetrieved = true;
      this.dealRequest = null;
      mergeDetailToDeal();
      resetAgents(null);
      if ((this.dealRetrieved) && (this.shopId != 0) && (!this.dealDetailRetrieved))
        sendDealDetailRequest();
      dispatchDealChanged(null);
      PurchaseResultHelper.instance().setDPObject(this.dpDeal);
      PurchaseResultHelper.instance().setPayFailTo(PurchaseResultHelper.PayFailTo.DEALINFO);
      this.pullToRefreshScrollView.onRefreshComplete();
    }
    if (paramMApiRequest == this.dealDetailRequest)
    {
      this.dpDealDetail = ((DPObject)paramMApiResponse.result());
      this.dealDetailRetrieved = true;
      this.dealDetailRequest = null;
      mergeDetailToDeal();
      dispatchDealChanged(null);
    }
  }

  public void onResume()
  {
    super.onResume();
    if (!this.dealRetrieved)
      sendDealRequest();
  }

  protected void sendDealDetailRequest()
  {
    if (this.dealDetailRequest != null)
      return;
    this.dealDetailRequest = getCurrentAgentListConfig().createDealDetailRequest();
    if (this.dealDetailRequest == null)
    {
      dispatchMessage(new AgentMessage("dealInfoSendDealDetail"));
      return;
    }
    mapiService().exec(this.dealDetailRequest, this);
  }

  protected void sendDealRequest()
  {
    if (this.dealRequest != null)
      return;
    this.dealRequest = getCurrentAgentListConfig().createDealRequest();
    mapiService().exec(this.dealRequest, this);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    if (paramView != null)
    {
      if (paramView.getParent() != null)
        ((ViewGroup)paramView.getParent()).removeView(paramView);
      this.bottomCellContainer.removeAllViews();
      this.bottomCellContainer.addView(paramView);
      this.bottomView.setVisibility(0);
      return;
    }
    this.bottomCellContainer.removeAllViews();
    this.bottomView.setVisibility(8);
  }

  public void setStyle(int paramInt)
  {
    this.agentStyle = paramInt;
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
    if (paramView != null)
    {
      if (paramView.getParent() == null)
      {
        this.topCellContainer.removeAllViews();
        this.topCellAgent = paramCellAgent;
        this.topCellContainer.addView(paramView);
      }
      return;
    }
    this.topCellContainer.removeAllViews();
    this.topCellAgent = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.fragment.DealInfoAgentFragment
 * JD-Core Version:    0.6.0
 */