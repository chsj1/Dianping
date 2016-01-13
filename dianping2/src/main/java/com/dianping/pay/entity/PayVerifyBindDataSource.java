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

public class PayVerifyBindDataSource
{
  static final String RESET_VERIFY_SMS_URL = "http://api.p.dianping.com/cashier/verifypaysms.pay";
  static final String RESET_VERIFY_USER_URL = "http://api.p.dianping.com/quickpay/resetquickpayinfoverify.pay";
  static final String SEND_SMS_URL = "http://api.p.dianping.com/cashier/sendpaysms.pay";
  public static final int SOURCE_RESET_FORGET = 2;
  public static final int SOURCE_RESET_PAY_PROCESS = 1;
  public static final int SOURCE_VERIFY_SMS_RESET = 1;
  static final String TAG = PayBindDataSource.class.getSimpleName();
  public static final int VERIFY_SESSION_INVALID = 15002;
  public static final int VERIFY_SMS_FAIL = 15003;
  public static final int VERIFY_USER_FAIL = 15001;
  public static final int VERIFY_USER_SUCCESS = 15000;
  NovaActivity activity;
  public String bankCardNumber;
  public DPObject bankElement;
  public String idCardNumber;
  public Boolean isCommitPay;
  public Boolean isVerifySuccuss;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayVerifyBindDataSource.1(this);
  public String mobileNumber;
  public String paySessionId;
  public String quickPayContractId;
  public String realName;
  MApiRequest resetVerifySmsReq;
  MApiRequest resetVerifyUserReq;
  public ResultListener resultListener;
  MApiRequest sendSmsReq;
  public int source;
  public String verifyCode;

  public PayVerifyBindDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    this.isVerifySuccuss = Boolean.valueOf(false);
  }

  public void releaseRequests()
  {
    if (this.resetVerifyUserReq != null)
    {
      this.activity.mapiService().abort(this.resetVerifyUserReq, this.mapiHandler, true);
      this.resetVerifyUserReq = null;
    }
    if (this.sendSmsReq != null)
    {
      this.activity.mapiService().abort(this.sendSmsReq, this.mapiHandler, true);
      this.sendSmsReq = null;
    }
    if (this.resetVerifySmsReq != null)
    {
      this.activity.mapiService().abort(this.resetVerifySmsReq, this.mapiHandler, true);
      this.resetVerifySmsReq = null;
    }
  }

  public void reqResetVerifySms()
  {
    if (this.resetVerifySmsReq != null)
      this.activity.mapiService().abort(this.resetVerifySmsReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("source");
    String str = null;
    boolean bool;
    if (1 == this.source)
    {
      str = String.valueOf(3);
      localArrayList.add(str);
      localArrayList.add("verifycode");
      localArrayList.add(this.verifyCode);
      localArrayList.add("mobilenumber");
      localArrayList.add(this.mobileNumber);
      localArrayList.add("iscommit");
      bool = false;
      if (1 != this.source)
        break label263;
      bool = true;
    }
    while (true)
    {
      localArrayList.add(String.valueOf(bool));
      this.resetVerifySmsReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/cashier/verifypaysms.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      this.activity.mapiService().exec(this.resetVerifySmsReq, this.mapiHandler);
      return;
      if (2 != this.source)
        break;
      str = String.valueOf(1);
      break;
      label263: if (2 != this.source)
        continue;
      bool = false;
    }
  }

  public void reqResetVerifyUser()
  {
    if (this.resetVerifyUserReq != null)
      this.activity.mapiService().abort(this.resetVerifyUserReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("realname");
    localArrayList.add(WebankManager.encryptByRsa(this.realName));
    localArrayList.add("mobilenumber");
    localArrayList.add(WebankManager.encryptByRsa(this.mobileNumber));
    localArrayList.add("idcardnumber");
    localArrayList.add(WebankManager.encryptByRsa(this.idCardNumber));
    localArrayList.add("bankcardnumber");
    localArrayList.add(WebankManager.encryptByRsa(this.bankCardNumber));
    localArrayList.add("source");
    localArrayList.add(String.valueOf(this.source));
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("quickpaycontractid");
    localArrayList.add(this.quickPayContractId);
    this.resetVerifyUserReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/resetquickpayinfoverify.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.resetVerifyUserReq, this.mapiHandler);
  }

  public void reqSendSms()
  {
    if (this.sendSmsReq != null)
      this.activity.mapiService().abort(this.sendSmsReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mobileno");
    localArrayList.add(this.mobileNumber);
    localArrayList.add("source");
    localArrayList.add(String.valueOf(1));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    this.sendSmsReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/cashier/sendpaysms.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.sendSmsReq, this.mapiHandler);
  }

  public void restoreData(Bundle paramBundle)
  {
    this.source = paramBundle.getInt("source");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putBoolean("isVerifySuccuss", this.isVerifySuccuss.booleanValue());
    this.isVerifySuccuss = Boolean.valueOf(paramBundle.getBoolean("isVerifySuccuss"));
  }

  public static abstract interface ResultListener
  {
    public abstract void verifySuccess();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PayVerifyBindDataSource
 * JD-Core Version:    0.6.0
 */