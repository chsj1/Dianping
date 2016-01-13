package com.dianping.search.deallist.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
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
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class AggHuiViewItem extends NovaFrameLayout
  implements ViewItemInterface
{
  public static final String RMB = "¥";
  private TextView bottomStatus;
  private CheckBox checkBox;
  private RelativeLayout dealInfoRl;
  private LinearLayout eventsView;
  private TextView extraRecommendReason;
  private View iconFrame;
  private TextView newdealView;
  private TextView originalPriceView;
  private TextView priceView;
  private TextView recommendReason;
  private RelativeLayout rootRl;
  private ImageView statusNoPic;
  private ImageView statusView;
  private NetworkThumbView thumbImage;
  private TextView titleView;

  public AggHuiViewItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public AggHuiViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.TUAN_DEAL;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.rootRl = ((RelativeLayout)findViewById(R.id.agg_deal_item_rl));
    this.iconFrame = findViewById(R.id.agg_deal_item_icon_frame);
    this.statusView = ((ImageView)findViewById(R.id.agg_deal_item_status));
    this.statusNoPic = ((ImageView)findViewById(R.id.agg_deal_item_status_nopic));
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.agg_deal_item_icon));
    this.titleView = ((TextView)findViewById(R.id.agg_deal_item_title));
    this.priceView = ((TextView)findViewById(R.id.agg_deal_item_price));
    this.originalPriceView = ((TextView)findViewById(R.id.agg_deal_item_origin_price));
    this.eventsView = ((LinearLayout)findViewById(R.id.agg_deal_item_tags));
    this.checkBox = ((CheckBox)findViewById(R.id.agg_deal_item_checkbox));
    this.newdealView = ((TextView)findViewById(R.id.agg_deal_item_rec_text));
    this.dealInfoRl = ((RelativeLayout)findViewById(R.id.agg_deal_item_info));
    this.bottomStatus = ((TextView)findViewById(R.id.agg_deal_item_bottom_status));
    this.recommendReason = ((TextView)findViewById(R.id.agg_deal_item_recommend_reason));
    this.extraRecommendReason = ((TextView)findViewById(R.id.agg_deal_item_extra_recommend_reason));
    setEditable(false);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checkBox.setChecked(paramBoolean);
  }

  public void setData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "HuiDetail"));
    label312: label1214: label1352: 
    while (true)
    {
      return;
      Object localObject1 = paramDPObject.getString("Title");
      this.titleView.setText((CharSequence)localObject1);
      int j = DPApplication.instance().getApplicationContext().getResources().getDisplayMetrics().widthPixels - this.rootRl.getPaddingLeft() - this.rootRl.getPaddingRight();
      paramDPObject.getInt("DealType");
      int k = paramDPObject.getInt("Status");
      this.priceView.setText("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("Price")));
      localObject1 = new SpannableString("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("OriginalPrice")));
      ((SpannableString)localObject1).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject1).length(), 33);
      this.originalPriceView.setText((CharSequence)localObject1);
      this.originalPriceView.setVisibility(0);
      int i = R.drawable.deal_list_item_status_hui;
      this.statusView.setImageResource(i);
      this.statusNoPic.setImageResource(i);
      label246: int m;
      if (paramBoolean)
      {
        this.iconFrame.setVisibility(0);
        this.thumbImage.setImage(paramDPObject.getString("BigPhoto"));
        localObject1 = this.statusView;
        if (i != 0)
        {
          i = 0;
          ((ImageView)localObject1).setVisibility(i);
          this.statusNoPic.setVisibility(8);
          i = j - getResources().getDimensionPixelSize(R.dimen.agg_deal_list_image_width);
          j = this.dealInfoRl.getPaddingLeft();
          m = this.dealInfoRl.getPaddingRight();
          localObject1 = "";
          if ((k & 0x4) == 0)
            break label543;
          localObject1 = "已结束";
          if ("".equals(localObject1))
            break label558;
          this.bottomStatus.setText((CharSequence)localObject1);
          this.bottomStatus.setVisibility(0);
          this.priceView.setVisibility(8);
          this.originalPriceView.setVisibility(8);
          this.eventsView.setVisibility(8);
          this.newdealView.setVisibility(8);
          this.recommendReason.setVisibility(8);
          this.extraRecommendReason.setVisibility(8);
        }
      }
      while (true)
      {
        if (paramDPObject == null)
          break label1352;
        paramDPObject = TextUtils.jsonParseText(paramDPObject.getString("DisplayJson"));
        if (paramDPObject == null)
          break;
        this.priceView.setText(paramDPObject);
        this.originalPriceView.setVisibility(8);
        return;
        i = 8;
        break label246;
        this.iconFrame.setVisibility(8);
        this.statusView.setVisibility(8);
        if (i != 0)
          this.dealInfoRl.setPadding(ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 10.0F));
        localObject1 = this.statusNoPic;
        if (i != 0);
        for (i = 0; ; i = 8)
        {
          ((ImageView)localObject1).setVisibility(i);
          i = j;
          break;
        }
        if ((k & 0x2) == 0)
          break label312;
        localObject1 = "已售完";
        break label312;
        this.bottomStatus.setVisibility(8);
        this.priceView.setVisibility(0);
        k = ViewUtils.measureTextView(this.priceView);
        int n = this.priceView.getPaddingLeft();
        int i1 = this.priceView.getPaddingRight();
        this.originalPriceView.setVisibility(0);
        j = i - j - m - k - n - i1 - ViewUtils.measureTextView(this.originalPriceView) - this.originalPriceView.getPaddingLeft() - this.originalPriceView.getPaddingRight();
        this.eventsView.setVisibility(0);
        this.newdealView.setVisibility(0);
        localObject1 = paramDPObject.getString("RecommendReason");
        this.recommendReason.setVisibility(8);
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          this.recommendReason.setText((CharSequence)localObject1);
          this.recommendReason.setVisibility(0);
        }
        localObject1 = paramDPObject.getString("ExtraRecommendReason");
        this.extraRecommendReason.setVisibility(8);
        if ((this.recommendReason.getVisibility() != 0) && (!TextUtils.isEmpty((CharSequence)localObject1)))
        {
          this.extraRecommendReason.setText(TextUtils.jsonParseText((String)localObject1));
          this.extraRecommendReason.setVisibility(0);
        }
        this.newdealView.setVisibility(8);
        localObject1 = paramDPObject.getString("SalesDesc");
        Object localObject2 = paramDPObject.getString("SalesTag");
        String str1;
        ColorBorderTextView localColorBorderTextView;
        String str2;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          localObject1 = TextUtils.jsonParseText((String)localObject2);
          if (!TextUtils.isEmpty((CharSequence)localObject1))
          {
            this.newdealView.setText((CharSequence)localObject1);
            this.newdealView.setVisibility(0);
          }
          i = j - ViewUtils.measureTextView(this.newdealView) - this.newdealView.getPaddingLeft() - this.newdealView.getPaddingRight();
          localObject1 = paramDPObject.getArray("EventList");
          if (DPObjectUtils.isArrayEmpty(localObject1))
            break label1326;
          this.eventsView.removeAllViews();
          this.eventsView.setVisibility(0);
          n = Math.min(2, localObject1.length);
          localObject2 = new LinearLayout.LayoutParams(-2, -2);
          ((LinearLayout.LayoutParams)localObject2).setMargins(10, 0, 0, 0);
          j = this.eventsView.getPaddingLeft() + this.eventsView.getPaddingRight();
          k = 0;
          if (k >= n)
            break label1247;
          str1 = localObject1[k].getString("ShortTitle");
          m = j;
          if (!TextUtils.isEmpty(str1))
          {
            localColorBorderTextView = new ColorBorderTextView(getContext());
            str2 = localObject1[k].getString("Color");
            localColorBorderTextView.setTextColor(str2);
            if (Build.VERSION.SDK_INT >= 11)
              break label1214;
            localColorBorderTextView.setBorderColor(str2);
          }
        }
        while (true)
        {
          localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          localColorBorderTextView.setSingleLine();
          localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
          localColorBorderTextView.setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
          localColorBorderTextView.setText(str1);
          this.eventsView.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject2);
          m = ViewUtils.measureTextView(localColorBorderTextView) + j + ViewUtils.dip2px(getContext(), 8.0F) + 10;
          k += 1;
          j = m;
          break label947;
          i = j;
          if (TextUtils.isEmpty((CharSequence)localObject1))
            break;
          localObject1 = TextUtils.jsonParseText((String)localObject1);
          if (!TextUtils.isEmpty((CharSequence)localObject1))
          {
            this.newdealView.setText((CharSequence)localObject1);
            this.newdealView.setVisibility(0);
          }
          i = j - ViewUtils.measureTextView(this.newdealView) - this.newdealView.getPaddingLeft() - this.newdealView.getPaddingRight();
          break;
          localColorBorderTextView.setBorderColor("#C8" + str2.substring(1));
        }
        label1247: if (j <= i)
          continue;
        k = i;
        if (this.newdealView.getVisibility() != 8)
        {
          k = ViewUtils.measureTextView(this.newdealView) + i + this.newdealView.getPaddingLeft() + this.newdealView.getPaddingRight();
          this.newdealView.setVisibility(8);
        }
        if (j <= k)
          continue;
        this.originalPriceView.setVisibility(8);
        continue;
        this.eventsView.setVisibility(8);
        this.newdealView.setVisibility(0);
        this.originalPriceView.setVisibility(0);
      }
    }
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

  public void setRecommendReason(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      paramString = new SpannableString(paramString);
      paramString.setSpan(new ForegroundColorSpan(Color.argb(255, 180, 140, 31)), 0, paramString.length(), 33);
    }
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setData(paramDPObject.getObject("Deal"), paramDouble1, paramDouble2, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.AggHuiViewItem
 * JD-Core Version:    0.6.0
 */