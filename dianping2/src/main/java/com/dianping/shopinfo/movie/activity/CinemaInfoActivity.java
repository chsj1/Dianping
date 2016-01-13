package com.dianping.shopinfo.movie.activity;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.shopinfo.movie.fragment.CinemaInfoFragment;

public class CinemaInfoActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new CinemaInfoFragment();
    return this.mFragment;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.activity.CinemaInfoActivity
 * JD-Core Version:    0.6.0
 */