package com.dianping.base.widget;

import com.dianping.archive.DPObject;

public abstract interface ViewItemInterface
{
  public abstract ViewItemType getType();

  public abstract void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ViewItemInterface
 * JD-Core Version:    0.6.0
 */