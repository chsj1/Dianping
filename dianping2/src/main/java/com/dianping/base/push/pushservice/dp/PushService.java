package com.dianping.base.push.pushservice.dp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

public abstract interface PushService
{
  public static final String ACTION_KEEPALIVE = "com.dianping.push.KEEP_ALIVE";
  public static final String ACTION_RECONNECT = "com.dianping.push.RECONNECT";
  public static final String ACTION_START = "com.dianping.push.START";
  public static final String ACTION_STOP = "com.dianping.push.STOP";

  public abstract void onCreate(Service paramService);

  public abstract void onDestroy(Service paramService);

  public abstract int onStartCommand(Service paramService, Intent paramIntent, int paramInt1, int paramInt2);

  public abstract void startService(Context paramContext);

  public abstract void stopService(Context paramContext);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.dp.PushService
 * JD-Core Version:    0.6.0
 */