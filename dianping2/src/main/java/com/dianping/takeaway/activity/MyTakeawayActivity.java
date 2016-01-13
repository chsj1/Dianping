package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.takeaway.fragment.MyTakeawayFragment;

public class MyTakeawayActivity extends NovaActivity
{
  private MyTakeawayFragment myTakeawayFragment = null;

  public String getPageName()
  {
    return "mytakeawayorderlist";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (TextUtils.isEmpty(super.accountService().token()))
    {
      super.showShortToast("登录后才能查看订单哦");
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          MyTakeawayActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://mytakeawayorderlist")));
        }
      });
      super.finish();
    }
    do
    {
      return;
      paramBundle = new FrameLayout(this);
      paramBundle.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
      paramBundle.setId(16908300);
      super.setContentView(paramBundle);
    }
    while (getSupportFragmentManager().findFragmentById(16908300) != null);
    this.myTakeawayFragment = new MyTakeawayFragment();
    getSupportFragmentManager().beginTransaction().add(16908300, this.myTakeawayFragment).commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.MyTakeawayActivity
 * JD-Core Version:    0.6.0
 */