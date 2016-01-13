package com.dianping.base.push.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaApplication;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;

public class PhoneStatusReceiver extends BroadcastReceiver
{
  protected String TAG = PhoneStatusReceiver.class.getSimpleName();

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Log.d(this.TAG, "PhoneStatusReceiver:onReceive " + paramContext.getApplicationContext());
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()));
    for (int i = 1; i != 0; i = 0)
    {
      NovaApplication.instance().pushStatisticsService().flush();
      Push.startPushService(paramContext, paramIntent.getAction());
      paramContext.startService(new Intent(paramContext, PullService.class));
      return;
    }
    Push.stopPushService(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.PhoneStatusReceiver
 * JD-Core Version:    0.6.0
 */