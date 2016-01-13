package com.dianping.base.basic;

import android.os.Bundle;
import com.dianping.base.util.NovaConfigUtils;

public class ShopAndPromoListActivity extends DeleteListActivity
{
  public boolean isShowPromoTypeImg = false;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    NovaConfigUtils.showDialogInMobileNetworkWhenFirstStart(this);
  }

  protected void onImageSwitchChanged()
  {
  }

  protected void onResume()
  {
    super.onResume();
    if (NovaConfigUtils.isShowShopImg != NovaConfigUtils.isShowImageInMobileNetwork())
    {
      NovaConfigUtils.isShowShopImg = NovaConfigUtils.isShowImageInMobileNetwork();
      onImageSwitchChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ShopAndPromoListActivity
 * JD-Core Version:    0.6.0
 */