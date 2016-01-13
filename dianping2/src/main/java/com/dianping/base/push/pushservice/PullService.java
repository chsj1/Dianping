package com.dianping.base.push.pushservice;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaApplication;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import java.util.Arrays;
import java.util.Calendar;
import org.json.JSONObject;

public class PullService extends IntentService
{
  private static final long ONE_DAY = 86400000L;
  private static final long ONE_HOUR = 3600000L;
  private static final String PULL_PUSH = "4";
  public static final String TAG = PullService.class.getSimpleName();
  private static final long THREE_HOUR = 10800000L;

  public PullService()
  {
    super(TAG);
  }

  private String dpObject2JsonString(DPObject paramDPObject)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("d", paramDPObject.getString("MsgID"));
      localJSONObject.put("t", paramDPObject.getString("Title"));
      localJSONObject.put("c", paramDPObject.getString("Content"));
      localJSONObject.put("a", paramDPObject.getString("Url"));
      localJSONObject.put("p", paramDPObject.getString("PushBizID"));
      localJSONObject.put("n", paramDPObject.getInt("ChannelID"));
      localJSONObject.put("s", paramDPObject.getInt("Sound"));
      localJSONObject.put("e", paramDPObject.getLong("ExpireTime"));
      localJSONObject.put("y", "4");
      label121: return localJSONObject.toString();
    }
    catch (org.json.JSONException paramDPObject)
    {
      break label121;
    }
  }

  @TargetApi(11)
  private SharedPreferences getSharedPreferences()
  {
    if (Build.VERSION.SDK_INT >= 11)
      return getSharedPreferences("dppushservice", 4);
    return getSharedPreferences("dppushservice", 0);
  }

  private void pullMessages(long paramLong)
  {
    Calendar localCalendar1 = Calendar.getInstance();
    Calendar localCalendar2 = Calendar.getInstance();
    SharedPreferences localSharedPreferences = getSharedPreferences();
    long l;
    if (ConfigHelper.enablePullService)
    {
      int i = localCalendar1.get(11);
      if ((i >= 8) && (i < 20))
      {
        localCalendar2.setTimeInMillis(localSharedPreferences.getLong("lastReceivedTime", 0L));
        l = localSharedPreferences.getLong("lastPullTime", 0L);
        if ((localCalendar2.get(1) != localCalendar1.get(1)) || (localCalendar2.get(6) != localCalendar1.get(6)))
          break label117;
        if (paramLong - l > 10800000L)
          startPull(paramLong);
      }
    }
    label117: 
    do
      return;
    while (paramLong - l <= 3600000L);
    startPull(paramLong);
  }

  private void refreshConfig(long paramLong)
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    if (paramLong - localSharedPreferences.getLong("lastRefreshConfigTime", 0L) > 86400000L)
    {
      NovaApplication.instance().configService().refresh();
      localSharedPreferences.edit().putLong("lastRefreshConfigTime", paramLong).commit();
    }
  }

  private void startPull(long paramLong)
  {
    String str1 = "http://m.api.dianping.com/pullpush.bin";
    Object localObject = str1;
    if (Environment.isDebug())
    {
      String str2 = getSharedPreferences().getString("pullPushUrl", "");
      localObject = str1;
      if (!TextUtils.isEmpty(str2))
        localObject = str2;
    }
    localObject = (MApiResponse)NovaApplication.instance().mapiService().execSync(BasicMApiRequest.mapiGet((String)localObject, CacheType.DISABLED));
    int j;
    int i;
    if ((localObject != null) && (((MApiResponse)localObject).statusCode() == 200))
    {
      if ((((MApiResponse)localObject).result() instanceof DPObject[]))
      {
        getSharedPreferences().edit().putLong("lastPullTime", paramLong).commit();
        localObject = (DPObject[])(DPObject[])((MApiResponse)localObject).result();
        Log.d(TAG, "request pullpush.bin finished:" + Arrays.toString(localObject));
        j = localObject.length;
        i = 0;
      }
    }
    else
      while (i < j)
      {
        str1 = localObject[i];
        PushNotificationHelper.intance(this).showPushMessage(dpObject2JsonString(str1));
        i += 1;
        continue;
        Log.d(TAG, "request pullpush.bin failed:" + ((MApiResponse)localObject).statusCode());
      }
  }

  @TargetApi(11)
  public void onCreate()
  {
    super.onCreate();
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    long l = System.currentTimeMillis();
    pullMessages(l);
    refreshConfig(l);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.PullService
 * JD-Core Version:    0.6.0
 */