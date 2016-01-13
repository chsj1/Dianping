package cloudwns.i;

import QMF_SERVICE.WnsIpInfo;
import cloudwns.d.m;
import cloudwns.d.p;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class e
{
  public static String a;
  private static final String b = e.class.getName();
  private static byte k;
  private static String[] l;
  private static String[] m;
  private List c = new ArrayList();
  private List d = new ArrayList();
  private List e = new ArrayList();
  private List f = new ArrayList();
  private ConcurrentHashMap g = new ConcurrentHashMap();
  private int h = 11;
  private byte i = f.a.a();
  private Map j = new HashMap();

  static
  {
    a = "WIFI_OPERATOR";
    k = 0;
    l = new String[] { "wnsacc.qcloud.com", "cwns.qq.com" };
    m = new String[] { "115.159.15.249", "117.144.244.125" };
  }

  public e()
  {
    i();
  }

  public static d a(WnsIpInfo paramWnsIpInfo, int paramInt)
  {
    d locald = new d();
    locald.a = paramWnsIpInfo.c;
    locald.b = cloudwns.a.a.b(cloudwns.a.a.c(paramWnsIpInfo.a));
    locald.c = paramWnsIpInfo.b;
    locald.e = paramInt;
    locald.d = paramWnsIpInfo.d;
    return locald;
  }

  public static String a()
  {
    if (l == null)
      return "can't.reach.here.com";
    if (k < l.length)
      return l[k];
    return l[0];
  }

  public static List a(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramString == null) || (paramString.length() < 1));
    while (true)
    {
      return localArrayList;
      try
      {
        paramString = new JSONObject(paramString).getJSONArray("ips");
        int n = 0;
        while (n < paramString.length())
        {
          JSONObject localJSONObject = paramString.getJSONObject(n);
          localArrayList.add(new d(cloudwns.a.a.b(localJSONObject.getString("ip")), localJSONObject.getInt("port"), 1, Integer.parseInt(localJSONObject.getString("apn"))));
          n += 1;
        }
      }
      catch (org.json.JSONException paramString)
      {
      }
    }
    return localArrayList;
  }

  public static void a(byte paramByte)
  {
    k = paramByte;
  }

  public static String b()
  {
    return a();
  }

  private void b(List paramList1, List paramList2, int paramInt)
  {
    monitorenter;
    try
    {
      cloudwns.l.a.c(b, "saveToDabaBase type = " + paramInt + " size = " + paramList1.size());
      paramList1 = paramList1.iterator();
      while (paramList1.hasNext())
      {
        d locald = (d)paramList1.next();
        locald.e = paramInt;
        paramList2.add(locald);
      }
    }
    finally
    {
      monitorexit;
    }
    monitorexit;
  }

  private void i()
  {
    this.j.put(cloudwns.d.a.b.a(), Byte.valueOf(0));
    this.j.put(cloudwns.d.a.a.a(), Byte.valueOf(0));
    this.j.put(cloudwns.d.a.c.a(), Byte.valueOf(1));
    this.j.put(cloudwns.d.a.d.a(), Byte.valueOf(2));
    this.j.put(cloudwns.d.a.e.a(), Byte.valueOf(5));
    this.j.put(cloudwns.d.a.f.a(), Byte.valueOf(6));
    this.j.put(cloudwns.d.a.g.a(), Byte.valueOf(3));
    this.j.put(cloudwns.d.a.h.a(), Byte.valueOf(4));
    this.j.put(cloudwns.d.a.i.a(), Byte.valueOf(9));
    this.j.put(cloudwns.d.a.j.a(), Byte.valueOf(8));
  }

  public void a(int paramInt)
  {
    monitorenter;
    if (paramInt == 3);
    while (true)
    {
      try
      {
        this.i = f.d.a();
        c.a("WIFI_OPERATOR", String.valueOf(paramInt));
        if (!cloudwns.d.e.m())
          continue;
        String str = p.a();
        if (str == null)
          continue;
        c.a(str, String.valueOf(paramInt) + ":" + System.currentTimeMillis());
        if (!cloudwns.d.e.l())
          continue;
        str = cloudwns.d.e.e();
        if (str == null)
          continue;
        c.a(str.toLowerCase(), String.valueOf(paramInt) + ":" + System.currentTimeMillis());
        return;
        if (paramInt == 5)
        {
          this.i = f.c.a();
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      if (paramInt == 8)
      {
        this.i = f.b.a();
        continue;
      }
      this.i = f.a.a();
    }
  }

  public void a(List paramList1, List paramList2, int paramInt)
  {
    monitorenter;
    if ((paramList1 == null) || (paramList2 == null));
    while (true)
    {
      monitorexit;
      return;
      try
      {
        paramList2.clear();
        b(paramList1, paramList2, paramInt);
      }
      finally
      {
        monitorexit;
      }
    }
  }

  // ERROR //
  public void a(Map paramMap)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull +14 -> 17
    //   6: aload_1
    //   7: invokeinterface 312 1 0
    //   12: istore_3
    //   13: iload_3
    //   14: ifeq +6 -> 20
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_1
    //   21: ldc_w 314
    //   24: invokeinterface 317 2 0
    //   29: ifeq +125 -> 154
    //   32: aload_1
    //   33: ldc_w 314
    //   36: invokeinterface 321 2 0
    //   41: checkcast 238	java/util/Map
    //   44: astore_2
    //   45: aload_2
    //   46: ifnull +108 -> 154
    //   49: aload_2
    //   50: invokeinterface 312 1 0
    //   55: ifne +99 -> 154
    //   58: aload_0
    //   59: aload_2
    //   60: ldc_w 323
    //   63: invokeinterface 321 2 0
    //   68: invokevirtual 324	java/lang/Object:toString	()Ljava/lang/String;
    //   71: invokestatic 326	cloudwns/i/e:a	(Ljava/lang/String;)Ljava/util/List;
    //   74: aload_0
    //   75: getfield 63	cloudwns/i/e:c	Ljava/util/List;
    //   78: iconst_1
    //   79: invokevirtual 328	cloudwns/i/e:a	(Ljava/util/List;Ljava/util/List;I)V
    //   82: aload_0
    //   83: aload_2
    //   84: ldc_w 330
    //   87: invokeinterface 321 2 0
    //   92: invokevirtual 324	java/lang/Object:toString	()Ljava/lang/String;
    //   95: invokestatic 326	cloudwns/i/e:a	(Ljava/lang/String;)Ljava/util/List;
    //   98: aload_0
    //   99: getfield 65	cloudwns/i/e:d	Ljava/util/List;
    //   102: iconst_0
    //   103: invokevirtual 328	cloudwns/i/e:a	(Ljava/util/List;Ljava/util/List;I)V
    //   106: aload_0
    //   107: aload_2
    //   108: ldc_w 332
    //   111: invokeinterface 321 2 0
    //   116: invokevirtual 324	java/lang/Object:toString	()Ljava/lang/String;
    //   119: invokestatic 326	cloudwns/i/e:a	(Ljava/lang/String;)Ljava/util/List;
    //   122: aload_0
    //   123: getfield 67	cloudwns/i/e:e	Ljava/util/List;
    //   126: iconst_2
    //   127: invokevirtual 328	cloudwns/i/e:a	(Ljava/util/List;Ljava/util/List;I)V
    //   130: aload_0
    //   131: aload_2
    //   132: ldc_w 334
    //   135: invokeinterface 321 2 0
    //   140: invokevirtual 324	java/lang/Object:toString	()Ljava/lang/String;
    //   143: invokestatic 326	cloudwns/i/e:a	(Ljava/lang/String;)Ljava/util/List;
    //   146: aload_0
    //   147: getfield 69	cloudwns/i/e:f	Ljava/util/List;
    //   150: iconst_3
    //   151: invokevirtual 328	cloudwns/i/e:a	(Ljava/util/List;Ljava/util/List;I)V
    //   154: aload_1
    //   155: ldc_w 336
    //   158: invokeinterface 317 2 0
    //   163: istore_3
    //   164: iload_3
    //   165: ifeq -148 -> 17
    //   168: aload_1
    //   169: ldc_w 336
    //   172: invokeinterface 321 2 0
    //   177: checkcast 238	java/util/Map
    //   180: astore_1
    //   181: aload_1
    //   182: ifnull -165 -> 17
    //   185: aload_1
    //   186: invokeinterface 312 1 0
    //   191: ifne -174 -> 17
    //   194: new 96	cloudwns/i/d
    //   197: dup
    //   198: invokespecial 97	cloudwns/i/d:<init>	()V
    //   201: astore_2
    //   202: aload_2
    //   203: aload_1
    //   204: ldc 161
    //   206: invokeinterface 321 2 0
    //   211: checkcast 42	java/lang/String
    //   214: invokestatic 172	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   217: putfield 118	cloudwns/i/d:c	I
    //   220: aload_2
    //   221: aload_1
    //   222: ldc 152
    //   224: invokeinterface 321 2 0
    //   229: checkcast 42	java/lang/String
    //   232: putfield 113	cloudwns/i/d:b	Ljava/lang/String;
    //   235: invokestatic 277	cloudwns/d/p:a	()Ljava/lang/String;
    //   238: ifnull -221 -> 17
    //   241: aload_0
    //   242: getfield 74	cloudwns/i/e:g	Ljava/util/concurrent/ConcurrentHashMap;
    //   245: invokestatic 277	cloudwns/d/p:a	()Ljava/lang/String;
    //   248: aload_2
    //   249: invokevirtual 337	java/util/concurrent/ConcurrentHashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   252: pop
    //   253: goto -236 -> 17
    //   256: astore_1
    //   257: getstatic 34	cloudwns/i/e:b	Ljava/lang/String;
    //   260: ldc_w 339
    //   263: invokestatic 341	cloudwns/l/a:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   266: goto -249 -> 17
    //   269: astore_1
    //   270: aload_0
    //   271: monitorexit
    //   272: aload_1
    //   273: athrow
    //   274: astore_1
    //   275: getstatic 34	cloudwns/i/e:b	Ljava/lang/String;
    //   278: ldc_w 343
    //   281: invokestatic 341	cloudwns/l/a:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   284: goto -267 -> 17
    //
    // Exception table:
    //   from	to	target	type
    //   168	181	256	java/lang/NumberFormatException
    //   185	253	256	java/lang/NumberFormatException
    //   6	13	269	finally
    //   20	45	269	finally
    //   49	154	269	finally
    //   154	164	269	finally
    //   168	181	269	finally
    //   185	253	269	finally
    //   257	266	269	finally
    //   275	284	269	finally
    //   168	181	274	java/lang/Exception
    //   185	253	274	java/lang/Exception
  }

  public d c()
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (this.d.size() > 0)
          continue;
        this.d.add(new d(cloudwns.a.a.b(m[k]), 80, 0, f.b.a()));
        this.d.add(new d(cloudwns.a.a.b(m[k]), 80, 0, f.c.a()));
        this.d.add(new d(cloudwns.a.a.b(m[k]), 80, 0, f.d.a()));
        int i1 = f();
        int i2 = f.e.a();
        int n = i1;
        if (i2 != i1)
          continue;
        try
        {
          switch (Integer.parseInt(c.b()))
          {
          case 4:
          case 6:
          case 7:
            n = f.c.a();
            if (f.a.a() != n)
              break label341;
            n = f.c.a();
            Object localObject3 = this.d.iterator();
            if (((Iterator)localObject3).hasNext())
            {
              d locald = (d)((Iterator)localObject3).next();
              if (n != locald.a)
                continue;
              localObject3 = locald;
              if (locald != null)
                continue;
              localObject3 = new d(cloudwns.a.a.b(m[k]), 80, 0, f.b.a());
              monitorexit;
              return localObject3;
            }
          case 3:
            n = f.d.a();
            break;
          case 5:
            n = f.c.a();
            break;
          case 8:
            n = f.b.a();
            continue;
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          n = f.c.a();
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      Object localObject2 = null;
      continue;
      label341: continue;
    }
  }

  public d d()
  {
    monitorenter;
    while (true)
    {
      try
      {
        if (this.e == null)
          continue;
        if ((!this.e.isEmpty()) || (!this.e.isEmpty()))
          continue;
        this.e.add(new d(cloudwns.a.a.b("117.135.171.235"), 80, 2, 1));
        this.e.add(new d(cloudwns.a.a.b("140.206.160.170"), 80, 2, 2));
        this.e.add(new d(cloudwns.a.a.b("101.226.129.182"), 80, 2, 3));
        int n = f();
        Object localObject3 = this.e.iterator();
        if (((Iterator)localObject3).hasNext())
        {
          d locald = (d)((Iterator)localObject3).next();
          if (n != locald.a)
            continue;
          localObject3 = locald;
          if (locald != null)
            continue;
          localObject3 = locald;
          if (this.e.isEmpty())
            continue;
          localObject3 = (d)this.e.get(0);
          return localObject3;
          localObject3 = new d(cloudwns.a.a.b("117.135.171.235"), 80, 2, 1);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      Object localObject2 = null;
    }
  }

  public d e()
  {
    monitorenter;
    try
    {
      d locald1 = new d();
      locald1.b = a();
      if (cloudwns.d.e.m())
        locald1.a = 7;
      while (true)
      {
        return locald1;
        int n = f();
        if (this.c != null)
        {
          Iterator localIterator = this.c.iterator();
          while (true)
            if (localIterator.hasNext())
            {
              d locald2 = (d)localIterator.next();
              if (locald2.a != n)
                continue;
              locald1.b = locald2.b;
              locald1.a = n;
              break;
            }
          locald1.a = n;
          continue;
        }
        locald1.a = n;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public byte f()
  {
    if (cloudwns.d.e.l())
      return f.a(cloudwns.d.e.c().b().a());
    return f.e.a();
  }

  public byte g()
  {
    monitorenter;
    try
    {
      int n;
      if (cloudwns.d.e.l())
      {
        cloudwns.d.a locala = cloudwns.d.e.c();
        if (locala != null)
          n = ((Byte)this.j.get(locala.a())).byteValue();
      }
      while (true)
      {
        return n;
        n = 0;
        continue;
        n = 7;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public d h()
  {
    Object localObject4 = null;
    monitorenter;
    Object localObject1 = localObject4;
    try
    {
      if (this.g != null)
      {
        localObject1 = localObject4;
        if (!this.g.isEmpty())
        {
          localObject1 = p.a();
          if (localObject1 == null)
            break label49;
        }
      }
      label49: for (localObject1 = (d)this.g.get(localObject1); ; localObject1 = null)
        return localObject1;
    }
    catch (Exception localObject2)
    {
      while (true)
      {
        cloudwns.l.a.a(b, "getWifiOptimalServer fail", localException);
        Object localObject2 = localObject4;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject3;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.i.e
 * JD-Core Version:    0.6.0
 */