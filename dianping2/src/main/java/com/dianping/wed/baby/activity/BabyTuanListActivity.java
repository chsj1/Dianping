package com.dianping.wed.baby.activity;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.wed.baby.fragment.BabyTuanListFragment;

public class BabyTuanListActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    setTitle("本店团购");
    return new BabyTuanListFragment();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyTuanListActivity
 * JD-Core Version:    0.6.0
 */