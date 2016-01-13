package com.dianping.locationservice.impl286.util;

import com.dianping.archive.DPObject;
import com.dianping.model.GPSCoordinate;

public class CoordUtil
{
  public static GPSCoordinate coordinate(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    return new GPSCoordinate(paramDPObject.getDouble("Lat"), paramDPObject.getDouble("Lng"), paramDPObject.getInt("Accuracy"), 0L, paramDPObject.getString("Source"));
  }

  public static GPSCoordinate offsetCoordinate(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    return new GPSCoordinate(paramDPObject.getDouble("OffsetLat"), paramDPObject.getDouble("OffsetLng"), paramDPObject.getInt("Accuracy"), 0L, paramDPObject.getString("Source"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.util.CoordUtil
 * JD-Core Version:    0.6.0
 */