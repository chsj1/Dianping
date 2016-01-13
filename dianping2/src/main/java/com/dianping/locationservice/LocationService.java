package com.dianping.locationservice;

import com.dianping.archive.DPObject;
import com.dianping.model.GPSCoordinate;

public abstract interface LocationService
{
  public static final int STATUS_FAIL = -1;
  public static final int STATUS_IDLE = 0;
  public static final int STATUS_LOCATED = 2;
  public static final int STATUS_TRYING = 1;

  public abstract void addListener(LocationListener paramLocationListener);

  public abstract String address();

  public abstract DPObject city();

  public abstract boolean hasLocation();

  public abstract DPObject location();

  public abstract GPSCoordinate offsetCoordinate();

  public abstract GPSCoordinate realCoordinate();

  public abstract boolean refresh();

  public abstract void removeListener(LocationListener paramLocationListener);

  public abstract void selectCoordinate(int paramInt, GPSCoordinate paramGPSCoordinate);

  public abstract boolean start();

  public abstract int status();

  public abstract void stop();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.LocationService
 * JD-Core Version:    0.6.0
 */