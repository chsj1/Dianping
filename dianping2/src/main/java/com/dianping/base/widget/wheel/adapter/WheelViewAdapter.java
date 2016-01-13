package com.dianping.base.widget.wheel.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public abstract interface WheelViewAdapter
{
  public abstract View getEmptyItem(View paramView, ViewGroup paramViewGroup);

  public abstract View getItem(int paramInt, View paramView, ViewGroup paramViewGroup);

  public abstract int getItemsCount();

  public abstract void registerDataSetObserver(DataSetObserver paramDataSetObserver);

  public abstract void unregisterDataSetObserver(DataSetObserver paramDataSetObserver);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wheel.adapter.WheelViewAdapter
 * JD-Core Version:    0.6.0
 */