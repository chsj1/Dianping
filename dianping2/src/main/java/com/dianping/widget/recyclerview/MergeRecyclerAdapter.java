package com.dianping.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MergeRecyclerAdapter<T extends RecyclerView.Adapter> extends RecyclerView.Adapter
{
  public ArrayList<MergeRecyclerAdapter<T>.LocalAdapter> mAdapters = new ArrayList();
  private Context mContext;
  private int mViewTypeIndex = 0;
  protected MergeRecyclerAdapter<T>.ForwardingDataSetObserver observer = new ForwardingDataSetObserver(null);

  public MergeRecyclerAdapter()
  {
  }

  public MergeRecyclerAdapter(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public void addAdapter(int paramInt, T paramT)
  {
    this.mAdapters.add(paramInt, new LocalAdapter(paramT));
    paramT.registerAdapterDataObserver(this.observer);
    notifyDataSetChanged();
  }

  public void addAdapter(T paramT)
  {
    addAdapter(this.mAdapters.size(), paramT);
  }

  public void addView(View paramView)
  {
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(paramView);
    addViews(localArrayList);
  }

  public void addViews(List<View> paramList)
  {
    addAdapter(new ViewsAdapter(this.mContext, paramList));
  }

  public void clear()
  {
    Iterator localIterator = this.mAdapters.iterator();
    while (localIterator.hasNext())
      ((LocalAdapter)localIterator.next()).mAdapter.unregisterAdapterDataObserver(this.observer);
    this.mAdapters.clear();
    notifyDataSetChanged();
  }

  public MergeRecyclerAdapter<T>.LocalAdapter getAdapterOffsetForItem(int paramInt)
  {
    int m = this.mAdapters.size();
    int i = 0;
    int j = 0;
    while (i < m)
    {
      LocalAdapter localLocalAdapter = (LocalAdapter)this.mAdapters.get(i);
      int k = j + localLocalAdapter.mAdapter.getItemCount();
      if (paramInt < k)
      {
        localLocalAdapter.mLocalPosition = (paramInt - j);
        return localLocalAdapter;
      }
      j = k;
      i += 1;
    }
    return null;
  }

  public int getItemCount()
  {
    int i = 0;
    Iterator localIterator = this.mAdapters.iterator();
    while (localIterator.hasNext())
      i += ((LocalAdapter)localIterator.next()).mAdapter.getItemCount();
    return i;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    LocalAdapter localLocalAdapter = getAdapterOffsetForItem(paramInt);
    paramInt = localLocalAdapter.mAdapter.getItemViewType(localLocalAdapter.mLocalPosition);
    if (localLocalAdapter.mViewTypesMap.containsValue(Integer.valueOf(paramInt)))
    {
      Iterator localIterator = localLocalAdapter.mViewTypesMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (((Integer)localEntry.getValue()).intValue() == paramInt)
          return ((Integer)localEntry.getKey()).intValue();
      }
    }
    this.mViewTypeIndex += 1;
    localLocalAdapter.mViewTypesMap.put(Integer.valueOf(this.mViewTypeIndex), Integer.valueOf(paramInt));
    return this.mViewTypeIndex;
  }

  public T getSubAdapter(int paramInt)
  {
    return ((LocalAdapter)this.mAdapters.get(paramInt)).mAdapter;
  }

  public int getSubAdapterCount()
  {
    return this.mAdapters.size();
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    LocalAdapter localLocalAdapter = getAdapterOffsetForItem(paramInt);
    localLocalAdapter.mAdapter.onBindViewHolder(paramViewHolder, localLocalAdapter.mLocalPosition);
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
    Iterator localIterator = this.mAdapters.iterator();
    while (localIterator.hasNext())
    {
      LocalAdapter localLocalAdapter = (LocalAdapter)localIterator.next();
      if (localLocalAdapter.mViewTypesMap.containsKey(Integer.valueOf(paramInt)))
        return localLocalAdapter.mAdapter.onCreateViewHolder(paramViewGroup, ((Integer)localLocalAdapter.mViewTypesMap.get(Integer.valueOf(paramInt))).intValue());
    }
    return null;
  }

  public void removeAdapter(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.mAdapters.size()))
      return;
    ((LocalAdapter)this.mAdapters.remove(paramInt)).mAdapter.unregisterAdapterDataObserver(this.observer);
    notifyDataSetChanged();
  }

  public void removeAdapter(T paramT)
  {
    if (!this.mAdapters.contains(paramT))
      return;
    removeAdapter(this.mAdapters.indexOf(paramT));
  }

  private class ForwardingDataSetObserver extends RecyclerView.AdapterDataObserver
  {
    private ForwardingDataSetObserver()
    {
    }

    public void onChanged()
    {
      MergeRecyclerAdapter.this.notifyDataSetChanged();
    }

    public void onItemRangeChanged(int paramInt1, int paramInt2)
    {
      super.onItemRangeChanged(paramInt1, paramInt2);
      MergeRecyclerAdapter.this.notifyItemRangeChanged(paramInt1, paramInt2);
    }

    public void onItemRangeInserted(int paramInt1, int paramInt2)
    {
      super.onItemRangeInserted(paramInt1, paramInt2);
      MergeRecyclerAdapter.this.notifyItemRangeInserted(paramInt1, paramInt2);
    }

    public void onItemRangeRemoved(int paramInt1, int paramInt2)
    {
      super.onItemRangeRemoved(paramInt1, paramInt2);
      MergeRecyclerAdapter.this.notifyItemRangeRemoved(paramInt1, paramInt2);
    }
  }

  public class LocalAdapter
  {
    public final T mAdapter;
    public int mLocalPosition = 0;
    public Map<Integer, Integer> mViewTypesMap = new HashMap();

    public LocalAdapter()
    {
      Object localObject;
      this.mAdapter = localObject;
    }
  }

  public static class ViewsAdapter extends RecyclerView.Adapter
  {
    private Context context;
    private List<View> views = null;

    public ViewsAdapter(Context paramContext, int paramInt)
    {
      this.context = paramContext;
      this.views = new ArrayList(paramInt);
      int i = 0;
      while (i < paramInt)
      {
        this.views.add(null);
        i += 1;
      }
    }

    public ViewsAdapter(Context paramContext, List<View> paramList)
    {
      this.context = paramContext;
      this.views = paramList;
    }

    public int getItemCount()
    {
      return this.views.size();
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      return paramInt;
    }

    public boolean hasView(View paramView)
    {
      return this.views.contains(paramView);
    }

    protected View newView(int paramInt, ViewGroup paramViewGroup)
    {
      throw new RuntimeException("You must override newView()!");
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new MergeRecyclerAdapter.ViewsViewHolder((View)this.views.get(paramInt));
    }
  }

  public static class ViewsViewHolder extends RecyclerView.ViewHolder
  {
    public ViewsViewHolder(View paramView)
    {
      super();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.recyclerview.MergeRecyclerAdapter
 * JD-Core Version:    0.6.0
 */