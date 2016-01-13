package com.tencent.a.b;

import android.content.Context;
import java.io.File;

public final class c
{
  private static Context a;

  public static final Context a()
  {
    if (a == null)
      return null;
    return a;
  }

  public static final void a(Context paramContext)
  {
    a = paramContext;
  }

  public static final String b()
  {
    return a().getPackageName();
  }

  public static final File c()
  {
    return a().getFilesDir();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.b.c
 * JD-Core Version:    0.6.0
 */