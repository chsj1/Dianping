package com.dianping.base.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LazyScrollView extends ScrollView
{
  Handler handler;
  boolean moved;
  OnScrollListener onScrollListener;
  View.OnTouchListener onTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      switch (paramMotionEvent.getAction())
      {
      case 0:
      default:
      case 1:
      }
      while (true)
      {
        return false;
        if ((LazyScrollView.this.view == null) || (LazyScrollView.this.onScrollListener == null))
          continue;
        LazyScrollView.this.handler.sendMessageDelayed(LazyScrollView.this.handler.obtainMessage(1), 200L);
      }
    }
  };
  LinearLayout view;

  public LazyScrollView(Context paramContext)
  {
    super(paramContext);
  }

  public LazyScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public LazyScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void init()
  {
    setOnTouchListener(this.onTouchListener);
    this.handler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        switch (paramMessage.what)
        {
        default:
        case 1:
        }
        do
          while (true)
          {
            return;
            if ((LazyScrollView.this.view.getChildAt(0).getMeasuredHeight() - 20 <= LazyScrollView.this.getScrollY() + LazyScrollView.this.getHeight()) || (LazyScrollView.this.view.getChildAt(1).getMeasuredHeight() - 20 <= LazyScrollView.this.getScrollY() + LazyScrollView.this.getHeight()))
            {
              if (LazyScrollView.this.onScrollListener == null)
                continue;
              LazyScrollView.this.onScrollListener.onBottom();
              return;
            }
            if (LazyScrollView.this.getScrollY() != 0)
              break;
            if (LazyScrollView.this.onScrollListener == null)
              continue;
            LazyScrollView.this.onScrollListener.onTop();
            return;
          }
        while (LazyScrollView.this.onScrollListener == null);
        LazyScrollView.this.onScrollListener.onScroll();
      }
    };
  }

  public int computeVerticalScrollOffset()
  {
    return super.computeVerticalScrollOffset();
  }

  public int computeVerticalScrollRange()
  {
    return super.computeHorizontalScrollRange();
  }

  public void getView()
  {
    this.view = ((LinearLayout)getChildAt(0));
    if (this.view != null)
      init();
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!this.moved)
    {
      smoothScrollBy(0, 1);
      this.moved = true;
    }
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.onScrollListener.onAutoScroll(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void scrollTo(int paramInt1, int paramInt2)
  {
    super.scrollTo(paramInt1, paramInt2);
  }

  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    this.onScrollListener = paramOnScrollListener;
  }

  public static abstract interface OnScrollListener
  {
    public abstract void onAutoScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract void onBottom();

    public abstract void onScroll();

    public abstract void onTop();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.LazyScrollView
 * JD-Core Version:    0.6.0
 */