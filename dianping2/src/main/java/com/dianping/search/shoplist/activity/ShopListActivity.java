package com.dianping.search.shoplist.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.widget.view.GAUserInfo;

public class ShopListActivity extends AgentActivity
{
  ShopListAgentFragment mShopListAgentFragment;

  protected AgentFragment getAgentFragment()
  {
    this.mShopListAgentFragment = new ShopListAgentFragment();
    return this.mShopListAgentFragment;
  }

  public String getPageName()
  {
    if ((this.mShopListAgentFragment != null) && (this.mShopListAgentFragment.getDataSource() != null) && (this.mShopListAgentFragment.getDataSource().currentTabIndex() == 1))
      return "shoptglist";
    return "shoplist";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    hideTitleBar();
    if (getIntent().getData().getHost().equals("wxshoplist"))
    {
      ((NovaApplication)getApplication()).setStartType(1);
      return;
    }
    if (getIntent().getData().getHost().equals("qqshoplist"))
    {
      ((NovaApplication)getApplication()).setStartType(2);
      return;
    }
    ((NovaApplication)getApplication()).setStartType(0);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((getIntent().getData().getHost().equals("qqshoplist")) || (getIntent().getData().getHost().equals("wxshoplist")))
      ((NovaApplication)getApplication()).setStartType(0);
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
  }

  public void onResume()
  {
    super.onResume();
    if (getIntent().getData().getHost().equals("wxshoplist"))
    {
      ((NovaApplication)getApplication()).setStartType(1);
      return;
    }
    if (getIntent().getData().getHost().equals("qqshoplist"))
    {
      ((NovaApplication)getApplication()).setStartType(2);
      return;
    }
    ((NovaApplication)getApplication()).setStartType(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.activity.ShopListActivity
 * JD-Core Version:    0.6.0
 */