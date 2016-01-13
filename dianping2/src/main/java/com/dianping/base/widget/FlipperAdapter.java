package com.dianping.base.widget;

import android.view.View;

public abstract interface FlipperAdapter<T>
{
  public abstract T getNextItem(T paramT);

  public abstract T getPreviousItem(T paramT);

  public abstract View getView(T paramT, View paramView);

  public abstract void onMoved(T paramT1, T paramT2);

  public abstract void onMoving(T paramT1, T paramT2);

  public abstract void onTap(T paramT);

  public abstract void recycleView(View paramView);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.FlipperAdapter
 * JD-Core Version:    0.6.0
 */