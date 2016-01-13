package com.dianping.base.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class RouteFrameLayout extends FrameLayout
{
  int ANIMATION_BOUND_VALUE;
  int BOTTOM_LIST_HEIGHT;
  int DRAG_BAR_VALUE;
  int FAST_ANIMATION_DURATION = 100;
  int MAX_ANIMATION_DURATION = 600;
  int MAX_MOVE_VALUE;
  private ImageView arrowView;
  int arrowViewSrcDown = R.drawable.navibar_icon_arrow_down_ed;
  int arrowViewSrcUp = R.drawable.navibar_icon_arrow_up;
  boolean isAnimating;
  private LayoutClickListener layoutClickListener;
  private LinearLayout routeSummary;
  private View scrollView;
  int state;

  public RouteFrameLayout(Context paramContext)
  {
    super(paramContext);
  }

  public RouteFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public RouteFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.routeSummary = ((LinearLayout)findViewById(R.id.route_cont));
    this.arrowView = ((ImageView)findViewById(R.id.arrow));
    this.scrollView = findViewById(R.id.route_step);
    this.MAX_MOVE_VALUE = ViewUtils.dip2px(getContext(), 20.0F);
    this.DRAG_BAR_VALUE = ViewUtils.dip2px(getContext(), 51.0F);
    this.ANIMATION_BOUND_VALUE = ViewUtils.dip2px(getContext(), 15.0F);
    this.BOTTOM_LIST_HEIGHT = ViewUtils.dip2px(getContext(), 80.0F);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.routeSummary.getTop();
    int j = this.routeSummary.getBottom();
    int k = this.routeSummary.getLeft();
    int m = this.routeSummary.getRight();
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (i != j)
    {
      this.routeSummary.layout(k, i, m, j);
      this.scrollView.layout(k, this.DRAG_BAR_VALUE, m, j - i);
    }
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.layoutClickListener = new LayoutClickListener(paramInt1, paramInt2);
    this.routeSummary.setOnTouchListener(this.layoutClickListener);
    if (this.state == 0)
    {
      this.routeSummary.layout(0, 0, paramInt1, paramInt2);
      this.arrowView.setImageResource(this.arrowViewSrcDown);
      return;
    }
    this.routeSummary.layout(0, paramInt2 - this.DRAG_BAR_VALUE - this.BOTTOM_LIST_HEIGHT, paramInt1, paramInt2);
    this.arrowView.setImageResource(this.arrowViewSrcUp);
  }

  public void setArrowSrc(int paramInt1, int paramInt2)
  {
    this.arrowViewSrcUp = paramInt1;
    this.arrowViewSrcDown = paramInt2;
  }

  public void setDragBarValue(int paramInt)
  {
    this.DRAG_BAR_VALUE = ViewUtils.dip2px(getContext(), paramInt);
  }

  public void updateFrameLayout(int paramInt)
  {
    this.state = paramInt;
  }

  class LayoutClickListener
    implements View.OnTouchListener, Animator.AnimatorListener
  {
    int containerHeight;

    public LayoutClickListener(int paramInt1, int arg3)
    {
      int i;
      this.containerHeight = i;
    }

    private void move2Bottom(int paramInt)
    {
      int i = paramInt;
      if (paramInt < 100)
      {
        i = 100;
        Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
      }
      RouteFrameLayout.this.state = 2;
      if ((i == RouteFrameLayout.this.FAST_ANIMATION_DURATION) || (i < 200));
      for (ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(RouteFrameLayout.this.routeSummary, "translationY", new float[] { 0.0F, this.containerHeight - RouteFrameLayout.access$000(RouteFrameLayout.this).getTop() - RouteFrameLayout.this.DRAG_BAR_VALUE - RouteFrameLayout.this.BOTTOM_LIST_HEIGHT }).setDuration(i); ; localObjectAnimator = ObjectAnimator.ofFloat(RouteFrameLayout.this.routeSummary, "translationY", new float[] { 0.0F, paramInt, paramInt - RouteFrameLayout.this.ANIMATION_BOUND_VALUE, paramInt }).setDuration(i))
      {
        RouteFrameLayout.this.arrowView.setImageResource(RouteFrameLayout.this.arrowViewSrcUp);
        localObjectAnimator.addListener(this);
        localObjectAnimator.start();
        return;
        paramInt = this.containerHeight - RouteFrameLayout.this.routeSummary.getTop() - RouteFrameLayout.this.DRAG_BAR_VALUE - RouteFrameLayout.this.BOTTOM_LIST_HEIGHT;
      }
    }

    private void move2Top(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        RouteFrameLayout.this.routeSummary.layout(0, RouteFrameLayout.this.routeSummary.getTop(), RouteFrameLayout.this.routeSummary.getWidth(), RouteFrameLayout.this.routeSummary.getTop() + this.containerHeight);
        RouteFrameLayout.this.scrollView.layout(0, RouteFrameLayout.this.scrollView.getTop(), RouteFrameLayout.this.routeSummary.getWidth(), this.containerHeight);
        RouteFrameLayout.this.scrollView.requestLayout();
      }
      int i = paramInt;
      if (paramInt < 100)
      {
        i = 100;
        Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
      }
      RouteFrameLayout.this.state = 0;
      if ((i == RouteFrameLayout.this.FAST_ANIMATION_DURATION) || (i < 200));
      for (ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(RouteFrameLayout.this.routeSummary, "translationY", new float[] { 0.0F, -RouteFrameLayout.access$000(RouteFrameLayout.this).getTop() }).setDuration(i); ; localObjectAnimator = ObjectAnimator.ofFloat(RouteFrameLayout.this.routeSummary, "translationY", new float[] { 0.0F, -RouteFrameLayout.access$000(RouteFrameLayout.this).getTop(), -RouteFrameLayout.access$000(RouteFrameLayout.this).getTop() + RouteFrameLayout.this.ANIMATION_BOUND_VALUE, -RouteFrameLayout.access$000(RouteFrameLayout.this).getTop() }).setDuration(i))
      {
        RouteFrameLayout.this.arrowView.setImageResource(RouteFrameLayout.this.arrowViewSrcDown);
        localObjectAnimator.addListener(this);
        localObjectAnimator.start();
        return;
      }
    }

    private void tapAnimation()
    {
      if (RouteFrameLayout.this.state == 0)
        move2Bottom(RouteFrameLayout.this.MAX_ANIMATION_DURATION);
      do
      {
        return;
        if (RouteFrameLayout.this.state != 2)
          continue;
        move2Top(RouteFrameLayout.this.MAX_ANIMATION_DURATION, true);
        return;
      }
      while (RouteFrameLayout.this.state != 1);
      move2Top(RouteFrameLayout.this.MAX_ANIMATION_DURATION, true);
    }

    public void onAnimationCancel(Animator paramAnimator)
    {
      RouteFrameLayout.this.isAnimating = false;
    }

    public void onAnimationEnd(Animator paramAnimator)
    {
      RouteFrameLayout.this.isAnimating = false;
      int i = this.containerHeight;
      switch (RouteFrameLayout.this.state)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        if (Build.VERSION.SDK_INT <= 15)
          RouteFrameLayout.this.requestLayout();
        return;
        RouteFrameLayout.this.routeSummary.layout(0, 0, RouteFrameLayout.this.routeSummary.getWidth(), i);
        ViewHelper.setTranslationY(RouteFrameLayout.this.routeSummary, 0.0F);
        RouteFrameLayout.this.scrollView.layout(0, RouteFrameLayout.this.scrollView.getTop(), RouteFrameLayout.this.scrollView.getWidth(), i - RouteFrameLayout.this.routeSummary.getTop());
        continue;
        RouteFrameLayout.this.routeSummary.layout(0, this.containerHeight / 2, RouteFrameLayout.this.routeSummary.getWidth(), i);
        ViewHelper.setTranslationY(RouteFrameLayout.this.routeSummary, 0.0F);
        RouteFrameLayout.this.scrollView.layout(0, RouteFrameLayout.this.DRAG_BAR_VALUE, RouteFrameLayout.this.scrollView.getWidth(), i - this.containerHeight / 2);
        continue;
        RouteFrameLayout.this.routeSummary.layout(0, i - RouteFrameLayout.this.DRAG_BAR_VALUE - RouteFrameLayout.this.BOTTOM_LIST_HEIGHT, RouteFrameLayout.this.routeSummary.getWidth(), i);
        ViewHelper.setTranslationY(RouteFrameLayout.this.routeSummary, 0.0F);
        RouteFrameLayout.this.scrollView.layout(0, RouteFrameLayout.this.DRAG_BAR_VALUE, RouteFrameLayout.this.scrollView.getWidth(), RouteFrameLayout.this.DRAG_BAR_VALUE + RouteFrameLayout.this.BOTTOM_LIST_HEIGHT);
      }
    }

    public void onAnimationRepeat(Animator paramAnimator)
    {
    }

    public void onAnimationStart(Animator paramAnimator)
    {
      RouteFrameLayout.this.isAnimating = true;
      if (Build.VERSION.SDK_INT <= 15)
        RouteFrameLayout.this.requestLayout();
    }

    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      if (RouteFrameLayout.this.isAnimating)
        return true;
      switch (paramMotionEvent.getAction())
      {
      default:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        return false;
        tapAnimation();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RouteFrameLayout
 * JD-Core Version:    0.6.0
 */