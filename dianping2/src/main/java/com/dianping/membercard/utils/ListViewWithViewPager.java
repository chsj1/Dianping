package com.dianping.membercard.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewWithViewPager extends ListView
{
  public ListViewWithViewPager(Context paramContext)
  {
    this(paramContext, null);
  }

  public ListViewWithViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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
 * Qualified Name:     com.dianping.membercard.utils.ListViewWithViewPager
 * JD-Core Version:    0.6.0
 */