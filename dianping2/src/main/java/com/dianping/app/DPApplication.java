package com.dianping.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.loader.RepositoryManager;
import com.dianping.loader.SiteManager;
import com.dianping.loader.UrlMapping;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.monitor.impl.DefaultMonitorService;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.CrashReportHelper;
import com.dianping.util.Log;
import com.dianping.util.log.NovaLog;
import com.tencent.beacon.event.UserAction;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.apache.http.NameValuePair;

public class DPApplication extends MultiDexApplication
{
  public static boolean DEV_LOADER = false;
  public static final String PRIMARY_SCHEME = "dianping";
  public static final String SECONDARY_SCHEME = "dpinner";
  private static int activeCounter;
  private static Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 1) && (DPApplication.access$006() == 0))
        DPApplication.instance().onApplicationStop();
      if (paramMessage.what == 2)
        sendEmptyMessageDelayed(3, 100L);
      if ((paramMessage.what == 3) && (DPApplication.access$106() == 0))
        DPApplication.instance().onApplicationPause();
    }
  };
  private static DPApplication instance;
  private static int liveCounter;
  private AccountService accountService;
  private CityConfig cityConfig;
  private ConfigService configService;
  private HttpService httpService;
  private LocationService locationService;
  private CacheService mapiCacheService;
  private MApiService mapiService;
  private StatisticsService pushStatisticsService;
  private ServiceManager services;
  private String sessionId;
  private SiteManager siteManager;
  private StatisticsService statisticsService;
  private ConfigService tunnelConfigService;

  public DPApplication()
  {
    instance = this;
  }

  static DPApplication _instance()
  {
    return instance;
  }

  private void checkCrashReport()
  {
    CrashReportHelper.initialize(this);
    CrashReportHelper.installSafeLooper();
    if (CrashReportHelper.isAvailable())
      CrashReportHelper.sendAndDelete();
    if ((CrashReportHelper.lastOutOfMemoryMills + 10000L > System.currentTimeMillis()) && (Environment.isDebug()))
      Toast.makeText(this, "内存不足", 1).show();
    CrashReportHelper.lastOutOfMemoryMills = 0L;
  }

  public static DPApplication instance()
  {
    if (instance == null)
      throw new IllegalStateException("Application has not been created");
    return instance;
  }

  public AccountService accountService()
  {
    if (this.accountService == null)
      this.accountService = ((AccountService)getService("account"));
    return this.accountService;
  }

  public void activityOnCreate(Activity paramActivity)
  {
    int i = liveCounter;
    liveCounter = i + 1;
    if (i == 0)
      onApplicationStart();
  }

  public void activityOnDestory(Activity paramActivity)
  {
    handler.sendEmptyMessage(1);
  }

  public void activityOnPause(Activity paramActivity)
  {
    handler.sendEmptyMessage(2);
  }

  public void activityOnResume(Activity paramActivity)
  {
    int i = activeCounter;
    activeCounter = i + 1;
    if (i == 0)
      instance().onApplicationResume();
  }

  public City city()
  {
    return cityConfig().currentCity();
  }

  public CityConfig cityConfig()
  {
    if (this.cityConfig == null)
      this.cityConfig = new CityConfig(this);
    return this.cityConfig;
  }

  public int cityId()
  {
    return city().id();
  }

  public ConfigService configService()
  {
    if (this.configService == null)
      this.configService = ((ConfigService)getService("config"));
    return this.configService;
  }

  public Object getService(String paramString)
  {
    if (this.services == null)
      this.services = new ServiceManager(this);
    return this.services.getService(paramString);
  }

  public HttpService httpService()
  {
    if (this.httpService == null)
      this.httpService = ((HttpService)getService("http"));
    return this.httpService;
  }

  public LocationService locationService()
  {
    if (this.locationService == null)
      this.locationService = ((LocationService)getService("location"));
    return this.locationService;
  }

  public CacheService mapiCacheService()
  {
    if (this.mapiCacheService == null)
      this.mapiCacheService = ((CacheService)getService("mapi_cache"));
    return this.mapiCacheService;
  }

  public MApiService mapiService()
  {
    if (this.mapiService == null)
      this.mapiService = ((MApiService)getService("mapi"));
    return this.mapiService;
  }

  public void onApplicationPause()
  {
    Log.i("application", "onApplicationPause");
    ((LocationService)getService("location")).stop();
    ((DefaultMonitorService)getService("monitor")).flush();
    ((DefaultMonitorService)getService("monitor")).setSuspending(true);
  }

  public void onApplicationResume()
  {
    Log.i("application", "onApplicationResume");
    DefaultMApiService localDefaultMApiService = (DefaultMApiService)getService("mapi_original");
    if (localDefaultMApiService != null)
    {
      localDefaultMApiService.resetTunnel();
      localDefaultMApiService.resetLocalDns();
      localDefaultMApiService.startDpid(true);
    }
    tunnelConfigService().refresh();
    new Handler().post(new Runnable()
    {
      public void run()
      {
        ((LocationService)DPApplication.this.getService("location")).start();
      }
    });
    ((DefaultMonitorService)getService("monitor")).setSuspending(false);
    ((DefaultMonitorService)getService("monitor")).flush();
  }

  public void onApplicationStart()
  {
    Log.i("application", "onApplicationStart");
    if ((getApplicationInfo().flags & 0x2) != 0)
      Log.LEVEL = 2;
    while (true)
    {
      this.sessionId = UUID.randomUUID().toString();
      new Handler().post(new Runnable()
      {
        public void run()
        {
          if (DPApplication.DEV_LOADER)
            return;
          if (Environment.isDebug())
          {
            DPApplication.this.siteManager().start(0L);
            return;
          }
          DPApplication.this.siteManager().start(300000L);
        }
      });
      ((StatisticsService)getService("statistics")).flush();
      ((StatisticsService)getService("statistics_new")).flush();
      UserAction.doUploadRecords();
      return;
      Log.LEVEL = 2147483647;
    }
  }

  public void onApplicationStop()
  {
    Log.i("application", "onApplicationStop");
    this.sessionId = null;
    ((StatisticsService)getService("statistics")).flush();
    ((StatisticsService)getService("statistics_new")).flush();
    UserAction.doUploadRecords();
    ((DefaultMonitorService)getService("monitor")).setSuspending(true);
    this.services.stop();
  }

  public void onCreate()
  {
    super.onCreate();
    if ((getApplicationInfo().flags & 0x2) != 0)
      Log.LEVEL = 2;
    while (true)
    {
      checkCrashReport();
      return;
      Log.LEVEL = 2147483647;
    }
  }

  public StatisticsService pushStatisticsService()
  {
    if (this.pushStatisticsService == null)
      this.pushStatisticsService = ((StatisticsService)getService("push_statistics"));
    return this.pushStatisticsService;
  }

  public RepositoryManager repositoryManager()
  {
    return siteManager().repositoryManager();
  }

  String sessionId()
  {
    return this.sessionId;
  }

  public SiteManager siteManager()
  {
    if (this.siteManager == null)
      this.siteManager = new MySiteManager(this);
    return this.siteManager;
  }

  public void startActivity(Intent paramIntent)
  {
    paramIntent = urlMap(paramIntent);
    CrashReportHelper.putUrlSchema(paramIntent.getDataString());
    try
    {
      super.startActivity(paramIntent);
      return;
    }
    catch (Exception paramIntent)
    {
      NovaLog.e("START ACTIVITY", "Exception e: " + paramIntent.toString());
    }
  }

  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    statisticsEvent(paramString1, paramString2, paramString3, paramInt, null);
  }

  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    ((StatisticsService)getService("statistics")).event(paramString1, paramString2, paramString3, paramInt, paramList);
  }

  public StatisticsService statisticsService()
  {
    if (this.statisticsService == null)
      this.statisticsService = ((StatisticsService)getService("statistics"));
    return this.statisticsService;
  }

  public ConfigService tunnelConfigService()
  {
    if (this.tunnelConfigService == null)
      this.tunnelConfigService = ((ConfigService)getService("tunnel_config"));
    return this.tunnelConfigService;
  }

  public Intent urlMap(Intent paramIntent)
  {
    return new UrlMapping(this, siteManager()).urlMap(paramIntent);
  }

  public int wnsAppId()
  {
    return 200001;
  }

  protected class MySiteManager extends SiteManager
  {
    public MySiteManager(Context arg2)
    {
      super();
    }

    public String url()
    {
      StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/site");
      while (true)
      {
        Object localObject2;
        try
        {
          Object localObject1 = DPApplication.this.getResources().getDisplayMetrics();
          localObject1 = ((DisplayMetrics)localObject1).widthPixels + "x" + ((DisplayMetrics)localObject1).heightPixels + "x" + ((DisplayMetrics)localObject1).density + "x" + ((DisplayMetrics)localObject1).densityDpi;
          Locale localLocale = DPApplication.this.getResources().getConfiguration().locale;
          localObject2 = new String[16];
          localObject2[0] = "version";
          localObject2[1] = Environment.versionName();
          localObject2[2] = "os";
          localObject2[3] = Build.VERSION.RELEASE;
          localObject2[4] = "brand";
          localObject2[5] = Build.BRAND;
          localObject2[6] = "model";
          localObject2[7] = Build.MODEL;
          localObject2[8] = "source";
          localObject2[9] = Environment.source();
          localObject2[10] = "deviceid";
          localObject2[11] = Environment.imei();
          localObject2[12] = "screen";
          localObject2[13] = localObject1;
          localObject2[14] = "locale";
          if (localLocale != null)
            continue;
          localObject1 = "";
          break label421;
          if (i >= localObject2.length)
            continue;
          localObject1 = localObject2[i];
          localLocale = localObject2[(i + 1)];
          if (i != 0)
            break label442;
          c = '?';
          localStringBuilder.append(c);
          localStringBuilder.append((String)localObject1);
          localStringBuilder.append('=');
          if (localLocale == null)
            break label433;
          localStringBuilder.append(URLEncoder.encode(localLocale, "ISO-8859-1"));
          break label433;
          localObject1 = localLocale.toString();
          break label421;
          if (!Environment.isDebug())
            continue;
          localStringBuilder.append("&debug=1");
          localObject1 = DPApplication.this.getSharedPreferences("site", 0).getAll().entrySet().iterator();
          if (((Iterator)localObject1).hasNext())
          {
            localObject2 = (Map.Entry)((Iterator)localObject1).next();
            localStringBuilder.append('&').append((String)((Map.Entry)localObject2).getKey()).append('=');
            localObject2 = ((Map.Entry)localObject2).getValue();
            if (localObject2 == null)
              continue;
            localStringBuilder.append(URLEncoder.encode(String.valueOf(localObject2), "ISO-8859-1"));
            continue;
          }
        }
        catch (Exception localException)
        {
          Log.e("loader", "build site.txt url", localException);
        }
        return localStringBuilder.toString();
        label421: localObject2[15] = localException;
        int i = 0;
        continue;
        label433: i += 2;
        continue;
        label442: char c = '&';
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.DPApplication
 * JD-Core Version:    0.6.0
 */