package com.dianping.content;

import android.content.Context;
import com.dianping.model.City;
import java.util.ArrayList;

public class TuanCityStore extends CityStore
{
  public TuanCityStore(Context paramContext)
  {
    super(paramContext);
  }

  protected City[] getSpecialCities(City[] paramArrayOfCity)
  {
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfCity.length;
    int i = 0;
    while (i < j)
    {
      City localCity = paramArrayOfCity[i];
      if (localCity.isTuan())
        localArrayList.add(localCity);
      i += 1;
    }
    return (City[])localArrayList.toArray(new City[localArrayList.size()]);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.content.TuanCityStore
 * JD-Core Version:    0.6.0
 */