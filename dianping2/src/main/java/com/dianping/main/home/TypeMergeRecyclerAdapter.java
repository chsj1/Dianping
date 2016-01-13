package com.dianping.main.home;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.dianping.widget.recyclerview.MergeRecyclerAdapter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class TypeMergeRecyclerAdapter extends MergeRecyclerAdapter
{
  protected ArrayList<HomeAgent.HomeAgentAdapter> mTypeAdaptes = new ArrayList();

  public void addAdapter(int paramInt, RecyclerView.Adapter paramAdapter)
  {
    if (!(paramAdapter instanceof HomeAgent.HomeAgentAdapter))
      throw new RuntimeException("Adapter must be HomeAgentAdapter");
    this.mTypeAdaptes.add(paramInt, (HomeAgent.HomeAgentAdapter)paramAdapter);
    paramAdapter.registerAdapterDataObserver(this.observer);
    notifyDataSetChanged();
  }

  public void addAdapter(RecyclerView.Adapter paramAdapter)
  {
    addAdapter(this.mTypeAdaptes.size(), paramAdapter);
  }

  public void clear()
  {
    Iterator localIterator = this.mTypeAdaptes.iterator();
    while (localIterator.hasNext())
      ((RecyclerView.Adapter)localIterator.next()).unregisterAdapterDataObserver(this.observer);
    this.mTypeAdaptes.clear();
    notifyDataSetChanged();
  }

  public int getItemCount()
  {
    int i = 0;
    Iterator localIterator = this.mTypeAdaptes.iterator();
    while (localIterator.hasNext())
      i += ((RecyclerView.Adapter)localIterator.next()).getItemCount();
    return i;
  }

  public int getItemViewType(int paramInt)
  {
    Iterator localIterator = this.mTypeAdaptes.iterator();
    while (localIterator.hasNext())
    {
      RecyclerView.Adapter localAdapter = (RecyclerView.Adapter)localIterator.next();
      int i = localAdapter.getItemCount();
      if (paramInt < i)
      {
        System.out.println();
        return localAdapter.getItemViewType(paramInt);
      }
      paramInt -= i;
    }
    return -1;
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    Iterator localIterator = this.mTypeAdaptes.iterator();
    while (true)
    {
      int i;
      if (localIterator.hasNext())
      {
        RecyclerView.Adapter localAdapter = (RecyclerView.Adapter)localIterator.next();
        i = localAdapter.getItemCount();
        if (paramInt < i)
          localAdapter.onBindViewHolder(paramViewHolder, paramInt);
      }
      else
      {
        return;
      }
      paramInt -= i;
    }
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
    Iterator localIterator = this.mTypeAdaptes.iterator();
    while (localIterator.hasNext())
    {
      HomeAgent.HomeAgentAdapter localHomeAgentAdapter = (HomeAgent.HomeAgentAdapter)localIterator.next();
      if ((paramInt >= localHomeAgentAdapter.getDefaultType()) && (paramInt < localHomeAgentAdapter.getDefaultType() + localHomeAgentAdapter.getViewTypeCount()))
        return localHomeAgentAdapter.onCreateViewHolder(paramViewGroup, paramInt);
    }
    return null;
  }

  public void removeAdapter(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.mTypeAdaptes.size()))
      return;
    ((RecyclerView.Adapter)this.mTypeAdaptes.remove(paramInt)).unregisterAdapterDataObserver(this.observer);
    notifyDataSetChanged();
  }

  public void removeAdapter(RecyclerView.Adapter paramAdapter)
  {
    if (!this.mTypeAdaptes.contains(paramAdapter))
      return;
    removeAdapter(this.mTypeAdaptes.indexOf(paramAdapter));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.TypeMergeRecyclerAdapter
 * JD-Core Version:    0.6.0
 */