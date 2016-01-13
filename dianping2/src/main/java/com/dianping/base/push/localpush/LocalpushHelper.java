package com.dianping.base.push.localpush;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.dianping.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class LocalpushHelper
{
  public static final String ACTION_NOTIFY = "com.dianping.action.Intent.ACTION_NOTIFY";
  public static final String ACTION_SHUTDOWN = "com.dianping.action.Intent.ACTION_SHUTDOWN";
  public static final String ACTION_UPDATE = "com.dianping.action.Intent.ACTION_UPDATE";
  public static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
  private static final boolean DEBUG = true;
  public static final String SHARED_PREFERENCE_LOCALPUSH = "localpush";
  private static final String TAG = "LocalpushHelper";
  public static final long TIME_DAY = 86400000L;
  public static final long TIME_WEEK = 604800000L;
  public static final int TYPE_REPEAT_DAY = 2;
  public static final int TYPE_REPEAT_WEEK = 1;
  public static final int TYPE_SPECIFICTIME = 0;
  private static LocalpushHelper instance;
  private Context appContext;

  private LocalpushHelper(Context paramContext)
  {
    this.appContext = paramContext;
  }

  public static LocalpushHelper instance(Context paramContext)
  {
    if (instance == null)
      instance = new LocalpushHelper(paramContext);
    return instance;
  }

  public boolean clear()
  {
    return getLocalpushSharedPreference().edit().clear().commit();
  }

  public boolean delete(String paramString)
  {
    return getLocalpushSharedPreference().edit().remove(paramString).commit();
  }

  public LocalpushInfo get(String paramString)
  {
    Object localObject = null;
    String str = getLocalpushSharedPreference().getString(paramString, null);
    paramString = localObject;
    if (str != null)
      paramString = new LocalpushInfo(str);
    return paramString;
  }

  public Set<String> getIDs()
  {
    return getLocalpushSharedPreference().getAll().keySet();
  }

  public SharedPreferences getLocalpushSharedPreference()
  {
    return this.appContext.getSharedPreferences("localpush", 0);
  }

  public boolean insert(LocalpushInfo paramLocalpushInfo)
  {
    Log.d("LocalpushHelper", "insert a new item:" + paramLocalpushInfo);
    SharedPreferences localSharedPreferences = getLocalpushSharedPreference();
    String str = localSharedPreferences.getString(paramLocalpushInfo.id, null);
    if (!paramLocalpushInfo.getValue().equals(str))
      return localSharedPreferences.edit().putString(paramLocalpushInfo.id, paramLocalpushInfo.getValue()).commit();
    return false;
  }

  public static class LocalpushInfo
  {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String context;
    public long endtime;
    public String id;
    public long starttime;
    public long triggertime;
    public int type;
    public String url;

    public LocalpushInfo(String paramString)
    {
      if ((paramString != null) && (paramString.length() > 0))
      {
        paramString = paramString.split("\\|");
        if ((paramString != null) && (paramString.length == 7))
        {
          this.id = paramString[0];
          this.context = paramString[3];
          this.url = paramString[4];
        }
      }
      try
      {
        this.starttime = Long.parseLong(paramString[1]);
        this.endtime = Long.parseLong(paramString[2]);
        this.type = Integer.parseInt(paramString[5]);
        this.triggertime = Long.parseLong(paramString[6]);
        return;
      }
      catch (java.lang.NumberFormatException paramString)
      {
      }
    }

    public LocalpushInfo(String paramString1, long paramLong1, long paramLong2, String paramString2, String paramString3, int paramInt, long paramLong3)
    {
      this.id = paramString1;
      this.starttime = paramLong1;
      this.endtime = paramLong2;
      this.context = paramString2;
      this.url = paramString3;
      this.type = paramInt;
      this.triggertime = paramLong3;
    }

    public String getValue()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(this.id).append("|").append(this.starttime).append("|").append(this.endtime).append("|").append(this.context).append("|").append(this.url).append("|").append(this.type).append("|").append(this.triggertime);
      return localStringBuilder.toString();
    }

    public boolean isExpired()
    {
      long l = System.currentTimeMillis();
      if ((this.endtime <= this.starttime) || (this.endtime <= l) || (this.triggertime < this.starttime) || (this.triggertime > this.endtime));
      do
        return true;
      while ((this.type == 0) && (this.triggertime < l));
      return false;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("LocalPush ID:").append(this.id).append(" ").append("开始时间:").append(SDF.format(new Date(this.starttime))).append(" ").append("结束时间:").append(SDF.format(new Date(this.endtime))).append(" ").append("内容:").append(this.context).append(" ").append("链接:").append(this.url).append(" ").append("类型:").append(this.type).append(" ").append("触发时间:").append(SDF.format(new Date(this.triggertime)));
      return localStringBuilder.toString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.localpush.LocalpushHelper
 * JD-Core Version:    0.6.0
 */