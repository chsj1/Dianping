package com.dianping.dataservice.image.impl;

import B;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.impl.BasicHttpResponse;
import com.dianping.dataservice.http.impl.DefaultHttpService;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.impl.BasicResponse;
import com.dianping.monitor.MonitorService;
import com.dianping.util.BlockingItem;
import com.dianping.util.Daemon;
import com.dianping.util.Log;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultImageService
  implements ImageService
{
  private static final String TAG = "image";
  private ImageCacheService cache;
  private final RequestHandler<HttpRequest, HttpResponse> cacheHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpResponse = (DefaultImageService.Session)DefaultImageService.this.runningSession.get(paramHttpRequest);
      if ((paramHttpResponse == null) || (paramHttpResponse.status != 1))
        return;
      if ((paramHttpRequest instanceof ImageRequest));
      for (boolean bool = ((ImageRequest)paramHttpRequest).cacheOnly(); bool; bool = false)
      {
        DefaultImageService.this.runningSession.remove(paramHttpRequest, paramHttpResponse);
        paramHttpResponse.handler.onRequestFailed(paramHttpRequest, new BasicResponse(null, "cache only"));
        return;
      }
      paramHttpResponse.status = 2;
      DefaultImageService.this.http().exec(paramHttpRequest, DefaultImageService.this.httpHandler);
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (DefaultImageService.Session)DefaultImageService.this.runningSession.get(paramHttpRequest);
      if ((paramHttpRequest == null) || (paramHttpRequest.status != 1))
        return;
      paramHttpRequest.response = paramHttpResponse;
      paramHttpRequest.status = 3;
      DefaultImageService.this.dhandler.sendMessage(DefaultImageService.this.dhandler.obtainMessage(1, paramHttpRequest));
    }
  };
  private Context context;
  private final Handler dhandler = new Handler(createLooper("decode"))
  {
    public void handleMessage(Message paramMessage)
    {
      DefaultImageService.Session localSession = (DefaultImageService.Session)paramMessage.obj;
      Object localObject1 = null;
      Object localObject4 = null;
      try
      {
        localObject3 = (byte[])(byte[])localSession.response.result();
        localObject1 = localObject3;
        Bitmap localBitmap = BitmapFactory.decodeByteArray(localObject3, 0, localObject3.length);
        if (localSession.status == 3)
        {
          if (localBitmap == null)
          {
            localObject1 = new BasicResponse(null, "fail to decode bitmap");
            localSession.response = ((Response)localObject1);
            localSession.writeToCache = ((B)localObject3);
            localSession.status = 4;
            localObject1 = DefaultImageService.this.mhandler;
            localObject3 = DefaultImageService.this.mhandler;
            if (localBitmap != null)
              break label237;
            i = 3;
            ((Handler)localObject1).sendMessage(((Handler)localObject3).obtainMessage(i, localSession));
          }
        }
        else
        {
          if (localBitmap != null)
          {
            if (paramMessage.what != 1)
              break label243;
            DefaultImageService.this.whandler.sendMessageDelayed(DefaultImageService.this.whandler.obtainMessage(paramMessage.what, localSession), 600L);
          }
          return;
        }
      }
      catch (Throwable localObject2)
      {
        while (true)
        {
          Object localObject2 = localObject4;
          Object localObject3 = localObject1;
          if (!Log.isLoggable(6))
            continue;
          Log.e("image", "unable to decode image " + localSession.request.url());
          localObject2 = localObject4;
          localObject3 = localObject1;
          continue;
          localObject1 = new BasicResponse(localObject2, null);
          continue;
          label237: int i = 2;
        }
        label243: DefaultImageService.this.whandler.sendMessage(DefaultImageService.this.whandler.obtainMessage(paramMessage.what, localSession));
      }
    }
  };
  private DefaultHttpService http;
  private final FullRequestHandle<HttpRequest, HttpResponse> httpHandler = new FullRequestHandle()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultImageService.Session localSession = (DefaultImageService.Session)DefaultImageService.this.runningSession.remove(paramHttpRequest);
      if ((localSession == null) || (localSession.status != 2))
        return;
      long l;
      if ((DefaultImageService.this.monitor != null) && (!localSession.request.disableStatistics()))
      {
        if (localSession.time >= 0L)
          break label215;
        l = localSession.time + SystemClock.elapsedRealtime();
      }
      while (true)
      {
        int j = paramHttpResponse.statusCode();
        int i = j;
        if (j == 0)
          i = -100;
        j = localSession.requestBytes;
        DefaultImageService.this.monitor.pv(0L, "_pic_" + localSession.request.url(), 0, 0, i, j, 0, (int)l);
        if (!TextUtils.isEmpty(localSession.request.imageModule()))
          DefaultImageService.this.monitor.pv(0L, "pic.down." + localSession.request.imageModule(), 0, 0, i, j, 0, (int)l);
        localSession.handler.onRequestFailed(paramHttpRequest, paramHttpResponse);
        return;
        label215: l = localSession.time;
      }
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultImageService.Session localSession = (DefaultImageService.Session)DefaultImageService.this.runningSession.get(paramHttpRequest);
      if ((localSession == null) || (localSession.status != 2))
        return;
      long l;
      int j;
      int k;
      if ((DefaultImageService.this.monitor != null) && (!localSession.request.disableStatistics()))
      {
        if (localSession.time >= 0L)
          break label266;
        l = localSession.time + SystemClock.elapsedRealtime();
        j = paramHttpResponse.statusCode();
        k = localSession.requestBytes;
        if (!(paramHttpResponse.result() instanceof byte[]))
          break label275;
      }
      label266: label275: for (int i = ((byte[])(byte[])paramHttpResponse.result()).length; ; i = 0)
      {
        DefaultImageService.this.monitor.pv(0L, "_pic_" + localSession.request.url(), 0, 0, j, k, i, (int)l);
        if (!TextUtils.isEmpty(localSession.request.imageModule()))
          DefaultImageService.this.monitor.pv(0L, "pic.down." + localSession.request.imageModule(), 0, 0, j, k, i, (int)l);
        if (paramHttpResponse.statusCode() / 100 != 2)
          break label281;
        localSession.response = paramHttpResponse;
        localSession.status = 3;
        DefaultImageService.this.dhandler.sendMessage(DefaultImageService.this.dhandler.obtainMessage(2, localSession));
        return;
        l = localSession.time;
        break;
      }
      label281: DefaultImageService.this.runningSession.remove(paramHttpRequest, localSession);
      localSession.handler.onRequestFailed(paramHttpRequest, paramHttpResponse);
    }

    public void onRequestProgress(HttpRequest paramHttpRequest, int paramInt1, int paramInt2)
    {
      DefaultImageService.Session localSession;
      if (((paramHttpRequest instanceof ImageRequest)) && (((ImageRequest)paramHttpRequest).type() == 2))
      {
        localSession = (DefaultImageService.Session)DefaultImageService.this.runningSession.get(paramHttpRequest);
        if ((localSession != null) && (localSession.status == 2))
          break label49;
      }
      label49: 
      do
        return;
      while (!(localSession.handler instanceof FullRequestHandle));
      ((FullRequestHandle)localSession.handler).onRequestProgress(paramHttpRequest, paramInt1, paramInt2);
    }

    public void onRequestStart(HttpRequest paramHttpRequest)
    {
      paramHttpRequest = (DefaultImageService.Session)DefaultImageService.this.runningSession.get(paramHttpRequest);
      if ((paramHttpRequest == null) || (paramHttpRequest.status != 2))
        return;
      paramHttpRequest.time = (-SystemClock.elapsedRealtime());
      InputStream localInputStream = paramHttpRequest.request.input();
      int i;
      if (localInputStream == null)
        i = 0;
      try
      {
        while (true)
        {
          paramHttpRequest.requestBytes = i;
          return;
          i = localInputStream.available();
        }
      }
      catch (java.lang.Exception paramHttpRequest)
      {
      }
    }
  };
  private final Handler mhandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramMessage)
    {
      DefaultImageService.Session localSession = (DefaultImageService.Session)paramMessage.obj;
      localSession = (DefaultImageService.Session)DefaultImageService.this.runningSession.remove(localSession.request);
      if ((localSession == null) || (localSession.status != 4))
        return;
      switch (paramMessage.what)
      {
      default:
        return;
      case 2:
        localSession.handler.onRequestFinish(localSession.request, localSession.response);
        return;
      case 3:
      }
      localSession.handler.onRequestFailed(localSession.request, localSession.response);
    }
  };
  private MonitorService monitor;
  private int poolSize;
  private final ConcurrentHashMap<Request, Session> runningSession = new ConcurrentHashMap();
  private final Handler whandler = new Handler(Daemon.looper())
  {
    public void handleMessage(Message paramMessage)
    {
      DefaultImageService.Session localSession = (DefaultImageService.Session)paramMessage.obj;
      try
      {
        if (localSession.status != 4)
          return;
        ImageRequest localImageRequest = localSession.request;
        if (paramMessage.what == 1)
          DefaultImageService.this.cache().touch(localImageRequest, System.currentTimeMillis());
        if ((paramMessage.what == 2) && (localSession.writeToCache != null))
        {
          DefaultImageService.this.cache().put(localImageRequest, new BasicHttpResponse(0, localSession.writeToCache, null, null), System.currentTimeMillis());
          return;
        }
      }
      catch (java.lang.Exception paramMessage)
      {
        Log.e("image", "unable to write image cache", paramMessage);
      }
    }
  };

  public DefaultImageService(Context paramContext, int paramInt, MonitorService paramMonitorService)
  {
    this.context = paramContext;
    this.poolSize = paramInt;
    this.monitor = paramMonitorService;
  }

  private Looper createLooper(String paramString)
  {
    BlockingItem localBlockingItem = new BlockingItem();
    new Thread(paramString, localBlockingItem)
    {
      public void run()
      {
        Looper.prepare();
        Looper localLooper = Looper.myLooper();
        this.val$bl.put(localLooper);
        Looper.loop();
      }
    }
    .start();
    try
    {
      paramString = (Looper)localBlockingItem.take();
      return paramString;
    }
    catch (java.lang.Exception paramString)
    {
    }
    return Daemon.looper();
  }

  private HttpService http()
  {
    monitorenter;
    try
    {
      if (this.http == null)
        this.http = new ImageHttpService(this.context, this.poolSize);
      DefaultHttpService localDefaultHttpService = this.http;
      return localDefaultHttpService;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void abort(Request paramRequest, RequestHandler<Request, Response> paramRequestHandler, boolean paramBoolean)
  {
    if ((!(paramRequest instanceof HttpRequest)) || (!"GET".equals(((HttpRequest)paramRequest).method())))
      throw new IllegalArgumentException("request must be a GET http request");
    Session localSession = (Session)this.runningSession.get(paramRequest);
    if ((localSession != null) && (localSession.handler == paramRequestHandler))
    {
      this.runningSession.remove(paramRequest, localSession);
      if (localSession.status == 2)
        http().abort((HttpRequest)paramRequest, this.httpHandler, true);
      localSession.status = 0;
    }
  }

  public void asyncTrimToCount(int paramInt1, int paramInt2)
  {
    this.dhandler.post(new Runnable(paramInt1, paramInt2)
    {
      public void run()
      {
        ImageCacheService localImageCacheService = DefaultImageService.this.cache;
        if ((localImageCacheService instanceof ImageCacheService))
        {
          int i = ((ImageCacheService)localImageCacheService).trimToCount(this.val$type, this.val$expected);
          Log.i("image", "trim image cache, type=" + this.val$type + ", deleted=" + i);
        }
      }
    });
  }

  public CacheService cache()
  {
    monitorenter;
    try
    {
      if (this.cache == null)
        this.cache = new ImageCacheService(this.context);
      ImageCacheService localImageCacheService = this.cache;
      return localImageCacheService;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void close()
  {
    monitorenter;
    try
    {
      if (this.cache != null)
        this.cache.close();
      if (this.http != null)
        this.http.close();
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void exec(Request paramRequest, RequestHandler<Request, Response> paramRequestHandler)
  {
    if ((!(paramRequest instanceof ImageRequest)) || (!"GET".equals(((HttpRequest)paramRequest).method())))
      throw new IllegalArgumentException("request must be a GET ImageRequest");
    if ((paramRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)paramRequestHandler).onRequestStart(paramRequest);
    paramRequestHandler = new Session((ImageRequest)paramRequest, paramRequestHandler);
    if ((Session)this.runningSession.putIfAbsent(paramRequest, paramRequestHandler) == null)
    {
      paramRequestHandler.status = 1;
      cache().exec((HttpRequest)paramRequest, this.cacheHandler);
      return;
    }
    Log.e("image", "cannot exec duplicate request (same instance)");
  }

  public Response execSync(Request paramRequest)
  {
    if ((!(paramRequest instanceof ImageRequest)) || (!"GET".equals(((HttpRequest)paramRequest).method())))
      throw new IllegalArgumentException("request must be a GET ImageRequest");
    boolean bool = false;
    if ((paramRequest instanceof ImageRequest))
      bool = ((ImageRequest)paramRequest).cacheOnly();
    Object localObject1 = cache().execSync((HttpRequest)paramRequest);
    if ((((Response)localObject1).result() instanceof byte[]))
    {
      paramRequest = (byte[])(byte[])((Response)localObject1).result();
      try
      {
        paramRequest = BitmapFactory.decodeByteArray(paramRequest, 0, paramRequest.length);
        if (paramRequest == null)
          return new BasicResponse(null, "fail to decode bitmap");
        paramRequest = new BasicResponse(paramRequest, null);
        return paramRequest;
      }
      catch (Throwable paramRequest)
      {
        return new BasicResponse(null, paramRequest);
      }
    }
    if (bool)
      return new BasicResponse(null, "cache only");
    localObject1 = (HttpResponse)http().execSync((HttpRequest)paramRequest);
    if (((((HttpResponse)localObject1).result() instanceof byte[])) && (((HttpResponse)localObject1).statusCode() / 100 == 2))
    {
      Object localObject2 = (byte[])(byte[])((HttpResponse)localObject1).result();
      try
      {
        localObject2 = BitmapFactory.decodeByteArray(localObject2, 0, localObject2.length);
        if (localObject2 != null)
          cache().put(paramRequest, (HttpResponse)localObject1, System.currentTimeMillis());
        if (localObject2 == null)
        {
          paramRequest = new BasicResponse(null, "fail to decode bitmap");
          return paramRequest;
        }
      }
      catch (Throwable paramRequest)
      {
        return new BasicResponse(null, paramRequest);
      }
      paramRequest = new BasicResponse(localObject2, null);
      return paramRequest;
    }
    return (Response)(Response)localObject1;
  }

  public int runningCount()
  {
    return this.runningSession.size();
  }

  private class ImageHttpService extends DefaultHttpService
  {
    public ImageHttpService(Context paramInt, int arg3)
    {
      super(new ThreadPoolExecutor(i, i, 2147483647L, TimeUnit.SECONDS, new LinkedBlockingQueue()));
    }

    protected boolean isLoggable()
    {
      return false;
    }

    protected void log(String paramString)
    {
    }
  }

  private static class Session
  {
    public RequestHandler<Request, Response> handler;
    public ImageRequest request;
    public int requestBytes;
    public Response response;
    public int status;
    public long time;
    public byte[] writeToCache;

    public Session(ImageRequest paramImageRequest, RequestHandler<Request, Response> paramRequestHandler)
    {
      this.request = paramImageRequest;
      this.handler = paramRequestHandler;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.image.impl.DefaultImageService
 * JD-Core Version:    0.6.0
 */