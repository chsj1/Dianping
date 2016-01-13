package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class CircleImageView extends NetworkThumbView
{
  private int mStrokeColor = 0;
  private int mStrokeWidth = 0;

  public CircleImageView(Context paramContext)
  {
    super(paramContext);
  }

  public CircleImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public Bitmap getCircleBitmap(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null)
    {
      paramBitmap = null;
      return paramBitmap;
    }
    if (paramInt == 0)
      paramInt = Math.min(paramBitmap.getWidth(), paramBitmap.getHeight());
    while (true)
    {
      Bitmap localBitmap = Bitmap.createBitmap(paramInt, paramInt, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      Paint localPaint = new Paint();
      Rect localRect = new Rect(this.mStrokeWidth, this.mStrokeWidth, paramInt - this.mStrokeWidth, paramInt - this.mStrokeWidth);
      RectF localRectF = new RectF(localRect);
      localPaint.setAntiAlias(true);
      localCanvas.drawArc(localRectF, 0.0F, 360.0F, true, localPaint);
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
      paramBitmap = localBitmap;
      if (this.mStrokeWidth <= 0)
        break;
      paramBitmap = new RectF(new Rect(0, 0, paramInt, paramInt));
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
      localPaint.setColor(this.mStrokeColor);
      localCanvas.drawArc(paramBitmap, 0.0F, 360.0F, false, localPaint);
      return localBitmap;
    }
  }

  public void setImageBitmap(Bitmap paramBitmap)
  {
    if (this.savedScaleType != null)
      setScaleType(this.savedScaleType);
    Object localObject = paramBitmap;
    if (this.direction != 0)
    {
      localObject = new Matrix();
      ((Matrix)localObject).setRotate(this.direction, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
      localObject = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, false);
    }
    this.currentPlaceholder = false;
    super.setImageBitmap(getCircleBitmap((Bitmap)localObject, 0));
  }

  public void setImageResource(int paramInt)
  {
    setImageBitmap(((BitmapDrawable)getContext().getResources().getDrawable(paramInt)).getBitmap());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CircleImageView
 * JD-Core Version:    0.6.0
 */