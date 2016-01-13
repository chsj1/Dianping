package com.dianping.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView.ScaleType;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import uk.co.senab.photoview.IPhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class ZoomNetworkImageView extends NetworkImageView
  implements IPhotoView, OnLoadChangeListener
{
  private OnLoadImageListener listener;
  private final PhotoViewAttacher mAttacher;
  private ImageView.ScaleType mPendingScaleType;
  private boolean originalZoomable;

  public ZoomNetworkImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ZoomNetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ZoomNetworkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    super.setScaleType(ImageView.ScaleType.CENTER);
    this.mAttacher = createPhotoViewAttacher();
    if (this.mPendingScaleType != null)
    {
      setScaleType(this.mPendingScaleType);
      this.mPendingScaleType = null;
    }
    setLoadChangeListener(this);
  }

  private boolean isPlaceHolderRes(int paramInt)
  {
    return (paramInt == this.placeholderEmpty) || (paramInt == this.placeholderError) || (paramInt == this.placeholderLoading);
  }

  public boolean canZoom()
  {
    return this.mAttacher.canZoom();
  }

  protected PhotoViewAttacher createPhotoViewAttacher()
  {
    return new PhotoViewAttacher(this);
  }

  protected Bitmap getBitmapFromResponse(Response paramResponse)
  {
    paramResponse = super.getBitmapFromResponse(paramResponse);
    float f;
    if ((paramResponse.getHeight() != 0) && (paramResponse.getWidth() != 0))
    {
      if (getWidth() <= getHeight())
        break label65;
      f = getWidth() / paramResponse.getWidth();
    }
    while (true)
    {
      setScale(f, getWidth() / 2.0F, getHeight() / 2.0F, false);
      return paramResponse;
      label65: f = getHeight() / paramResponse.getHeight();
    }
  }

  public Matrix getDisplayMatrix()
  {
    return this.mAttacher.getDrawMatrix();
  }

  public RectF getDisplayRect()
  {
    return this.mAttacher.getDisplayRect();
  }

  public IPhotoView getIPhotoViewImplementation()
  {
    return this.mAttacher;
  }

  public float getMaxScale()
  {
    return getMaximumScale();
  }

  public float getMaximumScale()
  {
    return this.mAttacher.getMaximumScale();
  }

  public float getMediumScale()
  {
    return this.mAttacher.getMediumScale();
  }

  public float getMidScale()
  {
    return getMediumScale();
  }

  public float getMinScale()
  {
    return getMinimumScale();
  }

  public float getMinimumScale()
  {
    return this.mAttacher.getMinimumScale();
  }

  public PhotoViewAttacher.OnPhotoTapListener getOnPhotoTapListener()
  {
    return this.mAttacher.getOnPhotoTapListener();
  }

  public PhotoViewAttacher.OnViewTapListener getOnViewTapListener()
  {
    return this.mAttacher.getOnViewTapListener();
  }

  public float getScale()
  {
    return this.mAttacher.getScale();
  }

  public ImageView.ScaleType getScaleType()
  {
    return this.mAttacher.getScaleType();
  }

  public Bitmap getVisibleRectangleBitmap()
  {
    return this.mAttacher.getVisibleRectangleBitmap();
  }

  protected void onDetachedFromWindow()
  {
    this.mAttacher.cleanup();
    super.onDetachedFromWindow();
  }

  public void onImageLoadFailed()
  {
    super.setScaleType(ImageView.ScaleType.MATRIX);
    setZoomable(this.originalZoomable);
  }

  public void onImageLoadStart()
  {
    setZoomable(false);
  }

  public void onImageLoadSuccess(Bitmap paramBitmap)
  {
    super.setScaleType(ImageView.ScaleType.MATRIX);
    setZoomable(this.originalZoomable);
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    super.onRequestFailed(paramRequest, paramResponse);
    if (this.listener != null)
      this.listener.onLoadFailed();
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    super.onRequestFinish(paramRequest, paramResponse);
    if (this.listener != null)
      this.listener.onLoadSuccess();
  }

  public void setAllowParentInterceptOnEdge(boolean paramBoolean)
  {
    this.mAttacher.setAllowParentInterceptOnEdge(paramBoolean);
  }

  public boolean setDisplayMatrix(Matrix paramMatrix)
  {
    return this.mAttacher.setDisplayMatrix(paramMatrix);
  }

  public void setImageDrawable(Drawable paramDrawable)
  {
    super.setImageDrawable(paramDrawable);
    if (this.mAttacher != null)
      this.mAttacher.update();
  }

  public void setImageResource(int paramInt)
  {
    super.setImageResource(paramInt);
    if ((this.mAttacher != null) && (!isPlaceHolderRes(paramInt)))
      this.mAttacher.update();
  }

  public void setImageURI(Uri paramUri)
  {
    super.setImageURI(paramUri);
    if (this.mAttacher != null)
      this.mAttacher.update();
  }

  public void setImageZoomable(boolean paramBoolean)
  {
    this.originalZoomable = paramBoolean;
    setZoomable(paramBoolean);
  }

  public void setMaxScale(float paramFloat)
  {
    setMaximumScale(paramFloat);
  }

  public void setMaximumScale(float paramFloat)
  {
    this.mAttacher.setMaximumScale(paramFloat);
  }

  public void setMediumScale(float paramFloat)
  {
    this.mAttacher.setMediumScale(paramFloat);
  }

  public void setMidScale(float paramFloat)
  {
    setMediumScale(paramFloat);
  }

  public void setMinScale(float paramFloat)
  {
    setMinimumScale(paramFloat);
  }

  public void setMinimumScale(float paramFloat)
  {
    this.mAttacher.setMinimumScale(paramFloat);
  }

  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    this.mAttacher.setOnDoubleTapListener(paramOnDoubleTapListener);
  }

  public void setOnLoadImageListener(OnLoadImageListener paramOnLoadImageListener)
  {
    this.listener = paramOnLoadImageListener;
  }

  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mAttacher.setOnLongClickListener(paramOnLongClickListener);
  }

  public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener paramOnMatrixChangedListener)
  {
    this.mAttacher.setOnMatrixChangeListener(paramOnMatrixChangedListener);
  }

  public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener paramOnPhotoTapListener)
  {
    this.mAttacher.setOnPhotoTapListener(paramOnPhotoTapListener);
  }

  public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener paramOnViewTapListener)
  {
    this.mAttacher.setOnViewTapListener(paramOnViewTapListener);
  }

  public void setPhotoViewRotation(float paramFloat)
  {
    this.mAttacher.setPhotoViewRotation(paramFloat);
  }

  public void setRotationBy(float paramFloat)
  {
    this.mAttacher.setRotationBy(paramFloat);
  }

  public void setRotationTo(float paramFloat)
  {
    this.mAttacher.setRotationTo(paramFloat);
  }

  public void setScale(float paramFloat)
  {
    this.mAttacher.setScale(paramFloat);
  }

  public void setScale(float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
  {
    this.mAttacher.setScale(paramFloat1, paramFloat2, paramFloat3, paramBoolean);
  }

  public void setScale(float paramFloat, boolean paramBoolean)
  {
    this.mAttacher.setScale(paramFloat, paramBoolean);
  }

  public void setScaleType(ImageView.ScaleType paramScaleType)
  {
    if (this.mAttacher != null)
    {
      this.mAttacher.setScaleType(paramScaleType);
      return;
    }
    this.mPendingScaleType = paramScaleType;
  }

  public void setZoomTransitionDuration(int paramInt)
  {
    this.mAttacher.setZoomTransitionDuration(paramInt);
  }

  public void setZoomable(boolean paramBoolean)
  {
    this.mAttacher.setZoomable(paramBoolean);
  }

  public static abstract interface OnLoadImageListener
  {
    public abstract void onLoadFailed();

    public abstract void onLoadSuccess();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.ZoomNetworkImageView
 * JD-Core Version:    0.6.0
 */