package com.dianping.utn.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.http.impl.InnerHttpResponse;
import com.dianping.util.Log;
import com.dianping.utn.ResponseChain;
import com.dianping.utn.UtnResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class AndroidUtnConnection extends UtnPingConnection
  implements HttpService
{
  private static final List<SocketAddress> DEFAULT_SERVERS;
  public static final int ERROR_CODE_MALFORMED_RESPONSE = -182;
  public static final int ERROR_CODE_TIMEOUT_RECV = -186;
  public static final int ERROR_CODE_TIMEOUT_SEND = -185;
  public static final int ERROR_CODE_TIMEOUT_SERVER = -188;
  public static final int ERROR_CODE_TUNNEL_NOT_AVAILABLE = -181;
  public static final int ERROR_CODE_UNKNOWN = -189;
  private static final Handler handler;
  private static final Handler mainHandler;
  private final Context context;
  private SharedPreferences debugPrefs;
  private final NetworkInfoHelper networkInfo;

  static
  {
    Object localObject = new String[23];
    localObject[0] = "140.207.219.163";
    localObject[1] = "140.207.219.164";
    localObject[2] = "140.207.219.165";
    localObject[3] = "140.207.219.166";
    localObject[4] = "140.207.219.168";
    localObject[5] = "140.207.219.169";
    localObject[6] = "180.153.132.11";
    localObject[7] = "180.153.132.12";
    localObject[8] = "180.153.132.13";
    localObject[9] = "180.153.132.14";
    localObject[10] = "180.153.132.16";
    localObject[11] = "180.153.132.17";
    localObject[12] = "180.153.132.18";
    localObject[13] = "180.153.132.19";
    localObject[14] = "180.153.132.21";
    localObject[15] = "180.153.132.22";
    localObject[16] = "180.153.132.23";
    localObject[17] = "180.153.132.24";
    localObject[18] = "221.130.190.195";
    localObject[19] = "221.130.190.196";
    localObject[20] = "221.130.190.244";
    localObject[21] = "221.130.190.245";
    localObject[22] = "221.130.190.246";
    ArrayList localArrayList = new ArrayList(localObject.length);
    int j = localObject.length;
    int i = 0;
    while (i < j)
    {
      String str = localObject[i];
      localArrayList.add(new InetSocketAddress(str, 8080));
      localArrayList.add(new InetSocketAddress(str, 53));
      i += 1;
    }
    DEFAULT_SERVERS = Collections.unmodifiableList(localArrayList);
    localObject = new HandlerThread("utn_handler");
    ((HandlerThread)localObject).start();
    handler = new Handler(((HandlerThread)localObject).getLooper());
    mainHandler = new Handler(Looper.getMainLooper());
  }

  public AndroidUtnConnection(Context paramContext)
  {
    this.context = paramContext;
    this.networkInfo = new NetworkInfoHelper(paramContext);
  }

  private void dispatchSessionResult(UtnConnection.Session paramSession)
  {
    MySession localMySession = (MySession)paramSession;
    String str2 = null;
    if ((localMySession.server instanceof InetSocketAddress))
      str2 = ((InetSocketAddress)localMySession.server).getAddress().getHostAddress();
    if (paramSession.status == 4)
      try
      {
        InnerHttpResponse localInnerHttpResponse = transferResponse(paramSession.respChain.getResponse());
        localInnerHttpResponse.source = 2;
        localInnerHttpResponse.ip = str2;
        if (localInnerHttpResponse.statusCode() > 0)
        {
          localMySession.httpHandler.onRequestFinish(localMySession.httpReq, localInnerHttpResponse);
          return;
        }
        localMySession.httpHandler.onRequestFailed(localMySession.httpReq, localInnerHttpResponse);
        return;
      }
      catch (Exception localException)
      {
        if (loggable())
          log("CORRUPTED: " + paramSession);
        paramSession = new InnerHttpResponse(-182, null, null, localException);
        paramSession.source = 2;
        paramSession.ip = str2;
        localMySession.httpHandler.onRequestFailed(localMySession.httpReq, paramSession);
        return;
      }
    int i = -189;
    String str1 = "Error";
    if (paramSession.status == -2)
    {
      i = -185;
      str1 = "Timeout Send";
    }
    while (true)
    {
      paramSession = new InnerHttpResponse(i, null, null, str1);
      paramSession.source = 2;
      paramSession.ip = str2;
      localMySession.httpHandler.onRequestFailed(localMySession.httpReq, paramSession);
      return;
      if (paramSession.status != -3)
        continue;
      i = -186;
      str1 = "Timeout Recv";
    }
  }

  public void abort(com.dianping.dataservice.http.HttpRequest paramHttpRequest, RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    monitorenter;
    while (true)
    {
      MySession localMySession;
      try
      {
        Iterator localIterator = this.sessions.values().iterator();
        if (!localIterator.hasNext())
          break;
        localMySession = (MySession)(UtnConnection.Session)localIterator.next();
        if (localMySession.httpReq != paramHttpRequest)
          continue;
        if (paramRequestHandler == null)
        {
          abort(localMySession.req.requestId);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      RequestHandler localRequestHandler = localMySession.httpHandler;
      if (paramRequestHandler != localRequestHandler)
        continue;
    }
    monitorexit;
  }

  protected com.dianping.utn.HttpRequest createRequest(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
  {
    com.dianping.utn.HttpRequest localHttpRequest = new com.dianping.utn.HttpRequest();
    if ("GET".equals(paramHttpRequest.method()))
      localHttpRequest.method = 0;
    while (true)
    {
      localHttpRequest.url = paramHttpRequest.url();
      if (paramHttpRequest.headers() != null)
      {
        HashMap localHashMap = new HashMap(paramHttpRequest.headers().size());
        try
        {
          Iterator localIterator = paramHttpRequest.headers().iterator();
          while (localIterator.hasNext())
          {
            NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
            localHashMap.put(localNameValuePair.getName(), localNameValuePair.getValue());
          }
        }
        catch (Exception localException)
        {
          localHttpRequest.headers = localHashMap;
        }
      }
      localHttpRequest.body = transferBody(paramHttpRequest.input());
      return localHttpRequest;
      if ("POST".equals(paramHttpRequest.method()))
      {
        localHttpRequest.method = 2;
        continue;
      }
      if ("DELETE".equals(paramHttpRequest.method()))
      {
        localHttpRequest.method = 4;
        continue;
      }
      if (!"PUT".equals(paramHttpRequest.method()))
        continue;
      localHttpRequest.method = 3;
    }
  }

  protected UtnConnection.Session createSession(com.dianping.utn.HttpRequest paramHttpRequest, Object paramObject)
  {
    com.dianping.dataservice.http.HttpRequest localHttpRequest = (com.dianping.dataservice.http.HttpRequest)((Object[])(Object[])paramObject)[0];
    paramObject = (RequestHandler)((Object[])(Object[])paramObject)[1];
    paramObject = new MySession(getServer(), paramHttpRequest, localHttpRequest, paramObject);
    paramHttpRequest.network = getNetwork();
    paramHttpRequest.requestId = UtnUtils.generateHttpRequestId();
    return paramObject;
  }

  protected void dispatchDone(UtnConnection.Session paramSession)
  {
    super.dispatchDone(paramSession);
    if (Looper.myLooper() != Looper.getMainLooper())
    {
      mainHandler.post(new Runnable(paramSession)
      {
        public void run()
        {
          AndroidUtnConnection.this.dispatchSessionResult(this.val$s);
        }
      });
      return;
    }
    dispatchSessionResult(paramSession);
  }

  public void exec(com.dianping.dataservice.http.HttpRequest paramHttpRequest, RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> paramRequestHandler)
  {
    if (send(createRequest(paramHttpRequest), new Object[] { paramHttpRequest, paramRequestHandler }) == 0)
      mainHandler.post(new Runnable(paramRequestHandler, paramHttpRequest)
      {
        public void run()
        {
          InnerHttpResponse localInnerHttpResponse = new InnerHttpResponse(-181, null, null, new Exception("UTN not available"));
          localInnerHttpResponse.source = 2;
          this.val$handler.onRequestFailed(this.val$req, localInnerHttpResponse);
        }
      });
  }

  public HttpResponse execSync(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
  {
    throw new UnsupportedOperationException();
  }

  protected int getNetwork()
  {
    switch (this.networkInfo.getNetworkType())
    {
    case 2:
    default:
      return 0;
    case 3:
      return 1;
    case 4:
      return 2;
    case 1:
    }
    return 3;
  }

  protected List<SocketAddress> getServers()
  {
    return DEFAULT_SERVERS;
  }

  protected void log(String paramString)
  {
    Log.d("utn", paramString);
  }

  protected boolean loggable()
  {
    boolean bool = false;
    if (Log.isLoggable(3))
    {
      if (this.debugPrefs == null)
        this.debugPrefs = this.context.getSharedPreferences("com.dianping.mapidebugagent", 0);
      bool = this.debugPrefs.getBoolean("tunnelLog", false);
    }
    return bool;
  }

  protected void scheduleRun(Runnable paramRunnable, long paramLong)
  {
    handler.postDelayed(paramRunnable, paramLong);
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

  protected InnerHttpResponse transferResponse(UtnResponse paramUtnResponse)
  {
    Object localObject = null;
    if (paramUtnResponse.headers != null)
    {
      ArrayList localArrayList = new ArrayList(paramUtnResponse.headers.size());
      Iterator localIterator = paramUtnResponse.headers.entrySet().iterator();
      while (true)
      {
        localObject = localArrayList;
        if (!localIterator.hasNext())
          break;
        localObject = (Map.Entry)localIterator.next();
        localArrayList.add(new BasicNameValuePair((String)((Map.Entry)localObject).getKey(), (String)((Map.Entry)localObject).getValue()));
      }
    }
    return (InnerHttpResponse)new InnerHttpResponse(paramUtnResponse.statusCode, paramUtnResponse.body, (List)localObject, null);
  }

  protected void unscheduleRun(Runnable paramRunnable)
  {
    handler.removeCallbacks(paramRunnable);
  }

  private class MySession extends UtnConnection.Session
  {
    final RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> httpHandler;
    final com.dianping.dataservice.http.HttpRequest httpReq;

    public MySession(com.dianping.utn.HttpRequest paramHttpRequest, com.dianping.dataservice.http.HttpRequest paramRequestHandler, RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> arg4)
    {
      super(paramHttpRequest, paramRequestHandler);
      Object localObject1;
      this.httpReq = localObject1;
      Object localObject2;
      this.httpHandler = localObject2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.client.AndroidUtnConnection
 * JD-Core Version:    0.6.0
 */