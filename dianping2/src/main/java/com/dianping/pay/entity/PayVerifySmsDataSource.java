package com.dianping.pay.entity;

import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DeviceUtils;
import java.util.ArrayList;

public class PayVerifySmsDataSource
{
  public static final int RESULT_VERIFY_SMS_FAIL_TO_ALERT = 21000;
  public static final int RESULT_VERIFY_SMS_FAIL_TO_PAY = 21001;
  public static final int RESULT_VERIFY_SMS_FAIL_TO_SETTING = 21004;
  public static final int RESULT_VERIFY_SMS_PAY_SUCCESS = 21002;
  public static final int RESULT_VERIFY_SMS_SET_PWD = 21003;
  static final String SEND_PAY_SMS_URL = "http://api.p.dianping.com/cashier/sendpaysms.pay";
  public static final int SOURCE_SEND_SMS_PAY = 2;
  public static final int SOURCE_SEND_SMS_RESET = 1;
  public static final int SOURCE_VERIFY_SMS_PAY = 2;
  public static final int SOURCE_VERIFY_SMS_RESET_PAY = 3;
  public static final int SOURCE_VERIFY_SMS_RESET_SETTING = 1;
  static final String TAG = PayVerifySmsDataSource.class.getSimpleName();
  static final String VERIFY_PAY_SMS_URL = "http://api.p.dianping.com/cashier/verifypaysms.pay";
  NovaActivity activity;
  public Boolean isCommitPay = Boolean.valueOf(true);
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayVerifySmsDataSource.1(this);
  public String mobileNo;
  public String paySessionId;
  MApiRequest sendPaySmsReq;
  public String verifyCode;
  MApiRequest verifyPaySmsReq;

  public PayVerifySmsDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
  }

  public void releaseRequests()
  {
    if (this.sendPaySmsReq != null)
    {
      this.activity.mapiService().abort(this.sendPaySmsReq, this.mapiHandler, true);
      this.sendPaySmsReq = null;
    }
    if (this.verifyPaySmsReq != null)
    {
      this.activity.mapiService().abort(this.verifyPaySmsReq, this.mapiHandler, true);
      this.verifyPaySmsReq = null;
    }
  }

  public void reqPaySms()
  {
    if (this.sendPaySmsReq != null)
      this.activity.mapiService().abort(this.sendPaySmsReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mobileno");
    localArrayList.add(this.mobileNo);
    localArrayList.add("source");
    localArrayList.add(String.valueOf(2));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    this.sendPaySmsReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/cashier/sendpaysms.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.sendPaySmsReq, this.mapiHandler);
  }

  public void reqVerifySms()
  {
    if (this.verifyPaySmsReq != null)
      this.activity.mapiService().abort(this.verifyPaySmsReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("mobilenumber");
    localArrayList.add(this.mobileNo);
    localArrayList.add("source");
    localArrayList.add(String.valueOf(2));
    localArrayList.add("verifycode");
    localArrayList.add(this.verifyCode);
    localArrayList.add("iscommit");
    localArrayList.add(String.valueOf(this.isCommitPay));
    this.activity.showProgressDialog("正在支付...");
    this.verifyPaySmsReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/cashier/verifypaysms.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.verifyPaySmsReq, this.mapiHandler);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PayVerifySmsDataSource
 * JD-Core Version:    0.6.0
 */