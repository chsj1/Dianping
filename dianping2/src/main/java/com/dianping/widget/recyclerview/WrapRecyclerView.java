package com.dianping.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;

public class WrapRecyclerView extends RecyclerView
{
  private RecyclerView.Adapter mAdapter;
  private ArrayList<View> mFootViews = new ArrayList();
  private ArrayList<View> mHeaderViews = new ArrayList();

  public WrapRecyclerView(Context paramContext)
  {
    super(paramContext);
  }

  public WrapRecyclerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public WrapRecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void addHeaderView(View paramView)
  {
    this.mHeaderViews.add(paramView);
    if ((this.mAdapter != null) && (!(this.mAdapter instanceof HeaderViewRecyclerAdapter)))
    {
      this.mAdapter = new HeaderViewRecyclerAdapter(this.mHeaderViews, this.mFootViews, this.mAdapter);
      super.setAdapter(this.mAdapter);
    }
  }

  public void removeFooterView(View paramView)
  {
    this.mFootViews.remove(paramView);
    if ((this.mAdapter != null) && (!(this.mAdapter instanceof HeaderViewRecyclerAdapter)))
    {
      this.mAdapter = new HeaderViewRecyclerAdapter(this.mHeaderViews, this.mFootViews, this.mAdapter);
      super.setAdapter(this.mAdapter);
    }
  }

  public void removeHeaderView(View paramView)
  {
    this.mHeaderViews.remove(paramView);
    if ((this.mAdapter != null) && (!(this.mAdapter instanceof HeaderViewRecyclerAdapter)))
    {
      this.mAdapter = new HeaderViewRecyclerAdapter(this.mHeaderViews, this.mFootViews, this.mAdapter);
      super.setAdapter(this.mAdapter);
    }
  }

  public void setAdapter(RecyclerView.Adapter paramAdapter)
  {
    if ((this.mHeaderViews.isEmpty()) && (this.mFootViews.isEmpty()))
      super.setAdapter(paramAdapter);
    while (true)
    {
      this.mAdapter = paramAdapter;
      return;
      paramAdapter = new HeaderViewRecyclerAdapter(this.mHeaderViews, this.mFootViews, paramAdapter);
      super.setAdapter(paramAdapter);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.recyclerview.WrapRecyclerView
 * JD-Core Version:    0.6.0
 */