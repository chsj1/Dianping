package com.dianping.membercard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.dianping.base.widget.ShopPower;
import com.dianping.util.ViewUtils;

public class ShopPowerItemView extends OneLineListItemView
{
  private ShopPower mShopPower;

  public ShopPowerItemView(Context paramContext)
  {
    super(paramContext);
    initShopPower(paramContext);
  }

  public ShopPowerItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initShopPower(paramContext);
  }

  private void initShopPower(Context paramContext)
  {
    RelativeLayout localRelativeLayout = getContainer();
    this.mShopPower = new ShopPower(paramContext);
    localRelativeLayout.addView(this.mShopPower);
    this.mShopPower.setPadding(0, 0, ViewUtils.dip2px(paramContext, 8.0F), 0);
  }

  public ShopPowerItemView setShopPower(int paramInt)
  {
    this.mShopPower.setPower(paramInt);
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.ShopPowerItemView
 * JD-Core Version:    0.6.0
 */