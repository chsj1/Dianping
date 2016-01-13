package com.dianping.locationservice.impl286.locate;

import android.content.Context;
import android.location.Location;
import com.dianping.locationservice.impl286.model.CoordGpsModel;
import com.dianping.locationservice.impl286.model.CoordModel;

public class GpsLocator extends LocalLocator
{
  public GpsLocator(Context paramContext)
  {
    super(paramContext);
  }

  protected String getProviderName()
  {
    return "gps";
  }

  protected CoordModel locationToCoord(Location paramLocation)
  {
    return new CoordGpsModel(paramLocation.getLatitude(), paramLocation.getLongitude(), (int)paramLocation.getAccuracy(), paramLocation.getTime());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.GpsLocator
 * JD-Core Version:    0.6.0
 */