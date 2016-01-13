package com.dianping.base.push.pushservice.dp;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class FakeService extends Service
{
  public static void start(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, FakeService.class);
    localIntent.setAction("start");
    paramContext.startService(localIntent);
  }

  public static void stop(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, FakeService.class);
    localIntent.setAction("stop");
    paramContext.startService(localIntent);
  }

  public IBinder onBind(Intent paramIntent)
  {
    throw new UnsupportedOperationException("fake service can't support bind operation.");
  }

  public void onCreate()
  {
    super.onCreate();
    startForeground(2147483647, new Notification());
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if ((paramIntent != null) && ("stop".equals(paramIntent.getAction())))
      stopSelf();
    return super.onStartCommand(paramIntent, paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.dp.FakeService
 * JD-Core Version:    0.6.0
 */