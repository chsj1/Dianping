package com.dianping.base.shoplist.data;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractDataSource
  implements DataSource
{
  ArrayList<DataSource.OnDataChangeListener> mDataListeners = new ArrayList();
  DataSource.DataLoader mDataLoader;
  private int mStatus = 1;
  private DataSource.OnDataSourceStatusChangeListener mStatusChangeListener;

  public void addDataChangeListener(DataSource.OnDataChangeListener paramOnDataChangeListener)
  {
    if (!this.mDataListeners.contains(paramOnDataChangeListener))
      this.mDataListeners.add(paramOnDataChangeListener);
  }

  protected void changeStatus(int paramInt)
  {
    if (paramInt == this.mStatus)
      return;
    this.mStatus = paramInt;
    if (this.mStatusChangeListener != null)
      this.mStatusChangeListener.onDataSourceStatusChange(paramInt);
    publishDataChange(1);
  }

  protected void publishDataChange(int paramInt)
  {
    new Handler(Looper.getMainLooper()).post(new Runnable(paramInt)
    {
      public void run()
      {
        Iterator localIterator = AbstractDataSource.this.mDataListeners.iterator();
        while (localIterator.hasNext())
          ((DataSource.OnDataChangeListener)localIterator.next()).onDataChanged(this.val$dataType);
      }
    });
  }

  public void removeDataChangeListener(DataSource.OnDataChangeListener paramOnDataChangeListener)
  {
    this.mDataListeners.remove(paramOnDataChangeListener);
  }

  public void setDataLoader(DataSource.DataLoader paramDataLoader)
  {
    this.mDataLoader = paramDataLoader;
  }

  public void setDataSourceStatusChangeListener(DataSource.OnDataSourceStatusChangeListener paramOnDataSourceStatusChangeListener)
  {
    this.mStatusChangeListener = paramOnDataSourceStatusChangeListener;
  }

  public int status()
  {
    return this.mStatus;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.AbstractDataSource
 * JD-Core Version:    0.6.0
 */