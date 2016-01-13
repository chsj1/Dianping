package com.dianping.main.login.nativelogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.impl.DefaultAccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.sso.ILogininListener;
import com.dianping.base.sso.IThirdSSOLogin;
import com.dianping.base.sso.ThirdSSOLoginFactory;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.base.util.SMSHelper;
import com.dianping.base.util.SMSHelper.verCodeComeListener;
import com.dianping.base.web.client.NovaZeusWebViewClient;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.zeus.client.ZeusWebViewClient;
import com.dianping.zeus.ui.ZeusFragment;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginWebFragment extends NovaZeusFragment
  implements RequestHandler<MApiRequest, MApiResponse>, SMSHelper.verCodeComeListener, ILogininListener
{
  private DefaultAccountService mDefaultAccountService;
  private int mLoginResultId = 64034;
  private SMSHelper mSmsHelper;
  IThirdSSOLogin mSsoLogin;
  private MApiRequest mSyncCardReq;
  MApiRequest mUserReq;

  private void syncCard(String paramString)
  {
    if (this.mSyncCardReq == null)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("token");
      localArrayList.add(paramString);
      localArrayList.add("uuid");
      localArrayList.add(Environment.uuid());
      this.mSyncCardReq = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/syncard.mc", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      ((NovaActivity)getActivity()).mapiService().exec(this.mSyncCardReq, this);
    }
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return new ProductDetailWebViewClient(this);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    paramBundle = ((NovaZeusActivity)getActivity()).accountService();
    if ((paramBundle instanceof DefaultAccountService))
      this.mDefaultAccountService = ((DefaultAccountService)paramBundle);
    try
    {
      while (true)
      {
        this.mSmsHelper = new SMSHelper(getActivity());
        this.mSmsHelper.setOnVerCodeComeListener(this);
        this.mSmsHelper.registerSMSHelper();
        return;
        getActivity().finish();
      }
    }
    catch (Exception paramBundle)
    {
      while (!Environment.isDebug());
      Toast.makeText(getActivity(), "短信权限未开启", 0).show();
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (this.mSsoLogin != null)
      this.mSsoLogin.callback(paramInt1, paramInt2, paramIntent);
  }

  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      this.mSmsHelper.unregisterSMSHelper();
      if (getActivity() == null)
        return;
    }
    catch (Exception localException2)
    {
      while (true)
      {
        if (!Environment.isDebug())
          continue;
        Toast.makeText(getActivity(), "短信权限未开启", 0).show();
        continue;
        if ((getActivity().isFinishing()) && (this.mDefaultAccountService != null) && (this.mDefaultAccountService.profile() == null) && (!((DPActivity)getActivity()).getBooleanParam("isFromNative")))
          this.mDefaultAccountService.cancelLogin();
        Object localObject = getActivity().getIntent().getData();
        if ((localObject == null) || (!getActivity().isFinishing()) || (this.mLoginResultId != 64033))
          continue;
        try
        {
          if (!"dianping".equals(((Uri)localObject).getScheme()))
            continue;
          localObject = ((Uri)localObject).getQueryParameter("goto");
          if (TextUtils.isEmpty((CharSequence)localObject))
            continue;
          startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
          return;
        }
        catch (Exception localException2)
        {
          Log.e(localException2.toString());
        }
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mUserReq == paramMApiRequest)
    {
      this.mLoginResultId = 64034;
      this.mUserReq = null;
      if ((getActivity() instanceof NovaActivity))
      {
        getActivity().setResult(0);
        if (((DPActivity)getActivity()).getBooleanParam("isFromNative"))
          getActivity().finish();
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mUserReq == paramMApiRequest)
    {
      if (!(paramMApiResponse.result() instanceof DPObject))
        break label157;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.mLoginResultId = 64033;
      new FavoriteHelper(null).refresh(0, true);
      DPApplication.instance().accountService().update(paramMApiRequest);
      if ((getActivity() instanceof NovaActivity))
      {
        syncCard(paramMApiRequest.getString("Token"));
        ((NovaActivity)getActivity()).statisticsEvent("user5", "user5_login_success", "", 0);
        ((NovaActivity)getActivity()).showShortToast("登录成功！");
        getActivity().setResult(64033);
        getActivity().finish();
      }
    }
    while (true)
    {
      if ((getActivity() instanceof NovaActivity))
        ((NovaActivity)getActivity()).dismissDialog();
      this.mUserReq = null;
      return;
      label157: this.mLoginResultId = 64034;
    }
  }

  public void onSSOLoginCancel(int paramInt)
  {
    if (((getActivity() instanceof NovaActivity)) && (((NovaActivity)getActivity()).getBooleanParam("isFromNative")))
      ((NovaActivity)getActivity()).finish();
  }

  public void onSSOLoginFailed(int paramInt)
  {
    if (((getActivity() instanceof NovaActivity)) && (((NovaActivity)getActivity()).getBooleanParam("isFromNative")))
      ((NovaActivity)getActivity()).finish();
  }

  public void onSSOLoginSucceed(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    default:
    case 1:
    case 32:
    }
    while (true)
    {
      return;
      paramObject = (Bundle)paramObject;
      if (!Oauth2AccessToken.parseAccessToken(paramObject).isSessionValid())
        continue;
      Object localObject2 = paramObject.keySet();
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramObject.getString("mSsoUrl"));
      localObject2 = ((Set)localObject2).iterator();
      Object localObject3;
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (String)((Iterator)localObject2).next();
        ((StringBuilder)localObject1).append((String)localObject3).append("=").append(paramObject.get((String)localObject3)).append("&");
      }
      if (!(getActivity() instanceof LoginWebActivity))
        continue;
      ((LoginWebActivity)getActivity()).refresh(((StringBuilder)localObject1).toString());
      return;
      Object localObject5 = (JSONObject)paramObject;
      paramObject = "";
      try
      {
        localObject1 = ((JSONObject)localObject5).getString("access_token");
        paramObject = localObject1;
        label178: localObject1 = "";
        try
        {
          localObject2 = ((JSONObject)localObject5).getString("expires_in");
          localObject1 = localObject2;
          label195: localObject2 = "";
          try
          {
            localObject3 = ((JSONObject)localObject5).getString("openid");
            localObject2 = localObject3;
            label214: localObject3 = "";
            try
            {
              Object localObject4 = ((JSONObject)localObject5).getString("pay_token");
              localObject3 = localObject4;
              label233: localObject4 = "";
              try
              {
                localObject5 = ((JSONObject)localObject5).getString("sso_url");
                localObject4 = localObject5;
                label252: if (!(getActivity() instanceof LoginWebActivity))
                  continue;
                localObject4 = new StringBuilder((String)localObject4);
                ((StringBuilder)localObject4).append("token=").append(paramObject);
                ((StringBuilder)localObject4).append("&expires_in=").append((String)localObject1);
                ((StringBuilder)localObject4).append("&openid=").append((String)localObject2);
                ((StringBuilder)localObject4).append("&pay_token=").append((String)localObject3);
                ((LoginWebActivity)getActivity()).refresh(((StringBuilder)localObject4).toString());
                return;
              }
              catch (JSONException localJSONException5)
              {
                break label252;
              }
            }
            catch (JSONException localJSONException4)
            {
              break label233;
            }
          }
          catch (JSONException localJSONException3)
          {
            break label214;
          }
        }
        catch (JSONException localJSONException2)
        {
          break label195;
        }
      }
      catch (JSONException localJSONException1)
      {
        break label178;
      }
    }
  }

  public void onVerCodeCome(String paramString)
  {
    this.webView.loadUrl("javascript:window.DPApp.fillVcode('" + paramString + "')");
  }

  public void refresh(String paramString)
  {
    try
    {
      this.webView.clearCache(false);
      label8: loadUrl(paramString);
      return;
    }
    catch (Exception localException)
    {
      break label8;
    }
  }

  private class ProductDetailWebViewClient extends NovaZeusWebViewClient
  {
    public ProductDetailWebViewClient(ZeusFragment arg2)
    {
      super();
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      if (LoginWebFragment.this.getActivity() != null)
        ((NovaActivity)LoginWebFragment.this.getActivity()).dismissDialog();
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      super.onPageStarted(paramWebView, paramString, paramBitmap);
      if (LoginWebFragment.this.getActivity() != null)
        ((NovaActivity)LoginWebFragment.this.getActivity()).showProgressDialog("加载中...");
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      LoginWebFragment.this.mSsoLogin = ThirdSSOLoginFactory.ssoLogin(paramString);
      if ((!TextUtils.isEmpty(paramString)) && (paramString.contains("dianping://weblogincallback")))
      {
        paramString = Uri.parse(paramString);
        paramWebView = paramString.getQueryParameter("token");
        paramString = paramString.getQueryParameter("newtoken");
        LoginWebFragment.this.mUserReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/user.bin?token=" + paramWebView + "&newtoken=" + paramString + "&userid=0&refresh=true", CacheType.DISABLED);
        ((NovaActivity)LoginWebFragment.this.getActivity()).mapiService().exec(LoginWebFragment.this.mUserReq, LoginWebFragment.this);
        return true;
      }
      if ((!TextUtils.isEmpty(paramString)) && (LoginWebFragment.this.mSsoLogin != null))
      {
        LoginWebFragment.this.mSsoLogin.ssoLogin(paramString, LoginWebFragment.this.getActivity(), LoginWebFragment.this);
        return true;
      }
      return super.shouldOverrideUrlLoading(paramWebView, paramString);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.LoginWebFragment
 * JD-Core Version:    0.6.0
 */