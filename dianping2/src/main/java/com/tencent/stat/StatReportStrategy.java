package com.tencent.stat;

public enum StatReportStrategy
{
  int a;

  static
  {
    BATCH = new StatReportStrategy("BATCH", 2, 3);
    APP_LAUNCH = new StatReportStrategy("APP_LAUNCH", 3, 4);
    DEVELOPER = new StatReportStrategy("DEVELOPER", 4, 5);
    PERIOD = new StatReportStrategy("PERIOD", 5, 6);
    ONLY_WIFI_NO_CACHE = new StatReportStrategy("ONLY_WIFI_NO_CACHE", 6, 7);
    b = new StatReportStrategy[] { INSTANT, ONLY_WIFI, BATCH, APP_LAUNCH, DEVELOPER, PERIOD, ONLY_WIFI_NO_CACHE };
  }

  private StatReportStrategy(int paramInt)
  {
    this.a = paramInt;
  }

  public static StatReportStrategy getStatReportStrategy(int paramInt)
  {
    StatReportStrategy[] arrayOfStatReportStrategy = values();
    int j = arrayOfStatReportStrategy.length;
    int i = 0;
    while (i < j)
    {
      StatReportStrategy localStatReportStrategy = arrayOfStatReportStrategy[i];
      if (paramInt == localStatReportStrategy.a())
        return localStatReportStrategy;
      i += 1;
    }
    return null;
  }

  public int a()
  {
    return this.a;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.StatReportStrategy
 * JD-Core Version:    0.6.0
 */