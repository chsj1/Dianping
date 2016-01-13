package com.dianping.base.sso;

public class ThirdSSOLoginFactory
{
  public static IThirdSSOLogin ssoLogin(String paramString)
  {
    if (paramString.startsWith("dianping://sinassologin"))
      return new SinaSSOLogin();
    if (paramString.startsWith("dianping://wxssologin"))
      return new WXSSOLogin();
    if (paramString.startsWith("dianping://qzonessologin"))
      return new QQSSOLogin();
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.sso.ThirdSSOLoginFactory
 * JD-Core Version:    0.6.0
 */