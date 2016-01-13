package com.tencent.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.tencent.stat.a.e;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class n
{
  private static StatLogger e = k.b();
  private static n f = null;
  Handler a = null;
  volatile int b = 0;
  DeviceInfo c = null;
  private w d;
  private HashMap<String, String> g = new HashMap();

  private n(Context paramContext)
  {
    try
    {
      HandlerThread localHandlerThread = new HandlerThread("StatStore");
      localHandlerThread.start();
      e.w("Launch store thread:" + localHandlerThread);
      this.a = new Handler(localHandlerThread.getLooper());
      paramContext = paramContext.getApplicationContext();
      this.d = new w(paramContext);
      this.d.getWritableDatabase();
      this.d.getReadableDatabase();
      b(paramContext);
      c();
      f();
      this.a.post(new o(this));
      return;
    }
    catch (Throwable paramContext)
    {
      e.e(paramContext);
    }
  }

  public static n a(Context paramContext)
  {
    monitorenter;
    try
    {
      if (f == null)
        f = new n(paramContext);
      paramContext = f;
      return paramContext;
    }
    finally
    {
      monitorexit;
    }
    throw paramContext;
  }

  public static n b()
  {
    return f;
  }

  private void b(int paramInt)
  {
    monitorenter;
    while (true)
    {
      int i;
      ArrayList localArrayList2;
      try
      {
        i = this.b;
        if ((i <= 0) || (paramInt <= 0))
          return;
        e.i("Load " + Integer.toString(this.b) + " unsent events");
        ArrayList localArrayList1 = new ArrayList();
        localArrayList2 = new ArrayList();
        if (paramInt == -1)
          continue;
        i = paramInt;
        if (paramInt <= StatConfig.a())
          continue;
        i = StatConfig.a();
        this.b -= i;
        c(localArrayList2, i);
        e.i("Peek " + Integer.toString(localArrayList2.size()) + " unsent events.");
        if (localArrayList2.isEmpty())
          continue;
        b(localArrayList2, 2);
        Iterator localIterator = localArrayList2.iterator();
        if (localIterator.hasNext())
        {
          localArrayList1.add(((x)localIterator.next()).b);
          continue;
        }
      }
      catch (Throwable localThrowable)
      {
        e.e(localThrowable);
        continue;
      }
      finally
      {
        monitorexit;
      }
      d.b().b(localList, new u(this, localArrayList2, i));
    }
  }

  // ERROR //
  private void b(e parame, c paramc)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic 224	com/tencent/stat/StatConfig:getMaxStoreEventCount	()I
    //   5: istore 5
    //   7: iload 5
    //   9: ifgt +6 -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: aload_0
    //   16: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   19: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   22: invokevirtual 229	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   25: aload_0
    //   26: getfield 41	com/tencent/stat/n:b	I
    //   29: invokestatic 224	com/tencent/stat/StatConfig:getMaxStoreEventCount	()I
    //   32: if_icmple +35 -> 67
    //   35: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   38: ldc 231
    //   40: invokevirtual 234	com/tencent/stat/common/StatLogger:warn	(Ljava/lang/Object;)V
    //   43: aload_0
    //   44: aload_0
    //   45: getfield 41	com/tencent/stat/n:b	I
    //   48: aload_0
    //   49: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   52: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: ldc 236
    //   57: ldc 238
    //   59: aconst_null
    //   60: invokevirtual 242	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   63: isub
    //   64: putfield 41	com/tencent/stat/n:b	I
    //   67: new 244	android/content/ContentValues
    //   70: dup
    //   71: invokespecial 245	android/content/ContentValues:<init>	()V
    //   74: astore_3
    //   75: aload_1
    //   76: invokevirtual 249	com/tencent/stat/a/e:d	()Ljava/lang/String;
    //   79: invokestatic 252	com/tencent/stat/common/k:c	(Ljava/lang/String;)Ljava/lang/String;
    //   82: astore 4
    //   84: aload_3
    //   85: ldc 254
    //   87: aload 4
    //   89: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   92: aload_3
    //   93: ldc_w 260
    //   96: ldc_w 262
    //   99: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   102: aload_3
    //   103: ldc_w 264
    //   106: iconst_1
    //   107: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   110: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   113: aload_3
    //   114: ldc_w 266
    //   117: aload_1
    //   118: invokevirtual 269	com/tencent/stat/a/e:b	()J
    //   121: invokestatic 275	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   124: invokevirtual 278	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   127: aload_0
    //   128: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   131: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   134: ldc 236
    //   136: aconst_null
    //   137: aload_3
    //   138: invokevirtual 282	android/database/sqlite/SQLiteDatabase:insert	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   141: ldc2_w 283
    //   144: lcmp
    //   145: ifne +47 -> 192
    //   148: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   151: new 60	java/lang/StringBuilder
    //   154: dup
    //   155: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   158: ldc_w 286
    //   161: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: aload 4
    //   166: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: invokevirtual 289	com/tencent/stat/common/StatLogger:error	(Ljava/lang/Object;)V
    //   175: aload_0
    //   176: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   179: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   182: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   185: goto -173 -> 12
    //   188: astore_1
    //   189: goto -177 -> 12
    //   192: aload_0
    //   193: aload_0
    //   194: getfield 41	com/tencent/stat/n:b	I
    //   197: iconst_1
    //   198: iadd
    //   199: putfield 41	com/tencent/stat/n:b	I
    //   202: aload_0
    //   203: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   206: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   209: invokevirtual 295	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   212: aload_2
    //   213: ifnull -38 -> 175
    //   216: aload_2
    //   217: invokeinterface 299 1 0
    //   222: goto -47 -> 175
    //   225: astore_1
    //   226: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   229: aload_1
    //   230: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   233: aload_0
    //   234: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   237: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   240: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   243: goto -231 -> 12
    //   246: astore_1
    //   247: goto -235 -> 12
    //   250: astore_1
    //   251: aload_0
    //   252: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   255: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   258: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   261: aload_1
    //   262: athrow
    //   263: astore_1
    //   264: aload_0
    //   265: monitorexit
    //   266: aload_1
    //   267: athrow
    //   268: astore_2
    //   269: goto -8 -> 261
    //
    // Exception table:
    //   from	to	target	type
    //   175	185	188	java/lang/Throwable
    //   15	67	225	java/lang/Throwable
    //   67	175	225	java/lang/Throwable
    //   192	212	225	java/lang/Throwable
    //   216	222	225	java/lang/Throwable
    //   233	243	246	java/lang/Throwable
    //   15	67	250	finally
    //   67	175	250	finally
    //   192	212	250	finally
    //   216	222	250	finally
    //   226	233	250	finally
    //   2	7	263	finally
    //   175	185	263	finally
    //   233	243	263	finally
    //   251	261	263	finally
    //   261	263	263	finally
    //   251	261	268	java/lang/Throwable
  }

  // ERROR //
  private void b(b paramb)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual 303	com/tencent/stat/b:a	()Ljava/lang/String;
    //   6: astore 4
    //   8: aload 4
    //   10: invokestatic 305	com/tencent/stat/common/k:a	(Ljava/lang/String;)Ljava/lang/String;
    //   13: astore_2
    //   14: new 244	android/content/ContentValues
    //   17: dup
    //   18: invokespecial 245	android/content/ContentValues:<init>	()V
    //   21: astore 5
    //   23: aload 5
    //   25: ldc 254
    //   27: aload_1
    //   28: getfield 308	com/tencent/stat/b:b	Lorg/json/JSONObject;
    //   31: invokevirtual 311	org/json/JSONObject:toString	()Ljava/lang/String;
    //   34: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   37: aload 5
    //   39: ldc_w 313
    //   42: aload_2
    //   43: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   46: aload_1
    //   47: aload_2
    //   48: putfield 315	com/tencent/stat/b:c	Ljava/lang/String;
    //   51: aload 5
    //   53: ldc_w 317
    //   56: aload_1
    //   57: getfield 319	com/tencent/stat/b:d	I
    //   60: invokestatic 322	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   63: invokevirtual 325	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   66: aload_0
    //   67: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   70: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   73: ldc_w 327
    //   76: aconst_null
    //   77: aconst_null
    //   78: aconst_null
    //   79: aconst_null
    //   80: aconst_null
    //   81: aconst_null
    //   82: invokevirtual 331	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   85: astore_3
    //   86: aload_3
    //   87: astore_2
    //   88: aload_3
    //   89: invokeinterface 336 1 0
    //   94: ifeq +241 -> 335
    //   97: aload_3
    //   98: astore_2
    //   99: aload_3
    //   100: iconst_0
    //   101: invokeinterface 340 2 0
    //   106: aload_1
    //   107: getfield 342	com/tencent/stat/b:a	I
    //   110: if_icmpne -24 -> 86
    //   113: iconst_1
    //   114: istore 6
    //   116: iconst_1
    //   117: iload 6
    //   119: if_icmpne +91 -> 210
    //   122: aload_3
    //   123: astore_2
    //   124: aload_0
    //   125: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   128: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   131: ldc_w 327
    //   134: aload 5
    //   136: ldc_w 344
    //   139: iconst_1
    //   140: anewarray 346	java/lang/String
    //   143: dup
    //   144: iconst_0
    //   145: aload_1
    //   146: getfield 342	com/tencent/stat/b:a	I
    //   149: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   152: aastore
    //   153: invokevirtual 350	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   156: i2l
    //   157: lstore 7
    //   159: lload 7
    //   161: ldc2_w 283
    //   164: lcmp
    //   165: ifne +85 -> 250
    //   168: aload_3
    //   169: astore_2
    //   170: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   173: new 60	java/lang/StringBuilder
    //   176: dup
    //   177: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   180: ldc_w 352
    //   183: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: aload 4
    //   188: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   194: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   197: aload_3
    //   198: ifnull +9 -> 207
    //   201: aload_3
    //   202: invokeinterface 355 1 0
    //   207: aload_0
    //   208: monitorexit
    //   209: return
    //   210: aload_3
    //   211: astore_2
    //   212: aload 5
    //   214: ldc_w 357
    //   217: aload_1
    //   218: getfield 342	com/tencent/stat/b:a	I
    //   221: invokestatic 322	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   224: invokevirtual 325	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   227: aload_3
    //   228: astore_2
    //   229: aload_0
    //   230: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   233: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   236: ldc_w 327
    //   239: aconst_null
    //   240: aload 5
    //   242: invokevirtual 282	android/database/sqlite/SQLiteDatabase:insert	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   245: lstore 7
    //   247: goto -88 -> 159
    //   250: aload_3
    //   251: astore_2
    //   252: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   255: new 60	java/lang/StringBuilder
    //   258: dup
    //   259: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   262: ldc_w 359
    //   265: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: aload 4
    //   270: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   276: invokevirtual 361	com/tencent/stat/common/StatLogger:d	(Ljava/lang/Object;)V
    //   279: goto -82 -> 197
    //   282: astore_1
    //   283: aload_3
    //   284: astore_2
    //   285: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   288: aload_1
    //   289: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   292: aload_3
    //   293: ifnull -86 -> 207
    //   296: aload_3
    //   297: invokeinterface 355 1 0
    //   302: goto -95 -> 207
    //   305: astore_1
    //   306: aload_0
    //   307: monitorexit
    //   308: aload_1
    //   309: athrow
    //   310: astore_1
    //   311: aconst_null
    //   312: astore_2
    //   313: aload_2
    //   314: ifnull +9 -> 323
    //   317: aload_2
    //   318: invokeinterface 355 1 0
    //   323: aload_1
    //   324: athrow
    //   325: astore_1
    //   326: goto -13 -> 313
    //   329: astore_1
    //   330: aconst_null
    //   331: astore_3
    //   332: goto -49 -> 283
    //   335: iconst_0
    //   336: istore 6
    //   338: goto -222 -> 116
    //
    // Exception table:
    //   from	to	target	type
    //   88	97	282	java/lang/Throwable
    //   99	113	282	java/lang/Throwable
    //   124	159	282	java/lang/Throwable
    //   170	197	282	java/lang/Throwable
    //   212	227	282	java/lang/Throwable
    //   229	247	282	java/lang/Throwable
    //   252	279	282	java/lang/Throwable
    //   201	207	305	finally
    //   296	302	305	finally
    //   317	323	305	finally
    //   323	325	305	finally
    //   2	86	310	finally
    //   88	97	325	finally
    //   99	113	325	finally
    //   124	159	325	finally
    //   170	197	325	finally
    //   212	227	325	finally
    //   229	247	325	finally
    //   252	279	325	finally
    //   285	292	325	finally
    //   2	86	329	java/lang/Throwable
  }

  // ERROR //
  private void b(List<x> paramList)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   5: new 60	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   12: ldc_w 365
    //   15: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: aload_1
    //   19: invokeinterface 181 1 0
    //   24: invokevirtual 368	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   27: ldc_w 370
    //   30: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: invokestatic 376	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   36: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   39: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   42: invokevirtual 164	com/tencent/stat/common/StatLogger:i	(Ljava/lang/Object;)V
    //   45: aload_0
    //   46: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   49: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   52: invokevirtual 229	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   55: aload_1
    //   56: invokeinterface 191 1 0
    //   61: astore_1
    //   62: aload_1
    //   63: invokeinterface 196 1 0
    //   68: ifeq +75 -> 143
    //   71: aload_1
    //   72: invokeinterface 200 1 0
    //   77: checkcast 202	com/tencent/stat/x
    //   80: astore_2
    //   81: aload_0
    //   82: aload_0
    //   83: getfield 41	com/tencent/stat/n:b	I
    //   86: aload_0
    //   87: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   90: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   93: ldc 236
    //   95: ldc_w 378
    //   98: iconst_1
    //   99: anewarray 346	java/lang/String
    //   102: dup
    //   103: iconst_0
    //   104: aload_2
    //   105: getfield 381	com/tencent/stat/x:a	J
    //   108: invokestatic 384	java/lang/Long:toString	(J)Ljava/lang/String;
    //   111: aastore
    //   112: invokevirtual 242	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   115: isub
    //   116: putfield 41	com/tencent/stat/n:b	I
    //   119: goto -57 -> 62
    //   122: astore_1
    //   123: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   126: aload_1
    //   127: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   130: aload_0
    //   131: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   134: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   137: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   140: aload_0
    //   141: monitorexit
    //   142: return
    //   143: aload_0
    //   144: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   147: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   150: invokevirtual 295	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   153: aload_0
    //   154: aload_0
    //   155: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   158: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   161: ldc 236
    //   163: invokestatic 390	android/database/DatabaseUtils:queryNumEntries	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)J
    //   166: l2i
    //   167: putfield 41	com/tencent/stat/n:b	I
    //   170: aload_0
    //   171: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   174: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   177: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   180: goto -40 -> 140
    //   183: astore_1
    //   184: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   187: aload_1
    //   188: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   191: goto -51 -> 140
    //   194: astore_1
    //   195: aload_0
    //   196: monitorexit
    //   197: aload_1
    //   198: athrow
    //   199: astore_1
    //   200: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   203: aload_1
    //   204: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   207: goto -67 -> 140
    //   210: astore_1
    //   211: aload_0
    //   212: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   215: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   218: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   221: aload_1
    //   222: athrow
    //   223: astore_2
    //   224: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   227: aload_2
    //   228: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   231: goto -10 -> 221
    //
    // Exception table:
    //   from	to	target	type
    //   45	62	122	java/lang/Throwable
    //   62	119	122	java/lang/Throwable
    //   143	170	122	java/lang/Throwable
    //   170	180	183	android/database/sqlite/SQLiteException
    //   2	45	194	finally
    //   130	140	194	finally
    //   170	180	194	finally
    //   184	191	194	finally
    //   200	207	194	finally
    //   211	221	194	finally
    //   221	223	194	finally
    //   224	231	194	finally
    //   130	140	199	android/database/sqlite/SQLiteException
    //   45	62	210	finally
    //   62	119	210	finally
    //   123	130	210	finally
    //   143	170	210	finally
    //   211	221	223	android/database/sqlite/SQLiteException
  }

  // ERROR //
  private void b(List<x> paramList, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   5: new 60	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   12: ldc_w 397
    //   15: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: aload_1
    //   19: invokeinterface 181 1 0
    //   24: invokevirtual 368	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   27: ldc_w 399
    //   30: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: iload_2
    //   34: invokevirtual 368	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   37: ldc_w 401
    //   40: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: invokestatic 376	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   46: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   49: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   52: invokevirtual 164	com/tencent/stat/common/StatLogger:i	(Ljava/lang/Object;)V
    //   55: new 244	android/content/ContentValues
    //   58: dup
    //   59: invokespecial 245	android/content/ContentValues:<init>	()V
    //   62: astore_3
    //   63: aload_3
    //   64: ldc_w 264
    //   67: iload_2
    //   68: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   71: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   74: aload_0
    //   75: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   78: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   81: invokevirtual 229	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   84: aload_1
    //   85: invokeinterface 191 1 0
    //   90: astore_1
    //   91: aload_1
    //   92: invokeinterface 196 1 0
    //   97: ifeq +233 -> 330
    //   100: aload_1
    //   101: invokeinterface 200 1 0
    //   106: checkcast 202	com/tencent/stat/x
    //   109: astore 4
    //   111: aload 4
    //   113: getfield 402	com/tencent/stat/x:d	I
    //   116: iconst_1
    //   117: iadd
    //   118: invokestatic 405	com/tencent/stat/StatConfig:getMaxSendRetryCount	()I
    //   121: if_icmple +66 -> 187
    //   124: aload_0
    //   125: aload_0
    //   126: getfield 41	com/tencent/stat/n:b	I
    //   129: aload_0
    //   130: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   133: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   136: ldc 236
    //   138: ldc_w 407
    //   141: iconst_1
    //   142: anewarray 346	java/lang/String
    //   145: dup
    //   146: iconst_0
    //   147: aload 4
    //   149: getfield 381	com/tencent/stat/x:a	J
    //   152: invokestatic 384	java/lang/Long:toString	(J)Ljava/lang/String;
    //   155: aastore
    //   156: invokevirtual 242	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   159: isub
    //   160: putfield 41	com/tencent/stat/n:b	I
    //   163: goto -72 -> 91
    //   166: astore_1
    //   167: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   170: aload_1
    //   171: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   174: aload_0
    //   175: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   178: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   181: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   184: aload_0
    //   185: monitorexit
    //   186: return
    //   187: aload_3
    //   188: ldc_w 260
    //   191: aload 4
    //   193: getfield 402	com/tencent/stat/x:d	I
    //   196: iconst_1
    //   197: iadd
    //   198: invokestatic 322	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   201: invokevirtual 325	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   204: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   207: new 60	java/lang/StringBuilder
    //   210: dup
    //   211: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   214: ldc_w 409
    //   217: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: aload 4
    //   222: getfield 381	com/tencent/stat/x:a	J
    //   225: invokevirtual 412	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   228: ldc_w 414
    //   231: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: aload_3
    //   235: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   238: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   241: invokevirtual 164	com/tencent/stat/common/StatLogger:i	(Ljava/lang/Object;)V
    //   244: aload_0
    //   245: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   248: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   251: ldc 236
    //   253: aload_3
    //   254: ldc_w 407
    //   257: iconst_1
    //   258: anewarray 346	java/lang/String
    //   261: dup
    //   262: iconst_0
    //   263: aload 4
    //   265: getfield 381	com/tencent/stat/x:a	J
    //   268: invokestatic 384	java/lang/Long:toString	(J)Ljava/lang/String;
    //   271: aastore
    //   272: invokevirtual 350	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   275: istore_2
    //   276: iload_2
    //   277: ifgt -186 -> 91
    //   280: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   283: new 60	java/lang/StringBuilder
    //   286: dup
    //   287: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   290: ldc_w 416
    //   293: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: iload_2
    //   297: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   300: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   303: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   306: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   309: goto -218 -> 91
    //   312: astore_1
    //   313: aload_0
    //   314: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   317: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   320: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   323: aload_1
    //   324: athrow
    //   325: astore_1
    //   326: aload_0
    //   327: monitorexit
    //   328: aload_1
    //   329: athrow
    //   330: aload_0
    //   331: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   334: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   337: invokevirtual 295	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   340: aload_0
    //   341: aload_0
    //   342: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   345: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   348: ldc 236
    //   350: invokestatic 390	android/database/DatabaseUtils:queryNumEntries	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;)J
    //   353: l2i
    //   354: putfield 41	com/tencent/stat/n:b	I
    //   357: aload_0
    //   358: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   361: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   364: invokevirtual 292	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   367: goto -183 -> 184
    //   370: astore_1
    //   371: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   374: aload_1
    //   375: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   378: goto -194 -> 184
    //   381: astore_1
    //   382: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   385: aload_1
    //   386: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   389: goto -205 -> 184
    //   392: astore_3
    //   393: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   396: aload_3
    //   397: invokevirtual 393	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Exception;)V
    //   400: goto -77 -> 323
    //
    // Exception table:
    //   from	to	target	type
    //   55	91	166	java/lang/Throwable
    //   91	163	166	java/lang/Throwable
    //   187	276	166	java/lang/Throwable
    //   280	309	166	java/lang/Throwable
    //   330	357	166	java/lang/Throwable
    //   55	91	312	finally
    //   91	163	312	finally
    //   167	174	312	finally
    //   187	276	312	finally
    //   280	309	312	finally
    //   330	357	312	finally
    //   2	55	325	finally
    //   174	184	325	finally
    //   313	323	325	finally
    //   323	325	325	finally
    //   357	367	325	finally
    //   371	378	325	finally
    //   382	389	325	finally
    //   393	400	325	finally
    //   357	367	370	android/database/sqlite/SQLiteException
    //   174	184	381	android/database/sqlite/SQLiteException
    //   313	323	392	android/database/sqlite/SQLiteException
  }

  // ERROR //
  private void c(List<x> paramList, int paramInt)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aload_0
    //   4: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   7: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   10: astore_3
    //   11: iconst_1
    //   12: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   15: astore 5
    //   17: iload_2
    //   18: invokestatic 159	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   21: astore 6
    //   23: aload_3
    //   24: ldc 236
    //   26: aconst_null
    //   27: ldc_w 419
    //   30: iconst_1
    //   31: anewarray 346	java/lang/String
    //   34: dup
    //   35: iconst_0
    //   36: aload 5
    //   38: aastore
    //   39: aconst_null
    //   40: aconst_null
    //   41: ldc_w 421
    //   44: aload 6
    //   46: invokevirtual 424	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   49: astore_3
    //   50: aload_3
    //   51: invokeinterface 336 1 0
    //   56: ifeq +76 -> 132
    //   59: aload_1
    //   60: new 202	com/tencent/stat/x
    //   63: dup
    //   64: aload_3
    //   65: iconst_0
    //   66: invokeinterface 428 2 0
    //   71: aload_3
    //   72: iconst_1
    //   73: invokeinterface 431 2 0
    //   78: invokestatic 433	com/tencent/stat/common/k:d	(Ljava/lang/String;)Ljava/lang/String;
    //   81: aload_3
    //   82: iconst_2
    //   83: invokeinterface 340 2 0
    //   88: aload_3
    //   89: iconst_3
    //   90: invokeinterface 340 2 0
    //   95: invokespecial 436	com/tencent/stat/x:<init>	(JLjava/lang/String;II)V
    //   98: invokeinterface 209 2 0
    //   103: pop
    //   104: goto -54 -> 50
    //   107: astore 4
    //   109: aload_3
    //   110: astore_1
    //   111: aload 4
    //   113: astore_3
    //   114: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   117: aload_3
    //   118: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   121: aload_1
    //   122: ifnull +9 -> 131
    //   125: aload_1
    //   126: invokeinterface 355 1 0
    //   131: return
    //   132: aload_3
    //   133: ifnull -2 -> 131
    //   136: aload_3
    //   137: invokeinterface 355 1 0
    //   142: return
    //   143: astore_1
    //   144: aload 4
    //   146: astore_3
    //   147: aload_3
    //   148: ifnull +9 -> 157
    //   151: aload_3
    //   152: invokeinterface 355 1 0
    //   157: aload_1
    //   158: athrow
    //   159: astore_1
    //   160: goto -13 -> 147
    //   163: astore 4
    //   165: aload_1
    //   166: astore_3
    //   167: aload 4
    //   169: astore_1
    //   170: goto -23 -> 147
    //   173: astore_3
    //   174: aconst_null
    //   175: astore_1
    //   176: goto -62 -> 114
    //
    // Exception table:
    //   from	to	target	type
    //   50	104	107	java/lang/Throwable
    //   3	50	143	finally
    //   50	104	159	finally
    //   114	121	163	finally
    //   3	50	173	java/lang/Throwable
  }

  private void e()
  {
    try
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("status", Integer.valueOf(1));
      this.d.getWritableDatabase().update("events", localContentValues, "status=?", new String[] { Long.toString(2L) });
      this.b = (int)DatabaseUtils.queryNumEntries(this.d.getReadableDatabase(), "events");
      e.i("Total " + this.b + " unsent events.");
      return;
    }
    catch (Throwable localThrowable)
    {
      e.e(localThrowable);
    }
  }

  // ERROR //
  private void f()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   4: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   7: ldc_w 442
    //   10: aconst_null
    //   11: aconst_null
    //   12: aconst_null
    //   13: aconst_null
    //   14: aconst_null
    //   15: aconst_null
    //   16: invokevirtual 331	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   19: astore_2
    //   20: aload_2
    //   21: astore_1
    //   22: aload_2
    //   23: invokeinterface 336 1 0
    //   28: ifeq +51 -> 79
    //   31: aload_2
    //   32: astore_1
    //   33: aload_0
    //   34: getfield 48	com/tencent/stat/n:g	Ljava/util/HashMap;
    //   37: aload_2
    //   38: iconst_0
    //   39: invokeinterface 431 2 0
    //   44: aload_2
    //   45: iconst_1
    //   46: invokeinterface 431 2 0
    //   51: invokevirtual 445	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   54: pop
    //   55: goto -35 -> 20
    //   58: astore_3
    //   59: aload_2
    //   60: astore_1
    //   61: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   64: aload_3
    //   65: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   68: aload_2
    //   69: ifnull +9 -> 78
    //   72: aload_2
    //   73: invokeinterface 355 1 0
    //   78: return
    //   79: aload_2
    //   80: ifnull -2 -> 78
    //   83: aload_2
    //   84: invokeinterface 355 1 0
    //   89: return
    //   90: astore_2
    //   91: aconst_null
    //   92: astore_1
    //   93: aload_1
    //   94: ifnull +9 -> 103
    //   97: aload_1
    //   98: invokeinterface 355 1 0
    //   103: aload_2
    //   104: athrow
    //   105: astore_2
    //   106: goto -13 -> 93
    //   109: astore_3
    //   110: aconst_null
    //   111: astore_2
    //   112: goto -53 -> 59
    //
    // Exception table:
    //   from	to	target	type
    //   22	31	58	java/lang/Throwable
    //   33	55	58	java/lang/Throwable
    //   0	20	90	finally
    //   22	31	105	finally
    //   33	55	105	finally
    //   61	68	105	finally
    //   0	20	109	java/lang/Throwable
  }

  public int a()
  {
    return this.b;
  }

  void a(int paramInt)
  {
    this.a.post(new v(this, paramInt));
  }

  void a(e parame, c paramc)
  {
    if (!StatConfig.isEnableStatService())
      return;
    try
    {
      if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId())
      {
        b(parame, paramc);
        return;
      }
    }
    catch (Throwable parame)
    {
      e.e(parame);
      return;
    }
    this.a.post(new r(this, parame, paramc));
  }

  void a(b paramb)
  {
    if (paramb == null)
      return;
    this.a.post(new s(this, paramb));
  }

  void a(List<x> paramList)
  {
    try
    {
      if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId())
      {
        b(paramList);
        return;
      }
      this.a.post(new q(this, paramList));
      return;
    }
    catch (android.database.sqlite.SQLiteException paramList)
    {
      e.e(paramList);
    }
  }

  void a(List<x> paramList, int paramInt)
  {
    try
    {
      if (Thread.currentThread().getId() == this.a.getLooper().getThread().getId())
      {
        b(paramList, paramInt);
        return;
      }
      this.a.post(new p(this, paramList, paramInt));
      return;
    }
    catch (Throwable paramList)
    {
      e.e(paramList);
    }
  }

  // ERROR //
  public DeviceInfo b(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 43	com/tencent/stat/n:c	Lcom/tencent/stat/DeviceInfo;
    //   6: ifnull +12 -> 18
    //   9: aload_0
    //   10: getfield 43	com/tencent/stat/n:c	Lcom/tencent/stat/DeviceInfo;
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: aload_0
    //   19: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   22: invokevirtual 108	com/tencent/stat/w:getReadableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   25: ldc_w 478
    //   28: aconst_null
    //   29: aconst_null
    //   30: aconst_null
    //   31: aconst_null
    //   32: aconst_null
    //   33: aconst_null
    //   34: aconst_null
    //   35: invokevirtual 424	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   38: astore_3
    //   39: iconst_0
    //   40: istore 9
    //   42: aload_3
    //   43: invokeinterface 336 1 0
    //   48: ifeq +353 -> 401
    //   51: aload_3
    //   52: iconst_0
    //   53: invokeinterface 431 2 0
    //   58: astore 8
    //   60: aload 8
    //   62: invokestatic 433	com/tencent/stat/common/k:d	(Ljava/lang/String;)Ljava/lang/String;
    //   65: astore 4
    //   67: aload_3
    //   68: iconst_1
    //   69: invokeinterface 340 2 0
    //   74: istore 12
    //   76: aload_3
    //   77: iconst_2
    //   78: invokeinterface 431 2 0
    //   83: astore_2
    //   84: aload_3
    //   85: iconst_3
    //   86: invokeinterface 428 2 0
    //   91: lstore 13
    //   93: invokestatic 483	java/lang/System:currentTimeMillis	()J
    //   96: ldc2_w 484
    //   99: ldiv
    //   100: lstore 15
    //   102: iload 12
    //   104: iconst_1
    //   105: if_icmpeq +668 -> 773
    //   108: lload 13
    //   110: ldc2_w 484
    //   113: lmul
    //   114: invokestatic 487	com/tencent/stat/common/k:a	(J)Ljava/lang/String;
    //   117: ldc2_w 484
    //   120: lload 15
    //   122: lmul
    //   123: invokestatic 487	com/tencent/stat/common/k:a	(J)Ljava/lang/String;
    //   126: invokevirtual 490	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   129: ifne +644 -> 773
    //   132: iconst_1
    //   133: istore 9
    //   135: aload_2
    //   136: aload_1
    //   137: invokestatic 494	com/tencent/stat/common/k:r	(Landroid/content/Context;)Ljava/lang/String;
    //   140: invokevirtual 490	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   143: ifne +623 -> 766
    //   146: iload 9
    //   148: iconst_2
    //   149: ior
    //   150: istore 10
    //   152: aload 4
    //   154: ldc_w 496
    //   157: invokevirtual 500	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   160: astore 6
    //   162: aload 6
    //   164: ifnull +414 -> 578
    //   167: aload 6
    //   169: arraylength
    //   170: ifle +408 -> 578
    //   173: aload 6
    //   175: iconst_0
    //   176: aaload
    //   177: astore_2
    //   178: aload_2
    //   179: ifnull +12 -> 191
    //   182: aload_2
    //   183: invokevirtual 503	java/lang/String:length	()I
    //   186: bipush 11
    //   188: if_icmpge +562 -> 750
    //   191: aload_1
    //   192: invokestatic 506	com/tencent/stat/common/k:l	(Landroid/content/Context;)Ljava/lang/String;
    //   195: astore 5
    //   197: aload 5
    //   199: ifnull +545 -> 744
    //   202: aload 5
    //   204: invokevirtual 503	java/lang/String:length	()I
    //   207: bipush 10
    //   209: if_icmple +535 -> 744
    //   212: iconst_1
    //   213: istore 9
    //   215: aload 5
    //   217: astore_2
    //   218: goto +562 -> 780
    //   221: aload 6
    //   223: ifnull +369 -> 592
    //   226: aload 6
    //   228: arraylength
    //   229: iconst_2
    //   230: if_icmplt +362 -> 592
    //   233: aload 6
    //   235: iconst_1
    //   236: aaload
    //   237: astore 6
    //   239: new 60	java/lang/StringBuilder
    //   242: dup
    //   243: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   246: aload 4
    //   248: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: ldc_w 496
    //   254: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: aload 6
    //   259: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   265: astore 5
    //   267: iload 9
    //   269: istore 11
    //   271: aload_0
    //   272: new 508	com/tencent/stat/DeviceInfo
    //   275: dup
    //   276: aload 4
    //   278: aload 6
    //   280: iload 10
    //   282: invokespecial 511	com/tencent/stat/DeviceInfo:<init>	(Ljava/lang/String;Ljava/lang/String;I)V
    //   285: putfield 43	com/tencent/stat/n:c	Lcom/tencent/stat/DeviceInfo;
    //   288: new 244	android/content/ContentValues
    //   291: dup
    //   292: invokespecial 245	android/content/ContentValues:<init>	()V
    //   295: astore_2
    //   296: aload_2
    //   297: ldc_w 513
    //   300: aload 5
    //   302: invokestatic 252	com/tencent/stat/common/k:c	(Ljava/lang/String;)Ljava/lang/String;
    //   305: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   308: aload_2
    //   309: ldc_w 515
    //   312: iload 10
    //   314: invokestatic 322	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   317: invokevirtual 325	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   320: aload_2
    //   321: ldc_w 517
    //   324: aload_1
    //   325: invokestatic 494	com/tencent/stat/common/k:r	(Landroid/content/Context;)Ljava/lang/String;
    //   328: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   331: aload_2
    //   332: ldc_w 519
    //   335: lload 15
    //   337: invokestatic 275	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   340: invokevirtual 278	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   343: iload 11
    //   345: ifeq +30 -> 375
    //   348: aload_0
    //   349: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   352: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   355: ldc_w 478
    //   358: aload_2
    //   359: ldc_w 521
    //   362: iconst_1
    //   363: anewarray 346	java/lang/String
    //   366: dup
    //   367: iconst_0
    //   368: aload 8
    //   370: aastore
    //   371: invokevirtual 350	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   374: pop
    //   375: iload 10
    //   377: iload 12
    //   379: if_icmpeq +359 -> 738
    //   382: aload_0
    //   383: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   386: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   389: ldc_w 478
    //   392: aconst_null
    //   393: aload_2
    //   394: invokevirtual 524	android/database/sqlite/SQLiteDatabase:replace	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   397: pop2
    //   398: iconst_1
    //   399: istore 9
    //   401: iload 9
    //   403: ifne +157 -> 560
    //   406: aload_1
    //   407: invokestatic 526	com/tencent/stat/common/k:b	(Landroid/content/Context;)Ljava/lang/String;
    //   410: astore 4
    //   412: aload_1
    //   413: invokestatic 528	com/tencent/stat/common/k:c	(Landroid/content/Context;)Ljava/lang/String;
    //   416: astore 5
    //   418: aload 5
    //   420: ifnull +312 -> 732
    //   423: aload 5
    //   425: invokevirtual 503	java/lang/String:length	()I
    //   428: ifle +304 -> 732
    //   431: new 60	java/lang/StringBuilder
    //   434: dup
    //   435: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   438: aload 4
    //   440: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   443: ldc_w 496
    //   446: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   449: aload 5
    //   451: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   454: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   457: astore_2
    //   458: invokestatic 483	java/lang/System:currentTimeMillis	()J
    //   461: ldc2_w 484
    //   464: ldiv
    //   465: lstore 13
    //   467: aload_1
    //   468: invokestatic 494	com/tencent/stat/common/k:r	(Landroid/content/Context;)Ljava/lang/String;
    //   471: astore_1
    //   472: new 244	android/content/ContentValues
    //   475: dup
    //   476: invokespecial 245	android/content/ContentValues:<init>	()V
    //   479: astore 6
    //   481: aload 6
    //   483: ldc_w 513
    //   486: aload_2
    //   487: invokestatic 252	com/tencent/stat/common/k:c	(Ljava/lang/String;)Ljava/lang/String;
    //   490: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   493: aload 6
    //   495: ldc_w 515
    //   498: iconst_0
    //   499: invokestatic 322	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   502: invokevirtual 325	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   505: aload 6
    //   507: ldc_w 517
    //   510: aload_1
    //   511: invokevirtual 258	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   514: aload 6
    //   516: ldc_w 519
    //   519: lload 13
    //   521: invokestatic 275	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   524: invokevirtual 278	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   527: aload_0
    //   528: getfield 101	com/tencent/stat/n:d	Lcom/tencent/stat/w;
    //   531: invokevirtual 105	com/tencent/stat/w:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   534: ldc_w 478
    //   537: aconst_null
    //   538: aload 6
    //   540: invokevirtual 282	android/database/sqlite/SQLiteDatabase:insert	(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;)J
    //   543: pop2
    //   544: aload_0
    //   545: new 508	com/tencent/stat/DeviceInfo
    //   548: dup
    //   549: aload 4
    //   551: aload 5
    //   553: iconst_0
    //   554: invokespecial 511	com/tencent/stat/DeviceInfo:<init>	(Ljava/lang/String;Ljava/lang/String;I)V
    //   557: putfield 43	com/tencent/stat/n:c	Lcom/tencent/stat/DeviceInfo;
    //   560: aload_3
    //   561: ifnull +9 -> 570
    //   564: aload_3
    //   565: invokeinterface 355 1 0
    //   570: aload_0
    //   571: getfield 43	com/tencent/stat/n:c	Lcom/tencent/stat/DeviceInfo;
    //   574: astore_1
    //   575: goto -561 -> 14
    //   578: aload_1
    //   579: invokestatic 526	com/tencent/stat/common/k:b	(Landroid/content/Context;)Ljava/lang/String;
    //   582: astore_2
    //   583: aload_2
    //   584: astore 4
    //   586: iconst_1
    //   587: istore 9
    //   589: goto -368 -> 221
    //   592: aload_1
    //   593: invokestatic 528	com/tencent/stat/common/k:c	(Landroid/content/Context;)Ljava/lang/String;
    //   596: astore 7
    //   598: aload 7
    //   600: astore 6
    //   602: iload 9
    //   604: istore 11
    //   606: aload_2
    //   607: astore 5
    //   609: aload 7
    //   611: ifnull -340 -> 271
    //   614: aload 7
    //   616: astore 6
    //   618: iload 9
    //   620: istore 11
    //   622: aload_2
    //   623: astore 5
    //   625: aload 7
    //   627: invokevirtual 503	java/lang/String:length	()I
    //   630: ifle -359 -> 271
    //   633: new 60	java/lang/StringBuilder
    //   636: dup
    //   637: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   640: aload 4
    //   642: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   645: ldc_w 496
    //   648: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   651: aload 7
    //   653: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   656: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   659: astore 5
    //   661: iconst_1
    //   662: istore 11
    //   664: aload 7
    //   666: astore 6
    //   668: goto -397 -> 271
    //   671: astore_2
    //   672: aconst_null
    //   673: astore_1
    //   674: getstatic 28	com/tencent/stat/n:e	Lcom/tencent/stat/common/StatLogger;
    //   677: aload_2
    //   678: invokevirtual 126	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   681: aload_1
    //   682: ifnull -112 -> 570
    //   685: aload_1
    //   686: invokeinterface 355 1 0
    //   691: goto -121 -> 570
    //   694: astore_1
    //   695: aload_0
    //   696: monitorexit
    //   697: aload_1
    //   698: athrow
    //   699: astore_1
    //   700: aconst_null
    //   701: astore_3
    //   702: aload_3
    //   703: ifnull +9 -> 712
    //   706: aload_3
    //   707: invokeinterface 355 1 0
    //   712: aload_1
    //   713: athrow
    //   714: astore_1
    //   715: goto -13 -> 702
    //   718: astore_2
    //   719: aload_1
    //   720: astore_3
    //   721: aload_2
    //   722: astore_1
    //   723: goto -21 -> 702
    //   726: astore_2
    //   727: aload_3
    //   728: astore_1
    //   729: goto -55 -> 674
    //   732: aload 4
    //   734: astore_2
    //   735: goto -277 -> 458
    //   738: iconst_1
    //   739: istore 9
    //   741: goto -340 -> 401
    //   744: iconst_0
    //   745: istore 9
    //   747: goto +33 -> 780
    //   750: iconst_0
    //   751: istore 9
    //   753: aload_2
    //   754: astore 5
    //   756: aload 4
    //   758: astore_2
    //   759: aload 5
    //   761: astore 4
    //   763: goto -542 -> 221
    //   766: iload 9
    //   768: istore 10
    //   770: goto -618 -> 152
    //   773: iload 12
    //   775: istore 9
    //   777: goto -642 -> 135
    //   780: aload_2
    //   781: astore 5
    //   783: aload 4
    //   785: astore_2
    //   786: aload 5
    //   788: astore 4
    //   790: goto -569 -> 221
    //
    // Exception table:
    //   from	to	target	type
    //   18	39	671	java/lang/Throwable
    //   2	14	694	finally
    //   564	570	694	finally
    //   570	575	694	finally
    //   685	691	694	finally
    //   706	712	694	finally
    //   712	714	694	finally
    //   18	39	699	finally
    //   42	102	714	finally
    //   108	132	714	finally
    //   135	146	714	finally
    //   152	162	714	finally
    //   167	173	714	finally
    //   182	191	714	finally
    //   191	197	714	finally
    //   202	212	714	finally
    //   226	233	714	finally
    //   239	267	714	finally
    //   271	343	714	finally
    //   348	375	714	finally
    //   382	398	714	finally
    //   406	418	714	finally
    //   423	458	714	finally
    //   458	560	714	finally
    //   578	583	714	finally
    //   592	598	714	finally
    //   625	661	714	finally
    //   674	681	718	finally
    //   42	102	726	java/lang/Throwable
    //   108	132	726	java/lang/Throwable
    //   135	146	726	java/lang/Throwable
    //   152	162	726	java/lang/Throwable
    //   167	173	726	java/lang/Throwable
    //   182	191	726	java/lang/Throwable
    //   191	197	726	java/lang/Throwable
    //   202	212	726	java/lang/Throwable
    //   226	233	726	java/lang/Throwable
    //   239	267	726	java/lang/Throwable
    //   271	343	726	java/lang/Throwable
    //   348	375	726	java/lang/Throwable
    //   382	398	726	java/lang/Throwable
    //   406	418	726	java/lang/Throwable
    //   423	458	726	java/lang/Throwable
    //   458	560	726	java/lang/Throwable
    //   578	583	726	java/lang/Throwable
    //   592	598	726	java/lang/Throwable
    //   625	661	726	java/lang/Throwable
  }

  void c()
  {
    this.a.post(new t(this));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.n
 * JD-Core Version:    0.6.0
 */