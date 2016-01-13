package com.dianping.statistics.impl;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.dianping.statistics.utils.StatisticsInitializer;
import com.tencent.beacon.event.UserAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;

public class NewStatisticsService extends NewDefaultStatisticsService
{
  private StatisticsInitializer initializer;
  private Map<String, String> staticEnvironment = new HashMap();

  public NewStatisticsService(Context paramContext, StatisticsInitializer paramStatisticsInitializer)
  {
    this(paramContext, "statistics_new", paramStatisticsInitializer);
  }

  public NewStatisticsService(Context paramContext, String paramString, StatisticsInitializer paramStatisticsInitializer)
  {
    super(paramContext, paramString, paramStatisticsInitializer);
    this.initializer = paramStatisticsInitializer;
    this.staticEnvironment.put("app_type", "1");
    this.staticEnvironment.put("platform", "Android");
    this.staticEnvironment.put("platform_version", Build.VERSION.RELEASE);
    this.staticEnvironment.put("manufacture", Build.MANUFACTURER);
    this.staticEnvironment.put("model", Build.MODEL);
    this.staticEnvironment.put("app_version", StatisticsInitializer.app_version);
    this.staticEnvironment.put("app_market", StatisticsInitializer.app_market);
    this.staticEnvironment.put("mac", StatisticsInitializer.mac);
    this.staticEnvironment.put("imei", StatisticsInitializer.imei);
    this.staticEnvironment.put("uuid", StatisticsInitializer.uuid);
    this.staticEnvironment.put("userAgent", paramStatisticsInitializer.userAgent);
  }

  private void pushToDENGTA(Map<String, String> paramMap)
  {
    UserAction.onUserAction("dianping", true, -1L, -1L, paramMap, true, "pageview".equals(paramMap.get("element_id")));
  }

  public void event(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
  }

  public void pageView(String paramString, List<NameValuePair> paramList)
  {
  }

  public void record(List<NameValuePair> paramList)
  {
  }

  public void record(Map<String, String> paramMap)
  {
    paramMap.putAll(this.staticEnvironment);
    if (this.initializer.disableDengTa)
    {
      push(paramMap);
      return;
    }
    pushToDENGTA(paramMap);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.NewStatisticsService
 * JD-Core Version:    0.6.0
 */