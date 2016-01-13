package com.dianping.util;

import android.os.Handler;
import android.os.Looper;

public class SafeLooper
  implements Runnable
{
  private static final Object EXIT = new Object();
  private static Handler handler = new Handler(Looper.getMainLooper());
  private static boolean installed;
  private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  public static void install()
  {
    handler.removeMessages(0, EXIT);
    handler.post(new SafeLooper());
  }

  public static void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler)
  {
    uncaughtExceptionHandler = paramUncaughtExceptionHandler;
  }

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: getstatic 54	com/dianping/util/SafeLooper:installed	Z
    //   3: ifeq +4 -> 7
    //   6: return
    //   7: invokestatic 57	android/os/Looper:myLooper	()Landroid/os/Looper;
    //   10: invokestatic 29	android/os/Looper:getMainLooper	()Landroid/os/Looper;
    //   13: if_acmpne -7 -> 6
    //   16: aconst_null
    //   17: astore_1
    //   18: ldc 59
    //   20: ldc 61
    //   22: iconst_0
    //   23: anewarray 63	java/lang/Class
    //   26: invokevirtual 67	java/lang/Class:getDeclaredMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   29: astore_2
    //   30: aload_2
    //   31: iconst_1
    //   32: invokevirtual 73	java/lang/reflect/Method:setAccessible	(Z)V
    //   35: ldc 75
    //   37: ldc 77
    //   39: invokevirtual 81	java/lang/Class:getDeclaredField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   42: astore_3
    //   43: aload_3
    //   44: iconst_1
    //   45: invokevirtual 84	java/lang/reflect/Field:setAccessible	(Z)V
    //   48: getstatic 90	android/os/Build$VERSION:SDK_INT	I
    //   51: bipush 21
    //   53: if_icmplt +20 -> 73
    //   56: ldc 75
    //   58: ldc 92
    //   60: iconst_0
    //   61: anewarray 63	java/lang/Class
    //   64: invokevirtual 67	java/lang/Class:getDeclaredMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   67: astore_1
    //   68: aload_1
    //   69: iconst_1
    //   70: invokevirtual 73	java/lang/reflect/Method:setAccessible	(Z)V
    //   73: iconst_1
    //   74: putstatic 54	com/dianping/util/SafeLooper:installed	Z
    //   77: invokestatic 96	android/os/Looper:myQueue	()Landroid/os/MessageQueue;
    //   80: astore 4
    //   82: invokestatic 102	android/os/Binder:clearCallingIdentity	()J
    //   85: pop2
    //   86: invokestatic 102	android/os/Binder:clearCallingIdentity	()J
    //   89: pop2
    //   90: aload_2
    //   91: aload 4
    //   93: iconst_0
    //   94: anewarray 4	java/lang/Object
    //   97: invokevirtual 106	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   100: checkcast 75	android/os/Message
    //   103: astore 5
    //   105: aload 5
    //   107: ifnull +22 -> 129
    //   110: aload 5
    //   112: getfield 109	android/os/Message:obj	Ljava/lang/Object;
    //   115: astore 6
    //   117: getstatic 21	com/dianping/util/SafeLooper:EXIT	Ljava/lang/Object;
    //   120: astore 7
    //   122: aload 6
    //   124: aload 7
    //   126: if_acmpne +14 -> 140
    //   129: iconst_0
    //   130: putstatic 54	com/dianping/util/SafeLooper:installed	Z
    //   133: return
    //   134: astore_1
    //   135: aload_1
    //   136: invokevirtual 112	java/lang/Exception:printStackTrace	()V
    //   139: return
    //   140: aload_3
    //   141: aload 5
    //   143: invokevirtual 116	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   146: checkcast 23	android/os/Handler
    //   149: aload 5
    //   151: invokevirtual 120	android/os/Handler:dispatchMessage	(Landroid/os/Message;)V
    //   154: invokestatic 102	android/os/Binder:clearCallingIdentity	()J
    //   157: pop2
    //   158: getstatic 90	android/os/Build$VERSION:SDK_INT	I
    //   161: bipush 21
    //   163: if_icmplt +49 -> 212
    //   166: aload_1
    //   167: ifnull -77 -> 90
    //   170: aload_1
    //   171: aload 5
    //   173: iconst_0
    //   174: anewarray 4	java/lang/Object
    //   177: invokevirtual 106	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   180: pop
    //   181: goto -91 -> 90
    //   184: astore_2
    //   185: invokestatic 126	com/dianping/app/Environment:isDebug	()Z
    //   188: ifeq +32 -> 220
    //   191: invokestatic 132	java/lang/Thread:getDefaultUncaughtExceptionHandler	()Ljava/lang/Thread$UncaughtExceptionHandler;
    //   194: astore_1
    //   195: aload_1
    //   196: ifnull -67 -> 129
    //   199: aload_1
    //   200: invokestatic 136	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   203: aload_2
    //   204: invokeinterface 142 3 0
    //   209: goto -80 -> 129
    //   212: aload 5
    //   214: invokevirtual 145	android/os/Message:recycle	()V
    //   217: goto -127 -> 90
    //   220: getstatic 49	com/dianping/util/SafeLooper:uncaughtExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
    //   223: astore 4
    //   225: aload_2
    //   226: astore_1
    //   227: aload_2
    //   228: instanceof 147
    //   231: ifeq +19 -> 250
    //   234: aload_2
    //   235: checkcast 147	java/lang/reflect/InvocationTargetException
    //   238: invokevirtual 151	java/lang/reflect/InvocationTargetException:getCause	()Ljava/lang/Throwable;
    //   241: astore_3
    //   242: aload_3
    //   243: astore_1
    //   244: aload_3
    //   245: ifnonnull +5 -> 250
    //   248: aload_2
    //   249: astore_1
    //   250: aload_2
    //   251: getstatic 157	java/lang/System:err	Ljava/io/PrintStream;
    //   254: invokevirtual 160	java/lang/Exception:printStackTrace	(Ljava/io/PrintStream;)V
    //   257: aload 4
    //   259: ifnull +14 -> 273
    //   262: aload 4
    //   264: invokestatic 136	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   267: aload_1
    //   268: invokeinterface 142 3 0
    //   273: new 23	android/os/Handler
    //   276: dup
    //   277: invokespecial 161	android/os/Handler:<init>	()V
    //   280: aload_0
    //   281: invokevirtual 45	android/os/Handler:post	(Ljava/lang/Runnable;)Z
    //   284: pop
    //   285: goto -156 -> 129
    //
    // Exception table:
    //   from	to	target	type
    //   18	48	134	java/lang/Exception
    //   48	73	134	java/lang/Exception
    //   90	105	184	java/lang/Exception
    //   110	122	184	java/lang/Exception
    //   140	166	184	java/lang/Exception
    //   170	181	184	java/lang/Exception
    //   212	217	184	java/lang/Exception
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.SafeLooper
 * JD-Core Version:    0.6.0
 */