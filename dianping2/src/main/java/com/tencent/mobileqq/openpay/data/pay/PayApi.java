package com.tencent.mobileqq.openpay.data.pay;

import android.os.Bundle;
import android.text.TextUtils;
import com.tencent.mobileqq.openpay.data.base.BaseApi;

public class PayApi extends BaseApi
{
  public String bargainorId;
  public String callbackScheme;
  public String nonce;
  public String pubAcc;
  public String pubAccHint;
  public String serialNumber;
  public String sig;
  public String sigType;
  public long timeStamp;
  public String tokenId;

  public boolean checkParams()
  {
    if (TextUtils.isEmpty(this.appId));
    do
      return false;
    while ((TextUtils.isEmpty("native")) || ("native".compareTo("native") != 0) || (TextUtils.isEmpty(this.callbackScheme)) || (TextUtils.isEmpty(this.tokenId)) || (TextUtils.isEmpty(this.bargainorId)) || (TextUtils.isEmpty(this.nonce)) || (TextUtils.isEmpty(this.sig)) || (TextUtils.isEmpty(this.sigType)) || (this.timeStamp <= 0L) || (TextUtils.isEmpty(this.serialNumber)));
    return true;
  }

  public void fromBundle(Bundle paramBundle)
  {
    super.fromBundle(paramBundle);
    this.serialNumber = paramBundle.getString("_mqqpay_payapi_serialnumber");
    this.callbackScheme = paramBundle.getString("_mqqpay_payapi_callbackscheme");
    this.pubAcc = paramBundle.getString("_mqqpay_payapi_pubacc");
    this.pubAccHint = paramBundle.getString("_mqqpay_payapi_pubacchint");
    this.tokenId = paramBundle.getString("_mqqpay_payapi_tokenid");
    this.nonce = paramBundle.getString("_mqqpay_payapi_nonce");
    this.timeStamp = paramBundle.getLong("_mqqpay_payapi_timeStamp");
    this.bargainorId = paramBundle.getString("_mqqpay_payapi_bargainorId");
    this.sigType = paramBundle.getString("_mqqpay_payapi_sigType");
    this.sig = paramBundle.getString("_mqqpay_payapi_sig");
  }

  public int getApiMark()
  {
    return 1;
  }

  public String getApiName()
  {
    return "pay";
  }

  public void toBundle(Bundle paramBundle)
  {
    super.toBundle(paramBundle);
    paramBundle.putString("_mqqpay_payapi_serialnumber", this.serialNumber);
    paramBundle.putString("_mqqpay_payapi_callbackscheme", this.callbackScheme);
    paramBundle.putString("_mqqpay_payapi_pubacc", this.pubAcc);
    paramBundle.putString("_mqqpay_payapi_pubacchint", this.pubAccHint);
    paramBundle.putString("_mqqpay_payapi_tokenid", this.tokenId);
    paramBundle.putString("_mqqpay_payapi_nonce", this.nonce);
    paramBundle.putLong("_mqqpay_payapi_timeStamp", this.timeStamp);
    paramBundle.putString("_mqqpay_payapi_bargainorId", this.bargainorId);
    paramBundle.putString("_mqqpay_payapi_sigType", this.sigType);
    paramBundle.putString("_mqqpay_payapi_sig", this.sig);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.data.pay.PayApi
 * JD-Core Version:    0.6.0
 */