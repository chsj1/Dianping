package com.dianping.base.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;

public class ExpandAnimation extends Animation
{
  private View mAnimatedView;
  private boolean mIsVisibleAfter = false;
  private int mMarginEnd;
  private int mMarginStart;
  private LinearLayout.LayoutParams mViewLayoutParams;
  private boolean mWasEndedAlready = false;
  OnExpendAnimationListener onAnimationListener;
  private OnExpendActionListener onExpendActionListener;

  public ExpandAnimation(View paramView, int paramInt)
  {
    setDuration(paramInt);
    this.mAnimatedView = paramView;
    this.mViewLayoutParams = ((LinearLayout.LayoutParams)paramView.getLayoutParams());
    boolean bool;
    if (this.mViewLayoutParams.bottomMargin == 0)
    {
      bool = true;
      this.mIsVisibleAfter = bool;
      this.mMarginStart = this.mViewLayoutParams.bottomMargin;
      if (this.mMarginStart != 0)
        break label106;
    }
    label106: for (paramInt = 0 - paramView.getHeight(); ; paramInt = 0)
    {
      this.mMarginEnd = paramInt;
      paramView.setVisibility(0);
      setAnimationListener(new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnimation)
        {
          if (ExpandAnimation.this.onAnimationListener != null)
            ExpandAnimation.this.onAnimationListener.onAnimationEnd();
        }

        public void onAnimationRepeat(Animation paramAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnimation)
        {
          if (ExpandAnimation.this.onAnimationListener != null)
            ExpandAnimation.this.onAnimationListener.onAnimationStart();
        }
      });
      return;
      bool = false;
      break;
    }
  }

  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    super.applyTransformation(paramFloat, paramTransformation);
    if (paramFloat < 1.0F)
    {
      this.mViewLayoutParams.bottomMargin = (this.mMarginStart + (int)((this.mMarginEnd - this.mMarginStart) * paramFloat));
      this.mAnimatedView.requestLayout();
      if (this.onExpendActionListener != null)
        this.onExpendActionListener.onExpendAction((View)this.mAnimatedView.getParent());
    }
    do
      return;
    while (this.mWasEndedAlready);
    this.mViewLayoutParams.bottomMargin = this.mMarginEnd;
    this.mAnimatedView.requestLayout();
    if (this.onExpendActionListener != null)
      this.onExpendActionListener.onExpendAction((View)this.mAnimatedView.getParent());
    if (this.mIsVisibleAfter)
      this.mAnimatedView.setVisibility(8);
    this.mWasEndedAlready = true;
  }

  public void setOnAnimationListener(OnExpendAnimationListener paramOnExpendAnimationListener)
  {
    this.onAnimationListener = paramOnExpendAnimationListener;
  }

  public void setOnExpendActionListener(OnExpendActionListener paramOnExpendActionListener)
  {
    this.onExpendActionListener = paramOnExpendActionListener;
  }

  public static abstract interface OnExpendActionListener
  {
    public abstract void onExpendAction(View paramView);
  }

  public static abstract interface OnExpendAnimationListener
  {
    public abstract void onAnimationEnd();

    public abstract void onAnimationStart();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ExpandAnimation
 * JD-Core Version:    0.6.0
 */