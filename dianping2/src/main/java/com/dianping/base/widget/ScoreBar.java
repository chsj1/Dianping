package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.v1.R.drawable;

public class ScoreBar extends View
{
  Bitmap icon_high;
  Bitmap icon_low;
  Bitmap icon_normal;
  OnRatingChangedListener listener;
  private Paint mPaint;
  int score;

  public ScoreBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public ScoreBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ScoreBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
    paramContext = getResources();
    this.icon_normal = BitmapFactory.decodeResource(paramContext, R.drawable.score_icon_normal);
    this.icon_low = BitmapFactory.decodeResource(paramContext, R.drawable.score_icon_cry);
    this.icon_high = BitmapFactory.decodeResource(paramContext, R.drawable.score_icon_smile);
  }

  private void init()
  {
    this.mPaint = new Paint();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int j = this.score / 10;
    this.mPaint.reset();
    paramCanvas.translate(getPaddingLeft(), getPaddingTop());
    int i = 0;
    if (i < 5)
    {
      Bitmap localBitmap = this.icon_normal;
      if (i < j)
        if (j > 1)
          break label91;
      label91: for (localBitmap = this.icon_low; ; localBitmap = this.icon_high)
      {
        paramCanvas.drawBitmap(localBitmap, this.icon_normal.getWidth() * i, 0.0F, this.mPaint);
        i += 1;
        break;
      }
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    setMeasuredDimension(this.icon_normal.getWidth() * 5, this.icon_normal.getHeight());
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    }
    while (true)
    {
      return true;
      int i = Math.min(50, Math.max((int)(10.0F * (float)Math.ceil((paramMotionEvent.getX() - getPaddingLeft()) / this.icon_normal.getWidth())), 0));
      if (this.score != i)
      {
        this.score = i;
        if (this.listener != null)
          this.listener.onRatingChanged();
      }
      invalidate();
      continue;
      if (this.listener != null)
        this.listener.afterRatingChanged();
      invalidate();
    }
  }

  public int score()
  {
    return this.score;
  }

  public void setOnRatingChangedListener(OnRatingChangedListener paramOnRatingChangedListener)
  {
    this.listener = paramOnRatingChangedListener;
  }

  public void setScore(int paramInt)
  {
    paramInt = paramInt / 10 * 10;
    if (this.score != paramInt)
    {
      this.score = paramInt;
      if (this.listener != null)
        this.listener.onRatingChanged();
      invalidate();
    }
  }

  public static abstract interface OnRatingChangedListener
  {
    public abstract void afterRatingChanged();

    public abstract void onRatingChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ScoreBar
 * JD-Core Version:    0.6.0
 */