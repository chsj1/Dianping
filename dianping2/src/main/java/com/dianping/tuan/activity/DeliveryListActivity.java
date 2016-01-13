package com.dianping.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.tuan.fragment.DeliveryListFragment;

public class DeliveryListActivity extends BaseTuanActivity
{
  DeliveryListFragment deliveryListFragment;
  DPObject dpDeal;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.deliveryListFragment = DeliveryListFragment.newInstance(this, getIntent().getExtras());
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    if (this.deliveryListFragment != null)
      this.deliveryListFragment.onCreateTitleBar(paramTitleBar);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.DeliveryListActivity
 * JD-Core Version:    0.6.0
 */