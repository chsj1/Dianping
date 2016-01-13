package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public abstract class ImageGalleryAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, HorizontalImageGallery.OnGalleryImageClickListener
{
  private static final String CELL_IMAGEGALLERY = "0200Basic.06ImageGallery";
  private View mEmptyImageGallery = null;
  private boolean mFetchedImages = false;
  private HorizontalImageGallery mHorizontalImageGallery = null;
  private MApiRequest mImageRequest = null;

  public ImageGalleryAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected MApiRequest getImagesRequest()
  {
    return null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    do
    {
      do
        return;
      while (this.mFetchedImages);
      this.mFetchedImages = true;
      this.mImageRequest = getImagesRequest();
    }
    while (this.mImageRequest == null);
    getFragment().mapiService().exec(this.mImageRequest, this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mImageRequest != null)
    {
      getFragment().mapiService().abort(this.mImageRequest, this, true);
      this.mImageRequest = null;
    }
  }

  protected void onEmptyClicked()
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mImageRequest)
    {
      this.mImageRequest = null;
      requestImageFinish(false, paramMApiResponse);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mImageRequest)
    {
      this.mImageRequest = null;
      requestImageFinish(true, paramMApiResponse);
    }
  }

  protected void requestImageFinish(boolean paramBoolean, MApiResponse paramMApiResponse)
  {
  }

  public void setImages(String[] paramArrayOfString, int paramInt)
  {
    if (paramArrayOfString == null)
      return;
    if (paramArrayOfString.length < paramInt);
    for (boolean bool = true; ; bool = false)
    {
      setImages(paramArrayOfString, bool);
      return;
    }
  }

  public void setImages(String[] paramArrayOfString, boolean paramBoolean)
  {
    if (paramArrayOfString == null);
    do
    {
      do
      {
        return;
        this.mFetchedImages = true;
        if (this.mHorizontalImageGallery == null)
        {
          FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(getContext()).inflate(R.layout.shop_image_gallery, getParentView(), false);
          this.mHorizontalImageGallery = ((HorizontalImageGallery)localFrameLayout.findViewById(R.id.image_gallery));
          this.mHorizontalImageGallery.setOnGalleryImageClickListener(this);
          this.mEmptyImageGallery = localFrameLayout.findViewById(R.id.image_gallery_empty);
          this.mEmptyImageGallery.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              ImageGalleryAgent.this.onEmptyClicked();
            }
          });
          addCell("0200Basic.06ImageGallery", localFrameLayout);
        }
        this.mHorizontalImageGallery.removeAllImages();
        this.mHorizontalImageGallery.addImages(paramArrayOfString, paramBoolean);
      }
      while (paramArrayOfString.length != 0);
      if (this.mEmptyImageGallery == null)
        continue;
      this.mEmptyImageGallery.setVisibility(0);
    }
    while (this.mHorizontalImageGallery == null);
    this.mHorizontalImageGallery.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ImageGalleryAgent
 * JD-Core Version:    0.6.0
 */