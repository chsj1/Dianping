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

public class PayBindDataSource
{
  static final String BIND_CARD_URL = "http://api.p.dianping.com/quickpay/bindquickpaybankcard.pay";
  public static final int BIND_PAY_FAIL = 14001;
  public static final int BIND_PAY_SESSION_INVALID = 14005;
  public static final int BIND_PAY_SUCCESS = 14000;
  public static final int BIND_PROCESS_FAIL = 14006;
  public static final int BIND_SET_PWD_FIRST = 14003;
  public static final int BIND_SET_PWD_SECOND = 14002;
  public static final int BIND_VERIFY_CODE_FAIL = 14004;
  public static final int SEND_BANK_SMS_FAIL = 24001;
  public static final int SEND_BANK_SMS_SESSION_INVALID = 24002;
  public static final int SEND_BANK_SMS_SUCCESS = 24000;
  static final String SEND_BANK_SMS_URL = "http://api.p.dianping.com/quickpay/sendbanksms.pay";
  public static final int SOURCE_FIRST_BIND = 1;
  public static final int SOURCE_SECOND_BIND = 2;
  static final String TAG = PayBindDataSource.class.getSimpleName();
  public static final int VERIFY_INFO_FAIL = 13001;
  public static final int VERIFY_INFO_SESSION_INVALID = 13002;
  public static final int VERIFY_INFO_SUCCESS = 13000;
  static final String VERIFY_USER_INFO_URL = "http://api.p.dianping.com/quickpay/verifyquickpayuserinfo.pay";
  NovaActivity activity;
  public String bankCardNumber;
  public DPObject bankElement;
  MApiRequest bindCardReq;
  public Boolean bindInfoChecked;
  public BindResultListener bindResultListener;
  public String idCardNumber;
  public Boolean isCommitPay;
  public Boolean isVerifySuccuss;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayBindDataSource.1(this);
  public String mobileNumber;
  public String paySessionId;
  public String realName;
  MApiRequest sendBankSmsReq;
  public int source;
  public String verifyAccountId;
  public String verifyCode;
  MApiRequest verifyUserInfoReq;

  public PayBindDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    this.isVerifySuccuss = Boolean.valueOf(false);
    this.bindInfoChecked = Boolean.valueOf(false);
  }

  public void releaseRequests()
  {
    if (this.verifyUserInfoReq != null)
    {
      this.activity.mapiService().abort(this.verifyUserInfoReq, this.mapiHandler, true);
      this.verifyUserInfoReq = null;
    }
    if (this.sendBankSmsReq != null)
    {
      this.activity.mapiService().abort(this.sendBankSmsReq, this.mapiHandler, true);
      this.sendBankSmsReq = null;
    }
    if (this.bindCardReq != null)
    {
      this.activity.mapiService().abort(this.bindCardReq, this.mapiHandler, true);
      this.bindCardReq = null;
    }
  }

  public void reqBindCard()
  {
    if (this.bindCardReq != null)
      this.activity.mapiService().abort(this.bindCardReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("verifycode");
    localArrayList.add(this.verifyCode);
    localArrayList.add("verifyaccountid");
    localArrayList.add(this.verifyAccountId);
    localArrayList.add("iscommitpay");
    localArrayList.add(String.valueOf(this.isCommitPay));
    this.bindCardReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/bindquickpaybankcard.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.bindCardReq, this.mapiHandler);
  }

  public void reqSendBankSms()
  {
    if (this.sendBankSmsReq != null)
      this.activity.mapiService().abort(this.sendBankSmsReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("paysessionid");
    localArrayList.add(this.paySessionId);
    localArrayList.add("verifyaccountid");
    localArrayList.add(this.verifyAccountId);
    this.sendBankSmsReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/sendbanksms.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.sendBankSmsReq, this.mapiHandler);
  }

  public void reqVerifyInfo()
  {
    if (this.verifyUserInfoReq != null)
      this.activity.mapiService().abort(this.verifyUserInfoReq, this.mapiHandler, true);
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
    localArrayList.add(WebankManager.encryptByRsa(this.mobileNumber));
    localArrayList.add("idcardnumber");
    localArrayList.add(WebankManager.encryptByRsa(this.idCardNumber));
    localArrayList.add("realname");
    localArrayList.add(WebankManager.encryptByRsa(this.realName));
    localArrayList.add("bankcardnumber");
    localArrayList.add(WebankManager.encryptByRsa(this.bankCardNumber));
    this.verifyUserInfoReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/quickpay/verifyquickpayuserinfo.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.verifyUserInfoReq, this.mapiHandler);
  }

  public void restoreData(Bundle paramBundle)
  {
    this.verifyAccountId = paramBundle.getString("verifyAccountId");
    this.isVerifySuccuss = Boolean.valueOf(paramBundle.getBoolean("isVerifySuccuss"));
    this.bindInfoChecked = Boolean.valueOf(paramBundle.getBoolean("bindInfoChecked"));
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("verifyAccountId", this.verifyAccountId);
    paramBundle.putBoolean("isVerifySuccuss", this.isVerifySuccuss.booleanValue());
    paramBundle.putBoolean("bindInfoChecked", this.bindInfoChecked.booleanValue());
  }

  public static abstract interface BindResultListener
  {
    public abstract void verifySuccess();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PayBindDataSource
 * JD-Core Version:    0.6.0
 */