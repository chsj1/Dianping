package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.dianping.model.ShopImageData;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class UploadPhotoInfoItem extends FrameLayout
{
  Bitmap mBitmap;
  NetworkImageView thumb;

  public UploadPhotoInfoItem(Context paramContext)
  {
    super(paramContext);
  }

  public UploadPhotoInfoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public UploadPhotoInfoItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public ImageView getImageView()
  {
    return this.thumb;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumb = ((NetworkImageView)findViewById(R.id.upload_photo));
  }

  public void onRotateRight(int paramInt1, int paramInt2)
  {
    if (this.thumb != null)
    {
      RotateAnimation localRotateAnimation = new RotateAnimation(paramInt1, paramInt2, 1, 0.5F, 1, 0.5F);
      localRotateAnimation.setDuration(500L);
      localRotateAnimation.setFillAfter(true);
      this.thumb.startAnimation(localRotateAnimation);
    }
  }

  public void setPhoto(ShopImageData paramShopImageData, int paramInt)
  {
    if (paramInt > 0)
      this.thumb.setImageSize(paramInt);
    this.thumb.setImage(paramShopImageData.oriPath);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UploadPhotoInfoItem
 * JD-Core Version:    0.6.0
 */