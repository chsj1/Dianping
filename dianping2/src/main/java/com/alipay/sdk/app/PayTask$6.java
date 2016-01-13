package com.alipay.sdk.app;

import com.alipay.sdk.util.Utils;
import com.alipay.sdk.widget.SystemDefaultDialog;

class PayTask$6
  implements Runnable
{
  public void run()
  {
    if (Utils.b(PayTask.a(this.a), PayTask.i(this.a)))
    {
      Utils.a(PayTask.a(this.a), PayTask.i(this.a));
      PayTask.a(this.a, SystemDefaultDialog.a(PayTask.a(this.a), "提示", "是否取消安装？", "重新安装", new PayTask.6.1(this), "取消", new PayTask.6.2(this)));
      return;
    }
    synchronized (PayTask.a)
    {
      Result.a(Result.c());
    }
    try
    {
      PayTask.a.notify();
      label98: monitorexit;
      return;
      localObject2 = finally;
      monitorexit;
      throw localObject2;
    }
    catch (Exception localException)
    {
      break label98;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.PayTask.6
 * JD-Core Version:    0.6.0
 */