package com.dianping.selectdish.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.google.zxing.client.android.DPViewfinderView;

public class SelectDishesViewfinderView extends DPViewfinderView
{
  private Bitmap bottomLeftCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_bottom_left);
  private Bitmap bottomRightCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_bottom_right);
  private Rect frame = new Rect();
  private boolean isFirstPaint = true;
  private Bitmap laserScanerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_line);
  private int maskColor = getResources().getColor(R.color.scan_background);
  private Rect rect = new Rect();
  private Bitmap topLeftCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_top_left);
  private Bitmap topRightCornerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_top_right);

  public SelectDishesViewfinderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onDraw(Canvas paramCanvas)
  {
    if (this.cameraManager == null)
      return;
    int n = paramCanvas.getWidth();
    int j = getMeasuredHeight();
    int i = Math.min(n, j);
    int i1 = Math.max(n, j);
    int k;
    int m;
    if (n < j)
    {
      k = i / 6;
      m = i1 / 2 - k * 2;
      i = k * 4;
      this.frame.set(k, m, k + i, m + i);
      Paint localPaint = this.paint;
      if (this.resultBitmap == null)
        break label620;
      i = this.resultColor;
      label102: localPaint.setColor(i);
      this.paint.setAlpha(255);
      paramCanvas.drawRect(0.0F, 0.0F, n, this.frame.top, this.paint);
      paramCanvas.drawRect(0.0F, this.frame.top, this.frame.left, this.frame.bottom + 1, this.paint);
      paramCanvas.drawRect(this.frame.right + 1, this.frame.top, n, this.frame.bottom + 1, this.paint);
      paramCanvas.drawRect(0.0F, this.frame.bottom + 1, n, getResources().getDisplayMetrics().heightPixels, this.paint);
      paramCanvas.drawBitmap(this.topLeftCornerBitmap, this.frame.left - 10, this.frame.top - 10, null);
      paramCanvas.drawBitmap(this.topRightCornerBitmap, this.frame.right + 10 - this.topRightCornerBitmap.getWidth(), this.frame.top - 10, null);
      paramCanvas.drawBitmap(this.bottomLeftCornerBitmap, this.frame.left - 10, this.frame.bottom - this.bottomLeftCornerBitmap.getHeight() + 10, null);
      paramCanvas.drawBitmap(this.bottomRightCornerBitmap, this.frame.right + 10 - this.bottomLeftCornerBitmap.getWidth(), this.frame.bottom + 10 - this.bottomLeftCornerBitmap.getHeight(), null);
      this.rect.left = this.frame.left;
      this.rect.right = (this.frame.left + this.frame.width());
      if (this.isFirstPaint)
      {
        this.rect.top = (this.frame.top - this.laserScanerBitmap.getHeight() / 2);
        this.isFirstPaint = false;
      }
      if (this.rect.top + this.laserScanerBitmap.getHeight() / 2 <= this.frame.bottom)
        break label628;
      this.rect.top = (this.frame.top - this.laserScanerBitmap.getHeight() / 2);
    }
    while (true)
    {
      this.rect.bottom = (this.rect.top + this.laserScanerBitmap.getHeight());
      paramCanvas.drawBitmap(this.laserScanerBitmap, null, this.rect, this.paint);
      if (this.resultBitmap == null)
        break label647;
      this.paint.setAlpha(160);
      paramCanvas.drawBitmap(this.resultBitmap, null, this.frame, this.paint);
      return;
      m = 10;
      i -= 20;
      k = i1 / 2 - i / 2;
      break;
      label620: i = this.maskColor;
      break label102;
      label628: this.rect.top += 5;
    }
    label647: postInvalidateDelayed(80L, this.frame.left - 6, 0, this.frame.right + 6, this.frame.bottom + 6);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishesViewfinderView
 * JD-Core Version:    0.6.0
 */