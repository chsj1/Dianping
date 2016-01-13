package com.dianping.monitor.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.monitor.MonitorService;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;

public class DefaultMonitorService
  implements MonitorService
{
  static final Handler handler = new Handler(Looper.getMainLooper());
  final LinkedList<String> buffer = new LinkedList();
  final Context context;
  final NetworkInfoHelper networkInfo;
  final SharedPreferences prefs;
  boolean suspend;
  private final Runnable upload = new Runnable()
  {
    public void run()
    {
      new DefaultMonitorService.UploadThread(DefaultMonitorService.this, null).start();
    }
  };
  String url = null;
  final int version;

  public DefaultMonitorService(Context paramContext, String paramString)
  {
    this.context = paramContext;
    this.url = paramString;
    this.networkInfo = new NetworkInfoHelper(paramContext);
    this.prefs = paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
    int i = 0;
    try
    {
      int j = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionCode;
      i = j;
      label89: this.version = i;
      return;
    }
    catch (Exception paramContext)
    {
      break label89;
    }
  }

  public void flush()
  {
    handler.removeCallbacks(this.upload);
    handler.post(this.upload);
  }

  public String getCommand(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    int j = paramString.indexOf('?');
    int i = j;
    if (j < 0)
      i = paramString.length();
    int k = paramString.lastIndexOf('/', i);
    j = k;
    if (k < 0)
      j = -1;
    return paramString.substring(j + 1, i);
  }

  public void pv(long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    pv3(paramLong, paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, null);
  }

  public void pv3(long paramLong, String arg3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    long l = paramLong;
    if (paramLong == 0L)
      l = System.currentTimeMillis();
    localStringBuilder.append(l);
    localStringBuilder.append('\t');
    int i = paramInt1;
    if (paramInt1 == 0)
      i = this.networkInfo.getNetworkType();
    localStringBuilder.append(i);
    localStringBuilder.append('\t');
    localStringBuilder.append(this.version);
    localStringBuilder.append('\t');
    localStringBuilder.append(paramInt2);
    localStringBuilder.append('\t');
    try
    {
      localStringBuilder.append(URLEncoder.encode(???, "utf-8"));
      localStringBuilder.append('\t');
      paramInt1 = paramInt3;
      if (paramInt3 / 100 == -1)
      {
        paramInt1 = paramInt3;
        if (!NetworkInfoHelper.isNetworkConnected(this.context))
          paramInt1 = -199;
      }
      localStringBuilder.append(paramInt1);
      localStringBuilder.append('\t');
      localStringBuilder.append("1\t");
      localStringBuilder.append(paramInt4);
      localStringBuilder.append('\t');
      localStringBuilder.append(paramInt5);
      localStringBuilder.append('\t');
      localStringBuilder.append(paramInt6);
      localStringBuilder.append('\t');
      ??? = paramString2;
      if (paramString2 == null)
        ??? = "";
      localStringBuilder.append(???);
    }
    catch (Exception localException)
    {
      do
        synchronized (this.buffer)
        {
          while (true)
          {
            paramInt1 = this.buffer.size();
            while (paramInt1 > 16)
            {
              this.buffer.removeFirst();
              paramInt1 -= 1;
            }
            localException = localException;
            localException.printStackTrace();
            localStringBuilder.append(???);
          }
          this.buffer.addLast(localStringBuilder.toString());
          if ((this.suspend) || (paramInt1 != 0))
            continue;
          handler.removeCallbacks(this.upload);
          handler.postDelayed(this.upload, 15000L);
          return;
        }
      while ((this.suspend) || (paramInt1 <= 15));
      handler.removeCallbacks(this.upload);
      handler.post(this.upload);
    }
  }

  public void setSuspending(boolean paramBoolean)
  {
    this.suspend = paramBoolean;
  }

  private class UploadThread extends Thread
  {
    private UploadThread()
    {
    }

    public void run()
    {
      long l1 = SystemClock.elapsedRealtime();
      StringBuilder localStringBuilder = new StringBuilder();
      ??? = DefaultMonitorService.this.prefs.getString("dpid", null);
      Object localObject1 = ???;
      if (??? == null)
        localObject1 = "";
      localStringBuilder.append("v=3&dpid=" + (String)localObject1 + "&c=\n");
      localObject1 = new LinkedList();
      synchronized (DefaultMonitorService.this.buffer)
      {
        if (DefaultMonitorService.this.buffer.size() <= 0)
          break label353;
        Iterator localIterator = DefaultMonitorService.this.buffer.iterator();
        if (localIterator.hasNext())
          localStringBuilder.append((String)localIterator.next()).append('\n');
      }
      localObject2.addAll(DefaultMonitorService.this.buffer);
      DefaultMonitorService.this.buffer.clear();
      monitorexit;
      int j;
      if (!DefaultMonitorService.this.suspend)
        j = 1;
      while (true)
      {
        int i = 0;
        try
        {
          ??? = (HttpURLConnection)new URL(DefaultMonitorService.this.url).openConnection();
          ((HttpURLConnection)???).addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
          ((HttpURLConnection)???).setReadTimeout(15000);
          ((HttpURLConnection)???).setDoOutput(true);
          ((HttpURLConnection)???).setRequestMethod("POST");
          ((HttpURLConnection)???).getOutputStream().write(localStringBuilder.toString().getBytes("utf-8"));
          int k = ((HttpURLConnection)???).getResponseCode();
          ((HttpURLConnection)???).disconnect();
          k /= 100;
          label277: boolean bool;
          if (k == 2)
          {
            i = 1;
            if ((i == 0) && (j != 0))
            {
              synchronized (DefaultMonitorService.this.buffer)
              {
                bool = DefaultMonitorService.this.buffer.isEmpty();
                if ((localObject2.size() <= 0) || (DefaultMonitorService.this.buffer.size() >= 16))
                  break label368;
                DefaultMonitorService.this.buffer.addFirst(localObject2.removeLast());
              }
              label353: monitorexit;
            }
          }
          label368: 
          do
          {
            return;
            j = 0;
            break;
            i = 0;
            break label277;
            monitorexit;
          }
          while ((!bool) || (DefaultMonitorService.this.suspend));
          long l2 = 15000L - (SystemClock.elapsedRealtime() - l1);
          l1 = l2;
          if (l2 > 15000L)
            l1 = 15000L;
          l2 = l1;
          if (l1 < 3000L)
            l2 = 3000L;
          DefaultMonitorService.handler.removeCallbacks(DefaultMonitorService.this.upload);
          DefaultMonitorService.handler.postDelayed(DefaultMonitorService.this.upload, l2);
          return;
        }
        catch (Exception localException)
        {
          break label277;
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.monitor.impl.DefaultMonitorService
 * JD-Core Version:    0.6.0
 */