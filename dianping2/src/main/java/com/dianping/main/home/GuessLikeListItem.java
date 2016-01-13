package com.dianping.main.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianping.app.DPApplication;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopPower;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R;
import com.dianping.widget.view.NovaLinearLayout;

import org.json.JSONException;

public class GuessLikeListItem extends NovaLinearLayout
{
  public static final String RMB = "¥";
  private ImageView bookIcon;
  private LinearLayout bottomLayout;
  private CheckBox checkBox;
  private Context context;
  private LinearLayout dealInfoRl;
  private TextView distanceView;
  private HomeGuessLikeItem dpDeal;
  private LinearLayout eventsView;
  private ImageView labelIcon;
  private TextView newdealView;
  private ShopPower power;
  private TextView priceView;
  private ImageView promoIcon;
  private LinearLayout rootRl;
  private ImageView sceneryorderIcon;
  private boolean showSalesFlag = false;
  private ImageView statusView;
  private TextView subtitleView;
  private NetworkThumbView thumbImage;
  private TextView titleView;
  private ImageView tuanIcon;

  public GuessLikeListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public GuessLikeListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private String calculateDistance(double paramDouble1, double paramDouble2)
  {
    double d = ConfigHelper.configDistanceFactor;
    if (d <= 0.0D);
    do
    {
      do
        return null;
      while ((paramDouble1 == 0.0D) || (paramDouble2 == 0.0D) || (this.dpDeal.lat == 0.0D) || (this.dpDeal.lng == 0.0D));
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.dpDeal.lat, this.dpDeal.lng)) * d;
    }
    while ((Double.isNaN(paramDouble1)) || (paramDouble1 <= 0.0D));
    return getDistanceText((int)Math.round(paramDouble1 / 10.0D) * 10);
  }

  private String getDistanceText(long paramLong)
  {
    if ((this.dpDeal.lat == 0.0D) && (this.dpDeal.lng == 0.0D))
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

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.statusView = ((ImageView)findViewById(R.id.deal_item_status));
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.deal_item_icon));
    this.titleView = ((TextView)findViewById(R.id.deal_item_title));
    this.priceView = ((TextView)findViewById(R.id.deal_item_price));
    this.eventsView = ((LinearLayout)findViewById(R.id.deal_item_tags));
    this.distanceView = ((TextView)findViewById(R.id.deal_item_distance));
    this.checkBox = ((CheckBox)findViewById(R.id.deal_item_checkbox));
    this.subtitleView = ((TextView)findViewById(R.id.deal_item_subtitle));
    this.newdealView = ((TextView)findViewById(R.id.deal_item_rec_text));
    this.tuanIcon = ((ImageView)findViewById(R.id.ic_tuan));
    this.promoIcon = ((ImageView)findViewById(R.id.ic_promo));
    this.sceneryorderIcon = ((ImageView)findViewById(R.id.ic_sceneryorder));
    this.bookIcon = ((ImageView)findViewById(R.id.ic_book));
    this.power = ((ShopPower)findViewById(R.id.shop_power));
    this.labelIcon = ((ImageView)findViewById(R.id.deal_info_label));
    this.rootRl = ((LinearLayout)findViewById(R.id.root_rl));
    this.dealInfoRl = ((LinearLayout)findViewById(R.id.deal_item_info));
    this.bottomLayout = ((LinearLayout)findViewById(R.id.guesslikeitem_bottom_layout));
    setEditable(false);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checkBox.setChecked(paramBoolean);
  }

  public void setDeal(HomeGuessLikeItem paramHomeGuessLikeItem, double paramDouble1, double paramDouble2)
  {
    this.dpDeal = paramHomeGuessLikeItem;
    int i = DPApplication.instance().getApplicationContext().getResources().getDisplayMetrics().widthPixels - this.rootRl.getPaddingLeft() - this.rootRl.getPaddingRight() - getResources().getDimensionPixelSize(R.dimen.home_deal_list_image_width) - this.dealInfoRl.getPaddingLeft() - this.dealInfoRl.getPaddingRight();
    this.titleView.setText(paramHomeGuessLikeItem.mainTitle);
    label154: Object localObject;
    label188: int j;
    if (!TextUtils.isEmpty(paramHomeGuessLikeItem.subTitle))
    {
      this.subtitleView.setText(paramHomeGuessLikeItem.subTitle);
      if (TextUtils.isEmpty(paramHomeGuessLikeItem.price))
        break label813;
      this.priceView.setText(paramHomeGuessLikeItem.price);
      this.priceView.setVisibility(0);
      i = i - ViewUtils.measureTextView(this.priceView) - this.priceView.getPaddingLeft() - this.priceView.getPaddingRight();
      localObject = calculateDistance(paramDouble1, paramDouble2);
      if (TextUtils.isEmpty((CharSequence)localObject))
        break label865;
      this.distanceView.setText((CharSequence)localObject);
      this.distanceView.setVisibility(0);
      j = 0;
      if (paramHomeGuessLikeItem.tagType != 1)
        break label877;
      j = R.drawable.deal_list_item_status_reservation;
      label204: this.statusView.setImageResource(j);
      localObject = this.statusView;
      if (j == 0)
        break label909;
      j = 0;
      label227: ((ImageView)localObject).setVisibility(j);
      this.thumbImage.setImage(paramHomeGuessLikeItem.picUrl);
      this.thumbImage.setImageModule("guesslike");
      if ((this.dpDeal.extContentJson == null) || (TextUtils.isEmpty(this.dpDeal.extContentJson.toString())))
        break label971;
      j = i;
    }
    int m;
    while (true)
    {
      try
      {
        localObject = this.dpDeal.extContentJson.getString("salesdesc");
        j = i;
        if (TextUtils.isEmpty((CharSequence)localObject))
          continue;
        j = i;
        this.newdealView.setText((CharSequence)localObject);
        j = i;
        this.newdealView.setVisibility(0);
        j = i;
        i = i - ViewUtils.measureTextView(this.newdealView) - this.newdealView.getPaddingLeft() - this.newdealView.getPaddingRight();
        j = i;
        if (this.dpDeal.extContentJson.optInt("hasthumb", 0) != 1)
          continue;
        j = i;
        this.labelIcon.setVisibility(0);
        j = i;
        int k = this.labelIcon.getMeasuredWidth();
        j = i;
        m = this.labelIcon.getPaddingLeft();
        j = i;
        int n = this.labelIcon.getPaddingRight();
        i = i - k - m - n;
        String[] arrayOfString = paramHomeGuessLikeItem.tags;
        if ((arrayOfString == null) || (arrayOfString.length <= 0))
          break label1036;
        this.eventsView.setVisibility(0);
        int i3 = Math.min(2, arrayOfString.length);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams.setMargins(5, 0, 0, 0);
        j = this.eventsView.getPaddingLeft() + this.eventsView.getPaddingRight();
        m = 0;
        n = 0;
        if (n >= i3)
          break label993;
        int i2 = i;
        int i1 = m;
        k = j;
        if (TextUtils.isEmpty(arrayOfString[n]))
          continue;
        TextView localTextView = (TextView)this.eventsView.getChildAt(n);
        localObject = localTextView;
        k = j;
        if (localTextView != null)
          continue;
        localObject = new TextView(getContext());
        ((TextView)localObject).setTextColor(this.context.getResources().getColor(R.color.tuan_common_orange));
        ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.home_tagsize));
        ((TextView)localObject).setSingleLine();
        ((TextView)localObject).setEllipsize(TextUtils.TruncateAt.END);
        ((TextView)localObject).setPadding(3, 3, 3, 3);
        ((TextView)localObject).setBackgroundResource(R.drawable.background_round_textview_lightred);
        this.eventsView.addView((View)localObject, localLayoutParams);
        k = ViewUtils.measureTextView((TextView)localObject) + j + ViewUtils.dip2px(getContext(), 8.0F) + 10;
        ((TextView)localObject).setText(arrayOfString[n]);
        j = ViewUtils.measureTextView((TextView)localObject);
        if (i <= j + getResources().getDimension(R.dimen.home_deal_bottom_add_width))
          break label983;
        ((TextView)localObject).setVisibility(0);
        i = i - ViewUtils.measureTextView((TextView)localObject) - ((TextView)localObject).getPaddingLeft() - ((TextView)localObject).getPaddingRight();
        i1 = m + 1;
        i2 = i;
        n += 1;
        i = i2;
        m = i1;
        j = k;
        continue;
        this.subtitleView.setVisibility(8);
        break;
        label813: this.priceView.setText(" ");
        this.priceView.setVisibility(8);
        localObject = (LinearLayout.LayoutParams)this.bottomLayout.getLayoutParams();
        ((LinearLayout.LayoutParams)localObject).setMargins(0, 5, 0, 0);
        this.bottomLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
        break label154;
        label865: this.distanceView.setVisibility(8);
        break label188;
        label877: if (paramHomeGuessLikeItem.tagType != 2)
          continue;
        j = R.drawable.deal_list_item_status_free;
        break label204;
        if (paramHomeGuessLikeItem.tagType != 3)
          break label204;
        j = R.drawable.deal_list_item_status_coupon;
        break label204;
        label909: j = 8;
        break label227;
        j = i;
        this.newdealView.setVisibility(8);
        continue;
      }
      catch (JSONException localJSONException)
      {
        this.newdealView.setVisibility(8);
        localJSONException.printStackTrace();
        i = j;
        continue;
        j = i;
        this.labelIcon.setVisibility(8);
        continue;
      }
      label971: this.newdealView.setVisibility(8);
      continue;
      label983: localJSONException.setVisibility(8);
    }
    label993: i = this.eventsView.getChildCount() - 1;
    while (i > m - 1)
    {
      this.eventsView.getChildAt(i).setVisibility(8);
      i -= 1;
      continue;
      label1036: this.eventsView.setVisibility(8);
    }
    ImageView localImageView = this.tuanIcon;
    if (paramHomeGuessLikeItem.showTuan)
    {
      i = 0;
      localImageView.setVisibility(i);
      localImageView = this.promoIcon;
      if (!paramHomeGuessLikeItem.showCu)
        break label1171;
      i = 0;
      label1084: localImageView.setVisibility(i);
      localImageView = this.sceneryorderIcon;
      if (!paramHomeGuessLikeItem.showPiao)
        break label1178;
      i = 0;
      label1107: localImageView.setVisibility(i);
      localImageView = this.bookIcon;
      if (!paramHomeGuessLikeItem.showDing)
        break label1185;
    }
    label1171: label1178: label1185: for (i = 0; ; i = 8)
    {
      localImageView.setVisibility(i);
      if (paramHomeGuessLikeItem.shopPower <= 0)
        break label1192;
      this.power.setVisibility(0);
      this.power.setPower(paramHomeGuessLikeItem.shopPower);
      return;
      i = 8;
      break;
      i = 8;
      break label1084;
      i = 8;
      break label1107;
    }
    label1192: this.power.setVisibility(8);
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.GuessLikeListItem
 * JD-Core Version:    0.6.0
 */