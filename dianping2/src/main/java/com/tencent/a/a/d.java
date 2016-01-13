package com.tencent.a.a;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;
import com.tencent.a.b.b.a;
import com.tencent.a.b.c;
import java.io.File;

public class d
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  protected static final g b;
  protected static final g c;
  private volatile boolean a = false;
  protected a d;
  private volatile boolean e = true;
  private volatile boolean f = true;

  static
  {
    File localFile = c();
    b = new g(localFile, 24, 262144, 8192, "OpenSDK.Client.File.Tracer", 10000L, 10, ".app.log", 604800000L);
    c = new g(localFile, 24, 262144, 8192, "OpenSDK.File.Tracer", 10000L, 10, ".OpenSDK.log", 604800000L);
  }

  public static File c()
  {
    int j = 0;
    String str = b.a.a + File.separator + c.b();
    com.tencent.a.b.d locald = com.tencent.a.b.a.b();
    int i = j;
    if (locald != null)
    {
      i = j;
      if (locald.c() > 8388608L)
        i = 1;
    }
    if (i != 0)
      return new File(Environment.getExternalStorageDirectory(), str);
    return new File(c.c(), str);
  }

  public void a(int paramInt, String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (d())
    {
      if (!e())
        break label40;
      if (this.d != null)
        break label22;
    }
    label22: label40: 
    do
    {
      return;
      this.d.b(paramInt, Thread.currentThread(), System.currentTimeMillis(), paramString1, paramString2, paramThrowable);
    }
    while (!f());
    f.a.b(paramInt, Thread.currentThread(), System.currentTimeMillis(), paramString1, paramString2, paramThrowable);
  }

  public void b()
  {
    if (this.d != null)
    {
      this.d.a();
      this.d.b();
    }
  }

  public final boolean d()
  {
    return this.a;
  }

  public final boolean e()
  {
    return this.e;
  }

  public final boolean f()
  {
    return this.f;
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if ("debug.file.tracelevel".equals(paramString))
    {
      int i = paramSharedPreferences.getInt("debug.file.tracelevel", 63);
      a(8, "WnsTracer", "File Trace Level Changed = " + i, null);
      this.d.a(i);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.a.d
 * JD-Core Version:    0.6.0
 */