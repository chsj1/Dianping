package com.dianping.main.find.pictureplaza;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.widget.TabHost;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class PlazaHomeActivity extends PlazaTabsPagerActivity
{
  private final int SHOW_FANS = 2;
  private final int SHOW_PLAZA = 0;
  private final int SHOW_SEARCH = 1;
  private String curGAName = "moments_main";

  private void initPlaza()
  {
    this.tabPagerFragment.setRightButtonListener("myprofile", new ExtendTabPagerFragment.RightButtonListener()
    {
      public void clickAction()
      {
        if (PlazaHomeActivity.this.accountService().id() != 0)
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", PlazaHomeActivity.this.accountService().id() + "").build());
          PlazaHomeActivity.this.startActivity(localIntent);
          return;
        }
        PlazaHomeActivity.this.accountService().login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            paramAccountService = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", PlazaHomeActivity.this.accountService().id() + "").build());
            PlazaHomeActivity.this.startActivity(paramAccountService);
          }
        });
      }
    });
    addTab("图趣", R.layout.find_plaza_home_indicator, new PlazaHomeMainFragment(), null);
    addTab("探索", R.layout.find_plaza_home_indicator, new ExploreHomeFragment(), null);
    addTab("关注", R.layout.find_plaza_home_indicator, new PlazaHomeFansFragment(), null);
    this.tabPagerFragment.tabsAdapter().notifyDataSetChanged();
    this.tabPagerFragment.tabHost().setCurrentTab(0);
    GAHelper.instance().contextStatisticsEvent(this, "main_list", null, "tap");
  }

  public String getPageName()
  {
    return this.curGAName;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initPlaza();
  }

  public void onPageSelected(int paramInt)
  {
    switch (paramInt)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      onNewGAPager(new GAUserInfo());
      return;
      this.curGAName = "moments_main";
      GAHelper.instance().contextStatisticsEvent(this, "main_list", null, "tap");
      continue;
      this.curGAName = "moments_explore";
      GAHelper.instance().contextStatisticsEvent(this, "explore", null, "tap");
      continue;
      this.curGAName = "moments_mytopic";
      GAHelper.instance().contextStatisticsEvent(this, "mytopic_list", null, "tap");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeActivity
 * JD-Core Version:    0.6.0
 */