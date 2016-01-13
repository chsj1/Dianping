package com.dianping.tuan.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.config.TuanOrderRefundConfig;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class TuanRefundAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  private final String TAG = "TuanRefundAgentFragment";
  ArrayList<AgentListConfig> mAgentListConfigs;
  private MApiRequest mApplyRequest;
  protected LinearLayout mBottomView;
  protected LinearLayout mContentView;
  protected View mErrorView;
  protected View mLoadingView;
  private int mOrderId;
  protected PullToRefreshScrollView mPullToRefreshScrollView;
  protected DPObject mRefundApplyInfoObj;
  protected LinearLayout mRootView;

  private boolean parseIntent()
  {
    try
    {
      this.mOrderId = getIntParam("orderid");
      if (this.mOrderId != 0)
        return true;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
    return false;
  }

  private void queryRefundApply()
  {
    if (this.mApplyRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("refundapplicationgn.bin");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.mOrderId));
    this.mApplyRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    showProgressDialog("正在请求退款信息...");
    mapiService().exec(this.mApplyRequest, this);
    if (!this.mPullToRefreshScrollView.isRefreshing())
      this.mLoadingView.setVisibility(0);
    this.mErrorView.setVisibility(8);
    this.mContentView.setVisibility(8);
  }

  protected void dispatchDataChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("orderid", this.mOrderId);
    if ((this.mRefundApplyInfoObj != null) && (DPObjectUtils.isDPObjectof(this.mRefundApplyInfoObj, "RefundApplication")))
      localBundle.putParcelable("applyinfo", this.mRefundApplyInfoObj);
    dispatchAgentChanged(null, localBundle);
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    super.dispatchMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && (paramAgentMessage.what == "appy_refund_refund_success"))
      getActivity().finish();
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    this.mAgentListConfigs = new ArrayList();
    this.mAgentListConfigs.add(new TuanOrderRefundConfig());
    return this.mAgentListConfigs;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.mOrderId <= 0)
      getActivity().finish();
    do
      return;
    while (!isLogined());
    queryRefundApply();
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.i("TuanRefundAgentFragment", "onCreate");
    super.onCreate(paramBundle);
    this.mOrderId = getIntParam("orderid");
    if (paramBundle != null)
      this.mOrderId = paramBundle.getInt("orderId");
    do
      return;
    while (parseIntent());
    Toast.makeText(getContext(), "退款信息错误", 1).show();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.tuan_order_refund_agent_container, paramViewGroup, false);
    this.mRootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.mRootView.setBackgroundResource(R.drawable.main_background);
    this.mBottomView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.mPullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.scroll));
    this.mLoadingView = paramLayoutInflater.findViewById(R.id.loading);
    this.mErrorView = paramLayoutInflater.findViewById(R.id.error);
    this.mContentView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.mPullToRefreshScrollView.setScrollingWhileRefreshingEnabled(false);
    this.mPullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
    paramViewGroup = this.mPullToRefreshScrollView.getLoadingLayoutProxy(true, false);
    paramViewGroup.setLoadingLayoutBackground(getResources().getDrawable(R.drawable.transparent));
    paramViewGroup.setLoadingDrawable(getResources().getDrawable(R.drawable.dropdown_anim_00));
    paramViewGroup.setBackgroundColor(0);
    this.mPullToRefreshScrollView.setOnRefreshListener(new TuanRefundAgentFragment.1(this));
    setAgentContainerView(this.mRootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mApplyRequest != null)
    {
      mapiService().abort(this.mApplyRequest, this, true);
      this.mApplyRequest = null;
    }
  }

  public void onLogin(boolean paramBoolean)
  {
    AgentMessage localAgentMessage = new AgentMessage("loginResult");
    localAgentMessage.body.putBoolean("loginresult", paramBoolean);
    dispatchMessage(localAgentMessage);
    if (paramBoolean)
      queryRefundApply();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mApplyRequest)
    {
      this.mApplyRequest = null;
      this.mLoadingView.setVisibility(8);
      this.mContentView.setVisibility(8);
      this.mErrorView.setVisibility(0);
      this.mPullToRefreshScrollView.onRefreshComplete();
      if ((paramMApiResponse == null) || (paramMApiResponse.message() == null))
        break label89;
    }
    label89: for (paramMApiRequest = paramMApiResponse.message().content(); ; paramMApiRequest = "请求退款信息失败")
    {
      Toast.makeText(getContext(), paramMApiRequest, 1).show();
      getActivity().finish();
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mApplyRequest)
    {
      this.mApplyRequest = null;
      this.mLoadingView.setVisibility(8);
      this.mPullToRefreshScrollView.onRefreshComplete();
      if (DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "RefundApplication"))
      {
        this.mRefundApplyInfoObj = ((DPObject)paramMApiResponse.result());
        this.mContentView.setVisibility(0);
        resetAgents(null);
        dispatchDataChanged();
      }
    }
    else
    {
      return;
    }
    this.mErrorView.setVisibility(0);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("orderid", this.mOrderId);
    super.onSaveInstanceState(paramBundle);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.mBottomView.removeAllViews();
    this.mBottomView.addView(paramView);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanRefundAgentFragment
 * JD-Core Version:    0.6.0
 */