package com.dianping.util;

public class Log
{
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int INFO = 4;
  public static int LEVEL = 0;
  private static String TAG_DEFAULT = "dianping";
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  public static void d(String paramString)
  {
    if (3 >= LEVEL)
      android.util.Log.d(TAG_DEFAULT, paramString);
  }

  public static void d(String paramString1, String paramString2)
  {
    if (3 >= LEVEL)
      android.util.Log.d(paramString1, paramString2);
  }

  public static void d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (3 >= LEVEL)
      android.util.Log.d(paramString1, paramString2, paramThrowable);
  }

  public static void e(String paramString)
  {
    if (6 >= LEVEL)
      android.util.Log.e(TAG_DEFAULT, paramString);
  }

  public static void e(String paramString1, String paramString2)
  {
    if (6 >= LEVEL)
      android.util.Log.e(paramString1, paramString2);
  }

  public static void e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (6 >= LEVEL)
      android.util.Log.e(paramString1, paramString2, paramThrowable);
  }

  public static void i(String paramString)
  {
    if (4 >= LEVEL)
      android.util.Log.i(TAG_DEFAULT, paramString);
  }

  public static void i(String paramString1, String paramString2)
  {
    if (4 >= LEVEL)
      android.util.Log.i(paramString1, paramString2);
  }

  public static void i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (4 >= LEVEL)
      android.util.Log.i(paramString1, paramString2, paramThrowable);
  }

  public static boolean isLoggable(int paramInt)
  {
    return paramInt >= LEVEL;
  }

  public static void v(String paramString)
  {
    if (2 >= LEVEL)
      android.util.Log.v(TAG_DEFAULT, paramString);
  }

  public static void v(String paramString1, String paramString2)
  {
    if (2 >= LEVEL)
      android.util.Log.v(paramString1, paramString2);
  }

  public static void v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (2 >= LEVEL)
      android.util.Log.v(paramString1, paramString2, paramThrowable);
  }

  public static void w(String paramString)
  {
    if (5 >= LEVEL)
      android.util.Log.w(TAG_DEFAULT, paramString);
  }

  public static void w(String paramString1, String paramString2)
  {
    if (5 >= LEVEL)
      android.util.Log.w(paramString1, paramString2);
  }

  public static void w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (5 >= LEVEL)
      android.util.Log.w(paramString1, paramString2, paramThrowable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.Log
 * JD-Core Version:    0.6.0
 */