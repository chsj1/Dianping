package com.tencent.a.a;

public class c extends d
{
  public static c a = null;

  public c()
  {
    this.d = new a(b);
  }

  public static c a()
  {
    if (a == null)
      monitorenter;
    try
    {
      if (a == null)
        a = new c();
      return a;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static final void a(String paramString1, String paramString2)
  {
    a().a(1, paramString1, paramString2, null);
  }

  public static final void a(String paramString1, String paramString2, Throwable paramThrowable)
  {
    a().a(16, paramString1, paramString2, paramThrowable);
  }

  public static final void b(String paramString1, String paramString2)
  {
    a().a(2, paramString1, paramString2, null);
  }

  public static final void c(String paramString1, String paramString2)
  {
    a().a(4, paramString1, paramString2, null);
  }

  public static final void d(String paramString1, String paramString2)
  {
    a().a(16, paramString1, paramString2, null);
  }

  public void b()
  {
    monitorenter;
    try
    {
      if (this.d != null)
      {
        this.d.a();
        this.d.b();
        this.d = null;
        a = null;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.a.c
 * JD-Core Version:    0.6.0
 */