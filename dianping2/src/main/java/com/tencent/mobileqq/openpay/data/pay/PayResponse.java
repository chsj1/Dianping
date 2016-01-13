package com.tencent.mobileqq.openpay.data.pay;

import android.os.Bundle;
import android.text.TextUtils;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;

public class PayResponse extends BaseResponse
{
  private String a;
  public String callbackUrl;
  public String payTime;
  public String serialNumber;
  public String spData;
  public String totalFee;
  public String transactionId;

  public boolean checkParams()
  {
    if (this.retCode == -9999999);
    do
      return false;
    while ((isSuccess()) && (!isPayByWeChat()) && ((TextUtils.isEmpty(this.transactionId)) || (TextUtils.isEmpty(this.payTime)) || (TextUtils.isEmpty(this.totalFee))));
    return true;
  }

  public void fromBundle(Bundle paramBundle)
  {
    super.fromBundle(paramBundle);
    this.a = paramBundle.getString("_mqqpay_payresp_paychanneltype");
    this.transactionId = paramBundle.getString("_mqqpay_payresp_transactionid");
    this.payTime = paramBundle.getString("_mqqpay_payresp_paytime");
    this.totalFee = paramBundle.getString("_mqqpay_payresp_totalfee");
    this.callbackUrl = paramBundle.getString("_mqqpay_payresp_callbackurl");
    this.spData = paramBundle.getString("_mqqpay_payresp_spdata");
    this.serialNumber = paramBundle.getString("_mqqpay_payapi_serialnumber");
  }

  public boolean isPayByWeChat()
  {
    if (TextUtils.isEmpty(this.a));
    do
      return false;
    while (this.a.compareTo("1") != 0);
    return true;
  }

  public void toBundle(Bundle paramBundle)
  {
    super.toBundle(paramBundle);
    paramBundle.putString("_mqqpay_payresp_paychanneltype", this.a);
    paramBundle.putString("_mqqpay_payresp_transactionid", this.transactionId);
    paramBundle.putString("_mqqpay_payresp_paytime", this.payTime);
    paramBundle.putString("_mqqpay_payresp_totalfee", this.totalFee);
    paramBundle.putString("_mqqpay_payresp_callbackurl", this.callbackUrl);
    paramBundle.putString("_mqqpay_payresp_spdata", this.spData);
    paramBundle.putString("_mqqpay_payapi_serialnumber", this.serialNumber);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.data.pay.PayResponse
 * JD-Core Version:    0.6.0
 */