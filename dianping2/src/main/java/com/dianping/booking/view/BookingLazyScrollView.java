package com.dianping.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class BookingLazyScrollView extends ScrollView
{
  OnScrollListener onScrollListener;

  public BookingLazyScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    onTouchEvent(paramMotionEvent);
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.onScrollListener != null)
      this.onScrollListener.onAutoScroll(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    this.onScrollListener = paramOnScrollListener;
  }

  public static abstract interface OnScrollListener
  {
    public abstract void onAutoScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.BookingLazyScrollView
 * JD-Core Version:    0.6.0
 */