package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;

public class PlazaTopicActivity extends AgentActivity
{
  private PlazaTopicFragment mFragment;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new PlazaTopicFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "moments_topic";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.gaExtra.biz_id = String.valueOf(getIntParam("topicid", 1));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaTopicActivity
 * JD-Core Version:    0.6.0
 */