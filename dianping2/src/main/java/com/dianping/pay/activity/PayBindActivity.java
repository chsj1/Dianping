package com.dianping.pay.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.pay.entity.PayBindDataSource;
import com.dianping.pay.entity.PayBindDataSource.BindResultListener;
import com.dianping.pay.utils.WebankManager;
import com.dianping.pay.view.WebankBindInfoView;
import com.dianping.pay.view.WebankBindInfoView.VerifyCodeListener;
import com.dianping.pay.view.WebankBindStepView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaCheckBox;
import com.dianping.widget.view.NovaTextView;

public class PayBindActivity extends NovaActivity
  implements View.OnClickListener, PayBindDataSource.BindResultListener
{
  public static final int CARD_TYPE_CREDIT = 2;
  public static final int CARD_TYPE_DEPOSIT = 1;
  protected WebankBindInfoView bindInfoView;
  protected NovaButton btnSubmit;
  protected NovaCheckBox checkProtocol;
  protected PayBindDataSource dataSource;
  protected NetworkImageView imageCardInfoIcon;
  protected ImageButton imageUserNameHelp;
  protected WebankBindStepView layerBindStep;
  protected RelativeLayout layerBindUser;
  protected TextView textBindUserName;
  protected TextView textCardInfoName;
  protected TextView textCardInfoType;
  protected TextView textProtocolAnd;
  protected NovaTextView textProtocolDp;
  protected NovaTextView textProtocolWebank;
  protected View viewEmptyBanner;

  private void initView()
  {
    this.layerBindStep = ((WebankBindStepView)findViewById(R.id.bind_step_layer));
    this.layerBindStep.setStep(1);
    this.imageCardInfoIcon = ((NetworkImageView)findViewById(R.id.card_info_icon));
    if (!TextUtils.isEmpty(this.dataSource.bankElement.getString("IconUrl")))
      this.imageCardInfoIcon.setImage(this.dataSource.bankElement.getString("IconUrl"));
    this.textCardInfoName = ((TextView)findViewById(R.id.card_info_name));
    if (!TextUtils.isEmpty(this.dataSource.bankElement.getString("BankName")))
      this.textCardInfoName.setText(this.dataSource.bankElement.getString("BankName"));
    this.textCardInfoType = ((TextView)findViewById(R.id.card_info_type));
    if (this.dataSource.bankElement.getInt("CardType") == 1)
    {
      this.textCardInfoType.setText("储蓄卡");
      this.layerBindUser = ((RelativeLayout)findViewById(R.id.bind_user_layer));
      this.textBindUserName = ((TextView)findViewById(R.id.bind_user_name));
      if (!TextUtils.isEmpty(this.dataSource.realName))
        this.textBindUserName.setText(this.dataSource.realName);
      this.imageUserNameHelp = ((ImageButton)findViewById(R.id.bind_user_name_help));
      this.imageUserNameHelp.setOnClickListener(this);
      this.viewEmptyBanner = findViewById(R.id.empty_banner);
      this.bindInfoView = ((WebankBindInfoView)findViewById(R.id.bind_info_layer));
      this.bindInfoView.setParamRealName(this.dataSource.realName);
      this.bindInfoView.setListener(new WebankBindInfoView.VerifyCodeListener()
      {
        public void onBtnClick()
        {
          if (PayBindActivity.this.dataSource.isVerifySuccuss.booleanValue())
          {
            PayBindActivity.this.bindInfoView.resetVerifyBtnSeconds();
            PayBindActivity.this.dataSource.reqSendBankSms();
            return;
          }
          PayBindActivity.this.bindInfoView.updateToBindDataSource(PayBindActivity.this.dataSource);
          PayBindActivity.this.dataSource.reqVerifyInfo();
        }

        public void setSubmitBtn(boolean paramBoolean)
        {
          PayBindActivity.this.dataSource.bindInfoChecked = Boolean.valueOf(paramBoolean);
          PayBindActivity.this.updateSubmitBtn();
        }
      });
      this.checkProtocol = ((NovaCheckBox)findViewById(R.id.check));
      this.checkProtocol.setOnClickListener(this);
      this.btnSubmit = ((NovaButton)findViewById(R.id.submit));
      this.btnSubmit.setEnabled(false);
      this.btnSubmit.setOnClickListener(this);
      this.btnSubmit.setGAString("button_bindCard_bind");
      if (1 != this.dataSource.source)
        break label542;
      this.dataSource.isCommitPay = Boolean.valueOf(false);
      this.layerBindStep.setVisibility(0);
      this.layerBindUser.setVisibility(8);
      this.bindInfoView.setUserLayer(true);
      this.viewEmptyBanner.setVisibility(0);
    }
    while (true)
    {
      this.textProtocolDp = ((NovaTextView)findViewById(R.id.protocol_dp));
      this.textProtocolDp.setOnClickListener(this);
      this.textProtocolDp.setGAString("link_serviceContract_bind");
      this.textProtocolWebank = ((NovaTextView)findViewById(R.id.protocol_webank));
      this.textProtocolWebank.setOnClickListener(this);
      this.textProtocolWebank.setGAString("link_serviceContract_bind");
      this.textProtocolAnd = ((TextView)findViewById(R.id.protocol_and));
      if (1 != this.dataSource.source)
        break label601;
      this.textProtocolWebank.setVisibility(0);
      this.textProtocolAnd.setVisibility(0);
      return;
      if (this.dataSource.bankElement.getInt("CardType") != 2)
        break;
      this.textCardInfoType.setText("信用卡");
      break;
      label542: if (2 != this.dataSource.source)
        continue;
      this.dataSource.isCommitPay = Boolean.valueOf(true);
      this.layerBindStep.setVisibility(8);
      this.layerBindUser.setVisibility(0);
      this.bindInfoView.setUserLayer(false);
      this.viewEmptyBanner.setVisibility(8);
    }
    label601: this.textProtocolWebank.setVisibility(8);
    this.textProtocolAnd.setVisibility(8);
  }

  private void updateSubmitBtn()
  {
    if ((this.dataSource.bindInfoChecked.booleanValue()) && (this.checkProtocol.isChecked()))
    {
      this.btnSubmit.setEnabled(true);
      return;
    }
    this.btnSubmit.setEnabled(false);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.submit)
    {
      this.bindInfoView.updateToBindDataSource(this.dataSource);
      this.dataSource.reqBindCard();
    }
    do
    {
      return;
      if (i == R.id.protocol_dp)
      {
        startActivity("dianping://web?url=http://h5.dianping.com/tuan/pay/protrocol/service-protocol.html");
        return;
      }
      if (i == R.id.protocol_webank)
      {
        startActivity("dianping://web?url=http://h5.dianping.com/tuan/pay/protrocol/privacy-policy.html");
        return;
      }
      if (i != R.id.bind_user_name_help)
        continue;
      WebankManager.showAlertDialog(this, "开户人须为持卡人本人", "一个点评账户仅能绑定一个人的银行卡，无法再绑定他人的银行卡。");
      return;
    }
    while (i != R.id.check);
    updateSubmitBtn();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.pay_webank_bind_activity);
    this.dataSource = new PayBindDataSource(this);
    this.dataSource.bindResultListener = this;
    this.dataSource.source = getIntParam("source");
    this.dataSource.realName = getStringParam("realname");
    this.dataSource.paySessionId = getStringParam("paysessionid");
    this.dataSource.bankElement = getObjectParam("bankelement");
    if ((this.dataSource.bankElement == null) || (this.dataSource.paySessionId == null))
    {
      Toast.makeText(this, "绑定参数错误！", 0).show();
      finish();
      return;
    }
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
 * Qualified Name:     com.dianping.pay.activity.PayBindActivity
 * JD-Core Version:    0.6.0
 */