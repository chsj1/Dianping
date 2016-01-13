package com.dianping.pay.view;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TableView;
import com.dianping.pay.entity.PayBindDataSource;
import com.dianping.pay.entity.PayVerifyBindDataSource;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class WebankBindInfoView extends TableView
  implements TextWatcher
{
  protected NovaButton btnVerfiyCodeGet;
  protected EditText editBindCardNo;
  protected EditText editBindIdCard;
  protected EditText editBindPhone;
  protected EditText editBindUser;
  protected EditText editBindVerifyCode;
  protected boolean hasVerificationCode;
  protected ImageButton imageBindPhoneHelp;
  protected ImageButton imageBindUserHelp;
  protected LinearLayout layerBindUser;
  protected VerifyCodeListener listener;
  Handler mHandler = new WebankBindInfoView.1(this);
  protected int mLastTime = 60;
  protected String paramRealName;
  protected int source;

  public WebankBindInfoView(Context paramContext)
  {
    this(paramContext, null);
  }

  public WebankBindInfoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.pay_webank_bind_info_view, this);
    initView();
  }

  private String getEditText(EditText paramEditText)
  {
    return paramEditText.getText().toString().trim();
  }

  private void resetButton()
  {
    this.btnVerfiyCodeGet.setEnabled(true);
    this.mLastTime = 60;
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0);
    setVerfiyCodeText("获取验证码");
  }

  private void setVerfiyCodeText(String paramString)
  {
    this.btnVerfiyCodeGet.setText(paramString);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  protected void initView()
  {
    this.layerBindUser = ((LinearLayout)findViewById(R.id.bind_user_layer));
    this.editBindCardNo = ((EditText)findViewById(R.id.bind_card_no));
    this.editBindCardNo.addTextChangedListener(this);
    this.editBindUser = ((EditText)findViewById(R.id.bind_user));
    this.editBindUser.addTextChangedListener(this);
    this.imageBindUserHelp = ((ImageButton)findViewById(R.id.bind_user_help));
    this.imageBindUserHelp.setOnClickListener(new WebankBindInfoView.2(this));
    this.editBindIdCard = ((EditText)findViewById(R.id.bind_id_card));
    this.editBindIdCard.addTextChangedListener(this);
    this.editBindPhone = ((EditText)findViewById(R.id.bind_phone));
    this.editBindPhone.addTextChangedListener(this);
    this.imageBindPhoneHelp = ((ImageButton)findViewById(R.id.bind_phone_help));
    this.imageBindPhoneHelp.setOnClickListener(new WebankBindInfoView.3(this));
    this.editBindVerifyCode = ((EditText)findViewById(R.id.bind_verify_code));
    this.editBindVerifyCode.addTextChangedListener(this);
    this.editBindVerifyCode.setEnabled(false);
    this.btnVerfiyCodeGet = ((NovaButton)findViewById(R.id.bind_verify_code_get));
    this.btnVerfiyCodeGet.setEnabled(false);
    this.btnVerfiyCodeGet.setOnClickListener(new WebankBindInfoView.4(this));
    this.btnVerfiyCodeGet.setGAString("button_sendMessage_bind");
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    String str1 = this.editBindCardNo.getText().toString().trim();
    String str4;
    if (this.layerBindUser.getVisibility() == 0)
    {
      paramCharSequence = this.editBindUser.getText().toString().trim();
      String str2 = this.editBindIdCard.getText().toString().trim();
      String str3 = this.editBindPhone.getText().toString().trim();
      str4 = this.editBindVerifyCode.getText().toString().trim();
      if ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(paramCharSequence)) || (TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)) || (this.mLastTime != 60))
        break label164;
      this.btnVerfiyCodeGet.setEnabled(true);
    }
    while (true)
    {
      if (!this.hasVerificationCode)
        break label186;
      if (TextUtils.isEmpty(str4))
        break label175;
      this.listener.setSubmitBtn(true);
      return;
      paramCharSequence = "default";
      break;
      label164: this.btnVerfiyCodeGet.setEnabled(false);
    }
    label175: this.listener.setSubmitBtn(false);
    return;
    label186: this.listener.setSubmitBtn(false);
  }

  public void resetVerifyBtnSeconds()
  {
    this.mHandler.sendEmptyMessageDelayed(0, 1000L);
    this.btnVerfiyCodeGet.setEnabled(false);
    setVerfiyCodeText("重新发送(" + this.mLastTime + ")");
  }

  public void setEditHints(DPObject paramDPObject, int paramInt)
  {
    this.source = paramInt;
    if (1 == paramInt)
      this.btnVerfiyCodeGet.setGAString("button_sendDpMessage_pay");
    while (true)
    {
      String str1 = "";
      if (!TextUtils.isEmpty(paramDPObject.getString("BankName")))
        str1 = "" + paramDPObject.getString("BankName");
      String str2 = str1;
      if (!TextUtils.isEmpty(paramDPObject.getString("TailNumber")))
        str2 = str1 + "(尾号" + paramDPObject.getString("TailNumber") + ")";
      paramDPObject = str2 + "的完整卡号";
      this.editBindCardNo.setHint(paramDPObject);
      this.editBindUser.setHint("您的姓名");
      this.editBindIdCard.setHint("您的身份证号");
      this.editBindPhone.setHint("该卡在银行预留的手机号码");
      return;
      if (2 != paramInt)
        continue;
      this.btnVerfiyCodeGet.setGAString("button_sendDpMessage_setPass");
    }
  }

  public void setListener(VerifyCodeListener paramVerifyCodeListener)
  {
    this.listener = paramVerifyCodeListener;
  }

  public void setParamRealName(String paramString)
  {
    this.paramRealName = paramString;
  }

  public void setUserLayer(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.layerBindUser.setVisibility(0);
      return;
    }
    this.layerBindUser.setVisibility(8);
  }

  public void setVerifySuccuss()
  {
    this.hasVerificationCode = true;
    this.editBindCardNo.setEnabled(false);
    this.editBindUser.setEnabled(false);
    this.editBindIdCard.setEnabled(false);
    this.editBindPhone.setEnabled(false);
    this.editBindVerifyCode.setEnabled(true);
    resetVerifyBtnSeconds();
  }

  public void updateToBindDataSource(PayBindDataSource paramPayBindDataSource)
  {
    paramPayBindDataSource.bankCardNumber = getEditText(this.editBindCardNo);
    if (TextUtils.isEmpty(this.paramRealName))
      paramPayBindDataSource.realName = getEditText(this.editBindUser);
    paramPayBindDataSource.idCardNumber = getEditText(this.editBindIdCard);
    paramPayBindDataSource.mobileNumber = getEditText(this.editBindPhone);
    paramPayBindDataSource.verifyCode = getEditText(this.editBindVerifyCode);
  }

  public void updateToVerifyBindDataSource(PayVerifyBindDataSource paramPayVerifyBindDataSource)
  {
    paramPayVerifyBindDataSource.bankCardNumber = getEditText(this.editBindCardNo);
    paramPayVerifyBindDataSource.realName = getEditText(this.editBindUser);
    paramPayVerifyBindDataSource.idCardNumber = getEditText(this.editBindIdCard);
    paramPayVerifyBindDataSource.mobileNumber = getEditText(this.editBindPhone);
    paramPayVerifyBindDataSource.verifyCode = getEditText(this.editBindVerifyCode);
  }

  public static abstract interface VerifyCodeListener
  {
    public abstract void onBtnClick();

    public abstract void setSubmitBtn(boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.WebankBindInfoView
 * JD-Core Version:    0.6.0
 */