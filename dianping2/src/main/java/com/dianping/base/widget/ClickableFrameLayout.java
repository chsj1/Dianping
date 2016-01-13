package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ClickableFrameLayout extends FrameLayout
{
  ClickListener clickListener;

  public ClickableFrameLayout(Context paramContext)
  {
    super(paramContext);
  }

  public ClickableFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.clickListener == null)
      return super.onInterceptTouchEvent(paramMotionEvent);
    return this.clickListener.isClickable();
  }

  public void setClickListener(ClickListener paramClickListener)
  {
    this.clickListener = paramClickListener;
  }

  public static abstract interface ClickListener
  {
    public abstract boolean isClickable();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ClickableFrameLayout
 * JD-Core Version:    0.6.0
 */