package com.dianping.dataservice.http;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.impl.InnerHttpResponse;
import com.dianping.util.Log;
import com.tencent.base.Global;
import com.tencent.base.util.ProcessUtils;
import com.tencent.wns.client.inte.IWnsCallback.WnsBindCallback;
import com.tencent.wns.client.inte.IWnsResult.IWnsBindResult;
import com.tencent.wns.client.inte.WnsClientFactory;
import com.tencent.wns.client.inte.WnsService;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WnsHttpService
  implements HttpService
{
  public static final int STATUS_CODE_ERROR = -295;
  public static final int STATUS_CODE_TIMEOUT = -299;
  private static boolean wnsInited = false;
  private static boolean wnsStarted = false;
  private final Executor executor = new ThreadPoolExecutor(0, 2147483647, 5L, TimeUnit.SECONDS, new SynchronousQueue());
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final ConcurrentHashMap<HttpRequest, Session> runningSessions = new ConcurrentHashMap();

  public static void initWns(Context paramContext, int paramInt, String paramString1, String paramString2, String paramString3)
  {
    if (!wnsInited)
    {
      paramContext = (Application)paramContext.getApplicationContext();
      Global.init(paramContext, null);
      WnsService localWnsService = WnsClientFactory.getThirdPartyWnsService();
      if (ProcessUtils.isMainProcess(paramContext))
      {
        localWnsService.initWnsWithAppInfo(paramInt, paramString1, paramString2, false);
        localWnsService.bind(paramString3, new IWnsCallback.WnsBindCallback()
        {
          public void onBindFinished(IWnsResult.IWnsBindResult paramIWnsBindResult)
          {
            Log.d("WnsHttpService", "bind wns:" + paramIWnsBindResult.toString());
          }
        });
        wnsInited = true;
        localWnsService.startWnsService();
        wnsStarted = true;
      }
    }
  }

  public static boolean isWnsAvailable()
  {
    try
    {
      WnsHttpService.class.getClassLoader().loadClass("com.tencent.wns.client.inte.WnsClientFactory");
      return true;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public static void startWnsService()
  {
    WnsService localWnsService = WnsClientFactory.getThirdPartyWnsService();
    if (!wnsStarted);
    try
    {
      localWnsService.startWnsService();
      label16: wnsStarted = true;
      return;
    }
    catch (Exception localException)
    {
      break label16;
    }
  }

  public static void stopWnsService()
  {
    WnsService localWnsService = WnsClientFactory.getThirdPartyWnsService();
    if (wnsStarted);
    try
    {
      localWnsService.stopWnsService();
      label16: wnsStarted = false;
      return;
    }
    catch (Exception localException)
    {
      break label16;
    }
  }

  private long time()
  {
    return SystemClock.uptimeMillis();
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    Session localSession = (Session)this.runningSessions.get(paramHttpRequest);
    if ((localSession != null) && ((paramRequestHandler == null) || (localSession.handler == paramRequestHandler)))
      this.runningSessions.remove(paramHttpRequest, localSession);
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    Session localSession = new Session(null);
    localSession.request = paramHttpRequest;
    localSession.handler = paramRequestHandler;
    this.runningSessions.put(paramHttpRequest, localSession);
    this.handler.post(localSession);
  }

  public HttpResponse execSync(HttpRequest paramHttpRequest)
  {
    Object localObject1;
    Object localObject2;
    Object localObject4;
    while (true)
    {
      try
      {
        localObject1 = WnsClientFactory.getThirdPartyWnsService();
        paramHttpRequest = transferRequest(paramHttpRequest);
        localObject2 = ((WnsService)localObject1).getWnsHttpUrl(paramHttpRequest.url());
        if (localObject2 != null)
          continue;
        paramHttpRequest = new InnerHttpResponse(-295, null, null, "WNS_ERR: wnsHttpUrl == null");
        paramHttpRequest.source = 4;
        paramHttpRequest.tunnel = 4;
        return paramHttpRequest;
        localObject1 = (HttpURLConnection)((URL)localObject2).openConnection();
        ((HttpURLConnection)localObject1).setRequestMethod(paramHttpRequest.method());
        if (paramHttpRequest.timeout() > 0L)
        {
          i = (int)paramHttpRequest.timeout();
          ((HttpURLConnection)localObject1).setConnectTimeout(i);
          ((HttpURLConnection)localObject1).setReadTimeout(i);
          if (paramHttpRequest.headers() == null)
            break;
          localObject3 = paramHttpRequest.headers().iterator();
          if (!((Iterator)localObject3).hasNext())
            break;
          localObject4 = (NameValuePair)((Iterator)localObject3).next();
          ((HttpURLConnection)localObject1).addRequestProperty(((NameValuePair)localObject4).getName(), ((NameValuePair)localObject4).getValue());
          continue;
        }
      }
      catch (Exception paramHttpRequest)
      {
        paramHttpRequest = new InnerHttpResponse(-295, null, null, paramHttpRequest);
        paramHttpRequest.source = 4;
        paramHttpRequest.tunnel = 4;
        return paramHttpRequest;
      }
      i = 10000;
    }
    ((HttpURLConnection)localObject1).addRequestProperty("wns-http-reqcmd", ((URL)localObject2).getHost() + ((URL)localObject2).getPath());
    if ((paramHttpRequest.method().equals("POST")) || (paramHttpRequest.method().equals("PUT")))
    {
      ((HttpURLConnection)localObject1).setDoOutput(true);
      localObject2 = new DataOutputStream(((HttpURLConnection)localObject1).getOutputStream());
      ((DataOutputStream)localObject2).write(transferBody(paramHttpRequest.input()));
      ((DataOutputStream)localObject2).close();
    }
    ((HttpURLConnection)localObject1).setDoInput(true);
    int j = ((HttpURLConnection)localObject1).getResponseCode();
    int k = ((HttpURLConnection)localObject1).getContentLength();
    paramHttpRequest = null;
    if (k > 0)
    {
      localObject2 = ((HttpURLConnection)localObject1).getInputStream();
      paramHttpRequest = new byte[k];
      i = 0;
      while (i < k)
      {
        paramHttpRequest[i] = (byte)((InputStream)localObject2).read();
        i += 1;
      }
      ((InputStream)localObject2).close();
    }
    Object localObject3 = new ArrayList();
    int i = 0;
    while (true)
    {
      localObject2 = ((HttpURLConnection)localObject1).getHeaderFieldKey(i);
      localObject4 = ((HttpURLConnection)localObject1).getHeaderField(i);
      if ((localObject2 == null) && (localObject4 == null))
      {
        localObject2 = null;
        localObject1 = localObject2;
        if (j / 100 != 2)
        {
          localObject1 = localObject2;
          if (j / 100 != 4)
            localObject1 = "WNS_ERR:" + j;
        }
        paramHttpRequest = new InnerHttpResponse(transferStatusCode(j), paramHttpRequest, (List)localObject3, localObject1);
        paramHttpRequest.source = 4;
        paramHttpRequest.tunnel = 4;
        return paramHttpRequest;
      }
      ((ArrayList)localObject3).add(new BasicNameValuePair((String)localObject2, (String)localObject4));
      i += 1;
    }
  }

  protected byte[] transferBody(InputStream paramInputStream)
  {
    if (paramInputStream == null)
      return null;
    while (true)
    {
      try
      {
        if (!paramInputStream.markSupported())
          continue;
        paramInputStream.mark(0);
        i = paramInputStream.available();
        if (i > 0)
        {
          ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(i);
          byte[] arrayOfByte = new byte[4096];
          i = paramInputStream.read(arrayOfByte);
          if (i == -1)
            continue;
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
          continue;
          if (!paramInputStream.markSupported())
            continue;
          paramInputStream.reset();
          paramInputStream = localByteArrayOutputStream.toByteArray();
          return paramInputStream;
        }
      }
      catch (Exception paramInputStream)
      {
        return null;
      }
      int i = 4096;
    }
  }

  protected HttpRequest transferRequest(HttpRequest paramHttpRequest)
  {
    return paramHttpRequest;
  }

  protected int transferStatusCode(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return paramInt;
    case 10000:
      return -250;
    case 10001:
      return -251;
    case 10002:
      return -252;
    case 10003:
      return -253;
    case 10100:
      return -254;
    case 10101:
      return -255;
    case 10102:
    }
    return -256;
  }

  private class Session
    implements Runnable
  {
    RequestHandler<HttpRequest, HttpResponse> handler;
    HttpRequest request;
    HttpResponse resp;
    long startTime;

    private Session()
    {
    }

    private long timeout()
    {
      if (this.request.timeout() > 0L)
        return this.request.timeout();
      return 10000L;
    }

    public void run()
    {
      if (WnsHttpService.this.runningSessions.get(this.request) != this);
      do
      {
        return;
        if (this.startTime == 0L)
        {
          this.startTime = WnsHttpService.this.time();
          WnsHttpService.this.executor.execute(new WnsHttpService.Session.1(this));
          if ((this.handler instanceof FullRequestHandle))
            ((FullRequestHandle)this.handler).onRequestStart(this.request);
          WnsHttpService.this.handler.postDelayed(this, timeout());
          return;
        }
        if (this.resp == null)
          continue;
        if ((this.resp.statusCode() > 0) && (this.resp.statusCode() < 1000))
          this.handler.onRequestFinish(this.request, this.resp);
        while (true)
        {
          WnsHttpService.this.runningSessions.remove(this.request, this);
          return;
          this.handler.onRequestFailed(this.request, this.resp);
        }
      }
      while (WnsHttpService.this.time() - this.startTime + 1L < timeout());
      InnerHttpResponse localInnerHttpResponse = new InnerHttpResponse(-299, null, null, "timeout");
      localInnerHttpResponse.source = 4;
      localInnerHttpResponse.tunnel = 4;
      this.handler.onRequestFailed(this.request, localInnerHttpResponse);
      WnsHttpService.this.runningSessions.remove(this.request, this);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.WnsHttpService
 * JD-Core Version:    0.6.0
 */