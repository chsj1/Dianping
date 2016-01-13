package com.dianping.membercard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import com.dianping.base.widget.NovaFragmentTabActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.membercard.fragment.MyCardFragment;
import com.dianping.membercard.fragment.MyPrepaidCardFragment;
import com.dianping.v1.R.layout;

public class MyCardActivity extends NovaFragmentTabActivity
{
  private static final String MEMBERCARD_TAG = "会员卡";
  private static final String STORECARD_TAG = "储值卡";

  private Fragment getCurrentFragment()
  {
    return getSupportFragmentManager().findFragmentByTag(this.mTabHost.getCurrentTabTag());
  }

  public String getPageName()
  {
    if ("prepaidcardlist".equals(getIntent().getData().getHost()))
      return "prepaidcardlist";
    return "membercardlist";
  }

  public void hideTabs()
  {
    findViewById(16908307).setVisibility(8);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Fragment localFragment = getCurrentFragment();
    if (((localFragment instanceof MyCardFragment)) || ((localFragment instanceof MyPrepaidCardFragment)))
      localFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ("prepaidcardlist".equals(getIntent().getData().getHost()))
    {
      addTab("储值卡", R.layout.mc_tab_indicator_prepaidcard, MyPrepaidCardFragment.class, null);
      ((MyPrepaidCardFragment)getSupportFragmentManager().findFragmentByTag("储值卡")).prepareToRefresh();
    }
    while (true)
    {
      hideTabs();
      return;
      if (!"membercardlist".equals(getIntent().getData().getHost()))
        continue;
      addTab("会员卡", R.layout.mc_tab_indicator_mycard, MyCardFragment.class, null);
      ((MyCardFragment)getSupportFragmentManager().findFragmentByTag("会员卡")).prepareToRefresh();
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      Fragment localFragment = getCurrentFragment();
      if ((localFragment instanceof MyCardFragment))
      {
        ((MyCardFragment)localFragment).onLeftButtonClick();
        return true;
      }
      if ((localFragment instanceof MyPrepaidCardFragment))
      {
        ((MyPrepaidCardFragment)localFragment).onLeftButtonClick();
        return true;
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.mc_tab_activity);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MyCardActivity
 * JD-Core Version:    0.6.0
 */