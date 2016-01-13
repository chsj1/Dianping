package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.dianping.util.Log;

public class DPScrollView extends ScrollView
{
  private static final int DONE = 3;
  private static final int LOADING = 4;
  private static final int PULL_To_REFRESH = 1;
  private static final float RATIO = 1.3F;
  private static final int REFRESHING = 2;
  private static final int RELEASE_To_REFRESH = 0;
  private static final String TAG = DPScrollView.class.getSimpleName();
  private int headContentHeight;
  private View headView;
  private LinearLayout innerLayout;
  private boolean isBack;
  private boolean isRecored;
  private boolean isShowHeaderable;
  boolean mHasTouchDownInside;
  View mInsideScroll;
  boolean mInsideScrollNeedUp;
  private OnPullToShowHeaderListener onPullToShowHeaderListener;
  private ScrollViewListener scrollViewListener;
  private int startY;
  private int state;

  public DPScrollView(Context paramContext)
  {
    this(paramContext, null);
    init(paramContext);
  }

  public DPScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  private void changeHeaderViewByState()
  {
    switch (this.state)
    {
    case 0:
    default:
    case 1:
      do
        return;
      while (!this.isBack);
      this.isBack = false;
      return;
    case 2:
      this.innerLayout.setPadding(0, 0, 0, 0);
      this.isShowHeaderable = false;
      return;
    case 3:
    }
    this.innerLayout.setPadding(0, this.headContentHeight * -1, 0, 0);
  }

  private void init(Context paramContext)
  {
    this.state = 3;
  }

  private void onPullToShowHeader()
  {
    if (this.scrollViewListener != null)
      this.onPullToShowHeaderListener.onPullToShowHeader();
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = false;
    if (this.mInsideScroll == null)
      try
      {
        bool = super.dispatchTouchEvent(paramMotionEvent);
        return bool;
      }
      catch (java.lang.Exception paramMotionEvent)
      {
        return false;
      }
    if (this.mInsideScroll.getVisibility() == 0)
    {
      int i = this.mInsideScroll.getTop() - getScrollY();
      int j = this.mInsideScroll.getHeight();
      float f = paramMotionEvent.getY();
      if ((f > i) && (f < i + j))
      {
        if (paramMotionEvent.getAction() != 1)
          bool = true;
        this.mInsideScrollNeedUp = bool;
        this.mInsideScroll.dispatchTouchEvent(paramMotionEvent);
        return true;
      }
      if ((this.mInsideScrollNeedUp) && (paramMotionEvent.getAction() == 1))
      {
        this.mInsideScrollNeedUp = false;
        this.mInsideScroll.dispatchTouchEvent(paramMotionEvent);
      }
    }
    if (paramMotionEvent.getAction() == 0)
      this.mHasTouchDownInside = true;
    if (!this.mHasTouchDownInside)
      return true;
    if (paramMotionEvent.getAction() == 1)
      this.mHasTouchDownInside = false;
    try
    {
      bool = super.dispatchTouchEvent(paramMotionEvent);
      return bool;
    }
    catch (java.lang.Exception paramMotionEvent)
    {
    }
    return false;
  }

  public View getHeadView()
  {
    return this.headView;
  }

  public int getPaddingTop()
  {
    if (this.innerLayout != null)
      return this.innerLayout.getPaddingTop();
    return 0;
  }

  public boolean isPulling()
  {
    return this.isShowHeaderable;
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.scrollViewListener != null)
      this.scrollViewListener.onScrollChanged(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.isShowHeaderable)
      switch (paramMotionEvent.getAction())
      {
      default:
      case 0:
      case 1:
      case 2:
      }
    while (true)
    {
      return super.onTouchEvent(paramMotionEvent);
      if ((getScrollY() != 0) || (this.isRecored))
        continue;
      this.isRecored = true;
      this.startY = (int)paramMotionEvent.getY();
      Log.i(TAG, "在down时候记录当前位置‘");
      continue;
      if ((this.state == 2) || (this.state == 4) || (this.state == 3));
      while (true)
      {
        this.isRecored = false;
        this.isBack = false;
        break;
        if (this.state == 1)
        {
          this.state = 3;
          changeHeaderViewByState();
          continue;
        }
        if (this.state != 0)
          continue;
        this.state = 2;
        changeHeaderViewByState();
        onPullToShowHeader();
      }
      int i = (int)paramMotionEvent.getY();
      if ((!this.isRecored) && (getScrollY() == 0))
      {
        this.isRecored = true;
        this.startY = i;
      }
      if ((this.state == 2) || (!this.isRecored) || (this.state == 4))
        continue;
      if (this.state == 0)
      {
        if (((i - this.startY) / 1.3F < this.headContentHeight) && (i - this.startY > 0))
        {
          this.state = 1;
          changeHeaderViewByState();
        }
        while (true)
        {
          this.innerLayout.setPadding(0, (int)((i - this.startY) / 1.3F - this.headContentHeight), 0, 0);
          break;
          if (i - this.startY > 0)
            continue;
          this.state = 3;
          changeHeaderViewByState();
        }
      }
      if (this.state == 1)
      {
        if ((i - this.startY) / 1.3F >= this.headContentHeight)
        {
          this.state = 0;
          this.isBack = true;
          changeHeaderViewByState();
        }
        while (true)
        {
          this.innerLayout.setPadding(0, this.headContentHeight * -1 + (int)((i - this.startY) / 1.3F), 0, 0);
          break;
          if (i - this.startY > 0)
            continue;
          this.state = 3;
          changeHeaderViewByState();
        }
      }
      if ((this.state != 3) || (i - this.startY <= 0))
        continue;
      this.state = 1;
      changeHeaderViewByState();
    }
  }

  public void setInsideScrollViewId(View paramView)
  {
    this.mInsideScroll = paramView;
  }

  public void setScrollViewListener(ScrollViewListener paramScrollViewListener)
  {
    this.scrollViewListener = paramScrollViewListener;
  }

  public static abstract interface OnPullToShowHeaderListener
  {
    public abstract void onPullToShowHeader();
  }

  public static abstract interface ScrollViewListener
  {
    public abstract void onScrollChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DPScrollView
 * JD-Core Version:    0.6.0
 */