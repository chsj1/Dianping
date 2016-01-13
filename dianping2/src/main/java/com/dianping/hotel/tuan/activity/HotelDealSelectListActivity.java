package com.dianping.hotel.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.hotel.tuan.fragment.HotelDealSelectListFragment;

public class HotelDealSelectListActivity extends NovaActivity
{
  HotelDealSelectListFragment dealSelectListFragment;
  DPObject dpDeal;
  DPObject dpExtraData;
  boolean hasRoom;

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
    this.hasRoom = paramBundle.getBooleanExtra("hasRoom", false);
    if (this.dpDeal == null)
      finish();
    this.dealSelectListFragment = HotelDealSelectListFragment.newInstance(this, this.dpDeal, this.dpExtraData);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelDealSelectListActivity
 * JD-Core Version:    0.6.0
 */