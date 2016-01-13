package com.dianping.statistics;

import java.util.List;
import org.apache.http.NameValuePair;

public abstract interface StatisticsService
{
  public static final String ACTION_UPLOAD_SUCCESS = "com.dianping.action.STAT_UPLOAD_SUCCESS";

  public abstract void event(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList);

  public abstract void flush();

  public abstract void pageView(String paramString, List<NameValuePair> paramList);

  public abstract void push(String paramString);

  public abstract void record(List<NameValuePair> paramList);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.StatisticsService
 * JD-Core Version:    0.6.0
 */