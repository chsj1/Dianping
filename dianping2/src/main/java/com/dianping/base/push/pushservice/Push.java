package com.dianping.base.push.pushservice;

import android.content.Context;
import com.dianping.base.push.pushservice.dp.DPPushService;
import com.dianping.configservice.impl.ConfigHelper;

public class Push
{
  public static String clickReceiverFilter;
  public static int clientType = 0;

  static
  {
    clickReceiverFilter = "";
  }

  public static void init(int paramInt, String paramString)
  {
    clientType = paramInt;
    clickReceiverFilter = paramString;
  }

  public static int pushServiceType(Context paramContext)
  {
    return ConfigHelper.pushFlag;
  }

  public static void startPushService(Context paramContext, String paramString)
  {
    int i = pushServiceType(paramContext);
    if (i == 0)
      DPPushService.start(paramContext, paramString);
    do
      return;
    while (i != -1);
    DPPushService.stop(paramContext);
  }

  public static void stopPushService(Context paramContext)
  {
    DPPushService.stop(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.Push
 * JD-Core Version:    0.6.0
 */