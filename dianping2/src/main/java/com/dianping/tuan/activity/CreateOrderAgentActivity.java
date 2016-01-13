package com.dianping.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.model.UserProfile;
import com.dianping.tuan.fragment.CreateOrderAgentFragment;
import com.dianping.widget.view.GAUserInfo;

public class CreateOrderAgentActivity extends TuanAgentActivity
{
  protected DPObject dpDeal;

  private void resolveArguments()
  {
    this.dpDeal = ((DPObject)getIntent().getParcelableExtra("deal"));
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new CreateOrderAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "createtuanorder";
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (this.mFragment != null)
      ((CreateOrderAgentFragment)this.mFragment).onAccountSwitched(paramUserProfile);
  }

  public void onCreate(Bundle paramBundle)
  {
    resolveArguments();
    super.onCreate(paramBundle);
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    if (this.mFragment != null)
    {
      super.onCreateTitleBar(paramTitleBar);
      ((CreateOrderAgentFragment)this.mFragment).onCreateTitleBar(paramTitleBar);
    }
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (this.mFragment != null)
      this.mFragment.onLogin(paramBoolean);
    return super.onLogin(paramBoolean);
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    if (this.dpDeal != null)
      localGAUserInfo.dealgroup_id = Integer.valueOf(this.dpDeal.getInt("ID"));
    super.onNewGAPager(localGAUserInfo);
  }

  protected void onUpdateAccount()
  {
    onAccountSwitched(getAccount());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.CreateOrderAgentActivity
 * JD-Core Version:    0.6.0
 */