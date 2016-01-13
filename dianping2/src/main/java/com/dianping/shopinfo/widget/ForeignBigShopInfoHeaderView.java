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
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class ForeignBigShopInfoHeaderView extends ShopInfoHeaderView
{
  protected TextView scoreText;

  public ForeignBigShopInfoHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public ForeignBigShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.scoreText = ((TextView)findViewById(R.id.score_text));
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (this.icon == null);
    while (true)
    {
      return;
      Object localObject = paramDPObject.getString("DefaultPic");
      if (TextUtils.isEmpty((CharSequence)localObject))
      {
        localObject = MyResources.getResource(ForeignBigShopInfoHeaderView.class);
        this.icon.setBackgroundResource(R.color.gray_light_background);
        this.icon.setLocalBitmap(BitmapFactory.decodeResource(((MyResources)localObject).getResources(), R.drawable.placeholder_default));
        this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      }
      while (this.imgCount != null)
      {
        if (paramDPObject.getInt("PicCount") == 0)
          break label142;
        this.imgCount.setVisibility(0);
        this.imgCount.setText("" + paramDPObject.getInt("PicCount"));
        return;
        this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.icon.setImage((String)localObject);
      }
    }
    label142: this.imgCount.setVisibility(8);
  }

  protected void setPrice(DPObject paramDPObject)
  {
    if (this.price != null)
    {
      if (!TextUtils.isEmpty(paramDPObject.getString("PriceText")))
        this.price.setText(paramDPObject.getString("PriceText"));
    }
    else
      return;
    this.price.setVisibility(8);
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
    if (this.scoreText != null)
    {
      if (!TextUtils.isEmpty(paramDPObject.getString("ScoreText")))
      {
        this.scoreText.setText(paramDPObject.getString("ScoreText"));
        this.scoreText.setVisibility(0);
      }
    }
    else
      return;
    this.scoreText.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ForeignBigShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */