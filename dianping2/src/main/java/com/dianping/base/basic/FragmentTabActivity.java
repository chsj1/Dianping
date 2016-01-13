package com.dianping.base.basic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import com.dianping.app.LabelIndicatorStrategy;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.HashMap;
import java.util.UUID;

public class FragmentTabActivity extends NovaActivity
{
  static final String LOG_TAG = FragmentTabActivity.class.getSimpleName();
  protected TabHost mTabHost;
  protected TabManager mTabManager;

  public void addTab(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle)
  {
    if (paramString == null)
      throw new IllegalArgumentException("title cann't be null!");
    View localView = new LabelIndicatorStrategy(this, paramString, paramInt).createIndicatorView(this.mTabHost);
    this.mTabManager.addTab(this.mTabHost.newTabSpec(paramString).setIndicator(localView), paramClass, paramBundle);
    localView.setOnTouchListener(new View.OnTouchListener(paramString)
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if ((paramMotionEvent.getAction() == 1) && (!this.val$title.equals(FragmentTabActivity.this.mTabHost.getCurrentTabTag())))
        {
          FragmentTabActivity.this.setGaPageNameByTitle(this.val$title);
          GAHelper.instance().contextStatisticsEvent(FragmentTabActivity.this, "tab", this.val$title, 2147483647, "tap");
          GAHelper.instance().setGAPageName(FragmentTabActivity.this.getPageName());
          GAHelper.instance().setRequestId(FragmentTabActivity.this, UUID.randomUUID().toString(), null, false);
        }
        return false;
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setOnContentView();
    this.mTabHost = ((TabHost)findViewById(16908306));
    this.mTabHost.setup();
    this.mTabManager = new TabManager(this, this.mTabHost, R.id.realtabcontent);
    setTabWidgetBackground(0);
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("tab", this.mTabHost.getCurrentTabTag());
  }

  public void onTabChanged(String paramString)
  {
  }

  protected void setGaPageNameByTitle(String paramString)
  {
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.fragment_tabs);
  }

  protected void setTabWidgetBackground(int paramInt)
  {
    if (paramInt > 0)
      this.mTabHost.getTabWidget().setBackgroundResource(paramInt);
  }

  public static class TabManager
    implements TabHost.OnTabChangeListener
  {
    private final FragmentTabActivity mActivity;
    private final int mContainerId;
    TabInfo mLastTab;
    private final TabHost mTabHost;
    private final HashMap<String, TabInfo> mTabs = new HashMap();

    public TabManager(FragmentTabActivity paramFragmentTabActivity, TabHost paramTabHost, int paramInt)
    {
      this.mActivity = paramFragmentTabActivity;
      this.mTabHost = paramTabHost;
      this.mContainerId = paramInt;
      this.mTabHost.setOnTabChangedListener(this);
    }

    public void addTab(TabHost.TabSpec paramTabSpec, Class<?> paramClass, Bundle paramBundle)
    {
      paramTabSpec.setContent(new DummyTabFactory(this.mActivity));
      String str = paramTabSpec.getTag();
      paramClass = new TabInfo(str, paramClass, paramBundle);
      paramClass.fragment = this.mActivity.getSupportFragmentManager().findFragmentByTag(str);
      if ((paramClass.fragment != null) && (!paramClass.fragment.isHidden()))
      {
        paramBundle = this.mActivity.getSupportFragmentManager().beginTransaction();
        paramBundle.hide(paramClass.fragment);
        paramBundle.commitAllowingStateLoss();
      }
      this.mTabs.put(str, paramClass);
      this.mTabHost.addTab(paramTabSpec);
    }

    public void onTabChanged(String paramString)
    {
      TabInfo localTabInfo = (TabInfo)this.mTabs.get(paramString);
      FragmentTransaction localFragmentTransaction;
      if (this.mLastTab != localTabInfo)
      {
        localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction();
        if ((this.mLastTab != null) && (this.mLastTab.fragment != null))
          localFragmentTransaction.hide(this.mLastTab.fragment);
        if (localTabInfo == null)
          break label219;
        if (localTabInfo.fragment != null)
          break label177;
        localTabInfo.fragment = Fragment.instantiate(this.mActivity, localTabInfo.clss.getName(), localTabInfo.args);
        localFragmentTransaction.add(this.mContainerId, localTabInfo.fragment, localTabInfo.tag);
        Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", newTab.fragment is null, newTab.tag is " + localTabInfo.tag);
      }
      while (true)
      {
        this.mLastTab = localTabInfo;
        localFragmentTransaction.commitAllowingStateLoss();
        this.mActivity.getSupportFragmentManager().executePendingTransactions();
        this.mActivity.onTabChanged(paramString);
        return;
        label177: localFragmentTransaction.show(localTabInfo.fragment);
        Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", show fragment success");
        continue;
        label219: Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", newTab is null");
      }
    }

    static class DummyTabFactory
      implements TabHost.TabContentFactory
    {
      private final Context mContext;

      public DummyTabFactory(Context paramContext)
      {
        this.mContext = paramContext;
      }

      public View createTabContent(String paramString)
      {
        paramString = new View(this.mContext);
        paramString.setMinimumWidth(0);
        paramString.setMinimumHeight(0);
        return paramString;
      }
    }

    static final class TabInfo
    {
      final Bundle args;
      final Class<?> clss;
      Fragment fragment;
      final String tag;

      TabInfo(String paramString, Class<?> paramClass, Bundle paramBundle)
      {
        this.tag = paramString;
        this.clss = paramClass;
        this.args = paramBundle;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.FragmentTabActivity
 * JD-Core Version:    0.6.0
 */