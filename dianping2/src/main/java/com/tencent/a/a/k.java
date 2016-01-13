package com.tencent.a.a;

import com.tencent.a.c.d.a;

public abstract class k
{
  private volatile int a = 63;
  private volatile boolean b = true;
  private b c = b.a;

  public k()
  {
    this(63, true, b.a);
  }

  public k(int paramInt, boolean paramBoolean, b paramb)
  {
    a(paramInt);
    a(paramBoolean);
    a(paramb);
  }

  public void a(int paramInt)
  {
    this.a = paramInt;
  }

  protected abstract void a(int paramInt, Thread paramThread, long paramLong, String paramString1, String paramString2, Throwable paramThrowable);

  public void a(b paramb)
  {
    this.c = paramb;
  }

  public void a(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }

  public void b(int paramInt, Thread paramThread, long paramLong, String paramString1, String paramString2, Throwable paramThrowable)
  {
    if ((d()) && (d.a.a(this.a, paramInt)))
      a(paramInt, paramThread, paramLong, paramString1, paramString2, paramThrowable);
  }

  public boolean d()
  {
    return this.b;
  }

  public b e()
  {
    return this.c;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.a.k
 * JD-Core Version:    0.6.0
 */