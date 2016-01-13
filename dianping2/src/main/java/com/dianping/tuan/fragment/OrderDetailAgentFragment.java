package com.dianping.tuan.fragment;

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
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.config.DefaultOrderDetailConfig;
import com.dianping.tuan.config.HotelBookingOrderDetailConfig;
import com.dianping.tuan.config.OrderDishOrderDetailConfig;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class OrderDetailAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  private final String TAG = "OrderDetailAgentFragment";
  ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  private ViewGroup bottomCellContainer;
  private ViewGroup bottomView;
  private DPObject dpOrder;
  private boolean isMemberCard;
  private MApiRequest loadOrderInfoRequest;
  private BroadcastReceiver mOrderChangedListener = new OrderDetailAgentFragment.1(this);
  private int orderId;
  private int orderType;
  private LinearLayout rootView;

  private void dispatchOrderChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("order", this.dpOrder);
    dispatchAgentChanged(null, localBundle);
  }

  private void loadOrderInfo()
  {
    if (this.loadOrderInfoRequest != null)
      Log.i("OrderDetailAgentFragment", "loadOrderInfoRequest is running");
    do
      return;
    while (TextUtils.isEmpty(accountService().token()));
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("ordergn.bin");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.orderId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    localUrlBuilder.addParam("dealtype", Integer.valueOf(this.orderType));
    if (this.isMemberCard);
    for (int i = 1; ; i = 0)
    {
      localUrlBuilder.addParam("ismembercard", Integer.valueOf(i));
      this.loadOrderInfoRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.CRITICAL);
      mapiService().exec(this.loadOrderInfoRequest, this);
      return;
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return this.agentListConfigs;
  }

  public DPObject getOrder()
  {
    return this.dpOrder;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dpOrder != null)
    {
      paramBundle = new Bundle();
      paramBundle.putParcelable("order", this.dpOrder);
      dispatchAgentChanged("tuanorder/header", paramBundle);
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("order", this.dpOrder);
    switch (paramInt1)
    {
    default:
    case 11:
    case 111:
    }
    while (true)
    {
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
      localBundle.putBoolean("need_reload", true);
      dispatchAgentChanged("tuanorder/hotel", localBundle);
      continue;
      localBundle.putBoolean("need_reload", true);
      dispatchAgentChanged("tuanorder/footer", localBundle);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new OrderDishOrderDetailConfig(this));
    this.agentListConfigs.add(new HotelBookingOrderDetailConfig(this));
    this.agentListConfigs.add(new DefaultOrderDetailConfig(this));
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
    if (paramBundle != null)
      this.dpOrder = ((DPObject)paramBundle.getParcelable("order"));
    if (this.dpOrder == null)
    {
      paramViewGroup = (DPObject)getActivity().getIntent().getParcelableExtra("order");
      if (paramViewGroup != null)
      {
        this.dpOrder = paramViewGroup;
        this.orderId = paramViewGroup.getInt("ID");
        this.orderType = paramViewGroup.getInt("DealType");
      }
      for (this.isMemberCard = paramViewGroup.getBoolean("ismembercard"); this.orderId == 0; this.isMemberCard = getBooleanParam("ismembercard"))
      {
        Toast.makeText(getActivity(), "订单信息为空", 1).show();
        getActivity().finish();
        return paramLayoutInflater;
        this.orderId = getIntParam("orderid", 0);
        this.orderType = getIntParam("ordertype", 1);
      }
      loadOrderInfo();
    }
    while (true)
    {
      paramViewGroup = new IntentFilter("tuan:add_review_successfully");
      registerReceiver(this.mOrderChangedListener, paramViewGroup);
      return paramLayoutInflater;
      dispatchOrderChanged();
    }
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
      resetAgents(null);
      dispatchOrderChanged();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if ((paramBundle != null) && (this.dpOrder != null))
      paramBundle.putParcelable("order", this.dpOrder);
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
 * Qualified Name:     com.dianping.tuan.fragment.OrderDetailAgentFragment
 * JD-Core Version:    0.6.0
 */