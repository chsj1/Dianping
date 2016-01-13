package com.tencent.wns.service;

import QMF_LOG.WnsCmdLogUploadReq;
import QMF_SERVICE.WnsCloudCmdTestReq;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import cloudwns.i.d;
import cloudwns.s.l;
import com.tencent.wns.client.data.Option;
import com.tencent.wns.client.data.WnsError;
import com.tencent.wns.data.A2Ticket;
import com.tencent.wns.data.AccountInfo;
import com.tencent.wns.data.B2Ticket;
import com.tencent.wns.data.Client;
import com.tencent.wns.ipc.d.a;
import com.tencent.wns.ipc.d.b;
import com.tencent.wns.ipc.d.c;
import com.tencent.wns.ipc.d.d;
import com.tencent.wns.ipc.d.e;
import com.tencent.wns.ipc.d.f;
import com.tencent.wns.ipc.d.g;
import com.tencent.wns.ipc.d.h;
import com.tencent.wns.ipc.d.j;
import com.tencent.wns.ipc.d.k;
import com.tencent.wns.ipc.d.l;
import com.tencent.wns.ipc.d.m;
import com.tencent.wns.ipc.d.n;
import com.tencent.wns.ipc.d.o;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public final class e extends com.tencent.wns.ipc.b.a
  implements cloudwns.d.k, cloudwns.k.b, cloudwns.k.f, cloudwns.s.b, b.a, Observer
{
  public static final e a = new e();
  private static final String[] m = { "wnstest/test100b", "wnstest/test200b", "wnstest/test500b", "wnstest/test1k", "wnstest/test4k" };
  private static final cloudwns.u.b.a p = new j();
  private volatile cloudwns.q.a b;
  private Object c = new Object();
  private int d = 1;
  private long e = 0L;
  private long f = 0L;
  private final ConcurrentHashMap g = new ConcurrentHashMap(5);
  private long h = -1L;
  private volatile CountDownLatch i;
  private long j = 600000L;
  private long k = 60000L;
  private long l;
  private Random n = new Random(System.currentTimeMillis());
  private WnsGlobal.b o = new i(this);

  private e()
  {
    long l1 = System.currentTimeMillis();
    cloudwns.d.e.a(this);
    l.a().a(this);
    cloudwns.i.a.a().addObserver(this);
    cloudwns.k.a.a().a(this);
    cloudwns.k.e.a().a(this);
    WnsGlobal.a(true);
    b.a(this);
    m();
    k();
    WnsGlobal.a(this.o);
    long l2 = System.currentTimeMillis();
    cloudwns.b.i.a("WnsBinder init  cost=" + (l2 - l1));
  }

  private int a(d.b paramb, com.tencent.wns.ipc.a parama)
  {
    cloudwns.l.a.c("WnsBinder", "bindUid  " + paramb);
    boolean bool = paramb.c();
    paramb = paramb.d();
    if ((bool) && (TextUtils.isEmpty(paramb)))
    {
      a(new d.c(), 604, 0, WnsError.getErrorMessage(604), parama);
      return -1;
    }
    this.b.a(paramb, bool, parama);
    return 0;
  }

  private int a(d.d paramd, com.tencent.wns.ipc.a parama)
  {
    return 0;
  }

  private int a(d.h paramh, com.tencent.wns.ipc.a parama)
  {
    cloudwns.l.a.c("WnsBinder", "setPush  " + paramh);
    this.b.a(paramh.c(), parama);
    return 0;
  }

  private int a(d.j paramj, com.tencent.wns.ipc.a parama)
  {
    long l1 = a(paramj);
    String str1 = paramj.j();
    if (TextUtils.isEmpty(str1))
    {
      if (parama != null);
      try
      {
        cloudwns.l.a.d("WnsBinder", "upload succ ? false");
        paramj = new d.n();
        paramj.b(582);
        parama.a(paramj.a());
        return -1;
      }
      catch (RemoteException paramj)
      {
        while (true)
          cloudwns.l.a.b("WnsBinder", paramj.getMessage(), paramj);
      }
    }
    String str2 = (String)cloudwns.i.a.a().e().a("ReportLogServer", "183.61.39.173");
    str2 = "http://" + str2 + ":80";
    cloudwns.l.a.d("WnsBinder", "upload file " + str1 + " to " + str2);
    cloudwns.r.c.a(l1, str2, new File(str1), paramj.d(), paramj.e(), paramj.i(), str2, new g(this, parama));
    return 0;
  }

  private int a(d.m paramm, com.tencent.wns.ipc.a parama)
  {
    return -1;
  }

  private long a(d.j paramj)
  {
    long l1 = 0L;
    long l2 = paramj.c();
    if ((l2 == 0L) && (l2 < 0L));
    while (true)
    {
      try
      {
        l2 = Long.parseLong(paramj.h());
        l1 = l2;
        if (l1 <= 10000L);
        return l1;
      }
      catch (NumberFormatException paramj)
      {
        continue;
      }
      l1 = l2;
    }
  }

  private e a(Client paramClient, Messenger paramMessenger)
  {
    b(paramClient);
    a(paramClient);
    WnsGlobal.a(paramClient);
    n.a(paramMessenger);
    l();
    cloudwns.f.a.a().a(WnsGlobal.a());
    if (paramMessenger != null)
      cloudwns.c.g.a().execute(new f(this));
    return this;
  }

  private void a(Client paramClient)
  {
    cloudwns.i.e.a((byte)paramClient.g());
  }

  public static void a(d.o paramo, int paramInt1, int paramInt2, String paramString, com.tencent.wns.ipc.a parama)
  {
    if (parama != null)
    {
      paramo.b(paramInt1);
      paramo.a(paramString);
      paramo.c(paramInt2);
    }
    try
    {
      parama.a(paramo.a());
      return;
    }
    catch (RemoteException paramo)
    {
      paramo.printStackTrace();
    }
  }

  private int b(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 0;
    case 2:
    case 3:
    case 4:
      return 2;
    case 1:
    }
    return 1;
  }

  private int b(d.d paramd, com.tencent.wns.ipc.a parama)
  {
    return 0;
  }

  private int b(d.j paramj, com.tencent.wns.ipc.a parama)
  {
    long l3 = e();
    long l4 = paramj.f();
    long l5 = paramj.g();
    Object localObject = paramj.k();
    String str;
    if (TextUtils.isEmpty((CharSequence)((HashMap)localObject).get("batchid")))
      str = "";
    while (true)
    {
      long l1;
      if (TextUtils.isEmpty((CharSequence)((HashMap)localObject).get("attachinfo")))
      {
        localObject = "";
        cloudwns.l.a.d("WnsBinder", "begin LogUpload of <" + l3 + ">, from " + cloudwns.l.c.printTimeStr(l4) + " to " + cloudwns.l.c.printTimeStr(l5) + ", batchid = " + str + ", attachInfo = " + (String)localObject + ", Prepare the Logs");
        l1 = 0L;
      }
      try
      {
        long l2 = Long.valueOf(str).longValue();
        l1 = l2;
        str = (String)cloudwns.i.a.a().e().a("ReportLogServer", "183.61.39.173");
        str = "http://" + str + ":80";
        cloudwns.l.a.d("WnsBinder", "report log to " + str);
        cloudwns.r.b.a(l3, str, null, l4, l5, 0, paramj.d(), paramj.e(), 1048576, paramj.i(), l1, (String)localObject, new h(this, parama));
        return 0;
        str = (String)((HashMap)localObject).get("batchid");
        continue;
        localObject = (String)((HashMap)localObject).get("attachinfo");
      }
      catch (NumberFormatException localNumberFormatException)
      {
        while (true)
          cloudwns.l.a.e("WnsBinder", localNumberFormatException.toString());
      }
    }
  }

  private int b(d.m paramm, com.tencent.wns.ipc.a parama)
  {
    cloudwns.l.a.c("WnsBinder", "BEGIN Transfer => " + paramm);
    cloudwns.q.a locala = this.b;
    if (locala != null)
      locala.a(paramm, parama);
    while (true)
    {
      return 0;
      cloudwns.l.a.d("WnsBinder", "END Transfer => Not Login Yet, Transfer Failed : " + paramm);
      if (parama == null)
        continue;
      paramm = new d.n();
      paramm.b(533);
      parama.a(paramm.a());
    }
  }

  private void b(Client paramClient)
  {
    paramClient = paramClient.b();
    if (paramClient != null)
    {
      switch (e.1.a[paramClient.ordinal()])
      {
      default:
        throw new RuntimeException("unknown business type");
      case 1:
        l.a().a(true);
      case 2:
      }
      for (paramClient = cloudwns.m.f.a(); ; paramClient = cloudwns.m.f.b())
      {
        cloudwns.m.b.a().a(paramClient);
        return;
        l.a().a(false);
      }
    }
    throw new RuntimeException("must set businessType");
  }

  private int c(d.d paramd, com.tencent.wns.ipc.a parama)
  {
    return 0;
  }

  private int d(d.d paramd, com.tencent.wns.ipc.a parama)
  {
    return 0;
  }

  private void d(String paramString)
  {
    l.a().b(paramString);
  }

  private int e(d.d paramd, com.tencent.wns.ipc.a parama)
  {
    cloudwns.l.a.c("WnsBinder", "BEGIN Login => " + paramd);
    int i1 = paramd.c();
    if (this.i == null)
      this.i = new CountDownLatch(1);
    int i2;
    switch (i1)
    {
    default:
      i2 = -1;
      i1 = i2;
      if (this.i == null)
        break;
      this.i.countDown();
      this.i = null;
      i1 = i2;
    case 2:
    case 1:
    case 3:
    case 0:
    case 4:
    }
    while (true)
    {
      return i1;
      try
      {
        i2 = c(paramd, parama);
        i1 = i2;
        return i2;
        i2 = d(paramd, parama);
        i1 = i2;
        return i2;
        i2 = b(paramd, parama);
        i1 = i2;
        return i2;
        i2 = a(paramd, parama);
        i1 = i2;
        return i2;
      }
      finally
      {
        if (this.i != null)
        {
          this.i.countDown();
          this.i = null;
        }
      }
    }
    throw paramd;
  }

  private void k()
  {
    synchronized (this.c)
    {
      if (this.b == null)
      {
        cloudwns.l.a.c("WnsBinder", "create new empty biz");
        this.b = cloudwns.q.a.a(this, -1L, true);
      }
      this.b.a();
      return;
    }
  }

  private final void l()
  {
    Option.putString("protect.client", WnsGlobal.a().toString()).commit();
    cloudwns.l.a.e("WnsBinder", "Client Protection Saved : " + WnsGlobal.a().toString());
  }

  private final void m()
  {
    String str = Option.getString("protect.client", null);
    if ((str == null) || (str.length() < 1))
      return;
    cloudwns.l.a.e("WnsBinder", "Client Protection Loaded : " + str);
    try
    {
      a(new Client(str), null);
      o();
      return;
    }
    catch (Exception localException)
    {
      cloudwns.l.a.c("WnsBinder", "Client Protection Failed", localException);
    }
  }

  private final void n()
  {
    synchronized (this.c)
    {
      if (this.b == null);
      for (String str = ""; ; str = this.b.toString())
      {
        cloudwns.l.a.c("WnsBinder", "Biz Protection Saved : " + str);
        if (!com.tencent.base.util.g.b(str))
          break;
        Option.remove("protect.biz").commit();
        return;
      }
      Option.putString("protect.biz", str).commit();
    }
  }

  private final void o()
  {
    synchronized (this.c)
    {
      String str = Option.getString("protect.biz", null);
      cloudwns.l.a.c("WnsBinder", "Biz Protection Loaded : " + str);
      if ((str != null) && (cloudwns.q.a.a(str)))
        this.b = cloudwns.q.a.a(this, str);
      return;
    }
  }

  private void p()
  {
    if ((WnsGlobal.d()) && (WnsGlobal.a().f()))
    {
      long l3 = System.currentTimeMillis();
      long l2 = cloudwns.i.a.a().e().a("TestModeReqInterval", this.j);
      long l1 = l2;
      if (l2 < this.k)
        l1 = this.k;
      if (l3 - this.l > l1)
      {
        int i1 = this.n.nextInt(m.length);
        Object localObject = m[i1];
        d.m localm = new d.m();
        localm.a((String)localObject);
        i1 = (int)cloudwns.i.a.a().e().a("RequestTimeout", 60000L);
        localm.b(i1);
        localm.a(new WnsCloudCmdTestReq().toByteArray());
        cloudwns.l.a.b("WnsBinder", "QuickVerification:send test req, cmd = " + (String)localObject + ", timeout = " + i1);
        localObject = this.b;
        if (localObject != null)
        {
          ((cloudwns.q.a)localObject).a(localm, new k(this));
          this.l = l3;
          return;
        }
        cloudwns.l.a.d("WnsBinder", "QuickVerification:send test req => Not Login Yet, test Failed : " + localm);
        return;
      }
      cloudwns.l.a.b("WnsBinder", "QuickVerification:Foreground, curTime = " + l3 + ", interval = " + (l3 - this.l) + ", default interval = " + this.j + ", so ignore.");
      return;
    }
    cloudwns.l.a.b("WnsBinder", "QuickVerification:isForeground = " + WnsGlobal.d() + ", isDebug = " + WnsGlobal.a().f());
  }

  public int a(int paramInt, Bundle paramBundle, com.tencent.wns.ipc.a parama)
  {
    switch (paramInt)
    {
    case 7:
    default:
      return -1;
    case 1:
    case 2:
    case 3:
    case 4:
    case 6:
    case 5:
    case 8:
    case 10:
    case 9:
    case 11:
    case 12:
    }
    try
    {
      return a(new d.a(paramBundle), parama);
      return a(new d.g(paramBundle), parama);
      return a(new d.k(paramBundle), parama);
      return e(new d.d(paramBundle), parama);
      return a(new d.e(paramBundle), parama);
      return b(new d.m(paramBundle), parama);
      return b(new d.j(paramBundle), parama);
      return a(new d.j(paramBundle), parama);
      return a(new d.m(paramBundle), parama);
      return a(new d.h(paramBundle), parama);
      paramInt = a(new d.b(paramBundle), parama);
      return paramInt;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle;
    }
    catch (Exception paramBundle)
    {
    }
    throw new Error(paramBundle);
  }

  public int a(Bundle paramBundle)
  {
    if (paramBundle == null)
      return -2147483648;
    try
    {
      paramBundle.setClassLoader(Client.class.getClassLoader());
      Client localClient = (Client)paramBundle.getParcelable("ipc.client.info");
      if (localClient == null)
        return -2147483648;
      paramBundle = (Messenger)paramBundle.getParcelable("ipc.client.notifier");
      if (paramBundle == null)
        return -2147483648;
      a(localClient, paramBundle);
      int i1 = Process.myPid();
      return i1;
    }
    catch (Exception paramBundle)
    {
    }
    throw new Error(paramBundle);
  }

  public int a(d.a parama, com.tencent.wns.ipc.a parama1)
  {
    this.h = System.currentTimeMillis();
    return 0;
  }

  public int a(d.e parame, com.tencent.wns.ipc.a parama)
  {
    d.f localf = new d.f();
    this.b.a(parame.c());
    if (parama != null)
      parama.a(localf.a());
    return 0;
  }

  public int a(d.g paramg, com.tencent.wns.ipc.a parama)
  {
    return 0;
  }

  public int a(d.k paramk, com.tencent.wns.ipc.a parama)
  {
    if (!cloudwns.d.e.a())
    {
      paramk = new d.l();
      paramk.b(519);
      paramk.a("网络不可用，请检查网络链接".getBytes());
      if (parama != null)
        parama.a(paramk.a());
    }
    return -1;
  }

  public int a(String paramString, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    return cloudwns.u.b.a().a(paramString, paramLong1, paramLong2, paramBoolean);
  }

  public A2Ticket a(String paramString)
  {
    try
    {
      paramString = cloudwns.g.b.b(paramString);
      return paramString;
    }
    catch (Exception paramString)
    {
    }
    throw new Error(paramString);
  }

  public B2Ticket a(long paramLong)
  {
    try
    {
      B2Ticket localB2Ticket = cloudwns.g.b.b(paramLong);
      return localB2Ticket;
    }
    catch (Exception localException)
    {
    }
    throw new Error(localException);
  }

  public Map a(String[] paramArrayOfString)
  {
    HashMap localHashMap;
    try
    {
      localHashMap = new HashMap();
      int i2 = paramArrayOfString.length;
      int i1 = 0;
      while (i1 < i2)
      {
        String str = paramArrayOfString[i1];
        AccountInfo localAccountInfo = new AccountInfo();
        localAccountInfo.a(str);
        localAccountInfo.a(cloudwns.g.b.a(str));
        localHashMap.put(localAccountInfo, cloudwns.g.b.b(str));
        i1 += 1;
      }
    }
    catch (Exception paramArrayOfString)
    {
      throw new Error(paramArrayOfString);
    }
    return localHashMap;
  }

  public void a(long paramLong, boolean paramBoolean)
  {
  }

  public void a(long paramLong, boolean paramBoolean, int paramInt)
  {
  }

  public void a(cloudwns.d.j paramj1, cloudwns.d.j paramj2)
  {
    paramj1 = cloudwns.i.a.a().f().d();
    cloudwns.f.a.a(paramj1.a() + ':' + paramj1.c);
  }

  public void a(String paramString1, String paramString2)
  {
    try
    {
      if ("idle.timespan".equals(paramString1))
      {
        WnsGlobal.a(Boolean.valueOf(paramString2).booleanValue());
        return;
      }
      if ("suicide.enabled".equals(paramString1))
      {
        o.a(Boolean.valueOf(paramString2).booleanValue());
        return;
      }
    }
    catch (Exception paramString1)
    {
      throw new Error(paramString1);
    }
    if ("suicide.time.startup".equals(paramString1))
    {
      o.a(Long.valueOf(paramString2).longValue());
      return;
    }
    if ("guest.postfix".equals(paramString1))
    {
      WnsGlobal.a(paramString2);
      return;
    }
    if ("wns.debug.ip".equals(paramString1))
    {
      d(paramString2);
      return;
    }
    if ((!"wtlogin.debug.ip".equals(paramString1)) && ("wtlogin.clear.login".equals(paramString1)))
      cloudwns.g.b.c(paramString2);
  }

  public boolean a()
  {
    return true;
  }

  public boolean a(int paramInt)
  {
    if (this.b == null)
    {
      cloudwns.l.a.b("WnsBinder", "onPingFailed when No Account / Client");
      l.a().b(600);
    }
    return true;
  }

  public boolean a(int paramInt1, int paramInt2)
  {
    cloudwns.l.a.c("WnsBinder", "Session State Changed From " + paramInt1 + " → " + paramInt2);
    int i1 = b(paramInt1);
    int i2 = b(paramInt2);
    if ((paramInt2 != 4) && ((paramInt1 != 4) || (paramInt2 != 3)))
      a(i1, i2, true);
    return true;
  }

  public boolean a(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt1 != paramInt2)
      n.a(6, paramInt1, Integer.valueOf(paramInt2));
    return true;
  }

  public boolean a(int paramInt, String paramString, Object paramObject)
  {
    if (1915 == paramInt)
      n.a(11, paramInt, paramString, (String)paramObject);
    while (true)
    {
      return true;
      n.a(9, paramInt, paramString);
    }
  }

  public boolean a(long paramLong, int paramInt)
  {
    cloudwns.l.a.c("WnsBinder", "OpenSession ret = " + paramInt);
    return true;
  }

  public boolean a(long paramLong, int paramInt, byte[] paramArrayOfByte, byte paramByte, cloudwns.j.k paramk)
  {
    if (paramInt == 0)
    {
      cloudwns.l.a.c("WnsBinder", "HeartBeat(" + paramByte + ") of " + paramLong + " Success，Ticket Saved");
      if (paramInt != 1907)
        break label141;
      if (paramArrayOfByte == null)
        break label133;
      paramArrayOfByte = new String(paramArrayOfByte);
      label69: n.a(7, paramInt, Long.valueOf(paramLong), paramArrayOfByte);
    }
    while (true)
    {
      return true;
      cloudwns.l.a.e("WnsBinder", "HeartBeat(" + paramByte + ") of " + paramLong + " Failed，ret = " + paramInt);
      break;
      label133: paramArrayOfByte = "";
      break label69;
      label141: if (paramInt == 3020)
      {
        if (this.h < paramk.k())
        {
          if (paramArrayOfByte != null);
          for (paramArrayOfByte = new String(paramArrayOfByte); ; paramArrayOfByte = "")
          {
            n.a(7, paramInt, Long.valueOf(paramLong), paramArrayOfByte);
            break;
          }
        }
        cloudwns.l.a.d("WnsBinder", "is not hb right time, authTime=" + this.h + ", req init time=" + paramk.k());
        continue;
      }
      paramArrayOfByte = this.b;
      if (paramArrayOfByte == null)
        continue;
      paramArrayOfByte.a(paramInt, paramByte);
    }
  }

  public boolean a(long paramLong, WnsCmdLogUploadReq paramWnsCmdLogUploadReq)
  {
    long l1 = paramWnsCmdLogUploadReq.d * 1000L;
    long l2 = paramWnsCmdLogUploadReq.e * 1000L;
    int i1 = paramWnsCmdLogUploadReq.b;
    long l3 = paramWnsCmdLogUploadReq.j;
    if (paramWnsCmdLogUploadReq.i == null);
    for (String str = ""; ; str = paramWnsCmdLogUploadReq.i)
    {
      cloudwns.l.a.d("WnsBinder", "Reiceve LogUpload of <" + paramLong + ">, from " + cloudwns.l.c.printTimeStr(l1) + " to " + cloudwns.l.c.printTimeStr(l2) + ", batchid = " + l3 + ", attachInfo = " + str + ", Prepare the Logs");
      cloudwns.r.b.a(paramLong, "http://" + cloudwns.a.a.b(paramWnsCmdLogUploadReq.f) + ':' + paramWnsCmdLogUploadReq.g, null, l1, l2, i1, l3, str);
      return true;
    }
  }

  public boolean a(long paramLong, ArrayList paramArrayList)
  {
    cloudwns.q.a locala = this.b;
    if ((locala == null) || (paramLong != locala.g()))
    {
      cloudwns.l.a.e("WnsBinder", "Receive Push(es) of <" + paramLong + ">, But No BizServant ... Ignore it.");
      return false;
    }
    return locala.a(paramArrayList);
  }

  public int b(String paramString)
  {
    return cloudwns.u.b.a().a(paramString);
  }

  public Map b()
  {
    return null;
  }

  public int c()
  {
    try
    {
      int i1 = b(l.a().c());
      return i1;
    }
    catch (Exception localException)
    {
    }
    throw new Error(localException);
  }

  public boolean c(String paramString)
  {
    return cloudwns.u.b.a().c(paramString);
  }

  public Map d()
  {
    try
    {
      Map localMap = cloudwns.i.a.a().g();
      return localMap;
    }
    catch (Exception localException)
    {
    }
    throw new Error(localException);
  }

  public long e()
  {
    if (this.b != null)
      return this.b.g();
    return 0L;
  }

  public String f()
  {
    return cloudwns.l.b.a().d();
  }

  public void g()
  {
    cloudwns.u.b.a().a(p);
    boolean bool1 = WnsGlobal.f();
    String str;
    long l2;
    label34: long l3;
    long l1;
    cloudwns.q.a locala;
    if (bool1)
    {
      str = "HeartbeatTimeIdle";
      if (!bool1)
        break label185;
      l2 = 1200000L;
      l3 = cloudwns.i.a.a().e().a(str, l2);
      if (!bool1)
        this.d = 3;
      p();
      cloudwns.l.a.c("WnsBinder", "HEARTBEAT Time => NEXT is " + l3 + ", PING is " + this.d + '/' + 3);
      l1 = l3;
      if (bool1)
        l1 = l3 / 3;
      b.a(l1);
      locala = this.b;
      if (this.d < 3)
        break label252;
      if (bool1)
        this.d = 1;
      if (locala != null)
        break label193;
      cloudwns.l.a.b("WnsBinder", "Send Pin' Packet for No Account / Client");
      l.a().d(this.e);
    }
    label185: label193: label246: label250: label252: 
    do
    {
      byte b1;
      while (true)
      {
        return;
        str = "HeartbeatTime";
        break;
        l2 = 180000L;
        break label34;
        if (bool1)
        {
          b1 = 4;
          bool1 = locala.a(b1);
          if (bool1)
            this.f = System.currentTimeMillis();
          if (!bool1)
            break label246;
        }
        for (i1 = 1; ; i1 = 0)
        {
          if (i1 != 0)
            break label250;
          l.a().d(0L);
          return;
          b1 = 3;
          break;
        }
      }
      if (locala == null)
        break label388;
      l2 = cloudwns.i.a.a().e().a(str, l2) + 30000L;
      l3 = System.currentTimeMillis();
      if ((l3 - this.f < l2) && (l1 + l3 - this.f < l2))
        break label388;
      if (bool1)
      {
        b1 = 4;
        boolean bool2 = locala.a(b1);
        if (bool2)
          this.f = System.currentTimeMillis();
        if (!bool2)
          break label371;
      }
      for (int i1 = 1; ; i1 = 0)
      {
        if (i1 != 0)
          break label377;
        l.a().d(0L);
        this.d += 1;
        return;
        b1 = 3;
        break;
      }
    }
    while (!bool1);
    label371: label377: this.d = 1;
    return;
    label388: this.d += 1;
    l.a().d(this.e);
  }

  public void h()
  {
    n();
  }

  public boolean i()
  {
    cloudwns.q.a locala = this.b;
    if (locala != null)
      locala.c();
    return true;
  }

  public void j()
  {
    cloudwns.l.a.c("WnsBinder", "onMasterSessionUpdate sendEvent to client");
    n.a(14);
  }

  public void update(Observable paramObservable, Object paramObject)
  {
    if ((paramObject instanceof HashMap))
      n.a(1, 0, paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.service.e
 * JD-Core Version:    0.6.0
 */