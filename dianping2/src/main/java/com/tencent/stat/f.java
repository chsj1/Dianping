package com.tencent.stat;

import com.tencent.stat.common.StatLogger;
import java.util.List;

class f
  implements Runnable
{
  f(d paramd, List paramList, c paramc)
  {
  }

  public void run()
  {
    try
    {
      this.c.a(this.a, this.b);
      return;
    }
    catch (Throwable localThrowable)
    {
      d.c().e(localThrowable);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.f
 * JD-Core Version:    0.6.0
 */