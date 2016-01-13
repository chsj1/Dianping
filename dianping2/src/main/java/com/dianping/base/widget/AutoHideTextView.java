package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.dianping.v1.R.styleable;

public class AutoHideTextView extends TextView
{
  private boolean autoHide;
  private OnVisibilityChangedListener listener;
  private Paint mPaint;

  public AutoHideTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public AutoHideTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (this.mPaint == null)
      this.mPaint = new Paint();
    if (paramAttributeSet == null)
      return;
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AutoHideTextView);
    this.autoHide = paramContext.getBoolean(R.styleable.AutoHideTextView_auto_hide, false);
    paramContext.recycle();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if ((View.MeasureSpec.getMode(paramInt1) == 0) || (!this.autoHide))
      return;
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    float f1 = getPaint().measureText(getText().toString());
    float f2 = getPaddingLeft();
    float f3 = getPaddingRight();
    if (paramInt1 >= f1 + f2 + f3)
    {
      setVisibility(0);
      return;
    }
    setVisibility(8);
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

  public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType)
  {
    super.setText(paramCharSequence, paramBufferType);
    setVisibility(0);
  }

  public static abstract interface OnVisibilityChangedListener
  {
    public abstract void onVisibilityChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AutoHideTextView
 * JD-Core Version:    0.6.0
 */