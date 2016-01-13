package com.dianping.base.basic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FragmentTabsPagerActivity extends NovaActivity
  implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
  protected TabPagerFragment tabPagerFragment;

  public void addTab(String paramString, int paramInt, Fragment paramFragment, Bundle paramBundle)
  {
    this.tabPagerFragment.addTab(paramString, paramInt, paramFragment, paramBundle);
  }

  public void addTab(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle)
  {
    if (paramString == null)
      throw new IllegalArgumentException("title cann't be null!");
    this.tabPagerFragment.addTab(paramString, paramInt, paramClass, paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setOnContentView();
    this.tabPagerFragment = ((TabPagerFragment)getSupportFragmentManager().findFragmentById(R.id.viewer));
    this.tabPagerFragment.setOnTabChangeListener(this);
    this.tabPagerFragment.setOnPageChangeListener(this);
    if (paramBundle != null)
      this.tabPagerFragment.tabHost().setCurrentTabByTag(paramBundle.getString("tab"));
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("tab", this.tabPagerFragment.tabHost().getCurrentTabTag());
  }

  public void onTabChanged(String paramString)
  {
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.tab_pager_fragment);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.FragmentTabsPagerActivity
 * JD-Core Version:    0.6.0
 */