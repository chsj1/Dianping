package com.dianping.tuan.activity;

import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.tuan.fragment.OrderDetailAgentFragment;

public class OrderDetailAgentActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new OrderDetailAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "orderdetail";
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
 * Qualified Name:     com.dianping.tuan.activity.OrderDetailAgentActivity
 * JD-Core Version:    0.6.0
 */