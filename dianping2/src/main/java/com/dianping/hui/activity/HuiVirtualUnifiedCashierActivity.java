package com.dianping.hui.activity;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.hui.fragment.HuiVirtualUnifiedCashierFragment;

public class HuiVirtualUnifiedCashierActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new HuiVirtualUnifiedCashierFragment();
    return this.mFragment;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiVirtualUnifiedCashierActivity
 * JD-Core Version:    0.6.0
 */