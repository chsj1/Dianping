package com.dianping.widget.pulltorefresh.internal;

import com.dianping.util.Log;

public class Utils
{
  static final String LOG_TAG = "PullToRefresh";

  public static void warnDeprecation(String paramString1, String paramString2)
  {
    Log.w("PullToRefresh", "You're using the deprecated " + paramString1 + " attr, please switch over to " + paramString2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.Utils
 * JD-Core Version:    0.6.0
 */