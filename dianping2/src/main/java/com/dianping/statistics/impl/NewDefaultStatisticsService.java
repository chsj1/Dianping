package com.dianping.statistics.impl;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.monitor.MonitorService;
import com.dianping.statistics.StatisticsService;
import com.dianping.statistics.utils.StatisticsInitializer;
import com.dianping.util.Daemon;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONObject;

public abstract class NewDefaultStatisticsService
  implements StatisticsService
{
  private static final int CHECK_AND_UPLOAD = 1;
  private static final int FORCE_UPLOAD = 2;
  private static final String TAG = "newstatistics";
  private static final int UPLOAD_AND_CLOSE = 3;
  private Context context;
  private SQLiteDatabase db;
  private boolean drain;
  final Handler handler = new Handler(Daemon.looper())
  {
    public void handleMessage(Message paramMessage)
    {
      if (NewDefaultStatisticsService.this.drain)
        return;
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        NewDefaultStatisticsService.this.flush();
        return;
      case 2:
        label70: int j;
        if ((paramMessage.obj instanceof String))
        {
          paramMessage = (String)(String)paramMessage.obj;
          j = NewDefaultStatisticsService.this.sdb.count();
          if (j >= NewDefaultStatisticsService.this.maxCount)
            break label289;
          if (NewDefaultStatisticsService.this.sdb.push(paramMessage) < 0L)
            break label283;
        }
        label283: for (int i = 1; i != 0; i = 0)
        {
          NewDefaultStatisticsService.this.item.put(1);
          if (j != 0)
            break;
          removeMessages(1);
          sendEmptyMessageDelayed(1, NewDefaultStatisticsService.this.uploadInterval);
          return;
          if ((paramMessage.obj instanceof Map))
          {
            Object localObject = (Map)(Map)paramMessage.obj;
            paramMessage = new JSONObject();
            try
            {
              localObject = ((Map)localObject).entrySet().iterator();
              while (((Iterator)localObject).hasNext())
              {
                Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
                if (TextUtils.isEmpty((CharSequence)localEntry.getValue()))
                  continue;
                String str = URLEncoder.encode((String)localEntry.getValue(), "UTF-8");
                paramMessage.putOpt((String)localEntry.getKey(), str);
              }
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
              paramMessage = paramMessage.toString();
            }
            break label70;
          }
          paramMessage = "";
          break label70;
        }
        label289: paramMessage = NewDefaultStatisticsService.this.sdb.getLastRows(NewDefaultStatisticsService.this.pDeleteCount);
        NewDefaultStatisticsService.this.sdb.delete(paramMessage, 0, NewDefaultStatisticsService.this.pDeleteCount);
        return;
      case 3:
      }
      NewDefaultStatisticsService.this.item.put(3);
    }
  };
  private HttpService httpService;
  private final MaxBlockingItem item = new MaxBlockingItem();
  private MApiService mapiService;
  private final int maxCount;
  private final int maxUploadCount;
  private MonitorService monitorService;
  private final int pDeleteCount;
  private StatisticsDB sdb;
  private boolean suspend;
  final Thread thread = new Thread("Statistics")
  {
    public void run()
    {
      int[] arrayOfInt = new int[NewDefaultStatisticsService.this.maxUploadCount];
      String[] arrayOfString = new String[NewDefaultStatisticsService.this.maxUploadCount];
      StringBuilder localStringBuilder = new StringBuilder();
      while (true)
      {
        if (!NewDefaultStatisticsService.this.drain);
        try
        {
          int j = NewDefaultStatisticsService.this.item.take();
          if ((j == -1) || ((j == 1) && (NewDefaultStatisticsService.this.sdb.count() < NewDefaultStatisticsService.this.uploadCount)))
            continue;
          k = NewDefaultStatisticsService.this.sdb.read(arrayOfInt, arrayOfString);
          if (k <= 0)
            continue;
          localStringBuilder.setLength(0);
          localStringBuilder.append("[");
          i = 0;
          while ((i < k) && (!TextUtils.isEmpty(arrayOfString[i])))
          {
            localStringBuilder.append(arrayOfString[i]).append(",");
            i += 1;
          }
          localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
          localStringBuilder.append("]");
          if (NewDefaultStatisticsService.this.suspend)
            Log.i("newstatistics", "suspending...");
          while ((j == 3) && (NewDefaultStatisticsService.this.drain))
          {
            NewDefaultStatisticsService.this.sdb.close();
            NewDefaultStatisticsService.this.db.close();
            Log.i("newstatistics", "statistics service closed");
            break;
            Object localObject = BasicMApiRequest.mapiPostGzipString(NewDefaultStatisticsService.this.url, "applogjson=" + localStringBuilder.toString(), true, null);
            localHttpResponse = (HttpResponse)NewDefaultStatisticsService.this.mapiService.execSync((Request)localObject);
            if (localHttpResponse.statusCode() / 100 != 2)
              break label400;
            NewDefaultStatisticsService.this.sdb.delete(arrayOfInt, 0, k);
            Log.i("newstatistics", "upload finished, count = " + k);
            localObject = new Intent("com.dianping.action.STAT_UPLOAD_SUCCESS");
            ((Intent)localObject).putExtra("count", k);
            NewDefaultStatisticsService.this.context.sendBroadcast((Intent)localObject);
          }
        }
        catch (Exception localIntent)
        {
          HttpResponse localHttpResponse;
          while (true)
          {
            int k;
            Log.i("newstatistics", "", localException);
            break;
            label400: if (((HttpResponse)NewDefaultStatisticsService.this.httpService.execSync(localException)).statusCode() / 100 != 2)
              break label505;
            NewDefaultStatisticsService.this.sdb.delete(arrayOfInt, 0, k);
            Log.i("newstatistics", "upload finished, count = " + k);
            Intent localIntent = new Intent("com.dianping.action.STAT_UPLOAD_SUCCESS");
            localIntent.putExtra("count", k);
            NewDefaultStatisticsService.this.context.sendBroadcast(localIntent);
          }
          label505: if (localHttpResponse.statusCode() == 0);
          for (int i = -100; ; i = localHttpResponse.statusCode())
          {
            NewDefaultStatisticsService.this.monitorService.pv(0L, "statistics_failed", 0, 0, i, 0, 0, 0);
            break;
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

  public NewDefaultStatisticsService(Context paramContext, String paramString, StatisticsInitializer paramStatisticsInitializer)
  {
    this.context = paramContext;
    this.url = paramStatisticsInitializer.uploadString;
    this.uploadCount = paramStatisticsInitializer.uploadCount;
    this.maxCount = paramStatisticsInitializer.maxCount;
    this.maxUploadCount = paramStatisticsInitializer.maxUploadCount;
    this.uploadInterval = paramStatisticsInitializer.uploadInterval;
    this.pDeleteCount = paramStatisticsInitializer.pDeleteCount;
    try
    {
      this.db = paramContext.getApplicationContext().openOrCreateDatabase(paramString, 0, null);
      this.sdb = new StatisticsDB(this.db);
      this.thread.start();
      this.item.put(2);
      this.mapiService = paramStatisticsInitializer.mapiService;
      this.httpService = paramStatisticsInitializer.httpService;
      this.monitorService = paramStatisticsInitializer.monitorService;
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

  public void push(Map<String, String> paramMap)
  {
    if (this.drain)
      return;
    this.handler.sendMessage(this.handler.obtainMessage(2, paramMap));
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
 * Qualified Name:     com.dianping.statistics.impl.NewDefaultStatisticsService
 * JD-Core Version:    0.6.0
 */