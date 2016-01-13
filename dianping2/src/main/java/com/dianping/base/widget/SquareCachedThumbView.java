package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SquareCachedThumbView extends CachedNetworkThumbView
{
  public SquareCachedThumbView(Context paramContext)
  {
    super(paramContext);
  }

  public SquareCachedThumbView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SquareCachedThumbView
 * JD-Core Version:    0.6.0
 */