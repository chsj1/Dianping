package com.dianping.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.ShopAndPromoListActivity;
import com.dianping.base.basic.ShowOrHideImage;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PromoListItem extends LinearLayout
  implements ShowOrHideImage
{
  static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  TextView countTxt;
  TextView deadlineTxt;
  TextView distanceTxt;
  ImageView ic_distance;
  TextView priceTxt;
  DPObject promo;
  TextView shopName;
  TextView tagTxt;
  NetworkThumbView thumb;
  TextView titleTxt;
  ImageView typeImg;

  public PromoListItem(Context paramContext)
  {
    super(paramContext);
  }

  public PromoListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setDistanceText(double paramDouble1, double paramDouble2)
  {
    Object localObject2 = null;
    double d = ConfigHelper.configDistanceFactor;
    Object localObject1;
    if (d <= 0.0D)
      localObject1 = localObject2;
    while (true)
    {
      this.distanceTxt.setText((CharSequence)localObject1);
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        break;
      this.ic_distance.setVisibility(8);
      return;
      localObject1 = localObject2;
      if (paramDouble1 == 0.0D)
        continue;
      localObject1 = localObject2;
      if (paramDouble2 == 0.0D)
        continue;
      localObject1 = localObject2;
      if (this.promo.getDouble("Latitude") == 0.0D)
        continue;
      localObject1 = localObject2;
      if (this.promo.getDouble("Longitude") == 0.0D)
        continue;
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.promo.getDouble("Latitude"), this.promo.getDouble("Longitude"))) * d;
      localObject1 = localObject2;
      if (Double.isNaN(paramDouble1))
        continue;
      localObject1 = localObject2;
      if (paramDouble1 <= 0.0D)
        continue;
      int i = (int)Math.round(paramDouble1 / 10.0D) * 10;
      if (i <= 100)
      {
        localObject1 = "<100m";
        continue;
      }
      if (i > 100000)
      {
        localObject1 = ">100km";
        continue;
      }
      if (i >= 10000)
      {
        localObject1 = i / 1000 + "km";
        continue;
      }
      if (i < 1000)
      {
        localObject1 = i + "m";
        continue;
      }
      i /= 100;
      localObject1 = i / 10 + "." + i % 10 + "km";
    }
    this.ic_distance.setVisibility(0);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumb = ((NetworkThumbView)findViewById(16908294));
    this.shopName = ((TextView)findViewById(16908308));
    this.titleTxt = ((TextView)findViewById(16908309));
    this.countTxt = ((TextView)findViewById(R.id.text3));
    this.distanceTxt = ((TextView)findViewById(R.id.text4));
    this.tagTxt = ((TextView)findViewById(R.id.text5));
    this.priceTxt = ((TextView)findViewById(R.id.price));
    this.deadlineTxt = ((TextView)findViewById(R.id.deadline));
    this.typeImg = ((ImageView)findViewById(R.id.imageView));
    this.ic_distance = ((ImageView)findViewById(R.id.ic_distance));
  }

  public void setPromo(DPObject paramDPObject, double paramDouble1, double paramDouble2, ShopAndPromoListActivity paramShopAndPromoListActivity)
  {
    this.promo = paramDPObject;
    if (NovaConfigUtils.isShowShopImg)
    {
      this.thumb.setImage(paramDPObject.getString("PromoPhoto"));
      this.thumb.setVisibility(0);
      this.titleTxt.setText(paramDPObject.getString("PromoTitle"));
      if (this.shopName != null)
        this.shopName.setText(paramDPObject.getString("ShopName"));
      if (this.countTxt != null)
        this.countTxt.setText("" + paramDPObject.getInt("DownloadCount"));
      if (this.tagTxt != null)
      {
        if ((paramDPObject.getString("Tag") != null) && (!"".equals(paramDPObject.getString("Tag").trim())))
          break label308;
        this.tagTxt.setVisibility(8);
      }
      label145: if (this.priceTxt != null)
        this.priceTxt.setText("￥" + PRICE_DF.format(paramDPObject.getDouble("Price")));
      if (this.deadlineTxt != null)
      {
        if (TextUtils.isEmpty(paramDPObject.getString("EndTime")))
          break label332;
        this.deadlineTxt.setText("有效期至:" + paramDPObject.getString("EndTime"));
      }
      label239: if ((paramShopAndPromoListActivity != null) && (!paramShopAndPromoListActivity.isShowPromoTypeImg))
        break label430;
      if (paramDPObject.getInt("Flag") != 4)
        break label386;
      this.typeImg.setVisibility(0);
      this.typeImg.setImageResource(R.drawable.promo_item_hot);
    }
    while (true)
    {
      if (this.distanceTxt != null)
        setDistanceText(paramDouble1, paramDouble2);
      return;
      this.thumb.setVisibility(8);
      break;
      label308: this.tagTxt.setVisibility(0);
      this.tagTxt.setText(paramDPObject.getString("Tag"));
      break label145;
      label332: if (paramDPObject.getTime("EndTime") <= 0L)
        break label239;
      this.deadlineTxt.setText("有效期至:" + fmt.format(Long.valueOf(paramDPObject.getTime("EndTime"))));
      break label239;
      label386: if (paramDPObject.getInt("Flag") == 2)
      {
        this.typeImg.setVisibility(0);
        this.typeImg.setImageResource(R.drawable.promo_item_new);
        continue;
      }
      this.typeImg.setVisibility(8);
      continue;
      label430: this.typeImg.setVisibility(8);
    }
  }

  public void showOrHideShopImg(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.thumb.setImage(this.promo.getString("PromoPhoto"));
      this.thumb.setVisibility(0);
      return;
    }
    this.thumb.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PromoListItem
 * JD-Core Version:    0.6.0
 */