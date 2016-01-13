package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public class HorizontalImageGallery extends HorizontalScrollView
  implements View.OnClickListener
{
  private static final String TAG = "HorizontalImageGallery";
  private LinearLayout content;
  private String elementName;
  private String flingElementId;
  private int imageSpace;
  private int itemLayout;
  private View lastFocusItem = null;
  private GestureDetectorCompat mGesture;
  private OnGalleryImageClickListener mOnGalleryImageClickListener;
  private int mTotal = 0;
  private int maxShownCount;

  public HorizontalImageGallery(Context paramContext)
  {
    super(paramContext);
  }

  public HorizontalImageGallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOverScrollMode(2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.HorizontalImageGallery);
    this.maxShownCount = paramAttributeSet.getInt(R.styleable.HorizontalImageGallery_maxSize, 7);
    this.imageSpace = paramAttributeSet.getInt(R.styleable.HorizontalImageGallery_imageSpace, 0);
    this.flingElementId = paramAttributeSet.getString(R.styleable.HorizontalImageGallery_flingElementId);
    this.itemLayout = paramAttributeSet.getResourceId(R.styleable.HorizontalImageGallery_itemLayout, R.layout.common_image_gallery_item);
    paramAttributeSet.recycle();
    this.content = new LinearLayout(paramContext);
    this.content.setOrientation(0);
    addView(this.content);
    if (this.flingElementId != null)
      this.mGesture = new GestureDetectorCompat(paramContext, new GestureDetector.OnGestureListener()
      {
        public boolean onDown(MotionEvent paramMotionEvent)
        {
          return false;
        }

        public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
        {
          Log.d("HorizontalImageGallery", "onFling velocityX=" + paramFloat1 + " velocityY=" + paramFloat2);
          GAHelper.instance().contextStatisticsEvent(HorizontalImageGallery.this.getContext(), HorizontalImageGallery.this.flingElementId, null, 0, "slide");
          return false;
        }

        public void onLongPress(MotionEvent paramMotionEvent)
        {
        }

        public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
        {
          return false;
        }

        public void onShowPress(MotionEvent paramMotionEvent)
        {
        }

        public boolean onSingleTapUp(MotionEvent paramMotionEvent)
        {
          return false;
        }
      });
  }

  public void addImages(String[] paramArrayOfString, boolean paramBoolean)
  {
    this.mTotal = paramArrayOfString.length;
    if (this.mTotal > this.maxShownCount)
      this.mTotal = this.maxShownCount;
    int j = this.mTotal - 1;
    int i = 0;
    while (i <= j)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(this.itemLayout, null, false);
      localNovaRelativeLayout.setTag(Integer.valueOf(i));
      localNovaRelativeLayout.setOnClickListener(this);
      localNovaRelativeLayout.setGAString(this.elementName, null, i);
      Object localObject = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.image_gallery_pic);
      ((NetworkImageView)localObject).setImage(paramArrayOfString[i]);
      ((NetworkImageView)localObject).setSelected(false);
      if ((i == j) && (paramBoolean))
      {
        localObject = localNovaRelativeLayout.findViewById(R.id.image_gallery_more);
        if (localObject != null)
          ((View)localObject).setVisibility(0);
      }
      localObject = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject).setMargins(0, 0, ViewUtils.dip2px(getContext(), this.imageSpace), 0);
      this.content.addView(localNovaRelativeLayout, (ViewGroup.LayoutParams)localObject);
      i += 1;
    }
  }

  public void onClick(View paramView)
  {
    if (this.mOnGalleryImageClickListener != null)
    {
      Drawable localDrawable = ((NetworkImageView)paramView.findViewById(R.id.image_gallery_pic)).getDrawable();
      this.mOnGalleryImageClickListener.onGalleryImageClick(((Integer)paramView.getTag()).intValue(), this.mTotal, localDrawable);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    try
    {
      String str = getResources().getResourceName(getId());
      this.elementName = str.substring(str.lastIndexOf("/") + 1);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mGesture != null)
      this.mGesture.onTouchEvent(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }

  public void removeAllImages()
  {
    this.content.removeAllViews();
    this.mTotal = 0;
  }

  public void removeImageByIndex(int paramInt)
  {
    this.content.removeViewAt(paramInt);
    this.mTotal -= 1;
    while (paramInt < this.mTotal)
    {
      this.content.getChildAt(paramInt).setTag(Integer.valueOf(paramInt));
      paramInt += 1;
    }
  }

  public void setElementName(String paramString)
  {
    this.elementName = paramString;
  }

  public void setOnGalleryImageClickListener(OnGalleryImageClickListener paramOnGalleryImageClickListener)
  {
    this.mOnGalleryImageClickListener = paramOnGalleryImageClickListener;
  }

  public void setSelectedImage(int paramInt)
  {
    if (this.lastFocusItem != null)
    {
      this.lastFocusItem.findViewById(R.id.image_gallery_pic).setSelected(false);
      this.lastFocusItem.setSelected(false);
    }
    View localView = this.content.getChildAt(paramInt);
    if (localView != null)
    {
      localView.findViewById(R.id.image_gallery_pic).setSelected(true);
      this.lastFocusItem = localView;
      localView.setSelected(true);
      int[] arrayOfInt1 = new int[2];
      localView.getLocationOnScreen(arrayOfInt1);
      int[] arrayOfInt2 = new int[2];
      getLocationOnScreen(arrayOfInt2);
      if ((paramInt != 0) && (arrayOfInt1[0] != 0) && (arrayOfInt2[0] != 0) && (arrayOfInt1[0] + localView.getWidth() > arrayOfInt2[0] + getWidth()))
      {
        paramInt = localView.getLeft() - getWidth() + localView.getWidth();
        if (paramInt <= 0)
          break label146;
        scrollTo(paramInt, 0);
      }
    }
    return;
    label146: scrollTo(0, 0);
  }

  public static abstract interface OnGalleryImageClickListener
  {
    public abstract void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.HorizontalImageGallery
 * JD-Core Version:    0.6.0
 */