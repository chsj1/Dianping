package com.tonicartos.widget.stickygridheaders;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class StickyGridHeadersListAdapterWrapper extends BaseAdapter
  implements StickyGridHeadersBaseAdapter
{
  private DataSetObserver mDataSetObserver = new StickyGridHeadersListAdapterWrapper.1(this);
  private ListAdapter mDelegate;

  public StickyGridHeadersListAdapterWrapper(ListAdapter paramListAdapter)
  {
    this.mDelegate = paramListAdapter;
    if (paramListAdapter != null)
      paramListAdapter.registerDataSetObserver(this.mDataSetObserver);
  }

  public int getCount()
  {
    if (this.mDelegate == null)
      return 0;
    return this.mDelegate.getCount();
  }

  public int getCountForHeader(int paramInt)
  {
    return 0;
  }

  public View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return null;
  }

  public Object getItem(int paramInt)
  {
    if (this.mDelegate == null)
      return null;
    return this.mDelegate.getItem(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return this.mDelegate.getItemId(paramInt);
  }

  public int getItemViewType(int paramInt)
  {
    return this.mDelegate.getItemViewType(paramInt);
  }

  public int getNumHeaders()
  {
    return 0;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return this.mDelegate.getView(paramInt, paramView, paramViewGroup);
  }

  public int getViewTypeCount()
  {
    return this.mDelegate.getViewTypeCount();
  }

  public boolean hasStableIds()
  {
    return this.mDelegate.hasStableIds();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tonicartos.widget.stickygridheaders.StickyGridHeadersListAdapterWrapper
 * JD-Core Version:    0.6.0
 */