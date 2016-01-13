package com.tencent.base.os.clock;

import android.app.PendingIntent;

public class a extends c
{
  private PendingIntent a;
  private String b;

  public a(String paramString, long paramLong, d paramd)
  {
    super(-1, paramLong, paramd);
    a(paramString);
  }

  public void a()
  {
    b.b(this);
  }

  public void a(PendingIntent paramPendingIntent)
  {
    this.a = paramPendingIntent;
  }

  public void a(String paramString)
  {
    this.b = paramString;
  }

  public PendingIntent b()
  {
    return this.a;
  }

  public String c()
  {
    return this.b;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.base.os.clock.a
 * JD-Core Version:    0.6.0
 */