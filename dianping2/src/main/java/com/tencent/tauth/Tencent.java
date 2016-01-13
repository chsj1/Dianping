package com.tencent.tauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.BaseApi;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.SocialApi;
import com.tencent.open.utils.HttpUtils;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.open.utils.SystemUtils;
import com.tencent.open.utils.TemporaryStorage;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class Tencent
{
  private static Tencent sInstance;
  private QQAuth mQQAuth;

  private Tencent(String paramString, Context paramContext)
  {
    com.tencent.a.b.c.a(paramContext.getApplicationContext());
    this.mQQAuth = QQAuth.createInstance(paramString, paramContext);
  }

  // ERROR //
  private static boolean checkManifestConfig(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: new 40	android/content/ComponentName
    //   3: dup
    //   4: aload_0
    //   5: invokevirtual 44	android/content/Context:getPackageName	()Ljava/lang/String;
    //   8: ldc 46
    //   10: invokespecial 49	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   13: astore_2
    //   14: aload_0
    //   15: invokevirtual 53	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   18: aload_2
    //   19: iconst_0
    //   20: invokevirtual 59	android/content/pm/PackageManager:getActivityInfo	(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;
    //   23: pop
    //   24: new 40	android/content/ComponentName
    //   27: dup
    //   28: aload_0
    //   29: invokevirtual 44	android/content/Context:getPackageName	()Ljava/lang/String;
    //   32: ldc 61
    //   34: invokespecial 49	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   37: astore_1
    //   38: aload_0
    //   39: invokevirtual 53	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   42: aload_1
    //   43: iconst_0
    //   44: invokevirtual 59	android/content/pm/PackageManager:getActivityInfo	(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;
    //   47: pop
    //   48: iconst_1
    //   49: ireturn
    //   50: astore_0
    //   51: new 63	java/lang/StringBuilder
    //   54: dup
    //   55: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   58: ldc 66
    //   60: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: aload_1
    //   64: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: ldc 72
    //   69: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: invokevirtual 75	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: astore_0
    //   76: new 63	java/lang/StringBuilder
    //   79: dup
    //   80: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   83: aload_0
    //   84: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: ldc 77
    //   89: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: aload_1
    //   93: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: ldc 79
    //   98: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc 81
    //   103: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: ldc 83
    //   108: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: invokevirtual 75	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: astore_0
    //   115: invokestatic 88	com/tencent/a/a/c:a	()Lcom/tencent/a/a/c;
    //   118: pop
    //   119: ldc 90
    //   121: aload_0
    //   122: invokestatic 93	com/tencent/a/a/c:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   125: iconst_0
    //   126: ireturn
    //   127: astore_0
    //   128: new 63	java/lang/StringBuilder
    //   131: dup
    //   132: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   135: ldc 95
    //   137: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc 97
    //   142: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: invokevirtual 75	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: astore_0
    //   149: invokestatic 88	com/tencent/a/a/c:a	()Lcom/tencent/a/a/c;
    //   152: pop
    //   153: ldc 99
    //   155: aload_0
    //   156: invokestatic 93	com/tencent/a/a/c:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   159: iconst_0
    //   160: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	24	50	android/content/pm/PackageManager$NameNotFoundException
    //   24	48	127	android/content/pm/PackageManager$NameNotFoundException
  }

  public static Tencent createInstance(String paramString, Context paramContext)
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (sInstance != null)
          continue;
        sInstance = new Tencent(paramString, paramContext);
        boolean bool = checkManifestConfig(paramContext, paramString);
        if (!bool)
        {
          paramString = null;
          return paramString;
          if (paramString.equals(sInstance.getAppId()))
            continue;
          sInstance.logout(paramContext);
          sInstance = new Tencent(paramString, paramContext);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      com.tencent.a.a.c.a("openSDK_LOG", "createInstance()  --end");
      paramString = sInstance;
    }
  }

  public int ask(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new SocialApi(this.mQQAuth.getQQToken()).ask(paramActivity, paramBundle, paramIUiListener);
    return 0;
  }

  public String getAccessToken()
  {
    return this.mQQAuth.getQQToken().getAccessToken();
  }

  public String getAppId()
  {
    return this.mQQAuth.getQQToken().getAppId();
  }

  public long getExpiresIn()
  {
    return this.mQQAuth.getQQToken().getExpireTimeInSecond();
  }

  public String getOpenId()
  {
    return this.mQQAuth.getQQToken().getOpenId();
  }

  public QQToken getQQToken()
  {
    return this.mQQAuth.getQQToken();
  }

  public int gift(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new SocialApi(this.mQQAuth.getQQToken()).gift(paramActivity, paramBundle, paramIUiListener);
    return 0;
  }

  public void handleLoginData(Intent paramIntent, IUiListener paramIUiListener)
  {
    BaseApi.handleDataToListener(paramIntent, paramIUiListener);
  }

  public int invite(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new SocialApi(this.mQQAuth.getQQToken()).invite(paramActivity, paramBundle, paramIUiListener);
    return 0;
  }

  public boolean isReady()
  {
    return (isSessionValid()) && (getOpenId() != null);
  }

  public boolean isSessionValid()
  {
    return this.mQQAuth.isSessionValid();
  }

  public boolean isSupportSSOLogin(Activity paramActivity)
  {
    if (SystemUtils.getAppVersionName(paramActivity, "com.tencent.mobileqq") == null)
    {
      Toast.makeText(paramActivity, "没有安装手Q", 0).show();
      return false;
    }
    if (SystemUtils.checkMobileQQ(paramActivity))
    {
      Toast.makeText(paramActivity, "已安装的手Q版本支持SSO登陆", 0).show();
      return true;
    }
    Toast.makeText(paramActivity, "已安装的手Q版本不支持SSO登陆", 0).show();
    return false;
  }

  public int login(Activity paramActivity, String paramString, IUiListener paramIUiListener)
  {
    return this.mQQAuth.login(paramActivity, paramString, paramIUiListener);
  }

  public int login(Fragment paramFragment, String paramString, IUiListener paramIUiListener)
  {
    return this.mQQAuth.login(paramFragment, paramString, paramIUiListener, "");
  }

  public int loginWithOEM(Activity paramActivity, String paramString1, IUiListener paramIUiListener, String paramString2, String paramString3, String paramString4)
  {
    return this.mQQAuth.loginWithOEM(paramActivity, paramString1, paramIUiListener, paramString2, paramString3, paramString4);
  }

  public void logout(Context paramContext)
  {
    this.mQQAuth.getQQToken().setAccessToken(null, "0");
    this.mQQAuth.getQQToken().setOpenId(null);
  }

  public boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    return false;
  }

  public int reAuth(Activity paramActivity, String paramString, IUiListener paramIUiListener)
  {
    return this.mQQAuth.reAuth(paramActivity, paramString, paramIUiListener);
  }

  public void releaseResource()
  {
    TemporaryStorage.remove("shareToQQ");
    TemporaryStorage.remove("shareToQzone");
  }

  public JSONObject request(String paramString1, Bundle paramBundle, String paramString2)
    throws IOException, JSONException, HttpUtils.NetworkUnavailableException, HttpUtils.HttpStatusException
  {
    return HttpUtils.request(this.mQQAuth.getQQToken(), com.tencent.a.b.c.a(), paramString1, paramBundle, paramString2);
  }

  public void requestAsync(String paramString1, Bundle paramBundle, String paramString2, IRequestListener paramIRequestListener, Object paramObject)
  {
    HttpUtils.requestAsync(this.mQQAuth.getQQToken(), com.tencent.a.b.c.a(), paramString1, paramBundle, paramString2, paramIRequestListener);
  }

  public void setAccessToken(String paramString1, String paramString2)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "setAccessToken(), expiresIn = " + paramString2 + "");
    this.mQQAuth.setAccessToken(paramString1, paramString2);
  }

  public void setOpenId(String paramString)
  {
    com.tencent.a.a.c.a("openSDK_LOG", "setOpenId() --start");
    this.mQQAuth.setOpenId(com.tencent.a.b.c.a(), paramString);
    com.tencent.a.a.c.a("openSDK_LOG", "setOpenId() --end");
  }

  public void shareToQQ(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new QQShare(paramActivity, this.mQQAuth.getQQToken()).shareToQQ(paramActivity, paramBundle, paramIUiListener);
  }

  public void shareToQzone(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new QzoneShare(paramActivity, this.mQQAuth.getQQToken()).shareToQzone(paramActivity, paramBundle, paramIUiListener);
  }

  public int story(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    new SocialApi(this.mQQAuth.getQQToken()).story(paramActivity, paramBundle, paramIUiListener);
    return 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tauth.Tencent
 * JD-Core Version:    0.6.0
 */