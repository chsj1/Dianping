package cloudwns.l;

public final class a extends c
{
  private static volatile a a = null;

  protected a()
  {
    this.fileTracer = new cloudwns.b.a(SERVICE_CONFIG);
    c.setInstance(this);
    onSharedPreferenceChanged(null, null);
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

  public static void a(String paramString1, String paramString2)
  {
    a().trace(1, paramString1, paramString2, null);
  }

  public static void a(String paramString1, String paramString2, Throwable paramThrowable)
  {
    a().trace(4, paramString1, paramString2, paramThrowable);
  }

  public static void b(String paramString1, String paramString2)
  {
    a().trace(2, paramString1, paramString2, null);
  }

  public static void b(String paramString1, String paramString2, Throwable paramThrowable)
  {
    a().trace(8, paramString1, paramString2, paramThrowable);
  }

  public static void c(String paramString1, String paramString2)
  {
    a().trace(4, paramString1, paramString2, null);
  }

  public static void c(String paramString1, String paramString2, Throwable paramThrowable)
  {
    a().trace(16, paramString1, paramString2, paramThrowable);
  }

  public static void d(String paramString1, String paramString2)
  {
    a().trace(8, paramString1, paramString2, null);
  }

  public static void e(String paramString1, String paramString2)
  {
    a().trace(16, paramString1, paramString2, null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.l.a
 * JD-Core Version:    0.6.0
 */