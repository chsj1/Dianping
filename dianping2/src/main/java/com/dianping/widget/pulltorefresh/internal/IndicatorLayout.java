package com.dianping.widget.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;

@SuppressLint({"ViewConstructor"})
public class IndicatorLayout extends FrameLayout
  implements Animation.AnimationListener
{
  static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
  private ImageView mArrowImageView;
  private Animation mInAnim;
  private Animation mOutAnim;
  private final Animation mResetRotateAnimation;
  private final Animation mRotateAnimation;

  public IndicatorLayout(Context paramContext, PullToRefreshBase.Mode paramMode)
  {
    super(paramContext);
    this.mArrowImageView = new ImageView(paramContext);
    Drawable localDrawable = getResources().getDrawable(R.drawable.indicator_arrow);
    this.mArrowImageView.setImageDrawable(localDrawable);
    int i = getResources().getDimensionPixelSize(R.dimen.indicator_internal_padding);
    this.mArrowImageView.setPadding(i, i, i, i);
    addView(this.mArrowImageView);
    int j;
    switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[paramMode.ordinal()])
    {
    default:
      i = R.anim.slide_in_from_top;
      j = R.anim.slide_out_to_top;
      setBackgroundResource(R.drawable.indicator_bg_top);
    case 1:
    }
    while (true)
    {
      this.mInAnim = AnimationUtils.loadAnimation(paramContext, i);
      this.mInAnim.setAnimationListener(this);
      this.mOutAnim = AnimationUtils.loadAnimation(paramContext, j);
      this.mOutAnim.setAnimationListener(this);
      paramContext = new LinearInterpolator();
      this.mRotateAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
      this.mRotateAnimation.setInterpolator(paramContext);
      this.mRotateAnimation.setDuration(150L);
      this.mRotateAnimation.setFillAfter(true);
      this.mResetRotateAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
      this.mResetRotateAnimation.setInterpolator(paramContext);
      this.mResetRotateAnimation.setDuration(150L);
      this.mResetRotateAnimation.setFillAfter(true);
      return;
      i = R.anim.slide_in_from_bottom;
      j = R.anim.slide_out_to_bottom;
      setBackgroundResource(R.drawable.indicator_bg_bottom);
      this.mArrowImageView.setScaleType(ImageView.ScaleType.MATRIX);
      paramMode = new Matrix();
      paramMode.setRotate(180.0F, localDrawable.getIntrinsicWidth() / 2.0F, localDrawable.getIntrinsicHeight() / 2.0F);
      this.mArrowImageView.setImageMatrix(paramMode);
    }
  }

  public void hide()
  {
    startAnimation(this.mOutAnim);
  }

  public final boolean isVisible()
  {
    Animation localAnimation = getAnimation();
    if (localAnimation != null)
      if (this.mInAnim != localAnimation);
    do
    {
      return true;
      return false;
    }
    while (getVisibility() == 0);
    return false;
  }

  public void onAnimationEnd(Animation paramAnimation)
  {
    if (paramAnimation == this.mOutAnim)
    {
      this.mArrowImageView.clearAnimation();
      setVisibility(8);
    }
    while (true)
    {
      clearAnimation();
      return;
      if (paramAnimation != this.mInAnim)
        continue;
      setVisibility(0);
    }
  }

  public void onAnimationRepeat(Animation paramAnimation)
  {
  }

  public void onAnimationStart(Animation paramAnimation)
  {
    setVisibility(0);
  }

  public void pullToRefresh()
  {
    this.mArrowImageView.startAnimation(this.mResetRotateAnimation);
  }

  public void releaseToRefresh()
  {
    this.mArrowImageView.startAnimation(this.mRotateAnimation);
  }

  public void show()
  {
    this.mArrowImageView.clearAnimation();
    startAnimation(this.mInAnim);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.IndicatorLayout
 * JD-Core Version:    0.6.0
 */