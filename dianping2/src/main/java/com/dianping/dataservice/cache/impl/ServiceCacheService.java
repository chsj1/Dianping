package com.dianping.dataservice.cache.impl;

import B;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.os.Looper;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.monitor.MonitorService;
import com.dianping.util.Daemon;
import com.dianping.util.DataCursor;
import com.dianping.util.Log;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class ServiceCacheService
  implements CacheService
{
  private static final String TAG = "cache";
  private final String c;
  private final String cacheName;
  private final AtomicInteger count = new AtomicInteger();
  private SQLiteDatabase db;
  private ServiceCacheService.Statement deleteStat;
  private final Handler dhandler = new ServiceCacheService.2(this, Daemon.looper());
  private boolean drain;
  private HttpService httpService;
  private int iHeader;
  private int iKey;
  private int iTime;
  private int iVal;
  private DatabaseUtils.InsertHelper insertHelper;
  private final byte[] k;
  private final Handler mhandler = new ServiceCacheService.1(this, Looper.getMainLooper());
  private MonitorService monitor;
  private final AtomicInteger ops;
  private ServiceCacheService.Statement queryTimeStat;
  private final ConcurrentHashMap<Request, ServiceCacheService.Session> runningSession = new ConcurrentHashMap();
  private ServiceCacheService.Statement touchStat;
  private ServiceCacheService.Statement updateStat;
  private final byte[] v;

  public ServiceCacheService(HttpService paramHttpService, MonitorService paramMonitorService, SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    this.db = paramSQLiteDatabase;
    this.cacheName = paramString;
    this.ops = new AtomicInteger();
    this.c = new String(new char[] { 65, 69, 83, 47, 67, 66, 67, 47, 78, 111, 80, 97, 100, 100, 105, 110, 103 });
    Object localObject = new byte[16];
    Object tmp197_195 = localObject;
    tmp197_195[0] = 92;
    Object tmp202_197 = tmp197_195;
    tmp202_197[1] = 115;
    Object tmp207_202 = tmp202_197;
    tmp207_202[2] = 116;
    Object tmp212_207 = tmp207_202;
    tmp212_207[3] = 117;
    Object tmp217_212 = tmp212_207;
    tmp217_212[4] = 112;
    Object tmp222_217 = tmp217_212;
    tmp222_217[5] = 113;
    Object tmp227_222 = tmp222_217;
    tmp227_222[6] = 6;
    Object tmp233_227 = tmp227_222;
    tmp233_227[7] = 112;
    Object tmp239_233 = tmp233_227;
    tmp239_233[8] = 112;
    Object tmp245_239 = tmp239_233;
    tmp245_239[9] = 3;
    Object tmp251_245 = tmp245_239;
    tmp251_245[10] = 3;
    Object tmp257_251 = tmp251_245;
    tmp257_251[11] = 4;
    Object tmp263_257 = tmp257_251;
    tmp263_257[12] = 6;
    Object tmp269_263 = tmp263_257;
    tmp269_263[13] = 118;
    Object tmp275_269 = tmp269_263;
    tmp275_269[14] = 0;
    Object tmp281_275 = tmp275_269;
    tmp281_275[15] = 112;
    tmp281_275;
    int m = 24;
    int j = 0;
    int i;
    while (j < localObject.length)
    {
      i = (byte)(localObject[j] & 0xFF ^ m);
      localObject[j] = i;
      m = i;
      j += 1;
    }
    this.k = ((B)localObject);
    localObject = new byte[16];
    Object tmp352_350 = localObject;
    tmp352_350[0] = 0;
    Object tmp357_352 = tmp352_350;
    tmp357_352[1] = 118;
    Object tmp362_357 = tmp357_352;
    tmp362_357[2] = 122;
    Object tmp367_362 = tmp362_357;
    tmp367_362[3] = 10;
    Object tmp372_367 = tmp367_362;
    tmp372_367[4] = 3;
    Object tmp377_372 = tmp372_367;
    tmp377_372[5] = 116;
    Object tmp382_377 = tmp377_372;
    tmp382_377[6] = 124;
    Object tmp388_382 = tmp382_377;
    tmp388_382[7] = 10;
    Object tmp394_388 = tmp388_382;
    tmp394_388[8] = 5;
    Object tmp400_394 = tmp394_388;
    tmp400_394[9] = 117;
    Object tmp406_400 = tmp400_394;
    tmp406_400[10] = 6;
    Object tmp412_406 = tmp406_400;
    tmp412_406[11] = 5;
    Object tmp418_412 = tmp412_406;
    tmp418_412[12] = 3;
    Object tmp424_418 = tmp418_412;
    tmp424_418[13] = 4;
    Object tmp430_424 = tmp424_418;
    tmp430_424[14] = 2;
    Object tmp436_430 = tmp430_424;
    tmp436_430[15] = 37;
    tmp436_430;
    m = 97;
    j = localObject.length - 1;
    while (j >= 0)
    {
      i = (byte)(localObject[j] & 0xFF ^ m);
      localObject[j] = i;
      m = i;
      j -= 1;
    }
    this.v = ((B)localObject);
    if (paramSQLiteDatabase == null)
      return;
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("CREATE TABLE IF NOT EXISTS ").append(paramString).append(" (");
    ((StringBuilder)localObject).append("K TEXT PRIMARY KEY, ");
    ((StringBuilder)localObject).append("T INT8, ");
    ((StringBuilder)localObject).append("H TEXT, ");
    ((StringBuilder)localObject).append("V BLOB);");
    paramSQLiteDatabase.execSQL(((StringBuilder)localObject).toString());
    localObject = paramSQLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + paramString, null);
    try
    {
      if (((Cursor)localObject).moveToFirst())
        this.count.set(((Cursor)localObject).getInt(0));
      ((Cursor)localObject).close();
      this.queryTimeStat = new ServiceCacheService.Statement(this, "SELECT T FROM " + paramString + " WHERE K=?");
      this.deleteStat = new ServiceCacheService.Statement(this, "DELETE FROM " + paramString + " WHERE K=?");
      this.touchStat = new ServiceCacheService.Statement(this, "UPDATE " + paramString + " SET T=? WHERE K=?");
      this.updateStat = new ServiceCacheService.Statement(this, "UPDATE " + paramString + " SET T=?,H=?,V=? WHERE K=?");
      this.insertHelper = new DatabaseUtils.InsertHelper(paramSQLiteDatabase, paramString);
      this.iKey = this.insertHelper.getColumnIndex("K");
      this.iTime = this.insertHelper.getColumnIndex("T");
      this.iHeader = this.insertHelper.getColumnIndex("H");
      this.iVal = this.insertHelper.getColumnIndex("V");
      this.httpService = paramHttpService;
      this.monitor = paramMonitorService;
      return;
    }
    finally
    {
      ((Cursor)localObject).close();
    }
    throw paramHttpService;
  }

  private String getHeaderValue(List<NameValuePair> paramList, String paramString1, String paramString2)
  {
    Object localObject = paramString2;
    if (paramList != null)
    {
      localObject = paramString2;
      if (paramString1 != null)
      {
        paramList = paramList.iterator();
        while (true)
        {
          localObject = paramString2;
          if (!paramList.hasNext())
            break;
          localObject = (NameValuePair)paramList.next();
          if (!paramString1.equalsIgnoreCase(((NameValuePair)localObject).getName()))
            continue;
          localObject = ((NameValuePair)localObject).getValue();
        }
      }
    }
    return (String)localObject;
  }

  private String headerListToJsonString(List<NameValuePair> paramList)
  {
    if (paramList == null)
      return null;
    JSONObject localJSONObject = new JSONObject();
    try
    {
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        NameValuePair localNameValuePair = (NameValuePair)paramList.next();
        localJSONObject.put(localNameValuePair.getName().toLowerCase(), localNameValuePair.getValue());
      }
    }
    catch (org.json.JSONException paramList)
    {
    }
    return localJSONObject.toString();
  }

  private HttpService httpService()
  {
    return this.httpService;
  }

  private void release(SQLiteDatabase arg1)
  {
    synchronized (this.ops)
    {
      this.ops.decrementAndGet();
      return;
    }
  }

  private SQLiteDatabase retain()
  {
    synchronized (this.ops)
    {
      if (this.drain)
        return null;
      this.ops.incrementAndGet();
      SQLiteDatabase localSQLiteDatabase = this.db;
      return localSQLiteDatabase;
    }
  }

  public void abort(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler, boolean paramBoolean)
  {
    ServiceCacheService.Session localSession = (ServiceCacheService.Session)this.runningSession.get(paramHttpRequest);
    if ((localSession != null) && (localSession.handler == paramRequestHandler))
      this.runningSession.remove(paramHttpRequest, localSession);
  }

  // ERROR //
  public void clear()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 348	com/dianping/dataservice/cache/impl/ServiceCacheService:retain	()Landroid/database/sqlite/SQLiteDatabase;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnonnull +4 -> 10
    //   9: return
    //   10: aload_1
    //   11: aload_0
    //   12: getfield 90	com/dianping/dataservice/cache/impl/ServiceCacheService:cacheName	Ljava/lang/String;
    //   15: ldc_w 350
    //   18: aconst_null
    //   19: invokevirtual 354	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   22: pop
    //   23: aload_0
    //   24: getfield 58	com/dianping/dataservice/cache/impl/ServiceCacheService:count	Ljava/util/concurrent/atomic/AtomicInteger;
    //   27: iconst_0
    //   28: invokevirtual 183	java/util/concurrent/atomic/AtomicInteger:set	(I)V
    //   31: aload_0
    //   32: aload_1
    //   33: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   36: return
    //   37: astore_2
    //   38: aload_0
    //   39: aload_1
    //   40: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   43: return
    //   44: astore_2
    //   45: aload_0
    //   46: aload_1
    //   47: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   50: aload_2
    //   51: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   10	31	37	java/lang/Exception
    //   10	31	44	finally
  }

  // ERROR //
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 92	com/dianping/dataservice/cache/impl/ServiceCacheService:ops	Ljava/util/concurrent/atomic/AtomicInteger;
    //   6: astore_1
    //   7: aload_1
    //   8: monitorenter
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield 320	com/dianping/dataservice/cache/impl/ServiceCacheService:drain	Z
    //   14: aload_1
    //   15: monitorexit
    //   16: aload_0
    //   17: getfield 92	com/dianping/dataservice/cache/impl/ServiceCacheService:ops	Ljava/util/concurrent/atomic/AtomicInteger;
    //   20: invokevirtual 358	java/util/concurrent/atomic/AtomicInteger:get	()I
    //   23: ifle +19 -> 42
    //   26: invokestatic 363	java/lang/Thread:yield	()V
    //   29: goto -13 -> 16
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    //   37: astore_2
    //   38: aload_1
    //   39: monitorexit
    //   40: aload_2
    //   41: athrow
    //   42: aload_0
    //   43: getfield 320	com/dianping/dataservice/cache/impl/ServiceCacheService:drain	Z
    //   46: ifne +12 -> 58
    //   49: aload_0
    //   50: getfield 88	com/dianping/dataservice/cache/impl/ServiceCacheService:db	Landroid/database/sqlite/SQLiteDatabase;
    //   53: astore_1
    //   54: aload_1
    //   55: ifnonnull +6 -> 61
    //   58: aload_0
    //   59: monitorexit
    //   60: return
    //   61: aload_0
    //   62: getfield 216	com/dianping/dataservice/cache/impl/ServiceCacheService:insertHelper	Landroid/database/DatabaseUtils$InsertHelper;
    //   65: invokevirtual 364	android/database/DatabaseUtils$InsertHelper:close	()V
    //   68: aload_0
    //   69: getfield 195	com/dianping/dataservice/cache/impl/ServiceCacheService:queryTimeStat	Lcom/dianping/dataservice/cache/impl/ServiceCacheService$Statement;
    //   72: invokevirtual 365	com/dianping/dataservice/cache/impl/ServiceCacheService$Statement:close	()V
    //   75: aload_0
    //   76: getfield 199	com/dianping/dataservice/cache/impl/ServiceCacheService:deleteStat	Lcom/dianping/dataservice/cache/impl/ServiceCacheService$Statement;
    //   79: invokevirtual 365	com/dianping/dataservice/cache/impl/ServiceCacheService$Statement:close	()V
    //   82: aload_0
    //   83: getfield 205	com/dianping/dataservice/cache/impl/ServiceCacheService:touchStat	Lcom/dianping/dataservice/cache/impl/ServiceCacheService$Statement;
    //   86: invokevirtual 365	com/dianping/dataservice/cache/impl/ServiceCacheService$Statement:close	()V
    //   89: aload_0
    //   90: getfield 209	com/dianping/dataservice/cache/impl/ServiceCacheService:updateStat	Lcom/dianping/dataservice/cache/impl/ServiceCacheService$Statement;
    //   93: invokevirtual 365	com/dianping/dataservice/cache/impl/ServiceCacheService$Statement:close	()V
    //   96: aload_0
    //   97: getfield 88	com/dianping/dataservice/cache/impl/ServiceCacheService:db	Landroid/database/sqlite/SQLiteDatabase;
    //   100: invokevirtual 366	android/database/sqlite/SQLiteDatabase:close	()V
    //   103: aload_0
    //   104: aconst_null
    //   105: putfield 88	com/dianping/dataservice/cache/impl/ServiceCacheService:db	Landroid/database/sqlite/SQLiteDatabase;
    //   108: goto -50 -> 58
    //   111: astore_1
    //   112: goto -9 -> 103
    //
    // Exception table:
    //   from	to	target	type
    //   2	9	32	finally
    //   16	29	32	finally
    //   40	42	32	finally
    //   42	54	32	finally
    //   61	103	32	finally
    //   103	108	32	finally
    //   9	16	37	finally
    //   38	40	37	finally
    //   61	103	111	java/lang/Exception
  }

  public int count()
  {
    return this.count.get();
  }

  public void exec(HttpRequest paramHttpRequest, RequestHandler<HttpRequest, HttpResponse> paramRequestHandler)
  {
    ServiceCacheService.Session localSession = new ServiceCacheService.Session(paramHttpRequest, paramRequestHandler);
    if ((ServiceCacheService.Session)this.runningSession.putIfAbsent(paramHttpRequest, localSession) == null)
    {
      if ((paramRequestHandler instanceof FullRequestHandle))
        ((FullRequestHandle)paramRequestHandler).onRequestStart(paramHttpRequest);
      this.dhandler.sendMessage(this.dhandler.obtainMessage(0, localSession));
      return;
    }
    Log.e("cache", "cannot exec duplicate request (same instance)");
  }

  public BasicCacheResponse execSync(HttpRequest paramHttpRequest)
  {
    SQLiteDatabase localSQLiteDatabase = retain();
    if (localSQLiteDatabase == null)
      return new BasicCacheResponse(0L, null, null, "db closed");
    try
    {
      Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT T,V,H FROM " + this.cacheName + " WHERE K=\"" + paramHttpRequest.url() + "\"", null);
      if (localCursor.moveToFirst())
      {
        long l = localCursor.getLong(0);
        paramHttpRequest = localCursor.getBlob(1);
        String str = localCursor.getString(2);
        localCursor.close();
        paramHttpRequest = new BasicCacheResponse(l, paramHttpRequest, str, null);
        return paramHttpRequest;
      }
      localCursor.close();
      paramHttpRequest = new BasicCacheResponse(0L, null, null, "not found: " + paramHttpRequest.url());
      return paramHttpRequest;
    }
    catch (java.lang.Exception paramHttpRequest)
    {
      paramHttpRequest = new BasicCacheResponse(0L, null, null, paramHttpRequest);
      return paramHttpRequest;
    }
    finally
    {
      release(localSQLiteDatabase);
    }
    throw paramHttpRequest;
  }

  public DataCursor<String> getIteratorByTime()
  {
    if ((this.drain) || (this.db == null))
      return DataCursor.EMPTY_CURSOR;
    String str = "SELECT K FROM " + this.cacheName + " ORDER BY T ASC";
    return new ServiceCacheService.3(this, this.db.rawQuery(str, null));
  }

  public long getTime(String paramString)
  {
    SQLiteDatabase localSQLiteDatabase = retain();
    if (localSQLiteDatabase == null)
      return -1L;
    Object localObject2 = null;
    Object localObject1 = null;
    try
    {
      SQLiteStatement localSQLiteStatement = this.queryTimeStat.create();
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      localSQLiteStatement.bindString(1, paramString);
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      long l = localSQLiteStatement.simpleQueryForLong();
      return l;
    }
    catch (java.lang.Exception paramString)
    {
      return -1L;
    }
    finally
    {
      if (localObject2 != null)
        this.queryTimeStat.dispose(localObject2);
      release(localSQLiteDatabase);
    }
    throw paramString;
  }

  public boolean insert(String paramString, HttpResponse paramHttpResponse, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = retain();
    if (localSQLiteDatabase == null)
      return false;
    DatabaseUtils.InsertHelper localInsertHelper = this.insertHelper;
    monitorenter;
    try
    {
      this.insertHelper.prepareForInsert();
      this.insertHelper.bind(this.iKey, paramString);
      this.insertHelper.bind(this.iTime, Long.valueOf(getHeaderValue(paramHttpResponse.headers(), "Cache-Policy", "0")).longValue() * 1000L + paramLong);
      this.insertHelper.bind(this.iHeader, headerListToJsonString(paramHttpResponse.headers()));
      this.insertHelper.bind(this.iVal, (byte[])(byte[])paramHttpResponse.result());
      paramLong = this.insertHelper.execute();
      if (paramLong < 0L);
      try
      {
        release(localSQLiteDatabase);
        return false;
        this.count.incrementAndGet();
        release(localSQLiteDatabase);
        return true;
      }
      finally
      {
        monitorexit;
      }
    }
    catch (java.lang.Exception paramString)
    {
      release(localSQLiteDatabase);
      monitorexit;
      return false;
    }
    finally
    {
      release(localSQLiteDatabase);
    }
    throw paramString;
  }

  public boolean put(Request paramRequest, HttpResponse paramHttpResponse, long paramLong)
  {
    return put(paramRequest.url(), paramHttpResponse, paramLong);
  }

  public boolean put(String paramString, HttpResponse paramHttpResponse, long paramLong)
  {
    Object localObject3 = paramHttpResponse.result();
    if ((localObject3 instanceof byte[]))
    {
      if (getTime(paramString) < 0L)
        return insert(paramString, paramHttpResponse, paramLong);
      SQLiteDatabase localSQLiteDatabase = retain();
      if (localSQLiteDatabase == null)
        return false;
      Object localObject2 = null;
      Object localObject1 = null;
      try
      {
        SQLiteStatement localSQLiteStatement = this.updateStat.create();
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        localSQLiteStatement.bindLong(1, Long.valueOf(getHeaderValue(paramHttpResponse.headers(), "Cache-Policy", "0")).longValue() * 1000L + paramLong);
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        localSQLiteStatement.bindString(2, headerListToJsonString(paramHttpResponse.headers()));
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        localSQLiteStatement.bindBlob(3, (byte[])(byte[])localObject3);
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        localSQLiteStatement.bindString(4, paramString);
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        paramLong = localSQLiteStatement.executeInsert();
        if (paramLong > 0L);
        for (int i = 1; ; i = 0)
          return i;
      }
      catch (java.lang.Exception paramString)
      {
        return false;
      }
      finally
      {
        if (localObject2 != null)
          this.updateStat.dispose(localObject2);
        release(localSQLiteDatabase);
      }
    }
    return false;
  }

  public void remove(Request paramRequest)
  {
    remove(paramRequest.url());
  }

  public void remove(String paramString)
  {
    SQLiteDatabase localSQLiteDatabase = retain();
    if (localSQLiteDatabase == null)
      return;
    Object localObject2 = null;
    Object localObject1 = null;
    try
    {
      SQLiteStatement localSQLiteStatement = this.deleteStat.create();
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      localSQLiteStatement.bindString(1, paramString);
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      if (localSQLiteStatement.executeInsert() > 0L)
      {
        localObject1 = localSQLiteStatement;
        localObject2 = localSQLiteStatement;
        this.count.decrementAndGet();
      }
      return;
    }
    catch (java.lang.Exception paramString)
    {
      return;
    }
    finally
    {
      if (localObject2 != null)
        this.deleteStat.dispose(localObject2);
      release(localSQLiteDatabase);
    }
    throw paramString;
  }

  public int runningCount()
  {
    return this.runningSession.size();
  }

  public boolean touch(Request paramRequest, long paramLong)
  {
    return touch(paramRequest.url(), paramLong);
  }

  public boolean touch(String paramString, long paramLong)
  {
    int i = 1;
    SQLiteDatabase localSQLiteDatabase = retain();
    if (localSQLiteDatabase == null)
      return false;
    Object localObject2 = null;
    Object localObject1 = null;
    try
    {
      SQLiteStatement localSQLiteStatement = this.touchStat.create();
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      localSQLiteStatement.bindLong(1, paramLong);
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      localSQLiteStatement.bindString(2, paramString);
      localObject1 = localSQLiteStatement;
      localObject2 = localSQLiteStatement;
      paramLong = localSQLiteStatement.executeInsert();
      if (paramLong > 0L);
      while (true)
      {
        return i;
        i = 0;
      }
    }
    catch (java.lang.Exception paramString)
    {
      return false;
    }
    finally
    {
      if (localObject2 != null)
        this.touchStat.dispose(localObject2);
      release(localSQLiteDatabase);
    }
    throw paramString;
  }

  // ERROR //
  public int trimToCount(int paramInt)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: invokespecial 348	com/dianping/dataservice/cache/impl/ServiceCacheService:retain	()Landroid/database/sqlite/SQLiteDatabase;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +10 -> 21
    //   14: iload 4
    //   16: istore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: iload_1
    //   20: ireturn
    //   21: aload_0
    //   22: invokevirtual 554	com/dianping/dataservice/cache/impl/ServiceCacheService:count	()I
    //   25: istore 5
    //   27: iload 5
    //   29: iload_1
    //   30: isub
    //   31: istore 5
    //   33: iload 4
    //   35: istore_1
    //   36: iload 5
    //   38: ifle -21 -> 17
    //   41: aload_2
    //   42: new 136	java/lang/StringBuilder
    //   45: dup
    //   46: invokespecial 137	java/lang/StringBuilder:<init>	()V
    //   49: ldc 188
    //   51: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: aload_0
    //   55: getfield 90	com/dianping/dataservice/cache/impl/ServiceCacheService:cacheName	Ljava/lang/String;
    //   58: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: ldc_w 556
    //   64: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: iload 5
    //   69: invokevirtual 559	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   72: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: aconst_null
    //   76: invokevirtual 169	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   79: astore_3
    //   80: aload_3
    //   81: invokeinterface 175 1 0
    //   86: ifeq +33 -> 119
    //   89: aload_3
    //   90: iconst_0
    //   91: invokeinterface 427 2 0
    //   96: lstore 6
    //   98: aload_3
    //   99: invokeinterface 186 1 0
    //   104: aload_0
    //   105: aload_2
    //   106: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   109: aload_0
    //   110: lload 6
    //   112: invokevirtual 563	com/dianping/dataservice/cache/impl/ServiceCacheService:trimToTime	(J)I
    //   115: istore_1
    //   116: goto -99 -> 17
    //   119: aload_3
    //   120: invokeinterface 186 1 0
    //   125: aload_0
    //   126: aload_2
    //   127: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   130: iload 4
    //   132: istore_1
    //   133: goto -116 -> 17
    //   136: astore_2
    //   137: aload_0
    //   138: monitorexit
    //   139: aload_2
    //   140: athrow
    //   141: astore_3
    //   142: aload_0
    //   143: aload_2
    //   144: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   147: iload 4
    //   149: istore_1
    //   150: goto -133 -> 17
    //   153: astore_3
    //   154: aload_0
    //   155: aload_2
    //   156: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   159: aload_3
    //   160: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   5	10	136	finally
    //   21	27	136	finally
    //   104	116	136	finally
    //   125	130	136	finally
    //   142	147	136	finally
    //   154	161	136	finally
    //   41	104	141	java/lang/Exception
    //   119	125	141	java/lang/Exception
    //   41	104	153	finally
    //   119	125	153	finally
  }

  // ERROR //
  public int trimToTime(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 348	com/dianping/dataservice/cache/impl/ServiceCacheService:retain	()Landroid/database/sqlite/SQLiteDatabase;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnonnull +11 -> 19
    //   11: iconst_0
    //   12: istore 5
    //   14: aload_0
    //   15: monitorexit
    //   16: iload 5
    //   18: ireturn
    //   19: aload_3
    //   20: aload_0
    //   21: getfield 90	com/dianping/dataservice/cache/impl/ServiceCacheService:cacheName	Ljava/lang/String;
    //   24: new 136	java/lang/StringBuilder
    //   27: dup
    //   28: invokespecial 137	java/lang/StringBuilder:<init>	()V
    //   31: ldc_w 565
    //   34: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: lload_1
    //   38: invokevirtual 568	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   41: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   44: aconst_null
    //   45: invokevirtual 354	android/database/sqlite/SQLiteDatabase:delete	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
    //   48: istore 5
    //   50: iload 5
    //   52: ifle +14 -> 66
    //   55: aload_0
    //   56: getfield 58	com/dianping/dataservice/cache/impl/ServiceCacheService:count	Ljava/util/concurrent/atomic/AtomicInteger;
    //   59: iload 5
    //   61: ineg
    //   62: invokevirtual 571	java/util/concurrent/atomic/AtomicInteger:addAndGet	(I)I
    //   65: pop
    //   66: aload_0
    //   67: aload_3
    //   68: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   71: goto -57 -> 14
    //   74: astore_3
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_3
    //   78: athrow
    //   79: astore 4
    //   81: aload_0
    //   82: aload_3
    //   83: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   86: iconst_0
    //   87: istore 5
    //   89: goto -75 -> 14
    //   92: astore 4
    //   94: aload_0
    //   95: aload_3
    //   96: invokespecial 356	com/dianping/dataservice/cache/impl/ServiceCacheService:release	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   99: aload 4
    //   101: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   2	7	74	finally
    //   66	71	74	finally
    //   81	86	74	finally
    //   94	102	74	finally
    //   19	50	79	java/lang/Exception
    //   55	66	79	java/lang/Exception
    //   19	50	92	finally
    //   55	66	92	finally
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.cache.impl.ServiceCacheService
 * JD-Core Version:    0.6.0
 */