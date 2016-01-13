package com.dianping.statistics.impl;

import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.concurrent.atomic.AtomicInteger;

class StatisticsDB
{
  private static final String FD_DATA = "raw";
  private static final String FD_TIME = "time";
  private static final String TABLE_NAME = "statistics";
  private final String[] _q_2 = { "_ROWID_", "raw" };
  protected final AtomicInteger count = new AtomicInteger();
  protected SQLiteDatabase db;
  private SQLiteStatement deleteStat;
  private int iData;
  private int iTime;
  private DatabaseUtils.InsertHelper insertHelper;

  public StatisticsDB(SQLiteDatabase paramSQLiteDatabase)
  {
    this.db = paramSQLiteDatabase;
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS statistics (time INT8, raw TEXT);");
    Cursor localCursor = paramSQLiteDatabase.rawQuery("SELECT COUNT(*) FROM statistics", null);
    try
    {
      if (localCursor.moveToFirst())
        this.count.set(localCursor.getInt(0));
      localCursor.close();
      this.insertHelper = new DatabaseUtils.InsertHelper(paramSQLiteDatabase, "statistics");
      this.iTime = this.insertHelper.getColumnIndex("time");
      this.iData = this.insertHelper.getColumnIndex("raw");
      this.deleteStat = paramSQLiteDatabase.compileStatement("DELETE FROM statistics WHERE _ROWID_=?");
      return;
    }
    finally
    {
      localCursor.close();
    }
    throw paramSQLiteDatabase;
  }

  public void close()
  {
    monitorenter;
    try
    {
      if (this.db != null)
      {
        this.db = null;
        this.insertHelper.close();
        this.insertHelper = null;
        this.deleteStat.close();
        this.deleteStat = null;
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public int count()
  {
    return this.count.get();
  }

  // ERROR //
  public void delete(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 45	com/dianping/statistics/impl/StatisticsDB:db	Landroid/database/sqlite/SQLiteDatabase;
    //   4: invokevirtual 113	android/database/sqlite/SQLiteDatabase:beginTransaction	()V
    //   7: aload_0
    //   8: getfield 99	com/dianping/statistics/impl/StatisticsDB:deleteStat	Landroid/database/sqlite/SQLiteStatement;
    //   11: astore 4
    //   13: aload 4
    //   15: monitorenter
    //   16: iconst_0
    //   17: istore 5
    //   19: iload 5
    //   21: iload_3
    //   22: if_icmpge +45 -> 67
    //   25: aload_1
    //   26: iload_2
    //   27: iload 5
    //   29: iadd
    //   30: iaload
    //   31: istore 6
    //   33: aload_0
    //   34: getfield 99	com/dianping/statistics/impl/StatisticsDB:deleteStat	Landroid/database/sqlite/SQLiteStatement;
    //   37: iconst_1
    //   38: iload 6
    //   40: i2l
    //   41: invokevirtual 117	android/database/sqlite/SQLiteStatement:bindLong	(IJ)V
    //   44: aload_0
    //   45: getfield 99	com/dianping/statistics/impl/StatisticsDB:deleteStat	Landroid/database/sqlite/SQLiteStatement;
    //   48: invokevirtual 121	android/database/sqlite/SQLiteStatement:executeInsert	()J
    //   51: lconst_0
    //   52: lcmp
    //   53: ifle +99 -> 152
    //   56: aload_0
    //   57: getfield 37	com/dianping/statistics/impl/StatisticsDB:count	Ljava/util/concurrent/atomic/AtomicInteger;
    //   60: invokevirtual 124	java/util/concurrent/atomic/AtomicInteger:decrementAndGet	()I
    //   63: pop
    //   64: goto +88 -> 152
    //   67: aload 4
    //   69: monitorexit
    //   70: aload_0
    //   71: getfield 45	com/dianping/statistics/impl/StatisticsDB:db	Landroid/database/sqlite/SQLiteDatabase;
    //   74: invokevirtual 127	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
    //   77: aload_0
    //   78: getfield 45	com/dianping/statistics/impl/StatisticsDB:db	Landroid/database/sqlite/SQLiteDatabase;
    //   81: ldc 55
    //   83: aconst_null
    //   84: invokevirtual 59	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   87: astore_1
    //   88: aload_1
    //   89: invokeinterface 65 1 0
    //   94: ifeq +17 -> 111
    //   97: aload_0
    //   98: getfield 37	com/dianping/statistics/impl/StatisticsDB:count	Ljava/util/concurrent/atomic/AtomicInteger;
    //   101: aload_1
    //   102: iconst_0
    //   103: invokeinterface 69 2 0
    //   108: invokevirtual 73	java/util/concurrent/atomic/AtomicInteger:set	(I)V
    //   111: aload_1
    //   112: invokeinterface 76 1 0
    //   117: aload_0
    //   118: getfield 45	com/dianping/statistics/impl/StatisticsDB:db	Landroid/database/sqlite/SQLiteDatabase;
    //   121: invokevirtual 130	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   124: return
    //   125: astore_1
    //   126: aload 4
    //   128: monitorexit
    //   129: aload_1
    //   130: athrow
    //   131: astore_1
    //   132: aload_0
    //   133: getfield 45	com/dianping/statistics/impl/StatisticsDB:db	Landroid/database/sqlite/SQLiteDatabase;
    //   136: invokevirtual 130	android/database/sqlite/SQLiteDatabase:endTransaction	()V
    //   139: aload_1
    //   140: athrow
    //   141: astore 4
    //   143: aload_1
    //   144: invokeinterface 76 1 0
    //   149: aload 4
    //   151: athrow
    //   152: iload 5
    //   154: iconst_1
    //   155: iadd
    //   156: istore 5
    //   158: goto -139 -> 19
    //
    // Exception table:
    //   from	to	target	type
    //   33	64	125	finally
    //   67	70	125	finally
    //   126	129	125	finally
    //   7	16	131	finally
    //   70	88	131	finally
    //   111	117	131	finally
    //   129	131	131	finally
    //   143	152	131	finally
    //   88	111	141	finally
  }

  public int[] getLastRows(int paramInt)
  {
    if (this.db == null)
      return null;
    int[] arrayOfInt = new int[paramInt];
    Cursor localCursor = this.db.query(false, "statistics", this._q_2, null, null, null, null, "time", String.valueOf(paramInt));
    paramInt = 0;
    int i = arrayOfInt.length;
    while (true)
    {
      if (paramInt < i);
      try
      {
        if (localCursor.moveToNext())
        {
          arrayOfInt[paramInt] = localCursor.getInt(0);
          paramInt += 1;
          continue;
        }
        return arrayOfInt;
      }
      finally
      {
        localCursor.close();
      }
    }
    throw localObject;
  }

  public long push(String paramString)
  {
    return push(paramString, System.currentTimeMillis());
  }

  public long push(String paramString, long paramLong)
  {
    if (this.db == null)
      return -1L;
    synchronized (this.insertHelper)
    {
      this.insertHelper.prepareForInsert();
      this.insertHelper.bind(this.iTime, paramLong);
      this.insertHelper.bind(this.iData, paramString);
      paramLong = this.insertHelper.execute();
      if (paramLong < 0L)
        return -1L;
      this.count.incrementAndGet();
      return paramLong;
    }
  }

  public int read(int[] paramArrayOfInt, String[] paramArrayOfString)
  {
    if (this.db == null)
      return 0;
    Cursor localCursor = this.db.query("statistics", this._q_2, null, null, null, null, "time");
    int i = 0;
    int j = paramArrayOfInt.length;
    while (true)
    {
      if (i < j);
      try
      {
        if (localCursor.moveToNext())
        {
          paramArrayOfInt[i] = localCursor.getInt(0);
          paramArrayOfString[i] = localCursor.getString(1);
          i += 1;
          continue;
        }
        return i;
      }
      finally
      {
        localCursor.close();
      }
    }
    throw paramArrayOfInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.StatisticsDB
 * JD-Core Version:    0.6.0
 */