package com.dianping.main.login.nativelogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.fastloginview.FastLoginView;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SignupActivity extends BasicLoginActivity
  implements View.OnClickListener, TextWatcher
{
  private static final String SPKEY_LOGIN_PHONE_NUM = "mainLoginPhoneNum";
  private CheckBox mCheckbox;
  private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
      if (paramInt == 2)
        SignupActivity.this.signup();
      return false;
    }
  };
  private FastLoginView mFastLoginView;
  private CustomEditText mPassWordEditText;
  private Button mSignup;

  private void initView()
  {
    this.mSignup = ((Button)findViewById(R.id.register));
    this.mSignup.setOnClickListener(this);
    this.mSignup.setEnabled(false);
    this.mFastLoginView = ((FastLoginView)findViewById(R.id.input_root));
    this.mFastLoginView.addPhoneEditTextChangedListener(this);
    this.mFastLoginView.setGetVerCodeType(2);
    this.mFastLoginView.getVerificationCodeEditText().mEdit.addTextChangedListener(this);
    this.mPassWordEditText = ((CustomEditText)LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_edit_text, this.mFastLoginView, false));
    this.mPassWordEditText.mEdit.setInputType(129);
    this.mPassWordEditText.mEdit.setHint("6-32位字母数字组合");
    this.mPassWordEditText.mIcon.setVisibility(8);
    this.mPassWordEditText.mTitle.setVisibility(0);
    this.mPassWordEditText.mTitle.setText("密码");
    this.mPassWordEditText.mEdit.addTextChangedListener(this);
    this.mPassWordEditText.mEdit.setImeOptions(2);
    this.mPassWordEditText.mEdit.setOnEditorActionListener(this.mEditorActionListener);
    this.mFastLoginView.addView(this.mPassWordEditText);
    this.mCheckbox = ((CheckBox)findViewById(R.id.check));
    findViewById(R.id.user_guide).setOnClickListener(this);
    super.getTitleBar().setTitle("立即注册");
  }

  private void signup()
  {
    if (!this.mCheckbox.isChecked())
    {
      showToast("必须同意用户使用协议");
      return;
    }
    String str1 = this.mFastLoginView.getPhoneEditText();
    String str2 = this.mFastLoginView.getVerificationCodeEditText().mEdit.getText().toString().trim();
    String str3 = this.mPassWordEditText.mEdit.getText().toString().trim();
    if ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)))
    {
      super.showToast("手机号,验证码或密码不能为空");
      return;
    }
    Signup(str1, str3, str2, this);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  protected AgentFragment getAgentFragment()
  {
    return null;
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    super.setContentView(R.layout.activity_signup);
    initView();
  }

  public void onBackPressed()
  {
    if (!TextUtils.isEmpty(this.mFastLoginView.getLastVerCodeRegisterdPhone()))
    {
      Intent localIntent = new Intent();
      localIntent.putExtra("mainLoginPhoneNum", this.mFastLoginView.getLastVerCodeRegisterdPhone());
      setResult(64036, localIntent);
    }
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.register)
      if (!this.mCheckbox.isChecked())
        showToast("必须同意用户使用协议");
    do
    {
      return;
      paramView = this.mFastLoginView.getPhoneEditText();
      String str1 = this.mFastLoginView.getVerificationCodeEditText().mEdit.getText().toString().trim();
      String str2 = this.mPassWordEditText.mEdit.getText().toString().trim();
      if ((TextUtils.isEmpty(paramView)) || (TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)))
      {
        super.showToast("手机号,验证码或密码不能为空");
        return;
      }
      Signup(paramView, str2, str1, this);
      return;
    }
    while (i != R.id.user_guide);
    super.startActivity("dianping://web?url=http://www.dianping.com/aboutus/useragreement");
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    super.onLoginFailed(paramInt, paramSimpleMsg);
    this.mFastLoginView.getVerificationCodeEditText().mEdit.setText("");
    this.mPassWordEditText.mEdit.setText("");
  }

  public void onLoginSucceed()
  {
    super.onLoginSucceed();
    super.setResult(64033);
    super.finish();
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    paramCharSequence = this.mFastLoginView.getPhoneEditText();
    String str1 = this.mPassWordEditText.mEdit.getText().toString().trim();
    String str2 = this.mFastLoginView.getVerificationCodeEditText().mEdit.getText().toString().trim();
    if ((!TextUtils.isEmpty(paramCharSequence)) && (!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)) && (this.mFastLoginView.isVerCodeButtonClicked()))
    {
      this.mSignup.setEnabled(true);
      return;
    }
    this.mSignup.setEnabled(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.SignupActivity
 * JD-Core Version:    0.6.0
 */