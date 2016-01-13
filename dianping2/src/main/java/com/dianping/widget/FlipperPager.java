package com.dianping.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FlipperPager extends FrameLayout
  implements ViewPager.OnPageChangeListener
{
  private static final int DELEAYED_TIME = 5000;
  public static final Object EMPTY;
  public static final Object ERROR;
  public static final Object HEAD;
  public static final Object LOADING = new Object();
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      FlipperPager.this.moveToNext(true);
      sendEmptyMessageDelayed(1, 5000L);
    }
  };
  private boolean isAutoFlip;
  private boolean mEnableNavigation;
  private boolean mEnablePageIndex;
  private boolean mEnableTitle;
  private ViewPager.OnPageChangeListener mOnPageChangeListener;
  private ViewPager mPager;
  private PagerAdapter mPagerAdapter;
  private CharSequence mTitle;
  private NavigationDot navigationDot;
  private int pageIndexGravity;
  private TextView pageIndexView;
  private TextView titleView;

  static
  {
    ERROR = new Object();
    HEAD = new Object();
    EMPTY = new Object();
  }

  public FlipperPager(Context paramContext)
  {
    this(paramContext, null);
  }

  public FlipperPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (this.mPager == null)
    {
      this.mPager = new ViewPager(paramContext, paramAttributeSet);
      this.mPager.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 17));
      this.mPager.setClickable(true);
      this.mPager.setId(R.id.flipper_pager);
      addView(this.mPager);
    }
    this.mPager.setOffscreenPageLimit(2);
  }

  private void updatePageIndexView()
  {
    int i = this.mPagerAdapter.getCount();
    int j = this.mPager.getCurrentItem() + 1;
    if (j <= i)
      this.pageIndexView.setText(j + "/" + i);
  }

  public void enableNavigationDotMode(boolean paramBoolean)
  {
    enableNavigationDotMode(paramBoolean, 81);
  }

  public void enableNavigationDotMode(boolean paramBoolean, int paramInt)
  {
    if ((this.mPagerAdapter != null) && (this.mPagerAdapter.getCount() < 2));
    do
    {
      do
      {
        return;
        this.mEnableNavigation = paramBoolean;
      }
      while (!this.mEnableNavigation);
      if (this.navigationDot != null)
        continue;
      this.navigationDot = new NavigationDot(getContext(), true);
      this.navigationDot.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, paramInt));
      this.navigationDot.setPadding(0, 0, 5, 5);
      addView(this.navigationDot);
    }
    while (this.mPagerAdapter == null);
    this.navigationDot.setTotalDot(this.mPagerAdapter.getCount());
  }

  public void enablePageIndexMode(boolean paramBoolean)
  {
    enablePageIndexMode(paramBoolean, 85);
  }

  public void enablePageIndexMode(boolean paramBoolean, int paramInt)
  {
    this.pageIndexGravity = paramInt;
    this.mEnablePageIndex = paramBoolean;
    if (paramBoolean)
    {
      if (this.pageIndexView == null)
      {
        this.pageIndexView = new TextView(getContext());
        this.pageIndexView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, paramInt));
        this.pageIndexView.setTypeface(Typeface.SERIF, 1);
        this.pageIndexView.setTextAppearance(getContext(), 16973894);
        this.pageIndexView.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_very_small));
        this.pageIndexView.setGravity(17);
        this.pageIndexView.setTextColor(-7829368);
        this.pageIndexView.setPadding(0, 0, 8, 8);
        this.pageIndexView.setSingleLine();
        addView(this.pageIndexView);
      }
      if (this.mPagerAdapter != null)
        updatePageIndexView();
    }
  }

  public int getCurrentItem()
  {
    return this.mPager.getCurrentItem();
  }

  public void hasTitle(boolean paramBoolean)
  {
    this.mEnableTitle = paramBoolean;
    if (this.mEnableTitle)
    {
      if (this.titleView == null)
      {
        this.titleView = new TextView(getContext());
        this.titleView.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.fliper_title_height_size), 80));
        this.titleView.setTypeface(Typeface.SERIF, 1);
        this.titleView.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_small));
        this.titleView.setPadding(3, 15, 3, 0);
        this.titleView.setEllipsize(TextUtils.TruncateAt.END);
        this.titleView.setGravity(49);
        this.titleView.setTextColor(-1);
        this.titleView.setSingleLine();
        this.titleView.setBackgroundColor(Color.argb(145, 0, 0, 0));
        addView(this.titleView);
      }
      if (this.mPagerAdapter != null)
      {
        this.mTitle = this.mPagerAdapter.getPageTitle(this.mPager.getCurrentItem());
        this.titleView.setText(this.mTitle);
      }
      if ((this.mPagerAdapter == null) || (this.mPagerAdapter.getCount() == 0))
        this.titleView.setVisibility(8);
    }
    else
    {
      return;
    }
    this.titleView.setVisibility(0);
  }

  public void moveToNext(boolean paramBoolean)
  {
    int i = this.mPager.getCurrentItem();
    if ((this.isAutoFlip) && (i == this.mPagerAdapter.getCount() - 1))
    {
      setCurrentItem(0, false);
      return;
    }
    setCurrentItem(i + 1, paramBoolean);
  }

  public void moveToPre(boolean paramBoolean)
  {
    setCurrentItem(this.mPager.getCurrentItem() - 1, paramBoolean);
  }

  public void notifyDataSetChanged()
  {
    if (this.mPagerAdapter != null)
      this.mPagerAdapter.notifyDataSetChanged();
    hasTitle(this.mEnableTitle);
    enableNavigationDotMode(this.mEnableNavigation);
    enablePageIndexMode(this.mEnablePageIndex, this.pageIndexGravity);
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    if (this.mEnableTitle)
    {
      if (paramFloat >= 0.5D)
        break label59;
      this.mTitle = this.mPagerAdapter.getPageTitle(paramInt1);
      this.titleView.setText(this.mTitle);
    }
    label59: 
    do
      return;
    while (paramFloat <= 0.5D);
    this.mTitle = this.mPagerAdapter.getPageTitle(paramInt1 + 1);
    this.titleView.setText(this.mTitle);
  }

  public void onPageSelected(int paramInt)
  {
    if (this.isAutoFlip)
    {
      this.handler.removeMessages(1);
      this.handler.sendEmptyMessageDelayed(1, 5000L);
    }
    if (this.mOnPageChangeListener != null)
      this.mOnPageChangeListener.onPageSelected(paramInt);
    if (this.mEnableNavigation)
      this.navigationDot.moveToPosition(paramInt);
    if (this.mEnablePageIndex)
      updatePageIndexView();
    if (this.mEnableTitle)
    {
      this.mTitle = this.mPagerAdapter.getPageTitle(paramInt);
      this.titleView.setText(this.mTitle);
    }
  }

  public <T extends FlipperFragmentPagerAdapter> void setAdapter(T paramT)
  {
    this.mPagerAdapter = paramT;
    this.mPager.setAdapter(this.mPagerAdapter);
    this.mPager.setOnPageChangeListener(this);
    hasTitle(this.mEnableTitle);
    enableNavigationDotMode(this.mEnableNavigation);
  }

  public <T extends FlipperPagerAdapter> void setAdapter(T paramT)
  {
    this.mPagerAdapter = paramT;
    this.mPager.setAdapter(this.mPagerAdapter);
    this.mPager.setOnPageChangeListener(this);
    hasTitle(this.mEnableTitle);
    enableNavigationDotMode(this.mEnableNavigation);
  }

  public void setCurrentItem(int paramInt)
  {
    setCurrentItem(paramInt, false);
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    this.mPager.setCurrentItem(paramInt, paramBoolean);
  }

  public void setDotNormalId(int paramInt)
  {
    if (this.navigationDot == null)
      return;
    this.navigationDot.setDotNormalId(paramInt);
  }

  public void setDotPressedId(int paramInt)
  {
    if (this.navigationDot == null)
      return;
    this.navigationDot.setDotPressedId(paramInt);
  }

  public void setFlipperPageMargin(int paramInt)
  {
    this.mPager.setPageMargin(paramInt);
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }

  public void startAutoFlip()
  {
    this.isAutoFlip = true;
    this.handler.sendEmptyMessageDelayed(1, 5000L);
  }

  public void stopAutoFlip()
  {
    this.isAutoFlip = false;
    this.handler.removeMessages(1);
  }

  public ViewPager viewPager()
  {
    return this.mPager;
  }

  public static abstract class FlipperFragmentPagerAdapter extends FragmentStatePagerAdapter
  {
    public FlipperFragmentPagerAdapter(FragmentManager paramFragmentManager)
    {
      super();
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView((View)paramObject);
    }

    public int getCount()
    {
      return getFragmentCount();
    }

    public abstract int getFragmentCount();

    public abstract Fragment getFragmentItem(int paramInt);

    public Fragment getItem(int paramInt)
    {
      return getFragmentItem(paramInt);
    }

    public int getItemPosition(Object paramObject)
    {
      return -2;
    }

    protected Fragment getLoadingFragment(String paramString)
    {
      return FlipperPager.LoadingFragment.newInstance(paramString);
    }
  }

  public static abstract class FlipperPagerAdapter extends PagerAdapter
  {
    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView((View)paramObject);
    }

    protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry, ViewGroup paramViewGroup)
    {
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.error_item, paramViewGroup, false);
      paramViewGroup.setTag(FlipperPager.ERROR);
      ((TextView)paramViewGroup.findViewById(16908308)).setText(paramString);
      if (!(paramViewGroup instanceof LoadingErrorView))
        return null;
      ((LoadingErrorView)paramViewGroup).setCallBack(paramLoadRetry);
      return paramViewGroup;
    }

    public int getItemPosition(Object paramObject)
    {
      return -2;
    }

    protected View getLoadingView(ViewGroup paramViewGroup)
    {
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.loading_item, paramViewGroup, false);
      paramViewGroup.setTag(FlipperPager.LOADING);
      return paramViewGroup;
    }
  }

  public static class LoadingFragment extends Fragment
  {
    private String loadingText;

    static LoadingFragment newInstance(String paramString)
    {
      LoadingFragment localLoadingFragment = new LoadingFragment();
      Bundle localBundle = new Bundle();
      localBundle.putString("loading_text", paramString);
      localLoadingFragment.setArguments(localBundle);
      return localLoadingFragment;
    }

    public void onCreate(Bundle paramBundle)
    {
      super.onCreate(paramBundle);
      if (paramBundle != null)
      {
        this.loadingText = paramBundle.getString("loading_text");
        return;
      }
      if (getArguments() != null);
      for (paramBundle = getArguments().getString("loading_text"); ; paramBundle = "")
      {
        this.loadingText = paramBundle;
        return;
      }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
      paramLayoutInflater = paramLayoutInflater.inflate(R.layout.loading_item, paramViewGroup, false);
      paramViewGroup = (TextView)paramLayoutInflater.findViewById(16908308);
      if (!TextUtils.isEmpty(this.loadingText))
        paramViewGroup.setText(this.loadingText);
      return paramLayoutInflater;
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      super.onSaveInstanceState(paramBundle);
      paramBundle.putString("loading_text", this.loadingText);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.FlipperPager
 * JD-Core Version:    0.6.0
 */