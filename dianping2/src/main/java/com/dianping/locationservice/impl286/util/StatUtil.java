package com.dianping.locationservice.impl286.util;

import com.dianping.model.GPSCoordinate;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class StatUtil
{
  private static void doUpload(StatisticsService paramStatisticsService, List<NameValuePair> paramList)
  {
    if (paramStatisticsService == null)
      return;
    paramStatisticsService.record(paramList);
    paramStatisticsService.flush();
  }

  public static void statisticsEvent(StatisticsService paramStatisticsService, String paramString1, String paramString2, String paramString3, int paramInt)
  {
    statisticsEvent(paramStatisticsService, paramString1, paramString2, paramString3, paramInt, null);
  }

  public static void statisticsEvent(StatisticsService paramStatisticsService, String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    if (paramStatisticsService == null)
      return;
    paramStatisticsService.event(paramString1, paramString2, paramString3, paramInt, paramList);
  }

  public static void uploadCustomLoc(StatisticsService paramStatisticsService, GPSCoordinate paramGPSCoordinate)
  {
    ArrayList localArrayList = new ArrayList(10);
    localArrayList.add(new BasicNameValuePair("trainid", "l"));
    localArrayList.add(new BasicNameValuePair("customloc", "1"));
    if (paramGPSCoordinate != null)
      localArrayList.add(new BasicNameValuePair("coord", String.format("%s,%s,%s", new Object[] { Double.valueOf(paramGPSCoordinate.latitude()), Double.valueOf(paramGPSCoordinate.longitude()), Integer.valueOf(paramGPSCoordinate.accuracy()) })));
    doUpload(paramStatisticsService, localArrayList);
  }

  public static void uploadElapse(StatisticsService paramStatisticsService, long paramLong)
  {
    Log.i("dp locate elapse: " + paramLong);
    ArrayList localArrayList = new ArrayList(10);
    localArrayList.add(new BasicNameValuePair("dpLocElapse", String.valueOf(paramLong)));
    doUpload(paramStatisticsService, localArrayList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.util.StatUtil
 * JD-Core Version:    0.6.0
 */