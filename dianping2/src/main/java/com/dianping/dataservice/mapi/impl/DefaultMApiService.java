package com.dianping.dataservice.mapi.impl;

import B;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.impl.BaseAccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.Unarchiver;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.FormInputStream;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.http.TimeoutConfigHelper;
import com.dianping.dataservice.http.WnsHttpService;
import com.dianping.dataservice.http.fork.ForkHttpService;
import com.dianping.dataservice.http.impl.BasicHttpResponse;
import com.dianping.dataservice.http.impl.DefaultHttpService;
import com.dianping.dataservice.http.impl.DefaultHttpService.Task;
import com.dianping.dataservice.http.impl.InnerHttpResponse;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.monitor.MonitorService;
import com.dianping.monitor.impl.DefaultMonitorService;
import com.dianping.statistics.StatisticsService;
import com.dianping.tunnel.AndroidTunnel;
import com.dianping.tunnel.TunnelRequest;
import com.dianping.util.BlockingItem;
import com.dianping.util.ChainInputStream;
import com.dianping.util.Daemon;
import com.dianping.util.Log;
import com.dianping.util.NativeHelper;
import com.dianping.util.WrapInputStream;
import com.dianping.utn.client.AndroidUtnConnection;
import com.dianping.utn.client.UtnConnection.Session;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class DefaultMApiService
  implements MApiService
{
  public static boolean ENABLE_LOG = false;
  private static final int HTTP_TIMEOUT_2G = 15000;
  private static final int HTTP_TIMEOUT_3G = 10000;
  private static final int HTTP_TIMEOUT_4G = 10000;
  private static final int HTTP_TIMEOUT_WIFI = 10000;
  public static final String PROTOCOL_VERSION = "1.1";
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  private static final String TAG = "mapi";
  private AccountService account;
  private String androidId;
  private final String c;
  private MApiCacheService cache;
  private final RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> cacheHandler = new RequestHandler()
  {
    public void onRequestFailed(com.dianping.dataservice.http.HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultMApiService.Session localSession = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
      if (localSession == null);
      do
      {
        do
        {
          return;
          localSession.cacheResponse = paramHttpResponse;
          if (localSession.status != 1)
            continue;
          localSession.status = 2;
          DefaultMApiService.this.http().exec(paramHttpRequest, DefaultMApiService.this.httpHandler);
          return;
        }
        while (localSession.status != 3);
        paramHttpResponse = new BasicMApiResponse(localSession.httpResponse.statusCode(), null, null, localSession.httpResponse.headers(), localSession.httpResponse.error());
        localSession.handler.onRequestFailed((MApiRequest)paramHttpRequest, paramHttpResponse);
        paramHttpResponse = new StringBuilder("cache-fail3 ");
        if (DefaultMApiService.this.monitor != null)
          paramHttpResponse.append(DefaultMApiService.this.monitor.getCommand(paramHttpRequest.url())).append(" ");
        DefaultMApiService.this.addDiagnosis(paramHttpResponse.toString());
        if ((DefaultMApiService.this.statistics == null) || (localSession.request.disableStatistics()))
          continue;
        paramHttpResponse = new ArrayList(4);
        paramHttpResponse.add(new BasicNameValuePair("network", DefaultMApiService.this.networkInfo.getNetworkInfo()));
        paramHttpResponse.add(new BasicNameValuePair("elapse", "-1"));
        paramHttpResponse.add(new BasicNameValuePair("tag", String.valueOf(localSession.httpResponse.statusCode() / 100 * 100)));
        DefaultMApiService.this.statistics.pageView(localSession.request.url(), paramHttpResponse);
      }
      while (!Log.isLoggable(3));
      Log.d("mapi", "fail (cache.CRITICAL) " + paramHttpRequest.url());
    }

    public void onRequestFinish(com.dianping.dataservice.http.HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultMApiService.Session localSession = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
      if ((localSession == null) || ((localSession.status != 1) && (localSession.status != 3)));
      MApiRequest localMApiRequest;
      do
        while (true)
        {
          return;
          localSession.cacheResponse = paramHttpResponse;
          localMApiRequest = (MApiRequest)paramHttpRequest;
          try
          {
            paramHttpResponse = (byte[])(byte[])paramHttpResponse.result();
            paramHttpResponse = new BasicMApiResponse(0, paramHttpResponse, DefaultMApiService.this.getResult(paramHttpResponse), Collections.emptyList(), null);
            DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
            localSession.handler.onRequestFinish(localMApiRequest, paramHttpResponse);
            if ((DefaultMApiService.this.statistics != null) && (!localSession.request.disableStatistics()))
            {
              paramHttpResponse = new ArrayList(4);
              paramHttpResponse.add(new BasicNameValuePair("network", "cache"));
              paramHttpResponse.add(new BasicNameValuePair("tag", "CACHE"));
              DefaultMApiService.this.statistics.pageView(localSession.request.url(), paramHttpResponse);
            }
            if (!Log.isLoggable(3))
              continue;
            Log.d("mapi", "finish (cache." + localMApiRequest.defaultCacheType() + ") " + paramHttpRequest.url());
            return;
          }
          catch (Exception paramHttpRequest)
          {
            Log.e("mapi", "exception when processing cached data, ignored", paramHttpRequest);
          }
        }
      while (localSession.status != 1);
      localSession.status = 2;
      DefaultMApiService.this.http().exec(localMApiRequest, DefaultMApiService.this.httpHandler);
    }
  };
  private ConfigService config;
  private Context context;
  private String deviceId;
  private String deviceSerialId;
  private final Handler dhandler = new Handler(Daemon.looper())
  {
    public void handleMessage(Message paramMessage)
    {
      paramMessage = (DefaultMApiService.Session)paramMessage.obj;
      Object localObject;
      String str1;
      String str2;
      if (paramMessage.writeToCache != null)
      {
        DefaultMApiService.this.cache().put(paramMessage.request, new BasicHttpResponse(0, paramMessage.writeToCache, paramMessage.httpResponse.headers(), null), System.currentTimeMillis());
        if ((paramMessage.request.defaultCacheType() == CacheType.NORMAL) && (paramMessage.writeToCache.length > 0) && (paramMessage.writeToCache[0] == 79))
        {
          localObject = new DPObject(paramMessage.writeToCache, 0, paramMessage.writeToCache.length);
          int i = ((DPObject)localObject).getInt("StartIndex");
          int j = ((DPObject)localObject).getInt("NextStartIndex");
          if (j > i)
          {
            localObject = paramMessage.request.url();
            str1 = "start=" + i;
            str2 = "start=" + j;
            if (!((String)localObject).endsWith(str1))
              break label385;
            localObject = ((String)localObject).replaceFirst(str1, str2);
            DefaultMApiService.this.cache.remove(BasicMApiRequest.mapiGet((String)localObject, CacheType.NORMAL));
          }
        }
      }
      while (true)
      {
        if ((DefaultMApiService.this.statistics != null) && (!paramMessage.request.disableStatistics()))
        {
          localObject = new ArrayList(4);
          ((ArrayList)localObject).add(new BasicNameValuePair("network", DefaultMApiService.this.networkInfo.getNetworkInfo()));
          ((ArrayList)localObject).add(new BasicNameValuePair("elapse", String.valueOf(paramMessage.time)));
          str1 = DefaultMApiService.this.getIp(paramMessage.request.url());
          if (str1 != null)
            ((ArrayList)localObject).add(new BasicNameValuePair("ip", str1));
          ((ArrayList)localObject).add(new BasicNameValuePair("tag", String.valueOf(paramMessage.httpResponse.statusCode() / 100 * 100)));
          DefaultMApiService.this.statistics.pageView(paramMessage.request.url(), (List)localObject);
        }
        return;
        label385: if (!((String)localObject).contains(str1 + "&"))
          continue;
        localObject = ((String)localObject).replaceFirst(str1 + "&", str2 + "&");
        DefaultMApiService.this.cache.remove(BasicMApiRequest.mapiGet((String)localObject, CacheType.NORMAL));
      }
    }
  };
  private final LinkedList<String> diagnosis = new LinkedList();
  private DismissTokenListener dismissTokenListener;
  private String dismissedToken = "";
  private String dpid;
  private MApiRequest dpidRequest;
  private ForkHttpService http;
  private final HttpHandler httpHandler = new HttpHandler(null);
  private final byte[] k;
  private long lastDnsResetTime = 0L;
  private LocalDNS mobileDns;
  private MonitorService monitor;
  private NetworkInfoHelper networkInfo;
  private final ConcurrentHashMap<MApiRequest, Session> runningSession = new ConcurrentHashMap();
  private StatisticsService statistics;
  private TimeoutConfigHelper timeoutConfigHelper;
  private MyTunnel tunnel;
  private MonitorService tunnelMonitor;
  private String userAgent;
  private MyUtnConnection utn;
  private String uuid;
  private final byte[] v;
  private LocalDNS wifiDns;
  private String wifiMac;
  private MyWnsService wns;

  static
  {
    ENABLE_LOG = false;
  }

  public DefaultMApiService(Context paramContext, String paramString1, String paramString2, String paramString3, ConfigService paramConfigService, AccountService paramAccountService, StatisticsService paramStatisticsService, MonitorService paramMonitorService)
  {
    this(paramContext, paramString1, paramString2, paramString3, paramConfigService, paramAccountService, paramStatisticsService, paramMonitorService, null, null, null);
  }

  public DefaultMApiService(Context paramContext, String paramString1, String paramString2, String paramString3, ConfigService paramConfigService, AccountService paramAccountService, StatisticsService paramStatisticsService, MonitorService paramMonitorService, String paramString4, String paramString5, String paramString6)
  {
    this.context = paramContext;
    int j = paramString1.indexOf('(');
    String str = paramString1;
    if (j > 0)
      str = "MApi 1.1 " + paramString1.substring(j);
    this.userAgent = str;
    this.deviceId = paramString2;
    this.uuid = paramString3;
    this.wifiMac = paramString4;
    this.deviceSerialId = paramString5;
    this.androidId = paramString6;
    this.config = paramConfigService;
    this.account = paramAccountService;
    this.statistics = paramStatisticsService;
    this.monitor = paramMonitorService;
    this.tunnelMonitor = new DefaultMonitorService(paramContext, "http://114.80.165.63/broker-service/api/connection?");
    this.networkInfo = new NetworkInfoHelper(paramContext);
    this.timeoutConfigHelper = new TimeoutConfigHelper(paramConfigService, this.networkInfo);
    this.dpid = paramContext.getSharedPreferences(paramContext.getPackageName(), 0).getString("dpid", null);
    this.mobileDns = new LocalDNS("mobile", paramContext, paramMonitorService, this.timeoutConfigHelper);
    this.wifiDns = new LocalDNS("wifi", paramContext, paramMonitorService, this.timeoutConfigHelper);
    this.c = new String(new char[] { 65, 69, 83, 47, 67, 66, 67, 47, 78, 111, 80, 97, 100, 100, 105, 110, 103 });
    paramContext = new byte[16];
    Context tmp424_423 = paramContext;
    tmp424_423[0] = 92;
    Context tmp430_424 = tmp424_423;
    tmp430_424[1] = 115;
    Context tmp436_430 = tmp430_424;
    tmp436_430[2] = 116;
    Context tmp442_436 = tmp436_430;
    tmp442_436[3] = 117;
    Context tmp448_442 = tmp442_436;
    tmp448_442[4] = 112;
    Context tmp454_448 = tmp448_442;
    tmp454_448[5] = 113;
    Context tmp460_454 = tmp454_448;
    tmp460_454[6] = 6;
    Context tmp467_460 = tmp460_454;
    tmp467_460[7] = 112;
    Context tmp474_467 = tmp467_460;
    tmp474_467[8] = 112;
    Context tmp481_474 = tmp474_467;
    tmp481_474[9] = 3;
    Context tmp488_481 = tmp481_474;
    tmp488_481[10] = 3;
    Context tmp495_488 = tmp488_481;
    tmp495_488[11] = 4;
    Context tmp502_495 = tmp495_488;
    tmp502_495[12] = 6;
    Context tmp509_502 = tmp502_495;
    tmp509_502[13] = 118;
    Context tmp516_509 = tmp509_502;
    tmp516_509[14] = 0;
    Context tmp522_516 = tmp516_509;
    tmp522_516[15] = 112;
    tmp522_516;
    int m = 24;
    j = 0;
    int i;
    while (j < paramContext.length)
    {
      i = (byte)(paramContext[j] & 0xFF ^ m);
      paramContext[j] = i;
      m = i;
      j += 1;
    }
    this.k = paramContext;
    paramContext = new byte[16];
    Context tmp588_587 = paramContext;
    tmp588_587[0] = 0;
    Context tmp593_588 = tmp588_587;
    tmp593_588[1] = 118;
    Context tmp599_593 = tmp593_588;
    tmp599_593[2] = 122;
    Context tmp605_599 = tmp599_593;
    tmp605_599[3] = 10;
    Context tmp611_605 = tmp605_599;
    tmp611_605[4] = 3;
    Context tmp617_611 = tmp611_605;
    tmp617_611[5] = 116;
    Context tmp623_617 = tmp617_611;
    tmp623_617[6] = 124;
    Context tmp630_623 = tmp623_617;
    tmp630_623[7] = 10;
    Context tmp637_630 = tmp630_623;
    tmp637_630[8] = 5;
    Context tmp644_637 = tmp637_630;
    tmp644_637[9] = 117;
    Context tmp651_644 = tmp644_637;
    tmp651_644[10] = 6;
    Context tmp658_651 = tmp651_644;
    tmp658_651[11] = 5;
    Context tmp665_658 = tmp658_651;
    tmp665_658[12] = 3;
    Context tmp672_665 = tmp665_658;
    tmp672_665[13] = 4;
    Context tmp679_672 = tmp672_665;
    tmp679_672[14] = 2;
    Context tmp686_679 = tmp679_672;
    tmp686_679[15] = 37;
    tmp686_679;
    m = 97;
    j = paramContext.length - 1;
    while (j >= 0)
    {
      i = (byte)(paramContext[j] & 0xFF ^ m);
      paramContext[j] = i;
      m = i;
      j -= 1;
    }
    this.v = paramContext;
  }

  private InputStream dins(InputStream paramInputStream)
  {
    if ((paramInputStream instanceof MApiFormInputStream))
      return new MApiFormWrapper((MApiFormInputStream)paramInputStream);
    if ((paramInputStream instanceof ChainInputStream))
    {
      paramInputStream = ((ChainInputStream)paramInputStream).streams();
      InputStream[] arrayOfInputStream = new InputStream[paramInputStream.length];
      int i = 0;
      if (i < paramInputStream.length)
      {
        if ((paramInputStream[i] instanceof MApiFormInputStream))
          arrayOfInputStream[i] = new MApiFormWrapper((MApiFormInputStream)paramInputStream[i]);
        while (true)
        {
          i += 1;
          break;
          arrayOfInputStream[i] = paramInputStream[i];
        }
      }
      return new ChainInputStream(arrayOfInputStream);
    }
    return paramInputStream;
  }

  private static String getCommand(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    int j = paramString.indexOf('?');
    int i = j;
    if (j < 0)
      i = paramString.length();
    int m = paramString.lastIndexOf('/', i);
    j = m;
    if (m < 0)
      j = -1;
    return paramString.substring(j + 1, i);
  }

  private long getFlexibleTimeout()
  {
    int i = 15000;
    switch (new NetworkInfoHelper(this.context).getNetworkType())
    {
    default:
    case 2:
    case 3:
    case 4:
    case 1:
    }
    while (true)
    {
      return i;
      i = 15000;
      continue;
      i = 10000;
      continue;
      i = 10000;
      continue;
      i = 10000;
    }
  }

  public static String getHost(String paramString)
  {
    int i = paramString.indexOf("://");
    if (i < 0)
      return "";
    int j = paramString.indexOf('/', i + 3);
    if (j > 0)
      return paramString.substring(i + 3, j);
    return paramString.substring(i + 3);
  }

  private String getIp(String paramString)
  {
    if (paramString == null);
    do
      return null;
    while (!paramString.startsWith("http://"));
    String str = paramString.substring(7);
    int i = str.indexOf('/');
    paramString = str;
    if (i > 0)
      paramString = str.substring(0, i);
    try
    {
      paramString = Inet4Address.getByName(paramString).getAddress();
      paramString = (paramString[0] & 0xFF) + "." + (paramString[1] & 0xFF) + "." + (paramString[2] & 0xFF) + "." + (paramString[3] & 0xFF);
      return paramString;
    }
    catch (Exception paramString)
    {
    }
    return null;
  }

  private Object getResult(byte[] paramArrayOfByte)
    throws Exception
  {
    if ((paramArrayOfByte.length > 0) && (paramArrayOfByte[0] == 65))
      return DPObject.createArray(paramArrayOfByte, 0, paramArrayOfByte.length);
    if ((paramArrayOfByte.length > 0) && (paramArrayOfByte[0] == 83))
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, 1, paramArrayOfByte.length - 1);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      return new String(paramArrayOfByte, 3, localByteBuffer.getShort() & 0xFFFF, "UTF-8");
    }
    return DPObject.createObject(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  private HttpService http()
  {
    monitorenter;
    try
    {
      if (this.http == null)
      {
        if (WnsHttpService.isWnsAvailable())
          this.wns = new MyWnsService(null);
        this.tunnel = new MyTunnel(this.context);
        localObject1 = new MApiHttpService(this.context);
        this.utn = new MyUtnConnection(this.context);
        this.http = new ForkHttpService(this.context, this.config, this.tunnelMonitor, this.wns, this.tunnel, (HttpService)localObject1, this.utn)
        {
          private boolean switchDomain(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            if (Log.LEVEL >= 2147483647);
            while (true)
            {
              return false;
              try
              {
                com.dianping.dataservice.http.HttpRequest localHttpRequest = DefaultMApiService.this.transferUriRequest(paramHttpRequest);
                boolean bool = paramHttpRequest.url().equals(localHttpRequest.url());
                if (!bool)
                  return true;
              }
              catch (Exception paramHttpRequest)
              {
              }
            }
            return false;
          }

          protected int httpHold(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            int i = DefaultMApiService.this.timeoutConfigHelper.getHttpHold();
            if (i > 0)
              return i;
            return super.httpHold(paramHttpRequest);
          }

          protected boolean httpsEnabled(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            return DefaultMApiService.this.httpsEnable(paramHttpRequest);
          }

          protected boolean tunnelEnabled(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            if ((switchDomain(paramHttpRequest)) || (DefaultMApiService.this.httpsEnable(paramHttpRequest)))
              return false;
            return super.tunnelEnabled(paramHttpRequest);
          }

          protected int tunnelTimeout()
          {
            int i = DefaultMApiService.this.timeoutConfigHelper.getTunnelTimeout();
            if (i > 0)
              return i;
            return super.tunnelTimeout();
          }

          protected boolean utnEnabled(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            if ((switchDomain(paramHttpRequest)) || (DefaultMApiService.this.httpsEnable(paramHttpRequest)))
              return false;
            return super.utnEnabled(paramHttpRequest);
          }

          protected int utnHold(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            int i = DefaultMApiService.this.timeoutConfigHelper.getUtnHold();
            if (i > 0)
              return i;
            return super.utnHold(paramHttpRequest);
          }

          protected boolean wnsEnabled(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
          {
            if ((switchDomain(paramHttpRequest)) || (DefaultMApiService.this.httpsEnable(paramHttpRequest)))
              return false;
            return super.wnsEnabled(paramHttpRequest);
          }
        };
      }
      Object localObject1 = this.http;
      return localObject1;
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  private boolean httpsEnable(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
  {
    if ((paramHttpRequest.url() != null) && (paramHttpRequest.url().startsWith("https://")))
      return true;
    Object localObject = this.config.dump().optString("httpsWhiteList", "");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      paramHttpRequest = getCommand(paramHttpRequest.url());
      localObject = new StringTokenizer((String)localObject, "|");
      while (((StringTokenizer)localObject).hasMoreTokens())
        if (paramHttpRequest.equals(((StringTokenizer)localObject).nextToken()))
          return true;
    }
    return false;
  }

  private String mergeDomain(String paramString)
  {
    if (paramString.startsWith("https://"))
      return paramString;
    String str;
    switch (this.networkInfo.getNetworkType())
    {
    default:
      str = this.mobileDns.getHost();
    case 1:
    case 0:
    }
    while (true)
    {
      return mergeDomain(paramString, str);
      str = this.wifiDns.getHost();
      continue;
      str = "mapi.dianping.com";
    }
  }

  private String mergeDomain(String paramString1, String paramString2)
  {
    try
    {
      int i = paramString1.indexOf("://");
      int j = paramString1.indexOf('/', i + 3);
      str3 = paramString1.substring(0, i);
      str4 = paramString1.substring(i + 3, j);
      String str1 = paramString1.substring(j + 1);
      Object localObject = this.config.dump().optString("domainMap", "m.api.dianping.com>mapi|app.t.dianping.com>tuan|l.api.dianping.com>locate|waimai.api.dianping.com>waimai|mc.api.dianping.com>mc|rs.api.dianping.com>rs|hui.api.dianping.com>hui|menu.api.dianping.com>menu|beauty.api.dianping.com>beauty|app.movie.dianping.com>movie");
      String str5 = str4 + ">";
      str4 = paramString1;
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localObject = new StringTokenizer((String)localObject, "|");
        while (true)
        {
          str4 = paramString1;
          if (!((StringTokenizer)localObject).hasMoreTokens())
            break;
          str4 = ((StringTokenizer)localObject).nextToken();
          if (!str4.startsWith(str5))
            continue;
          paramString1 = new StringBuilder();
          paramString1.append(str3).append("://");
          paramString1.append(paramString2).append('/');
          paramString1.append(str4.substring(str5.length())).append('/');
          paramString1.append(str1);
          str4 = paramString1.toString();
        }
      }
      return str4;
    }
    catch (Exception str2)
    {
      while (true)
      {
        String str3 = "http";
        String str4 = "?";
        String str2 = "";
      }
    }
  }

  static SocketAddress parseAddress(String paramString, int paramInt)
  {
    int j = paramString.indexOf(':');
    if (j < 0)
      return new InetSocketAddress(paramString, paramInt);
    try
    {
      int i = Integer.parseInt(paramString.substring(j + 1));
      paramInt = i;
      label37: return new InetSocketAddress(paramString.substring(0, j), paramInt);
    }
    catch (Exception localException)
    {
      break label37;
    }
  }

  static List<SocketAddress> parseAddressList(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
    {
      paramString = Collections.emptyList();
      return paramString;
    }
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "|");
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      paramString = localArrayList;
      if (!localStringTokenizer.hasMoreTokens())
        break;
      paramString = localStringTokenizer.nextToken();
      if (TextUtils.isEmpty(paramString))
        continue;
      localArrayList.add(parseAddress(paramString, paramInt));
    }
  }

  public static String replaceHost(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf("://");
    if (i < 0)
      return paramString1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1.substring(0, i + 3));
    localStringBuilder.append(paramString2);
    i = paramString1.indexOf('/', i + 3);
    if (i > 0)
      localStringBuilder.append(paramString1.substring(i));
    return localStringBuilder.toString();
  }

  private com.dianping.dataservice.http.HttpRequest transferHttpsUriRequest(com.dianping.dataservice.http.HttpRequest paramHttpRequest1, com.dianping.dataservice.http.HttpRequest paramHttpRequest2)
  {
    if (httpsEnable(paramHttpRequest1))
    {
      paramHttpRequest1 = paramHttpRequest2.url();
      if ((paramHttpRequest1 != null) && (paramHttpRequest1.startsWith("http://")))
      {
        String str1 = "https" + paramHttpRequest1.substring(4);
        String str2 = paramHttpRequest2.method();
        InputStream localInputStream = paramHttpRequest2.input();
        List localList = paramHttpRequest2.headers();
        long l = paramHttpRequest2.timeout();
        if ((paramHttpRequest2 instanceof BasicHttpRequest));
        for (paramHttpRequest1 = ((BasicHttpRequest)paramHttpRequest2).proxy(); ; paramHttpRequest1 = null)
          return new BasicHttpRequest(str1, str2, localInputStream, localList, l, paramHttpRequest1);
      }
    }
    return paramHttpRequest2;
  }

  public void abort(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, boolean paramBoolean)
  {
    Session localSession = (Session)this.runningSession.get(paramMApiRequest);
    if ((localSession != null) && (localSession.handler == paramRequestHandler))
    {
      this.runningSession.remove(paramMApiRequest, localSession);
      if (localSession.status != 2)
        break label69;
      http().abort(paramMApiRequest, this.httpHandler, true);
    }
    while (true)
    {
      localSession.status = 0;
      return;
      label69: if ((localSession.status != 1) || (!Log.isLoggable(3)))
        continue;
      Log.d("mapi", "abort (cache." + paramMApiRequest.defaultCacheType() + ") " + paramMApiRequest.url());
    }
  }

  void addDiagnosis(String paramString)
  {
    this.diagnosis.add(paramString);
    while (this.diagnosis.size() > 8)
      this.diagnosis.removeFirst();
  }

  public void asyncTrimToCount(int paramInt)
  {
    new Handler(Daemon.looper()).post(new Runnable(paramInt)
    {
      public void run()
      {
        MApiCacheService localMApiCacheService = DefaultMApiService.this.cache;
        if ((localMApiCacheService instanceof MApiCacheService))
        {
          int i = ((MApiCacheService)localMApiCacheService).trimToCount(this.val$expected);
          Log.i("mapi", "trim mapi cache, deleted=" + i);
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
        this.cache = new MApiCacheService(this.context, http(), this.monitor);
      MApiCacheService localMApiCacheService = this.cache;
      return localMApiCacheService;
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
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  @Deprecated
  public void detectDnsMapping(String paramString1, String paramString2)
  {
  }

  public String diagnosisInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("dpid=" + this.dpid);
    localStringBuilder.append("\nip-wifi=").append(this.wifiDns.getCurrentIp());
    localStringBuilder.append("\nip-mobile=").append(this.mobileDns.getCurrentIp());
    Iterator localIterator = this.diagnosis.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localStringBuilder.append("\n");
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
  }

  public void exec(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    if ((paramRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)paramRequestHandler).onRequestStart(paramMApiRequest);
    if ((paramMApiRequest.defaultCacheType() == CacheType.NORMAL) || (paramMApiRequest.defaultCacheType() == CacheType.HOURLY) || (paramMApiRequest.defaultCacheType() == CacheType.DAILY) || (paramMApiRequest.defaultCacheType() == CacheType.SERVICE))
    {
      paramRequestHandler = new Session(paramMApiRequest, paramRequestHandler);
      if ((Session)this.runningSession.putIfAbsent(paramMApiRequest, paramRequestHandler) == null)
      {
        paramRequestHandler.status = 1;
        cache().exec(paramMApiRequest, this.cacheHandler);
        return;
      }
      Log.e("mapi", "cannot exec duplicate request (same instance)");
      return;
    }
    paramRequestHandler = new Session(paramMApiRequest, paramRequestHandler);
    if ((Session)this.runningSession.putIfAbsent(paramMApiRequest, paramRequestHandler) == null)
    {
      paramRequestHandler.status = 2;
      http().exec(paramMApiRequest, this.httpHandler);
      return;
    }
    Log.e("mapi", "cannot exec duplicate request (same instance)");
  }

  public MApiResponse execSync(MApiRequest paramMApiRequest)
  {
    Log.w("mapi", "MApiService.execSync() is a temporary solution, use it as your own risk (TIP: do not try to abort sync request)");
    BlockingItem localBlockingItem = new BlockingItem();
    exec(paramMApiRequest, new RequestHandler(localBlockingItem)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        this.val$item.put(paramMApiResponse);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        this.val$item.put(paramMApiResponse);
      }
    });
    try
    {
      paramMApiRequest = (MApiResponse)localBlockingItem.take();
      return paramMApiRequest;
    }
    catch (Exception paramMApiRequest)
    {
    }
    return new BasicMApiResponse(0, null, null, Collections.emptyList(), paramMApiRequest);
  }

  public String getDpid()
  {
    if (this.dpid == null)
      startDpid(false);
    return this.dpid;
  }

  public void resetLocalDns()
  {
    long l = SystemClock.elapsedRealtime();
    if (l - this.lastDnsResetTime < 15000L)
      return;
    this.lastDnsResetTime = l;
    this.mobileDns.reset();
    this.wifiDns.reset();
  }

  public void resetTunnel()
  {
  }

  public void setDismissTokenListener(DismissTokenListener paramDismissTokenListener)
  {
    this.dismissTokenListener = paramDismissTokenListener;
  }

  public void setDpid(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    this.dpid = paramString;
    this.context.getSharedPreferences(this.context.getPackageName(), 0).edit().putString("dpid", paramString).commit();
  }

  public void startDpid(boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.dpid != null));
    do
      return;
    while (this.dpidRequest != null);
    this.dpidRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/getdpid.bin?imei=" + this.deviceId + "&uuid=" + this.uuid + "&androidid=" + this.androidId + "&serialnumber=" + this.deviceSerialId + "&wifimac=" + this.wifiMac, CacheType.DAILY);
    exec(this.dpidRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        DefaultMApiService.access$1302(DefaultMApiService.this, null);
        Log.i("mapi", "getdpid.bin fail!");
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        DefaultMApiService.access$1302(DefaultMApiService.this, null);
        paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("DPID");
        DefaultMApiService.this.setDpid(paramMApiRequest);
        Log.i("mapi", "getdpid.bin, DPID=" + paramMApiRequest);
      }
    });
  }

  protected com.dianping.dataservice.http.HttpRequest transferUriRequest(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
    throws Exception
  {
    return paramHttpRequest;
  }

  private class HttpHandler
    implements FullRequestHandle<com.dianping.dataservice.http.HttpRequest, HttpResponse>
  {
    private HttpHandler()
    {
    }

    private String getHostIp()
    {
      switch (DefaultMApiService.this.networkInfo.getNetworkType())
      {
      default:
        return DefaultMApiService.this.mobileDns.getCurrentIp();
      case 1:
        return DefaultMApiService.this.wifiDns.getCurrentIp();
      case 0:
      }
      return "0.0.0.0";
    }

    public void onRequestFailed(com.dianping.dataservice.http.HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultMApiService.Session localSession = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
      if ((localSession == null) || (localSession.status != 2));
      label637: 
      do
      {
        while (true)
        {
          return;
          localObject3 = new StringBuilder("http-fail ");
          if (DefaultMApiService.this.monitor != null)
            ((StringBuilder)localObject3).append(DefaultMApiService.this.monitor.getCommand(paramHttpRequest.url())).append(" ");
          ((StringBuilder)localObject3).append(paramHttpResponse.statusCode());
          if (localSession.time < 0L)
            localSession.time += SystemClock.elapsedRealtime();
          long l;
          Object localObject1;
          if ((DefaultMApiService.this.monitor != null) && (!localSession.request.disableStatistics()))
          {
            if (localSession.time < 0L)
            {
              l = localSession.time + SystemClock.elapsedRealtime();
              ((StringBuilder)localObject3).append(" ,").append(l).append("ms");
              int j = paramHttpResponse.statusCode();
              int i = j;
              if (j == 0)
                i = -100;
              j = 0;
              localObject1 = null;
              if ((paramHttpResponse instanceof InnerHttpResponse))
              {
                j = ((InnerHttpResponse)paramHttpResponse).tunnel;
                localObject1 = ((InnerHttpResponse)paramHttpResponse).ip;
              }
              int k = localSession.requestBytes;
              DefaultMApiService.this.monitor.pv3(0L, DefaultMApiService.this.monitor.getCommand(paramHttpRequest.url()), 0, j, i, k, 0, (int)l, (String)localObject1);
            }
          }
          else
          {
            DefaultMApiService.this.resetLocalDns();
            if ((paramHttpResponse.error() instanceof Exception))
              ((StringBuilder)localObject3).append("\n\t").append(paramHttpResponse.error());
            DefaultMApiService.this.addDiagnosis(((StringBuilder)localObject3).toString());
            localObject3 = (MApiRequest)paramHttpRequest;
            if (DefaultMApiService.ENABLE_LOG)
              LogTool.log(paramHttpRequest, paramHttpResponse, null);
            localSession.httpResponse = paramHttpResponse;
            if (((((MApiRequest)localObject3).defaultCacheType() != CacheType.NORMAL) && (((MApiRequest)localObject3).defaultCacheType() != CacheType.HOURLY) && (((MApiRequest)localObject3).defaultCacheType() != CacheType.DAILY)) || (localSession.cacheResponse == null))
              break label637;
          }
          try
          {
            localObject1 = (byte[])(byte[])localSession.cacheResponse.result();
            localObject1 = new BasicMApiResponse(0, localObject1, DefaultMApiService.this.getResult(localObject1), Collections.emptyList(), null);
            if (localObject1 != null)
            {
              DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
              localSession.handler.onRequestFinish((Request)localObject3, (Response)localObject1);
              if ((DefaultMApiService.this.statistics != null) && (!localSession.request.disableStatistics()))
              {
                paramHttpResponse = new ArrayList(4);
                paramHttpResponse.add(new BasicNameValuePair("network", "cache"));
                paramHttpResponse.add(new BasicNameValuePair("tag", "CACHE"));
                DefaultMApiService.this.statistics.pageView(localSession.request.url(), paramHttpResponse);
              }
              if (!Log.isLoggable(3))
                continue;
              Log.d("mapi", "finish (cache." + ((MApiRequest)localObject3).defaultCacheType() + ") " + paramHttpRequest.url());
              Log.d("mapi", "    expired cache is also accepted when http fail");
              return;
              l = localSession.time;
            }
          }
          catch (Exception localObject2)
          {
            while (true)
              localObject2 = null;
          }
        }
        if (((MApiRequest)localObject3).defaultCacheType() == CacheType.CRITICAL)
        {
          localSession.status = 3;
          DefaultMApiService.this.cache().exec(paramHttpRequest, DefaultMApiService.this.cacheHandler);
          return;
        }
        localObject2 = new BasicMApiResponse(paramHttpResponse.statusCode(), null, null, paramHttpResponse.headers(), paramHttpResponse.error());
        DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
        localSession.handler.onRequestFailed((Request)localObject3, (Response)localObject2);
        if ((DefaultMApiService.this.statistics == null) || (localSession.request.disableStatistics()))
          continue;
        localObject2 = new ArrayList(4);
        ((ArrayList)localObject2).add(new BasicNameValuePair("network", DefaultMApiService.this.networkInfo.getNetworkInfo()));
        ((ArrayList)localObject2).add(new BasicNameValuePair("elapse", "-1"));
        ((ArrayList)localObject2).add(new BasicNameValuePair("tag", "TIMEOUT"));
        DefaultMApiService.this.statistics.pageView(localSession.request.url(), (List)localObject2);
      }
      while (!Log.isLoggable(3));
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("fail (");
      ((StringBuilder)localObject3).append(paramHttpRequest.method()).append(',');
      ((StringBuilder)localObject3).append(paramHttpResponse.statusCode()).append(',');
      ((StringBuilder)localObject3).append(localSession.time).append("ms,");
      Object localObject2 = "http";
      if ((paramHttpResponse instanceof InnerHttpResponse))
        localObject2 = ((InnerHttpResponse)paramHttpResponse).from();
      ((StringBuilder)localObject3).append((String)localObject2);
      ((StringBuilder)localObject3).append(") ").append(paramHttpRequest.url());
      Log.d("mapi", ((StringBuilder)localObject3).toString());
      if ((paramHttpRequest.input() instanceof FormInputStream))
      {
        paramHttpRequest = (FormInputStream)paramHttpRequest.input();
        Log.d("mapi", "    " + paramHttpRequest.toString());
      }
      Log.d("mapi", "    " + paramHttpResponse.error());
    }

    public void onRequestFinish(com.dianping.dataservice.http.HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      DefaultMApiService.Session localSession = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
      if ((localSession == null) || (localSession.status != 2));
      label773: label912: label1428: label2582: 
      while (true)
      {
        return;
        Object localObject4 = null;
        int i = 0;
        int j = 0;
        StringBuilder localStringBuilder = new StringBuilder("http-code ");
        localStringBuilder.append(paramHttpResponse.statusCode()).append(" ");
        if (DefaultMApiService.this.monitor != null)
          localStringBuilder.append(DefaultMApiService.this.monitor.getCommand(paramHttpRequest.url()));
        localSession.httpResponse = paramHttpResponse;
        if (localSession.time < 0L)
          localSession.time += SystemClock.elapsedRealtime();
        MApiRequest localMApiRequest = (MApiRequest)paramHttpRequest;
        Object localObject5;
        Object localObject3;
        int k;
        Object localObject2;
        label638: int m;
        label759: long l;
        label825: label834: int i1;
        int n;
        String str;
        if ((paramHttpResponse.statusCode() / 100 == 2) || (paramHttpResponse.statusCode() / 100 == 4))
        {
          try
          {
            localObject5 = (byte[])(byte[])paramHttpResponse.result();
            Object localObject1 = null;
            localObject3 = localObject1;
            if (localObject5.length % 16 == 0)
            {
              localObject3 = localObject1;
              if (NativeHelper.A)
                localObject3 = NativeHelper.ndug(localObject5, DefaultMApiService.this.k, DefaultMApiService.this.v);
            }
            localObject1 = localObject3;
            if (localObject3 != null)
              break label1175;
            localObject1 = Cipher.getInstance(DefaultMApiService.this.c);
            ((Cipher)localObject1).init(2, new SecretKeySpec(DefaultMApiService.this.k, DefaultMApiService.this.c.substring(0, 3)), new IvParameterSpec(DefaultMApiService.this.v));
            localObject1 = new GZIPInputStream(new ByteArrayInputStream(((Cipher)localObject1).doFinal(localObject5)));
            localObject3 = new ByteArrayOutputStream(16384);
            localObject5 = new byte[4096];
            while (true)
            {
              k = ((GZIPInputStream)localObject1).read(localObject5);
              if (k <= 0)
                break;
              ((ByteArrayOutputStream)localObject3).write(localObject5, 0, k);
            }
          }
          catch (Exception localException)
          {
            localObject3 = new BasicMApiResponse(paramHttpResponse.statusCode(), null, null, paramHttpResponse.headers(), BasicMApiResponse.ERROR_MALFORMED);
            if (paramHttpResponse.statusCode() != 200)
              break label1295;
          }
          if ((paramHttpResponse.result() instanceof byte[]))
            if (HttpRequestInfoLogger.isText((byte[])(byte[])paramHttpResponse.result()))
            {
              i = -109;
              localStringBuilder.append(", malformed ").append(i);
              if (Log.isLoggable(6))
              {
                localObject5 = new StringBuilder();
                ((StringBuilder)localObject5).append("malformed (");
                ((StringBuilder)localObject5).append(paramHttpRequest.method()).append(',');
                ((StringBuilder)localObject5).append(((MApiResponse)localObject3).statusCode()).append(',');
                ((StringBuilder)localObject5).append(localSession.time).append("ms,");
                localObject2 = "http";
                if ((paramHttpResponse instanceof InnerHttpResponse))
                  localObject2 = ((InnerHttpResponse)paramHttpResponse).from();
                ((StringBuilder)localObject5).append((String)localObject2);
                ((StringBuilder)localObject5).append(") ").append(paramHttpRequest.url());
                Log.d("mapi", ((StringBuilder)localObject5).toString());
                if ((paramHttpRequest.input() instanceof FormInputStream))
                {
                  localObject2 = (FormInputStream)paramHttpRequest.input();
                  Log.d("mapi", "    " + ((FormInputStream)localObject2).toString());
                }
              }
              localObject5 = null;
              j = i;
              localObject2 = localObject3;
              localObject3 = localObject5;
              m = 0;
              i = m;
              if (paramHttpResponse.headers() != null)
              {
                k = 0;
                if ((paramHttpResponse instanceof InnerHttpResponse))
                  k = ((InnerHttpResponse)paramHttpResponse).source;
                i = m;
                if (k == 0)
                {
                  k = 1;
                  localObject5 = paramHttpResponse.headers().iterator();
                  while (true)
                  {
                    i = k;
                    if (!((Iterator)localObject5).hasNext())
                      break;
                    localObject6 = (NameValuePair)((Iterator)localObject5).next();
                    if (!"Server".equals(((NameValuePair)localObject6).getName()))
                      continue;
                    if ("dpweb".equalsIgnoreCase(((NameValuePair)localObject6).getValue()))
                      break label1394;
                    i = 1;
                  }
                }
              }
              if (DefaultMApiService.ENABLE_LOG)
              {
                if (localObject3 != null)
                  break label1400;
                localObject5 = localObject2;
                LogTool.log(localMApiRequest, paramHttpResponse, (MApiResponse)localObject5);
              }
              if ((DefaultMApiService.this.monitor != null) && (!localSession.request.disableStatistics()))
              {
                if (localSession.time >= 0L)
                  break label1407;
                l = localSession.time + SystemClock.elapsedRealtime();
                if (j == 0)
                  break label1417;
                k = j;
                m = k;
                if (k == 0)
                  m = -100;
                k = 0;
                localObject5 = null;
                if ((paramHttpResponse instanceof InnerHttpResponse))
                {
                  k = ((InnerHttpResponse)paramHttpResponse).tunnel;
                  localObject5 = ((InnerHttpResponse)paramHttpResponse).ip;
                }
                i1 = localSession.requestBytes;
                if (!(paramHttpResponse.result() instanceof byte[]))
                  break label1428;
                n = ((byte[])(byte[])paramHttpResponse.result()).length;
                str = DefaultMApiService.this.monitor.getCommand(paramHttpRequest.url());
                DefaultMApiService.this.monitor.pv3(0L, str, 0, k, m, i1, n, (int)l, (String)localObject5);
                if ((j != 0) || ((localObject3 == null) && (i != 0)))
                  if (HttpRequestInfoLogger.setAndStart(30000L))
                    if (i == 0)
                      break label1434;
              }
            }
        }
        label1175: label1434: for (Object localObject6 = "HIJACK"; ; localObject6 = "MALFORMED")
        {
          new Thread(new HttpRequestInfoLogger((String)localObject6, DefaultMApiService.this.dpid, str, paramHttpResponse, k, DefaultMApiService.this.networkInfo.getNetworkInfo())).start();
          DefaultMApiService.this.monitor.pv3(0L, "hijack", 0, k, m, i1, n, (int)l, (String)localObject5);
          if (((j == 0) && (i == 0)) || (paramHttpResponse.headers() == null))
            break label1442;
          localObject5 = paramHttpResponse.headers().iterator();
          while (((Iterator)localObject5).hasNext())
          {
            localObject6 = (NameValuePair)((Iterator)localObject5).next();
            localStringBuilder.append("\n\t").append(((NameValuePair)localObject6).getName());
            localStringBuilder.append(": ").append(((NameValuePair)localObject6).getValue());
          }
          ((GZIPInputStream)localObject2).close();
          ((ByteArrayOutputStream)localObject3).close();
          localObject2 = ((ByteArrayOutputStream)localObject3).toByteArray();
          if (paramHttpResponse.statusCode() / 100 == 2)
          {
            localObject3 = DefaultMApiService.this.getResult(localObject2);
            localObject3 = new BasicMApiResponse(paramHttpResponse.statusCode(), localObject2, localObject3, paramHttpResponse.headers(), null);
            localObject4 = localObject2;
            localObject2 = null;
            j = i;
            break label638;
          }
          localObject3 = (SimpleMsg)new Unarchiver(localObject2).readObject(SimpleMsg.DECODER);
          localObject2 = new BasicMApiResponse(paramHttpResponse.statusCode(), localObject2, null, paramHttpResponse.headers(), localObject3);
          localObject3 = null;
          j = i;
          break label638;
          i = -108;
          break;
          i = j;
          if (paramHttpResponse.statusCode() != 400)
            break;
          i = j;
          if (!(paramHttpResponse.result() instanceof byte[]))
            break;
          if (HttpRequestInfoLogger.isText((byte[])(byte[])paramHttpResponse.result()));
          for (i = -111; ; i = -110)
            break;
          localObject2 = new BasicMApiResponse(paramHttpResponse.statusCode(), null, null, paramHttpResponse.headers(), BasicMApiResponse.ERROR_STATUS);
          localObject3 = null;
          j = i;
          break label638;
          label1394: i = 0;
          break label759;
          label1400: localObject5 = localObject3;
          break label773;
          label1407: l = localSession.time;
          break label825;
          k = paramHttpResponse.statusCode();
          break label834;
          n = 0;
          break label912;
        }
        label1442: if (localObject3 == null)
          DefaultMApiService.this.addDiagnosis(localStringBuilder.toString());
        if (((DefaultMApiService.this.account instanceof BaseAccountService)) && (paramHttpResponse.headers() != null))
        {
          localObject5 = paramHttpResponse.headers().iterator();
          while (((Iterator)localObject5).hasNext())
          {
            localObject6 = (NameValuePair)((Iterator)localObject5).next();
            if ((!"pragma-newtoken".equals(((NameValuePair)localObject6).getName())) || (((NameValuePair)localObject6).getValue() == null) || (((NameValuePair)localObject6).getValue().length() <= 0))
              continue;
            ((BaseAccountService)DefaultMApiService.this.account).setNewToken(((NameValuePair)localObject6).getValue());
          }
        }
        if (localObject3 != null)
        {
          DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
          localSession.handler.onRequestFinish(localMApiRequest, (Response)localObject3);
          localSession.writeToCache = ((B)localObject4);
          DefaultMApiService.this.dhandler.sendMessage(DefaultMApiService.this.dhandler.obtainMessage(1, localSession));
          if (!Log.isLoggable(3))
            continue;
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("finish (");
          ((StringBuilder)localObject4).append(paramHttpRequest.method()).append(',');
          ((StringBuilder)localObject4).append(((MApiResponse)localObject3).statusCode()).append(',');
          ((StringBuilder)localObject4).append(localSession.time).append("ms,");
          localObject2 = "http";
          if ((paramHttpResponse instanceof InnerHttpResponse))
            localObject2 = ((InnerHttpResponse)paramHttpResponse).from();
          ((StringBuilder)localObject4).append((String)localObject2);
          ((StringBuilder)localObject4).append(") ").append(paramHttpRequest.url());
          Log.d("mapi", ((StringBuilder)localObject4).toString());
          if (!(paramHttpRequest.input() instanceof FormInputStream))
            continue;
          paramHttpRequest = (FormInputStream)paramHttpRequest.input();
          Log.d("mapi", "    " + paramHttpRequest.toString());
          return;
        }
        if (((MApiResponse)localObject2).statusCode() == 401)
        {
          paramHttpResponse = DefaultMApiService.this.account.token();
          if ((paramHttpResponse != null) && (!paramHttpResponse.equals(DefaultMApiService.this.dismissedToken)) && (DefaultMApiService.this.dismissTokenListener != null))
          {
            DefaultMApiService.this.dismissTokenListener.onDismissToken();
            DefaultMApiService.access$2202(DefaultMApiService.this, paramHttpResponse);
          }
        }
        if (((localMApiRequest.defaultCacheType() == CacheType.NORMAL) || (localMApiRequest.defaultCacheType() == CacheType.HOURLY) || (localMApiRequest.defaultCacheType() == CacheType.DAILY)) && (localSession.cacheResponse != null))
          try
          {
            paramHttpResponse = (byte[])(byte[])localSession.cacheResponse.result();
            paramHttpResponse = new BasicMApiResponse(0, paramHttpResponse, DefaultMApiService.this.getResult(paramHttpResponse), Collections.emptyList(), null);
            DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
            localSession.handler.onRequestFinish(localMApiRequest, paramHttpResponse);
            if ((DefaultMApiService.this.statistics != null) && (!localSession.request.disableStatistics()))
            {
              paramHttpResponse = new ArrayList(4);
              paramHttpResponse.add(new BasicNameValuePair("network", "cache"));
              paramHttpResponse.add(new BasicNameValuePair("tag", "CACHE"));
              DefaultMApiService.this.statistics.pageView(localSession.request.url(), paramHttpResponse);
            }
            if (!Log.isLoggable(3))
              continue;
            Log.d("mapi", "finish (cache." + localMApiRequest.defaultCacheType() + ") " + paramHttpRequest.url());
            Log.d("mapi", "    expired cache is also accepted when http fail");
            return;
          }
          catch (Exception paramHttpResponse)
          {
          }
        if (localMApiRequest.defaultCacheType() == CacheType.CRITICAL)
        {
          localSession.status = 3;
          DefaultMApiService.this.cache().exec(paramHttpRequest, DefaultMApiService.this.cacheHandler);
          return;
        }
        DefaultMApiService.this.runningSession.remove(paramHttpRequest, localSession);
        localSession.handler.onRequestFailed(localMApiRequest, (Response)localObject2);
        if ((DefaultMApiService.this.statistics != null) && (!localSession.request.disableStatistics()))
        {
          if (j == 0)
            break label2557;
          paramHttpResponse = new ArrayList(4);
          paramHttpResponse.add(new BasicNameValuePair("network", DefaultMApiService.this.networkInfo.getNetworkInfo()));
          paramHttpResponse.add(new BasicNameValuePair("elapse", "-1"));
          paramHttpResponse.add(new BasicNameValuePair("tag", "MALFORMED"));
          DefaultMApiService.this.statistics.pageView(localSession.request.url(), paramHttpResponse);
        }
        while (true)
        {
          if ((!Log.isLoggable(3)) || (j != 0))
            break label2582;
          paramHttpResponse = new StringBuilder();
          paramHttpResponse.append("fail (");
          paramHttpResponse.append(paramHttpRequest.method()).append(',');
          paramHttpResponse.append(((MApiResponse)localObject2).statusCode()).append(',');
          paramHttpResponse.append(localSession.time).append("ms,");
          paramHttpResponse.append("http");
          paramHttpResponse.append(") ").append(paramHttpRequest.url());
          Log.d("mapi", paramHttpResponse.toString());
          if ((paramHttpRequest.input() instanceof FormInputStream))
          {
            paramHttpRequest = (FormInputStream)paramHttpRequest.input();
            Log.d("mapi", "    " + paramHttpRequest.toString());
          }
          paramHttpRequest = ((MApiResponse)localObject2).error();
          if (!(paramHttpRequest instanceof SimpleMsg))
            break;
          Log.d("mapi", "    " + paramHttpRequest);
          return;
          label2557: DefaultMApiService.this.dhandler.sendMessage(DefaultMApiService.this.dhandler.obtainMessage(2, localSession));
        }
      }
    }

    public void onRequestProgress(com.dianping.dataservice.http.HttpRequest paramHttpRequest, int paramInt1, int paramInt2)
    {
      DefaultMApiService.Session localSession = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
      if ((localSession == null) || (localSession.status != 2));
      do
        return;
      while (!(localSession.handler instanceof FullRequestHandle));
      ((FullRequestHandle)localSession.handler).onRequestProgress((MApiRequest)paramHttpRequest, paramInt1, paramInt2);
    }

    public void onRequestStart(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
    {
      paramHttpRequest = (DefaultMApiService.Session)DefaultMApiService.this.runningSession.get(paramHttpRequest);
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
      catch (Exception paramHttpRequest)
      {
      }
    }
  }

  private class MApiFormWrapper extends WrapInputStream
  {
    private MApiFormInputStream wrappedInputStream;

    public MApiFormWrapper(MApiFormInputStream arg2)
    {
      Object localObject;
      this.wrappedInputStream = localObject;
    }

    protected InputStream wrappedInputStream()
      throws IOException
    {
      this.wrappedInputStream.reset();
      int j = this.wrappedInputStream.available();
      if (j % 16 == 0);
      byte[] arrayOfByte;
      for (int i = j; ; i = 16 - j % 16 + j)
      {
        arrayOfByte = new byte[i];
        i = 0;
        while (true)
        {
          int k = this.wrappedInputStream.read(arrayOfByte, i, j - i);
          if (k <= 0)
            break;
          i += k;
        }
      }
      Object localObject1 = null;
      if (NativeHelper.A)
      {
        localObject2 = new byte[arrayOfByte.length];
        localObject1 = localObject2;
        if (!NativeHelper.ne(arrayOfByte, localObject2, DefaultMApiService.this.k, DefaultMApiService.this.v))
          localObject1 = null;
      }
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject1 = new SecretKeySpec(DefaultMApiService.this.k, DefaultMApiService.this.c.substring(0, 3));
        localObject2 = new IvParameterSpec(DefaultMApiService.this.v);
      }
      try
      {
        Cipher localCipher = Cipher.getInstance(DefaultMApiService.this.c);
        localCipher.init(1, (Key)localObject1, (AlgorithmParameterSpec)localObject2);
        localObject2 = localCipher.doFinal(arrayOfByte);
        return new ByteArrayInputStream(localObject2);
      }
      catch (Exception localException)
      {
      }
      throw new IOException();
    }
  }

  private class MApiHttpService extends DefaultHttpService
  {
    public MApiHttpService(Context arg2)
    {
      super(new ThreadPoolExecutor(6, 8, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue()));
    }

    protected DefaultHttpService.Task createTask(com.dianping.dataservice.http.HttpRequest paramHttpRequest, RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> paramRequestHandler)
    {
      return new MApiTask(paramHttpRequest, paramRequestHandler);
    }

    protected boolean isLoggable()
    {
      return false;
    }

    protected void log(String paramString)
    {
    }

    private class MApiTask extends DefaultHttpService.Task
    {
      public MApiTask(RequestHandler<com.dianping.dataservice.http.HttpRequest, HttpResponse> arg2)
      {
        super(localHttpRequest, localRequestHandler);
      }

      private void addHeader(List<NameValuePair> paramList, String paramString1, String paramString2)
      {
        if (!headersContainsKey(paramList, paramString1))
          paramList.add(new BasicNameValuePair(paramString1, paramString2));
      }

      private boolean headersContainsKey(List<NameValuePair> paramList, String paramString)
      {
        if ((paramList == null) || (paramList.size() == 0));
        do
          while (!paramList.hasNext())
          {
            return false;
            paramList = paramList.iterator();
          }
        while (!((NameValuePair)paramList.next()).getName().equals(paramString));
        return true;
      }

      protected BasicHttpRequest getHttpRequest()
        throws Exception
      {
        Object localObject1 = DefaultMApiService.this.transferUriRequest(this.req);
        com.dianping.dataservice.http.HttpRequest localHttpRequest = DefaultMApiService.this.transferHttpsUriRequest(this.req, (com.dianping.dataservice.http.HttpRequest)localObject1);
        String str2 = DefaultMApiService.this.mergeDomain(localHttpRequest.url());
        localObject1 = null;
        if ((localHttpRequest instanceof BasicHttpRequest))
        {
          localObject2 = ((BasicHttpRequest)localHttpRequest).proxy();
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            localObject1 = localObject2;
            if (DefaultMApiService.this.networkInfo.getProxy() != null)
              localObject1 = new Proxy(Proxy.Type.HTTP, DefaultMApiService.this.networkInfo.getProxy());
          }
        }
        Object localObject3 = localHttpRequest.headers();
        Object localObject2 = localObject3;
        if (localObject3 == null)
          localObject2 = new ArrayList();
        addHeader((List)localObject2, "User-Agent", DefaultMApiService.this.userAgent);
        addHeader((List)localObject2, "pragma-os", DefaultMApiService.this.userAgent);
        addHeader((List)localObject2, "pragma-device", DefaultMApiService.this.deviceId);
        if (DefaultMApiService.this.uuid != null)
          addHeader((List)localObject2, "pragma-uuid", DefaultMApiService.this.uuid);
        if (DefaultMApiService.this.dpid != null)
          addHeader((List)localObject2, "pragma-dpid", DefaultMApiService.this.dpid);
        if (DefaultMApiService.this.networkInfo != null)
          addHeader((List)localObject2, "network-type", DefaultMApiService.this.networkInfo.getDetailNetworkType());
        long l2;
        long l1;
        if (DefaultMApiService.this.account == null)
        {
          localObject3 = null;
          if (localObject3 != null)
          {
            addHeader((List)localObject2, "pragma-token", (String)localObject3);
            if ((DefaultMApiService.this.account instanceof BaseAccountService))
            {
              String str1 = ((BaseAccountService)DefaultMApiService.this.account).newToken();
              localObject3 = str1;
              if (str1 == null)
                localObject3 = "";
              addHeader((List)localObject2, "pragma-newtoken", (String)localObject3);
            }
          }
          l2 = localHttpRequest.timeout();
          l1 = l2;
          if (l2 <= 0L)
          {
            if (!str2.startsWith("https"))
              break label505;
            l2 = DefaultMApiService.this.timeoutConfigHelper.getHttpsTimeout();
          }
        }
        while (true)
        {
          l1 = l2;
          if (l2 == 0L)
            l1 = DefaultMApiService.this.getFlexibleTimeout();
          localObject3 = localHttpRequest.method();
          if ((((String)localObject3).equals("GET")) || (((String)localObject3).equals("POST")))
            break label524;
          throw new IllegalArgumentException("unsupported http method " + (String)localObject3);
          localObject3 = DefaultMApiService.this.account.token();
          break;
          label505: l2 = DefaultMApiService.this.timeoutConfigHelper.getHttpTimeout();
        }
        label524: return (BasicHttpRequest)(BasicHttpRequest)(BasicHttpRequest)new BasicHttpRequest(str2, (String)localObject3, DefaultMApiService.this.dins(localHttpRequest.input()), (List)localObject2, l1, (Proxy)localObject1);
      }
    }
  }

  private class MyTunnel extends AndroidTunnel
  {
    MyTunnel(Context arg2)
    {
      super();
    }

    protected TunnelRequest createRequest(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
    {
      TunnelRequest localTunnelRequest = super.createRequest(paramHttpRequest);
      if (localTunnelRequest.headers == null)
        localTunnelRequest.headers = new JSONObject();
      try
      {
        localTunnelRequest.headers.put("User-Agent", DefaultMApiService.this.userAgent);
        localTunnelRequest.headers.put("pragma-os", DefaultMApiService.this.userAgent);
        if (DefaultMApiService.this.deviceId != null)
          localTunnelRequest.headers.put("pragma-device", DefaultMApiService.this.deviceId);
        if (DefaultMApiService.this.uuid != null)
          localTunnelRequest.headers.put("pragma-uuid", DefaultMApiService.this.uuid);
        if (getDpid() != null)
          localTunnelRequest.headers.put("pragma-dpid", DefaultMApiService.this.dpid);
        if (DefaultMApiService.this.networkInfo != null)
          localTunnelRequest.headers.put("network-type", DefaultMApiService.this.networkInfo.getDetailNetworkType());
        if (DefaultMApiService.this.account == null);
        for (paramHttpRequest = null; paramHttpRequest != null; paramHttpRequest = DefaultMApiService.this.account.token())
        {
          localTunnelRequest.headers.put("pragma-token", paramHttpRequest);
          if (!(DefaultMApiService.this.account instanceof BaseAccountService))
            break;
          String str = ((BaseAccountService)DefaultMApiService.this.account).newToken();
          JSONObject localJSONObject = localTunnelRequest.headers;
          paramHttpRequest = str;
          if (str == null)
            paramHttpRequest = "";
          localJSONObject.put("pragma-newtoken", paramHttpRequest);
          return localTunnelRequest;
        }
      }
      catch (Exception paramHttpRequest)
      {
      }
      return localTunnelRequest;
    }

    protected List<SocketAddress> getServers()
    {
      List localList2 = DefaultMApiService.parseAddressList(DefaultMApiService.this.config.dump().optString("tunnelServers"), 80);
      List localList1;
      if (localList2 != null)
      {
        localList1 = localList2;
        if (!localList2.isEmpty());
      }
      else
      {
        localList1 = super.getServers();
      }
      return localList1;
    }

    protected void onConnectResult(SocketAddress paramSocketAddress, long paramLong)
    {
      super.onConnectResult(paramSocketAddress, paramLong);
      int i;
      MonitorService localMonitorService;
      if (((paramSocketAddress instanceof InetSocketAddress)) && (DefaultMApiService.this.monitor != null))
      {
        paramSocketAddress = (InetSocketAddress)paramSocketAddress;
        switch (paramSocketAddress.getPort())
        {
        default:
          i = 0;
          localMonitorService = DefaultMApiService.this.monitor;
          if (paramLong <= 0L)
            break;
          i += 200;
        case 80:
        case 8080:
        case 443:
        case 14000:
        }
      }
      while (true)
      {
        localMonitorService.pv3(0L, "tunnel_connect", 0, 1, i, 0, 0, (int)paramLong, paramSocketAddress.getAddress().getHostAddress());
        return;
        i = 1;
        break;
        i = 2;
        break;
        i = 3;
        break;
        i = 4;
        break;
        i = -100 - i;
      }
    }

    protected byte[] transferBody(InputStream paramInputStream)
    {
      return super.transferBody(DefaultMApiService.this.dins(paramInputStream));
    }
  }

  private class MyUtnConnection extends AndroidUtnConnection
  {
    long lastPingTime;

    public MyUtnConnection(Context arg2)
    {
      super();
    }

    protected UtnConnection.Session createSession(com.dianping.utn.HttpRequest paramHttpRequest, Object paramObject)
    {
      if (paramHttpRequest.headers == null)
        paramHttpRequest.headers = new HashMap(6);
      paramHttpRequest.headers.put("User-Agent", DefaultMApiService.this.userAgent);
      if (!paramHttpRequest.headers.containsKey("pragma-os"))
        paramHttpRequest.headers.put("pragma-os", DefaultMApiService.this.userAgent);
      if ((!paramHttpRequest.headers.containsKey("pragma-device")) && (DefaultMApiService.this.deviceId != null))
        paramHttpRequest.headers.put("pragma-device", DefaultMApiService.this.deviceId);
      if ((DefaultMApiService.this.uuid != null) && (!paramHttpRequest.headers.containsKey("pragma-uuid")))
        paramHttpRequest.headers.put("pragma-uuid", DefaultMApiService.this.uuid);
      if ((DefaultMApiService.this.dpid != null) && (!paramHttpRequest.headers.containsKey("pragma-dpid")))
        paramHttpRequest.headers.put("pragma-dpid", DefaultMApiService.this.dpid);
      if (DefaultMApiService.this.account == null);
      for (Object localObject = null; ; localObject = DefaultMApiService.this.account.token())
      {
        if (localObject != null)
        {
          paramHttpRequest.headers.put("pragma-token", localObject);
          if ((DefaultMApiService.this.account instanceof BaseAccountService))
          {
            String str = ((BaseAccountService)DefaultMApiService.this.account).newToken();
            Map localMap = paramHttpRequest.headers;
            localObject = str;
            if (str == null)
              localObject = "";
            localMap.put("pragma-newtoken", localObject);
          }
        }
        return super.createSession(paramHttpRequest, paramObject);
      }
    }

    protected List<SocketAddress> getServers()
    {
      List localList2 = DefaultMApiService.parseAddressList(DefaultMApiService.this.config.dump().optString("utnServer"), 8080);
      List localList1;
      if (localList2 != null)
      {
        localList1 = localList2;
        if (!localList2.isEmpty());
      }
      else
      {
        localList1 = super.getServers();
      }
      return localList1;
    }

    protected void onPingResult(SocketAddress paramSocketAddress, long paramLong)
    {
      super.onPingResult(paramSocketAddress, paramLong);
      long l = SystemClock.uptimeMillis();
      int i;
      String str;
      if (((paramSocketAddress == null) || ((paramSocketAddress instanceof InetSocketAddress))) && (l > this.lastPingTime + 60000L) && (DefaultMApiService.this.monitor != null))
      {
        this.lastPingTime = l;
        i = -100;
        str = null;
        if (paramSocketAddress != null)
        {
          paramSocketAddress = (InetSocketAddress)paramSocketAddress;
          str = paramSocketAddress.getAddress().getHostAddress();
          switch (paramSocketAddress.getPort())
          {
          default:
            i = 200;
          case 80:
          case 8080:
          case 53:
          case 14000:
          case 123:
          }
        }
      }
      while (true)
      {
        DefaultMApiService.this.monitor.pv3(0L, "ping_utn", 0, 2, i, 0, 0, (int)paramLong, str);
        return;
        i = 201;
        continue;
        i = 202;
        continue;
        i = 203;
        continue;
        i = 204;
        continue;
        i = 205;
      }
    }

    protected byte[] transferBody(InputStream paramInputStream)
    {
      return super.transferBody(DefaultMApiService.this.dins(paramInputStream));
    }
  }

  private class MyWnsService extends WnsHttpService
  {
    private MyWnsService()
    {
    }

    private void addHeader(List<NameValuePair> paramList, String paramString1, String paramString2)
    {
      if (!headersContainsKey(paramList, paramString1))
        paramList.add(new BasicNameValuePair(paramString1, paramString2));
    }

    private boolean headersContainsKey(List<NameValuePair> paramList, String paramString)
    {
      if ((paramList == null) || (paramList.size() == 0));
      do
        while (!paramList.hasNext())
        {
          return false;
          paramList = paramList.iterator();
        }
      while (!((NameValuePair)paramList.next()).getName().equals(paramString));
      return true;
    }

    protected byte[] transferBody(InputStream paramInputStream)
    {
      return super.transferBody(DefaultMApiService.this.dins(paramInputStream));
    }

    protected com.dianping.dataservice.http.HttpRequest transferRequest(com.dianping.dataservice.http.HttpRequest paramHttpRequest)
    {
      String str2 = DefaultMApiService.this.mergeDomain(paramHttpRequest.url(), "mapi.dianping.com");
      ArrayList localArrayList = new ArrayList();
      addHeader(localArrayList, "User-Agent", DefaultMApiService.this.userAgent);
      addHeader(localArrayList, "pragma-os", DefaultMApiService.this.userAgent);
      addHeader(localArrayList, "pragma-device", DefaultMApiService.this.deviceId);
      if (DefaultMApiService.this.uuid != null)
        addHeader(localArrayList, "pragma-uuid", DefaultMApiService.this.uuid);
      if (DefaultMApiService.this.getDpid() != null)
        addHeader(localArrayList, "pragma-dpid", DefaultMApiService.this.dpid);
      if (DefaultMApiService.this.networkInfo != null)
        addHeader(localArrayList, "network-type", DefaultMApiService.this.networkInfo.getDetailNetworkType());
      if (DefaultMApiService.this.account == null);
      long l1;
      for (Object localObject = null; ; localObject = DefaultMApiService.this.account.token())
      {
        if (localObject != null)
        {
          localArrayList.add(new BasicNameValuePair("pragma-token", (String)localObject));
          if ((DefaultMApiService.this.account instanceof BaseAccountService))
          {
            String str1 = ((BaseAccountService)DefaultMApiService.this.account).newToken();
            localObject = str1;
            if (str1 == null)
              localObject = "";
            localArrayList.add(new BasicNameValuePair("pragma-newtoken", (String)localObject));
          }
        }
        paramHttpRequest.addHeaders(localArrayList);
        long l2 = paramHttpRequest.timeout();
        l1 = l2;
        if (l2 <= 0L)
          l1 = DefaultMApiService.this.timeoutConfigHelper.getWnsTimeout();
        if ((!paramHttpRequest.url().equals(str2)) || (l1 != paramHttpRequest.timeout()))
          break;
        return paramHttpRequest;
      }
      return (com.dianping.dataservice.http.HttpRequest)new BasicHttpRequest(str2, paramHttpRequest.method(), paramHttpRequest.input(), paramHttpRequest.headers(), l1);
    }
  }

  private static class Session
  {
    public HttpResponse cacheResponse;
    public RequestHandler<MApiRequest, MApiResponse> handler;
    public HttpResponse httpResponse;
    public MApiRequest request;
    public int requestBytes;
    public int status;
    public long time;
    public byte[] writeToCache;

    public Session(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
    {
      this.request = paramMApiRequest;
      this.handler = paramRequestHandler;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.DefaultMApiService
 * JD-Core Version:    0.6.0
 */