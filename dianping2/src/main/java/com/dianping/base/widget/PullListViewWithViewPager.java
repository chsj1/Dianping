package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class PullListViewWithViewPager extends PullToRefreshListView
{
  float xDistance;
  float xLast;
  float yDistance;
  float yLast;

  public PullListViewWithViewPager(Context paramContext)
  {
    this(paramContext, null);
  }

  public PullListViewWithViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setCacheColorHint(0);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    case 1:
    default:
    case 0:
    case 2:
    }
    do
    {
      while (true)
      {
        return super.onInterceptTouchEvent(paramMotionEvent);
        this.yDistance = 0.0F;
        this.xDistance = 0.0F;
        this.xLast = paramMotionEvent.getX();
        this.yLast = paramMotionEvent.getY();
      }
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.xDistance += Math.abs(f1 - this.xLast);
      this.yDistance += Math.abs(f2 - this.yLast);
      this.xLast = f1;
      this.yLast = f2;
    }
    while (this.xDistance <= this.yDistance);
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    try
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      return bool;
    }
    catch (java.lang.Exception paramMotionEvent)
    {
    }
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PullListViewWithViewPager
 * JD-Core Version:    0.6.0
 */