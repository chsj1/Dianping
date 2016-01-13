package com.dianping.tunnel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.http.impl.InnerHttpResponse;
import com.dianping.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class AndroidTunnel extends Tunnel
  implements HttpService
{
  private static final List<SocketAddress> DEFAULT_SERVERS;
  private static Handler handler = new Handler(Looper.getMainLooper());
  private final Context context;
  private SharedPreferences debugPrefs;
  private int lastSendNetwork = -1;
  private final NetworkInfoHelper networkInfo;
  private final Random rnd;

  static
  {
    String[] arrayOfString = new String[23];
    arrayOfString[0] = "140.207.219.163";
    arrayOfString[1] = "140.207.219.164";
    arrayOfString[2] = "140.207.219.165";
    arrayOfString[3] = "140.207.219.166";
    arrayOfString[4] = "140.207.219.168";
    arrayOfString[5] = "140.207.219.169";
    arrayOfString[6] = "180.153.132.11";
    arrayOfString[7] = "180.153.132.12";
    arrayOfString[8] = "180.153.132.13";
    arrayOfString[9] = "180.153.132.14";
    arrayOfString[10] = "180.153.132.16";
    arrayOfString[11] = "180.153.132.17";
    arrayOfString[12] = "180.153.132.18";
    arrayOfString[13] = "180.153.132.19";
    arrayOfString[14] = "180.153.132.21";
    arrayOfString[15] = "180.153.132.22";
    arrayOfString[16] = "180.153.132.23";
    arrayOfString[17] = "180.153.132.24";
    arrayOfString[18] = "221.130.190.195";
    arrayOfString[19] = "221.130.190.196";
    arrayOfString[20] = "221.130.190.244";
    arrayOfString[21] = "221.130.190.245";
    arrayOfString[22] = "221.130.190.246";
    ArrayList localArrayList = new ArrayList(arrayOfString.length);
    int j = arrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = arrayOfString[i];
      localArrayList.add(new InetSocketAddress(str, 80));
      localArrayList.add(new InetSocketAddress(str, 443));
      i += 1;
    }
    DEFAULT_SERVERS = Collections.unmodifiableList(localArrayList);
  }

  public AndroidTunnel(Context paramContext)
  {
    this.context = paramContext;
    this.networkInfo = new NetworkInfoHelper(paramContext);
    this.rnd = new Random(System.currentTimeMillis());
  }

  private void dispatchSessionResult(Tunnel.Session paramSession)
  {
    MySession localMySession = (MySession)paramSession;
    Object localObject2 = null;
    Object localObject1 = localObject2;
    if (paramSession.connection != null)
    {
      paramSession = paramSession.connection.socket.getRemoteSocketAddress();
      localObject1 = localObject2;
      if ((paramSession instanceof InetSocketAddress))
        localObject1 = ((InetSocketAddress)paramSession).getAddress().getHostAddress();
    }
    if ((localMySession.resp == null) || (localMySession.resp.statusCode <= 0))
    {
      if (localMySession.resp == null);
      for (paramSession = new InnerHttpResponse(-150, null, null, "null"); ; paramSession = transferResponse(localMySession.resp))
      {
        paramSession.source = 1;
        paramSession.ip = ((String)localObject1);
        localMySession.httpHandler.onRequestFailed(localMySession.httpReq, paramSession);
        return;
      }
    }
    paramSession = transferResponse(localMySession.resp);
    paramSession.source = 1;
    paramSession.ip = ((String)localObject1);
    localMySession.httpHandler.onRequestFinish(localMySession.httpReq, paramSession);
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    Iterator localIterator = this.runningSessions.values().iterator();
    while (localIterator.hasNext())
    {
      MySession localMySession = (MySession)(Tunnel.Session)localIterator.next();
      if (localMySession.httpReq != paramHttpRequest)
        continue;
      if (paramRequestHandler == null);
      while (true)
      {
        abort(localMySession.request.id);
        break;
        if (paramRequestHandler != localMySession.httpHandler)
          break;
      }
    }
  }

  protected TunnelRequest createRequest(HttpRequest paramHttpRequest)
  {
    TunnelRequest localTunnelRequest = new TunnelRequest();
    localTunnelRequest.id = TunnelUtils.generateHttpRequestId();
    localTunnelRequest.method = paramHttpRequest.method();
    localTunnelRequest.url = paramHttpRequest.url();
    if (paramHttpRequest.headers() != null)
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        Iterator localIterator = paramHttpRequest.headers().iterator();
        while (localIterator.hasNext())
        {
          NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
          localJSONObject.put(localNameValuePair.getName(), localNameValuePair.getValue());
        }
      }
      catch (Exception localException)
      {
        localTunnelRequest.headers = localJSONObject;
      }
    }
    localTunnelRequest.buffer = transferBody(paramHttpRequest.input());
    return localTunnelRequest;
  }

  public Tunnel.Session createSession(TunnelRequest paramTunnelRequest, Object paramObject)
  {
    MySession localMySession = new MySession();
    localMySession.request = paramTunnelRequest;
    paramTunnelRequest = (Object[])(Object[])paramObject;
    localMySession.httpReq = ((HttpRequest)paramTunnelRequest[0]);
    localMySession.httpHandler = ((RequestHandler)paramTunnelRequest[1]);
    return localMySession;
  }

  public void dispatchDone(Tunnel.Session paramSession)
  {
    if (Looper.myLooper() != Looper.getMainLooper())
    {
      handler.post(new Runnable(paramSession)
      {
        public void run()
        {
          AndroidTunnel.this.dispatchSessionResult(this.val$s);
        }
      });
      return;
    }
    dispatchSessionResult(paramSession);
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    exec(paramHttpRequest, paramRequestHandler, defaultClientTimeout());
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, int paramInt)
  {
    send(createRequest(paramHttpRequest), paramInt, new Object[] { paramHttpRequest, paramRequestHandler });
  }

  public HttpResponse execSync(HttpRequest paramHttpRequest)
  {
    throw new UnsupportedOperationException();
  }

  protected List<SocketAddress> getServers()
  {
    ArrayList localArrayList = new ArrayList(DEFAULT_SERVERS);
    Collections.shuffle(localArrayList, this.rnd);
    return localArrayList;
  }

  public void log(String paramString)
  {
    Log.d("tunnel", paramString);
  }

  public boolean loggable()
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

  public void scheduleRun(Runnable paramRunnable, long paramLong)
  {
    handler.postDelayed(paramRunnable, paramLong);
  }

  public void send(TunnelRequest paramTunnelRequest, int paramInt, Object paramObject)
  {
    super.send(paramTunnelRequest, paramInt, paramObject);
    paramInt = this.networkInfo.getNetworkType();
    if (paramInt != this.lastSendNetwork)
    {
      if (this.lastSendNetwork != -1)
        clearConnections(true);
      this.lastSendNetwork = paramInt;
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

  protected InnerHttpResponse transferResponse(TunnelResponse paramTunnelResponse)
  {
    Object localObject1 = null;
    if (paramTunnelResponse.headers != null)
    {
      localObject2 = new ArrayList();
      Iterator localIterator = paramTunnelResponse.headers.keys();
      while (true)
      {
        localObject1 = localObject2;
        if (!localIterator.hasNext())
          break;
        localObject1 = (String)localIterator.next();
        ((ArrayList)localObject2).add(new BasicNameValuePair((String)localObject1, paramTunnelResponse.headers.optString((String)localObject1)));
      }
    }
    int i = paramTunnelResponse.statusCode;
    Object localObject2 = paramTunnelResponse.body;
    if (paramTunnelResponse.statusCode <= 0);
    for (paramTunnelResponse = "error"; ; paramTunnelResponse = null)
      return new InnerHttpResponse(i, localObject2, (List)localObject1, paramTunnelResponse);
  }

  public void unscheduleRun(Runnable paramRunnable)
  {
    handler.removeCallbacks(paramRunnable);
  }

  protected class MySession extends Tunnel.Session
  {
    RequestHandler<HttpRequest, HttpResponse> httpHandler;
    HttpRequest httpReq;

    protected MySession()
    {
      super();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tunnel.AndroidTunnel
 * JD-Core Version:    0.6.0
 */