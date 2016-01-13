package cloudwns.j;

import QMF_PROTOCAL.QmfDownstream;
import QMF_PROTOCAL.QmfTokenInfo;
import android.text.TextUtils;
import cloudwns.d.j;
import cloudwns.d.l;
import cloudwns.g.b;
import cloudwns.q.h;
import cloudwns.w.a.a;
import com.qq.jce.wup.UniAttribute;
import com.tencent.base.util.h.a;
import com.tencent.wns.data.A2Ticket;
import com.tencent.wns.data.B2Ticket;
import com.tencent.wns.data.Client;
import com.tencent.wns.data.i;
import com.tencent.wns.service.WnsGlobal;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class k
{
  public static String q;
  public static String r;
  public static final String[] s;
  private static AtomicInteger t = new AtomicInteger(1);
  private long A = 0L;
  private long B = 0L;
  private long C = 0L;
  private long D = 0L;
  private long E = 0L;
  private long F = 0L;
  private long G = 0L;
  private int H = 0;
  private int I = 0;
  private boolean J = false;
  private cloudwns.i.g K = null;
  private int L = 0;
  private String M = null;
  private int N = 0;
  private byte O = 0;
  private a.a P = a.a.b;
  private String Q = "";
  private String R = "";
  private final int S = 60000;
  private boolean T = false;
  private int U = 0;
  private boolean a = true;
  private long b = 0L;
  private int c = t.getAndIncrement();
  u d = new u();
  protected QmfDownstream e = null;
  protected int f = 0;
  protected g g = null;
  protected boolean h = false;
  protected q i = new q();
  protected int j = 0;
  protected byte[] k;
  protected A2Ticket l = null;
  protected B2Ticket m = null;
  protected int n = 0;
  protected String o;
  protected String p;
  private UniAttribute u = null;
  private int v = 0;
  private boolean w = false;
  private boolean x = true;
  private boolean y = true;
  private long z = 0L;

  static
  {
    q = "Statistic.Request.Counter";
    r = "[Session No:%d] [S:%d] [C:%s |ALL = %dms |INIT = %dms |QUEUE = %dms|SENT = %dms|NETWORK = %dms |RECV = %dms |REQ_SIZE = %db |RSP SIZE = %db]";
    s = new String[] { "WNS.", "QMFSERVICE." };
  }

  public k(long paramLong)
  {
    this(paramLong, null);
  }

  public k(long paramLong, String paramString)
  {
    if (this.K != null)
    {
      this.H = (int)this.K.a("SendTimeout");
      this.I = (int)this.K.a("RecvTimeout");
      this.z = System.currentTimeMillis();
      this.F = System.currentTimeMillis();
    }
    this.u = new UniAttribute();
    if (this.d != null)
    {
      g(paramLong);
      this.d.a(this.c);
    }
    this.p = paramString;
    if (TextUtils.isEmpty(this.p))
      this.p = h.a;
    this.o = b.e(paramLong);
    I();
  }

  private void I()
  {
    d(WnsGlobal.a().a());
  }

  private boolean a(k paramk)
  {
    if (paramk == null);
    do
      return false;
    while (c(paramk.w()));
    return true;
  }

  private boolean c(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0));
    while (true)
    {
      return false;
      paramString = paramString.toUpperCase();
      String[] arrayOfString = s;
      int i2 = arrayOfString.length;
      int i1 = 0;
      while (i1 < i2)
      {
        if (paramString.startsWith(arrayOfString[i1]))
          return true;
        i1 += 1;
      }
    }
  }

  public static void d(int paramInt)
  {
    p.c(paramInt);
  }

  public static int s()
  {
    return p.b();
  }

  public static String t()
  {
    return p.d();
  }

  public boolean A()
  {
    return this.J;
  }

  public void B()
  {
    if (this.i != null)
    {
      this.i.c();
      this.i.b();
    }
  }

  public boolean C()
  {
    return this.x;
  }

  public boolean D()
  {
    return this.y;
  }

  public int E()
  {
    return this.c;
  }

  public byte F()
  {
    return this.O;
  }

  public boolean G()
  {
    return this.T;
  }

  public int H()
  {
    return this.n;
  }

  public void a(byte paramByte)
  {
    this.O = paramByte;
  }

  public void a(int paramInt)
  {
    this.N = paramInt;
  }

  void a(int paramInt, String paramString)
  {
    cloudwns.l.a.e("Request", String.format("[S:%d] ", new Object[] { Integer.valueOf(E()) }) + "Request" + " failed errCode = " + paramInt + ", errMsg=" + paramString);
    if (this.g != null)
      this.g.a(z(), paramInt, paramString);
    a(w(), Integer.valueOf(paramInt), "protocol = " + j());
  }

  public void a(int paramInt, boolean paramBoolean)
  {
    int i1 = paramInt;
    if (paramInt <= 0)
      i1 = 60000;
    if (paramBoolean)
    {
      j localj = cloudwns.d.e.q();
      if ((localj != null) && (localj.c().equals(l.c)))
      {
        this.j = (i1 + 15000);
        return;
      }
      this.j = i1;
      return;
    }
    this.j = i1;
  }

  public void a(long paramLong)
  {
    this.j = (int)(this.j + paramLong);
  }

  abstract void a(QmfDownstream paramQmfDownstream);

  public void a(g paramg)
  {
    this.g = paramg;
  }

  protected void a(UniAttribute paramUniAttribute)
  {
  }

  public void a(Object paramObject)
  {
    String str = b();
    if (a(this))
      a(w(), paramObject, str);
  }

  public void a(String paramString)
  {
    this.M = paramString;
  }

  public void a(String paramString1, Object paramObject, String paramString2)
  {
    int i3 = 0;
    int i1 = -1;
    while (true)
    {
      try
      {
        if (!(paramObject instanceof QmfDownstream))
          continue;
        paramObject = (QmfDownstream)paramObject;
        cloudwns.f.d locald = cloudwns.f.a.a().b();
        locald.a(23, this.p);
        locald.a(9, Long.valueOf(r()));
        if (paramObject != null)
          continue;
        break label397;
        locald.a(25, Integer.valueOf(i2));
        if (i1 != 0)
          continue;
        String str = paramString1;
        if (i2 == 0)
          continue;
        str = paramString1;
        if (this.h != true)
          continue;
        str = this.i.d();
        locald.a(10, i.a().b(str));
        locald.a(11, Integer.valueOf(i1));
        if (paramObject != null)
          continue;
        locald.a(12, Long.valueOf(System.currentTimeMillis() - this.z));
        locald.a(13, Integer.valueOf(this.v));
        if (this.h)
          break label369;
        i1 = i3;
        if (this.e == null)
          continue;
        if (this.e.f != null)
          break label356;
        i1 = i3;
        locald.a(14, Integer.valueOf(i1));
        locald.a(15, g());
        locald.a(16, Integer.valueOf(h()));
        locald.a(17, paramString2);
        locald.a(18, Integer.valueOf(E()));
        cloudwns.f.a.a().a(locald);
        return;
        if (!(paramObject instanceof Integer))
          break label392;
        i1 = ((Integer)paramObject).intValue();
        paramObject = null;
        continue;
        i1 = paramObject.c;
        break label397;
        i2 = paramObject.d;
        continue;
        l1 = o() - n();
        if (l1 > 0L)
        {
          locald.a(12, Long.valueOf(l1));
          continue;
        }
      }
      catch (Exception paramString1)
      {
        cloudwns.l.a.c("Request", "statistic", paramString1);
        return;
      }
      long l1 = 0L;
      continue;
      label356: i1 = this.e.f.length;
      continue;
      label369: i1 = i3;
      if (this.i == null)
        continue;
      i1 = this.i.e();
      continue;
      label392: paramObject = null;
      continue;
      label397: if (paramObject != null)
        continue;
      int i2 = 0;
    }
  }

  public void a(boolean paramBoolean)
  {
    this.h = paramBoolean;
    if (this.h == true)
      this.i.a(w());
  }

  public void a(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    g localg;
    long l1;
    if (this.g != null)
    {
      localg = this.g;
      l1 = this.b;
      if (paramBoolean)
        break label35;
    }
    label35: for (paramBoolean = true; ; paramBoolean = false)
    {
      localg.a(l1, paramBoolean, paramArrayOfByte);
      return;
    }
  }

  abstract byte[] a();

  public byte[] a(long paramLong, boolean paramBoolean)
  {
    Object localObject3;
    try
    {
      localObject3 = a();
      a(this.u);
      Object localObject1 = null;
      if (this.d != null)
        localObject1 = this.d.a(localObject3, this.w, this.u, cloudwns.v.c.a().a(false), paramLong, paramBoolean, u(), this.p, this.o);
      if (localObject1 == null)
      {
        cloudwns.l.a.e("Request", "call createQmfUpstream fail.");
        return null;
      }
      this.f = h.a.a(this.f, 8192);
      if (!C())
        break label496;
      this.f = h.a.a(this.f, 1);
      if (this.P == a.a.b)
        this.f = h.a.a(this.f, 8);
      while (true)
      {
        localObject3 = a(localObject1, this.P);
        if (D())
          this.f = h.a.a(this.f, 2);
        this.f = h.a.a(this.f, this.O << 16);
        localObject1 = c();
        if (localObject1 != null)
          break;
        cloudwns.l.a.e("Request", "cryptor is NONE.");
        return null;
        if (this.P != a.a.c)
          continue;
        this.f = h.a.a(this.f, 4);
      }
    }
    catch (Exception localException)
    {
      cloudwns.l.a.c("Request", "busiData fail", localException);
      return null;
    }
    Object localObject2;
    if (this.d != null)
    {
      byte[] arrayOfByte;
      if (1 == localException.a())
      {
        arrayOfByte = this.m.b();
        if (arrayOfByte != null)
          this.d.b(arrayOfByte);
      }
      while (true)
      {
        localObject3 = localException.a(localObject3);
        if (localObject3 != null)
          break;
        cloudwns.l.a.e("Request", "encrypt call fail.");
        return null;
        localObject2 = new cloudwns.x.d();
        this.d.b(null);
        continue;
        if (3 == ((cloudwns.x.a)localObject2).a())
        {
          arrayOfByte = this.l.c();
          if (arrayOfByte != null)
          {
            this.d.b(arrayOfByte);
            continue;
          }
          localObject2 = new cloudwns.x.d();
          this.d.b(null);
          continue;
        }
        if (2 == ((cloudwns.x.a)localObject2).a())
        {
          this.d.b(null);
          continue;
        }
        this.d.b(null);
      }
      this.d.a('\004');
      this.d.b(this.f);
      this.d.a(((cloudwns.x.a)localObject2).a());
      this.d.a(localObject3);
      localObject2 = this.d.f();
      if (localObject2 == null)
        break label503;
    }
    label496: label503: for (int i1 = localObject2.length; ; i1 = 0)
    {
      this.v = i1;
      return localObject2;
      cloudwns.l.a.e("Request", "stream.toByteArray fail.");
      return null;
      localObject3 = localObject2;
      break;
    }
  }

  byte[] a(byte[] paramArrayOfByte, a.a parama)
  {
    parama = cloudwns.w.a.a(parama);
    if (parama == null)
    {
      cloudwns.l.a.e("Request", "NO COMPRESS METHOD!");
      return null;
    }
    paramArrayOfByte = parama.a(paramArrayOfByte);
    if (paramArrayOfByte == null)
    {
      cloudwns.l.a.e("Request", "I AM SO SORRY,MAY BE NO MEMORY!");
      return null;
    }
    return paramArrayOfByte;
  }

  public String b()
  {
    long l6;
    long l1;
    long l2;
    long l3;
    long l4;
    long l5;
    long l7;
    long l8;
    if (this.e == null)
    {
      l6 = System.currentTimeMillis();
      l1 = l() - k();
      l2 = l1;
      if (l1 < 0L)
        l2 = 0L;
      l1 = m() - l();
      l3 = l1;
      if (l1 < 0L)
        l3 = 0L;
      l1 = n() - m();
      l4 = l1;
      if (l1 < 0L)
        l4 = 0L;
      l1 = o() - n();
      l5 = l1;
      if (l1 < 0L)
        l5 = 0L;
      l7 = l6 - o();
      l1 = l7;
      if (l7 < 0L)
        l1 = 0L;
      l7 = l1;
      if (l1 > o())
        l7 = 0L;
      l1 = l6 - k();
      l6 = l1;
      if (l1 < 0L)
        l6 = System.currentTimeMillis() - k();
      l8 = this.v;
      if (this.h)
        break label340;
      if ((this.e != null) && (this.e.f != null))
        break label327;
      l1 = 0L;
    }
    while (true)
    {
      String str = String.format(r, new Object[] { Integer.valueOf(this.n), Integer.valueOf(E()), i.a().b(w()), Long.valueOf(l6), Long.valueOf(l2), Long.valueOf(l3), Long.valueOf(l4), Long.valueOf(l5), Long.valueOf(l7), Long.valueOf(l8), Long.valueOf(l1) });
      cloudwns.l.a.c(q, str);
      return str;
      l6 = p();
      break;
      label327: l1 = this.e.f.length;
      continue;
      label340: if (this.i == null)
      {
        l1 = 0L;
        continue;
      }
      l1 = this.i.e();
    }
  }

  public void b(int paramInt)
  {
    int i1 = paramInt;
    if (paramInt <= 0)
      i1 = 60000;
    j localj = cloudwns.d.e.q();
    if ((localj != null) && (localj.c().equals(l.c)))
    {
      this.j = (i1 + 15000);
      return;
    }
    this.j = i1;
  }

  public void b(int paramInt, String paramString)
  {
    cloudwns.l.a.e("Request", "notifyError : " + paramString + " request = " + this);
    a(paramInt, paramString);
  }

  public void b(long paramLong)
  {
    this.A = paramLong;
  }

  public void b(QmfDownstream paramQmfDownstream)
  {
    this.e = paramQmfDownstream;
  }

  public void b(String paramString)
  {
    if (this.d != null)
      this.d.a(paramString);
  }

  public void b(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }

  protected cloudwns.x.a c()
  {
    this.m = b.b(this.d.c());
    if (this.m != null)
    {
      if ((this.m.c() != null) && (this.m.c().length > 0))
        return new cloudwns.x.g(1, this.m.c());
      return new cloudwns.x.d();
    }
    return new cloudwns.x.d();
  }

  public void c(int paramInt)
  {
    this.L = paramInt;
  }

  public void c(long paramLong)
  {
    this.B = paramLong;
  }

  @Deprecated
  public void c(boolean paramBoolean)
  {
  }

  public void d(long paramLong)
  {
    this.C = paramLong;
  }

  public void d(boolean paramBoolean)
  {
    this.J = paramBoolean;
  }

  public boolean d()
  {
    return this.h;
  }

  public int e()
  {
    return this.I;
  }

  public void e(int paramInt)
  {
    try
    {
      if (this.i != null)
      {
        cloudwns.f.d locald = cloudwns.f.a.a().b();
        locald.a(9, Long.valueOf(r()));
        locald.a(10, this.i.d());
        locald.a(11, Integer.valueOf(paramInt));
        locald.a(12, Long.valueOf(this.i.a()));
        locald.a(13, Integer.valueOf(this.v));
        locald.a(14, Integer.valueOf(this.i.f()));
        locald.a(15, g());
        locald.a(16, Integer.valueOf(h()));
        locald.a(17, this.i.toString());
        locald.a(18, Integer.valueOf(E()));
        cloudwns.f.a.a().a(locald);
      }
      return;
    }
    catch (Exception localException)
    {
      cloudwns.l.a.c("Request", "statistic", localException);
    }
  }

  public void e(long paramLong)
  {
    this.D = paramLong;
  }

  public void e(boolean paramBoolean)
  {
    this.T = paramBoolean;
  }

  public int f()
  {
    return this.H;
  }

  public void f(int paramInt)
  {
    this.n = paramInt;
  }

  public void f(long paramLong)
  {
    this.E = paramLong;
  }

  public String g()
  {
    return this.M;
  }

  public void g(int paramInt)
  {
    if (this.i != null)
    {
      this.i.a(paramInt);
      this.i.b(paramInt);
      e(0);
    }
  }

  public void g(long paramLong)
  {
    if (this.d != null)
      this.d.a(paramLong);
  }

  public int h()
  {
    return this.N;
  }

  public void h(long paramLong)
  {
    this.G = paramLong;
  }

  public int i()
  {
    return this.j;
  }

  public void i(long paramLong)
  {
    this.b = paramLong;
  }

  public int j()
  {
    return this.L;
  }

  public long k()
  {
    return this.z;
  }

  public long l()
  {
    return this.A;
  }

  public long m()
  {
    return this.B;
  }

  public long n()
  {
    return this.C;
  }

  public long o()
  {
    return this.D;
  }

  public long p()
  {
    return this.E;
  }

  public boolean q()
  {
    return this.a;
  }

  public long r()
  {
    if (this.d != null)
      return this.d.c();
    return 0L;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" seqNO = " + this.c);
    localStringBuilder.append(" command = " + w());
    return localStringBuilder.toString();
  }

  protected QmfTokenInfo u()
  {
    if (this.k != null)
      return (QmfTokenInfo)cloudwns.v.e.a(QmfTokenInfo.class, this.k);
    int i1 = s.e.a();
    HashMap localHashMap = new HashMap(0);
    return new QmfTokenInfo(i1, new byte[] { 0, 0 }, localHashMap);
  }

  public byte[] v()
  {
    return this.k;
  }

  public String w()
  {
    if (this.d != null)
      return this.d.e();
    return "";
  }

  public QmfDownstream x()
  {
    return this.e;
  }

  public boolean y()
  {
    return System.currentTimeMillis() - this.F > this.j;
  }

  public long z()
  {
    if (this.b != 0L)
      return this.b;
    return r();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.j.k
 * JD-Core Version:    0.6.0
 */