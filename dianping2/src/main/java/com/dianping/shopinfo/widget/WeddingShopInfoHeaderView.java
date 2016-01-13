package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetImageHandler;
import com.dianping.widget.NetworkImageView;

public class WeddingShopInfoHeaderView extends ShopInfoHeaderView
{
  private DPObject extra;

  public WeddingShopInfoHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public WeddingShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setExtraInfo(DPObject paramDPObject)
  {
    this.extra = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
      return;
    while (this.extra == null);
    int i = this.extra.getInt("ImgCount");
    int j = paramDPObject.getInt("PicCount");
    if ((i == 0) && (j == 0) && (TextUtils.isEmpty(paramDPObject.getString("DefaultImg"))))
    {
      paramDPObject = MyResources.getResource(ShopInfoHeaderView.class);
      this.icon.setImageDrawable(paramDPObject.getResources().getDrawable(R.drawable.wedding_photo_none));
      if (this.imgCount != null)
      {
        if (i + j <= 0)
          break label188;
        this.imgCount.setVisibility(0);
        this.imgCount.setText("" + (i + j) + "张");
      }
    }
    while (true)
    {
      setPreferIcon();
      return;
      paramDPObject = MyResources.getResource(ShopInfoHeaderView.class);
      this.icon.setLocalBitmap(BitmapFactory.decodeResource(paramDPObject.getResources(), R.drawable.wedding_photo_default));
      this.icon.setImage(this.extra.getString("DefaultImg"));
      this.icon.setImageHandler(new NetImageHandler()
      {
        public void onFinish()
        {
          WeddingShopInfoHeaderView.this.icon.setScaleType(ImageView.ScaleType.FIT_XY);
        }
      });
      break;
      label188: this.imgCount.setVisibility(8);
    }
  }

  protected void setPreferIcon()
  {
    if (this.extra.getInt("PreferFlag") == 1)
    {
      findViewById(R.id.title_youxuan).setVisibility(0);
      return;
    }
    findViewById(R.id.title_youxuan).setVisibility(8);
  }

  protected void setPrice(DPObject paramDPObject)
  {
    if (this.extra == null)
      return;
    paramDPObject = this.extra.getString("Price");
    if ((paramDPObject == null) || (paramDPObject.equals("")))
      if (findViewById(R.id.wedding_middle_didver) != null)
        findViewById(R.id.wedding_middle_didver).setVisibility(8);
    while (true)
    {
      this.price.setText(paramDPObject);
      return;
      if (findViewById(R.id.wedding_middle_didver) == null)
        continue;
      findViewById(R.id.wedding_middle_didver).setVisibility(0);
    }
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.WeddingShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */