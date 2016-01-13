package com.dianping.shopinfo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class TourShopInfoHeaderView extends ShopInfoHeaderView
{
  private TextView imgCount;

  public TourShopInfoHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public TourShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.imgCount = ((TextView)findViewById(R.id.imgCount));
    this.isTourShopInfoHeader = true;
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    super.setBaseInfo(paramDPObject);
    if (paramDPObject.getInt("AvgPrice") == 0)
      this.price.setVisibility(8);
    while (paramDPObject.getInt("PicCount") != 0)
    {
      this.imgCount.setVisibility(0);
      this.imgCount.setText(paramDPObject.getInt("PicCount") + "张");
      return;
      this.price.setVisibility(0);
    }
    this.imgCount.setVisibility(8);
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    for (paramDPObject = null; TextUtils.isEmpty(paramDPObject); paramDPObject = paramDPObject.getString("DefaultPic"))
    {
      this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.icon.setLocalDrawable(MyResources.getResource(TourShopInfoHeaderView.class).getDrawable(R.drawable.shopinfo_head_default));
      return;
    }
    this.icon.setImage(paramDPObject);
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.TourShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */