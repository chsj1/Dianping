package com.dianping.locationservice.impl286.geo;

public class LocTimeRecorder
{
  private long mCellScanElapse;
  private long mLocalLocElapse;
  private long mWifiScanElapse;

  public void calCellScanElapse()
  {
    monitorenter;
    try
    {
      this.mCellScanElapse = (System.currentTimeMillis() - this.mCellScanElapse);
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

  public void calLocalLocElapse()
  {
    monitorenter;
    try
    {
      this.mLocalLocElapse = (System.currentTimeMillis() - this.mLocalLocElapse);
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

  public void calWifiScanElapse()
  {
    monitorenter;
    try
    {
      this.mWifiScanElapse = (System.currentTimeMillis() - this.mWifiScanElapse);
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

  public long getCellScanElapse()
  {
    monitorenter;
    try
    {
      long l = this.mCellScanElapse;
      monitorexit;
      return l;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public long getLocalLocElapse()
  {
    monitorenter;
    try
    {
      long l = this.mLocalLocElapse;
      monitorexit;
      return l;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public long getWifiScanElapse()
  {
    monitorenter;
    try
    {
      long l = this.mWifiScanElapse;
      monitorexit;
      return l;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void initCellScanElapse()
  {
    monitorenter;
    try
    {
      this.mCellScanElapse = System.currentTimeMillis();
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

  public void initLocalLocElapse()
  {
    monitorenter;
    try
    {
      this.mLocalLocElapse = System.currentTimeMillis();
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

  public void initWifiScanElapse()
  {
    monitorenter;
    try
    {
      this.mWifiScanElapse = System.currentTimeMillis();
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

  public void resetAll()
  {
    monitorenter;
    try
    {
      this.mCellScanElapse = 0L;
      this.mWifiScanElapse = 0L;
      this.mLocalLocElapse = 0L;
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.geo.LocTimeRecorder
 * JD-Core Version:    0.6.0
 */