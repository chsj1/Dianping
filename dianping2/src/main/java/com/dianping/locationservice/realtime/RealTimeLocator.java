package com.dianping.locationservice.realtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import com.dianping.locationservice.impl286.dpgeo.DPGeoListener;
import com.dianping.locationservice.impl286.dpgeo.DPGeoServiceImpl;
import com.dianping.locationservice.impl286.geo.GeoListener;
import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.scan.CellScanner;
import com.dianping.locationservice.impl286.scan.WifiScanner;
import com.dianping.model.GPSCoordinate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RealTimeLocator
  implements DPGeoListener, GeoListener
{
  private static final int CELL_SCAN_COUNT = 3;
  private static final int CELL_SCAN_TIMEOUT = 400;
  private static final String DATA_KEY = "data";
  private static volatile RealTimeLocator INSTANCE = null;
  public static final String LOG_HIT_CACHE = "hit_cache";
  public static final String LOG_LOCATE_EXCEPTION = "loc_ex";
  public static final String LOG_LOCATE_FAILED = "loc_fail";
  public static final String LOG_LOCATE_FINISH = "loc_fin";
  public static final String LOG_LOCATE_PROGRESS = "loc_p";
  public static final String LOG_MISS_CACHE = "miss_cache";
  public static final String LOG_SCAN_FAILED = "scan_fail";
  public static final String LOG_TURN_OFF = "turn_off";
  public static final String PERSISTENT_COORD_SPLITTER = ",";
  public static final String PERSISTENT_ITEM_SPLITTER = "|,|,|";
  public static final String PERSISTENT_ITEM_SPLITTER_PATTERN = "\\|,\\|,\\|";
  public static final String PERSISTENT_KV_SPLITTER = "|";
  private static final String SHARED_PREFERENCE_KEY = "real_time_locate";
  private static final int WIFI_SCAN_TIMEOUT = 500;
  private Context context;
  private DpIdSupplier dpIdSupplier;
  private volatile LocationIdentifier lastLocationIdentifier;
  private volatile long lastQueryAPITime = System.currentTimeMillis();
  private RunMode runMode;
  private final AtomicBoolean started = new AtomicBoolean();
  private volatile Set<RealTimeLocateListener> waitingListenerSet = Collections.newSetFromMap(new ConcurrentHashMap());

  // ERROR //
  public static RealTimeLocator getInstance(DpIdSupplier paramDpIdSupplier, Context paramContext, RunMode paramRunMode)
  {
    // Byte code:
    //   0: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   3: ifnonnull +57 -> 60
    //   6: ldc 2
    //   8: monitorenter
    //   9: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   12: ifnonnull +45 -> 57
    //   15: new 2	com/dianping/locationservice/realtime/RealTimeLocator
    //   18: dup
    //   19: invokespecial 126	com/dianping/locationservice/realtime/RealTimeLocator:<init>	()V
    //   22: putstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   25: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   28: aload_1
    //   29: putfield 115	com/dianping/locationservice/realtime/RealTimeLocator:context	Landroid/content/Context;
    //   32: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   35: aload_2
    //   36: putfield 128	com/dianping/locationservice/realtime/RealTimeLocator:runMode	Lcom/dianping/locationservice/realtime/RunMode;
    //   39: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   42: aload_0
    //   43: putfield 130	com/dianping/locationservice/realtime/RealTimeLocator:dpIdSupplier	Lcom/dianping/locationservice/realtime/DpIdSupplier;
    //   46: aload_1
    //   47: invokestatic 135	com/dianping/locationservice/impl286/geo/GeoServiceImpl:getInstance	(Landroid/content/Context;)Lcom/dianping/locationservice/impl286/geo/GeoServiceImpl;
    //   50: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   53: invokevirtual 139	com/dianping/locationservice/impl286/geo/GeoServiceImpl:addListener	(Lcom/dianping/locationservice/impl286/geo/GeoListener;)Z
    //   56: pop
    //   57: ldc 2
    //   59: monitorexit
    //   60: getstatic 83	com/dianping/locationservice/realtime/RealTimeLocator:INSTANCE	Lcom/dianping/locationservice/realtime/RealTimeLocator;
    //   63: areturn
    //   64: astore_0
    //   65: ldc 2
    //   67: monitorexit
    //   68: aload_0
    //   69: athrow
    //   70: astore_0
    //   71: goto -14 -> 57
    //
    // Exception table:
    //   from	to	target	type
    //   9	25	64	finally
    //   25	57	64	finally
    //   57	60	64	finally
    //   65	68	64	finally
    //   25	57	70	java/lang/Throwable
  }

  @TargetApi(11)
  private SharedPreferences getSharedPreferences()
  {
    if (Build.VERSION.SDK_INT >= 11)
      return this.context.getSharedPreferences("real_time_locate", 4);
    return this.context.getSharedPreferences("real_time_locate", 0);
  }

  public Map<LocationIdentifier, GPSCoordinate> getLocationIdCoordMapFromPersistence()
  {
    HashMap localHashMap = new HashMap();
    Object localObject = getSharedPreferences().getString("data", null);
    int j;
    int i;
    if (localObject != null)
    {
      RealTimeLocateLog.d("get cache, " + (String)localObject);
      localObject = ((String)localObject).split("\\|,\\|,\\|");
      j = localObject.length;
      i = 0;
    }
    while (true)
    {
      if (i < j)
      {
        String str = localObject[i];
        try
        {
          int k = str.lastIndexOf("|");
          if (k != -1)
          {
            LocationIdentifier localLocationIdentifier = new LocationIdentifier(str.substring(0, k));
            String[] arrayOfString = str.substring(k + 1).split(",");
            if (arrayOfString.length == 2)
            {
              localHashMap.put(localLocationIdentifier, new GPSCoordinate(Double.parseDouble(arrayOfString[0]), Double.parseDouble(arrayOfString[1])));
              break label266;
            }
            RealTimeLocateLog.e("invalid persistent location coordinate, " + str);
          }
        }
        catch (Throwable localThrowable)
        {
          RealTimeLocateLog.e("invalid persistent location, " + str, localThrowable);
        }
        RealTimeLocateLog.e("invalid persistent location, " + str);
      }
      else
      {
        RealTimeLocateLog.d("get cache, size: " + localHashMap.size());
        return localHashMap;
      }
      label266: i += 1;
    }
  }

  public void onLocateFail()
  {
    try
    {
      RealTimeLocateLog.w("locate.bin failed");
      replyListeners(new RealTimeLocateResult(RealTimeLocateSettings.get(this.dpIdSupplier, this.context), "loc_fail"));
      return;
    }
    catch (Throwable localThrowable)
    {
    }
  }

  // ERROR //
  public void onLocateFinish(com.dianping.archive.DPObject paramDPObject)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 130	com/dianping/locationservice/realtime/RealTimeLocator:dpIdSupplier	Lcom/dianping/locationservice/realtime/DpIdSupplier;
    //   4: aload_0
    //   5: getfield 115	com/dianping/locationservice/realtime/RealTimeLocator:context	Landroid/content/Context;
    //   8: invokestatic 260	com/dianping/locationservice/realtime/RealTimeLocateSettings:get	(Lcom/dianping/locationservice/realtime/DpIdSupplier;Landroid/content/Context;)Lcom/dianping/locationservice/realtime/RealTimeLocateSettings;
    //   11: astore_2
    //   12: aload_2
    //   13: bipush 32
    //   15: invokevirtual 273	com/dianping/locationservice/realtime/RealTimeLocateSettings:hasAnyFlag	(I)Z
    //   18: ifeq +18 -> 36
    //   21: aload_0
    //   22: new 254	com/dianping/locationservice/realtime/RealTimeLocateResult
    //   25: dup
    //   26: aload_2
    //   27: ldc 47
    //   29: invokespecial 263	com/dianping/locationservice/realtime/RealTimeLocateResult:<init>	(Lcom/dianping/locationservice/realtime/RealTimeLocateSettings;Ljava/lang/String;)V
    //   32: invokevirtual 267	com/dianping/locationservice/realtime/RealTimeLocator:replyListeners	(Lcom/dianping/locationservice/realtime/RealTimeLocateResult;)V
    //   35: return
    //   36: aload_1
    //   37: getstatic 279	com/dianping/model/Location:DECODER	Lcom/dianping/archive/DecodingFactory;
    //   40: invokevirtual 285	com/dianping/archive/DPObject:decodeToObject	(Lcom/dianping/archive/DecodingFactory;)Ljava/lang/Object;
    //   43: checkcast 275	com/dianping/model/Location
    //   46: astore_1
    //   47: new 210	com/dianping/model/GPSCoordinate
    //   50: dup
    //   51: aload_1
    //   52: invokevirtual 289	com/dianping/model/Location:latitude	()D
    //   55: aload_1
    //   56: invokevirtual 292	com/dianping/model/Location:longitude	()D
    //   59: invokespecial 219	com/dianping/model/GPSCoordinate:<init>	(DD)V
    //   62: astore_1
    //   63: aload_0
    //   64: getfield 294	com/dianping/locationservice/realtime/RealTimeLocator:lastLocationIdentifier	Lcom/dianping/locationservice/realtime/LocationIdentifier;
    //   67: astore_3
    //   68: new 170	java/lang/StringBuilder
    //   71: dup
    //   72: invokespecial 171	java/lang/StringBuilder:<init>	()V
    //   75: ldc_w 296
    //   78: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: aload_3
    //   82: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   85: ldc_w 301
    //   88: invokevirtual 177	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: aload_1
    //   92: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   95: invokevirtual 181	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   98: invokestatic 304	com/dianping/locationservice/realtime/RealTimeLocateLog:i	(Ljava/lang/String;)V
    //   101: aload_3
    //   102: ifnull +26 -> 128
    //   105: aload_0
    //   106: invokevirtual 306	com/dianping/locationservice/realtime/RealTimeLocator:getLocationIdCoordMapFromPersistence	()Ljava/util/Map;
    //   109: astore 4
    //   111: aload 4
    //   113: aload_3
    //   114: aload_1
    //   115: invokeinterface 225 3 0
    //   120: pop
    //   121: aload_0
    //   122: aload 4
    //   124: aload_2
    //   125: invokevirtual 310	com/dianping/locationservice/realtime/RealTimeLocator:saveLocationIdCoordMapToPersistence	(Ljava/util/Map;Lcom/dianping/locationservice/realtime/RealTimeLocateSettings;)V
    //   128: aload_0
    //   129: new 254	com/dianping/locationservice/realtime/RealTimeLocateResult
    //   132: dup
    //   133: aload_2
    //   134: aload_3
    //   135: aload_1
    //   136: ldc 35
    //   138: invokespecial 313	com/dianping/locationservice/realtime/RealTimeLocateResult:<init>	(Lcom/dianping/locationservice/realtime/RealTimeLocateSettings;Lcom/dianping/locationservice/realtime/LocationIdentifier;Lcom/dianping/model/GPSCoordinate;Ljava/lang/String;)V
    //   141: invokevirtual 267	com/dianping/locationservice/realtime/RealTimeLocator:replyListeners	(Lcom/dianping/locationservice/realtime/RealTimeLocateResult;)V
    //   144: return
    //   145: astore_1
    //   146: ldc_w 315
    //   149: aload_1
    //   150: invokestatic 235	com/dianping/locationservice/realtime/RealTimeLocateLog:e	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   153: aload_0
    //   154: new 254	com/dianping/locationservice/realtime/RealTimeLocateResult
    //   157: dup
    //   158: aload_2
    //   159: ldc 29
    //   161: aload_1
    //   162: invokespecial 318	com/dianping/locationservice/realtime/RealTimeLocateResult:<init>	(Lcom/dianping/locationservice/realtime/RealTimeLocateSettings;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   165: invokevirtual 267	com/dianping/locationservice/realtime/RealTimeLocator:replyListeners	(Lcom/dianping/locationservice/realtime/RealTimeLocateResult;)V
    //   168: return
    //   169: astore 4
    //   171: ldc_w 320
    //   174: aload 4
    //   176: invokestatic 235	com/dianping/locationservice/realtime/RealTimeLocateLog:e	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   179: goto -51 -> 128
    //   182: astore_1
    //   183: return
    //
    // Exception table:
    //   from	to	target	type
    //   36	63	145	java/lang/Throwable
    //   105	128	169	java/lang/Throwable
    //   0	35	182	java/lang/Throwable
    //   63	101	182	java/lang/Throwable
    //   128	144	182	java/lang/Throwable
    //   146	168	182	java/lang/Throwable
    //   171	179	182	java/lang/Throwable
  }

  public void onRequestGeoParamsFinish(Map<String, String> paramMap)
  {
    try
    {
      this.lastLocationIdentifier = new LocationIdentifier(paramMap);
      return;
    }
    catch (Throwable paramMap)
    {
      RealTimeLocateLog.e("deal scan result failed, runMode:" + this.runMode, paramMap);
    }
  }

  protected void replyListeners(RealTimeLocateResult paramRealTimeLocateResult)
  {
    try
    {
      Object localObject = this.waitingListenerSet;
      this.waitingListenerSet = Collections.newSetFromMap(new ConcurrentHashMap());
      localObject = ((Set)localObject).iterator();
      while (((Iterator)localObject).hasNext())
        ((RealTimeLocateListener)((Iterator)localObject).next()).onRealTimeLocateFinish(paramRealTimeLocateResult);
    }
    catch (Throwable paramRealTimeLocateResult)
    {
      RealTimeLocateLog.e("reply listener failed", paramRealTimeLocateResult);
    }
  }

  public void saveLocationIdCoordMapToPersistence(Map<LocationIdentifier, GPSCoordinate> paramMap, RealTimeLocateSettings paramRealTimeLocateSettings)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    PriorityQueue localPriorityQueue = new PriorityQueue(paramMap.size(), LocationIdentifier.createDateComparator);
    localPriorityQueue.addAll(paramMap.keySet());
    int i = 0;
    while (true)
    {
      LocationIdentifier localLocationIdentifier = (LocationIdentifier)localPriorityQueue.poll();
      if (localLocationIdentifier != null)
      {
        i += 1;
        if ((i <= paramRealTimeLocateSettings.maxCount) && (!localLocationIdentifier.expired(paramRealTimeLocateSettings)));
      }
      else
      {
        if (localStringBuilder.length() > 0)
          localStringBuilder.delete(localStringBuilder.length() - "|,|,|".length(), localStringBuilder.length());
        RealTimeLocateLog.d("save cache, size: " + paramMap.size() + ", " + localStringBuilder.toString());
        getSharedPreferences().edit().putString("data", localStringBuilder.toString()).commit();
        return;
      }
      GPSCoordinate localGPSCoordinate = (GPSCoordinate)paramMap.get(localLocationIdentifier);
      localStringBuilder.append(localLocationIdentifier).append("|").append(localGPSCoordinate.latitudeString()).append(",").append(localGPSCoordinate.longitudeString()).append("|,|,|");
    }
  }

  public void start()
  {
    try
    {
      if (this.started.compareAndSet(false, true))
        DPGeoServiceImpl.getInstance().addListener(this);
      return;
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void tryLocate(RealTimeLocateListener paramRealTimeLocateListener)
  {
    try
    {
      RealTimeLocateSettings localRealTimeLocateSettings = RealTimeLocateSettings.get(this.dpIdSupplier, this.context);
      if (localRealTimeLocateSettings.hasAnyFlag(32))
      {
        paramRealTimeLocateListener.onRealTimeLocateFinish(new RealTimeLocateResult(localRealTimeLocateSettings, "turn_off"));
        return;
      }
      if ((localRealTimeLocateSettings.hasAnyFlag(64)) && (System.currentTimeMillis() - this.lastQueryAPITime > localRealTimeLocateSettings.minAPILocateInterval))
      {
        RealTimeLocateLog.i("request locate.bin");
        this.waitingListenerSet.add(paramRealTimeLocateListener);
        this.lastQueryAPITime = System.currentTimeMillis();
        paramRealTimeLocateListener.onRealTimeLocateFinish(new RealTimeLocateResult(localRealTimeLocateSettings, "loc_p"));
        DPGeoServiceImpl.getInstance().start();
        return;
      }
    }
    catch (Throwable localRealTimeLocateResult)
    {
      if (paramRealTimeLocateListener != null)
      {
        RealTimeLocateResult localRealTimeLocateResult = new RealTimeLocateResult();
        localRealTimeLocateResult.setLog("scan_fail");
        paramRealTimeLocateListener.onRealTimeLocateFinish(localRealTimeLocateResult);
        return;
        RealTimeLocateLog.i("try locate");
        start();
        ExecutorService localExecutorService = Executors.newCachedThreadPool();
        localExecutorService.submit(new Runnable(localExecutorService, localRealTimeLocateResult, paramRealTimeLocateListener)
        {
          public void run()
          {
            Object localObject1 = this.val$executorService.submit(new Callable()
            {
              public List<CellModel> call()
                throws Exception
              {
                Object localObject1 = null;
                int i = 1;
                while (true)
                {
                  Object localObject2 = localObject1;
                  if (i <= 3)
                  {
                    RealTimeLocateLog.i("cell scan count: " + i + "/" + 3);
                    localObject2 = RealTimeLocator.1.this.val$executorService.submit(new CellScanner(RealTimeLocator.this.context));
                  }
                  try
                  {
                    localObject2 = (List)((Future)localObject2).get(400L, TimeUnit.MILLISECONDS);
                    if (localObject2 == null)
                    {
                      localObject1 = localObject2;
                    }
                    else
                    {
                      localObject1 = localObject2;
                      boolean bool = ((List)localObject2).isEmpty();
                      localObject1 = localObject2;
                      if (!bool)
                        return localObject2;
                    }
                  }
                  catch (Exception localException)
                  {
                    RealTimeLocateLog.e("cell scan failed, " + i + "/" + 3, localException);
                    i += 1;
                  }
                }
              }
            });
            Object localObject3 = this.val$executorService.submit(new WifiScanner(RealTimeLocator.this.context));
            try
            {
              localLocationIdentifier = new LocationIdentifier((List)((Future)localObject1).get(1600L, TimeUnit.MILLISECONDS), (List)((Future)localObject3).get(500L, TimeUnit.MILLISECONDS));
              RealTimeLocateLog.d("scan finish, " + localLocationIdentifier);
              localObject3 = null;
              localObject1 = null;
              int j;
              int i;
              Iterator localIterator;
              if (((this.val$settings.hasAnyFlag(2)) || (localLocationIdentifier.hasCell())) && ((this.val$settings.hasAnyFlag(4)) || (localLocationIdentifier.hasWifi())))
              {
                localObject3 = RealTimeLocator.this.getLocationIdCoordMapFromPersistence();
                j = 2147483647;
                i = 2147483647;
                localIterator = ((Map)localObject3).entrySet().iterator();
              }
              while (true)
              {
                localObject3 = localObject1;
                if (!localIterator.hasNext())
                  break;
                localObject3 = (Map.Entry)localIterator.next();
                if (((LocationIdentifier)((Map.Entry)localObject3).getKey()).expired(this.val$settings))
                  continue;
                int m = localLocationIdentifier.cellDifferenceFrom((LocationIdentifier)((Map.Entry)localObject3).getKey(), this.val$settings);
                int k = localLocationIdentifier.wifiDifferenceFrom((LocationIdentifier)((Map.Entry)localObject3).getKey(), this.val$settings);
                RealTimeLocateLog.d("difference " + m + "," + k + " from " + ((Map.Entry)localObject3).getKey());
                if (m < j)
                {
                  j = m;
                  i = k;
                  localObject1 = (GPSCoordinate)((Map.Entry)localObject3).getValue();
                  continue;
                }
                if ((j != m) || (k >= i))
                  continue;
                i = k;
                localObject1 = (GPSCoordinate)((Map.Entry)localObject3).getValue();
                continue;
                RealTimeLocateLog.i("invalid scan deal to without cell/wifi");
              }
              if ((localObject3 == null) && (System.currentTimeMillis() - RealTimeLocator.this.lastQueryAPITime > this.val$settings.minAPILocateInterval))
              {
                RealTimeLocateLog.i("request locate.bin");
                RealTimeLocator.this.waitingListenerSet.add(this.val$listener);
                RealTimeLocator.access$102(RealTimeLocator.this, System.currentTimeMillis());
                this.val$listener.onRealTimeLocateFinish(new RealTimeLocateResult(this.val$settings, "loc_p"));
                DPGeoServiceImpl.getInstance().start();
              }
              while (true)
              {
                return;
                if (localObject3 == null)
                  break;
                RealTimeLocateLog.i("hit local cache, " + localLocationIdentifier + ", " + localObject3);
                localObject1 = "hit_cache";
                this.val$listener.onRealTimeLocateFinish(new RealTimeLocateResult(this.val$settings, localLocationIdentifier, (GPSCoordinate)localObject3, (String)localObject1));
              }
            }
            catch (Throwable str)
            {
              while (true)
              {
                LocationIdentifier localLocationIdentifier;
                RealTimeLocateLog.e("scan failed", localThrowable);
                this.val$listener.onRealTimeLocateFinish(new RealTimeLocateResult(this.val$settings, "scan_fail", localThrowable));
                return;
                RealTimeLocateLog.i("miss local cache, " + localLocationIdentifier);
                String str = "miss_cache";
              }
            }
            finally
            {
              this.val$executorService.shutdown();
            }
            throw localObject2;
          }
        });
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.realtime.RealTimeLocator
 * JD-Core Version:    0.6.0
 */