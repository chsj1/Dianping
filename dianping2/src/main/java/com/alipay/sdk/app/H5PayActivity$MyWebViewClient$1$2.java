package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.webkit.SslErrorHandler;

class H5PayActivity$MyWebViewClient$1$2
  implements DialogInterface.OnClickListener
{
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    this.a.a.cancel();
    H5PayActivity.a(this.a.b.a, false);
    Result.a(Result.b());
    this.a.b.a.finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.H5PayActivity.MyWebViewClient.1.2
 * JD-Core Version:    0.6.0
 */