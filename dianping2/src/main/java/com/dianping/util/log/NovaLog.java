package com.dianping.util.log;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class NovaLog
{
  private static ArrayList<Appender> appenderArrayList = new ArrayList();

  public static void e(String paramString1, String paramString2)
  {
    println(4, paramString1, paramString2);
  }

  public static void i(String paramString1, String paramString2)
  {
    println(3, paramString1, paramString2);
  }

  public static void initialAppenders()
  {
    int i = 0;
    if (i < LogConfig.outputTargetList.length)
    {
      if (LogConfig.outputTargetList[i] == 0)
        appenderArrayList.add(new ConsoleAppender());
      while (true)
      {
        i += 1;
        break;
        if (LogConfig.outputTargetList[i] == 1)
        {
          appenderArrayList.add(FileAppender.getInstance());
          continue;
        }
        Log.e("NOVA_LOG", "appender:" + LogConfig.outputTargetList[i] + " is unsupported.");
      }
    }
    if ((appenderArrayList != null) && (!appenderArrayList.isEmpty()))
    {
      Iterator localIterator = appenderArrayList.iterator();
      while (localIterator.hasNext())
        ((Appender)localIterator.next()).open();
    }
  }

  private static void println(int paramInt, String paramString1, String paramString2)
  {
    if ((appenderArrayList == null) || (appenderArrayList.isEmpty()));
    while (true)
    {
      return;
      int i = 0;
      while (i < appenderArrayList.size())
      {
        println((Appender)appenderArrayList.get(i), paramInt, paramString1, paramString2);
        i += 1;
      }
    }
  }

  private static void println(Appender paramAppender, int paramInt, String paramString1, String paramString2)
  {
    switch (paramInt)
    {
    default:
      return;
    case 3:
      paramAppender.i(paramInt, paramString1, paramString2);
      return;
    case 4:
    }
    paramAppender.e(paramInt, paramString1, paramString2);
  }

  public static void removeAllAppenders()
  {
    if ((appenderArrayList != null) && (!appenderArrayList.isEmpty()))
    {
      Iterator localIterator = appenderArrayList.iterator();
      while (localIterator.hasNext())
        ((Appender)localIterator.next()).close();
      appenderArrayList.clear();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.log.NovaLog
 * JD-Core Version:    0.6.0
 */