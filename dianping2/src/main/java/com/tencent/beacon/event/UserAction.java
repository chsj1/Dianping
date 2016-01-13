package com.tencent.beacon.event;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import com.tencent.beacon.b.h;
import com.tencent.beacon.b.i;
import com.tencent.beacon.b.j;
import com.tencent.beacon.b.l;
import com.tencent.beacon.f.c;
import com.tencent.beacon.upload.InitHandleListener;
import com.tencent.beacon.upload.UploadHandleListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAction
{
  protected static Map<String, String> a;
  private static Context b = null;
  private static String c = "";
  private static String d = "10000";
  private static String e = "";
  private static Runnable f;

  static
  {
    a = null;
    f = new UserAction.1();
  }

  @TargetApi(14)
  private static void a(Context paramContext, UploadHandleListener paramUploadHandleListener, boolean paramBoolean, long paramLong, InitHandleListener paramInitHandleListener)
  {
    if (paramContext == null)
    {
      com.tencent.beacon.f.b.c(" the context is null! init beacon sdk failed!", new Object[0]);
      return;
    }
    Context localContext = paramContext.getApplicationContext();
    if (localContext != null)
    {
      b = localContext;
      if (paramLong > 0L)
      {
        long l = paramLong;
        if (paramLong > 10000L)
          l = 10000L;
        com.tencent.beacon.b.b.d.a(l);
      }
      paramLong = new Date().getTime();
      if (Integer.valueOf(Build.VERSION.SDK).intValue() < 14)
        break label219;
      paramContext = new i();
      ((Application)b).registerActivityLifecycleCallbacks(paramContext);
    }
    while (true)
    {
      com.tencent.beacon.f.b.a("API Level: %s", new Object[] { Build.VERSION.SDK });
      com.tencent.beacon.f.b.a("initUserAction t1:" + (new Date().getTime() - paramLong), new Object[0]);
      paramContext = p.a(b, paramBoolean);
      p.a(b, true, paramContext, paramUploadHandleListener, paramInitHandleListener);
      com.tencent.beacon.c.d.a(b, paramContext);
      com.tencent.beacon.f.b.a("initUserAction t1:" + (new Date().getTime() - paramLong), new Object[0]);
      return;
      b = paramContext;
      break;
      label219: paramContext = new com.tencent.beacon.b.a(b);
      com.tencent.beacon.b.e.a().a(paramContext);
    }
  }

  public static void clearAppTotalConsume(Context paramContext)
  {
    j.e(paramContext.getApplicationContext());
  }

  public static void clearSDKTotalConsume(Context paramContext)
  {
    j.d(paramContext.getApplicationContext());
  }

  public static void closeUseInfoEvent()
  {
    p localp = p.d();
    if (localp != null)
      localp.f();
  }

  public static void doUploadRecords()
  {
    com.tencent.beacon.b.e.a().a(f);
  }

  public static void flushObjectsToDB(boolean paramBoolean)
  {
    p.c(paramBoolean);
  }

  public static String getAPN()
  {
    if (b == null)
    {
      com.tencent.beacon.f.b.d("please initUserAction first!", new Object[0]);
      return "unknown";
    }
    return c.c(b);
  }

  public static String getAppkey()
  {
    return e;
  }

  public static String getCloudParas(String paramString)
  {
    String str = null;
    Map localMap = com.tencent.beacon.b.b.e.a().d();
    if (localMap != null)
      str = (String)localMap.get(paramString);
    return str;
  }

  public static String getGatewayIP()
  {
    com.tencent.beacon.b.f localf = com.tencent.beacon.b.f.m();
    if (localf != null)
      return localf.g();
    return "unknown";
  }

  public static String getNetWorkType()
  {
    if (b == null)
    {
      com.tencent.beacon.f.b.d("please initUserAction first!", new Object[0]);
      return "unknown";
    }
    h.a(b);
    return h.l(b);
  }

  public static String getQIMEI()
  {
    String str;
    if ((b == null) || (p.d() == null))
    {
      com.tencent.beacon.f.b.d("please initUserAction first!", new Object[0]);
      str = "";
      if (b == null);
    }
    try
    {
      h.a(b);
      str = h.b(b);
      return str;
      return l.a(b).a();
    }
    catch (Exception localException)
    {
    }
    return "";
  }

  public static String getQQ()
  {
    return c;
  }

  public static long getSDKTotalConsume(Context paramContext, boolean paramBoolean)
  {
    paramContext = j.b(paramContext.getApplicationContext());
    if (paramContext != null)
    {
      if (paramBoolean)
        return paramContext.e;
      long l = paramContext.d;
      return paramContext.e + l;
    }
    return -1L;
  }

  public static String getSDKVersion()
  {
    return "2.1.6";
  }

  public static g getUserActionRuntimeStrategy()
  {
    try
    {
      g localg = p.d().i();
      return localg;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }

  public static String getUserID()
  {
    return d;
  }

  @Deprecated
  public static boolean heartbeatEvent()
  {
    com.tencent.beacon.f.b.c(" heartbeatEvent is Deprecated !", new Object[0]);
    return true;
  }

  public static void initUserAction(Context paramContext)
  {
    a(paramContext, null, true, 0L, null);
  }

  public static void initUserAction(Context paramContext, boolean paramBoolean)
  {
    a(paramContext, null, paramBoolean, 0L, null);
  }

  public static void initUserAction(Context paramContext, boolean paramBoolean, long paramLong)
  {
    a(paramContext, null, paramBoolean, paramLong, null);
  }

  public static void initUserAction(Context paramContext, boolean paramBoolean, long paramLong, InitHandleListener paramInitHandleListener)
  {
    a(paramContext, null, paramBoolean, paramLong, paramInitHandleListener);
  }

  public static void initUserAction(Context paramContext, boolean paramBoolean, long paramLong, UploadHandleListener paramUploadHandleListener)
  {
    a(paramContext, paramUploadHandleListener, paramBoolean, paramLong, null);
  }

  public static boolean loginEvent(boolean paramBoolean, long paramLong, Map<String, String> paramMap)
  {
    if (b != null)
    {
      h.a(b);
      paramMap.put("A33", h.l(b));
    }
    return p.a("rqd_wgLogin", paramBoolean, paramLong, 0L, paramMap, true);
  }

  public static void onAppExited()
  {
    p.e();
  }

  @Deprecated
  public static boolean onMergeUserAction(String paramString, boolean paramBoolean1, long paramLong1, long paramLong2, boolean paramBoolean2, d[] paramArrayOfd)
  {
    if ((paramString == null) || ("".equals(paramString.trim())))
      com.tencent.beacon.f.b.c("param eventName is null or \"\", please check it, return false! ", new Object[0]);
    do
      return false;
    while (com.tencent.beacon.a.a.b(paramString) == null);
    return p.a(paramString, paramBoolean1, paramLong1, paramLong2, paramBoolean2, paramArrayOfd);
  }

  public static boolean onUserAction(String paramString, boolean paramBoolean1, long paramLong1, long paramLong2, Map<String, String> paramMap, boolean paramBoolean2)
  {
    return onUserAction(paramString, paramBoolean1, paramLong1, paramLong2, paramMap, paramBoolean2, false);
  }

  public static boolean onUserAction(String paramString, boolean paramBoolean1, long paramLong1, long paramLong2, Map<String, String> paramMap, boolean paramBoolean2, boolean paramBoolean3)
  {
    if ((paramString == null) || ("".equals(paramString.trim())))
    {
      com.tencent.beacon.f.b.c("param eventName is null or \"\", please check it, return false! ", new Object[0]);
      return false;
    }
    paramString = com.tencent.beacon.a.a.b(paramString);
    if (paramString == null)
      return false;
    return p.a(paramString, paramBoolean1, paramLong1, paramLong2, paramMap, paramBoolean2, paramBoolean3);
  }

  public static void setAPPVersion(String paramString)
  {
    if ((paramString != null) && (!paramString.trim().equals("")))
      com.tencent.beacon.b.b.a(paramString);
  }

  public static void setAdditionalInfo(Map<String, String> paramMap)
  {
    if ((paramMap != null) && (paramMap.size() <= 20))
    {
      HashMap localHashMap = new HashMap();
      a = localHashMap;
      localHashMap.putAll(paramMap);
    }
  }

  public static void setAppKey(Context paramContext, String paramString)
    throws Exception
  {
    com.tencent.beacon.f.b.a(" setAppKey:" + paramString, new Object[0]);
    if (paramContext == null)
      com.tencent.beacon.f.b.c(" the context is null! setAppKey failed!", new Object[0]);
    label113: label119: 
    while (true)
    {
      return;
      Context localContext = paramContext.getApplicationContext();
      if (localContext != null)
      {
        b = localContext;
        if ((paramString == null) || (paramString.trim().length() <= 0) || (paramString.trim().length() >= 20))
          break;
        paramContext = com.tencent.beacon.b.f.m();
        if (paramContext != null)
          break label113;
        com.tencent.beacon.b.f.a(b);
        paramContext = com.tencent.beacon.b.f.m();
      }
      while (true)
      {
        if (paramContext == null)
          break label119;
        paramContext.e(paramString);
        return;
        b = paramContext;
        break;
        paramContext.e(paramString);
      }
    }
    com.tencent.beacon.f.b.c(" setAppKey: appkey is null or not available!", new Object[0]);
    throw new RuntimeException("appkey is null or not available! please check it!");
  }

  public static void setAppkey(String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
      e = paramString;
  }

  @Deprecated
  public static void setAutoLaunchEventUsable(boolean paramBoolean)
  {
  }

  public static void setChannelID(String paramString)
  {
    com.tencent.beacon.b.f localf2 = com.tencent.beacon.b.f.m();
    com.tencent.beacon.b.f localf1 = localf2;
    if (localf2 == null)
    {
      com.tencent.beacon.b.f.a(b);
      localf1 = com.tencent.beacon.b.f.m();
    }
    if (localf1 == null)
    {
      com.tencent.beacon.f.b.d("please set the channelID after call initUserAction!", new Object[0]);
      return;
    }
    localf1.d(com.tencent.beacon.a.a.d(paramString));
  }

  public static void setInitChannelPath(Context paramContext, String paramString)
    throws Exception
  {
    if (b == null)
    {
      com.tencent.beacon.b.b.a(paramContext.getApplicationContext(), paramString);
      return;
    }
    com.tencent.beacon.f.b.d("please set the channel path before call initUserAction!", new Object[0]);
    throw new RuntimeException("please set the channel path before call initUserAction!");
  }

  public static void setLogAble(boolean paramBoolean1, boolean paramBoolean2)
  {
    com.tencent.beacon.f.b.a = paramBoolean1;
    com.tencent.beacon.f.b.b = paramBoolean2;
  }

  @Deprecated
  public static void setNetSpeedMonitorUsable(boolean paramBoolean)
  {
    com.tencent.beacon.f.b.c(" SpeedMonitorModule is Deprecated !", new Object[0]);
  }

  public static void setQQ(String paramString)
  {
    if (paramString != null)
      try
      {
        if (Long.parseLong(paramString) > 10000L)
          c = paramString;
        return;
      }
      catch (Exception paramString)
      {
        com.tencent.beacon.f.b.c(" setQQ: set qq is not available !", new Object[0]);
        return;
      }
    com.tencent.beacon.f.b.c(" setQQ: set qq is null !", new Object[0]);
  }

  public static void setUploadMode(boolean paramBoolean)
  {
    p localp = p.d();
    if (localp != null)
    {
      localp.b(paramBoolean);
      return;
    }
    com.tencent.beacon.f.b.c(" UserActionRecord.getInstance is null, please initUserAction first!", new Object[0]);
  }

  public static void setUserActionUsable(boolean paramBoolean)
  {
    p localp = p.d();
    if (localp != null)
    {
      localp.a(paramBoolean);
      return;
    }
    com.tencent.beacon.f.b.c(" UserActionRecord.getInstance is null, please initUserAction first!", new Object[0]);
  }

  public static void setUserID(String paramString)
  {
    com.tencent.beacon.f.b.a(" setUserID:" + paramString, new Object[0]);
    if ((paramString != null) && (paramString.trim().length() > 0) && (!"10000".equals(paramString)) && (!"10000".equals(com.tencent.beacon.a.a.c(paramString))))
      d = paramString;
  }

  public static boolean testSpeedDomain(List<String> paramList)
  {
    p localp = p.d();
    if (localp != null)
      return localp.b(paramList);
    return false;
  }

  public static boolean testSpeedIp(List<String> paramList)
  {
    p localp = p.d();
    if (localp != null)
      return localp.a(paramList);
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.beacon.event.UserAction
 * JD-Core Version:    0.6.0
 */