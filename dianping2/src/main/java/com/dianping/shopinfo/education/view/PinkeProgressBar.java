package com.dianping.shopinfo.education.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class PinkeProgressBar extends ProgressBar
{
  private Paint dPaint;
  private Paint mPaint;
  private int pro;
  private int radius;

  public PinkeProgressBar(Context paramContext)
  {
    super(paramContext);
    initPaint();
  }

  public PinkeProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initPaint();
  }

  public PinkeProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initPaint();
  }

  private void initPaint()
  {
    this.mPaint = new Paint();
    this.mPaint.setAntiAlias(false);
    this.mPaint.setStyle(Paint.Style.FILL);
    this.mPaint.setColor(Color.rgb(255, 102, 51));
    this.dPaint = new Paint();
    this.dPaint.setAntiAlias(false);
    this.dPaint.setStyle(Paint.Style.FILL);
    this.dPaint.setColor(Color.rgb(255, 223, 212));
  }

  protected void onDraw(Canvas paramCanvas)
  {
    monitorenter;
    while (true)
    {
      int i;
      int j;
      int k;
      int m;
      try
      {
        super.onDraw(paramCanvas);
        i = getWidth() / 2;
        j = this.radius;
        k = getWidth() - this.radius;
        m = getHeight() / 2;
        if (this.pro < 100)
          continue;
        paramCanvas.drawCircle(j, m, this.radius, this.mPaint);
        paramCanvas.drawCircle(i, m, this.radius, this.mPaint);
        paramCanvas.drawCircle(k, m, this.radius, this.mPaint);
        return;
        if ((this.pro >= 50) && (this.pro < 100))
        {
          paramCanvas.drawCircle(j, m, this.radius, this.mPaint);
          paramCanvas.drawCircle(i, m, this.radius, this.mPaint);
          paramCanvas.drawCircle(k, m, this.radius, this.dPaint);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      if ((this.pro <= 50) && (this.pro > 0))
      {
        paramCanvas.drawCircle(j, m, this.radius, this.mPaint);
        paramCanvas.drawCircle(i, m, this.radius, this.dPaint);
        paramCanvas.drawCircle(k, m, this.radius, this.dPaint);
        continue;
      }
      if (this.pro > 0)
        continue;
      paramCanvas.drawCircle(j, m, this.radius, this.dPaint);
      paramCanvas.drawCircle(i, m, this.radius, this.dPaint);
      paramCanvas.drawCircle(k, m, this.radius, this.dPaint);
    }
  }

  public void setCircleRadius(int paramInt)
  {
    this.radius = paramInt;
  }

  public void setProgress(int paramInt)
  {
    monitorenter;
    try
    {
      super.setProgress(paramInt);
      this.pro = paramInt;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.view.PinkeProgressBar
 * JD-Core Version:    0.6.0
 */