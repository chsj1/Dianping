package com.dianping.utn.client;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class UtnUtils
{
  private static final AtomicInteger reqId = new AtomicInteger(new Random(System.nanoTime()).nextInt());

  public static int generateHttpRequestId()
  {
    return 0x20000000 | reqId.getAndIncrement() & 0xFFFFFFF;
  }

  public static int generatePingRequestId()
  {
    return 0x10000000 | reqId.getAndIncrement() & 0xFFFFFFF;
  }

  public static boolean isHttpRequestId(int paramInt)
  {
    return (0x20000000 & paramInt) == 536870912;
  }

  public static boolean isPingRequestId(int paramInt)
  {
    return (0x10000000 & paramInt) == 268435456;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.client.UtnUtils
 * JD-Core Version:    0.6.0
 */