package com.dianping.shopinfo.education.view;

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

public class EducationShopHeaderView extends ShopInfoHeaderView
{
  private static final String REVIEW_TAIL = "条点评";
  private TextView edu_booking_numberTV;
  private DPObject headerInfo;
  private TextView reviewCount;

  public EducationShopHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public EducationShopHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.name.setText(getFullName(paramDPObject));
    setIconImage(paramDPObject);
    this.power.setPower(paramDPObject.getInt("ShopPower"));
    setReviewCountStr();
    setPicCountStr(paramDPObject);
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
          EducationShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        EducationShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.val$coverImage.setVisibility(0);
      }
    });
  }

  public void setPicCountStr(DPObject paramDPObject)
  {
    this.imgCount = ((TextView)findViewById(R.id.imgCount));
    if (this.imgCount != null)
    {
      if (paramDPObject.getInt("PicCount") >= 0)
      {
        this.imgCount.setVisibility(0);
        this.imgCount.setText(paramDPObject.getInt("PicCount") + "张");
      }
    }
    else
      return;
    this.imgCount.setVisibility(8);
  }

  public void setReviewCountStr()
  {
    if (this.headerInfo == null)
      return;
    String str = String.valueOf(this.headerInfo.getInt("ReviewCount"));
    this.reviewCount = ((TextView)findViewById(R.id.shop_review_count));
    if (TextUtils.isEmpty(str))
    {
      this.reviewCount.setVisibility(4);
      str = this.headerInfo.getString("BookingNumberText");
      this.edu_booking_numberTV = ((TextView)findViewById(R.id.edu_booking_numberTV));
      if (TextUtils.isEmpty(str))
      {
        this.edu_booking_numberTV.setVisibility(4);
        return;
      }
    }
    else
    {
      if (str.contains("条点评"));
      while (true)
      {
        this.reviewCount.setText(str);
        this.reviewCount.setVisibility(0);
        break;
        str = str + "条点评";
      }
    }
    this.edu_booking_numberTV.setText(str);
    this.reviewCount.setVisibility(0);
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }

  protected void setScoreSourceInfo(DPObject paramDPObject)
  {
    if (this.rateSource != null)
      super.setScoreSourceInfo(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.view.EducationShopHeaderView
 * JD-Core Version:    0.6.0
 */