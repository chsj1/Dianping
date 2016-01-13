package com.dianping.base.share.business;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.Uri.Builder;

public class QQShareEngine
  implements IShareEngine
{
  public static boolean isSupportQQ(Context paramContext)
  {
    try
    {
      paramContext.getPackageManager().getPackageInfo("com.tencent.mobileqq", 0);
      return true;
    }
    catch (java.lang.Exception paramContext)
    {
    }
    return false;
  }

  public String getPlatform()
  {
    return "qzone";
  }

  public String getSnsStatusName()
  {
    return "qzone";
  }

  public String getSsoLoginUrl()
  {
    Uri.Builder localBuilder = Uri.parse("dianping://qzonessologin").buildUpon();
    localBuilder.appendQueryParameter("app_key", "200002");
    localBuilder.appendQueryParameter("scope", "get_user_info,add_share");
    localBuilder.appendQueryParameter("ssourl", "http://m.dianping.com/loginunion/app?ft=6&redir=&");
    return localBuilder.build().toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.business.QQShareEngine
 * JD-Core Version:    0.6.0
 */