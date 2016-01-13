package com.dianping.dataservice.mapi.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.cache.impl.BlobCacheService;
import com.dianping.dataservice.cache.impl.ServiceCacheService;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.monitor.MonitorService;
import com.dianping.util.Log;
import java.io.File;

public class MApiCacheService
  implements CacheService
{
  private static final String TAG = "mapi";
  private BlobCacheService cache0;
  private BlobCacheService cache1;
  private ServiceCacheService cache2;
  private Context context;
  private HttpService httpService;
  private MonitorService monitorService;

  public MApiCacheService(Context paramContext, HttpService paramHttpService, MonitorService paramMonitorService)
  {
    this.context = paramContext;
    this.httpService = paramHttpService;
    this.monitorService = paramMonitorService;
  }

  private BlobCacheService cache0()
  {
    monitorenter;
    try
    {
      File localFile;
      Object localObject1;
      if (this.cache0 == null)
      {
        localFile = getCacheFile(this.context);
        localObject1 = null;
      }
      try
      {
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localFile, null);
        localObject1 = localSQLiteDatabase;
        this.cache0 = new BlobCacheService((SQLiteDatabase)localObject1, "c0");
        localObject1 = this.cache0;
        monitorexit;
        return localObject1;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("mapi", "cannot open database " + localFile.getAbsolutePath(), localException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private BlobCacheService cache1()
  {
    monitorenter;
    try
    {
      File localFile;
      Object localObject1;
      if (this.cache1 == null)
      {
        localFile = getCacheFile(this.context);
        localObject1 = null;
      }
      try
      {
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localFile, null);
        localObject1 = localSQLiteDatabase;
        this.cache1 = new BlobCacheService((SQLiteDatabase)localObject1, "c1");
        localObject1 = this.cache1;
        monitorexit;
        return localObject1;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("mapi", "cannot open database " + localFile.getAbsolutePath(), localException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private ServiceCacheService cache2()
  {
    monitorenter;
    try
    {
      File localFile;
      Object localObject1;
      if (this.cache2 == null)
      {
        localFile = getCacheFile(this.context);
        localObject1 = null;
      }
      try
      {
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localFile, null);
        localObject1 = localSQLiteDatabase;
        this.cache2 = new ServiceCacheService(this.httpService, this.monitorService, (SQLiteDatabase)localObject1, "c2");
        localObject1 = this.cache2;
        monitorexit;
        return localObject1;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("mapi", "cannot open database " + localFile.getAbsolutePath(), localException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private CacheService getCache(Request paramRequest)
  {
    if ((paramRequest instanceof MApiRequest));
    switch (1.$SwitchMap$com$dianping$dataservice$mapi$CacheType[((MApiRequest)paramRequest).defaultCacheType().ordinal()])
    {
    default:
      return cache1();
    case 1:
      return cache0();
    case 2:
    }
    return cache2();
  }

  public static File getCacheFile(Context paramContext)
  {
    return new File(paramContext.getCacheDir(), "mapi.db");
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    getCache(paramHttpRequest).abort(paramHttpRequest, paramRequestHandler, paramBoolean);
  }

  public void clear()
  {
    Log.i("mapi", "mapi cache clear");
    cache0().clear();
    cache1().clear();
    cache2().clear();
  }

  public void close()
  {
    monitorenter;
    try
    {
      if (this.cache0 != null)
      {
        this.cache0.close();
        this.cache0 = null;
      }
      if (this.cache1 != null)
      {
        this.cache1.close();
        this.cache1 = null;
      }
      if (this.cache2 != null)
      {
        this.cache2.close();
        this.cache2 = null;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    getCache(paramHttpRequest).exec(paramHttpRequest, paramRequestHandler);
  }

  public HttpResponse execSync(HttpRequest paramHttpRequest)
  {
    return (HttpResponse)getCache(paramHttpRequest).execSync(paramHttpRequest);
  }

  public boolean put(Request paramRequest, HttpResponse paramHttpResponse, long paramLong)
  {
    return getCache(paramRequest).put(paramRequest, paramHttpResponse, paramLong);
  }

  public void remove(Request paramRequest)
  {
    if (Log.isLoggable(3))
      Log.d("mapi", "mapi cache delete " + paramRequest.url());
    getCache(paramRequest).remove(paramRequest);
  }

  public boolean touch(Request paramRequest, long paramLong)
  {
    return getCache(paramRequest).touch(paramRequest, paramLong);
  }

  public int trimToCount(int paramInt)
  {
    if (this.cache1 != null)
      return this.cache1.trimToCount(paramInt);
    return 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.MApiCacheService
 * JD-Core Version:    0.6.0
 */