package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;

public class RatingBar extends View
{
  Bitmap empty_normal;
  OnRatingChangedListener listener;
  Paint mPaint;
  Bitmap selected_normal;
  int star = -1;

  public RatingBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public RatingBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public RatingBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
    Resources localResources = getResources();
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RatingBar);
    paramAttributeSet = null;
    Context localContext = null;
    paramContext = paramAttributeSet;
    while (true)
    {
      try
      {
        Drawable localDrawable = localTypedArray.getDrawable(R.styleable.RatingBar_empty_normal);
        paramContext = paramAttributeSet;
        Object localObject = localTypedArray.getDrawable(R.styleable.RatingBar_selected_normal);
        paramContext = paramAttributeSet;
        paramAttributeSet = ((BitmapDrawable)localDrawable).getBitmap();
        paramContext = paramAttributeSet;
        localObject = ((BitmapDrawable)localObject).getBitmap();
        paramContext = (Context)localObject;
        localTypedArray.recycle();
        localContext = paramContext;
        paramContext = paramAttributeSet;
        if (paramContext != null)
        {
          this.empty_normal = paramContext;
          if (localContext == null)
            break;
          this.selected_normal = localContext;
          return;
        }
      }
      catch (java.lang.Exception paramAttributeSet)
      {
        localTypedArray.recycle();
        continue;
      }
      finally
      {
        localTypedArray.recycle();
      }
      this.empty_normal = BitmapFactory.decodeResource(localResources, R.drawable.star_0_normal);
    }
    this.selected_normal = BitmapFactory.decodeResource(localResources, R.drawable.star_1_normal);
  }

  private void init()
  {
    if (this.mPaint == null)
      this.mPaint = new Paint();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int j = this.star / 10;
    this.mPaint.reset();
    paramCanvas.translate(getPaddingLeft(), getPaddingTop());
    int i = 0;
    if (i < 5)
    {
      if (i < j);
      for (Bitmap localBitmap = this.selected_normal; ; localBitmap = this.empty_normal)
      {
        paramCanvas.drawBitmap(localBitmap, this.empty_normal.getWidth() * i, 0.0F, this.mPaint);
        i += 1;
        break;
      }
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    setMeasuredDimension(this.empty_normal.getWidth() * 5, this.empty_normal.getHeight());
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
      int i = Math.min(50, Math.max((int)(10.0F * (float)Math.ceil((paramMotionEvent.getX() - getPaddingLeft()) / this.empty_normal.getWidth())), 0));
      if (this.star != i)
      {
        this.star = i;
        if (this.listener != null)
          this.listener.onRatingChanged(this.star);
      }
      invalidate();
      continue;
      if (this.listener != null)
        this.listener.afterRatingChanged(this.star);
      invalidate();
    }
  }

  public void setOnRatingChangedListener(OnRatingChangedListener paramOnRatingChangedListener)
  {
    this.listener = paramOnRatingChangedListener;
  }

  public void setStar(int paramInt)
  {
    paramInt = paramInt / 10 * 10;
    if (this.star != paramInt)
    {
      this.star = paramInt;
      if (this.listener != null)
        this.listener.onRatingChanged(paramInt);
      invalidate();
    }
  }

  public int star()
  {
    return this.star;
  }

  public static abstract interface OnRatingChangedListener
  {
    public abstract void afterRatingChanged(int paramInt);

    public abstract void onRatingChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RatingBar
 * JD-Core Version:    0.6.0
 */