package com.dianping.base.push.localpush;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LocalpushReceiver extends BroadcastReceiver
{
  private static final boolean DEBUG = true;
  private static final int LOCALPUSH_ID = -1;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final String TAG = "LocalpushReceiver";
  private static boolean isStarted = false;
  private Bitmap logo;

  private boolean cancelAlarm(Context paramContext, LocalpushHelper.LocalpushInfo paramLocalpushInfo)
  {
    Log.d("LocalpushReceiver", "cancel an alarm for " + paramLocalpushInfo);
    if (paramLocalpushInfo == null)
      return false;
    Intent localIntent = new Intent(paramContext, LocalpushReceiver.class);
    localIntent.setAction("com.dianping.action.Intent.ACTION_NOTIFY");
    localIntent.putExtra("id", paramLocalpushInfo.id);
    try
    {
      paramLocalpushInfo = PendingIntent.getBroadcast(paramContext, Integer.parseInt(paramLocalpushInfo.id), localIntent, 0);
      ((AlarmManager)paramContext.getSystemService("alarm")).cancel(paramLocalpushInfo);
      return true;
    }
    catch (NumberFormatException paramContext)
    {
    }
    return false;
  }

  private Bitmap getLogo(Context paramContext)
  {
    if (this.logo == null)
      this.logo = BitmapFactory.decodeResource(paramContext.getResources(), R.drawable.push_notification_logo);
    return this.logo;
  }

  private boolean setAlarm(Context paramContext, LocalpushHelper.LocalpushInfo paramLocalpushInfo)
  {
    Log.d("LocalpushReceiver", "set an alarm for " + paramLocalpushInfo);
    if (paramLocalpushInfo == null)
      return false;
    AlarmManager localAlarmManager = (AlarmManager)paramContext.getSystemService("alarm");
    Object localObject = new Intent(paramContext, LocalpushReceiver.class);
    ((Intent)localObject).setAction("com.dianping.action.Intent.ACTION_NOTIFY");
    ((Intent)localObject).putExtra("id", paramLocalpushInfo.id);
    while (true)
    {
      try
      {
        paramContext = PendingIntent.getBroadcast(paramContext, Integer.parseInt(paramLocalpushInfo.id), (Intent)localObject, 0);
        if (paramLocalpushInfo.type != 0)
          continue;
        Log.i("LocalpushReceiver", "set an alarm. trigger time=" + SDF.format(new Date(paramLocalpushInfo.triggertime)));
        localAlarmManager.set(1, paramLocalpushInfo.triggertime, paramContext);
        continue;
        if ((paramLocalpushInfo.type != 1) && (paramLocalpushInfo.type != 2))
          continue;
        long l4 = System.currentTimeMillis();
        long l1 = paramLocalpushInfo.triggertime;
        if (paramLocalpushInfo.type == 1)
        {
          l2 = 604800000L;
          continue;
          Log.i("LocalpushReceiver", "set a repeat alarm. trigger time=" + SDF.format(new Date(l1)));
          localAlarmManager.setRepeating(1, l1, l2, paramContext);
          continue;
          paramLocalpushInfo = Calendar.getInstance();
          paramLocalpushInfo.setTimeInMillis(l1);
          localObject = Calendar.getInstance();
          ((Calendar)localObject).setTimeInMillis(l4);
          ((Calendar)localObject).set(11, paramLocalpushInfo.get(11));
          ((Calendar)localObject).set(12, paramLocalpushInfo.get(12));
          ((Calendar)localObject).set(13, paramLocalpushInfo.get(13));
          int i = (int)Math.ceil(1.0D * (((Calendar)localObject).getTimeInMillis() - paramLocalpushInfo.getTimeInMillis()) / l2);
          l1 = paramLocalpushInfo.getTimeInMillis();
          long l3 = l1 + i * l2;
          l1 = l3;
          if (l3 >= l4)
            continue;
          l1 = l3 + 1L * l2;
          continue;
          return true;
          if (l1 < l4)
            continue;
          continue;
        }
      }
      catch (NumberFormatException paramContext)
      {
        return false;
      }
      long l2 = 86400000L;
    }
  }

  public boolean isStarted(Context paramContext)
  {
    return paramContext.getSharedPreferences("localpushreceiver", 0).getBoolean("started", false);
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Object localObject2 = paramIntent.getAction();
    Log.d("LocalpushReceiver", "receive an intent. action=" + (String)localObject2 + " currenttime=" + SDF.format(new Date(System.currentTimeMillis())));
    Log.d("LocalpushReceiver", "receiver instance " + this);
    if (("com.dianping.action.Intent.ACTION_UPDATE".equals(localObject2)) || ("android.intent.action.USER_PRESENT".equals(localObject2)))
      if ("android.intent.action.USER_PRESENT".equals(localObject2))
        if ((isStarted) || (isStarted(paramContext)))
          Log.w("LocalpushReceiver", "alarm is set");
    do
    {
      while (true)
      {
        return;
        setStarted(paramContext, true);
        isStarted = true;
        paramIntent = LocalpushHelper.instance(paramContext.getApplicationContext());
        Object localObject3 = paramIntent.getIDs();
        localObject1 = new ArrayList();
        if (localObject3 != null)
        {
          if ("com.dianping.action.Intent.ACTION_UPDATE".equals(localObject2))
          {
            localObject2 = ((Set)localObject3).iterator();
            while (((Iterator)localObject2).hasNext())
              cancelAlarm(paramContext, paramIntent.get((String)((Iterator)localObject2).next()));
          }
          localObject2 = ((Set)localObject3).iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (String)((Iterator)localObject2).next();
            LocalpushHelper.LocalpushInfo localLocalpushInfo = paramIntent.get((String)localObject3);
            Log.d("LocalpushReceiver", "checking " + localLocalpushInfo);
            if (localLocalpushInfo != null)
            {
              if (localLocalpushInfo.isExpired())
              {
                paramIntent.delete((String)localObject3);
                Log.i("LocalpushReceiver", localLocalpushInfo + " is expired. remove it.");
                continue;
              }
              ((List)localObject1).add(localObject3);
              continue;
            }
            Log.w("LocalpushReceiver", "cann't find item " + (String)localObject3);
          }
        }
        localObject1 = ((List)localObject1).iterator();
        while (((Iterator)localObject1).hasNext())
          setAlarm(paramContext, paramIntent.get((String)((Iterator)localObject1).next()));
        continue;
        if (!"com.dianping.action.Intent.ACTION_NOTIFY".equals(localObject2))
          break;
        localObject1 = LocalpushHelper.instance(paramContext.getApplicationContext());
        paramIntent = ((LocalpushHelper)localObject1).get(paramIntent.getStringExtra("id"));
        Log.d("LocalpushReceiver", "start to push a notification " + paramIntent);
        if (paramIntent == null)
          continue;
        if (System.currentTimeMillis() >= paramIntent.endtime)
        {
          Log.w("LocalpushReceiver", paramIntent + " is over.");
          cancelAlarm(paramContext, paramIntent);
          ((LocalpushHelper)localObject1).delete(paramIntent.id);
          return;
        }
        try
        {
          localObject1 = new Intent("android.intent.action.VIEW", Uri.parse(paramIntent.url));
          ((Intent)localObject1).addFlags(268435456);
          localObject2 = new NotificationCompat.Builder(paramContext);
          ((NotificationCompat.Builder)localObject2).setContentTitle("大众点评").setContentText(paramIntent.context).setSmallIcon(R.drawable.icon).setLargeIcon(getLogo(paramContext)).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(paramContext, 0, (Intent)localObject1, 134217728));
          ((NotificationManager)paramContext.getSystemService("notification")).notify(-1, ((NotificationCompat.Builder)localObject2).build());
          return;
        }
        catch (NumberFormatException paramContext)
        {
          Log.e("LocalpushReceiver", "fail to push a notification " + paramContext.toString());
          return;
        }
      }
      if ((!"android.intent.action.QUICKBOOT_POWEROFF".equals(localObject2)) && (!"android.intent.action.ACTION_SHUTDOWN".equals(localObject2)))
        continue;
      setStarted(paramContext, false);
      return;
    }
    while (!"com.dianping.action.Intent.ACTION_SHUTDOWN".equals(localObject2));
    paramIntent = LocalpushHelper.instance(paramContext.getApplicationContext());
    Object localObject1 = paramIntent.getIDs();
    if (localObject1 != null)
    {
      localObject1 = ((Set)localObject1).iterator();
      while (((Iterator)localObject1).hasNext())
        cancelAlarm(paramContext, paramIntent.get((String)((Iterator)localObject1).next()));
    }
    paramIntent.clear();
  }

  public void setStarted(Context paramContext, boolean paramBoolean)
  {
    paramContext.getSharedPreferences("localpushreceiver", 0).edit().putBoolean("started", paramBoolean).commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.localpush.LocalpushReceiver
 * JD-Core Version:    0.6.0
 */