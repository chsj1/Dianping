package com.google.zxing.client.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewfinderView extends View
{
  protected static final long ANIMATION_DELAY = 80L;
  protected static final int CURRENT_POINT_OPACITY = 160;
  protected static final int MAX_RESULT_POINTS = 20;
  protected static final int POINT_SIZE = 6;
  protected static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
  protected CameraManager cameraManager;
  protected final int laserColor;
  protected List<ResultPoint> lastPossibleResultPoints;
  protected final int maskColor;
  protected final Paint paint = new Paint(1);
  protected List<ResultPoint> possibleResultPoints;
  protected Bitmap resultBitmap;
  protected final int resultColor;
  protected final int resultPointColor;
  protected int scannerAlpha;

  public ViewfinderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = getResources();
    this.maskColor = paramContext.getColor(R.color.viewfinder_mask);
    this.resultColor = paramContext.getColor(R.color.result_view);
    this.laserColor = paramContext.getColor(R.color.viewfinder_laser);
    this.resultPointColor = paramContext.getColor(R.color.possible_result_points);
    this.scannerAlpha = 0;
    this.possibleResultPoints = new ArrayList(5);
    this.lastPossibleResultPoints = null;
  }

  public void addPossibleResultPoint(ResultPoint paramResultPoint)
  {
    synchronized (this.possibleResultPoints)
    {
      ???.add(paramResultPoint);
      int i = ???.size();
      if (i > 20)
        ???.subList(0, i - 10).clear();
      return;
    }
  }

  public void drawResultBitmap(Bitmap paramBitmap)
  {
    this.resultBitmap = paramBitmap;
    invalidate();
  }

  public void drawViewfinder()
  {
    Bitmap localBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (localBitmap != null)
      localBitmap.recycle();
    invalidate();
  }

  @SuppressLint({"DrawAllocation"})
  public void onDraw(Canvas paramCanvas)
  {
    if (this.cameraManager == null);
    Rect localRect;
    do
    {
      return;
      localRect = this.cameraManager.getFramingRect();
      localObject1 = this.cameraManager.getFramingRectInPreview();
    }
    while ((localRect == null) || (localObject1 == null));
    int j = paramCanvas.getWidth();
    int m = paramCanvas.getHeight();
    Object localObject2 = this.paint;
    if (this.resultBitmap != null);
    for (int i = this.resultColor; ; i = this.maskColor)
    {
      ((Paint)localObject2).setColor(i);
      paramCanvas.drawRect(0.0F, 0.0F, j, localRect.top, this.paint);
      paramCanvas.drawRect(0.0F, localRect.top, localRect.left, localRect.bottom + 1, this.paint);
      paramCanvas.drawRect(localRect.right + 1, localRect.top, j, localRect.bottom + 1, this.paint);
      paramCanvas.drawRect(0.0F, localRect.bottom + 1, j, m, this.paint);
      if (this.resultBitmap == null)
        break;
      this.paint.setAlpha(160);
      paramCanvas.drawBitmap(this.resultBitmap, null, localRect, this.paint);
      return;
    }
    this.paint.setColor(this.laserColor);
    this.paint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
    this.scannerAlpha = ((this.scannerAlpha + 1) % SCANNER_ALPHA.length);
    i = localRect.height() / 2 + localRect.top;
    paramCanvas.drawRect(localRect.left + 2, i - 1, localRect.right - 1, i + 2, this.paint);
    float f1 = localRect.width() / ((Rect)localObject1).width();
    float f2 = localRect.height() / ((Rect)localObject1).height();
    localObject2 = this.possibleResultPoints;
    Object localObject1 = this.lastPossibleResultPoints;
    i = localRect.left;
    int k = localRect.top;
    if (((List)localObject2).isEmpty())
      this.lastPossibleResultPoints = null;
    while (localObject1 != null)
    {
      this.paint.setAlpha(80);
      this.paint.setColor(this.resultPointColor);
      monitorenter;
      Object localObject3;
      try
      {
        localObject2 = ((List)localObject1).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (ResultPoint)((Iterator)localObject2).next();
          paramCanvas.drawCircle((int)(((ResultPoint)localObject3).getX() * f1) + i, (int)(((ResultPoint)localObject3).getY() * f2) + k, 3.0F, this.paint);
        }
      }
      finally
      {
        monitorexit;
      }
      this.possibleResultPoints = new ArrayList(5);
      this.lastPossibleResultPoints = ((List)localObject2);
      this.paint.setAlpha(160);
      this.paint.setColor(this.resultPointColor);
      monitorenter;
      try
      {
        localObject3 = ((List)localObject2).iterator();
        while (((Iterator)localObject3).hasNext())
        {
          ResultPoint localResultPoint = (ResultPoint)((Iterator)localObject3).next();
          paramCanvas.drawCircle((int)(localResultPoint.getX() * f1) + i, (int)(localResultPoint.getY() * f2) + k, 6.0F, this.paint);
        }
      }
      finally
      {
        monitorexit;
      }
      monitorexit;
      continue;
      monitorexit;
    }
    postInvalidateDelayed(80L, localRect.left - 6, localRect.top - 6, localRect.right + 6, localRect.bottom + 6);
  }

  public void setCameraManager(CameraManager paramCameraManager)
  {
    this.cameraManager = paramCameraManager;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.ViewfinderView
 * JD-Core Version:    0.6.0
 */