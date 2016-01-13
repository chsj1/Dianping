package com.dianping.hui.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.hui.fragment.HuiDetailAgentFragment;

public class HuiDetailAgentActivity extends AgentActivity
{
  private AgentFragment mFragment;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new HuiDetailAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "huidetail";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiDetailAgentActivity
 * JD-Core Version:    0.6.0
 */