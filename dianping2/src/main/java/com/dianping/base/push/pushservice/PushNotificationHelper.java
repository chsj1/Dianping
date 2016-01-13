package com.dianping.base.push.pushservice;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.Style;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaApplication;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.image.impl.ImageRequest;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.statistics.StatisticsService;
import com.dianping.statistics.impl.MyStatisticsService;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushNotificationHelper
{
  public static final int MAX_MSG = 100;
  public static final String MSG_IDS = "msgIds";
  public static final String PUSH_DUPLICATE = "101";
  public static final String PUSH_EXPIRED = "102";
  public static final String PUSH_SHOW = "100";
  private static final String TAG = PushNotificationHelper.class.getSimpleName();
  private static PushNotificationHelper notificationHelper;
  private Context appContext;
  private ImageService imageService;
  private Bitmap logo;
  private NotificationManager mNotificationManager;

  @TargetApi(11)
  private PushNotificationHelper(Context paramContext)
  {
    this.mNotificationManager = ((NotificationManager)paramContext.getSystemService("notification"));
    this.appContext = paramContext.getApplicationContext();
    this.logo = BitmapFactory.decodeResource(paramContext.getResources(), R.drawable.push_notification_logo);
  }

  private void configBuilderForDebug(NotificationCompat.BigTextStyle paramBigTextStyle, String paramString)
  {
    if (Environment.isDebug())
    {
      if ("2".equals(paramString))
        paramBigTextStyle.setSummaryText("from mi push(透传)");
      do
      {
        return;
        if (!"3".equals(paramString))
          continue;
        paramBigTextStyle.setSummaryText("from dp push");
        return;
      }
      while (!"4".equals(paramString));
      paramBigTextStyle.setSummaryText("from pull");
      return;
    }
    paramBigTextStyle.setSummaryText("大众点评");
  }

  private String dpid()
  {
    String str = null;
    Object localObject = DPApplication.instance().getService("mapi_original");
    if ((localObject instanceof DefaultMApiService))
      str = ((DefaultMApiService)localObject).getDpid();
    do
    {
      return str;
      localObject = DPApplication.instance().getService("mapi");
    }
    while (!(localObject instanceof DefaultMApiService));
    return ((DefaultMApiService)localObject).getDpid();
  }

  @TargetApi(11)
  private SharedPreferences getMsgidSharedPreferences()
  {
    if (Build.VERSION.SDK_INT >= 11)
      return this.appContext.getSharedPreferences("pushMsgId", 4);
    return this.appContext.getSharedPreferences("pushMsgId", 0);
  }

  @TargetApi(11)
  private SharedPreferences getSharedPreferences()
  {
    if (Build.VERSION.SDK_INT >= 11)
      return this.appContext.getSharedPreferences("dppushservice", 4);
    return this.appContext.getSharedPreferences("dppushservice", 0);
  }

  private ImageService imageService()
  {
    if (this.imageService == null)
      this.imageService = ((ImageService)DPApplication.instance().getService("image"));
    return this.imageService;
  }

  public static PushNotificationHelper intance(Context paramContext)
  {
    if (notificationHelper == null)
      notificationHelper = new PushNotificationHelper(paramContext);
    return notificationHelper;
  }

  private boolean isAppOnForeground()
  {
    Object localObject = (ActivityManager)this.appContext.getApplicationContext().getSystemService("activity");
    String str = this.appContext.getApplicationContext().getPackageName();
    localObject = ((ActivityManager)localObject).getRunningAppProcesses();
    if (localObject == null);
    ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo;
    do
    {
      while (!((Iterator)localObject).hasNext())
      {
        return false;
        localObject = ((List)localObject).iterator();
      }
      localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)((Iterator)localObject).next();
    }
    while ((!localRunningAppProcessInfo.processName.equals(str)) || (localRunningAppProcessInfo.importance != 100));
    return true;
  }

  private void setReceivedMsgId(String paramString)
  {
    SharedPreferences localSharedPreferences = getSharedPreferences();
    Object localObject = getMsgidSharedPreferences();
    long l = System.currentTimeMillis();
    localSharedPreferences.edit().putLong("lastPullTime", l).putLong("lastReceivedTime", l).commit();
    ArrayList localArrayList = new ArrayList();
    if (((SharedPreferences)localObject).contains("next"))
    {
      int i = ((SharedPreferences)localObject).getInt("first", 0);
      int j = ((SharedPreferences)localObject).getInt("next", 0);
      while (i < j)
      {
        String str = ((SharedPreferences)localObject).getString("" + i, "");
        ((SharedPreferences)localObject).edit().remove("" + i).commit();
        localSharedPreferences.edit().remove(str).commit();
        localArrayList.add(str);
        i += 1;
      }
      ((SharedPreferences)localObject).edit().remove("first").remove("next").commit();
    }
    localObject = localSharedPreferences.getString("msgIds", "");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localArrayList.addAll(Arrays.asList(((String)localObject).split(";")));
    while (localArrayList.size() >= 100)
      localArrayList.remove(0);
    localArrayList.add(paramString);
    paramString = TextUtils.join(";", localArrayList);
    localSharedPreferences.edit().putString("msgIds", paramString).commit();
  }

  public boolean containsMsgId(String paramString)
  {
    Object localObject = getSharedPreferences();
    boolean bool = ((SharedPreferences)localObject).contains(paramString);
    ArrayList localArrayList = new ArrayList();
    localObject = ((SharedPreferences)localObject).getString("msgIds", "");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localArrayList.addAll(Arrays.asList(((String)localObject).split(";")));
    return (bool) || (localArrayList.contains(paramString));
  }

  public void showPushDialog(JSONObject paramJSONObject)
  {
    Intent localIntent = new Intent("com.dianping.action.ShowPushDialog");
    localIntent.putExtra("contentTitle", paramJSONObject.optString("t", "大众点评"));
    localIntent.putExtra("contentText", paramJSONObject.optString("c", "您的团购已消费，赶快评价获取积分！"));
    localIntent.putExtra("url", paramJSONObject.optString("a", "dianping://home"));
    this.appContext.sendBroadcast(localIntent);
  }

  public void showPushMessage(String paramString)
  {
    while (true)
    {
      String str;
      try
      {
        Object localObject = new JSONObject(paramString);
        paramString = new ArrayList();
        paramString.add(new BasicNameValuePair("type", ((JSONObject)localObject).optString("y")));
        paramString.add(new BasicNameValuePair("dpid", dpid()));
        paramString.add(new BasicNameValuePair("pushid", ((JSONObject)localObject).optString("p")));
        paramString.add(new BasicNameValuePair("msgid", ((JSONObject)localObject).optString("d")));
        paramString.add(new BasicNameValuePair("content", ((JSONObject)localObject).optString("c")));
        paramString.add(new BasicNameValuePair("timestamp", "" + System.currentTimeMillis()));
        str = ((JSONObject)localObject).optString("d");
        Log.i(TAG, "msg id :" + str);
        if (containsMsgId(str))
          break label451;
        long l = ((JSONObject)localObject).optLong("e", 0L);
        if ((l == 0L) || (l - System.currentTimeMillis() > 0L))
        {
          str = localObject.optString("p").split(";")[0];
          if (((!str.equals("biz_coupon")) && (!str.equals("biz_coupon_hotel"))) || (!isAppOnForeground()))
            continue;
          showPushDialog((JSONObject)localObject);
          paramString.add(new BasicNameValuePair("state", "100"));
          if (!Environment.isDebug())
            continue;
          ((MyStatisticsService)NovaApplication.instance().pushStatisticsService()).setUploadInterval(2500);
          localObject = getSharedPreferences().getString("pushStatsUrl", "");
          if (TextUtils.isEmpty((CharSequence)localObject))
            continue;
          ((MyStatisticsService)NovaApplication.instance().pushStatisticsService()).setUploadUrl((String)localObject);
          NovaApplication.instance().pushStatisticsService().record(paramString);
          NovaApplication.instance().pushStatisticsService().flush();
          return;
          showPushNotification((JSONObject)localObject);
          continue;
        }
      }
      catch (JSONException paramString)
      {
        paramString.printStackTrace();
        return;
      }
      paramString.add(new BasicNameValuePair("state", "102"));
      Log.i(TAG, "expired msg id :" + str);
      continue;
      label451: paramString.add(new BasicNameValuePair("state", "101"));
      Log.i(TAG, "duplicate msg id :" + str);
    }
  }

  public void showPushNotification(JSONObject paramJSONObject)
  {
    String str1;
    String str2;
    Object localObject3;
    String str3;
    Object localObject2;
    Object localObject1;
    int j;
    int i;
    Object localObject5;
    Object localObject6;
    int k;
    int m;
    int n;
    while (true)
    {
      try
      {
        str1 = dpid();
        str2 = paramJSONObject.optString("d");
        localObject3 = paramJSONObject.optString("t", "大众点评");
        str3 = paramJSONObject.optString("c");
        localObject2 = paramJSONObject.optString("a", "dianping://home");
        localObject1 = localObject2;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          continue;
        localObject1 = "dianping://home";
        j = paramJSONObject.optInt("n");
        i = j;
        if (j != 0)
          continue;
        i = paramJSONObject.getString("d").hashCode();
        localObject2 = new NotificationCompat.Builder(this.appContext).setTicker(str3).setAutoCancel(true);
        localObject4 = paramJSONObject.optString("r");
        if (TextUtils.isEmpty((CharSequence)localObject4))
          break label947;
        localObject4 = new JSONObject((String)localObject4);
        localObject5 = ((JSONObject)localObject4).optString("b");
        if (!TextUtils.isEmpty((CharSequence)localObject5))
          break label358;
        if ((Build.VERSION.SDK_INT != 14) && (Build.VERSION.SDK_INT != 15))
          continue;
        ((NotificationCompat.Builder)localObject2).setLargeIcon(this.logo).setSmallIcon(R.drawable.push_notification_small_icon);
        localObject5 = ((JSONObject)localObject4).optJSONArray("h");
        if (localObject5 == null)
          break label652;
        localObject6 = new SpannableString((CharSequence)localObject3);
        j = 0;
        if (j >= ((JSONArray)localObject5).length())
          break;
        JSONObject localJSONObject = ((JSONArray)localObject5).optJSONObject(j);
        if (localJSONObject == null)
          break label1070;
        k = localJSONObject.optInt("b", -1);
        m = localJSONObject.optInt("e", -1);
        n = localJSONObject.optInt("c");
        if ((k < 0) || (m <= 0) || (m <= k) || (m > ((String)localObject3).length()))
          break label1070;
        ((SpannableString)localObject6).setSpan(new ForegroundColorSpan(n), k, m, 18);
        break label1070;
        if (Build.VERSION.SDK_INT >= 21)
        {
          ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.push_notification_white_icon);
          continue;
        }
      }
      catch (JSONException paramJSONObject)
      {
        paramJSONObject.printStackTrace();
        return;
      }
      ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.icon);
      continue;
      label358: localObject5 = new ImageRequest((String)localObject5, 2);
      localObject5 = imageService().execSync((Request)localObject5);
      if ((localObject5 != null) && ((((Response)localObject5).result() instanceof Bitmap)))
      {
        ((NotificationCompat.Builder)localObject2).setLargeIcon((Bitmap)((Response)localObject5).result());
        if (Build.VERSION.SDK_INT >= 21)
        {
          ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.push_notification_white_icon);
          continue;
        }
        ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.push_notification_small_icon);
        continue;
      }
      if ((Build.VERSION.SDK_INT == 14) || (Build.VERSION.SDK_INT == 15))
      {
        ((NotificationCompat.Builder)localObject2).setLargeIcon(this.logo).setSmallIcon(R.drawable.push_notification_small_icon);
        continue;
      }
      if (Build.VERSION.SDK_INT >= 21)
      {
        ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.push_notification_white_icon);
        continue;
      }
      ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.icon);
    }
    ((NotificationCompat.Builder)localObject2).setContentTitle((CharSequence)localObject6);
    label517: Object localObject4 = ((JSONObject)localObject4).optJSONArray("l");
    if (localObject4 != null)
    {
      localObject5 = new SpannableString(str3);
      j = 0;
    }
    while (true)
    {
      if (j < ((JSONArray)localObject4).length())
      {
        localObject6 = ((JSONArray)localObject4).optJSONObject(j);
        if (localObject6 != null)
        {
          k = ((JSONObject)localObject6).optInt("b", -1);
          m = ((JSONObject)localObject6).optInt("e", -1);
          n = ((JSONObject)localObject6).optInt("c");
          if ((k >= 0) && (m > 0) && (m > k) && (m <= str3.length()))
          {
            ((SpannableString)localObject5).setSpan(new ForegroundColorSpan(n), k, m, 18);
            break label1079;
            label652: ((NotificationCompat.Builder)localObject2).setContentTitle((CharSequence)localObject3);
            break label517;
          }
        }
      }
      else
      {
        ((NotificationCompat.Builder)localObject2).setContentText((CharSequence)localObject5);
        if (Environment.isDebug())
        {
          localObject3 = new NotificationCompat.BigTextStyle().setBigContentTitle((CharSequence)localObject3).bigText(str3);
          configBuilderForDebug((NotificationCompat.BigTextStyle)localObject3, paramJSONObject.optString("y"));
          ((NotificationCompat.Builder)localObject2).setStyle((NotificationCompat.Style)localObject3);
        }
        if (Build.VERSION.SDK_INT < 21)
          ((NotificationCompat.Builder)localObject2).setPriority(2);
        localObject2 = ((NotificationCompat.Builder)localObject2).build();
        j = paramJSONObject.optInt("s");
        if ((j & 0x1) != 0)
        {
          localObject3 = paramJSONObject.optString("v");
          if (!TextUtils.isEmpty((CharSequence)localObject3))
            break label1023;
        }
        label947: label1023: for (((Notification)localObject2).sound = RingtoneManager.getDefaultUri(2); ; ((Notification)localObject2).sound = Uri.parse("android.resource://" + this.appContext.getPackageName() + "/raw/" + (String)localObject3))
        {
          if ((j & 0x2) != 0)
            ((Notification)localObject2).vibrate = new long[] { 0L, 500L };
          if ((j & 0x4) != 0)
            ((Notification)localObject2).flags |= 32;
          localObject1 = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject1));
          ((Intent)localObject1).addFlags(268435456);
          ((Intent)localObject1).putExtra("isFromPush", true);
          localObject3 = new Intent(Push.clickReceiverFilter);
          ((Intent)localObject3).putExtra("realIntent", (Parcelable)localObject1);
          paramJSONObject.put("i", str1);
          ((Intent)localObject3).putExtra("jsonMsg", paramJSONObject.toString());
          ((Notification)localObject2).contentIntent = PendingIntent.getBroadcast(this.appContext, i, (Intent)localObject3, 134217728);
          this.mNotificationManager.cancel(i);
          this.mNotificationManager.notify(i, (Notification)localObject2);
          setReceivedMsgId(str2);
          return;
          ((NotificationCompat.Builder)localObject2).setContentText(str3);
          break;
          if ((Build.VERSION.SDK_INT == 14) || (Build.VERSION.SDK_INT == 15))
            ((NotificationCompat.Builder)localObject2).setLargeIcon(this.logo).setSmallIcon(R.drawable.push_notification_small_icon);
          while (true)
          {
            ((NotificationCompat.Builder)localObject2).setContentText(str3).setContentTitle((CharSequence)localObject3);
            break;
            if (Build.VERSION.SDK_INT >= 21)
            {
              ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.push_notification_white_icon);
              continue;
            }
            ((NotificationCompat.Builder)localObject2).setSmallIcon(R.drawable.icon);
          }
        }
        label1070: j += 1;
        break;
      }
      label1079: j += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.PushNotificationHelper
 * JD-Core Version:    0.6.0
 */