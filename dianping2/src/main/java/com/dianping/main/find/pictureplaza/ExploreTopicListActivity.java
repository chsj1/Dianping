package com.dianping.main.find.pictureplaza;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;

public class ExploreTopicListActivity extends AgentActivity
{
  private ExploreTopicListFragment exploreTopicListFragment;

  protected AgentFragment getAgentFragment()
  {
    if (this.exploreTopicListFragment == null)
      this.exploreTopicListFragment = new ExploreTopicListFragment();
    return this.exploreTopicListFragment;
  }

  public String getPageName()
  {
    return "moments_explore_tag";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreTopicListActivity
 * JD-Core Version:    0.6.0
 */