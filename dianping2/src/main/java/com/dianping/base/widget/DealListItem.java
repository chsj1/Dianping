package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.utils.DistanceUtils;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class DealListItem extends NovaFrameLayout
  implements ViewItemInterface
{
  public static final int RANKING_INDEX_IGNORE = -1;
  public static final int RANKING_INDEX_MAX = 10;
  public static final int RANKING_INDEX_MIN = 1;
  public static final String RMB = "¥";
  protected TextView bottomStatus;
  protected CheckBox checkBox;
  protected RelativeLayout dealInfoRl;
  protected TextView distanceView;
  protected DPObject dpDeal;
  protected LinearLayout eventsView;
  protected TextView extraRecommendReason;
  protected View iconFrame;
  protected TextView newDealView;
  protected TextView originalPriceView;
  protected TextView priceView;
  protected TextView rankingView;
  protected TextView recommendReason;
  protected RelativeLayout rootRl;
  protected ImageView statusNoPic;
  protected ImageView statusView;
  protected TextView subtitleView;
  protected NetworkThumbView thumbImage;
  protected TextView titleView;

  public DealListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public DealListItem(Context paramContext, AttributeSet paramAttributeSet)
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
    if ((this.dpDeal.getDouble("Latitude") == 0.0D) && (this.dpDeal.getDouble("Longitude") == 0.0D))
      return "";
    String str;
    if (paramLong <= 100L)
      str = "<100m";
    while (true)
    {
      return str;
      if (paramLong < 1000L)
      {
        str = paramLong + "m";
        continue;
      }
      if (paramLong < 10000L)
      {
        paramLong /= 100L;
        str = paramLong / 10L + "." + paramLong % 10L + "km";
        continue;
      }
      if (paramLong < 100000L)
      {
        str = paramLong / 1000L + "km";
        continue;
      }
      str = "";
    }
  }

  public ViewItemType getType()
  {
    return ViewItemType.TUAN_DEAL;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.rootRl = ((RelativeLayout)findViewById(R.id.root_rl));
    this.iconFrame = findViewById(R.id.deal_item_icon_frame);
    this.statusView = ((ImageView)findViewById(R.id.deal_item_status));
    this.statusNoPic = ((ImageView)findViewById(R.id.deal_item_status_nopic));
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.deal_item_icon));
    this.rankingView = ((TextView)findViewById(R.id.deal_item_ranking));
    this.titleView = ((TextView)findViewById(R.id.deal_item_title));
    this.priceView = ((TextView)findViewById(R.id.deal_item_price));
    this.originalPriceView = ((TextView)findViewById(R.id.deal_item_origin_price));
    this.eventsView = ((LinearLayout)findViewById(R.id.deal_item_tags));
    this.distanceView = ((TextView)findViewById(R.id.deal_item_distance));
    this.checkBox = ((CheckBox)findViewById(R.id.deal_item_checkbox));
    this.subtitleView = ((TextView)findViewById(R.id.deal_item_subtitle));
    this.newDealView = ((TextView)findViewById(R.id.deal_item_rec_text));
    this.dealInfoRl = ((RelativeLayout)findViewById(R.id.deal_item_info));
    this.bottomStatus = ((TextView)findViewById(R.id.deal_item_bottom_status));
    this.recommendReason = ((TextView)findViewById(R.id.deal_item_recommend_reason));
    this.extraRecommendReason = ((TextView)findViewById(R.id.deal_item_extra_recommend_reason));
    setEditable(false);
  }

  public void refreshDistance(double paramDouble1, double paramDouble2)
  {
    if (this.dpDeal == null)
      return;
    String str = DistanceUtils.getDistance(this.dpDeal.getDouble("Latitude"), this.dpDeal.getDouble("Longitude"), paramDouble1, paramDouble2);
    if (!android.text.TextUtils.isEmpty(str))
    {
      this.distanceView.setText(str);
      this.distanceView.setVisibility(0);
      return;
    }
    this.distanceView.setVisibility(8);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checkBox.setChecked(paramBoolean);
  }

  public void setDeal(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean, int paramInt)
  {
    setDeal(paramDPObject, paramDouble1, paramDouble2, paramBoolean, paramInt, false, -1);
  }

  public void setDeal(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (paramDPObject == null)
      return;
    this.dpDeal = paramDPObject;
    int i = DPApplication.instance().getApplicationContext().getResources().getDisplayMetrics().widthPixels - this.rootRl.getPaddingLeft() - this.rootRl.getPaddingRight();
    label104: Object localObject1;
    label182: int k;
    label334: int j;
    if (paramBoolean2)
      if ((paramInt2 >= 1) && (paramInt2 <= 10))
        if (paramInt2 == 1)
        {
          this.rankingView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ranking_index_1));
          this.rankingView.setText(String.valueOf(paramInt2));
          this.rankingView.setVisibility(0);
          localObject1 = paramDPObject.getString("ShortTitle");
          this.titleView.setText((CharSequence)localObject1);
          localObject1 = paramDPObject.getString("DealTitlePrefix");
          if ((android.text.TextUtils.isEmpty((CharSequence)localObject1)) || (paramDPObject.getInt("DealChannel") == 4))
            break label715;
          this.subtitleView.setText((String)localObject1 + paramDPObject.getString("DealTitle"));
          k = paramDPObject.getInt("DealType");
          paramInt2 = paramDPObject.getInt("Status");
          this.priceView.setText("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("Price")));
          localObject1 = new SpannableString("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("OriginalPrice")));
          ((SpannableString)localObject1).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject1).length(), 33);
          this.originalPriceView.setText((CharSequence)localObject1);
          this.originalPriceView.setVisibility(0);
          paramInt1 = 0;
          if ((this.dpDeal.getInt("Tag") & 0x200) == 0)
            break label732;
          paramInt1 = R.drawable.deal_list_item_status_free;
          this.statusView.setImageResource(paramInt1);
          this.statusNoPic.setImageResource(paramInt1);
          if (!paramBoolean1)
            break label831;
          this.iconFrame.setVisibility(0);
          this.thumbImage.setImage(paramDPObject.getString("Photo"));
          localObject1 = this.statusView;
          if (paramInt1 == 0)
            break label824;
          paramInt1 = 0;
          label393: ((ImageView)localObject1).setVisibility(paramInt1);
          this.statusNoPic.setVisibility(8);
          paramInt1 = i - getResources().getDimensionPixelSize(R.dimen.deal_list_image_width);
          i = this.dealInfoRl.getPaddingLeft();
          j = this.dealInfoRl.getPaddingRight();
          localObject1 = "";
          if ((paramInt2 & 0x4) == 0)
            break label909;
          localObject1 = "已结束";
          label458: if ("".equals(localObject1))
            break label924;
          this.bottomStatus.setText((CharSequence)localObject1);
          this.bottomStatus.setVisibility(0);
          this.priceView.setVisibility(8);
          this.originalPriceView.setVisibility(8);
          this.eventsView.setVisibility(8);
          this.newDealView.setVisibility(8);
          this.recommendReason.setVisibility(8);
          this.extraRecommendReason.setVisibility(8);
        }
    Object localObject2;
    while (true)
    {
      if (k == 5)
        this.originalPriceView.setVisibility(8);
      localObject2 = DistanceUtils.getDistance(this.dpDeal.getDouble("Latitude"), this.dpDeal.getDouble("Longitude"), paramDouble1, paramDouble2);
      if (("".equals(localObject1)) && (!android.text.TextUtils.isEmpty((CharSequence)localObject2)) && (paramDPObject.getInt("DealChannel") != 4))
        break label1711;
      this.distanceView.setVisibility(8);
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
      break label104;
      this.rankingView.setVisibility(8);
      break label104;
      label715: this.subtitleView.setText(paramDPObject.getString("DealTitle"));
      break label182;
      label732: if ((paramDPObject.getInt("Tag") & 0x400) != 0)
      {
        paramInt1 = R.drawable.deal_list_item_status_dianping_chosen;
        break label334;
      }
      if ((paramDPObject.getInt("Tag") & 0x100) != 0)
      {
        paramInt1 = R.drawable.deal_list_item_status_booking;
        break label334;
      }
      if (k == 3)
      {
        paramInt1 = R.drawable.deal_list_item_status_lottery;
        break label334;
      }
      if (k == 5)
      {
        paramInt1 = R.drawable.deal_list_item_status_online;
        break label334;
      }
      if ((paramDPObject.getInt("Tag") & 0x1) == 0)
        break label334;
      paramInt1 = R.drawable.deal_list_item_status_reservation;
      break label334;
      label824: paramInt1 = 8;
      break label393;
      label831: this.iconFrame.setVisibility(8);
      this.statusView.setVisibility(8);
      if (paramInt1 != 0)
        this.dealInfoRl.setPadding(0, ViewUtils.dip2px(getContext(), 15.0F), 0, 0);
      localObject1 = this.statusNoPic;
      if (paramInt1 != 0);
      for (paramInt1 = 0; ; paramInt1 = 8)
      {
        ((ImageView)localObject1).setVisibility(paramInt1);
        paramInt1 = i;
        break;
      }
      label909: if ((paramInt2 & 0x2) == 0)
        break label458;
      localObject1 = "已售完";
      break label458;
      label924: this.bottomStatus.setVisibility(8);
      this.priceView.setVisibility(0);
      paramInt2 = ViewUtils.measureTextView(this.priceView);
      int m = this.priceView.getPaddingLeft();
      int n = this.priceView.getPaddingRight();
      this.originalPriceView.setVisibility(0);
      paramInt2 = paramInt1 - i - j - paramInt2 - m - n - ViewUtils.measureTextView(this.originalPriceView) - this.originalPriceView.getPaddingLeft() - this.originalPriceView.getPaddingRight();
      this.eventsView.setVisibility(0);
      this.newDealView.setVisibility(0);
      localObject2 = paramDPObject.getString("RecommendReason");
      this.recommendReason.setVisibility(8);
      if (!android.text.TextUtils.isEmpty((CharSequence)localObject2))
      {
        this.recommendReason.setText((CharSequence)localObject2);
        this.recommendReason.setVisibility(0);
      }
      localObject2 = this.dpDeal.getString("ExtraRecommendReason");
      this.extraRecommendReason.setVisibility(8);
      if ((this.recommendReason.getVisibility() != 0) && (!android.text.TextUtils.isEmpty((CharSequence)localObject2)))
      {
        this.extraRecommendReason.setText(com.dianping.util.TextUtils.jsonParseText((String)localObject2));
        this.extraRecommendReason.setVisibility(0);
      }
      this.newDealView.setVisibility(8);
      localObject2 = this.dpDeal.getString("SalesDesc");
      Object localObject3 = this.dpDeal.getString("SalesTag");
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject3))
      {
        localObject2 = com.dianping.util.TextUtils.jsonParseText((String)localObject3);
        if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2))
        {
          this.newDealView.setText((CharSequence)localObject2);
          this.newDealView.setVisibility(0);
        }
        paramInt1 = paramInt2 - ViewUtils.measureTextView(this.newDealView) - this.newDealView.getPaddingLeft() - this.newDealView.getPaddingRight();
      }
      while (true)
      {
        localObject2 = paramDPObject.getArray("EventList");
        if (!DPObjectUtils.isArrayEmpty(localObject2))
        {
          this.eventsView.removeAllViews();
          this.eventsView.setVisibility(0);
          m = Math.min(2, localObject2.length);
          localObject3 = new LinearLayout.LayoutParams(-2, -2);
          ((LinearLayout.LayoutParams)localObject3).setMargins(10, 0, 0, 0);
          paramInt2 = this.eventsView.getPaddingLeft() + this.eventsView.getPaddingRight();
          i = 0;
          while (true)
            if (i < m)
            {
              String str1 = localObject2[i].getString("ShortTitle");
              j = paramInt2;
              if (!android.text.TextUtils.isEmpty(str1))
              {
                ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
                String str2 = localObject2[i].getString("Color");
                localColorBorderTextView.setTextColor(str2);
                localColorBorderTextView.setBorderColor("#C8" + str2.substring(1));
                localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
                localColorBorderTextView.setSingleLine();
                localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
                localColorBorderTextView.setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
                localColorBorderTextView.setText(str1);
                this.eventsView.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject3);
                j = ViewUtils.measureTextView(localColorBorderTextView) + paramInt2 + ViewUtils.dip2px(getContext(), 8.0F) + 10;
              }
              i += 1;
              paramInt2 = j;
              continue;
              paramInt1 = paramInt2;
              if (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2))
                break;
              localObject2 = com.dianping.util.TextUtils.jsonParseText((String)localObject2);
              if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2))
              {
                this.newDealView.setText((CharSequence)localObject2);
                this.newDealView.setVisibility(0);
              }
              paramInt1 = paramInt2 - ViewUtils.measureTextView(this.newDealView) - this.newDealView.getPaddingLeft() - this.newDealView.getPaddingRight();
              break;
            }
          if (paramInt2 <= paramInt1)
            break;
          i = paramInt1;
          if (this.newDealView.getVisibility() != 8)
          {
            i = ViewUtils.measureTextView(this.newDealView) + paramInt1 + this.newDealView.getPaddingLeft() + this.newDealView.getPaddingRight();
            this.newDealView.setVisibility(8);
          }
          if (paramInt2 <= i)
            break;
          this.originalPriceView.setVisibility(8);
          break;
        }
      }
      this.eventsView.setVisibility(8);
      this.newDealView.setVisibility(0);
      this.originalPriceView.setVisibility(0);
    }
    label1711: this.distanceView.setText((CharSequence)localObject2);
    this.distanceView.setVisibility(0);
  }

  public void setEditable(boolean paramBoolean)
  {
    CheckBox localCheckBox = this.checkBox;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localCheckBox.setVisibility(i);
      return;
    }
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setDeal(paramDPObject.getObject("Deal"), paramDouble1, paramDouble2, paramBoolean, 1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DealListItem
 * JD-Core Version:    0.6.0
 */