package com.dianping.pm.activity;

import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.pm.fragment.PmOrderDetailAgentFragment;

public class PmOrderDetailAgentActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new PmOrderDetailAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "pmorderdetail";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 1);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.activity.PmOrderDetailAgentActivity
 * JD-Core Version:    0.6.0
 */