package com.dianping.main.user.agent.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.main.user.UserFragment;

public class UserAgent extends CellAgent
  implements View.OnClickListener, LoginResultListener
{
  private static View mLoginBackView;
  private boolean mIsRefreshing = false;
  private UserFragment mUserFragment;

  public UserAgent(Object paramObject)
  {
    super(paramObject);
    if ((this.fragment instanceof UserFragment))
    {
      this.mUserFragment = ((UserFragment)this.fragment);
      return;
    }
    throw new RuntimeException();
  }

  private View getLoginBackView()
  {
    return mLoginBackView;
  }

  private void gotoLogin(View paramView)
  {
    gotoLogin();
    mLoginBackView = paramView;
  }

  private void setLoginBackView(View paramView)
  {
    mLoginBackView = paramView;
  }

  protected DPObject getProfile()
  {
    return accountService().profile();
  }

  public void gotoLogin()
  {
    accountService().login(this);
  }

  protected boolean isLogin()
  {
    return accountService().token() != null;
  }

  protected boolean needGotoLogin(View paramView)
  {
    if (isLogin())
      return false;
    gotoLogin(paramView);
    return true;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
  }

  public void onClick(View paramView)
  {
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
    setLoginBackView(null);
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (getLoginBackView() != null)
      onClick(getLoginBackView());
    setLoginBackView(null);
  }

  public void onRefresh()
  {
  }

  protected void onRefreshRequestFinish()
  {
    if (this.mIsRefreshing)
    {
      this.mIsRefreshing = false;
      this.mUserFragment.onAgentRequestFinish();
    }
  }

  protected void onRefreshRequestStart()
  {
    if (!this.mIsRefreshing)
    {
      this.mIsRefreshing = true;
      this.mUserFragment.onAgentRequestStart();
    }
  }

  public String token()
  {
    if (super.token() == null)
      return "";
    return super.token();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.app.UserAgent
 * JD-Core Version:    0.6.0
 */