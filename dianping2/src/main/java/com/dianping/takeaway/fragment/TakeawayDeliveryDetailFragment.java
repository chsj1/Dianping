package com.dianping.takeaway.fragment;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.agent.DPPromoDeskAgent;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.agents.TakeawayDeliveryAddressAgent;
import com.dianping.takeaway.agents.TakeawayDeliveryExtraFeeAgent;
import com.dianping.takeaway.agents.TakeawayDeliveryExtraInfoAgent;
import com.dianping.takeaway.agents.TakeawayDeliveryPayTypeAgent;
import com.dianping.takeaway.agents.TakeawayDeliveryPromoAgent;
import com.dianping.takeaway.agents.TakeawayDeliverySelectedDishAgent;
import com.dianping.takeaway.agents.TakeawayDeliveryThirdpartyPromoDeskAgent;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.DataLoadListener;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.LoadCause;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.util.TakeawayCarCacheManager;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.takeaway.view.OnDialogOperationListener;
import com.dianping.takeaway.view.TAAlertDialog;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TakeawayDeliveryDetailFragment extends AgentFragment
  implements TakeawayDeliveryDataSource.DataLoadListener
{
  public final int REQUEST_CODE_CHANGEADDRESS = 1;
  private final int REQUEST_CODE_DELIVERYDETAIL_ELEME_COUPON = 8;
  private final int REQUEST_CODE_DELIVERYDETAIL_PHONE_VERIFY = 7;
  private final int RESULT_CODE_DELIVERYDETAIL_CLOSEMENU = 6;
  private final int RESULT_CODE_DELIVERYDETAIL_ELEME_COUPON_COMMON = 81;
  private final int RESULT_CODE_DELIVERYDETAIL_ELEME_COUPON_PHONE_CHANGE = 82;
  private final int RESULT_CODE_DELIVERYDETAIL_MENUCHANGED = 5;
  private final int RESULT_CODE_DELIVERYDETAIL_MENUREFRESH = 9;
  private final int RESULT_CODE_DELIVERYDETAIL_PHONE_VERIFY_COMMON = 71;
  private final int RESULT_CODE_DELIVERYDETAIL_PHONE_VERIFY_ELEME = 72;
  private LinearLayout containerView;
  private TakeawayDeliveryDataSource dataSource;
  private ProgressDialog loadingDlg;
  private View loadingView;
  private RMBLabelItem priceView;
  private ScrollView scrollView;
  private NovaButton submitBtn;

  private Bundle getDpPromoMsgBody()
  {
    Bundle localBundle = new Bundle();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DPObject("PromoProduct").edit().putInt("ProductCode", this.dataSource.productCode).putInt("ProductId", Integer.parseInt(this.dataSource.shopID)).putDouble("Price", this.dataSource.midPrice.doubleValue()).putInt("Quantity", 1).putBoolean("Selected", true).putDouble("NoDiscountAmount", 0.0D).generate());
    localBundle.putParcelableArrayList("promoProductList", localArrayList);
    localBundle.putInt("shopId", Integer.parseInt(this.dataSource.shopID));
    localBundle.putString("mobileNo", this.dataSource.getPhone());
    localBundle.putString("gaProdcutCode", String.valueOf(this.dataSource.productCode));
    if (TextUtils.isEmpty(accountService().token()))
    {
      localArrayList = new ArrayList();
      localArrayList.add(Integer.valueOf(2));
      localBundle.putIntegerArrayList("businessDisplay", localArrayList);
    }
    if (!this.dataSource.canUseCoupon)
    {
      localArrayList = new ArrayList();
      localArrayList.add(Integer.valueOf(2));
      localArrayList.add(Integer.valueOf(3));
      localArrayList.add(Integer.valueOf(7));
      localBundle.putIntegerArrayList("businessRestrictions", localArrayList);
    }
    return localBundle;
  }

  private void gotoActivityElemePhoneVerify(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayphoneverify"));
    localIntent.putExtra("phone", paramString);
    localIntent.putExtra("shopid", this.dataSource.shopID);
    localIntent.putExtra("type", "eleme");
    startActivityForResult(localIntent, 7);
  }

  private void handleMsgFromDpPromo(AgentMessage paramAgentMessage)
  {
    String str;
    int i;
    if (paramAgentMessage != null)
    {
      str = paramAgentMessage.what;
      i = -1;
      switch (str.hashCode())
      {
      default:
      case 158294100:
      case -168430325:
      case -979264211:
      }
    }
    while (true)
      switch (i)
      {
      default:
        return;
        if (!str.equals("com.dianping.base.tuan.agent.DPPromoDeskAgent.DPPromoInitFailed"))
          continue;
        i = 0;
        continue;
        if (!str.equals("com.dianping.base.tuan.agent.DPPromoDeskAgent.DPPromoInitSucceed"))
          continue;
        i = 1;
        continue;
        if (!str.equals("com.dianping.base.tuan.agent.DPPromoDeskAgent.DPPromoChanged"))
          continue;
        i = 2;
      case 0:
      case 1:
      case 2:
      }
    this.dataSource.dpDiscountPrice = BigDecimal.ZERO;
    this.dataSource.dpDiscountStr = "";
    updatePriceView();
    if (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.FIR_LOAD)
      this.loadingView.setVisibility(8);
    while (true)
    {
      this.scrollView.scrollTo(0, 0);
      return;
      this.loadingDlg.dismiss();
    }
    this.dataSource.dpDiscountPrice = BigDecimal.valueOf(paramAgentMessage.body.getDouble("dpDiscountPrice"));
    this.dataSource.dpDiscountStr = paramAgentMessage.body.getString("dpDiscountStr");
    updatePriceView();
  }

  private void showErrorDialog(String paramString, int paramInt)
  {
    new AlertDialog.Builder(getActivity()).setMessage(paramString).setCancelable(false).setPositiveButton("知道了", new DialogInterface.OnClickListener(paramInt)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
        TakeawayDeliveryDetailFragment.this.getActivity().setResult(this.val$resultCode);
        TakeawayDeliveryDetailFragment.this.getActivity().finish();
      }
    }).show();
  }

  private void showErrorToast(String paramString)
  {
    showToast(paramString);
    updatePromoInfo();
  }

  private void showLoadingDialog(String paramString)
  {
    this.loadingDlg.setMessage(paramString);
    this.loadingDlg.show();
  }

  private void showWarningDialog(String paramString, int paramInt)
  {
    new AlertDialog.Builder(getActivity()).setMessage(paramString).setCancelable(false).setPositiveButton("知道了", new DialogInterface.OnClickListener(paramInt)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
        TakeawayDeliveryDetailFragment.this.getActivity().setResult(this.val$resultCode);
      }
    }).show();
  }

  private void updatePriceView()
  {
    this.dataSource.finalPrice = this.dataSource.midPrice.subtract(this.dataSource.dpDiscountPrice);
    if (this.dataSource.finalPrice.compareTo(BigDecimal.ZERO) <= 0)
      this.dataSource.finalPrice = BigDecimal.ZERO;
    this.priceView.setRMBLabelStyle(4, 2, false, getResources().getColor(R.color.light_red));
    this.priceView.setRMBLabelValue(this.dataSource.finalPrice.doubleValue());
  }

  private void updatePromoInfo()
  {
    this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.LOG_IN_SUCCESS;
    this.dataSource.confirmOrderTask();
  }

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    super.dispatchMessage(paramAgentMessage);
    handleMsgFromDpPromo(paramAgentMessage);
  }

  public void elemeCouponOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    dismissDialog();
    switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      showLoadingDialog("载入中...");
      return;
    case 2:
      this.loadingDlg.dismiss();
      switch (this.dataSource.needToStartActivity)
      {
      default:
        return;
      case 7:
        gotoActivityElemePhoneVerify(TakeawayPreferencesManager.getTakeawayDeliveryPreferences(getActivity()).getString("eleme_phone", ""));
        return;
      case 8:
      }
      paramTakeawayNetLoadStatus = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayelemecoupon"));
      paramTakeawayNetLoadStatus.putExtra("elemecouponlist", this.dataSource.couponInfo);
      paramTakeawayNetLoadStatus.putExtra("canusethirdpartycode", this.dataSource.canUseThirdpartyCode);
      paramTakeawayNetLoadStatus.putExtra("cartid", this.dataSource.cartId);
      paramTakeawayNetLoadStatus.putExtra("thirdpartycouponstr", this.dataSource.thirdpartyCouponStr);
      paramTakeawayNetLoadStatus.putExtra("thirdpartycoupontype", this.dataSource.thirdpartyCouponType);
      startActivityForResult(paramTakeawayNetLoadStatus, 8);
      return;
    case 3:
    }
    this.loadingDlg.dismiss();
    if ((paramObject instanceof SimpleMsg))
    {
      paramObject = ((SimpleMsg)paramObject).content();
      paramTakeawayNetLoadStatus = paramObject;
      if (TextUtils.isEmpty(paramObject))
        paramTakeawayNetLoadStatus = "网络错误，请重试";
      showToast(paramTakeawayNetLoadStatus);
      return;
    }
    showToast("网络错误，请重试");
  }

  protected boolean enableSubmitOrder()
  {
    if (this.dataSource.orderAddress == null)
    {
      this.scrollView.scrollTo(0, 0);
      super.showToast("请输入送餐信息");
      return false;
    }
    if ((this.dataSource.selectedInvoiceType == 2) && (TextUtils.isEmpty(this.dataSource.inputInvoiceHeader)))
    {
      this.scrollView.scrollTo(0, 0);
      super.showToast("开公司发票请填写抬头");
      return false;
    }
    return true;
  }

  public void forceHideKeyboard()
  {
    if (getActivity().getCurrentFocus() != null)
      ((InputMethodManager)getActivity().getSystemService("input_method")).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("takeawayorder/address", TakeawayDeliveryAddressAgent.class);
        localHashMap.put("takeawayorder/paytype", TakeawayDeliveryPayTypeAgent.class);
        localHashMap.put("takeawayorder/promo", TakeawayDeliveryPromoAgent.class);
        localHashMap.put("takeawayorder/dppromo", DPPromoDeskAgent.class);
        localHashMap.put("takeawayorder/thirdpartypromo", TakeawayDeliveryThirdpartyPromoDeskAgent.class);
        localHashMap.put("takeawayorder/extrafee", TakeawayDeliveryExtraFeeAgent.class);
        localHashMap.put("takeawayorder/extrainfo", TakeawayDeliveryExtraInfoAgent.class);
        localHashMap.put("takeawayorder/dish", TakeawayDeliverySelectedDishAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public TakeawayDeliveryDataSource getDataSource()
  {
    return this.dataSource;
  }

  public void loadOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    dismissDialog();
    switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      if (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.FIR_LOAD)
      {
        this.loadingView.setVisibility(0);
        return;
      }
      showLoadingDialog("载入中...");
      return;
    case 2:
      if (!TextUtils.isEmpty(this.dataSource.toastMsg))
        showWarningDialog(this.dataSource.toastMsg, 9);
      paramTakeawayNetLoadStatus = new AgentMessage("com.dianping.base.tuan.agent.DPPromoDeskAgent.ShopPromoInitSucceed");
      paramTakeawayNetLoadStatus.body = getDpPromoMsgBody();
      dispatchMessage(paramTakeawayNetLoadStatus);
      dispatchMessage(new AgentMessage("DELIVERY_LOAD_ORDER_SUCCESS"));
      return;
    case 3:
    }
    if (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.FIR_LOAD)
      this.loadingView.setVisibility(8);
    while ((paramObject instanceof SimpleMsg))
    {
      paramObject = (SimpleMsg)paramObject;
      paramTakeawayNetLoadStatus = paramObject.content();
      switch (paramObject.flag())
      {
      default:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "网络错误，请重试";
        showToast(paramObject);
        return;
        this.loadingDlg.dismiss();
        break;
      case 1:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "商户不营业";
        showErrorDialog(paramObject, 6);
        return;
      case 2:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "菜品信息发生变化，请重新确认";
        showErrorDialog(paramObject, 5);
        return;
      }
    }
    showToast("网络错误，请重试");
    getActivity().finish();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    super.getActivity().getWindow().setBackgroundDrawable(null);
    this.dataSource.fetchParams(paramBundle);
    this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.FIR_LOAD;
    this.dataSource.confirmOrderTask();
    paramBundle = new Bundle();
    paramBundle.putInt("promodeskheadstyle", 0);
    paramBundle.putInt("promodeskotherpromoheadstyle", 1);
    paramBundle.putInt("promodeskfootstyle", 1);
    paramBundle.putInt("promodeskforceautopromo", 1);
    paramBundle.putInt("promodeskloadingstyle", 1);
    dispatchAgentChanged("takeawayorder/dppromo", paramBundle);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1)
      if ((paramInt2 == -1) && (paramIntent != null))
      {
        this.dataSource.orderAddress = ((DPObject)paramIntent.getParcelableExtra("address"));
        this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.ADDRESS_CHANGED;
        this.dataSource.confirmOrderTask();
      }
    label55: 
    do
    {
      do
        while (true)
        {
          break label55;
          do
            return;
          while ((paramInt2 != 0) || ((paramIntent != null) && (paramIntent.getBooleanExtra("isfrommodify", false))));
          this.dataSource.orderAddress = null;
          paramIntent = new AgentMessage("DELIVERY_ADDRESS_DELETED");
          paramIntent.target = super.findAgent("takeawayorder/address");
          dispatchMessage(paramIntent);
          return;
          if (paramInt1 != 7)
            break;
          if (paramInt2 == 71)
          {
            this.dataSource.phoneVerified = true;
            this.dataSource.submitOrderTask();
            return;
          }
          if (paramInt2 != 72)
            continue;
          this.dataSource.confirmCouponTask();
          return;
        }
      while (paramInt1 != 8);
      if (paramInt2 != 81)
        continue;
      this.dataSource.thirdpartyCouponStr = paramIntent.getStringExtra("thirdpartycouponstr");
      this.dataSource.thirdpartyCouponType = paramIntent.getIntExtra("thirdpartycoupontype", 0);
      this.dataSource.confirmOrderTask();
      return;
    }
    while (paramInt2 != 82);
    gotoActivityElemePhoneVerify("");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setHost("takeawayorder");
    this.dataSource = new TakeawayDeliveryDataSource((NovaActivity)getActivity());
    this.dataSource.setDataLoadListener(this);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.takeaway_delivery_detail, paramViewGroup, false);
    this.scrollView = ((ScrollView)paramLayoutInflater.findViewById(R.id.scroll_view));
    this.scrollView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          TakeawayDeliveryDetailFragment.this.forceHideKeyboard();
        return false;
      }
    });
    this.containerView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.container_view));
    super.setAgentContainerView(this.containerView);
    this.submitBtn = ((NovaButton)paramLayoutInflater.findViewById(R.id.submit_btn));
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TakeawayDeliveryDetailFragment.this.enableSubmitOrder())
        {
          paramView = TakeawayDeliveryDetailFragment.this.dataSource.getGAUserInfo();
          GAHelper.instance().contextStatisticsEvent(TakeawayDeliveryDetailFragment.this.getActivity(), "submitorder", paramView, "tap");
          TakeawayDeliveryDetailFragment.this.dataSource.submitOrderTask();
          TakeawayCarCacheManager.clearCacheMenu(TakeawayDeliveryDetailFragment.this.getActivity());
        }
      }
    });
    this.priceView = ((RMBLabelItem)paramLayoutInflater.findViewById(R.id.price));
    this.loadingView = paramLayoutInflater.findViewById(R.id.loading_view);
    this.loadingView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    this.loadingDlg = new ProgressDialog(getActivity());
    this.loadingDlg.setCancelable(false);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.abortRequests();
  }

  public void onLogin(boolean paramBoolean)
  {
    super.onLogin(paramBoolean);
    updatePromoInfo();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  public void setRemarkViewTouchEvent()
  {
    if (this.scrollView != null)
      this.scrollView.requestDisallowInterceptTouchEvent(true);
  }

  public void submitOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
    case 1:
    case 2:
      while (true)
      {
        return;
        showLoadingDialog("正在处理，请稍候...");
        return;
        this.loadingDlg.dismiss();
        paramTakeawayNetLoadStatus = (DPObject)paramObject;
        if (paramTakeawayNetLoadStatus.getInt("NeedVerify") == 1)
        {
          paramTakeawayNetLoadStatus = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayphoneverify"));
          paramTakeawayNetLoadStatus.putExtra("phone", this.dataSource.getPhone());
          startActivityForResult(paramTakeawayNetLoadStatus, 7);
          return;
        }
        int i = this.dataSource.payType;
        this.dataSource.getClass();
        if (i != 1)
          break;
        paramObject = paramTakeawayNetLoadStatus.getObject("PayOrderResult");
        if (paramObject == null)
          continue;
        i = paramObject.getInt("PayOrderId");
        paramObject = paramObject.getObject("PayProduct");
        int j = paramObject.getInt("ProductType");
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://minipayorder"));
        localIntent.putExtra("orderid", String.valueOf(i));
        localIntent.putExtra("productcode", String.valueOf(j));
        localIntent.putExtra("mainproductcode", j);
        localIntent.putExtra("orderinfotitle", paramObject.getString("Title"));
        localIntent.putExtra("backurl", "dianping://takeawayorderdetail?source=takeawayshoplist&orderviewid=" + paramTakeawayNetLoadStatus.getString("OrderId"));
        localIntent.putExtra("callbackurl", "dianping://takeawayorderdetail?source=takeawayshoplist&queryid=" + this.dataSource.queryId + "&pagefrom=1&orderviewid=" + paramTakeawayNetLoadStatus.getString("OrderId"));
        localIntent.putExtra("callbackfailurl", "dianping://takeawayfailure?source=takeawayshoplist&payorderid=" + i);
        startActivity(localIntent);
        return;
      }
      paramObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayorderdetail"));
      paramObject.putExtra("orderviewid", paramTakeawayNetLoadStatus.getString("OrderId"));
      paramObject.putExtra("source", "takeawayshoplist");
      paramObject.putExtra("pagefrom", 2);
      paramObject.putExtra("queryid", this.dataSource.queryId);
      getActivity().startActivity(paramObject);
      getActivity().finish();
      return;
    case 3:
    }
    this.loadingDlg.dismiss();
    if ((paramObject instanceof SimpleMsg))
    {
      paramObject = (SimpleMsg)paramObject;
      paramTakeawayNetLoadStatus = paramObject.content();
      switch (paramObject.flag())
      {
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      default:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "网络错误，请重试";
        showErrorToast(paramObject);
        return;
      case 1:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "商户不营业";
        showErrorDialog(paramObject, 6);
        return;
      case 2:
      case 8:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "菜品信息发生变化，请重新确认";
        showErrorDialog(paramObject, 5);
        return;
      case 9:
        paramObject = paramTakeawayNetLoadStatus;
        if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
          paramObject = "请勿重复提交订单，您的订单可能已经成功提交。";
        showErrorDialog(paramObject, 6);
        return;
      case 10:
      }
      paramObject = getActivity();
      if (TextUtils.isEmpty(paramTakeawayNetLoadStatus))
        paramTakeawayNetLoadStatus = "抱歉，您的地址不在送餐范围内";
      while (true)
      {
        paramTakeawayNetLoadStatus = new TAAlertDialog(paramObject, "", paramTakeawayNetLoadStatus, "修改送餐地址", "查看可送餐厅");
        paramObject = this.dataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(getActivity(), "addresscheck", paramObject, "view");
        paramTakeawayNetLoadStatus.setCanceledOnTouchOutside(true);
        paramTakeawayNetLoadStatus.setListener(new OnDialogOperationListener()
        {
          public void cancel()
          {
            Object localObject = TakeawayDeliveryDetailFragment.this.dataSource.getGAUserInfo();
            ((GAUserInfo)localObject).title = "修改送餐地址";
            GAHelper.instance().contextStatisticsEvent(TakeawayDeliveryDetailFragment.this.getActivity(), "addresscheck", (GAUserInfo)localObject, "tap");
            localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayaddressmodify?shopid=" + TakeawayDeliveryDetailFragment.this.dataSource.shopID));
            ((Intent)localObject).putExtra("address", TakeawayDeliveryDetailFragment.this.dataSource.orderAddress);
            TakeawayDeliveryDetailFragment.this.startActivityForResult((Intent)localObject, 1);
          }

          public void confirm(int paramInt)
          {
            Object localObject = TakeawayDeliveryDetailFragment.this.dataSource.getGAUserInfo();
            ((GAUserInfo)localObject).title = "查看可送餐厅";
            GAHelper.instance().contextStatisticsEvent(TakeawayDeliveryDetailFragment.this.getActivity(), "addresscheck", (GAUserInfo)localObject, "tap");
            localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshoplist"));
            ((Intent)localObject).putExtra("address", TakeawayDeliveryDetailFragment.this.dataSource.getAddress());
            ((Intent)localObject).putExtra("lat", TakeawayDeliveryDetailFragment.this.dataSource.getLat());
            ((Intent)localObject).putExtra("lng", TakeawayDeliveryDetailFragment.this.dataSource.getLng());
            ((Intent)localObject).putExtra("geotype", 2);
            ((Intent)localObject).setFlags(67108864);
            TakeawayDeliveryDetailFragment.this.getActivity().startActivity((Intent)localObject);
            TakeawayDeliveryDetailFragment.this.getActivity().finish();
          }
        });
        paramTakeawayNetLoadStatus.show();
        return;
      }
    }
    if ((paramObject instanceof IllegalArgumentException))
    {
      showErrorToast("啊哦，备注中只能填中英文数字和标点符号，请修改后重试");
      return;
    }
    showErrorToast("网络错误，请重试");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment
 * JD-Core Version:    0.6.0
 */