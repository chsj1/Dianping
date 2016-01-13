package com.dianping.base.push.pushservice.dp.impl3v8;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.push.pushservice.PushNotificationHelper;
import com.dianping.base.push.pushservice.dp.ConnectionLog;
import com.dianping.base.push.pushservice.dp.PushService;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.locationservice.realtime.RealTimeLocateResult;
import com.dianping.locationservice.realtime.RealTimeLocator;
import com.dianping.locationservice.realtime.RunMode;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class PushServiceImpl
  implements PushService
{
  private static final String DISABLE_PUSH_LOCATING = "disablePushLocating";
  private static final String DP_PUSH = "3";
  private static final int FIVE_MINUTES = 300000;
  public static final String KEEPALIVEINTERVAL = "keepAliveInterval";
  private static final long KEEP_ALIVE_INTERVAL = 240000L;
  private static final String LAST_PUSH_LOCATION_TIME = "lastPushLocationTime";
  private static final long MOBILE_CACHE_TIME = 21600000L;
  private static final int ONE_MINUTE = 60000;
  private static final int ONE_SECOND = 1000;
  private static final String[] PUSHSERVERIP = { "180.153.132.16", "180.153.132.11", "180.153.132.12", "180.153.132.13", "180.153.132.14", "180.153.132.17", "180.153.132.18", "180.153.132.21", "180.153.132.22", "180.153.132.19", "180.153.132.23", "180.153.132.24", "140.207.219.163", "140.207.219.164", "140.207.219.165", "140.207.219.166", "140.207.219.168", "140.207.219.169", "221.130.190.244", "221.130.190.245", "221.130.190.246" };
  public static final String PUSHSERVERLIST = "pushServerList";
  private static final int[] PUSHSERVERPORT = { 80 };
  public static final String RECONNECTAFTER = "reconnectAfter";
  private static final int START_LOCATING = 1;
  private static final int STOP_GPS_LOCATING = 2;
  private static final int STOP_NETWORK_LOCATING = 3;
  public static final String TAG = "PushServiceImpl";
  private static final int TWO_MINUTES = 120000;
  private static final String VERSION_CODE = "38";
  private static BlackWhiteList blackWhiteList;
  private static Socket currentPushSocket;
  private PendingIntent checkServiceAlivePendingInent;
  private Location currentBestLocation;
  private volatile RealTimeLocateResult currentRealTimeLocateResult;
  private String dpid;
  private ExecutorService executorService;
  private LocationListener gpsLocationListener = new PushServiceImpl.PushLocationListener(this, null);
  private PendingIntent keepAlivePendingInent;
  private Handler locationHandler = new PushServiceImpl.1(this);
  private LocationManager locationManager;
  private AlarmManager mAlarmMgr;
  private ConnectivityManager mConnMan;
  private PushServiceImpl.ConnectionThread mConnection;
  private BroadcastReceiver mConnectivityChanged = new PushServiceImpl.5(this);
  private ConnectionLog mLog;
  private Service mService;
  private boolean mStarted;
  final Handler mhandler = new Handler(Looper.getMainLooper());
  private boolean needSendLocationInfo;
  private LocationListener networkLocationListener = new PushServiceImpl.PushLocationListener(this, null);
  private PendingIntent reconnectPendingInent;
  private Random rnd;
  private String source = "";
  private Runnable startRunnable = new PushServiceImpl.4(this);
  private WifiManager wifiManager;

  private void cancelScheduleCheckServiceAlive()
  {
    this.mAlarmMgr.cancel(this.checkServiceAlivePendingInent);
  }

  private int getCityId()
  {
    return NovaApplication.instance().cityId();
  }

  private JSONObject getConfig()
  {
    Object localObject = getSharedPreferences();
    try
    {
      localObject = new JSONObject(((SharedPreferences)localObject).getString("config", ""));
      return localObject;
    }
    catch (Exception localException)
    {
    }
    return (JSONObject)new JSONObject();
  }

  private String getDpid()
  {
    return this.dpid;
  }

  private int getMobileOperator()
  {
    Object localObject = this.mConnMan.getActiveNetworkInfo();
    if (localObject == null);
    while (true)
    {
      return 0;
      if (((NetworkInfo)localObject).getType() != 0)
        continue;
      try
      {
        localObject = ((TelephonyManager)this.mService.getSystemService("phone")).getSimOperator();
        if (localObject == null)
          continue;
        if ((((String)localObject).equals("46000")) || (((String)localObject).equals("46002")))
          break;
        if (((String)localObject).equals("46001"))
          return 2;
        boolean bool = ((String)localObject).equals("46003");
        if (bool)
          return 3;
      }
      catch (Exception localException)
      {
        return 0;
      }
    }
    return 1;
  }

  private String getNetworkType()
  {
    NetworkInfo localNetworkInfo = this.mConnMan.getActiveNetworkInfo();
    if (localNetworkInfo == null)
      return "";
    if (localNetworkInfo.getType() == 1)
      return "wifi";
    if (localNetworkInfo.getType() == 0);
    try
    {
      int i = ((TelephonyManager)this.mService.getSystemService("phone")).getNetworkType();
      switch (i)
      {
      default:
        return "";
      case 1:
      case 2:
      case 4:
      case 7:
      case 11:
        return "2g";
      case 3:
      case 5:
      case 6:
      case 8:
      case 9:
      case 10:
      case 12:
      case 13:
      case 14:
      case 15:
      }
      return "3g";
    }
    catch (Exception localException)
    {
    }
    return "2g";
  }

  @TargetApi(11)
  private SharedPreferences getSharedPreferences()
  {
    if (this.mService == null)
      return null;
    if (Build.VERSION.SDK_INT >= 11)
      return this.mService.getSharedPreferences("dppushservice", 4);
    return this.mService.getSharedPreferences("dppushservice", 0);
  }

  private String getToken()
  {
    return NovaApplication.instance().accountService().token();
  }

  private String getVersion()
  {
    return Environment.versionName();
  }

  private void handleCrashedService()
  {
    if (wasStarted())
    {
      stopKeepAlives();
      start("restart");
    }
  }

  private boolean isMobile()
  {
    NetworkInfo localNetworkInfo = this.mConnMan.getActiveNetworkInfo();
    if (localNetworkInfo == null);
    do
      return false;
    while (localNetworkInfo.getType() != 0);
    return true;
  }

  private boolean isSameProvider(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return paramString2 == null;
    return paramString1.equals(paramString2);
  }

  private void keepAlive()
  {
    monitorenter;
    try
    {
      if ((this.mStarted) && (this.mConnection != null))
        this.mConnection.sendKeepAlive();
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

  private void log(String paramString)
  {
    Log.i("PushServiceImpl", paramString);
    if (this.mLog != null);
    try
    {
      this.mLog.println(paramString);
      return;
    }
    catch (IOException paramString)
    {
    }
  }

  private void makeUseOfNewLocation(Location paramLocation)
  {
    if (this.currentBestLocation == null)
      this.currentBestLocation = paramLocation;
    long l = paramLocation.getTime() - this.currentBestLocation.getTime();
    int j;
    int k;
    label49: int i;
    label58: label67: int m;
    label89: label97: label108: boolean bool;
    if (l > 120000L)
    {
      j = 1;
      if (l >= -120000L)
        break label141;
      k = 1;
      if (l <= 0L)
        break label147;
      i = 1;
      if (j == 0)
        break label152;
      this.currentBestLocation = paramLocation;
      m = (int)(paramLocation.getAccuracy() - this.currentBestLocation.getAccuracy());
      if (m <= 0)
        break label158;
      j = 1;
      if (m >= 0)
        break label163;
      k = 1;
      if (m <= 200)
        break label169;
      m = 1;
      bool = isSameProvider(paramLocation.getProvider(), this.currentBestLocation.getProvider());
      if (k == 0)
        break label175;
      this.currentBestLocation = paramLocation;
    }
    label141: label147: label152: label158: label163: label169: label175: 
    do
    {
      return;
      j = 0;
      break;
      k = 0;
      break label49;
      i = 0;
      break label58;
      if (k == 0)
        break label67;
      return;
      j = 0;
      break label89;
      k = 0;
      break label97;
      m = 0;
      break label108;
      if ((i == 0) || (j != 0))
        continue;
      this.currentBestLocation = paramLocation;
      return;
    }
    while ((i == 0) || (m != 0) || (!bool));
    this.currentBestLocation = paramLocation;
  }

  private boolean reconnectAfter()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    if (localSharedPreferences == null);
    long l1;
    long l2;
    do
    {
      return false;
      l1 = System.currentTimeMillis();
      l2 = 1000L * localSharedPreferences.getInt("reconnectAfter", 0);
      localSharedPreferences.edit().remove("reconnectAfter").commit();
    }
    while (l2 <= l1);
    this.mAlarmMgr.set(0, l2, this.reconnectPendingInent);
    return true;
  }

  private void reconnectIfNecessary()
  {
    monitorenter;
    try
    {
      if ((this.mStarted) && (this.mConnection == null))
      {
        log("Reconnecting...");
        this.mConnection = new PushServiceImpl.ConnectionThread(this, null);
        this.mConnection.start();
      }
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

  private void scheduleCheckServiceAlive()
  {
    long l = SystemClock.elapsedRealtime();
    this.mAlarmMgr.setRepeating(2, l + 3600000L, 3600000L, this.checkServiceAlivePendingInent);
  }

  private void setStarted(boolean paramBoolean)
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    if (localSharedPreferences == null)
      return;
    localSharedPreferences.edit().putBoolean("isStarted", paramBoolean).commit();
    this.mStarted = paramBoolean;
  }

  private void showNotification(String paramString)
  {
    try
    {
      paramString = new JSONObject(paramString);
      paramString.put("y", "3");
      PushNotificationHelper.intance(this.mService).showPushMessage(paramString.toString());
      return;
    }
    catch (JSONException paramString)
    {
      paramString.printStackTrace();
    }
  }

  private void start(String paramString)
  {
    monitorenter;
    try
    {
      this.mhandler.removeCallbacks(this.startRunnable);
      NovaApplication.instance().pushStatisticsService().flush();
      DefaultMApiService localDefaultMApiService = (DefaultMApiService)DPApplication.instance().getService("mapi_original");
      if (TextUtils.isEmpty(this.dpid))
      {
        log("attempt to get dpid...");
        localDefaultMApiService.startDpid(false);
      }
      if (TextUtils.isEmpty(this.source))
        this.source = paramString;
      this.mhandler.post(this.startRunnable);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  private void startKeepAlives()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    if (localSharedPreferences == null)
      return;
    long l2 = localSharedPreferences.getInt("keepAliveInterval", 0) * 1000;
    long l1 = l2;
    if (l2 == 0L)
      l1 = 240000L;
    this.mAlarmMgr.setRepeating(0, System.currentTimeMillis() + l1, l1, this.keepAlivePendingInent);
  }

  private void startLocating()
  {
    this.currentBestLocation = null;
    this.currentRealTimeLocateResult = null;
    if (!getConfig().optBoolean("disablePushLocating"))
    {
      Log.i("PushServiceImpl", "disablePushLocating is false");
      SharedPreferences localSharedPreferences = getSharedPreferences();
      if (localSharedPreferences != null)
      {
        localSharedPreferences.edit().putLong("lastPushLocationTime", System.currentTimeMillis()).commit();
        RealTimeLocator.getInstance(new PushServiceImpl.3(this), this.mService, RunMode.IN_PUSH_PROCESS).tryLocate(new PushServiceImpl.2(this));
      }
    }
    do
    {
      try
      {
        this.locationManager.requestLocationUpdates("network", 0L, 0.0F, this.networkLocationListener);
        this.locationManager.requestLocationUpdates("gps", 0L, 0.0F, this.gpsLocationListener);
        this.locationHandler.sendEmptyMessageDelayed(2, 1000L);
        this.locationHandler.sendEmptyMessageDelayed(3, 60000L);
        this.locationHandler.sendEmptyMessageDelayed(1, 300000L);
        return;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        while (true)
          Log.w("PushServiceImpl", localIllegalArgumentException.toString());
      }
      catch (SecurityException localSecurityException)
      {
        while (true)
          Log.w("PushServiceImpl", localSecurityException.toString());
      }
      catch (NullPointerException localNullPointerException)
      {
        while (true)
          Log.w("PushServiceImpl", localNullPointerException.toString());
      }
      Log.i("PushServiceImpl", "disablePushLocating is true");
      this.locationHandler.sendEmptyMessageDelayed(1, 300000L);
    }
    while ((!this.mStarted) || (this.mConnection == null));
    PushServiceImpl.ConnectionThread.access$400(this.mConnection);
  }

  private void stop()
  {
    monitorenter;
    try
    {
      if (!this.mStarted)
        log("Attempt to stop connection not active.");
      while (true)
      {
        return;
        setStarted(false);
        this.mService.unregisterReceiver(this.mConnectivityChanged);
        cancelReconnect();
        cancelScheduleCheckServiceAlive();
        if (this.mConnection == null)
          continue;
        this.mConnection.abort();
        this.mConnection = null;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void stopKeepAlives()
  {
    this.mAlarmMgr.cancel(this.keepAlivePendingInent);
  }

  private void stopLocating(boolean paramBoolean)
  {
    if (paramBoolean);
    try
    {
      this.locationManager.removeUpdates(this.networkLocationListener);
      if ((this.mStarted) && (this.mConnection != null))
      {
        PushServiceImpl.ConnectionThread.access$400(this.mConnection);
        return;
        this.locationManager.removeUpdates(this.gpsLocationListener);
        return;
      }
    }
    catch (NullPointerException localNullPointerException)
    {
      Log.w("PushServiceImpl", localNullPointerException.toString());
      return;
    }
    catch (SecurityException localSecurityException)
    {
      Log.w("PushServiceImpl", localSecurityException.toString());
    }
  }

  private boolean wasStarted()
  {
    int j = 0;
    SharedPreferences localSharedPreferences = getSharedPreferences();
    int i = j;
    if (localSharedPreferences != null)
    {
      i = j;
      if (localSharedPreferences.getBoolean("isStarted", false))
        i = 1;
    }
    return i;
  }

  public void cancelReconnect()
  {
    this.mAlarmMgr.cancel(this.reconnectPendingInent);
  }

  public void onCreate(Service paramService)
  {
    this.mService = paramService;
    this.needSendLocationInfo = false;
    this.locationManager = ((LocationManager)paramService.getSystemService("location"));
    this.wifiManager = ((WifiManager)paramService.getSystemService("wifi"));
    long l = getSharedPreferences().getLong("lastPushLocationTime", 0L);
    l = System.currentTimeMillis() - l;
    if (l > 300000L)
      this.locationHandler.sendEmptyMessage(1);
    while (true)
    {
      this.rnd = new Random(System.currentTimeMillis());
      this.executorService = Executors.newSingleThreadExecutor();
      if (Environment.isDebug());
      try
      {
        this.mLog = new ConnectionLog(paramService);
        Log.i("PushServiceImpl", "Opened log at " + this.mLog.getPath());
        this.mAlarmMgr = ((AlarmManager)this.mService.getSystemService("alarm"));
        Intent localIntent = new Intent();
        localIntent.setClass(this.mService, this.mService.getClass());
        localIntent.setAction("com.dianping.push.KEEP_ALIVE");
        this.keepAlivePendingInent = PendingIntent.getService(this.mService, 0, localIntent, 0);
        localIntent = new Intent();
        localIntent.setClass(this.mService, this.mService.getClass());
        localIntent.setAction("com.dianping.push.RECONNECT");
        this.reconnectPendingInent = PendingIntent.getService(this.mService, 0, localIntent, 0);
        localIntent = new Intent();
        localIntent.setClass(this.mService, this.mService.getClass());
        localIntent.setAction("com.dianping.push.START");
        this.checkServiceAlivePendingInent = PendingIntent.getService(this.mService, 0, localIntent, 0);
      }
      catch (IOException localNullPointerException)
      {
        try
        {
          this.mConnMan = ((ConnectivityManager)paramService.getSystemService("connectivity"));
          if (reconnectAfter())
          {
            paramService.stopSelf();
            scheduleCheckServiceAlive();
            return;
            this.locationHandler.sendEmptyMessageDelayed(1, 300000L - l);
            continue;
            localIOException = localIOException;
            Log.i("PushServiceImpl", "failed open log,reason:" + localIOException);
          }
        }
        catch (NullPointerException localNullPointerException)
        {
          while (true)
          {
            stop();
            this.mService.stopSelf();
            continue;
            handleCrashedService();
          }
        }
      }
    }
  }

  public void onDestroy(Service paramService)
  {
    log("Service destroyed (started=" + this.mStarted + ")");
    if (this.mStarted)
      stop();
    try
    {
      if (this.mLog != null)
        this.mLog.close();
      label58: this.mhandler.removeCallbacks(this.startRunnable);
      this.mService = null;
      return;
    }
    catch (IOException paramService)
    {
      break label58;
    }
  }

  public int onStartCommand(Service paramService, Intent paramIntent, int paramInt1, int paramInt2)
  {
    log("Service started with intent=" + paramIntent);
    if (paramIntent != null)
    {
      if (!"com.dianping.push.STOP".equals(paramIntent.getAction()))
        break label54;
      stop();
      this.mService.stopSelf();
    }
    while (true)
    {
      return 1;
      label54: if ("com.dianping.push.START".equals(paramIntent.getAction()))
      {
        start(paramIntent.getStringExtra("source"));
        continue;
      }
      if ("com.dianping.push.KEEP_ALIVE".equals(paramIntent.getAction()))
      {
        keepAlive();
        continue;
      }
      if (!"com.dianping.push.RECONNECT".equals(paramIntent.getAction()))
        continue;
      reconnectIfNecessary();
    }
  }

  public void scheduleReconnect()
  {
    log("Rescheduling connection to load balance.");
    this.mAlarmMgr.set(0, System.currentTimeMillis() + 120000L, this.reconnectPendingInent);
  }

  public void startService(Context paramContext)
  {
    if (this.mService != null)
      paramContext.startService(new Intent(paramContext, this.mService.getClass()));
  }

  public void stopService(Context paramContext)
  {
    if (this.mService != null)
      paramContext.stopService(new Intent(paramContext, this.mService.getClass()));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.dp.impl3v8.PushServiceImpl
 * JD-Core Version:    0.6.0
 */