package com.tencent.base.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import java.util.Iterator;
import java.util.List;

public class ProcessUtils
{
  private static volatile Boolean a = null;
  private static Object b = new Object();
  private static volatile String c = null;
  private static Object d = new Object();

  public static String a(Context paramContext)
  {
    if (c != null)
      return c;
    synchronized (d)
    {
      paramContext = b(paramContext);
      c = paramContext;
      return paramContext;
    }
  }

  private static String b(Context paramContext)
  {
    if (paramContext == null)
      return null;
    try
    {
      int i = Process.myPid();
      paramContext = ((ActivityManager)paramContext.getSystemService("activity")).getRunningAppProcesses();
      if ((paramContext != null) && (paramContext.size() > 0))
      {
        paramContext = paramContext.iterator();
        while (paramContext.hasNext())
        {
          ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)paramContext.next();
          if ((localRunningAppProcessInfo == null) || (localRunningAppProcessInfo.pid != i))
            continue;
          paramContext = localRunningAppProcessInfo.processName;
          return paramContext;
        }
      }
      return null;
    }
    catch (Exception paramContext)
    {
      paramContext.printStackTrace();
    }
    return null;
  }

  // ERROR //
  public static boolean isMainProcess(Context arg0)
  {
    // Byte code:
    //   0: getstatic 15	com/tencent/base/util/ProcessUtils:a	Ljava/lang/Boolean;
    //   3: ifnull +10 -> 13
    //   6: getstatic 15	com/tencent/base/util/ProcessUtils:a	Ljava/lang/Boolean;
    //   9: invokevirtual 88	java/lang/Boolean:booleanValue	()Z
    //   12: ireturn
    //   13: getstatic 20	com/tencent/base/util/ProcessUtils:b	Ljava/lang/Object;
    //   16: astore_1
    //   17: aload_1
    //   18: monitorenter
    //   19: aload_0
    //   20: ifnonnull +12 -> 32
    //   23: aload_1
    //   24: monitorexit
    //   25: iconst_0
    //   26: ireturn
    //   27: astore_0
    //   28: aload_1
    //   29: monitorexit
    //   30: aload_0
    //   31: athrow
    //   32: aload_0
    //   33: invokestatic 90	com/tencent/base/util/ProcessUtils:a	(Landroid/content/Context;)Ljava/lang/String;
    //   36: astore_2
    //   37: aload_2
    //   38: ifnonnull +7 -> 45
    //   41: aload_1
    //   42: monitorexit
    //   43: iconst_0
    //   44: ireturn
    //   45: aload_2
    //   46: aload_0
    //   47: invokevirtual 94	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   50: getfield 97	android/content/pm/ApplicationInfo:processName	Ljava/lang/String;
    //   53: invokevirtual 103	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   56: invokestatic 107	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   59: putstatic 15	com/tencent/base/util/ProcessUtils:a	Ljava/lang/Boolean;
    //   62: getstatic 15	com/tencent/base/util/ProcessUtils:a	Ljava/lang/Boolean;
    //   65: invokevirtual 88	java/lang/Boolean:booleanValue	()Z
    //   68: istore_3
    //   69: aload_1
    //   70: monitorexit
    //   71: iload_3
    //   72: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   23	25	27	finally
    //   28	30	27	finally
    //   32	37	27	finally
    //   41	43	27	finally
    //   45	71	27	finally
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.base.util.ProcessUtils
 * JD-Core Version:    0.6.0
 */