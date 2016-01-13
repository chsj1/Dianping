package com.dianping.base.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.DPApplication.MySiteManager;
import com.dianping.app.Environment;
import com.dianping.base.util.RedAlertManager;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.dataservice.mapi.impl.DismissTokenListener;
import com.dianping.loader.SiteManager;
import com.dianping.model.City;
import com.dianping.monitor.MonitorService;
import java.io.File;
import java.io.FileInputStream;

public class NovaApplication extends DPApplication
  implements CityConfig.SwitchListener, AccountListener, DismissTokenListener
{
  private Bundle bundleFromWX = null;
  private boolean dismissTokenSet = false;
  private SiteManager siteManager;
  private int startType = 0;

  private void uploadPushLoadBalanceResult()
  {
    new Thread()
    {
      public void run()
      {
        File localFile = new File(NovaApplication.this.getCacheDir(), "push_slb_monitor");
        if (!localFile.exists())
          return;
        while (true)
        {
          int j;
          try
          {
            Object localObject1 = new byte[32];
            FileInputStream localFileInputStream = new FileInputStream(localFile);
            int i = localFileInputStream.read(localObject1);
            localFileInputStream.close();
            j = Integer.parseInt(new String(localObject1, 0, i, "US_ASCII"));
            localObject1 = (MonitorService)NovaApplication.this.getService("monitor");
            if (j <= 0)
              continue;
            i = 200;
            break label145;
            ((MonitorService)localObject1).pv3(0L, "push_slb", 0, 0, i, 0, 0, j, null);
            return;
            i = -1;
            break label145;
            j = 0;
            continue;
          }
          catch (Exception localException)
          {
            return;
          }
          finally
          {
            localFile.delete();
          }
          label145: if (j <= 0)
            continue;
        }
      }
    }
    .start();
  }

  public Object getService(String paramString)
  {
    Object localObject = super.getService(paramString);
    if ((!this.dismissTokenSet) && ("mapi".equals(paramString)))
    {
      ((DefaultMApiService)getService("mapi_original")).setDismissTokenListener(this);
      this.dismissTokenSet = true;
    }
    return localObject;
  }

  public int getStartType()
  {
    return this.startType;
  }

  public Bundle getWXBundle()
  {
    return this.bundleFromWX;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    CookieSyncManager.createInstance(getApplicationContext());
    if (paramAccountService.id() == 0)
    {
      getSharedPreferences(getPackageName(), 3).edit().remove("syncMask").commit();
      CookieManager.getInstance().removeAllCookie();
    }
    while (true)
    {
      CookieSyncManager.getInstance().sync();
      return;
      CookieManager.getInstance().setCookie(".51ping.com", "dper=" + paramAccountService.newToken());
      CookieManager.getInstance().setCookie(".dianping.com", "dper=" + paramAccountService.newToken());
    }
  }

  public void onApplicationPause()
  {
    super.onApplicationPause();
    uploadPushLoadBalanceResult();
  }

  public void onApplicationResume()
  {
    super.onApplicationResume();
    RedAlertManager.getInstance();
    uploadPushLoadBalanceResult();
  }

  public void onApplicationStart()
  {
    super.onApplicationStart();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity2 == null)
      return;
    DPApplication.instance().tunnelConfigService().refresh();
    paramCity1 = getSharedPreferences(getPackageName(), 0);
    SharedPreferences.Editor localEditor = paramCity1.edit();
    localEditor.remove("findconditions_category");
    localEditor.remove("findconditions_region");
    localEditor.remove("findconditions_sort");
    localEditor.remove("findconditions_range");
    localEditor.putInt("cityId", paramCity2.id());
    localEditor.putString("cityName", paramCity2.name());
    localEditor.putString("cityAreaCode", paramCity2.areaCode());
    localEditor.putBoolean("isPromo", paramCity2.isPromo());
    localEditor.putBoolean("isTuan", paramCity2.isTuan());
    localEditor.putBoolean("isLocalPromo", paramCity2.isLocalPromo());
    localEditor.putFloat("latitude", (float)paramCity2.latitude());
    localEditor.putFloat("longitude", (float)paramCity2.longitude());
    localEditor.putBoolean("isRank", paramCity2.isRankIndexCity());
    localEditor.putBoolean("isLocalDish", paramCity2.isLocalDish());
    localEditor.putInt("flag", paramCity2.flag());
    if (!paramCity1.getBoolean("hasChangedCity", false))
    {
      localEditor.putBoolean("needUploadLog", true);
      localEditor.putInt("firstCityId", paramCity2.id());
      localEditor.putBoolean("hasChangedCity", true);
    }
    localEditor.remove("tuan_filter_region_id");
    localEditor.remove("tuan_filter_region_name");
    localEditor.remove("tuan_filter_region_parent_id");
    localEditor.remove("tuan_filter_category_id");
    localEditor.remove("tuan_filter_category_name");
    localEditor.remove("tuan_filter_id");
    localEditor.remove("tuan_filter_name");
    localEditor.remove("tuan_filter_type");
    localEditor.commit();
  }

  @TargetApi(11)
  public void onCreate()
  {
    super.onCreate();
    MApiDebugAgent localMApiDebugAgent;
    if (Environment.isDebug())
    {
      localMApiDebugAgent = (MApiDebugAgent)getService("mapi_debug");
      localSharedPreferences = getSharedPreferences("com.dianping.mapidebugagent", 0);
      localMApiDebugAgent.setProxy(localSharedPreferences.getString("proxy", null), localSharedPreferences.getInt("proxyPort", 0));
      localMApiDebugAgent.setSwitchDomain(localSharedPreferences.getString("setSwitchDomain", null));
      localMApiDebugAgent.setMapiDomain(localSharedPreferences.getString("setMapiDomain", null));
      localMApiDebugAgent.setBookingDebugDomain(localSharedPreferences.getString("setBookingDebugDomain", null));
      localMApiDebugAgent.setTDebugDomain(localSharedPreferences.getString("setTDebugDomain", null));
      localMApiDebugAgent.setPayDebugDomain(localSharedPreferences.getString("setPayDebugDomain", null));
      localMApiDebugAgent.setMovieDebugDomain(localSharedPreferences.getString("setMovieDebugDomain", null));
      localMApiDebugAgent.setMembercardDebugDomain(localSharedPreferences.getString("setMembercardDebugDomain", null));
      localMApiDebugAgent.setTakeawayDebugDomain(localSharedPreferences.getString("setTakeawayDebugDomain", null));
      localMApiDebugAgent.setHuihuiDebugDomain(localSharedPreferences.getString("setHuihuiDebugDomain", null));
      localMApiDebugAgent.setLocateDebugDomain(localSharedPreferences.getString("setLocateDebugDomain", null));
      localMApiDebugAgent.setConfigDebugDomain(localSharedPreferences.getString("setConfigDebugDomain", null));
      if (Build.VERSION.SDK_INT < 11)
        break label266;
    }
    label266: for (SharedPreferences localSharedPreferences = getSharedPreferences("dppushservice", 4); ; localSharedPreferences = getSharedPreferences("dppushservice", 0))
    {
      localMApiDebugAgent.setPushDebugDomain(localSharedPreferences.getString("dpPushUrl", null));
      return;
    }
  }

  public void onDismissToken()
  {
    ((AccountService)getService("account")).logout();
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
    localIntent.setPackage(getPackageName());
    localIntent.addFlags(335544320);
    startActivity(localIntent);
    Toast.makeText(this, "你的账户异常，请重新登陆", 1).show();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void setStartType(int paramInt)
  {
    this.startType = paramInt;
  }

  public void setWXBundle(Bundle paramBundle)
  {
    this.bundleFromWX = paramBundle;
  }

  public SiteManager siteManager()
  {
    if (this.siteManager == null)
      this.siteManager = new DPApplication.MySiteManager(this)
      {
        public String url()
        {
          String str3 = super.url();
          String str4 = DPActivity.preferences(NovaApplication.this).getString("siteUrl", null);
          if (TextUtils.isEmpty(str4))
            return str3;
          String str2;
          try
          {
            if (str4.indexOf('?') > 0)
            {
              str1 = str4;
              if (str4.endsWith("&"));
            }
            for (String str1 = str4 + "&"; ; str1 = str4 + "?")
            {
              int i = str3.indexOf('?');
              str1 = str1 + str3.substring(i + 1);
              break;
            }
          }
          catch (Exception str2)
          {
            str2 = str3;
          }
          return str2;
        }
      };
    return this.siteManager;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.NovaApplication
 * JD-Core Version:    0.6.0
 */