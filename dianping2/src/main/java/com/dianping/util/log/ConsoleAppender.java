package com.dianping.util.log;

import com.dianping.util.Log;

public final class ConsoleAppender extends Appender
{
  public void e(int paramInt, String paramString1, String paramString2)
  {
    Log.e(paramString1, paramString2);
  }

  public void i(int paramInt, String paramString1, String paramString2)
  {
    Log.i(paramString1, paramString2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.log.ConsoleAppender
 * JD-Core Version:    0.6.0
 */