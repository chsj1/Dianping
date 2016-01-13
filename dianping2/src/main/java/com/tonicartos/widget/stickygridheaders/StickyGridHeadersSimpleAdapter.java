package com.tonicartos.widget.stickygridheaders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public abstract interface StickyGridHeadersSimpleAdapter extends ListAdapter
{
  public abstract long getHeaderId(int paramInt);

  public abstract View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter
 * JD-Core Version:    0.6.0
 */