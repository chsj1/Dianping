package com.dianping.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FlipAnimator extends Animation
{
  private Camera mCamera;
  private float mCenterX;
  private float mCenterY;
  private boolean mForward = true;
  private View mFromView;
  private View mToView;
  private boolean mVisibilitySwapped;

  public FlipAnimator(View paramView1, View paramView2, int paramInt1, int paramInt2)
  {
    this.mFromView = paramView1;
    this.mToView = paramView2;
    this.mCenterX = paramInt1;
    this.mCenterY = paramInt2;
    setDuration(600L);
    setFillAfter(true);
    setInterpolator(new AccelerateDecelerateInterpolator());
  }

  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    double d = 3.141592653589793D * paramFloat;
    float f2 = (float)(180.0D * d / 3.141592653589793D);
    float f1 = f2;
    if (paramFloat >= 0.5F)
    {
      paramFloat = f2 - 180.0F;
      f1 = paramFloat;
      if (!this.mVisibilitySwapped)
      {
        this.mFromView.setVisibility(4);
        this.mToView.setVisibility(0);
        this.mVisibilitySwapped = true;
        f1 = paramFloat;
      }
    }
    paramFloat = f1;
    if (this.mForward)
      paramFloat = -f1;
    paramTransformation = paramTransformation.getMatrix();
    this.mCamera.save();
    this.mCamera.translate(0.0F, 0.0F, (float)(150.0D * Math.sin(d)));
    this.mCamera.rotateY(paramFloat);
    this.mCamera.getMatrix(paramTransformation);
    this.mCamera.restore();
    paramTransformation.preTranslate(-this.mCenterX, -this.mCenterY);
    paramTransformation.postTranslate(this.mCenterX, this.mCenterY);
  }

  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mCamera = new Camera();
  }

  public FlipAnimator reverse()
  {
    this.mForward = false;
    View localView = this.mToView;
    this.mToView = this.mFromView;
    this.mFromView = localView;
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.FlipAnimator
 * JD-Core Version:    0.6.0
 */