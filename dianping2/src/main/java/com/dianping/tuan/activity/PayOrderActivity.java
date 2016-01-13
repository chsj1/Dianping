package com.dianping.tuan.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.share.model.PayVerifyDialog;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.pay.PayManager;
import com.dianping.base.tuan.pay.PayManager.IPayResult;
import com.dianping.base.tuan.widget.BasicSingleItem;
import com.dianping.base.tuan.widget.PayChanelItem;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.util.PurchaseResultHelper;
import com.dianping.base.util.PurchaseResultHelper.PayFailTo;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.widget.TitleContentItem;
import com.dianping.util.DeviceUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.util.log.NovaLog;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayOrderActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, TableView.OnItemClickListener, View.OnClickListener
{
  public static final int MESSAGE_COUNT_DOWN = 65299;
  public static final int MESSAGE_LOAD_CASHIER = 65297;
  public static final int MESSAGE_PAY_CODE_COUNT_DOWN = 65301;
  public static final int REQUEST_CODE_ALISDK = 102;
  public static final int REQUEST_CODE_BANK = 106;
  public static final int REQUEST_CODE_DISCOUNT = 211;
  public static final int REQUEST_CODE_MINIALISDK = 105;
  public static final int REQUEST_CODE_TENPAY = 104;
  public static final int REQUEST_CODE_UMPAY = 103;
  public static final int REQUEST_CODE_WEB = 101;
  public static final int REQUEST_RETRY_COUNT = 3;
  private static final String RMB = "¥";
  public static final String TAG = "PayOrderActivity";
  protected TitleContentItem addressItemView;
  protected View allPayView;
  protected BasicSingleItem balanceItemView;
  protected BasicSingleItem buyCountItemView;
  protected String callBackFailUrl;
  protected String callBackUrl;
  protected boolean canPay = true;
  protected boolean canUseMemberPoint;
  protected MApiRequest cashierRequest;
  protected Button confirmButton;
  protected View deliveryInfoView;
  protected TitleContentItem deliveryItemView;
  protected BasicSingleItem discountItemView;
  protected boolean displayMorePaymentTool = false;
  protected DPObject dpBankElement;
  protected DPObject dpCashier;
  protected DPObject dpCurrentPaymentTool;
  protected DPObject dpDiscount;
  protected DPObject dpPayProduct;
  protected ArrayList<DPObject> dpPaymentToolList = new ArrayList();
  protected DPObject dpPrePayOrderDTO;
  protected String eventChanel;
  protected BasicSingleItem eventDiscountItemView;
  protected MApiRequest getPayCodeReq;
  protected boolean hasEventDiscount;
  protected double hongbaoBalance;
  protected BasicSingleItem hongbaoItemView;
  protected LinearLayout layerPayOrderCountDownTimer;
  protected LinearLayout layerPayOrderHint;
  protected TextView layerPayOrderPrompt;
  protected View loading;
  protected Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      while (true)
      {
        try
        {
          switch (paramMessage.what)
          {
          case 65298:
          case 65300:
            super.handleMessage(paramMessage);
            return;
          case 65297:
            PayOrderActivity.this.loadCachier();
            continue;
          case 65299:
          case 65301:
          }
        }
        catch (Exception paramMessage)
        {
          paramMessage.printStackTrace();
          return;
        }
        PayOrderActivity.this.formateRemainTime(paramMessage.arg1);
        if (!PayOrderActivity.this.payVerifyDialogIsShow())
          break;
        PayOrderActivity localPayOrderActivity = PayOrderActivity.this;
        localPayOrderActivity.payCodeCountDown -= 1;
        if (PayOrderActivity.this.payCodeCountDown == 0)
        {
          PayOrderActivity.this.payVerifyDialog.setGetCodeButtonText("获取验证码");
          PayOrderActivity.this.payVerifyDialog.setGetCodeButtonEnable(true);
          PayOrderActivity.this.payCodeCountDown = 60;
          return;
        }
        PayOrderActivity.this.payVerifyDialog.setGetCodeButtonText("重新获取(" + PayOrderActivity.this.payCodeCountDown + "s)");
        PayOrderActivity.this.payVerifyDialog.setGetCodeButtonEnable(false);
        removeMessages(65301);
        sendEmptyMessageDelayed(65301, 1000L);
        continue;
      }
    }
  };
  protected int memberPointRuleId;
  protected int memberPointValue;
  protected BasicSingleItem memberpointItemView;
  protected double myBalance;
  protected double needPay;
  protected BasicSingleItem needPayItemView;
  protected String orderId;
  protected int payCodeCountDown = 60;
  protected MApiRequest payRequest;
  protected TableView payTableView;
  protected PayVerifyDialog payVerifyDialog;
  protected double payamount;
  protected PaymentToolAdapter paymentToolAdapter;
  protected BasicSingleItem priceItemView;
  protected String productInfo;
  protected LinearLayout productInfoView;
  protected int productType;
  protected int requestCount = 0;
  protected TitleContentItem saySomeItemView;
  protected TextView titleView;
  protected String token;
  protected BasicSingleItem totalPriceItemView;
  protected TitleContentItem userItemView;

  private void setupHintView()
  {
    boolean bool1 = setupPromptMsg();
    boolean bool2 = setupRemainTime();
    if (bool1)
    {
      this.layerPayOrderPrompt.setVisibility(0);
      if (!bool2)
        break label63;
      this.layerPayOrderCountDownTimer.setVisibility(0);
    }
    while (true)
    {
      if ((!bool1) && (!bool2))
        break label75;
      this.layerPayOrderHint.setVisibility(0);
      return;
      this.layerPayOrderPrompt.setVisibility(8);
      break;
      label63: this.layerPayOrderCountDownTimer.setVisibility(8);
    }
    label75: this.layerPayOrderHint.setVisibility(8);
  }

  private boolean setupPromptMsg()
  {
    if ((this.dpCashier != null) && (!TextUtils.isEmpty(this.dpCashier.getString("PromptMsg"))))
    {
      this.layerPayOrderPrompt.setText(this.dpCashier.getString("PromptMsg"));
      return true;
    }
    return false;
  }

  protected void afterConfirmOrder()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    if (TextUtils.isEmpty(this.callBackUrl))
      localIntent.setData(Uri.parse("dianping://purchaseresult"));
    while (true)
    {
      localIntent.putExtra("orderid", this.orderId);
      Bundle localBundle = getIntent().getBundleExtra("payextra");
      if (localBundle != null)
        localIntent.putExtra("payextra", localBundle);
      localIntent.addFlags(67108864);
      startActivity(localIntent);
      return;
      localIntent.setData(Uri.parse(this.callBackUrl));
    }
  }

  protected void afterConfirmOrderFail()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.addFlags(67108864);
    Bundle localBundle = getIntent().getBundleExtra("payextra");
    if (localBundle != null)
      localIntent.putExtra("payextra", localBundle);
    if (TextUtils.isEmpty(this.callBackFailUrl))
      localIntent.setData(Uri.parse("dianping://tuanmain"));
    while (true)
    {
      startActivity(localIntent);
      return;
      localIntent.setData(Uri.parse(this.callBackFailUrl));
    }
  }

  protected void confirmPay(String paramString)
  {
    if (!this.canPay)
      new AlertDialog.Builder(this).setTitle("提示").setMessage("该订单已过期，不能支付了!").setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PayOrderActivity.this.seckillForward();
        }
      }).setCancelable(false).show();
    do
    {
      return;
      if ((this.needPay <= 0.0D) || (this.dpCurrentPaymentTool != null))
        continue;
      showAlertDialog("请选择支付方式");
      return;
    }
    while (this.payRequest != null);
    String str2 = getPaymentTool();
    Uri.Builder localBuilder;
    if (this.dpDiscount == null)
    {
      str1 = "";
      localBuilder = Uri.parse("http://api.p.dianping.com/payorder.pay?").buildUpon();
      localBuilder.appendQueryParameter("token", this.token);
      localBuilder.appendQueryParameter("orderid", this.orderId);
      localBuilder.appendQueryParameter("paymenttool", str2);
      localBuilder.appendQueryParameter("discountid", str1);
      localBuilder.appendQueryParameter("eventchannel", this.eventChanel);
      localBuilder.appendQueryParameter("producttype", this.productType + "");
      if (!this.balanceItemView.isRightImageViewSelected())
        break label392;
      str1 = "1";
      label207: localBuilder.appendQueryParameter("usebalance", str1);
      if (!this.hongbaoItemView.isRightImageViewSelected())
        break label399;
      str1 = "1";
      label230: localBuilder.appendQueryParameter("usehongbao", str1);
      if (!this.memberpointItemView.isRightImageViewSelected())
        break label406;
    }
    label392: label399: label406: for (String str1 = "1"; ; str1 = "0")
    {
      localBuilder.appendQueryParameter("useuserpoint", str1);
      localBuilder.appendQueryParameter("pointruleid", "" + this.memberPointRuleId);
      localBuilder.appendQueryParameter("cx", DeviceUtils.cxInfo("payorder"));
      if (!TextUtils.isEmpty(paramString))
        localBuilder.appendQueryParameter("mobileverifycode", paramString);
      this.payRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.payRequest, this);
      showProgressDialog("正在提交...");
      return;
      str1 = this.dpDiscount.getInt("ID") + "";
      break;
      str1 = "0";
      break label207;
      str1 = "0";
      break label230;
    }
  }

  protected void dismissPayVerifyDialog()
  {
    if (payVerifyDialogIsShow())
      this.payVerifyDialog.dismiss();
  }

  protected DPObject findBestMemberPointRule(DPObject[] paramArrayOfDPObject, int paramInt)
  {
    Object localObject2 = null;
    Object localObject1 = null;
    if (paramArrayOfDPObject != null)
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        localObject2 = localObject1;
        if (i >= j)
          break;
        DPObject localDPObject = paramArrayOfDPObject[i];
        localObject2 = localObject1;
        if (localDPObject.getInt("UserPointValue") <= paramInt)
          if (localObject1 != null)
          {
            localObject2 = localObject1;
            if (localObject1.getInt("UserPointValue") >= localDPObject.getInt("UserPointValue"));
          }
          else
          {
            localObject2 = localDPObject;
          }
        i += 1;
        localObject1 = localObject2;
      }
    }
    return localObject2;
  }

  protected DPObject findBestMemberPointRule(DPObject[] paramArrayOfDPObject, int paramInt, double paramDouble)
  {
    Object localObject2 = null;
    Object localObject1 = null;
    if (paramArrayOfDPObject != null)
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        localObject2 = localObject1;
        if (i >= j)
          break;
        DPObject localDPObject = paramArrayOfDPObject[i];
        localObject2 = localObject1;
        if (localDPObject.getInt("UserPointValue") <= paramInt)
        {
          localObject2 = localObject1;
          if (localDPObject.getDouble("UserPointBalance") <= paramDouble)
            if (localObject1 != null)
            {
              localObject2 = localObject1;
              if (localObject1.getDouble("UserPointBalance") >= localDPObject.getDouble("UserPointBalance"));
            }
            else
            {
              localObject2 = localDPObject;
            }
        }
        i += 1;
        localObject1 = localObject2;
      }
    }
    return localObject2;
  }

  protected double findMinimumPointRuleBalance(DPObject[] paramArrayOfDPObject)
  {
    double d2 = 2.0D;
    if (paramArrayOfDPObject != null)
    {
      double d1 = 0.0D;
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        d2 = d1;
        if (i >= j)
          break;
        DPObject localDPObject = paramArrayOfDPObject[i];
        if (d1 != 0.0D)
        {
          d2 = d1;
          if (d1 <= localDPObject.getDouble("UserPointBalance"));
        }
        else
        {
          d2 = localDPObject.getDouble("UserPointBalance");
        }
        i += 1;
        d1 = d2;
      }
    }
    return d2;
  }

  protected void formateRemainTime(int paramInt)
  {
    if (paramInt < 0)
      return;
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    long l1 = paramInt / 60;
    long l2 = paramInt % 60;
    StringBuilder localStringBuilder = new StringBuilder();
    if (l1 < 10L)
    {
      localObject = "0";
      localSpannableStringBuilder.append((String)localObject + l1 + "分");
      localStringBuilder = new StringBuilder();
      if (l2 >= 10L)
        break label231;
    }
    label231: for (Object localObject = "0"; ; localObject = "")
    {
      localSpannableStringBuilder.append((String)localObject + l2 + "秒");
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 0, 2, 33);
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 3, 5, 33);
      ((TextView)this.layerPayOrderCountDownTimer.findViewById(R.id.tv_payorder_remain_time)).setText(localSpannableStringBuilder);
      paramInt -= 1;
      if (paramInt < 0)
        break label238;
      localObject = this.mHandler.obtainMessage(65299);
      ((Message)localObject).arg1 = paramInt;
      this.mHandler.sendMessageDelayed((Message)localObject, 1000L);
      return;
      localObject = "";
      break;
    }
    label238: this.canPay = false;
  }

  protected void getPayCode()
  {
    if (this.getPayCodeReq != null)
      mapiService().abort(this.getPayCodeReq, this, true);
    this.getPayCodeReq = BasicMApiRequest.mapiGet("http://api.p.dianping.com/paymobileverify.pay?token=" + this.token, CacheType.DISABLED);
    mapiService().exec(this.getPayCodeReq, this);
  }

  protected String getPaymentTool()
  {
    if (this.dpCurrentPaymentTool == null);
    for (String str = null; ; str = this.dpCurrentPaymentTool.getString("ID"))
    {
      if (this.dpBankElement != null)
        str = this.dpBankElement.getString("PaymentID");
      if (this.needPay <= 0.0D)
        break;
      return str;
    }
    return "";
  }

  protected boolean goBanklist()
  {
    if ((this.needPay > 0.0D) && (this.dpCurrentPaymentTool != null) && (this.dpCurrentPaymentTool.getBoolean("IsRedirectBankList")))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://banklist"));
      localIntent.putExtra("orderid", this.orderId);
      localIntent.putExtra("producttype", this.productType);
      localIntent.putExtra("paymenttool", this.dpCurrentPaymentTool);
      startActivityForResult(localIntent, 106);
      overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.fade_light_out);
      return true;
    }
    return false;
  }

  protected void handlePayRequestResult(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("Flag");
    paramDPObject = paramDPObject.getString("Content");
    if (i == 0)
      afterConfirmOrder();
    do
    {
      do
      {
        return;
        if (i == 1)
        {
          pay(i, paramDPObject);
          return;
        }
        if (i != 2)
          continue;
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuan"));
        localIntent.putExtra("url", paramDPObject);
        startActivityForResult(localIntent, 101);
        return;
      }
      while (i == 3);
      if (i == 5)
      {
        umpay(paramDPObject);
        return;
      }
      if (i != 7)
        continue;
      pay(i, paramDPObject);
      return;
    }
    while (i != 10);
    pay(i, paramDPObject);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void loadCachier()
  {
    if (this.cashierRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://api.p.dianping.com/");
    localStringBuilder.append("getcashier.pay");
    localStringBuilder.append("?orderid=").append(this.orderId);
    localStringBuilder.append("&token=").append(this.token);
    localStringBuilder.append("&producttype=").append(this.productType);
    localStringBuilder.append("&eventchannel=").append(this.eventChanel);
    this.cashierRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.cashierRequest, this);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 101) || (paramInt1 == 102) || (paramInt1 == 104))
      if (paramInt2 == -1)
        afterConfirmOrder();
    do
      while (true)
      {
        return;
        Object localObject;
        String str;
        if (paramInt1 == 103)
        {
          if (paramIntent == null)
          {
            Toast.makeText(this, "支付失败，请选择其他支付方式", 0).show();
            return;
          }
          localObject = paramIntent.getStringExtra("umpResultCode");
          str = paramIntent.getStringExtra("umpResultMessage");
          paramIntent.getStringExtra("orderId");
          if ("0000".equalsIgnoreCase((String)localObject))
          {
            afterConfirmOrder();
            return;
          }
          if ("1001".equalsIgnoreCase((String)localObject))
            continue;
          showAlertDialog(str);
          return;
        }
        if (paramInt1 == 105)
        {
          if (paramIntent == null)
          {
            Toast.makeText(this, "支付失败，请选择其他支付方式", 0).show();
            return;
          }
          str = paramIntent.getStringExtra("resultStatus");
          localObject = str;
          if (TextUtils.isEmpty(str))
            localObject = paramIntent.getStringExtra("result_status");
          if (!"9000".equals(localObject))
            continue;
          afterConfirmOrder();
          return;
        }
        if (paramInt1 != 106)
          break;
        if ((paramInt2 != -1) || (paramIntent == null))
          continue;
        this.dpBankElement = ((DPObject)paramIntent.getParcelableExtra("bankelement"));
        confirmPay(null);
        return;
      }
    while ((paramInt1 != 211) || (paramIntent == null));
    this.dpDiscount = ((DPObject)paramIntent.getParcelableExtra("discount"));
    updateView();
  }

  public void onClick(View paramView)
  {
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool1 = true;
    if (this.dpCashier == null);
    do
    {
      return;
      if (this.discountItemView == paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://pickdiscount"));
        paramView.putExtra("orderid", this.orderId);
        paramView.putExtra("product", this.dpPayProduct);
        paramView.putExtra("discount", this.dpDiscount);
        startActivityForResult(paramView, 211);
        return;
      }
      if (this.confirmButton == paramView)
      {
        if ((this.dpCashier.getBoolean("NeedConfirm")) && (!TextUtils.isEmpty(this.dpCashier.getString("ConfirmContent"))))
        {
          new AlertDialog.Builder(this).setTitle("提示").setMessage(this.dpCashier.getString("ConfirmContent")).setPositiveButton("确定", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              PayOrderActivity.this.userConfirmedToPay();
            }
          }).setNegativeButton("取消", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          }).create().show();
          return;
        }
        userConfirmedToPay();
        return;
      }
      if (this.balanceItemView == paramView)
      {
        paramView = this.balanceItemView;
        if (!this.balanceItemView.isRightImageViewSelected());
        while (true)
        {
          paramView.setRightImageViewSelected(bool1);
          updateView();
          return;
          bool1 = false;
        }
      }
      if (this.hongbaoItemView != paramView)
        continue;
      paramView = this.hongbaoItemView;
      if (!this.hongbaoItemView.isRightImageViewSelected());
      for (bool1 = bool2; ; bool1 = false)
      {
        paramView.setRightImageViewSelected(bool1);
        updateView();
        return;
      }
    }
    while (this.memberpointItemView != paramView);
    if (this.canUseMemberPoint)
    {
      paramView = this.memberpointItemView;
      if (!this.memberpointItemView.isRightImageViewSelected());
      for (bool1 = bool3; ; bool1 = false)
      {
        paramView.setRightImageViewSelected(bool1);
        updateView();
        return;
      }
    }
    startActivity("dianping://efte?unit=unit-dianping-tuan&path=src/credit/creditrule.html&query={memberpoint:" + this.memberPointValue + "}");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject;
    BasicSingleItem localBasicSingleItem;
    if (paramBundle != null)
    {
      this.callBackUrl = paramBundle.getString("callBackUrl");
      this.callBackFailUrl = paramBundle.getString("callBackFailUrl");
      this.productInfo = paramBundle.getString("productInfo");
      this.token = paramBundle.getString("token");
      this.eventChanel = paramBundle.getString("eventChanel");
      this.dpPayProduct = ((DPObject)paramBundle.getParcelable("dpPayProduct"));
      this.dpPrePayOrderDTO = ((DPObject)paramBundle.getParcelable("dpPrePayOrderDTO"));
      this.orderId = paramBundle.getString("orderId");
      this.dpCashier = ((DPObject)paramBundle.getParcelable("dpCashier"));
      this.dpPaymentToolList = paramBundle.getParcelableArrayList("dpPaymentToolList");
      this.dpCurrentPaymentTool = ((DPObject)paramBundle.getParcelable("dpCurrentPaymentTool"));
      this.displayMorePaymentTool = paramBundle.getBoolean("displayMorePaymentTool");
      this.canPay = paramBundle.getBoolean("canPay");
      this.needPay = paramBundle.getDouble("needPay");
      this.dpDiscount = ((DPObject)paramBundle.getParcelable("dpDiscount"));
      this.dpBankElement = ((DPObject)paramBundle.getParcelable("dpBankElement"));
      this.hasEventDiscount = paramBundle.getBoolean("hasEventDiscount");
      setupView();
      if (paramBundle != null)
      {
        localObject = paramBundle.getString("usebalance");
        localBasicSingleItem = this.balanceItemView;
        if (!((String)localObject).equals("1"))
          break label630;
        bool = true;
        label248: localBasicSingleItem.setRightImageViewSelected(bool);
        localObject = paramBundle.getString("usehongbao");
        localBasicSingleItem = this.hongbaoItemView;
        if (!((String)localObject).equals("1"))
          break label636;
      }
    }
    label630: label636: for (boolean bool = true; ; bool = false)
    {
      localBasicSingleItem.setRightImageViewSelected(bool);
      updateView();
      if (paramBundle != null)
        break label642;
      loadCachier();
      return;
      this.callBackUrl = getStringParam("callbackurl");
      this.callBackFailUrl = getStringParam("callbackfailurl");
      this.productInfo = getStringParam("productinfo");
      if (isLogined())
        this.token = accountService().token();
      if (TextUtils.isEmpty(this.token))
        this.token = getStringParam("token");
      this.eventChanel = getStringParam("eventchannel");
      this.dpPayProduct = ((DPObject)getIntent().getParcelableExtra("payproduct"));
      this.dpPrePayOrderDTO = ((DPObject)getIntent().getParcelableExtra("prepayorderdto"));
      if (this.dpPrePayOrderDTO != null)
        this.orderId = (this.dpPrePayOrderDTO.getInt("OrderID") + "");
      if (TextUtils.isEmpty(this.orderId))
        this.orderId = getStringParam("orderid");
      if (TextUtils.isEmpty(this.orderId))
      {
        i = getIntParam("orderid", -1000);
        if (i != -1000)
          this.orderId = (i + "");
      }
      if (this.dpPayProduct == null)
      {
        i = getIntParam("payproducttype");
        localObject = getStringParam("title");
        this.dpPayProduct = new DPObject("PayProduct").edit().putString("Title", (String)localObject).putInt("ProductType", i).generate();
      }
      if ((this.dpPayProduct != null) && (!TextUtils.isEmpty(this.orderId)))
        break;
      finish();
      Toast.makeText(this, "缺少必要参数!", 0).show();
      return;
      bool = false;
      break label248;
    }
    label642: int i = 0;
    label645: if (i < this.payTableView.getChildCount())
    {
      localObject = this.paymentToolAdapter.getItem(i);
      if (DPObjectUtils.isDPObjectof(localObject, "PaymentTool"))
      {
        paramBundle = (PayChanelItem)this.payTableView.getChildAt(i);
        localObject = (DPObject)localObject;
        if (this.dpCurrentPaymentTool != localObject)
          break label721;
      }
    }
    label721: for (bool = true; ; bool = false)
    {
      paramBundle.setChecked(bool);
      i += 1;
      break label645;
      break;
    }
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    super.onCreateTitleBar(paramTitleBar);
    paramTitleBar.removeAllRightViewItem();
    paramTitleBar.findViewById(R.id.title_bar_left_view_container).setVisibility(0);
  }

  protected void onDestroy()
  {
    if (this.cashierRequest != null)
    {
      mapiService().abort(this.cashierRequest, this, true);
      this.cashierRequest = null;
    }
    this.mHandler.removeMessages(65299);
    this.mHandler.removeMessages(65297);
    this.mHandler.removeMessages(65301);
    PayManager.instance().release();
    super.onDestroy();
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    boolean bool = false;
    paramTableView = this.paymentToolAdapter.getItem(paramInt);
    if (DPObjectUtils.isDPObjectof(paramTableView, "PaymentTool"))
    {
      if (this.dpCurrentPaymentTool != paramTableView)
      {
        this.dpCurrentPaymentTool = ((DPObject)paramTableView);
        if (this.dpCurrentPaymentTool.getDouble("EventDiscountAmount") > 0.0D)
          bool = true;
        this.hasEventDiscount = bool;
        if (!TextUtils.isEmpty(this.dpCurrentPaymentTool.getString("BankName")))
          break label88;
      }
      label88: for (this.dpBankElement = null; ; this.dpBankElement = new DPObject().edit().putString("PaymentID", this.dpCurrentPaymentTool.getString("ID")).putString("BankName", this.dpCurrentPaymentTool.getString("BankName")).generate())
      {
        updateView();
        return;
      }
    }
    this.displayMorePaymentTool = false;
    this.paymentToolAdapter.notifyDataSetChanged();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    localGAUserInfo.order_id = Integer.valueOf(this.orderId);
    super.onNewGAPager(localGAUserInfo);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    if (this.cashierRequest != null)
    {
      this.mHandler.removeMessages(65297);
      mapiService().abort(this.cashierRequest, this, true);
      this.cashierRequest = null;
      finish();
    }
    if (this.payRequest != null)
    {
      mapiService().abort(this.payRequest, this, true);
      this.payRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    if (this.cashierRequest == paramMApiRequest)
    {
      this.cashierRequest = null;
      if (this.requestCount < 3)
      {
        this.mHandler.sendEmptyMessageDelayed(65297, 3000L);
        this.requestCount += 1;
      }
    }
    do
    {
      return;
      Toast.makeText(this, paramMApiResponse.content(), 0).show();
      finish();
      return;
      if (this.payRequest != paramMApiRequest)
        continue;
      this.payRequest = null;
      if (paramMApiResponse.flag() == 1)
      {
        new AlertDialog.Builder(this).setTitle(paramMApiResponse.title()).setMessage(paramMApiResponse.content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            PayOrderActivity.this.afterConfirmOrderFail();
          }
        }).setCancelable(false).show();
        return;
      }
      if (paramMApiResponse.flag() == 2)
      {
        showPayVertifyDialog(paramMApiResponse.content());
        return;
      }
      if (paramMApiResponse.flag() == 3)
      {
        setPayVerifyError(paramMApiResponse.content());
        return;
      }
      Toast.makeText(this, paramMApiResponse.content(), 0).show();
      return;
    }
    while (this.getPayCodeReq != paramMApiRequest);
    this.getPayCodeReq = null;
    Toast.makeText(this, paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (isDPObjectof(paramMApiResponse.result()))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (this.cashierRequest != paramMApiRequest)
        break label317;
      this.cashierRequest = null;
      this.dpCashier = paramMApiResponse;
      this.dpPrePayOrderDTO = this.dpCashier.getObject("PrePayOrderDTO");
      if (this.dpPrePayOrderDTO == null)
      {
        paramMApiRequest = null;
        this.dpDiscount = paramMApiRequest;
        this.dpPaymentToolList.clear();
        if (this.dpCashier.getArray("PaymentTools") != null)
          this.dpPaymentToolList.addAll(Arrays.asList(this.dpCashier.getArray("PaymentTools")));
        paramMApiRequest = this.dpPaymentToolList.iterator();
        label122: if (!paramMApiRequest.hasNext())
          break label312;
        paramMApiResponse = (DPObject)paramMApiRequest.next();
        if ((this.dpCurrentPaymentTool == null) && (paramMApiResponse.getBoolean("IsLastUsed")))
        {
          this.dpCurrentPaymentTool = paramMApiResponse;
          if (paramMApiResponse.getDouble("EventDiscountAmount") <= 0.0D)
            break label244;
          bool = true;
          label177: this.hasEventDiscount = bool;
          if (!TextUtils.isEmpty(this.dpCurrentPaymentTool.getString("BankName")))
            break label249;
          this.dpBankElement = null;
        }
        label203: if (this.displayMorePaymentTool)
          break label305;
        if (paramMApiResponse.getInt("DisplayMode") != 0)
          break label307;
      }
      label305: label307: for (boolean bool = true; ; bool = false)
      {
        this.displayMorePaymentTool = bool;
        break label122;
        paramMApiRequest = this.dpPrePayOrderDTO.getObject("Discount");
        break;
        label244: bool = false;
        break label177;
        label249: this.dpBankElement = new DPObject().edit().putString("PaymentID", this.dpCurrentPaymentTool.getString("ID")).putString("BankName", this.dpCurrentPaymentTool.getString("BankName")).generate();
        break label203;
        break label122;
      }
      label312: updateView();
    }
    label317: 
    do
    {
      return;
      if (this.payRequest != paramMApiRequest)
        continue;
      dismissPayVerifyDialog();
      this.payRequest = null;
      handlePayRequestResult(paramMApiResponse);
      return;
    }
    while (this.getPayCodeReq != paramMApiRequest);
    this.getPayCodeReq = null;
    this.mHandler.sendEmptyMessage(65301);
    Toast.makeText(this, paramMApiResponse.getString("Content"), 0).show();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("callBackUrl", this.callBackUrl);
    paramBundle.putString("callBackFailUrl", this.callBackFailUrl);
    paramBundle.putString("productInfo", this.productInfo);
    paramBundle.putString("token", this.token);
    paramBundle.putString("eventChanel", this.eventChanel);
    paramBundle.putParcelable("dpPayProduct", this.dpPayProduct);
    paramBundle.putParcelable("dpPrePayOrderDTO", this.dpPrePayOrderDTO);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putParcelable("dpCashier", this.dpCashier);
    paramBundle.putParcelableArrayList("dpPaymentToolList", this.dpPaymentToolList);
    paramBundle.putParcelable("dpCurrentPaymentTool", this.dpCurrentPaymentTool);
    paramBundle.putBoolean("displayMorePaymentTool", this.displayMorePaymentTool);
    paramBundle.putBoolean("canPay", this.canPay);
    paramBundle.putDouble("needPay", this.needPay);
    paramBundle.putParcelable("dpDiscount", this.dpDiscount);
    paramBundle.putParcelable("dpBankElement", this.dpBankElement);
    paramBundle.putBoolean("hasEventDiscount", this.hasEventDiscount);
    if (this.balanceItemView.isRightImageViewSelected())
    {
      str = "1";
      paramBundle.putString("usebalance", str);
      if (!this.hongbaoItemView.isRightImageViewSelected())
        break label244;
    }
    label244: for (String str = "1"; ; str = "0")
    {
      paramBundle.putString("usehongbao", str);
      return;
      str = "0";
      break;
    }
  }

  protected void pay(int paramInt, String paramString)
  {
    PayManager.instance().pay(paramInt, paramString, this, new PayManager.IPayResult(paramString)
    {
      public void onPayFailed(int paramInt1, int paramInt2, String paramString)
      {
        NovaLog.i("PayOrderActivity", "onPayFailed()orderID=" + PayOrderActivity.this.orderId + "payType=" + paramInt1 + "payContent=" + this.val$payContent + "errorCode" + paramInt2 + "errorMsg" + paramString);
        String str = paramString;
        if (TextUtils.isEmpty(paramString))
          str = "支付失败，请选择其他支付方式";
        if (paramInt1 == 1)
          PayOrderActivity.this.showAlertDialog(str);
        do
        {
          return;
          if (paramInt1 != 7)
            continue;
          PayOrderActivity.this.showAlertDialog(str);
          return;
        }
        while (paramInt1 != 10);
        PayOrderActivity.this.showAlertDialog(str);
      }

      public void onPaySuccess(int paramInt1, int paramInt2, String paramString)
      {
        if (paramInt1 == 1)
          PayOrderActivity.this.afterConfirmOrder();
        do
        {
          return;
          if (paramInt1 != 7)
            continue;
          PayOrderActivity.this.afterConfirmOrder();
          return;
        }
        while (paramInt1 != 10);
        PayOrderActivity.this.afterConfirmOrder();
      }
    });
  }

  protected boolean payVerifyDialogIsShow()
  {
    return (this.payVerifyDialog != null) && (this.payVerifyDialog.isShowing());
  }

  protected void seckillForward()
  {
    startActivity(PurchaseResultHelper.instance().getIntentAfterBuy());
    Intent localIntent = new Intent("android.intent.action.VIEW");
    PurchaseResultHelper.PayFailTo localPayFailTo = PurchaseResultHelper.instance().getPayFailTo();
    if (localPayFailTo == PurchaseResultHelper.PayFailTo.UNPAID)
    {
      localIntent.setData(Uri.parse("dianping://myorder?tab=unpaid"));
      startActivity(localIntent);
    }
    do
      return;
    while (localPayFailTo != PurchaseResultHelper.PayFailTo.DEALINFO);
    localIntent.setData(Uri.parse("dianping://tuandeal"));
    localIntent.putExtra("deal", PurchaseResultHelper.instance().getDPObject());
    startActivity(localIntent);
  }

  protected void setPayVerifyError(String paramString)
  {
    if (!payVerifyDialogIsShow())
      return;
    this.payVerifyDialog.setError(paramString);
  }

  protected boolean setupRemainTime()
  {
    boolean bool;
    if (this.dpPrePayOrderDTO == null)
    {
      bool = false;
      if (this.dpPrePayOrderDTO != null)
        break label47;
    }
    label47: for (int i = 0; ; i = this.dpPrePayOrderDTO.getInt("RemainSecond"))
    {
      if (!bool)
        break label305;
      if (i >= 0)
        break label62;
      return false;
      bool = this.dpPrePayOrderDTO.getBoolean("NeedCountDown");
      break;
    }
    label62: SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    long l1 = i / 60;
    long l2 = i % 60;
    StringBuilder localStringBuilder = new StringBuilder();
    if (l1 < 10L)
    {
      localObject = "0";
      localSpannableStringBuilder.append((String)localObject + l1 + "分");
      localStringBuilder = new StringBuilder();
      if (l2 >= 10L)
        break label291;
    }
    label291: for (Object localObject = "0"; ; localObject = "")
    {
      localSpannableStringBuilder.append((String)localObject + l2 + "秒");
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 0, 2, 33);
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 3, 5, 33);
      ((TextView)this.layerPayOrderCountDownTimer.findViewById(R.id.tv_payorder_remain_time)).setText(localSpannableStringBuilder);
      i -= 1;
      if (i < 0)
        break label298;
      localObject = this.mHandler.obtainMessage(65299);
      ((Message)localObject).arg1 = i;
      this.mHandler.sendMessageDelayed((Message)localObject, 1000L);
      return true;
      localObject = "";
      break;
    }
    label298: this.canPay = false;
    return false;
    label305: return false;
  }

  protected void setupView()
  {
    setContentView(R.layout.pay_order_layout);
    this.layerPayOrderHint = ((LinearLayout)findViewById(R.id.layer_payorder_hint));
    this.layerPayOrderHint.setVisibility(8);
    this.layerPayOrderPrompt = ((TextView)findViewById(R.id.tv_payorder_prompt));
    this.layerPayOrderPrompt.setVisibility(8);
    this.layerPayOrderCountDownTimer = ((LinearLayout)findViewById(R.id.layer_payorder_countdowntimer));
    this.layerPayOrderCountDownTimer.setVisibility(8);
    this.titleView = ((TextView)findViewById(R.id.title));
    this.productInfoView = ((LinearLayout)findViewById(R.id.product_info_layout));
    this.deliveryInfoView = findViewById(R.id.delivery_info_layout);
    this.userItemView = ((TitleContentItem)findViewById(R.id.user));
    this.addressItemView = ((TitleContentItem)findViewById(R.id.address));
    this.deliveryItemView = ((TitleContentItem)findViewById(R.id.delivery));
    this.saySomeItemView = ((TitleContentItem)findViewById(R.id.say_something));
    this.priceItemView = ((BasicSingleItem)findViewById(R.id.price));
    this.priceItemView.setVisibility(8);
    this.buyCountItemView = ((BasicSingleItem)findViewById(R.id.buy_count));
    this.totalPriceItemView = ((BasicSingleItem)findViewById(R.id.total_price));
    this.discountItemView = ((BasicSingleItem)findViewById(R.id.discount));
    this.hongbaoItemView = ((BasicSingleItem)findViewById(R.id.hongbao));
    this.memberpointItemView = ((BasicSingleItem)findViewById(R.id.memberpoint));
    this.memberpointItemView.getSubTitleView().setMaxWidth(ViewUtils.dip2px(this, 200.0F));
    this.memberpointItemView.getSubTitleView().setMaxLines(2);
    this.balanceItemView = ((BasicSingleItem)findViewById(R.id.balance));
    this.eventDiscountItemView = ((BasicSingleItem)findViewById(R.id.event_discount));
    this.needPayItemView = ((BasicSingleItem)findViewById(R.id.need_pay));
    this.allPayView = findViewById(R.id.all_pay_layer);
    this.payTableView = ((TableView)findViewById(R.id.pay_table));
    this.confirmButton = ((Button)findViewById(R.id.confirm));
    this.loading = findViewById(R.id.loading);
    this.confirmButton.setOnClickListener(this);
    this.payTableView.setOnItemClickListener(this);
    this.eventDiscountItemView.setVisibility(8);
    this.allPayView.setVisibility(8);
    this.paymentToolAdapter = new PaymentToolAdapter();
    this.payTableView.setAdapter(this.paymentToolAdapter);
    if (!TextUtils.isEmpty(this.productInfo))
    {
      this.productInfoView.setVisibility(0);
      showProductInfoView(this.productInfo);
      return;
    }
    this.productInfoView.setVisibility(8);
  }

  protected void showPayVertifyDialog(String paramString)
  {
    if (payVerifyDialogIsShow())
      this.payVerifyDialog.dismiss();
    this.payVerifyDialog = new PayVerifyDialog(this);
    this.payVerifyDialog.setContent(paramString);
    this.payVerifyDialog.setGetCodeButton(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayOrderActivity.this.getPayCode();
      }
    }).setOkButton(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TextUtils.isEmpty(PayOrderActivity.this.payVerifyDialog.getCode()))
        {
          Toast.makeText(PayOrderActivity.this, "请输入验证码", 0).show();
          return;
        }
        KeyboardUtils.hideKeyboard(PayOrderActivity.this.payVerifyDialog.getCodeInputText());
        paramView = PayOrderActivity.this.payVerifyDialog.getCode().toString();
        PayOrderActivity.this.confirmPay(paramView);
      }
    }).setCancelButton(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(PayOrderActivity.this.payVerifyDialog.getCodeInputText());
        PayOrderActivity.this.dismissPayVerifyDialog();
      }
    });
    this.payVerifyDialog.show();
    this.mHandler.sendEmptyMessage(65301);
  }

  protected void showProductInfoView(String paramString)
  {
    try
    {
      paramString = new JSONArray(paramString);
      if (paramString != null)
      {
        this.productInfoView.removeAllViews();
        int i = 0;
        while (i < paramString.length())
        {
          TitleContentItem localTitleContentItem = new TitleContentItem(this);
          JSONObject localJSONObject = paramString.getJSONObject(i);
          localTitleContentItem.setTitle(localJSONObject.getString("title"));
          localTitleContentItem.setContent(localJSONObject.getString("content"));
          this.productInfoView.addView(localTitleContentItem);
          i += 1;
        }
      }
    }
    catch (JSONException paramString)
    {
      paramString.printStackTrace();
    }
  }

  protected void umpay(String paramString)
  {
    try
    {
      if (TextUtils.isEmpty(paramString))
        return;
      paramString = paramString.split("\\|\\|");
      if (paramString.length >= 2)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://umpay"));
        localIntent.putExtra("tradeNo", paramString[0]);
        localIntent.putExtra("cardType", paramString[1]);
        if ((this.dpBankElement != null) && (!TextUtils.isEmpty(this.dpBankElement.getString("BankName"))))
          localIntent.putExtra("bankName", this.dpBankElement.getString("BankName"));
        startActivityForResult(localIntent, 103);
        return;
      }
    }
    catch (Exception paramString)
    {
      Toast.makeText(this, "支付失败，请选择其他支付方式", 0).show();
    }
  }

  protected void updateView()
  {
    setupHintView();
    if (this.dpCashier != null)
      this.loading.setVisibility(8);
    while (true)
    {
      this.productType = this.dpPayProduct.getInt("ProductType");
      Object localObject1 = this.dpPayProduct.getString("Title");
      label71: int i;
      label81: label123: double d1;
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        this.titleView.setText((CharSequence)localObject1);
        this.titleView.setVisibility(0);
        if (this.dpPrePayOrderDTO != null)
          break label2065;
        i = 0;
        this.buyCountItemView.setSubTitle("x " + i);
        localObject1 = this.buyCountItemView;
        if (i <= 0)
          break label2080;
        i = 0;
        ((BasicSingleItem)localObject1).setVisibility(i);
        if (this.dpPrePayOrderDTO != null)
          break label2087;
        d1 = 0.0D;
        label139: this.payamount = d1;
        this.totalPriceItemView.setSubTitle("¥" + PriceFormatUtils.formatPrice(this.payamount));
        this.needPay = this.payamount;
        this.deliveryInfoView.setVisibility(8);
        if ((this.dpPrePayOrderDTO == null) || (this.productType != 1));
      }
      label1410: label2694: 
      try
      {
        localObject1 = this.dpPrePayOrderDTO.getObject("Delivery");
        localObject3 = this.dpPrePayOrderDTO.getObject("DeliveryType");
        this.userItemView.setTitle("收货人");
        this.userItemView.setContent(((DPObject)localObject1).getString("Receiver") + " " + ((DPObject)localObject1).getString("PhoneNo"));
        this.addressItemView.setTitle("收货地址");
        this.addressItemView.setContent(((DPObject)localObject1).getString("ShowAddress"));
        this.deliveryItemView.setTitle("配送方式");
        this.deliveryItemView.setContent(((DPObject)localObject3).getString("Name"));
        localObject1 = ((DPObject)localObject1).getString("Memo");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          this.saySomeItemView.setTitle("特殊说明");
          this.saySomeItemView.setContent((String)localObject1);
          this.saySomeItemView.setVisibility(0);
          label382: this.deliveryInfoView.setVisibility(0);
          label390: if (this.dpCashier != null)
            break label2118;
          bool2 = true;
          label400: bool5 = this.hongbaoItemView.isRightImageViewSelected();
          if (this.dpDiscount == null)
            break label2133;
          i = 1;
          label419: m = 0;
          k = 0;
          if (this.dpPrePayOrderDTO != null)
            break label2139;
          d1 = 0.0D;
          label434: this.needPay -= d1;
          if (d1 <= 0.0D)
            break label2153;
          j = 1;
          label453: if (j != 0)
          {
            localObject1 = new SpannableStringBuilder();
            if (this.needPay <= 0.0D)
              break label2159;
            ((SpannableStringBuilder)localObject1).append("¥" + PriceFormatUtils.formatPrice(this.needPay));
            label507: localObject3 = new SpannableString("已享受立减" + PriceFormatUtils.formatPrice(d1) + "元");
            ((SpannableString)localObject3).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_very_small)), 0, ((SpannableString)localObject3).length(), 33);
            ((SpannableString)localObject3).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_gray)), 0, ((SpannableString)localObject3).length(), 33);
            ((SpannableStringBuilder)localObject1).append("\n").append((CharSequence)localObject3);
            this.totalPriceItemView.getSubTitleView().setMaxLines(2);
            this.totalPriceItemView.getSubTitleView().setGravity(5);
            this.totalPriceItemView.setSubTitle((CharSequence)localObject1);
          }
          if (!this.hasEventDiscount)
            break label2171;
          this.eventDiscountItemView.setVisibility(0);
          label665: d2 = 0.0D;
          d1 = d2;
          if (this.dpDiscount == null);
        }
      }
      catch (Exception localException4)
      {
        label2439: label2823: 
        try
        {
          Object localObject3;
          boolean bool2;
          boolean bool5;
          int m;
          int k;
          int j;
          d1 = Double.valueOf(this.dpDiscount.getString("Price")).doubleValue();
          boolean bool1;
          label703: boolean bool3;
          if (this.dpCashier == null)
          {
            bool1 = true;
            if (this.dpCashier != null)
              break label2198;
            bool3 = true;
            label713: boolean bool4 = bool1;
            d2 = d1;
            j = m;
            if (bool1)
            {
              bool4 = bool1;
              d2 = d1;
              j = m;
              if (!bool2)
              {
                bool4 = bool1;
                d2 = d1;
                j = m;
                if (bool5)
                {
                  d2 = 0.0D;
                  this.dpDiscount = null;
                  bool4 = false;
                  j = 1;
                }
              }
            }
            if (!bool4)
              break label2244;
            if (d2 <= 0.0D)
              break label2213;
            this.discountItemView.setSubTitle("-¥" + PriceFormatUtils.formatPrice(d2));
            label812: this.discountItemView.setIndicator(R.drawable.arrow_normal);
            this.discountItemView.setOnClickListener(this);
            label830: if (j != 0)
            {
              localObject1 = new SpannableString("不可同时使用返现金额");
              if (ViewUtils.getScreenWidthPixels(this) <= 600)
                break label2275;
              ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_list)), 0, ((SpannableString)localObject1).length(), 33);
              label887: this.discountItemView.setSubTitle((CharSequence)localObject1);
            }
            this.needPay -= d2;
          }
          label1419: label1424: label2065: label2322: label2962: 
          try
          {
            if (this.dpCashier == null)
            {
              d1 = 0.0D;
              label915: this.hongbaoBalance = d1;
              label920: if (this.dpCashier != null)
                break label2322;
              bool1 = true;
              label930: bool3 = bool1;
              j = k;
              if (bool1)
              {
                bool3 = bool1;
                j = k;
                if (!bool2)
                {
                  bool3 = bool1;
                  j = k;
                  if (i != 0)
                  {
                    bool3 = false;
                    j = 1;
                  }
                }
              }
              if (!bool3)
                break label2402;
              this.hongbaoItemView.setIndicator(R.drawable.cbx_bg);
              if ((this.needPay <= 0.0D) || (this.hongbaoBalance <= 0.0D))
                break label2337;
              this.hongbaoItemView.setOnClickListener(this);
              label1016: d1 = 0.0D;
              if (this.hongbaoItemView.isRightImageViewSelected())
              {
                if (this.needPay <= this.hongbaoBalance)
                  break label2356;
                d1 = this.hongbaoBalance;
              }
              label1045: localObject3 = this.hongbaoItemView;
              if (!this.hongbaoItemView.isRightImageViewSelected())
                break label2364;
              localObject1 = "使用返现";
              label1066: ((BasicSingleItem)localObject3).setTitle((CharSequence)localObject1);
              localObject3 = this.hongbaoItemView;
              if (d1 <= 0.0D)
                break label2372;
              localObject1 = "-¥" + PriceFormatUtils.formatPrice(d1);
              label1110: ((BasicSingleItem)localObject3).setSubTitle((CharSequence)localObject1);
              this.needPay -= d1;
              label1127: if (j != 0)
              {
                localObject1 = new SpannableString("不可与抵用券同时使用");
                if (ViewUtils.getScreenWidthPixels(this) <= 600)
                  break label2439;
                ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_list)), 0, ((SpannableString)localObject1).length(), 33);
                this.hongbaoItemView.setSubTitle((CharSequence)localObject1);
              }
              label1184: if (this.hongbaoBalance <= 0.0D)
              {
                this.hongbaoItemView.setIndicator(0);
                this.hongbaoItemView.setTitle("我的返现");
                this.hongbaoItemView.setSubTitle("¥0");
                this.hongbaoItemView.setClickable(false);
              }
              if (this.dpCashier != null)
                break label2472;
              localObject1 = null;
              label1248: if (localObject1 != null)
              {
                this.memberpointItemView.setVisibility(0);
                this.memberpointItemView.setTitle("积分");
                this.canUseMemberPoint = ((DPObject)localObject1).getBoolean("CanUseUserPoint");
                this.memberPointValue = ((DPObject)localObject1).getInt("TotalUserPointValue");
                if (!this.canUseMemberPoint)
                  break label2597;
                localObject3 = findBestMemberPointRule(((DPObject)localObject1).getArray("UserPointExchangeRules"), this.memberPointValue, this.needPay);
                if (localObject3 != null)
                  break label2487;
                this.memberpointItemView.setOnClickListener(null);
                this.memberpointItemView.setClickable(false);
                this.memberpointItemView.setIndicator(0);
                this.memberpointItemView.setRightImageViewSelected(false);
                d1 = findMinimumPointRuleBalance(((DPObject)localObject1).getArray("UserPointExchangeRules"));
                this.memberpointItemView.setSubTitle("满" + PriceFormatUtils.formatPrice(d1) + "元可使用积分");
              }
            }
            label2709: 
            try
            {
              if (this.dpCashier == null)
              {
                d1 = 0.0D;
                this.myBalance = d1;
                if (this.dpCashier != null)
                  break label2694;
                bool1 = true;
                label1434: if (!bool1)
                  break label2774;
                this.balanceItemView.setIndicator(R.drawable.cbx_bg);
                if ((this.needPay <= 0.0D) || (this.myBalance <= 0.0D))
                  break label2709;
                this.balanceItemView.setOnClickListener(this);
                label1475: d1 = 0.0D;
                if (this.balanceItemView.isRightImageViewSelected())
                {
                  if (this.needPay <= this.myBalance)
                    break label2728;
                  d1 = this.myBalance;
                }
                label1504: localObject3 = this.balanceItemView;
                if (!this.balanceItemView.isRightImageViewSelected())
                  break label2736;
                localObject1 = "使用余额";
                label1525: ((BasicSingleItem)localObject3).setTitle((CharSequence)localObject1);
                localObject3 = this.balanceItemView;
                if (d1 <= 0.0D)
                  break label2744;
                localObject1 = "-¥" + PriceFormatUtils.formatPrice(d1);
                label1569: ((BasicSingleItem)localObject3).setSubTitle((CharSequence)localObject1);
                this.needPay -= d1;
                label1586: if (this.myBalance <= 0.0D)
                {
                  this.balanceItemView.setIndicator(0);
                  this.balanceItemView.setTitle("我的余额");
                  this.balanceItemView.setSubTitle("¥0");
                  this.balanceItemView.setClickable(false);
                }
                if ((this.dpPaymentToolList.size() <= 0) || (this.needPay <= 0.0D))
                  break label2811;
                this.allPayView.setVisibility(0);
                label1658: if ((this.dpCurrentPaymentTool != null) && (this.dpCurrentPaymentTool.getDouble("EventDiscountAmount") > 0.0D))
                {
                  localObject1 = "暂不满足立减条件";
                  d1 = this.dpCurrentPaymentTool.getDouble("EventDiscountAmount");
                  d2 = this.dpCurrentPaymentTool.getDouble("EventLimitAmount");
                  if (this.needPay - d1 >= d2)
                  {
                    this.needPay -= d1;
                    localObject1 = "-¥" + PriceFormatUtils.formatPrice(d1);
                  }
                  this.eventDiscountItemView.setSubTitle((CharSequence)localObject1);
                }
                if (this.needPay <= 0.0D)
                  break label2823;
                this.needPayItemView.setSubTitle("¥" + PriceFormatUtils.formatPrice(this.needPay));
                if (this.dpCashier != null)
                  break label2836;
                i = 0;
                label1813: localObject1 = this.buyCountItemView;
                if (i != 1)
                  break label2851;
                i = 8;
                label1829: ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2857;
                i = 0;
                label1846: localObject1 = this.totalPriceItemView;
                if (i != 1)
                  break label2872;
                i = 8;
                label1862: ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2878;
                i = 0;
                label1879: localObject1 = this.discountItemView;
                if (i != 1)
                  break label2893;
                i = 8;
                label1895: ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2899;
                i = 0;
                label1912: localObject1 = this.hongbaoItemView;
                if (i != 1)
                  break label2914;
                i = 8;
                ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2920;
                i = 0;
                label1945: localObject1 = this.memberpointItemView;
                if (i != 1)
                  break label2935;
                i = 8;
                label1961: ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2941;
                i = 0;
                label1978: localObject1 = this.balanceItemView;
                if (i != 1)
                  break label2956;
                i = 8;
                label1994: ((BasicSingleItem)localObject1).setVisibility(i);
                if (this.dpCashier != null)
                  break label2962;
                i = 0;
                label2011: localObject1 = this.needPayItemView;
                if (i != 1)
                  break label2977;
              }
              label2080: label2337: label2977: for (i = 8; ; i = 0)
              {
                ((BasicSingleItem)localObject1).setVisibility(i);
                this.paymentToolAdapter.notifyDataSetChanged();
                return;
                this.loading.setVisibility(0);
                break;
                this.titleView.setVisibility(8);
                break label71;
                i = this.dpPrePayOrderDTO.getInt("Count");
                break label81;
                i = 8;
                break label123;
                label2087: d1 = this.dpPrePayOrderDTO.getDouble("TotalPrice");
                break label139;
                this.saySomeItemView.setVisibility(8);
                break label382;
                localException1 = localException1;
                break label390;
                label2118: bool2 = this.dpCashier.getBoolean("CanCombineDiscountPayment");
                break label400;
                label2133: i = 0;
                break label419;
                label2139: d1 = this.dpPrePayOrderDTO.getDouble("ReductionPrice");
                break label434;
                label2153: j = 0;
                break label453;
                label2159: localException1.append("¥0");
                break label507;
                label2171: this.eventDiscountItemView.setVisibility(8);
                break label665;
                bool1 = this.dpCashier.getBoolean("CanUseDiscount");
                break label703;
                bool3 = this.dpCashier.getBoolean("UserHasDiscount");
                break label713;
                label2213: if (bool3)
                {
                  this.discountItemView.setSubTitle("可用");
                  break label812;
                }
                this.discountItemView.setSubTitle("");
                break label812;
                label2244: d2 = 0.0D;
                this.discountItemView.setSubTitle("不可用");
                this.discountItemView.setIndicator(0);
                this.discountItemView.setClickable(false);
                break label830;
                label2275: localException1.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_hint)), 0, localException1.length(), 33);
                break label887;
                d1 = this.dpCashier.getDouble("RedEnvelopeBalance");
                break label915;
                bool1 = this.dpCashier.getBoolean("CanUseRedEnvelope");
                break label930;
                this.hongbaoItemView.setRightImageViewSelected(false);
                this.hongbaoItemView.setClickable(false);
                break label1016;
                label2356: d1 = this.needPay;
                break label1045;
                label2364: Object localObject2 = "我的返现";
                break label1066;
                label2372: localObject2 = "¥" + PriceFormatUtils.formatPrice(this.hongbaoBalance);
                break label1110;
                label2402: this.hongbaoItemView.setSubTitle("不可用");
                this.hongbaoItemView.setIndicator(0);
                this.hongbaoItemView.setRightImageViewSelected(false);
                this.hongbaoItemView.setClickable(false);
                break label1127;
                ((SpannableString)localObject2).setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_hint)), 0, ((SpannableString)localObject2).length(), 33);
                break label1184;
                label2472: localObject2 = this.dpCashier.getObject("UserPoint");
                break label1248;
                label2487: this.memberpointItemView.setOnClickListener(this);
                this.memberpointItemView.setIndicator(R.drawable.cbx_bg);
                this.memberPointRuleId = ((DPObject)localObject3).getInt("RuleID");
                if (this.memberpointItemView.isRightImageViewSelected())
                {
                  d1 = ((DPObject)localObject3).getDouble("UserPointBalance");
                  this.needPay -= d1;
                  this.memberpointItemView.setSubTitle("-¥" + PriceFormatUtils.formatPrice(d1));
                  break label1410;
                }
                this.memberpointItemView.setSubTitle(((DPObject)localObject3).getString("Memo"));
                break label1410;
                label2597: this.memberpointItemView.setOnClickListener(this);
                this.memberpointItemView.setIndicator(R.drawable.arrow_normal);
                this.memberpointItemView.setRightImageViewSelected(false);
                localObject2 = findBestMemberPointRule(((DPObject)localObject2).getArray("UserPointExchangeRules"), this.memberPointValue);
                localObject3 = this.memberpointItemView;
                if (localObject2 == null);
                for (localObject2 = "积分不足"; ; localObject2 = ((DPObject)localObject2).getString("Memo"))
                {
                  ((BasicSingleItem)localObject3).setSubTitle((CharSequence)localObject2);
                  break;
                }
                d1 = this.dpCashier.getDouble("UserBalance");
                break label1419;
                bool1 = this.dpCashier.getBoolean("CanUseBalance");
                break label1434;
                this.balanceItemView.setRightImageViewSelected(false);
                this.balanceItemView.setClickable(false);
                break label1475;
                label2728: d1 = this.needPay;
                break label1504;
                label2736: localObject2 = "我的余额";
                break label1525;
                label2744: localObject2 = "¥" + PriceFormatUtils.formatPrice(this.myBalance);
                break label1569;
                label2774: this.balanceItemView.setSubTitle("不可用");
                this.balanceItemView.setIndicator(0);
                this.balanceItemView.setRightImageViewSelected(false);
                this.balanceItemView.setClickable(false);
                break label1586;
                label2811: this.allPayView.setVisibility(8);
                break label1658;
                this.needPayItemView.setSubTitle("¥0");
                break label1803;
                i = this.dpCashier.getInt("ShowQuantityMode");
                break label1813;
                label2851: i = 0;
                break label1829;
                label2857: i = this.dpCashier.getInt("ShowTotalAmountMode");
                break label1846;
                label2872: i = 0;
                break label1862;
                label2878: i = this.dpCashier.getInt("ShowDiscountMode");
                break label1879;
                label2893: i = 0;
                break label1895;
                label2899: i = this.dpCashier.getInt("ShowRedEnvelopeMode");
                break label1912;
                label2914: i = 0;
                break label1928;
                label2920: i = this.dpCashier.getInt("ShowUserPointMode");
                break label1945;
                label2935: i = 0;
                break label1961;
                label2941: i = this.dpCashier.getInt("ShowBalanceMode");
                break label1978;
                i = 0;
                break label1994;
                i = this.dpCashier.getInt("ShowPaymentAmountMode");
                break label2011;
              }
            }
            catch (Exception localException2)
            {
              label2198: break label1424;
            }
          }
          catch (Exception localException3)
          {
            label2836: break label920;
          }
        }
        catch (Exception localException4)
        {
          label1928: 
          while (true)
          {
            double d2;
            label1803: label2956: d1 = d2;
          }
        }
      }
    }
  }

  protected void userConfirmedToPay()
  {
    if (!goBanklist())
      confirmPay(null);
  }

  class PaymentToolAdapter extends BasicAdapter
  {
    PaymentToolAdapter()
    {
    }

    public int getCount()
    {
      int j = PayOrderActivity.this.dpPaymentToolList.size();
      int i = j;
      if (PayOrderActivity.this.displayMorePaymentTool)
        i = j + 1;
      return i;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < PayOrderActivity.this.dpPaymentToolList.size())
        return PayOrderActivity.this.dpPaymentToolList.get(paramInt);
      return LAST_EXTRA;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramView, "PaymentTool"))
      {
        paramView = (DPObject)paramView;
        paramViewGroup = new PayChanelItem(PayOrderActivity.this);
        paramViewGroup.setChecked(PayOrderActivity.this.dpCurrentPaymentTool.getString("ID").equals(paramView.getString("ID")));
        paramViewGroup.setPaymentTool(paramView);
        paramViewGroup.setBackgroundResource(R.drawable.table_view_item);
        paramViewGroup.setClickable(true);
        if ((paramView.getInt("DisplayMode") == 0) && (PayOrderActivity.this.displayMorePaymentTool) && (PayOrderActivity.this.dpCurrentPaymentTool != paramView))
          paramViewGroup.setVisibility(8);
        return paramViewGroup;
      }
      paramView = LayoutInflater.from(PayOrderActivity.this).inflate(R.layout.display_more_pay, paramViewGroup, false);
      paramView.setClickable(true);
      return paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.PayOrderActivity
 * JD-Core Version:    0.6.0
 */