package com.dianping.base.thirdparty.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WXStartReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    WXHelper.registerApp(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.wxapi.WXStartReceiver
 * JD-Core Version:    0.6.0
 */