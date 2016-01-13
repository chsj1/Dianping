package com.dianping.base.tuan.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.thirdparty.qqapi.TenPayHelper;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.pay.utils.WebankManager;
import com.dianping.util.Log;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;
import com.tencent.mobileqq.openpay.data.pay.PayApi;
import com.tencent.mobileqq.openpay.data.pay.PayResponse;
import java.util.HashMap;

public class PayManager
{
  private static final int ALIPAYSDK_PAY_FLAG = 1;
  public static final int PAY_ERRORCODE_OK = 0;
  public static final int PAY_ERRORCODE_PARAM_ERROR = 1;
  public static final int PAY_ERRORCODE_THIRDPARTY_EXECUTE_ERROR = 4;
  public static final int PAY_ERRORCODE_THIRDPARTY_NOTINSTALLED = 2;
  public static final int PAY_ERRORCODE_THIRDPARTY_NOTSUPPORTED = 3;
  public static final int PAY_ERRORCODE_THIRDPARTY_OPEN_ERROR = 5;
  public static final int PAY_ERRORCODE_UNKNOWN = -1;
  public static final String PAY_RESULT_SUCCESS = "payresult:pay_result_success";
  public static final int PAY_TYPE_BALANCE = 0;
  public static final int PAY_TYPE_MINIALIPAY = 1;
  public static final int PAY_TYPE_TENPAY = 10;
  public static final int PAY_TYPE_TUAN_WEB = 2;
  public static final int PAY_TYPE_UMPAY = 5;
  public static final int PAY_TYPE_UNKNOWN = -1;
  public static final int PAY_TYPE_UPOMP = 3;
  public static final int PAY_TYPE_WEB = 11;
  public static final int PAY_TYPE_WEBANK = 12;
  public static final int PAY_TYPE_WEIXINPAY = 7;
  public static final int REQUEST_CODE_ALISDK = 102;
  public static final int REQUEST_CODE_BANK = 106;
  public static final int REQUEST_CODE_TENPAY = 104;
  public static final int REQUEST_CODE_TUAN_WEB = 101;
  public static final int REQUEST_CODE_UMPAY = 103;
  private static PayManager payManager = null;
  public final int[] SUPPORTED_PAY_TYPE_LIST = { 1, 7, 10, 2, 5, 11, 12 };
  private final String TAG = "PayManager";
  private Handler mHandler = new PayManager.2(this);
  private PayManager.PayItem payItem = new PayManager.PayItem(this);
  private BroadcastReceiver webPaySuccessReceiver = new PayManager.1(this);

  private PayManager()
  {
    IntentFilter localIntentFilter = new IntentFilter("payresult:pay_result_success");
    DPApplication.instance().registerReceiver(this.webPaySuccessReceiver, localIntentFilter);
  }

  private String checkArgs(int paramInt, String paramString)
  {
    int k = 0;
    int i = 0;
    while (true)
    {
      int j = k;
      if (i < this.SUPPORTED_PAY_TYPE_LIST.length)
      {
        if (paramInt == this.SUPPORTED_PAY_TYPE_LIST[i])
          j = 1;
      }
      else
      {
        if (j != 0)
          break;
        return "支付类型错误";
      }
      i += 1;
    }
    if (TextUtils.isEmpty(paramString))
      return "支付内容为空";
    return "";
  }

  public static PayManager instance()
  {
    if (payManager == null)
      payManager = new PayManager();
    return payManager;
  }

  private void miniAliPay(String paramString, Activity paramActivity, IPayResult paramIPayResult)
  {
    new Thread(new PayManager.3(this, paramActivity, paramString)).start();
  }

  private void tenPay(String paramString, Activity paramActivity, IPayResult paramIPayResult)
  {
    if (this.payItem.checkArgs(10))
    {
      if (!TenPayHelper.isQQAppInstalled(paramActivity))
      {
        onPayFailed(10, 2, "您尚未安装QQ");
        return;
      }
      if (!TenPayHelper.isSupportPay(paramActivity))
      {
        onPayFailed(10, 3, "QQ版本过低，不支持支付功能");
        return;
      }
      paramIPayResult = new HashMap();
      paramString = paramString.split("&");
      int i = 0;
      while (i < paramString.length)
      {
        int j = paramString[i].indexOf("=");
        if (j > 0)
          paramIPayResult.put(paramString[i].substring(0, j), paramString[i].substring(j + 1));
        i += 1;
      }
      paramString = new PayApi();
      if (((String)paramIPayResult.get("appid")).equals("200002"))
      {
        paramString.appId = ((String)paramIPayResult.get("appid"));
        paramString.serialNumber = ((String)paramIPayResult.get("serialNumber"));
        paramString.callbackScheme = ((String)paramIPayResult.get("callbackScheme"));
        paramString.tokenId = ((String)paramIPayResult.get("token_id"));
        paramString.pubAcc = ((String)paramIPayResult.get("pubAcc"));
        paramString.pubAccHint = ((String)paramIPayResult.get("pubAccHint"));
        if (paramIPayResult.get("timeStamp") != null)
          paramString.timeStamp = Long.valueOf((String)paramIPayResult.get("timeStamp")).longValue();
        paramString.nonce = ((String)paramIPayResult.get("nonce"));
        paramString.bargainorId = ((String)paramIPayResult.get("bargainor_id"));
        paramString.sig = ((String)paramIPayResult.get("sign"));
        paramString.sigType = ((String)paramIPayResult.get("sigType"));
        if (paramString.checkParams())
        {
          TenPayHelper.getQQAPI(paramActivity).execApi(paramString);
          return;
        }
      }
      else
      {
        onPayFailed(10, 1, "QQ验证失败，请选择其他支付方式");
        return;
      }
      onPayFailed(10, 1, "QQ验证失败，请选择其他支付方式.");
      return;
    }
    this.payItem.clear();
  }

  private void umpay(String paramString1, Activity paramActivity, String paramString2)
  {
    if (this.payItem.checkArgs(5))
      try
      {
        paramString1 = paramString1.split("\\|\\|");
        if (paramString1.length < 2)
        {
          onPayFailed(5, 1, "支付失败，参数错误");
          return;
        }
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://umpay"));
        localIntent.putExtra("tradeNo", paramString1[0]);
        localIntent.putExtra("cardType", paramString1[1]);
        if ((paramString2 != null) && (!TextUtils.isEmpty(paramString2)))
          localIntent.putExtra("bankName", paramString2);
        paramActivity.startActivityForResult(localIntent, 103);
        return;
      }
      catch (java.lang.Exception paramString1)
      {
        onPayFailed(5, 1, "支付失败，请选择其他支付方式");
        return;
      }
    this.payItem.clear();
  }

  private void webankPay(String paramString, Activity paramActivity)
  {
    if (this.payItem.checkArgs(12))
    {
      Object localObject = new HashMap();
      paramString = paramString.split("&");
      int i = 0;
      while (i < paramString.length)
      {
        int j = paramString[i].indexOf("=");
        if (j > 0)
          ((HashMap)localObject).put(paramString[i].substring(0, j), paramString[i].substring(j + 1));
        i += 1;
      }
      paramString = (String)((HashMap)localObject).get("code");
      String str1 = (String)((HashMap)localObject).get("paysessionid");
      String str2 = (String)((HashMap)localObject).get("realname");
      localObject = (String)((HashMap)localObject).get("mobileno");
      WebankManager.instance().startWebankPay(paramActivity, paramString, str1, str2, (String)localObject);
      return;
    }
    this.payItem.clear();
  }

  private void weixinPay(String paramString, Activity paramActivity, IPayResult paramIPayResult)
  {
    if (this.payItem.checkArgs(7))
    {
      if (!WXHelper.isWXAppInstalled(paramActivity, false))
        onPayFailed(7, 2, "您尚未安装微信");
      while (true)
      {
        return;
        if (!WXHelper.isSupportPay(paramActivity, false))
        {
          onPayFailed(7, 3, "微信版本过低，不支持支付功能");
          return;
        }
        paramIPayResult = new HashMap();
        paramString = paramString.split("&");
        int i = 0;
        while (i < paramString.length)
        {
          int j = paramString[i].indexOf("=");
          if (j > 0)
            paramIPayResult.put(paramString[i].substring(0, j), paramString[i].substring(j + 1));
          i += 1;
        }
        paramString = new PayReq();
        if (!((String)paramIPayResult.get("appid")).equals("wx8e251222d6836a60"))
          break;
        paramString.appId = ((String)paramIPayResult.get("appid"));
        paramString.partnerId = ((String)paramIPayResult.get("partnerid"));
        paramString.prepayId = ((String)paramIPayResult.get("prepayid"));
        paramString.nonceStr = ((String)paramIPayResult.get("noncestr"));
        paramString.timeStamp = ((String)paramIPayResult.get("timestamp"));
        paramString.packageValue = ((String)paramIPayResult.get("package"));
        paramString.sign = ((String)paramIPayResult.get("sign"));
        if (WXHelper.getWXAPI(paramActivity).sendReq(paramString))
          continue;
        onPayFailed(7, 5, "打开微信失败，请选择其他支付方式");
        return;
      }
      onPayFailed(7, 1, "微信验证失败，请选择其他支付方式");
      return;
    }
    this.payItem.clear();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 101)
      if (this.payItem.checkArgs(2))
        if (paramInt2 == -1)
          onPaySuccess(2, 0, "支付成功");
    while (true)
    {
      return;
      onPayFailed(2, 4, "支付失败");
      return;
      this.payItem.clear();
      return;
      if (paramInt1 != 103)
        break label156;
      if (!this.payItem.checkArgs(5))
        break;
      if (paramIntent == null)
      {
        onPayFailed(5, 4, "支付失败，请选择其他支付方式");
        return;
      }
      String str1 = paramIntent.getStringExtra("umpResultCode");
      String str2 = paramIntent.getStringExtra("umpResultMessage");
      paramIntent.getStringExtra("orderId");
      if ("0000".equalsIgnoreCase(str1))
      {
        onPaySuccess(5, 0, "支付成功");
        return;
      }
      if ("1001".equalsIgnoreCase(str1))
        continue;
      onPayFailed(5, 4, str2);
      return;
    }
    this.payItem.clear();
    return;
    label156: this.payItem.clear();
  }

  protected void onPayFailed(int paramInt1, int paramInt2, String paramString)
  {
    this.payItem.payResult.onPayFailed(paramInt1, paramInt2, paramString);
    this.payItem.clear();
  }

  protected void onPaySuccess(int paramInt1, int paramInt2, String paramString)
  {
    this.payItem.payResult.onPaySuccess(paramInt1, paramInt2, paramString);
    this.payItem.clear();
  }

  public void pay(int paramInt, String paramString, Activity paramActivity, IPayResult paramIPayResult)
  {
    pay(paramInt, paramString, paramActivity, paramIPayResult, null);
  }

  public void pay(int paramInt, String paramString1, Activity paramActivity, IPayResult paramIPayResult, String paramString2)
  {
    Log.v("PayManager", "pay: payType=" + paramInt + ",payContent=" + paramString1 + ",activity=" + paramActivity.toString());
    this.payItem.set(paramInt, paramIPayResult);
    String str = checkArgs(paramInt, paramString1);
    if (!TextUtils.isEmpty(str))
      if (this.payItem.checkArgs(paramInt))
        onPayFailed(paramInt, 1, str);
    do
    {
      return;
      if (paramInt == 1)
      {
        miniAliPay(paramString1, paramActivity, paramIPayResult);
        return;
      }
      if (paramInt == 7)
      {
        weixinPay(paramString1, paramActivity, paramIPayResult);
        return;
      }
      if (paramInt == 10)
      {
        tenPay(paramString1, paramActivity, paramIPayResult);
        return;
      }
      if (paramInt == 12)
      {
        webankPay(paramString1, paramActivity);
        return;
      }
      if (paramInt == 2)
      {
        paramIPayResult = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuan"));
        paramIPayResult.putExtra("url", paramString1);
        paramActivity.startActivityForResult(paramIPayResult, 101);
        return;
      }
      if (paramInt != 11)
        continue;
      paramActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString1)));
      return;
    }
    while (paramInt != 5);
    umpay(paramString1, paramActivity, paramString2);
  }

  public void release()
  {
    this.mHandler.removeMessages(1);
    this.payItem.clear();
  }

  public void tenPayResult(BaseResponse paramBaseResponse)
  {
    if (this.payItem.checkArgs(10))
    {
      if (paramBaseResponse == null)
        paramBaseResponse = "支付失败!";
      while (true)
      {
        onPayFailed(10, 4, paramBaseResponse);
        return;
        if ((paramBaseResponse instanceof PayResponse))
        {
          PayResponse localPayResponse = (PayResponse)paramBaseResponse;
          paramBaseResponse = "" + localPayResponse.retMsg;
          if (!localPayResponse.isSuccess())
            continue;
          onPaySuccess(10, 0, "支付成功");
          return;
        }
        paramBaseResponse = "支付失败";
      }
    }
    this.payItem.clear();
  }

  public void webankPayResult(boolean paramBoolean, int paramInt, String paramString)
  {
    if (paramBoolean)
    {
      onPaySuccess(12, paramInt, paramString);
      return;
    }
    onPayFailed(12, paramInt, paramString);
  }

  public void weixinPayResult(Bundle paramBundle)
  {
    paramBundle = new PayResp(paramBundle);
    if (this.payItem.checkArgs(7))
    {
      if (paramBundle.errCode == 0)
      {
        onPaySuccess(7, 0, paramBundle.errStr);
        return;
      }
      if (paramBundle.errCode == -2)
      {
        onPayFailed(7, 4, "用户取消支付");
        return;
      }
      if ((TextUtils.isEmpty(paramBundle.errStr)) && (Environment.isDebug()))
      {
        onPayFailed(7, 4, "测试版本无法进行微信支付，请选择其他支付方式");
        return;
      }
      onPayFailed(7, 4, paramBundle.errStr);
      return;
    }
    this.payItem.clear();
  }

  public static abstract interface IPayResult
  {
    public abstract void onPayFailed(int paramInt1, int paramInt2, String paramString);

    public abstract void onPaySuccess(int paramInt1, int paramInt2, String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.pay.PayManager
 * JD-Core Version:    0.6.0
 */