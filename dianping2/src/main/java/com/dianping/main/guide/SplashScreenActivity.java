package com.dianping.main.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.util.BluetoothHelper;
import com.dianping.cache.DPCache;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.SiteManager;
import com.dianping.monitor.MonitorService;
import com.dianping.util.Log;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class SplashScreenActivity extends DPActivity
{
  private static final String ACTION_SHOW_SPLASH = "com.dianping.action.SHOW_SPLASH";
  private static final String BINARY_CITIES = "PL4JF98GHJSLSNF0IK";
  private static final int ONE_SECOND_TIMER = 3;
  private static final int SHOW_SPLASH = 4;
  private static final int START_GUIDANCE_ACTIVITY = 2;
  private static final int START_GUIDANCE_NEW_COMER_ACTIVITY = 1;
  private static final int START_MAIN_ACTIVITY = 5;
  private static final String TAG = "SplashManager";
  Bitmap bgImage = null;
  final Handler h = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      if (paramMessage.what == 1)
        if ((PermissionCheckHelper.instance().isRequesting()) && (!SplashScreenActivity.this.isResumed))
          sendEmptyMessageDelayed(1, 500L);
      while (true)
      {
        return;
        SplashScreenActivity.this.statisticsEvent("index5", "index5_guide", "新用户", 0);
        paramMessage = new Intent("android.intent.action.VIEW", Uri.parse("dianping://guidancenewcomer"));
        SplashScreenActivity.this.startActivity(paramMessage);
        SplashScreenActivity.this.overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
        SplashScreenActivity.this.finish();
        return;
        if (paramMessage.what == 2)
        {
          if ((PermissionCheckHelper.instance().isRequesting()) && (!SplashScreenActivity.this.isResumed))
          {
            sendEmptyMessageDelayed(2, 500L);
            return;
          }
          SplashScreenActivity.this.statisticsEvent("index5", "index5_guide", "老用户", 0);
          paramMessage = new Intent("android.intent.action.VIEW", Uri.parse("dianping://guidance"));
          SplashScreenActivity.this.startActivity(paramMessage);
          SplashScreenActivity.this.overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
          SplashScreenActivity.this.finish();
          return;
        }
        if (paramMessage.what == 3)
          SplashScreenActivity.this.hasOneSecond = true;
        try
        {
          SplashScreenActivity.this.unregisterReceiver(SplashScreenActivity.this.showSplashReceiver);
          label225: long l1 = SystemClock.uptimeMillis();
          long l2 = SplashScreenActivity.this.mStartSplashMillis;
          ((MonitorService)SplashScreenActivity.this.getService("monitor")).pv(0L, "splash.all", 0, 0, -602, 0, 0, (int)(l1 - l2));
          sendEmptyMessage(5);
          Log.d("SplashManager", "load splash timeout");
          return;
          if (paramMessage.what == 4)
          {
            if ((SplashScreenActivity.this.bgImage != null) && (SplashScreenActivity.this.image != null))
            {
              SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).saveShowSplashImage();
              if (SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).getSplashNeedMonitor())
              {
                l1 = SystemClock.uptimeMillis() - SplashScreenActivity.this.mStartSplashMillis;
                ((MonitorService)SplashScreenActivity.this.getService("monitor")).pv(0L, "splash.all", 0, 0, 200, 0, 0, (int)l1);
                Log.i("SplashMonitor", "splash.all " + (int)l1);
              }
              ViewUtils.showView(SplashScreenActivity.this.mBtnSkip);
              SplashScreenActivity.this.image.setImageBitmap(SplashScreenActivity.this.bgImage);
              paramMessage = SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).getSplashClickUrl();
              if (!TextUtils.isEmpty(paramMessage))
                SplashScreenActivity.this.image.setOnClickListener(new View.OnClickListener(paramMessage)
                {
                  public void onClick(View paramView)
                  {
                    SplashScreenActivity.2.this.removeMessages(5);
                    paramView = SplashScreenActivity.this.prefs.getString("splash_click_ga_url", "");
                    if (!TextUtils.isEmpty(paramView))
                      new AdvertisementGa().sendAdGA(paramView);
                    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
                    paramView.putExtra("url", this.val$clickUrl);
                    try
                    {
                      SplashScreenActivity.this.startActivity(paramView);
                      SplashScreenActivity.this.overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
                      SplashScreenActivity.this.finish();
                      return;
                    }
                    catch (Exception paramView)
                    {
                      while (true)
                      {
                        SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        paramView.printStackTrace();
                      }
                    }
                  }
                });
              sendEmptyMessageDelayed(5, SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).getSplashShowTime());
              return;
            }
            sendEmptyMessage(5);
            return;
          }
          if (paramMessage.what != 5)
            continue;
          if ((PermissionCheckHelper.instance().isRequesting()) && (!SplashScreenActivity.this.isResumed))
          {
            sendEmptyMessageDelayed(5, 500L);
            return;
          }
          paramMessage = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
          try
          {
            SplashScreenActivity.this.startActivity(paramMessage);
            SplashScreenActivity.this.overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
            SplashScreenActivity.this.finish();
            return;
          }
          catch (Exception paramMessage)
          {
            while (true)
            {
              SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
              paramMessage.printStackTrace();
            }
          }
        }
        catch (Exception paramMessage)
        {
          break label225;
        }
      }
    }
  };
  boolean hasOneSecond = false;
  ImageView image;
  Button mBtnSkip;
  long mStartSplashMillis = 0L;
  private SharedPreferences prefs;
  final BroadcastReceiver showSplashReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((!"com.dianping.action.SHOW_SPLASH".equals(paramIntent.getAction())) || (!SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).needShowSplashImage()));
      do
      {
        return;
        SplashScreenActivity.this.h.removeMessages(3);
      }
      while (SplashScreenActivity.this.hasOneSecond);
      SplashScreenActivity.this.bgImage = SplashManager.instance(SplashScreenActivity.this.getApplicationContext()).getLocalSplashImage();
      if (SplashScreenActivity.this.bgImage != null)
      {
        SplashScreenActivity.this.h.sendEmptyMessage(4);
        Log.d("SplashManager", "show splash");
        return;
      }
      SplashScreenActivity.this.h.sendEmptyMessage(5);
      Log.d("SplashManager", "splash is null and start main activity");
    }
  };
  private SpeedMonitorHelper speedMonitorHelper;

  private void sendUpgrade()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("imei");
    localArrayList.add(Environment.imei());
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    mapiService().exec(BasicMApiRequest.mapiPost("http://m.api.dianping.com/upgrade.bin", (String[])localArrayList.toArray(new String[0])), null);
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return false;
  }

  public String getPageName()
  {
    return "splash";
  }

  public void onCreate(Bundle paramBundle)
  {
    this.speedMonitorHelper = new SpeedMonitorHelper("launch_splash");
    super.onCreate(paramBundle);
    int i;
    int j;
    if (BluetoothHelper.hasBleFeature(getApplicationContext()))
    {
      BluetoothHelper.scanLeDevice(getApplicationContext());
      i = Environment.versionCode();
      this.prefs = getSharedPreferences(getPackageName(), 0);
      j = this.prefs.getInt("guidanceShowVersion", 0);
      if (ConfigHelper.enableShoufaSplash)
        this.bgImage = SplashManager.instance(getApplicationContext()).getShoufaSplashImage();
      if (j != 0)
        break label242;
      sendUpgrade();
      this.h.sendEmptyMessageDelayed(1, SplashManager.instance(getApplicationContext()).getSplashShowTime());
      label113: super.setContentView(R.layout.activity_splash);
      this.image = ((ImageView)findViewById(R.id.iv_splash));
      this.mBtnSkip = ((Button)findViewById(R.id.skip));
      this.mBtnSkip.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SplashScreenActivity.this.h.removeMessages(5);
          SplashScreenActivity.this.h.sendEmptyMessage(5);
        }
      });
      if (this.bgImage == null)
        break label355;
      this.image.setImageBitmap(this.bgImage);
    }
    while (true)
    {
      paramBundle = new AlphaAnimation(0.1F, 1.0F);
      paramBundle.setDuration(800L);
      this.image.startAnimation(paramBundle);
      new Thread(j, i)
      {
        public void run()
        {
          DPApplication.instance().siteManager().site();
          if (this.val$guidanceShowVersion < this.val$versionCode)
          {
            DPCache.getInstance().clearByCategory("homeIcons");
            SplashScreenActivity.this.deleteFile("PL4JF98GHJSLSNF0IK");
          }
        }
      }
      .start();
      this.speedMonitorHelper.setResponseTime(1, System.currentTimeMillis());
      return;
      BluetoothHelper.sendBroadcast(getApplicationContext());
      break;
      label242: if (j < i)
      {
        sendUpgrade();
        this.h.sendEmptyMessageDelayed(2, SplashManager.instance(getApplicationContext()).getSplashShowTime());
        break label113;
      }
      if (this.bgImage == null)
      {
        registerReceiver(this.showSplashReceiver, new IntentFilter("com.dianping.action.SHOW_SPLASH"));
        SplashManager.instance(getApplicationContext()).getSplash(true);
        this.mStartSplashMillis = SystemClock.uptimeMillis();
        this.h.sendEmptyMessageDelayed(3, 1000L);
        break label113;
      }
      this.h.sendEmptyMessageDelayed(5, SplashManager.instance(getApplicationContext()).getSplashShowTime());
      break label113;
      label355: this.image.setScaleType(ImageView.ScaleType.FIT_XY);
      this.image.setImageResource(R.drawable.ic_splash_screen);
    }
  }

  public void onDestroy()
  {
    this.speedMonitorHelper.setResponseTime(2, System.currentTimeMillis());
    this.speedMonitorHelper.sendReport();
    try
    {
      unregisterReceiver(this.showSplashReceiver);
      label26: this.h.removeCallbacksAndMessages(null);
      if (this.bgImage != null)
        this.bgImage.recycle();
      super.onDestroy();
      return;
    }
    catch (Exception localException)
    {
      break label26;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.SplashScreenActivity
 * JD-Core Version:    0.6.0
 */