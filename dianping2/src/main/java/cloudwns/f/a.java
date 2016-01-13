package cloudwns.f;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import cloudwns.c.d.a;
import cloudwns.c.d.b;
import cloudwns.c.g;
import com.tencent.wns.client.data.Option;
import com.tencent.wns.client.data.WnsError;
import com.tencent.wns.data.Client;
import com.tencent.wns.data.e.a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class a
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  private static volatile a a = null;
  private static final String l = d.a("2.5.14");
  private static final String m = d.a(Build.MODEL + '(' + Build.VERSION.RELEASE + ')');
  private cloudwns.a.c b = new cloudwns.a.c();
  private List c = new ArrayList();
  private volatile long d = 600000L;
  private volatile int e = 50;
  private volatile int f = 10;
  private String g = "http://wspeed.qq.com/w.cgi";
  private String h = null;
  private volatile boolean i = false;
  private volatile boolean j = false;
  private final Random k = new Random();
  private d.b n = d.b.a;
  private Client o;
  private volatile int p = 0;
  private volatile String q = null;
  private volatile String r = null;
  private a.a s = null;
  private com.tencent.base.os.clock.e t;
  private com.tencent.base.os.clock.d u = new b(this);

  public a()
  {
    j();
    onSharedPreferenceChanged(null, null);
    Option.startListen(this);
  }

  public static a a()
  {
    if (a == null)
      monitorenter;
    try
    {
      if (a == null)
        a = new a();
      return a;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static final void a(int paramInt)
  {
    int i1 = paramInt;
    if (paramInt < 1)
      i1 = 50;
    Option.putInt("access.data.count", i1).commit();
  }

  public static final void a(long paramLong)
  {
    long l1 = paramLong;
    if (paramLong < 1000L)
      l1 = 600000L;
    Option.putLong("access.time.interval", l1).commit();
  }

  public static final void a(String paramString)
  {
    Option.putString("access.server.backup", paramString).commit();
  }

  private boolean a(String paramString1, String paramString2)
  {
    boolean bool2 = a(h(), null, paramString1, paramString2);
    boolean bool1 = bool2;
    if (!bool2)
    {
      String str = i();
      bool1 = bool2;
      if (str != null)
        bool1 = a(str, "wspeed.qq.com", paramString1, paramString2);
    }
    return bool1;
  }

  private boolean a(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    boolean bool1 = false;
    switch (a.1.a[this.n.ordinal()])
    {
    default:
    case 1:
      boolean bool2;
      do
      {
        return bool1;
        bool1 = cloudwns.c.d.a(cloudwns.c.d.a(paramString1, paramString3, paramString4, true, null, 60000, 60000, paramString2));
        if (bool1)
        {
          this.n = d.b.b;
          return bool1;
        }
        bool2 = cloudwns.c.d.a(cloudwns.c.d.a(paramString1, paramString3, paramString4, true, d.a.a, 60000, 60000, paramString2));
        bool1 = bool2;
      }
      while (!bool2);
      this.n = d.b.c;
      return bool2;
    case 2:
      return cloudwns.c.d.a(cloudwns.c.d.a(paramString1, paramString3, paramString4, true, null, 60000, 60000, paramString2));
    case 3:
    }
    return cloudwns.c.d.a(cloudwns.c.d.a(paramString1, paramString3, paramString4, true, d.a.a, 60000, 60000, paramString2));
  }

  public static final void b(int paramInt)
  {
    int i1 = paramInt;
    if (paramInt < 1)
      i1 = 1;
    Option.putInt("access.samplerate", i1).commit();
  }

  private boolean e(int paramInt)
  {
    ArrayList localArrayList = this.b.a();
    Object localObject1 = new ArrayList(localArrayList);
    if (this.c != null)
      ((ArrayList)localObject1).addAll(this.c);
    cloudwns.l.c.autoTrace(4, "Reporter", "Will Flush All = " + ((ArrayList)localObject1).size(), null);
    if (((ArrayList)localObject1).size() < 1)
      return true;
    Object localObject2 = new StringBuilder();
    String str = d.a(cloudwns.c.b.a());
    ((StringBuilder)localObject2).append("device").append('=').append(m).append('&');
    ((StringBuilder)localObject2).append("deviceinfo").append('=').append(str).append('&');
    ((StringBuilder)localObject2).append("sdkversion").append('=').append(l).append('&');
    ((StringBuilder)localObject2).append("frequency").append('=').append(paramInt);
    localObject2 = d.a((StringBuilder)localObject2, (List)localObject1);
    if (((ArrayList)localObject1).size() > 1);
    boolean bool;
    for (localObject1 = "POST"; ; localObject1 = "GET")
    {
      bool = a((String)localObject1, (String)localObject2);
      cloudwns.l.c.autoTrace(4, "Reporter", "Send Result = " + bool, null);
      localObject1 = this.c.iterator();
      while (((Iterator)localObject1).hasNext())
        ((d)((Iterator)localObject1).next()).c();
    }
    this.c.clear();
    if (bool)
    {
      localObject1 = localArrayList.iterator();
      while (((Iterator)localObject1).hasNext())
        ((d)((Iterator)localObject1).next()).c();
    }
    this.c.addAll(localArrayList);
    return bool;
  }

  private void f(int paramInt)
  {
    this.f = paramInt;
  }

  private void j()
  {
    com.tencent.base.os.clock.e.a(e(), e(), this.u);
  }

  private void k()
  {
    g.a().execute(new c(this));
  }

  private void l()
  {
    boolean bool2 = true;
    if (this.i)
      return;
    this.i = true;
    int i1;
    boolean bool1;
    if (this.j)
    {
      i1 = 1;
      this.j = false;
      bool1 = bool2;
      if (i1 > 1)
      {
        if (this.k.nextInt(i1) != 0)
          break label117;
        bool1 = bool2;
      }
      label54: cloudwns.l.c.autoTrace(4, "Reporter", "Flushin' with rate = " + i1 + " & " + bool1, null);
      if (!bool1)
        break label123;
      e(i1);
    }
    while (true)
    {
      this.i = false;
      return;
      i1 = g();
      break;
      label117: bool1 = false;
      break label54;
      label123: Object localObject1 = this.b.a();
      Object localObject2 = new StringBuilder().append("--> CYCLE ");
      if (localObject1 == null);
      for (i1 = -1; ; i1 = ((List)localObject1).size())
      {
        cloudwns.l.c.autoTrace(4, "Reporter", i1, null);
        localObject2 = ((List)localObject1).iterator();
        while (((Iterator)localObject2).hasNext())
          ((d)((Iterator)localObject2).next()).c();
      }
      ((List)localObject1).clear();
      localObject1 = this.c.iterator();
      while (((Iterator)localObject1).hasNext())
        ((d)((Iterator)localObject1).next()).c();
      this.c.clear();
    }
  }

  public void a(a.a parama)
  {
    this.s = parama;
  }

  public void a(d paramd)
  {
    if (paramd == null);
    while (true)
    {
      return;
      d locald = paramd;
      if (this.s != null)
        locald = this.s.a(paramd);
      if (locald == null)
        continue;
      try
      {
        paramd = locald.b(11);
        String str = locald.b(26);
        if ((!paramd.equals("")) && (TextUtils.isEmpty(str)))
        {
          int i1 = Integer.valueOf(paramd).intValue();
          locald.a(11, Integer.valueOf(WnsError.convertToMainErrorCode(i1)));
          locald.a(26, Integer.valueOf(i1));
        }
        label94: if (this.b.a(locald) < f())
          continue;
        c();
        return;
      }
      catch (java.lang.RuntimeException paramd)
      {
        break label94;
      }
    }
  }

  public void a(Client paramClient)
  {
    this.o = paramClient;
  }

  public d b()
  {
    d locald = d.a();
    if (cloudwns.d.e.m());
    for (String str = "wifi"; ; str = cloudwns.d.e.e())
    {
      locald.a(0, str);
      locald.a(1, Long.valueOf(System.currentTimeMillis() / 1000L));
      locald.a(6, "0");
      locald.a(7, "");
      locald.a(19, Integer.valueOf(this.p));
      locald.a(20, this.q);
      locald.a(21, this.r);
      if (this.o != null)
      {
        locald.a(2, Integer.valueOf(this.o.a()));
        locald.a(3, this.o.c());
        locald.a(4, this.o.d());
        locald.a(5, this.o.c() + "_" + this.o.d());
      }
      return locald;
    }
  }

  public void b(long paramLong)
  {
    this.d = paramLong;
  }

  public void b(String paramString)
  {
    this.h = paramString;
  }

  public void c()
  {
    com.tencent.base.os.clock.e.a(this.t);
    k();
    j();
  }

  public void c(int paramInt)
  {
    this.e = paramInt;
  }

  public void c(String paramString)
  {
    this.q = paramString;
  }

  public void d()
  {
    this.j = true;
  }

  public void d(int paramInt)
  {
    this.p = paramInt;
  }

  public void d(String paramString)
  {
    this.r = paramString;
  }

  public long e()
  {
    return this.d;
  }

  public int f()
  {
    if ((this.o != null) && (this.o.f()))
      return 1;
    return this.e;
  }

  public int g()
  {
    if ((this.o != null) && (this.o.f()))
      return 1;
    return this.f;
  }

  public String h()
  {
    return this.g;
  }

  public String i()
  {
    if (this.h == null)
      return null;
    return "http://" + this.h + "/w.cgi";
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    int i1 = 1;
    int i2;
    if (("access.samplerate".equals(paramString)) || (paramString == null))
    {
      i2 = Option.getInt("access.samplerate", 10);
      if (i2 >= 1)
        break label148;
    }
    while (true)
    {
      f(i1);
      return;
      if (("access.data.count".equals(paramString)) || (paramString == null))
      {
        i1 = Option.getInt("access.data.count", 50);
        if (i1 >= 1)
          break label145;
        i1 = 50;
      }
      label145: 
      while (true)
      {
        c(i1);
        return;
        if (("access.time.interval".equals(paramString)) || (paramString == null))
        {
          long l2 = Option.getLong("access.time.interval", 600000L);
          long l1 = l2;
          if (l2 < 1000L)
            l1 = 600000L;
          b(l1);
          return;
        }
        if ((!"access.server.backup".equals(paramString)) && (paramString != null))
          break;
        b(Option.getString("access.server.backup", e.a.a));
        return;
      }
      label148: i1 = i2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.f.a
 * JD-Core Version:    0.6.0
 */