package com.tencent.wns.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import com.tencent.base.Global;
import com.tencent.base.os.clock.a;
import com.tencent.base.os.clock.e;
import java.util.ArrayList;
import java.util.List;

public class b
{
  public static final a a = new a("wns.heartbeat", 180000L, new c());
  private static final com.tencent.base.os.clock.d b = new d();
  private static volatile long c = System.currentTimeMillis();
  private static volatile long d = 180000L;
  private static e e;
  private static List f = new ArrayList();

  public static void a()
  {
    b();
    if (!com.tencent.base.os.clock.b.a(a))
    {
      cloudwns.l.c.autoTrace(1, "WnsAlarm", "alarmManager failed use SimpleClock ", null);
      e = e.a(30000L, 30000L, b);
    }
    cloudwns.l.c.autoTrace(4, "WnsAlarm", "Heartbeat Alarm Enabled :)", null);
  }

  public static void a(long paramLong)
  {
    monitorenter;
    try
    {
      d = paramLong;
      monitorexit;
      a.a(paramLong);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static void a(a parama)
  {
    synchronized (f)
    {
      f.add(parama);
      return;
    }
  }

  public static void b()
  {
    ((AlarmManager)Global.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(Global.getContext(), 0, new Intent(a.c()), 134217728));
    a.a();
    e.a(e);
  }

  private static void b(String paramString)
  {
    cloudwns.l.c.autoTrace(1, "WnsAlarm", "Alarm Notify From " + paramString, null);
    monitorenter;
    try
    {
      if (System.currentTimeMillis() - c <= d - 30000L)
      {
        cloudwns.l.c.autoTrace(2, "WnsAlarm", "Alarm Denied From " + paramString, null);
        return;
      }
      monitorexit;
      c = System.currentTimeMillis();
      WnsGlobal.c();
      c();
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  private static void c()
  {
    synchronized (f)
    {
      Object[] arrayOfObject = f.toArray();
      int j = arrayOfObject.length;
      int i = 0;
      if (i < j)
      {
        ((a)arrayOfObject[i]).g();
        i += 1;
      }
    }
  }

  public static abstract interface a
  {
    public abstract void g();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.service.b
 * JD-Core Version:    0.6.0
 */