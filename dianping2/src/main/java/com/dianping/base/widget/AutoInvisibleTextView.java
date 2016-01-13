package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AutoInvisibleTextView extends TextView
{
  private OnVisibilityChangedListener listener;

  public AutoInvisibleTextView(Context paramContext)
  {
    super(paramContext);
  }

  public AutoInvisibleTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private boolean isOverFlowed()
  {
    return getPaint().measureText(getText().toString()) > getAvailableWidth();
  }

  public int getAvailableWidth()
  {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 != paramInt3) && (isOverFlowed()))
      setVisibility(4);
  }

  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (this.listener != null)
      this.listener.onVisibilityChanged(paramInt);
  }

  public void setOnVisibilityChangedListener(OnVisibilityChangedListener paramOnVisibilityChangedListener)
  {
    this.listener = paramOnVisibilityChangedListener;
  }

  public static abstract interface OnVisibilityChangedListener
  {
    public abstract void onVisibilityChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AutoInvisibleTextView
 * JD-Core Version:    0.6.0
 */