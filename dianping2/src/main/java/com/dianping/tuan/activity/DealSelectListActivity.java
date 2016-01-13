package com.dianping.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.tuan.fragment.DealSelectListFragment;

public class DealSelectListActivity extends NovaActivity
{
  DealSelectListFragment dealSelectListFragment;
  DPObject dpDeal;
  DPObject dpExtraData;

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onBackPressed()
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    this.dpDeal = ((DPObject)paramBundle.getParcelableExtra("dpDeal"));
    this.dpExtraData = ((DPObject)paramBundle.getParcelableExtra("extradata"));
    if (this.dpDeal == null)
      finish();
    this.dealSelectListFragment = DealSelectListFragment.newInstance(this, this.dpDeal, this.dpExtraData);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.DealSelectListActivity
 * JD-Core Version:    0.6.0
 */