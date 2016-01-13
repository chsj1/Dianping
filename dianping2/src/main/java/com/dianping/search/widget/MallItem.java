package com.dianping.search.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.widget.ShopListItemIcon;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class MallItem extends NovaLinearLayout
{
  private ShopListItemIcon icons;
  private TextView mDistanceView;
  private View mDivider;
  private View mIconFrame;
  private TextView mPromoCountView;
  private TextView mRegionView;
  private TextView mShopCountView;
  private View mShopCountViewImg;
  private NetworkThumbView mThumbImage;
  private TextView mTitleView;
  private ShopDataModel mall;

  public MallItem(Context paramContext)
  {
    super(paramContext);
  }

  public MallItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public MallItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void setDistanceText(double paramDouble1, double paramDouble2)
  {
    Object localObject2 = null;
    double d = ConfigHelper.configDistanceFactor;
    Object localObject1;
    if (d <= 0.0D)
    {
      localObject1 = localObject2;
      this.mDistanceView.setText((CharSequence)localObject1);
      if (TextUtils.isEmpty((CharSequence)localObject1))
        break label316;
    }
    label316: for (boolean bool = true; ; bool = false)
    {
      showDistanceText(bool);
      return;
      localObject1 = localObject2;
      if (paramDouble1 == 0.0D)
        break;
      localObject1 = localObject2;
      if (paramDouble2 == 0.0D)
        break;
      localObject1 = localObject2;
      if (this.mall.lat == 0.0D)
        break;
      localObject1 = localObject2;
      if (this.mall.lng == 0.0D)
        break;
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.mall.lat, this.mall.lng)) * d;
      localObject1 = localObject2;
      if (Double.isNaN(paramDouble1))
        break;
      localObject1 = localObject2;
      if (paramDouble1 <= 0.0D)
        break;
      int i = (int)Math.round(paramDouble1 / 10.0D) * 10;
      if (i <= 100)
      {
        localObject1 = "<100m";
        break;
      }
      if (i > 100000)
      {
        localObject1 = ">100km";
        break;
      }
      if (i >= 10000)
      {
        localObject1 = i / 1000 + "km";
        break;
      }
      if (i < 1000)
      {
        localObject1 = i + "m";
        break;
      }
      i /= 100;
      localObject1 = i / 10 + "." + i % 10 + "km";
      break;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mIconFrame = findViewById(R.id.mall_icon_frame);
    this.mThumbImage = ((NetworkThumbView)findViewById(R.id.mall_icon));
    this.mTitleView = ((TextView)findViewById(R.id.mall_title));
    this.icons = new ShopListItemIcon(this);
    this.mRegionView = ((TextView)findViewById(R.id.mall_region));
    this.mDistanceView = ((TextView)findViewById(R.id.mall_distance));
    this.mShopCountView = ((TextView)findViewById(R.id.mall_shop_count));
    this.mShopCountViewImg = findViewById(R.id.mall_shop_count_img);
    this.mPromoCountView = ((TextView)findViewById(R.id.mall_promo_count));
    this.mDivider = findViewById(R.id.divider);
  }

  public void refreshDistance(double paramDouble1, double paramDouble2, String paramString)
  {
    if (this.mall == null)
      return;
    if (!TextUtils.isEmpty(paramString))
    {
      this.mDistanceView.setText(paramString);
      showDistanceText(true);
      return;
    }
    setDistanceText(paramDouble1, paramDouble2);
  }

  public void setMall(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this.mall = new ShopDataModel(paramDPObject);
    setMall(this.mall, paramDouble1, paramDouble2, paramBoolean, false);
  }

  public void setMall(ShopDataModel paramShopDataModel, double paramDouble1, double paramDouble2, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mall = paramShopDataModel;
    if (paramBoolean1)
    {
      this.mIconFrame.setVisibility(0);
      this.mThumbImage.setImage(paramShopDataModel.defaultPic);
      this.mTitleView.setText(paramShopDataModel.fullName);
      if (TextUtils.isEmpty(paramShopDataModel.matchText))
        break label163;
      this.mRegionView.setText(paramShopDataModel.matchText);
      this.icons.setVisibility(paramShopDataModel.iconVisibility);
      this.mTitleView.requestLayout();
      refreshDistance(paramDouble1, paramDouble2, paramShopDataModel.distanceText);
      if (!TextUtils.isEmpty(paramShopDataModel.shopInfoInMall))
        break label256;
      this.mShopCountView.setVisibility(8);
      this.mShopCountViewImg.setVisibility(8);
      label118: if (!TextUtils.isEmpty(paramShopDataModel.promoInfoInMall))
        break label286;
      this.mPromoCountView.setVisibility(8);
    }
    while (true)
    {
      if (!paramBoolean2)
        break label308;
      this.mDivider.setVisibility(0);
      return;
      this.mIconFrame.setVisibility(8);
      break;
      label163: StringBuilder localStringBuilder = new StringBuilder();
      if (TextUtils.isEmpty(paramShopDataModel.regionName))
      {
        str = "";
        label186: localStringBuilder = localStringBuilder.append(str).append(" ");
        if (!TextUtils.isEmpty(paramShopDataModel.categoryName))
          break label247;
      }
      label247: for (String str = ""; ; str = paramShopDataModel.categoryName)
      {
        str = str;
        this.mRegionView.setText(str);
        break;
        str = paramShopDataModel.regionName;
        break label186;
      }
      label256: this.mShopCountView.setText(paramShopDataModel.shopInfoInMall);
      this.mShopCountView.setVisibility(0);
      this.mShopCountViewImg.setVisibility(0);
      break label118;
      label286: this.mPromoCountView.setText(paramShopDataModel.promoInfoInMall);
      this.mPromoCountView.setVisibility(0);
    }
    label308: this.mDivider.setVisibility(8);
  }

  public void showDistanceText(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mDistanceView.setVisibility(0);
      return;
    }
    this.mDistanceView.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.MallItem
 * JD-Core Version:    0.6.0
 */