package com.tencent.wns.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import cloudwns.b.i;
import cloudwns.s.l;
import com.tencent.base.Global;
import com.tencent.wns.data.h;

public class WnsMain extends Service
{
  private void a()
  {
    cloudwns.i.e.a((byte)Global.getSharedPreferencesForWns().getInt("whichDns", 0));
  }

  public IBinder onBind(Intent paramIntent)
  {
    cloudwns.l.a.d("WnsMain", "Wns Service Binded");
    return e.a;
  }

  public void onCreate()
  {
    super.onCreate();
    long l1 = System.currentTimeMillis();
    a();
    Thread.setDefaultUncaughtExceptionHandler(new h());
    WnsGlobal.a = SystemClock.elapsedRealtime();
    cloudwns.l.a.d("WnsMain", "Wns Service Created");
    b.a();
    l.a();
    cloudwns.f.a.a();
    cloudwns.l.a.d("WnsMain", "Wns Service Create Binder = " + e.a.toString());
    long l2 = System.currentTimeMillis();
    i.a("WnsMain onCreate  cost=" + (l2 - l1));
  }

  public void onDestroy()
  {
    cloudwns.l.a.d("WnsMain", "Wns Service Destroyed");
    super.onDestroy();
  }

  public void onRebind(Intent paramIntent)
  {
    onBind(paramIntent);
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    int i = -1;
    cloudwns.l.a.d("WnsMain", "Wns Service Started");
    if (paramIntent != null)
      i = paramIntent.getIntExtra("onStartCommandReturn", -1);
    cloudwns.l.a.d("WnsMain", "Wns Service Started ,and onStartCommandReturn=" + i);
    if (i <= 0)
      return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    return 1;
  }

  public void onTaskRemoved(Intent paramIntent)
  {
    cloudwns.l.a.d("WnsMain", "wns service removed, call stopself now");
  }

  public boolean onUnbind(Intent paramIntent)
  {
    cloudwns.l.a.d("WnsMain", "Wns Service UnBinded");
    WnsGlobal.a(true);
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.service.WnsMain
 * JD-Core Version:    0.6.0
 */