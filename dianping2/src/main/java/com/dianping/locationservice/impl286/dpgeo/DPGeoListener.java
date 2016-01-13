package com.dianping.locationservice.impl286.dpgeo;

import com.dianping.archive.DPObject;

public abstract interface DPGeoListener
{
  public abstract void onLocateFail();

  public abstract void onLocateFinish(DPObject paramDPObject);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.dpgeo.DPGeoListener
 * JD-Core Version:    0.6.0
 */