package com.tonicartos.widget.stickygridheaders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public abstract interface StickyGridHeadersBaseAdapter extends ListAdapter
{
  public abstract int getCountForHeader(int paramInt);

  public abstract View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup);

  public abstract int getNumHeaders();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter
 * JD-Core Version:    0.6.0
 */