package com.tencent.stat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;

class w extends SQLiteOpenHelper
{
  public w(Context paramContext)
  {
    super(paramContext, k.v(paramContext), null, 3);
  }

  // ERROR //
  private void a(SQLiteDatabase paramSQLiteDatabase)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aload_1
    //   4: ldc 22
    //   6: aconst_null
    //   7: aconst_null
    //   8: aconst_null
    //   9: aconst_null
    //   10: aconst_null
    //   11: aconst_null
    //   12: invokevirtual 28	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   15: astore_3
    //   16: aload_3
    //   17: astore_2
    //   18: new 30	android/content/ContentValues
    //   21: dup
    //   22: invokespecial 33	android/content/ContentValues:<init>	()V
    //   25: astore 5
    //   27: aload_3
    //   28: astore_2
    //   29: aload_3
    //   30: invokeinterface 39 1 0
    //   35: ifeq +58 -> 93
    //   38: aload_3
    //   39: astore_2
    //   40: aload_3
    //   41: iconst_0
    //   42: invokeinterface 43 2 0
    //   47: astore 4
    //   49: aload_3
    //   50: astore_2
    //   51: aload_3
    //   52: iconst_1
    //   53: invokeinterface 47 2 0
    //   58: pop
    //   59: aload_3
    //   60: astore_2
    //   61: aload_3
    //   62: iconst_2
    //   63: invokeinterface 43 2 0
    //   68: pop
    //   69: aload_3
    //   70: astore_2
    //   71: aload_3
    //   72: iconst_3
    //   73: invokeinterface 51 2 0
    //   78: pop2
    //   79: aload_3
    //   80: astore_2
    //   81: aload 5
    //   83: ldc 53
    //   85: aload 4
    //   87: invokestatic 57	com/tencent/stat/common/k:c	(Ljava/lang/String;)Ljava/lang/String;
    //   90: invokevirtual 61	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   93: aload 4
    //   95: ifnull +25 -> 120
    //   98: aload_3
    //   99: astore_2
    //   100: aload_1
    //   101: ldc 22
    //   103: aload 5
    //   105: ldc 63
    //   107: iconst_1
    //   108: anewarray 65	java/lang/String
    //   111: dup
    //   112: iconst_0
    //   113: aload 4
    //   115: aastore
    //   116: invokevirtual 69	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   119: pop
    //   120: aload_3
    //   121: ifnull +9 -> 130
    //   124: aload_3
    //   125: invokeinterface 72 1 0
    //   130: return
    //   131: astore_1
    //   132: aconst_null
    //   133: astore_3
    //   134: aload_3
    //   135: astore_2
    //   136: invokestatic 78	com/tencent/stat/n:d	()Lcom/tencent/stat/common/StatLogger;
    //   139: aload_1
    //   140: invokevirtual 84	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   143: aload_3
    //   144: ifnull -14 -> 130
    //   147: aload_3
    //   148: invokeinterface 72 1 0
    //   153: return
    //   154: astore_1
    //   155: aconst_null
    //   156: astore_2
    //   157: aload_2
    //   158: ifnull +9 -> 167
    //   161: aload_2
    //   162: invokeinterface 72 1 0
    //   167: aload_1
    //   168: athrow
    //   169: astore_1
    //   170: goto -13 -> 157
    //   173: astore_1
    //   174: goto -40 -> 134
    //
    // Exception table:
    //   from	to	target	type
    //   3	16	131	java/lang/Throwable
    //   3	16	154	finally
    //   18	27	169	finally
    //   29	38	169	finally
    //   40	49	169	finally
    //   51	59	169	finally
    //   61	69	169	finally
    //   71	79	169	finally
    //   81	93	169	finally
    //   100	120	169	finally
    //   136	143	169	finally
    //   18	27	173	java/lang/Throwable
    //   29	38	173	java/lang/Throwable
    //   40	49	173	java/lang/Throwable
    //   51	59	173	java/lang/Throwable
    //   61	69	173	java/lang/Throwable
    //   71	79	173	java/lang/Throwable
    //   81	93	173	java/lang/Throwable
    //   100	120	173	java/lang/Throwable
  }

  // ERROR //
  private void b(SQLiteDatabase paramSQLiteDatabase)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 87
    //   3: aconst_null
    //   4: aconst_null
    //   5: aconst_null
    //   6: aconst_null
    //   7: aconst_null
    //   8: aconst_null
    //   9: invokevirtual 28	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   12: astore_2
    //   13: new 89	java/util/ArrayList
    //   16: dup
    //   17: invokespecial 90	java/util/ArrayList:<init>	()V
    //   20: astore 4
    //   22: aload_2
    //   23: invokeinterface 39 1 0
    //   28: ifeq +72 -> 100
    //   31: aload 4
    //   33: new 92	com/tencent/stat/x
    //   36: dup
    //   37: aload_2
    //   38: iconst_0
    //   39: invokeinterface 51 2 0
    //   44: aload_2
    //   45: iconst_1
    //   46: invokeinterface 43 2 0
    //   51: aload_2
    //   52: iconst_2
    //   53: invokeinterface 47 2 0
    //   58: aload_2
    //   59: iconst_3
    //   60: invokeinterface 47 2 0
    //   65: invokespecial 95	com/tencent/stat/x:<init>	(JLjava/lang/String;II)V
    //   68: invokeinterface 101 2 0
    //   73: pop
    //   74: goto -52 -> 22
    //   77: astore_3
    //   78: aload_2
    //   79: astore_1
    //   80: aload_3
    //   81: astore_2
    //   82: invokestatic 78	com/tencent/stat/n:d	()Lcom/tencent/stat/common/StatLogger;
    //   85: aload_2
    //   86: invokevirtual 84	com/tencent/stat/common/StatLogger:e	(Ljava/lang/Object;)V
    //   89: aload_1
    //   90: ifnull +9 -> 99
    //   93: aload_1
    //   94: invokeinterface 72 1 0
    //   99: return
    //   100: new 30	android/content/ContentValues
    //   103: dup
    //   104: invokespecial 33	android/content/ContentValues:<init>	()V
    //   107: astore_3
    //   108: aload 4
    //   110: invokeinterface 105 1 0
    //   115: astore 4
    //   117: aload 4
    //   119: invokeinterface 110 1 0
    //   124: ifeq +70 -> 194
    //   127: aload 4
    //   129: invokeinterface 114 1 0
    //   134: checkcast 92	com/tencent/stat/x
    //   137: astore 5
    //   139: aload_3
    //   140: ldc 116
    //   142: aload 5
    //   144: getfield 119	com/tencent/stat/x:b	Ljava/lang/String;
    //   147: invokestatic 57	com/tencent/stat/common/k:c	(Ljava/lang/String;)Ljava/lang/String;
    //   150: invokevirtual 61	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   153: aload_1
    //   154: ldc 87
    //   156: aload_3
    //   157: ldc 121
    //   159: iconst_1
    //   160: anewarray 65	java/lang/String
    //   163: dup
    //   164: iconst_0
    //   165: aload 5
    //   167: getfield 124	com/tencent/stat/x:a	J
    //   170: invokestatic 130	java/lang/Long:toString	(J)Ljava/lang/String;
    //   173: aastore
    //   174: invokevirtual 69	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   177: pop
    //   178: goto -61 -> 117
    //   181: astore_1
    //   182: aload_2
    //   183: ifnull +9 -> 192
    //   186: aload_2
    //   187: invokeinterface 72 1 0
    //   192: aload_1
    //   193: athrow
    //   194: aload_2
    //   195: ifnull -96 -> 99
    //   198: aload_2
    //   199: invokeinterface 72 1 0
    //   204: return
    //   205: astore_1
    //   206: aconst_null
    //   207: astore_2
    //   208: goto -26 -> 182
    //   211: astore_3
    //   212: aload_1
    //   213: astore_2
    //   214: aload_3
    //   215: astore_1
    //   216: goto -34 -> 182
    //   219: astore_2
    //   220: aconst_null
    //   221: astore_1
    //   222: goto -140 -> 82
    //
    // Exception table:
    //   from	to	target	type
    //   13	22	77	java/lang/Throwable
    //   22	74	77	java/lang/Throwable
    //   100	117	77	java/lang/Throwable
    //   117	178	77	java/lang/Throwable
    //   13	22	181	finally
    //   22	74	181	finally
    //   100	117	181	finally
    //   117	178	181	finally
    //   0	13	205	finally
    //   82	89	211	finally
    //   0	13	219	java/lang/Throwable
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table if not exists events(event_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content TEXT, status INTEGER, send_count INTEGER, timestamp LONG)");
    paramSQLiteDatabase.execSQL("create table if not exists user(uid TEXT PRIMARY KEY, user_type INTEGER, app_ver TEXT, ts INTEGER)");
    paramSQLiteDatabase.execSQL("create table if not exists config(type INTEGER PRIMARY KEY NOT NULL, content TEXT, md5sum TEXT, version INTEGER)");
    paramSQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT NULL, value TEXT)");
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    n.d().debug("upgrade DB from oldVersion " + paramInt1 + " to newVersion " + paramInt2);
    if (paramInt1 == 1)
    {
      paramSQLiteDatabase.execSQL("create table if not exists keyvalues(key TEXT PRIMARY KEY NOT NULL, value TEXT)");
      a(paramSQLiteDatabase);
      b(paramSQLiteDatabase);
    }
    if (paramInt1 == 2)
    {
      a(paramSQLiteDatabase);
      b(paramSQLiteDatabase);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.w
 * JD-Core Version:    0.6.0
 */