package com.github.mmin18.layoutcast.inflater;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import com.github.mmin18.layoutcast.context.OverrideContext;

public class BootInflater extends BaseInflater
{
  public static LayoutInflater systemInflater;

  public BootInflater(Context paramContext)
  {
    super(paramContext);
  }

  private static boolean CxtImpSFetcherExisted()
  {
    try
    {
      Class.forName("android.app.ContextImpl$StaticServiceFetcher");
      return true;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
    }
    return false;
  }

  private static boolean SysRegSFetcherExisted()
  {
    try
    {
      Class.forName("android.app.SystemServiceRegistry$StaticServiceFetcher");
      return true;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
    }
    return false;
  }

  // ERROR //
  public static void initApplication(android.app.Application paramApplication)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc 32
    //   3: invokevirtual 38	android/app/Application:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   6: checkcast 40	android/view/LayoutInflater
    //   9: astore_1
    //   10: aload_1
    //   11: instanceof 2
    //   14: ifeq +4 -> 18
    //   17: return
    //   18: aload_1
    //   19: putstatic 42	com/github/mmin18/layoutcast/inflater/BootInflater:systemInflater	Landroid/view/LayoutInflater;
    //   22: aload_0
    //   23: invokevirtual 46	android/app/Application:getBaseContext	()Landroid/content/Context;
    //   26: invokevirtual 52	java/lang/Object:getClass	()Ljava/lang/Class;
    //   29: astore_3
    //   30: ldc 54
    //   32: aload_3
    //   33: invokevirtual 58	java/lang/Class:getName	()Ljava/lang/String;
    //   36: invokevirtual 64	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   39: ifeq +277 -> 316
    //   42: aload_3
    //   43: invokevirtual 68	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
    //   46: astore 4
    //   48: invokestatic 70	com/github/mmin18/layoutcast/inflater/BootInflater:CxtImpSFetcherExisted	()Z
    //   51: istore 7
    //   53: invokestatic 72	com/github/mmin18/layoutcast/inflater/BootInflater:SysRegSFetcherExisted	()Z
    //   56: istore 8
    //   58: iload 7
    //   60: ifne +291 -> 351
    //   63: iload 8
    //   65: ifeq -48 -> 17
    //   68: goto +283 -> 351
    //   71: aload 4
    //   73: aload_1
    //   74: invokevirtual 77	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   77: astore 5
    //   79: aconst_null
    //   80: astore_2
    //   81: iconst_1
    //   82: istore 6
    //   84: aload_2
    //   85: astore_1
    //   86: iload 6
    //   88: bipush 50
    //   90: if_icmpge +49 -> 139
    //   93: iload 7
    //   95: ifeq +153 -> 248
    //   98: new 79	java/lang/StringBuilder
    //   101: dup
    //   102: invokespecial 82	java/lang/StringBuilder:<init>	()V
    //   105: ldc 84
    //   107: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: iload 6
    //   112: invokevirtual 91	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   115: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   118: astore_1
    //   119: aload 4
    //   121: aload_1
    //   122: invokevirtual 77	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   125: astore_1
    //   126: aload 5
    //   128: aload_1
    //   129: invokevirtual 98	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   132: istore 8
    //   134: iload 8
    //   136: ifeq +227 -> 363
    //   139: aload_1
    //   140: iconst_0
    //   141: anewarray 19	java/lang/Class
    //   144: invokevirtual 102	java/lang/Class:getDeclaredConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   147: astore_1
    //   148: aload_1
    //   149: iconst_1
    //   150: invokevirtual 108	java/lang/reflect/Constructor:setAccessible	(Z)V
    //   153: aload_1
    //   154: iconst_0
    //   155: anewarray 48	java/lang/Object
    //   158: invokevirtual 112	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   161: astore_2
    //   162: aload 5
    //   164: ldc 114
    //   166: invokevirtual 118	java/lang/Class:getDeclaredField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   169: astore_1
    //   170: aload_1
    //   171: iconst_1
    //   172: invokevirtual 121	java/lang/reflect/Field:setAccessible	(Z)V
    //   175: aload_1
    //   176: aload_2
    //   177: new 2	com/github/mmin18/layoutcast/inflater/BootInflater
    //   180: dup
    //   181: aload_0
    //   182: invokespecial 122	com/github/mmin18/layoutcast/inflater/BootInflater:<init>	(Landroid/content/Context;)V
    //   185: invokevirtual 126	java/lang/reflect/Field:set	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   188: iload 7
    //   190: ifeq +82 -> 272
    //   193: aload_3
    //   194: ldc 128
    //   196: invokevirtual 118	java/lang/Class:getDeclaredField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   199: astore_1
    //   200: aload_1
    //   201: iconst_1
    //   202: invokevirtual 121	java/lang/reflect/Field:setAccessible	(Z)V
    //   205: aload_1
    //   206: aconst_null
    //   207: invokevirtual 132	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   210: checkcast 134	java/util/HashMap
    //   213: ldc 32
    //   215: aload_2
    //   216: invokevirtual 138	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   219: pop
    //   220: aload_0
    //   221: ldc 32
    //   223: invokevirtual 38	android/app/Application:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   226: instanceof 2
    //   229: ifne -212 -> 17
    //   232: new 140	java/lang/RuntimeException
    //   235: dup
    //   236: ldc 142
    //   238: invokespecial 145	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   241: athrow
    //   242: ldc 26
    //   244: astore_1
    //   245: goto -174 -> 71
    //   248: new 79	java/lang/StringBuilder
    //   251: dup
    //   252: invokespecial 82	java/lang/StringBuilder:<init>	()V
    //   255: ldc 147
    //   257: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   260: iload 6
    //   262: invokevirtual 91	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   265: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   268: astore_1
    //   269: goto -150 -> 119
    //   272: aload 4
    //   274: ldc 149
    //   276: invokevirtual 77	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   279: ldc 151
    //   281: invokevirtual 118	java/lang/Class:getDeclaredField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   284: astore_1
    //   285: goto -85 -> 200
    //   288: astore_0
    //   289: new 140	java/lang/RuntimeException
    //   292: dup
    //   293: new 79	java/lang/StringBuilder
    //   296: dup
    //   297: invokespecial 82	java/lang/StringBuilder:<init>	()V
    //   300: ldc 142
    //   302: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: aload_0
    //   306: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   309: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   312: invokespecial 145	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   315: athrow
    //   316: new 140	java/lang/RuntimeException
    //   319: dup
    //   320: new 79	java/lang/StringBuilder
    //   323: dup
    //   324: invokespecial 82	java/lang/StringBuilder:<init>	()V
    //   327: ldc 156
    //   329: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: aload_3
    //   333: invokevirtual 58	java/lang/Class:getName	()Ljava/lang/String;
    //   336: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   339: ldc 158
    //   341: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   344: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   347: invokespecial 145	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   350: athrow
    //   351: iload 7
    //   353: ifeq -111 -> 242
    //   356: ldc 17
    //   358: astore_1
    //   359: goto -288 -> 71
    //   362: astore_1
    //   363: iload 6
    //   365: iconst_1
    //   366: iadd
    //   367: istore 6
    //   369: goto -285 -> 84
    //
    // Exception table:
    //   from	to	target	type
    //   42	58	288	java/lang/Exception
    //   71	79	288	java/lang/Exception
    //   98	119	288	java/lang/Exception
    //   139	188	288	java/lang/Exception
    //   193	200	288	java/lang/Exception
    //   200	220	288	java/lang/Exception
    //   248	269	288	java/lang/Exception
    //   272	285	288	java/lang/Exception
    //   119	134	362	java/lang/Exception
  }

  public LayoutInflater cloneInContext(Context paramContext)
  {
    if ((paramContext instanceof ContextWrapper));
    try
    {
      OverrideContext.overrideDefault((ContextWrapper)paramContext);
      return super.cloneInContext(paramContext);
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("lcast", "fail to override resource in context " + paramContext, localException);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.inflater.BootInflater
 * JD-Core Version:    0.6.0
 */