package cloudwns.s;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.widget.Toast;
import cloudwns.b.i;
import cloudwns.i.f;
import cloudwns.j.j;
import cloudwns.j.j.a;
import cloudwns.j.k;
import com.tencent.base.Global;
import com.tencent.wns.client.data.Option;
import com.tencent.wns.client.data.WnsError;
import com.tencent.wns.data.B2Ticket;
import com.tencent.wns.service.WnsGlobal;
import com.tencent.wns.service.WnsGlobal.a;
import com.tencent.wns.service.WnsGlobal.b;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class l extends HandlerThread
{
  private static boolean D;
  private static l g = null;
  private static l.b i = null;
  private ConnectivityManager A = null;
  private long B = 0L;
  private long C = 0L;
  private WnsGlobal.a E = WnsGlobal.a.a;
  private WnsGlobal.b F = new m(this);
  private com.tencent.base.os.clock.e G;
  private Object H = new Object();
  private volatile boolean I;
  private volatile boolean J = false;
  private List K = new ArrayList();
  volatile long a = 0L;
  private h b = null;
  private h c = null;
  private h d = null;
  private List e = new ArrayList();
  private List f = new ArrayList();
  private a h = null;
  private int j = 0;
  private boolean k = false;
  private b l = null;
  private cloudwns.j.n m = null;
  private boolean n = false;
  private PowerManager.WakeLock o = null;
  private Object p = null;
  private ConcurrentLinkedQueue q = new ConcurrentLinkedQueue();
  private long r = 0L;
  private boolean s = true;
  private int t = 0;
  private long u = 0L;
  private boolean v = false;
  private String w = null;
  private long x = 5L;
  private long y = 0L;
  private l.a z = null;

  static
  {
    D = false;
  }

  private l()
  {
    super("SessionManager");
    long l1 = System.currentTimeMillis();
    start();
    i = new l.b(this, getLooper());
    this.w = Option.getString("debug_ip", null);
    Object localObject;
    if (this.w == null)
      localObject = d.b();
    while (true)
    {
      this.h = ((a)localObject);
      this.m = new cloudwns.j.n();
      d(0);
      this.k = false;
      this.p = new Object();
      WnsGlobal.a(this.F);
      this.z = new l.a(this, null);
      try
      {
        Global.registerReceiver(this.z, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.A = ((ConnectivityManager)Global.getSystemService("connectivity"));
        com.tencent.wns.network.a.a().b();
        long l2 = System.currentTimeMillis();
        i.a("SessionManager init  cost=" + (l2 - l1));
        return;
        localObject = new ad(this.w);
      }
      catch (Exception localException)
      {
        while (true)
          cloudwns.l.a.c("SessionManager", "Get CONNECTIVITY_SERVICE fail", localException);
      }
    }
  }

  private void A()
  {
    ac.a().a(i, this.e);
  }

  private void B()
  {
    this.u = SystemClock.elapsedRealtime();
    if (i != null)
      i.post(new r(this));
  }

  private void C()
  {
    if (i != null)
      i.post(new s(this));
  }

  private void D()
  {
    if ((this.l != null) && (this.c != null))
      this.l.j();
  }

  private boolean E()
  {
    if ((this.c != null) && (this.c.f().f() == 1));
    for (int i1 = 0; ; i1 = 1)
    {
      if ((i1 != 0) && (this.d != null) && (this.d.f().f() == 1))
        return false;
      return i1;
    }
  }

  public static l a()
  {
    monitorenter;
    try
    {
      if (g == null)
        g = new l();
      l locall = g;
      return locall;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void a(int paramInt, long paramLong)
  {
    if (!this.J);
    long l1;
    do
    {
      return;
      l1 = System.currentTimeMillis();
    }
    while (((paramInt == 1) && (this.a > 0L) && (l1 - this.a < 1800000L)) || (i == null));
    i.postDelayed(new v(this, paramInt), paramLong);
  }

  private void a(g paramg)
  {
    ac.a().a(paramg, this.r, i);
  }

  private void a(g paramg, int paramInt)
  {
    ac.a().a(paramg, paramInt, this.r, i, this.E, this.l);
  }

  private void a(g paramg1, g paramg2)
  {
    ac.a().a(paramg1, paramg2, this.r);
  }

  private void a(g[] paramArrayOfg)
  {
    if ((paramArrayOfg == null) || (paramArrayOfg.length == 0));
    do
    {
      return;
      if (D)
        continue;
      D = true;
      return;
    }
    while (!ad.class.equals(this.h.getClass()));
    paramArrayOfg = paramArrayOfg[0];
    paramArrayOfg = Toast.makeText(Global.getContext(), "\n正在使用测试环境(" + paramArrayOfg.b() + ":" + paramArrayOfg.c() + ")\n", 1);
    paramArrayOfg.setGravity(17, 0, 0);
    paramArrayOfg.show();
  }

  private boolean a(long paramLong, h paramh, int paramInt, boolean paramBoolean, byte paramByte)
  {
    if (paramh == null);
    Object localObject;
    do
    {
      return false;
      cloudwns.l.a.c("SessionManager", "sendHeartBeat uin = " + paramLong + ",session = " + paramh + "  ,scene:" + paramByte);
      localObject = paramh.f();
    }
    while (localObject == null);
    byte b3 = (byte)((g)localObject).a();
    byte b1 = f.a.a();
    int i1;
    if (cloudwns.d.e.l())
    {
      b1 = f.a(cloudwns.d.e.c().b().a());
      i1 = paramInt;
      if (paramInt == 0)
        i1 = (int)cloudwns.i.a.a().e().a("HeartbeatTimeout", 60000L);
      if (this.E != WnsGlobal.a.a)
        break label214;
    }
    label214: for (byte b2 = 0; ; b2 = 1)
    {
      localObject = new cloudwns.j.e(paramLong, b3, b1, b2, false, paramByte);
      ((cloudwns.j.e)localObject).a(i1, paramBoolean);
      ((cloudwns.j.e)localObject).a(5);
      ((cloudwns.j.e)localObject).a(new aa(this, (cloudwns.j.e)localObject, paramByte));
      return paramh.a((k)localObject);
      if (!cloudwns.d.e.m())
        break;
      b1 = f.e.a();
      break;
    }
  }

  private boolean a(h paramh)
  {
    Iterator localIterator = this.f.iterator();
    while (localIterator.hasNext())
      if (paramh == (h)localIterator.next())
        return true;
    return false;
  }

  private boolean b(h paramh)
  {
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
      if (paramh == (h)localIterator.next())
        return true;
    return false;
  }

  private void c(h paramh)
  {
    ac.a().a(paramh);
  }

  private boolean c(k paramk)
  {
    if (paramk == null);
    do
    {
      return false;
      if (!cloudwns.d.e.a())
      {
        paramk.b(519, "network disable");
        return false;
      }
      this.r = paramk.r();
      x();
    }
    while (i == null);
    if (this.j == 0)
      c(paramk.r());
    return i.post(new p(this, paramk));
  }

  private void d(int paramInt)
  {
    cloudwns.l.a.c("SessionManager", "setState mState = " + this.j + ",newState = " + paramInt);
    if (this.j != paramInt)
    {
      int i1 = this.j;
      this.j = paramInt;
      if (this.l != null)
        this.l.a(i1, this.j);
    }
  }

  private void d(h paramh)
  {
    this.c = paramh;
    D();
  }

  private void d(h paramh, int paramInt)
  {
    f(paramh, paramInt);
    Object localObject;
    if (!k())
      localObject = this.h.a(paramh.f(), paramInt);
    while (localObject == null)
    {
      paramh.d();
      localObject = this.e.iterator();
      while (true)
        if (((Iterator)localObject).hasNext())
        {
          if (paramh != (h)((Iterator)localObject).next())
            continue;
          ((Iterator)localObject).remove();
          continue;
          cloudwns.l.a.e("SessionManager", "protection acc svr, so stop get new server profile.");
          localObject = null;
          break;
        }
      if (this.e.isEmpty())
      {
        if (this.b == null)
          break label150;
        d(this.b);
        this.b = null;
        d(3);
        a(0);
        if (this.c != null)
          a(this.c.f());
      }
      return;
      label150: d(0);
      if ((this.k) && (cloudwns.d.e.a()))
      {
        d();
        return;
      }
      a(516);
      return;
    }
    paramInt = 0;
    label184: if (paramInt < localObject.length)
      if (localObject[paramInt] != null)
        break label203;
    while (true)
    {
      paramInt += 1;
      break label184;
      break;
      label203: if (paramInt == 0)
      {
        paramh.a(this.r, localObject[paramInt], true);
        continue;
      }
      h localh = new h();
      this.e.add(localh);
      localh.a(this.r, localObject[paramInt], true);
    }
  }

  private void e(int paramInt)
  {
    ac.a().a(paramInt, this.c, this.r, this.u);
  }

  private void e(long paramLong)
  {
    a(1, paramLong);
  }

  private boolean e(h paramh)
  {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramh != null)
    {
      paramh = paramh.f();
      bool1 = bool2;
      if (paramh != null)
      {
        bool1 = bool2;
        if (paramh.f() == 2)
        {
          paramh.b(1);
          h localh = new h();
          this.K.add(localh);
          bool1 = localh.a(this.r, paramh, false);
        }
      }
    }
    return bool1;
  }

  private boolean e(h paramh, int paramInt)
  {
    g localg1 = null;
    Object localObject = null;
    if (paramh == null)
      return false;
    switch (this.j)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return true;
      this.b = paramh;
      this.e.remove(paramh);
      d(2);
      localObject = paramh.f();
      if (((g)localObject).f() == 2)
        n();
      while (true)
      {
        if (this.h != null)
          this.h.a(paramh.f());
        if (paramInt != 0)
        {
          localObject = new h();
          this.e.add(localObject);
          ((h)localObject).a(this.r, paramh.g(), false);
          cloudwns.l.a.b("SessionManager", "updateSession open redict Session:" + paramh.g());
        }
        c(paramh);
        if (!this.e.isEmpty())
          break;
        if (this.b != null)
          d(this.b);
        this.b = null;
        d(3);
        a(0);
        if (this.c != null)
          a(this.c.f());
        e(0L);
        break;
        if (((g)localObject).f() != 1)
          continue;
        o();
      }
      int i1;
      if (this.b == null)
      {
        cloudwns.l.a.e("SessionManager", "updateSession in temp session state,but tempsession == null!!!");
        this.b = paramh;
        localObject = paramh;
        i1 = paramInt;
        if (this.h != null)
        {
          this.h.a(paramh.f());
          i1 = paramInt;
          localObject = paramh;
        }
      }
      while (true)
      {
        if (i1 != 0)
        {
          paramh = new h();
          this.e.add(paramh);
          paramh.a(this.r, ((h)localObject).g(), false);
          cloudwns.l.a.b("SessionManager", "updateSession open redict Session:" + ((h)localObject).g());
        }
        if (!this.e.isEmpty())
          break;
        if (this.b != null)
          d(this.b);
        this.b = null;
        d(3);
        a(0);
        if (this.c != null)
          a(this.c.f());
        e(0L);
        break;
        localObject = paramh.f();
        if (((g)localObject).b(this.b.f()))
        {
          if ((paramh.n() == true) && (!this.b.n()))
          {
            cloudwns.l.a.d("SessionManager", "new session isCrossOpr = " + paramh.n() + ",old session isCrossOpr = " + this.b.n() + ",so use old one!");
            this.e.remove(paramh);
            paramh.d();
            a(this.b.f(), (g)localObject);
            i1 = 0;
            localObject = null;
            continue;
          }
          if (this.b != paramh)
            this.f.add(this.b);
          this.b = paramh;
          this.e.remove(paramh);
          d(2);
          localObject = paramh.f();
          if (((g)localObject).f() == 2)
            n();
          while (true)
          {
            if (this.h != null)
              this.h.a(paramh.f());
            c(paramh);
            localObject = paramh;
            i1 = paramInt;
            break;
            if (((g)localObject).f() != 1)
              continue;
            o();
          }
        }
        this.e.remove(paramh);
        paramh.d();
        i1 = 0;
        localObject = null;
      }
      if (this.c == paramh)
        cloudwns.l.a.e("SessionManager", "updateSession in single session state,but session == mMasterSession again，sholud error happened before");
      while (true)
      {
        c(paramh);
        a(paramh.f());
        break;
        if (this.d == paramh)
        {
          d(4);
          continue;
        }
        localg1 = paramh.f();
        if (this.c != null)
          localObject = this.c.f();
        if (!localg1.b((g)localObject))
          continue;
        localObject = this.c;
        d(paramh);
        this.f.add(localObject);
      }
      if ((this.c != paramh) && (this.d != paramh))
      {
        g localg2 = paramh.f();
        localObject = localg1;
        if (this.c != null)
          localObject = this.c.f();
        if (localg2.b((g)localObject))
        {
          localObject = this.c;
          d(paramh);
          this.f.add(localObject);
        }
      }
      cloudwns.l.a.e("SessionManager", "updateSession in dual session state,but other session return!!!");
      c(paramh);
      a(paramh.f());
      continue;
      this.f.add(this.c);
      d(paramh);
      this.d = null;
      d(3);
      cloudwns.l.a.d("SessionManager", "updateSession in detect_session_state");
      c(paramh);
      a(paramh.f());
      e(552);
    }
  }

  private void f(h paramh, int paramInt)
  {
    ac.a().a(paramh, paramInt);
  }

  private void j()
  {
    this.C = (cloudwns.i.a.a().e().a("OverLoadInterval", 180000L) + System.currentTimeMillis());
    cloudwns.l.a.e("SessionManager", "reset mQuietPeriodTs = " + this.C);
  }

  private boolean k()
  {
    return System.currentTimeMillis() <= this.C;
  }

  private h l()
  {
    switch (this.j)
    {
    default:
      return null;
    case 0:
    case 1:
    case 5:
      return null;
    case 2:
      return this.b;
    case 3:
      if ((this.c != null) && (this.c.j() > 3) && (this.d == null))
      {
        cloudwns.l.a.d("SessionManager", "start slaver session");
        this.d = new h();
        g localg = this.c.f();
        if (localg != null)
          this.d.a(this.r, localg, false);
        z();
      }
      return this.c;
    case 4:
    }
    if ((this.d == null) || (this.c == null))
      return this.c;
    int i1;
    if (this.d.m())
    {
      i1 = this.d.j();
      if (!this.c.m())
        break label208;
    }
    label208: for (int i2 = this.c.j(); ; i2 = 2147483647)
    {
      if ((i1 != i2) || (i1 != 2147483647))
        break label215;
      return null;
      i1 = 2147483647;
      break;
    }
    label215: if (i1 < i2)
      return this.d;
    return this.c;
  }

  private h m()
  {
    switch (this.j)
    {
    case 0:
    case 1:
    case 5:
    default:
      return null;
    case 2:
      return this.b;
    case 3:
      return this.c;
    case 4:
    }
    return this.c;
  }

  private void n()
  {
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      h localh = (h)localIterator.next();
      if ((localh == null) || (localh.f().f() != 2))
        continue;
      localIterator.remove();
      this.f.add(localh);
    }
  }

  private void o()
  {
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      h localh = (h)localIterator.next();
      localIterator.remove();
      this.f.add(localh);
    }
  }

  private void p()
  {
    Iterator localIterator = this.f.iterator();
    while (localIterator.hasNext())
    {
      h localh = (h)localIterator.next();
      if (!localh.k())
        continue;
      cloudwns.l.a.d("SessionManager", "abandon session weight =0 ,so close it");
      localIterator.remove();
      localh.d();
    }
  }

  private void q()
  {
    i.removeMessages(4);
    i.sendEmptyMessageDelayed(4, 2000L);
  }

  private void r()
  {
    i.removeMessages(4);
  }

  private void s()
  {
    if (i != null)
      i.post(new z(this));
  }

  private void t()
  {
    ConcurrentLinkedQueue localConcurrentLinkedQueue = new ConcurrentLinkedQueue();
    Iterator localIterator = this.q.iterator();
    k localk;
    while (localIterator.hasNext())
    {
      localk = (k)localIterator.next();
      if ((localk == null) || (!localk.y()))
        continue;
      localConcurrentLinkedQueue.add(localk);
    }
    localIterator = localConcurrentLinkedQueue.iterator();
    while (localIterator.hasNext())
    {
      localk = (k)localIterator.next();
      cloudwns.l.a.e("SessionManager", "cacheRequest wait time out command = " + localk.w() + " seqNo = " + localk.E());
      this.q.remove(localk);
      if (localk == null)
        continue;
      localk.a(Integer.valueOf(514));
      localk.b(514, "write time out");
    }
    localConcurrentLinkedQueue.clear();
  }

  private void u()
  {
    ConcurrentLinkedQueue localConcurrentLinkedQueue = new ConcurrentLinkedQueue();
    Iterator localIterator = this.q.iterator();
    while (localIterator.hasNext())
      localConcurrentLinkedQueue.add((k)localIterator.next());
    localIterator = localConcurrentLinkedQueue.iterator();
    while (localIterator.hasNext())
    {
      k localk = (k)localIterator.next();
      this.q.remove(localk);
      if (localk == null)
        continue;
      localk.b(514, "write time out");
      cloudwns.l.a.e("SessionManager", "cacheRequest wait time out command = " + localk.w() + " seqNo = " + localk.E());
    }
    localConcurrentLinkedQueue.clear();
  }

  private boolean v()
  {
    h localh = l();
    if (localh == null)
    {
      cloudwns.l.a.e("SessionManager", "sendCacheRequest session == null impossible!!!");
      return false;
    }
    g();
    if (this.l != null)
      this.l.i();
    cloudwns.l.a.c("SessionManager", "sendCacheRequest size = " + this.q.size());
    Iterator localIterator = this.q.iterator();
    while (localIterator.hasNext())
    {
      k localk = (k)localIterator.next();
      if (localk == null)
      {
        localIterator.remove();
        continue;
      }
      B2Ticket localB2Ticket = cloudwns.g.b.b(localk.r());
      if ((localk.w() != "wnscloud.login") && ((localB2Ticket == null) || (localB2Ticket.b() == null)) && (localk.r() != 999L) && (localk.w() != "wnscloud.getuid") && (localk.w() != "wnscloud.getwid"))
        continue;
      int i1 = localk.i() - (int)(System.currentTimeMillis() - localk.k());
      long l1 = cloudwns.i.a.a().e().a("RequestTimeout", 60000L) / 2L;
      if (i1 < l1)
        localk.a(l1 - i1);
      if (this.x < 5L)
      {
        this.x += 1L;
        localk.b(localk.i() / 2);
      }
      localh.a(localk);
      localIterator.remove();
    }
    return true;
  }

  private void w()
  {
    synchronized (this.p)
    {
      try
      {
        if (this.o != null)
        {
          cloudwns.l.a.d("SessionManager", "Wakelock RELEASED :)");
          this.o.release();
          this.o = null;
        }
        return;
      }
      catch (Exception localException)
      {
        while (true)
        {
          cloudwns.l.a.c("SessionManager", "releaseWakeLock exception", localException);
          this.o = null;
        }
      }
    }
  }

  private void x()
  {
    if (this.E == WnsGlobal.a.a);
    while (true)
    {
      return;
      if (i == null)
        continue;
      i.removeMessages(10);
      synchronized (this.p)
      {
        try
        {
          Context localContext = Global.getApplicationContext();
          if ((localContext != null) && (this.o == null))
          {
            cloudwns.l.a.d("SessionManager", "Wakelock ACQUIRED :)");
            this.o = ((PowerManager)localContext.getApplicationContext().getSystemService("power")).newWakeLock(1, "wns");
            this.o.acquire();
          }
          if (i == null)
            continue;
          i.sendEmptyMessageDelayed(10, 3000L);
          return;
        }
        catch (Exception localException)
        {
          while (true)
            cloudwns.l.a.c("SessionManager", "acquireWakeLock exception", localException);
        }
      }
    }
  }

  private void y()
  {
    int i2;
    int i1;
    if (this.E != WnsGlobal.a.a)
    {
      i2 = 0;
      i1 = i2;
      if (cloudwns.i.a.a().e().a("EnableWakeLockDelay", 0L) != 0L)
      {
        if ((this.b == null) || (this.b.k()))
          break label78;
        i1 = 1;
      }
    }
    while (true)
    {
      if ((this.j == 1) || (this.j == 2))
        i1 = 1;
      if (i1 != 0)
        x();
      return;
      label78: if ((this.c != null) && (!this.c.k()))
      {
        i1 = 1;
        continue;
      }
      i1 = i2;
      if (this.d == null)
        continue;
      i1 = i2;
      if (this.d.k())
        continue;
      i1 = 1;
    }
  }

  private void z()
  {
    ac.a().a(i);
  }

  public void a(long paramLong, byte paramByte)
  {
    x();
    this.r = paramLong;
    this.t = 0;
    if (this.j == 0)
      c(paramLong);
    do
    {
      return;
      a(paramLong, m(), 0, true, paramByte);
    }
    while ((this.E != WnsGlobal.a.b) || (this.j != 4));
    cloudwns.l.a.d("SessionManager", "sendHeartBeat under background to close SlaverSession");
    s();
  }

  public void a(long paramLong1, String paramString1, int paramInt, long paramLong2, String paramString2)
  {
  }

  public void a(b paramb)
  {
    this.l = paramb;
  }

  public void a(boolean paramBoolean)
  {
    this.J = paramBoolean;
  }

  public boolean a(int paramInt)
  {
    if ((this.v == true) && (this.w != null))
    {
      this.v = false;
      this.h = new ad(this.w);
      d();
      return true;
    }
    if (paramInt != 0)
    {
      u();
      r();
      cloudwns.l.b.a().a("[跑马][失败] code=" + paramInt + ", errMsg=" + WnsError.getErrorMessage(paramInt));
      g localg = null;
      if (this.c != null)
        localg = this.c.f();
      a(localg, paramInt);
    }
    while (true)
    {
      this.k = false;
      if (this.l != null)
        break;
      return false;
      cloudwns.l.b.a().a("[跑马][成功][" + this.c.f().g() + "]");
    }
    return this.l.a(this.r, paramInt);
  }

  public boolean a(int paramInt1, int paramInt2)
  {
    if (i == null)
      return false;
    Message localMessage = i.obtainMessage(12);
    localMessage.arg1 = paramInt1;
    localMessage.arg2 = paramInt2;
    return i.sendMessage(localMessage);
  }

  public boolean a(int paramInt, String paramString, Object paramObject)
  {
    if (i == null)
      return false;
    Message localMessage = i.obtainMessage(8);
    localMessage.arg1 = paramInt;
    localMessage.obj = paramObject;
    if (paramString != null)
      localMessage.getData().putString("ERROR_MSG", paramString);
    return i.sendMessage(localMessage);
  }

  public boolean a(long paramLong)
  {
    Long localLong = Long.valueOf(System.currentTimeMillis());
    if (localLong.longValue() - this.B >= 600000L)
    {
      this.B = localLong.longValue();
      return b(paramLong);
    }
    return false;
  }

  public boolean a(long paramLong, j.a parama, String paramString)
  {
    int i1 = (int)cloudwns.i.a.a().e().a("RequestTimeout", 60000L);
    parama = new j(paramLong, parama, paramString, false);
    parama.b(i1);
    return c(parama);
  }

  public boolean a(long paramLong, ArrayList paramArrayList)
  {
    int i1 = (int)cloudwns.i.a.a().e().a("RequestTimeout", 60000L);
    paramArrayList = new cloudwns.j.o(paramLong, paramArrayList);
    paramArrayList.b(i1);
    paramArrayList.a(new n(this));
    return c(paramArrayList);
  }

  public boolean a(long paramLong, boolean paramBoolean)
  {
    return true;
  }

  public boolean a(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    cloudwns.l.a.d("SessionManager", "simpleLogOff uin = " + paramLong + ", bSend = " + paramBoolean1);
    if (!paramBoolean1)
    {
      if (this.b != null)
        this.b.c();
      if (this.c != null)
        this.c.c();
      if (this.d != null)
        this.d.c();
      return true;
    }
    return a(paramLong, paramBoolean2);
  }

  public boolean a(long paramLong, byte[] paramArrayOfByte1, String paramString, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, byte paramByte, cloudwns.j.g paramg, byte[] paramArrayOfByte2)
  {
    paramArrayOfByte1 = new cloudwns.j.t(paramLong, paramArrayOfByte1, paramString, paramBoolean1, paramBoolean2, paramInt1, paramInt2, paramg, paramArrayOfByte2);
    paramArrayOfByte1.b(paramInt2);
    paramArrayOfByte1.a(paramByte);
    return c(paramArrayOfByte1);
  }

  public boolean a(k paramk)
  {
    return c(paramk);
  }

  public boolean a(h paramh, int paramInt)
  {
    if (i == null)
      return false;
    paramh = i.obtainMessage(1, paramh);
    paramh.arg1 = paramInt;
    return i.sendMessage(paramh);
  }

  public boolean a(Object paramObject, int paramInt)
  {
    if ((i == null) || (paramObject == null))
      return false;
    paramObject = i.obtainMessage(5, paramObject);
    paramObject.arg1 = paramInt;
    return i.sendMessage(paramObject);
  }

  public boolean a(String paramString)
  {
    if ((i != null) && ("wnscloud.handshake".equals(paramString)))
      return i.sendEmptyMessage(13);
    return false;
  }

  public void b(String paramString)
  {
    cloudwns.l.a.c("SessionManager", "switchDebugServer debugServer = " + paramString);
    if (((paramString != null) && (paramString.equals(this.w))) || (paramString == this.w))
    {
      cloudwns.l.a.c("SessionManager", "switchDebugServer to the same ip,drop it");
      return;
    }
    this.w = paramString;
    Option.putString("debug_ip", this.w).commit();
    if (this.w != null)
    {
      i.post(new q(this));
      return;
    }
    this.v = false;
    this.h = d.b();
    b(this.r);
  }

  public boolean b()
  {
    int i1 = 1;
    if ((this.j == 0) || (this.j == 1))
      i1 = 0;
    return i1;
  }

  public boolean b(int paramInt)
  {
    return i.post(new y(this, paramInt));
  }

  public boolean b(long paramLong)
  {
    if (!cloudwns.d.e.a())
    {
      cloudwns.l.a.c("SessionManager", "can not forceOpen session, network is not available.");
      return false;
    }
    cloudwns.l.a.c("SessionManager", "forceOpen session, uin = " + paramLong);
    this.r = paramLong;
    if (i == null)
    {
      cloudwns.l.a.c("SessionManager", "can not forceOpen session, mHandler == null.");
      return false;
    }
    x();
    return i.post(new w(this));
  }

  public boolean b(long paramLong, byte paramByte)
  {
    int i1 = (int)cloudwns.i.a.a().e().a("RequestTimeout", 60000L);
    cloudwns.j.b localb = new cloudwns.j.b(paramLong, paramByte);
    localb.b(i1);
    localb.a(new o(this));
    return c(localb);
  }

  public boolean b(k paramk)
  {
    if ((i == null) || (paramk == null))
      return false;
    paramk = i.obtainMessage(9, paramk);
    return i.sendMessage(paramk);
  }

  public boolean b(h paramh, int paramInt)
  {
    if (i == null)
      return false;
    paramh = i.obtainMessage(2, paramh);
    paramh.arg1 = paramInt;
    return i.sendMessage(paramh);
  }

  public int c()
  {
    return this.j;
  }

  public void c(int paramInt)
  {
    cloudwns.l.a.d("SessionManager", "close nReason = " + paramInt);
    d(0);
    if (this.b != null)
    {
      this.b.d(paramInt);
      this.b = null;
    }
    if (this.c != null)
    {
      this.c.d(paramInt);
      d(null);
    }
    if (this.d != null)
    {
      this.d.d(paramInt);
      this.d = null;
    }
  }

  public boolean c(long paramLong)
  {
    if (!cloudwns.d.e.a())
    {
      cloudwns.l.a.c("SessionManager", "can not open session, network is not available.");
      return false;
    }
    if (this.j != 0)
    {
      cloudwns.l.a.c("SessionManager", "can not open session, mState is not NO_SESSION_STATE.");
      return false;
    }
    cloudwns.l.a.c("SessionManager", "open session, uin = " + paramLong);
    this.r = paramLong;
    if (i == null)
    {
      cloudwns.l.a.c("SessionManager", "can not open session, mHandler == null.");
      return false;
    }
    return i.post(new x(this));
  }

  public boolean c(h paramh, int paramInt)
  {
    if (i == null)
      return false;
    paramh = i.obtainMessage(6, paramh);
    paramh.arg1 = paramInt;
    return i.sendMessage(paramh);
  }

  public void d()
  {
    cloudwns.l.a.c("SessionManager", "open session, internalOpen with mState = " + this.j);
    if (k())
      cloudwns.l.a.e("SessionManager", "protection of the acc svr, does not allow open session.");
    Object localObject1;
    Object localObject2;
    do
    {
      do
      {
        return;
        q();
        switch (this.j)
        {
        default:
          cloudwns.l.a.e("SessionManager", "internalOpen wrong state = " + this.j);
          return;
        case 0:
          localObject1 = this.h.a(this.n);
        case 1:
        case 2:
        case 3:
        case 4:
        }
      }
      while (localObject1 == null);
      a(localObject1);
      cloudwns.l.b.a().c();
      this.e.clear();
      this.f.clear();
      i1 = 0;
      if (i1 < localObject1.length)
      {
        if (localObject1[i1] == null);
        while (true)
        {
          i1 += 1;
          break;
          localObject2 = new h();
          this.e.add(localObject2);
          ((h)localObject2).a(this.r, localObject1[i1], true);
        }
      }
      d(1);
      this.k = false;
      z();
      return;
      this.k = true;
      cloudwns.l.a.c("SessionManager", "internalOpen cache open reqeust in mState = " + this.j);
      return;
      cloudwns.l.a.c("SessionManager", "internalOpen in mState = " + this.j);
      localObject2 = this.h.a(this.n);
    }
    while (localObject2 == null);
    a(localObject2);
    cloudwns.l.b.a().c();
    this.e.clear();
    this.f.clear();
    if (this.c != null)
      this.c.e();
    if (this.d != null)
      this.d.e();
    if ((localObject2.length == 1) && (this.d != null))
    {
      this.d.d();
      this.d = null;
    }
    int i1 = 0;
    label395: if (i1 < localObject2.length)
    {
      if (i1 != 0)
        break label482;
      if (this.c == null)
        break label471;
      localObject1 = this.c;
      d(null);
    }
    while (true)
    {
      if (localObject1 != null)
      {
        this.e.add(localObject1);
        ((h)localObject1).a(this.r, localObject2[i1], true);
      }
      d(1);
      this.k = false;
      z();
      i1 += 1;
      break label395;
      break;
      label471: localObject1 = new h();
      continue;
      label482: if (i1 == 1)
      {
        if (this.d != null)
        {
          localObject1 = this.d;
          this.d = null;
          continue;
        }
        localObject1 = new h();
        continue;
      }
      localObject1 = new h();
    }
  }

  public boolean d(long paramLong)
  {
    long l1 = System.currentTimeMillis();
    int i1 = (int)cloudwns.i.a.a().e().a("PingRequestInterval", 60000L);
    if (l1 - this.y <= i1)
      return false;
    this.y = l1;
    i1 = (int)cloudwns.i.a.a().e().a("PingRequestTimeout", 30000L);
    cloudwns.j.h localh = new cloudwns.j.h(paramLong);
    localh.b(true);
    localh.b(i1);
    localh.a(new ab(this));
    return c(localh);
  }

  public boolean e()
  {
    int i2 = 1;
    int i1;
    if (i == null)
      i1 = 0;
    do
    {
      do
      {
        return i1;
        i1 = i2;
      }
      while (this.E == WnsGlobal.a.a);
      i1 = i2;
    }
    while (cloudwns.i.a.a().e().a("EnableWakeLockDelay", 0L) != 0L);
    return i.sendEmptyMessage(10);
  }

  public void f()
  {
    cloudwns.l.a.d("SessionManager", "close");
    d(0);
    if (this.b != null)
    {
      this.b.d();
      this.b = null;
    }
    if (this.c != null)
    {
      this.c.d();
      d(null);
    }
    if (this.d != null)
    {
      this.d.d();
      this.d = null;
    }
  }

  public void g()
  {
    Object localObject8 = new ArrayList();
    Object localObject7 = new ArrayList();
    Object localObject6 = new ArrayList();
    Object localObject5 = new ArrayList();
    Object localObject4 = new ArrayList();
    Object localObject3 = new ArrayList();
    Object localObject2 = new ArrayList();
    Object localObject1 = new ArrayList();
    Iterator localIterator = this.q.iterator();
    while (localIterator.hasNext())
    {
      k localk = (k)localIterator.next();
      if (localk == null)
      {
        localIterator.remove();
        continue;
      }
      switch (localk.F())
      {
      default:
        ((ArrayList)localObject1).add(localk);
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      }
      while (true)
      {
        localIterator.remove();
        break;
        ((ArrayList)localObject8).add(localk);
        continue;
        ((ArrayList)localObject7).add(localk);
        continue;
        ((ArrayList)localObject6).add(localk);
        continue;
        ((ArrayList)localObject5).add(localk);
        continue;
        ((ArrayList)localObject4).add(localk);
        continue;
        ((ArrayList)localObject3).add(localk);
        continue;
        ((ArrayList)localObject2).add(localk);
      }
    }
    localObject8 = ((ArrayList)localObject8).iterator();
    while (((Iterator)localObject8).hasNext())
      this.q.add(((Iterator)localObject8).next());
    localObject7 = ((ArrayList)localObject7).iterator();
    while (((Iterator)localObject7).hasNext())
      this.q.add(((Iterator)localObject7).next());
    localObject6 = ((ArrayList)localObject6).iterator();
    while (((Iterator)localObject6).hasNext())
      this.q.add(((Iterator)localObject6).next());
    localObject5 = ((ArrayList)localObject5).iterator();
    while (((Iterator)localObject5).hasNext())
      this.q.add(((Iterator)localObject5).next());
    localObject4 = ((ArrayList)localObject4).iterator();
    while (((Iterator)localObject4).hasNext())
      this.q.add(((Iterator)localObject4).next());
    localObject3 = ((ArrayList)localObject3).iterator();
    while (((Iterator)localObject3).hasNext())
      this.q.add(((Iterator)localObject3).next());
    localObject2 = ((ArrayList)localObject2).iterator();
    while (((Iterator)localObject2).hasNext())
      this.q.add(((Iterator)localObject2).next());
    localObject1 = ((ArrayList)localObject1).iterator();
    while (((Iterator)localObject1).hasNext())
      this.q.add(((Iterator)localObject1).next());
  }

  public void h()
  {
    synchronized (this.H)
    {
      if (this.G != null)
        this.G.a();
      this.G = com.tencent.base.os.clock.e.a(1800000L, 0L, new t(this));
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.s.l
 * JD-Core Version:    0.6.0
 */