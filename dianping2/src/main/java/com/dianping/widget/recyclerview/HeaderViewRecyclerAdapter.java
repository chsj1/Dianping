package com.dianping.widget.recyclerview;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class HeaderViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
  static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList();
  public static final int FOOTER_TYPE = -100;
  public static final int HEADER_TYPE = -1;
  private final RecyclerView.Adapter mAdapter;
  private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver()
  {
    public void onChanged()
    {
      super.onChanged();
      HeaderViewRecyclerAdapter.this.notifyDataSetChanged();
    }

    public void onItemRangeChanged(int paramInt1, int paramInt2)
    {
      super.onItemRangeChanged(paramInt1, paramInt2);
      HeaderViewRecyclerAdapter.this.notifyItemRangeChanged(HeaderViewRecyclerAdapter.this.getHeaderCount() + paramInt1, paramInt2);
    }

    public void onItemRangeInserted(int paramInt1, int paramInt2)
    {
      super.onItemRangeInserted(paramInt1, paramInt2);
      HeaderViewRecyclerAdapter.this.notifyItemRangeInserted(HeaderViewRecyclerAdapter.this.getHeaderCount() + paramInt1, paramInt2);
    }

    public void onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
    {
      super.onItemRangeMoved(paramInt1, paramInt2, paramInt3);
      int i = HeaderViewRecyclerAdapter.this.getHeaderCount();
      HeaderViewRecyclerAdapter.this.notifyItemRangeChanged(paramInt1 + i, paramInt2 + i + paramInt3);
    }

    public void onItemRangeRemoved(int paramInt1, int paramInt2)
    {
      super.onItemRangeRemoved(paramInt1, paramInt2);
      HeaderViewRecyclerAdapter.this.notifyItemRangeRemoved(HeaderViewRecyclerAdapter.this.getHeaderCount() + paramInt1, paramInt2);
    }
  };
  ArrayList<View> mFooterViews;
  ArrayList<View> mHeaderViews;

  public HeaderViewRecyclerAdapter(ArrayList<View> paramArrayList1, ArrayList<View> paramArrayList2, RecyclerView.Adapter paramAdapter)
  {
    this.mAdapter = paramAdapter;
    this.mAdapter.registerAdapterDataObserver(this.mDataObserver);
    if (paramArrayList1 == null);
    for (this.mHeaderViews = EMPTY_INFO_LIST; paramArrayList2 == null; this.mHeaderViews = paramArrayList1)
    {
      this.mFooterViews = EMPTY_INFO_LIST;
      return;
    }
    this.mFooterViews = paramArrayList2;
  }

  public int getFooterCount()
  {
    return this.mFooterViews.size();
  }

  public int getFootersCount()
  {
    return this.mFooterViews.size();
  }

  public int getHeaderCount()
  {
    return this.mHeaderViews.size();
  }

  public int getHeadersCount()
  {
    return this.mHeaderViews.size();
  }

  public int getItemCount()
  {
    if (this.mAdapter != null)
      return getFootersCount() + getHeadersCount() + this.mAdapter.getItemCount();
    return getFootersCount() + getHeadersCount();
  }

  public long getItemId(int paramInt)
  {
    int i = getHeadersCount();
    if ((this.mAdapter != null) && (paramInt >= i))
    {
      paramInt -= i;
      if (paramInt < this.mAdapter.getItemCount())
        return this.mAdapter.getItemId(paramInt);
    }
    return -1L;
  }

  public int getItemViewType(int paramInt)
  {
    int i = getHeadersCount();
    if (paramInt < i)
      return -1 - paramInt;
    int j = paramInt - i;
    paramInt = 0;
    if (this.mAdapter != null)
    {
      i = this.mAdapter.getItemCount();
      paramInt = i;
      if (j < i)
        return this.mAdapter.getItemViewType(j);
    }
    return -100 - (j - paramInt);
  }

  public RecyclerView.Adapter getWrappedAdapter()
  {
    return this.mAdapter;
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    int i = getHeadersCount();
    if (paramInt < i);
    do
    {
      return;
      paramInt -= i;
    }
    while ((this.mAdapter == null) || (paramInt >= this.mAdapter.getItemCount()));
    this.mAdapter.onBindViewHolder(paramViewHolder, paramInt);
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
    if (paramInt <= -100)
      return new WrapViewHolder((View)this.mFooterViews.get(-100 - paramInt));
    if (paramInt <= -1)
      return new WrapViewHolder((View)this.mHeaderViews.get(-1 - paramInt));
    return this.mAdapter.onCreateViewHolder(paramViewGroup, paramInt);
  }

  public boolean removeFooter(View paramView)
  {
    return this.mFooterViews.remove(paramView);
  }

  public boolean removeHeader(View paramView)
  {
    return this.mHeaderViews.remove(paramView);
  }

  private static class WrapViewHolder extends RecyclerView.ViewHolder
  {
    public WrapViewHolder(View paramView)
    {
      super();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.recyclerview.HeaderViewRecyclerAdapter
 * JD-Core Version:    0.6.0
 */