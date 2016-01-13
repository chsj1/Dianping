package com.tencent.base.os.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import cloudwns.l.c;
import com.tencent.base.Global;
import java.util.HashMap;

public class b
{
  private static HashMap a = new HashMap();

  public static a a(String paramString)
  {
    return (a)a.get(paramString);
  }

  public static boolean a(a parama)
  {
    try
    {
      AlarmManager localAlarmManager = (AlarmManager)Global.getSystemService("alarm");
      long l1 = SystemClock.elapsedRealtime();
      long l2 = parama.d();
      Object localObject = new Intent(parama.c());
      localObject = PendingIntent.getBroadcast(Global.getContext(), 0, (Intent)localObject, 134217728);
      parama.a((PendingIntent)localObject);
      localAlarmManager.set(2, l1 + l2, (PendingIntent)localObject);
      monitorenter;
      try
      {
        a.put(parama.c(), parama);
        return true;
      }
      finally
      {
        monitorexit;
      }
    }
    catch (java.lang.Exception parama)
    {
      c.autoTrace(4, "AlarmClockService", "set alarmManager failed", parama);
    }
    return false;
  }

  public static void b(a parama)
  {
    AlarmManager localAlarmManager = (AlarmManager)Global.getSystemService("alarm");
    if (parama.b() != null)
    {
      localAlarmManager.cancel(parama.b());
      parama.a(null);
    }
    monitorenter;
    try
    {
      a.remove(Integer.valueOf(parama.e()));
      return;
    }
    finally
    {
      monitorexit;
    }
    throw parama;
  }

  public static void c(a parama)
  {
    monitorenter;
    try
    {
      parama.a(null);
      a.remove(parama.c());
      return;
    }
    finally
    {
      monitorexit;
    }
    throw parama;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.base.os.clock.b
 * JD-Core Version:    0.6.0
 */