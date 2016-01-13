package com.tencent.wns.service;

import android.os.SystemClock;
import com.tencent.base.os.clock.d;
import com.tencent.base.os.clock.e;
import com.tencent.wns.data.Client;
import java.util.ArrayList;
import java.util.List;

public final class WnsGlobal
{
  public static long a;
  private static Client b = new Client();
  private static volatile long c;
  private static long d;
  private static WnsGlobal.a e;
  private static final List f;
  private static final d g;
  private static String[] h;

  static
  {
    a = SystemClock.elapsedRealtime();
    c = 0L;
    d = 900000L;
    e = WnsGlobal.a.b;
    f = new ArrayList();
    g = new l();
    e.a(10000L, 10000L, g);
    c = SystemClock.elapsedRealtime();
    h = new String[0];
  }

  public static final Client a()
  {
    return b;
  }

  public static final void a(Client paramClient)
  {
    b = paramClient;
  }

  public static void a(WnsGlobal.b paramb)
  {
    synchronized (f)
    {
      f.add(paramb);
      return;
    }
  }

  public static void a(String paramString)
  {
    try
    {
      h = paramString.split(";");
      cloudwns.f.a.a().a(new m());
      return;
    }
    catch (java.lang.Exception paramString)
    {
      while (true)
        h = null;
    }
  }

  public static void a(boolean paramBoolean)
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (g() == paramBoolean)
          continue;
        if (paramBoolean)
        {
          l = SystemClock.elapsedRealtime();
          c = l;
          c();
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      long l = 0L;
    }
  }

  public static final long b()
  {
    return SystemClock.elapsedRealtime() - a;
  }

  public static boolean b(String paramString)
  {
    if (h == null);
    while (true)
    {
      return false;
      String[] arrayOfString = h;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        if (arrayOfString[i].equals(paramString))
          return true;
        i += 1;
      }
    }
  }

  public static void c()
  {
    monitorenter;
    try
    {
      WnsGlobal.a locala = h();
      int i;
      if (locala.equals(WnsGlobal.a.a))
        i = 0;
      while (true)
      {
        cloudwns.f.a.a().d(i);
        if (locala == e)
          break;
        cloudwns.l.a.d("WnsMain", "Runtime State Changed from " + e + " → " + locala);
        synchronized (f)
        {
          Object[] arrayOfObject = f.toArray();
          int j = arrayOfObject.length;
          i = 0;
          while (true)
            if (i < j)
            {
              ((WnsGlobal.b)arrayOfObject[i]).a(e, locala);
              i += 1;
              continue;
              boolean bool = locala.equals(WnsGlobal.a.b);
              if (bool)
              {
                i = 1;
                break;
              }
              i = 2;
            }
        }
      }
    }
    finally
    {
      monitorexit;
    }
    e = localObject2;
    monitorexit;
  }

  public static final boolean d()
  {
    return c < 1L;
  }

  public static final boolean e()
  {
    return (c > 0L) && (SystemClock.elapsedRealtime() - c < d);
  }

  public static final boolean f()
  {
    return (c > 0L) && (SystemClock.elapsedRealtime() - c >= d);
  }

  public static final boolean g()
  {
    return c > 0L;
  }

  public static WnsGlobal.a h()
  {
    if (d())
      return WnsGlobal.a.a;
    if (e())
      return WnsGlobal.a.b;
    return WnsGlobal.a.c;
  }

  public static String i()
  {
    return "V1_AND_CLOUDWNS_2.5.14_1_CLOUD_A";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.service.WnsGlobal
 * JD-Core Version:    0.6.0
 */