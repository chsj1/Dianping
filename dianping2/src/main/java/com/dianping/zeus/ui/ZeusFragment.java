package com.dianping.zeus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.zeus.client.WebChromeClientFactory;
import com.dianping.zeus.client.WebViewClientFactory;
import com.dianping.zeus.client.ZeusWebChromeClient;
import com.dianping.zeus.client.ZeusWebViewClient;
import com.dianping.zeus.js.JsBean;
import com.dianping.zeus.js.JsHandlerFactory;
import com.dianping.zeus.js.JsHost;
import com.dianping.zeus.js.jshandler.JsHandler;
import com.dianping.zeus.js.jshandler.SetTitleButtonJsHandler;
import com.dianping.zeus.utils.ViewUtils;
import com.dianping.zeus.utils.ZeusGaWrapper;
import com.dianping.zeus.widget.BaseTitleBar;
import com.dianping.zeus.widget.DefaultTitleBar;
import com.dianping.zeus.widget.LoadingErrorView;
import com.dianping.zeus.widget.NavigateBar;
import com.dianping.zeus.widget.ZeusWebView;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ZeusFragment extends Fragment
  implements JsHost, NavigateBarHost
{
  public static final int MSG_MONITOR_TIMEOUT = 101;
  public static final int REQ_OPEN_EXTERNAL = 99;
  public static final int RES_CLOSE_BUTTON_FINISH = 88;
  public static final int RES_CLOSE_BUTTON_VISIBLE = 77;
  public static final String TAG = ZeusFragment.class.getSimpleName();
  private boolean isFinished = false;
  public boolean mIsBtnCloseDisable = false;
  public boolean mIsBtnCloseShow;
  public boolean mIsNoTitleBar;
  public boolean mIsThirdParty = false;
  public final HashMap<String, JsHandler> mJsHandlerMap = new HashMap();
  public FrameLayout mLayMask;
  public BaseTitleBar mLayTitle;
  public FrameLayout mLayVideo;
  public LinearLayout mLayWeb;
  protected ArrayList<NameValuePair> mLoginParams = new ArrayList();
  public Handler mMonitorTimeoutHandler;
  public ZeusWebChromeClient mNovaEfteWebChromeClient;
  public final HashMap<String, JsHandler> mSubscribeJsHandlerMap = new HashMap();
  public String mTitle;
  private ViewGroup.LayoutParams mTitleParams;
  public TextView mTvUrl;
  private boolean onScroll;
  protected String url;
  protected WebView webView;

  private void resetMask()
  {
    if (this.mLayMask == null)
      return;
    int j = R.string.service_unavailable;
    int i = 1;
    try
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)getContext().getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo != null)
      {
        boolean bool = localNetworkInfo.isConnected();
        if (bool)
          break label77;
      }
      label77: for (i = 1; ; i = 0)
      {
        if (i != 0)
          j = R.string.default_error_message;
        try
        {
          ((TextView)this.mLayMask.findViewById(16908308)).setText(j);
          return;
        }
        catch (Exception localException1)
        {
          return;
        }
      }
    }
    catch (Exception localException2)
    {
      while (true)
        Log.e(TAG, localException2.toString());
    }
  }

  private void showCloseButton(boolean paramBoolean)
  {
    this.mIsBtnCloseShow = paramBoolean;
    BaseTitleBar localBaseTitleBar = getTitleBarHost();
    if (!paramBoolean);
    for (boolean bool = true; ; bool = false)
    {
      localBaseTitleBar.setLRButton(null, null, bool, null);
      if (paramBoolean)
        getTitleBarHost().setLLButton(null, getDefaultCustomBackIcon(), false, null);
      return;
    }
  }

  public boolean canGoBack()
  {
    if (TextUtils.isEmpty(this.url))
      return false;
    return this.webView.canGoBack();
  }

  @Deprecated
  public int configDomainPermission(String paramString)
  {
    if (isInWhiteList(paramString))
      return 255;
    return 0;
  }

  public BaseTitleBar createDefaultTitleBar()
  {
    return new DefaultTitleBar(getContext());
  }

  protected ZeusWebChromeClient createWebChromeClient()
  {
    this.mNovaEfteWebChromeClient = WebChromeClientFactory.createClient(this, getActivity().getIntent());
    return this.mNovaEfteWebChromeClient;
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return WebViewClientFactory.createClient(this, getActivity().getIntent());
  }

  public void doWebMonitor(String paramString, int paramInt, long paramLong)
  {
    ZeusGaWrapper.pv(0L, paramString, 0, 0, paramInt, 0, 0, (int)paramLong);
  }

  public void finish()
  {
    this.isFinished = true;
    Handler localHandler = this.webView.getHandler();
    if (localHandler != null)
      localHandler.removeCallbacksAndMessages(null);
    if (this.mMonitorTimeoutHandler.hasMessages(101))
      this.mMonitorTimeoutHandler.removeMessages(101);
    if (getActivity() == null)
      return;
    getActivity().finish();
  }

  public Context getContext()
  {
    return this.webView.getContext();
  }

  public int getDefaultBackIcon()
  {
    return R.drawable.ic_web_back;
  }

  public int getDefaultCloseIcon()
  {
    return R.drawable.ic_web_close;
  }

  public int getDefaultCustomBackIcon()
  {
    return R.drawable.ic_web_back_text;
  }

  public int getDefaultSearchIcon()
  {
    return R.drawable.ic_web_search;
  }

  public int getDefaultShareIcon()
  {
    return R.drawable.icon_web_share;
  }

  @Deprecated
  public int getDomainPermission()
  {
    return configDomainPermission(getUrl());
  }

  public JsHandler getJsHandler(String paramString)
  {
    return (JsHandler)this.mJsHandlerMap.get(paramString);
  }

  @Deprecated
  public int getLayoutId()
  {
    return R.layout.fragment_zeus_web;
  }

  public int getNavigatorLayoutId()
  {
    return R.layout.web_navi_bar;
  }

  public JsHandler getSubscribeJsHandler(String paramString)
  {
    return (JsHandler)this.mSubscribeJsHandlerMap.get(paramString);
  }

  public BaseTitleBar getTitleBarHost()
  {
    return this.mLayTitle;
  }

  public String getUrl()
  {
    return this.url;
  }

  public int getWebLayoutId()
  {
    return R.layout.web_webview;
  }

  public abstract int getWebTimeout();

  public WebView getWebView(View paramView)
  {
    return (WebView)paramView.findViewById(R.id.layout_webview);
  }

  public void goBack()
  {
    if (this.mNovaEfteWebChromeClient.mCustomView != null)
    {
      this.mNovaEfteWebChromeClient.onHideCustomView();
      return;
    }
    if (canGoBack())
    {
      this.webView.goBack();
      showCloseButton(true);
      return;
    }
    getActivity().setResult(77);
    finish();
  }

  protected void handleArgments()
  {
    if (getArguments() == null);
    String str;
    do
    {
      return;
      str = getArguments().getString("url");
    }
    while (TextUtils.isEmpty(str));
    try
    {
      this.url = URLDecoder.decode(str);
      return;
    }
    catch (Exception localException)
    {
      this.url = str;
    }
  }

  public void hideMask()
  {
    if (this.mLayMask == null)
      return;
    ViewUtils.hideView(this.mLayMask, true);
    ViewUtils.showView(this.webView);
  }

  protected void initMask(View paramView)
  {
    this.mLayMask = ((FrameLayout)paramView.findViewById(R.id.mask));
    if (this.mLayMask == null)
      return;
    this.mLayMask.removeAllViews();
    ((LoadingErrorView)getActivity().getLayoutInflater().inflate(R.layout.zeus_error_item, this.mLayMask, true).findViewById(R.id.error)).setCallBack(new ZeusFragment.7(this));
  }

  protected void initTitleBar()
  {
    boolean bool;
    if (this.mLayTitle != null)
    {
      BaseTitleBar localBaseTitleBar = this.mLayTitle;
      if (!this.mIsNoTitleBar)
      {
        bool = true;
        localBaseTitleBar.showTitleBar(bool);
      }
    }
    else
    {
      if (!this.mIsThirdParty)
        break label127;
      getTitleBarHost().setLLButton(null, R.drawable.ic_left_title_bar_close, false, new ZeusFragment.3(this));
      label53: getTitleBarHost().setLRButton(null, getDefaultCloseIcon(), false, new ZeusFragment.6(this));
      if ((!this.mIsBtnCloseShow) || (this.mIsBtnCloseDisable))
        break label189;
      getTitleBarHost().setLRButton(null, null, false, null);
    }
    while (true)
    {
      getTitleBarHost().setRLButton(null, null, true, null);
      getTitleBarHost().setRRButton(null, null, true, null);
      return;
      bool = false;
      break;
      label127: if ((this.mIsBtnCloseShow) && (!this.mIsBtnCloseDisable))
      {
        getTitleBarHost().setLLButton(null, getDefaultCustomBackIcon(), false, new ZeusFragment.4(this));
        break label53;
      }
      getTitleBarHost().setLLButton(null, getDefaultBackIcon(), false, new ZeusFragment.5(this));
      break label53;
      label189: getTitleBarHost().setLRButton(null, null, true, null);
    }
  }

  protected void initWebView()
  {
    WebSettings localWebSettings = this.webView.getSettings();
    this.webView.setWebChromeClient(createWebChromeClient());
    this.webView.setWebViewClient(createWebViewClient());
    this.webView.setScrollBarStyle(0);
    if (Build.VERSION.SDK_INT >= 11)
      this.webView.removeJavascriptInterface("searchBoxJavaBridge_");
    if (Build.VERSION.SDK_INT > 16)
      this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
    if (isDebug());
    try
    {
      WebView.class.getMethod("setWebContentsDebuggingEnabled", new Class[] { Boolean.TYPE }).invoke(null, new Object[] { Boolean.TRUE });
      label115: setupWebSettings(localWebSettings);
      if ((this.webView instanceof ZeusWebView))
      {
        ((ZeusWebView)this.webView).setResizeListener(new ZeusFragment.1(this));
        ((ZeusWebView)this.webView).setScrollListener(new ZeusFragment.2(this));
      }
      return;
    }
    catch (Exception localException)
    {
      break label115;
    }
  }

  public boolean isActivated()
  {
    return (!this.isFinished) && (isVisible()) && (getActivity() != null);
  }

  public abstract boolean isDebug();

  public abstract boolean isInWhiteList(String paramString);

  public boolean isOnScroll()
  {
    return this.onScroll;
  }

  public void loadJs(String paramString)
  {
    if (isDebug())
      Log.d(TAG, "loadJS: " + paramString);
    if (this.isFinished)
    {
      Log.e(TAG, "ZeusFragment is going to exit, can't load url=" + paramString);
      return;
    }
    this.webView.loadUrl(paramString);
  }

  public void loadUrl(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    if (isInWhiteList(paramString))
      this.webView.loadUrl(paramString, null);
    while (true)
    {
      this.url = paramString;
      return;
      this.webView.loadUrl(paramString);
    }
  }

  public void navigateBackward()
  {
    if (this.webView.canGoBack())
      this.webView.goBack();
  }

  public void navigateForward()
  {
    if (this.webView.canGoForward())
      this.webView.goForward();
  }

  public void navigateRefresh()
  {
    this.webView.reload();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (!TextUtils.isEmpty(this.url))
      loadUrl(processUrl(this.url));
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Iterator localIterator = this.mJsHandlerMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (TextUtils.isEmpty((String)localEntry.getKey()))
        continue;
      ((JsHandler)localEntry.getValue()).onActivityResult(paramInt1, paramInt2, paramIntent);
    }
    if (paramInt1 == 99)
    {
      if (paramInt2 != 88)
        break label106;
      getActivity().setResult(88);
      finish();
    }
    label106: 
    do
      return;
    while ((paramInt2 != 77) || (this.mJsHandlerMap.containsKey("setLRButton")) || (this.mIsBtnCloseDisable));
    showCloseButton(true);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    JsHandlerFactory.addJsHost(this);
    handleArgments();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    return paramLayoutInflater.inflate(getLayoutId(), paramViewGroup, false);
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.isFinished = true;
    Iterator localIterator = this.mJsHandlerMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (TextUtils.isEmpty((String)localEntry.getKey()))
        continue;
      ((JsHandler)localEntry.getValue()).onDestroy();
    }
    if (this.mLayWeb != null)
      this.mLayWeb.removeAllViews();
    this.webView.removeAllViews();
    this.webView.destroy();
    JsHandlerFactory.removeJsHost(this);
  }

  public void onPause()
  {
    super.onPause();
    if (Build.VERSION.SDK_INT >= 11)
      this.webView.onPause();
    loadJs("javascript:window.DPApp && window.DPApp.onDisappear && window.DPApp.onDisappear()");
  }

  public void onResume()
  {
    super.onResume();
    if (Build.VERSION.SDK_INT >= 11)
      this.webView.onResume();
    loadJs("javascript:window.DPApp && window.DPApp.onAppear && window.DPApp.onAppear()");
  }

  public void onStart()
  {
    super.onStart();
    publish("foreground");
  }

  public void onStop()
  {
    super.onStop();
    publish("background");
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    boolean bool2 = false;
    super.onViewCreated(paramView, paramBundle);
    this.mLayWeb = ((LinearLayout)paramView.findViewById(R.id.lay_web_parent));
    paramBundle = (ViewStub)paramView.findViewById(R.id.web_webview);
    paramBundle.setLayoutResource(getWebLayoutId());
    paramBundle.inflate();
    this.webView = getWebView(paramView);
    initWebView();
    this.mLayTitle = createDefaultTitleBar();
    this.mTitleParams = new ViewGroup.LayoutParams(-1, getContext().getResources().getDimensionPixelSize(R.dimen.titlebar_height));
    this.mLayWeb.addView(this.mLayTitle, 0, this.mTitleParams);
    paramBundle = getActivity().getIntent().getData().getQueryParameter("notitlebar");
    boolean bool1;
    if (("1".equals(paramBundle)) || ("true".equals(paramBundle)))
    {
      bool1 = true;
      this.mIsNoTitleBar = bool1;
      initTitleBar();
      paramBundle = (ViewStub)paramView.findViewById(R.id.web_navi_bar);
      String str = getActivity().getIntent().getData().getQueryParameter("thirdparty");
      if (!"1".equals(str))
      {
        bool1 = bool2;
        if (!"true".equals(str));
      }
      else
      {
        bool1 = true;
      }
      this.mIsThirdParty = bool1;
      if (!this.mIsThirdParty)
        break label393;
      paramBundle.setLayoutResource(getNavigatorLayoutId());
      paramBundle.inflate();
      paramBundle = paramView.findViewById(R.id.lay_navigator);
      if (paramBundle != null)
      {
        ((NavigateBar)paramBundle).setHost(this);
        ViewUtils.showView(paramBundle);
      }
      if (this.mLayWeb != null)
      {
        paramBundle = (FrameLayout.LayoutParams)this.mLayWeb.getLayoutParams();
        paramBundle.bottomMargin = ViewUtils.dip2px(getActivity(), 56.0F);
        this.mLayWeb.setLayoutParams(paramBundle);
      }
    }
    while (true)
    {
      initMask(paramView);
      this.mLayVideo = ((FrameLayout)paramView.findViewById(R.id.video));
      this.mTvUrl = ((TextView)paramView.findViewById(R.id.url));
      if (this.mTvUrl != null)
      {
        paramView = new ZeusFragment.CopyOnClickListener(this, null);
        this.mTvUrl.setOnClickListener(paramView);
      }
      ViewUtils.showView(this.mTvUrl, isDebug(), true);
      this.mMonitorTimeoutHandler = new ZeusFragment.MonitorTimeoutHandler(this);
      return;
      bool1 = false;
      break;
      label393: paramBundle.setVisibility(8);
    }
  }

  public void post(Runnable paramRunnable)
  {
    this.webView.post(paramRunnable);
  }

  public String processUrl(String paramString)
  {
    this.mTitle = Uri.parse(paramString).getQueryParameter("title");
    if ((TextUtils.isEmpty(this.mTitle)) && (getActivity() != null))
      this.mTitle = getActivity().getIntent().getData().getQueryParameter("title");
    this.mLoginParams.add(new BasicNameValuePair("logintype", Uri.parse(paramString).getQueryParameter("logintype")));
    this.mLoginParams.add(new BasicNameValuePair("cannormallogin", Uri.parse(paramString).getQueryParameter("cannormallogin")));
    return paramString;
  }

  public void publish(String paramString)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("action", paramString);
      publish(localJSONObject);
      return;
    }
    catch (JSONException paramString)
    {
      while (true)
        Log.e(TAG, paramString.toString());
    }
  }

  public void publish(JSONObject paramJSONObject)
  {
    try
    {
      paramJSONObject.put("status", "action");
      JsHandler localJsHandler = getSubscribeJsHandler(paramJSONObject.optString("action"));
      if (localJsHandler != null)
        localJsHandler.jsCallback(paramJSONObject);
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        Log.e(TAG, localJSONException.toString());
    }
  }

  public void putJsHandler(JsHandler paramJsHandler)
  {
    this.mJsHandlerMap.put(paramJsHandler.jsBean().method, paramJsHandler);
  }

  public void replaceTitleBar(BaseTitleBar paramBaseTitleBar)
  {
    this.mLayWeb.removeView(this.mLayTitle);
    this.mLayTitle = paramBaseTitleBar;
    this.mLayWeb.addView(this.mLayTitle, 0, this.mTitleParams);
    initTitleBar();
    paramBaseTitleBar = this.mJsHandlerMap.values().iterator();
    while (paramBaseTitleBar.hasNext())
    {
      JsHandler localJsHandler = (JsHandler)paramBaseTitleBar.next();
      if (!(localJsHandler instanceof SetTitleButtonJsHandler))
        continue;
      ((SetTitleButtonJsHandler)localJsHandler).setTitleButton();
    }
  }

  public void resetJsHandler()
  {
    this.mJsHandlerMap.clear();
    this.mSubscribeJsHandlerMap.clear();
    initTitleBar();
  }

  public void setBackgroundColor(int paramInt)
  {
    this.webView.setBackgroundColor(paramInt);
  }

  public void setOnScroll(boolean paramBoolean)
  {
    this.onScroll = paramBoolean;
  }

  public void setTitle(String paramString)
  {
    getTitleBarHost().setWebTitle(paramString);
  }

  protected void setupWebSettings(WebSettings paramWebSettings)
  {
    paramWebSettings.setBuiltInZoomControls(false);
    paramWebSettings.setSupportZoom(false);
    paramWebSettings.setSaveFormData(false);
    paramWebSettings.setSavePassword(false);
    paramWebSettings.setAllowFileAccess(true);
    paramWebSettings.setUseWideViewPort(true);
    paramWebSettings.setLoadWithOverviewMode(true);
    paramWebSettings.setLoadsImagesAutomatically(true);
    paramWebSettings.setJavaScriptEnabled(true);
    paramWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    paramWebSettings.setDomStorageEnabled(true);
    paramWebSettings.setDatabaseEnabled(true);
    paramWebSettings.setDatabasePath(getContext().getApplicationContext().getDatabasePath("webview").getAbsolutePath());
    paramWebSettings.setAppCacheEnabled(true);
    paramWebSettings.setAppCachePath(getContext().getApplicationContext().getCacheDir().getAbsolutePath() + File.separator + "webview");
    paramWebSettings.setCacheMode(-1);
    paramWebSettings.setGeolocationEnabled(true);
    Object localObject;
    if (Build.VERSION.SDK_INT >= 16)
      localObject = paramWebSettings.getClass();
    try
    {
      localObject = ((Class)localObject).getMethod("setAllowUniversalAccessFromFileURLs", new Class[] { Boolean.TYPE });
      if (localObject != null)
        ((Method)localObject).invoke(paramWebSettings, new Object[] { Boolean.valueOf(true) });
      label189: if (Build.VERSION.SDK_INT >= 21)
        paramWebSettings.setMixedContentMode(0);
      return;
    }
    catch (Exception localException)
    {
      break label189;
    }
  }

  public abstract void share();

  public void showMask()
  {
    if (this.mLayMask == null)
      return;
    resetMask();
    ViewUtils.showView(this.mLayMask);
    ViewUtils.hideView(this.webView, false);
  }

  public void subscribe(String paramString, JsHandler paramJsHandler)
  {
    this.mSubscribeJsHandlerMap.put(paramString, paramJsHandler);
  }

  public void unsubscribe(String paramString)
  {
    this.mSubscribeJsHandlerMap.remove(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.zeus.ui.ZeusFragment
 * JD-Core Version:    0.6.0
 */