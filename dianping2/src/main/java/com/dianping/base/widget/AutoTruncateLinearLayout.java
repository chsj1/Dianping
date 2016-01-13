package com.dianping.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import com.dianping.widget.view.NovaLinearLayout;

public class AutoTruncateLinearLayout extends NovaLinearLayout
{
  public AutoTruncateLinearLayout(Context paramContext)
  {
    super(paramContext);
  }

  public AutoTruncateLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  @SuppressLint({"NewApi"})
  public AutoTruncateLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if ((getOrientation() == 0) && (View.MeasureSpec.getSize(paramInt1) > 0))
    {
      paramInt2 = getPaddingLeft() + getPaddingRight();
      int i = View.MeasureSpec.getSize(paramInt1);
      paramInt1 = 0;
      while (paramInt1 < getChildCount())
      {
        View localView = getChildAt(paramInt1);
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)localView.getLayoutParams();
        paramInt2 += localView.getMeasuredWidth() + localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin;
        if (paramInt2 > i)
          localView.setVisibility(8);
        paramInt1 += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AutoTruncateLinearLayout
 * JD-Core Version:    0.6.0
 */