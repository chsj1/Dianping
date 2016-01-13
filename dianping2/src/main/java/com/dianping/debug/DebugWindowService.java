package com.dianping.debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import java.util.Timer;
import java.util.TimerTask;

public class DebugWindowService extends Service
{
  Handler handler = new Handler();
  private Timer timer;

  private List<String> getHomes()
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = getPackageManager();
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.HOME");
    localObject = ((PackageManager)localObject).queryIntentActivities(localIntent, 65536).iterator();
    while (((Iterator)localObject).hasNext())
      localArrayList.add(((ResolveInfo)((Iterator)localObject).next()).activityInfo.packageName);
    return (List<String>)localArrayList;
  }

  private boolean isAppRunning()
  {
    if (Build.VERSION.SDK_INT < 21)
      return isAppRunningBelowL();
    return isAppRunningUpL();
  }

  private boolean isAppRunningBelowL()
  {
    Object localObject = ((ActivityManager)getSystemService("activity")).getRunningTasks(1);
    if (localObject == null);
    String str;
    ActivityManager.RunningTaskInfo localRunningTaskInfo;
    do
    {
      while (!((Iterator)localObject).hasNext())
      {
        return false;
        str = getPackageName();
        localObject = ((List)localObject).iterator();
      }
      localRunningTaskInfo = (ActivityManager.RunningTaskInfo)((Iterator)localObject).next();
    }
    while ((!localRunningTaskInfo.topActivity.getPackageName().equals(str)) || (!localRunningTaskInfo.baseActivity.getPackageName().equals(str)));
    return true;
  }

  // ERROR //
  private boolean isAppRunningUpL()
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 7
    //   3: aload_0
    //   4: ldc 103
    //   6: invokevirtual 107	com/dianping/debug/DebugWindowService:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   9: checkcast 109	android/app/ActivityManager
    //   12: astore_2
    //   13: ldc 138
    //   15: ldc 140
    //   17: invokevirtual 146	java/lang/Class:getDeclaredField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   20: astore_1
    //   21: aload_2
    //   22: invokevirtual 149	android/app/ActivityManager:getRunningAppProcesses	()Ljava/util/List;
    //   25: invokeinterface 62 1 0
    //   30: astore_2
    //   31: iload 7
    //   33: istore 6
    //   35: aload_2
    //   36: invokeinterface 67 1 0
    //   41: ifeq +63 -> 104
    //   44: aload_2
    //   45: invokeinterface 71 1 0
    //   50: checkcast 138	android/app/ActivityManager$RunningAppProcessInfo
    //   53: astore_3
    //   54: aload_3
    //   55: getfield 152	android/app/ActivityManager$RunningAppProcessInfo:importance	I
    //   58: bipush 100
    //   60: if_icmpne -29 -> 31
    //   63: aload_3
    //   64: getfield 155	android/app/ActivityManager$RunningAppProcessInfo:importanceReasonCode	I
    //   67: ifne -36 -> 31
    //   70: aload_1
    //   71: aload_3
    //   72: invokevirtual 161	java/lang/reflect/Field:getInt	(Ljava/lang/Object;)I
    //   75: iconst_2
    //   76: if_icmpne -45 -> 31
    //   79: aload_3
    //   80: getfield 164	android/app/ActivityManager$RunningAppProcessInfo:pid	I
    //   83: istore 4
    //   85: invokestatic 170	android/os/Process:myPid	()I
    //   88: istore 5
    //   90: iload 7
    //   92: istore 6
    //   94: iload 4
    //   96: iload 5
    //   98: if_icmpne +6 -> 104
    //   101: iconst_1
    //   102: istore 6
    //   104: iload 6
    //   106: ireturn
    //   107: astore_1
    //   108: iconst_0
    //   109: ireturn
    //   110: astore_1
    //   111: iconst_0
    //   112: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   13	21	107	java/lang/Exception
    //   70	90	110	java/lang/Exception
  }

  private boolean isHome()
  {
    List localList = ((ActivityManager)getSystemService("activity")).getRunningTasks(1);
    if (localList == null)
      return false;
    return getHomes().contains(((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity.getPackageName());
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.timer.cancel();
    this.timer = null;
    DebugWindowManager.removeSmallWindow(getApplicationContext());
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (this.timer == null)
    {
      this.timer = new Timer();
      this.timer.scheduleAtFixedRate(new RefreshTask(), 0L, 500L);
    }
    return super.onStartCommand(paramIntent, paramInt1, paramInt2);
  }

  class RefreshTask extends TimerTask
  {
    RefreshTask()
    {
    }

    public void run()
    {
      boolean bool = DebugWindowService.this.isAppRunning();
      if ((bool) && (!DebugWindowManager.isWindowShowing()))
        DebugWindowService.this.handler.post(new DebugWindowService.RefreshTask.1(this));
      do
        return;
      while ((bool) || (!DebugWindowManager.isWindowShowing()));
      DebugWindowService.this.handler.post(new DebugWindowService.RefreshTask.2(this));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugWindowService
 * JD-Core Version:    0.6.0
 */