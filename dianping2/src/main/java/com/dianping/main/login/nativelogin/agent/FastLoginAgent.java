package com.dianping.main.login.nativelogin.agent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.fastloginview.FastLoginView;
import com.dianping.base.widget.fastloginview.FastLoginView.FastLoginLoginListener;
import com.dianping.loader.MyResources;
import com.dianping.main.login.nativelogin.BasicLoginActivity;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FastLoginAgent extends CellAgent
  implements View.OnClickListener, TextWatcher, FastLoginView.FastLoginLoginListener
{
  private static final String LAST_COUNTRY_CODE = "last_country_code";
  private static final int REQUEST_CODE_OTHER_LOGIN = 3233;
  private static final int REQUEST_CODE_SIGNUP = 35;
  private static final String SPKEY_LOGIN_PHONE_NUM = "mainLoginPhoneNum";
  private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
      if (paramInt == 2)
        FastLoginAgent.this.mFastLoginView.login(FastLoginAgent.this);
      return false;
    }
  };
  private FastLoginView mFastLoginView;
  private LinearLayout mLocalLoginView;
  private Button mLoginButton;
  private TextView mSwitchLoginWayTextView;
  private CustomEditText mVerificationCodeEditText;

  public FastLoginAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((3233 == paramInt1) && (paramInt2 == 64033))
      if ((getContext() instanceof Activity))
      {
        ((Activity)getContext()).setResult(64033);
        ((Activity)getContext()).finish();
      }
    String str;
    do
    {
      do
        return;
      while ((35 != paramInt1) || (64036 != paramInt2));
      paramIntent = paramIntent.getStringExtra("mainLoginPhoneNum");
      str = DPActivity.preferences().getString("last_country_code", "");
    }
    while (TextUtils.isEmpty(paramIntent));
    this.mFastLoginView.setPhoneNum(paramIntent, str, true, false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mLocalLoginView == null)
    {
      this.mLocalLoginView = ((LinearLayout)this.res.inflate(getContext(), R.layout.fast_login, getParentView(), false));
      this.mLoginButton = ((Button)this.mLocalLoginView.findViewById(R.id.fast_login));
      this.mLoginButton.setOnClickListener(this);
      this.mLoginButton.setEnabled(false);
      this.mFastLoginView = ((FastLoginView)this.mLocalLoginView.findViewById(R.id.input_root));
      this.mFastLoginView.addPhoneEditTextChangedListener(this);
      this.mVerificationCodeEditText = this.mFastLoginView.getVerificationCodeEditText();
      this.mVerificationCodeEditText.mEdit.addTextChangedListener(this);
      this.mVerificationCodeEditText.mEdit.setImeOptions(2);
      this.mVerificationCodeEditText.mEdit.setOnEditorActionListener(this.mEditorActionListener);
      Object localObject = DPActivity.preferences();
      paramBundle = ((SharedPreferences)localObject).getString("mainLoginPhoneNum", "");
      localObject = ((SharedPreferences)localObject).getString("last_country_code", "");
      if (!TextUtils.isEmpty(paramBundle))
        this.mFastLoginView.setPhoneNum(paramBundle, (String)localObject, true, false);
      this.mSwitchLoginWayTextView = ((TextView)this.mLocalLoginView.findViewById(R.id.login));
      if ((DPActivity.preferences().getInt("dianping.login.login_mode", 0) != 1) && (!"fastlogin".equals(((Activity)getContext()).getIntent().getData().getHost())))
        break label340;
      this.mSwitchLoginWayTextView.setVisibility(8);
      paramBundle = getFragment().getStringParam("cannormallogin");
      if ((!TextUtils.isEmpty(paramBundle)) && (!"1".equals(paramBundle)) && (!"true".equals(paramBundle)))
        break label351;
      this.mSwitchLoginWayTextView.setOnClickListener(this);
      label295: if (TextUtils.isEmpty(getFragment().getStringParam("title")))
        break label363;
      super.getFragment().getTitleBar().setTitle(getFragment().getStringParam("title"));
    }
    while (true)
    {
      super.addCell("010locallogin", this.mLocalLoginView);
      return;
      label340: this.mSwitchLoginWayTextView.setVisibility(0);
      break;
      label351: this.mSwitchLoginWayTextView.setVisibility(8);
      break label295;
      label363: super.getFragment().getTitleBar().setTitle("无密码快捷登录");
    }
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.fast_login)
      this.mFastLoginView.login(this);
    do
      return;
    while (i != R.id.login);
    paramView = Uri.parse("dianping://sublogin").buildUpon();
    paramView.appendQueryParameter("isFromNative", "true");
    startActivityForResult(paramView.build().toString(), 3233);
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    if ((getContext() instanceof BasicLoginActivity))
      ((BasicLoginActivity)getContext()).onLoginFailed(paramInt, paramSimpleMsg);
    this.mFastLoginView.clearVerificationCode();
  }

  public void onLoginSucceed()
  {
    DPActivity.preferences().edit().putInt("dianping.login.login_mode", 0).commit();
    if ((getContext() instanceof BasicLoginActivity))
    {
      ((BasicLoginActivity)getContext()).onLoginSucceed();
      ((Activity)getContext()).setResult(64033);
      ((Activity)getContext()).finish();
    }
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    paramCharSequence = this.mFastLoginView.getPhoneEditText();
    String str = this.mVerificationCodeEditText.mEdit.getText().toString().trim();
    if ((!TextUtils.isEmpty(paramCharSequence)) && (!TextUtils.isEmpty(str)) && (this.mFastLoginView.isVerCodeButtonClicked()))
    {
      this.mLoginButton.setEnabled(true);
      return;
    }
    this.mLoginButton.setEnabled(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.agent.FastLoginAgent
 * JD-Core Version:    0.6.0
 */