package com.dianping.base.push.pushservice;

import android.os.SystemClock;
import com.dianping.monitor.MonitorService;

public class UdpPing
{
  private static long lastPing;

  public static void ping(long paramLong, MonitorService paramMonitorService)
  {
    long l = SystemClock.elapsedRealtime();
    if (lastPing < l - paramLong)
    {
      lastPing = l;
      new UdpPing.1(paramMonitorService).start();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.UdpPing
 * JD-Core Version:    0.6.0
 */