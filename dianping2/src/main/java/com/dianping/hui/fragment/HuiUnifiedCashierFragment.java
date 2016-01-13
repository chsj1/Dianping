package com.dianping.hui.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.utils.PayUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.HuiCashierDataSource;
import com.dianping.hui.entity.HuiCashierDataSource.HuiCashierDataLoaderListener;
import com.dianping.hui.entity.HuiMapiStatus;
import com.dianping.hui.util.HuiUtils;
import com.dianping.hui.widget.HuiAlertDialog;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaTextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class HuiUnifiedCashierFragment extends AgentFragment
  implements HuiCashierDataSource.HuiCashierDataLoaderListener, AgentFragment.CellStable, DialogInterface.OnCancelListener
{
  private static final String TAG = HuiUnifiedCashierFragment.class.getSimpleName();
  LinearLayout containerView;
  protected HuiCashierDataSource dataSource;
  private ViewGroup huiCashierBottomLayout;
  private View huiCashierErrorLayout;
  private View huiCashierLoadedLayout;
  private View huiCashierLoadingLayout;
  private ViewGroup wholeLayout;

  private void fetchParams(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      this.dataSource.restoreData(paramBundle);
      return;
    }
    this.dataSource.shopName = getStringParam("shopname");
    this.dataSource.shopId = getIntParam("shopid");
    this.dataSource.promoString = getStringParam("promostring");
    HuiCashierDataSource localHuiCashierDataSource = this.dataSource;
    if (TextUtils.isEmpty(getStringParam("amount")))
    {
      paramBundle = "0.0";
      localHuiCashierDataSource.originAmount = new BigDecimal(paramBundle);
      localHuiCashierDataSource = this.dataSource;
      if (!TextUtils.isEmpty(getStringParam("nodiscountamount")))
        break label232;
    }
    label232: for (paramBundle = "0.0"; ; paramBundle = getStringParam("nodiscountamount"))
    {
      localHuiCashierDataSource.noDiscountAmount = new BigDecimal(paramBundle);
      this.dataSource.thirdPartyOrderId = getStringParam("thirdpartyorderid");
      this.dataSource.thirdPartyOrderType = getIntParam("thirdpartyordertype");
      this.dataSource.bizOrderType = getIntParam("bizordertype");
      this.dataSource.bizOrderId = getStringParam("bizorderid");
      this.dataSource.callBackUrl = getStringParam("successurl");
      this.dataSource.callBackFailUrl = getStringParam("failureurl");
      this.dataSource.amountLocked = getIntParam("amountlocked", 0);
      this.dataSource.config = getStringParam("config");
      return;
      paramBundle = getStringParam("amount");
      break;
    }
  }

  private String getMiniPayOrderInfoTitle(String paramString1, String paramString2)
  {
    JSONArray localJSONArray = new JSONArray();
    try
    {
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("text", paramString1);
      localJSONObject.put("textsize", 20);
      localJSONObject.put("textcolor", "#FF000000");
      localJSONArray.put(localJSONObject);
      if (!TextUtils.isEmpty(paramString2))
      {
        paramString1 = new JSONObject();
        paramString1.put("text", paramString2);
        paramString1.put("textsize", 16);
        paramString1.put("textcolor", "#FF999999");
        localJSONArray.put(paramString1);
      }
      return localJSONArray.toString();
    }
    catch (org.json.JSONException paramString1)
    {
      while (true)
        Log.e("HuiUnifiedCashier", "wrong json!", paramString1);
    }
  }

  private void setInputMaskVisibile()
  {
    ViewGroup localViewGroup = (ViewGroup)getActivity().getWindow().getDecorView();
    LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.hui_cashier_layer, localViewGroup, false);
    localViewGroup.addView(localLinearLayout);
    Button localButton = (Button)localLinearLayout.findViewById(R.id.know_button);
    localLinearLayout.setClickable(true);
    localButton.setOnClickListener(new HuiUnifiedCashierFragment.3(this, localViewGroup, localLinearLayout));
  }

  private void showNewUserInputMask()
  {
    try
    {
      if ((!TextUtils.isEmpty(accountService().token())) && (this.dataSource.amountLocked == 0) && (this.dataSource.isNewUser == 10))
      {
        Object localObject = getActivity().getSharedPreferences(getActivity().getPackageName(), 0);
        HuiCashierDataSource localHuiCashierDataSource = this.dataSource;
        if (!((SharedPreferences)localObject).getBoolean("isCashierInputMaskShow", false))
        {
          localObject = ((SharedPreferences)localObject).edit();
          localHuiCashierDataSource = this.dataSource;
          ((SharedPreferences.Editor)localObject).putBoolean("isCashierInputMaskShow", true).commit();
          setInputMaskVisibile();
        }
      }
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "showNewUserInputMask fail");
    }
  }

  private void submitPay()
  {
    AgentMessage localAgentMessage = new AgentMessage("com.dianping.base.tuan.agent.DPCashierDeskAgent.PaySubmitRequest");
    Bundle localBundle = new Bundle();
    localBundle.putString("orderId", (String)this.dataSource.payOrderResultMap.get("orderid"));
    localBundle.putString("productCode", (String)this.dataSource.payOrderResultMap.get("productcode"));
    localAgentMessage.body = localBundle;
    dispatchMessage(localAgentMessage);
  }

  public void addHeightObserver()
  {
    this.containerView.getViewTreeObserver().addOnGlobalLayoutListener(new HuiUnifiedCashierFragment.2(this));
  }

  public void createOrder(HuiMapiStatus paramHuiMapiStatus, Object paramObject)
  {
    dismissDialog();
    switch (HuiUnifiedCashierFragment.8.$SwitchMap$com$dianping$hui$entity$HuiMapiStatus[paramHuiMapiStatus.ordinal()])
    {
    default:
      return;
    case 1:
      showProgressDialog("加载中");
      return;
    case 2:
      if (this.dataSource.orderStatus == 50)
      {
        paramHuiMapiStatus = new Intent("android.intent.action.VIEW", Uri.parse("dianping://huiunifiedpayresult"));
        paramHuiMapiStatus.putExtra("shopid", this.dataSource.shopId);
        paramHuiMapiStatus.putExtra("ordercreatetime", this.dataSource.orderCreateTime);
        paramHuiMapiStatus.putExtra("serializedid", this.dataSource.serializedId);
        startActivity(paramHuiMapiStatus);
        return;
      }
      submitPay();
      return;
    case 3:
    }
    switch (this.dataSource.orderStatus)
    {
    default:
      showToast(this.dataSource.message);
      return;
    case 20:
    case 21:
    case 31:
      showToast(this.dataSource.message);
      return;
    case 32:
    case 40:
    case 41:
    }
    showMessageDialog(this.dataSource.message, "确定", new HuiUnifiedCashierFragment.7(this));
  }

  public void dismissLoadingDialog()
  {
    dismissDialog();
  }

  protected void dispatchLoadedMsg()
  {
    dispatchMessage(new AgentMessage("hui_unified_cashier_init_success"));
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new HuiUnifiedCashierFragment.4(this));
    return localArrayList;
  }

  public HuiCashierDataSource getDataSource()
  {
    if (this.dataSource == null)
      this.dataSource = new HuiCashierDataSource((NovaActivity)getActivity());
    return this.dataSource;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    if (!TextUtils.isEmpty(this.dataSource.gaProductCode))
      localGAUserInfo.prepay_info = PayUtils.generateGAPrepayInfos(this.dataSource.gaProductCode.split(","));
    return localGAUserInfo;
  }

  public void loadCashierStrategy(HuiMapiStatus paramHuiMapiStatus, Object paramObject)
  {
    this.dataSource.loadStatus = paramHuiMapiStatus;
    updateViewAccordingToStatus();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.getActivity().getWindow().setBackgroundDrawable(null);
    setHost("hui_cashier");
    this.dataSource = new HuiCashierDataSource((NovaActivity)getActivity());
    this.dataSource.huiCashierDataLoaderListener = this;
    fetchParams(paramBundle);
    super.getActivity().setTitle(this.dataSource.shopName);
    startLoadData();
    super.onActivityCreated(paramBundle);
    paramBundle = new Bundle();
    paramBundle.putInt("promodeskheadstyle", 0);
    paramBundle.putInt("promodeskotherpromoheadstyle", 0);
    paramBundle.putInt("promodeskforceautopromo", 1);
    dispatchAgentChanged(null, paramBundle);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    this.dataSource.abortCreateOrderReq();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.hui_unifiedcashier_fragment, paramViewGroup, false);
    this.wholeLayout = ((ViewGroup)paramLayoutInflater.findViewById(R.id.whole_layout));
    this.containerView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.hui_cashier_container_layout));
    this.huiCashierLoadingLayout = paramLayoutInflater.findViewById(R.id.hui_cashier_loading_layout);
    this.huiCashierLoadedLayout = paramLayoutInflater.findViewById(R.id.hui_cashier_loaded_layout);
    this.huiCashierErrorLayout = paramLayoutInflater.findViewById(R.id.error_layout);
    this.huiCashierErrorLayout.setOnClickListener(new HuiUnifiedCashierFragment.1(this));
    this.huiCashierBottomLayout = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_layout));
    addHeightObserver();
    setAgentContainerView(this.containerView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    this.dataSource.releaseRequests();
    super.onDestroy();
  }

  public boolean onGoBack()
  {
    GAHelper.instance().contextStatisticsEvent(getActivity(), "back", getGAUserInfo(), "tap");
    return super.onGoBack();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    if (paramView != null)
    {
      this.huiCashierBottomLayout.removeAllViews();
      this.huiCashierBottomLayout.addView(paramView);
      HuiUtils.updateViewVisibility(this.huiCashierBottomLayout, 0);
      return;
    }
    this.huiCashierBottomLayout.removeAllViews();
    HuiUtils.updateViewVisibility(this.huiCashierBottomLayout, 8);
  }

  public void setHuiRules(String paramString1, String paramString2)
  {
    ((TextView)super.getTitleBar().findViewById(R.id.title_bar_title)).setMaxWidth(ViewUtils.dip2px(getActivity(), 200.0F));
    NovaTextView localNovaTextView = (NovaTextView)LayoutInflater.from(getActivity()).inflate(R.layout.title_bar_text, null, false);
    localNovaTextView.setPadding(0, 0, 0, 0);
    localNovaTextView.setText(paramString1);
    super.getTitleBar().addRightViewItem(localNovaTextView, "huirules", new HuiUnifiedCashierFragment.6(this, paramString2));
  }

  public void setSubmitButtonEnable(boolean paramBoolean)
  {
    AgentMessage localAgentMessage = new AgentMessage("hui_unified_cashier_submit_btn_enable_status");
    localAgentMessage.target = findAgent("hui_cashier/submitbutton");
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("enable", paramBoolean);
    localAgentMessage.body = localBundle;
    dispatchMessage(localAgentMessage);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
  }

  public void showMessageDialog(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setMessage(paramString1).setCancelable(false).setPositiveButton(paramString2, paramOnClickListener);
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  public void showPayDialog(String paramString)
  {
    showPayDialog(paramString, null);
  }

  public void showPayDialog(String paramString1, String paramString2)
  {
    dismissDialog();
    paramString1 = new HuiAlertDialog(getActivity(), "提示", paramString1, paramString2);
    paramString1.setCanceledOnTouchOutside(true);
    paramString1.setListener(new HuiUnifiedCashierFragment.5(this));
    paramString1.show();
    statisticsEvent("hui7", "hui7_cashier_restrict_alert", Integer.toString(this.dataSource.shopId), 0);
  }

  public void startLoadData()
  {
    this.dataSource.loadStatus = HuiMapiStatus.STATUS_START;
    updateViewAccordingToStatus();
    this.dataSource.requestPayStrategy();
  }

  protected void updateViewAccordingToStatus()
  {
    switch (HuiUnifiedCashierFragment.8.$SwitchMap$com$dianping$hui$entity$HuiMapiStatus[this.dataSource.loadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      HuiUtils.updateViewVisibility(this.huiCashierLoadingLayout, 0);
      HuiUtils.updateViewVisibility(this.huiCashierLoadedLayout, 8);
      HuiUtils.updateViewVisibility(this.huiCashierErrorLayout, 8);
      HuiUtils.updateViewVisibility(this.huiCashierBottomLayout, 8);
      return;
    case 2:
      HuiUtils.updateViewVisibility(this.huiCashierLoadingLayout, 8);
      HuiUtils.updateViewVisibility(this.huiCashierLoadedLayout, 0);
      HuiUtils.updateViewVisibility(this.huiCashierErrorLayout, 8);
      HuiUtils.updateViewVisibility(this.huiCashierBottomLayout, 0);
      dispatchLoadedMsg();
      showNewUserInputMask();
      return;
    case 3:
    }
    HuiUtils.updateViewVisibility(this.huiCashierLoadingLayout, 8);
    HuiUtils.updateViewVisibility(this.huiCashierLoadedLayout, 8);
    HuiUtils.updateViewVisibility(this.huiCashierErrorLayout, 0);
    HuiUtils.updateViewVisibility(this.huiCashierBottomLayout, 8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.fragment.HuiUnifiedCashierFragment
 * JD-Core Version:    0.6.0
 */