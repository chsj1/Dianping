package com.dianping.base.widget.wheel.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheelAdapter
  implements WheelViewAdapter
{
  private List<DataSetObserver> datasetObservers;

  public View getEmptyItem(View paramView, ViewGroup paramViewGroup)
  {
    return null;
  }

  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (this.datasetObservers == null)
      this.datasetObservers = new LinkedList();
    this.datasetObservers.add(paramDataSetObserver);
  }

  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (this.datasetObservers != null)
      this.datasetObservers.remove(paramDataSetObserver);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wheel.adapter.AbstractWheelAdapter
 * JD-Core Version:    0.6.0
 */