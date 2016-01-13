package com.dianping.tuan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.PayUtils;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.DPScrollView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.config.DefaultCommonPayConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class CommonPayAgentFragement extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  public static final int GETMULTIPREPAYORDERINFO_REQUEST_RETRY_COUNT = 3;
  public static final int MSG_RETRY_GETMULTIPREPAYORDERINFO = 1;
  protected LinearLayout bottomView;
  protected String callBackFailUrl;
  protected String callBackUrl;
  protected DPObject dpPrepayOrderInfo;
  protected MApiRequest getMultiPrepayOrderInfoRequest;
  protected int getMultiPrepayOrderInfoRequestCount = 0;
  protected Handler mHandler = new CommonPayAgentFragement.1(this);
  protected int mainProductCode;
  protected int needReservePreviousPages;
  protected String orderId;
  protected String[] orderIdList;
  protected String orderInfoTitle;
  protected String pageTitle;
  protected String productCode;
  protected String[] productCodeList;
  protected String productInfo;
  protected LinearLayout rootView;
  protected DPScrollView scrollView;
  protected int submitBtnHeight;
  protected String token;

  private void requestGetMultiPrepayOrderInfo(boolean paramBoolean)
  {
    if (this.getMultiPrepayOrderInfoRequest != null);
    do
    {
      String str;
      do
      {
        return;
        str = PayUtils.generatePrepayInfos(this.orderIdList, this.productCodeList);
      }
      while (str == null);
      UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/");
      localUrlBuilder.appendPath("getmultiprepayorderinfo.pay");
      localUrlBuilder.addParam("token", this.token);
      localUrlBuilder.addParam("prepayorders", str);
      localUrlBuilder.addParam("mainproductcode", Integer.valueOf(this.mainProductCode));
      this.getMultiPrepayOrderInfoRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
      mapiService().exec(this.getMultiPrepayOrderInfoRequest, this);
    }
    while (!paramBoolean);
    showProgressDialog("正在获取订单信息...");
  }

  protected void afterConfirmOrder()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    if (TextUtils.isEmpty(this.callBackUrl))
    {
      localIntent.setData(Uri.parse("dianping://purchaseresult"));
      if ((this.orderIdList != null) && (this.orderIdList.length == 1))
      {
        localIntent.putExtra("orderid", Integer.parseInt(this.orderIdList[0]));
        Bundle localBundle = getActivity().getIntent().getBundleExtra("payextra");
        if (localBundle != null)
          localIntent.putExtra("payextra", localBundle);
      }
    }
    while (true)
    {
      if (this.needReservePreviousPages == 0)
        localIntent.addFlags(67108864);
      startActivity(localIntent);
      return;
      localIntent.setData(Uri.parse(this.callBackUrl));
    }
  }

  protected void dispatchCashierInitMessage()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("orderId", this.orderId);
    localBundle.putString("productCode", this.productCode);
    localBundle.putString("token", this.token);
    localBundle.putBoolean("forceShowPaymentTool", false);
    localBundle.putInt("mainProductCode", this.mainProductCode);
    localBundle.putString("callBackUrl", this.callBackUrl);
    localBundle.putString("callBackFailUrl", this.callBackFailUrl);
    localBundle.putInt("needReservePreviousPages", this.needReservePreviousPages);
    AgentMessage localAgentMessage = new AgentMessage("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayCashierDeskInit");
    localAgentMessage.body = localBundle;
    dispatchMessage(localAgentMessage);
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    if ("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayRefreshOrder".equals(paramAgentMessage.what))
    {
      this.getMultiPrepayOrderInfoRequestCount = 0;
      requestGetMultiPrepayOrderInfo(true);
      return;
    }
    if ("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayResult".equals(paramAgentMessage.what))
    {
      if (paramAgentMessage.body.getBoolean("success"))
      {
        afterConfirmOrder();
        return;
      }
      showAlertDialog(paramAgentMessage.body.getString("errorMsg"));
      return;
    }
    super.dispatchMessage(paramAgentMessage);
  }

  protected void dispatchOrderInfoMessage()
  {
    AgentMessage localAgentMessage = new AgentMessage("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayOrderInfoInit");
    localAgentMessage.body.putString("orderInfoTitle", this.orderInfoTitle);
    localAgentMessage.body.putString("productInfo", this.productInfo);
    localAgentMessage.body.putParcelable("dpPrepayOrderInfo", this.dpPrepayOrderInfo);
    dispatchMessage(localAgentMessage);
    localAgentMessage = new AgentMessage("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayAmountChanged");
    if (this.dpPrepayOrderInfo != null)
    {
      double d2 = this.dpPrepayOrderInfo.getDouble("Amount") - this.dpPrepayOrderInfo.getDouble("DiscountAmount");
      double d1 = d2;
      if (d2 < 0.0D)
        d1 = 0.0D;
      localAgentMessage.body.putDouble("needPayAmount", d1);
    }
    dispatchMessage(localAgentMessage);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DefaultCommonPayConfig());
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    requestGetMultiPrepayOrderInfo(true);
    dispatchCashierInitMessage();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      this.orderId = getStringParam("orderid");
      this.productCode = getStringParam("productcode");
      this.productInfo = getStringParam("productinfo");
      this.mainProductCode = getIntParam("mainproductcode");
      this.pageTitle = getStringParam("pagetitle");
      this.orderInfoTitle = getStringParam("orderinfotitle");
      this.token = getStringParam("token");
      this.callBackUrl = getStringParam("callbackurl");
      this.callBackFailUrl = getStringParam("callbackfailurl");
      this.needReservePreviousPages = getIntParam("needreservepreviouspages");
      if ((TextUtils.isEmpty(this.orderId)) || (TextUtils.isEmpty(this.productCode)))
        getActivity().finish();
      do
      {
        return;
        this.orderIdList = this.orderId.split(",");
        this.productCodeList = this.productCode.split(",");
        if ((this.orderIdList == null) || (this.orderIdList.length == 0) || (this.productCodeList == null) || (this.productCodeList.length == 0) || (this.orderIdList.length != this.productCodeList.length))
        {
          getActivity().finish();
          return;
        }
        if (this.mainProductCode != 0)
          continue;
        getActivity().finish();
        return;
      }
      while (!TextUtils.isEmpty(this.token));
      if (isLogined())
      {
        this.token = accountService().token();
        return;
      }
      getActivity().finish();
      return;
    }
    this.productInfo = paramBundle.getString("productInfo");
    this.mainProductCode = paramBundle.getInt("mainProductCode");
    this.pageTitle = paramBundle.getString("pageTitle");
    this.orderInfoTitle = paramBundle.getString("orderInfoTitle");
    this.token = paramBundle.getString("token");
    this.orderId = paramBundle.getString("orderId");
    this.productCode = paramBundle.getString("productCode");
    this.orderIdList = paramBundle.getStringArray("orderIdList");
    this.productCodeList = paramBundle.getStringArray("productCodeList");
    this.callBackUrl = paramBundle.getString("callBackUrl");
    this.callBackFailUrl = paramBundle.getString("callBackFailUrl");
    this.needReservePreviousPages = paramBundle.getInt("needReservePreviousPages");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (TextUtils.isEmpty(this.pageTitle))
      getTitleBar().setTitle("支付订单");
    while (true)
    {
      paramLayoutInflater = paramLayoutInflater.inflate(R.layout.createorder_agent_container, paramViewGroup, false);
      this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
      this.rootView.setBackgroundResource(R.drawable.main_background);
      this.scrollView = ((DPScrollView)paramLayoutInflater.findViewById(R.id.scrollview));
      this.bottomView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_view));
      this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new CommonPayAgentFragement.2(this));
      setAgentContainerView(this.rootView);
      return paramLayoutInflater;
      getTitleBar().setTitle(this.pageTitle);
    }
  }

  public void onDestroy()
  {
    this.mHandler.removeMessages(1);
    super.onDestroy();
  }

  protected void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.getMultiPrepayOrderInfoRequest != null)
    {
      this.mHandler.removeMessages(1);
      mapiService().abort(this.getMultiPrepayOrderInfoRequest, this, true);
      this.getMultiPrepayOrderInfoRequest = null;
      getActivity().finish();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.getMultiPrepayOrderInfoRequest)
    {
      this.getMultiPrepayOrderInfoRequest = null;
      if (this.getMultiPrepayOrderInfoRequestCount < 3)
      {
        this.mHandler.sendEmptyMessageDelayed(1, 3000L);
        this.getMultiPrepayOrderInfoRequestCount += 1;
      }
    }
    else
    {
      return;
    }
    dismissDialog();
    Toast.makeText(getActivity(), paramMApiResponse.content(), 0).show();
    getActivity().finish();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest == this.getMultiPrepayOrderInfoRequest)
    {
      dismissDialog();
      this.getMultiPrepayOrderInfoRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "PrepayOrderInfo"))
      {
        this.dpPrepayOrderInfo = paramMApiResponse;
        if (!TextUtils.isEmpty(this.dpPrepayOrderInfo.getString("ProductInfo")))
          this.productInfo = this.dpPrepayOrderInfo.getString("ProductInfo");
        dispatchOrderInfoMessage();
      }
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("productInfo", this.productInfo);
    paramBundle.putInt("mainProductCode", this.mainProductCode);
    paramBundle.putString("pageTitle", this.pageTitle);
    paramBundle.putString("orderInfoTitle", this.orderInfoTitle);
    paramBundle.putString("token", this.token);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putString("productCode", this.productCode);
    paramBundle.putStringArray("orderIdList", this.orderIdList);
    paramBundle.putStringArray("productCodeList", this.productCodeList);
    paramBundle.putString("callBackUrl", this.callBackUrl);
    paramBundle.putString("callBackFailUrl", this.callBackFailUrl);
    paramBundle.putInt("needReservePreviousPages", this.needReservePreviousPages);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.bottomView.removeAllViews();
    this.bottomView.addView(paramView);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.CommonPayAgentFragement
 * JD-Core Version:    0.6.0
 */