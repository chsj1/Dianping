package com.dianping.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;

public class NavigationDot extends View
{
  protected static final Paint paint = new Paint(1);
  protected int currentDot;
  long deleayedTime = 0L;
  protected Bitmap dotNormal;
  private int dotNormalId;
  protected Bitmap dotPressed;
  private int dotPressedId;
  protected int dot_height;
  protected int dot_width;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      NavigationDot.this.moveToNext();
      sendEmptyMessageDelayed(1, NavigationDot.this.deleayedTime);
    }
  };
  private int height;
  private boolean isLoop;
  protected final int padding;
  protected int totalDot;
  protected int width;

  public NavigationDot(Context paramContext)
  {
    this(paramContext, null);
  }

  public NavigationDot(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = getResources();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NavigationDot);
    this.dotPressedId = paramAttributeSet.getResourceId(R.styleable.NavigationDot_navigationDotSelected, 0);
    this.dotNormalId = paramAttributeSet.getResourceId(R.styleable.NavigationDot_navigationDotUnselected, 0);
    this.totalDot = paramAttributeSet.getInt(R.styleable.NavigationDot_navigationDotCount, 0);
    paramAttributeSet.recycle();
    if (this.dotPressedId != 0)
    {
      this.dotPressed = BitmapFactory.decodeResource(localResources, this.dotPressedId);
      if (this.dotNormalId == 0)
        break label169;
    }
    label169: for (this.dotNormal = BitmapFactory.decodeResource(localResources, this.dotNormalId); ; this.dotNormal = BitmapFactory.decodeResource(localResources, R.drawable.navigation_dot_normal))
    {
      if (this.dotNormal != null)
      {
        this.dot_width = this.dotNormal.getWidth();
        this.dot_height = this.dotNormal.getHeight();
      }
      this.padding = dip2px(paramContext, 6.0F);
      return;
      this.dotPressed = BitmapFactory.decodeResource(localResources, R.drawable.navigation_dot_pressed);
      break;
    }
  }

  public NavigationDot(Context paramContext, boolean paramBoolean)
  {
    this(paramContext, null);
    this.isLoop = paramBoolean;
  }

  public static int dip2px(Context paramContext, float paramFloat)
  {
    return (int)(paramFloat * paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  private int measureHeight(int paramInt)
  {
    int k = View.MeasureSpec.getMode(paramInt);
    int i = View.MeasureSpec.getSize(paramInt);
    if (k == 1073741824)
      paramInt = i;
    while (true)
    {
      this.height = paramInt;
      return paramInt;
      int j = this.dot_height + getPaddingTop() + getPaddingBottom();
      paramInt = j;
      if (k != -2147483648)
        continue;
      paramInt = Math.min(j, i);
    }
  }

  private int measureWidth(int paramInt)
  {
    int k = View.MeasureSpec.getMode(paramInt);
    int i = View.MeasureSpec.getSize(paramInt);
    if (k == 1073741824)
      paramInt = i;
    while (true)
    {
      this.width = paramInt;
      return paramInt;
      int j = (this.dot_width + this.padding) * this.totalDot + getPaddingLeft() + getPaddingRight();
      paramInt = j;
      if (k != -2147483648)
        continue;
      paramInt = Math.min(j, i);
    }
  }

  public static int px2dip(Context paramContext, float paramFloat)
  {
    return (int)(paramFloat / paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  public void moveToNext()
  {
    if ((this.currentDot >= this.totalDot) && (!this.isLoop))
      return;
    int i = this.currentDot + 1;
    this.currentDot = i;
    this.currentDot = (i % this.totalDot);
    invalidate();
  }

  public void moveToPosition(int paramInt)
  {
    if (paramInt > this.totalDot)
      return;
    this.currentDot = paramInt;
    invalidate();
  }

  public void moveToPrevious()
  {
    if (this.currentDot <= 0)
    {
      if (!this.isLoop)
        return;
      this.currentDot = this.totalDot;
    }
    this.currentDot -= 1;
    invalidate();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.translate(getPaddingLeft(), getPaddingTop());
    int i = this.dot_width;
    int j = this.totalDot;
    int k = this.padding;
    int m = this.totalDot;
    j = (this.width - (i * j + k * (m - 1))) / 2;
    i = 0;
    if (i < this.totalDot)
    {
      if (this.currentDot == i);
      for (Bitmap localBitmap = this.dotPressed; ; localBitmap = this.dotNormal)
      {
        paramCanvas.drawBitmap(localBitmap, (this.dot_width + this.padding) * i + j, 0.0F, paint);
        i += 1;
        break;
      }
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(measureWidth(paramInt1), measureHeight(paramInt2));
  }

  public void setCurrentIndex(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.totalDot));
    do
      return;
    while (this.currentDot == paramInt);
    this.currentDot = paramInt;
    invalidate();
  }

  public void setDotNormalBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return;
    this.dotNormal = paramBitmap;
    this.dot_width = this.dotNormal.getWidth();
    this.dot_height = this.dotNormal.getHeight();
  }

  public void setDotNormalBitmap(Drawable paramDrawable)
  {
    setDotNormalBitmap(((BitmapDrawable)paramDrawable).getBitmap());
  }

  public void setDotNormalId(int paramInt)
  {
    this.dotNormal = BitmapFactory.decodeResource(getResources(), paramInt);
    this.dot_width = this.dotNormal.getWidth();
    this.dot_height = this.dotNormal.getHeight();
  }

  public void setDotPressedBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return;
    this.dotPressed = paramBitmap;
  }

  public void setDotPressedBitmap(Drawable paramDrawable)
  {
    setDotPressedBitmap(((BitmapDrawable)paramDrawable).getBitmap());
  }

  public void setDotPressedId(int paramInt)
  {
    this.dotPressed = BitmapFactory.decodeResource(getResources(), paramInt);
  }

  public void setFlipInterval(int paramInt)
  {
    long l;
    if (paramInt > 0)
      l = paramInt;
    while (true)
    {
      this.deleayedTime = l;
      return;
      l = 500L;
    }
  }

  public void setTotalDot(int paramInt)
  {
    if (paramInt > 0)
    {
      this.totalDot = paramInt;
      requestLayout();
    }
  }

  public void startFlipping()
  {
    this.deleayedTime = 500L;
    this.handler.sendEmptyMessageDelayed(1, this.deleayedTime);
  }

  public void stopFlipping()
  {
    this.handler.removeMessages(1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.NavigationDot
 * JD-Core Version:    0.6.0
 */