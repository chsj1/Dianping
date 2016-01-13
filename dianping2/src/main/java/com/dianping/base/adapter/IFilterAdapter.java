package com.dianping.base.adapter;

import com.dianping.archive.DPObject;
import java.util.ArrayList;
import java.util.List;

public abstract interface IFilterAdapter
{
  public abstract void setDataSet(ArrayList<DPObject> paramArrayList);

  public abstract void setDataSet(List<DPObject> paramList, DPObject paramDPObject);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.IFilterAdapter
 * JD-Core Version:    0.6.0
 */