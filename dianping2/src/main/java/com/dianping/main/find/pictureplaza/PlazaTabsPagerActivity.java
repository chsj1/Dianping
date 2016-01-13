package com.dianping.main.find.pictureplaza;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.loader.MyResources;
import com.dianping.loader.MyResources.ResourceOverrideable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PlazaTabsPagerActivity extends NovaActivity
  implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, MyResources.ResourceOverrideable
{
  private AssetManager assetManager;
  private MyResources myResources;
  private Resources resources;
  protected ExtendTabPagerFragment tabPagerFragment;
  private Resources.Theme theme;

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

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setOnContentView();
    this.tabPagerFragment = ((ExtendTabPagerFragment)getSupportFragmentManager().findFragmentById(R.id.viewer));
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
    super.setContentView(R.layout.plaza_tabs_pager_layout);
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaTabsPagerActivity
 * JD-Core Version:    0.6.0
 */