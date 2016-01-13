package com.dianping.takeaway.activity;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;

public class TakeawayDeliveryDetailActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    return new TakeawayDeliveryDetailFragment();
  }

  public String getPageName()
  {
    return "takeawayorder";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayDeliveryDetailActivity
 * JD-Core Version:    0.6.0
 */