package com.dianping.locationservice.impl286.locate;

import android.content.Context;
import android.location.Location;
import com.dianping.locationservice.impl286.model.CoordModel;
import com.dianping.locationservice.impl286.model.CoordNetModel;

public class NetworkLocator extends LocalLocator
{
  public NetworkLocator(Context paramContext)
  {
    super(paramContext);
  }

  protected String getProviderName()
  {
    return "network";
  }

  protected CoordModel locationToCoord(Location paramLocation)
  {
    return new CoordNetModel(paramLocation.getLatitude(), paramLocation.getLongitude(), (int)paramLocation.getAccuracy(), paramLocation.getTime());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.NetworkLocator
 * JD-Core Version:    0.6.0
 */