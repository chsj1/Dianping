package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import android.widget.TabHost;
import com.dianping.base.basic.FragmentTabsPagerActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class StarUserActivity extends FragmentTabsPagerActivity
{
  public static final int STAR_USER_TYPE_DAILY = 0;
  public static final int STAR_USER_TYPE_WEEKLY = 1;
  StarUserFragment dailyStarUserFragment;
  private int mTopicId;
  StarUserFragment weeklyStarUserFragment;

  public String getPageName()
  {
    return "moments_leaderboard";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mTopicId = getIntParam("topicid", 1);
    getTitleBar().setTitle("话题达人榜");
    setOnFragment();
  }

  public void onPageSelected(int paramInt)
  {
    super.onPageSelected(paramInt);
    if (paramInt == 0)
    {
      GAHelper.instance().contextStatisticsEvent(this, "weekly_ranking", null, "tap");
      return;
    }
    GAHelper.instance().contextStatisticsEvent(this, "daily_ranking", null, "tap");
  }

  public void setOnFragment()
  {
    this.weeklyStarUserFragment = new StarUserFragment();
    this.weeklyStarUserFragment.setData(1, this.mTopicId);
    this.dailyStarUserFragment = new StarUserFragment();
    this.dailyStarUserFragment.setData(0, this.mTopicId);
    addTab("周排行", R.layout.find_plaza_staruser_indicator, this.weeklyStarUserFragment, null);
    addTab("日排行", R.layout.find_plaza_staruser_indicator, this.dailyStarUserFragment, null);
    this.tabPagerFragment.tabHost().setCurrentTab(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.StarUserActivity
 * JD-Core Version:    0.6.0
 */