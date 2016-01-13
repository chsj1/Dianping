package com.dianping.main.find.pictureplaza;

import android.content.Context;
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
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;

public class SquareHorizontalImageGallery extends HorizontalScrollView
  implements View.OnClickListener
{
  private static final String TAG = "HorizontalSquareImageGallery";
  private LinearLayout content;
  private DPNetworkImageView currentImage;
  private String flingElementId;
  private int imageSpace;
  private int itemLayout;
  private GestureDetectorCompat mGesture;
  private OnSquareGalleryImageClickListener mOnSquareGalleryImageClickListener;
  private int mTotal = 0;
  private int maxShownCount;

  public SquareHorizontalImageGallery(Context paramContext)
  {
    super(paramContext);
  }

  public SquareHorizontalImageGallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOverScrollMode(2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.HorizontalImageGallery);
    this.maxShownCount = paramAttributeSet.getInt(R.styleable.HorizontalImageGallery_maxSize, 7);
    this.imageSpace = paramAttributeSet.getInt(R.styleable.HorizontalImageGallery_imageSpace, 0);
    this.flingElementId = paramAttributeSet.getString(R.styleable.HorizontalImageGallery_flingElementId);
    this.itemLayout = paramAttributeSet.getResourceId(R.styleable.HorizontalImageGallery_itemLayout, R.layout.square_horizontal_image_gallery_layout);
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
          Log.d("HorizontalSquareImageGallery", "onFling velocityX=" + paramFloat1 + " velocityY=" + paramFloat2);
          GAHelper.instance().contextStatisticsEvent(SquareHorizontalImageGallery.this.getContext(), SquareHorizontalImageGallery.this.flingElementId, null, 0, "slide");
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

  public void addImages(String[] paramArrayOfString, int paramInt)
  {
    this.currentImage = null;
    this.mTotal = paramArrayOfString.length;
    if (this.mTotal > this.maxShownCount)
      this.mTotal = this.maxShownCount;
    int k = this.mTotal;
    int j = 0;
    int i = paramInt;
    paramInt = j;
    while (paramInt <= k - 1)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(this.itemLayout, null, false);
      localNovaRelativeLayout.setTag(Integer.valueOf(paramInt));
      localNovaRelativeLayout.setOnClickListener(this);
      localNovaRelativeLayout.setGAString("smallpic", null, paramInt);
      Object localObject = (DPNetworkImageView)localNovaRelativeLayout.findViewById(R.id.image_gallery_pic);
      ((DPNetworkImageView)localObject).setImage(paramArrayOfString[paramInt]);
      j = i;
      if (i < 0)
        j = 0;
      if (paramInt == j)
      {
        ((DPNetworkImageView)localObject).setBackgroundResource(R.drawable.orange_red_frame_bg);
        this.currentImage = ((DPNetworkImageView)localObject);
      }
      localObject = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject).setMargins(0, 0, ViewUtils.dip2px(getContext(), this.imageSpace), 0);
      this.content.addView(localNovaRelativeLayout, (ViewGroup.LayoutParams)localObject);
      paramInt += 1;
      i = j;
    }
    if (i > 2)
      postDelayed(new Runnable(i)
      {
        public void run()
        {
          SquareHorizontalImageGallery.this.scrollTo(this.val$curIndex * ViewUtils.dip2px(SquareHorizontalImageGallery.this.getContext(), 73.0F), 0);
        }
      }
      , 100L);
  }

  public void addImages(String[] paramArrayOfString, boolean paramBoolean)
  {
    addImages(paramArrayOfString, 0);
  }

  public void onClick(View paramView)
  {
    if (this.mOnSquareGalleryImageClickListener != null)
    {
      if (this.currentImage != null)
        this.currentImage.setBackgroundResource(0);
      DPNetworkImageView localDPNetworkImageView = (DPNetworkImageView)paramView.findViewById(R.id.image_gallery_pic);
      localDPNetworkImageView.setBackgroundResource(R.drawable.orange_red_frame_bg);
      this.currentImage = localDPNetworkImageView;
      this.mOnSquareGalleryImageClickListener.onGalleryImageClick(((Integer)paramView.getTag()).intValue(), this.mTotal, localDPNetworkImageView.getDrawable());
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

  public void setmOnSquareGalleryImageClickListener(OnSquareGalleryImageClickListener paramOnSquareGalleryImageClickListener)
  {
    this.mOnSquareGalleryImageClickListener = paramOnSquareGalleryImageClickListener;
  }

  public static abstract interface OnSquareGalleryImageClickListener
  {
    public abstract void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.SquareHorizontalImageGallery
 * JD-Core Version:    0.6.0
 */