package com.dianping.shopinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.widget.TitleBar;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class ShopInfoActivity extends AgentActivity
{
  ShopInfoFragment contentFragment;
  private SpeedMonitorHelper speedMonitorHelper;

  public static void speedTest(Context paramContext, int paramInt)
  {
    if ((paramContext instanceof ShopInfoActivity))
      ((ShopInfoActivity)paramContext).getSpeedMonitorHelper().setResponseTime(paramInt, System.currentTimeMillis());
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.contentFragment == null)
      this.contentFragment = new ShopInfoFragment();
    return this.contentFragment;
  }

  public SpeedMonitorHelper getSpeedMonitorHelper()
  {
    return this.speedMonitorHelper;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    String str = "";
    if (getIntent().getData() != null)
      str = getIntent().getData().getHost();
    this.speedMonitorHelper = new SpeedMonitorHelper(str);
    super.onCreate(paramBundle);
  }

  protected void onDestroy()
  {
    this.speedMonitorHelper.sendReport();
    super.onDestroy();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.activity.ShopInfoActivity
 * JD-Core Version:    0.6.0
 */