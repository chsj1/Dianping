package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.dianping.util.Log;
import com.dianping.v1.R.dimen;

public class TxtProgressBar extends ProgressBar
{
  private Paint defaultPaint;
  private int mProgress;

  public TxtProgressBar(Context paramContext)
  {
    super(paramContext);
  }

  public TxtProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public TxtProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mProgress == 0)
      return;
    if (this.defaultPaint == null)
    {
      this.defaultPaint = new Paint();
      this.defaultPaint.setColor(getContext().getResources().getColor(17170435));
      this.defaultPaint.setTextAlign(Paint.Align.CENTER);
      this.defaultPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_very_small));
    }
    Log.e("progress", "" + this.mProgress);
    paramCanvas.drawText("" + this.mProgress, getWidth() / 2.0F, getHeight() / 2.0F - this.defaultPaint.ascent() / 2.0F, this.defaultPaint);
  }

  public void setProgress(int paramInt)
  {
    this.mProgress = paramInt;
    super.setProgress(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TxtProgressBar
 * JD-Core Version:    0.6.0
 */