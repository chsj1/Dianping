package com.dianping.locationservice.impl286.dpgeo;

public abstract interface DPGeoService
{
  public abstract boolean addListener(DPGeoListener paramDPGeoListener);

  public abstract void doRgc(double paramDouble1, double paramDouble2, int paramInt1, String paramString, int paramInt2);

  public abstract boolean removeListener(DPGeoListener paramDPGeoListener);

  public abstract void start();

  public abstract void stop();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.dpgeo.DPGeoService
 * JD-Core Version:    0.6.0
 */