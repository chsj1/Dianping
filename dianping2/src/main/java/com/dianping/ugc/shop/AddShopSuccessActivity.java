package com.dianping.ugc.shop;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.layout;

public class AddShopSuccessActivity extends NovaActivity
{
  private DPObject shopObj;

  private void setupView()
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.shopObj = ((DPObject)getIntent().getExtras().get("shop"));
    if (this.shopObj == null)
      finish();
    super.setContentView(R.layout.add_shop_success);
    setupView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.shop.AddShopSuccessActivity
 * JD-Core Version:    0.6.0
 */