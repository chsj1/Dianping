package com.dianping.app;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.impl.DefaultAccountService;
import com.dianping.configservice.ConfigChangeListener;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.MyConfigService;
import com.dianping.configservice.impl.TunnelConfigService;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.impl.DefaultHttpService;
import com.dianping.dataservice.image.impl.DefaultImageService;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.locationservice.proxy.LocationServiceProxy;
import com.dianping.monitor.MonitorService;
import com.dianping.monitor.impl.DefaultMonitorService;
import com.dianping.statistics.StatisticsService;
import com.dianping.statistics.impl.MyStatisticsService;
import com.dianping.statistics.impl.NewStatisticsService;
import com.dianping.statistics.impl.PVProcessStatisticsService;
import com.dianping.statistics.utils.StatisticsInitializer;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

class ServiceManager
{
  private static final String[] sDomains = { "http://m.api.dianping.com/", "http://mapi.dianping.com/", "http://rs.api.dianping.com/", "http://app.t.dianping.com/", "http://mc.api.dianping.com/", "http://l.api.dianping.com/", "http://cf.api.dianping.com/", "http://api.p.dianping.com/", "http://waimai.api.dianping.com/", "http://hui.api.dianping.com/", "http://beauty.api.dianping.com/", "http://app.movie.dianping.com/" };
  private DefaultAccountService account;
  private MyConfigService config;
  private final Context context;
  private DefaultHttpService http;
  private DefaultImageService image;
  private LocationServiceProxy location;
  private MApiDebug mapi_debug;
  private DefaultMApiService mapi_orig;
  private DefaultMonitorService monitor;
  private NewStatisticsService newStatisticsService;
  private MyStatisticsService pushStatistics;
  private PVProcessStatisticsService pvProcess;
  private MyStatisticsService statistics;
  private TunnelConfigService tunnelConfig;
  private ConfigProxy tunnelConfigProxy = new ConfigProxy(null);

  public ServiceManager(Context paramContext)
  {
    this.context = paramContext;
  }

  public Object getService(String paramString)
  {
    monitorenter;
    try
    {
      if ("http".equals(paramString))
      {
        if (this.http == null)
        {
          paramString = new ThreadPoolExecutor(2, 6, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
          this.http = new DefaultHttpService(this.context, paramString);
        }
        paramString = this.http;
      }
      while (true)
      {
        return paramString;
        if ("image".equals(paramString))
        {
          if (this.image == null)
          {
            paramString = (MonitorService)getService("monitor");
            this.image = new DefaultImageService(this.context, 2, paramString);
          }
          paramString = this.image;
          continue;
        }
        if ("image_cahce".equals(paramString))
        {
          if (this.image == null)
            getService("image");
          paramString = this.image.cache();
          continue;
        }
        if (("mapi".equals(paramString)) || ("mapi_original".equals(paramString)))
        {
          if (this.mapi_orig == null)
          {
            if (this.account == null)
              getService("account");
            paramString = (StatisticsService)getService("pvprocess");
            MonitorService localMonitorService = (MonitorService)getService("monitor");
            this.mapi_orig = new DefaultMApiService(this.context, Environment.mapiUserAgent(), Environment.imei(), Environment.uuid(), this.tunnelConfigProxy, this.account, paramString, localMonitorService, Environment.getWifiMac(), Environment.getDeviceSerialNum(), Environment.getAndroidId(this.context))
            {
              Random rnd = new Random(System.currentTimeMillis());

              private String switchDomain(String paramString1, String paramString2, String paramString3)
              {
                if (paramString1.startsWith("http://"));
                for (String str = ""; ; str = "http://")
                {
                  str = str + paramString1;
                  paramString1 = str;
                  if (!str.endsWith("/"))
                    paramString1 = str + "/";
                  paramString1 = paramString1 + paramString3.substring(paramString2.length());
                  Log.i("mapi", "mapi_debug url:" + paramString1);
                  return paramString1;
                }
              }

              protected HttpRequest transferUriRequest(HttpRequest paramHttpRequest)
                throws Exception
              {
                if (ServiceManager.this.mapi_debug != null)
                {
                  if (ServiceManager.this.mapi_debug.nextFail > 0)
                  {
                    paramHttpRequest = ServiceManager.this.mapi_debug;
                    paramHttpRequest.nextFail -= 1;
                    throw new Exception("这是一次模拟的网络错误");
                  }
                  if ((ServiceManager.this.mapi_debug.failHalf) && (this.rnd.nextBoolean()))
                    throw new Exception("这是一次模拟的网络错误");
                  String str1 = paramHttpRequest.url();
                  Object localObject2 = str1;
                  Object localObject1 = localObject2;
                  if (ServiceManager.this.mapi_debug.switchDomain != null)
                  {
                    localObject1 = localObject2;
                    if (ServiceManager.this.mapi_debug.switchDomain.length() > 0)
                    {
                      localObject1 = localObject2;
                      if (str1.startsWith(ServiceManager.sDomains[0]))
                        localObject1 = switchDomain(ServiceManager.this.mapi_debug.switchDomain, ServiceManager.sDomains[0], str1);
                    }
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.mapiDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.mapiDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[1]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.mapiDomain, ServiceManager.sDomains[1], str1);
                    }
                  }
                  localObject1 = localObject2;
                  if (ServiceManager.this.mapi_debug.bookingDebugDomain != null)
                  {
                    localObject1 = localObject2;
                    if (ServiceManager.this.mapi_debug.bookingDebugDomain.length() > 0)
                    {
                      localObject1 = localObject2;
                      if (str1.startsWith(ServiceManager.sDomains[2]))
                        localObject1 = switchDomain(ServiceManager.this.mapi_debug.bookingDebugDomain, ServiceManager.sDomains[2], str1);
                    }
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.tDebugDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.tDebugDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[3]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.tDebugDomain, ServiceManager.sDomains[3], str1);
                    }
                  }
                  localObject1 = localObject2;
                  if (ServiceManager.this.mapi_debug.payDebugDomain != null)
                  {
                    localObject1 = localObject2;
                    if (ServiceManager.this.mapi_debug.payDebugDomain.length() > 0)
                    {
                      localObject1 = localObject2;
                      if (str1.startsWith(ServiceManager.sDomains[7]))
                        localObject1 = switchDomain(ServiceManager.this.mapi_debug.payDebugDomain, ServiceManager.sDomains[7], str1);
                    }
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.membercardDebugDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.membercardDebugDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[4]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.membercardDebugDomain, ServiceManager.sDomains[4], str1);
                    }
                  }
                  localObject1 = localObject2;
                  if (ServiceManager.this.mapi_debug.takeawayDebugDomain != null)
                  {
                    localObject1 = localObject2;
                    if (ServiceManager.this.mapi_debug.takeawayDebugDomain.length() > 0)
                    {
                      localObject1 = localObject2;
                      if (str1.startsWith(ServiceManager.sDomains[8]))
                        localObject1 = switchDomain(ServiceManager.this.mapi_debug.takeawayDebugDomain, ServiceManager.sDomains[8], str1);
                    }
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.huihuiDebugDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.huihuiDebugDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[9]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.huihuiDebugDomain, ServiceManager.sDomains[9], str1);
                    }
                  }
                  localObject1 = localObject2;
                  if (!TextUtils.isEmpty(ServiceManager.this.mapi_debug.beautyDebugDomain))
                  {
                    localObject1 = localObject2;
                    if (str1.startsWith(ServiceManager.sDomains[10]))
                      localObject1 = switchDomain(ServiceManager.this.mapi_debug.beautyDebugDomain, ServiceManager.sDomains[10], str1);
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.locateDebugDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.locateDebugDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[5]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.locateDebugDomain, ServiceManager.sDomains[5], str1);
                    }
                  }
                  localObject1 = localObject2;
                  if (ServiceManager.this.mapi_debug.configDebugDomain != null)
                  {
                    localObject1 = localObject2;
                    if (ServiceManager.this.mapi_debug.configDebugDomain.length() > 0)
                    {
                      localObject1 = localObject2;
                      if (str1.startsWith(ServiceManager.sDomains[6]))
                        localObject1 = switchDomain(ServiceManager.this.mapi_debug.configDebugDomain, ServiceManager.sDomains[6], str1);
                    }
                  }
                  localObject2 = localObject1;
                  if (ServiceManager.this.mapi_debug.movieDebugDomain != null)
                  {
                    localObject2 = localObject1;
                    if (ServiceManager.this.mapi_debug.movieDebugDomain.length() > 0)
                    {
                      localObject2 = localObject1;
                      if (str1.startsWith(ServiceManager.sDomains[11]))
                        localObject2 = switchDomain(ServiceManager.this.mapi_debug.movieDebugDomain, ServiceManager.sDomains[11], str1);
                    }
                  }
                  if ((ServiceManager.this.mapi_debug.delay > 0L) && (Looper.myLooper() != Looper.getMainLooper()))
                    Thread.sleep(ServiceManager.this.mapi_debug.delay);
                  String str2 = ServiceManager.this.mapi_debug.proxy;
                  int i = ServiceManager.this.mapi_debug.proxyPort;
                  str1 = null;
                  localObject1 = str1;
                  if (!TextUtils.isEmpty(str2))
                  {
                    localObject1 = str1;
                    if (i > 0)
                    {
                      localObject1 = new InetSocketAddress(str2, i);
                      localObject1 = new Proxy(Proxy.Type.HTTP, (SocketAddress)localObject1);
                    }
                  }
                  return new BasicHttpRequest((String)localObject2, paramHttpRequest.method(), paramHttpRequest.input(), paramHttpRequest.headers(), paramHttpRequest.timeout(), (Proxy)localObject1);
                }
                return (HttpRequest)(HttpRequest)super.transferUriRequest(paramHttpRequest);
              }
            };
          }
          paramString = this.mapi_orig;
          continue;
        }
        if ("mapi_cache".equals(paramString))
        {
          if (this.mapi_orig == null)
            getService("mapi");
          paramString = this.mapi_orig.cache();
          continue;
        }
        if ("mapi_debug".equals(paramString))
        {
          if (this.mapi_debug == null)
            this.mapi_debug = new MApiDebug(null);
          paramString = this.mapi_debug;
          continue;
        }
        if ("config".equals(paramString))
        {
          if (this.config == null)
          {
            getService("mapi");
            this.config = new MyConfigService(this.context, this.mapi_orig);
          }
          paramString = this.config;
          continue;
        }
        if ("tunnel_config".equals(paramString))
        {
          if (this.tunnelConfig == null)
          {
            getService("mapi");
            this.tunnelConfig = new TunnelConfigService(this.context, this.mapi_orig);
          }
          paramString = this.tunnelConfig;
          continue;
        }
        if ("account".equals(paramString))
        {
          if (this.account == null)
            this.account = new DefaultAccountService(this.context);
          paramString = this.account;
          continue;
        }
        if ("location".equals(paramString))
        {
          if (this.location == null)
            this.location = new LocationServiceProxy(this.context);
          paramString = this.location;
          continue;
        }
        if ("statistics".equals(paramString))
        {
          if (this.statistics == null)
            this.statistics = new MyStatisticsService(this.context, "http://stat.api.dianping.com/utm.js?v=androidv1");
          paramString = this.statistics;
          continue;
        }
        if ("statistics_new".equals(paramString))
        {
          if (this.newStatisticsService == null)
          {
            paramString = new StatisticsInitializer();
            paramString.uploadString = "http://m.api.dianping.com/applog/applog.api";
            StatisticsInitializer.app_version = Environment.versionName();
            StatisticsInitializer.app_market = Environment.source();
            StatisticsInitializer.mac = DeviceUtils.mac();
            StatisticsInitializer.imei = Environment.imei();
            StatisticsInitializer.uuid = Environment.uuid();
            paramString.userAgent = Environment.mapiUserAgent();
            paramString.uploadCount = 30;
            paramString.maxCount = 300;
            paramString.maxUploadCount = 50;
            paramString.uploadInterval = 15000;
            paramString.pDeleteCount = 100;
            paramString.disableDengTa = DPApplication.instance().tunnelConfigService().dump().optBoolean("disableDengTa", true);
            paramString.mapiService = ((MApiService)getService("mapi"));
            paramString.httpService = ((HttpService)getService("http"));
            paramString.monitorService = ((MonitorService)getService("monitor"));
            this.newStatisticsService = new NewStatisticsService(DPApplication.instance(), paramString);
          }
          paramString = this.newStatisticsService;
          continue;
        }
        if ("monitor".equals(paramString))
        {
          if (this.monitor == null)
            this.monitor = new DefaultMonitorService(this.context, "http://114.80.165.63/broker-service/api/batch?");
          paramString = this.monitor;
          continue;
        }
        if ("pvprocess".equals(paramString))
        {
          if (this.pvProcess == null)
            this.pvProcess = new PVProcessStatisticsService((StatisticsService)getService("statistics"));
          paramString = this.pvProcess;
          continue;
        }
        if ("push_statistics".equals(paramString))
        {
          if (this.pushStatistics == null)
            this.pushStatistics = new MyStatisticsService(this.context, "push_statistics", "http://stat.api.dianping.com/utm.js?v=androidpush");
          paramString = this.pushStatistics;
          continue;
        }
        Log.e("unknown service \"" + paramString + "\"");
        paramString = null;
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void stop()
  {
    monitorenter;
    try
    {
      if (this.image != null)
      {
        this.image.asyncTrimToCount(1, 250);
        this.image.asyncTrimToCount(2, 40);
      }
      if (this.mapi_orig != null)
        this.mapi_orig.asyncTrimToCount(160);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private class ConfigProxy
    implements ConfigService
  {
    private ConfigProxy()
    {
    }

    public void addListener(String paramString, ConfigChangeListener paramConfigChangeListener)
    {
    }

    public JSONObject dump()
    {
      if (ServiceManager.this.tunnelConfig == null)
        return new JSONObject();
      return ServiceManager.this.tunnelConfig.dump();
    }

    public void refresh()
    {
    }

    public void removeListener(String paramString, ConfigChangeListener paramConfigChangeListener)
    {
    }
  }

  private static class MApiDebug
    implements MApiDebugAgent
  {
    public String beautyDebugDomain;
    public String bookingDebugDomain;
    public String configDebugDomain;
    public long delay;
    public boolean failHalf;
    public String huihuiDebugDomain;
    public String locateDebugDomain;
    public String mapiDomain;
    public String membercardDebugDomain;
    public String movieDebugDomain;
    public String newGADebugDomain;
    public int nextFail;
    public String payDebugDomain;
    public String proxy;
    public int proxyPort;
    public String pushDebugDomain;
    public String switchDomain;
    public String tDebugDomain;
    public String takeawayDebugDomain;

    public void addNextFail(int paramInt)
    {
      this.nextFail += paramInt;
    }

    public String beautyDebugDomain()
    {
      return this.beautyDebugDomain;
    }

    public String bookingDebugDomain()
    {
      return this.bookingDebugDomain;
    }

    public String configDebugDomain()
    {
      return this.configDebugDomain;
    }

    public long delay()
    {
      return this.delay;
    }

    public boolean failHalf()
    {
      return this.failHalf;
    }

    public String huihuiDebugDomain()
    {
      return this.huihuiDebugDomain;
    }

    public String locateDebugDomain()
    {
      return this.locateDebugDomain;
    }

    public String mapiDomain()
    {
      return this.mapiDomain;
    }

    public String membercardDebugDomain()
    {
      return this.membercardDebugDomain;
    }

    public String movieDebugDomain()
    {
      return this.movieDebugDomain;
    }

    public String newGADebugDomain()
    {
      return this.newGADebugDomain;
    }

    public String payDebugDomain()
    {
      return this.payDebugDomain;
    }

    public String proxy()
    {
      return this.proxy;
    }

    public int proxyPort()
    {
      return this.proxyPort;
    }

    public String pushDebugDomain()
    {
      return this.pushDebugDomain;
    }

    public void setBeautyDebugDomain(String paramString)
    {
      this.beautyDebugDomain = paramString;
    }

    public void setBookingDebugDomain(String paramString)
    {
      this.bookingDebugDomain = paramString;
    }

    public void setConfigDebugDomain(String paramString)
    {
      this.configDebugDomain = paramString;
    }

    public void setDelay(long paramLong)
    {
      this.delay = paramLong;
    }

    public void setFailHalf(boolean paramBoolean)
    {
      this.failHalf = paramBoolean;
    }

    public void setHuihuiDebugDomain(String paramString)
    {
      this.huihuiDebugDomain = paramString;
    }

    public void setLocateDebugDomain(String paramString)
    {
      this.locateDebugDomain = paramString;
    }

    public void setMapiDomain(String paramString)
    {
      this.mapiDomain = paramString;
    }

    public void setMembercardDebugDomain(String paramString)
    {
      this.membercardDebugDomain = paramString;
    }

    public void setMovieDebugDomain(String paramString)
    {
      this.movieDebugDomain = paramString;
    }

    public void setNewGADebugDomain(String paramString)
    {
      this.newGADebugDomain = paramString;
    }

    public void setPayDebugDomain(String paramString)
    {
      this.payDebugDomain = paramString;
    }

    public void setProxy(String paramString, int paramInt)
    {
      this.proxy = paramString;
      this.proxyPort = paramInt;
    }

    public void setPushDebugDomain(String paramString)
    {
      this.pushDebugDomain = paramString;
    }

    public void setSwitchDomain(String paramString)
    {
      this.switchDomain = paramString;
    }

    public void setTDebugDomain(String paramString)
    {
      this.tDebugDomain = paramString;
    }

    public void setTakeawayDebugDomain(String paramString)
    {
      this.takeawayDebugDomain = paramString;
    }

    public String switchDomain()
    {
      return this.switchDomain;
    }

    public String tDebugDomain()
    {
      return this.tDebugDomain;
    }

    public String takeawayDebugDomain()
    {
      return this.takeawayDebugDomain;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.ServiceManager
 * JD-Core Version:    0.6.0
 */