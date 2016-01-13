package com.dianping.tunnel;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TunnelUtils
{
  private static final AtomicInteger reqId = new AtomicInteger(new Random(System.nanoTime()).nextInt());

  public static String generateHttpRequestId()
  {
    return Integer.toString(0xFFFFFFF & reqId.getAndIncrement());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tunnel.TunnelUtils
 * JD-Core Version:    0.6.0
 */