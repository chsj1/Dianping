package com.dianping.pay.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.pay.entity.PayVerifyBindDataSource;
import com.dianping.pay.entity.PayVerifyBindDataSource.ResultListener;
import com.dianping.pay.view.WebankBindInfoView;
import com.dianping.pay.view.WebankBindInfoView.VerifyCodeListener;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class PayVerfiyBindActivity extends NovaActivity
  implements View.OnClickListener, PayVerifyBindDataSource.ResultListener
{
  protected WebankBindInfoView bindInfoView;
  protected NovaButton btnSubmit;
  protected PayVerifyBindDataSource dataSource;
  protected LinearLayout promptLayer;
  protected TextView textPromptInfo;

  private void initView()
  {
    this.textPromptInfo = ((TextView)findViewById(R.id.prompt_info));
    this.bindInfoView = ((WebankBindInfoView)findViewById(R.id.bind_info_layer));
    this.bindInfoView.setEditHints(this.dataSource.bankElement, this.dataSource.source);
    this.bindInfoView.setListener(new WebankBindInfoView.VerifyCodeListener()
    {
      public void onBtnClick()
      {
        if (PayVerfiyBindActivity.this.dataSource.isVerifySuccuss.booleanValue())
        {
          PayVerfiyBindActivity.this.bindInfoView.resetVerifyBtnSeconds();
          PayVerfiyBindActivity.this.dataSource.reqSendSms();
          return;
        }
        PayVerfiyBindActivity.this.bindInfoView.updateToVerifyBindDataSource(PayVerfiyBindActivity.this.dataSource);
        PayVerfiyBindActivity.this.dataSource.reqResetVerifyUser();
      }

      public void setSubmitBtn(boolean paramBoolean)
      {
        PayVerfiyBindActivity.this.btnSubmit.setEnabled(paramBoolean);
      }
    });
    this.btnSubmit = ((NovaButton)findViewById(R.id.submit));
    this.btnSubmit.setEnabled(false);
    this.btnSubmit.setOnClickListener(this);
    if (1 == this.dataSource.source)
    {
      this.btnSubmit.setGAString("button_verifyDpMessage_pay");
      this.dataSource.isCommitPay = Boolean.valueOf(true);
      setTitle("银行卡支付");
      this.textPromptInfo.setText("因您尚未设置支付密码，需重新校验银行卡信息");
    }
    do
      return;
    while (2 != this.dataSource.source);
    this.btnSubmit.setGAString("button_verifyDpMessage_setPass");
    this.dataSource.isCommitPay = Boolean.valueOf(false);
    setTitle("找回支付密码");
    Object localObject2 = "填写";
    if (!TextUtils.isEmpty(this.dataSource.bankElement.getString("BankName")))
      localObject2 = "填写" + this.dataSource.bankElement.getString("BankName");
    Object localObject1;
    if (1 == this.dataSource.bankElement.getInt("CardType"))
      localObject1 = (String)localObject2 + "|储蓄卡";
    while (true)
    {
      localObject2 = localObject1;
      if (!TextUtils.isEmpty(this.dataSource.bankElement.getString("TailNumber")))
        localObject2 = (String)localObject1 + "(" + this.dataSource.bankElement.getString("TailNumber") + ")";
      localObject1 = (String)localObject2 + "的信息用于验证";
      this.textPromptInfo.setText((CharSequence)localObject1);
      return;
      localObject1 = localObject2;
      if (2 != this.dataSource.bankElement.getInt("CardType"))
        continue;
      localObject1 = (String)localObject2 + "|信用卡";
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.submit)
    {
      this.bindInfoView.updateToVerifyBindDataSource(this.dataSource);
      this.dataSource.reqResetVerifySms();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.pay_webank_verify_bind_activity);
    this.dataSource = new PayVerifyBindDataSource(this);
    this.dataSource.resultListener = this;
    this.dataSource.source = getIntParam("source");
    this.dataSource.paySessionId = getStringParam("paysessionid");
    this.dataSource.bankElement = getObjectParam("bankelement");
    if (this.dataSource.bankElement == null)
    {
      Toast.makeText(this, "找回密码参数错误！", 0).show();
      finish();
      return;
    }
    this.dataSource.quickPayContractId = this.dataSource.bankElement.getString("PaymentID");
    initView();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  public void verifySuccess()
  {
    this.bindInfoView.setVerifySuccuss();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayVerfiyBindActivity
 * JD-Core Version:    0.6.0
 */