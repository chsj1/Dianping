package com.alipay.sdk.widget;

import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;

class Loading$1
  implements Runnable
{
  public void run()
  {
    if (Loading.a(this.d) == null)
      Loading.a(this.d, new ProgressDialog(Loading.b(this.d)));
    Loading.a(this.d).setCancelable(this.a);
    Loading.a(this.d).setOnCancelListener(this.b);
    Loading.a(this.d).setMessage(this.c);
    try
    {
      Loading.a(this.d).show();
      return;
    }
    catch (Exception localException)
    {
      Loading.a(this.d, null);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.widget.Loading.1
 * JD-Core Version:    0.6.0
 */