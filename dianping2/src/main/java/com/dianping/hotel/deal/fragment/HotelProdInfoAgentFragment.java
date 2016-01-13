package com.dianping.hotel.deal.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.agent.TuanGroupCellAgent.OnCellRefreshListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.deal.config.DefaultHotelProdInfoConfig;
import com.dianping.hotel.deal.config.HotelProdInfoConfig;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.MyScrollView.OnScrollListener;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.State;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HotelProdInfoAgentFragment extends GroupAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  private ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  public int bizType;
  protected DPObject dpHotelProdBase;
  protected DPObject dpHotelProdDetail;
  protected ViewGroup mBottomCellContainer;
  protected ViewGroup mBottomView;
  protected PullToRefreshScrollView mPullToRefreshScrollView;
  protected FrameLayout mRootView;
  protected MyScrollView mScrollView;
  protected CellAgent mTopCellAgent;
  protected ViewGroup mTopCellContainer;
  public String[] moduleSort;
  protected MApiRequest prodBaseRequest;
  private boolean prodBaseRetrieved;
  protected MApiRequest prodDetailRequest;
  public int productId;

  private void dispatchProdInfoChanged(Parcelable paramParcelable)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("hotelprodbase", this.dpHotelProdBase);
    localBundle.putParcelable("hotelproddetail", this.dpHotelProdDetail);
    localBundle.putInt("status", getDealStatus());
    if (paramParcelable != null)
      localBundle.putParcelable("extra", paramParcelable);
    dispatchAgentChanged(null, localBundle);
  }

  private MApiRequest makeProdBaseRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/hotelm/hotelproductbase.hotel");
    localStringBuilder.append("?cityid=" + cityId());
    localStringBuilder.append("&biztype=" + this.bizType);
    localStringBuilder.append("&productid=" + this.productId);
    return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
  }

  private MApiRequest makeProdDetailRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/hotelm/hotelproductdetail.hotel");
    localStringBuilder.append("?cityid=" + cityId());
    localStringBuilder.append("&biztype=" + this.bizType);
    localStringBuilder.append("&productid=" + this.productId);
    return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
  }

  private void onRefresh()
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

  private void resolveArguments(Bundle paramBundle)
  {
    this.productId = getIntParam("id");
    this.bizType = getIntParam("biztype");
  }

  private void sendProdBaseRequest()
  {
    if (this.prodBaseRequest != null)
      return;
    this.prodBaseRequest = makeProdBaseRequest();
    mapiService().exec(this.prodBaseRequest, this);
  }

  private void sendProdDetailRequest()
  {
    if (this.prodDetailRequest != null)
      return;
    this.prodDetailRequest = makeProdDetailRequest();
    mapiService().exec(this.prodDetailRequest, this);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new HotelProdInfoConfig(this));
    this.agentListConfigs.add(new DefaultHotelProdInfoConfig(this));
    return this.agentListConfigs;
  }

  public int getDealStatus()
  {
    if (this.prodBaseRetrieved)
      return 1;
    if (this.prodBaseRequest != null)
      return 2;
    return -1;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (!this.prodBaseRetrieved)
      sendProdBaseRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    resolveArguments(paramBundle);
    sendProdBaseRequest();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.deal_info, paramViewGroup, false);
    this.mRootView = ((FrameLayout)paramLayoutInflater.findViewById(R.id.root));
    this.mPullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.scroll));
    this.mScrollView = ((MyScrollView)this.mPullToRefreshScrollView.getRefreshableView());
    this.mBottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.mPullToRefreshScrollView.setScrollingWhileRefreshingEnabled(false);
    this.mPullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
    paramViewGroup = this.mPullToRefreshScrollView.getLoadingLayoutProxy(true, false);
    paramViewGroup.setLoadingLayoutBackground(getResources().getDrawable(R.drawable.transparent));
    paramViewGroup.setLoadingDrawable(getResources().getDrawable(R.drawable.dropdown_anim_00));
    paramViewGroup.setBackgroundColor(0);
    this.mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        if (HotelProdInfoAgentFragment.this.prodBaseRequest != null)
        {
          HotelProdInfoAgentFragment.this.mapiService().abort(HotelProdInfoAgentFragment.this.prodBaseRequest, HotelProdInfoAgentFragment.this, true);
          HotelProdInfoAgentFragment.this.prodBaseRequest = null;
        }
        HotelProdInfoAgentFragment.this.sendProdBaseRequest();
        if (HotelProdInfoAgentFragment.this.prodDetailRequest != null)
        {
          HotelProdInfoAgentFragment.this.mapiService().abort(HotelProdInfoAgentFragment.this.prodDetailRequest, HotelProdInfoAgentFragment.this, true);
          HotelProdInfoAgentFragment.this.prodDetailRequest = null;
        }
        HotelProdInfoAgentFragment.this.sendProdDetailRequest();
        HotelProdInfoAgentFragment.this.onRefresh();
      }
    });
    this.mScrollView.setOnScrollListener(new MyScrollView.OnScrollListener()
    {
      public void onScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        if ((HotelProdInfoAgentFragment.this.mTopCellAgent != null) && (HotelProdInfoAgentFragment.this.mTopCellAgent.getView() != null))
        {
          ViewParent localViewParent = HotelProdInfoAgentFragment.this.mTopCellAgent.getView().getParent();
          if (localViewParent == null)
            return;
          if (((ViewGroup)localViewParent).getTop() <= paramInt2)
          {
            HotelProdInfoAgentFragment.this.mTopCellContainer.setVisibility(0);
            return;
          }
          HotelProdInfoAgentFragment.this.mTopCellContainer.setVisibility(4);
          return;
        }
        HotelProdInfoAgentFragment.this.mTopCellContainer.setVisibility(4);
      }
    });
    this.mTopCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.mTopCellContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 48));
    this.mTopCellContainer.setVisibility(4);
    this.mRootView.addView(this.mTopCellContainer);
    this.mBottomCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.mBottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.mBottomView.setVisibility(8);
    this.mBottomView.addView(this.mBottomCellContainer);
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.prodBaseRequest != null)
      mapiService().abort(this.prodBaseRequest, this, true);
    if (this.prodDetailRequest != null)
      mapiService().abort(this.prodDetailRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prodBaseRequest)
    {
      this.prodBaseRetrieved = false;
      this.prodBaseRequest = null;
      this.dpHotelProdBase = null;
      resetAgents(null);
      dispatchProdInfoChanged(null);
      if (getActivity() != null)
      {
        if ((this.mPullToRefreshScrollView.getState() != PullToRefreshBase.State.REFRESHING) || (getActivity() == null))
          break label105;
        Toast.makeText(getActivity(), "刷新失败", 0).show();
      }
    }
    while (true)
    {
      this.mPullToRefreshScrollView.onRefreshComplete();
      if (paramMApiRequest == this.prodDetailRequest)
      {
        this.prodDetailRequest = null;
        this.dpHotelProdDetail = null;
        dispatchProdInfoChanged(null);
      }
      return;
      label105: if (paramMApiResponse.message() == null)
        continue;
      Toast.makeText(getActivity(), paramMApiResponse.message().content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prodBaseRequest)
    {
      this.prodBaseRetrieved = true;
      this.dpHotelProdBase = ((DPObject)paramMApiResponse.result());
      this.moduleSort = this.dpHotelProdBase.getStringArray("ModuleSort");
      this.prodBaseRequest = null;
      resetAgents(null);
      sendProdDetailRequest();
      dispatchProdInfoChanged(null);
      this.mPullToRefreshScrollView.onRefreshComplete();
    }
    if (paramMApiRequest == this.prodDetailRequest)
    {
      this.dpHotelProdDetail = ((DPObject)paramMApiResponse.result());
      this.prodDetailRequest = null;
      dispatchProdInfoChanged(null);
    }
  }

  public void onResume()
  {
    super.onResume();
    if (!this.prodBaseRetrieved)
      sendProdBaseRequest();
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    if (paramView != null)
    {
      if (paramView.getParent() == null)
      {
        this.mBottomCellContainer.removeAllViews();
        this.mBottomCellContainer.addView(paramView);
        this.mBottomView.setVisibility(0);
      }
      return;
    }
    this.mBottomCellContainer.removeAllViews();
    this.mBottomView.setVisibility(8);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
    if (paramView != null)
    {
      if (paramView.getParent() == null)
      {
        this.mTopCellContainer.removeAllViews();
        this.mTopCellAgent = paramCellAgent;
        this.mTopCellContainer.addView(paramView);
      }
      return;
    }
    this.mTopCellContainer.removeAllViews();
    this.mTopCellAgent = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment
 * JD-Core Version:    0.6.0
 */