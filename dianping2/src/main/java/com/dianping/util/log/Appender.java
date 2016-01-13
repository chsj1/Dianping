package com.dianping.util.log;

public abstract class Appender
{
  public void close()
  {
  }

  public abstract void e(int paramInt, String paramString1, String paramString2);

  public abstract void i(int paramInt, String paramString1, String paramString2);

  public void open()
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.log.Appender
 * JD-Core Version:    0.6.0
 */