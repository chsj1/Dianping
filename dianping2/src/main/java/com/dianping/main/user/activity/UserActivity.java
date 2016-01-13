package com.dianping.main.user.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.main.user.UserProfileFragment;

public class UserActivity extends AgentActivity
{
  private UserProfileFragment userProfileFragment;

  protected AgentFragment getAgentFragment()
  {
    this.userProfileFragment = new UserProfileFragment();
    return this.userProfileFragment;
  }

  public String getPageName()
  {
    return "moments_profile";
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
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.UserActivity
 * JD-Core Version:    0.6.0
 */