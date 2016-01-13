package com.tencent.stat;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

class e extends DefaultConnectionKeepAliveStrategy
{
  e(d paramd)
  {
  }

  public long getKeepAliveDuration(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    long l2 = super.getKeepAliveDuration(paramHttpResponse, paramHttpContext);
    long l1 = l2;
    if (l2 == -1L)
      l1 = 20000L;
    return l1;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.e
 * JD-Core Version:    0.6.0
 */