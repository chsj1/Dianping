package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.dianping.v1.R.color;

public class DashLine extends View
{
  private Paint mPaint;
  private Path mPath = new Path();

  public DashLine(Context paramContext)
  {
    super(paramContext);
  }

  public DashLine(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public DashLine(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mPaint == null)
    {
      this.mPaint = new Paint();
      this.mPaint.setStyle(Paint.Style.STROKE);
      this.mPaint.setColor(getContext().getResources().getColor(R.color.light_gray));
      this.mPaint.setPathEffect(new DashPathEffect(new float[] { 3.0F, 3.0F, 3.0F, 3.0F }, 1.0F));
    }
    this.mPath.reset();
    this.mPath.moveTo(0.0F, getHeight() / 2.0F);
    this.mPath.lineTo(getWidth(), getHeight() / 2.0F);
    paramCanvas.drawPath(this.mPath, this.mPaint);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DashLine
 * JD-Core Version:    0.6.0
 */