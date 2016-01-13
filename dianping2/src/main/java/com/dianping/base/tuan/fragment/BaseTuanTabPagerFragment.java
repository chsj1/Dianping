package com.dianping.base.tuan.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import com.dianping.app.LabelIndicatorStrategy;
import com.dianping.base.tuan.widget.CustomViewPager;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class BaseTuanTabPagerFragment extends BaseTuanFragment
  implements TabHost.OnTabChangeListener
{
  protected TabHost mTabHost;
  protected BaseTuanTabPagerFragment.TabsAdapter mTabsAdapter;
  protected CustomViewPager mViewPager;
  protected ViewPager.OnPageChangeListener onPageChangeListener;
  protected TabHost.OnTabChangeListener onTabChangeListener;

  public void addTab(int paramInt1, int paramInt2, Fragment paramFragment, Bundle paramBundle)
  {
    addTab(getResources().getString(paramInt1), paramInt2, paramFragment, paramBundle);
  }

  public void addTab(int paramInt1, int paramInt2, Class<?> paramClass, Bundle paramBundle)
  {
    addTab(getString(paramInt1), paramInt2, paramClass, paramBundle);
  }

  public void addTab(String paramString, int paramInt, Fragment paramFragment, Bundle paramBundle)
  {
    if (paramString == null)
      throw new IllegalArgumentException("title cann't be null!");
    this.mTabsAdapter.addTab(this.mTabHost.newTabSpec(paramString).setIndicator(new LabelIndicatorStrategy(getActivity(), paramString, paramInt).createIndicatorView(this.mTabHost)), paramFragment, paramBundle);
  }

  public void addTab(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle)
  {
    if (paramString == null)
      throw new IllegalArgumentException("title cann't be null!");
    this.mTabsAdapter.addTab(this.mTabHost.newTabSpec(paramString).setIndicator(new LabelIndicatorStrategy(getActivity(), paramString, paramInt).createIndicatorView(this.mTabHost)), paramClass, paramBundle);
  }

  public void addTab(String paramString, Fragment paramFragment, Bundle paramBundle)
  {
    addTab(paramString, 0, paramFragment, paramBundle);
  }

  public void addTab(String paramString, Class<?> paramClass, Bundle paramBundle)
  {
    addTab(paramString, 0, paramClass, paramBundle);
  }

  public void clearAllFragment()
  {
    this.mTabsAdapter.clear();
  }

  public boolean getCanScroll()
  {
    return this.mViewPager.getCanScroll();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.tuan_tabs_pager_fragment, paramViewGroup, false);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("tab", this.mTabHost.getCurrentTabTag());
  }

  public void onTabChanged(String paramString)
  {
    int i = this.mTabHost.getCurrentTab();
    this.mTabsAdapter.notifyDataSetChanged();
    this.mViewPager.setCurrentItem(i);
    if (this.onTabChangeListener != null)
      this.onTabChangeListener.onTabChanged(paramString);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.mTabHost = ((TabHost)paramView.findViewById(16908306));
    this.mTabHost.setup();
    this.mTabHost.setOnTabChangedListener(this);
    this.mViewPager = ((CustomViewPager)paramView.findViewById(R.id.pager));
    this.mViewPager.setOffscreenPageLimit(3);
    this.mTabsAdapter = new BaseTuanTabPagerFragment.TabsAdapter(this, this.mTabHost, this.mViewPager);
    if (paramBundle != null)
      this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
  }

  public void setCanScroll(boolean paramBoolean)
  {
    this.mViewPager.setCanScroll(paramBoolean);
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    this.onPageChangeListener = paramOnPageChangeListener;
  }

  public void setOnTabChangeListener(TabHost.OnTabChangeListener paramOnTabChangeListener)
  {
    this.onTabChangeListener = paramOnTabChangeListener;
  }

  public void setmTabsAdapter(BaseTuanTabPagerFragment.TabsAdapter paramTabsAdapter)
  {
    this.mTabsAdapter = paramTabsAdapter;
  }

  public TabHost tabHost()
  {
    return this.mTabHost;
  }

  public BaseTuanTabPagerFragment.TabsAdapter tabsAdapter()
  {
    return this.mTabsAdapter;
  }

  public CustomViewPager viewPager()
  {
    return this.mViewPager;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment
 * JD-Core Version:    0.6.0
 */