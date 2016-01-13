package com.dianping.util.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation
{
  private Camera mCamera;
  private float mCenterX;
  private float mCenterY;
  private float mDepthZ;
  private float mFromDegrees;
  private boolean mReverse;
  private float mToDegrees;

  public Rotate3dAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, boolean paramBoolean)
  {
    this.mFromDegrees = paramFloat1;
    this.mToDegrees = paramFloat2;
    this.mCenterX = paramFloat3;
    this.mCenterY = paramFloat4;
    this.mDepthZ = paramFloat5;
    this.mReverse = paramBoolean;
  }

  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f1 = this.mFromDegrees;
    float f2 = this.mToDegrees;
    float f3 = this.mCenterX;
    float f4 = this.mCenterY;
    Camera localCamera = this.mCamera;
    paramTransformation = paramTransformation.getMatrix();
    localCamera.save();
    if (this.mReverse)
      localCamera.translate(0.0F, 0.0F, this.mDepthZ * paramFloat);
    while (true)
    {
      localCamera.rotateY(f1 + (f2 - f1) * paramFloat);
      localCamera.getMatrix(paramTransformation);
      localCamera.restore();
      paramTransformation.preTranslate(-f3, -f4);
      paramTransformation.postTranslate(f3, f4);
      return;
      localCamera.translate(0.0F, 0.0F, this.mDepthZ * (1.0F - paramFloat));
    }
  }

  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mCamera = new Camera();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.animation.Rotate3dAnimation
 * JD-Core Version:    0.6.0
 */