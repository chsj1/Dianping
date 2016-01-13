package com.alipay.sdk.app;

import android.app.Activity;
import android.app.Dialog;

class PayTask$5$1
  implements Runnable
{
  public void run()
  {
    if (PayTask.h(this.a.a) != null)
      PayTask.h(this.a.a).dismiss();
    PayTask.a(this.a.a, true);
    PayTask.a(this.a.a).unregisterReceiver(PayTask.c(this.a.a));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.PayTask.5.1
 * JD-Core Version:    0.6.0
 */