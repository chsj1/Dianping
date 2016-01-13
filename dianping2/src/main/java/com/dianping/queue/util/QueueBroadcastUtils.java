package com.dianping.queue.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class QueueBroadcastUtils
{
  public static void registerOrderDishBroadcast(Context paramContext, BroadcastReceiver paramBroadcastReceiver, String[] paramArrayOfString)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      localIntentFilter.addAction(paramArrayOfString[i]);
      i += 1;
    }
    paramContext.registerReceiver(paramBroadcastReceiver, localIntentFilter);
  }

  public static void sendQueueBroadcast(Context paramContext, String paramString, Bundle paramBundle)
  {
    Intent localIntent = new Intent();
    localIntent.setAction(paramString);
    if (paramBundle != null)
      localIntent.putExtras(paramBundle);
    paramContext.sendOrderedBroadcast(localIntent, null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.util.QueueBroadcastUtils
 * JD-Core Version:    0.6.0
 */