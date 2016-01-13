package com.alipay.sdk.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class PayTask$6$2
  implements DialogInterface.OnClickListener
{
  public void onClick(DialogInterface arg1, int paramInt)
  {
    PayTask.a(this.a.a).unregisterReceiver(PayTask.c(this.a.a));
    PayTask.a(this.a.a, false);
    Result.a(Result.b());
    try
    {
      synchronized (PayTask.a)
      {
        PayTask.a.notify();
        label53: return;
      }
    }
    catch (Exception localException)
    {
      break label53;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.PayTask.6.2
 * JD-Core Version:    0.6.0
 */