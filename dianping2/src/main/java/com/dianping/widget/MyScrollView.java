package com.dianping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.dianping.widget.view.NovaScrollView;

public class MyScrollView extends NovaScrollView
{
  private static final int STOP_TIME = 500;
  private int initialPosition;
  private float lastX;
  private float lastY;
  private OnScrollStoppedListener onScrollStoppedListener;
  private OnScrollListener scrollListener;
  private Runnable scrollerTask = new Runnable()
  {
    public void run()
    {
      int i = MyScrollView.this.getScrollY();
      if (MyScrollView.this.initialPosition - i == 0)
      {
        if (MyScrollView.this.onScrollStoppedListener != null)
          MyScrollView.this.onScrollStoppedListener.onScrollStopped();
        return;
      }
      MyScrollView.access$002(MyScrollView.this, MyScrollView.this.getScrollY());
      MyScrollView.this.postDelayed(MyScrollView.this.scrollerTask, 500L);
    }
  };
  private float xDistance;
  private float yDistance;

  public MyScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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
        this.lastX = paramMotionEvent.getX();
        this.lastY = paramMotionEvent.getY();
      }
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.xDistance += Math.abs(f1 - this.lastX);
      this.yDistance += Math.abs(f2 - this.lastY);
      this.lastX = f1;
      this.lastY = f2;
    }
    while (this.xDistance <= this.yDistance);
    return false;
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.scrollListener != null)
      this.scrollListener.onScroll(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    this.scrollListener = paramOnScrollListener;
  }

  public void setOnScrollStoppedListener(OnScrollStoppedListener paramOnScrollStoppedListener)
  {
    this.onScrollStoppedListener = paramOnScrollStoppedListener;
  }

  public void startScrollerTask()
  {
    this.initialPosition = getScrollY();
    postDelayed(this.scrollerTask, 500L);
  }

  public static abstract interface OnScrollListener
  {
    public abstract void onScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }

  public static abstract interface OnScrollStoppedListener
  {
    public abstract void onScrollStopped();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.MyScrollView
 * JD-Core Version:    0.6.0
 */