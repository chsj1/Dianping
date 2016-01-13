package com.dianping.pay.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.pay.entity.PayVerifySmsDataSource;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class PayVerifySmsActivity extends NovaActivity
  implements TextWatcher
{
  protected NovaButton btnSubmit;
  protected NovaButton btnVerfiyCodeGet;
  protected PayVerifySmsDataSource dataSource;
  protected EditText editVerifyCode;
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (PayVerifySmsActivity.this.mLastTime == 0)
      {
        PayVerifySmsActivity.this.resetButton();
        return;
      }
      PayVerifySmsActivity.this.mHandler.sendEmptyMessageDelayed(0, 1000L);
      paramMessage = PayVerifySmsActivity.this;
      paramMessage.mLastTime -= 1;
      PayVerifySmsActivity.this.btnVerfiyCodeGet.setText("重新发送(" + PayVerifySmsActivity.this.mLastTime + ")");
    }
  };
  protected int mLastTime = 60;
  protected TextView textSmsPrompt;

  private void initView()
  {
    phoneMasked(this.dataSource.mobileNo);
    this.textSmsPrompt = ((TextView)findViewById(R.id.sms_prompt));
    this.textSmsPrompt.setText("为保证您账户安全，已向预留手机" + phoneMasked(this.dataSource.mobileNo) + "发送了校验短信");
    this.editVerifyCode = ((EditText)findViewById(R.id.webank_verify_code));
    this.editVerifyCode.addTextChangedListener(this);
    this.btnVerfiyCodeGet = ((NovaButton)findViewById(R.id.webank_verify_code_get));
    this.btnVerfiyCodeGet.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayVerifySmsActivity.this.requestPaySms();
      }
    });
    this.btnSubmit = ((NovaButton)findViewById(R.id.submit));
    this.btnSubmit.setEnabled(false);
    this.btnSubmit.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayVerifySmsActivity.this.dataSource.verifyCode = PayVerifySmsActivity.this.editVerifyCode.getText().toString().trim();
        PayVerifySmsActivity.this.dataSource.reqVerifySms();
      }
    });
    requestPaySms();
  }

  private String phoneMasked(String paramString)
  {
    try
    {
      if (!TextUtils.isEmpty(paramString))
      {
        String str = paramString.substring(0, 3) + "****" + paramString.substring(7);
        return str;
      }
    }
    catch (Exception localException)
    {
    }
    return paramString;
  }

  private void requestPaySms()
  {
    this.dataSource.reqPaySms();
    this.mHandler.sendEmptyMessageDelayed(0, 1000L);
    this.btnVerfiyCodeGet.setEnabled(false);
    this.btnVerfiyCodeGet.setText("重新发送(" + this.mLastTime + ")");
  }

  private void resetButton()
  {
    this.btnVerfiyCodeGet.setEnabled(true);
    this.mLastTime = 60;
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0);
    this.btnVerfiyCodeGet.setText("获取验证码");
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.pay_webank_verify_sms_activity);
    this.dataSource = new PayVerifySmsDataSource(this);
    this.dataSource.paySessionId = getStringParam("paysessionid");
    this.dataSource.mobileNo = getStringParam("mobileno");
    initView();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!TextUtils.isEmpty(this.editVerifyCode.getText().toString().trim()))
    {
      this.btnSubmit.setEnabled(true);
      return;
    }
    this.btnSubmit.setEnabled(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayVerifySmsActivity
 * JD-Core Version:    0.6.0
 */