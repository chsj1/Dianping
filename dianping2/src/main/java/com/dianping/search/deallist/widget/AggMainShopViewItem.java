package com.dianping.search.deallist.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.DistanceUtils;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ShopPower;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class AggMainShopViewItem extends NovaLinearLayout
  implements ViewItemInterface
{
  protected TextView shopDetailName;
  protected TextView shopDistanceView;
  protected View shopInfoView;
  protected View shopMoreInfoView;
  protected TextView shopNameView;
  protected ShopPower shopPower;

  public AggMainShopViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public AggMainShopViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.SHOP;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopInfoView = findViewById(R.id.shop_info);
    this.shopMoreInfoView = findViewById(R.id.shop_more_info);
    this.shopNameView = ((TextView)findViewById(R.id.shop_name));
    this.shopDistanceView = ((TextView)findViewById(R.id.shop_distance));
    this.shopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.shopDetailName = ((TextView)findViewById(R.id.shop_detail_name));
    setClickable(true);
  }

  public boolean performClick()
  {
    return super.performClick();
  }

  public void setShopInfo(DPObject paramDPObject, double paramDouble1, double paramDouble2)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"));
    Object localObject;
    do
    {
      return;
      String str2 = paramDPObject.getString("Name");
      localObject = paramDPObject.getString("BranchName");
      String str1 = str2;
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        str1 = " (" + (String)localObject + ")";
        str1 = str2 + str1;
      }
      this.shopNameView.setText(str1);
      str1 = DistanceUtils.calculateDistance(paramDouble1, paramDouble2, paramDPObject.getDouble("Latitude"), paramDPObject.getDouble("Longitude"));
      str2 = paramDPObject.getString("FloorLabel");
      if (!TextUtils.isEmpty(str2))
        str1 = str2;
      this.shopDistanceView.setText(str1);
      this.shopPower.setPower(paramDPObject.getInt("ShopPower"));
      str1 = paramDPObject.getString("PriceText");
      str2 = paramDPObject.getString("RegionName");
      paramDPObject = paramDPObject.getString("CategoryName");
      localObject = new StringBuilder();
      if (!TextUtils.isEmpty(str1))
        ((StringBuilder)localObject).append("  ").append(str1);
      if (!TextUtils.isEmpty(str2))
        ((StringBuilder)localObject).append("  ").append(str2);
      if (TextUtils.isEmpty(paramDPObject))
        continue;
      ((StringBuilder)localObject).append("  ").append(paramDPObject);
    }
    while (((StringBuilder)localObject).length() <= 0);
    this.shopDetailName.setText(((StringBuilder)localObject).substring(2));
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setShopInfo(paramDPObject.getObject("Shop"), paramDouble1, paramDouble2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.AggMainShopViewItem
 * JD-Core Version:    0.6.0
 */