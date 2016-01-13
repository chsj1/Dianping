package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewWithScrollChild extends ListView
{
  private float xDistance;
  private float xLast;
  private float yDistance;
  private float yLast;

  public ListViewWithScrollChild(Context paramContext)
  {
    super(paramContext);
  }

  public ListViewWithScrollChild(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ListViewWithScrollChild(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ListViewWithScrollChild
 * JD-Core Version:    0.6.0
 */