package com.dianping.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.utils.DistanceUtils;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class ShopViewItem extends NovaFrameLayout
  implements ViewItemInterface
{
  protected CheckBox checkBox;
  protected ColorBorderTextView colorBorderTextView;
  protected RelativeLayout dealInfoRl;
  protected TextView distanceView;
  protected DPObject dpShop;
  protected View iconFrame;
  protected RelativeLayout rootRl;
  private ImageView statusNoPic;
  private ImageView statusView;
  protected TextView subtitleView;
  protected NetworkThumbView thumbImage;
  protected TextView titleView;

  public ShopViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.SHOP;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.rootRl = ((RelativeLayout)findViewById(R.id.root_rl));
    this.iconFrame = findViewById(R.id.deal_item_icon_frame);
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.deal_item_icon));
    this.statusView = ((ImageView)findViewById(R.id.deal_item_status));
    this.statusNoPic = ((ImageView)findViewById(R.id.deal_item_status_nopic));
    this.titleView = ((TextView)findViewById(R.id.deal_item_title));
    this.distanceView = ((TextView)findViewById(R.id.deal_item_distance));
    this.checkBox = ((CheckBox)findViewById(R.id.deal_item_checkbox));
    this.subtitleView = ((TextView)findViewById(R.id.deal_item_subtitle));
    this.dealInfoRl = ((RelativeLayout)findViewById(R.id.deal_item_info));
    this.colorBorderTextView = ((ColorBorderTextView)findViewById(R.id.desc));
    setEditable(false);
    setClickable(true);
  }

  public void refreshDistance(double paramDouble1, double paramDouble2)
  {
    if (this.dpShop == null)
      return;
    String str = DistanceUtils.getDistance(this.dpShop.getDouble("Latitude"), this.dpShop.getDouble("Longitude"), paramDouble1, paramDouble2);
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

  public void setShop(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this.dpShop = paramDPObject;
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"))
      return;
    Object localObject = paramDPObject.getString("Name");
    this.titleView.setText((CharSequence)localObject);
    localObject = com.dianping.util.TextUtils.jsonParseText(paramDPObject.getString("MatchText"));
    if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
    {
      this.subtitleView.setText((CharSequence)localObject);
      this.subtitleView.setVisibility(0);
    }
    localObject = paramDPObject.getString("Desc");
    if (android.text.TextUtils.isEmpty((CharSequence)localObject))
    {
      this.colorBorderTextView.setVisibility(8);
      i = R.drawable.agg_market_tag;
      this.statusView.setImageResource(i);
      this.statusNoPic.setImageResource(i);
      if (!paramBoolean)
        break label222;
      this.iconFrame.setVisibility(0);
      this.thumbImage.setImage(paramDPObject.getString("DefaultPic"));
      paramDPObject = this.statusView;
      if (i == 0)
        break label215;
    }
    label215: for (int i = 0; ; i = 8)
    {
      paramDPObject.setVisibility(i);
      this.statusNoPic.setVisibility(8);
      refreshDistance(paramDouble1, paramDouble2);
      return;
      this.colorBorderTextView.setVisibility(0);
      this.colorBorderTextView.setText((CharSequence)localObject);
      this.colorBorderTextView.setBorderColor(getResources().getColor(R.color.market_colored_desc));
      break;
    }
    label222: this.iconFrame.setVisibility(8);
    this.statusView.setVisibility(8);
    if (i != 0)
      this.dealInfoRl.setPadding(ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 10.0F), ViewUtils.dip2px(getContext(), 10.0F));
    paramDPObject = this.statusNoPic;
    if (i != 0);
    for (i = 0; ; i = 8)
    {
      paramDPObject.setVisibility(i);
      break;
    }
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setShop(paramDPObject.getObject("Shop"), paramDouble1, paramDouble2, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.ShopViewItem
 * JD-Core Version:    0.6.0
 */