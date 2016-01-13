package com.dianping.pay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.pay.fragment.OrderCenterTabFragment;

public class OrderCenterTabActivity extends BaseTuanActivity
{
  private OrderCenterTabFragment mTabFragment;

  private void showFragments()
  {
    Object localObject1 = getSupportFragmentManager().beginTransaction();
    Object localObject2 = new FrameLayout(this);
    ((FrameLayout)localObject2).setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    ((FrameLayout)localObject2).setId(16908300);
    setContentView((View)localObject2);
    this.mTabFragment = new OrderCenterTabFragment();
    localObject2 = new Bundle();
    ((Bundle)localObject2).putParcelableArrayList("arrOrders", getIntent().getParcelableArrayListExtra("arrOrders"));
    ((Bundle)localObject2).putString("target_tab", getIntent().getData().getQueryParameter("tab"));
    this.mTabFragment.setArguments((Bundle)localObject2);
    ((FragmentTransaction)localObject1).replace(16908300, this.mTabFragment);
    ((FragmentTransaction)localObject1).commitAllowingStateLoss();
    if (getIntent().getData().getHost().startsWith("usercenterorderlist"));
    for (localObject1 = "all"; ; localObject1 = getIntent().getData().getQueryParameter("tab"))
    {
      if (this.mTabFragment != null)
        this.mTabFragment.gotoOrderTab((String)localObject1);
      return;
    }
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("我的订单");
    if (isLogined())
      showFragments();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      showFragments();
    return super.onLogin(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.OrderCenterTabActivity
 * JD-Core Version:    0.6.0
 */