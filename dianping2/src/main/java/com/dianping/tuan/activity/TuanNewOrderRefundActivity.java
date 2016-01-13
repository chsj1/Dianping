package com.dianping.tuan.activity;

import android.content.Context;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.tuan.fragment.TuanRefundAgentFragment;

public class TuanNewOrderRefundActivity extends TuanAgentActivity
{
  protected Context context;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new TuanRefundAgentFragment();
    return this.mFragment;
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.context = this;
  }

  public boolean onGoBack()
  {
    return getAgentFragment().onGoBack();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (this.mFragment != null)
      this.mFragment.onLogin(paramBoolean);
    return super.onLogin(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanNewOrderRefundActivity
 * JD-Core Version:    0.6.0
 */