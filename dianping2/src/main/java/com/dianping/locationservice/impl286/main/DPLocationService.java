package com.dianping.locationservice.impl286.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.locationservice.impl286.dpgeo.DPGeoListener;
import com.dianping.locationservice.impl286.dpgeo.DPGeoService;
import com.dianping.locationservice.impl286.dpgeo.DPGeoServiceImpl;
import com.dianping.locationservice.impl286.util.CoordUtil;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.v1.R.string;
import java.util.ArrayList;
import java.util.Iterator;

public class DPLocationService
  implements LocationService, DPGeoListener
{
  private static final long EXPIRE_TIME = 30000L;
  private final DPGeoService mDPGeoService = DPGeoServiceImpl.getInstance();
  private boolean mIsCustomLoc = false;
  private DPObject mLastLocObj;
  private long mLastLocTime;
  private long mLastReqPermTime;
  private final ArrayList<LocationListener> mListenerList = new ArrayList();
  private DPObject mLocObj;
  private int mStatus = 0;

  public DPLocationService()
  {
    this.mDPGeoService.addListener(this);
  }

  public DPLocationService(Context paramContext)
  {
    this();
  }

  private void dispatchLocationChanged()
  {
    Iterator localIterator = this.mListenerList.iterator();
    while (localIterator.hasNext())
      ((LocationListener)localIterator.next()).onLocationChanged(this);
  }

  public static boolean hasPermission()
  {
    monitorenter;
    try
    {
      boolean bool1 = PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.ACCESS_FINE_LOCATION");
      boolean bool2 = PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.ACCESS_COARSE_LOCATION");
      if ((bool1) && (bool2))
      {
        bool1 = true;
        return bool1;
      }
      bool1 = false;
    }
    finally
    {
      monitorexit;
    }
  }

  private boolean isWIFIConnection()
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)DPApplication.instance().getSystemService("connectivity")).getActiveNetworkInfo();
    return (localNetworkInfo != null) && (localNetworkInfo.getType() == 1);
  }

  private boolean locate(boolean paramBoolean)
  {
    this.mIsCustomLoc = false;
    long l = System.currentTimeMillis();
    if ((!paramBoolean) && (this.mLastLocObj != null) && (this.mLastLocTime < l) && (this.mLastLocTime > l - 30000L))
    {
      this.mLocObj = this.mLastLocObj;
      this.mStatus = 2;
      dispatchLocationChanged();
      return true;
    }
    if (this.mStatus != 1)
    {
      if (!hasPermission())
        if ((this.mLastReqPermTime > l - 30000L) && (!paramBoolean))
          if (isWIFIConnection())
            this.mDPGeoService.start();
      while (true)
      {
        this.mStatus = 1;
        dispatchLocationChanged();
        return true;
        this.mStatus = -1;
        dispatchLocationChanged();
        return true;
        this.mLastReqPermTime = l;
        requestPermission();
        continue;
        this.mDPGeoService.start();
      }
    }
    return false;
  }

  private void requestPermission()
  {
    PermissionCheckHelper localPermissionCheckHelper = PermissionCheckHelper.instance();
    DPApplication localDPApplication = DPApplication.instance();
    String str = DPApplication.instance().getString(R.string.rationale_location);
    1 local1 = new PermissionCheckHelper.PermissionCallbackListener()
    {
      public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
      {
        DPLocationService.access$002(DPLocationService.this, System.currentTimeMillis());
        if ((paramArrayOfInt[0] == 0) || (paramArrayOfInt[1] == 0) || (DPLocationService.this.isWIFIConnection()))
        {
          DPLocationService.this.mDPGeoService.start();
          return;
        }
        DPLocationService.this.onLocateFail();
      }
    };
    localPermissionCheckHelper.requestPermissions(localDPApplication, 323, new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" }, new String[] { str, "" }, local1);
  }

  public void addListener(LocationListener paramLocationListener)
  {
    this.mListenerList.add(paramLocationListener);
  }

  public String address()
  {
    if (!hasLocation())
      return "";
    return this.mLocObj.getString("Address");
  }

  public DPObject city()
  {
    if (!hasLocation())
      return null;
    return this.mLocObj.getObject("City");
  }

  public boolean hasLocation()
  {
    return this.mLocObj != null;
  }

  public DPObject location()
  {
    int i;
    if (System.currentTimeMillis() - this.mLastLocTime >= 30000L)
    {
      i = 1;
      if (this.mStatus != 2)
        break label57;
    }
    label57: for (int j = 1; ; j = 0)
    {
      if ((this.mIsCustomLoc) && (i != 0) && (j != 0))
        this.mStatus = 0;
      return this.mLocObj;
      i = 0;
      break;
    }
  }

  public GPSCoordinate offsetCoordinate()
  {
    return CoordUtil.offsetCoordinate(this.mLocObj);
  }

  public void onLocateFail()
  {
    this.mStatus = -1;
    dispatchLocationChanged();
  }

  public void onLocateFinish(DPObject paramDPObject)
  {
    this.mLocObj = paramDPObject;
    this.mLastLocObj = this.mLocObj;
    this.mLastLocTime = System.currentTimeMillis();
    this.mStatus = 2;
    dispatchLocationChanged();
  }

  public GPSCoordinate realCoordinate()
  {
    return CoordUtil.coordinate(this.mLocObj);
  }

  public boolean refresh()
  {
    this.mLastLocTime = 0L;
    this.mLastLocObj = null;
    stop();
    return locate(true);
  }

  public void removeListener(LocationListener paramLocationListener)
  {
    this.mListenerList.remove(paramLocationListener);
  }

  public void selectCoordinate(int paramInt, GPSCoordinate paramGPSCoordinate)
  {
    stop();
    this.mIsCustomLoc = true;
    this.mLastLocTime = System.currentTimeMillis();
    this.mDPGeoService.doRgc(paramGPSCoordinate.latitude(), paramGPSCoordinate.longitude(), paramGPSCoordinate.accuracy(), paramGPSCoordinate.source(), 1);
  }

  public boolean start()
  {
    return locate(false);
  }

  public int status()
  {
    return this.mStatus;
  }

  public void stop()
  {
    this.mDPGeoService.stop();
    this.mStatus = 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.main.DPLocationService
 * JD-Core Version:    0.6.0
 */