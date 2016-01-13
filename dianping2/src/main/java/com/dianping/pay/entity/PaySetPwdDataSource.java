package com.dianping.pay.entity;

import android.os.Bundle;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.pay.utils.WebankManager;
import com.dianping.util.DeviceUtils;
import java.util.ArrayList;

public class PaySetPwdDataSource
{
  public static final int RESULT_PAY_FAIL = 17001;
  public static final int RESULT_PAY_SESSION_INVALID = 17004;
  public static final int RESULT_PAY_SUCCESS = 17000;
  public static final int RESULT_SET_PWD_FAIL = 17003;
  public static final int RESULT_SET_PWD_SUCCESS = 17002;
  static final String SET_PASSWORD_URL = "http://api.p.dianping.com/quickpay/setquickpaypassword.pay";
  public static final int SOURCE_BIND_CARD = 5;
  public static final int SOURCE_FIRST_BIND = 1;
  public static final int SOURCE_SECOND_BIND = 2;
  public static final int SOURCE_SETTING_FORGET = 4;
  public static final int SOURCE_SETTING_MODIFY = 3;
  static final String TAG = PaySetPwdDataSource.class.getSimpleName();
  NovaActivity activity;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PaySetPwdDataSource.1(this);
  public String password;
  public String paySessionId;
  public SetPasswordListener setPwdListener;
  MApiRequest setPwdReq;
  public int source;

  public PaySetPwdDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
  }

  private boolean isCommitPay()
  {
    return (1 == this.source) || (2 == this.source) || (5 == this.source);
  }

  public void releaseRequests()
  {
    if (this.setPwdReq != null)
    {
      this.activity.mapiService().abort(this.setPwdReq, this.mapiHandler, true);
      this.setPwdReq = null;
    }
  }

  public void reqSetPwd()
  {
    if (this.setPwdReq != null)
      this.activity.mapiService().abort(this.setPwdReq, this.mapiHandler, true);
    int i = this.activity.accountService().profile().getInt("UserID");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("iscommitpay");
    localArrayList.add(String.valueOf(isCommitPay()));
    localArrayList.add("password");
    localArrayList.add(WebankManager.encryptSetPwd(String.valueOf(i), this.password));
    localArrayList.add("source");
    localArrayList.add(String.valueOf(this.source));
    if (isCommitPay())
      this.activity.showProgressDialog("正在支付...");
    this.setPwdReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/setquickpaypassword.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.setPwdReq, this.mapiHandler);
  }

  public void restoreData(Bundle paramBundle)
  {
    this.password = paramBundle.getString("password");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("password", this.password);
  }

  public static abstract interface SetPasswordListener
  {
    public abstract void onSetPwdFail();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PaySetPwdDataSource
 * JD-Core Version:    0.6.0
 */