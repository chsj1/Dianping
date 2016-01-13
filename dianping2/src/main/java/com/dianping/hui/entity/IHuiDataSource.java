package com.dianping.hui.entity;

import android.os.Bundle;

public abstract interface IHuiDataSource
{
  public abstract void releaseRequests();

  public abstract void restoreData(Bundle paramBundle);

  public abstract void saveData(Bundle paramBundle);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.IHuiDataSource
 * JD-Core Version:    0.6.0
 */