package com.dianping.wed.baby.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.wed.baby.fragment.WeddingCaseDetaiFragment;

public class WeddingCaseDetailActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    return new WeddingCaseDetaiFragment();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("案例详情");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingCaseDetailActivity
 * JD-Core Version:    0.6.0
 */