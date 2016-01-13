package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class PoiImageGallery extends NovaLinearLayout
{
  private SquareHorizontalImageGallery hImageGallery;
  private PoiLargeImageView.PoiImageListener poiImageListener = null;
  private PoiLargeImageView poiLargeImageView;

  public PoiImageGallery(Context paramContext)
  {
    super(paramContext);
  }

  public PoiImageGallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.poiLargeImageView = ((PoiLargeImageView)findViewById(R.id.poi_large_image));
    this.hImageGallery = ((SquareHorizontalImageGallery)findViewById(R.id.horizontal_imagegallery));
  }

  public void setImageSource(DPObject[] paramArrayOfDPObject, HashMap<Integer, Integer> paramHashMap, int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
  {
    setImageSource(paramArrayOfDPObject, paramHashMap, paramInt1, paramString, paramBoolean, paramInt2, 0);
  }

  public void setImageSource(DPObject[] paramArrayOfDPObject, HashMap<Integer, Integer> paramHashMap, int paramInt1, String paramString, boolean paramBoolean, int paramInt2, int paramInt3)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0) || (this.hImageGallery == null))
      return;
    if (paramArrayOfDPObject.length == 1)
    {
      this.hImageGallery.setVisibility(8);
      this.poiLargeImageView.setPoiLargeImageSource(paramArrayOfDPObject[0], 0, paramString, paramBoolean, paramInt2, paramInt3);
    }
    while (true)
    {
      this.hImageGallery.setmOnSquareGalleryImageClickListener(new SquareHorizontalImageGallery.OnSquareGalleryImageClickListener(paramArrayOfDPObject, paramString, paramBoolean, paramInt2, paramInt3, paramHashMap, paramInt1)
      {
        public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
        {
          PoiImageGallery.this.poiLargeImageView.setPoiLargeImageSource(this.val$src[paramInt1], paramInt1, this.val$feedId, this.val$isTopicPage, this.val$topicId, this.val$reviewType);
          this.val$indexMaps.put(Integer.valueOf(this.val$position), Integer.valueOf(paramInt1));
        }
      });
      if (this.poiLargeImageView == null)
        break;
      this.poiLargeImageView.setPoiImageListener(this.poiImageListener);
      return;
      if (paramArrayOfDPObject.length > 1)
      {
        this.hImageGallery.setVisibility(0);
        this.hImageGallery.removeAllImages();
        int j = paramArrayOfDPObject.length;
        ArrayList localArrayList = new ArrayList(j);
        int i = 0;
        while (i < j)
        {
          localArrayList.add(paramArrayOfDPObject[i].getString("PicSmallUrl"));
          i += 1;
        }
        i = 0;
        if ((paramHashMap != null) && (paramHashMap.size() != 0) && (paramHashMap.containsKey(Integer.valueOf(paramInt1))))
          if (paramHashMap.get(Integer.valueOf(paramInt1)) == null)
            i = 0;
        while (true)
        {
          j = i;
          if (paramArrayOfDPObject.length <= i)
            j = paramArrayOfDPObject.length - 1;
          this.hImageGallery.addImages((String[])localArrayList.toArray(new String[0]), j);
          this.poiLargeImageView.setPoiLargeImageSource(paramArrayOfDPObject[j], j, paramString, paramBoolean, paramInt2, paramInt3);
          break;
          i = ((Integer)paramHashMap.get(Integer.valueOf(paramInt1))).intValue();
          continue;
          paramHashMap.put(Integer.valueOf(paramInt1), Integer.valueOf(0));
        }
      }
      this.hImageGallery.setBackgroundColor(getResources().getColor(R.color.shopinfo_multi_upload_backgroud_color));
      this.hImageGallery.setClickable(false);
    }
  }

  public void setPoiImageListener(PoiLargeImageView.PoiImageListener paramPoiImageListener)
  {
    this.poiImageListener = paramPoiImageListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PoiImageGallery
 * JD-Core Version:    0.6.0
 */