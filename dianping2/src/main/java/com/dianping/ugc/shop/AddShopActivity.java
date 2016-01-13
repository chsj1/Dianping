package com.dianping.ugc.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;

public class AddShopActivity extends AgentActivity
{
  private OnCancelListener mOnCancelListener;
  private DPObject shop;

  protected AgentFragment getAgentFragment()
  {
    AddShopConfigurableFragment localAddShopConfigurableFragment = new AddShopConfigurableFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("shop_object", this.shop);
    localBundle.putInt("cityid", cityId());
    if ((getIntent() != null) && (getIntent().getData() != null))
      localBundle.putString("shopname", getIntent().getData().getQueryParameter("shopName"));
    localAddShopConfigurableFragment.setArguments(localBundle);
    return localAddShopConfigurableFragment;
  }

  public void onBackPressed()
  {
    this.mOnCancelListener.OnCancel();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public void setOnCancelListener(OnCancelListener paramOnCancelListener)
  {
    this.mOnCancelListener = paramOnCancelListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.shop.AddShopActivity
 * JD-Core Version:    0.6.0
 */