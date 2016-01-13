package com.dianping.util.log;

public class LogConfig
{
  public static final String API_CODE_LOG = "http://m.api.dianping.com/applog/appcodelog.api";
  public static final String API_ERROR_LOG = "http://m.api.dianping.com/applog/apperrorlog.api";
  protected static final int ERROR = 4;
  protected static final int INFO = 3;
  protected static final String NOVA_LOG_TAG = "NOVA_LOG";
  protected static final int OFF = -1;
  protected static final int backupIndex = 4;
  protected static final long maxSize = 4096L;
  protected static final int[] outputTargetList = { 0, 1 };
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.log.LogConfig
 * JD-Core Version:    0.6.0
 */