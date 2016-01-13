package com.dianping.tuan.adapter.decorator;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public abstract class DecroatorAdapter
  implements ListAdapter
{
  protected Context context;
  protected ListAdapter mDataAdapter;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  protected DataSetObserver observer = new DecroatorAdapter.1(this);

  public DecroatorAdapter(Context paramContext, ListAdapter paramListAdapter)
  {
    this.context = paramContext;
    this.mDataAdapter = paramListAdapter;
    this.mDataAdapter.registerDataSetObserver(this.observer);
  }

  public boolean areAllItemsEnabled()
  {
    return this.mDataAdapter.areAllItemsEnabled();
  }

  public int getCount()
  {
    return this.mDataAdapter.getCount();
  }

  public ListAdapter getDataAdapter()
  {
    return this.mDataAdapter;
  }

  public Object getItem(int paramInt)
  {
    return this.mDataAdapter.getItem(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return this.mDataAdapter.getItemId(paramInt);
  }

  public int getItemViewType(int paramInt)
  {
    return this.mDataAdapter.getItemViewType(paramInt);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return this.mDataAdapter.getView(paramInt, paramView, paramViewGroup);
  }

  public int getViewTypeCount()
  {
    return this.mDataAdapter.getViewTypeCount();
  }

  public boolean hasStableIds()
  {
    return this.mDataAdapter.hasStableIds();
  }

  public boolean isEmpty()
  {
    return this.mDataAdapter.isEmpty();
  }

  public boolean isEnabled(int paramInt)
  {
    return this.mDataAdapter.isEnabled(paramInt);
  }

  public void notifyDataSetChanged()
  {
    this.mDataSetObservable.notifyChanged();
  }

  public void notifyDataSetInvalidated()
  {
    this.mDataSetObservable.notifyInvalidated();
  }

  public void onDataChanged()
  {
    notifyDataSetChanged();
  }

  public void onDataInvalidated()
  {
    notifyDataSetInvalidated();
  }

  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.registerObserver(paramDataSetObserver);
  }

  public void setDataAdapter(ListAdapter paramListAdapter)
  {
    this.mDataAdapter = paramListAdapter;
    this.mDataAdapter.registerDataSetObserver(this.observer);
  }

  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.adapter.decorator.DecroatorAdapter
 * JD-Core Version:    0.6.0
 */