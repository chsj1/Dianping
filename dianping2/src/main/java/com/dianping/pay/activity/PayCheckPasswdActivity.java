package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.pay.utils.WebankManager;
import com.dianping.pay.view.GridPasswordView;
import com.dianping.pay.view.GridPasswordView.OnPasswordChangedListener;
import com.dianping.pay.view.NumberKeyboardView;
import com.dianping.pay.view.NumberKeyboardView.OnNumKeyboardListener;
import com.dianping.util.DeviceUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PayCheckPasswdActivity extends NovaActivity
  implements RequestHandler, View.OnClickListener
{
  private static final String ACTION_MODIFYPWD = "modifypwd";
  private static final String ACTION_UNBIND = "unbind";
  private String mAction;
  private String mCardId;
  private GridPasswordView mGridPwd;
  private NumberKeyboardView mNumberKeyboard;
  private String mSessionId;
  private MApiRequest mUnbindRequest;
  private MApiRequest mVerifyRequest;

  private void setupViews()
  {
    setContentView(R.layout.pay_activity_check_pwd);
    TextView localTextView = (TextView)findViewById(R.id.tv_input_hint);
    if ("unbind".equals(this.mAction))
    {
      setTitle("解绑银行卡");
      localTextView.setText("请输入支付密码");
    }
    while (true)
    {
      ((TextView)findViewById(R.id.tv_forget_pwd)).setOnClickListener(this);
      this.mGridPwd = ((GridPasswordView)findViewById(R.id.grid_password));
      this.mGridPwd.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener()
      {
        public void onChanged(String paramString)
        {
        }

        public void onMaxLength(String paramString)
        {
          if ("unbind".equals(PayCheckPasswdActivity.this.mAction))
          {
            PayCheckPasswdActivity.this.showProgressDialog("正在解绑...");
            PayCheckPasswdActivity.this.unbindCard(paramString);
          }
          do
            return;
          while (!"modifypwd".equals(PayCheckPasswdActivity.this.mAction));
          PayCheckPasswdActivity.this.showProgressDialog("验证中...");
          PayCheckPasswdActivity.this.verifyPasswd(paramString);
        }
      });
      this.mNumberKeyboard = ((NumberKeyboardView)findViewById(R.id.num_keyboard));
      this.mNumberKeyboard.setKeyboardInputListener(new NumberKeyboardView.OnNumKeyboardListener()
      {
        public void onKeyChanged(int paramInt)
        {
          PayCheckPasswdActivity.this.mGridPwd.keyChanged(paramInt);
        }
      });
      return;
      if (!"modifypwd".equals(this.mAction))
        continue;
      setTitle("修改支付密码");
      localTextView.setText("为验证身份，请输入当前支付密码");
    }
  }

  private void unbindCard(String paramString)
  {
    if (this.mUnbindRequest != null)
      return;
    int i = accountService().profile().getInt("UserID");
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/quickpay/unbindquickpaybankcard.pay");
    localUrlBuilder.addParam("cx", DeviceUtils.cxInfo("payorder"));
    localUrlBuilder.addParam("password", WebankManager.encryptVerifyPwd(String.valueOf(i), paramString, this.mSessionId));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("quickpaycontractid", this.mCardId);
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    localUrlBuilder.addParam("paysessionid", this.mSessionId);
    this.mUnbindRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mUnbindRequest, this);
  }

  private void verifyPasswd(String paramString)
  {
    if (this.mVerifyRequest != null)
      return;
    int i = accountService().profile().getInt("UserID");
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://api.p.dianping.com/quickpay/verifyquickpaypassword.pay");
    localUrlBuilder.addParam("cx", DeviceUtils.cxInfo("payorder"));
    localUrlBuilder.addParam("password", WebankManager.encryptVerifyPwd(String.valueOf(i), paramString, this.mSessionId));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("cityid", Integer.valueOf(city().id()));
    localUrlBuilder.addParam("source", Integer.valueOf(3));
    localUrlBuilder.addParam("paysessionid", this.mSessionId);
    this.mVerifyRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mVerifyRequest, this);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.tv_forget_pwd)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist?state=forgot_pwd"));
      paramView.addFlags(67108864);
      startActivity(paramView);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAction = getStringParam("action");
    this.mSessionId = getStringParam("paysessionid");
    this.mCardId = getStringParam("cardid");
    if (TextUtils.isEmpty(this.mAction))
      this.mAction = "unbind";
    setupViews();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    dismissDialog();
    if (paramRequest == this.mVerifyRequest)
      this.mVerifyRequest = null;
    while (true)
    {
      this.mGridPwd.clearPassword();
      showToast("网络问题，请稍后重试");
      return;
      if (paramRequest != this.mUnbindRequest)
        continue;
      this.mUnbindRequest = null;
    }
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    dismissDialog();
    Object localObject = (DPObject)paramResponse.result();
    int i = ((DPObject)localObject).getInt("ResultCode");
    paramResponse = ((DPObject)localObject).getString("ResultTitle");
    localObject = ((DPObject)localObject).getString("ResultMsg");
    if (paramRequest == this.mUnbindRequest)
    {
      this.mUnbindRequest = null;
      switch (i)
      {
      case 12001:
      default:
        showAlertDialog(paramResponse, (String)localObject);
      case 12000:
      case 12002:
      }
    }
    while (true)
    {
      this.mGridPwd.clearPassword();
      return;
      showToast((String)localObject);
      paramRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
      paramRequest.addFlags(67108864);
      startActivity(paramRequest);
      continue;
      showToast((String)localObject);
      paramRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
      paramRequest.addFlags(67108864);
      startActivity(paramRequest);
      continue;
      if (paramRequest != this.mVerifyRequest)
        continue;
      this.mVerifyRequest = null;
      switch (i)
      {
      default:
        showAlertDialog(paramResponse, (String)localObject);
        break;
      case 18003:
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://paywebanksetpassword?paysessionid=" + this.mSessionId + "&source=3")));
        finish();
        break;
      case 18004:
        showToast((String)localObject);
        paramRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://paycardlist"));
        paramRequest.addFlags(67108864);
        startActivity(paramRequest);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayCheckPasswdActivity
 * JD-Core Version:    0.6.0
 */