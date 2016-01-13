package com.dianping.main.login.nativelogin.agent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.fastloginview.LoginHelper.LoginListener;
import com.dianping.base.widget.fastloginview.ShowVerificationCodeButton;
import com.dianping.base.widget.fastloginview.ShowVerificationCodeButton.VerficationCodeRefreshListener;
import com.dianping.loader.MyResources;
import com.dianping.main.login.nativelogin.BasicLoginActivity;
import com.dianping.model.SimpleMsg;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class NormalLoginAgent extends CellAgent
  implements LoginHelper.LoginListener, ShowVerificationCodeButton.VerficationCodeRefreshListener, View.OnClickListener, TextWatcher
{
  private static final int REQUEST_CODE_FORGET_PASSWORD = 3333;
  private static final int REQUEST_CODE_OTHER_LOGIN = 3233;
  private static final String SPKEY_LOGIN_ACCOUNT_NAME = "mainLoginAccountName";
  private static final String SPKEY_LOGIN_PHONE_NUM = "mainLoginPhoneNum";
  private boolean hasVerificationCode = false;
  private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
      if (paramInt == 2)
        NormalLoginAgent.this.login();
      return false;
    }
  };
  private LinearLayout mFastLoginView;
  private TextView mForgetPW;
  private TextView mLocalLoginPW;
  private Button mLoginButton;
  private CustomEditText mPassWordEditText;
  private ShowVerificationCodeButton mShowVerificationCodeButton;
  private TableView mTableView;
  private CustomEditText mUserNameEditText;
  private CustomEditText mVerificationCodeEditText;

  public NormalLoginAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initView()
  {
    this.mShowVerificationCodeButton = new ShowVerificationCodeButton((BasicLoginActivity)getFragment().getActivity(), this);
    this.mShowVerificationCodeButton.sendVerificationCodeRequest(1);
    this.mTableView = ((TableView)this.mFastLoginView.findViewById(R.id.input_root));
    this.mUserNameEditText = ((CustomEditText)this.mFastLoginView.findViewById(R.id.usr));
    this.mUserNameEditText.mEdit.setInputType(1);
    this.mUserNameEditText.mEdit.setHint("手机号/邮箱/用户名");
    String str = DPActivity.preferences().getString("mainLoginAccountName", "");
    if (TextUtils.isEmpty(str))
      str = DPActivity.preferences().getString("mainLoginPhoneNum", "");
    while (true)
    {
      if (!TextUtils.isEmpty(str))
      {
        this.mUserNameEditText.mEdit.setText(str);
        this.mUserNameEditText.mEdit.setSelection(str.length());
      }
      this.mUserNameEditText.mIcon.setVisibility(8);
      this.mUserNameEditText.mTitle.setVisibility(0);
      this.mUserNameEditText.mTitle.setText("账号");
      this.mUserNameEditText.mEdit.addTextChangedListener(this);
      this.mPassWordEditText = ((CustomEditText)this.mFastLoginView.findViewById(R.id.psw));
      this.mPassWordEditText.mEdit.setInputType(129);
      this.mPassWordEditText.mEdit.setHint("请填写密码");
      this.mPassWordEditText.mIcon.setVisibility(8);
      this.mPassWordEditText.mTitle.setVisibility(0);
      this.mPassWordEditText.mTitle.setText("密码");
      this.mPassWordEditText.mEdit.addTextChangedListener(this);
      this.mPassWordEditText.mEdit.setOnEditorActionListener(this.mEditorActionListener);
      if (!this.hasVerificationCode)
        this.mPassWordEditText.mEdit.setImeOptions(2);
      this.mLoginButton = ((Button)this.mFastLoginView.findViewById(R.id.login));
      this.mLoginButton.setOnClickListener(this);
      this.mLoginButton.setEnabled(false);
      this.mForgetPW = ((TextView)this.mFastLoginView.findViewById(R.id.forget));
      this.mForgetPW.setOnClickListener(this);
      this.mLocalLoginPW = ((TextView)this.mFastLoginView.findViewById(R.id.fastlogin));
      this.mLocalLoginPW.setOnClickListener(this);
      if (DPActivity.preferences().getInt("dianping.login.login_mode", 0) != 0)
        break;
      this.mLocalLoginPW.setVisibility(8);
      return;
    }
    this.mLocalLoginPW.setVisibility(0);
  }

  private void login()
  {
    String str2 = this.mUserNameEditText.mEdit.getText().toString().trim();
    String str3 = this.mPassWordEditText.mEdit.getText().toString().trim();
    if ((TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)))
    {
      super.showToast("用户名或密码为空");
      return;
    }
    Object localObject = null;
    if (this.hasVerificationCode)
    {
      String str1 = this.mVerificationCodeEditText.mEdit.getText().toString().trim();
      localObject = str1;
      if (TextUtils.isEmpty(str1))
      {
        super.showToast("验证码为空,请重新输入");
        this.mVerificationCodeEditText.mEdit.requestFocus();
        return;
      }
    }
    ((BasicLoginActivity)getFragment().getActivity()).login(str2, str3, "0", this.mShowVerificationCodeButton.ticket, localObject, this);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((3333 == paramInt1) && (paramInt2 == 64033))
      if ((getContext() instanceof Activity))
      {
        ((Activity)getContext()).setResult(64033);
        ((Activity)getContext()).finish();
      }
    do
      return;
    while ((3233 != paramInt1) || (paramInt2 != 64033) || (!(getContext() instanceof Activity)));
    ((Activity)getContext()).setResult(64033);
    ((Activity)getContext()).finish();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mFastLoginView == null)
    {
      this.mFastLoginView = ((LinearLayout)this.res.inflate(getContext(), R.layout.normal_login, getParentView(), false));
      initView();
      super.addCell("009locallogin", this.mFastLoginView);
      getFragment().getActivity().setTitle("点评账号登录");
    }
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.login)
      login();
    do
    {
      return;
      if (i != R.id.forget)
        continue;
      startActivityForResult("dianping://loginweb?url=http://m.dianping.com/resetpassword/app", 3333);
      return;
    }
    while (i != R.id.fastlogin);
    paramView = Uri.parse("dianping://sublogin").buildUpon();
    paramView.appendQueryParameter("isFromNative", "true");
    startActivityForResult(paramView.build().toString(), 3233);
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    if (getContext() == null)
      return;
    ((BasicLoginActivity)getContext()).onLoginFailed(paramInt, paramSimpleMsg);
    this.mPassWordEditText.mEdit.setText("");
    if (this.mVerificationCodeEditText != null)
      this.mVerificationCodeEditText.mEdit.setText("");
    if ((paramSimpleMsg != null) && (paramSimpleMsg.flag() == 1))
      this.mShowVerificationCodeButton.sendVerificationCodeRequest(0);
    GAHelper.instance().contextStatisticsEvent(getContext(), "error", paramSimpleMsg.content(), 2147483647, "view");
  }

  public void onLoginSucceed()
  {
    DPActivity.preferences().edit().putInt("dianping.login.login_mode", 1).commit();
    if (getContext() == null)
      return;
    ((BasicLoginActivity)getContext()).onLoginSucceed();
    ((BasicLoginActivity)getContext()).setResult(64033);
    ((BasicLoginActivity)getContext()).finish();
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    paramCharSequence = this.mUserNameEditText.mEdit.getText().toString().trim();
    String str = this.mPassWordEditText.mEdit.getText().toString().trim();
    if ((!TextUtils.isEmpty(paramCharSequence)) && (!TextUtils.isEmpty(str)))
    {
      if (this.hasVerificationCode)
      {
        if (!TextUtils.isEmpty(this.mVerificationCodeEditText.mEdit.getText().toString().trim()))
        {
          this.mLoginButton.setEnabled(true);
          return;
        }
        this.mLoginButton.setEnabled(false);
        return;
      }
      this.mLoginButton.setEnabled(true);
      return;
    }
    this.mLoginButton.setEnabled(false);
  }

  public void onVerficationCodeRefreshListener()
  {
    if (this.mShowVerificationCodeButton.getParent() == null)
    {
      this.mVerificationCodeEditText = ((CustomEditText)((BasicLoginActivity)getFragment().getActivity()).getLayoutInflater().inflate(R.layout.custom_edit_text, this.mTableView, false));
      this.mVerificationCodeEditText.mEdit.setInputType(1);
      this.mVerificationCodeEditText.mEdit.setHint("验证码");
      this.mVerificationCodeEditText.addView(this.mShowVerificationCodeButton);
      this.mVerificationCodeEditText.mIcon.setImageResource(R.drawable.login_ver);
      this.mVerificationCodeEditText.mEdit.addTextChangedListener(this);
      this.mVerificationCodeEditText.mEdit.setImeOptions(2);
      this.mVerificationCodeEditText.mEdit.setOnEditorActionListener(this.mEditorActionListener);
      this.mTableView.addView(this.mVerificationCodeEditText);
      this.hasVerificationCode = true;
      this.mPassWordEditText.mEdit.setImeOptions(5);
      this.mPassWordEditText.mEdit.setOnEditorActionListener(null);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.agent.NormalLoginAgent
 * JD-Core Version:    0.6.0
 */