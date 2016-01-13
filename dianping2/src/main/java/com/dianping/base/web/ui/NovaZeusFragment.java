package com.dianping.base.web.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.impl.BaseAccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.web.client.NovaZeusWebViewClient;
import com.dianping.base.web.js.ShareJsHandler;
import com.dianping.base.web.util.PushRestoreUrlV100;
import com.dianping.base.web.util.WebUtils;
import com.dianping.base.web.util.WebViewUploadPhotoStore;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.monitor.MonitorService;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.log.NovaLog;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.zeus.client.ZeusWebViewClient;
import com.dianping.zeus.js.jshandler.BaseJsHandler;
import com.dianping.zeus.ui.ComplexButton;
import com.dianping.zeus.ui.ZeusFragment;
import com.dianping.zeus.widget.BaseTitleBar;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class NovaZeusFragment extends ZeusFragment
  implements CityConfig.SwitchListener, PullToRefreshBase.OnRefreshListener
{
  private static final List<String> PARAM_KEYS;
  private static final int PENDING_EVEND_ID_LOCATE = 4;
  private static final int PENDING_EVENT_ID_ACCOUNT = 2;
  public static final String PREF_JSBRIDGE_STORAGE = "jsbridge_storage";
  private static final String TAG = NovaZeusFragment.class.getSimpleName();
  private static Pattern sParamPattern;
  private BroadcastReceiver mInjectJsReceiver;
  public boolean mIsFromPush;
  private boolean mIsOverSeas = false;
  private int mPendingEvendId;
  private String mPendingUrl;
  private PullToRefreshWebView mPullTORefreshWebView;
  public String mShareContent;

  static
  {
    PARAM_KEYS = new ArrayList();
    PARAM_KEYS.add("agent");
    PARAM_KEYS.add("version");
    PARAM_KEYS.add("cityid");
    PARAM_KEYS.add("token");
    PARAM_KEYS.add("newtoken");
    PARAM_KEYS.add("dpid");
    PARAM_KEYS.add("latitude");
    PARAM_KEYS.add("longitude");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(\\?|&|#)(");
    int i = 0;
    int j = PARAM_KEYS.size();
    while (i < j)
    {
      if (i > 0)
        localStringBuilder.append("|");
      localStringBuilder.append((String)PARAM_KEYS.get(i));
      i += 1;
    }
    localStringBuilder.append(")=(\\*|!)");
    sParamPattern = Pattern.compile(localStringBuilder.toString(), 2);
  }

  public static boolean hasRequestParam(String paramString)
  {
    if (!WebUtils.isFromDP(paramString));
    do
    {
      return false;
      paramString = sParamPattern.matcher(paramString);
    }
    while (!paramString.find());
    Log.d(TAG, "find param: " + paramString.group());
    return true;
  }

  private String processParam(String paramString)
  {
    int i = paramString.indexOf('?');
    if (i < 0)
      return paramString;
    String str1 = paramString.substring(0, i + 1);
    Object localObject1 = paramString.substring(i + 1);
    Matcher localMatcher = sParamPattern.matcher(paramString);
    label821: label1218: label1989: 
    while (localMatcher.find())
    {
      Object localObject2 = localMatcher.group();
      Log.d(TAG, "process param: " + (String)localObject2);
      String str2 = ((String)localObject2).substring(1);
      String[] arrayOfString = str2.split("=");
      if (arrayOfString.length <= 1)
        continue;
      arrayOfString[0] = arrayOfString[0].toLowerCase();
      if ((!PARAM_KEYS.contains(arrayOfString[0])) || ((!"*".equals(arrayOfString[1])) && (!"!".equals(arrayOfString[1]))))
        continue;
      localObject2 = localObject1;
      if ("agent".equals(arrayOfString[0]))
        localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + "android");
      localObject1 = localObject2;
      if ("version".equals(arrayOfString[0]))
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + Environment.versionName());
      localObject2 = localObject1;
      if ("screen".equals(arrayOfString[0]))
        localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + DeviceUtils.screen(getActivity()));
      localObject1 = localObject2;
      if ("sessionid".equals(arrayOfString[0]))
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + Environment.sessionId());
      localObject2 = localObject1;
      if ("deviceid".equals(arrayOfString[0]))
        localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + DeviceUtils.imei());
      localObject1 = localObject2;
      if ("uuid".equals(arrayOfString[0]))
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + DeviceUtils.uuid());
      localObject2 = localObject1;
      if ("dpid".equals(arrayOfString[0]))
        localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + DeviceUtils.dpid());
      localObject1 = localObject2;
      if ("cityid".equals(arrayOfString[0]))
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + ((NovaActivity)getActivity()).cityId());
      localObject2 = localObject1;
      Object localObject3;
      Object localObject4;
      label741: label894: int j;
      if ("newtoken".equals(arrayOfString[0]))
      {
        localObject3 = ((BaseAccountService)((NovaActivity)getActivity()).accountService()).newToken();
        if ("*".equals(arrayOfString[1]))
        {
          localObject4 = new StringBuilder().append(arrayOfString[0]).append("=");
          localObject2 = localObject3;
          if (localObject3 == null)
            localObject2 = "";
          localObject2 = ((String)localObject1).replace(str2, (String)localObject2);
        }
      }
      else
      {
        localObject1 = localObject2;
        if ("token".equals(arrayOfString[0]))
        {
          localObject3 = ((NovaActivity)getActivity()).accountService().token();
          if (!"*".equals(arrayOfString[1]))
            break label1480;
          localObject4 = new StringBuilder().append(arrayOfString[0]).append("=");
          localObject1 = localObject3;
          if (localObject3 == null)
            localObject1 = "";
          localObject1 = ((String)localObject2).replace(str2, (String)localObject1);
        }
        localObject4 = getCurrentLocation();
        localObject3 = ((NovaActivity)getActivity()).locationService().location();
        localObject2 = localObject1;
        StringBuilder localStringBuilder;
        if ("latitude".equals(arrayOfString[0]))
        {
          localObject2 = localObject1;
          if ("*".equals(arrayOfString[1]))
          {
            localStringBuilder = new StringBuilder().append(arrayOfString[0]).append("=");
            if (localObject4 != null)
              break label1602;
            localObject2 = "";
            localObject2 = ((String)localObject1).replace(str2, (String)localObject2);
          }
        }
        localObject1 = localObject2;
        if ("longitude".equals(arrayOfString[0]))
        {
          localObject1 = localObject2;
          if ("*".equals(arrayOfString[1]))
          {
            localStringBuilder = new StringBuilder().append(arrayOfString[0]).append("=");
            if (localObject4 != null)
              break label1617;
            localObject1 = "";
            localObject1 = ((String)localObject2).replace(str2, (String)localObject1);
          }
        }
        localObject2 = localObject1;
        if ("accuracy".equals(arrayOfString[0]))
        {
          localObject2 = localObject1;
          if ("*".equals(arrayOfString[1]))
          {
            localStringBuilder = new StringBuilder().append(arrayOfString[0]).append("=");
            if (localObject4 != null)
              break label1632;
            localObject2 = "";
            label967: localObject2 = ((String)localObject1).replace(str2, (String)localObject2);
          }
        }
        localObject1 = localObject2;
        if ("address".equals(arrayOfString[0]))
        {
          localObject1 = localObject2;
          if ("*".equals(arrayOfString[1]))
          {
            if (localObject3 != null)
              break label1648;
            localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=");
          }
        }
        j = 0;
        localObject2 = localObject1;
        i = j;
        if (0 == 0)
        {
          localObject2 = localObject1;
          i = j;
          if ("latitude".equals(arrayOfString[0]))
          {
            localObject2 = localObject1;
            i = j;
            if ("!".equals(arrayOfString[1]))
            {
              if (localObject4 != null)
                break label1740;
              i = 1;
              localObject2 = localObject1;
            }
          }
        }
        label1104: localObject1 = localObject2;
        j = i;
        if (i == 0)
        {
          localObject1 = localObject2;
          j = i;
          if ("longitude".equals(arrayOfString[0]))
          {
            localObject1 = localObject2;
            j = i;
            if ("!".equals(arrayOfString[1]))
            {
              if (localObject4 != null)
                break label1790;
              j = 1;
              localObject1 = localObject2;
            }
          }
        }
        localObject2 = localObject1;
        i = j;
        if (j == 0)
        {
          localObject2 = localObject1;
          i = j;
          if ("accuracy".equals(arrayOfString[0]))
          {
            localObject2 = localObject1;
            i = j;
            if ("!".equals(arrayOfString[1]))
            {
              if (localObject4 != null)
                break label1840;
              i = 1;
              localObject2 = localObject1;
            }
          }
        }
        localObject1 = localObject2;
        j = i;
        if (i == 0)
        {
          localObject1 = localObject2;
          j = i;
          if ("address".equals(arrayOfString[0]))
          {
            localObject1 = localObject2;
            j = i;
            if ("!".equals(arrayOfString[1]))
            {
              if (localObject3 != null)
                break label1891;
              j = 1;
              localObject1 = localObject2;
            }
          }
        }
      }
      while (true)
      {
        if (j == 0)
          break label1989;
        this.mPendingUrl = paramString;
        this.mPendingEvendId = 4;
        if (((NovaActivity)getActivity()).locationService().status() <= 0)
          ((NovaActivity)getActivity()).locationService().refresh();
        if (((NovaActivity)getActivity()).locationService().status() != -1)
          ((NovaActivity)getActivity()).showProgressDialog("正在定位...");
        return null;
        localObject2 = localObject1;
        if (!"!".equals(arrayOfString[1]))
          break;
        if (localObject3 != null)
        {
          localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + (String)localObject3);
          break;
        }
        this.mLoginParams.add(new BasicNameValuePair("title", this.mTitle));
        ((NovaActivity)getActivity()).gotoLogin(this.mLoginParams);
        this.mPendingUrl = (str1 + (String)localObject1);
        this.mPendingEvendId = 2;
        return null;
        label1480: localObject1 = localObject2;
        if (!"!".equals(arrayOfString[1]))
          break label741;
        if (localObject3 != null)
        {
          localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + (String)localObject3);
          break label741;
        }
        this.mLoginParams.add(new BasicNameValuePair("title", this.mTitle));
        ((NovaActivity)getActivity()).gotoLogin(this.mLoginParams);
        this.mPendingUrl = (str1 + (String)localObject2);
        this.mPendingEvendId = 2;
        return null;
        localObject2 = Location.FMT.format(((Location)localObject4).latitude());
        break label821;
        label1617: localObject1 = Location.FMT.format(((Location)localObject4).longitude());
        break label894;
        label1632: localObject2 = Location.FMT.format(((Location)localObject4).accuracy());
        break label967;
        label1648: if (!android.text.TextUtils.isEmpty(((DPObject)localObject3).getString("Road")))
        {
          localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + Uri.encode(((DPObject)localObject3).getString("Address")));
          break label1045;
        }
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=");
        break label1045;
        label1740: localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + Location.FMT.format(((Location)localObject4).latitude()));
        i = j;
        break label1104;
        label1790: localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + Location.FMT.format(((Location)localObject4).longitude()));
        j = i;
        break label1161;
        localObject2 = ((String)localObject1).replace(str2, arrayOfString[0] + "=" + Location.FMT.format(((Location)localObject4).accuracy()));
        i = j;
        break label1218;
        label1891: if (!android.text.TextUtils.isEmpty(((DPObject)localObject3).getString("Road")))
        {
          localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=" + Uri.encode(((DPObject)localObject3).getString("Address")));
          j = i;
          continue;
        }
        localObject1 = ((String)localObject2).replace(str2, arrayOfString[0] + "=");
        j = i;
      }
    }
    label1045: label1602: return (String)(String)(String)(String)(str1 + (String)localObject1);
  }

  private String utm()
  {
    String str;
    try
    {
      Object localObject2 = getActivity().getIntent().getData().getQueryParameter("_utm");
      Object localObject1 = localObject2;
      if (localObject2 == null)
        localObject1 = getActivity().getIntent().getData().getQueryParameter("utm_");
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = getArguments().getString("utm");
      localObject1 = localObject2;
      if (localObject2 == null)
        return "";
    }
    catch (Exception str)
    {
      localException.printStackTrace();
      str = "";
    }
    return (String)(String)str;
  }

  public String addDefaultParams(String paramString)
  {
    if (!WebUtils.isFromDP(paramString))
      return paramString;
    Uri.Builder localBuilder = Uri.parse(paramString).buildUpon();
    if (!paramString.contains("token="))
      localBuilder.appendQueryParameter("token", "*");
    if (!paramString.contains("agent="))
      localBuilder.appendQueryParameter("agent", "*");
    if (!paramString.contains("cityid="))
      localBuilder.appendQueryParameter("cityid", "*");
    if (!paramString.contains("version="))
      localBuilder.appendQueryParameter("version", "*");
    return localBuilder.build().toString();
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return new NovaZeusWebViewClient(this);
  }

  public void doWebMonitor(String paramString, int paramInt, long paramLong)
  {
    ((MonitorService)DPApplication.instance().getService("monitor")).pv(0L, paramString, 0, 0, paramInt, 0, 0, (int)paramLong);
    Log.d(TAG, "doWebMonitor: command=" + paramString + "; code=" + paramInt + "; elapse=" + (int)paramLong);
  }

  public void ga()
  {
    if ((this.webView instanceof NovaWebView))
      ((NovaWebView)this.webView).ga(this.url);
  }

  public Location getCurrentLocation()
  {
    if (((NovaActivity)getActivity()).locationService().location() == null)
      return null;
    try
    {
      Location localLocation = (Location)((NovaActivity)getActivity()).locationService().location().decodeToObject(Location.DECODER);
      return localLocation;
    }
    catch (ArchiveException localArchiveException)
    {
      localArchiveException.printStackTrace();
    }
    return null;
  }

  public String getPageName()
  {
    if (this.mIsThirdParty)
      return "exweb";
    if (this.mIsOverSeas)
      return "overseas_home";
    return "web";
  }

  public int getWebLayoutId()
  {
    return R.layout.nova_web;
  }

  public int getWebTimeout()
  {
    if ("2G".equals(NetworkUtils.getNetworkType(getContext())))
      return 15000;
    return 10000;
  }

  public WebView getWebView(View paramView)
  {
    return ((PullToRefreshWebView)paramView.findViewById(R.id.webview)).getWebView();
  }

  public boolean isDebug()
  {
    return Environment.isDebug();
  }

  public boolean isInWhiteList(String paramString)
  {
    int i = 0;
    if (!WebUtils.isFromDP(getUrl()))
    {
      paramString = getContext().getSharedPreferences("webview_jsbridge_settings", 0);
      if (!isDebug())
        break label50;
    }
    label50: for (boolean bool = true; ; bool = false)
    {
      if (paramString.getBoolean("whitelistdisable", bool))
        i = 1;
      return i;
    }
  }

  public void loadUrl(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString));
    while (true)
    {
      return;
      if (!hasRequestParam(paramString))
        break;
      paramString = processParam(paramString);
      if (paramString == null)
        continue;
      loadUrl(paramString);
      return;
    }
    super.loadUrl(paramString);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    ((DPActivity)getActivity()).cityConfig().addListener(this);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    paramCity1 = new JSONObject();
    try
    {
      paramCity1.put("action", "switchCity");
      paramCity1.put("cityId", paramCity2.id());
      publish(paramCity1);
      return;
    }
    catch (JSONException paramCity2)
    {
      while (true)
        Log.e(paramCity2.toString());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter();
    paramBundle.addAction("Intent.Action.Web_InjectJs_Weinre");
    this.mInjectJsReceiver = new NovaZeusFragment.1(this);
    getActivity().registerReceiver(this.mInjectJsReceiver, paramBundle);
  }

  public void onDestroy()
  {
    super.onDestroy();
    ((DPActivity)getActivity()).cityConfig().removeListener(this);
    getActivity().unregisterReceiver(this.mInjectJsReceiver);
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (this.mPendingEvendId != 4)
      return;
    if (paramLocationService.location() == null)
    {
      if (paramLocationService.status() == -1)
        ((NovaActivity)getActivity()).showToast("定位失败");
      this.mPendingUrl = null;
      this.mPendingEvendId = 0;
    }
    while (true)
    {
      ((NovaActivity)getActivity()).dismissDialog();
      return;
      this.url = this.mPendingUrl;
      this.mPendingUrl = null;
      this.mPendingEvendId = 0;
      if (android.text.TextUtils.isEmpty(this.url))
        continue;
      this.url = processUrl(this.url);
      loadUrl(this.url);
    }
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if ((!paramBoolean) || (getActivity() == null))
      return true;
    if (((NovaActivity)getActivity()).accountService().token() != null);
    for (int i = 1; ; i = 0)
    {
      if ((i != 0) && (this.mPendingEvendId == 2) && (this.mPendingUrl != null))
      {
        this.url = this.mPendingUrl;
        this.mPendingUrl = null;
        this.mPendingEvendId = 0;
        if (!android.text.TextUtils.isEmpty(this.url))
        {
          this.url = processUrl(this.url);
          loadUrl(this.url);
        }
      }
      publish("loginSuccess");
      return true;
    }
  }

  public void onLoginCancel()
  {
    if (this.mPendingEvendId == 2)
    {
      this.mPendingUrl = null;
      this.mPendingEvendId = 0;
    }
    try
    {
      if (this.webView.copyBackForwardList().getSize() == 0)
        finish();
      return;
    }
    catch (Exception localException)
    {
      NovaLog.e(TAG, "onLoginCancel: " + localException);
      finish();
    }
  }

  public void onRefresh(PullToRefreshBase paramPullToRefreshBase)
  {
    if (this.mPullTORefreshWebView != null)
      this.mPullTORefreshWebView.onRefreshComplete();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.mIsOverSeas = "overseas".equals(getActivity().getIntent().getData().getHost());
    try
    {
      this.mPullTORefreshWebView = ((PullToRefreshWebView)paramView.findViewById(R.id.webview));
      this.mLayTitle.mButtonLL.setPerformClickListener(new NovaZeusFragment.2(this));
      this.mLayTitle.mButtonLR.setPerformClickListener(new NovaZeusFragment.3(this));
      return;
    }
    catch (Exception paramView)
    {
      while (true)
        paramView.printStackTrace();
    }
  }

  public String processUrl(String paramString)
  {
    this.mIsFromPush = getArguments().getBoolean("isFromPush", false);
    String str1 = paramString;
    if (this.mIsFromPush)
      str1 = PushRestoreUrlV100.restoreUrl(paramString);
    if (!str1.startsWith("http"));
    Object localObject;
    String str2;
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                return str1;
                paramString = utm();
                localObject = str1;
                if (!str1.contains("utm="))
                {
                  localObject = str1;
                  if (!com.dianping.util.TextUtils.isEmpty(paramString))
                    localObject = Uri.parse(str1).buildUpon().appendQueryParameter("utm", paramString).build().toString();
                }
                paramString = (String)localObject;
                if (WebUtils.isFromDP((String)localObject))
                {
                  paramString = (String)localObject;
                  if (this.mIsFromPush)
                    paramString = addDefaultParams((String)localObject);
                }
                localObject = Uri.parse(paramString);
                this.mTitle = ((Uri)localObject).getQueryParameter("title");
                if ((com.dianping.util.TextUtils.isEmpty(this.mTitle)) && (getActivity() != null))
                  this.mTitle = getActivity().getIntent().getData().getQueryParameter("title");
                this.mLoginParams.add(new BasicNameValuePair("logintype", Uri.parse(paramString).getQueryParameter("logintype")));
                this.mLoginParams.add(new BasicNameValuePair("cannormallogin", Uri.parse(paramString).getQueryParameter("cannormallogin")));
                str1 = paramString;
              }
              while (getActivity() == null);
              str1 = paramString;
            }
            while (getActivity().getIntent() == null);
            str1 = paramString;
          }
          while (getActivity().getIntent().getData() == null);
          str1 = paramString;
        }
        while (!"modifyphone".equals(getActivity().getIntent().getData().getHost()));
        str2 = getArguments().getString("goto");
        str1 = paramString;
      }
      while (!android.text.TextUtils.isEmpty(((Uri)localObject).getQueryParameter("goto")));
      str1 = paramString;
    }
    while (android.text.TextUtils.isEmpty(str2));
    paramString = URLDecoder.decode(str2);
    return (String)((Uri)localObject).buildUpon().appendQueryParameter("goto", paramString).build().toString();
  }

  public void publish(JSONObject paramJSONObject)
  {
    super.publish(paramJSONObject);
    Log.d(TAG, "do js publish : " + paramJSONObject.toString());
  }

  public String requestId()
  {
    if ((this.webView instanceof NovaWebView))
      return ((NovaWebView)this.webView).mRequestId;
    return "";
  }

  public void resetJsHandler()
  {
    super.resetJsHandler();
    WebViewUploadPhotoStore.instance().clearLocalIds();
    if (this.mPullTORefreshWebView != null)
    {
      this.mPullTORefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
      this.mPullTORefreshWebView.setOnRefreshListener(this);
    }
  }

  public void setBackgroundColor(int paramInt)
  {
    super.setBackgroundColor(paramInt);
    if (this.mPullTORefreshWebView != null)
      this.mPullTORefreshWebView.setBackgroundColor(paramInt);
  }

  public void setPullTORefreshListener(PullToRefreshBase.OnRefreshListener<WebView> paramOnRefreshListener)
  {
    if (this.mPullTORefreshWebView != null)
    {
      this.mPullTORefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
      this.mPullTORefreshWebView.setOnRefreshListener(paramOnRefreshListener);
    }
  }

  public void setPullToRefreshEnabled(boolean paramBoolean)
  {
    if (this.mPullTORefreshWebView == null)
      return;
    if (paramBoolean)
    {
      this.mPullTORefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
      this.mPullTORefreshWebView.setOnRefreshListener(this);
      return;
    }
    this.mPullTORefreshWebView.setMode(PullToRefreshBase.Mode.DISABLED);
  }

  public void setUrlOnly(String paramString)
  {
    this.url = paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("网页由 ");
    localStringBuilder.append(Uri.parse(paramString).getHost());
    localStringBuilder.append(" 提供");
    if (this.mPullTORefreshWebView != null)
      this.mPullTORefreshWebView.setPullText(localStringBuilder.toString());
  }

  protected void setupWebSettings(WebSettings paramWebSettings)
  {
    super.setupWebSettings(paramWebSettings);
    if (!paramWebSettings.getUserAgentString().contains(WebUtils.ua()))
      paramWebSettings.setUserAgentString(paramWebSettings.getUserAgentString() + " " + WebUtils.ua());
  }

  public void share()
  {
    if (com.dianping.util.TextUtils.isEmpty(this.mShareContent))
    {
      if (com.dianping.util.TextUtils.isEmpty(this.url))
        return;
      localObject = new ShareHolder();
      if (this.mLayTitle != null);
      for (((ShareHolder)localObject).title = this.mLayTitle.getWebTitle().toString().trim(); ; ((ShareHolder)localObject).title = "")
      {
        ((ShareHolder)localObject).webUrl = com.dianping.util.TextUtils.dropParameter(this.url);
        ShareUtil.gotoShareTo(getActivity(), ShareType.WEB, (Parcelable)localObject, "webpage5", "webpage5_share");
        return;
      }
    }
    Object localObject = new ShareJsHandler();
    try
    {
      ((BaseJsHandler)localObject).jsBean().argsJson = new JSONObject(this.mShareContent);
      ((BaseJsHandler)localObject).setJsHost(this);
      ((BaseJsHandler)localObject).exec();
      return;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
  }

  public void stopPullToRefreshView()
  {
    if (this.mPullTORefreshWebView != null)
      this.mPullTORefreshWebView.onRefreshComplete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.web.ui.NovaZeusFragment
 * JD-Core Version:    0.6.0
 */