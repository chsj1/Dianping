package com.dianping.base.share.business;

import com.dianping.app.DPApplication;
import com.dianping.base.sso.QQSSOLogin;

public class ThirdLoginUrl
{
  public static String getThirdLoginUrl(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "http://m.dianping.com/auth/app?ft=2&source=1&redir=&sso=true";
    case 16:
      return "http://m.dianping.com/auth/app?ft=5&redir=";
    case 32:
      StringBuilder localStringBuilder = new StringBuilder("http://m.dianping.com/auth/app?ft=6&redir=");
      if (QQSSOLogin.isSupportQQ(DPApplication.instance()))
        localStringBuilder.append("&sso=true");
      return localStringBuilder.toString();
    case 1:
      return "http://m.dianping.com/auth/app?ft=2&source=1&redir=&sso=true";
    case 64:
      return "http://m.dianping.com/auth/app?ft=15&sso=true";
    case 128:
    }
    return "http://m.dianping.com/auth/app?ft=17&redir=";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.business.ThirdLoginUrl
 * JD-Core Version:    0.6.0
 */