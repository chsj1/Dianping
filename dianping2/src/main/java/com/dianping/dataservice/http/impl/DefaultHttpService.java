package com.dianping.dataservice.http.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.SystemClock;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.FormInputStream;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.mapi.impl.HttpExceptionLogger;
import com.dianping.util.ByteArrayPool;
import com.dianping.util.Log;
import com.dianping.util.MyTask;
import com.dianping.util.PoolingByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.apache.http.NameValuePair;

public class DefaultHttpService
  implements HttpService
{
  private static final int HTTP_ERROR_CANNOT_SEND_REQUEST = -102;
  private static final int HTTP_ERROR_READ_STREAM = -106;
  private static final int HTTP_ERROR_REDIRECT_OUTSIDE = -107;
  private static final int HTTP_ERROR_SENDING_REQUEST = -105;
  private static final int HTTP_ERROR_SENDING_REQUEST_CONNECTION_TIMEOUT = -103;
  private static final int HTTP_ERROR_SENDING_REQUEST_SOCKET_TIMEOUT = -104;
  private static final HashSet<Class<?>> LOGGED_EXCEPTIONS = new HashSet();
  private static final String TAG = "http";
  private final ByteArrayPool byteArrayPool;
  private Context context;
  private Executor executor;
  private NetworkInfoHelper networkInfo;
  private final ConcurrentHashMap<HttpRequest, Task> runningTasks = new ConcurrentHashMap();

  public DefaultHttpService(Context paramContext, Executor paramExecutor)
  {
    this.context = paramContext;
    this.executor = paramExecutor;
    this.networkInfo = new NetworkInfoHelper(this.context);
    this.byteArrayPool = new ByteArrayPool(4096);
  }

  private void logger(HttpRequest paramHttpRequest, int paramInt, Exception paramException)
  {
    monitorenter;
    try
    {
      Class localClass = paramException.getClass();
      if (!LOGGED_EXCEPTIONS.contains(localClass))
      {
        LOGGED_EXCEPTIONS.add(localClass);
        new Thread(new HttpExceptionLogger(this.context.getSharedPreferences(this.context.getPackageName(), 0).getString("dpid", null), paramHttpRequest, this.networkInfo.getNetworkInfo(), paramInt, paramException)).start();
      }
      monitorexit;
      return;
    }
    finally
    {
      paramHttpRequest = finally;
      monitorexit;
    }
    throw paramHttpRequest;
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    Task localTask = (Task)this.runningTasks.get(paramHttpRequest);
    if ((localTask != null) && (localTask.handler == paramRequestHandler))
    {
      this.runningTasks.remove(paramHttpRequest, localTask);
      localTask.cancel(paramBoolean);
    }
  }

  public void abortAll(boolean paramBoolean)
  {
    Iterator localIterator = this.runningTasks.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Task localTask = (Task)((Map.Entry)localIterator.next()).getValue();
      if (localTask == null)
        continue;
      localTask.cancel(paramBoolean);
    }
    this.runningTasks.clear();
  }

  public void close()
  {
    monitorenter;
    monitorexit;
  }

  protected Task createTask(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    return new Task(paramHttpRequest, paramRequestHandler);
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    paramRequestHandler = createTask(paramHttpRequest, paramRequestHandler);
    if ((Task)this.runningTasks.putIfAbsent(paramHttpRequest, paramRequestHandler) == null)
    {
      paramRequestHandler.executeOnExecutor(this.executor, new Void[0]);
      return;
    }
    Log.e("http", "cannot exec duplicate request (same instance)");
  }

  public HttpResponse execSync(HttpRequest paramHttpRequest)
  {
    return createTask(paramHttpRequest, null).doInBackground(new Void[0]);
  }

  protected boolean isLoggable()
  {
    return Log.isLoggable(3);
  }

  protected void log(String paramString)
  {
    Log.d("http", paramString);
  }

  public int runningTasks()
  {
    return this.runningTasks.size();
  }

  protected class Task extends MyTask<Void, Void, HttpResponse>
    implements WatchedInputStream.Listener
  {
    protected int availableBytes;
    protected int contentLength;
    protected final RequestHandler<HttpRequest, HttpResponse> handler;
    protected boolean isUploadProgress;
    protected long prevProgressTime;
    protected int receivedBytes;
    protected final HttpRequest req;
    protected int sentBytes;
    protected long startTime;
    protected int statusCode;
    protected HttpURLConnection urlConnection;

    public Task(RequestHandler<HttpRequest, HttpResponse> arg2)
    {
      Object localObject1;
      this.req = localObject1;
      Object localObject2;
      this.handler = localObject2;
    }

    private HttpURLConnection getUrlConnection()
      throws Exception
    {
      Object localObject2 = getHttpRequest();
      Object localObject1 = ((BasicHttpRequest)localObject2).proxy();
      Object localObject3 = new URL(((BasicHttpRequest)localObject2).url());
      if (localObject1 != null);
      Object localObject4;
      for (localObject1 = (HttpURLConnection)((URL)localObject3).openConnection((Proxy)localObject1); ; localObject1 = (HttpURLConnection)((URL)localObject3).openConnection())
      {
        ((HttpURLConnection)localObject1).setDoInput(true);
        ((HttpURLConnection)localObject1).setRequestProperty("Accept-Encoding", "identity");
        if (((BasicHttpRequest)localObject2).headers() == null)
          break;
        localObject3 = ((BasicHttpRequest)localObject2).headers().iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject4 = (NameValuePair)((Iterator)localObject3).next();
          ((HttpURLConnection)localObject1).setRequestProperty(((NameValuePair)localObject4).getName(), ((NameValuePair)localObject4).getValue());
        }
      }
      if (Build.VERSION.SDK_INT < 8)
        System.setProperty("http.keepAlive", "false");
      if (((BasicHttpRequest)localObject2).timeout() > 0L)
      {
        ((HttpURLConnection)localObject1).setConnectTimeout((int)((BasicHttpRequest)localObject2).timeout());
        ((HttpURLConnection)localObject1).setReadTimeout((int)((BasicHttpRequest)localObject2).timeout());
      }
      if (("GET".equals(((BasicHttpRequest)localObject2).method())) || ("DELETE".equals(((BasicHttpRequest)localObject2).method())) || ("HEAD".equals(((BasicHttpRequest)localObject2).method())))
        ((HttpURLConnection)localObject1).setRequestMethod(((BasicHttpRequest)localObject2).method());
      while (true)
      {
        return localObject1;
        if ((!"POST".equals(((BasicHttpRequest)localObject2).method())) && (!"PUT".equals(((BasicHttpRequest)localObject2).method())))
          break;
        ((HttpURLConnection)localObject1).setRequestMethod(((BasicHttpRequest)localObject2).method());
        ((HttpURLConnection)localObject1).setDoOutput(true);
        localObject3 = ((BasicHttpRequest)localObject2).input();
        if (localObject3 == null)
          continue;
        this.availableBytes = ((InputStream)localObject3).available();
        this.sentBytes = 0;
        localObject2 = localObject3;
        if (this.availableBytes > 4096)
        {
          localObject2 = new WatchedInputStream((InputStream)localObject3, 4096);
          ((WatchedInputStream)localObject2).setListener(this);
          this.isUploadProgress = true;
        }
        localObject3 = DefaultHttpService.this.byteArrayPool.getBuf(4096);
        localObject4 = DefaultHttpService.this.byteArrayPool;
        if (this.availableBytes > 0);
        for (int i = this.availableBytes; ; i = 4096)
        {
          localObject4 = new PoolingByteArrayOutputStream((ByteArrayPool)localObject4, i);
          while (true)
          {
            i = ((InputStream)localObject2).read(localObject3);
            if (i == -1)
              break;
            ((ByteArrayOutputStream)localObject4).write(localObject3, 0, i);
            ((ByteArrayOutputStream)localObject4).flush();
          }
        }
        ((HttpURLConnection)localObject1).setFixedLengthStreamingMode(this.availableBytes);
        localObject2 = new BufferedOutputStream(((HttpURLConnection)localObject1).getOutputStream());
        ((OutputStream)localObject2).write(((ByteArrayOutputStream)localObject4).toByteArray());
        ((OutputStream)localObject2).flush();
        ((OutputStream)localObject2).close();
        DefaultHttpService.this.byteArrayPool.returnBuf(localObject3);
        ((ByteArrayOutputStream)localObject4).close();
        return localObject1;
      }
      throw new IllegalArgumentException("unknown http method " + ((BasicHttpRequest)localObject2).method());
    }

    // ERROR //
    public HttpResponse doInBackground(Void[] paramArrayOfVoid)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aload_0
      //   3: getfield 39	com/dianping/dataservice/http/impl/DefaultHttpService$Task:req	Lcom/dianping/dataservice/http/HttpRequest;
      //   6: invokeinterface 272 1 0
      //   11: astore_2
      //   12: aload_1
      //   13: astore_3
      //   14: aload_2
      //   15: ifnull +21 -> 36
      //   18: aload_1
      //   19: astore_3
      //   20: aload_2
      //   21: invokevirtual 275	java/io/InputStream:markSupported	()Z
      //   24: ifeq +12 -> 36
      //   27: aload_2
      //   28: sipush 16384
      //   31: invokevirtual 278	java/io/InputStream:mark	(I)V
      //   34: aload_2
      //   35: astore_3
      //   36: aload_0
      //   37: aload_0
      //   38: invokespecial 280	com/dianping/dataservice/http/impl/DefaultHttpService$Task:getUrlConnection	()Ljava/net/HttpURLConnection;
      //   41: putfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   44: aload_0
      //   45: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   48: invokevirtual 285	java/net/HttpURLConnection:connect	()V
      //   51: aconst_null
      //   52: astore 5
      //   54: aconst_null
      //   55: astore 6
      //   57: aconst_null
      //   58: astore 4
      //   60: aload 5
      //   62: astore_2
      //   63: aload 6
      //   65: astore_1
      //   66: aload_0
      //   67: aload_0
      //   68: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   71: invokevirtual 288	java/net/HttpURLConnection:getResponseCode	()I
      //   74: putfield 290	com/dianping/dataservice/http/impl/DefaultHttpService$Task:statusCode	I
      //   77: aload 6
      //   79: astore_1
      //   80: aload_0
      //   81: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   84: invokevirtual 293	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
      //   87: astore_2
      //   88: aload_2
      //   89: astore_1
      //   90: aload_1
      //   91: ifnonnull +953 -> 1044
      //   94: aload_1
      //   95: astore_2
      //   96: aload_0
      //   97: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   100: invokevirtual 296	java/net/HttpURLConnection:getErrorStream	()Ljava/io/InputStream;
      //   103: astore 4
      //   105: aload 4
      //   107: astore_1
      //   108: aload_1
      //   109: ifnonnull +266 -> 375
      //   112: new 269	java/io/IOException
      //   115: dup
      //   116: ldc_w 298
      //   119: invokespecial 299	java/io/IOException:<init>	(Ljava/lang/String;)V
      //   122: athrow
      //   123: astore 4
      //   125: aload_1
      //   126: astore_2
      //   127: aload_2
      //   128: astore_1
      //   129: aload 4
      //   131: instanceof 301
      //   134: ifeq +864 -> 998
      //   137: bipush 152
      //   139: istore 9
      //   141: aload_2
      //   142: astore_1
      //   143: new 303	com/dianping/dataservice/http/impl/InnerHttpResponse
      //   146: dup
      //   147: iload 9
      //   149: aconst_null
      //   150: aconst_null
      //   151: aload 4
      //   153: invokespecial 306	com/dianping/dataservice/http/impl/InnerHttpResponse:<init>	(ILjava/lang/Object;Ljava/util/List;Ljava/lang/Object;)V
      //   156: astore 4
      //   158: aload_2
      //   159: astore_1
      //   160: aload_0
      //   161: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   164: ifnull +841 -> 1005
      //   167: aload_2
      //   168: astore_1
      //   169: aload_0
      //   170: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   173: invokevirtual 310	java/net/HttpURLConnection:getURL	()Ljava/net/URL;
      //   176: invokevirtual 311	java/net/URL:toString	()Ljava/lang/String;
      //   179: ldc_w 313
      //   182: invokevirtual 317	java/lang/String:startsWith	(Ljava/lang/String;)Z
      //   185: ifeq +820 -> 1005
      //   188: aload_2
      //   189: astore_1
      //   190: aload 4
      //   192: bipush 8
      //   194: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   197: aload_2
      //   198: ifnull +7 -> 205
      //   201: aload_2
      //   202: invokevirtual 321	java/io/InputStream:close	()V
      //   205: aload_0
      //   206: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   209: ifnull +10 -> 219
      //   212: aload_0
      //   213: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   216: invokevirtual 324	java/net/HttpURLConnection:disconnect	()V
      //   219: aload 4
      //   221: astore_1
      //   222: aload_3
      //   223: ifnull +10 -> 233
      //   226: aload_3
      //   227: invokevirtual 327	java/io/InputStream:reset	()V
      //   230: aload 4
      //   232: astore_1
      //   233: aload_1
      //   234: areturn
      //   235: astore_1
      //   236: aload_1
      //   237: invokevirtual 330	java/lang/Exception:printStackTrace	()V
      //   240: aload_1
      //   241: instanceof 301
      //   244: ifeq +54 -> 298
      //   247: bipush 153
      //   249: istore 9
      //   251: new 303	com/dianping/dataservice/http/impl/InnerHttpResponse
      //   254: dup
      //   255: iload 9
      //   257: aconst_null
      //   258: aconst_null
      //   259: aload_1
      //   260: invokespecial 306	com/dianping/dataservice/http/impl/InnerHttpResponse:<init>	(ILjava/lang/Object;Ljava/util/List;Ljava/lang/Object;)V
      //   263: astore_1
      //   264: aload_0
      //   265: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   268: ifnull +72 -> 340
      //   271: aload_0
      //   272: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   275: invokevirtual 310	java/net/HttpURLConnection:getURL	()Ljava/net/URL;
      //   278: invokevirtual 311	java/net/URL:toString	()Ljava/lang/String;
      //   281: ldc_w 313
      //   284: invokevirtual 317	java/lang/String:startsWith	(Ljava/lang/String;)Z
      //   287: ifeq +53 -> 340
      //   290: aload_1
      //   291: bipush 8
      //   293: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   296: aload_1
      //   297: areturn
      //   298: aload_1
      //   299: instanceof 332
      //   302: ifne +10 -> 312
      //   305: aload_1
      //   306: instanceof 334
      //   309: ifeq +10 -> 319
      //   312: bipush 154
      //   314: istore 9
      //   316: goto -65 -> 251
      //   319: bipush 151
      //   321: istore 9
      //   323: aload_0
      //   324: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   327: aload_0
      //   328: getfield 39	com/dianping/dataservice/http/impl/DefaultHttpService$Task:req	Lcom/dianping/dataservice/http/HttpRequest;
      //   331: bipush 151
      //   333: aload_1
      //   334: invokestatic 338	com/dianping/dataservice/http/impl/DefaultHttpService:access$200	(Lcom/dianping/dataservice/http/impl/DefaultHttpService;Lcom/dianping/dataservice/http/HttpRequest;ILjava/lang/Exception;)V
      //   337: goto -86 -> 251
      //   340: aload_1
      //   341: iconst_0
      //   342: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   345: aload_1
      //   346: areturn
      //   347: astore_1
      //   348: aload 5
      //   350: astore_2
      //   351: aload 6
      //   353: astore_1
      //   354: aload_0
      //   355: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   358: ldc_w 340
      //   361: invokevirtual 343	com/dianping/dataservice/http/impl/DefaultHttpService:log	(Ljava/lang/String;)V
      //   364: aload 4
      //   366: astore_1
      //   367: goto -277 -> 90
      //   370: astore 4
      //   372: goto -245 -> 127
      //   375: aload_0
      //   376: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   379: invokevirtual 346	java/net/HttpURLConnection:getContentLength	()I
      //   382: istore 10
      //   384: iload 10
      //   386: istore 9
      //   388: iload 10
      //   390: ifge +6 -> 396
      //   393: iconst_m1
      //   394: istore 9
      //   396: aload_0
      //   397: iload 9
      //   399: putfield 348	com/dianping/dataservice/http/impl/DefaultHttpService$Task:contentLength	I
      //   402: aload_0
      //   403: iconst_0
      //   404: putfield 350	com/dianping/dataservice/http/impl/DefaultHttpService$Task:receivedBytes	I
      //   407: aload_0
      //   408: getfield 348	com/dianping/dataservice/http/impl/DefaultHttpService$Task:contentLength	I
      //   411: ifgt +262 -> 673
      //   414: sipush 4096
      //   417: istore 9
      //   419: new 352	java/io/BufferedInputStream
      //   422: dup
      //   423: aload_1
      //   424: iload 9
      //   426: invokespecial 353	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
      //   429: astore 4
      //   431: aload 4
      //   433: astore_2
      //   434: aload 4
      //   436: astore_1
      //   437: new 204	com/dianping/util/PoolingByteArrayOutputStream
      //   440: dup
      //   441: aload_0
      //   442: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   445: invokestatic 196	com/dianping/dataservice/http/impl/DefaultHttpService:access$100	(Lcom/dianping/dataservice/http/impl/DefaultHttpService;)Lcom/dianping/util/ByteArrayPool;
      //   448: iload 9
      //   450: invokespecial 207	com/dianping/util/PoolingByteArrayOutputStream:<init>	(Lcom/dianping/util/ByteArrayPool;I)V
      //   453: astore 8
      //   455: aconst_null
      //   456: astore 5
      //   458: aload_0
      //   459: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   462: invokestatic 196	com/dianping/dataservice/http/impl/DefaultHttpService:access$100	(Lcom/dianping/dataservice/http/impl/DefaultHttpService;)Lcom/dianping/util/ByteArrayPool;
      //   465: sipush 4096
      //   468: invokevirtual 202	com/dianping/util/ByteArrayPool:getBuf	(I)[B
      //   471: astore 6
      //   473: lconst_0
      //   474: lstore 11
      //   476: aload 6
      //   478: astore 5
      //   480: aload 4
      //   482: aload 6
      //   484: invokevirtual 211	java/io/InputStream:read	([B)I
      //   487: istore 9
      //   489: iload 9
      //   491: iconst_m1
      //   492: if_icmpeq +230 -> 722
      //   495: aload 6
      //   497: astore 5
      //   499: aload 8
      //   501: aload 6
      //   503: iconst_0
      //   504: iload 9
      //   506: invokevirtual 217	java/io/ByteArrayOutputStream:write	([BII)V
      //   509: aload 6
      //   511: astore 5
      //   513: aload_0
      //   514: aload_0
      //   515: getfield 350	com/dianping/dataservice/http/impl/DefaultHttpService$Task:receivedBytes	I
      //   518: iload 9
      //   520: iadd
      //   521: putfield 350	com/dianping/dataservice/http/impl/DefaultHttpService$Task:receivedBytes	I
      //   524: aload 6
      //   526: astore 5
      //   528: ldc 144
      //   530: aload_0
      //   531: getfield 39	com/dianping/dataservice/http/impl/DefaultHttpService$Task:req	Lcom/dianping/dataservice/http/HttpRequest;
      //   534: invokeinterface 354 1 0
      //   539: invokevirtual 153	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   542: ifeq -66 -> 476
      //   545: aload 6
      //   547: astore 5
      //   549: aload_0
      //   550: getfield 348	com/dianping/dataservice/http/impl/DefaultHttpService$Task:contentLength	I
      //   553: sipush 4096
      //   556: if_icmplt -80 -> 476
      //   559: aload 6
      //   561: astore 5
      //   563: aload_0
      //   564: getfield 41	com/dianping/dataservice/http/impl/DefaultHttpService$Task:handler	Lcom/dianping/dataservice/RequestHandler;
      //   567: ifnull -91 -> 476
      //   570: aload 6
      //   572: astore 5
      //   574: aload_0
      //   575: getfield 350	com/dianping/dataservice/http/impl/DefaultHttpService$Task:receivedBytes	I
      //   578: aload_0
      //   579: getfield 348	com/dianping/dataservice/http/impl/DefaultHttpService$Task:contentLength	I
      //   582: if_icmplt +100 -> 682
      //   585: aload 6
      //   587: astore 5
      //   589: aload_0
      //   590: iconst_0
      //   591: anewarray 356	java/lang/Void
      //   594: invokevirtual 360	com/dianping/dataservice/http/impl/DefaultHttpService$Task:publishProgress	([Ljava/lang/Object;)V
      //   597: goto -121 -> 476
      //   600: astore 6
      //   602: aload 4
      //   604: astore_2
      //   605: aload 4
      //   607: astore_1
      //   608: aload_0
      //   609: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   612: invokestatic 196	com/dianping/dataservice/http/impl/DefaultHttpService:access$100	(Lcom/dianping/dataservice/http/impl/DefaultHttpService;)Lcom/dianping/util/ByteArrayPool;
      //   615: aload 5
      //   617: invokevirtual 248	com/dianping/util/ByteArrayPool:returnBuf	([B)V
      //   620: aload 4
      //   622: astore_2
      //   623: aload 4
      //   625: astore_1
      //   626: aload 8
      //   628: invokevirtual 249	java/io/ByteArrayOutputStream:close	()V
      //   631: aload 4
      //   633: astore_2
      //   634: aload 4
      //   636: astore_1
      //   637: aload 6
      //   639: athrow
      //   640: astore_2
      //   641: aload_1
      //   642: ifnull +7 -> 649
      //   645: aload_1
      //   646: invokevirtual 321	java/io/InputStream:close	()V
      //   649: aload_0
      //   650: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   653: ifnull +10 -> 663
      //   656: aload_0
      //   657: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   660: invokevirtual 324	java/net/HttpURLConnection:disconnect	()V
      //   663: aload_3
      //   664: ifnull +7 -> 671
      //   667: aload_3
      //   668: invokevirtual 327	java/io/InputStream:reset	()V
      //   671: aload_2
      //   672: athrow
      //   673: aload_0
      //   674: getfield 348	com/dianping/dataservice/http/impl/DefaultHttpService$Task:contentLength	I
      //   677: istore 9
      //   679: goto -260 -> 419
      //   682: aload 6
      //   684: astore 5
      //   686: invokestatic 365	android/os/SystemClock:elapsedRealtime	()J
      //   689: lstore 13
      //   691: lload 13
      //   693: lload 11
      //   695: lsub
      //   696: ldc2_w 366
      //   699: lcmp
      //   700: ifle -224 -> 476
      //   703: aload 6
      //   705: astore 5
      //   707: aload_0
      //   708: iconst_0
      //   709: anewarray 356	java/lang/Void
      //   712: invokevirtual 360	com/dianping/dataservice/http/impl/DefaultHttpService$Task:publishProgress	([Ljava/lang/Object;)V
      //   715: lload 13
      //   717: lstore 11
      //   719: goto -243 -> 476
      //   722: aload 6
      //   724: astore 5
      //   726: aload 8
      //   728: invokevirtual 236	java/io/ByteArrayOutputStream:toByteArray	()[B
      //   731: astore 7
      //   733: aload 4
      //   735: astore_2
      //   736: aload 4
      //   738: astore_1
      //   739: aload_0
      //   740: getfield 34	com/dianping/dataservice/http/impl/DefaultHttpService$Task:this$0	Lcom/dianping/dataservice/http/impl/DefaultHttpService;
      //   743: invokestatic 196	com/dianping/dataservice/http/impl/DefaultHttpService:access$100	(Lcom/dianping/dataservice/http/impl/DefaultHttpService;)Lcom/dianping/util/ByteArrayPool;
      //   746: aload 6
      //   748: invokevirtual 248	com/dianping/util/ByteArrayPool:returnBuf	([B)V
      //   751: aload 4
      //   753: astore_2
      //   754: aload 4
      //   756: astore_1
      //   757: aload 8
      //   759: invokevirtual 249	java/io/ByteArrayOutputStream:close	()V
      //   762: aload 4
      //   764: astore_2
      //   765: aload 4
      //   767: astore_1
      //   768: new 369	java/util/ArrayList
      //   771: dup
      //   772: bipush 8
      //   774: invokespecial 371	java/util/ArrayList:<init>	(I)V
      //   777: astore 5
      //   779: iconst_0
      //   780: istore 9
      //   782: aload 4
      //   784: astore_2
      //   785: aload 4
      //   787: astore_1
      //   788: aload_0
      //   789: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   792: iload 9
      //   794: invokevirtual 375	java/net/HttpURLConnection:getHeaderFieldKey	(I)Ljava/lang/String;
      //   797: astore 6
      //   799: aload 4
      //   801: astore_2
      //   802: aload 4
      //   804: astore_1
      //   805: aload_0
      //   806: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   809: iload 9
      //   811: invokevirtual 378	java/net/HttpURLConnection:getHeaderField	(I)Ljava/lang/String;
      //   814: astore 8
      //   816: aload 6
      //   818: ifnonnull +125 -> 943
      //   821: aload 8
      //   823: ifnonnull +120 -> 943
      //   826: aload 4
      //   828: astore_2
      //   829: aload 4
      //   831: astore_1
      //   832: new 303	com/dianping/dataservice/http/impl/InnerHttpResponse
      //   835: dup
      //   836: aload_0
      //   837: getfield 290	com/dianping/dataservice/http/impl/DefaultHttpService$Task:statusCode	I
      //   840: aload 7
      //   842: aload 5
      //   844: aconst_null
      //   845: invokespecial 306	com/dianping/dataservice/http/impl/InnerHttpResponse:<init>	(ILjava/lang/Object;Ljava/util/List;Ljava/lang/Object;)V
      //   848: astore 5
      //   850: aload 4
      //   852: astore_2
      //   853: aload 4
      //   855: astore_1
      //   856: aload_0
      //   857: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   860: ifnull +115 -> 975
      //   863: aload 4
      //   865: astore_2
      //   866: aload 4
      //   868: astore_1
      //   869: aload_0
      //   870: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   873: invokevirtual 310	java/net/HttpURLConnection:getURL	()Ljava/net/URL;
      //   876: invokevirtual 311	java/net/URL:toString	()Ljava/lang/String;
      //   879: ldc_w 313
      //   882: invokevirtual 317	java/lang/String:startsWith	(Ljava/lang/String;)Z
      //   885: ifeq +90 -> 975
      //   888: aload 4
      //   890: astore_2
      //   891: aload 4
      //   893: astore_1
      //   894: aload 5
      //   896: bipush 8
      //   898: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   901: aload 4
      //   903: ifnull +8 -> 911
      //   906: aload 4
      //   908: invokevirtual 321	java/io/InputStream:close	()V
      //   911: aload_0
      //   912: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   915: ifnull +10 -> 925
      //   918: aload_0
      //   919: getfield 282	com/dianping/dataservice/http/impl/DefaultHttpService$Task:urlConnection	Ljava/net/HttpURLConnection;
      //   922: invokevirtual 324	java/net/HttpURLConnection:disconnect	()V
      //   925: aload 5
      //   927: astore_1
      //   928: aload_3
      //   929: ifnull -696 -> 233
      //   932: aload_3
      //   933: invokevirtual 327	java/io/InputStream:reset	()V
      //   936: aload 5
      //   938: areturn
      //   939: astore_1
      //   940: aload 5
      //   942: areturn
      //   943: aload 4
      //   945: astore_2
      //   946: aload 4
      //   948: astore_1
      //   949: aload 5
      //   951: new 380	org/apache/http/message/BasicNameValuePair
      //   954: dup
      //   955: aload 6
      //   957: aload 8
      //   959: invokespecial 382	org/apache/http/message/BasicNameValuePair:<init>	(Ljava/lang/String;Ljava/lang/String;)V
      //   962: invokevirtual 385	java/util/ArrayList:add	(Ljava/lang/Object;)Z
      //   965: pop
      //   966: iload 9
      //   968: iconst_1
      //   969: iadd
      //   970: istore 9
      //   972: goto -190 -> 782
      //   975: aload 4
      //   977: astore_2
      //   978: aload 4
      //   980: astore_1
      //   981: aload 5
      //   983: iconst_0
      //   984: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   987: goto -86 -> 901
      //   990: astore_1
      //   991: aload_1
      //   992: invokevirtual 386	java/io/IOException:printStackTrace	()V
      //   995: goto -84 -> 911
      //   998: bipush 150
      //   1000: istore 9
      //   1002: goto -861 -> 141
      //   1005: aload_2
      //   1006: astore_1
      //   1007: aload 4
      //   1009: iconst_0
      //   1010: putfield 320	com/dianping/dataservice/http/impl/InnerHttpResponse:source	I
      //   1013: goto -816 -> 197
      //   1016: astore_1
      //   1017: aload_1
      //   1018: invokevirtual 386	java/io/IOException:printStackTrace	()V
      //   1021: goto -816 -> 205
      //   1024: astore_1
      //   1025: aload_1
      //   1026: invokevirtual 386	java/io/IOException:printStackTrace	()V
      //   1029: goto -380 -> 649
      //   1032: astore_1
      //   1033: aload 4
      //   1035: areturn
      //   1036: astore_1
      //   1037: goto -366 -> 671
      //   1040: astore_2
      //   1041: goto -400 -> 641
      //   1044: goto -936 -> 108
      //
      // Exception table:
      //   from	to	target	type
      //   112	123	123	java/io/IOException
      //   375	384	123	java/io/IOException
      //   396	414	123	java/io/IOException
      //   419	431	123	java/io/IOException
      //   673	679	123	java/io/IOException
      //   36	51	235	java/lang/Exception
      //   80	88	347	java/io/IOException
      //   66	77	370	java/io/IOException
      //   96	105	370	java/io/IOException
      //   354	364	370	java/io/IOException
      //   437	455	370	java/io/IOException
      //   608	620	370	java/io/IOException
      //   626	631	370	java/io/IOException
      //   637	640	370	java/io/IOException
      //   739	751	370	java/io/IOException
      //   757	762	370	java/io/IOException
      //   768	779	370	java/io/IOException
      //   788	799	370	java/io/IOException
      //   805	816	370	java/io/IOException
      //   832	850	370	java/io/IOException
      //   856	863	370	java/io/IOException
      //   869	888	370	java/io/IOException
      //   894	901	370	java/io/IOException
      //   949	966	370	java/io/IOException
      //   981	987	370	java/io/IOException
      //   458	473	600	finally
      //   480	489	600	finally
      //   499	509	600	finally
      //   513	524	600	finally
      //   528	545	600	finally
      //   549	559	600	finally
      //   563	570	600	finally
      //   574	585	600	finally
      //   589	597	600	finally
      //   686	691	600	finally
      //   707	715	600	finally
      //   726	733	600	finally
      //   66	77	640	finally
      //   80	88	640	finally
      //   96	105	640	finally
      //   129	137	640	finally
      //   143	158	640	finally
      //   160	167	640	finally
      //   169	188	640	finally
      //   190	197	640	finally
      //   354	364	640	finally
      //   437	455	640	finally
      //   608	620	640	finally
      //   626	631	640	finally
      //   637	640	640	finally
      //   739	751	640	finally
      //   757	762	640	finally
      //   768	779	640	finally
      //   788	799	640	finally
      //   805	816	640	finally
      //   832	850	640	finally
      //   856	863	640	finally
      //   869	888	640	finally
      //   894	901	640	finally
      //   949	966	640	finally
      //   981	987	640	finally
      //   1007	1013	640	finally
      //   932	936	939	java/lang/Exception
      //   906	911	990	java/io/IOException
      //   201	205	1016	java/io/IOException
      //   645	649	1024	java/io/IOException
      //   226	230	1032	java/lang/Exception
      //   667	671	1036	java/lang/Exception
      //   112	123	1040	finally
      //   375	384	1040	finally
      //   396	414	1040	finally
      //   419	431	1040	finally
      //   673	679	1040	finally
    }

    protected BasicHttpRequest getHttpRequest()
      throws Exception
    {
      InetSocketAddress localInetSocketAddress = DefaultHttpService.this.networkInfo.getProxy();
      Proxy localProxy = null;
      if (localInetSocketAddress != null)
        localProxy = new Proxy(Proxy.Type.HTTP, localInetSocketAddress);
      return new BasicHttpRequest(this.req.url(), this.req.method(), this.req.input(), this.req.headers(), this.req.timeout(), localProxy);
    }

    public void notify(int paramInt)
    {
      if ((this.handler == null) || (!this.isUploadProgress));
      long l;
      do
      {
        return;
        this.sentBytes += paramInt;
        if (this.sentBytes >= this.availableBytes)
        {
          publishProgress(new Void[0]);
          return;
        }
        l = SystemClock.elapsedRealtime();
      }
      while (l - this.prevProgressTime <= 50L);
      publishProgress(new Void[0]);
      this.prevProgressTime = l;
    }

    protected void onCancelled()
    {
      if (DefaultHttpService.this.isLoggable())
      {
        long l1 = SystemClock.elapsedRealtime();
        long l2 = this.startTime;
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("abort (");
        ((StringBuilder)localObject).append(this.req.method()).append(',');
        ((StringBuilder)localObject).append(this.statusCode).append(',');
        ((StringBuilder)localObject).append(l1 - l2).append("ms");
        ((StringBuilder)localObject).append(") ").append(this.req.url());
        DefaultHttpService.this.log(((StringBuilder)localObject).toString());
        if ((this.req.input() instanceof FormInputStream))
        {
          localObject = (FormInputStream)this.req.input();
          DefaultHttpService.this.log("    " + ((FormInputStream)localObject).toString());
        }
      }
      if (this.urlConnection != null)
        this.urlConnection.disconnect();
    }

    protected void onPostExecute(HttpResponse paramHttpResponse)
    {
      long l1;
      long l2;
      Object localObject;
      if (DefaultHttpService.this.runningTasks.remove(this.req, this))
      {
        if (paramHttpResponse.result() == null)
          break label265;
        this.handler.onRequestFinish(this.req, paramHttpResponse);
        if (DefaultHttpService.this.isLoggable())
        {
          l1 = SystemClock.elapsedRealtime();
          l2 = this.startTime;
          localObject = new StringBuilder();
          if (paramHttpResponse.result() == null)
            break label282;
          ((StringBuilder)localObject).append("finish (");
        }
      }
      while (true)
      {
        ((StringBuilder)localObject).append(this.req.method()).append(',');
        ((StringBuilder)localObject).append(this.statusCode).append(',');
        ((StringBuilder)localObject).append(l1 - l2).append("ms");
        ((StringBuilder)localObject).append(") ").append(this.req.url());
        DefaultHttpService.this.log(((StringBuilder)localObject).toString());
        if ((this.req.input() instanceof FormInputStream))
        {
          localObject = (FormInputStream)this.req.input();
          DefaultHttpService.this.log("    " + ((FormInputStream)localObject).toString());
        }
        if (paramHttpResponse.result() == null)
          DefaultHttpService.this.log("    " + paramHttpResponse.error());
        return;
        label265: this.handler.onRequestFailed(this.req, paramHttpResponse);
        break;
        label282: ((StringBuilder)localObject).append("fail (");
      }
    }

    protected void onPreExecute()
    {
      if ((this.handler instanceof FullRequestHandle))
        ((FullRequestHandle)this.handler).onRequestStart(this.req);
      this.startTime = SystemClock.elapsedRealtime();
    }

    protected void onProgressUpdate(Void[] paramArrayOfVoid)
    {
      if (this.isUploadProgress)
        if ((this.handler instanceof FullRequestHandle))
          ((FullRequestHandle)this.handler).onRequestProgress(this.req, this.sentBytes, this.availableBytes);
      do
        return;
      while (!(this.handler instanceof FullRequestHandle));
      ((FullRequestHandle)this.handler).onRequestProgress(this.req, this.receivedBytes, this.contentLength);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.impl.DefaultHttpService
 * JD-Core Version:    0.6.0
 */