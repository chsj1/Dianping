package com.dianping.shopinfo.beauty.hair.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.NetworkImageView;

public class BeautyShopInfoHeaderView extends ShopInfoHeaderView
{
  private TextView avgCostLabel;
  private TextView avgCostView;
  private DPObject beautyDPObject;
  private TextView common_count;
  private DPObject shop;
  private TextView verticalDivider;

  public BeautyShopInfoHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public BeautyShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.common_count = ((TextView)findViewById(R.id.comment_count));
    this.avgCostView = ((TextView)findViewById(R.id.shop_avg_cost));
    this.avgCostLabel = ((TextView)findViewById(R.id.shop_avg_cost_label));
    this.verticalDivider = ((TextView)findViewById(R.id.vertical_divider));
  }

  public void setAvgPrice()
  {
    int i;
    if ((this.shop != null) && (this.avgCostView != null) && (this.avgCostLabel != null) && (this.verticalDivider != null))
    {
      i = this.shop.getInt("AvgPrice");
      if (i > 0)
        break label71;
    }
    label71: for (String str = ""; TextUtils.isEmpty(str); str = String.valueOf(i))
    {
      this.avgCostView.setVisibility(8);
      this.verticalDivider.setVisibility(8);
      return;
    }
    this.verticalDivider.setVisibility(0);
    this.avgCostLabel.setVisibility(0);
    this.avgCostView.setText(str);
    this.avgCostView.setVisibility(0);
  }

  public void setBeautyDPObject(DPObject paramDPObject)
  {
    this.beautyDPObject = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    MyResources.getResource(ShopInfoHeaderView.class);
    paramDPObject = "";
    if (this.beautyDPObject != null)
      paramDPObject = this.beautyDPObject.getString("HeadPhotoUrl");
    if (TextUtils.isEmpty(paramDPObject))
    {
      this.icon.setBackgroundResource(R.color.gray_light_background);
      return;
    }
    this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
    this.icon.setImage(paramDPObject);
  }

  public void setShop(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.shop = paramDPObject;
    setBaseInfo(paramDPObject);
  }

  public void setTotalReviewTextView(int paramInt)
  {
    if (paramInt > 0)
    {
      this.common_count.setText(Integer.toString(paramInt) + getResources().getString(R.string.beauty_num_unit));
      this.common_count.setVisibility(0);
      return;
    }
    this.common_count.setVisibility(4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.beauty.hair.widget.BeautyShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */