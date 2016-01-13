package com.dianping.tuan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PurchaseResultHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.config.BasePurchaseResultConfig;
import com.dianping.tuan.config.DefaultPurchaseResultConfig;
import com.dianping.tuan.config.HotelBookingPurchaseResultConfig;
import com.dianping.tuan.config.MoviePurchaseResultConfig;
import com.dianping.tuan.config.OrderDishPurchaseResultConfig;
import com.dianping.tuan.config.ScenicPurchaseResultConfig;
import com.dianping.tuan.config.TravelPurchaseResultConfig;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.util.network.WifiModel;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class PurchaseResultAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final int MSG_RETRY_QUERYORDER = 100;
  public static final int PAYORDERRESULT_STATUS_CANCEL = 3;
  public static final int PAYORDERRESULT_STATUS_FAIL = 4;
  public static final int PAYORDERRESULT_STATUS_SUCCESS = 2;
  public static final int PAYORDERRESULT_STATUS_UNPAY = 1;
  public static final int PAYORDERRESULT_STATUS_WAITING = 12;
  public static final int QUERYORDER_REQUEST_RETRY_COUNT = 2;
  private final String TAG = "PurchaseResultAgentFragment";
  protected MApiRequest addWifiRequest;
  ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  private boolean bonusGenerated = false;
  protected View bonusView;
  protected Button btnBonusCancel;
  protected Button btnBonusShare;
  protected LinearLayout contentView;
  protected int dealSubType = 0;
  protected int dealType = 0;
  protected DPObject dpDeal;
  protected DPObject dpOrderBouns;
  protected DPObject dpPayOrderResult;
  protected String failContent;
  protected int failStatus;
  protected String failTitle;
  protected MApiRequest getOrderBonusRequest;
  protected int getPayResultCount = 0;
  protected MApiRequest getPayResultRequest;
  protected View loadingView;
  protected Handler mHandler = new PurchaseResultAgentFragment.1(this);
  protected NetworkImageView nivBonusIcon;
  protected int orderId;
  protected int payOrderResultStatus = 99;
  protected PullToRefreshScrollView pullToRefreshScrollView;
  protected LinearLayout rootView;
  protected TextView tvBonusDesc;
  protected TextView tvBonusTitle;

  private void queryPayResult()
  {
    if (this.getPayResultRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("payresultgn.bin");
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.orderId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("token", accountService().token());
    this.getPayResultRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.getPayResultRequest, this);
    if (!this.pullToRefreshScrollView.isRefreshing())
      this.loadingView.setVisibility(0);
    this.contentView.setVisibility(8);
  }

  private void queryUploadWifi()
  {
    if (this.addWifiRequest != null)
      return;
    Object localObject3 = locationService().location();
    ArrayList localArrayList = new ArrayList();
    WifiModel localWifiModel = NetworkUtils.curWifi();
    localArrayList.add("ssid");
    localArrayList.add(TextUtils.stripHeadAndTailQuotations(localWifiModel.getSsid()));
    localArrayList.add("mac");
    Object localObject2 = localWifiModel.getBssid();
    Object localObject1 = localObject2;
    if (localObject2 == null)
      localObject1 = "";
    localArrayList.add(localObject1);
    if (localObject3 != null)
    {
      localArrayList.add("lat");
      localArrayList.add(Location.FMT.format(((DPObject)localObject3).getDouble("Lat")));
      localArrayList.add("lng");
      localArrayList.add(Location.FMT.format(((DPObject)localObject3).getDouble("Lng")));
    }
    while (true)
    {
      localArrayList.add("srctype");
      localArrayList.add("4");
      localArrayList.add("shopid");
      localArrayList.add("0");
      localArrayList.add("weight");
      localArrayList.add(Integer.toString(localWifiModel.getLevel()));
      localObject1 = NetworkUtils.wifiScanResultInfo2JsonArray();
      localArrayList.add("nearwifis");
      if (((JSONArray)localObject1).length() == 0)
      {
        localObject1 = "";
        label212: localArrayList.add(localObject1);
        if (this.dpPayOrderResult == null)
          break label404;
      }
      try
      {
        localObject1 = new JSONObject();
        ((JSONObject)localObject1).put("orderID", this.orderId);
        if ((this.payOrderResultStatus == 2) && (this.dealType == 1) && (!DPObjectUtils.isResultListEmpty(this.dpPayOrderResult, "ReceiptList")))
        {
          localObject2 = this.dpPayOrderResult.getArray("ReceiptList");
          localObject3 = new JSONArray();
          int i = 0;
          while (true)
            if (i < localObject2.length)
            {
              ((JSONArray)localObject3).put(parseCouponCode(localObject2[i].getObject("Code"))[0]);
              i += 1;
              continue;
              localArrayList.add("lat");
              localArrayList.add("0");
              localArrayList.add("lng");
              localArrayList.add("0");
              break;
              localObject1 = ((JSONArray)localObject1).toString();
              break label212;
            }
          ((JSONObject)localObject1).put("couponIDs", localObject3);
        }
        localArrayList.add("extrainfo");
        localArrayList.add(((JSONObject)localObject1).toString());
        label404: this.addWifiRequest = BasicMApiRequest.mapiPost("http://mapi.dianping.com/poi/poiwifi/addwifi.bin", (String[])localArrayList.toArray(new String[0]));
        mapiService().exec(this.addWifiRequest, this);
        return;
      }
      catch (Exception localException)
      {
        while (true)
          localException.printStackTrace();
      }
    }
  }

  private void requestOrderBonus()
  {
    if (this.getOrderBonusRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("orderbonusgn.bin");
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.orderId));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("cx", DeviceUtils.cxInfo("payorder"));
    this.getOrderBonusRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.getOrderBonusRequest, this);
  }

  private void updateBonusView()
  {
    if (this.bonusView.getParent() != null)
      ((ViewGroup)(ViewGroup)this.bonusView.getParent()).removeView(this.bonusView);
    if (this.dpOrderBouns == null)
      return;
    String str1 = this.dpOrderBouns.getString("ImgUrl");
    String str2 = this.dpOrderBouns.getString("Title");
    String str3 = this.dpOrderBouns.getString("Desc");
    DPObject localDPObject = this.dpOrderBouns.getObject("Share");
    this.nivBonusIcon.setImage(str1);
    this.tvBonusTitle.setText(str2);
    this.tvBonusDesc.setText(str3);
    this.btnBonusShare.setOnClickListener(new PurchaseResultAgentFragment.3(this, localDPObject));
    this.btnBonusCancel.setOnClickListener(new PurchaseResultAgentFragment.4(this));
    ((ViewGroup)((Activity)getContext()).getWindow().getDecorView()).addView(this.bonusView);
  }

  protected void dispatchDataChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("orderid", this.orderId);
    if (this.dpPayOrderResult != null)
      localBundle.putParcelable("payorderresult", this.dpPayOrderResult);
    if (this.dpDeal != null)
      localBundle.putParcelable("deal", this.dpDeal);
    localBundle.putInt("dealtype", this.dealType);
    localBundle.putInt("dealsubtype", this.dealSubType);
    localBundle.putInt("payorderresultstatus", this.payOrderResultStatus);
    localBundle.putInt("failstatus", this.failStatus);
    localBundle.putString("failtitle", this.failTitle);
    localBundle.putString("failcontent", this.failContent);
    dispatchAgentChanged(null, localBundle);
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    if (paramAgentMessage != null)
    {
      if ("PurchaseResultRefreshPage".equals(paramAgentMessage.what))
      {
        this.getPayResultCount = 0;
        queryPayResult();
      }
    }
    else
      return;
    super.dispatchMessage(paramAgentMessage);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return this.agentListConfigs;
  }

  protected BasePurchaseResultConfig getCurrentAgentListConfig()
  {
    Iterator localIterator = this.agentListConfigs.iterator();
    while (localIterator.hasNext())
    {
      AgentListConfig localAgentListConfig = (AgentListConfig)localIterator.next();
      if (localAgentListConfig.shouldShow())
        return (BasePurchaseResultConfig)localAgentListConfig;
    }
    return null;
  }

  public DPObject getDeal()
  {
    return this.dpDeal;
  }

  public int getDealType()
  {
    return this.dealType;
  }

  public DPObject getPayOrderResult()
  {
    return this.dpPayOrderResult;
  }

  public int getPayOrderResultStatus()
  {
    return this.payOrderResultStatus;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.orderId <= 0)
    {
      getActivity().finish();
      return;
    }
    if (Integer.valueOf(this.orderId).intValue() <= 0)
    {
      getActivity().finish();
      return;
    }
    getActivity().setTitle("");
    this.bonusGenerated = false;
    queryPayResult();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 != 111) || (paramInt2 != -1) || (paramIntent == null));
    do
      return;
    while (this.bonusView.getParent() == null);
    ((ViewGroup)(ViewGroup)this.bonusView.getParent()).removeView(this.bonusView);
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.i("PurchaseResultAgentFragment", "onCreate");
    super.onCreate(paramBundle);
    this.orderId = getIntParam("orderid");
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new OrderDishPurchaseResultConfig(this));
    this.agentListConfigs.add(new ScenicPurchaseResultConfig(this));
    this.agentListConfigs.add(new HotelBookingPurchaseResultConfig(this));
    this.agentListConfigs.add(new MoviePurchaseResultConfig(this));
    this.agentListConfigs.add(new TravelPurchaseResultConfig(this));
    this.agentListConfigs.add(new DefaultPurchaseResultConfig(this));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramBundle = paramLayoutInflater.inflate(R.layout.purchaseresult_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramBundle.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramBundle.findViewById(R.id.scroll));
    this.loadingView = paramBundle.findViewById(R.id.loading);
    this.contentView = ((LinearLayout)paramBundle.findViewById(R.id.content));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.pullToRefreshScrollView.setScrollingWhileRefreshingEnabled(false);
    this.pullToRefreshScrollView.setPullToRefreshOverScrollEnabled(false);
    ILoadingLayout localILoadingLayout = this.pullToRefreshScrollView.getLoadingLayoutProxy(true, false);
    localILoadingLayout.setLoadingLayoutBackground(getResources().getDrawable(R.drawable.transparent));
    localILoadingLayout.setLoadingDrawable(getResources().getDrawable(R.drawable.dropdown_anim_00));
    localILoadingLayout.setBackgroundColor(0);
    this.pullToRefreshScrollView.setOnRefreshListener(new PurchaseResultAgentFragment.2(this));
    this.bonusView = paramLayoutInflater.inflate(R.layout.purchaseresult_orderbonus, paramViewGroup, false);
    this.nivBonusIcon = ((NetworkImageView)this.bonusView.findViewById(R.id.order_bonus_icon));
    this.tvBonusTitle = ((TextView)this.bonusView.findViewById(R.id.order_bonus_title));
    this.tvBonusDesc = ((TextView)this.bonusView.findViewById(R.id.order_bonus_desc));
    this.btnBonusShare = ((Button)this.bonusView.findViewById(R.id.order_bonus_share));
    this.btnBonusCancel = ((Button)this.bonusView.findViewById(R.id.order_bonus_cancel));
    setAgentContainerView(this.rootView);
    return paramBundle;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.getPayResultRequest != null)
    {
      mapiService().abort(this.getPayResultRequest, this, true);
      this.getPayResultRequest = null;
    }
    if (this.addWifiRequest != null)
    {
      mapiService().abort(this.addWifiRequest, this, true);
      this.addWifiRequest = null;
    }
    if (this.getOrderBonusRequest != null)
    {
      mapiService().abort(this.getOrderBonusRequest, this, true);
      this.getOrderBonusRequest = null;
    }
  }

  public boolean onGoBack()
  {
    if (this.bonusView.getParent() != null)
    {
      ((ViewGroup)(ViewGroup)this.bonusView.getParent()).removeView(this.bonusView);
      return false;
    }
    BasePurchaseResultConfig localBasePurchaseResultConfig = getCurrentAgentListConfig();
    if (localBasePurchaseResultConfig != null)
      localBasePurchaseResultConfig.onGoBack();
    return super.onGoBack();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.getPayResultRequest)
    {
      this.getPayResultRequest = null;
      if (this.getPayResultCount < 2)
      {
        this.mHandler.sendEmptyMessageDelayed(100, 3000L);
        this.getPayResultCount += 1;
      }
    }
    do
    {
      do
      {
        return;
        this.loadingView.setVisibility(8);
        this.contentView.setVisibility(0);
      }
      while (getActivity() == null);
      if ((paramMApiResponse != null) && (!TextUtils.isEmpty(paramMApiResponse.content())))
        Toast.makeText(getActivity(), paramMApiResponse.content(), 0).show();
      PurchaseResultHelper.forwardUrlSchema(getActivity(), "dianping://myorder?tab=all");
      getActivity().finish();
      return;
      if (paramMApiRequest != this.addWifiRequest)
        continue;
      this.addWifiRequest = null;
      return;
    }
    while (this.getOrderBonusRequest != paramMApiRequest);
    this.getOrderBonusRequest = null;
    Toast.makeText(getContext(), paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    int i;
    if (paramMApiRequest == this.getPayResultRequest)
    {
      this.getPayResultRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "PayOrderResult"))
      {
        this.dpPayOrderResult = ((DPObject)paramMApiResponse);
        this.dpDeal = this.dpPayOrderResult.getObject("RelativeDeal");
        if (this.dpDeal != null)
          break label176;
        i = 0;
        this.dealType = i;
        if (this.dpDeal != null)
          break label190;
        i = 0;
        label75: this.dealSubType = i;
        this.failStatus = this.dpPayOrderResult.getInt("FailStatus");
        this.failTitle = this.dpPayOrderResult.getString("FailTitle");
        this.failContent = this.dpPayOrderResult.getString("FailContent");
        this.payOrderResultStatus = this.dpPayOrderResult.getInt("Status");
        if ((this.payOrderResultStatus != 1) || (this.getPayResultCount >= 2))
          break label204;
        this.mHandler.sendEmptyMessageDelayed(100, 3000L);
        this.getPayResultCount += 1;
      }
    }
    label176: label190: label204: 
    do
    {
      do
      {
        do
        {
          return;
          i = this.dpDeal.getInt("DealType");
          break;
          i = this.dpDeal.getInt("DealSubType");
          break label75;
          if (((this.payOrderResultStatus == 1) || (this.payOrderResultStatus == 2) || (this.payOrderResultStatus == 12)) && (getActivity() != null));
          this.loadingView.setVisibility(8);
          this.contentView.setVisibility(0);
          resetAgents(null);
          dispatchDataChanged();
          this.pullToRefreshScrollView.onRefreshComplete();
          queryUploadWifi();
        }
        while ((this.payOrderResultStatus != 2) || (this.bonusGenerated));
        requestOrderBonus();
        return;
        if (paramMApiRequest != this.addWifiRequest)
          continue;
        this.addWifiRequest = null;
        return;
      }
      while (this.getOrderBonusRequest != paramMApiRequest);
      this.getOrderBonusRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "OrderBonus"));
    this.bonusGenerated = true;
    this.dpOrderBouns = ((DPObject)paramMApiResponse);
    updateBonusView();
  }

  protected String[] parseCouponCode(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (paramDPObject.getString("Name") == null))
      return new String[] { "" };
    int i = paramDPObject.getInt("Type");
    String str = paramDPObject.getString("Name");
    if (i != 3)
      return new String[] { str };
    i = str.indexOf("*");
    if (i > -1)
    {
      paramDPObject = str.substring(0, i);
      str = str.substring(i, str.length());
      if (str.length() > 1)
        return new String[] { paramDPObject, str.substring(1) };
      return new String[] { paramDPObject };
    }
    return new String[] { str };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.PurchaseResultAgentFragment
 * JD-Core Version:    0.6.0
 */