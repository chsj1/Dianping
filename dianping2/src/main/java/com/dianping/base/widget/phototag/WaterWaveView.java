package com.dianping.base.widget.phototag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaterWaveView extends View
{
  private static final int FPS = 80;
  private Interpolator interpolator = new CycleInterpolator(0.5F);
  private boolean mFillAllView;
  private float mFillWaveSourceShapeRadius;
  private Wave mLastRmoveWave;
  private float mMaxWaveAreaRadius;
  private float mStirStep;
  private float mViewCenterX;
  private float mViewCenterY;
  private final Paint mWaveCenterShapePaint;
  private int mWaveColor;
  private float mWaveEndWidth;
  private float mWaveIntervalSize;
  private final Paint mWavePaint = new Paint();
  private float mWaveStartWidth;
  private final List<Wave> mWaves;

  public WaterWaveView(Context paramContext)
  {
    super(paramContext);
    this.mWavePaint.setAntiAlias(true);
    this.mWavePaint.setStyle(Paint.Style.STROKE);
    this.mWaveCenterShapePaint = new Paint();
    this.mWaveCenterShapePaint.setAntiAlias(true);
    this.mWaveCenterShapePaint.setStyle(Paint.Style.FILL);
    this.mFillAllView = false;
    this.mWaves = new ArrayList();
    init();
  }

  public WaterWaveView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mWavePaint.setAntiAlias(true);
    this.mWavePaint.setStyle(Paint.Style.STROKE);
    this.mWaveCenterShapePaint = new Paint();
    this.mWaveCenterShapePaint.setAntiAlias(true);
    this.mWaveCenterShapePaint.setStyle(Paint.Style.FILL);
    this.mFillAllView = false;
    this.mWaves = new ArrayList();
    init();
  }

  private void init()
  {
    setWaveInfo(60.0F, 2.0F, 2.0F, 15.0F, -872415232);
  }

  private void stir()
  {
    if (this.mWaves.isEmpty())
    {
      localWave = null;
      if ((localWave == null) || (localWave.radius >= this.mWaveIntervalSize))
      {
        if (this.mLastRmoveWave == null)
          break label232;
        localWave = this.mLastRmoveWave;
        this.mLastRmoveWave = null;
        localWave.reset();
      }
    }
    int j;
    while (true)
    {
      this.mWaves.add(0, localWave);
      float f3 = this.mWaveEndWidth;
      float f4 = this.mWaveStartWidth;
      j = this.mWaves.size();
      int i = 0;
      while (i < j)
      {
        localWave = (Wave)this.mWaves.get(i);
        float f2 = localWave.radius / this.mMaxWaveAreaRadius;
        float f1 = f2;
        if (f2 > 1.0F)
          f1 = 1.0F;
        localWave.width = (this.mWaveStartWidth + f1 * (f3 - f4));
        localWave.radius += this.mStirStep;
        f1 = this.interpolator.getInterpolation(f1);
        localWave.color = (this.mWaveColor & 0xFFFFFF | (int)(255.0F * f1 * 0.6D) << 24);
        i += 1;
      }
      localWave = (Wave)this.mWaves.get(0);
      break;
      label232: localWave = new Wave();
    }
    Wave localWave = (Wave)this.mWaves.get(j - 1);
    if (localWave.radius > this.mMaxWaveAreaRadius + localWave.width / 2.0F)
      this.mWaves.remove(j - 1);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    stir();
    Iterator localIterator = this.mWaves.iterator();
    while (localIterator.hasNext())
    {
      Wave localWave = (Wave)localIterator.next();
      this.mWavePaint.setColor(localWave.color);
      this.mWavePaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawCircle(this.mViewCenterX, this.mViewCenterY, localWave.radius, this.mWavePaint);
    }
    if (this.mFillWaveSourceShapeRadius > 0.0F)
      paramCanvas.drawCircle(this.mViewCenterX, this.mViewCenterY, this.mFillWaveSourceShapeRadius, this.mWaveCenterShapePaint);
    postInvalidateDelayed(80L);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mViewCenterX = (getWidth() / 2);
    this.mViewCenterY = (getHeight() / 2);
    float f = this.mMaxWaveAreaRadius;
    if (this.mFillAllView)
      f = (float)Math.sqrt(this.mViewCenterX * this.mViewCenterX + this.mViewCenterY * this.mViewCenterY);
    while (true)
    {
      if (this.mMaxWaveAreaRadius != f)
      {
        this.mMaxWaveAreaRadius = f;
        resetWave();
      }
      return;
      f = Math.min(this.mViewCenterX, this.mViewCenterY);
    }
  }

  public void resetWave()
  {
    this.mWaves.clear();
    postInvalidate();
  }

  public void setFillAllView(boolean paramBoolean)
  {
    this.mFillAllView = paramBoolean;
    resetWave();
  }

  public void setFillWaveSourceShapeRadius(float paramFloat)
  {
    this.mFillWaveSourceShapeRadius = paramFloat;
  }

  public void setWaveColor(int paramInt)
  {
    this.mWaveColor = paramInt;
    this.mWaveCenterShapePaint.setColor(this.mWaveColor);
  }

  public void setWaveInfo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
  {
    this.mWaveIntervalSize = paramFloat1;
    this.mStirStep = paramFloat2;
    this.mWaveStartWidth = paramFloat3;
    this.mWaveEndWidth = paramFloat4;
    setWaveColor(paramInt);
    resetWave();
  }

  class Wave
  {
    public int color;
    public float radius;
    public float width;

    public Wave()
    {
      reset();
    }

    public void reset()
    {
      this.radius = 0.0F;
      this.width = WaterWaveView.this.mWaveStartWidth;
      this.color = WaterWaveView.this.mWaveColor;
    }

    public String toString()
    {
      return "Wave [radius=" + this.radius + ", width=" + this.width + ", color=" + this.color + "]";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.phototag.WaterWaveView
 * JD-Core Version:    0.6.0
 */