package com.dianping.hui.fragment;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.fragment.TuanAgentFragment.Styleable;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hui.config.DefaultHuiInfoConfig;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.State;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class HuiDetailAgentFragment extends TuanAgentFragment
  implements AgentFragment.CellStable, TuanAgentFragment.Styleable, RequestHandler<MApiRequest, MApiResponse>
{
  private int agentStyle;
  ViewGroup bottomCellContainer;
  ViewGroup bottomView;
  int dealId;
  DPObject dpHuiDetail;
  MApiRequest huiDetailRequest;
  private boolean huiDetailRetrieved;
  protected int huiId;
  PullToRefreshScrollView pullToRefreshScrollView;
  ViewGroup rootView;
  MyScrollView scrollView;
  protected int shopId;
  CellAgent topCellAgent;
  ViewGroup topCellContainer;

  private void dispatchHuiDetailChanged(Parcelable paramParcelable)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("huidetail", this.dpHuiDetail);
    localBundle.putInt("huiid", this.huiId);
    localBundle.putInt("shopid", this.shopId);
    localBundle.putInt("dealid", this.dealId);
    localBundle.putInt("status", getHuiStatus());
    if (paramParcelable != null)
      localBundle.putParcelable("extra", paramParcelable);
    dispatchAgentChanged(null, localBundle);
  }

  private void resolveArguments()
  {
    this.huiId = getIntParam("id");
    this.shopId = getIntParam("shopid");
    this.dealId = getIntParam("dealid");
    String str;
    if (this.huiId <= 0)
      str = getStringParam("id");
    try
    {
      this.huiId = Integer.parseInt(str);
      label52: if (this.shopId <= 0)
        str = getStringParam("shopid");
      try
      {
        this.shopId = Integer.parseInt(str);
        label74: if (this.dealId <= 0)
          str = getStringParam("dealid");
        try
        {
          this.dealId = Integer.parseInt(str);
          return;
        }
        catch (Exception localException1)
        {
          return;
        }
      }
      catch (Exception localException2)
      {
        break label74;
      }
    }
    catch (Exception localException3)
    {
      break label52;
    }
  }

  private void sendHuiDetailRequest()
  {
    if (this.huiDetailRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.t.dianping.com/huidetailgn.bin").buildUpon();
    localBuilder.appendQueryParameter("huiid", Integer.toString(this.huiId));
    localBuilder.appendQueryParameter("shopid", Integer.toString(this.shopId));
    localBuilder.appendQueryParameter("dealid", Integer.toString(this.dealId));
    localBuilder.appendQueryParameter("cityid", Integer.toString(cityId()));
    Object localObject = location();
    if (localObject != null)
    {
      localBuilder.appendQueryParameter("lat", Double.toString(((Location)localObject).latitude()));
      localBuilder.appendQueryParameter("lng", Double.toString(((Location)localObject).longitude()));
    }
    localObject = accountService().token();
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localBuilder.appendQueryParameter("token", (String)localObject);
    this.huiDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.huiDetailRequest, this);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DefaultHuiInfoConfig());
    return localArrayList;
  }

  public int getHuiStatus()
  {
    if (this.huiDetailRetrieved)
      return 1;
    if (this.huiDetailRequest != null)
      return 2;
    return -1;
  }

  public int getStyle()
  {
    return this.agentStyle;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    resolveArguments();
    if (!this.huiDetailRetrieved)
      sendHuiDetailRequest();
    dispatchHuiDetailChanged(null);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.hui_detail_fragment_layout, paramViewGroup, false);
    this.rootView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.root));
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
    this.pullToRefreshScrollView.setOnRefreshListener(new HuiDetailAgentFragment.1(this));
    this.scrollView.setOnScrollListener(new HuiDetailAgentFragment.2(this));
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
    if (this.huiDetailRequest != null)
    {
      mapiService().abort(this.huiDetailRequest, this, false);
      this.huiDetailRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.huiDetailRequest)
    {
      this.huiDetailRetrieved = false;
      this.huiDetailRequest = null;
      resetAgents(null);
      dispatchHuiDetailChanged(null);
      if (getActivity() != null)
      {
        if ((this.pullToRefreshScrollView.getState() != PullToRefreshBase.State.REFRESHING) || (getActivity() == null))
          break label70;
        showToast("刷新失败");
      }
    }
    while (true)
    {
      this.pullToRefreshScrollView.onRefreshComplete();
      return;
      label70: if (paramMApiResponse.message() == null)
        continue;
      showToast(paramMApiResponse.message().content());
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.huiDetailRequest)
    {
      this.dpHuiDetail = ((DPObject)paramMApiResponse.result());
      this.huiDetailRetrieved = true;
      this.huiDetailRequest = null;
      resetAgents(null);
      paramMApiRequest = new Bundle();
      paramMApiRequest.putBoolean("detailretrieved", this.huiDetailRetrieved);
      dispatchHuiDetailChanged(paramMApiRequest);
      this.pullToRefreshScrollView.onRefreshComplete();
    }
  }

  public void onResume()
  {
    super.onResume();
    if (!this.huiDetailRetrieved)
      sendHuiDetailRequest();
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.bottomCellContainer.removeAllViews();
    this.bottomCellContainer.addView(paramView);
    this.bottomView.setVisibility(0);
  }

  public void setStyle(int paramInt)
  {
    this.agentStyle = paramInt;
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
    this.topCellContainer.removeAllViews();
    this.topCellAgent = paramCellAgent;
    this.topCellContainer.addView(paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.fragment.HuiDetailAgentFragment
 * JD-Core Version:    0.6.0
 */