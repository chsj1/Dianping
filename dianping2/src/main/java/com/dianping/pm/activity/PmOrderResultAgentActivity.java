package com.dianping.pm.activity;

import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.pm.fragment.PmOrderResultAgentFragment;

public class PmOrderResultAgentActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new PmOrderResultAgentFragment();
    return this.mFragment;
  }

  public boolean onGoBack()
  {
    return this.mFragment.onGoBack();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.activity.PmOrderResultAgentActivity
 * JD-Core Version:    0.6.0
 */