package com.tencent.a.b;

import android.os.Environment;

public class a
{
  public static boolean a()
  {
    String str = Environment.getExternalStorageState();
    return ("mounted".equals(str)) || ("mounted_ro".equals(str));
  }

  public static d b()
  {
    if (!a())
      return null;
    return d.b(Environment.getExternalStorageDirectory());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.b.a
 * JD-Core Version:    0.6.0
 */