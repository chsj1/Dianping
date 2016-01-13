package com.dianping.shopinfo.wed.home.market.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NetworkImageView;

public class HomeMarketHeaderView extends ShopInfoHeaderView
{
  private DPObject homeMarketBrief;

  public HomeMarketHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public HomeMarketHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setHomeMarketBrief(DPObject paramDPObject)
  {
    this.homeMarketBrief = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if ((this.homeMarketBrief != null) && (!TextUtils.isEmpty(this.homeMarketBrief.getString("Logo"))))
    {
      this.icon.setImage(this.homeMarketBrief.getString("Logo"));
      return;
    }
    if (!TextUtils.isEmpty(paramDPObject.getString("DefaultPic")))
    {
      this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.icon.setImage(paramDPObject.getString("DefaultPic"));
      return;
    }
    paramDPObject = MyResources.getResource(ShopInfoHeaderView.class);
    this.icon.setBackgroundResource(R.color.gray_light_background);
    this.icon.setLocalBitmap(BitmapFactory.decodeResource(paramDPObject.getResources(), R.drawable.placeholder_default));
    this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
  }

  protected void setPrice(DPObject paramDPObject)
  {
    if ((this.voteCount != null) && (!paramDPObject.getBoolean("IsForeignShop")))
    {
      if (paramDPObject.getInt("VoteTotal") == 0)
        this.voteCount.setVisibility(4);
    }
    else
      return;
    this.voteCount.setVisibility(0);
    this.voteCount.setText(paramDPObject.getInt("VoteTotal") + "条评论");
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.widget.HomeMarketHeaderView
 * JD-Core Version:    0.6.0
 */