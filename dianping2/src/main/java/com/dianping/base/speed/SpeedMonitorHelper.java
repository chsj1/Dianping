package com.dianping.base.speed;

import android.util.SparseIntArray;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.util.Log;
import com.dianping.util.network.NetworkUtils;
import java.util.HashMap;

public class SpeedMonitorHelper
{
  private static final String TAG = SpeedMonitorHelper.class.getSimpleName();
  private static HashMap<String, SpeedMonitorHelper> mData = new HashMap();
  private int networkType;
  private String page;
  private SparseIntArray responseTimeMap;
  private long timestamp;

  public SpeedMonitorHelper(String paramString)
  {
    this(paramString, System.currentTimeMillis());
  }

  public SpeedMonitorHelper(String paramString, long paramLong)
  {
    this.page = paramString;
    this.timestamp = paramLong;
    this.networkType = switchNetworkType(NetworkUtils.getNetworkType(DPApplication.instance()));
    this.responseTimeMap = new SparseIntArray();
  }

  public static void addEvent(String paramString, int paramInt)
  {
    if (!mData.containsKey(paramString))
    {
      Log.e(TAG, paramString + "no exist");
      return;
    }
    ((SpeedMonitorHelper)mData.get(paramString)).setResponseTime(paramInt, System.currentTimeMillis());
  }

  public static void sendEvent(String paramString)
  {
    if (!mData.containsKey(paramString))
    {
      Log.e(TAG, paramString + "no exist");
      return;
    }
    ((SpeedMonitorHelper)mData.get(paramString)).sendReport();
    mData.remove(paramString);
  }

  public static void startEvent(String paramString)
  {
    mData.put(paramString, new SpeedMonitorHelper(paramString));
  }

  private int switchNetworkType(String paramString)
  {
    int j = 1;
    int i = -1;
    switch (paramString.hashCode())
    {
    default:
    case 2664213:
    case 1621:
    case 1652:
    case 1683:
    }
    while (true)
      switch (i)
      {
      default:
        j = 0;
      case 0:
        return j;
        if (!paramString.equals("WIFI"))
          continue;
        i = 0;
        continue;
        if (!paramString.equals("2G"))
          continue;
        i = 1;
        continue;
        if (!paramString.equals("3G"))
          continue;
        i = 2;
        continue;
        if (!paramString.equals("4G"))
          continue;
        i = 3;
      case 1:
      case 2:
      case 3:
      }
    return 2;
    return 3;
    return 4;
  }

  public void sendReport()
  {
    if (Environment.isDebug());
    do
      return;
    while (this.responseTimeMap.size() <= 0);
    StringBuilder localStringBuilder = new StringBuilder("v=1&c=\n");
    localStringBuilder.append(this.timestamp);
    localStringBuilder.append('\t');
    localStringBuilder.append(this.networkType);
    localStringBuilder.append('\t');
    localStringBuilder.append(Environment.versionCode());
    localStringBuilder.append('\t');
    localStringBuilder.append(1);
    localStringBuilder.append('\t');
    localStringBuilder.append(this.page);
    localStringBuilder.append('\t');
    int i = 0;
    if (i < this.responseTimeMap.size())
    {
      int j = this.responseTimeMap.keyAt(i);
      localStringBuilder.append(j);
      localStringBuilder.append('-');
      localStringBuilder.append(this.responseTimeMap.get(j));
      if (i == this.responseTimeMap.size() - 1)
        localStringBuilder.append('\n');
      while (true)
      {
        i += 1;
        break;
        localStringBuilder.append('\t');
      }
    }
    Log.d(TAG, localStringBuilder.toString());
    new SpeedMonitorHelper.1(this, "Send Speed Report", localStringBuilder).start();
  }

  public void setResponseTime(int paramInt, long paramLong)
  {
    this.responseTimeMap.put(paramInt, (int)(paramLong - this.timestamp));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.speed.SpeedMonitorHelper
 * JD-Core Version:    0.6.0
 */