package com.dianping.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class MergeAdapter extends BaseAdapter
{
  DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      MergeAdapter.this.notifyDataSetChanged();
    }

    public void onInvalidated()
    {
      MergeAdapter.this.notifyDataSetInvalidated();
    }
  };
  protected ArrayList<ListAdapter> pieces = new ArrayList();

  public void addAdapter(ListAdapter paramListAdapter)
  {
    this.pieces.add(paramListAdapter);
    try
    {
      paramListAdapter.registerDataSetObserver(this.observer);
      label19: notifyDataSetChanged();
      return;
    }
    catch (java.lang.Exception paramListAdapter)
    {
      break label19;
    }
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public void clear()
  {
    if ((this.pieces != null) && (this.pieces.size() > 0))
      this.pieces.clear();
  }

  public int getCount()
  {
    int i = 0;
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
      i += ((ListAdapter)localIterator.next()).getCount();
    return i;
  }

  public Object getItem(int paramInt)
  {
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
    {
      ListAdapter localListAdapter = (ListAdapter)localIterator.next();
      int i = localListAdapter.getCount();
      if (paramInt < i)
        return localListAdapter.getItem(paramInt);
      paramInt -= i;
    }
    return null;
  }

  public long getItemId(int paramInt)
  {
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
    {
      ListAdapter localListAdapter = (ListAdapter)localIterator.next();
      int i = localListAdapter.getCount();
      if (paramInt < i)
        return localListAdapter.getItemId(paramInt);
      paramInt -= i;
    }
    return -1L;
  }

  public int getItemViewType(int paramInt)
  {
    int j = 0;
    int k = -1;
    Iterator localIterator = this.pieces.iterator();
    int i = paramInt;
    paramInt = j;
    while (true)
    {
      j = k;
      ListAdapter localListAdapter;
      if (localIterator.hasNext())
      {
        localListAdapter = (ListAdapter)localIterator.next();
        j = localListAdapter.getCount();
        if (i < j)
          j = paramInt + localListAdapter.getItemViewType(i);
      }
      else
      {
        return j;
      }
      i -= j;
      paramInt += localListAdapter.getViewTypeCount();
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
    {
      ListAdapter localListAdapter = (ListAdapter)localIterator.next();
      int i = localListAdapter.getCount();
      if (paramInt < i)
        return localListAdapter.getView(paramInt, paramView, paramViewGroup);
      paramInt -= i;
    }
    return null;
  }

  public int getViewTypeCount()
  {
    int i = 0;
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
      i += ((ListAdapter)localIterator.next()).getViewTypeCount();
    return Math.max(i, 1);
  }

  public boolean isEnabled(int paramInt)
  {
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
    {
      ListAdapter localListAdapter = (ListAdapter)localIterator.next();
      int i = localListAdapter.getCount();
      if (paramInt < i)
        return localListAdapter.isEnabled(paramInt);
      paramInt -= i;
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.adapter.MergeAdapter
 * JD-Core Version:    0.6.0
 */