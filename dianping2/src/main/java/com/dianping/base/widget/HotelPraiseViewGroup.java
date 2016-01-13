package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.util.ViewUtils;

public class HotelPraiseViewGroup extends ViewGroup
{
  private static int VIEW_MARGIN_HEIGHT;
  private static int VIEW_MARGIN_WIDTH;
  static int rowNum = 0;
  Context context;

  static
  {
    VIEW_MARGIN_WIDTH = 0;
  }

  public HotelPraiseViewGroup(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  public HotelPraiseViewGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  public HotelPraiseViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.context = paramContext;
  }

  public int getRowNums()
  {
    return rowNum + 1;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i1 = getChildCount();
    VIEW_MARGIN_HEIGHT = ViewUtils.dip2px(this.context, 10.0F);
    VIEW_MARGIN_WIDTH = 0;
    paramInt4 = paramInt1;
    int j = 0;
    int i = 0;
    while (i < i1)
    {
      View localView = getChildAt(i);
      int i2 = localView.getMeasuredWidth();
      int i3 = localView.getMeasuredHeight();
      int n = paramInt4 + (VIEW_MARGIN_WIDTH + i2);
      int m = (VIEW_MARGIN_HEIGHT + i3) * j + VIEW_MARGIN_HEIGHT + i3 + paramInt2;
      paramInt4 = n;
      int k = j;
      if (n > paramInt3)
      {
        paramInt4 = VIEW_MARGIN_WIDTH + i2 + paramInt1;
        k = j + 1;
        m = (VIEW_MARGIN_HEIGHT + i3) * k + VIEW_MARGIN_HEIGHT + i3 + paramInt2;
      }
      localView.layout(paramInt4 - i2, m - i3, paramInt4, m);
      i += 1;
      j = k;
    }
    rowNum = j;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    while (i < getChildCount())
    {
      getChildAt(i).measure(0, 0);
      i += 1;
    }
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.HotelPraiseViewGroup
 * JD-Core Version:    0.6.0
 */