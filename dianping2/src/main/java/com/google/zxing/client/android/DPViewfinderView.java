package com.google.zxing.client.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.google.zxing.client.android.camera.CameraManager;

public class DPViewfinderView extends ViewfinderView
{
  private Bitmap bottomLeftCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_bottom_left);
  private Bitmap bottomRightCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_bottom_right);
  private Bitmap laserScanerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_line);
  private Rect rect = new Rect();
  private Bitmap topLeftCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_top_left);
  private Bitmap topRightCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_top_right);

  public DPViewfinderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onDraw(Canvas paramCanvas)
  {
    if (this.cameraManager == null);
    Rect localRect;
    do
    {
      return;
      localRect = this.cameraManager.getFramingRect();
    }
    while (localRect == null);
    int j = paramCanvas.getWidth();
    int k = paramCanvas.getHeight();
    Paint localPaint = this.paint;
    int i;
    if (this.resultBitmap != null)
    {
      i = this.resultColor;
      localPaint.setColor(i);
      paramCanvas.drawRect(0.0F, 0.0F, j, localRect.top - 5, this.paint);
      paramCanvas.drawRect(0.0F, localRect.top - 5, localRect.left - 5, localRect.bottom + 6, this.paint);
      paramCanvas.drawRect(localRect.right + 6, localRect.top - 5, j, localRect.bottom + 6, this.paint);
      paramCanvas.drawRect(0.0F, localRect.bottom + 6, j, k, this.paint);
      this.paint.setColor(-1);
      paramCanvas.drawRect(localRect.left - 10, localRect.top - 10, localRect.left - 5, localRect.bottom + 10, this.paint);
      paramCanvas.drawRect(localRect.left - 4, localRect.top - 10, localRect.right + 3, localRect.top - 5, this.paint);
      paramCanvas.drawRect(localRect.right + 4, localRect.top - 10, localRect.right + 9, localRect.bottom + 10, this.paint);
      paramCanvas.drawRect(localRect.left - 4, localRect.bottom + 5, localRect.right + 3, localRect.bottom + 10, this.paint);
      paramCanvas.drawBitmap(this.topLeftCornerBitmap, localRect.left - 10, localRect.top - 10, null);
      paramCanvas.drawBitmap(this.topRightCornerBitmap, localRect.right + 10 - this.topRightCornerBitmap.getWidth(), localRect.top - 10, null);
      paramCanvas.drawBitmap(this.bottomLeftCornerBitmap, localRect.left - 10, localRect.bottom - this.bottomLeftCornerBitmap.getHeight() + 10, null);
      paramCanvas.drawBitmap(this.bottomRightCornerBitmap, localRect.right + 10 - this.bottomLeftCornerBitmap.getWidth(), localRect.bottom + 10 - this.bottomLeftCornerBitmap.getHeight(), null);
      this.rect.left = localRect.left;
      this.rect.right = (localRect.left + localRect.width());
      if (this.rect.top == 0)
        this.rect.top = (localRect.top - this.laserScanerBitmap.getHeight() / 2);
      if (this.rect.top + this.laserScanerBitmap.getHeight() / 2 <= localRect.bottom)
        break label639;
      this.rect.top = (localRect.top - this.laserScanerBitmap.getHeight() / 2);
    }
    while (true)
    {
      this.rect.bottom = (this.rect.top + this.laserScanerBitmap.getHeight());
      paramCanvas.drawBitmap(this.laserScanerBitmap, null, this.rect, this.paint);
      if (this.resultBitmap == null)
        break label664;
      this.paint.setAlpha(160);
      paramCanvas.drawBitmap(this.resultBitmap, null, localRect, this.paint);
      return;
      i = this.maskColor;
      break;
      label639: this.rect.top += localRect.width() / 70;
    }
    label664: postInvalidateDelayed(20L, localRect.left - 6, 0, localRect.right + 6, k);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.DPViewfinderView
 * JD-Core Version:    0.6.0
 */