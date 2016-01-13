package com.dianping.pm.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.pm.config.PmOrderDetailDefaultConfig;
import com.dianping.pm.config.PmVoucherOrderDetailConfig;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class PmOrderDetailAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  private final String TAG = "PmOrderDetailAgentFragment";
  ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  private ViewGroup bottomCellContainer;
  private ViewGroup bottomView;
  private DPObject dpOrder;
  private MApiRequest loadOrderInfoRequest;
  private BroadcastReceiver mOrderChangedListener = new PmOrderDetailAgentFragment.1(this);
  private int orderId;
  private int productType;
  private LinearLayout rootView;

  private void dispatchOrderChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("pointproductorder", this.dpOrder);
    localBundle.putInt("orderid", this.orderId);
    localBundle.putInt("producttype", this.productType);
    dispatchAgentChanged(null, localBundle);
  }

  private void loadOrderInfo()
  {
    if (this.loadOrderInfoRequest != null)
      Log.i("PmOrderDetailAgentFragment", "loadOrderInfoRequest is running");
    do
      return;
    while (TextUtils.isEmpty(accountService().token()));
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("orderpm.bin");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.orderId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("producttype", Integer.valueOf(this.productType));
    this.loadOrderInfoRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.loadOrderInfoRequest, this);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return this.agentListConfigs;
  }

  public int getProductType()
  {
    return this.productType;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dpOrder != null)
    {
      paramBundle = new Bundle();
      paramBundle.putParcelable("pointproductorder", this.dpOrder);
      dispatchAgentChanged("tuanorder/header", paramBundle);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new PmVoucherOrderDetailConfig(this));
    this.agentListConfigs.add(new PmOrderDetailDefaultConfig());
    IntentFilter localIntentFilter = new IntentFilter("tuan:add_review_successfully");
    registerReceiver(this.mOrderChangedListener, localIntentFilter);
    if (paramBundle != null)
      this.dpOrder = ((DPObject)paramBundle.getParcelable("pointproductorder"));
    if (this.dpOrder == null)
    {
      paramBundle = (DPObject)getActivity().getIntent().getParcelableExtra("pointproductorder");
      if (paramBundle != null)
      {
        this.dpOrder = paramBundle;
        this.orderId = paramBundle.getInt("OrderId");
      }
      for (this.productType = paramBundle.getInt("ProductType"); this.orderId == 0; this.productType = getIntParam("producttype", 1))
      {
        Toast.makeText(getActivity(), "订单信息为空", 1).show();
        getActivity().finish();
        return;
        this.orderId = getIntParam("orderid", 0);
      }
      loadOrderInfo();
      return;
    }
    dispatchOrderChanged();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.base_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.bottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.bottomCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.addView(this.bottomCellContainer);
    this.bottomView.setVisibility(8);
    setAgentContainerView(this.rootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    unregisterReceiver(this.mOrderChangedListener);
    super.onDestroy();
  }

  public void onLogin(boolean paramBoolean)
  {
    super.onLogin(paramBoolean);
    if ((paramBoolean) && (this.orderId != 0))
      loadOrderInfo();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.loadOrderInfoRequest)
    {
      this.loadOrderInfoRequest = null;
      if (getActivity() != null)
      {
        Toast.makeText(getActivity(), paramMApiResponse.message().content(), 0).show();
        dispatchOrderChanged();
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.loadOrderInfoRequest)
    {
      this.loadOrderInfoRequest = null;
      this.dpOrder = ((DPObject)paramMApiResponse.result());
      if (this.dpOrder != null)
      {
        this.orderId = this.dpOrder.getInt("OrderId");
        this.productType = this.dpOrder.getInt("ProductType");
      }
      dispatchOrderChanged();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if ((paramBundle != null) && (this.dpOrder != null))
      paramBundle.putParcelable("pointproductorder", this.dpOrder);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.bottomCellContainer.removeAllViews();
    this.bottomCellContainer.addView(paramView);
    this.bottomView.setVisibility(0);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.fragment.PmOrderDetailAgentFragment
 * JD-Core Version:    0.6.0
 */