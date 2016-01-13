package com.dianping.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class WrapHeightViewPager extends ViewPager
{
  public WrapHeightViewPager(Context paramContext)
  {
    super(paramContext);
  }

  public WrapHeightViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    paramInt2 = 0;
    while (paramInt2 < getChildCount())
    {
      View localView = getChildAt(paramInt2);
      localView.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(0, 0));
      int k = localView.getMeasuredHeight();
      int j = i;
      if (k > i)
        j = k;
      paramInt2 += 1;
      i = j;
    }
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(i, 1073741824));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.WrapHeightViewPager
 * JD-Core Version:    0.6.0
 */