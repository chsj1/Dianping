package com.dianping.base.share.business;

import android.net.Uri;
import android.net.Uri.Builder;

public class SinaShareEngine
  implements IShareEngine
{
  public String getPlatform()
  {
    return "sina";
  }

  public String getSnsStatusName()
  {
    return "Sina";
  }

  public String getSsoLoginUrl()
  {
    Uri.Builder localBuilder = Uri.parse("dianping://sinassologin").buildUpon();
    localBuilder.appendQueryParameter("app_key", "844890293");
    localBuilder.appendQueryParameter("weboauthurl", "http://m.dianping.com/login/ssoback");
    localBuilder.appendQueryParameter("type", String.valueOf(2));
    return localBuilder.build().toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.business.SinaShareEngine
 * JD-Core Version:    0.6.0
 */