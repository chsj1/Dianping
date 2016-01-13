package com.dianping.wed.baby.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.util.TextUtils;
import com.dianping.wed.baby.fragment.WeddingBookingFragment;

public class WeddingBookingActivity extends AgentActivity
{
  String buttontext;
  int productid;
  int shopId;
  String shopName;

  private boolean isEmpty(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return true;
    while ("null".equals(paramString));
    return false;
  }

  protected AgentFragment getAgentFragment()
  {
    return new WeddingBookingFragment();
  }

  @TargetApi(11)
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getIntent().getData() != null)
    {
      this.shopId = getIntParam("shopid");
      this.shopName = getStringParam("shopname");
      this.productid = getIntParam("productid");
      this.buttontext = getStringParam("bookingBtnText");
    }
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getInt("shopid");
      this.shopName = paramBundle.getString("shopname");
      this.productid = paramBundle.getInt("productid");
      this.buttontext = paramBundle.getString("bookingBtnText");
    }
    if (this.mFragment != null)
    {
      paramBundle = new Bundle();
      paramBundle.putInt("productid", this.productid);
      paramBundle.putInt("shopid", this.shopId);
      paramBundle.putString("shopname", this.shopName);
      paramBundle.putString("bookingBtnText", this.buttontext);
      this.mFragment.setArguments(paramBundle);
    }
    if (isEmpty(this.buttontext));
    for (paramBundle = "预约看店"; ; paramBundle = this.buttontext)
    {
      setTitle(paramBundle);
      return;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.shopId);
    paramBundle.putString("shopname", this.shopName);
    paramBundle.putInt("productid", this.productid);
    paramBundle.putString("bookingBtnText", this.buttontext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingBookingActivity
 * JD-Core Version:    0.6.0
 */