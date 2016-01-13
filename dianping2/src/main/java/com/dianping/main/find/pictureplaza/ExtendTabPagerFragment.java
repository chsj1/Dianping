package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import com.dianping.app.LabelIndicatorStrategy;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.NovaFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class ExtendTabPagerFragment extends NovaFragment
  implements TabHost.OnTabChangeListener, View.OnClickListener
{
  private static final int MAX_TAB_COUNT = 5;
  private CustomImageButton leftButton;
  protected TabHost mTabHost;
  protected TabsAdapter mTabsAdapter;
  protected FatherTabViewPager mViewPager;
  ViewPager.OnPageChangeListener onPageChangeListener;
  private TabHost.OnTabChangeListener onTabChangeListener;
  private RightButtonListener rightButtonListener;
  private NovaLinearLayout rightLayout;

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

  public void hideTab()
  {
    if (this.mTabsAdapter != null)
      this.mTabsAdapter.hideTab();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.back)
      getActivity().finish();
    do
      return;
    while ((paramView.getId() != R.id.right_layout) || (this.rightButtonListener == null));
    this.rightButtonListener.clickAction();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.plaza_tabs_pager_fragment, paramViewGroup, false);
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
    this.mViewPager = ((FatherTabViewPager)paramView.findViewById(R.id.pager));
    this.mViewPager.setOffscreenPageLimit(2);
    this.mTabsAdapter = new TabsAdapter(this, this.mTabHost, this.mViewPager);
    this.leftButton = ((CustomImageButton)paramView.findViewById(R.id.back));
    this.rightLayout = ((NovaLinearLayout)paramView.findViewById(R.id.right_layout));
    this.leftButton.setOnClickListener(this);
    this.rightLayout.setOnClickListener(this);
    if (paramBundle != null)
      this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    this.onPageChangeListener = paramOnPageChangeListener;
  }

  public void setOnTabChangeListener(TabHost.OnTabChangeListener paramOnTabChangeListener)
  {
    this.onTabChangeListener = paramOnTabChangeListener;
  }

  public void setRightButtonListener(String paramString, RightButtonListener paramRightButtonListener)
  {
    this.rightButtonListener = paramRightButtonListener;
    if (!TextUtils.isEmpty(paramString))
      this.rightLayout.setGAString(paramString);
  }

  public void setmTabsAdapter(TabsAdapter paramTabsAdapter)
  {
    this.mTabsAdapter = paramTabsAdapter;
  }

  public TabHost tabHost()
  {
    return this.mTabHost;
  }

  public TabsAdapter tabsAdapter()
  {
    return this.mTabsAdapter;
  }

  public ViewPager viewPager()
  {
    return this.mViewPager;
  }

  public static abstract interface RightButtonListener
  {
    public abstract void clickAction();
  }

  public class TabsAdapter extends FragmentPagerAdapter
    implements ViewPager.OnPageChangeListener
  {
    private final FragmentActivity mContext;
    private final ExtendTabPagerFragment mFragment;
    private final TabHost mTabHost;
    private final ArrayList<TabInfo> mTabs = new ArrayList();
    private final FatherTabViewPager mViewPager;

    public TabsAdapter(ExtendTabPagerFragment paramTabHost, TabHost paramFatherTabViewPager, FatherTabViewPager arg4)
    {
      super();
      this.mFragment = paramTabHost;
      this.mContext = paramTabHost.getActivity();
      this.mTabHost = paramFatherTabViewPager;
      Object localObject;
      this.mViewPager = localObject;
      this.mTabHost.setOnTabChangedListener(paramTabHost);
      this.mViewPager.setAdapter(this);
      this.mViewPager.setOnPageChangeListener(this);
    }

    private void resizeExpandIndicatorWidth()
    {
      TabWidget localTabWidget = this.mTabHost.getTabWidget();
      int i = Math.min(localTabWidget.getTabCount(), 5);
      if (i == 0);
      while (true)
      {
        return;
        int j = (ViewUtils.getScreenWidthPixels(this.mContext) - ViewUtils.dip2px(this.mContext, 104.0F)) / i;
        i = 0;
        while (i < localTabWidget.getTabCount())
        {
          View localView = localTabWidget.getChildTabViewAt(i);
          ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
          localLayoutParams.width = j;
          localView.setLayoutParams(localLayoutParams);
          i += 1;
        }
      }
    }

    public void addTab(TabHost.TabSpec paramTabSpec, Fragment paramFragment, Bundle paramBundle)
    {
      paramTabSpec.setContent(new DummyTabFactory(this.mContext));
      paramFragment = new TabInfo(paramTabSpec.getTag(), paramFragment, paramBundle);
      this.mTabs.add(paramFragment);
      this.mTabHost.addTab(paramTabSpec);
      notifyDataSetChanged();
    }

    public void addTab(TabHost.TabSpec paramTabSpec, Class<?> paramClass, Bundle paramBundle)
    {
      paramTabSpec.setContent(new DummyTabFactory(this.mContext));
      paramClass = new TabInfo(paramTabSpec.getTag(), paramClass, paramBundle);
      this.mTabs.add(paramClass);
      this.mTabHost.addTab(paramTabSpec);
      if (this.mTabHost.getVisibility() == 8)
        this.mTabHost.setVisibility(0);
      notifyDataSetChanged();
    }

    public int getCount()
    {
      return this.mTabs.size();
    }

    public Fragment getItem(int paramInt)
    {
      TabInfo localTabInfo = (TabInfo)this.mTabs.get(paramInt);
      if (localTabInfo.fragment != null)
        return localTabInfo.fragment;
      return Fragment.instantiate(this.mContext, localTabInfo.clss.getName(), localTabInfo.args);
    }

    public void hideTab()
    {
      if (this.mTabHost != null)
        this.mTabHost.setVisibility(8);
    }

    public void notifyDataSetChanged()
    {
      super.notifyDataSetChanged();
      resizeExpandIndicatorWidth();
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      if (this.mFragment.onPageChangeListener != null)
        this.mFragment.onPageChangeListener.onPageScrollStateChanged(paramInt);
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (this.mFragment.onPageChangeListener != null)
        this.mFragment.onPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    }

    public void onPageSelected(int paramInt)
    {
      TabWidget localTabWidget = this.mTabHost.getTabWidget();
      int i = localTabWidget.getDescendantFocusability();
      localTabWidget.setDescendantFocusability(393216);
      this.mTabHost.setCurrentTab(paramInt);
      localTabWidget.setDescendantFocusability(i);
      if (this.mFragment.onPageChangeListener != null)
        this.mFragment.onPageChangeListener.onPageSelected(paramInt);
    }

    class DummyTabFactory
      implements TabHost.TabContentFactory
    {
      private final Context mContext;

      public DummyTabFactory(Context arg2)
      {
        Object localObject;
        this.mContext = localObject;
      }

      public View createTabContent(String paramString)
      {
        paramString = new View(this.mContext);
        paramString.setMinimumWidth(0);
        paramString.setMinimumHeight(0);
        return paramString;
      }
    }

    class TabInfo
    {
      final Bundle args;
      final Class<?> clss;
      Fragment fragment;
      final String tag;

      TabInfo(String paramFragment, Fragment paramBundle, Bundle arg4)
      {
        this.tag = paramFragment;
        this.fragment = paramBundle;
        Object localObject;
        this.args = localObject;
        this.clss = null;
      }

      TabInfo(Class<?> paramBundle, Bundle arg3)
      {
        this.tag = paramBundle;
        Object localObject1;
        this.clss = localObject1;
        Object localObject2;
        this.args = localObject2;
        this.fragment = null;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExtendTabPagerFragment
 * JD-Core Version:    0.6.0
 */