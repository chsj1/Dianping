package com.dianping.pay.entity;

import android.app.ProgressDialog;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.pay.utils.WebankManager;
import com.dianping.util.DeviceUtils;
import java.util.ArrayList;

public class PayVerifyPwdDataSource
{
  public static final int RESULT_PAY_FAIL = 18001;
  public static final int RESULT_PAY_SESSION_INVALID = 18004;
  public static final int RESULT_PAY_SUCCESS = 18000;
  public static final int RESULT_PWD_SUCCESS_MODIFY = 18003;
  public static final int RESULT_PWD_SUCCESS_PAY = 18005;
  public static final int RESULT_VERIFY_PWD_FAIL = 18002;
  static final String SET_PASSWORD_URL = "http://api.p.dianping.com/quickpay/verifyquickpaypassword.pay";
  public static final int SOURCE_MODIFY_PWD = 3;
  public static final int SOURCE_PAY_PWD = 1;
  public static final int SOURCE_PAY_PWD_SMS = 2;
  DPActivity activity;
  private ProgressDialog dlg;
  public Boolean isCommitPay = Boolean.valueOf(true);
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayVerifyPwdDataSource.1(this);
  public String mobileNo;
  public String password;
  public String paySessionId;
  public int source;
  public VerifyPasswordListener verifyPwdListener;
  MApiRequest verifyPwdReq;

  public PayVerifyPwdDataSource(DPActivity paramDPActivity)
  {
    this.activity = paramDPActivity;
  }

  private void dismissDialog()
  {
    if (this.dlg != null)
    {
      this.dlg.dismiss();
      this.dlg = null;
    }
  }

  private void showProgressDialog(String paramString)
  {
    this.dlg = new ProgressDialog(this.activity, 3);
    this.dlg.setMessage(paramString);
    this.dlg.show();
  }

  public void releaseRequests()
  {
    if (this.verifyPwdReq != null)
    {
      this.activity.mapiService().abort(this.verifyPwdReq, this.mapiHandler, true);
      this.verifyPwdReq = null;
    }
  }

  public void reqVerifyPwd()
  {
    if (this.verifyPwdReq != null)
      this.activity.mapiService().abort(this.verifyPwdReq, this.mapiHandler, true);
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
    localArrayList.add(String.valueOf(this.isCommitPay));
    localArrayList.add("password");
    localArrayList.add(WebankManager.encryptVerifyPwd(String.valueOf(i), this.password, this.paySessionId));
    localArrayList.add("source");
    localArrayList.add(String.valueOf(this.source));
    this.verifyPwdReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/verifyquickpaypassword.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.verifyPwdReq, this.mapiHandler);
  }

  public static abstract interface VerifyPasswordListener
  {
    public abstract void onClearPwd();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PayVerifyPwdDataSource
 * JD-Core Version:    0.6.0
 */