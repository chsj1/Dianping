package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.phototag.PictureTagLayout;
import com.dianping.base.widget.phototag.PictureTagLayout.OnTagAddListener;
import com.dianping.base.widget.phototag.PictureTagLayout.OnTagClickListener;
import com.dianping.base.widget.phototag.PictureTagView.Direction;
import com.dianping.base.widget.phototag.PictureTagView.TagItem;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;

public class PoiLargeImageView extends NovaFrameLayout
{
  private NovaFrameLayout contentView;
  private boolean isCanClick = true;
  private DPNetworkImageView mNImage;
  private int mReviewType = 0;
  private int mWidth = 0;
  private PoiImageListener poiImageListener;
  private PictureTagLayout tagLayout;

  public PoiLargeImageView(Context paramContext)
  {
    super(paramContext);
  }

  public PoiLargeImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private int getTagCropPosition(int paramInt1, int paramInt2, double paramDouble, int paramInt3)
  {
    if (paramInt1 <= paramInt2)
      return 0;
    return (int)(paramInt3 * (paramInt1 * paramDouble - (paramInt1 - paramInt2) / 2) / paramInt2);
  }

  private void removeAllDotMarkView()
  {
    this.tagLayout.removeAllViews();
  }

  public void hideAllDotMarkView()
  {
    this.tagLayout.setVisibility(4);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.contentView = ((NovaFrameLayout)findViewById(16908290));
    this.mNImage = ((DPNetworkImageView)findViewById(16908294));
    this.tagLayout = ((PictureTagLayout)findViewById(R.id.tag_layout));
    this.tagLayout.setCanChangeDirection(false);
    this.tagLayout.setCanMove(false);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }

  public void setCanClick(boolean paramBoolean)
  {
    this.isCanClick = paramBoolean;
  }

  public void setPoiContent(String paramString)
  {
    PictureTagView.TagItem localTagItem = new PictureTagView.TagItem();
    localTagItem.type = 2;
    localTagItem.addressType = (this.mReviewType + 1);
    localTagItem.tagContent = paramString;
    this.tagLayout.addTagItem(localTagItem);
  }

  public void setPoiImageListener(PoiImageListener paramPoiImageListener)
  {
    this.poiImageListener = paramPoiImageListener;
  }

  public void setPoiLargeImageSource(DPObject paramDPObject, int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
  {
    setPoiLargeImageSource(paramDPObject, paramInt1, paramString, paramBoolean, paramInt2, 0);
  }

  public void setPoiLargeImageSource(DPObject paramDPObject, int paramInt1, String paramString, boolean paramBoolean, int paramInt2, int paramInt3)
  {
    if (paramDPObject == null)
      return;
    this.mReviewType = paramInt3;
    this.mNImage.setGAString("mainpic");
    this.mNImage.gaUserInfo.biz_id = paramString;
    this.mNImage.setLoadChangeListener(new OnLoadChangeListener(paramDPObject, paramBoolean, paramInt2, paramString, paramInt1)
    {
      public void onImageLoadFailed()
      {
      }

      public void onImageLoadStart()
      {
        PoiLargeImageView.this.removeAllDotMarkView();
      }

      public void onImageLoadSuccess(Bitmap paramBitmap)
      {
        Object localObject1 = this.val$source.getString("ShopName");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
          PoiLargeImageView.this.setPoiContent((String)localObject1);
        localObject1 = this.val$source.getArray("PlazaPicTags");
        if (localObject1 != null)
        {
          int i = 0;
          if (i < localObject1.length)
          {
            Object localObject2 = localObject1[i];
            PictureTagView.TagItem localTagItem = new PictureTagView.TagItem(localObject2.getInt("TopicId"));
            localTagItem.tagContent = localObject2.getString("Content");
            if (PoiLargeImageView.this.mWidth == 0)
              PoiLargeImageView.access$102(PoiLargeImageView.this, ViewUtils.getScreenWidthPixels(PoiLargeImageView.this.getContext()) - PoiLargeImageView.this.contentView.getPaddingLeft() - PoiLargeImageView.this.contentView.getPaddingRight());
            int k = paramBitmap.getWidth();
            int m = paramBitmap.getHeight();
            int j;
            if (k > m)
            {
              j = PoiLargeImageView.this.getTagCropPosition(k, m, localObject2.getDouble("XPosition"), PoiLargeImageView.this.mWidth);
              label175: localTagItem.x = j;
              if (k < m)
                break label273;
              j = (int)(localObject2.getDouble("YPosition") * PoiLargeImageView.this.mWidth);
              label207: localTagItem.y = j;
              if (!localObject2.getBoolean("IsRight"))
                break label302;
            }
            label273: label302: for (localTagItem.direction = PictureTagView.Direction.Right; ; localTagItem.direction = PictureTagView.Direction.Left)
            {
              PoiLargeImageView.this.tagLayout.addTagItem(localTagItem);
              i += 1;
              break;
              j = (int)(localObject2.getDouble("XPosition") * PoiLargeImageView.this.mWidth);
              break label175;
              j = PoiLargeImageView.this.getTagCropPosition(m, k, localObject2.getDouble("YPosition"), PoiLargeImageView.this.mWidth);
              break label207;
            }
          }
        }
        PoiLargeImageView.this.tagLayout.setTagClickListener(new PictureTagLayout.OnTagClickListener()
        {
          public void onTagClick(PictureTagView.TagItem paramTagItem)
          {
            if (paramTagItem.type == 1)
            {
              paramTagItem = new Intent("android.intent.action.VIEW", Uri.parse("dianping://plazatopic?topicid=" + paramTagItem.ID));
              PoiLargeImageView.this.getContext().startActivity(paramTagItem);
              return;
            }
            PoiLargeImageView.this.tagLayout.setGAString("poi");
            if (PoiLargeImageView.1.this.val$isTopicPage);
            for (PoiLargeImageView.this.tagLayout.gaUserInfo.biz_id = (PoiLargeImageView.1.this.val$topicId + ""); ; PoiLargeImageView.this.tagLayout.gaUserInfo.biz_id = PoiLargeImageView.1.this.val$feedId)
            {
              PoiLargeImageView.this.tagLayout.gaUserInfo.shop_id = Integer.valueOf(PoiLargeImageView.1.this.val$source.getInt("ShopId"));
              GAHelper.instance().statisticsEvent(PoiLargeImageView.this.tagLayout, "tap");
              PoiLargeImageView.this.poiImageListener.onPoiClick(PoiLargeImageView.1.this.val$index);
              return;
            }
          }
        });
        if (PoiLargeImageView.this.isCanClick)
          PoiLargeImageView.this.tagLayout.setTagAddListener(new PictureTagLayout.OnTagAddListener()
          {
            public void onTagAdd(PictureTagView.TagItem paramTagItem)
            {
              PoiLargeImageView.this.poiImageListener.onLargeImageClick(PoiLargeImageView.1.this.val$index, PoiLargeImageView.this.mNImage.getDrawable());
            }
          });
      }
    });
    this.mNImage.setImage(paramDPObject.getString("PicUrl"));
  }

  public void showAllDotMarkView()
  {
    this.tagLayout.setVisibility(0);
  }

  public static abstract interface PoiImageListener
  {
    public abstract void onLargeImageClick(int paramInt, Drawable paramDrawable);

    public abstract void onPoiClick(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PoiLargeImageView
 * JD-Core Version:    0.6.0
 */