package com.dianping.search.deallist;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.util.PurchaseResultHelper;
import com.dianping.base.widget.TitleBar;
import com.dianping.search.deallist.fragment.TuanDealListAgentFragment;

public class DealListActivity extends TuanAgentActivity
{
  TuanDealListAgentFragment mFragment;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new TuanDealListAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "deallist";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected boolean needTitleBarShadow()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  protected void onResume()
  {
    super.onResume();
    PurchaseResultHelper.instance().setIntentAfterBuy(getIntent());
  }

  protected void onStart()
  {
    super.onStart();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.DealListActivity
 * JD-Core Version:    0.6.0
 */