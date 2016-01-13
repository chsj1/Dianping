package com.dianping.base.sso;

import android.app.Activity;
import android.content.Intent;

public abstract interface IThirdSSOLogin
{
  public static final int FEED_ALIPAY = 16;
  public static final int FEED_QZONE = 32;
  public static final int FEED_SINA_WEIBO = 1;
  public static final int FEED_WEIXIN = 64;
  public static final int FEED_XIAOMI = 128;

  public abstract void callback(int paramInt1, int paramInt2, Intent paramIntent);

  public abstract void ssoLogin(String paramString, Activity paramActivity, ILogininListener paramILogininListener);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.sso.IThirdSSOLogin
 * JD-Core Version:    0.6.0
 */