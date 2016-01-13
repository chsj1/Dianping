package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;

public class ExpandableHeightGridView extends GridView
{
  boolean expanded = false;
  AbsListView.LayoutParams mLp;
  boolean measureHeight = false;

  public ExpandableHeightGridView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public ExpandableHeightGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public ExpandableHeightGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void init()
  {
    this.mLp = new AbsListView.LayoutParams(-1, -2);
  }

  public boolean isExpanded()
  {
    return this.expanded;
  }

  public boolean isMeasureHeight()
  {
    return this.measureHeight;
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    if (isMeasureHeight())
    {
      paramInt2 = 0;
      int i = 0;
      if (i < Math.ceil(getChildCount() / 2.0D))
      {
        int j = getChildAt(i * 2).getHeight();
        int k;
        if (getChildAt(i * 2 + 1) != null)
        {
          k = getChildAt(i * 2 + 1).getHeight();
          if (j > k)
          {
            this.mLp.width = -1;
            this.mLp.height = j;
            getChildAt(i * 2 + 1).setLayoutParams(this.mLp);
            paramInt2 += j;
          }
        }
        while (true)
        {
          i += 1;
          break;
          this.mLp.width = -1;
          this.mLp.height = k;
          getChildAt(i * 2).setLayoutParams(this.mLp);
          paramInt2 += k;
          continue;
          paramInt2 += j;
        }
      }
      super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(paramInt2 + 15, 1073741824));
      return;
    }
    if (isExpanded())
    {
      super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
      getLayoutParams().height = getMeasuredHeight();
      return;
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public void setExpanded(boolean paramBoolean)
  {
    this.expanded = paramBoolean;
  }

  public void setMeasureHeight(boolean paramBoolean)
  {
    this.measureHeight = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ExpandableHeightGridView
 * JD-Core Version:    0.6.0
 */