package com.dianping.selectdish.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.view.NovaFrameLayout;
import java.util.ArrayList;
import java.util.List;

public class SelectDishGuideBannerView extends NovaFrameLayout
  implements ViewPager.OnPageChangeListener
{
  private OnDragListener mDragListener;
  protected List<View> mImageViews = new ArrayList();
  long mLastTouchUpTime;
  protected NavigationDot mNaviDot;
  private OnPageChangedListener mPageChangedListener;
  protected ViewPager mPager;
  private int pageIndex;

  public SelectDishGuideBannerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SelectDishGuideBannerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setVisibility(8);
    initView(paramContext);
  }

  public static Bitmap drawableToBitmap(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if (paramDrawable.getOpacity() != -1);
    for (Object localObject = Bitmap.Config.ARGB_8888; ; localObject = Bitmap.Config.RGB_565)
    {
      localObject = Bitmap.createBitmap(i, j, (Bitmap.Config)localObject);
      Canvas localCanvas = new Canvas((Bitmap)localObject);
      paramDrawable.setBounds(0, 0, i, j);
      paramDrawable.draw(localCanvas);
      return localObject;
    }
  }

  protected void initView(Context paramContext)
  {
    FrameLayout localFrameLayout = new FrameLayout(getContext());
    localFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
    this.mPager = new MyPager(paramContext);
    this.mPager.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 1));
    this.mPager.setAdapter(new MyPagerAdapter());
    this.mPager.setOnPageChangeListener(this);
    localFrameLayout.addView(this.mPager);
    this.mNaviDot = new NavigationDot(getContext(), true);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2, 81);
    localLayoutParams.setMargins(0, 0, 0, ViewUtils.dip2px(paramContext, 90.0F));
    this.mNaviDot.setLayoutParams(localLayoutParams);
    this.mNaviDot.setDotNormalBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.select_dish_guide_navi_empty)));
    this.mNaviDot.setDotPressedBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.select_dish_guide_navi_real)));
    localFrameLayout.addView(this.mNaviDot);
    addView(localFrameLayout);
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    if ((paramInt == 0) && (this.mPager.getCurrentItem() != this.pageIndex))
      this.mPager.setCurrentItem(this.pageIndex, false);
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mDragListener != null)
      this.mDragListener.onDraged();
  }

  public void onPageSelected(int paramInt)
  {
    this.pageIndex = paramInt;
    this.mNaviDot.setCurrentIndex(this.pageIndex);
    if (this.mPageChangedListener != null)
      this.mPageChangedListener.onChanged(this.pageIndex);
  }

  public void setOnDragListener(OnDragListener paramOnDragListener)
  {
    this.mDragListener = paramOnDragListener;
  }

  public void setOnPageChangedListener(OnPageChangedListener paramOnPageChangedListener)
  {
    this.mPageChangedListener = paramOnPageChangedListener;
  }

  public void updateBannerView(int paramInt, ArrayList<View> paramArrayList)
  {
    this.mNaviDot.setTotalDot(paramInt);
    NavigationDot localNavigationDot = this.mNaviDot;
    if (paramInt > 1)
    {
      paramInt = 0;
      localNavigationDot.setVisibility(paramInt);
      if (paramArrayList != null)
        break label63;
      this.mImageViews.clear();
    }
    while (true)
    {
      this.mPager.getAdapter().notifyDataSetChanged();
      this.mPager.setCurrentItem(0);
      return;
      paramInt = 8;
      break;
      label63: this.mImageViews = ((ArrayList)paramArrayList.clone());
    }
  }

  class MyPager extends ViewPager
  {
    private GestureDetector mGestureDetector = new GestureDetector(paramAttributeSet, new MyGestureListener());

    public MyPager(Context arg2)
    {
      this(localContext, null);
    }

    public MyPager(Context paramAttributeSet, AttributeSet arg3)
    {
      super(localAttributeSet);
      setFadingEdgeLength(0);
    }

    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
    {
      SelectDishGuideBannerView.this.mLastTouchUpTime = SystemClock.elapsedRealtime();
      if (this.mGestureDetector.onTouchEvent(paramMotionEvent))
        getParent().requestDisallowInterceptTouchEvent(true);
      return super.dispatchTouchEvent(paramMotionEvent);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
      MyGestureListener()
      {
      }

      public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
      {
        return Math.abs(paramFloat2) < Math.abs(paramFloat1);
      }
    }
  }

  class MyPagerAdapter extends PagerAdapter
    implements OnLoadChangeListener
  {
    MyPagerAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
    }

    public int getCount()
    {
      return SelectDishGuideBannerView.this.mImageViews.size();
    }

    public int getItemPosition(Object paramObject)
    {
      return -2;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      if (((View)SelectDishGuideBannerView.this.mImageViews.get(paramInt)).getParent() == null)
        paramViewGroup.addView((View)SelectDishGuideBannerView.this.mImageViews.get(paramInt));
      if ((SelectDishGuideBannerView.this.mImageViews.get(0) instanceof NetworkImageView))
        ((NetworkImageView)SelectDishGuideBannerView.this.mImageViews.get(0)).setLoadChangeListener(this);
      return SelectDishGuideBannerView.this.mImageViews.get(paramInt);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void onImageLoadFailed()
    {
    }

    public void onImageLoadStart()
    {
    }

    public void onImageLoadSuccess(Bitmap paramBitmap)
    {
      SelectDishGuideBannerView.this.setVisibility(0);
    }
  }

  public static abstract interface OnDragListener
  {
    public abstract void onDraged();
  }

  public static abstract interface OnPageChangedListener
  {
    public abstract void onChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishGuideBannerView
 * JD-Core Version:    0.6.0
 */