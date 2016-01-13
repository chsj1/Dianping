package com.dianping.shopinfo.mall.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class MallSaleHeaderView extends ShopInfoHeaderView
{
  private CircleImageView circleImageView;
  private ImageView frameView;
  private DPObject picInfo;

  public MallSaleHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public MallSaleHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    this.circleImageView = ((CircleImageView)findViewById(R.id.mall_logo_icon));
    this.frameView = ((ImageView)findViewById(R.id.mall_logo_frame));
    super.onFinishInflate();
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if ((this.picInfo != null) && (this.picInfo.getBoolean("Coop")))
    {
      ((ImageView)findViewById(R.id.shop_panel_cover_image)).setVisibility(0);
      this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.icon.setImage(this.picInfo.getString("Pic"));
      if ((this.circleImageView != null) && (this.frameView != null))
      {
        if (TextUtils.isEmpty(this.picInfo.getString("Logo")))
          break label192;
        this.circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.circleImageView.setImage(this.picInfo.getString("Logo"));
        this.circleImageView.setVisibility(0);
        this.frameView.setVisibility(0);
      }
    }
    while (true)
    {
      if (this.imgCount != null)
      {
        if (this.picInfo.getInt("Count") <= 0)
          break;
        this.imgCount.setVisibility(0);
        this.imgCount.setText(this.picInfo.getInt("Count") + "张");
      }
      return;
      label192: this.circleImageView.setVisibility(8);
      this.frameView.setVisibility(8);
    }
    this.imgCount.setVisibility(8);
  }

  public void setPicInfo(DPObject paramDPObject)
  {
    this.picInfo = paramDPObject;
  }

  public void setShop(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    setBaseInfo(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.view.MallSaleHeaderView
 * JD-Core Version:    0.6.0
 */