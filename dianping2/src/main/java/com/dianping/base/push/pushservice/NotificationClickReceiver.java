package com.dianping.base.push.pushservice;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaApplication;
import com.dianping.statistics.StatisticsService;
import com.dianping.statistics.impl.MyStatisticsService;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationClickReceiver extends BroadcastReceiver
{
  public static final String PUSH_CLICKED = "103";

  @TargetApi(11)
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Object localObject = (Intent)paramIntent.getParcelableExtra("realIntent");
    if (localObject == null)
      return;
    paramContext.startActivity((Intent)localObject);
    paramIntent = paramIntent.getStringExtra("jsonMsg");
    while (true)
    {
      try
      {
        localObject = new JSONObject(paramIntent);
        paramIntent = new ArrayList();
        paramIntent.add(new BasicNameValuePair("type", ((JSONObject)localObject).optString("y")));
        paramIntent.add(new BasicNameValuePair("dpid", ((JSONObject)localObject).optString("i")));
        paramIntent.add(new BasicNameValuePair("pushid", ((JSONObject)localObject).optString("p")));
        paramIntent.add(new BasicNameValuePair("msgid", ((JSONObject)localObject).optString("d")));
        paramIntent.add(new BasicNameValuePair("content", ((JSONObject)localObject).optString("c")));
        paramIntent.add(new BasicNameValuePair("timestamp", "" + System.currentTimeMillis()));
        paramIntent.add(new BasicNameValuePair("state", "103"));
        if (!Environment.isDebug())
          continue;
        ((MyStatisticsService)NovaApplication.instance().pushStatisticsService()).setUploadInterval(2500);
        if (Build.VERSION.SDK_INT >= 11)
        {
          paramContext = paramContext.getSharedPreferences("dppushservice", 4);
          paramContext = paramContext.getString("pushStatsUrl", "");
          if (TextUtils.isEmpty(paramContext))
            continue;
          ((MyStatisticsService)NovaApplication.instance().pushStatisticsService()).setUploadUrl(paramContext);
          NovaApplication.instance().pushStatisticsService().record(paramIntent);
          NovaApplication.instance().pushStatisticsService().flush();
          return;
        }
      }
      catch (JSONException paramContext)
      {
        paramContext.printStackTrace();
        return;
      }
      paramContext = paramContext.getSharedPreferences("dppushservice", 0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.NotificationClickReceiver
 * JD-Core Version:    0.6.0
 */