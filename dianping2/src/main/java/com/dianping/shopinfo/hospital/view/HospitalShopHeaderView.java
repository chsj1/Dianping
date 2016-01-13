package com.dianping.shopinfo.hospital.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetImageHandler;
import com.dianping.widget.NetworkImageView;

public class HospitalShopHeaderView extends ShopInfoHeaderView
{
  private DPObject headerInfo;

  public HospitalShopHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public HospitalShopHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.name.setText(getFullName(paramDPObject));
    setIconImage(paramDPObject);
    this.power.setPower(paramDPObject.getInt("ShopPower"));
  }

  public void setHeaderInfo(DPObject paramDPObject)
  {
    this.headerInfo = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (this.headerInfo == null)
      return;
    paramDPObject = (ImageView)findViewById(R.id.shop_panel_cover_image);
    MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
    String str = this.headerInfo.getString("DefaultImg");
    if (TextUtils.isEmpty(str))
    {
      this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.header_default));
      this.icon.setImageHandler(new NetImageHandler(paramDPObject)
      {
        public void onFinish()
        {
          HospitalShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
          this.val$coverImage.setVisibility(0);
        }
      });
      return;
    }
    this.icon.setImage(str);
    this.icon.setImageHandler(new NetImageHandler(paramDPObject)
    {
      public void onFinish()
      {
        HospitalShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.val$coverImage.setVisibility(0);
      }
    });
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }

  protected void setScoreSourceInfo(DPObject paramDPObject)
  {
    if (this.rateSource != null)
    {
      if (!TextUtils.isEmpty(paramDPObject.getString("StarTips")))
      {
        this.rateSource.setText(paramDPObject.getString("StarTips"));
        this.rateSource.setVisibility(0);
      }
    }
    else
      return;
    super.setScoreSourceInfo(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.view.HospitalShopHeaderView
 * JD-Core Version:    0.6.0
 */