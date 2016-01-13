package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.webkit.SslErrorHandler;

class H5AuthActivity$MyWebViewClient$1$1
  implements DialogInterface.OnClickListener
{
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    H5AuthActivity.a(this.a.b.a, true);
    this.a.a.proceed();
    paramDialogInterface.dismiss();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.H5AuthActivity.MyWebViewClient.1.1
 * JD-Core Version:    0.6.0
 */