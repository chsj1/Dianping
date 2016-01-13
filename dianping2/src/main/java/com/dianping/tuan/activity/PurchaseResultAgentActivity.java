package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.tuan.fragment.PurchaseResultAgentFragment;

public class PurchaseResultAgentActivity extends TuanAgentActivity
{
  protected Context context;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new PurchaseResultAgentFragment();
    return this.mFragment;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    getAgentFragment().onActivityResult(paramInt1, paramInt2, paramIntent);
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.PurchaseResultAgentActivity
 * JD-Core Version:    0.6.0
 */