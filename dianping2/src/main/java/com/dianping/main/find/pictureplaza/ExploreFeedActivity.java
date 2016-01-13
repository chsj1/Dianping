package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;

public class ExploreFeedActivity extends AgentActivity
{
  private ExploreFeedFragment exploreFeedFragment;
  private int feedType;

  protected AgentFragment getAgentFragment()
  {
    if (this.exploreFeedFragment == null)
      this.exploreFeedFragment = new ExploreFeedFragment();
    return this.exploreFeedFragment;
  }

  public String getPageName()
  {
    if (this.feedType == 1)
      return "moments_explore_hot";
    return "moments_explore_new";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.feedType = getIntParam("feedtype");
    TitleBar localTitleBar = getTitleBar();
    if (this.feedType == 1);
    for (paramBundle = "热门"; ; paramBundle = "最新")
    {
      localTitleBar.setTitle(paramBundle);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreFeedActivity
 * JD-Core Version:    0.6.0
 */