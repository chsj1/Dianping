package com.dianping.search.deallist.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class HotelZuesViewItem extends NovaFrameLayout
  implements ViewItemInterface
{
  public static final String RMB = "¥";
  private CheckBox checkBox;
  private RelativeLayout dealInfoRl;
  private View iconFrame;
  private TextView newdealView;
  private TextView originalPriceView;
  private TextView priceView;
  private RelativeLayout rootRl;
  protected ImageView statusNoPic;
  protected ImageView statusView;
  private NetworkThumbView thumbImage;
  private TextView titleView;

  public HotelZuesViewItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelZuesViewItem(Context paramContext, AttributeSet paramAttributeSet)
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
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.agg_deal_item_icon));
    this.statusView = ((ImageView)findViewById(R.id.agg_deal_item_status));
    this.statusNoPic = ((ImageView)findViewById(R.id.agg_deal_item_status_nopic));
    this.titleView = ((TextView)findViewById(R.id.agg_deal_item_title));
    this.priceView = ((TextView)findViewById(R.id.agg_deal_item_price));
    this.originalPriceView = ((TextView)findViewById(R.id.agg_deal_item_origin_price));
    this.checkBox = ((CheckBox)findViewById(R.id.agg_deal_item_checkbox));
    this.newdealView = ((TextView)findViewById(R.id.agg_deal_item_rec_text));
    this.dealInfoRl = ((RelativeLayout)findViewById(R.id.agg_deal_item_info));
    setEditable(false);
    setClickable(true);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checkBox.setChecked(paramBoolean);
  }

  public void setDeal(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "SpuInfoDTO"))
      return;
    Object localObject = paramDPObject.getString("Title");
    this.titleView.setText((CharSequence)localObject);
    this.priceView.setText("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("Price")));
    localObject = new SpannableString("¥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("MarketPrice")));
    ((SpannableString)localObject).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject).length(), 33);
    this.originalPriceView.setText((CharSequence)localObject);
    this.originalPriceView.setVisibility(0);
    int i = R.drawable.deal_list_item_status_booking;
    this.statusView.setImageResource(i);
    this.statusNoPic.setImageResource(i);
    if (paramBoolean)
    {
      this.iconFrame.setVisibility(0);
      this.thumbImage.setImage(paramDPObject.getString("ImgUrl"));
      localObject = this.statusView;
      if (i != 0);
      for (i = 0; ; i = 8)
      {
        ((ImageView)localObject).setVisibility(i);
        this.statusNoPic.setVisibility(8);
        this.priceView.setVisibility(0);
        this.originalPriceView.setVisibility(0);
        i = paramDPObject.getInt("SellVol");
        if (i <= 0)
          break;
        paramDPObject = "已售" + i;
        this.newdealView.setText(paramDPObject);
        this.newdealView.setVisibility(0);
        return;
      }
    }
    this.iconFrame.setVisibility(8);
    this.statusView.setVisibility(8);
    if (i != 0)
      this.dealInfoRl.setPadding(ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 10.0F));
    localObject = this.statusNoPic;
    if (i != 0);
    for (i = 0; ; i = 8)
    {
      ((ImageView)localObject).setVisibility(i);
      break;
    }
    this.newdealView.setVisibility(8);
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
      setDeal(paramDPObject.getObject("HotelZeus"), paramDouble1, paramDouble2, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.HotelZuesViewItem
 * JD-Core Version:    0.6.0
 */