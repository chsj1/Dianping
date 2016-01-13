package com.dianping.tuan.activity;

import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.tuan.fragment.MallDealListAgentFragment;

public class MallDealListAgentActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new MallDealListAgentFragment();
    return this.mFragment;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.MallDealListAgentActivity
 * JD-Core Version:    0.6.0
 */