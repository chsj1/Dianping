package com.dianping.search.deallist.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils.TruncateAt;
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

public class HuiViewItem extends NovaFrameLayout
  implements ViewItemInterface
{
  public static final String RMB = "¥";
  protected TextView bottomStatus;
  protected CheckBox checkBox;
  protected RelativeLayout dealInfoRl;
  protected TextView distanceView;
  protected DPObject dpHui;
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

  public HuiViewItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public HuiViewItem(Context paramContext, AttributeSet paramAttributeSet)
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

  public void setChecked(boolean paramBoolean)
  {
    this.checkBox.setChecked(paramBoolean);
  }

  public void setData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this.priceView.setVisibility(8);
    this.originalPriceView.setVisibility(8);
    this.bottomStatus.setVisibility(8);
    this.recommendReason.setVisibility(8);
    this.extraRecommendReason.setVisibility(8);
    if (paramDPObject == null);
    label940: label977: 
    while (true)
    {
      return;
      this.dpHui = paramDPObject;
      int j = DPApplication.instance().getApplicationContext().getResources().getDisplayMetrics().widthPixels - this.rootRl.getPaddingLeft() - this.rootRl.getPaddingRight();
      this.rankingView.setVisibility(8);
      Object localObject = paramDPObject.getString("ShortTitle");
      this.titleView.setText((CharSequence)localObject);
      this.subtitleView.setText(paramDPObject.getString("Title"));
      int i = R.drawable.deal_list_item_status_hui;
      this.statusView.setImageResource(i);
      this.statusNoPic.setImageResource(i);
      String str1;
      if (paramBoolean)
      {
        this.iconFrame.setVisibility(0);
        this.thumbImage.setImage(paramDPObject.getString("BigPhoto"));
        localObject = this.statusView;
        if (i != 0)
        {
          i = 0;
          ((ImageView)localObject).setVisibility(i);
          this.statusNoPic.setVisibility(8);
          i = j - getResources().getDimensionPixelSize(R.dimen.deal_list_image_width);
          j = i - this.dealInfoRl.getPaddingLeft() - this.dealInfoRl.getPaddingRight();
          this.eventsView.setVisibility(0);
          this.newDealView.setVisibility(0);
          this.newDealView.setVisibility(8);
          localObject = this.dpHui.getString("SalesDesc");
          str1 = this.dpHui.getString("SalesTag");
          if (TextUtils.isEmpty(str1))
            break label728;
          localObject = TextUtils.jsonParseText(str1);
          if (!TextUtils.isEmpty((CharSequence)localObject))
          {
            this.newDealView.setText((CharSequence)localObject);
            this.newDealView.setVisibility(0);
          }
          i = j - ViewUtils.measureTextView(this.newDealView) - this.newDealView.getPaddingLeft() - this.newDealView.getPaddingRight();
        }
      }
      int k;
      while (true)
      {
        paramDPObject = paramDPObject.getArray("EventList");
        if (DPObjectUtils.isArrayEmpty(paramDPObject))
          break label940;
        this.eventsView.removeAllViews();
        this.eventsView.setVisibility(0);
        int n = Math.min(2, paramDPObject.length);
        localObject = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject).setMargins(10, 0, 0, 0);
        k = this.eventsView.getPaddingLeft() + this.eventsView.getPaddingRight();
        j = 0;
        while (j < n)
        {
          str1 = paramDPObject[j].getString("ShortTitle");
          int m = k;
          if (!TextUtils.isEmpty(str1))
          {
            ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
            String str2 = paramDPObject[j].getString("Color");
            localColorBorderTextView.setTextColor(str2);
            localColorBorderTextView.setBorderColor("#C8" + str2.substring(1));
            localColorBorderTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
            localColorBorderTextView.setSingleLine();
            localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
            localColorBorderTextView.setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
            localColorBorderTextView.setText(str1);
            this.eventsView.addView(localColorBorderTextView, (ViewGroup.LayoutParams)localObject);
            m = ViewUtils.measureTextView(localColorBorderTextView) + k + ViewUtils.dip2px(getContext(), 8.0F) + 10;
          }
          j += 1;
          k = m;
        }
        i = 8;
        break;
        this.iconFrame.setVisibility(8);
        this.statusView.setVisibility(8);
        if (i != 0)
          this.dealInfoRl.setPadding(0, ViewUtils.dip2px(getContext(), 15.0F), 0, 0);
        localObject = this.statusNoPic;
        if (i != 0);
        for (i = 0; ; i = 8)
        {
          ((ImageView)localObject).setVisibility(i);
          i = j;
          break;
        }
        label728: i = j;
        if (TextUtils.isEmpty((CharSequence)localObject))
          continue;
        localObject = TextUtils.jsonParseText((String)localObject);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          this.newDealView.setText((CharSequence)localObject);
          this.newDealView.setVisibility(0);
        }
        i = j - ViewUtils.measureTextView(this.newDealView) - this.newDealView.getPaddingLeft() - this.newDealView.getPaddingRight();
      }
      if ((k > i) && (this.newDealView.getVisibility() != 8))
      {
        ViewUtils.measureTextView(this.newDealView);
        this.newDealView.getPaddingLeft();
        this.newDealView.getPaddingRight();
        this.newDealView.setVisibility(8);
      }
      paramDPObject = DistanceUtils.getDistance(this.dpHui.getDouble("Lat"), this.dpHui.getDouble("Lng"), paramDouble1, paramDouble2);
      if (TextUtils.isEmpty(paramDPObject))
        this.distanceView.setVisibility(8);
      while (true)
      {
        if (this.dpHui == null)
          break label977;
        paramDPObject = TextUtils.jsonParseText(this.dpHui.getString("DisplayJson"));
        if (paramDPObject == null)
          break label979;
        this.priceView.setText(paramDPObject);
        this.priceView.setVisibility(0);
        return;
        this.eventsView.setVisibility(8);
        this.newDealView.setVisibility(0);
        break;
        this.distanceView.setText(paramDPObject);
        this.distanceView.setVisibility(0);
      }
    }
    label979: this.priceView.setVisibility(8);
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
      setData(paramDPObject.getObject("Hui"), paramDouble1, paramDouble2, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.HuiViewItem
 * JD-Core Version:    0.6.0
 */