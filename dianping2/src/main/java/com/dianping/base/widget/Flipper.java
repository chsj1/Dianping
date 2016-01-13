package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;

public class Flipper<T> extends FrameLayout
{
  protected static final int ANIM_NONE = 0;
  private static final int ANIM_TRANS = 2;
  private static final int ANIM_TRANS_DURATION1 = 30;
  private static final int ANIM_TRANS_DURATION2 = 150;
  private static final int ANIM_TRANS_TO_NEXT = 1;
  private static final int ANIM_TRANS_TO_PREVIOUS = -1;
  private static final int FLING_VELOCITY = 500;
  protected FlipperAdapter<T> adapter;
  private int animationDuration;
  protected int animationMode = 0;
  private long animationStartMs;
  private int animationX1;
  private int animationX2;
  protected T currentItem;
  protected View currentView;
  private float flipDistance = 0.0F;
  protected GestureDetector gestureDetector = new GestureDetector(this.gestureListener);
  protected GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
  {
    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      Flipper.this.onFling(paramFloat1);
      return true;
    }

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      if ((Flipper.this.previousItem == null) && (Flipper.this.nextItem == null))
      {
        Flipper.this.isScrolling = false;
        return true;
      }
      Flipper.this.isScrolling = true;
      Flipper.this.onScrollX(paramMotionEvent1, paramMotionEvent2, paramFloat1);
      return true;
    }

    public void onShowPress(MotionEvent paramMotionEvent)
    {
      Flipper.this.animationMode = 0;
      super.onShowPress(paramMotionEvent);
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      Flipper.this.onTap();
      return true;
    }
  };
  protected boolean isScrolling;
  private int mItemSpaceAdjust;
  public NavigationDot navigationDot;
  protected T nextItem;
  protected View nextView;
  protected T previousItem;
  protected View previousView;

  public Flipper(Context paramContext)
  {
    this(paramContext, null);
  }

  public Flipper(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private boolean isEquals(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
  }

  private void recycle(View paramView)
  {
    if ((paramView != null) && (this.adapter != null))
      this.adapter.recycleView(paramView);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    long l;
    if ((this.animationMode == 2) || (this.animationMode == -1) || (this.animationMode == 1))
    {
      l = AnimationUtils.currentAnimationTimeMillis();
      if (this.animationStartMs + this.animationDuration >= l)
        break label112;
      if (this.animationMode != -1)
        break label84;
      this.adapter.onMoved(this.nextItem, this.currentItem);
      this.animationMode = 0;
      this.flipDistance = 0.0F;
    }
    while (true)
    {
      super.dispatchDraw(paramCanvas);
      return;
      label84: if (this.animationMode != 1)
        break;
      this.adapter.onMoved(this.previousItem, this.currentItem);
      break;
      label112: float f = (float)(l - this.animationStartMs) / this.animationDuration;
      this.flipDistance = (this.animationX1 + (int)((this.animationX2 - this.animationX1) * f));
      invalidate();
    }
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
    {
      int i = 0;
      if (this.previousView != null)
      {
        i = this.previousView.getVisibility();
        this.previousView.setVisibility(4);
      }
      int j = 0;
      if (this.nextView != null)
      {
        j = this.nextView.getVisibility();
        this.nextView.setVisibility(4);
      }
      try
      {
        boolean bool = super.dispatchTouchEvent(paramMotionEvent);
        return bool;
      }
      finally
      {
        if (this.previousView != null)
          this.previousView.setVisibility(i);
        if (this.nextView != null)
          this.nextView.setVisibility(j);
      }
    }
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    boolean bool2;
    if ((paramView instanceof NavigationDot))
      bool2 = super.drawChild(paramCanvas, paramView, paramLong);
    boolean bool1;
    do
    {
      return bool2;
      bool1 = true;
      if (paramView == this.previousView)
      {
        paramCanvas.save();
        paramCanvas.translate(-getWidth() + this.mItemSpaceAdjust - this.flipDistance, 0.0F);
        bool1 = super.drawChild(paramCanvas, paramView, paramLong);
        paramCanvas.restore();
      }
      if (paramView == this.nextView)
      {
        paramCanvas.save();
        paramCanvas.translate(getWidth() - this.mItemSpaceAdjust - this.flipDistance, 0.0F);
        bool1 = super.drawChild(paramCanvas, paramView, paramLong);
        paramCanvas.restore();
      }
      if (paramView == this.currentView)
      {
        paramCanvas.save();
        paramCanvas.translate(-this.flipDistance, 0.0F);
        bool1 = super.drawChild(paramCanvas, paramView, paramLong);
        paramCanvas.restore();
      }
      bool2 = bool1;
    }
    while (this.navigationDot == null);
    super.drawChild(paramCanvas, this.navigationDot, paramLong);
    return bool1;
  }

  public void enableNavigationDotView(int paramInt)
  {
    if (paramInt > 1)
    {
      if (this.navigationDot == null)
      {
        this.navigationDot = ((NavigationDot)LayoutInflater.from(getContext()).inflate(R.layout.navigation_dot, this, false));
        addView(this.navigationDot);
      }
      this.navigationDot.setTotalDot(paramInt);
    }
  }

  public float flipDistance()
  {
    return this.flipDistance;
  }

  public T getCurrentItem()
  {
    return this.currentItem;
  }

  public View getCurrentView()
  {
    return this.currentView;
  }

  public boolean moveToNext(boolean paramBoolean)
  {
    if (this.nextItem != null)
    {
      View localView = this.previousView;
      if (this.previousView != null)
        removeView(this.previousView);
      this.previousItem = this.currentItem;
      this.previousView = this.currentView;
      this.currentItem = this.nextItem;
      this.currentView = this.nextView;
      this.nextItem = this.adapter.getNextItem(this.currentItem);
      if (this.nextItem != null)
      {
        this.nextView = this.adapter.getView(this.nextItem, localView);
        if (this.nextView != null)
          addView(this.nextView);
        if (!paramBoolean)
          break label233;
        int i = getWidth();
        this.flipDistance -= i;
        this.animationMode = 1;
        this.animationX1 = (int)this.flipDistance;
        this.animationX2 = 0;
        this.animationStartMs = AnimationUtils.currentAnimationTimeMillis();
        this.animationDuration = ((int)(Math.abs(this.flipDistance) / i * 120.0F) + 30);
        invalidate();
        this.adapter.onMoving(this.previousItem, this.currentItem);
      }
      while (true)
      {
        if (this.navigationDot != null)
          this.navigationDot.moveToNext();
        return true;
        recycle(localView);
        this.nextView = null;
        break;
        label233: this.flipDistance = 0.0F;
        this.animationMode = 0;
        this.adapter.onMoved(this.previousItem, this.currentItem);
        invalidate();
      }
    }
    restorePosition(paramBoolean);
    return false;
  }

  public boolean moveToPrevious(boolean paramBoolean)
  {
    if (this.previousItem != null)
    {
      View localView = this.nextView;
      if (this.nextView != null)
        removeView(this.nextView);
      this.nextItem = this.currentItem;
      this.nextView = this.currentView;
      this.currentItem = this.previousItem;
      this.currentView = this.previousView;
      this.previousItem = this.adapter.getPreviousItem(this.currentItem);
      if (this.previousItem != null)
      {
        this.previousView = this.adapter.getView(this.previousItem, localView);
        if (this.previousView != null)
          addView(this.previousView);
        if (!paramBoolean)
          break label233;
        int i = getWidth();
        this.flipDistance += i;
        this.animationMode = -1;
        this.animationX1 = (int)this.flipDistance;
        this.animationX2 = 0;
        this.animationStartMs = AnimationUtils.currentAnimationTimeMillis();
        this.animationDuration = ((int)(Math.abs(this.flipDistance) / i * 120.0F) + 30);
        invalidate();
        this.adapter.onMoving(this.nextItem, this.currentItem);
      }
      while (true)
      {
        if (this.navigationDot != null)
          this.navigationDot.moveToPrevious();
        return true;
        recycle(localView);
        this.previousView = null;
        break;
        label233: this.flipDistance = 0.0F;
        this.animationMode = 0;
        this.adapter.onMoved(this.nextItem, this.currentItem);
        invalidate();
      }
    }
    restorePosition(paramBoolean);
    return false;
  }

  public void onFling(float paramFloat)
  {
    int i = getWidth();
    if ((paramFloat < -500.0F) || (this.flipDistance > i / 2.0F))
    {
      moveToNext(true);
      return;
    }
    if ((paramFloat > 500.0F) || (this.flipDistance < -i / 2.0F))
    {
      moveToPrevious(true);
      return;
    }
    restorePosition(true);
  }

  protected void onScrollX(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat)
  {
    this.flipDistance += paramFloat;
    invalidate();
  }

  public void onScrollXEnd()
  {
    int i = getWidth();
    if (this.flipDistance < -i / 2.0F)
    {
      moveToPrevious(true);
      return;
    }
    if (this.flipDistance > i / 2.0F)
    {
      moveToNext(true);
      return;
    }
    restorePosition(true);
  }

  protected void onTap()
  {
    this.adapter.onTap(this.currentItem);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.gestureDetector.onTouchEvent(paramMotionEvent))
    {
      if ((paramMotionEvent.getAction() == 1) && (this.isScrolling))
      {
        onScrollXEnd();
        this.isScrolling = false;
      }
      if (paramMotionEvent.getAction() == 3)
      {
        onScrollXEnd();
        this.isScrolling = false;
      }
    }
    return true;
  }

  public void restorePosition(boolean paramBoolean)
  {
    if (this.flipDistance == 0.0F)
      return;
    if (paramBoolean)
    {
      int i = getWidth();
      this.animationMode = 2;
      this.animationX1 = (int)this.flipDistance;
      this.animationX2 = 0;
      this.animationStartMs = AnimationUtils.currentAnimationTimeMillis();
      this.animationDuration = ((int)(Math.abs(this.flipDistance) / i * 120.0F) + 30);
      invalidate();
      return;
    }
    this.flipDistance = 0.0F;
    this.animationMode = 0;
    invalidate();
  }

  public void setAdapter(FlipperAdapter<T> paramFlipperAdapter)
  {
    this.adapter = paramFlipperAdapter;
  }

  public void setCurrentDot(int paramInt)
  {
    if (this.navigationDot != null)
      this.navigationDot.setCurrentIndex(paramInt);
  }

  public void setCurrentItem(T paramT)
  {
    Object localObject1 = this.currentItem;
    Object localObject2 = this.previousItem;
    Object localObject3 = this.nextItem;
    this.currentItem = paramT;
    this.previousItem = this.adapter.getPreviousItem(paramT);
    this.nextItem = this.adapter.getNextItem(paramT);
    if (!isEquals(this.currentItem, localObject1))
    {
      if (this.currentView != null)
        removeView(this.currentView);
      if (this.currentItem == null)
        break label287;
      this.currentView = this.adapter.getView(this.currentItem, this.currentView);
      if (this.currentView != null)
        addView(this.currentView);
    }
    if (!isEquals(this.previousItem, localObject2))
    {
      if (this.previousView != null)
        removeView(this.previousView);
      if (this.previousItem == null)
        break label303;
      this.previousView = this.adapter.getView(this.previousItem, this.previousView);
      label174: if (this.previousView != null)
        addView(this.previousView);
    }
    if (!isEquals(this.nextItem, localObject3))
    {
      if (this.nextView != null)
        removeView(this.nextView);
      if (this.nextItem == null)
        break label319;
    }
    for (this.nextView = this.adapter.getView(this.nextItem, this.nextView); ; this.nextView = null)
    {
      if (this.nextView != null)
        addView(this.nextView);
      if (!isEquals(localObject1, this.currentItem))
        this.adapter.onMoved(localObject1, this.currentItem);
      return;
      label287: recycle(this.currentView);
      this.currentView = null;
      break;
      label303: recycle(this.previousView);
      this.previousView = null;
      break label174;
      label319: recycle(this.nextView);
    }
  }

  public void setItemSpaceSpanAdjust(int paramInt)
  {
    this.mItemSpaceAdjust = paramInt;
  }

  public void update()
  {
    this.previousItem = this.adapter.getPreviousItem(this.currentItem);
    this.nextItem = this.adapter.getNextItem(this.currentItem);
    if (this.currentView != null)
      removeView(this.currentView);
    if (this.currentItem != null)
    {
      this.currentView = this.adapter.getView(this.currentItem, this.currentView);
      if (this.currentView != null)
        addView(this.currentView);
      if (this.previousView != null)
        removeView(this.previousView);
      if (this.previousItem == null)
        break label225;
      this.previousView = this.adapter.getView(this.previousItem, this.previousView);
      label135: if (this.previousView != null)
        addView(this.previousView);
      if (this.nextView != null)
        removeView(this.nextView);
      if (this.nextItem == null)
        break label241;
    }
    for (this.nextView = this.adapter.getView(this.nextItem, this.nextView); ; this.nextView = null)
    {
      if (this.nextView != null)
        addView(this.nextView);
      return;
      recycle(this.currentView);
      this.currentView = null;
      break;
      label225: recycle(this.previousView);
      this.previousView = null;
      break label135;
      label241: recycle(this.nextView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.Flipper
 * JD-Core Version:    0.6.0
 */