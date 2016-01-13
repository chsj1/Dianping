package com.dianping.dataservice.mapi;

public enum CacheType
{
  static
  {
    HOURLY = new CacheType("HOURLY", 2);
    DAILY = new CacheType("DAILY", 3);
    SERVICE = new CacheType("SERVICE", 4);
    CRITICAL = new CacheType("CRITICAL", 5);
    $VALUES = new CacheType[] { DISABLED, NORMAL, HOURLY, DAILY, SERVICE, CRITICAL };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.CacheType
 * JD-Core Version:    0.6.0
 */