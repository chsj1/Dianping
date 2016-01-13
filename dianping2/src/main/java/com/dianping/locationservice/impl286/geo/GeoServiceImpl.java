package com.dianping.locationservice.impl286.geo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dianping.locationservice.impl286.locate.BMapLocator;
import com.dianping.locationservice.impl286.locate.GpsLocator;
import com.dianping.locationservice.impl286.locate.LocateListener;
import com.dianping.locationservice.impl286.locate.Locator;
import com.dianping.locationservice.impl286.locate.NetworkLocator;
import com.dianping.locationservice.impl286.main.DPLocationService;
import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.model.CoordModel;
import com.dianping.locationservice.impl286.model.WifiModel;
import com.dianping.locationservice.impl286.scan.CellScanner;
import com.dianping.locationservice.impl286.scan.WifiScanner;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class GeoServiceImpl
  implements GeoService, LocateListener
{
  private static final int ASSIST_LOCATE_TIMEOUT = 1000;
  private static final int CELL_SCAN_COUNT = 3;
  private static final int CELL_SCAN_TIMEOUT = 400;
  private static final int MSG_REQUEST_GEO_PARAMS_FINISH = 1002;
  private static final int MSG_SCAN_FINISH = 1001;
  private static final int WIFI_SCAN_TIMEOUT = 500;
  private static GeoServiceImpl sInstance;
  private final CopyOnWriteArrayList<CellModel> mCellList = new CopyOnWriteArrayList();
  private Context mContext;
  private final CopyOnWriteArrayList<CoordModel> mCoordList = new CopyOnWriteArrayList();
  private final ConcurrentHashMap<String, String> mGeoParams = new ConcurrentHashMap();
  private final Locator mGpsLocator;
  private boolean mIsLocating = false;
  private final List<GeoListener> mListenerList = new ArrayList();
  private final LocTimeRecorder mLocTimeRecorder;
  private final Object mLock = new Object();
  private final Handler mMainHandler;
  private final Locator mNetworkLocator;
  private final TaskCounter mTaskCounter;
  private final CopyOnWriteArrayList<WifiModel> mWifiList = new CopyOnWriteArrayList();

  private GeoServiceImpl(Context paramContext)
  {
    this.mContext = paramContext;
    this.mGpsLocator = new GpsLocator(paramContext);
    this.mNetworkLocator = new NetworkLocator(paramContext);
    this.mMainHandler = new Handler(paramContext.getMainLooper())
    {
      public void handleMessage(Message arg1)
      {
        switch (???.what)
        {
        default:
          return;
        case 1001:
          GeoServiceImpl.this.onScanEnd();
          return;
        case 1002:
        }
        GeoServiceImpl.this.notifyListeners();
        synchronized (GeoServiceImpl.this.mLock)
        {
          GeoServiceImpl.access$302(GeoServiceImpl.this, false);
          return;
        }
      }
    };
    this.mTaskCounter = new TaskCounter();
    this.mLocTimeRecorder = new LocTimeRecorder();
  }

  private void assistLocate()
  {
    new Thread()
    {
      public void run()
      {
        ExecutorService localExecutorService = Executors.newSingleThreadExecutor();
        Object localObject = new FutureTask(new BMapLocator(GeoServiceImpl.this.mContext, GeoServiceImpl.this.mCellList, GeoServiceImpl.this.mWifiList));
        localExecutorService.submit((Runnable)localObject);
        try
        {
          localObject = (List)((FutureTask)localObject).get(1000L, TimeUnit.MILLISECONDS);
          if (localObject != null)
            GeoServiceImpl.this.mCoordList.addAllAbsent((Collection)localObject);
          localExecutorService.shutdown();
          GeoServiceImpl.this.mTaskCounter.removeTask();
          GeoServiceImpl.this.checkLocateEnd();
          return;
        }
        catch (Exception localException)
        {
          while (true)
            Log.e(localException.toString());
        }
      }
    }
    .start();
  }

  private void cellScan()
  {
    this.mLocTimeRecorder.initCellScanElapse();
    ExecutorService localExecutorService = Executors.newFixedThreadPool(3);
    int i = 1;
    while (true)
    {
      Object localObject;
      if (i <= 3)
      {
        Log.d("cell scan count: " + i + "/" + 3);
        localObject = new FutureTask(new CellScanner(this.mContext));
        localExecutorService.submit((Runnable)localObject);
      }
      try
      {
        localObject = (List)((FutureTask)localObject).get(400L, TimeUnit.MILLISECONDS);
        if ((localObject != null) && (!((List)localObject).isEmpty()))
        {
          this.mCellList.addAllAbsent((Collection)localObject);
          localExecutorService.shutdown();
          this.mTaskCounter.removeTask();
          this.mLocTimeRecorder.calCellScanElapse();
          checkScanEnd();
          return;
        }
      }
      catch (Exception localException)
      {
        Log.e(localException.toString());
        i += 1;
      }
    }
  }

  private void checkLocateEnd()
  {
    if (this.mTaskCounter.getTaskCount() == 0)
    {
      this.mLocTimeRecorder.calLocalLocElapse();
      onLocateEnd();
    }
  }

  private void checkScanEnd()
  {
    if (this.mTaskCounter.getTaskCount() == 3)
      this.mMainHandler.sendEmptyMessage(1001);
  }

  private void clearData()
  {
    this.mCellList.clear();
    this.mWifiList.clear();
    this.mCoordList.clear();
    this.mLocTimeRecorder.resetAll();
    this.mTaskCounter.clearTask();
    int i = 0;
    while (i < 5)
    {
      this.mTaskCounter.addTask();
      i += 1;
    }
  }

  public static GeoServiceImpl getInstance(Context paramContext)
  {
    monitorenter;
    try
    {
      if (sInstance == null)
        sInstance = new GeoServiceImpl(paramContext);
      paramContext = sInstance;
      return paramContext;
    }
    finally
    {
      monitorexit;
    }
    throw paramContext;
  }

  private void locate()
  {
    this.mLocTimeRecorder.initLocalLocElapse();
    if (!DPLocationService.hasPermission())
    {
      this.mTaskCounter.removeTask();
      this.mTaskCounter.removeTask();
      return;
    }
    this.mGpsLocator.locate(this);
    this.mNetworkLocator.locate(this);
  }

  private void notifyListeners()
  {
    Iterator localIterator = this.mListenerList.iterator();
    while (localIterator.hasNext())
      ((GeoListener)localIterator.next()).onRequestGeoParamsFinish(this.mGeoParams);
  }

  private void onLocateEnd()
  {
    this.mGeoParams.clear();
    this.mGeoParams.put("noSimInfo", CellModel.listToNoSimString(this.mCellList));
    this.mGeoParams.put("gsmInfo", CellModel.listToGsmString(this.mCellList));
    this.mGeoParams.put("cdmaInfo", CellModel.listToCdmaString(this.mCellList));
    this.mGeoParams.put("lteInfo", CellModel.listToLteString(this.mCellList));
    this.mGeoParams.put("wcdmaInfo", CellModel.listToWcdmaString(this.mCellList));
    this.mGeoParams.put("wifiInfo", WifiModel.listToString(this.mWifiList));
    this.mGeoParams.put("coordGps", CoordModel.listToGpsString(this.mCoordList));
    this.mGeoParams.put("coordNetwork", CoordModel.listToNetworkString(this.mCoordList));
    this.mGeoParams.put("coordBMap", CoordModel.listToBMapString(this.mCoordList));
    this.mGeoParams.put("cellScanElapse", String.valueOf(this.mLocTimeRecorder.getCellScanElapse()));
    this.mGeoParams.put("wifiScanElapse", String.valueOf(this.mLocTimeRecorder.getWifiScanElapse()));
    this.mGeoParams.put("localLocElapse", String.valueOf(this.mLocTimeRecorder.getLocalLocElapse()));
    this.mMainHandler.sendEmptyMessage(1002);
  }

  private void onRequestGeoParams()
  {
    clearData();
    scan();
  }

  private void onScanEnd()
  {
    locate();
    assistLocate();
  }

  private void scan()
  {
    new Thread()
    {
      public void run()
      {
        GeoServiceImpl.this.cellScan();
      }
    }
    .start();
    new Thread()
    {
      public void run()
      {
        GeoServiceImpl.this.wifiScan();
      }
    }
    .start();
  }

  private void wifiScan()
  {
    this.mLocTimeRecorder.initWifiScanElapse();
    ExecutorService localExecutorService = Executors.newSingleThreadExecutor();
    Object localObject = new FutureTask(new WifiScanner(this.mContext));
    localExecutorService.submit((Runnable)localObject);
    try
    {
      localObject = (List)((FutureTask)localObject).get(500L, TimeUnit.MILLISECONDS);
      if (localObject != null)
        this.mWifiList.addAllAbsent((Collection)localObject);
      localExecutorService.shutdown();
      this.mTaskCounter.removeTask();
      this.mLocTimeRecorder.calWifiScanElapse();
      checkScanEnd();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e(localException.toString());
    }
  }

  public boolean addListener(GeoListener paramGeoListener)
  {
    return this.mListenerList.add(paramGeoListener);
  }

  public void onLocateFinish(List<CoordModel> paramList)
  {
    this.mCoordList.addAllAbsent(paramList);
    this.mTaskCounter.removeTask();
    checkLocateEnd();
  }

  public boolean removeListener(GeoListener paramGeoListener)
  {
    return this.mListenerList.remove(paramGeoListener);
  }

  public void requestGeoParams()
  {
    synchronized (this.mLock)
    {
      if (this.mIsLocating)
        return;
      this.mIsLocating = true;
      onRequestGeoParams();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.geo.GeoServiceImpl
 * JD-Core Version:    0.6.0
 */