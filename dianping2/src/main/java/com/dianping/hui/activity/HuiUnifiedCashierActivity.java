package com.dianping.hui.activity;

import android.content.Intent;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.hui.fragment.HuiUnifiedCashierFragment;

public class HuiUnifiedCashierActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new HuiUnifiedCashierFragment();
    return this.mFragment;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    getAgentFragment().onActivityResult(paramInt1, paramInt2, paramIntent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiUnifiedCashierActivity
 * JD-Core Version:    0.6.0
 */