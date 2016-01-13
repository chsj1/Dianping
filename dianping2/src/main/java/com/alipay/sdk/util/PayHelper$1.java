package com.alipay.sdk.util;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.alipay.android.app.IAlixPay.Stub;

class PayHelper$1
  implements ServiceConnection
{
  public void onServiceConnected(ComponentName arg1, IBinder paramIBinder)
  {
    synchronized (PayHelper.a(this.a))
    {
      PayHelper.a(this.a, IAlixPay.Stub.asInterface(paramIBinder));
      PayHelper.a(this.a).notify();
      return;
    }
  }

  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    PayHelper.a(this.a, null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.util.PayHelper.1
 * JD-Core Version:    0.6.0
 */