package com.dianping.locationservice.realtime;

import com.dianping.model.GPSCoordinate;

public class RealTimeLocateResult
{
  private GPSCoordinate coordinate;
  private LocationIdentifier locationId;
  private String log;

  public RealTimeLocateResult()
  {
  }

  public RealTimeLocateResult(RealTimeLocateSettings paramRealTimeLocateSettings, LocationIdentifier paramLocationIdentifier, GPSCoordinate paramGPSCoordinate, String paramString)
  {
    if (paramRealTimeLocateSettings.hasAnyFlag(16))
      this.locationId = paramLocationIdentifier;
    this.coordinate = paramGPSCoordinate;
    if (!paramRealTimeLocateSettings.hasAnyFlag(128))
      this.log = paramString;
  }

  public RealTimeLocateResult(RealTimeLocateSettings paramRealTimeLocateSettings, GPSCoordinate paramGPSCoordinate, String paramString)
  {
    this.coordinate = paramGPSCoordinate;
    if (!paramRealTimeLocateSettings.hasAnyFlag(128))
      this.log = paramString;
  }

  public RealTimeLocateResult(RealTimeLocateSettings paramRealTimeLocateSettings, String paramString)
  {
    if (!paramRealTimeLocateSettings.hasAnyFlag(128))
      this.log = paramString;
  }

  public RealTimeLocateResult(RealTimeLocateSettings paramRealTimeLocateSettings, String paramString, Throwable paramThrowable)
  {
    if (!paramRealTimeLocateSettings.hasAnyFlag(128))
    {
      if (paramRealTimeLocateSettings.hasAnyFlag(256))
        this.log = (paramString + paramThrowable.getMessage());
    }
    else
      return;
    this.log = paramString;
  }

  public GPSCoordinate getCoordinate()
  {
    return this.coordinate;
  }

  public LocationIdentifier getLocationId()
  {
    return this.locationId;
  }

  public String getLog()
  {
    return this.log;
  }

  public void setCoordinate(GPSCoordinate paramGPSCoordinate)
  {
    this.coordinate = paramGPSCoordinate;
  }

  public void setLocationId(LocationIdentifier paramLocationIdentifier)
  {
    this.locationId = paramLocationIdentifier;
  }

  public void setLog(String paramString)
  {
    this.log = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.realtime.RealTimeLocateResult
 * JD-Core Version:    0.6.0
 */