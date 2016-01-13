package com.dianping.shopinfo.mall.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class MallDealListItem extends NovaRelativeLayout
{
  public static final String RMB = "￥";
  protected TextView distanceView;
  protected DPObject dpDeal;
  protected TextView markView;
  protected TextView originalPriceView;
  protected TextView priceView;
  protected TextView subtitleView;
  protected NetworkThumbView thumbImage;
  protected TextView titleView;

  public MallDealListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public MallDealListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private CharSequence extractPriceText(DPObject paramDPObject)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    SpannableString localSpannableString = new SpannableString("￥" + paramDPObject.getString("Price"));
    localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_title)), 1, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length(), 33);
    localSpannableStringBuilder.append(localSpannableString);
    localSpannableStringBuilder.append(" ");
    paramDPObject = new SpannableString("￥" + paramDPObject.getString("OriginalPrice"));
    paramDPObject.setSpan(new StrikethroughSpan(), 1, paramDPObject.length(), 33);
    paramDPObject.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_size_hint)), 0, paramDPObject.length(), 33);
    paramDPObject.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, paramDPObject.length(), 33);
    localSpannableStringBuilder.append(paramDPObject);
    return localSpannableStringBuilder;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.deal_item_icon));
    this.titleView = ((TextView)findViewById(R.id.deal_item_title));
    this.priceView = ((TextView)findViewById(R.id.deal_item_price));
    this.originalPriceView = ((TextView)findViewById(R.id.deal_item_origin_price));
    this.distanceView = ((TextView)findViewById(R.id.deal_item_distance));
    this.subtitleView = ((TextView)findViewById(R.id.deal_item_subtitle));
    this.markView = ((TextView)findViewById(R.id.mark_text));
  }

  public void setDeal(DPObject paramDPObject)
  {
    if (!TextUtils.isEmpty(paramDPObject.getString("Photo")))
    {
      this.thumbImage.setImage(paramDPObject.getString("Photo"));
      this.thumbImage.setVisibility(0);
      if (TextUtils.isEmpty(paramDPObject.getString("Title")))
        break label195;
      this.titleView.setText(paramDPObject.getString("Title"));
      this.titleView.setVisibility(0);
      label66: if (TextUtils.isEmpty(paramDPObject.getString("Floor")))
        break label207;
      this.distanceView.setText(paramDPObject.getString("Floor"));
      this.distanceView.setVisibility(0);
      label99: if (TextUtils.isEmpty(paramDPObject.getString("ContentTitle")))
        break label219;
      this.subtitleView.setText(paramDPObject.getString("ContentTitle"));
      this.subtitleView.setVisibility(0);
    }
    while (true)
    {
      CharSequence localCharSequence = extractPriceText(paramDPObject);
      if (!TextUtils.isEmpty(localCharSequence))
      {
        this.priceView.setText(localCharSequence);
        this.priceView.setVisibility(0);
      }
      if (paramDPObject.getBoolean("NeedReservation"))
        findViewById(R.id.deal_tag).setVisibility(8);
      return;
      this.thumbImage.setVisibility(8);
      break;
      label195: this.titleView.setVisibility(8);
      break label66;
      label207: this.distanceView.setVisibility(8);
      break label99;
      label219: this.subtitleView.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.view.MallDealListItem
 * JD-Core Version:    0.6.0
 */