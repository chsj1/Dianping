package com.dianping.dataservice.http.fork;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.http.impl.InnerHttpResponse;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.monitor.MonitorService;
import com.dianping.tunnel.AndroidTunnel;
import com.dianping.util.Log;
import com.dianping.utn.client.AndroidUtnConnection;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

public class ForkHttpService
  implements HttpService
{
  private static final Handler HANDLER = new Handler(Looper.getMainLooper());
  private static final int SESSION_STATUS_FAILED = -1;
  private static final int SESSION_STATUS_FINISHED = 3;
  private static final int SESSION_STATUS_IDLE = 0;
  private static final int SESSION_STATUS_STARTED = 1;
  final ConfigService config;
  final SharedPreferences debugPrefs;
  private final RequestHandler<HttpRequest, HttpResponse> httpHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFailed(paramHttpRequest.http, paramHttpResponse);
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFinish(paramHttpRequest.http, paramHttpResponse);
    }
  };
  final HttpService httpService;
  final MonitorService monitor;
  final NetworkInfoHelper networkInfo;
  final SharedPreferences prefs;
  private final ConcurrentHashMap<HttpRequest, Session> runningSessions = new ConcurrentHashMap();
  private final RequestHandler<HttpRequest, HttpResponse> tunnelHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFailed(paramHttpRequest.tunnel, paramHttpResponse);
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFinish(paramHttpRequest.tunnel, paramHttpResponse);
    }
  };
  final AndroidTunnel tunnelService;
  private final RequestHandler<HttpRequest, HttpResponse> utnHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFailed(paramHttpRequest.utn, paramHttpResponse);
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      paramHttpRequest = (ForkHttpService.Session)ForkHttpService.this.runningSessions.get(paramHttpRequest);
      if (paramHttpRequest != null)
        paramHttpRequest.onRequestFinish(paramHttpRequest.utn, paramHttpResponse);
    }
  };
  final AndroidUtnConnection utnService;
  private Boolean wnsEnabled;
  final HttpService wnsService;

  public ForkHttpService(Context paramContext, ConfigService paramConfigService, MonitorService paramMonitorService, HttpService paramHttpService1, AndroidTunnel paramAndroidTunnel, HttpService paramHttpService2, AndroidUtnConnection paramAndroidUtnConnection)
  {
    this.prefs = paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
    this.debugPrefs = paramContext.getSharedPreferences("com.dianping.mapidebugagent", 0);
    this.networkInfo = new NetworkInfoHelper(paramContext);
    this.config = paramConfigService;
    this.monitor = paramMonitorService;
    this.wnsService = paramHttpService1;
    this.tunnelService = paramAndroidTunnel;
    this.httpService = paramHttpService2;
    this.utnService = paramAndroidUtnConnection;
  }

  private void dump()
  {
    Handler localHandler = new Handler();
    localHandler.post(new Runnable(localHandler)
    {
      public void run()
      {
        StringBuilder localStringBuilder = new StringBuilder();
        Iterator localIterator = ForkHttpService.this.runningSessions.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          localStringBuilder.append(ForkHttpService.getCommand(((HttpRequest)localEntry.getKey()).url()));
          localStringBuilder.append(" ").append(((ForkHttpService.Session)localEntry.getValue()).tunnelStatus);
          localStringBuilder.append(", ").append(((ForkHttpService.Session)localEntry.getValue()).httpStatus);
          localStringBuilder.append(", ").append(((ForkHttpService.Session)localEntry.getValue()).utnStatus);
          localStringBuilder.append("\n");
        }
        if (localStringBuilder.length() > 0)
          System.out.println(localStringBuilder);
        this.val$handler.postDelayed(this, 1000L);
      }
    });
  }

  public static String getCommand(String paramString)
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

  private static long time()
  {
    return System.nanoTime() / 1000000L;
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    if (wnsEnabled(paramHttpRequest))
      this.wnsService.abort(paramHttpRequest, paramRequestHandler, paramBoolean);
    do
    {
      return;
      paramHttpRequest = (Session)this.runningSessions.remove(paramHttpRequest);
    }
    while (paramHttpRequest == null);
    paramHttpRequest.recycle();
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    if (wnsEnabled(paramHttpRequest))
      this.wnsService.exec(paramHttpRequest, paramRequestHandler);
    do
    {
      return;
      Session localSession = new Session(null);
      localSession.req = paramHttpRequest;
      localSession.handler = paramRequestHandler;
      localSession.aggressive = isAggressive(paramHttpRequest);
      if (tunnelEnabled(paramHttpRequest))
        localSession.tunnel = this.tunnelService;
      localSession.http = this.httpService;
      if (utnEnabled(paramHttpRequest))
        localSession.utn = this.utnService;
      if (((localSession.tunnel != null) || (localSession.utn != null)) && (Log.LEVEL < 2147483647) && (this.debugPrefs.getBoolean("httpDisabled", false)))
        localSession.http = null;
      localSession.httpHold = httpHold(paramHttpRequest);
      localSession.utnHold = utnHold(paramHttpRequest);
      this.runningSessions.put(paramHttpRequest, localSession);
      HANDLER.post(localSession);
      if (tunnelStatus() <= 0)
        continue;
      this.tunnelService.prepareConnections();
    }
    while (utnStatus() <= 0);
    this.utnService.pingIfNecessary();
  }

  public HttpResponse execSync(HttpRequest paramHttpRequest)
  {
    throw new UnsupportedOperationException("execSync is not supported in ForkHttpService");
  }

  protected int httpHold(HttpRequest paramHttpRequest)
  {
    int i = 1500;
    switch (this.networkInfo.getNetworkType())
    {
    default:
      i = 1000;
    case 1:
    case 4:
      return i;
    case 2:
      return 5400;
    case 3:
    }
    return 3000;
  }

  protected boolean httpsEnabled(HttpRequest paramHttpRequest)
  {
    return false;
  }

  protected boolean isAggressive(HttpRequest paramHttpRequest)
  {
    return (paramHttpRequest.method() == null) || ("GET".equals(paramHttpRequest.method()));
  }

  protected boolean tunnelEnabled(HttpRequest paramHttpRequest)
  {
    int i = tunnelStatus();
    if ((i <= 0) || (!paramHttpRequest.url().contains(".dianping.com/")));
    do
    {
      do
      {
        return false;
        localObject = paramHttpRequest.input();
      }
      while ((localObject != null) && (!(localObject instanceof MApiFormInputStream)));
      String str = this.config.dump().optString("tunnelBlacklist", null);
      Object localObject = str;
      if (TextUtils.isEmpty(str))
        localObject = "/updatephoto.bin:/uploadphoto.bin:/addphotoscheckin.bin:/addfaviouteshop.bin:/addshopphoto.bin:/addavatar.bin:/locate.bin:/rgc.bin:/applog.api:/applog.bin";
      localObject = new StringTokenizer((String)localObject, ":");
      while (((StringTokenizer)localObject).hasMoreTokens())
      {
        str = ((StringTokenizer)localObject).nextToken();
        if ((!TextUtils.isEmpty(str)) && (paramHttpRequest.url().contains(str)))
          return false;
      }
    }
    while ((i == 1) && (this.tunnelService.isBlocked(paramHttpRequest.url())));
    return true;
  }

  protected int tunnelStatus()
  {
    if (this.tunnelService == null);
    do
      while (true)
      {
        return 0;
        if ((Log.LEVEL >= 2147483647) || (!this.debugPrefs.getBoolean("tunnelDebug", false)))
          break;
        if (this.debugPrefs.getBoolean("tunnelEnabled", false))
          return 2;
      }
    while ((this.config == null) || (!this.config.dump().optBoolean("tunnelEnabled", true)));
    return 1;
  }

  protected int tunnelTimeout()
  {
    return 25000;
  }

  protected boolean utnEnabled(HttpRequest paramHttpRequest)
  {
    if ((utnStatus() <= 0) || (!paramHttpRequest.url().contains(".dianping.com/")));
    do
    {
      return false;
      localObject = paramHttpRequest.input();
    }
    while ((localObject != null) && (!(localObject instanceof MApiFormInputStream)));
    String str = this.config.dump().optString("utnBlacklist", null);
    Object localObject = str;
    if (TextUtils.isEmpty(str))
      localObject = "/updatephoto.bin:/uploadphoto.bin:/addphotoscheckin.bin:/addfaviouteshop.bin:/addshopphoto.bin:/addavatar.bin:/locate.bin:/rgc.bin:/applog.api:/applog.bin";
    localObject = new StringTokenizer((String)localObject, ":");
    while (((StringTokenizer)localObject).hasMoreTokens())
    {
      str = ((StringTokenizer)localObject).nextToken();
      if ((!TextUtils.isEmpty(str)) && (paramHttpRequest.url().contains(str)))
        return false;
    }
    return true;
  }

  protected int utnHold(HttpRequest paramHttpRequest)
  {
    switch (this.networkInfo.getNetworkType())
    {
    case 1:
    case 4:
    default:
      return 2000;
    case 2:
      return 5000;
    case 3:
    }
    return 3000;
  }

  protected int utnStatus()
  {
    if (this.utnService == null);
    do
      while (true)
      {
        return 0;
        if ((Log.LEVEL >= 2147483647) || (!this.debugPrefs.getBoolean("tunnelDebug", false)))
          break;
        if (this.debugPrefs.getBoolean("utnEnabled", false))
          return 2;
      }
    while ((this.config == null) || (!this.config.dump().optBoolean("utnEnabled", true)));
    return 1;
  }

  protected boolean wnsEnabled(HttpRequest paramHttpRequest)
  {
    int i = wnsStatus();
    if (i == 2)
      return true;
    if (i == 1)
    {
      Object localObject = this.config.dump().optString("wnsWhiteList", "");
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        paramHttpRequest = getCommand(paramHttpRequest.url());
        localObject = new StringTokenizer((String)localObject, ":");
        while (((StringTokenizer)localObject).hasMoreTokens())
          if (paramHttpRequest.equals(((StringTokenizer)localObject).nextToken()))
            return true;
      }
      return false;
    }
    return false;
  }

  protected int wnsStatus()
  {
    int i = 1;
    if (this.wnsService == null);
    Object localObject;
    do
    {
      while (true)
      {
        return 0;
        if ((Log.LEVEL < 2147483647) && (this.debugPrefs.getBoolean("tunnelDebug", false)))
          if (this.debugPrefs.getBoolean("wnsEnabled", false))
            return 2;
        if (this.wnsEnabled == null)
          break;
        if (this.wnsEnabled.booleanValue())
          return 1;
      }
      localObject = this.prefs.getString("dpid", null);
    }
    while (TextUtils.isEmpty((CharSequence)localObject));
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(((String)localObject).getBytes());
      localObject = localMessageDigest.digest();
      int j = this.config.dump().optInt("wnsPercent", 50);
      boolean bool;
      if (new BigInteger(localObject).mod(new BigInteger("100")).intValue() < j)
      {
        bool = true;
        this.wnsEnabled = Boolean.valueOf(bool);
        bool = this.wnsEnabled.booleanValue();
        if (!bool)
          break label195;
      }
      while (true)
      {
        return i;
        bool = false;
        break;
        label195: i = 0;
      }
    }
    catch (Exception localException)
    {
    }
    return 0;
  }

  private class Session
    implements Runnable
  {
    boolean aggressive;
    HttpResponse failResp;
    RequestHandler<HttpRequest, HttpResponse> handler;
    HttpService http;
    int httpHold;
    long httpStartTime;
    int httpStatus;
    HttpRequest req;
    HttpResponse resp;
    HttpService respService;
    boolean started;
    AndroidTunnel tunnel;
    long tunnelStartTime;
    int tunnelStatus;
    AndroidUtnConnection utn;
    int utnHold;
    long utnStartTime;
    int utnStatus;

    private Session()
    {
    }

    private void recycle()
    {
      ForkHttpService.this.runningSessions.remove(this.req, this);
      if (this.tunnelStatus == 1)
        this.tunnel.abort(this.req, ForkHttpService.this.tunnelHandler, true);
      if (this.httpStatus == 1)
        this.http.abort(this.req, ForkHttpService.this.httpHandler, true);
      if (this.utnStatus == 1)
        this.utn.abort(this.req, ForkHttpService.this.utnHandler, true);
      ForkHttpService.HANDLER.removeCallbacks(this);
    }

    public void onRequestFailed(HttpService paramHttpService, HttpResponse paramHttpResponse)
    {
      Object localObject = null;
      if (ForkHttpService.this.runningSessions.get(this.req) != this)
        return;
      int i = 0;
      int k = 0;
      label118: int j;
      if (paramHttpService == this.tunnel)
      {
        this.tunnelStatus = -1;
        if (this.failResp == null)
          this.failResp = paramHttpResponse;
        if ((this.http != null) && (this.httpStatus == 0))
        {
          this.http.exec(this.req, ForkHttpService.this.httpHandler);
          this.httpStartTime = ForkHttpService.access$400();
          this.httpStatus = 1;
          if (this.utn != null)
            ForkHttpService.HANDLER.postDelayed(this, this.utnHold);
          i = 1;
          j = (int)(ForkHttpService.access$400() - this.tunnelStartTime);
          label132: if (((this.tunnel == null) || (this.tunnelStatus == -1)) && ((this.http == null) || (this.httpStatus == -1)) && ((this.utn == null) || (this.utnStatus == -1)))
          {
            if (this.failResp == null)
              this.failResp = new InnerHttpResponse(-100, null, null, "Fail");
            if (!(this.failResp instanceof InnerHttpResponse))
              this.failResp = new InnerHttpResponse(this.failResp.statusCode(), this.failResp.result(), this.failResp.headers(), this.failResp.error());
            if (this.utn == null)
              break label552;
            ((InnerHttpResponse)this.failResp).tunnel = 2;
          }
        }
      }
      while (true)
      {
        this.handler.onRequestFailed(this.req, this.failResp);
        recycle();
        if (i == 0)
          break;
        paramHttpService = localObject;
        if ((paramHttpResponse instanceof InnerHttpResponse))
          paramHttpService = ((InnerHttpResponse)paramHttpResponse).ip;
        ForkHttpService.this.monitor.pv3(0L, ForkHttpService.this.monitor.getCommand(this.req.url()), 0, i, paramHttpResponse.statusCode(), 0, 0, j, paramHttpService);
        return;
        if ((this.utn == null) || (this.utnStatus != 0))
          break label118;
        this.utn.exec(this.req, ForkHttpService.this.utnHandler);
        this.utnStartTime = ForkHttpService.access$400();
        this.utnStatus = 1;
        break label118;
        if (paramHttpService == this.http)
        {
          this.httpStatus = -1;
          this.failResp = paramHttpResponse;
          if ((this.utn != null) && (this.utnStatus == 0))
          {
            this.utn.exec(this.req, ForkHttpService.this.utnHandler);
            this.utnStartTime = ForkHttpService.access$400();
            this.utnStatus = 1;
          }
          j = k;
          if (!ForkHttpService.this.httpsEnabled(this.req))
            break label132;
          i = 8;
          j = k;
          break label132;
        }
        j = k;
        if (paramHttpService != this.utn)
          break label132;
        this.utnStatus = -1;
        if (this.failResp == null)
          this.failResp = paramHttpResponse;
        i = 2;
        j = (int)(ForkHttpService.access$400() - this.utnStartTime);
        break label132;
        label552: if (this.tunnel != null)
        {
          ((InnerHttpResponse)this.failResp).tunnel = 1;
          continue;
        }
        if (ForkHttpService.this.httpsEnabled(this.req))
        {
          ((InnerHttpResponse)this.failResp).tunnel = 8;
          continue;
        }
        ((InnerHttpResponse)this.failResp).tunnel = 0;
      }
    }

    public void onRequestFinish(HttpService paramHttpService, HttpResponse paramHttpResponse)
    {
      if (ForkHttpService.this.runningSessions.get(this.req) != this);
      int i;
      int k;
      int j;
      Object localObject;
      while (true)
      {
        return;
        i = 0;
        k = 0;
        if (paramHttpService != this.tunnel)
          break;
        this.tunnelStatus = 3;
        i = 1;
        j = (int)(ForkHttpService.access$400() - this.tunnelStartTime);
        localObject = paramHttpResponse;
        if (paramHttpResponse != null)
        {
          localObject = paramHttpResponse;
          if (!(paramHttpResponse instanceof InnerHttpResponse))
            localObject = new InnerHttpResponse(paramHttpResponse.statusCode(), paramHttpResponse.result(), paramHttpResponse.headers(), paramHttpResponse.error());
        }
        if ((localObject instanceof InnerHttpResponse))
        {
          if (this.utn == null)
            break label294;
          ((InnerHttpResponse)localObject).tunnel = 2;
        }
        label121: this.resp = ((HttpResponse)localObject);
        this.respService = paramHttpService;
        this.handler.onRequestFinish(this.req, (Response)localObject);
        recycle();
        if (i == 0)
          continue;
        if (!(localObject instanceof InnerHttpResponse))
          break label349;
      }
      label294: label349: for (paramHttpService = ((InnerHttpResponse)localObject).ip; ; paramHttpService = null)
      {
        ForkHttpService.this.monitor.pv3(0L, ForkHttpService.this.monitor.getCommand(this.req.url()), 0, i, ((HttpResponse)localObject).statusCode(), 0, 0, j, paramHttpService);
        return;
        if (paramHttpService == this.http)
        {
          this.httpStatus = 3;
          j = k;
          if (!ForkHttpService.this.httpsEnabled(this.req))
            break;
          i = 8;
          j = k;
          break;
        }
        j = k;
        if (paramHttpService != this.utn)
          break;
        this.utnStatus = 3;
        i = 2;
        j = (int)(ForkHttpService.access$400() - this.utnStartTime);
        break;
        if (this.tunnel != null)
        {
          ((InnerHttpResponse)localObject).tunnel = 1;
          break label121;
        }
        if (ForkHttpService.this.httpsEnabled(this.req))
        {
          ((InnerHttpResponse)localObject).tunnel = 8;
          break label121;
        }
        ((InnerHttpResponse)localObject).tunnel = 0;
        break label121;
      }
    }

    public void run()
    {
      if (ForkHttpService.this.runningSessions.get(this.req) != this);
      label147: label280: long l1;
      long l2;
      do
      {
        do
          while (true)
          {
            return;
            if (this.started)
              break;
            this.started = true;
            int i;
            if (this.tunnel != null)
            {
              AndroidTunnel localAndroidTunnel = this.tunnel;
              HttpRequest localHttpRequest = this.req;
              RequestHandler localRequestHandler = ForkHttpService.this.tunnelHandler;
              if (this.aggressive)
              {
                i = ForkHttpService.this.tunnelTimeout();
                localAndroidTunnel.exec(localHttpRequest, localRequestHandler, i);
                this.tunnelStartTime = ForkHttpService.access$400();
                this.tunnelStatus = 1;
                if (this.http == null)
                  break label147;
                ForkHttpService.HANDLER.postDelayed(this, this.httpHold);
              }
            }
            while (true)
            {
              if (!(this.handler instanceof FullRequestHandle))
                break label280;
              ((FullRequestHandle)this.handler).onRequestStart(this.req);
              return;
              i = 10000;
              break;
              if (this.utn == null)
                continue;
              ForkHttpService.HANDLER.postDelayed(this, this.utnHold);
              continue;
              if (this.http != null)
              {
                this.http.exec(this.req, ForkHttpService.this.httpHandler);
                this.httpStartTime = ForkHttpService.access$400();
                this.httpStatus = 1;
                if (this.utn == null)
                  continue;
                ForkHttpService.HANDLER.postDelayed(this, this.utnHold);
                continue;
              }
              if (this.utn == null)
                throw new RuntimeException("no http service to fork");
              this.utn.exec(this.req, ForkHttpService.this.utnHandler);
              this.utnStartTime = ForkHttpService.access$400();
              this.utnStatus = 1;
            }
          }
        while (!this.aggressive);
        l1 = ForkHttpService.access$400();
        l2 = this.tunnelStartTime;
        if ((this.http != null) && (this.httpStatus == 0) && (l1 - l2 + 1L >= this.httpHold))
        {
          this.http.exec(this.req, ForkHttpService.this.httpHandler);
          this.httpStartTime = l1;
          this.httpStatus = 1;
          if (this.utn != null)
            ForkHttpService.HANDLER.postDelayed(this, this.utnHold);
        }
        l2 = Math.max(this.tunnelStartTime, this.httpStartTime);
      }
      while ((this.utn == null) || (this.utnStatus != 0) || (l1 - l2 + 1L < this.utnHold));
      this.utn.exec(this.req, ForkHttpService.this.utnHandler);
      this.utnStartTime = ForkHttpService.access$400();
      this.utnStatus = 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.fork.ForkHttpService
 * JD-Core Version:    0.6.0
 */