package com.dianping.base.push.pushservice.dp;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.dianping.util.Log;

public class DPPushService extends Service
{
  public static final String TAG = "DPPushService";
  private static boolean isStarted = false;
  private PushService pushService = new PushServiceProxy(this);

  public static boolean isStarted()
  {
    return isStarted;
  }

  public static void reconnect(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, DPPushService.class);
    localIntent.setAction("com.dianping.push.RECONNECT");
    paramContext.startService(localIntent);
  }

  public static void start(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent();
    localIntent.setClass(paramContext, DPPushService.class);
    localIntent.setAction("com.dianping.push.START");
    localIntent.putExtra("source", paramString);
    paramContext.startService(localIntent);
    isStarted = true;
  }

  public static void stop(Context paramContext)
  {
    paramContext.stopService(new Intent(paramContext, DPPushService.class));
    isStarted = false;
  }

  public IBinder onBind(Intent paramIntent)
  {
    throw new UnsupportedOperationException("push service can't support bind operation.");
  }

  public void onCreate()
  {
    super.onCreate();
    Log.i("DPPushService", "DPPushService created");
    FakeService.start(this);
    startForeground(2147483647, new Notification());
    FakeService.stop(this);
    this.pushService.onCreate(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    Log.i("DPPushService", "DPPushService destroyed");
    this.pushService.onDestroy(this);
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("DPPushService onStartCommand with action:");
    if (paramIntent == null);
    for (String str = ""; ; str = paramIntent.getAction())
    {
      Log.i("DPPushService", str);
      return this.pushService.onStartCommand(this, paramIntent, paramInt1, paramInt2);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.dp.DPPushService
 * JD-Core Version:    0.6.0
 */