package com.dianping.monitor;

public abstract interface MonitorService
{
  public abstract void flush();

  public abstract String getCommand(String paramString);

  public abstract void pv(long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

  public abstract void pv3(long paramLong, String paramString1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString2);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.monitor.MonitorService
 * JD-Core Version:    0.6.0
 */