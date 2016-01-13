package com.dianping.movie.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.DPScrollView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.config.MoviePayConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class MoviePayAgentFragement extends AgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AgentFragment.CellStable
{
  public static final int GETORDER_REQUEST_RETRY_COUNT = 3;
  public static final int MSG_RETRY_GET_ORDER = 1;
  protected LinearLayout bottomView;
  protected String callBackFailUrl;
  protected String callBackUrl;
  protected DPObject dpMovieTicketOrder;
  protected DPObject dpPayProduct;
  protected String eventChanel;
  protected int getOrderRequestCount = 0;
  protected boolean isCashierInitialized;
  protected Handler mHandler = new MoviePayAgentFragement.1(this);
  protected int mainProductCode;
  protected int movieDiscountId;
  protected MApiRequest movieTicketOrderDetailRequest;
  protected String orderId;
  protected int originMovieDiscountId = 0;
  protected String productCode;
  protected LinearLayout rootView;
  protected DPScrollView scrollView;
  protected String token;

  private void requestMovieTicketOrder(boolean paramBoolean)
  {
    if (this.movieTicketOrderDetailRequest != null);
    do
    {
      return;
      Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/movieticketorderdetailmv.bin?").buildUpon();
      localBuilder.appendQueryParameter("token", accountService().token());
      localBuilder.appendQueryParameter("orderid", String.valueOf(this.orderId));
      this.movieTicketOrderDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.movieTicketOrderDetailRequest, this);
    }
    while (!paramBoolean);
    showProgressDialog("正在获取订单信息...");
  }

  private void showOrderAlertMsg(String paramString)
  {
    new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage(paramString).setPositiveButton("确定", new MoviePayAgentFragement.2(this)).create().show();
  }

  protected void afterConfirmOrder()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse(this.callBackUrl));
    localIntent.putExtra("orderid", this.orderId);
    Bundle localBundle = new Bundle();
    int i = 0;
    if (this.dpMovieTicketOrder != null)
      i = this.dpMovieTicketOrder.getInt("ShopID");
    localBundle.putInt("shopid", i);
    localBundle.putString("orderid", this.orderId);
    localIntent.putExtra("payextra", localBundle);
    localIntent.addFlags(67108864);
    startActivity(localIntent);
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    if ("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayRefreshOrder".equals(paramAgentMessage.what))
    {
      this.getOrderRequestCount = 0;
      requestMovieTicketOrder(true);
      dispatchOrderId();
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

  protected void dispatchMovieTicketOrder()
  {
    try
    {
      Object localObject = this.dpMovieTicketOrder;
      if (localObject == null)
        d = 0.0D;
      while (true)
      {
        localObject = new Bundle();
        ((Bundle)localObject).putString("token", this.token);
        ((Bundle)localObject).putDouble("needPayAmount", d);
        ((Bundle)localObject).putBoolean("forceShowPaymentTool", false);
        ((Bundle)localObject).putBoolean("forceShowNeedPay", true);
        ((Bundle)localObject).putBoolean("refreshCashierDesk", false);
        ((Bundle)localObject).putString("callBackFailUrl", this.callBackFailUrl);
        ((Bundle)localObject).putInt("mainProductCode", this.mainProductCode);
        ((Bundle)localObject).putString("orderid", this.orderId);
        ((Bundle)localObject).putString("productCode", this.productCode);
        ((Bundle)localObject).putParcelable("dpPayProduct", this.dpPayProduct);
        ((Bundle)localObject).putParcelable("dpMovieTicketOrder", this.dpMovieTicketOrder);
        ((Bundle)localObject).putInt("originMovieDiscountId", this.originMovieDiscountId);
        dispatchAgentChanged(null, (Bundle)localObject);
        return;
        d = Double.parseDouble(this.dpMovieTicketOrder.getString("TotalAmount"));
      }
    }
    catch (Exception localException)
    {
      while (true)
        double d = 0.0D;
    }
  }

  protected void dispatchOrderId()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("orderId", this.orderId);
    localBundle.putString("productCode", this.productCode);
    localBundle.putString("token", this.token);
    localBundle.putBoolean("forceShowNeedPay", true);
    localBundle.putBoolean("forceShowPaymentTool", false);
    localBundle.putBoolean("refreshCashierDesk", true);
    localBundle.putString("callBackFailUrl", this.callBackFailUrl);
    localBundle.putInt("mainProductCode", this.mainProductCode);
    localBundle.putInt("originMovieDiscountId", this.originMovieDiscountId);
    dispatchAgentChanged(null, localBundle);
  }

  protected void dispatchPayCashier()
  {
    try
    {
      Object localObject = this.dpMovieTicketOrder;
      if (localObject == null)
        d = 0.0D;
      while (true)
      {
        localObject = new AgentMessage("com.dianping.base.tuan.agent.DPCashierDeskAgent.PayCashierDeskInit");
        ((AgentMessage)localObject).body.putString("token", this.token);
        ((AgentMessage)localObject).body.putDouble("needPayAmount", d);
        ((AgentMessage)localObject).body.putBoolean("forceShowPaymentTool", false);
        ((AgentMessage)localObject).body.putBoolean("forceShowNeedPay", true);
        ((AgentMessage)localObject).body.putBoolean("refreshCashierDesk", false);
        ((AgentMessage)localObject).body.putInt("mainProductCode", this.mainProductCode);
        ((AgentMessage)localObject).body.putString("orderId", this.orderId);
        ((AgentMessage)localObject).body.putString("productCode", this.productCode);
        dispatchMessage((AgentMessage)localObject);
        this.isCashierInitialized = true;
        return;
        d = Double.parseDouble(this.dpMovieTicketOrder.getString("TotalAmount"));
      }
    }
    catch (Exception localException)
    {
      while (true)
        double d = 0.0D;
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new MoviePayConfig());
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dpMovieTicketOrder == null)
    {
      requestMovieTicketOrder(true);
      dispatchOrderId();
    }
    do
    {
      return;
      dispatchMovieTicketOrder();
    }
    while (this.isCashierInitialized);
    dispatchPayCashier();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      if (TextUtils.isEmpty(accountService().token()))
        getActivity().finish();
      do
      {
        return;
        this.token = accountService().token();
        this.orderId = getStringParam("orderid");
        this.dpPayProduct = ((DPObject)getActivity().getIntent().getParcelableExtra("payproduct"));
        if (this.dpPayProduct == null)
        {
          int j = getIntParam("payproducttype");
          int i = j;
          if (j == 0)
            i = 3;
          paramBundle = getStringParam("title");
          this.dpPayProduct = new DPObject("PayProduct").edit().putString("Title", paramBundle).putInt("ProductType", i).generate();
        }
        if ((TextUtils.isEmpty(this.orderId)) || (this.dpPayProduct == null))
        {
          getActivity().finish();
          showToast("缺少必要参数!");
          return;
        }
        this.productCode = getStringParam("productcode");
        if (TextUtils.isEmpty(this.productCode))
          this.productCode = "3";
        try
        {
          this.mainProductCode = Integer.valueOf(this.productCode).intValue();
          this.callBackUrl = getStringParam("callbackurl");
          if (TextUtils.isEmpty(this.callBackUrl))
            this.callBackUrl = "dianping://purchasemovieticketresult";
          this.callBackFailUrl = getStringParam("callbackfailurl");
          if (TextUtils.isEmpty(this.callBackFailUrl))
            this.callBackFailUrl = "dianping://home";
          this.eventChanel = getStringParam("eventchannel");
          this.dpMovieTicketOrder = ((DPObject)getActivity().getIntent().getParcelableExtra("movieticketorder"));
          this.originMovieDiscountId = getIntParam("moviediscountid", 0);
          this.movieDiscountId = this.originMovieDiscountId;
          if (this.dpMovieTicketOrder != null)
            continue;
          requestMovieTicketOrder(true);
          return;
        }
        catch (Exception paramBundle)
        {
          while (true)
            this.mainProductCode = 3;
        }
      }
      while ((TextUtils.isEmpty(this.dpMovieTicketOrder.getString("ErrorMsg"))) || (this.dpMovieTicketOrder.getInt("RemainSecond") <= 0));
      showOrderAlertMsg(this.dpMovieTicketOrder.getString("ErrorMsg"));
      return;
    }
    this.callBackUrl = paramBundle.getString("callBackUrl");
    this.callBackFailUrl = paramBundle.getString("callBackFailUrl");
    this.token = paramBundle.getString("token");
    this.eventChanel = paramBundle.getString("eventChanel");
    this.dpPayProduct = ((DPObject)paramBundle.getParcelable("dpPayProduct"));
    this.dpMovieTicketOrder = ((DPObject)paramBundle.getParcelable("dpMovieTicketOrder"));
    this.orderId = paramBundle.getString("orderId");
    this.productCode = paramBundle.getString("productCode");
    this.mainProductCode = paramBundle.getInt("mainProductCode");
    this.originMovieDiscountId = paramBundle.getInt("originMovieDiscountId");
    this.movieDiscountId = paramBundle.getInt("movieDiscountId");
    this.isCashierInitialized = paramBundle.getBoolean("isCashierInitialized");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    getTitleBar().setTitle("支付订单");
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.moviepayorder_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.scrollView = ((DPScrollView)paramLayoutInflater.findViewById(R.id.scrollview));
    this.bottomView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_view));
    setAgentContainerView(this.rootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    this.mHandler.removeMessages(1);
    super.onDestroy();
  }

  protected void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.movieTicketOrderDetailRequest != null)
    {
      this.mHandler.removeMessages(1);
      mapiService().abort(this.movieTicketOrderDetailRequest, this, true);
      this.movieTicketOrderDetailRequest = null;
      getActivity().finish();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.movieTicketOrderDetailRequest)
    {
      this.movieTicketOrderDetailRequest = null;
      if (this.getOrderRequestCount < 3)
      {
        this.mHandler.sendEmptyMessageDelayed(1, 3000L);
        this.getOrderRequestCount += 1;
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
    if (paramMApiRequest == this.movieTicketOrderDetailRequest)
    {
      dismissDialog();
      this.movieTicketOrderDetailRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieTicketOrder"))
      {
        this.dpMovieTicketOrder = paramMApiResponse;
        if ((!TextUtils.isEmpty(this.dpMovieTicketOrder.getString("ErrorMsg"))) && (this.dpMovieTicketOrder.getInt("RemainSecond") > 0))
          showOrderAlertMsg(this.dpMovieTicketOrder.getString("ErrorMsg"));
        dispatchMovieTicketOrder();
        dispatchPayCashier();
      }
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("callBackUrl", this.callBackUrl);
    paramBundle.putString("callBackFailUrl", this.callBackFailUrl);
    paramBundle.putString("token", this.token);
    paramBundle.putString("eventChanel", this.eventChanel);
    paramBundle.putParcelable("dpPayProduct", this.dpPayProduct);
    paramBundle.putParcelable("dpMovieTicketOrder", this.dpMovieTicketOrder);
    paramBundle.putString("productCode", this.productCode);
    paramBundle.putInt("mainProductCode", this.mainProductCode);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putInt("originMovieDiscountId", this.originMovieDiscountId);
    paramBundle.putInt("movieDiscountId", this.movieDiscountId);
    paramBundle.putBoolean("isCashierInitialized", this.isCashierInitialized);
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
 * Qualified Name:     com.dianping.movie.fragment.MoviePayAgentFragement
 * JD-Core Version:    0.6.0
 */