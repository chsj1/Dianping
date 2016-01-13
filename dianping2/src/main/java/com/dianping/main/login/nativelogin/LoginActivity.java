package com.dianping.main.login.nativelogin;

import android.content.SharedPreferences;
import com.dianping.base.app.loader.AgentFragment;

public class LoginActivity extends BasicLoginActivity
{
  protected AgentFragment getAgentFragment()
  {
    return new LoginAgentFragment();
  }

  public String getPageName()
  {
    if (preferences().getInt("dianping.login.login_mode", 0) == 0)
      return "fastlogin";
    return "login";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.LoginActivity
 * JD-Core Version:    0.6.0
 */