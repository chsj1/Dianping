package com.dianping.locationservice.realtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class RealTimeLocateSettings
{
  public static final int ALWAYS_REQUEST_API = 64;
  public static final int FULL_LOG = 256;
  public static final int LOG_LOCATION_ID = 16;
  public static final int NO_LOG = 128;
  private static final String SHARED_PREFERENCE_KEY = "dppushservice";
  public static final int TURN_OFF = 32;
  public static final int USE_WGS84 = 8;
  public static final int USE_WIFI = 1;
  public static final int VALID_WITHOUT_CELL = 2;
  public static final int VALID_WITHOUT_WIFI = 4;
  int cellInvalidDifferenceLimit = 1;
  int cellLackDifference = 10000;
  int cellOverDifference = 5000;
  long expireSeconds = TimeUnit.DAYS.toSeconds(7L);
  int flag = 7;
  int maxCount = 20;
  long minAPILocateInterval = TimeUnit.MINUTES.toMillis(60L);
  int wifiInvalidDifferenceLimit = 1;
  int wifiLackDifference = 10000;
  int wifiOverDifference = 5000;

  public static RealTimeLocateSettings get(DpIdSupplier paramDpIdSupplier, Context paramContext)
  {
    RealTimeLocateSettings localRealTimeLocateSettings = new RealTimeLocateSettings();
    Object localObject = null;
    String str = getSharedPreferences(paramContext).getString("config", null);
    paramContext = localObject;
    if (str != null)
    {
      paramContext = localObject;
      if (str.length() <= 0);
    }
    try
    {
      paramContext = new JSONObject(str).optString("realTimeLocate");
      RealTimeLocateLog.i("get settings: " + paramContext);
      if (paramContext != null)
      {
        paramDpIdSupplier = hit(paramContext, paramDpIdSupplier);
        RealTimeLocateLog.i("hit settings: " + paramDpIdSupplier);
        paramContext = paramDpIdSupplier.split("\\|");
        if (paramContext.length <= 0);
      }
    }
    catch (Throwable localThrowable9)
    {
      try
      {
        localRealTimeLocateSettings.minAPILocateInterval = TimeUnit.MINUTES.toMillis(Integer.parseInt(paramContext[0]));
        if (paramContext.length <= 1);
      }
      catch (Throwable localThrowable9)
      {
        try
        {
          localRealTimeLocateSettings.flag = Integer.parseInt(paramContext[1]);
          if (paramContext.length <= 2);
        }
        catch (Throwable localThrowable9)
        {
          try
          {
            localRealTimeLocateSettings.cellInvalidDifferenceLimit = Integer.parseInt(paramContext[2]);
            if (paramContext.length <= 3);
          }
          catch (Throwable localThrowable9)
          {
            try
            {
              localRealTimeLocateSettings.cellLackDifference = Integer.parseInt(paramContext[3]);
              if (paramContext.length <= 4);
            }
            catch (Throwable localThrowable9)
            {
              try
              {
                localRealTimeLocateSettings.cellOverDifference = Integer.parseInt(paramContext[4]);
                if (paramContext.length <= 5);
              }
              catch (Throwable localThrowable9)
              {
                try
                {
                  localRealTimeLocateSettings.wifiInvalidDifferenceLimit = Integer.parseInt(paramContext[5]);
                  if (paramContext.length <= 6);
                }
                catch (Throwable localThrowable9)
                {
                  try
                  {
                    localRealTimeLocateSettings.wifiLackDifference = Integer.parseInt(paramContext[6]);
                    if (paramContext.length <= 7);
                  }
                  catch (Throwable localThrowable9)
                  {
                    try
                    {
                      localRealTimeLocateSettings.wifiOverDifference = Integer.parseInt(paramContext[7]);
                      if (paramContext.length <= 8);
                    }
                    catch (Throwable localThrowable9)
                    {
                      try
                      {
                        localRealTimeLocateSettings.expireSeconds = Integer.parseInt(paramContext[8]);
                        if (paramContext.length <= 9);
                      }
                      catch (Throwable localThrowable9)
                      {
                        try
                        {
                          while (true)
                          {
                            localRealTimeLocateSettings.maxCount = Integer.parseInt(paramContext[9]);
                            return localRealTimeLocateSettings;
                            paramContext = paramContext;
                            RealTimeLocateLog.w("read config failed", paramContext);
                            paramContext = localObject;
                            continue;
                            localThrowable1 = localThrowable1;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable1.getMessage());
                            continue;
                            localThrowable2 = localThrowable2;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable2.getMessage());
                            continue;
                            localThrowable3 = localThrowable3;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable3.getMessage());
                            continue;
                            localThrowable4 = localThrowable4;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable4.getMessage());
                            continue;
                            localThrowable5 = localThrowable5;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable5.getMessage());
                            continue;
                            localThrowable6 = localThrowable6;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable6.getMessage());
                            continue;
                            localThrowable7 = localThrowable7;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable7.getMessage());
                            continue;
                            localThrowable8 = localThrowable8;
                            RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable8.getMessage());
                          }
                          localThrowable9 = localThrowable9;
                          RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + localThrowable9.getMessage());
                        }
                        catch (Throwable paramContext)
                        {
                          RealTimeLocateLog.e("parse real time locate failed, " + paramDpIdSupplier + ", " + paramContext.getMessage());
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return localRealTimeLocateSettings;
  }

  @TargetApi(11)
  private static SharedPreferences getSharedPreferences(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 11)
      return paramContext.getSharedPreferences("dppushservice", 4);
    return paramContext.getSharedPreferences("dppushservice", 0);
  }

  private static String hit(String paramString, DpIdSupplier paramDpIdSupplier)
  {
    String[] arrayOfString = paramString.split("\\|&\\|");
    paramString = null;
    int j = arrayOfString.length;
    int i = 0;
    Object localObject = paramString;
    if (i < j)
    {
      String str1 = arrayOfString[i];
      int k = str1.indexOf(":");
      if (k == -1)
      {
        localObject = paramString;
        if (paramString == null)
          localObject = str1;
      }
      String str2;
      String str3;
      do
      {
        i += 1;
        paramString = (String)localObject;
        break;
        str2 = str1.substring(0, k);
        str3 = paramDpIdSupplier.getDpId();
        RealTimeLocateLog.d("dpId: " + str3);
        localObject = paramString;
      }
      while (!str2.equals(str3));
      localObject = str1.substring(k + 1);
    }
    return (String)localObject;
  }

  boolean hasAllFlag(int paramInt)
  {
    return (this.flag & paramInt) == paramInt;
  }

  boolean hasAnyFlag(int paramInt)
  {
    return (this.flag & paramInt) != 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.realtime.RealTimeLocateSettings
 * JD-Core Version:    0.6.0
 */