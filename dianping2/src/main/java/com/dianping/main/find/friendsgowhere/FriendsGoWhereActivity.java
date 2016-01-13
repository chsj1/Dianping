package com.dianping.main.find.friendsgowhere;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;

public class FriendsGoWhereActivity extends AgentActivity
{
  private void redirectToUser()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://user").buildUpon().appendQueryParameter("userid", accountService().id() + "").build()));
  }

  protected AgentFragment getAgentFragment()
  {
    return new FriendsGoWhereFragment();
  }

  public String getPageName()
  {
    return "friend";
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    if (accountService().id() == 0)
    {
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
          FriendsGoWhereActivity.this.finish();
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          FriendsGoWhereActivity.this.recreate();
        }
      });
      return;
    }
    super.initViewAgentView(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("好友去哪");
    getTitleBar().addRightViewItem("我的", "me", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (FriendsGoWhereActivity.this.accountService().id() != 0)
        {
          FriendsGoWhereActivity.this.redirectToUser();
          return;
        }
        FriendsGoWhereActivity.this.accountService().login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            FriendsGoWhereActivity.this.redirectToUser();
          }
        });
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.friendsgowhere.FriendsGoWhereActivity
 * JD-Core Version:    0.6.0
 */