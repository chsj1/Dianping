package com.tencent.mobileqq.openpay.data.base;

import android.os.Bundle;

public abstract class BaseResponse
{
  protected final int RET_CODE_SUCESS = 0;
  protected final int RET_CODE_UNKNOW = -9999999;
  public int apiMark;
  public String apiName;
  public int retCode = -9999999;
  public String retMsg;

  public abstract boolean checkParams();

  public void fromBundle(Bundle paramBundle)
  {
    this.retCode = paramBundle.getInt("_mqqpay_baseresp_retcode");
    this.retMsg = paramBundle.getString("_mqqpay_baseresp_retmsg");
    this.apiName = paramBundle.getString("_mqqpay_baseapi_apiname");
    this.apiMark = paramBundle.getInt("_mqqpay_baseapi_apimark");
  }

  public boolean isSuccess()
  {
    return this.retCode == 0;
  }

  public void toBundle(Bundle paramBundle)
  {
    paramBundle.putInt("_mqqpay_baseresp_retcode", this.retCode);
    paramBundle.putString("_mqqpay_baseresp_retmsg", this.retMsg);
    paramBundle.putString("_mqqpay_baseapi_apiname", this.apiName);
    paramBundle.putInt("_mqqpay_baseapi_apimark", this.apiMark);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.data.base.BaseResponse
 * JD-Core Version:    0.6.0
 */