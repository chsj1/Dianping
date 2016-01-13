package com.dianping.content;

import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.model.City;

public class CityUtils
{
  private static CityStore sCityStore;

  private static CityStore cityStore()
  {
    if (sCityStore == null)
      sCityStore = new AllCityStore(DPApplication.instance());
    return sCityStore;
  }

  public static City[] getCities()
  {
    return cityStore().getCities();
  }

  public static City getCityById(int paramInt)
  {
    return cityStore().getCityById(paramInt);
  }

  public static final City[] getSortBy1stChar()
  {
    return cityStore().getSortBy1stChar();
  }

  public static void setCities(DPObject[] paramArrayOfDPObject, byte[] paramArrayOfByte)
  {
    cityStore().setCities(paramArrayOfDPObject, paramArrayOfByte);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.content.CityUtils
 * JD-Core Version:    0.6.0
 */