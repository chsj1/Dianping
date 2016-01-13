package com.dianping.mainapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Looper;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.advertisement.AdClient;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.AppResumeHelper;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.push.pushservice.PhoneStatusReceiver;
import com.dianping.base.push.pushservice.Push;
import com.dianping.base.push.pushservice.UdpPing;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.thirdparty.wxapi.WeiXinCard;
import com.dianping.base.web.util.WebUtils;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.http.WnsHttpService;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.debug.DebugWindowService;
import com.dianping.monitor.MonitorService;
import com.dianping.monitor.impl.DefaultMonitorService;
import com.dianping.util.Log;
import com.dianping.util.log.NovaLog;
import com.dianping.widget.view.GAHelper;
import com.dianping.zeus.utils.ZeusGaWrapper;
import com.dianping.zeus.utils.ZeusGaWrapper.IZeusGaHelper;
import com.github.mmin18.layoutcast.LayoutCast;
import com.tencent.beacon.event.UserAction;
import com.tencent.beacon.upload.UploadHandleListener;

public class NovaMainApplication extends NovaApplication
{
  AppResumeHelper appResumeHelper;
  private ZeusGaWrapper.IZeusGaHelper mZeusGaHelper;

  private void enablePhoneStatusReceiver()
  {
    getPackageManager().setComponentEnabledSetting(new ComponentName(this, PhoneStatusReceiver.class), 1, 1);
  }

  private void initNetEnvironment()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences("environment", 0);
    if (TextUtils.isEmpty(localSharedPreferences.getString("net", "")))
      localSharedPreferences.edit().putString("net", "product").apply();
  }

  private void initZeusCore()
  {
    initZeusGa();
    WebUtils.initWebEnvironment();
  }

  private void initZeusGa()
  {
    if (this.mZeusGaHelper == null)
      this.mZeusGaHelper = new ZeusGaWrapper.IZeusGaHelper()
      {
        public void ga(Context paramContext, String paramString1, String paramString2, int paramInt, String paramString3)
        {
          GAHelper.instance().contextStatisticsEvent(paramContext, paramString1, paramString2, paramInt, paramString3);
        }

        public void pv(long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
        {
          ((MonitorService)DPApplication.instance().getService("monitor")).pv(paramLong, paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
        }
      };
    ZeusGaWrapper.setZeusGaHelper(this.mZeusGaHelper);
  }

  private void startMTPush()
  {
    Intent localIntent = new Intent("com.sankuai.mtmp.WAKEUP");
    localIntent.setFlags(32);
    localIntent.putExtra("source", getPackageName());
    sendBroadcast(localIntent);
  }

  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    if (((getApplicationInfo().flags & 0x2) != 0) && (Build.VERSION.SDK_INT > 14))
      LayoutCast.init(this);
  }

  public void onApplicationPause()
  {
    super.onApplicationPause();
    WnsHttpService.stopWnsService();
  }

  public void onApplicationResume()
  {
    WnsHttpService.startWnsService();
    super.onApplicationResume();
    if (this.appResumeHelper == null)
    {
      this.appResumeHelper = AppResumeHelper.instance();
      this.appResumeHelper.init(getApplicationContext());
    }
    this.appResumeHelper.onApplicationResume();
    UdpPing.ping(3600000L, (MonitorService)getService("monitor"));
  }

  public void onApplicationStart()
  {
    super.onApplicationStart();
  }

  public void onApplicationStop()
  {
    if (this.appResumeHelper != null)
      this.appResumeHelper.onApplicationStop();
    cityConfig().removeListener(this);
    accountService().removeListener(this);
    WeiXinCard.instance().uninit();
    DPCache.getInstance().clearExpiredCache();
    NovaLog.removeAllAppenders();
    super.onApplicationStop();
    if (Environment.isDebug());
    try
    {
      stopService(new Intent(this, DebugWindowService.class));
      return;
    }
    catch (SecurityException localSecurityException)
    {
      localSecurityException.printStackTrace();
    }
  }

  public void onCreate()
  {
    SpeedMonitorHelper localSpeedMonitorHelper = new SpeedMonitorHelper("launch_application");
    super.onCreate();
    new Thread(new Runnable()
    {
      public void run()
      {
        if (Looper.myLooper() == null)
          Looper.prepare();
        Environment.imei();
        NovaLog.initialAppenders();
        NovaMainApplication.this.appResumeHelper = AppResumeHelper.instance();
        NovaMainApplication.this.appResumeHelper.init(NovaMainApplication.this.getApplicationContext());
        NovaMainApplication.this.cityConfig().addListener(NovaMainApplication.this);
        NovaMainApplication.this.accountService().addListener(NovaMainApplication.this);
        NovaMainApplication.this.initNetEnvironment();
        Push.init(1, "com.dianping.action.CLICK_NOTIFICATION");
        Push.startPushService(NovaMainApplication.this, NovaMainApplication.this.getPackageName());
        NovaMainApplication.this.enablePhoneStatusReceiver();
        NovaMainApplication.this.initZeusCore();
        NovaMainApplication.this.startMTPush();
      }
    }).start();
    try
    {
      UserAction.initUserAction(this, true, 0L, new UploadHandleListener()
      {
        public void onUploadEnd(int paramInt1, int paramInt2, long paramLong1, long paramLong2, boolean paramBoolean, String paramString)
        {
          if ((paramInt1 == 2) && (paramLong1 > 0L))
          {
            if (paramBoolean)
              ((DefaultMonitorService)NovaMainApplication.this.getService("monitor")).pv(0L, "dengta", 0, 0, 660, 0, (int)paramLong1, 0);
          }
          else
            return;
          ((DefaultMonitorService)NovaMainApplication.this.getService("monitor")).pv(0L, "dengta", 0, 0, 661, 0, 0, 0);
        }
      });
      localObject = "1.0";
    }
    catch (Throwable localThrowable)
    {
      try
      {
        String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        Object localObject = str;
        label69: localObject = (String)localObject + "-android";
        str = ((DefaultMApiService)mapiService()).getDpid();
        WnsHttpService.initWns(this, wnsAppId(), (String)localObject, "rdm", str);
        AdClient.initialize(getApplicationContext());
        localSpeedMonitorHelper.setResponseTime(1, System.currentTimeMillis());
        localSpeedMonitorHelper.sendReport();
        return;
        localThrowable = localThrowable;
        Log.e(localThrowable.toString());
      }
      catch (Exception localException)
      {
        break label69;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mainapplication.NovaMainApplication
 * JD-Core Version:    0.6.0
 */