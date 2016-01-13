package com.dianping.locationservice.impl286.locate;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dianping.locationservice.impl286.model.CoordModel;
import com.dianping.util.Log;
import java.util.List;

public abstract class LocalLocator extends BaseLocator
  implements LocationListener
{
  private static final int HALF_MINUTES = 30000;
  private static final int MSG_TIMEOUT = 1001;
  private static final int TIMEOUT = 1000;
  private Location mCurrentBestLocation;
  protected final LocationManager mLocManager;
  private final Handler mMonitorHandler;
  protected LocationProvider mProvider;

  public LocalLocator(Context paramContext)
  {
    super(paramContext);
    this.mLocManager = ((LocationManager)paramContext.getSystemService("location"));
    try
    {
      this.mProvider = this.mLocManager.getProvider(getProviderName());
      this.mMonitorHandler = new Handler(paramContext.getMainLooper())
      {
        public void handleMessage(Message paramMessage)
        {
          switch (paramMessage.what)
          {
          default:
            return;
          case 1001:
          }
          LocalLocator.this.finishLocate();
        }
      };
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        Log.w(localThrowable.toString());
        this.mProvider = null;
      }
    }
  }

  private void doLocalLocate()
  {
    this.mMonitorHandler.sendEmptyMessageDelayed(1001, 1000L);
    try
    {
      this.mCurrentBestLocation = this.mLocManager.getLastKnownLocation(getProviderName());
      this.mLocManager.requestLocationUpdates(getProviderName(), 0L, 0.0F, this);
      return;
    }
    catch (Throwable localThrowable)
    {
      Log.e(localThrowable.toString());
      finishLocate();
    }
  }

  private void finishLocate()
  {
    onLocateFinish();
    if (this.mCurrentBestLocation != null);
    while (true)
    {
      try
      {
        CoordModel localCoordModel = locationToCoord(this.mCurrentBestLocation);
        this.mResultList.add(localCoordModel);
        notifyLocateFinish();
        return;
      }
      catch (Exception localException)
      {
        this.mErrorMsg = (getProviderName() + " " + localException.toString());
        continue;
      }
      this.mErrorMsg = (getProviderName() + " locate timeout");
    }
  }

  private boolean isBetterLocation(Location paramLocation1, Location paramLocation2)
  {
    if (paramLocation2 == null)
      return true;
    long l = paramLocation1.getTime() - paramLocation2.getTime();
    int j;
    int k;
    if (l > 30000L)
    {
      j = 1;
      if (l >= -30000L)
        break label63;
      k = 1;
      label41: if (l <= 0L)
        break label69;
    }
    label63: label69: for (int i = 1; ; i = 0)
    {
      if (j == 0)
        break label74;
      return true;
      j = 0;
      break;
      k = 0;
      break label41;
    }
    label74: if (k != 0)
      return false;
    int m = (int)(paramLocation1.getAccuracy() - paramLocation2.getAccuracy());
    if (m > 0)
    {
      j = 1;
      if (m >= 0)
        break label133;
      k = 1;
      label109: if (m <= 200)
        break label139;
    }
    label133: label139: for (m = 1; ; m = 0)
    {
      if (k == 0)
        break label145;
      return true;
      j = 0;
      break;
      k = 0;
      break label109;
    }
    label145: if ((i != 0) && (j == 0))
      return true;
    return (i != 0) && (m == 0);
  }

  private void onLocateFinish()
  {
    this.mMonitorHandler.removeMessages(1001);
    try
    {
      this.mLocManager.removeUpdates(this);
      return;
    }
    catch (Throwable localThrowable)
    {
      Log.w(localThrowable.toString());
    }
  }

  protected abstract String getProviderName();

  protected boolean hasProvider()
  {
    if (this.mProvider == null);
    while (true)
    {
      return false;
      try
      {
        boolean bool = this.mLocManager.isProviderEnabled(getProviderName());
        if (bool)
          return true;
      }
      catch (Throwable localThrowable)
      {
        Log.w(localThrowable.toString());
      }
    }
    return false;
  }

  protected abstract CoordModel locationToCoord(Location paramLocation);

  public void onLocationChanged(Location paramLocation)
  {
    if (isBetterLocation(paramLocation, this.mCurrentBestLocation))
      this.mCurrentBestLocation = paramLocation;
    Log.d("");
    finishLocate();
  }

  public void onProviderDisabled(String paramString)
  {
  }

  public void onProviderEnabled(String paramString)
  {
  }

  protected void onStartLocate()
  {
    if (!hasProvider())
    {
      this.mErrorMsg = (getProviderName() + " provider is null or disabled");
      notifyLocateFinish();
      return;
    }
    doLocalLocate();
  }

  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.LocalLocator
 * JD-Core Version:    0.6.0
 */