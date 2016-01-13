package com.dianping.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.tuan.fragment.OrderLogisticsFragment;

public class OrderLogisticsActivity extends BaseTuanActivity
{
  OrderLogisticsFragment mOrderLogisticsFragment;

  public String getPageName()
  {
    return "orderlogistics";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isLogined())
      setupView();
  }

  protected void setupView()
  {
    Object localObject = getIntent();
    if (localObject != null)
    {
      int i = ((Intent)localObject).getIntExtra("OrderID", 0);
      this.mOrderLogisticsFragment = new OrderLogisticsFragment();
      localObject = new Bundle();
      ((Bundle)localObject).putInt("OrderID", i);
      this.mOrderLogisticsFragment.setArguments((Bundle)localObject);
      localObject = getSupportFragmentManager().beginTransaction();
      ((FragmentTransaction)localObject).add(16908290, this.mOrderLogisticsFragment);
      ((FragmentTransaction)localObject).setTransition(4097);
      ((FragmentTransaction)localObject).commit();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.OrderLogisticsActivity
 * JD-Core Version:    0.6.0
 */