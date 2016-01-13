package com.tencent.wns.data;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;
import android.text.format.Time;
import cloudwns.l.a;
import com.tencent.base.Global;
import java.io.PrintWriter;
import java.io.StringWriter;

public class h
  implements Thread.UncaughtExceptionHandler
{
  private static String a = h.class.getName();
  private static final Thread.UncaughtExceptionHandler d = Thread.getDefaultUncaughtExceptionHandler();
  private String b = Build.MODEL;
  private String c = Build.VERSION.SDK;

  private String a(int paramInt)
  {
    return String.format("%.2fMB", new Object[] { Double.valueOf(paramInt / 1024.0D) });
  }

  private String a(Context paramContext, long paramLong)
  {
    return Formatter.formatFileSize(paramContext, paramLong);
  }

  String a(Context paramContext)
  {
    Object localObject1 = "";
    try
    {
      Object localObject2 = "" + "\ntotalMemory()=" + a(paramContext, Runtime.getRuntime().totalMemory());
      localObject1 = localObject2;
      localObject2 = (String)localObject2 + "\nmaxMemory()=" + a(paramContext, Runtime.getRuntime().maxMemory());
      localObject1 = localObject2;
      paramContext = (String)localObject2 + "\nfreeMemory()=" + a(paramContext, Runtime.getRuntime().freeMemory());
      localObject1 = paramContext;
      localObject2 = new Debug.MemoryInfo();
      localObject1 = paramContext;
      Debug.getMemoryInfo((Debug.MemoryInfo)localObject2);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.dalvikPrivateDirty=" + a(((Debug.MemoryInfo)localObject2).dalvikPrivateDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.dalvikPss=" + a(((Debug.MemoryInfo)localObject2).dalvikPss);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.dalvikSharedDirty=" + a(((Debug.MemoryInfo)localObject2).dalvikSharedDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.nativePrivateDirty=" + a(((Debug.MemoryInfo)localObject2).nativePrivateDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.nativePss=" + a(((Debug.MemoryInfo)localObject2).nativePss);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.nativeSharedDirty=" + a(((Debug.MemoryInfo)localObject2).nativeSharedDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.otherPrivateDirty=" + a(((Debug.MemoryInfo)localObject2).otherPrivateDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.otherPss" + a(((Debug.MemoryInfo)localObject2).otherPss);
      localObject1 = paramContext;
      paramContext = paramContext + "\ndbg.mi.otherSharedDirty=" + a(((Debug.MemoryInfo)localObject2).otherSharedDirty);
      localObject1 = paramContext;
      paramContext = paramContext + "\nTotalPrivateDirty=" + a(((Debug.MemoryInfo)localObject2).getTotalPrivateDirty());
      localObject1 = paramContext;
      paramContext = paramContext + "\nTotalPss=" + a(((Debug.MemoryInfo)localObject2).getTotalPss());
      localObject1 = paramContext;
      paramContext = paramContext + "\nTotalSharedDirty=" + a(((Debug.MemoryInfo)localObject2).getTotalSharedDirty());
      return paramContext;
    }
    catch (java.lang.Exception paramContext)
    {
    }
    return (String)(String)localObject1;
  }

  public void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    StringWriter localStringWriter = new StringWriter();
    paramThrowable.printStackTrace(new PrintWriter(localStringWriter));
    a.e("CrashHandler", paramThrowable.getMessage() + "\n" + localStringWriter.toString());
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject = new Time();
    ((Time)localObject).setToNow();
    localObject = ((Time)localObject).format("%Y-%m-%d %H:%M:%S");
    localStringBuilder.append("\t\n==================LOG=================\t\n");
    localStringBuilder.append("PHONE_MODEL:" + this.b + "\t\n");
    localStringBuilder.append("ANDROID_SDK:" + this.c + "\t\n");
    localStringBuilder.append((String)localObject + "\t\n");
    localStringBuilder.append(localStringWriter.toString());
    localStringBuilder.append("\t\n==================MemoryInfo=================\t\n");
    localStringBuilder.append(a(Global.getContext()));
    localStringBuilder.append("\t\n--------------------------------------\t\n");
    a.c(a, localStringBuilder.toString());
    a.a().flush();
    try
    {
      Thread.sleep(1000L);
      label248: a.a().stop();
      d.uncaughtException(paramThread, paramThrowable);
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      break label248;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.data.h
 * JD-Core Version:    0.6.0
 */