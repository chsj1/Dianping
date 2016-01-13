package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class Scroller
{
  private static float ALPHA = 0.0F;
  private static float DECELERATION_RATE = 0.0F;
  private static float END_TENSION = 0.0F;
  private static final int FLING_MODE = 1;
  private static final int NB_SAMPLES = 100;
  private static final int SCROLL_MODE = 0;
  private static final float[] SPLINE = new float[101];
  private static float START_TENSION;
  private static float sViscousFluidNormalize;
  private static float sViscousFluidScale;
  private int mCurrX;
  private int mCurrY;
  private float mDeceleration;
  private float mDeltaX;
  private float mDeltaY;
  private int mDuration;
  private float mDurationReciprocal;
  private int mFinalX;
  private int mFinalY;
  private boolean mFinished = true;
  private boolean mFlywheel;
  private Interpolator mInterpolator;
  private int mMaxX;
  private int mMaxY;
  private int mMinX;
  private int mMinY;
  private int mMode;
  private final float mPpi;
  private long mStartTime;
  private int mStartX;
  private int mStartY;
  private float mVelocity;

  static
  {
    DECELERATION_RATE = (float)(Math.log(0.75D) / Math.log(0.9D));
    ALPHA = 800.0F;
    START_TENSION = 0.4F;
    END_TENSION = 1.0F - START_TENSION;
    float f1 = 0.0F;
    int i = 0;
    if (i <= 100)
    {
      float f4 = i / 100.0F;
      float f2 = 1.0F;
      while (true)
      {
        float f3 = f1 + (f2 - f1) / 2.0F;
        float f5 = 3.0F * f3 * (1.0F - f3);
        float f6 = ((1.0F - f3) * START_TENSION + END_TENSION * f3) * f5 + f3 * f3 * f3;
        if (Math.abs(f6 - f4) < 1.E-005D)
        {
          SPLINE[i] = (f5 + f3 * f3 * f3);
          i += 1;
          break;
        }
        if (f6 > f4)
        {
          f2 = f3;
          continue;
        }
        f1 = f3;
      }
    }
    SPLINE[100] = 1.0F;
    sViscousFluidScale = 8.0F;
    sViscousFluidNormalize = 1.0F;
    sViscousFluidNormalize = 1.0F / viscousFluid(1.0F);
  }

  public Scroller(Context paramContext)
  {
    this(paramContext, null);
  }

  public Scroller(Context paramContext, Interpolator paramInterpolator)
  {
    this(paramContext, paramInterpolator, true);
  }

  public Scroller(Context paramContext, Interpolator paramInterpolator, boolean paramBoolean)
  {
    this.mInterpolator = paramInterpolator;
    this.mPpi = (paramContext.getResources().getDisplayMetrics().density * 160.0F);
    this.mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
    this.mFlywheel = paramBoolean;
  }

  private float computeDeceleration(float paramFloat)
  {
    return 386.0878F * this.mPpi * paramFloat;
  }

  static float viscousFluid(float paramFloat)
  {
    paramFloat *= sViscousFluidScale;
    if (paramFloat < 1.0F)
      paramFloat -= 1.0F - (float)Math.exp(-paramFloat);
    while (true)
    {
      return paramFloat * sViscousFluidNormalize;
      paramFloat = 0.3678795F + (1.0F - 0.3678795F) * (1.0F - (float)Math.exp(1.0F - paramFloat));
    }
  }

  public boolean computeScrollOffset()
  {
    int k = 1;
    int j;
    if (this.mFinished)
      j = 0;
    while (true)
    {
      return j;
      int i = (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
      if (i >= this.mDuration)
        break;
      switch (this.mMode)
      {
      default:
        return true;
      case 0:
        f1 = i * this.mDurationReciprocal;
        if (this.mInterpolator == null)
          f1 = viscousFluid(f1);
        while (true)
        {
          this.mCurrX = (this.mStartX + Math.round(this.mDeltaX * f1));
          this.mCurrY = (this.mStartY + Math.round(this.mDeltaY * f1));
          return true;
          f1 = this.mInterpolator.getInterpolation(f1);
        }
      case 1:
      }
      float f1 = i / this.mDuration;
      i = (int)(100.0F * f1);
      float f2 = i / 100.0F;
      float f3 = (i + 1) / 100.0F;
      float f4 = SPLINE[i];
      float f5 = SPLINE[(i + 1)];
      f1 = f4 + (f1 - f2) / (f3 - f2) * (f5 - f4);
      this.mCurrX = (this.mStartX + Math.round((this.mFinalX - this.mStartX) * f1));
      this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
      this.mCurrX = Math.max(this.mCurrX, this.mMinX);
      this.mCurrY = (this.mStartY + Math.round((this.mFinalY - this.mStartY) * f1));
      this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
      this.mCurrY = Math.max(this.mCurrY, this.mMinY);
      j = k;
      if (this.mCurrX != this.mFinalX)
        continue;
      j = k;
      if (this.mCurrY != this.mFinalY)
        continue;
      this.mFinished = true;
      return true;
    }
    this.mCurrX = this.mFinalX;
    this.mCurrY = this.mFinalY;
    this.mFinished = true;
    return true;
  }

  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int j = paramInt3;
    int i = paramInt4;
    float f1;
    if (this.mFlywheel)
    {
      j = paramInt3;
      i = paramInt4;
      if (!this.mFinished)
      {
        f1 = getCurrVelocity();
        float f4 = this.mFinalX - this.mStartX;
        f2 = this.mFinalY - this.mStartY;
        float f3 = (float)Math.sqrt(f4 * f4 + f2 * f2);
        f4 /= f3;
        f2 /= f3;
        f3 = f4 * f1;
        f1 = f2 * f1;
        j = paramInt3;
        i = paramInt4;
        if (Math.signum(paramInt3) == Math.signum(f3))
        {
          j = paramInt3;
          i = paramInt4;
          if (Math.signum(paramInt4) == Math.signum(f1))
          {
            j = (int)(paramInt3 + f3);
            i = (int)(paramInt4 + f1);
          }
        }
      }
    }
    this.mMode = 1;
    this.mFinished = false;
    float f2 = (float)Math.sqrt(j * j + i * i);
    this.mVelocity = f2;
    double d = Math.log(START_TENSION * f2 / ALPHA);
    this.mDuration = (int)(1000.0D * Math.exp(d / (DECELERATION_RATE - 1.0D)));
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStartX = paramInt1;
    this.mStartY = paramInt2;
    if (f2 == 0.0F)
    {
      f1 = 1.0F;
      if (f2 != 0.0F)
        break label420;
      f2 = 1.0F;
    }
    while (true)
    {
      paramInt3 = (int)(ALPHA * Math.exp(DECELERATION_RATE / (DECELERATION_RATE - 1.0D) * d));
      this.mMinX = paramInt5;
      this.mMaxX = paramInt6;
      this.mMinY = paramInt7;
      this.mMaxY = paramInt8;
      this.mFinalX = (Math.round(paramInt3 * f1) + paramInt1);
      this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
      this.mFinalX = Math.max(this.mFinalX, this.mMinX);
      this.mFinalY = (Math.round(paramInt3 * f2) + paramInt2);
      this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
      this.mFinalY = Math.max(this.mFinalY, this.mMinY);
      return;
      f1 = j / f2;
      break;
      label420: f2 = i / f2;
    }
  }

  public final void forceFinished(boolean paramBoolean)
  {
    this.mFinished = paramBoolean;
  }

  public float getCurrVelocity()
  {
    return this.mVelocity - this.mDeceleration * timePassed() / 2000.0F;
  }

  public final int getCurrY()
  {
    return this.mCurrY;
  }

  public final int getDuration()
  {
    return this.mDuration;
  }

  public final int getFinalX()
  {
    return this.mFinalX;
  }

  public final int getFinalY()
  {
    return this.mFinalY;
  }

  public final int getStartX()
  {
    return this.mStartX;
  }

  public final int getStartY()
  {
    return this.mStartY;
  }

  public final boolean isFinished()
  {
    return this.mFinished;
  }

  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.mMode = 0;
    this.mFinished = false;
    this.mDuration = paramInt5;
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStartX = paramInt1;
    this.mStartY = paramInt2;
    this.mFinalX = (paramInt1 + paramInt3);
    this.mFinalY = (paramInt2 + paramInt4);
    this.mDeltaX = paramInt3;
    this.mDeltaY = paramInt4;
    this.mDurationReciprocal = (1.0F / this.mDuration);
  }

  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.Scroller
 * JD-Core Version:    0.6.0
 */