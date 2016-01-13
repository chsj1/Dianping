package com.dianping.hui.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.tuan.pay.AlipayResult;
import com.dianping.base.tuan.widget.PayChanelItem;
import com.dianping.base.widget.BeautifulProgressDialog;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hui.util.HuiUtils;
import com.dianping.hui.widget.HuiAlertDialog;
import com.dianping.hui.widget.HuiAlertDialog.OnDialogOperationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HuiCashierActivity extends NovaActivity
  implements TableView.OnItemClickListener, View.OnClickListener, TextWatcher, DialogInterface.OnCancelListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String ACTIVITY_JS_HEAD = "activityjsonresult:";
  private static final int ALIPAYSDK_PAY_FLAG = 100;
  private static final int EDIT_COST = 2;
  private static final String JS_HEAD = "jsonresult:";
  private static final String LOCATION_REQ = "http://hui.api.dianping.com/ordercheck.hui?";
  private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000");
  private static final String PAY_REQ = "http://hui.api.dianping.com/mopay.bin";
  private static final String STRATEGY_REQ = "http://hui.api.dianping.com/getpaystrategys.hui?";
  private static final String TAG = "TAG_hui";
  private static final int THIRD_PARTY_PAY_TYPE_SHANGLONG = 30;
  private static final int TICKETS_ACTIVITY_REQ = 1;
  private static final int UPDATE_PAY = 1;
  List<HuiCashierActivity.CashierDiscountItem.HuiActivityInfo> activities = new ArrayList();
  private String activityJsFunction;
  private int[] activityJsShowTypes;
  boolean backFromLogin = false;
  Button btnSubmit;
  DialogInterface.OnCancelListener childCancelListener;
  String cityId;
  BigDecimal cost;
  private View costHintView;
  boolean costNull = true;
  EditText costView;
  DiscountAdapter discountAdapter;
  List<CashierDiscountItem> discounts = new ArrayList();
  private TableView discountsTableView;
  private View editCostView;
  private View editNoDiscountView;
  private String enableNoDiscountInput;
  Button followingBtnSubmit;
  private View followingHuiCashierSubmitLayout;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        HuiCashierActivity.this.payView.setText(HuiCashierActivity.this.pay.stripTrailingZeros().toPlainString());
        if ((HuiCashierActivity.this.pay != null) && (HuiCashierActivity.this.pay.doubleValue() > 0.0D) && (!TextUtils.isEmpty(HuiCashierActivity.this.costView.getText())))
        {
          HuiCashierActivity.this.btnSubmit.setEnabled(true);
          HuiCashierActivity.this.followingBtnSubmit.setEnabled(true);
          HuiCashierActivity.this.btnSubmit.setText(HuiCashierActivity.this.pay.stripTrailingZeros().toPlainString() + "元 确认支付");
          HuiCashierActivity.this.followingBtnSubmit.setText(HuiCashierActivity.this.pay.stripTrailingZeros().toPlainString() + "元 确认支付");
        }
        while (true)
        {
          HuiCashierActivity.this.discountAdapter.notifyDataSetChanged();
          HuiCashierActivity.this.paymentToolAdapter.notifyDataSetChanged();
          if (!HuiCashierActivity.this.inNoDeductionMode())
            break;
          HuiCashierActivity.this.hideFinalResultLayout();
          return;
          HuiCashierActivity.this.btnSubmit.setEnabled(false);
          HuiCashierActivity.this.followingBtnSubmit.setEnabled(false);
          HuiCashierActivity.this.btnSubmit.setText("确认支付");
          HuiCashierActivity.this.followingBtnSubmit.setText("确认支付");
        }
      case 2:
        HuiCashierActivity.this.ruleJs();
        return;
      case 100:
      }
      paramMessage = new AlipayResult((String)paramMessage.obj).getResultStatus();
      if (TextUtils.equals(paramMessage, "9000"))
      {
        HuiCashierActivity.this.gotoPayResult();
        HuiCashierActivity.this.finish();
        return;
      }
      if (TextUtils.equals(paramMessage, "8000"))
      {
        HuiCashierActivity.this.showToast("支付结果确认中");
        return;
      }
      HuiCashierActivity.this.showToast("支付失败");
    }
  };
  private View huiCashierLoadedLayout;
  private View huiCashierLoadingLayout;
  private View huiCashierSubmitLayout;
  int huiOrderId;
  private Drawable icSmallChecked;
  private Drawable icSmallUnchecked;
  private boolean isNoDiscountChecked;
  private String jsFunction;
  String latitude;
  String longitude;
  Dialog managedDialog;
  private BigDecimal minAmount;
  BigDecimal noDiscountAmount;
  private CheckBox noDiscountCheckBox;
  private View noDiscountHintView;
  private ImageView noDiscountImageView;
  private View noDiscountInfoView;
  String noDiscountInputUrl;
  EditText noDiscountView;
  int orderBizType;
  String orderId;
  BigDecimal pay;
  private String payHint;
  private MApiRequest payLocationReq;
  int payOrderId;
  private MApiRequest payReq;
  private MApiRequest payStrategyReq;
  TextView payView;
  PaymentToolAdapter paymentToolAdapter;
  List<DPObject> paymentTools;
  private TableView paymentToolsTableView;
  boolean reloadWithInput = false;
  private int retry = 5;
  private volatile String ruleJsKey = null;
  private ArrayList<DPObject> rules;
  private String rulesDesc;
  CashierDiscountItem selectedDiscount;
  DPObject selectedPaymentTool;
  String serializedId;
  int shopId;
  String shopName;
  private Drawable smallYangDrawable;
  int source;
  String thirdPartyOrderId;
  Integer thirdPartyOrderType = Integer.valueOf(0);
  private TextView tvPayHint;
  String userMobile;
  private View vEditCostMarginBottom;
  private View vFinalResult;
  private View vFinalResultSplitLine;
  private View vPayOptionSplitLine;
  private WebView webView;
  private final BroadcastReceiver wxpayReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramIntent = new PayResp(paramIntent.getExtras());
      if (paramIntent.errCode == 0)
      {
        HuiCashierActivity.this.gotoPayResult();
        HuiCashierActivity.this.finish();
        return;
      }
      if (TextUtils.isEmpty(paramIntent.errStr))
      {
        Toast.makeText(paramContext, "微信支付取消", 1).show();
        return;
      }
      HuiCashierActivity.this.gotoPayResult();
      HuiCashierActivity.this.finish();
    }
  };
  private Drawable yangDrawable;

  private void aliPay(String paramString)
  {
    try
    {
      if (TextUtils.isEmpty(paramString))
      {
        showToast("获取订单号失败");
        return;
      }
      new Thread(new Runnable(paramString)
      {
        public void run()
        {
          String str = new PayTask(HuiCashierActivity.this).pay(this.val$payContent);
          Message localMessage = new Message();
          localMessage.what = 100;
          localMessage.obj = str;
          HuiCashierActivity.this.handler.sendMessage(localMessage);
        }
      }).start();
      return;
    }
    catch (java.lang.Exception paramString)
    {
      Toast.makeText(this, "支付失败，请选择其他支付方式", 0).show();
    }
  }

  private void calcActivityJs()
  {
    int i = 10;
    if (this.selectedDiscount != null)
      i = this.selectedDiscount.type;
    int j = 5;
    if (this.selectedPaymentTool != null)
      j = this.selectedPaymentTool.getInt("PaymentTool");
    if (this.pay == null);
    for (String str = "0"; ; str = HuiUtils.bigDecimalToString(this.pay, 2))
    {
      str = "javascript:calcactivityJs(" + str + ", " + j + ", " + i + ")";
      Log.d("TAG_hui", str);
      this.webView.loadUrl(str);
      return;
    }
  }

  private String createActivityInfoParam()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (!this.activities.isEmpty())
    {
      localObject1 = (HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)this.activities.get(0);
      localStringBuilder.append(((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).id).append(":");
      if ((((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).levels == null) || (((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).levels.length == 0))
        localStringBuilder.append("0:0");
    }
    else
    {
      return localStringBuilder.toString();
    }
    int k = 0;
    Object localObject1 = ((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).levels;
    int m = localObject1.length;
    int i = 0;
    while (true)
    {
      int j = k;
      if (i < m)
      {
        Object localObject2 = localObject1[i];
        if (localObject2.status == 10)
        {
          localStringBuilder.append(localObject2.id).append(":").append(localObject2.cut);
          j = 1;
        }
      }
      else
      {
        if (j != 0)
          break;
        localStringBuilder.append("0:0");
        break;
      }
      i += 1;
    }
  }

  private void displayMoPayFailSimpleMsg(SimpleMsg paramSimpleMsg)
  {
    if ((paramSimpleMsg != null) && (!TextUtils.isEmpty(paramSimpleMsg.content())));
    for (String str = paramSimpleMsg.content(); (paramSimpleMsg != null) && (paramSimpleMsg.flag() == 20); str = "请求失败")
    {
      showMessageDialog(str, "重新支付", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          HuiCashierActivity.this.reloadWithInput = true;
          HuiCashierActivity.this.requestPayStrategy();
        }
      });
      return;
    }
    showToast(str);
  }

  private void displayWholeLayout(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.huiCashierLoadedLayout.setVisibility(8);
      this.huiCashierSubmitLayout.setVisibility(8);
      this.huiCashierLoadingLayout.setVisibility(0);
      return;
    }
    this.huiCashierLoadingLayout.setVisibility(8);
    this.huiCashierLoadedLayout.setVisibility(0);
    this.huiCashierSubmitLayout.setVisibility(0);
  }

  private String getActivityCut(boolean paramBoolean)
  {
    Object localObject1 = (HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)this.activities.get(0);
    if (((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).status != 10)
      return ((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).statusMsg;
    if (!paramBoolean)
      return "-¥0";
    localObject1 = ((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject1).levels;
    int j = localObject1.length;
    int i = 0;
    while (i < j)
    {
      Object localObject2 = localObject1[i];
      if (localObject2.status == 10)
        return "-¥" + localObject2.cut;
      i += 1;
    }
    return (String)"-¥0";
  }

  private String getDeductions()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.selectedDiscount == null)
      return localStringBuilder.toString();
    if (this.selectedDiscount.rules.isEmpty())
      return localStringBuilder.toString();
    Object localObject = this.selectedDiscount.getSelectedItemAndTicket();
    String str = (String)((Map)localObject).get("itemId");
    localObject = (String)((Map)localObject).get("ticketId");
    Iterator localIterator = this.selectedDiscount.rules.iterator();
    while (localIterator.hasNext())
    {
      HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule localCashierDiscountItemRule = (HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localIterator.next();
      if (!str.equals(Integer.toString(localCashierDiscountItemRule.id)))
      {
        localStringBuilder.append(localCashierDiscountItemRule.id).append(":").append("0,");
        continue;
      }
      localStringBuilder.append(str).append(":").append((String)localObject).append(",");
    }
    return (String)localStringBuilder.substring(0, localStringBuilder.length() - 1);
  }

  private int getDiscountSelectionCode(CashierDiscountItem paramCashierDiscountItem)
  {
    if (this.selectedDiscount == null)
      return -1;
    int k = -1;
    int j = k;
    int i = k;
    switch (this.selectedDiscount.type)
    {
    default:
      return -1;
    case 10:
      switch (paramCashierDiscountItem.type)
      {
      default:
        j = k;
      case 20:
      }
    case 20:
      switch (paramCashierDiscountItem.type)
      {
      default:
        i = j;
      case 10:
      case 30:
      }
    case 30:
    }
    while (true)
    {
      switch (paramCashierDiscountItem.type)
      {
      default:
        return i;
      case 20:
      }
      return 1;
      j = 0;
      break;
      i = 2;
      continue;
      i = 3;
    }
  }

  private CashierDiscountItem getDisplayedDiscount(List<CashierDiscountItem> paramList, int[] paramArrayOfInt, int paramInt, String paramString)
  {
    int j = 0;
    int i = paramInt;
    paramInt = j;
    CashierDiscountItem localCashierDiscountItem;
    if (paramInt < paramList.size())
    {
      localCashierDiscountItem = (CashierDiscountItem)paramList.get(paramInt);
      if (showDiscount(localCashierDiscountItem, paramArrayOfInt, paramString));
    }
    while (true)
    {
      paramInt += 1;
      break;
      if (i == 0)
      {
        return localCashierDiscountItem;
        return null;
      }
      i -= 1;
    }
  }

  private int getDisplayedDiscountCount(List<CashierDiscountItem> paramList, int[] paramArrayOfInt, String paramString)
  {
    int i = 0;
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      if (!showDiscount((CashierDiscountItem)paramList.next(), paramArrayOfInt, paramString))
        continue;
      i += 1;
    }
    return i;
  }

  private Object getJsCostParam(String paramString)
  {
    Object localObject;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject = paramString;
      if (!"null".equalsIgnoreCase(paramString));
    }
    else
    {
      localObject = JSONObject.NULL;
    }
    return localObject;
  }

  private boolean hasActivityInDiscount(int paramInt, int[] paramArrayOfInt, String paramString)
  {
    if ((paramArrayOfInt == null) || (TextUtils.isEmpty(paramString)));
    while (true)
    {
      return false;
      int j = paramArrayOfInt.length;
      int i = 0;
      while (i < j)
      {
        if (paramArrayOfInt[i] == paramInt)
          return true;
        i += 1;
      }
    }
  }

  private void hideFinalResultLayout()
  {
    if (TextUtils.isEmpty(this.payHint))
      this.vEditCostMarginBottom.setVisibility(8);
    while (true)
    {
      this.vFinalResultSplitLine.setVisibility(8);
      this.vPayOptionSplitLine.setVisibility(8);
      this.vFinalResult.setVisibility(8);
      return;
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      localLayoutParams.setMargins(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
      this.tvPayHint.setLayoutParams(localLayoutParams);
    }
  }

  private boolean inNoDeductionMode()
  {
    if ((TextUtils.isEmpty(this.jsFunction)) || (this.discounts.isEmpty()))
      return true;
    return (this.discounts.size() == 1) && (!showDiscount((CashierDiscountItem)this.discounts.get(0), this.activityJsShowTypes, this.activityJsFunction));
  }

  private void initView()
  {
    displayWholeLayout(false);
    setTitle(this.shopName);
    this.costView = ((EditText)findViewById(R.id.cost));
    this.costView.addTextChangedListener(this);
    this.editCostView = findViewById(R.id.edit_cost);
    this.editCostView.setOnClickListener(this);
    this.costHintView = findViewById(R.id.cost_hint);
    this.vEditCostMarginBottom = super.findViewById(R.id.edit_cost_margin_bottom);
    this.payView = ((TextView)findViewById(R.id.final_result));
    this.noDiscountInfoView = findViewById(R.id.no_discount_info);
    this.noDiscountCheckBox = ((CheckBox)findViewById(R.id.no_discount_checkbox));
    this.noDiscountImageView = ((ImageView)findViewById(R.id.no_discount_imageview));
    this.noDiscountImageView.setOnClickListener(this);
    Object localObject = this.noDiscountImageView;
    int i;
    if (TextUtils.isEmpty(this.noDiscountInputUrl))
    {
      i = 8;
      ((ImageView)localObject).setVisibility(i);
      if (TextUtils.isEmpty(this.enableNoDiscountInput))
        break label890;
      this.noDiscountInfoView.setVisibility(0);
      this.noDiscountCheckBox.setText(this.enableNoDiscountInput);
    }
    while (true)
    {
      this.editNoDiscountView = findViewById(R.id.edit_no_discount);
      this.editNoDiscountView.setOnClickListener(this);
      this.noDiscountHintView = findViewById(R.id.no_discount_hint);
      this.noDiscountView = ((EditText)findViewById(R.id.no_discount));
      this.noDiscountView.addTextChangedListener(this);
      this.yangDrawable = getResources().getDrawable(R.drawable.weixinpay_icon_rmb);
      this.yangDrawable.setBounds(0, 0, this.yangDrawable.getMinimumHeight() + 14, this.yangDrawable.getMinimumWidth() + 14);
      this.smallYangDrawable = getResources().getDrawable(R.drawable.weixinpay_icon_rmb);
      this.smallYangDrawable.setBounds(0, 0, this.smallYangDrawable.getMinimumHeight() - 9, this.smallYangDrawable.getMinimumWidth() - 9);
      this.icSmallChecked = getResources().getDrawable(R.drawable.hui_radio_btn_checked);
      this.icSmallChecked.setBounds(0, 0, this.icSmallChecked.getMinimumHeight() - 10, this.icSmallChecked.getMinimumWidth() - 10);
      this.icSmallUnchecked = getResources().getDrawable(R.drawable.hui_radio_btn_unchecked);
      this.icSmallUnchecked.setBounds(0, 0, this.icSmallUnchecked.getMinimumHeight() - 10, this.icSmallUnchecked.getMinimumWidth() - 10);
      this.noDiscountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
        {
          Log.d("TAG_hui", "checkbox check status changed, now is " + paramBoolean);
          HuiCashierActivity.access$602(HuiCashierActivity.this, paramBoolean);
          HuiCashierActivity.this.sendRuleMsg();
          if (paramBoolean)
          {
            HuiCashierActivity.this.editNoDiscountView.setVisibility(0);
            HuiCashierActivity.this.statisticsEvent("hui7", "hui7_nodiscount_on", Integer.toString(HuiCashierActivity.this.shopId), 0);
            return;
          }
          HuiCashierActivity.this.editNoDiscountView.setVisibility(8);
          HuiCashierActivity.this.statisticsEvent("hui7", "hui7_nodiscount_off", Integer.toString(HuiCashierActivity.this.shopId), 0);
        }
      });
      this.tvPayHint = ((TextView)super.findViewById(R.id.tv_pay_hint));
      ViewUtils.setVisibilityAndContent(this.tvPayHint, this.payHint);
      this.vPayOptionSplitLine = super.findViewById(R.id.pay_option_split_line);
      this.discountAdapter = new DiscountAdapter();
      this.discountsTableView = ((TableView)findViewById(R.id.discounts));
      this.discountsTableView.setDivider(new ColorDrawable(0));
      this.discountsTableView.setDividerOfGroupEnd(new ColorDrawable(0));
      this.discountsTableView.setAdapter(this.discountAdapter);
      this.discountsTableView.setOnItemClickListener(this);
      this.vFinalResultSplitLine = super.findViewById(R.id.final_result_split_line);
      this.vFinalResult = super.findViewById(R.id.layout_final_result);
      if ((TextUtils.isEmpty(this.jsFunction)) && (TextUtils.isEmpty(this.activityJsFunction)))
        hideFinalResultLayout();
      this.paymentToolAdapter = new PaymentToolAdapter();
      this.paymentToolsTableView = ((TableView)findViewById(R.id.payment_tools));
      this.paymentToolsTableView.setAdapter(this.paymentToolAdapter);
      this.paymentToolsTableView.setOnItemClickListener(this);
      if ((this.rules != null) && (!this.rules.isEmpty()))
      {
        ((TextView)super.getTitleBar().findViewById(R.id.title_bar_title)).setMaxWidth(ViewUtils.dip2px(this, 200.0F));
        localObject = (NovaTextView)LayoutInflater.from(this).inflate(R.layout.title_bar_text, null, false);
        ((NovaTextView)localObject).setPadding(0, 0, 0, 0);
        ((NovaTextView)localObject).setText("优惠说明");
        super.getTitleBar().addRightViewItem((View)localObject, "huirules", new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HuiCashierActivity.this.statisticsEvent("hui7", "hui7_cashier_rules_click", Integer.toString(HuiCashierActivity.this.shopId), 0);
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://huirules"));
            paramView.putParcelableArrayListExtra("rules", HuiCashierActivity.this.rules);
            paramView.putExtra("tip", HuiCashierActivity.this.rulesDesc);
            HuiCashierActivity.this.startActivity(paramView);
          }
        });
      }
      this.btnSubmit = ((Button)findViewById(R.id.submit));
      this.btnSubmit.setOnClickListener(this);
      this.followingBtnSubmit = ((Button)findViewById(R.id.following_submit));
      this.followingBtnSubmit.setOnClickListener(this);
      if ((this.backFromLogin) && (this.cost != null) && (this.cost.doubleValue() > 0.0D))
        this.costView.setText(HuiUtils.bigDecimalToString(this.cost, 2));
      if ((this.cost != null) && (this.cost.doubleValue() > 0.0D) && (this.thirdPartyOrderType.intValue() == 30))
      {
        this.costView.setText(HuiUtils.bigDecimalToString(this.cost));
        this.costView.setEnabled(false);
        this.editCostView.setEnabled(false);
        this.costView.setTextColor(getResources().getColor(R.color.light_gray));
      }
      return;
      i = 0;
      break;
      label890: this.noDiscountInfoView.setVisibility(8);
    }
  }

  private void injectJsFunction()
  {
    this.webView = new WebView(this);
    this.webView.getSettings().setJavaScriptEnabled(true);
    this.webView.setWebViewClient(new WebViewClient()
    {
      public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
      {
        boolean bool = true;
        if (paramString.startsWith("jsonresult:"))
        {
          try
          {
            paramWebView = URLDecoder.decode(paramString, "utf-8");
            paramWebView = paramWebView.substring("jsonresult:".length(), paramWebView.length());
            Log.d("TAG_hui", "Json Result : " + paramWebView);
            paramWebView = new JSONObject(paramWebView);
            HuiCashierActivity.this.parseJson(paramWebView);
            if (HuiCashierActivity.this.ruleJsKey == null)
              break label407;
            HuiCashierActivity.this.webView.loadUrl(HuiCashierActivity.this.ruleJsKey);
            HuiCashierActivity.access$1102(HuiCashierActivity.this, null);
            return true;
          }
          catch (java.io.UnsupportedEncodingException paramWebView)
          {
            Log.e("TAG_hui", "url=" + paramString, paramWebView);
            throw new RuntimeException("url=" + paramString, paramWebView);
          }
          catch (JSONException paramWebView)
          {
            Log.e("TAG_hui", "url=" + paramString, paramWebView);
            throw new RuntimeException("wrong json. url=" + paramString, paramWebView);
          }
        }
        else
        {
          if (paramString.startsWith("activityjsonresult:"))
            try
            {
              paramWebView = URLDecoder.decode(paramString, "utf-8");
              paramWebView = paramWebView.substring("activityjsonresult:".length(), paramWebView.length());
              Log.d("TAG_hui", "Json Result : " + paramWebView);
              paramWebView = new JSONObject(paramWebView);
              HuiCashierActivity.this.parseActivityJson(paramWebView);
              return true;
            }
            catch (JSONException paramWebView)
            {
              Log.e("TAG_hui", "url=" + paramString, paramWebView);
              throw new RuntimeException("wrong json. url=" + paramString, paramWebView);
            }
            catch (java.io.UnsupportedEncodingException paramWebView)
            {
              Log.e("TAG_hui", "url=" + paramString, paramWebView);
              throw new RuntimeException("url=" + paramString, paramWebView);
            }
          bool = super.shouldOverrideUrlLoading(paramWebView, paramString);
        }
        label407: return bool;
      }
    });
    Object localObject = this.jsFunction;
    localObject = "javascript:" + (String)localObject;
    localObject = (String)localObject + " function ruleJs (discountType, payAmount, payType, jsonParam) {var result = rule(discountType, payAmount, payType, jsonParam); window.location.href = 'jsonresult:' + encodeURIComponent(result);}";
    String str = (String)localObject + " function selectJs (discountType, offerId, levelOfferId, amount, jsonParam) {var result = select(discountType, offerId, levelOfferId, amount, jsonParam); window.location.href = 'jsonresult:' + encodeURIComponent(result);}";
    localObject = str;
    if (!TextUtils.isEmpty(this.activityJsFunction))
    {
      localObject = str + " " + this.activityJsFunction;
      localObject = (String)localObject + " function calcactivityJs (payAmount, payType, discountType) {var result = calculate(payAmount, payType, discountType); window.location.href = 'activityjsonresult:' + encodeURIComponent(result);}";
    }
    this.webView.loadUrl((String)localObject);
    Log.d("TAG_hui", "JS injected: " + (String)localObject);
  }

  private HuiCashierActivity.CashierDiscountItem.BookingMealTicket parseBookingMealTicket(JSONObject paramJSONObject)
    throws JSONException
  {
    int i = paramJSONObject.getInt("uid");
    boolean bool;
    String str3;
    String str4;
    String str1;
    if (paramJSONObject.getInt("status") == 10)
    {
      bool = true;
      str3 = paramJSONObject.getString("cut");
      str4 = paramJSONObject.getString("title");
      if (!paramJSONObject.has("subTitle"))
        break label119;
      str1 = paramJSONObject.getString("subTitle");
      label60: if (!paramJSONObject.has("rule"))
        break label126;
    }
    label119: label126: for (String str2 = paramJSONObject.getString("rule"); ; str2 = "")
    {
      return new HuiCashierActivity.CashierDiscountItem.BookingMealTicket(String.valueOf(i), bool, str3, str4, str1, str2, paramJSONObject.getString("desc"), paramJSONObject.getBoolean("selected"));
      bool = false;
      break;
      str1 = "";
      break label60;
    }
  }

  private List<HuiCashierActivity.CashierDiscountItem.BookingMealTicket> parseBookingMealTickets(JSONArray paramJSONArray)
    throws JSONException
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < paramJSONArray.length())
    {
      localArrayList.add(parseBookingMealTicket(paramJSONArray.getJSONObject(i)));
      i += 1;
    }
    return localArrayList;
  }

  private HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule parseCashierDiscountItem(JSONObject paramJSONObject)
    throws JSONException
  {
    boolean bool3 = true;
    boolean bool4 = paramJSONObject.has("levelOffers");
    int i = paramJSONObject.getInt("uid");
    String str2 = paramJSONObject.getString("title");
    String str3 = paramJSONObject.getString("desc");
    String str1;
    String str4;
    boolean bool5;
    boolean bool1;
    label89: boolean bool2;
    label112: boolean bool6;
    if (paramJSONObject.has("discountTip"))
    {
      str1 = paramJSONObject.getString("discountTip");
      str4 = paramJSONObject.getString("deduction");
      bool5 = paramJSONObject.getBoolean("showDeduction");
      if (paramJSONObject.getInt("status") != 10)
        break label192;
      bool1 = true;
      if ((!paramJSONObject.has("needLogin")) || (!paramJSONObject.getBoolean("needLogin")))
        break label198;
      bool2 = true;
      if ((!paramJSONObject.has("deductionHighlight")) || (!paramJSONObject.getBoolean("deductionHighlight")))
        break label204;
      label132: bool6 = paramJSONObject.getBoolean("showStatus");
      if (!bool4)
        break label210;
    }
    label192: label198: label204: label210: for (paramJSONObject = parseBookingMealTickets(paramJSONObject.getJSONArray("levelOffers")); ; paramJSONObject = Collections.EMPTY_LIST)
    {
      return new HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule(i, str2, str3, str1, str4, bool5, bool1, bool2, bool3, bool6, paramJSONObject);
      str1 = "";
      break;
      bool1 = false;
      break label89;
      bool2 = false;
      break label112;
      bool3 = false;
      break label132;
    }
  }

  private List<HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule> parseCashierDiscountItems(JSONArray paramJSONArray)
    throws JSONException
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < paramJSONArray.length())
    {
      JSONObject localJSONObject = paramJSONArray.getJSONObject(i);
      if (localJSONObject.getInt("status") == 10)
        localArrayList.add(parseCashierDiscountItem(localJSONObject));
      i += 1;
    }
    return localArrayList;
  }

  private CashierDiscountItem parseDiscount(JSONObject paramJSONObject)
    throws JSONException
  {
    boolean bool2 = paramJSONObject.has("offers");
    String str = paramJSONObject.getString("title");
    int i = paramJSONObject.getInt("type");
    boolean bool1;
    if (paramJSONObject.getInt("status") == 10)
    {
      bool1 = true;
      if (!bool2)
        break label76;
    }
    label76: for (paramJSONObject = parseCashierDiscountItems(paramJSONObject.getJSONArray("offers")); ; paramJSONObject = Collections.EMPTY_LIST)
    {
      return new CashierDiscountItem(str, i, bool1, paramJSONObject);
      bool1 = false;
      break;
    }
  }

  private void parseDiscounts(JSONArray paramJSONArray)
    throws JSONException
  {
    if (this.discounts == null)
      this.discounts = new ArrayList();
    while (true)
    {
      int i = 0;
      while (i < paramJSONArray.length())
      {
        this.discounts.add(parseDiscount(paramJSONArray.getJSONObject(i)));
        i += 1;
      }
      this.discounts.clear();
    }
    if (this.discounts.size() == 1)
    {
      ((CashierDiscountItem)this.discounts.get(0)).selected = true;
      this.selectedDiscount = ((CashierDiscountItem)this.discounts.get(0));
    }
  }

  private void requestLocation()
  {
    if (this.payLocationReq != null)
      mapiService().abort(this.payLocationReq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/ordercheck.hui?").buildUpon();
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localBuilder.appendQueryParameter("token", str);
    localBuilder.appendQueryParameter("lat", this.latitude);
    localBuilder.appendQueryParameter("lng", this.longitude);
    localBuilder.appendQueryParameter("shopid", Integer.toString(this.shopId));
    localBuilder.appendQueryParameter("cityid", this.cityId);
    this.payLocationReq = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.DISABLED, false, null, 2500L);
    mapiService().exec(this.payLocationReq, this);
  }

  private void requestPay()
  {
    if (this.payReq != null)
      mapiService().abort(this.payReq, this, true);
    ArrayList localArrayList = new ArrayList();
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
    {
      localArrayList.add("token");
      localArrayList.add(str);
    }
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    localArrayList.add("shopid");
    localArrayList.add(Integer.toString(this.shopId));
    localArrayList.add("cityid");
    localArrayList.add(Integer.toString(cityId()));
    localArrayList.add("amount");
    localArrayList.add(HuiUtils.bigDecimalToString(this.cost, 2));
    localArrayList.add("type");
    localArrayList.add(Integer.toString(this.selectedPaymentTool.getInt("Type")));
    localArrayList.add("callid");
    localArrayList.add(UUID.randomUUID().toString());
    localArrayList.add("isvalid");
    localArrayList.add("true");
    int i;
    if (this.selectedDiscount == null)
    {
      i = 0;
      localArrayList.add("ordertype");
      localArrayList.add(Integer.toString(i));
      localArrayList.add("orderid");
      if (this.orderId != null)
        break label570;
      str = "";
      label244: localArrayList.add(str);
      localArrayList.add("orderbiztype");
      localArrayList.add(Integer.toString(this.orderBizType));
      localArrayList.add("thirdpartyorderid");
      if (this.thirdPartyOrderId != null)
        break label578;
      str = "";
      label289: localArrayList.add(str);
      localArrayList.add("thirdpartyordertype");
      localArrayList.add(Integer.toString(this.thirdPartyOrderType.intValue()));
      localArrayList.add("deductions");
      localArrayList.add(getDeductions());
      localArrayList.add("calculayedamount");
      localArrayList.add(HuiUtils.bigDecimalToString(this.pay));
      localArrayList.add("usermobile");
      localArrayList.add(this.userMobile);
      if (!TextUtils.isEmpty(this.latitude))
      {
        localArrayList.add("lat");
        localArrayList.add(this.latitude);
      }
      if (!TextUtils.isEmpty(this.longitude))
      {
        localArrayList.add("lng");
        localArrayList.add(this.longitude);
      }
      if (!this.activities.isEmpty())
      {
        localArrayList.add("activities");
        if (!hasActivityInDiscount(i, this.activityJsShowTypes, this.activityJsFunction))
          break label586;
        str = createActivityInfoParam();
        label467: localArrayList.add(str);
      }
      localArrayList.add("cx");
      localArrayList.add(DeviceUtils.cxInfo("hui"));
      localArrayList.add("nodiscountamount");
      if (!this.isNoDiscountChecked)
        break label593;
    }
    label570: label578: label586: label593: for (str = this.noDiscountAmount.toString(); ; str = "0")
    {
      localArrayList.add(str);
      this.payReq = BasicMApiRequest.mapiPost("http://hui.api.dianping.com/mopay.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      mapiService().exec(this.payReq, this);
      return;
      i = this.selectedDiscount.type;
      break;
      str = this.orderId;
      break label244;
      str = this.thirdPartyOrderId;
      break label289;
      str = "";
      break label467;
    }
  }

  private void requestPayStrategy()
  {
    if (this.payStrategyReq != null)
      mapiService().abort(this.payStrategyReq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/getpaystrategys.hui?").buildUpon();
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localBuilder.appendQueryParameter("token", str);
    localBuilder.appendQueryParameter("clientuuid", Environment.uuid());
    localBuilder.appendQueryParameter("shopid", Integer.toString(this.shopId));
    localBuilder.appendQueryParameter("cityid", Integer.toString(cityId()));
    if (this.orderId == null)
    {
      str = "";
      localBuilder.appendQueryParameter("orderid", str);
      localBuilder.appendQueryParameter("orderbiztype", Integer.toString(this.orderBizType));
      if (this.thirdPartyOrderId != null)
        break label208;
    }
    label208: for (str = ""; ; str = this.thirdPartyOrderId)
    {
      localBuilder.appendQueryParameter("thirdpartyorderid", str);
      localBuilder.appendQueryParameter("thirdpartyordertype", Integer.toString(this.thirdPartyOrderType.intValue()));
      this.payStrategyReq = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.payStrategyReq, this);
      return;
      str = this.orderId;
      break;
    }
  }

  private void ruleJs(int paramInt1, String paramString1, int paramInt2, String paramString2)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("huiType", Integer.toString(paramInt1));
      localJSONObject.put("amount", getJsCostParam(paramString1));
      localJSONObject.put("channel", Integer.toString(paramInt2));
      localJSONObject.put("noDiscountAmount", getJsCostParam(paramString2));
      paramString1 = "javascript:ruleJs(" + paramInt1 + ", " + paramString1 + ", " + paramInt2 + ", " + localJSONObject.toString() + ")";
      Log.d("TAG_hui", paramString1);
      if (this.ruleJsKey == null)
        this.webView.loadUrl(paramString1);
      this.ruleJsKey = paramString1;
      return;
    }
    catch (JSONException paramString2)
    {
      while (true)
        Log.w("TAG_hui", "wrong json in ruleJs(" + paramInt1 + "," + paramString1 + "," + paramInt2 + ")", paramString2);
    }
  }

  private void selectJs(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("huiType", Integer.toString(paramInt));
      localJSONObject.put("amount", getJsCostParam(paramString3));
      localJSONObject.put("offerId", paramString1);
      localJSONObject.put("levelOfferId", paramString2);
      localJSONObject.put("noDiscountAmount", getJsCostParam(paramString4));
      paramString1 = "javascript:selectJs(" + paramInt + ", " + paramString1 + ", " + paramString2 + ", " + paramString3 + ", " + localJSONObject.toString() + ")";
      Log.d("TAG_hui", paramString1);
      this.webView.loadUrl(paramString1);
      return;
    }
    catch (JSONException paramString4)
    {
      while (true)
        Log.w("TAG_hui", "wrong json in ruleJs(" + paramInt + "," + paramString1 + "," + paramString2 + "," + paramString3 + ")", paramString4);
    }
  }

  private void sendRuleMsg()
  {
    this.handler.removeMessages(2);
    this.handler.sendEmptyMessage(2);
  }

  private void sendUpdatePayMsg()
  {
    this.handler.removeMessages(1);
    this.handler.sendEmptyMessage(1);
  }

  private boolean showDiscount(CashierDiscountItem paramCashierDiscountItem, int[] paramArrayOfInt, String paramString)
  {
    if (hasActivityInDiscount(paramCashierDiscountItem.type, paramArrayOfInt, paramString))
      return true;
    paramCashierDiscountItem = paramCashierDiscountItem.rules;
    if ((paramCashierDiscountItem != null) && (!paramCashierDiscountItem.isEmpty()))
    {
      paramCashierDiscountItem = paramCashierDiscountItem.iterator();
      while (paramCashierDiscountItem.hasNext())
        if ((((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)paramCashierDiscountItem.next()).flag & 0x1) > 0)
          return true;
    }
    return false;
  }

  private boolean wxPay(String paramString)
  {
    while (true)
    {
      int i;
      try
      {
        if (!TextUtils.isEmpty(paramString))
          continue;
        showToast("获取订单号失败");
        return false;
        if (!WXHelper.isSupportPay(this))
          return false;
        HashMap localHashMap = new HashMap();
        paramString = paramString.split("&");
        int j = paramString.length;
        i = 0;
        if (i >= j)
          continue;
        Object localObject = paramString[i];
        int k = localObject.indexOf("=");
        if (k > 0)
        {
          localHashMap.put(localObject.substring(0, k), localObject.substring(k + 1));
          break label225;
          paramString = new PayReq();
          paramString.appId = "wx8e251222d6836a60";
          paramString.partnerId = ((String)localHashMap.get("partnerid"));
          paramString.prepayId = ((String)localHashMap.get("prepayid"));
          paramString.nonceStr = ((String)localHashMap.get("noncestr"));
          paramString.timeStamp = ((String)localHashMap.get("timestamp"));
          paramString.packageValue = ((String)localHashMap.get("package"));
          paramString.sign = ((String)localHashMap.get("sign"));
          boolean bool = WXHelper.getWXAPI(this).sendReq(paramString);
          return bool;
        }
      }
      catch (java.lang.Exception paramString)
      {
        Toast.makeText(this, "支付失败，请选择其他支付方式", 0).show();
        return false;
      }
      label225: i += 1;
    }
  }

  public void afterTextChanged(Editable paramEditable)
  {
    String str2 = paramEditable.toString();
    String str1 = str2;
    if (str2.equals("."))
      paramEditable.delete(0, 1);
    for (str1 = paramEditable.toString(); str1.startsWith("00"); str1 = paramEditable.toString())
      paramEditable.delete(0, 1);
    if ((str1.length() > 1) && (str1.charAt(0) == '0') && (str1.charAt(1) != '.'))
      paramEditable.delete(0, 1);
    str1 = paramEditable.toString();
    int i = str1.indexOf(".");
    if ((i > 0) && (i + 3 < paramEditable.length()))
      paramEditable.delete(i + 3, paramEditable.length());
    if (!TextUtils.isEmpty(this.costView.getText()))
    {
      this.costNull = false;
      this.cost = new BigDecimal(this.costView.getText().toString());
      this.costHintView.setVisibility(8);
      this.costView.setCompoundDrawables(this.yangDrawable, null, null, null);
      if (TextUtils.isEmpty(this.noDiscountView.getText()))
        break label334;
      this.noDiscountAmount = new BigDecimal(this.noDiscountView.getText().toString());
      this.noDiscountHintView.setVisibility(8);
      this.noDiscountView.setCompoundDrawables(this.smallYangDrawable, null, null, null);
    }
    while (true)
    {
      Log.d("TAG_hui", "user input : " + str1);
      sendRuleMsg();
      return;
      this.costNull = true;
      this.cost = new BigDecimal("0.0");
      this.costHintView.setVisibility(0);
      this.costView.setCompoundDrawables(null, null, null, null);
      break;
      label334: this.noDiscountAmount = new BigDecimal("0.0");
      this.noDiscountHintView.setVisibility(0);
      this.noDiscountView.setCompoundDrawables(null, null, null, null);
    }
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  View createDiscountView(CashierDiscountItem paramCashierDiscountItem)
  {
    View localView1 = getLayoutInflater().inflate(R.layout.hui_discount_item, null);
    Object localObject2 = (TextView)localView1.findViewById(R.id.title);
    View localView2 = localView1.findViewById(R.id.hui_rules_split_line);
    Object localObject1;
    label101: Object localObject3;
    Object localObject4;
    TextView localTextView1;
    TextView localTextView2;
    Object localObject5;
    TextView localTextView3;
    ImageView localImageView;
    TextView localTextView4;
    if (TextUtils.isEmpty(paramCashierDiscountItem.title))
    {
      ((TextView)localObject2).setVisibility(8);
      localView2.setVisibility(8);
      if (this.discounts.size() != 1)
        break label479;
      ((TextView)localObject2).setCompoundDrawables(null, null, null, null);
      localObject1 = (LinearLayout)localView1.findViewById(R.id.rules);
      localObject2 = paramCashierDiscountItem.rules.iterator();
      if (!((Iterator)localObject2).hasNext())
        break label608;
      localObject3 = (HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)((Iterator)localObject2).next();
      localObject4 = getLayoutInflater().inflate(R.layout.hui_discount_item_rule, null);
      localTextView1 = (TextView)((View)localObject4).findViewById(R.id.title);
      localTextView1.setText(((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).content);
      localTextView2 = (TextView)((View)localObject4).findViewById(R.id.desc);
      localTextView2.setText(((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).remark);
      localObject5 = (TextView)((View)localObject4).findViewById(R.id.discount_tip);
      ViewUtils.setVisibilityAndContent((TextView)localObject5, ((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).discountTip);
      localTextView3 = (TextView)((View)localObject4).findViewById(R.id.deduction);
      localImageView = (ImageView)((View)localObject4).findViewById(R.id.right_arrow);
      localTextView4 = (TextView)((View)localObject4).findViewById(R.id.need_login);
      if ((!paramCashierDiscountItem.selected) || ((((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).flag & 0x4) == 0))
      {
        localTextView1.setTextColor(getResources().getColor(R.color.hui_text_unselected));
        localTextView2.setTextColor(getResources().getColor(R.color.hui_text_unselected));
        ((TextView)localObject5).setTextColor(getResources().getColor(R.color.hui_text_unselected));
        localTextView3.setTextColor(getResources().getColor(R.color.hui_text_unselected));
      }
      if ((((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).flag & 0x2) <= 0)
        break label581;
      localTextView3.setVisibility(0);
      localTextView3.setText("-¥" + ((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).discountAmount.toPlainString());
      if (((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).discountAmount.doubleValue() != 0.0D)
        break label511;
      localTextView3.setTextColor(getResources().getColor(R.color.hui_text_unselected));
      label397: if (!((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).bookingMealTickets.isEmpty())
        break label547;
      localImageView.setVisibility(8);
      label417: if (((paramCashierDiscountItem.type != 10) && (paramCashierDiscountItem.type != 30)) || ((((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).flag & 0x8) <= 0))
        break label598;
      localTextView4.setVisibility(0);
      localTextView4.setOnClickListener(this);
    }
    while (true)
    {
      ((LinearLayout)localObject1).addView((View)localObject4);
      break label101;
      ((TextView)localObject2).setText(paramCashierDiscountItem.title);
      break;
      label479: if (paramCashierDiscountItem.selected);
      for (localObject1 = this.icSmallChecked; ; localObject1 = this.icSmallUnchecked)
      {
        ((TextView)localObject2).setCompoundDrawables((Drawable)localObject1, null, null, null);
        break;
      }
      label511: if ((!paramCashierDiscountItem.selected) || ((((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3).flag & 0x10) != 0))
        break label397;
      localTextView3.setTextColor(getResources().getColor(R.color.hui_text_unselected));
      break label397;
      label547: localImageView.setVisibility(0);
      ((View)localObject4).setOnClickListener(new View.OnClickListener((HuiCashierActivity.CashierDiscountItem.CashierDiscountItemRule)localObject3, paramCashierDiscountItem)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://huicashierbookingtickets"));
          paramView.putExtra("discountitemid", String.valueOf(this.val$item.id));
          paramView.putExtra("selectedid", String.valueOf(this.val$discount.getSelectedItemAndTicket().get("ticketId")));
          paramView.putExtra("type", this.val$discount.type);
          paramView.putParcelableArrayListExtra("options", this.val$item.getTicketsDPObjectList());
          HuiCashierActivity.this.startActivityForResult(paramView, 1);
        }
      });
      ((View)localObject4).setClickable(paramCashierDiscountItem.selected);
      break label417;
      label581: localTextView3.setVisibility(8);
      localImageView.setVisibility(8);
      break label417;
      label598: localTextView4.setVisibility(8);
    }
    label608: if ((hasActivityInDiscount(paramCashierDiscountItem.type, this.activityJsShowTypes, this.activityJsFunction)) && (!this.activities.isEmpty()))
    {
      localObject2 = (HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)this.activities.get(0);
      localObject3 = getLayoutInflater().inflate(R.layout.hui_discount_item_rule, null);
      localObject4 = (TextView)((View)localObject3).findViewById(R.id.title);
      ((TextView)localObject4).setText(((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject2).title);
      localTextView1 = (TextView)((View)localObject3).findViewById(R.id.discount_tip);
      ViewUtils.setVisibilityAndContent(localTextView1, ((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject2).desc);
      localTextView2 = (TextView)((View)localObject3).findViewById(R.id.deduction);
      localObject5 = getActivityCut(paramCashierDiscountItem.selected);
      localTextView2.setText((CharSequence)localObject5);
      if (paramCashierDiscountItem.selected)
        break label888;
      ((TextView)localObject4).setTextColor(getResources().getColor(R.color.hui_text_unselected));
      localTextView1.setTextColor(getResources().getColor(R.color.hui_text_unselected));
      localTextView2.setTextColor(getResources().getColor(R.color.hui_text_unselected));
    }
    while (true)
    {
      ((LinearLayout)localObject1).addView((View)localObject3);
      if (this.discounts.size() == 1)
      {
        paramCashierDiscountItem = new LinearLayout.LayoutParams(-1, -2);
        paramCashierDiscountItem.setMargins(ViewUtils.dip2px(this, 15.0F), 0, 0, 0);
        localView2.setLayoutParams(paramCashierDiscountItem);
        paramCashierDiscountItem = new LinearLayout.LayoutParams(-1, -2);
        paramCashierDiscountItem.setMargins(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 17.0F), 0);
        ((LinearLayout)localObject1).setLayoutParams(paramCashierDiscountItem);
      }
      localView1.setClickable(true);
      return localView1;
      label888: if ((!"-¥0".equalsIgnoreCase((String)localObject5)) && (((HuiCashierActivity.CashierDiscountItem.HuiActivityInfo)localObject2).status == 10))
        continue;
      localTextView2.setTextColor(getResources().getColor(R.color.hui_text_unselected));
    }
  }

  public void dismissDialog()
  {
    if (this.isDestroyed);
    do
      return;
    while (this.managedDialog == null);
    if (this.managedDialog.isShowing())
      this.managedDialog.dismiss();
    this.managedDialog = null;
    this.childCancelListener = null;
  }

  void gotoPayResult()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mopayresult"));
    localIntent.putExtra("huiorderid", Integer.toString(this.huiOrderId));
    localIntent.putExtra("payorderid", this.payOrderId);
    localIntent.putExtra("orderid", this.orderId);
    localIntent.putExtra("orderbiztype", this.orderBizType);
    localIntent.putExtra("thirdpartyorderid", this.thirdPartyOrderId);
    localIntent.putExtra("thirdpartyordertype", this.thirdPartyOrderType);
    localIntent.putExtra("shopid", this.shopId);
    int i;
    if (this.source == 0)
    {
      i = 10;
      localIntent.putExtra("source", i);
      if (this.selectedDiscount != null)
        break label177;
    }
    label177: for (Object localObject = "10"; ; localObject = Integer.valueOf(this.selectedDiscount.type))
    {
      localIntent.putExtra("biztype", (Serializable)localObject);
      localIntent.putExtra("serializedid", this.serializedId);
      localIntent.setFlags(67108864);
      startActivity(localIntent);
      return;
      i = this.source;
      break;
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    String str1;
    if ((paramInt1 == 1) && (paramInt2 == -1) && (paramIntent != null))
    {
      if (this.cost.doubleValue() != 0.0D)
        break label92;
      str1 = "null";
      if ((this.isNoDiscountChecked) && (this.noDiscountAmount != null))
        break label104;
    }
    label92: label104: for (String str2 = "0"; ; str2 = HuiUtils.bigDecimalToString(this.noDiscountAmount))
    {
      paramInt1 = 10;
      if (this.selectedDiscount != null)
        paramInt1 = this.selectedDiscount.type;
      selectJs(paramInt1, paramIntent.getStringExtra("discountitemid"), paramIntent.getStringExtra("ticketid"), str1, str2);
      return;
      str1 = HuiUtils.bigDecimalToString(this.cost);
      break;
    }
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    if (this.payReq != null)
    {
      mapiService().abort(this.payReq, this, true);
      this.payReq = null;
    }
  }

  public void onClick(View paramView)
  {
    if (paramView == this.editCostView)
    {
      this.costView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 0.0F, 0.0F, 0));
      this.costView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, 0.0F, 0.0F, 0));
    }
    label244: label255: 
    do
    {
      return;
      if (paramView == this.editNoDiscountView)
      {
        this.noDiscountView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 0.0F, 0.0F, 0));
        this.noDiscountView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, 0.0F, 0.0F, 0));
        return;
      }
      if (paramView == this.noDiscountImageView)
      {
        statisticsEvent("hui7", "hui7_nodiscount_help", Integer.toString(this.shopId), 0);
        startActivity("dianping://web?url=" + Uri.encode(this.noDiscountInputUrl));
        return;
      }
      if ((paramView != this.btnSubmit) && (paramView != this.followingBtnSubmit))
        continue;
      if (paramView == this.btnSubmit)
      {
        paramView = "bottom";
        if (this.selectedDiscount != null)
          break label244;
      }
      for (int i = 30; ; i = this.selectedDiscount.type)
      {
        statisticsEvent("hui7", "hui7_cashier_paybutton_click", paramView, i);
        KeyboardUtils.hideKeyboard(this.costView);
        if (!TextUtils.isEmpty(this.costView.getText()))
          break label255;
        showToast("请输入支付金额");
        return;
        paramView = "following";
        break;
      }
      if (this.minAmount.compareTo(new BigDecimal(this.costView.getText().toString())) > 0)
      {
        showToast("输入金额不可低于" + HuiUtils.bigDecimalToString(this.minAmount, 2) + "元");
        return;
      }
      if (MAX_AMOUNT.compareTo(new BigDecimal(this.costView.getText().toString())) < 0)
      {
        showToast("可支付最大金额为" + HuiUtils.bigDecimalToString(MAX_AMOUNT, 0) + "元");
        return;
      }
      paramView = locationService().location();
      if (paramView == null)
      {
        this.latitude = null;
        this.longitude = null;
        showProgressDialog("载入中...", this);
        requestPay();
        this.btnSubmit.setClickable(false);
        this.followingBtnSubmit.setClickable(false);
        return;
      }
      this.latitude = Location.FMT.format(paramView.getDouble("Lat"));
      this.longitude = Location.FMT.format(paramView.getDouble("Lng"));
      this.cityId = String.valueOf(paramView.getObject("City").getInt("ID"));
      requestLocation();
      return;
    }
    while (paramView.getId() != R.id.need_login);
    statisticsEvent("hui7", "hui7_cashier_viplogin", Integer.toString(this.shopId), 0);
    accountService().login(this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.shopName = paramBundle.getString("shopname");
      this.shopId = paramBundle.getInt("shopid");
      this.orderId = paramBundle.getString("orderid");
      this.orderBizType = paramBundle.getInt("orderbiztype");
      this.thirdPartyOrderId = paramBundle.getString("thirdpartyorderid");
      this.thirdPartyOrderType = Integer.valueOf(paramBundle.getInt("thirdpartyordertype"));
      this.source = paramBundle.getInt("source");
      if (TextUtils.isEmpty(paramBundle.getString("totalAmount")));
      for (Object localObject = "0.0"; ; localObject = paramBundle.getString("totalAmount"))
      {
        this.cost = new BigDecimal((String)localObject);
        this.serializedId = paramBundle.getString("serializedid");
        this.userMobile = paramBundle.getString("usermobile");
        super.setTitle(this.shopName);
        this.paymentTools = new ArrayList();
        paramBundle = new DPObject("PaymentTool").edit().putString("Title", "微信支付").putString("SubTitle", "推荐已安装微信的用户使用").putString("ID", "11:1:null").putInt("Type", 1).putInt("PaymentTool", 11).generate();
        localObject = new DPObject("PaymentTool").edit().putString("Title", "支付宝支付").putInt("PaymentTool", 5).putString("SubTitle", "推荐支付宝用户使用").putString("ID", "5:1:null").putInt("Type", 2).generate();
        this.paymentTools.add(paramBundle);
        this.paymentTools.add(localObject);
        this.selectedPaymentTool = paramBundle;
        super.setContentView(R.layout.hui_cashier_activity);
        super.getWindow().setBackgroundDrawable(null);
        this.huiCashierLoadingLayout = super.findViewById(R.id.hui_cashier_loading_layout);
        this.huiCashierLoadedLayout = super.findViewById(R.id.hui_cashier_loaded_layout);
        this.huiCashierSubmitLayout = super.findViewById(R.id.submit_layout);
        displayWholeLayout(true);
        requestPayStrategy();
        this.followingHuiCashierSubmitLayout = super.findViewById(R.id.following_submit_layout);
        this.followingHuiCashierSubmitLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            if (HuiCashierActivity.this.followingHuiCashierSubmitLayout.getTop() < HuiCashierActivity.this.huiCashierSubmitLayout.getTop())
            {
              HuiCashierActivity.this.followingHuiCashierSubmitLayout.setVisibility(0);
              HuiCashierActivity.this.huiCashierSubmitLayout.setVisibility(8);
              return;
            }
            HuiCashierActivity.this.followingHuiCashierSubmitLayout.setVisibility(4);
            HuiCashierActivity.this.huiCashierSubmitLayout.setVisibility(0);
          }
        });
        super.registerReceiver(this.wxpayReceiver, new IntentFilter("com.dianping.base.thirdparty.wxapi.WXPAY"));
        return;
      }
    }
    this.shopName = getStringParam("shopname");
    this.shopId = getIntParam("shopid");
    this.source = getIntParam("source");
    label494: int i;
    if (TextUtils.isEmpty(getStringParam("amount")))
    {
      paramBundle = "0.0";
      this.cost = new BigDecimal(paramBundle);
      i = getIntParam("channel");
      if (i != 20)
        break label581;
      this.orderBizType = 20;
      label526: if (i == 30)
        break label610;
    }
    label581: label610: for (paramBundle = getStringParam("orderid"); ; paramBundle = "")
    {
      this.orderId = paramBundle;
      this.serializedId = getStringParam("serializedid");
      this.userMobile = getStringParam("usermobile");
      break;
      paramBundle = getStringParam("amount");
      break label494;
      if (i != 30)
        break label526;
      this.thirdPartyOrderId = getStringParam("orderid");
      this.thirdPartyOrderType = Integer.valueOf(30);
      break label526;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.payStrategyReq != null)
    {
      mapiService().abort(this.payStrategyReq, this, true);
      this.payStrategyReq = null;
    }
    if (this.payReq != null)
    {
      mapiService().abort(this.payReq, this, true);
      this.payReq = null;
    }
    if (this.payLocationReq != null)
    {
      mapiService().abort(this.payLocationReq, this, true);
      this.payLocationReq = null;
    }
    unregisterReceiver(this.wxpayReceiver);
    this.handler.removeMessages(100);
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    if (paramTableView.getId() == R.id.payment_tools)
    {
      paramTableView = (DPObject)this.paymentTools.get(paramInt);
      if (this.selectedPaymentTool != paramTableView)
      {
        this.selectedPaymentTool = paramTableView;
        ruleJs();
      }
    }
    do
    {
      do
        return;
      while (paramTableView.getId() != R.id.discounts);
      paramTableView = (CashierDiscountItem)this.discounts.get(paramInt);
    }
    while (this.selectedDiscount == paramTableView);
    statisticsEvent("hui7", "hui7_cashier_couponselection_click", Integer.toString(this.shopId), getDiscountSelectionCode(paramTableView));
    if ((paramTableView.type != 10) && (this.isNoDiscountChecked))
      this.noDiscountCheckBox.setChecked(false);
    while (true)
    {
      this.selectedDiscount = paramTableView;
      ruleJs();
      return;
      if (paramTableView.type != 10)
        continue;
      if ((this.noDiscountView.getText() != null) && (!TextUtils.isEmpty(this.noDiscountView.getText().toString())))
      {
        this.noDiscountCheckBox.setChecked(true);
        continue;
      }
      this.noDiscountCheckBox.setChecked(false);
    }
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
      if (localInputMethodManager.isActive())
        localInputMethodManager.toggleSoftInput(1, 2);
      displayWholeLayout(true);
      this.backFromLogin = true;
      requestPayStrategy();
    }
    return super.onLogin(paramBoolean);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.payStrategyReq)
    {
      this.payStrategyReq = null;
      int i = this.retry - 1;
      this.retry = i;
      if (i > 0)
        requestPayStrategy();
    }
    do
    {
      return;
      if (paramMApiRequest != this.payReq)
        continue;
      this.payReq = null;
      dismissDialog();
      this.btnSubmit.setClickable(true);
      this.followingBtnSubmit.setClickable(true);
      displayMoPayFailSimpleMsg(paramMApiResponse.message());
      return;
    }
    while (paramMApiRequest != this.payLocationReq);
    this.payLocationReq = null;
    showProgressDialog("载入中...", this);
    requestPay();
    this.btnSubmit.setClickable(false);
    this.followingBtnSubmit.setClickable(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.payStrategyReq)
    {
      this.payStrategyReq = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.orderId = Integer.toString(paramMApiRequest.getInt("OrderID"));
        this.orderBizType = paramMApiRequest.getInt("OrderBizType");
        this.thirdPartyOrderId = paramMApiRequest.getString("ThirdPartyOrderId");
        this.thirdPartyOrderType = Integer.valueOf(paramMApiRequest.getInt("ThirdPartyOrderType"));
        this.jsFunction = paramMApiRequest.getString("JSFunction");
        this.activityJsFunction = paramMApiRequest.getString("ActivityJSFunction");
        Log.d("TAG_hui", "jsFunction = " + this.jsFunction);
        Log.d("TAG_hui", "activityJsFunction = " + this.activityJsFunction);
        injectJsFunction();
        if ((!this.backFromLogin) && ((this.cost == null) || (this.cost.doubleValue() <= 0.0D) || (this.thirdPartyOrderType.intValue() != 30)) && (!this.reloadWithInput))
          break label349;
        this.backFromLogin = false;
        this.reloadWithInput = false;
        ruleJs();
        if (paramMApiRequest.getArray("Rules") == null)
          break label365;
        this.rules = new ArrayList(Arrays.asList(paramMApiRequest.getArray("Rules")));
        label257: this.rulesDesc = paramMApiRequest.getString("DiscountDesc");
        this.enableNoDiscountInput = paramMApiRequest.getString("EnableNoDiscountInput");
        this.noDiscountInputUrl = paramMApiRequest.getString("NoDiscountInputUrl");
        this.payHint = paramMApiRequest.getString("PayHint");
        this.activityJsShowTypes = paramMApiRequest.getIntArray("ActivityJSShowTypeList");
        if (this.activityJsShowTypes == null)
          this.activityJsShowTypes = new int[0];
        this.minAmount = new BigDecimal(paramMApiRequest.getString("MinAmount"));
        initView();
      }
    }
    label349: label365: label506: 
    do
    {
      do
      {
        do
        {
          do
          {
            return;
            ruleJs(0, "null", 11, "0");
            break;
            this.rules = new ArrayList();
            break label257;
            if (paramMApiRequest != this.payReq)
              break label506;
            this.payReq = null;
            dismissDialog();
            this.btnSubmit.setClickable(true);
            this.followingBtnSubmit.setClickable(true);
          }
          while (!(paramMApiResponse.result() instanceof DPObject));
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          this.huiOrderId = paramMApiRequest.getInt("OrderID");
          this.payOrderId = paramMApiRequest.getInt("PayOrderID");
          paramMApiRequest = paramMApiRequest.getString("MOPayContent");
          if (this.selectedPaymentTool.getInt("PaymentTool") != 11)
            continue;
          wxPay(paramMApiRequest);
          return;
        }
        while (this.selectedPaymentTool.getInt("PaymentTool") != 5);
        aliPay(paramMApiRequest);
        return;
      }
      while (paramMApiRequest != this.payLocationReq);
      this.payLocationReq = null;
    }
    while (!(paramMApiResponse.result() instanceof DPObject));
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest.getInt("Status") == 10)
    {
      showProgressDialog("载入中...", this);
      requestPay();
      this.btnSubmit.setClickable(false);
      this.followingBtnSubmit.setClickable(false);
      return;
    }
    if (paramMApiRequest.getInt("Status") == 20)
    {
      showPayDialog(paramMApiRequest.getString("Message"));
      return;
    }
    showProgressDialog("载入中...", this);
    requestPay();
    this.btnSubmit.setClickable(false);
    this.followingBtnSubmit.setClickable(false);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopName", this.shopName);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putInt("orderbiztype", this.orderBizType);
    paramBundle.putString("thirdpartyorderid", this.thirdPartyOrderId);
    paramBundle.putInt("thirdpartyordertype", this.thirdPartyOrderType.intValue());
    paramBundle.putInt("source", this.source);
    paramBundle.putString("serializedid", this.serializedId);
    paramBundle.putSerializable("usermobile", this.userMobile);
    super.onSaveInstanceState(paramBundle);
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  void parseActivityJson(JSONObject paramJSONObject)
    throws JSONException
  {
    String str2 = paramJSONObject.getString("amount");
    String str1 = str2;
    if ("NaN".equalsIgnoreCase(str2))
      str1 = "0";
    if ((this.selectedDiscount != null) && (hasActivityInDiscount(this.selectedDiscount.type, this.activityJsShowTypes, this.activityJsFunction)))
      this.pay = new BigDecimal(str1);
    if (paramJSONObject.has("activities"))
    {
      paramJSONObject = paramJSONObject.getJSONArray("activities");
      this.activities.clear();
      int i = 0;
      while (i < paramJSONObject.length())
      {
        this.activities.add(HuiCashierActivity.CashierDiscountItem.HuiActivityInfo.parseJson(paramJSONObject.getJSONObject(i)));
        i += 1;
      }
    }
    sendUpdatePayMsg();
  }

  void parseJson(JSONObject paramJSONObject)
    throws JSONException
  {
    String str2 = paramJSONObject.getString("amount");
    String str1 = str2;
    if ("NaN".equalsIgnoreCase(str2))
      str1 = "0";
    this.pay = new BigDecimal(str1);
    if (paramJSONObject.has("discount"))
    {
      paramJSONObject = paramJSONObject.getJSONArray("discount");
      if (paramJSONObject.length() > 0)
        parseDiscounts(paramJSONObject);
    }
    if (!TextUtils.isEmpty(this.activityJsFunction))
    {
      calcActivityJs();
      return;
    }
    sendUpdatePayMsg();
  }

  void ruleJs()
  {
    int i = 10;
    if (this.selectedDiscount != null)
      i = this.selectedDiscount.type;
    int j = 5;
    if (this.selectedPaymentTool != null)
      j = this.selectedPaymentTool.getInt("PaymentTool");
    String str1;
    if (this.costNull)
    {
      str1 = "null";
      if ((this.isNoDiscountChecked) && (this.noDiscountAmount != null))
        break label90;
    }
    label90: for (String str2 = "0"; ; str2 = HuiUtils.bigDecimalToString(this.noDiscountAmount))
    {
      ruleJs(i, str1, j, str2);
      return;
      str1 = HuiUtils.bigDecimalToString(this.cost);
      break;
    }
  }

  void showMessageDialog(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage(paramString1);
    localBuilder.setPositiveButton(paramString2, paramOnClickListener);
    paramString1 = localBuilder.create();
    paramString1.setCanceledOnTouchOutside(false);
    this.managedDialogId = 64006;
    this.managedDialog = paramString1;
    paramString1.show();
  }

  void showPayDialog(String paramString)
  {
    paramString = new HuiAlertDialog(this, "提示", paramString);
    paramString.setCanceledOnTouchOutside(true);
    paramString.setListener(new HuiAlertDialog.OnDialogOperationListener()
    {
      public void cancel()
      {
        HuiCashierActivity.this.statisticsEvent("hui7", "hui7_cashier_restrict_cancel", Integer.toString(HuiCashierActivity.this.shopId), 0);
      }

      public void confirm()
      {
        HuiCashierActivity.this.statisticsEvent("hui7", "hui7_cashier_restrict_continue", Integer.toString(HuiCashierActivity.this.shopId), 0);
        HuiCashierActivity.this.showProgressDialog("载入中...", HuiCashierActivity.this);
        HuiCashierActivity.this.requestPay();
        HuiCashierActivity.this.btnSubmit.setClickable(false);
        HuiCashierActivity.this.followingBtnSubmit.setClickable(false);
      }
    });
    paramString.show();
    statisticsEvent("hui7", "hui7_cashier_restrict_alert", Integer.toString(this.shopId), 0);
  }

  public void showProgressDialog(String paramString, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    BeautifulProgressDialog localBeautifulProgressDialog = new BeautifulProgressDialog(this);
    localBeautifulProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        if (HuiCashierActivity.this.childCancelListener != null)
        {
          HuiCashierActivity.this.childCancelListener.onCancel(paramDialogInterface);
          HuiCashierActivity.this.childCancelListener = null;
        }
        HuiCashierActivity.this.managedDialog = null;
        HuiCashierActivity.this.btnSubmit.setClickable(true);
        HuiCashierActivity.this.followingBtnSubmit.setClickable(true);
      }
    });
    String str = paramString;
    if (paramString == null)
      str = "载入中...";
    localBeautifulProgressDialog.setMessage(str);
    this.managedDialog = localBeautifulProgressDialog;
    this.childCancelListener = paramOnCancelListener;
    localBeautifulProgressDialog.show();
  }

  static class CashierDiscountItem
  {
    List<CashierDiscountItemRule> rules;
    boolean selected;
    String title;
    int type;

    public CashierDiscountItem(String paramString, int paramInt, boolean paramBoolean, List<CashierDiscountItemRule> paramList)
    {
      this.title = paramString;
      this.type = paramInt;
      this.selected = paramBoolean;
      this.rules = paramList;
    }

    public Map<String, String> getSelectedItemAndTicket()
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("itemId", "0");
      localHashMap.put("ticketId", "0");
      Iterator localIterator1 = this.rules.iterator();
      while (localIterator1.hasNext())
      {
        CashierDiscountItemRule localCashierDiscountItemRule = (CashierDiscountItemRule)localIterator1.next();
        if (localCashierDiscountItemRule.bookingMealTickets.isEmpty())
          continue;
        Iterator localIterator2 = localCashierDiscountItemRule.bookingMealTickets.iterator();
        while (localIterator2.hasNext())
        {
          BookingMealTicket localBookingMealTicket = (BookingMealTicket)localIterator2.next();
          if (!localBookingMealTicket.selected)
            continue;
          localHashMap.put("itemId", String.valueOf(localCashierDiscountItemRule.id));
          localHashMap.put("ticketId", String.valueOf(localBookingMealTicket.id));
        }
      }
      return localHashMap;
    }

    static class BookingMealTicket
    {
      boolean available;
      String content;
      BigDecimal discountAmount;
      String id;
      String remark;
      String rule;
      boolean selected;
      String subtitle;

      public BookingMealTicket(String paramString1, boolean paramBoolean1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean2)
      {
        this.id = paramString1;
        this.available = paramBoolean1;
        this.discountAmount = new BigDecimal(paramString2);
        this.content = paramString3;
        this.subtitle = paramString4;
        this.rule = paramString5;
        this.remark = paramString6;
        this.selected = paramBoolean2;
      }
    }

    static class CashierDiscountItemRule
    {
      static final int DEDUCTION_HIGHLIGHT = 16;
      static final int DISPLAY = 1;
      static final int DISPLAY_DEDUCTION = 2;
      static final int DISPLAY_NORMAL = 4;
      static final int NEED_LOGIN = 8;
      List<HuiCashierActivity.CashierDiscountItem.BookingMealTicket> bookingMealTickets;
      String content;
      BigDecimal discountAmount;
      String discountTip;
      int flag;
      int id;
      String remark;

      public CashierDiscountItemRule(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, List<HuiCashierActivity.CashierDiscountItem.BookingMealTicket> paramList)
      {
        this.id = paramInt;
        this.content = paramString1;
        this.remark = paramString2;
        this.discountTip = paramString3;
        int j = this.flag;
        if (paramBoolean2)
        {
          paramInt = 1;
          this.flag = (paramInt | j);
          j = this.flag;
          if (!paramBoolean5)
            break label163;
          paramInt = 4;
          label62: this.flag = (paramInt | j);
          j = this.flag;
          if (!paramBoolean1)
            break label168;
          paramInt = 2;
          label83: this.flag = (paramInt | j);
          j = this.flag;
          if (!paramBoolean3)
            break label173;
        }
        label163: label168: label173: for (paramInt = 8; ; paramInt = 0)
        {
          this.flag = (paramInt | j);
          j = this.flag;
          paramInt = i;
          if (paramBoolean4)
            paramInt = 16;
          this.flag = (j | paramInt);
          this.discountAmount = new BigDecimal(paramString4);
          this.bookingMealTickets = paramList;
          return;
          paramInt = 0;
          break;
          paramInt = 0;
          break label62;
          paramInt = 0;
          break label83;
        }
      }

      public ArrayList<DPObject> getTicketsDPObjectList()
      {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = this.bookingMealTickets.iterator();
        while (localIterator.hasNext())
        {
          HuiCashierActivity.CashierDiscountItem.BookingMealTicket localBookingMealTicket = (HuiCashierActivity.CashierDiscountItem.BookingMealTicket)localIterator.next();
          localArrayList.add(new DPObject("TicketOption").edit().putString("Id", localBookingMealTicket.id).putString("Deduction", localBookingMealTicket.discountAmount.toPlainString()).putString("Content", localBookingMealTicket.content).putString("Remark", localBookingMealTicket.remark).putBoolean("CanUse", localBookingMealTicket.available).putString("Subtitle", localBookingMealTicket.subtitle).putString("Rule", localBookingMealTicket.rule).generate());
        }
        return localArrayList;
      }
    }

    static class HuiActivityInfo
    {
      String desc;
      String id;
      Level[] levels;
      int status;
      String statusMsg;
      String title;

      public static HuiActivityInfo parseJson(JSONObject paramJSONObject)
        throws JSONException
      {
        HuiActivityInfo localHuiActivityInfo = new HuiActivityInfo();
        localHuiActivityInfo.id = paramJSONObject.getString("activityId");
        localHuiActivityInfo.title = paramJSONObject.getString("title");
        localHuiActivityInfo.desc = paramJSONObject.getString("desc");
        localHuiActivityInfo.status = paramJSONObject.getInt("status");
        if (paramJSONObject.has("statusMsg"));
        for (Object localObject = paramJSONObject.getString("statusMsg"); ; localObject = "")
        {
          localHuiActivityInfo.statusMsg = ((String)localObject);
          paramJSONObject = paramJSONObject.getJSONArray("levels");
          localObject = new Level[paramJSONObject.length()];
          int i = 0;
          while (i < paramJSONObject.length())
          {
            JSONObject localJSONObject = paramJSONObject.getJSONObject(i);
            localObject[i] = new Level(localJSONObject.getString("levelId"), localJSONObject.getString("full"), localJSONObject.getString("cut"), localJSONObject.getInt("status"));
            i += 1;
          }
        }
        localHuiActivityInfo.levels = ((Level)localObject);
        return (HuiActivityInfo)localHuiActivityInfo;
      }

      static class Level
      {
        String cut;
        String full;
        String id;
        int status;

        public Level(String paramString1, String paramString2, String paramString3, int paramInt)
        {
          this.id = paramString1;
          this.full = paramString2;
          this.cut = paramString3;
          this.status = paramInt;
        }
      }
    }
  }

  class DiscountAdapter extends BasicAdapter
  {
    DiscountAdapter()
    {
    }

    public int getCount()
    {
      return HuiCashierActivity.this.getDisplayedDiscountCount(HuiCashierActivity.this.discounts, HuiCashierActivity.this.activityJsShowTypes, HuiCashierActivity.this.activityJsFunction);
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = HuiCashierActivity.this.getDisplayedDiscount(HuiCashierActivity.this.discounts, HuiCashierActivity.this.activityJsShowTypes, paramInt, HuiCashierActivity.this.activityJsFunction);
      if (paramView.selected)
        HuiCashierActivity.this.selectedDiscount = paramView;
      return HuiCashierActivity.this.createDiscountView(paramView);
    }
  }

  class PaymentToolAdapter extends BasicAdapter
  {
    PaymentToolAdapter()
    {
    }

    public int getCount()
    {
      return HuiCashierActivity.this.paymentTools.size();
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (DPObject)HuiCashierActivity.this.paymentTools.get(paramInt);
      paramViewGroup = new PayChanelItem(HuiCashierActivity.this);
      if (HuiCashierActivity.this.selectedPaymentTool == paramView);
      for (boolean bool = true; ; bool = false)
      {
        paramViewGroup.setChecked(bool);
        paramViewGroup.setPaymentTool(paramView);
        paramViewGroup.setBackgroundResource(R.drawable.table_view_item);
        paramViewGroup.setClickable(true);
        return paramViewGroup;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiCashierActivity
 * JD-Core Version:    0.6.0
 */