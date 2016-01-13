package com.dianping.base.basic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.BitmapUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public abstract class ScreenSlidePagerActivity extends NovaActivity
  implements ViewPager.OnPageChangeListener
{
  public Bitmap currentBitmap;
  public int currentPage;
  protected PagerAdapter mPagerAdapter;
  public ArrayList<DPObject> pageList;
  protected ViewPager viewPager;

  public int currentPage()
  {
    return this.viewPager.getCurrentItem();
  }

  public int currentPageIndex()
  {
    return this.currentPage;
  }

  public void finish()
  {
    super.finish();
    overridePendingTransition(0, R.anim.activity_exit);
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void notifyDataSetChanged()
  {
    if (this.mPagerAdapter == null)
      this.mPagerAdapter = this.viewPager.getAdapter();
    this.mPagerAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.currentPage = paramBundle.getInt("currentPage");
      this.pageList = paramBundle.getParcelableArrayList("pageList");
      if (this.pageList == null)
        this.pageList = new ArrayList();
      if (this.currentPage <= this.pageList.size() - 1)
        break label222;
    }
    label222: for (int i = this.pageList.size() - 1; ; i = this.currentPage)
    {
      this.currentPage = i;
      this.viewPager = new ViewPager(this)
      {
        public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
        {
          try
          {
            boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
            return bool;
          }
          catch (IllegalArgumentException paramMotionEvent)
          {
            paramMotionEvent.printStackTrace();
          }
          return false;
        }
      };
      this.viewPager.setBackgroundResource(17170444);
      this.viewPager.setId(R.id.pager);
      this.viewPager.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
      super.setContentView(this.viewPager);
      this.viewPager.setOnPageChangeListener(this);
      return;
      if (getIntent() == null)
        break;
      this.pageList = getIntent().getParcelableArrayListExtra("pageList");
      this.currentPage = getIntent().getIntExtra("position", 0);
      paramBundle = getIntent().getByteArrayExtra("currentbitmap");
      if (paramBundle == null)
        break;
      paramBundle = new ByteArrayInputStream(paramBundle);
      this.currentBitmap = BitmapUtils.decodeSampledBitmapFromStream(Bitmap.Config.RGB_565, paramBundle, ViewUtils.getScreenWidthPixels(this), ViewUtils.getScreenHeightPixels(this));
      break;
    }
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.viewPager.setAdapter(pagerAdapter());
    if (this.pageList.size() > 0)
      this.viewPager.setCurrentItem(this.currentPage, true);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("currentPage", this.currentPage);
    paramBundle.putParcelableArrayList("pageList", this.pageList);
  }

  public ArrayList<DPObject> pageList()
  {
    return this.pageList;
  }

  protected PagerAdapter pagerAdapter()
  {
    this.mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this.currentPage, this.pageList, this.currentBitmap, ScreenSlidePageFragment.class, "screenSlide");
    return this.mPagerAdapter;
  }

  public void setBackgroundResource(int paramInt)
  {
    this.viewPager.setBackgroundResource(paramInt);
  }

  public ViewPager viewPager()
  {
    return this.viewPager;
  }

  @TargetApi(11)
  static class ZoomOutPageTransformer
    implements ViewPager.PageTransformer
  {
    private static float MIN_ALPHA;
    private static float MIN_SCALE = 0.85F;

    static
    {
      MIN_ALPHA = 0.5F;
    }

    public void transformPage(View paramView, float paramFloat)
    {
      int i = paramView.getWidth();
      int j = paramView.getHeight();
      if (paramFloat < -1.0F)
      {
        paramView.setAlpha(0.0F);
        return;
      }
      if (paramFloat <= 1.0F)
      {
        float f1 = Math.max(MIN_SCALE, 1.0F - Math.abs(paramFloat));
        float f2 = j * (1.0F - f1) / 2.0F;
        float f3 = i * (1.0F - f1) / 2.0F;
        if (paramFloat < 0.0F)
          paramView.setTranslationX(f3 - f2 / 2.0F);
        while (true)
        {
          paramView.setScaleX(f1);
          paramView.setScaleY(f1);
          paramView.setAlpha(MIN_ALPHA + (f1 - MIN_SCALE) / (1.0F - MIN_SCALE) * (1.0F - MIN_ALPHA));
          return;
          paramView.setTranslationX(-f3 + f2 / 2.0F);
        }
      }
      paramView.setAlpha(0.0F);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ScreenSlidePagerActivity
 * JD-Core Version:    0.6.0
 */