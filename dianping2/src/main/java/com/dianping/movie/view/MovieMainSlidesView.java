package com.dianping.movie.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieMainSlidesView extends NovaLinearLayout
  implements ViewPager.OnPageChangeListener
{
  private View allMovieLayer;
  private Context context;
  private int currentViewPagerPosition = 1;
  private ArrayList<DPObject> dpMovieList = new ArrayList();
  private SparseArray<MovieSlideView> mPagerAdapterItemTags = new SparseArray();
  private int mRealPosition = -1;
  private NavigationDot navigationDot;
  private MovieMainSlidesView.MyPagerAdapter pagerAdapter;
  private ViewPager viewPager;

  public MovieMainSlidesView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieMainSlidesView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.moviemain_slides_view, this, true);
    this.viewPager = ((ViewPager)findViewById(R.id.poster_slide));
    this.viewPager.setOffscreenPageLimit(3);
    this.viewPager.setOnPageChangeListener(this);
    this.pagerAdapter = new MovieMainSlidesView.MyPagerAdapter(this);
    this.viewPager.setAdapter(this.pagerAdapter);
    this.navigationDot = ((NavigationDot)findViewById(R.id.poster_navigation_dots));
    this.navigationDot.setDotNormalId(R.drawable.home_serve_dot);
    this.navigationDot.setDotPressedId(R.drawable.home_serve_dot_pressed);
    this.allMovieLayer = findViewById(R.id.allmovies);
    this.allMovieLayer.setOnClickListener(new MovieMainSlidesView.1(this));
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    if (paramInt == 0)
    {
      if (this.viewPager.getCurrentItem() != 0)
        break label32;
      this.viewPager.setCurrentItem(this.pagerAdapter.getCount() - 2, false);
    }
    label32: 
    do
      return;
    while (this.viewPager.getCurrentItem() != this.pagerAdapter.getCount() - 1);
    this.viewPager.setCurrentItem(1, false);
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    if ((this.pagerAdapter.getCount() == 1) || (getContext() == null))
      return;
    this.navigationDot.setCurrentIndex(paramInt - 1);
    if (paramInt == this.pagerAdapter.getCount() - 1)
    {
      this.mRealPosition = paramInt;
      this.navigationDot.setCurrentIndex(0);
      return;
    }
    if (paramInt == 0)
    {
      this.mRealPosition = paramInt;
      this.navigationDot.setCurrentIndex(this.pagerAdapter.getCount() - 3);
      return;
    }
    this.currentViewPagerPosition = paramInt;
  }

  public void setMovieList(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    this.mPagerAdapterItemTags.clear();
    this.dpMovieList.clear();
    this.dpMovieList.addAll(Arrays.asList(paramArrayOfDPObject));
    this.pagerAdapter.notifyDataSetChanged();
    if (this.pagerAdapter.getCount() == 1)
    {
      this.navigationDot.setVisibility(8);
      return;
    }
    if (this.currentViewPagerPosition > this.pagerAdapter.getCount() - 2)
      this.currentViewPagerPosition = (this.pagerAdapter.getCount() - 2);
    this.navigationDot.setVisibility(0);
    this.navigationDot.setTotalDot(this.pagerAdapter.getCount() - 2);
    this.viewPager.setCurrentItem(this.currentViewPagerPosition);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieMainSlidesView
 * JD-Core Version:    0.6.0
 */