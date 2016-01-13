package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import java.util.ArrayList;

public class MultiHeaderView extends DefaultShopInfoHeaderView
{
  protected FrameLayout imageGalleryLayout;
  protected View.OnClickListener mEmptyImageClickListener;
  protected View mEmptyImageGallery;
  protected HorizontalImageGallery mHorizontalImageGallery;
  protected HorizontalImageGallery.OnGalleryImageClickListener mOnGalleryImageClickListener;

  public MultiHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public MultiHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int getAvailableWith()
  {
    return ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 70.0F) - ViewUtils.getViewWidth(this.shopPower) - ViewUtils.getViewWidth(this.reviewCount) - ViewUtils.getViewWidth(this.priceAvg);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.imageGalleryLayout = ((FrameLayout)findViewById(R.id.multi_pic));
    if (this.imageGalleryLayout != null)
    {
      this.mHorizontalImageGallery = ((HorizontalImageGallery)this.imageGalleryLayout.findViewById(R.id.image_gallery));
      this.mEmptyImageGallery = this.imageGalleryLayout.findViewById(R.id.image_gallery_empty);
    }
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    String str = null;
    if (paramDPObject.getObject("ClientShopStyle") != null)
      str = paramDPObject.getObject("ClientShopStyle").getString("PicMode");
    if ("multipic".equals(str))
      setMultiImage(paramDPObject);
  }

  protected void setMultiImage(DPObject paramDPObject)
  {
    if (this.mHorizontalImageGallery == null)
      return;
    this.mHorizontalImageGallery.removeAllImages();
    paramDPObject = paramDPObject.getArray("AdvancedPics");
    if ((paramDPObject != null) && (paramDPObject.length > 0))
    {
      int j = paramDPObject.length;
      ArrayList localArrayList = new ArrayList(j);
      int i = 0;
      while (i < j)
      {
        localArrayList.add(paramDPObject[i].getString("ThumbUrl"));
        i += 1;
      }
      if (this.mEmptyImageGallery != null)
        this.mEmptyImageGallery.setVisibility(8);
      this.mHorizontalImageGallery.setVisibility(0);
      this.mHorizontalImageGallery.addImages((String[])localArrayList.toArray(new String[0]), true);
      return;
    }
    if ((this.mEmptyImageGallery != null) && (this.shopStatus == 0))
    {
      this.mEmptyImageGallery.setVisibility(0);
      this.mEmptyImageGallery.setBackgroundColor(getResources().getColor(R.color.shopinfo_multi_upload_backgroud_color));
    }
    this.mHorizontalImageGallery.setVisibility(8);
  }

  public void setOnEmptyClickedListener(View.OnClickListener paramOnClickListener)
  {
    this.mEmptyImageClickListener = paramOnClickListener;
    if (this.mEmptyImageClickListener != null)
      this.mEmptyImageGallery.setOnClickListener(this.mEmptyImageClickListener);
  }

  public void setOnGalleryImageClickListener(HorizontalImageGallery.OnGalleryImageClickListener paramOnGalleryImageClickListener)
  {
    this.mOnGalleryImageClickListener = paramOnGalleryImageClickListener;
    if (this.mOnGalleryImageClickListener != null)
      this.mHorizontalImageGallery.setOnGalleryImageClickListener(this.mOnGalleryImageClickListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.MultiHeaderView
 * JD-Core Version:    0.6.0
 */