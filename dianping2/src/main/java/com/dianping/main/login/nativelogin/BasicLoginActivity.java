package com.dianping.main.login.nativelogin;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.dianping.accountservice.impl.DefaultAccountService;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.widget.fastloginview.FastLoginView.FastLoginLoginListener;
import com.dianping.base.widget.fastloginview.LoginHelper;
import com.dianping.base.widget.fastloginview.LoginHelper.LoginListener;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BasicLoginActivity extends AgentActivity
  implements FastLoginView.FastLoginLoginListener
{
  public static final String FAST_LOGIN_GAPAGE_NAME = "fastlogin";
  public static final int LOGIN_MODE_FAST_LOGIN = 0;
  public static final int LOGIN_MODE_NORMAL_LOGIN = 1;
  public static final String NORMAL_LOGIN_GAPAGE_NAME = "login";
  public static final String PERFERENCE_KEY_LOGIN_MODE = "dianping.login.login_mode";
  private DefaultAccountService mDefaultAccountService;
  private LoginHelper mLoginHelper;

  public void Signup(String paramString1, String paramString2, String paramString3, LoginHelper.LoginListener paramLoginListener)
  {
    showProgressDialog("正在注册,请稍后...");
    this.mLoginHelper.Signup(paramString1, paramString2, paramString3, paramLoginListener);
  }

  public void login(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, LoginHelper.LoginListener paramLoginListener)
  {
    showProgressDialog("正在登录,请稍后...");
    this.mLoginHelper.login(paramString1, paramString2, paramString3, paramString4, paramString5, true, paramLoginListener);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = accountService();
    if ((paramBundle instanceof DefaultAccountService))
      this.mDefaultAccountService = ((DefaultAccountService)paramBundle);
    while (true)
    {
      this.mLoginHelper = LoginHelper.instance();
      return;
      super.finish();
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mLoginHelper.onDestroy();
    Object localObject = super.getIntent().getData();
    if ((localObject != null) && (super.isFinishing()) && (!TextUtils.isEmpty(this.mDefaultAccountService.token())));
    try
    {
      if ("dianping".equals(((Uri)localObject).getScheme()))
      {
        localObject = ((Uri)localObject).getQueryParameter("goto");
        if (!TextUtils.isEmpty((CharSequence)localObject))
          super.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      }
      return;
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    dismissDialog();
    if (!TextUtils.isEmpty(paramSimpleMsg.data()))
      try
      {
        Object localObject = new JSONObject(paramSimpleMsg.data());
        String str = ((JSONObject)localObject).optString("name");
        localObject = ((JSONObject)localObject).optString("url");
        showSimpleAlertDialog(paramSimpleMsg.title(), paramSimpleMsg.content(), str, new DialogInterface.OnClickListener((String)localObject)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            BasicLoginActivity.this.startActivity(this.val$url);
          }
        }
        , "取消", null);
        return;
      }
      catch (JSONException paramSimpleMsg)
      {
        paramSimpleMsg.printStackTrace();
        return;
      }
    showToast(paramSimpleMsg.content());
  }

  public void onLoginSucceed()
  {
    dismissDialog();
    showToast("登录成功！");
  }

  protected void onPause()
  {
    super.onPause();
    ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 2);
    if ((isFinishing()) && (this.mDefaultAccountService != null) && (this.mDefaultAccountService.profile() == null) && (!getBooleanParam("isFromNative")))
      this.mDefaultAccountService.cancelLogin();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.BasicLoginActivity
 * JD-Core Version:    0.6.0
 */