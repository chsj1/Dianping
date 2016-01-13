package com.alipay.sdk.auth;

import android.webkit.WebView;

class AuthActivity$10
  implements Runnable
{
  public void run()
  {
    try
    {
      AuthActivity.d(this.b).loadUrl("javascript:" + this.a);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.auth.AuthActivity.10
 * JD-Core Version:    0.6.0
 */