package com.dianping.locationservice.realtime;

import com.dianping.util.Log;

public class RealTimeLocateLog
{
  public static final String LOG_TAG = "RealTimeLocate";

  public static void d(String paramString)
  {
    Log.d("RealTimeLocate", paramString);
  }

  public static void e(String paramString)
  {
    Log.e("RealTimeLocate", paramString);
  }

  public static void e(String paramString, Throwable paramThrowable)
  {
    Log.e("RealTimeLocate", paramString, paramThrowable);
  }

  public static void i(String paramString)
  {
    Log.i("RealTimeLocate", paramString);
  }

  public static void w(String paramString)
  {
    Log.w("RealTimeLocate", paramString);
  }

  public static void w(String paramString, Throwable paramThrowable)
  {
    Log.w("RealTimeLocate", paramString, paramThrowable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.realtime.RealTimeLocateLog
 * JD-Core Version:    0.6.0
 */