package com.alipay.sdk.app;

import android.webkit.WebView;

class H5AuthActivity$10
  implements Runnable
{
  public void run()
  {
    try
    {
      H5AuthActivity.d(this.b).loadUrl("javascript:" + this.a);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.H5AuthActivity.10
 * JD-Core Version:    0.6.0
 */