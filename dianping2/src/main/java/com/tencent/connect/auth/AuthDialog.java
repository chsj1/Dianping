package com.tencent.connect.auth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.a.c.c;
import com.tencent.open.a.b;
import com.tencent.open.utils.ServerSetting;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthDialog extends Dialog
{
  private String a;
  private OnTimeListener b;
  private IUiListener c;
  private Handler d;
  private FrameLayout e;
  private LinearLayout f;
  private FrameLayout g;
  private ProgressBar h;
  private String i;
  private WebView j;
  private Context k;
  private boolean l = false;
  private int m;
  private String n;
  private String o;
  private long p = 0L;
  private long q = 30000L;
  private HashMap<String, Runnable> r;

  public AuthDialog(Context paramContext, String paramString1, String paramString2, IUiListener paramIUiListener, QQToken paramQQToken)
  {
    super(paramContext, 16973840);
    this.k = paramContext.getApplicationContext();
    this.a = paramString2;
    this.b = new OnTimeListener(paramString1, paramString2, paramQQToken.getAppId(), paramIUiListener);
    this.d = new THandler(this.b, paramContext.getMainLooper());
    this.c = paramIUiListener;
    this.i = paramString1;
    getWindow().setSoftInputMode(32);
  }

  private String a()
  {
    String str = this.a.substring(this.a.indexOf("?") + 1);
    str = "http://qzs.qq.com/open/mobile/login/qzsjump.html?" + str;
    Util.logd("AuthDialog", "-->generateDownloadUrl url: " + str);
    return str;
  }

  private String a(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    if (!TextUtils.isEmpty(this.o))
      paramString = "";
    try
    {
      String str = c.a("com.open", this.o);
      paramString = str;
      label34: localStringBuilder.append("_u_").append(paramString);
      return localStringBuilder.toString();
    }
    catch (Exception localException)
    {
      break label34;
    }
  }

  private void b()
  {
    c();
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -1);
    this.j = new WebView(this.k);
    this.j.setLayoutParams(localLayoutParams);
    Object localObject = this.j.getSettings().getClass();
    try
    {
      localObject = ((Class)localObject).getMethod("removeJavascriptInterface", new Class[] { String.class });
      if (localObject != null)
        ((Method)localObject).invoke(this.j, new Object[] { "searchBoxJavaBridge_" });
      label86: this.e = new FrameLayout(this.k);
      localLayoutParams.gravity = 17;
      this.e.setLayoutParams(localLayoutParams);
      this.e.addView(this.j);
      this.e.addView(this.g);
      setContentView(this.e);
      return;
    }
    catch (Exception localException)
    {
      break label86;
    }
  }

  private static void b(Context paramContext, String paramString)
  {
    try
    {
      paramString = Util.parseJson(paramString);
      int i1 = paramString.getInt("type");
      paramString = paramString.getString("msg");
      Toast.makeText(paramContext.getApplicationContext(), paramString, i1).show();
      return;
    }
    catch (JSONException paramContext)
    {
      paramContext.printStackTrace();
    }
  }

  private void c()
  {
    this.h = new ProgressBar(this.k);
    Object localObject1 = new LinearLayout.LayoutParams(-2, -2);
    this.h.setLayoutParams((ViewGroup.LayoutParams)localObject1);
    this.f = new LinearLayout(this.k);
    localObject1 = null;
    Object localObject2;
    if (this.i.equals("action_login"))
    {
      localObject2 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject2).gravity = 16;
      ((LinearLayout.LayoutParams)localObject2).leftMargin = 5;
      localObject1 = new TextView(this.k);
      if (!Locale.getDefault().getLanguage().equals("zh"))
        break label285;
      ((TextView)localObject1).setText("登录中...");
    }
    while (true)
    {
      ((TextView)localObject1).setTextColor(Color.rgb(255, 255, 255));
      ((TextView)localObject1).setTextSize(18.0F);
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      localObject2 = new FrameLayout.LayoutParams(-2, -2);
      ((FrameLayout.LayoutParams)localObject2).gravity = 17;
      this.f.setLayoutParams((ViewGroup.LayoutParams)localObject2);
      this.f.addView(this.h);
      if (localObject1 != null)
        this.f.addView((View)localObject1);
      this.g = new FrameLayout(this.k);
      localObject1 = new FrameLayout.LayoutParams(-1, -2);
      ((FrameLayout.LayoutParams)localObject1).leftMargin = 80;
      ((FrameLayout.LayoutParams)localObject1).rightMargin = 80;
      ((FrameLayout.LayoutParams)localObject1).topMargin = 40;
      ((FrameLayout.LayoutParams)localObject1).bottomMargin = 40;
      ((FrameLayout.LayoutParams)localObject1).gravity = 17;
      this.g.setLayoutParams((ViewGroup.LayoutParams)localObject1);
      this.g.setBackgroundResource(17301504);
      this.g.addView(this.f);
      return;
      label285: ((TextView)localObject1).setText("Logging in...");
    }
  }

  @SuppressLint({"SetJavaScriptEnabled"})
  private void d()
  {
    this.j.setVerticalScrollBarEnabled(false);
    this.j.setHorizontalScrollBarEnabled(false);
    this.j.setWebViewClient(new LoginWebViewClient(null));
    this.j.setWebChromeClient(new WebChromeClient());
    this.j.clearFormData();
    this.j.clearSslPreferences();
    this.j.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramView)
      {
        return true;
      }
    });
    WebSettings localWebSettings = this.j.getSettings();
    localWebSettings.setSavePassword(false);
    localWebSettings.setSaveFormData(false);
    localWebSettings.setCacheMode(-1);
    localWebSettings.setNeedInitialFocus(false);
    localWebSettings.setBuiltInZoomControls(true);
    localWebSettings.setSupportZoom(true);
    localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setDatabaseEnabled(true);
    localWebSettings.setDatabasePath(this.k.getDir("databases", 0).getPath());
    localWebSettings.setDomStorageEnabled(true);
    Log.d("AuthDialog", "-->mUrl : " + this.a);
    this.n = this.a;
    this.j.loadUrl(this.a);
    this.j.setVisibility(4);
    this.j.getSettings().setSavePassword(false);
  }

  private boolean e()
  {
    Object localObject1 = AuthMap.getInstance();
    String str = ((AuthMap)localObject1).makeKey();
    Object localObject2 = new AuthMap.Auth();
    ((AuthMap.Auth)localObject2).listener = this.c;
    ((AuthMap.Auth)localObject2).dialog = this;
    ((AuthMap.Auth)localObject2).key = str;
    localObject1 = ((AuthMap)localObject1).set((AuthMap.Auth)localObject2);
    localObject2 = this.a.substring(0, this.a.indexOf("?"));
    Bundle localBundle = Util.parseUrl(this.a);
    localBundle.putString("token_key", str);
    localBundle.putString("serial", (String)localObject1);
    localBundle.putString("browser", "1");
    this.a = ((String)localObject2 + "?" + Util.encodeUrl(localBundle));
    return Util.openBrowser(this.k, this.a);
  }

  public void callJs(String paramString1, String paramString2)
  {
    paramString1 = "javascript:" + paramString1 + "(" + paramString2 + ");void(" + System.currentTimeMillis() + ");";
    this.j.loadUrl(paramString1);
  }

  public void dismiss()
  {
    this.r.clear();
    this.d.removeCallbacksAndMessages(null);
    if (isShowing())
      super.dismiss();
  }

  public void onBackPressed()
  {
    if (!this.l)
      this.b.onCancel();
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    b();
    d();
    this.r = new HashMap();
  }

  protected void onStop()
  {
    super.onStop();
  }

  private class LoginWebViewClient extends WebViewClient
  {
    private LoginWebViewClient()
    {
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      Log.d("AuthDialog", "-->onPageFinished, url: " + paramString);
      AuthDialog.g(AuthDialog.this).setVisibility(8);
      AuthDialog.e(AuthDialog.this).setVisibility(0);
      if (!TextUtils.isEmpty(paramString))
        AuthDialog.m(AuthDialog.this).removeCallbacks((Runnable)AuthDialog.o(AuthDialog.this).remove(paramString));
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      Log.d("AuthDialog", "-->onPageStarted, url: " + paramString);
      super.onPageStarted(paramWebView, paramString, paramBitmap);
      AuthDialog.g(AuthDialog.this).setVisibility(0);
      AuthDialog.a(AuthDialog.this, SystemClock.elapsedRealtime());
      if (!TextUtils.isEmpty(AuthDialog.h(AuthDialog.this)))
        AuthDialog.m(AuthDialog.this).removeCallbacks((Runnable)AuthDialog.o(AuthDialog.this).remove(AuthDialog.h(AuthDialog.this)));
      AuthDialog.c(AuthDialog.this, paramString);
      paramWebView = new AuthDialog.TimeOutRunable(AuthDialog.this, AuthDialog.h(AuthDialog.this));
      AuthDialog.o(AuthDialog.this).put(paramString, paramWebView);
      AuthDialog.m(AuthDialog.this).postDelayed(paramWebView, 120000L);
    }

    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
      Log.d("AuthDialog", "-->onReceivedError, errorCode: " + paramInt + " | description: " + paramString1 + " | failingUrl: " + paramString2);
      if (!Util.checkNetWork(AuthDialog.a(AuthDialog.this)))
      {
        AuthDialog.f(AuthDialog.this).onError(new UiError(9001, "当前网络不可用，请稍后重试！", paramString2));
        AuthDialog.this.dismiss();
        return;
      }
      if (!AuthDialog.h(AuthDialog.this).startsWith("http://qzs.qq.com/open/mobile/login/qzsjump.html?"))
      {
        long l1 = SystemClock.elapsedRealtime();
        long l2 = AuthDialog.i(AuthDialog.this);
        if ((AuthDialog.j(AuthDialog.this) < 1) && (l1 - l2 < AuthDialog.k(AuthDialog.this)))
        {
          AuthDialog.l(AuthDialog.this);
          AuthDialog.m(AuthDialog.this).postDelayed(new Runnable()
          {
            public void run()
            {
              AuthDialog.e(AuthDialog.this).loadUrl(AuthDialog.h(AuthDialog.this));
            }
          }
          , 500L);
          return;
        }
        AuthDialog.e(AuthDialog.this).loadUrl(AuthDialog.n(AuthDialog.this));
        return;
      }
      AuthDialog.f(AuthDialog.this).onError(new UiError(paramInt, paramString1, paramString2));
      AuthDialog.this.dismiss();
    }

    @TargetApi(8)
    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
      super.onReceivedSslError(paramWebView, paramSslErrorHandler, paramSslError);
      AuthDialog.f(AuthDialog.this).onError(new UiError(paramSslError.getPrimaryError(), "请求不合法，请检查手机安全设置，如系统时间、代理等。", "ssl error"));
      AuthDialog.this.dismiss();
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      Log.d("AuthDialog", "-->Redirect URL: " + paramString);
      if (paramString.startsWith("auth://browser"))
      {
        paramWebView = Util.parseUrlToJson(paramString);
        AuthDialog.a(AuthDialog.this, AuthDialog.b(AuthDialog.this));
        if (AuthDialog.c(AuthDialog.this));
        while (true)
        {
          return true;
          if (paramWebView.optString("fail_cb", null) != null)
          {
            AuthDialog.this.callJs(paramWebView.optString("fail_cb"), "");
            continue;
          }
          if (paramWebView.optInt("fall_to_wv") == 1)
          {
            paramString = AuthDialog.this;
            if (AuthDialog.d(AuthDialog.this).indexOf("?") > -1);
            for (paramWebView = "&"; ; paramWebView = "?")
            {
              AuthDialog.a(paramString, paramWebView);
              AuthDialog.a(AuthDialog.this, "browser_error=1");
              AuthDialog.e(AuthDialog.this).loadUrl(AuthDialog.d(AuthDialog.this));
              break;
            }
          }
          paramWebView = paramWebView.optString("redir", null);
          if (paramWebView == null)
            continue;
          AuthDialog.e(AuthDialog.this).loadUrl(paramWebView);
        }
      }
      if (paramString.startsWith(ServerSetting.getInstance().getEnvUrl(AuthDialog.a(AuthDialog.this), "auth://tauth.qq.com/")))
      {
        AuthDialog.f(AuthDialog.this).onComplete(Util.parseUrlToJson(paramString));
        AuthDialog.this.dismiss();
        return true;
      }
      if (paramString.startsWith("auth://cancel"))
      {
        AuthDialog.f(AuthDialog.this).onCancel();
        AuthDialog.this.dismiss();
        return true;
      }
      if (paramString.startsWith("auth://close"))
      {
        AuthDialog.this.dismiss();
        return true;
      }
      if (paramString.startsWith("download://"))
        try
        {
          paramWebView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.decode(paramString.substring("download://".length()))));
          paramWebView.addFlags(268435456);
          AuthDialog.a(AuthDialog.this).startActivity(paramWebView);
          return true;
        }
        catch (Exception paramWebView)
        {
          while (true)
            Log.d("AuthDialog", "-->start download activity exception, e: " + paramWebView.getMessage());
        }
      if (paramString.startsWith("auth://progress"))
      {
        try
        {
          paramWebView = Uri.parse(paramString).getPathSegments();
          if (paramWebView.isEmpty())
            return true;
          int i = Integer.valueOf((String)paramWebView.get(0)).intValue();
          if (i == 0)
          {
            AuthDialog.g(AuthDialog.this).setVisibility(8);
            AuthDialog.e(AuthDialog.this).setVisibility(0);
            break label548;
          }
          if (i != 1)
            break label548;
          AuthDialog.g(AuthDialog.this).setVisibility(0);
        }
        catch (Exception paramWebView)
        {
          return true;
        }
      }
      else
      {
        if (paramString.startsWith("auth://onLoginSubmit"));
        try
        {
          paramWebView = Uri.parse(paramString).getPathSegments();
          if (!paramWebView.isEmpty())
            AuthDialog.b(AuthDialog.this, (String)paramWebView.get(0));
          label531: return true;
          Log.d("AuthDialog", "-->Redirect URL: return false");
          return false;
        }
        catch (Exception paramWebView)
        {
          break label531;
        }
      }
      label548: return true;
    }
  }

  private class OnTimeListener
    implements IUiListener
  {
    String a;
    String b;
    private String d;
    private IUiListener e;

    public OnTimeListener(String paramString1, String paramString2, String paramIUiListener, IUiListener arg5)
    {
      this.d = paramString1;
      this.a = paramString2;
      this.b = paramIUiListener;
      Object localObject;
      this.e = localObject;
    }

    private void a(String paramString)
    {
      try
      {
        onComplete(Util.parseJson(paramString));
        return;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        onError(new UiError(-4, "服务器返回数据格式有误!", paramString));
      }
    }

    public void onCancel()
    {
      if (this.e != null)
      {
        this.e.onCancel();
        this.e = null;
      }
    }

    public void onComplete(Object paramObject)
    {
      paramObject = (JSONObject)paramObject;
      b.a().a(AuthDialog.a(AuthDialog.this), this.d + "_H5", SystemClock.elapsedRealtime(), 0L, 0L, paramObject.optInt("ret", -6), this.b, this.a, "1000067");
      if (this.e != null)
      {
        this.e.onComplete(paramObject);
        this.e = null;
      }
    }

    public void onError(UiError paramUiError)
    {
      if (paramUiError.errorMessage != null);
      for (String str = paramUiError.errorMessage + this.a; ; str = this.a)
      {
        b.a().a(AuthDialog.a(AuthDialog.this), this.d + "_H5", SystemClock.elapsedRealtime(), 0L, 0L, paramUiError.errorCode, this.b, str, "1000067");
        AuthDialog.a(AuthDialog.this, str);
        if (this.e != null)
        {
          this.e.onError(paramUiError);
          this.e = null;
        }
        return;
      }
    }
  }

  private class THandler extends Handler
  {
    private AuthDialog.OnTimeListener b;

    public THandler(AuthDialog.OnTimeListener paramLooper, Looper arg3)
    {
      super();
      this.b = paramLooper;
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        AuthDialog.OnTimeListener.a(this.b, (String)paramMessage.obj);
        return;
      case 2:
        this.b.onCancel();
        return;
      case 3:
      }
      AuthDialog.a(AuthDialog.a(AuthDialog.this), (String)paramMessage.obj);
    }
  }

  class TimeOutRunable
    implements Runnable
  {
    String a = "";

    public TimeOutRunable(String arg2)
    {
      Object localObject;
      this.a = localObject;
    }

    public void run()
    {
      Log.d("AuthDialog", "-->timeoutUrl: " + this.a + " | mRetryUrl: " + AuthDialog.h(AuthDialog.this));
      if (this.a.equals(AuthDialog.h(AuthDialog.this)))
      {
        AuthDialog.f(AuthDialog.this).onError(new UiError(9002, "请求页面超时，请稍后重试！", AuthDialog.h(AuthDialog.this)));
        AuthDialog.this.dismiss();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.connect.auth.AuthDialog
 * JD-Core Version:    0.6.0
 */