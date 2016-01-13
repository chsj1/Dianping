package com.dianping.base.widget.fastloginview;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.TableView;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class FastLoginView extends TableView
  implements GetVerficationCodeButton.verCodeComeListener, LoginHelper.LoginListener, ShowVerificationCodeButton.VerficationCodeRefreshListener, TextWatcher
{
  private boolean hasVerificationCode = false;
  private CountryCodeView mCountryCodeView;
  private GetVerficationCodeButton mGetVerficationCodeButton;
  private CustomEditText mImageVerificationCodeEditText;
  private FastLoginLoginListener mListener;
  private LoginHelper mLoginHelper;
  private boolean mNeedCountryCode = true;
  private NovaActivity mNovaActivity;
  private CustomEditText mPhoneNumberEditText;
  private ShowVerificationCodeButton mShowVerificationCodeButton;
  private TableView mTableView;
  private CustomEditText mVerificationCodeEditText;

  public FastLoginView(Context paramContext)
  {
    super(paramContext);
  }

  public FastLoginView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setImageVerificationCodeEditText(CustomEditText paramCustomEditText)
  {
    if (this.mGetVerficationCodeButton != null)
      this.mGetVerficationCodeButton.setImageVerificationCodeEditText(paramCustomEditText);
  }

  private void setVerificationCodeEditText(CustomEditText paramCustomEditText)
  {
    if (this.mGetVerficationCodeButton != null)
      this.mGetVerficationCodeButton.setVerificationCodeEditText(paramCustomEditText);
  }

  public void addPhoneEditTextChangedListener(TextWatcher paramTextWatcher)
  {
    this.mPhoneNumberEditText.mEdit.addTextChangedListener(paramTextWatcher);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void clearVerificationCode()
  {
    this.mVerificationCodeEditText.mEdit.setText("");
  }

  public String getLastVerCodeRegisterdPhone()
  {
    return this.mGetVerficationCodeButton.phoneNum;
  }

  public String getPhoneEditText()
  {
    return com.dianping.util.TextUtils.getRealDialPhoneNum(this.mPhoneNumberEditText.mEdit.getText().toString().trim());
  }

  public CustomEditText getVerificationCodeEditText()
  {
    return this.mVerificationCodeEditText;
  }

  public boolean isVerCodeButtonClicked()
  {
    return this.mGetVerficationCodeButton.isVerCodeButtonClicked;
  }

  public void login(FastLoginLoginListener paramFastLoginLoginListener)
  {
    String str1 = getPhoneEditText();
    String str2 = this.mVerificationCodeEditText.mEdit.getText().toString().trim();
    this.mListener = paramFastLoginLoginListener;
    if ((android.text.TextUtils.isEmpty(str1)) || (android.text.TextUtils.isEmpty(str2)))
    {
      this.mNovaActivity.showToast("手机号或验证码不能为空");
      if (paramFastLoginLoginListener != null)
        paramFastLoginLoginListener.onLoginFailed(2, new SimpleMsg("手机号或验证码不能为空", "手机号或验证码不能为空", 0, 0));
    }
    do
    {
      return;
      this.mLoginHelper.login(str1, null, "1", this.mGetVerficationCodeButton.ticket, str2, this.mNeedCountryCode, this);
      paramFastLoginLoginListener = GAHelper.instance().getDpActivity(getContext());
    }
    while (!(paramFastLoginLoginListener instanceof NovaActivity));
    ((NovaActivity)paramFastLoginLoginListener).showProgressDialog("正在登录，请稍后...");
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mLoginHelper.onDestroy();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mNovaActivity = ((NovaActivity)GAHelper.instance().getDpActivity(getContext()));
    this.mLoginHelper = LoginHelper.instance();
    this.mTableView = ((TableView)findViewById(R.id.input_roots));
    this.mShowVerificationCodeButton = new ShowVerificationCodeButton((DPActivity)getContext(), this);
    this.mShowVerificationCodeButton.sendVerificationCodeRequest(1);
    this.mPhoneNumberEditText = ((CustomEditText)findViewById(R.id.phonenum));
    this.mPhoneNumberEditText.mEdit.setHint("手机号");
    this.mPhoneNumberEditText.mIcon.setVisibility(8);
    this.mPhoneNumberEditText.mEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    this.mPhoneNumberEditText.mEdit.setInputType(3);
    this.mCountryCodeView = new CountryCodeView(this.mNovaActivity);
    this.mGetVerficationCodeButton = new GetVerficationCodeButton(this.mNovaActivity, this.mPhoneNumberEditText.mEdit, 1);
    this.mPhoneNumberEditText.addView(this.mGetVerficationCodeButton);
    this.mVerificationCodeEditText = ((CustomEditText)findViewById(R.id.verificode));
    this.mVerificationCodeEditText.mEdit.setHint("请输入短信验证码");
    this.mVerificationCodeEditText.mEdit.setInputType(2);
    this.mVerificationCodeEditText.mIcon.setVisibility(8);
    this.mVerificationCodeEditText.mTitle.setVisibility(0);
    this.mVerificationCodeEditText.mTitle.setText("验证码");
    setVerificationCodeEditText(this.mVerificationCodeEditText);
    setShowVerificationCodeButton(this.mShowVerificationCodeButton);
    showCountryCode(Boolean.valueOf(true));
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    Context localContext = GAHelper.instance().getDpActivity(getContext());
    if ((localContext instanceof NovaActivity))
      ((NovaActivity)localContext).dismissDialog();
    if (this.mListener != null)
      this.mListener.onLoginFailed(paramInt, paramSimpleMsg);
  }

  public void onLoginSucceed()
  {
    Context localContext = GAHelper.instance().getDpActivity(getContext());
    if ((localContext instanceof NovaActivity))
      ((NovaActivity)localContext).dismissDialog();
    if (this.mListener != null)
      this.mListener.onLoginSucceed();
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.mImageVerificationCodeEditText.mEdit.getText().toString().length() == this.mShowVerificationCodeButton.mVerificationCodeLength)
      this.mGetVerficationCodeButton.sendGetVerficationCode();
  }

  public void onVerCodeCome(String paramString)
  {
    this.mVerificationCodeEditText.mEdit.setText(paramString);
  }

  public void onVerficationCodeRefreshListener()
  {
    if (this.mShowVerificationCodeButton.getParent() == null)
    {
      this.mImageVerificationCodeEditText = ((CustomEditText)((DPActivity)getContext()).getLayoutInflater().inflate(R.layout.custom_edit_text, this.mTableView, false));
      this.mImageVerificationCodeEditText.mEdit.setInputType(1);
      this.mImageVerificationCodeEditText.mEdit.setHint("验证码");
      this.mImageVerificationCodeEditText.addView(this.mShowVerificationCodeButton);
      this.mImageVerificationCodeEditText.mIcon.setImageResource(R.drawable.login_ver);
      this.mImageVerificationCodeEditText.mEdit.addTextChangedListener(this);
      this.mImageVerificationCodeEditText.mEdit.setImeOptions(2);
      this.mTableView.addView(this.mImageVerificationCodeEditText);
      this.hasVerificationCode = true;
      setImageVerificationCodeEditText(this.mImageVerificationCodeEditText);
    }
  }

  public void setGetVerCodeType(int paramInt)
  {
    this.mGetVerficationCodeButton.setGetVerCodeType(paramInt);
  }

  public void setPhoneNum(String paramString1, String paramString2, boolean paramBoolean)
  {
    setPhoneNum(paramString1, paramString2, paramBoolean, true);
  }

  public void setPhoneNum(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (android.text.TextUtils.isEmpty(paramString1))
      return;
    if (paramBoolean2)
      this.mPhoneNumberEditText.mEdit.setText(paramString1);
    while (true)
    {
      if (!android.text.TextUtils.isEmpty(paramString2))
        this.mCountryCodeView.setCountryCode(paramString2);
      if (!paramBoolean1)
      {
        this.mPhoneNumberEditText.mEdit.setTextColor(getResources().getColor(R.color.text_color_disable));
        this.mPhoneNumberEditText.mClear.setVisibility(8);
        this.mPhoneNumberEditText.mEdit.setCursorVisible(false);
        this.mPhoneNumberEditText.mEdit.setFocusable(false);
        this.mCountryCodeView.setTextColor(getResources().getColor(R.color.text_color_disable));
        this.mCountryCodeView.setFocusable(false);
        this.mCountryCodeView.setClickable(false);
      }
      if (!paramBoolean2)
        break;
      this.mGetVerficationCodeButton.performClick();
      return;
      this.mPhoneNumberEditText.mEdit.setText("");
      this.mPhoneNumberEditText.mEdit.setText(paramString1);
      this.mPhoneNumberEditText.mEdit.setSelection(this.mPhoneNumberEditText.mEdit.getText().length());
    }
  }

  public void setReplaceVerficationCodeUrlListener(FastLoginReplaceRequestListener paramFastLoginReplaceRequestListener)
  {
    this.mGetVerficationCodeButton.setReplaceRequestListener(paramFastLoginReplaceRequestListener);
  }

  public void setShowVerificationCodeButton(ShowVerificationCodeButton paramShowVerificationCodeButton)
  {
    this.mShowVerificationCodeButton = paramShowVerificationCodeButton;
    if (this.mGetVerficationCodeButton != null)
      this.mGetVerficationCodeButton.setShowVerificationCodeButton(paramShowVerificationCodeButton);
  }

  public void showCountryCode(Boolean paramBoolean)
  {
    if (paramBoolean.booleanValue())
    {
      this.mPhoneNumberEditText.mTitle.setVisibility(8);
      this.mPhoneNumberEditText.removeView(this.mCountryCodeView);
      this.mPhoneNumberEditText.addView(this.mCountryCodeView, 0);
    }
    while (true)
    {
      this.mNeedCountryCode = paramBoolean.booleanValue();
      this.mGetVerficationCodeButton.needCountryCode(paramBoolean.booleanValue());
      return;
      this.mPhoneNumberEditText.mTitle.setVisibility(0);
      this.mPhoneNumberEditText.mTitle.setText("手机号");
      this.mPhoneNumberEditText.removeView(this.mCountryCodeView);
    }
  }

  public static abstract interface FastLoginLoginListener extends LoginHelper.LoginListener
  {
  }

  public static abstract interface FastLoginReplaceRequestListener extends GetVerficationCodeButton.ReplaceRequestListener
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.fastloginview.FastLoginView
 * JD-Core Version:    0.6.0
 */