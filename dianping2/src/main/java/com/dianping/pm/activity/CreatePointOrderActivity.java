package com.dianping.pm.activity;

import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.pm.fragment.PmCreateOrderAgentFragment;

public class CreatePointOrderActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new PmCreateOrderAgentFragment();
    return this.mFragment;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.activity.CreatePointOrderActivity
 * JD-Core Version:    0.6.0
 */