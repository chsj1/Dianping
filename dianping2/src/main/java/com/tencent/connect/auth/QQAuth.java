package com.tencent.connect.auth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.tencent.connect.a.a;
import com.tencent.open.utils.ApkExternalInfoTool;
import com.tencent.tauth.IUiListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class QQAuth
{
  private AuthAgent a;
  private QQToken b;

  private QQAuth(String paramString, Context paramContext)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "new Tencent() --start");
    this.b = new QQToken(paramString);
    this.a = new AuthAgent(this.b);
    a.c(paramContext, this.b);
    com.tencent.a.a.c.a("openSDK_LOG", "new Tencent() --end");
  }

  private int a(Activity paramActivity, Fragment paramFragment, String paramString1, IUiListener paramIUiListener, String paramString2)
  {
    paramString2 = paramActivity.getApplicationContext().getPackageName();
    Iterator localIterator = paramActivity.getPackageManager().getInstalledApplications(128).iterator();
    ApplicationInfo localApplicationInfo;
    do
    {
      if (!localIterator.hasNext())
        break;
      localApplicationInfo = (ApplicationInfo)localIterator.next();
    }
    while (!paramString2.equals(localApplicationInfo.packageName));
    for (paramString2 = localApplicationInfo.sourceDir; ; paramString2 = null)
    {
      if (paramString2 != null)
        try
        {
          paramString2 = ApkExternalInfoTool.readChannelId(new File(paramString2));
          if (!TextUtils.isEmpty(paramString2))
          {
            com.tencent.a.a.c.b("openSDK_LOG", "-->login channelId: " + paramString2);
            int i = loginWithOEM(paramActivity, paramString1, paramIUiListener, paramString2, paramString2, "");
            return i;
          }
        }
        catch (IOException paramString2)
        {
          com.tencent.a.a.c.b("openSDK_LOG", "-->login get channel id exception." + paramString2.getMessage());
          paramString2.printStackTrace();
        }
      com.tencent.a.a.c.b("openSDK_LOG", "-->login channelId is null ");
      com.tencent.connect.common.BaseApi.isOEM = false;
      return this.a.doLogin(paramActivity, paramString1, paramIUiListener, false, false, paramFragment);
    }
  }

  public static QQAuth createInstance(String paramString, Context paramContext)
  {
    com.tencent.a.b.c.a(paramContext.getApplicationContext());
    com.tencent.a.a.c.a("openSDK_LOG", "createInstance() --start");
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      localPackageManager.getActivityInfo(new ComponentName(paramContext.getPackageName(), "com.tencent.tauth.AuthActivity"), 0);
      localPackageManager.getActivityInfo(new ComponentName(paramContext.getPackageName(), "com.tencent.connect.common.AssistActivity"), 0);
      paramString = new QQAuth(paramString, paramContext);
      com.tencent.a.a.c.a("openSDK_LOG", "createInstance()  --end");
      return paramString;
    }
    catch (android.content.pm.PackageManager.NameNotFoundException paramString)
    {
      com.tencent.a.a.c.a("openSDK_LOG", "createInstance() error --end", paramString);
      Toast.makeText(paramContext.getApplicationContext(), "请参照文档在Androidmanifest.xml加上AuthActivity和AssitActivity的定义 ", 1).show();
    }
    return null;
  }

  public QQToken getQQToken()
  {
    return this.b;
  }

  public boolean isSessionValid()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("isSessionValid(), result = ");
    if (this.b.isSessionValid());
    for (String str = "true"; ; str = "false")
    {
      com.tencent.a.a.c.a("openSDK_LOG", str + "");
      return this.b.isSessionValid();
    }
  }

  public int login(Activity paramActivity, String paramString, IUiListener paramIUiListener)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "login()");
    return login(paramActivity, paramString, paramIUiListener, "");
  }

  public int login(Activity paramActivity, String paramString1, IUiListener paramIUiListener, String paramString2)
  {
    com.tencent.a.a.c.b("openSDK_LOG", "-->login activity: " + paramActivity);
    return a(paramActivity, null, paramString1, paramIUiListener, paramString2);
  }

  public int login(Fragment paramFragment, String paramString1, IUiListener paramIUiListener, String paramString2)
  {
    FragmentActivity localFragmentActivity = paramFragment.getActivity();
    com.tencent.a.a.c.b("openSDK_LOG", "-->login activity: " + localFragmentActivity);
    return a(localFragmentActivity, paramFragment, paramString1, paramIUiListener, paramString2);
  }

  public int loginWithOEM(Activity paramActivity, String paramString1, IUiListener paramIUiListener, String paramString2, String paramString3, String paramString4)
  {
    com.tencent.a.a.c.b("openSDK_LOG", "loginWithOEM");
    com.tencent.connect.common.BaseApi.isOEM = true;
    String str = paramString2;
    if (paramString2.equals(""))
      str = "null";
    paramString2 = paramString3;
    if (paramString3.equals(""))
      paramString2 = "null";
    paramString3 = paramString4;
    if (paramString4.equals(""))
      paramString3 = "null";
    com.tencent.connect.common.BaseApi.installChannel = paramString2;
    com.tencent.connect.common.BaseApi.registerChannel = str;
    com.tencent.connect.common.BaseApi.businessId = paramString3;
    return this.a.doLogin(paramActivity, paramString1, paramIUiListener);
  }

  public void logout(Context paramContext)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "logout() --start");
    CookieSyncManager.createInstance(paramContext);
    setAccessToken(null, null);
    setOpenId(paramContext, null);
    com.tencent.a.a.c.a("openSDK_LOG", "logout() --end");
  }

  public boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    com.tencent.a.a.c.c("openSDK_LOG", "onActivityResult() ,resultCode = " + paramInt2 + "");
    return true;
  }

  public int reAuth(Activity paramActivity, String paramString, IUiListener paramIUiListener)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "reAuth()");
    return this.a.doLogin(paramActivity, paramString, paramIUiListener, true, true, null);
  }

  public void setAccessToken(String paramString1, String paramString2)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "setAccessToken(), validTimeInSecond = " + paramString2 + "");
    this.b.setAccessToken(paramString1, paramString2);
  }

  public void setOpenId(Context paramContext, String paramString)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "setOpenId() --start");
    this.b.setOpenId(paramString);
    a.d(paramContext, this.b);
    com.tencent.a.a.c.a("openSDK_LOG", "setOpenId() --end");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.connect.auth.QQAuth
 * JD-Core Version:    0.6.0
 */