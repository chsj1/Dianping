package com.dianping.widget.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Orientation;

public class DperLoadingLayout extends LoadingLayout
{
  static final int TOTAL_FRAME = 10;
  boolean isVisible = true;
  private final Matrix mHeaderImageMatrix;
  private float mRotationPivotX;
  private float mRotationPivotY;

  public DperLoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray)
  {
    super(paramContext, paramMode, paramOrientation, paramTypedArray);
    this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
    this.mHeaderImageMatrix = new Matrix(this.mHeaderImage.getImageMatrix());
  }

  private void resetImageRotation()
  {
    if (this.mHeaderImageMatrix != null)
    {
      this.mHeaderImageMatrix.reset();
      this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
    }
  }

  protected int getDefaultDrawableResId()
  {
    return R.drawable.dropdown_anim_00;
  }

  public void onLoadingDrawableSet(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      this.mRotationPivotX = Math.round(paramDrawable.getIntrinsicWidth() / 2.0F);
      this.mRotationPivotY = Math.round(paramDrawable.getIntrinsicHeight() / 2.0F);
    }
  }

  protected void onPullImpl(float paramFloat)
  {
    if (!this.isVisible)
      return;
    if (paramFloat <= 1.0F)
    {
      this.mHeaderImageMatrix.setScale(paramFloat, paramFloat, this.mRotationPivotX, this.mRotationPivotY);
      this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
      this.mHeaderImage.setImageResource((int)(paramFloat / 1.0F * 10.0F) + getDefaultDrawableResId());
      return;
    }
    this.mHeaderImageMatrix.setScale(1.0F, 1.0F, this.mRotationPivotX, this.mRotationPivotY);
    this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
    this.mHeaderImage.setImageResource(getDefaultDrawableResId() + 10);
  }

  protected void pullToRefreshImpl()
  {
  }

  protected void refreshingImpl()
  {
    if (!this.isVisible)
      return;
    this.mHeaderImage.setImageResource(R.anim.pull_loading);
    ((AnimationDrawable)this.mHeaderImage.getDrawable()).start();
  }

  protected void releaseToRefreshImpl()
  {
  }

  protected void resetImpl()
  {
    this.mHeaderImage.clearAnimation();
    resetImageRotation();
  }

  public void setLoadingVisible(boolean paramBoolean)
  {
    this.isVisible = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.DperLoadingLayout
 * JD-Core Version:    0.6.0
 */