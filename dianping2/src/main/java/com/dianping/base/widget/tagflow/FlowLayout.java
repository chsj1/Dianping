package com.dianping.base.widget.tagflow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.dianping.v1.R.styleable;
import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup
{
  protected List<List<View>> mAllViews = new ArrayList();
  protected List<Integer> mLineHeight = new ArrayList();
  private int mNumLine = 2147483647;

  public FlowLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public FlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }

  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }

  protected int getNumLine()
  {
    return this.mNumLine;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mAllViews.clear();
    this.mLineHeight.clear();
    int j = getWidth();
    paramInt2 = 0;
    paramInt1 = 0;
    Object localObject1 = new ArrayList();
    int k = getChildCount();
    paramInt3 = 0;
    Object localObject3;
    int n;
    int m;
    Object localObject2;
    int i;
    if (paramInt3 < k)
    {
      localObject3 = getChildAt(paramInt3);
      if (((View)localObject3).getVisibility() == 8);
      while (true)
      {
        paramInt3 += 1;
        break;
        LayoutParams localLayoutParams = (LayoutParams)((View)localObject3).getLayoutParams();
        n = ((View)localObject3).getMeasuredWidth();
        m = ((View)localObject3).getMeasuredHeight();
        paramInt4 = paramInt1;
        localObject2 = localObject1;
        i = paramInt2;
        if (n + paramInt2 + localLayoutParams.leftMargin + localLayoutParams.rightMargin > j - getPaddingLeft() - getPaddingRight())
        {
          this.mLineHeight.add(Integer.valueOf(paramInt1));
          this.mAllViews.add(localObject1);
          i = 0;
          paramInt4 = localLayoutParams.topMargin + m + localLayoutParams.bottomMargin;
          localObject2 = new ArrayList();
        }
        paramInt2 = i + (localLayoutParams.leftMargin + n + localLayoutParams.rightMargin);
        paramInt1 = Math.max(paramInt4, localLayoutParams.topMargin + m + localLayoutParams.bottomMargin);
        ((List)localObject2).add(localObject3);
        localObject1 = localObject2;
      }
    }
    this.mLineHeight.add(Integer.valueOf(paramInt1));
    this.mAllViews.add(localObject1);
    paramInt1 = getPaddingLeft();
    paramInt3 = getPaddingTop();
    paramInt4 = this.mAllViews.size();
    paramInt2 = paramInt4;
    if (paramInt4 >= this.mNumLine)
      paramInt2 = this.mNumLine;
    paramInt4 = 0;
    while (paramInt4 < paramInt2)
    {
      localObject1 = (List)this.mAllViews.get(paramInt4);
      m = ((Integer)this.mLineHeight.get(paramInt4)).intValue();
      j = 0;
      i = paramInt1;
      paramInt1 = j;
      while (paramInt1 < ((List)localObject1).size())
      {
        localObject2 = (View)((List)localObject1).get(paramInt1);
        if (((View)localObject2).getVisibility() == 8)
        {
          paramInt1 += 1;
          continue;
        }
        localObject3 = (LayoutParams)((View)localObject2).getLayoutParams();
        n = i + ((LayoutParams)localObject3).leftMargin;
        int i1 = ((View)localObject2).getMeasuredWidth();
        if (((LayoutParams)localObject3).gravity == 16)
          k = (m - ((View)localObject2).getMeasuredHeight()) / 2 + paramInt3 + ((LayoutParams)localObject3).topMargin;
        for (j = k + ((View)localObject2).getMeasuredHeight(); ; j = k + ((View)localObject2).getMeasuredHeight())
        {
          ((View)localObject2).layout(n, k, n + i1, j);
          i += ((View)localObject2).getMeasuredWidth() + ((LayoutParams)localObject3).leftMargin + ((LayoutParams)localObject3).rightMargin;
          break;
          k = paramInt3 + ((LayoutParams)localObject3).topMargin;
        }
      }
      paramInt1 = getPaddingLeft();
      paramInt3 += m;
      paramInt4 += 1;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i3 = View.MeasureSpec.getSize(paramInt1);
    int i7 = View.MeasureSpec.getMode(paramInt1);
    int i2 = View.MeasureSpec.getSize(paramInt2);
    int i6 = View.MeasureSpec.getMode(paramInt2);
    int m = 0;
    int n = 0;
    int j = 0;
    int i = 0;
    int i1 = -1;
    int i8 = getChildCount();
    int k = 0;
    int i5;
    int i4;
    while (k < i8)
    {
      View localView = getChildAt(k);
      if (localView.getVisibility() == 8)
      {
        k += 1;
        continue;
      }
      measureChild(localView, paramInt1, paramInt2);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      i5 = localView.getMeasuredWidth() + localLayoutParams.leftMargin + localLayoutParams.rightMargin;
      i4 = localView.getMeasuredHeight() + localLayoutParams.topMargin + localLayoutParams.bottomMargin;
      if (j + i5 <= i3 - getPaddingLeft() - getPaddingRight())
        break label242;
      i1 += 1;
      if (i1 != this.mNumLine - 1)
        break label215;
    }
    paramInt2 = Math.max(j, m);
    if (this.mNumLine == 1)
    {
      paramInt1 = i;
      label188: if (i7 != 1073741824)
        break label270;
      paramInt2 = i3;
      label198: if (i6 != 1073741824)
        break label285;
    }
    label270: label285: for (paramInt1 = i2; ; paramInt1 = getPaddingTop() + paramInt1 + getPaddingBottom())
    {
      setMeasuredDimension(paramInt2, paramInt1);
      return;
      label215: m = Math.max(m, j);
      j = i5;
      n += i;
      i = i4;
      break;
      label242: j += i5;
      i = Math.max(i, i4);
      break;
      paramInt1 = n + i;
      break label188;
      paramInt2 = getPaddingLeft() + paramInt2 + getPaddingRight();
      break label198;
    }
  }

  protected void setNumLine(int paramInt)
  {
    this.mNumLine = paramInt;
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    public int gravity = 16;

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TagFlowLayout);
      this.gravity = paramContext.getInt(R.styleable.TagFlowLayout_android_layout_gravity, this.gravity);
      paramContext.recycle();
    }

    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.tagflow.FlowLayout
 * JD-Core Version:    0.6.0
 */