package com.tencent.mobileqq.openpay.data.base;

import android.os.Bundle;

public abstract class BaseApi
{
  public String appId;
  protected String appName = "native";
  protected final String appType = "native";
  protected final String sdkVersion = "1.1.0";

  public abstract boolean checkParams();

  public void fromBundle(Bundle paramBundle)
  {
    this.appId = paramBundle.getString("_mqqpay_baseapi_appid");
    this.appName = paramBundle.getString("_mqqpay_baseapi_appname");
  }

  public abstract int getApiMark();

  public abstract String getApiName();

  public void toBundle(Bundle paramBundle)
  {
    paramBundle.putString("_mqqpay_baseapi_appid", this.appId);
    paramBundle.putString("_mqqpay_baseapi_appname", this.appName);
    paramBundle.putString("_mqqpay_baseapi_apptype", "native");
    paramBundle.putString("_mqqpay_baseapi_sdkversion", "1.1.0");
    paramBundle.putString("_mqqpay_baseapi_apiname", getApiName());
    paramBundle.putInt("_mqqpay_baseapi_apimark", getApiMark());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.data.base.BaseApi
 * JD-Core Version:    0.6.0
 */