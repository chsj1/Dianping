package com.dianping.base.widget.phototag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class PictureDecalView extends View
{
  public static final float MAX_SCALE_SIZE = 200.2F;
  public static final float MIN_SCALE_SIZE = 0.001F;
  public DecalItem decalItem;
  private boolean hasAddedImage;
  private boolean isFocused;
  private int itemHeight;
  private int itemWidth;
  private long lastInvalidateTime = System.currentTimeMillis();
  private boolean loadingBitmapRotate;
  private Handler loadingHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      PictureDecalView.this.invalidate();
      PictureDecalView localPictureDecalView = PictureDecalView.this;
      if (!PictureDecalView.this.loadingBitmapRotate);
      for (boolean bool = true; ; bool = false)
      {
        PictureDecalView.access$002(localPictureDecalView, bool);
        super.handleMessage(paramMessage);
        return;
      }
    }
  };
  private Bitmap mBitmap;
  private Paint mBorderPaint;
  private RectF mContentRect = new RectF();
  private Bitmap mControllerBitmap;
  private float mControllerHeight;
  private float mControllerWidth;
  private float mDecalScaleSize = -1.0F;
  private Bitmap mDeleteBitmap;
  private float mDeleteHeight;
  private float mDeleteWidth;
  private boolean mDrawController = true;
  private boolean mInController;
  private boolean mInDelete = false;
  private boolean mInMove;
  private float mLastPointX;
  private float mLastPointY;
  private Bitmap mLoadingBitmap;
  private Bitmap mLoadingBitmapRotate;
  private Matrix mMatrix = new Matrix();
  private OnDecalDeleteListener mOnDecalDeleteListener;
  private OnDecalFocusedListener mOnDecalFocusedListener;
  private OnRetryListener mOnRetryListener;
  private RectF mOriginContentRect;
  private float[] mOriginPoints;
  private Paint mPaint;
  private float[] mPoints = new float[10];
  private Bitmap mRetryBitmap;
  private RectF mViewRect;
  public boolean showLoading;
  public boolean showRetry;

  public PictureDecalView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PictureDecalView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public PictureDecalView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private float caculateLength(float paramFloat1, float paramFloat2)
  {
    paramFloat1 -= this.mPoints[8];
    paramFloat2 -= this.mPoints[9];
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
  }

  private float calculateDegree(float paramFloat1, float paramFloat2)
  {
    double d = paramFloat1 - this.mPoints[8];
    return (float)Math.toDegrees(Math.atan2(paramFloat2 - this.mPoints[9], d));
  }

  private boolean canDecalMove(float paramFloat1, float paramFloat2)
  {
    float f1 = this.mPoints[8];
    float f2 = this.mPoints[9];
    return this.mViewRect.contains(paramFloat1 + f1, paramFloat2 + f2);
  }

  private void doDeleteDecal()
  {
    this.mBitmap = null;
    invalidate();
    if (this.mOnDecalDeleteListener != null)
      this.mOnDecalDeleteListener.onDelete();
  }

  private void init()
  {
    this.mPaint = new Paint();
    this.mPaint.setAntiAlias(true);
    this.mPaint.setFilterBitmap(true);
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mBorderPaint = new Paint(this.mPaint);
    this.mBorderPaint.setColor(Color.parseColor("#595959"));
    this.mBorderPaint.setStrokeWidth(4.0F);
    this.mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ugc_decal_control);
    this.mControllerWidth = (this.mControllerBitmap.getWidth() + 4);
    this.mControllerHeight = (this.mControllerBitmap.getHeight() + 4);
    this.mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ugc_decal_delete);
    this.mDeleteWidth = (this.mDeleteBitmap.getWidth() + 4);
    this.mDeleteHeight = (this.mDeleteBitmap.getHeight() + 4);
    this.mLoadingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ugc_decal_loading);
    Matrix localMatrix = new Matrix();
    localMatrix.setRotate(180.0F);
    try
    {
      this.mLoadingBitmapRotate = Bitmap.createBitmap(this.mLoadingBitmap, 0, 0, this.mLoadingBitmap.getWidth(), this.mLoadingBitmap.getHeight(), localMatrix, true);
      this.mRetryBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ugc_decal_retry);
      this.mPoints = new float[10];
      this.mContentRect = new RectF();
      this.mMatrix = new Matrix();
      return;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      while (true)
        this.mLoadingBitmapRotate = this.mLoadingBitmap;
    }
  }

  private boolean isInController(float paramFloat1, float paramFloat2)
  {
    float f1 = this.mPoints[2];
    float f2 = this.mPoints[3];
    if (new RectF(f1 - this.mControllerWidth / 2.0F, f2 - this.mControllerHeight / 2.0F, this.mControllerWidth / 2.0F + f1, this.mControllerHeight / 2.0F + f2).contains(paramFloat1, paramFloat2))
    {
      if (!this.isFocused)
        setFocused(true);
    }
    else
      return false;
    return true;
  }

  private boolean isInDelete(float paramFloat1, float paramFloat2)
  {
    float f1 = this.mPoints[6];
    float f2 = this.mPoints[7];
    if (new RectF(f1 - this.mDeleteWidth / 2.0F, f2 - this.mDeleteHeight / 2.0F, this.mDeleteWidth / 2.0F + f1, this.mDeleteHeight / 2.0F + f2).contains(paramFloat1, paramFloat2))
    {
      if (!this.isFocused)
        setFocused(true);
    }
    else
      return false;
    return true;
  }

  private float rotation(MotionEvent paramMotionEvent)
  {
    float f1 = calculateDegree(this.mLastPointX, this.mLastPointY);
    float f2 = calculateDegree(paramMotionEvent.getX(), paramMotionEvent.getY());
    updateRotateData();
    return f2 - f1;
  }

  private void updateRotateData()
  {
    float f = calculateDegree((this.mPoints[2] + this.mPoints[4]) / 2.0F, (this.mPoints[3] + this.mPoints[5]) / 2.0F);
    if ((0.0F <= f) && (f <= 180.0F))
    {
      this.decalItem.rotateAngle = f;
      return;
    }
    this.decalItem.rotateAngle = ((360.0F + f) % 360.0F);
  }

  public Bitmap getBitmap()
  {
    Bitmap localBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    this.mDrawController = false;
    draw(localCanvas);
    this.mDrawController = true;
    localCanvas.save();
    return localBitmap;
  }

  public boolean getFocused()
  {
    return this.isFocused;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mMatrix == null);
    while (true)
    {
      return;
      this.mMatrix.mapPoints(this.mPoints, this.mOriginPoints);
      this.mMatrix.mapRect(this.mContentRect, this.mOriginContentRect);
      if ((this.showRetry) || (this.mBitmap == null))
        break;
      paramCanvas.drawBitmap(this.mBitmap, this.mMatrix, this.mPaint);
      if ((this.mDrawController) && (this.isFocused))
      {
        paramCanvas.drawLine(this.mPoints[0], this.mPoints[1], this.mPoints[2], this.mPoints[3], this.mBorderPaint);
        paramCanvas.drawLine(this.mPoints[2], this.mPoints[3], this.mPoints[4], this.mPoints[5], this.mBorderPaint);
        paramCanvas.drawLine(this.mPoints[4], this.mPoints[5], this.mPoints[6], this.mPoints[7], this.mBorderPaint);
        paramCanvas.drawLine(this.mPoints[6], this.mPoints[7], this.mPoints[0], this.mPoints[1], this.mBorderPaint);
        paramCanvas.drawBitmap(this.mControllerBitmap, this.mPoints[2] - this.mControllerWidth / 2.0F, this.mPoints[3] - this.mControllerWidth / 2.0F, this.mBorderPaint);
        paramCanvas.drawBitmap(this.mDeleteBitmap, this.mPoints[6] - this.mDeleteWidth / 2.0F, this.mPoints[7] - this.mDeleteHeight / 2.0F, this.mBorderPaint);
      }
      if ((!this.showLoading) || (System.currentTimeMillis() - this.lastInvalidateTime <= 200L))
        continue;
      this.lastInvalidateTime = System.currentTimeMillis();
      updateData();
      if (!this.loadingBitmapRotate)
        break label463;
    }
    label463: for (Bitmap localBitmap = this.mLoadingBitmap; ; localBitmap = this.mLoadingBitmapRotate)
    {
      paramCanvas.drawBitmap(localBitmap, this.decalItem.centerX - this.mLoadingBitmap.getWidth() / 2, this.decalItem.centerY - this.mLoadingBitmap.getHeight() / 2, this.mBorderPaint);
      this.loadingHandler.sendEmptyMessageDelayed(0, 200L);
      return;
      if (!this.showRetry)
        break;
      updateData();
      paramCanvas.drawBitmap(this.mRetryBitmap, this.decalItem.centerX - this.mRetryBitmap.getWidth() / 2, this.decalItem.centerY - this.mRetryBitmap.getHeight() / 2, this.mPaint);
      break;
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mContentRect == null)
      this.mContentRect = new RectF();
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    if ((this.mContentRect.contains(f1, f2)) && (!this.isFocused))
    {
      setFocused(true);
      return true;
    }
    if ((!this.mContentRect.contains(f1, f2)) && (!isInDelete(f1, f2)) && (!isInController(f1, f2)))
      return false;
    if (this.mViewRect == null)
      this.mViewRect = new RectF(0.0F, 0.0F, getMeasuredWidth(), getMeasuredHeight());
    if (this.mViewRect == null)
      this.mViewRect = new RectF(0.0F, 0.0F, getMeasuredWidth(), getMeasuredHeight());
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 1:
    case 3:
    case 2:
    }
    while (true)
    {
      return true;
      if ((this.mContentRect.contains(f1, f2)) && (this.showRetry) && (this.mOnRetryListener != null))
      {
        this.mOnRetryListener.onRetry();
        showRetry(false);
        this.showLoading = true;
        this.loadingHandler.sendEmptyMessageDelayed(0, 500L);
      }
      if (isInController(f1, f2))
      {
        if (!this.isFocused)
          return false;
        this.mInController = true;
        this.mLastPointY = f2;
        this.mLastPointX = f1;
        continue;
      }
      if (isInDelete(f1, f2))
      {
        if (!this.isFocused)
          return false;
        this.mInDelete = true;
        continue;
      }
      if (this.mContentRect.contains(f1, f2))
      {
        if (!this.isFocused)
        {
          setFocused(true);
          return false;
        }
        this.mLastPointY = f2;
        this.mLastPointX = f1;
        this.mInMove = true;
        continue;
      }
      if ((isInDelete(f1, f2)) && (this.mInDelete))
      {
        doDeleteDecal();
        continue;
      }
      this.mLastPointX = 0.0F;
      this.mLastPointY = 0.0F;
      this.mInController = false;
      this.mInMove = false;
      this.mInDelete = false;
      continue;
      if (this.mInController)
      {
        if (!this.isFocused)
        {
          setFocused(true);
          return false;
        }
        this.mMatrix.postRotate(rotation(paramMotionEvent), this.mPoints[8], this.mPoints[9]);
        f3 = caculateLength(this.mPoints[0], this.mPoints[1]);
        f4 = caculateLength(paramMotionEvent.getX(), paramMotionEvent.getY());
        if (Math.sqrt((f3 - f4) * (f3 - f4)) > 0.0D)
        {
          f3 = f4 / f3;
          f4 = this.mDecalScaleSize * f3;
          if ((f4 >= 0.001F) && (f4 <= 200.2F))
          {
            this.mMatrix.postScale(f3, f3, this.mPoints[8], this.mPoints[9]);
            this.mDecalScaleSize = f4;
          }
        }
        invalidate();
        this.mLastPointX = f1;
        this.mLastPointY = f2;
        continue;
      }
      if (this.mInMove != true)
        continue;
      if (!this.isFocused)
      {
        setFocused(true);
        return false;
      }
      float f3 = f1 - this.mLastPointX;
      float f4 = f2 - this.mLastPointY;
      this.mInController = false;
      int i = ViewUtils.dip2px(getContext(), 30.0F);
      int j = ViewUtils.dip2px(getContext(), 60.0F);
      if (((f2 < 0.0F) && (Math.max(Math.max(Math.max(this.mPoints[1], this.mPoints[3]), this.mPoints[5]), this.mPoints[7]) < i) && (f4 < 0.0F)) || ((getHeight() > 0) && (f2 > getHeight()) && (Math.min(Math.min(Math.min(this.mPoints[1], this.mPoints[3]), this.mPoints[5]), this.mPoints[7]) > getHeight() - j) && (f4 > 0.0F)) || (Math.sqrt(f3 * f3 + f4 * f4) <= 1.0D))
        continue;
      this.mMatrix.postTranslate(f3, f4);
      postInvalidate();
      this.mLastPointX = f1;
      this.mLastPointY = f2;
    }
  }

  public void setDecalItem(DecalItem paramDecalItem)
  {
    this.decalItem = paramDecalItem;
  }

  public void setFocused(boolean paramBoolean)
  {
    if (this.isFocused != paramBoolean)
    {
      this.isFocused = paramBoolean;
      postInvalidate();
      if (this.mOnDecalFocusedListener != null)
        this.mOnDecalFocusedListener.onFocused(paramBoolean);
    }
  }

  public void setImage(Bitmap paramBitmap)
  {
    this.showLoading = false;
    this.loadingHandler.sendEmptyMessageDelayed(0, 500L);
    this.hasAddedImage = true;
    if (paramBitmap == null)
    {
      this.showRetry = true;
      this.mBitmap = null;
      postInvalidate();
      return;
    }
    this.showRetry = false;
    int i = this.itemWidth;
    int j = this.itemHeight;
    int k = 10;
    int m = 10;
    if (this.mBitmap != null)
    {
      i = (int)(this.mBitmap.getWidth() * this.mDecalScaleSize);
      j = (int)(this.mBitmap.getHeight() * this.mDecalScaleSize);
    }
    if (this.mPoints != null)
    {
      k = (int)this.mPoints[0];
      m = (int)this.mPoints[1];
    }
    updateData();
    setImage(paramBitmap, k, m, i, j);
  }

  public void setImage(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.hasAddedImage)
    {
      this.showLoading = false;
      if (paramBitmap == null)
        this.showRetry = true;
    }
    while (true)
    {
      this.loadingHandler.sendEmptyMessageDelayed(0, 500L);
      if (paramBitmap == null)
        this.mBitmap = null;
      this.itemWidth = paramInt3;
      this.itemHeight = paramInt4;
      float f2;
      float f1;
      if (paramBitmap == null)
      {
        f2 = paramInt3;
        f1 = paramInt4;
        label68: this.mPoints = new float[10];
        this.mContentRect = new RectF();
        this.mMatrix = new Matrix();
        this.mBitmap = paramBitmap;
        this.mDecalScaleSize = 1.0F;
      }
      try
      {
        this.mOriginPoints = new float[] { 0.0F, 0.0F, f2, 0.0F, f2, f1, 0.0F, f1, f2 / 2.0F, f1 / 2.0F };
        this.mOriginContentRect = new RectF(0.0F, 0.0F, f2, f1);
        f2 = 1.0F * paramInt3 / f2;
        f1 = 1.0F * paramInt4 / f1;
        if (f2 < f1)
          f1 = f2;
        while (true)
        {
          this.mDecalScaleSize *= f1;
          this.mMatrix.postScale(f1, f1, this.mPoints[0], this.mPoints[1]);
          f1 = paramInt1;
          f2 = paramInt2;
          this.mMatrix.postTranslate(f1, f2);
          this.mPoints[0] = f1;
          this.mPoints[1] = f2;
          if ((this.decalItem != null) && (this.decalItem.rotateAngle != 0.0F) && (this.decalItem.rotateAngle > 1.0F) && (this.decalItem.rotateAngle < 359.0F))
            this.mMatrix.postRotate(this.decalItem.rotateAngle, this.mPoints[0] + paramInt3 / 2, this.mPoints[1] + paramInt4 / 2);
          postInvalidate();
          return;
          this.showRetry = false;
          break;
          this.showRetry = false;
          this.showLoading = true;
          this.hasAddedImage = true;
          break;
          f2 = paramBitmap.getWidth();
          f1 = paramBitmap.getHeight();
          break label68;
        }
      }
      catch (Exception paramBitmap)
      {
        while (true)
          paramBitmap.printStackTrace();
      }
    }
  }

  public void setOnDecalDeleteListener(OnDecalDeleteListener paramOnDecalDeleteListener)
  {
    this.mOnDecalDeleteListener = paramOnDecalDeleteListener;
  }

  public void setOnDecalFocusedListener(OnDecalFocusedListener paramOnDecalFocusedListener)
  {
    this.mOnDecalFocusedListener = paramOnDecalFocusedListener;
  }

  public void setOnRetryListener(OnRetryListener paramOnRetryListener)
  {
    this.mOnRetryListener = paramOnRetryListener;
  }

  public void showRetry(boolean paramBoolean)
  {
    if (this.showRetry != paramBoolean)
    {
      this.showRetry = paramBoolean;
      if (paramBoolean)
        this.showLoading = false;
      postInvalidate();
    }
  }

  public void updateData()
  {
    this.decalItem.centerX = ((int)(this.mPoints[0] + this.mPoints[4]) / 2);
    this.decalItem.centerY = ((int)(this.mPoints[1] + this.mPoints[5]) / 2);
    this.decalItem.width = (int)(caculateLength((this.mPoints[0] + this.mPoints[6]) / 2.0F, (this.mPoints[1] + this.mPoints[7]) / 2.0F) * 2.0F);
    this.decalItem.height = (int)(caculateLength((this.mPoints[0] + this.mPoints[2]) / 2.0F, (this.mPoints[1] + this.mPoints[3]) / 2.0F) * 2.0F);
  }

  public static class DecalItem
  {
    public String ID;
    public int centerX;
    public int centerY;
    public String groupId;
    public int height;
    public boolean isFirstAddNoneFocused;
    public String name;
    public float rotateAngle;
    public String url;
    public int width;
  }

  public static abstract interface OnDecalDeleteListener
  {
    public abstract void onDelete();
  }

  public static abstract interface OnDecalFocusedListener
  {
    public abstract void onFocused(boolean paramBoolean);
  }

  public static abstract interface OnRetryListener
  {
    public abstract void onRetry();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.phototag.PictureDecalView
 * JD-Core Version:    0.6.0
 */