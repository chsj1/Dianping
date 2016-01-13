package com.dianping.locationservice.impl286.locate;

import com.dianping.locationservice.impl286.model.CoordModel;
import java.util.List;

public abstract interface LocateListener
{
  public abstract void onLocateFinish(List<CoordModel> paramList);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.LocateListener
 * JD-Core Version:    0.6.0
 */