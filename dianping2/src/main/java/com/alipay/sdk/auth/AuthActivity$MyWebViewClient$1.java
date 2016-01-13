package com.alipay.sdk.auth;

import android.webkit.SslErrorHandler;
import com.alipay.sdk.widget.SystemDefaultDialog;

class AuthActivity$MyWebViewClient$1
  implements Runnable
{
  public void run()
  {
    SystemDefaultDialog.a(this.b.a, "安全警告", "由于您的设备缺少根证书，将无法校验该访问站点的安全性，可能存在风险，请选择是否继续？", "继续", new AuthActivity.MyWebViewClient.1.1(this), "退出", new AuthActivity.MyWebViewClient.1.2(this));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.auth.AuthActivity.MyWebViewClient.1
 * JD-Core Version:    0.6.0
 */