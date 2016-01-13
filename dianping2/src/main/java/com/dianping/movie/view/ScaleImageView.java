package com.dianping.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView.ScaleType;
import com.dianping.movie.util.MovieUtil;
import com.dianping.widget.view.NovaImageView;

public class ScaleImageView extends NovaImageView
{
  static float initScaleMate;
  public static float maxZoom = 1.0F;
  public static float minZoom = 1.0F;
  private static int moveBoundX = 200;
  private static int moveBoundY = 200;
  static int scaleType;
  private final int MSG_WHAT_DOUBLECLICKZOOM = 2;
  private final int MSG_WHAT_REBOUND = 1;
  private final int MSG_WHAT_RESET_BITMAP = 0;
  DisplayMetrics dm;
  private Handler handler = new ScaleImageView.1(this);
  public Matrix mBaseMatrix = new Matrix();
  public Bitmap mBitmapDisplayed;
  public Matrix mDisplayMatrix = new Matrix();
  public ScaleImageView.OnBitmapSetListener mListener;
  private float[] mMatrixValues = new float[9];
  private Runnable mOnLayoutRunnable = null;
  public Matrix mSuppMatrix = new Matrix();
  int mThisHeight = -1;
  int mThisWidth = -1;
  float scaleMate = 1.0F;
  protected float seatDensityFactor;

  public ScaleImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ScaleImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.seatDensityFactor = MovieUtil.getSeatDensityFactor(paramContext);
    init();
  }

  private void handleDoubleClickZoom(Message paramMessage)
  {
    paramMessage = paramMessage.getData();
    float f3 = paramMessage.getFloat("targetScale", 1.0F);
    float f2 = paramMessage.getFloat("tempScale", 1.0F);
    float f4 = paramMessage.getFloat("centerX");
    float f5 = paramMessage.getFloat("centerY");
    float f1;
    if (f3 > f2)
      if (f3 / f2 >= 1.05F)
      {
        f1 = 1.05F;
        f1 = f2 * f1;
        zoomTo(f1, f4, f5);
      }
    do
    {
      if ((f1 / f3 > 1.00001D) || (f1 / f3 < 0.9999900000000001D))
      {
        paramMessage = Message.obtain();
        paramMessage.what = 2;
        Bundle localBundle = new Bundle();
        localBundle.putFloat("targetScale", f3);
        localBundle.putFloat("tempScale", f1);
        localBundle.putFloat("centerX", f4);
        localBundle.putFloat("centerY", f5);
        paramMessage.setData(localBundle);
        this.handler.sendMessageDelayed(paramMessage, 20L);
      }
      return;
      f1 = f3 / f2;
      break;
      f1 = f2;
    }
    while (f3 >= f2);
    if (f2 / f3 >= 1.05F)
      f1 = 1.05F;
    while (true)
    {
      f1 = f2 / f1;
      zoomTo(f1, f4, f5);
      break;
      f1 = f2 / f3;
    }
  }

  private void handleRebound(Message paramMessage)
  {
    paramMessage = paramMessage.getData();
    int i5 = paramMessage.getInt("delta_x", 0);
    int i6 = paramMessage.getInt("delta_y", 0);
    int i2 = paramMessage.getInt("temp_x", 0);
    int i3 = paramMessage.getInt("temp_y", 0);
    int j = 1;
    if (i5 != 0)
    {
      i = (int)(Math.sqrt(Math.abs(i5 - i2)) * 1.2D);
      j = i;
      if (i5 < 0)
        j = i * -1;
    }
    int i = 1;
    int m;
    if (i6 != 0)
    {
      m = (int)(Math.sqrt(Math.abs(i6 - i3)) * 1.2D);
      i = m;
      if (i6 < 0)
        i = m * -1;
    }
    int i4 = 0;
    int i1;
    if ((i5 > 0) && (i2 < i5))
      if (i5 - i2 >= j)
      {
        m = j;
        i1 = i2 + m;
        label155: i4 = 0;
        if ((i6 <= 0) || (i3 >= i6))
          break label362;
        if (i6 - i3 < i)
          break label352;
        j = i;
        label182: i2 = i3 + j;
      }
    label352: label362: 
    do
    {
      do
      {
        postTranslate(m, j);
        if ((i1 != i5) || (i2 != i6))
        {
          paramMessage = Message.obtain();
          paramMessage.what = 1;
          Bundle localBundle = new Bundle();
          localBundle.putInt("delta_x", i5);
          localBundle.putInt("delta_y", i6);
          localBundle.putInt("temp_x", i1);
          localBundle.putInt("temp_y", i2);
          paramMessage.setData(localBundle);
          this.handler.sendMessageDelayed(paramMessage, 5L);
        }
        return;
        int n = i5 - i2;
        break;
        n = i4;
        i1 = i2;
        if (i5 >= 0)
          break label155;
        n = i4;
        i1 = i2;
        if (i2 <= i5)
          break label155;
        if (i5 - i2 <= j);
        for (n = j; ; n = i5 - i2)
        {
          i1 = i2 + n;
          break;
        }
        k = i6 - i3;
        break label182;
        k = i4;
        i2 = i3;
      }
      while (i6 >= 0);
      k = i4;
      i2 = i3;
    }
    while (i3 <= i6);
    if (i6 - i3 <= i);
    for (int k = i; ; k = i6 - i3)
    {
      i2 = i3 + k;
      break;
    }
  }

  public static void setZoom(float paramFloat, int paramInt)
  {
    scaleType = paramInt;
    initScaleMate = paramFloat;
    if (paramInt == 1)
    {
      maxZoom = 1.0F / paramFloat;
      minZoom = 0.33F / paramFloat;
    }
    do
      return;
    while (paramInt != 2);
    maxZoom = 1.0F / paramFloat;
    minZoom = 0.4F / paramFloat;
  }

  protected void center(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mBitmapDisplayed == null)
      return;
    Matrix localMatrix = getImageViewMatrix();
    RectF localRectF = new RectF(0.0F, 0.0F, this.mBitmapDisplayed.getWidth(), this.mBitmapDisplayed.getHeight());
    localMatrix.mapRect(localRectF);
    float f5 = localRectF.height();
    float f4 = localRectF.width();
    float f3 = 0.0F;
    float f2 = 0.0F;
    float f1 = f2;
    int i;
    if (paramBoolean2)
    {
      i = getHeight();
      if (f5 < i)
        f1 = (i - f5) / 2.0F - localRectF.top;
    }
    else
    {
      f2 = f3;
      if (paramBoolean1)
      {
        i = getWidth();
        if (f4 >= i)
          break label211;
        f2 = (i - f4) / 2.0F - localRectF.left;
      }
    }
    while (true)
    {
      postTranslate(f2, f1);
      setImageMatrix(getImageViewMatrix());
      return;
      if (localRectF.top > 0.0F)
      {
        f1 = -localRectF.top;
        break;
      }
      f1 = f2;
      if (localRectF.bottom >= i)
        break;
      f1 = getHeight() - localRectF.bottom;
      break;
      label211: if (localRectF.left > 0.0F)
      {
        f2 = -localRectF.left;
        continue;
      }
      f2 = f3;
      if (localRectF.right >= i)
        continue;
      f2 = i - localRectF.right;
    }
  }

  public void doubleClickZoom(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    Message localMessage = Message.obtain();
    localMessage.what = 2;
    Bundle localBundle = new Bundle();
    localBundle.putFloat("targetScale", paramFloat1);
    localBundle.putFloat("tempScale", getScale());
    localBundle.putFloat("centerX", paramFloat2);
    localBundle.putFloat("centerY", paramFloat3);
    localMessage.setData(localBundle);
    this.handler.sendMessage(localMessage);
  }

  public float getBitmapHeight()
  {
    if (this.mBitmapDisplayed == null)
      return 0.0F;
    return getScale(this.mDisplayMatrix) * this.mBitmapDisplayed.getHeight();
  }

  public float getBitmapWidth()
  {
    if (this.mBitmapDisplayed == null)
      return 0.0F;
    return getScale(this.mDisplayMatrix) * this.mBitmapDisplayed.getWidth();
  }

  public Matrix getImageViewMatrix()
  {
    this.mDisplayMatrix.set(this.mBaseMatrix);
    this.mDisplayMatrix.postConcat(this.mSuppMatrix);
    return this.mDisplayMatrix;
  }

  public void getProperBaseMatrix(Bitmap paramBitmap, Matrix paramMatrix)
  {
    if (paramBitmap == null)
      return;
    float f1 = getWidth();
    float f2 = getHeight();
    f2 = paramBitmap.getWidth();
    float f3 = paramBitmap.getHeight();
    paramMatrix.reset();
    paramMatrix.postScale(initScaleMate, initScaleMate);
    paramMatrix.postTranslate((f1 - initScaleMate * f2) / 2.0F, 0.0F);
  }

  public float getScale()
  {
    return getScale(this.mSuppMatrix);
  }

  public float getScale(Matrix paramMatrix)
  {
    return getValue(paramMatrix, 0);
  }

  public float getTranslateX()
  {
    return getTranslateX(this.mSuppMatrix);
  }

  public float getTranslateX(Matrix paramMatrix)
  {
    return getValue(paramMatrix, 2);
  }

  public float getTranslateY()
  {
    return getTranslateY(this.mSuppMatrix);
  }

  public float getTranslateY(Matrix paramMatrix)
  {
    return getValue(paramMatrix, 5);
  }

  public float getValue(Matrix paramMatrix, int paramInt)
  {
    paramMatrix.getValues(this.mMatrixValues);
    return this.mMatrixValues[paramInt];
  }

  public void init()
  {
    setScaleType(ImageView.ScaleType.MATRIX);
    this.dm = new DisplayMetrics();
    this.dm = getResources().getDisplayMetrics();
  }

  public void moveView(float paramFloat1, float paramFloat2)
  {
    float f2 = getValue(getImageViewMatrix(), 2);
    float f5 = getValue(getImageViewMatrix(), 5);
    float f3 = getBitmapWidth();
    float f6 = getBitmapHeight();
    float f4 = getWidth();
    float f7 = getHeight();
    getScale(getImageViewMatrix());
    if ((f3 < f4) && (f6 < f7))
    {
      center(true, true);
      return;
    }
    if (f3 < f4)
    {
      if (paramFloat2 + f5 > moveBoundY + 0)
        paramFloat1 = moveBoundY - f5;
      while (true)
      {
        postTranslate(0.0F, (int)paramFloat1);
        return;
        paramFloat1 = paramFloat2;
        if (paramFloat2 + f5 >= f7 - f6 - moveBoundY)
          continue;
        paramFloat1 = f7 - f6 - moveBoundY - f5;
      }
    }
    if (f6 < f7)
    {
      if (paramFloat1 + f2 > moveBoundX + 0)
        paramFloat2 = moveBoundX - f2;
      while (true)
      {
        postTranslate((int)paramFloat2, 0.0F);
        return;
        paramFloat2 = paramFloat1;
        if (paramFloat1 + f2 >= f4 - f3 - moveBoundX)
          continue;
        paramFloat2 = f4 - f3 - moveBoundX - f2;
      }
    }
    float f1;
    if (paramFloat2 + f5 > moveBoundY + 0)
    {
      f1 = moveBoundY - f5;
      if (paramFloat1 + f2 <= moveBoundX + 0)
        break label324;
      paramFloat2 = moveBoundX - f2;
    }
    while (true)
    {
      postTranslate((int)paramFloat2, (int)f1);
      return;
      f1 = paramFloat2;
      if (paramFloat2 + f5 >= f7 - f6 - moveBoundY)
        break;
      f1 = f7 - f6 - moveBoundY - f5;
      break;
      label324: paramFloat2 = paramFloat1;
      if (paramFloat1 + f2 >= f4 - f3 - moveBoundX)
        continue;
      paramFloat2 = f4 - f3 - moveBoundX - f2;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mThisWidth = (paramInt3 - paramInt1);
    this.mThisHeight = (paramInt4 - paramInt2);
  }

  public void postTranslate(float paramFloat1, float paramFloat2)
  {
    this.mSuppMatrix.postTranslate(paramFloat1, paramFloat2);
    setImageMatrix(getImageViewMatrix());
  }

  public void rebound()
  {
    float f5 = getValue(getImageViewMatrix(), 2);
    float f8 = getValue(getImageViewMatrix(), 5);
    float f6 = getBitmapWidth();
    float f9 = getBitmapHeight();
    float f7 = getWidth();
    float f10 = getHeight();
    if ((f6 < f7) && (f9 < f10))
    {
      center(true, true);
      return;
    }
    float f4 = 0.0F;
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3;
    if (f6 < f7)
      if (0.0F + f8 > 0.0F)
      {
        f3 = -f8;
        f1 = f4;
      }
    while (true)
    {
      Message localMessage = Message.obtain();
      localMessage.what = 1;
      Bundle localBundle = new Bundle();
      localBundle.putInt("delta_x", (int)f1);
      localBundle.putInt("delta_y", (int)f3);
      localBundle.putInt("temp_x", 0);
      localBundle.putInt("temp_y", 0);
      localMessage.setData(localBundle);
      this.handler.sendMessage(localMessage);
      return;
      f1 = f4;
      f3 = f2;
      if (0.0F + f8 >= f10 - f9)
        continue;
      f3 = f10 - f9 - f8;
      f1 = f4;
      continue;
      if (f9 < f10)
      {
        if (0.0F + f5 > 0.0F)
        {
          f1 = -f5;
          f3 = f2;
          continue;
        }
        f1 = f4;
        f3 = f2;
        if (0.0F + f5 >= f7 - f6)
          continue;
        f1 = f7 - f6 - f5;
        f3 = f2;
        continue;
      }
      if (0.0F + f8 > 0.0F)
        f2 = -f8;
      while (true)
      {
        if (0.0F + f5 <= 0.0F)
          break label323;
        f1 = -f5;
        f3 = f2;
        break;
        f2 = f1;
        if (0.0F + f8 >= f10 - f9)
          continue;
        f2 = f10 - f9 - f8;
      }
      label323: f1 = f4;
      f3 = f2;
      if (0.0F + f5 >= f7 - f6)
        continue;
      f1 = f7 - f6 - f5;
      f3 = f2;
    }
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null);
    do
    {
      return;
      this.mBitmapDisplayed = paramBitmap;
      if (getWidth() <= 0)
      {
        this.handler.sendEmptyMessageDelayed(0, 200L);
        return;
      }
      this.mBaseMatrix.reset();
      this.mSuppMatrix.reset();
      super.setImageBitmap(paramBitmap);
      getProperBaseMatrix(this.mBitmapDisplayed, this.mBaseMatrix);
      setImageMatrix(getImageViewMatrix());
      center(true, true);
    }
    while (this.mListener == null);
    this.mListener.onBitmapSet();
  }

  public void setImageBitmapResetBase(Bitmap paramBitmap, boolean paramBoolean)
  {
    if (getWidth() <= 0)
    {
      this.mOnLayoutRunnable = new ScaleImageView.2(this, paramBitmap, paramBoolean);
      new Thread(this.mOnLayoutRunnable).start();
      return;
    }
    if (paramBitmap != null)
    {
      getProperBaseMatrix(paramBitmap, this.mBaseMatrix);
      setImageBitmap(paramBitmap);
    }
    while (true)
    {
      if (paramBoolean)
        this.mSuppMatrix.reset();
      setImageMatrix(getImageViewMatrix());
      return;
      this.mBaseMatrix.reset();
      setImageBitmap(null);
    }
  }

  public void setOnBitmapSetListener(ScaleImageView.OnBitmapSetListener paramOnBitmapSetListener)
  {
    this.mListener = paramOnBitmapSetListener;
  }

  public void zoomTo(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f2 = getScale();
    float f1 = paramFloat1;
    if (paramFloat1 < minZoom)
      f1 = minZoom;
    paramFloat1 = f1;
    if (f1 > maxZoom)
      paramFloat1 = maxZoom;
    paramFloat1 /= f2;
    this.mSuppMatrix.postScale(paramFloat1, paramFloat1, paramFloat2, paramFloat3);
    setImageMatrix(getImageMatrix());
    center(true, true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.ScaleImageView
 * JD-Core Version:    0.6.0
 */