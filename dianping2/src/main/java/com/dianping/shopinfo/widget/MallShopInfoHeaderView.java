package com.dianping.shopinfo.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NetworkImageView;

public class MallShopInfoHeaderView extends ShopInfoHeaderView
{
  public MallShopInfoHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MallShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if ((paramDPObject.contains("PicCount")) && (paramDPObject.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramDPObject.getString("DefaultPic"))))
      if (this.icon != null)
      {
        MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
        this.icon.setBackgroundResource(R.color.gray_light_background);
        this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.placeholder_default));
        this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (this.imgCountZero != null)
        {
          this.imgCountZero.setVisibility(0);
          this.imgCountZero.setText("上传第1张图片");
        }
      }
    while (true)
    {
      if (this.imgCount != null)
      {
        if (paramDPObject.getInt("PicCount") == 0)
          break;
        this.imgCount.setVisibility(0);
        this.imgCount.setText("" + paramDPObject.getInt("PicCount"));
      }
      return;
      if (this.icon == null)
        continue;
      this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.icon.setImage(paramDPObject.getString("DefaultPic"));
    }
    this.imgCount.setVisibility(8);
  }

  protected void setPrice(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.MallShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */