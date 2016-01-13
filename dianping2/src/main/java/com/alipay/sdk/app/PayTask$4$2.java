package com.alipay.sdk.app;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class PayTask$4$2
  implements DialogInterface.OnClickListener
{
  public void onClick(DialogInterface arg1, int paramInt)
  {
    synchronized (PayTask.a)
    {
      Result.a(Result.b());
    }
    try
    {
      PayTask.a.notify();
      label18: monitorexit;
      return;
      localObject = finally;
      monitorexit;
      throw localObject;
    }
    catch (Exception localException)
    {
      break label18;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.PayTask.4.2
 * JD-Core Version:    0.6.0
 */