package com.dianping.statistics.impl;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.impl.DefaultHttpService;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Daemon;
import com.dianping.util.Log;
import com.dianping.util.URLEncodedUtils;
import java.util.List;
import org.apache.http.NameValuePair;

public abstract class DefaultStatisticsService
  implements StatisticsService
{
  private static final int CHECK_AND_UPLOAD = 1;
  private static final int FORCE_UPLOAD = 2;
  private static final String TAG = "statistics";
  private static final int UPLOAD_AND_CLOSE = 3;
  private Context context;
  private SQLiteDatabase db;
  private boolean drain;
  final Handler handler = new Handler(Daemon.looper())
  {
    public void handleMessage(Message paramMessage)
    {
      if (DefaultStatisticsService.this.drain);
      int j;
      do
      {
        return;
        switch (paramMessage.what)
        {
        default:
          return;
        case 1:
          DefaultStatisticsService.this.flush();
          return;
        case 2:
          if ((paramMessage.obj instanceof String))
            paramMessage = (String)(String)paramMessage.obj;
          while (true)
          {
            localObject = paramMessage.toCharArray();
            j = localObject.length;
            int k = localObject.length;
            i = 0;
            while (i < k)
            {
              j = j * 31 + localObject[i];
              i += 1;
            }
            if ((paramMessage.obj instanceof List))
            {
              paramMessage = URLEncodedUtils.format((List)(List)paramMessage.obj, "utf-8");
              continue;
            }
            paramMessage = "";
          }
          for (Object localObject = Integer.toHexString(0xFFFF & j ^ j >>> 16); ((String)localObject).length() < 4; localObject = "0" + (String)localObject);
          paramMessage = paramMessage + "&s=" + (String)localObject;
          j = DefaultStatisticsService.this.sdb.count();
        case 3:
        }
      }
      while (j >= DefaultStatisticsService.this.maxCount);
      if (DefaultStatisticsService.this.sdb.push(paramMessage) >= 0L);
      for (int i = 1; i != 0; i = 0)
      {
        DefaultStatisticsService.this.item.put(1);
        if (j != 0)
          break;
        removeMessages(1);
        sendEmptyMessageDelayed(1, DefaultStatisticsService.this.uploadInterval);
        return;
      }
      DefaultStatisticsService.this.item.put(3);
    }
  };
  private DefaultHttpService httpService;
  private final MaxBlockingItem item = new MaxBlockingItem();
  private final int maxCount;
  private StatisticsDB sdb;
  private boolean suspend;
  final Thread thread = new Thread("Statistics")
  {
    public void run()
    {
      int[] arrayOfInt = new int[DefaultStatisticsService.this.maxCount];
      String[] arrayOfString = new String[DefaultStatisticsService.this.maxCount];
      StringBuilder localStringBuilder = new StringBuilder(16384);
      while (true)
      {
        if (!DefaultStatisticsService.this.drain);
        try
        {
          int j = DefaultStatisticsService.this.item.take();
          if ((j == -1) || ((j == 1) && (DefaultStatisticsService.this.sdb.count() < DefaultStatisticsService.this.uploadCount)))
            continue;
          int k = DefaultStatisticsService.this.sdb.read(arrayOfInt, arrayOfString);
          if (k <= 0)
            continue;
          localStringBuilder.setLength(0);
          int i = 0;
          while (i < k)
          {
            localStringBuilder.append(arrayOfString[i]).append('\n');
            i += 1;
          }
          if (DefaultStatisticsService.this.suspend)
            Log.i("statistics", "suspending...");
          while ((j == 3) && (DefaultStatisticsService.this.drain))
          {
            DefaultStatisticsService.this.sdb.close();
            DefaultStatisticsService.this.db.close();
            Log.i("statistics", "statistics service closed");
            break;
            Object localObject = new BasicHttpRequest(DefaultStatisticsService.this.url, "POST", new StringInputStream(localStringBuilder.toString(), "UTF-8"));
            localObject = DefaultStatisticsService.this.httpService.execSync((HttpRequest)localObject);
            if (((HttpResponse)localObject).statusCode() / 100 != 2)
              break label354;
            DefaultStatisticsService.this.sdb.delete(arrayOfInt, 0, k);
            Log.i("statistics", "upload finished, count = " + k);
            localObject = new Intent("com.dianping.action.STAT_UPLOAD_SUCCESS");
            ((Intent)localObject).putExtra("count", k);
            DefaultStatisticsService.this.context.sendBroadcast((Intent)localObject);
          }
        }
        catch (Exception localException)
        {
          while (true)
          {
            Log.i("statistics", "", localException);
            break;
            label354: Log.i("statistics", "failed to upload, statusCode = " + localException.statusCode());
          }
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
    }
  };
  private final int uploadCount;
  private int uploadInterval;
  private String url;

  public DefaultStatisticsService(Context paramContext, String paramString1, String paramString2)
  {
    this(paramContext, paramString1, paramString2, 16, 64, 15000);
  }

  public DefaultStatisticsService(Context paramContext, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    this.context = paramContext;
    this.url = paramString2;
    this.uploadCount = paramInt1;
    this.maxCount = paramInt2;
    this.uploadInterval = paramInt3;
    try
    {
      this.db = paramContext.getApplicationContext().openOrCreateDatabase(paramString1, 0, null);
      this.sdb = new StatisticsDB(this.db);
      this.thread.start();
      this.item.put(1);
      this.httpService = new DefaultHttpService(paramContext, null);
      return;
    }
    catch (Exception paramContext)
    {
      this.drain = true;
      Log.e("statistics", "fail to initialize statistics database", paramContext);
    }
  }

  public void close()
  {
    if (this.drain)
      return;
    this.drain = true;
    this.handler.sendEmptyMessage(3);
  }

  public void flush()
  {
    if (this.drain)
      return;
    this.item.put(2);
  }

  public boolean isClosed()
  {
    return (this.drain) && (!this.thread.isAlive());
  }

  public boolean isSuspending()
  {
    return this.suspend;
  }

  public void push(String paramString)
  {
    if (this.drain)
      return;
    this.handler.sendMessage(this.handler.obtainMessage(2, paramString));
  }

  public void push(List<NameValuePair> paramList)
  {
    if (this.drain)
      return;
    this.handler.sendMessage(this.handler.obtainMessage(2, paramList));
  }

  public void setSuspending(boolean paramBoolean)
  {
    this.suspend = paramBoolean;
  }

  protected void setUploadInterval(int paramInt)
  {
    this.uploadInterval = paramInt;
  }

  protected void setUploadUrl(String paramString)
  {
    this.url = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.DefaultStatisticsService
 * JD-Core Version:    0.6.0
 */