package com.dianping.content;

import android.content.Context;
import com.dianping.model.City;

public class AllCityStore extends CityStore
{
  public AllCityStore(Context paramContext)
  {
    super(paramContext);
  }

  protected City[] getSpecialCities(City[] paramArrayOfCity)
  {
    return paramArrayOfCity;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.content.AllCityStore
 * JD-Core Version:    0.6.0
 */