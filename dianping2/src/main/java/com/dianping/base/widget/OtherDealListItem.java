package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class OtherDealListItem extends DealListItem
{
  private ImageView recIcon;

  public OtherDealListItem(Context paramContext)
  {
    super(paramContext);
  }

  public OtherDealListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected double calculateDistance(double paramDouble1, double paramDouble2)
  {
    double d = ConfigHelper.configDistanceFactor;
    if (d <= 0.0D)
      return 0.0D;
    if ((paramDouble1 == 0.0D) || (paramDouble2 == 0.0D))
      return 0.0D;
    if ((this.dpDeal.getDouble("Latitude") == 0.0D) || (this.dpDeal.getDouble("Longitude") == 0.0D))
      return 0.0D;
    paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.dpDeal.getDouble("Latitude"), this.dpDeal.getDouble("Longitude"))) * d;
    if (Double.isNaN(paramDouble1))
      return 0.0D;
    return paramDouble1;
  }

  protected String getDistance(double paramDouble1, double paramDouble2)
  {
    paramDouble1 = calculateDistance(paramDouble1, paramDouble2);
    if (paramDouble1 <= 0.0D)
      return null;
    return getDistanceText((int)Math.round(paramDouble1 / 10.0D) * 10);
  }

  protected String getDistanceText(long paramLong)
  {
    String str;
    if ((paramLong > 3000L) && (!TextUtils.isEmpty(this.dpDeal.getString("RegionName"))))
      str = this.dpDeal.getString("RegionName");
    do
    {
      return str;
      str = "";
    }
    while ((this.dpDeal.getDouble("Latitude") == 0.0D) && (this.dpDeal.getDouble("Longitude") == 0.0D));
    if (paramLong <= 100L)
      return "<100m";
    if (paramLong < 1000L)
      return paramLong + "m";
    if (paramLong < 10000L)
    {
      paramLong /= 100L;
      return paramLong / 10L + "." + paramLong % 10L + "km";
    }
    if (paramLong < 100000L)
      return paramLong / 1000L + "km";
    return "";
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.recIcon = ((ImageView)findViewById(R.id.deal_item_rec_icon));
  }

  public void setDeal(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    this.dpDeal = paramDPObject;
    label66: Object localObject;
    label137: int i;
    String str;
    if (paramBoolean2)
      if ((paramInt2 >= 1) && (paramInt2 <= 10))
        if (paramInt2 == 1)
        {
          this.rankingView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ranking_index_1));
          this.rankingView.setText(String.valueOf(paramInt2));
          this.rankingView.setVisibility(0);
          localObject = paramDPObject.getString("ShortTitle");
          this.titleView.setText((CharSequence)localObject);
          localObject = paramDPObject.getString("DealTitlePrefix");
          if ((localObject == null) || ("".equals(localObject)))
            break label621;
          this.subtitleView.setText((String)localObject + paramDPObject.getString("DealTitle"));
          paramInt2 = paramDPObject.getInt("DealType");
          i = paramDPObject.getInt("Status");
          this.priceView.setText("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("Price")));
          localObject = new SpannableString("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("OriginalPrice")));
          ((SpannableString)localObject).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject).length(), 33);
          this.originalPriceView.setText((CharSequence)localObject);
          this.originalPriceView.setVisibility(0);
          str = paramDPObject.getString("Distance");
          localObject = str;
          if (TextUtils.isEmpty(str))
            localObject = getDistance(paramDouble1, paramDouble2);
          this.distanceView.setVisibility(8);
          if (!TextUtils.isEmpty((CharSequence)localObject))
          {
            this.distanceView.setText((CharSequence)localObject);
            this.distanceView.setVisibility(0);
          }
          paramInt1 = 0;
          if (paramInt2 != 3)
            break label637;
          paramInt1 = R.drawable.deal_list_item_status_lottery;
          label337: this.statusView.setImageResource(paramInt1);
          this.statusNoPic.setImageResource(paramInt1);
          if (!paramBoolean1)
            break label678;
          this.iconFrame.setVisibility(0);
          this.thumbImage.setImage(paramDPObject.getString("Photo"));
          localObject = this.statusView;
          if (paramInt1 == 0)
            break label671;
          paramInt1 = 0;
          label396: ((ImageView)localObject).setVisibility(paramInt1);
          this.statusNoPic.setVisibility(8);
          localObject = "";
          if ((i & 0x4) == 0)
            break label752;
          localObject = "已结束";
          label428: if ("".equals(localObject))
            break label767;
          this.bottomStatus.setText((CharSequence)localObject);
          this.bottomStatus.setVisibility(0);
          this.priceView.setVisibility(8);
          this.originalPriceView.setVisibility(8);
          this.eventsView.setVisibility(8);
          this.newDealView.setVisibility(8);
          this.recIcon.setVisibility(8);
          this.distanceView.setVisibility(8);
        }
    while (true)
    {
      if (paramInt2 == 5)
        this.originalPriceView.setVisibility(8);
      return;
      if (paramInt2 == 2)
      {
        this.rankingView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ranking_index_2));
        break;
      }
      if (paramInt2 == 3)
      {
        this.rankingView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ranking_index_3));
        break;
      }
      this.rankingView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ranking_index_4to10));
      break;
      this.rankingView.setVisibility(8);
      break label66;
      this.rankingView.setVisibility(8);
      break label66;
      label621: this.subtitleView.setText(paramDPObject.getString("DealTitle"));
      break label137;
      label637: if (paramInt2 == 5)
      {
        paramInt1 = R.drawable.deal_list_item_status_online;
        break label337;
      }
      if ((paramDPObject.getInt("Tag") & 0x1) == 0)
        break label337;
      paramInt1 = R.drawable.deal_list_item_status_reservation;
      break label337;
      label671: paramInt1 = 8;
      break label396;
      label678: this.iconFrame.setVisibility(8);
      this.statusView.setVisibility(8);
      if (paramInt1 != 0)
        this.dealInfoRl.setPadding(0, ViewUtils.dip2px(getContext(), 15.0F), 0, 0);
      localObject = this.statusNoPic;
      if (paramInt1 != 0);
      for (paramInt1 = 0; ; paramInt1 = 8)
      {
        ((ImageView)localObject).setVisibility(paramInt1);
        break;
      }
      label752: if ((i & 0x2) == 0)
        break label428;
      localObject = "已售完";
      break label428;
      label767: this.bottomStatus.setVisibility(8);
      this.priceView.setVisibility(0);
      this.originalPriceView.setVisibility(0);
      this.eventsView.setVisibility(0);
      this.newDealView.setVisibility(0);
      this.distanceView.setVisibility(0);
      this.recIcon.setVisibility(0);
      localObject = paramDPObject.getString("TotalReviewRecommend");
      if ((i & 0x1) != 0)
      {
        this.recIcon.setVisibility(8);
        this.newDealView.setText("今日新单");
        this.newDealView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        this.newDealView.setTextColor(getResources().getColor(R.color.tuan_new_green));
      }
      while (true)
      {
        paramDPObject = paramDPObject.getArray("EventList");
        if (DPObjectUtils.isArrayEmpty(paramDPObject))
          break label1205;
        this.eventsView.removeAllViews();
        this.eventsView.setVisibility(0);
        this.originalPriceView.setVisibility(8);
        i = Math.min(2, paramDPObject.length);
        localObject = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject).setMargins(5, 0, 0, 0);
        paramInt1 = 0;
        while (paramInt1 < i)
        {
          str = paramDPObject[paramInt1].getString("ShortTitle");
          if (!TextUtils.isEmpty(str))
          {
            ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
            localColorBorderTextView.setTextColor(paramDPObject[paramInt1].getString("Color"));
            localColorBorderTextView.setBorderColor(paramDPObject[paramInt1].getString("Color"));
            localColorBorderTextView.setText(str);
            localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
            localColorBorderTextView.setSingleLine();
            localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
            localColorBorderTextView.setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
            this.eventsView.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject);
          }
          paramInt1 += 1;
        }
        break;
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          this.recIcon.setVisibility(0);
          this.newDealView.setText((CharSequence)localObject);
          this.newDealView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          this.newDealView.setTextColor(getResources().getColor(R.color.tuan_common_gray));
          continue;
        }
        this.recIcon.setVisibility(8);
        this.newDealView.setVisibility(8);
      }
      label1205: this.eventsView.setVisibility(8);
      this.originalPriceView.setVisibility(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.OtherDealListItem
 * JD-Core Version:    0.6.0
 */