package com.dianping.widget.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import com.dianping.v1.R.drawable;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Orientation;

@SuppressLint({"ViewConstructor"})
public class FlipLoadingLayout extends LoadingLayout
{
  static final int FLIP_ANIMATION_DURATION = 150;
  private final Animation mResetRotateAnimation;
  private final Animation mRotateAnimation;

  public FlipLoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray)
  {
    super(paramContext, paramMode, paramOrientation, paramTypedArray);
    if (paramMode == PullToRefreshBase.Mode.PULL_FROM_START);
    int j;
    for (int i = -180; ; j = 180)
    {
      this.mRotateAnimation = new RotateAnimation(0.0F, i, 1, 0.5F, 1, 0.5F);
      this.mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
      this.mRotateAnimation.setDuration(150L);
      this.mRotateAnimation.setFillAfter(true);
      this.mResetRotateAnimation = new RotateAnimation(i, 0.0F, 1, 0.5F, 1, 0.5F);
      this.mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
      this.mResetRotateAnimation.setDuration(150L);
      this.mResetRotateAnimation.setFillAfter(true);
      return;
    }
  }

  private float getDrawableRotationAngle()
  {
    switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mMode.ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    do
    {
      return 0.0F;
      if (this.mScrollDirection == PullToRefreshBase.Orientation.HORIZONTAL)
        return 90.0F;
      return 180.0F;
    }
    while (this.mScrollDirection != PullToRefreshBase.Orientation.HORIZONTAL);
    return 270.0F;
  }

  protected int getDefaultDrawableResId()
  {
    return R.drawable.default_ptr_flip;
  }

  protected void onLoadingDrawableSet(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      int i = paramDrawable.getIntrinsicHeight();
      int j = paramDrawable.getIntrinsicWidth();
      paramDrawable = this.mHeaderImage.getLayoutParams();
      int k = Math.max(i, j);
      paramDrawable.height = k;
      paramDrawable.width = k;
      this.mHeaderImage.requestLayout();
      this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
      Matrix localMatrix = new Matrix();
      localMatrix.postTranslate((paramDrawable.width - j) / 2.0F, (paramDrawable.height - i) / 2.0F);
      localMatrix.postRotate(getDrawableRotationAngle(), paramDrawable.width / 2.0F, paramDrawable.height / 2.0F);
      this.mHeaderImage.setImageMatrix(localMatrix);
    }
  }

  protected void onPullImpl(float paramFloat)
  {
  }

  protected void pullToRefreshImpl()
  {
    if (this.mRotateAnimation == this.mHeaderImage.getAnimation())
      this.mHeaderImage.startAnimation(this.mResetRotateAnimation);
  }

  protected void refreshingImpl()
  {
    this.mHeaderImage.clearAnimation();
    this.mHeaderImage.setVisibility(4);
    this.mHeaderProgress.setVisibility(0);
  }

  protected void releaseToRefreshImpl()
  {
    this.mHeaderImage.startAnimation(this.mRotateAnimation);
  }

  protected void resetImpl()
  {
    this.mHeaderImage.clearAnimation();
    this.mHeaderProgress.setVisibility(8);
    this.mHeaderImage.setVisibility(0);
  }

  public void setLoadingVisible(boolean paramBoolean)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.FlipLoadingLayout
 * JD-Core Version:    0.6.0
 */