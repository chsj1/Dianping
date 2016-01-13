package com.dianping.widget;

import android.content.Context;
import android.util.AttributeSet;

public class SquareNetworkImageView extends NetworkImageView
{
  public SquareNetworkImageView(Context paramContext)
  {
    super(paramContext);
  }

  public SquareNetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
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
 * Qualified Name:     com.dianping.widget.SquareNetworkImageView
 * JD-Core Version:    0.6.0
 */