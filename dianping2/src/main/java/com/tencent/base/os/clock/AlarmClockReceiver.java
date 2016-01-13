package com.tencent.base.os.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmClockReceiver extends BroadcastReceiver
{
  public final void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramIntent != null)
    {
      paramContext = paramIntent.getAction();
      if (paramContext != null)
      {
        paramContext = b.a(paramContext);
        if (paramContext != null)
        {
          paramIntent = paramContext.f();
          if (paramIntent != null)
          {
            if (!paramIntent.a(paramContext))
              break label47;
            b.a(paramContext);
          }
        }
      }
    }
    return;
    label47: b.c(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.base.os.clock.AlarmClockReceiver
 * JD-Core Version:    0.6.0
 */