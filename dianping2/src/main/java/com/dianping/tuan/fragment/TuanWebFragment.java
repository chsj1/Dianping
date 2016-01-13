package com.dianping.tuan.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.announce.AnnounceContentManager;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.tuan.activity.TuanWebActivity;
import com.dianping.v1.R.layout;
import com.dianping.zeus.client.ZeusWebViewClient;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class TuanWebFragment extends NovaZeusFragment
  implements LoginResultListener
{
  protected static final String ABOUT_BLANK = "about:blank";
  public static final String DEFAULT_ROOT_URL = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
  protected static final int PENDING_ACCOUNT = 2;
  protected static final int PENDING_GPS = 4;
  protected static final int PENDING_WAITING = 1;
  public static final String ROOT_URL = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
  AnnounceContentManager contentManager;
  protected LocationService locationService = DPApplication.instance().locationService();
  protected int pendingEvent;
  protected String pendingUrl;
  protected String tag;
  protected TextView textStack;
  protected TextView textUrl;

  public static String getSpecialUrlType(String paramString)
  {
    Object localObject1 = null;
    while (true)
    {
      try
      {
        Object localObject2 = Uri.parse(paramString);
        paramString = ((Uri)localObject2).getHost();
        if (!"m.t.dianping.com".equals(paramString))
        {
          if (!"m.t.51ping.com".equals(paramString))
            break label112;
          break label102;
          boolean bool2 = bool1;
          if (bool1)
            continue;
          bool2 = Uri.parse("http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home").getHost().equals(paramString);
          paramString = localObject1;
          if (!bool2)
            break label110;
          localObject2 = ((Uri)localObject2).getPath();
          if (("/".equals(localObject2)) || (((String)localObject2).length() == 0))
            break label107;
          paramString = localObject1;
          if (!((String)localObject2).startsWith("/receipt/myreceipt/"))
            break label110;
          return "my";
        }
      }
      catch (Exception paramString)
      {
        return null;
      }
      label102: boolean bool1 = true;
      continue;
      label107: paramString = "home";
      label110: return paramString;
      label112: bool1 = false;
    }
  }

  private boolean isUrlValid(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return false;
    while ((!paramString.startsWith("http")) && (!paramString.startsWith("https")));
    return true;
  }

  public int cityId()
  {
    return DPApplication.instance().cityConfig().currentCity().id();
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return new TuanWebFragment.TuanWebViewClient(this, this);
  }

  protected String currentStack()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = getActivity().getIntent().getStringExtra("stack");
    Object localObject = str;
    if (str == null)
      localObject = "";
    localObject = new StringTokenizer((String)localObject, "|");
    while (((StringTokenizer)localObject).hasMoreTokens())
    {
      localStringBuilder.append(((StringTokenizer)localObject).nextToken());
      localStringBuilder.append(" > ");
    }
    localStringBuilder.append(this.tag);
    return (String)localStringBuilder.toString();
  }

  void fillData(String paramString, byte[] paramArrayOfByte)
  {
    this.mLayMask.setVisibility(4);
    String str = "";
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, "utf-8");
      this.webView.loadDataWithBaseURL(paramString, paramArrayOfByte, "text/html", "utf-8", null);
      return;
    }
    catch (Exception paramArrayOfByte)
    {
      while (true)
      {
        setError(paramString, "公告栏编码错误，请稍后再试");
        paramArrayOfByte = str;
      }
    }
  }

  public void gotoLogin()
  {
    DPApplication.instance().accountService().login(this);
  }

  public void handleArgments()
  {
    this.url = getArguments().getString("url");
  }

  protected void loadData()
  {
    if (!isUrlValid(this.url))
    {
      getActivity().finish();
      return;
    }
    if (this.url.indexOf('?') >= 0)
    {
      loadUrl(this.url);
      return;
    }
    if (this.contentManager.containsKey(this.url))
    {
      fillData(this.url, this.contentManager.get(this.url));
      return;
    }
    this.mLayMask.removeAllViews();
    getActivity().getLayoutInflater().inflate(R.layout.loading_item, this.mLayMask, true);
    this.mLayMask.setVisibility(0);
    this.contentManager.get(this.url);
  }

  public void loadUrl(String paramString)
  {
    if (paramString == null);
    while (true)
    {
      return;
      if (paramString.startsWith("http://dianping/paymsg"))
      {
        localObject1 = new Intent();
        ((Intent)localObject1).putExtra("url", paramString);
        getActivity().setResult(-1, (Intent)localObject1);
        getActivity().finish();
        return;
      }
      localObject1 = getSpecialUrlType(paramString);
      if ((localObject1 != null) && (getActivity().getIntent().getStringExtra("stack") != null))
      {
        localObject2 = new Intent();
        ((Intent)localObject2).putExtra("url", paramString);
        ((Intent)localObject2).putExtra("tag", "$");
        ((Intent)localObject2).putExtra("tab", (String)localObject1);
        ((Intent)localObject2).putExtra("stack", currentStack());
        getActivity().setResult(1, (Intent)localObject2);
        getActivity().finish();
        return;
      }
      if (!hasRequestParam(paramString))
        break;
      paramString = processParam(paramString);
      if (paramString == null)
        continue;
      loadUrl(paramString);
      return;
    }
    Object localObject1 = null;
    int i = paramString.lastIndexOf("tag=");
    int k;
    if (i > 0)
    {
      k = i + 4;
      int j = paramString.indexOf("&", k);
      i = j;
      if (j < 0)
        i = paramString.indexOf("#", k);
      if (i > k)
        localObject1 = paramString.substring(k, i);
    }
    else
    {
      if (this.tag != null)
        break label332;
      this.tag = ((String)localObject1);
      this.webView.loadUrl(paramString);
    }
    while (true)
    {
      label248: if (this.textStack == null)
        break label533;
      localObject2 = new StringBuilder();
      localObject1 = getActivity().getIntent().getStringExtra("stack");
      paramString = (String)localObject1;
      if (localObject1 == null)
        paramString = "";
      paramString = new StringTokenizer(paramString, "|");
      while (paramString.hasMoreTokens())
      {
        ((StringBuilder)localObject2).append(paramString.nextToken());
        ((StringBuilder)localObject2).append(" > ");
      }
      localObject1 = paramString.substring(k);
      break;
      label332: if ((localObject1 == null) || (((String)localObject1).length() == 0))
      {
        this.webView.loadUrl(paramString);
        continue;
      }
      if (!this.tag.equals(localObject1))
        break label376;
      this.webView.loadUrl(paramString);
    }
    label376: Object localObject3 = getActivity().getIntent().getStringExtra("stack");
    Object localObject2 = localObject3;
    if (localObject3 == null)
      localObject2 = "";
    localObject3 = new StringTokenizer((String)localObject2, "|");
    while (((StringTokenizer)localObject3).hasMoreTokens())
    {
      if (!((String)localObject1).equals(((StringTokenizer)localObject3).nextToken()))
        continue;
      localObject2 = new Intent();
      ((Intent)localObject2).putExtra("url", paramString);
      ((Intent)localObject2).putExtra("tag", (String)localObject1);
      getActivity().setResult(1, (Intent)localObject2);
      getActivity().finish();
      return;
    }
    localObject1 = new Intent(getActivity(), TuanWebActivity.class);
    ((Intent)localObject1).setAction("com.dianping.action.TUAN");
    ((Intent)localObject1).putExtra("url", paramString);
    if (((String)localObject2).length() == 0);
    for (paramString = this.tag; ; paramString = (String)localObject2 + "|" + this.tag)
    {
      ((Intent)localObject1).putExtra("stack", paramString);
      startActivityForResult((Intent)localObject1, 1);
      break label248;
      label533: break;
    }
    ((StringBuilder)localObject2).append(this.tag);
    this.textStack.setText(((StringBuilder)localObject2).toString());
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
    super.onLoginCancel();
    if (this.pendingEvent == 2)
    {
      this.pendingUrl = null;
      this.pendingEvent = 0;
    }
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if ((paramAccountService.token() != null) && (this.pendingEvent == 2) && (this.pendingUrl != null))
    {
      paramAccountService = this.pendingUrl;
      this.pendingUrl = null;
      loadUrl(paramAccountService);
    }
  }

  public void onResume()
  {
    super.onResume();
    if ((this.url != null) && (this.url.contains("refresh=1")))
      loadData();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.contentManager = AnnounceContentManager.instance(getActivity());
    this.contentManager.setOnRequestEndListener(new TuanWebFragment.1(this));
    if (!isUrlValid(this.url))
    {
      Toast.makeText(getActivity(), "非法url链接", 0).show();
      getActivity().finish();
    }
    do
      return;
    while (this.url.contains("refresh=1"));
    loadData();
  }

  protected String phone()
  {
    try
    {
      String str = Environment.phone();
      if ((str != null) && (str.length() > 10))
        return str;
      return "";
    }
    catch (Exception localException)
    {
    }
    return "";
  }

  protected String processParam(String paramString)
  {
    int i = paramString.indexOf('?');
    if (i < 0)
      return paramString;
    String str2 = paramString.substring(0, i + 1);
    Object localObject2 = paramString.substring(i + 1);
    Object localObject1 = localObject2;
    if (((String)localObject2).contains("agent=!"))
      localObject1 = ((String)localObject2).replace("agent=!", "agent=android");
    localObject2 = localObject1;
    if (((String)localObject1).contains("agent=*"))
      localObject2 = ((String)localObject1).replace("agent=*", "agent=android");
    localObject1 = localObject2;
    if (((String)localObject2).contains("version=!"))
      localObject1 = ((String)localObject2).replace("version=!", "version=" + version());
    localObject2 = localObject1;
    if (((String)localObject1).contains("version=*"))
      localObject2 = ((String)localObject1).replace("version=*", "version=" + version());
    localObject1 = localObject2;
    if (((String)localObject2).contains("screen=!"))
      localObject1 = ((String)localObject2).replace("screen=!", "screen=" + screen());
    localObject2 = localObject1;
    if (((String)localObject1).contains("screen=*"))
      localObject2 = ((String)localObject1).replace("screen=*", "screen=" + screen());
    localObject1 = localObject2;
    if (((String)localObject2).contains("stack=!"))
      localObject1 = ((String)localObject2).replace("stack=!", "stack=" + stack());
    localObject2 = localObject1;
    if (((String)localObject1).contains("stack=*"))
      localObject2 = ((String)localObject1).replace("stack=*", "stack=" + stack());
    localObject1 = localObject2;
    if (((String)localObject2).contains("cityid=!"))
      localObject1 = ((String)localObject2).replace("cityid=!", "cityid=" + cityId());
    localObject2 = localObject1;
    if (((String)localObject1).contains("cityid=*"))
      localObject2 = ((String)localObject1).replace("cityid=*", "cityid=" + cityId());
    localObject1 = localObject2;
    if (((String)localObject2).contains("phone=!"))
      localObject1 = ((String)localObject2).replace("phone=!", "phone=" + phone());
    localObject2 = localObject1;
    if (((String)localObject1).contains("phone=*"))
      localObject2 = ((String)localObject1).replace("phone=*", "phone=" + phone());
    Object localObject3 = localObject2;
    if (((String)localObject2).contains("sessionid=!"))
      localObject3 = ((String)localObject2).replace("sessionid=!", "sessionid=" + sessionId());
    localObject1 = localObject3;
    if (((String)localObject3).contains("sessionid=*"))
      localObject1 = ((String)localObject3).replace("sessionid=*", "sessionid=" + sessionId());
    String str1 = DPApplication.instance().accountService().token();
    localObject3 = localObject1;
    label750: int j;
    if (((String)localObject1).contains("token=*"))
    {
      localObject3 = new StringBuilder().append("token=");
      if (str1 == null)
      {
        localObject2 = "";
        localObject3 = ((String)localObject1).replace("token=*", (String)localObject2);
      }
    }
    else
    {
      localObject2 = localObject3;
      if (((String)localObject3).contains("token=!"))
      {
        if (str1 == null)
          break label997;
        localObject2 = ((String)localObject3).replace("token=!", "token=" + str1);
      }
      localObject1 = localObject2;
      if (((String)localObject2).contains("latitude=*"))
      {
        localObject1 = getCurrentLocation();
        if (localObject1 != null)
          break label1032;
        localObject1 = ((String)localObject2).replace("latitude=*", "latitude=");
      }
      localObject2 = localObject1;
      if (((String)localObject1).contains("longitude=*"))
      {
        localObject2 = getCurrentLocation();
        if (localObject2 != null)
          break label1072;
        localObject2 = ((String)localObject1).replace("longitude=*", "longitude=");
      }
      label782: localObject1 = localObject2;
      if (((String)localObject2).contains("accuracy=*"))
      {
        localObject1 = getCurrentLocation();
        if (localObject1 != null)
          break label1112;
        localObject1 = ((String)localObject2).replace("accuracy=*", "accuracy=");
      }
      label814: j = 0;
      localObject2 = localObject1;
      i = j;
      if (0 == 0)
      {
        localObject2 = localObject1;
        i = j;
        if (((String)localObject1).contains("latitude=!"))
        {
          localObject2 = getCurrentLocation();
          if (localObject2 != null)
            break label1146;
          i = 1;
          localObject2 = localObject1;
        }
      }
      label857: localObject1 = localObject2;
      j = i;
      if (i == 0)
      {
        localObject1 = localObject2;
        j = i;
        if (((String)localObject2).contains("longitude=!"))
        {
          localObject1 = getCurrentLocation();
          if (localObject1 != null)
            break label1190;
          j = 1;
          localObject1 = localObject2;
        }
      }
      label898: localObject2 = localObject1;
      i = j;
      if (j == 0)
      {
        localObject2 = localObject1;
        i = j;
        if (((String)localObject1).contains("accuracy=!"))
        {
          localObject2 = getCurrentLocation();
          if (localObject2 != null)
            break label1234;
          i = 1;
          localObject2 = localObject1;
        }
      }
    }
    while (true)
    {
      if (i == 0)
        break label1272;
      this.pendingUrl = paramString;
      this.pendingEvent = 4;
      if (this.locationService.status() <= 0)
        this.locationService.refresh();
      if (this.locationService.status() != -1);
      return null;
      localObject2 = str1;
      break;
      label997: gotoLogin();
      this.pendingUrl = (str2 + (String)localObject3);
      this.pendingEvent = 2;
      return null;
      label1032: localObject1 = ((String)localObject2).replace("latitude=*", "latitude=" + Location.FMT.format(((Location)localObject1).latitude()));
      break label750;
      label1072: localObject2 = ((String)localObject1).replace("longitude=*", "longitude=" + Location.FMT.format(((Location)localObject2).longitude()));
      break label782;
      label1112: localObject1 = ((String)localObject2).replace("accuracy=*", "accuracy=" + ((Location)localObject1).accuracy());
      break label814;
      label1146: localObject2 = ((String)localObject1).replace("latitude=!", "latitude=" + Location.FMT.format(((Location)localObject2).latitude()));
      i = j;
      break label857;
      label1190: localObject1 = ((String)localObject2).replace("longitude=!", "longitude=" + Location.FMT.format(((Location)localObject1).longitude()));
      j = i;
      break label898;
      label1234: localObject2 = ((String)localObject1).replace("accuracy=!", "accuracy=" + ((Location)localObject2).accuracy());
      i = j;
    }
    label1272: return (String)(String)(String)(str2 + (String)localObject2);
  }

  protected String screen()
  {
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    return localDisplayMetrics.widthPixels + "x" + localDisplayMetrics.heightPixels;
  }

  protected String sessionId()
  {
    return Environment.sessionId();
  }

  void setError(String paramString1, String paramString2)
  {
    this.mLayMask.removeAllViews();
    getActivity().getLayoutInflater().inflate(R.layout.error_item, this.mLayMask, true);
    ((TextView)this.mLayMask.findViewById(16908308)).setText(paramString2);
    this.mLayMask.setVisibility(0);
  }

  protected String stack()
  {
    String str2 = getActivity().getIntent().getStringExtra("stack");
    String str1 = str2;
    if (str2 == null)
      str1 = "";
    return str1;
  }

  protected String utm()
  {
    Intent localIntent = getActivity().getIntent();
    Object localObject2;
    if (localIntent.getData() != null)
    {
      localObject2 = localIntent.getData().getQueryParameter("_utm");
      localObject1 = localObject2;
      if (localObject2 != null);
    }
    for (Object localObject1 = localIntent.getData().getQueryParameter("utm_"); ; localObject1 = getActivity().getIntent().getStringExtra("utm"))
    {
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "";
      return localObject2;
    }
  }

  protected String version()
  {
    return Environment.versionName();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanWebFragment
 * JD-Core Version:    0.6.0
 */