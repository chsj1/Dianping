package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.dianping.util.ViewUtils;

public class ColorBorderTextView extends AutoHideTextView
{
  private int borderColor;
  private boolean drawBorder;
  private Paint mPaint;

  public ColorBorderTextView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public ColorBorderTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    if (this.mPaint == null)
      this.mPaint = new Paint();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.drawBorder)
    {
      this.mPaint.reset();
      this.mPaint.setColor(this.borderColor);
      this.mPaint.setStyle(Paint.Style.STROKE);
      int i = ViewUtils.dip2px(getContext(), 1.0F);
      this.mPaint.setStrokeWidth(i);
      paramCanvas.drawRoundRect(new RectF(i / 2.0F, i / 2.0F, getWidth() - i / 2.0F, getHeight() - i / 2.0F), i * 2, i * 2, this.mPaint);
    }
  }

  public void setBorderColor(int paramInt)
  {
    if (this.borderColor != paramInt)
    {
      this.drawBorder = true;
      this.borderColor = paramInt;
      postInvalidate();
    }
  }

  public void setBorderColor(String paramString)
  {
    try
    {
      setBorderColor(Color.parseColor(paramString));
      return;
    }
    catch (java.lang.Exception paramString)
    {
    }
  }

  public void setTextColor(String paramString)
  {
    try
    {
      setTextColor(Color.parseColor(paramString));
      return;
    }
    catch (java.lang.Exception paramString)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ColorBorderTextView
 * JD-Core Version:    0.6.0
 */