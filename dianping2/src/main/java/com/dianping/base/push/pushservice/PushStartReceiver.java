package com.dianping.base.push.pushservice;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaApplication;
import com.dianping.statistics.StatisticsService;

public class PushStartReceiver extends BroadcastReceiver
{
  @TargetApi(11)
  private SharedPreferences getSharedPreferences(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 11)
      return paramContext.getSharedPreferences("dppushservice", 4);
    return paramContext.getSharedPreferences("dppushservice", 0);
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramIntent = paramIntent.getStringExtra("pushTag");
    getSharedPreferences(paramContext).edit().putString("pushTag", paramIntent).commit();
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()));
    for (int i = 1; i != 0; i = 0)
    {
      NovaApplication.instance().pushStatisticsService().flush();
      Push.startPushService(paramContext, paramIntent);
      paramContext.startService(new Intent(paramContext, PullService.class));
      return;
    }
    Push.stopPushService(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.PushStartReceiver
 * JD-Core Version:    0.6.0
 */