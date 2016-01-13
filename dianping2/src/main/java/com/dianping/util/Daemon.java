package com.dianping.util;

import android.os.Looper;

public class Daemon
{
  private static Looper looper;
  private static volatile boolean shouldStop;
  private static Thread thread = null;

  static
  {
    looper = null;
  }

  public static Looper looper()
  {
    if (looper == null)
      start();
    if (looper == null)
      return Looper.getMainLooper();
    return looper;
  }

  // ERROR //
  public static void start()
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   6: ifnonnull +51 -> 57
    //   9: new 39	com/dianping/util/BlockingItem
    //   12: dup
    //   13: invokespecial 40	com/dianping/util/BlockingItem:<init>	()V
    //   16: astore_0
    //   17: new 42	java/lang/Thread
    //   20: dup
    //   21: new 6	com/dianping/util/Daemon$1
    //   24: dup
    //   25: aload_0
    //   26: invokespecial 45	com/dianping/util/Daemon$1:<init>	(Lcom/dianping/util/BlockingItem;)V
    //   29: ldc 47
    //   31: invokespecial 50	java/lang/Thread:<init>	(Ljava/lang/Runnable;Ljava/lang/String;)V
    //   34: putstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   37: iconst_0
    //   38: putstatic 26	com/dianping/util/Daemon:shouldStop	Z
    //   41: getstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   44: invokevirtual 51	java/lang/Thread:start	()V
    //   47: aload_0
    //   48: invokevirtual 55	com/dianping/util/BlockingItem:take	()Ljava/lang/Object;
    //   51: checkcast 32	android/os/Looper
    //   54: putstatic 18	com/dianping/util/Daemon:looper	Landroid/os/Looper;
    //   57: ldc 2
    //   59: monitorexit
    //   60: return
    //   61: astore_0
    //   62: ldc 2
    //   64: monitorexit
    //   65: aload_0
    //   66: athrow
    //   67: astore_0
    //   68: goto -11 -> 57
    //
    // Exception table:
    //   from	to	target	type
    //   3	47	61	finally
    //   47	57	61	finally
    //   47	57	67	java/lang/InterruptedException
  }

  // ERROR //
  public static void stop()
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: iconst_1
    //   4: putstatic 26	com/dianping/util/Daemon:shouldStop	Z
    //   7: getstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   10: ifnull +29 -> 39
    //   13: getstatic 18	com/dianping/util/Daemon:looper	Landroid/os/Looper;
    //   16: ifnull +23 -> 39
    //   19: getstatic 18	com/dianping/util/Daemon:looper	Landroid/os/Looper;
    //   22: invokevirtual 61	android/os/Looper:quit	()V
    //   25: getstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   28: invokevirtual 64	java/lang/Thread:join	()V
    //   31: aconst_null
    //   32: putstatic 16	com/dianping/util/Daemon:thread	Ljava/lang/Thread;
    //   35: aconst_null
    //   36: putstatic 18	com/dianping/util/Daemon:looper	Landroid/os/Looper;
    //   39: ldc 2
    //   41: monitorexit
    //   42: return
    //   43: astore_0
    //   44: ldc 2
    //   46: monitorexit
    //   47: aload_0
    //   48: athrow
    //   49: astore_0
    //   50: goto -19 -> 31
    //
    // Exception table:
    //   from	to	target	type
    //   3	25	43	finally
    //   25	31	43	finally
    //   31	39	43	finally
    //   25	31	49	java/lang/Exception
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.Daemon
 * JD-Core Version:    0.6.0
 */