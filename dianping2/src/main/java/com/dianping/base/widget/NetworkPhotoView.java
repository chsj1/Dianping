package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.dianping.dataservice.Request;
import com.dianping.v1.R.dimen;
import com.dianping.widget.NetworkImageView;

public class NetworkPhotoView extends NetworkImageView
{
  protected Bitmap bitmap;
  int currentBytes;
  private Paint defaultPaint;
  String percent = " ";
  int totalBytes;

  public NetworkPhotoView(Context paramContext)
  {
    super(paramContext);
    this.isPhoto = true;
  }

  public NetworkPhotoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.isPhoto = true;
  }

  public NetworkPhotoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.isPhoto = true;
  }

  protected boolean discard()
  {
    this.currentBytes = 0;
    this.totalBytes = 0;
    return super.discard();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if ((Boolean.FALSE.equals(this.imageRetrieve)) && (this.totalBytes > 0))
      if ((!"flag".equals(this.percent)) && (!"".equals(this.percent)))
        break label155;
    label155: for (this.percent = ""; ; this.percent = (this.currentBytes * 100 / this.totalBytes + "%"))
    {
      if (this.defaultPaint == null)
      {
        this.defaultPaint = new Paint();
        this.defaultPaint.setColor(getContext().getResources().getColor(17170435));
        this.defaultPaint.setTextAlign(Paint.Align.CENTER);
        this.defaultPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_very_small));
      }
      paramCanvas.drawText(this.percent, getWidth() / 2.0F, getHeight() / 2.0F - this.defaultPaint.ascent(), this.defaultPaint);
      return;
    }
  }

  public void onRequestProgress(Request paramRequest, int paramInt1, int paramInt2)
  {
    this.totalBytes = paramInt2;
    this.currentBytes = paramInt1;
    invalidate();
  }

  public void setDrawable(Drawable paramDrawable, boolean paramBoolean)
  {
    super.setDrawable(paramDrawable, paramBoolean);
    if (this.bitmap != null)
    {
      this.bitmap.recycle();
      this.bitmap = null;
    }
    if ((!paramBoolean) && ((paramDrawable instanceof BitmapDrawable)))
      this.bitmap = ((BitmapDrawable)paramDrawable).getBitmap();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NetworkPhotoView
 * JD-Core Version:    0.6.0
 */