package com.dianping.shopinfo.market.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.base.basic.ScreenSlidePagerActivity;
import com.dianping.base.basic.ScreenSlidePagerAdapter;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ShowMarketPosterActivity extends ScreenSlidePagerActivity
{
  private TextView titleText;

  private void setupTitleView()
  {
    FrameLayout localFrameLayout = (FrameLayout)findViewById(16908290);
    View localView = getLayoutInflater().inflate(R.layout.double_line_title_bar_black, localFrameLayout, false);
    this.titleText = ((TextView)localView.findViewById(16908310));
    this.titleText.setText(currentPage() + 1 + "/" + pageList().size());
    localFrameLayout.addView(localView);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupTitleView();
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.titleText.setText(paramInt + 1 + "/" + pageList().size());
  }

  protected PagerAdapter pagerAdapter()
  {
    this.mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this.currentPage, this.pageList, this.currentBitmap, MarketPosterSlidePageFragment.class, "marketSlidePage");
    return this.mPagerAdapter;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.activity.ShowMarketPosterActivity
 * JD-Core Version:    0.6.0
 */