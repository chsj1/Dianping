package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class ListViewWithViewPager extends PullToRefreshListView
{
  public ListViewWithViewPager(Context paramContext)
  {
    this(paramContext, null);
  }

  public ListViewWithViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setCacheColorHint(0);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onInterceptTouchEvent(paramMotionEvent);
    return false;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ListViewWithViewPager
 * JD-Core Version:    0.6.0
 */