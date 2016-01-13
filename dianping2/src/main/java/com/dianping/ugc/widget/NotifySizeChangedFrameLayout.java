package com.dianping.ugc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class NotifySizeChangedFrameLayout extends FrameLayout
{
  private OnSizeChangedListener sizeChangedListener;

  public NotifySizeChangedFrameLayout(Context paramContext)
  {
    super(paramContext);
  }

  public NotifySizeChangedFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.sizeChangedListener != null)
      this.sizeChangedListener.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setOnSizeChangedListener(OnSizeChangedListener paramOnSizeChangedListener)
  {
    this.sizeChangedListener = paramOnSizeChangedListener;
  }

  public static abstract interface OnSizeChangedListener
  {
    public abstract void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.NotifySizeChangedFrameLayout
 * JD-Core Version:    0.6.0
 */