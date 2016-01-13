package com.dianping.base.push.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigRefreshReceiver extends BroadcastReceiver
{
  public static final String ACTION_UPDATE_DPPUSH = "com.dianping.action.UPDATE_DPPUSH";

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Object localObject2 = paramIntent.getStringExtra("oldConfig");
    Object localObject1 = paramIntent.getStringExtra("newConfig");
    try
    {
      localObject2 = new JSONObject((String)localObject2);
      localObject1 = new JSONObject((String)localObject1);
      if (((JSONObject)localObject2).optInt("pushFlag") != ((JSONObject)localObject1).optInt("pushFlag"))
      {
        Push.startPushService(paramContext, paramIntent.getAction());
        return;
      }
      if ((((JSONObject)localObject1).optInt("pushFlag") == 0) && (((JSONObject)localObject1).optInt("pushDex5Version") != ((JSONObject)localObject2).optInt("pushDex5Version")))
      {
        paramContext.sendBroadcast(new Intent("com.dianping.action.UPDATE_DPPUSH"));
        return;
      }
    }
    catch (JSONException paramContext)
    {
      paramContext.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.ConfigRefreshReceiver
 * JD-Core Version:    0.6.0
 */