package com.dianping.debug;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DebugWindowManager
{
  private static ActivityManager mActivityManager;
  private static WindowManager mWindowManager;
  private static DebugWindowSmallView smallWindow;
  private static WindowManager.LayoutParams smallWindowParams;

  public static void createSmallWindow(Context paramContext)
  {
    WindowManager localWindowManager = getWindowManager(paramContext);
    int i = localWindowManager.getDefaultDisplay().getWidth();
    int j = localWindowManager.getDefaultDisplay().getHeight();
    if (smallWindow == null)
    {
      smallWindow = new DebugWindowSmallView(paramContext);
      if (smallWindowParams == null)
      {
        smallWindowParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT < 23)
          break label154;
      }
    }
    label154: for (smallWindowParams.type = 2005; ; smallWindowParams.type = 2002)
    {
      smallWindowParams.format = 1;
      smallWindowParams.flags = 40;
      smallWindowParams.gravity = 51;
      smallWindowParams.width = DebugWindowSmallView.viewWidth;
      smallWindowParams.height = DebugWindowSmallView.viewHeight;
      smallWindowParams.x = i;
      smallWindowParams.y = (j / 2);
      smallWindow.setParams(smallWindowParams);
      localWindowManager.addView(smallWindow, smallWindowParams);
      return;
    }
  }

  private static ActivityManager getActivityManager(Context paramContext)
  {
    if (mActivityManager == null)
      mActivityManager = (ActivityManager)paramContext.getSystemService("activity");
    return mActivityManager;
  }

  private static WindowManager getWindowManager(Context paramContext)
  {
    if (mWindowManager == null)
      mWindowManager = (WindowManager)paramContext.getSystemService("window");
    return mWindowManager;
  }

  public static boolean isWindowShowing()
  {
    return smallWindow != null;
  }

  public static void removeSmallWindow(Context paramContext)
  {
    if ((smallWindow != null) && (smallWindow.getParent() != null))
    {
      getWindowManager(paramContext).removeView(smallWindow);
      smallWindow = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugWindowManager
 * JD-Core Version:    0.6.0
 */