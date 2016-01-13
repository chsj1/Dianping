package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MeasuredTextView extends TextView
{
  private boolean flag = false;

  public MeasuredTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isFlag()
  {
    return this.flag;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.flag)
    {
      paramInt1 = Math.max(getMeasuredWidth(), getMeasuredHeight());
      setMeasuredDimension(paramInt1, paramInt1);
      return;
    }
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
  }

  public void setFlag(boolean paramBoolean)
  {
    this.flag = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MeasuredTextView
 * JD-Core Version:    0.6.0
 */