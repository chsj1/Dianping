package com.dianping.dataservice.image.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.cache.impl.BasicCacheResponse;
import com.dianping.dataservice.cache.impl.BlobCacheService;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.util.Log;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class ImageCacheService
  implements CacheService
{
  private static final String TAG = "image";
  private Context context;
  private final Handler mhandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramMessage)
    {
      paramMessage = (HttpRequest)paramMessage.obj;
      RequestHandler localRequestHandler = (RequestHandler)ImageCacheService.this.runningFails.remove(paramMessage);
      if (localRequestHandler != null)
        localRequestHandler.onRequestFailed(paramMessage, ImageCacheService.this.getFailResponse(paramMessage));
    }
  };
  private BlobCacheService photoCache;
  private final ConcurrentHashMap<HttpRequest, RequestHandler<HttpRequest, HttpResponse>> runningFails = new ConcurrentHashMap();
  private BlobCacheService thumbnailCache;

  public ImageCacheService(Context paramContext)
  {
    this.context = paramContext;
  }

  private BasicCacheResponse getFailResponse(Request paramRequest)
  {
    return new BasicCacheResponse(0L, null, null, "not found (type=0): " + paramRequest.url());
  }

  public static File getPhotoCacheFile(Context paramContext)
  {
    return new File(paramContext.getCacheDir(), "photo.db");
  }

  public static File getThunbnailCacheFile(Context paramContext)
  {
    return new File(paramContext.getCacheDir(), "thumb.db");
  }

  private BlobCacheService photoCache()
  {
    monitorenter;
    try
    {
      File localFile;
      Object localObject1;
      if (this.photoCache == null)
      {
        localFile = getPhotoCacheFile(this.context);
        localObject1 = null;
      }
      try
      {
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localFile, null);
        localObject1 = localSQLiteDatabase;
        this.photoCache = new BlobCacheService((SQLiteDatabase)localObject1, "photo");
        localObject1 = this.photoCache;
        monitorexit;
        return localObject1;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("image", "cannot open database " + localFile.getAbsolutePath(), localException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private BlobCacheService thumbnailCache()
  {
    monitorenter;
    try
    {
      File localFile;
      Object localObject1;
      if (this.thumbnailCache == null)
      {
        localFile = getThunbnailCacheFile(this.context);
        localObject1 = null;
      }
      try
      {
        SQLiteDatabase localSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(localFile, null);
        localObject1 = localSQLiteDatabase;
        this.thumbnailCache = new BlobCacheService((SQLiteDatabase)localObject1, "thumb");
        localObject1 = this.thumbnailCache;
        monitorexit;
        return localObject1;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("image", "cannot open database " + localFile.getAbsolutePath(), localException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private int typeOf(Request paramRequest)
  {
    if ((paramRequest instanceof ImageRequest))
      return ((ImageRequest)paramRequest).type();
    return 0;
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    this.runningFails.remove(paramHttpRequest, paramRequestHandler);
  }

  public void clear()
  {
    thumbnailCache().clear();
    photoCache().clear();
  }

  public void close()
  {
    monitorenter;
    try
    {
      if (this.thumbnailCache != null)
        this.thumbnailCache.close();
      if (this.photoCache != null)
        this.photoCache.close();
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
    switch (typeOf(paramHttpRequest))
    {
    default:
      this.runningFails.put(paramHttpRequest, paramRequestHandler);
      this.mhandler.sendMessage(this.mhandler.obtainMessage(0, paramHttpRequest));
      return;
    case 1:
      thumbnailCache().exec(paramHttpRequest, paramRequestHandler);
      return;
    case 2:
    }
    photoCache().exec(paramHttpRequest, paramRequestHandler);
  }

  public BasicCacheResponse execSync(HttpRequest paramHttpRequest)
  {
    switch (typeOf(paramHttpRequest))
    {
    default:
      return getFailResponse(paramHttpRequest);
    case 1:
      return thumbnailCache().execSync(paramHttpRequest);
    case 2:
    }
    return photoCache().execSync(paramHttpRequest);
  }

  public boolean put(Request paramRequest, HttpResponse paramHttpResponse, long paramLong)
  {
    switch (typeOf(paramRequest))
    {
    default:
      return true;
    case 1:
      return thumbnailCache().put(paramRequest, paramHttpResponse, paramLong);
    case 2:
    }
    return photoCache().put(paramRequest, paramHttpResponse, paramLong);
  }

  public void remove(Request paramRequest)
  {
    switch (typeOf(paramRequest))
    {
    default:
      return;
    case 1:
      thumbnailCache().remove(paramRequest);
      return;
    case 2:
    }
    photoCache().remove(paramRequest);
  }

  public boolean touch(Request paramRequest, long paramLong)
  {
    switch (typeOf(paramRequest))
    {
    default:
      return true;
    case 1:
      return thumbnailCache().touch(paramRequest, paramLong);
    case 2:
    }
    return photoCache().touch(paramRequest, paramLong);
  }

  public int trimToCount(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default:
    case 1:
    case 2:
    }
    do
    {
      do
        return 0;
      while (this.thumbnailCache == null);
      return this.thumbnailCache.trimToCount(paramInt2);
    }
    while (this.photoCache == null);
    return this.photoCache.trimToCount(paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.image.impl.ImageCacheService
 * JD-Core Version:    0.6.0
 */