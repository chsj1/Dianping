package com.dianping.shopinfo.market.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class MarketProductListItem extends NovaRelativeLayout
{
  protected TextView collectCount;
  protected View line;
  protected NetworkThumbView productImage;
  protected LinearLayout productItemInfo;
  protected TextView subtitleView;
  protected TextView titleView;

  public MarketProductListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public MarketProductListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.productImage = ((NetworkThumbView)findViewById(R.id.product_item_icon));
    this.titleView = ((TextView)findViewById(R.id.product_item_title));
    this.subtitleView = ((TextView)findViewById(R.id.product_item_subtitle));
    this.collectCount = ((TextView)findViewById(R.id.product_item_collect_count));
    this.line = findViewById(R.id.line);
    this.productItemInfo = ((LinearLayout)findViewById(R.id.product_item_info));
  }

  public void setProduct(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if (!TextUtils.isEmpty(paramDPObject.getString("PicUrl")))
      this.productImage.setImage(paramDPObject.getString("PicUrl"));
    if (!TextUtils.isEmpty(paramDPObject.getString("Title")))
    {
      this.titleView.setText(paramDPObject.getString("Title"));
      this.titleView.setVisibility(0);
      if (TextUtils.isEmpty(paramDPObject.getString("PromoInfo")))
        break label152;
      this.subtitleView.setText(paramDPObject.getString("PromoInfo"));
      this.subtitleView.setVisibility(0);
      label91: if (TextUtils.isEmpty(paramDPObject.getString("CountInfo")))
        break label164;
      this.collectCount.setText(paramDPObject.getString("CountInfo"));
      this.collectCount.setVisibility(0);
    }
    while (true)
    {
      if (paramInt2 + 1 < paramInt1)
        this.line.setVisibility(0);
      return;
      this.titleView.setVisibility(8);
      break;
      label152: this.subtitleView.setVisibility(8);
      break label91;
      label164: this.collectCount.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.view.MarketProductListItem
 * JD-Core Version:    0.6.0
 */