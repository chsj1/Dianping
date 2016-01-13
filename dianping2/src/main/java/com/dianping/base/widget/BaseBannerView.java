package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import com.dianping.base.app.NovaActivity;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaImageView;
import java.util.ArrayList;
import java.util.List;

public class BaseBannerView extends NovaFrameLayout
  implements ViewPager.OnPageChangeListener
{
  public static final int ANNOUNCELAY_HEAD_ID = R.id.announcelay_head_id;
  private static final int AUTO_FLIP_INTERVAL = 5000;
  public static final int DEFAULT_BANNER_LAYOUT_ID = R.layout.base_banner_view;
  private static final int MSG_AUTO_FLIP = 1001;
  protected int HACK_ITEM_COUNT = 0;
  protected View announcelayHead;
  private int bannerLayoutId = DEFAULT_BANNER_LAYOUT_ID;
  protected ImageView mBtnClose;
  private OnDragListener mDragListener;
  private Handler mHandler;
  protected List<View> mImageViews = new ArrayList();
  long mLastTouchUpTime;
  protected NavigationDot mNaviDot;
  private NovaActivity mNovaActivity;
  private OnPageChangedListener mPageChangedListener;
  protected ViewPager mPager;
  private int pageIndex;

  public BaseBannerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public BaseBannerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BaseBannerView).getResourceId(R.styleable.BaseBannerView_bannnerLayout, 0);
    if (i != 0)
      this.bannerLayoutId = i;
    setVisibility(8);
    this.mHandler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default:
          return;
        case 1001:
        }
        BaseBannerView.this.autoFlip();
        BaseBannerView.this.startAutoFlip();
      }
    };
    paramAttributeSet = GAHelper.instance().getDpActivity(paramContext);
    if ((paramAttributeSet instanceof NovaActivity))
      this.mNovaActivity = ((NovaActivity)paramAttributeSet);
    initView(paramContext);
  }

  void autoFlip()
  {
    if ((this.mNovaActivity == null) || (!this.mNovaActivity.isResumed));
    do
      return;
    while (SystemClock.elapsedRealtime() - this.mLastTouchUpTime < 5000L);
    int j = this.mPager.getCurrentItem() + 1;
    int i = j;
    if (j >= this.mPager.getAdapter().getCount())
      i = 0;
    this.mPager.setCurrentItem(i);
  }

  public ViewPager getViewPager()
  {
    return new MyPager(getContext());
  }

  public void hideCloseButton()
  {
    ViewUtils.hideView(this.mBtnClose, true);
  }

  protected void initView(Context paramContext)
  {
    this.announcelayHead = LayoutInflater.from(paramContext).inflate(this.bannerLayoutId, null, false);
    paramContext = (ViewGroup)this.announcelayHead.findViewById(R.id.banner_pager_layout);
    this.mPager = getViewPager();
    this.mPager.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
    this.mPager.setAdapter(new MyPagerAdapter());
    this.mPager.setOnPageChangeListener(this);
    paramContext.addView(this.mPager);
    this.mBtnClose = ((NovaImageView)this.announcelayHead.findViewById(R.id.close_button));
    this.mNaviDot = ((NavigationDot)this.announcelayHead.findViewById(R.id.naviDot));
    addView(this.announcelayHead);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    startAutoFlip();
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    stopAutoFlip();
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
    int k = this.mImageViews.size();
    if ((this.HACK_ITEM_COUNT == 2) && (k > 1))
    {
      if (paramInt != 0)
        break label136;
      this.pageIndex = (k - this.HACK_ITEM_COUNT);
    }
    while (true)
    {
      int j = (paramInt - 1) % (k - this.HACK_ITEM_COUNT);
      int i = j;
      if (this.HACK_ITEM_COUNT == 2)
      {
        i = j;
        if (j == -1)
        {
          i = j;
          if (k > 2)
            i = k - this.HACK_ITEM_COUNT - 1;
        }
      }
      this.mNaviDot.setCurrentIndex(i);
      if (this.mPageChangedListener != null)
        this.mPageChangedListener.onChanged(i);
      GAHelper.instance().contextStatisticsEvent(getContext(), "basebanner", null, paramInt, "slide");
      return;
      label136: if (paramInt != this.mImageViews.size() - 1)
        continue;
      this.pageIndex = 1;
    }
  }

  public void setBtnOnCloseListener(View.OnClickListener paramOnClickListener)
  {
    if (this.mBtnClose != null)
      this.mBtnClose.setOnClickListener(paramOnClickListener);
  }

  public void setCloseDrawable(int paramInt)
  {
    this.mBtnClose.setImageResource(paramInt);
  }

  public void setNaviDotGravity(int paramInt)
  {
    Context localContext = getContext();
    Object localObject = null;
    switch (paramInt)
    {
    default:
    case 3:
    case 17:
    case 5:
    case 21:
    }
    while (true)
    {
      this.mNaviDot.setLayoutParams((ViewGroup.LayoutParams)localObject);
      return;
      localObject = new FrameLayout.LayoutParams(-2, -2, 83);
      ((FrameLayout.LayoutParams)localObject).setMargins(ViewUtils.dip2px(localContext, 10.0F), 0, 0, ViewUtils.dip2px(localContext, 6.0F));
      continue;
      localObject = new FrameLayout.LayoutParams(-2, -2, 81);
      ((FrameLayout.LayoutParams)localObject).setMargins(0, 0, 0, ViewUtils.dip2px(localContext, 6.0F));
      continue;
      localObject = new FrameLayout.LayoutParams(-2, -2, 85);
      ((FrameLayout.LayoutParams)localObject).setMargins(0, 0, ViewUtils.dip2px(localContext, 10.0F), ViewUtils.dip2px(localContext, 6.0F));
      continue;
      localObject = new FrameLayout.LayoutParams(-2, -2, 21);
      ((FrameLayout.LayoutParams)localObject).setMargins(0, 0, ViewUtils.dip2px(localContext, 10.0F), 0);
    }
  }

  public void setNavigationDotNormalDrawable(int paramInt)
  {
    this.mNaviDot.setDotNormalId(paramInt);
  }

  public void setNavigationDotPressedDrawable(int paramInt)
  {
    this.mNaviDot.setDotPressedId(paramInt);
  }

  public void setOnDragListener(OnDragListener paramOnDragListener)
  {
    this.mDragListener = paramOnDragListener;
  }

  public void setOnPageChangedListener(OnPageChangedListener paramOnPageChangedListener)
  {
    this.mPageChangedListener = paramOnPageChangedListener;
  }

  public void startAutoFlip()
  {
    stopAutoFlip();
    if (this.mImageViews.size() < 2)
      return;
    this.mHandler.sendEmptyMessageDelayed(1001, 5000L);
  }

  public void stopAutoFlip()
  {
    this.mHandler.removeMessages(1001);
  }

  public void updateBannerView(int paramInt, ArrayList<View> paramArrayList)
  {
    updateBannerView(paramInt, paramArrayList, true);
  }

  public void updateBannerView(int paramInt, ArrayList<View> paramArrayList, boolean paramBoolean)
  {
    int j = 0;
    int i;
    if (paramBoolean)
    {
      i = 2;
      this.HACK_ITEM_COUNT = i;
      this.mNaviDot.setTotalDot(paramInt);
      NavigationDot localNavigationDot = this.mNaviDot;
      if (paramInt <= 1)
        break label108;
      paramInt = 0;
      label37: localNavigationDot.setVisibility(paramInt);
      if (paramArrayList != null)
        break label114;
      this.mImageViews.clear();
    }
    while (true)
    {
      this.mPager.getAdapter().notifyDataSetChanged();
      paramArrayList = this.mPager;
      paramInt = j;
      if (paramBoolean)
      {
        paramInt = j;
        if (this.mImageViews.size() > 1)
          paramInt = 1;
      }
      paramArrayList.setCurrentItem(paramInt);
      return;
      i = 0;
      break;
      label108: paramInt = 8;
      break label37;
      label114: this.mImageViews = ((ArrayList)paramArrayList.clone());
    }
  }

  class AdaptiveNetworkImageView extends NetworkImageView
  {
    public AdaptiveNetworkImageView(Context arg2)
    {
      this(localContext, null);
    }

    public AdaptiveNetworkImageView(Context paramAttributeSet, AttributeSet arg3)
    {
      this(paramAttributeSet, localAttributeSet, 0);
    }

    public AdaptiveNetworkImageView(Context paramAttributeSet, AttributeSet paramInt, int arg4)
    {
      super(paramInt, i);
    }

    public void setImageBitmap(Bitmap paramBitmap)
    {
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      int k = ViewUtils.getScreenWidthPixels(getContext());
      Bitmap localBitmap = Bitmap.createScaledBitmap(paramBitmap, k, j * k / i, true);
      if ((localBitmap != paramBitmap) && (!paramBitmap.isRecycled()))
        paramBitmap.recycle();
      paramBitmap = (BaseBannerView.MyPager)BaseBannerView.this.mPager;
      if (localBitmap.getHeight() > ((BaseBannerView.MyPager)BaseBannerView.this.mPager).itemHeight);
      for (i = localBitmap.getHeight(); ; i = ((BaseBannerView.MyPager)BaseBannerView.this.mPager).itemHeight)
      {
        paramBitmap.itemHeight = i;
        super.setImageBitmap(localBitmap);
        BaseBannerView.this.mPager.setLayoutParams(new FrameLayout.LayoutParams(-1, ((BaseBannerView.MyPager)BaseBannerView.this.mPager).itemHeight, 1));
        return;
      }
    }
  }

  public class MyPager extends ViewPager
  {
    int itemHeight = -2147483648;
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
      BaseBannerView.this.mLastTouchUpTime = SystemClock.elapsedRealtime();
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
      return BaseBannerView.this.mImageViews.size();
    }

    public int getItemPosition(Object paramObject)
    {
      return -2;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      if (((View)BaseBannerView.this.mImageViews.get(paramInt)).getParent() == null)
        paramViewGroup.addView((View)BaseBannerView.this.mImageViews.get(paramInt));
      if ((BaseBannerView.this.mImageViews.get(0) instanceof NetworkImageView))
        ((NetworkImageView)BaseBannerView.this.mImageViews.get(0)).setLoadChangeListener(this);
      while (true)
      {
        return BaseBannerView.this.mImageViews.get(paramInt);
        if (!(BaseBannerView.this.mImageViews.get(0) instanceof DPNetworkImageView))
          continue;
        ((DPNetworkImageView)BaseBannerView.this.mImageViews.get(0)).setLoadChangeListener(this);
      }
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
      BaseBannerView.this.setVisibility(0);
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
 * Qualified Name:     com.dianping.base.widget.BaseBannerView
 * JD-Core Version:    0.6.0
 */