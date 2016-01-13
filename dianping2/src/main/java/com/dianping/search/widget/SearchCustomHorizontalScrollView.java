package com.dianping.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class SearchCustomHorizontalScrollView extends HorizontalScrollView
{
  public SearchCustomHorizontalScrollView(Context paramContext)
  {
    super(paramContext);
  }

  public SearchCustomHorizontalScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchCustomHorizontalScrollView
 * JD-Core Version:    0.6.0
 */