package com.dianping.hotel.deal.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.AutoHideTextView;
import com.dianping.base.widget.BuyDealView;
import com.dianping.util.TextUtils;
import com.dianping.widget.view.NovaButton;

public class HotelProdBuyView extends BuyDealView
{
  protected DPObject dataModel;

  public HotelProdBuyView(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelProdBuyView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setDataModel(DPObject paramDPObject)
  {
    this.dataModel = paramDPObject;
    updateView();
  }

  protected void updateView()
  {
    if ((this.dataModel != null) && (this.price != null))
    {
      SpannableString localSpannableString = new SpannableString("¥" + PriceFormatUtils.formatPrice(this.dataModel.getDouble("MarketPrice")));
      localSpannableString.setSpan(new StrikethroughSpan(), 0, localSpannableString.length(), 33);
      this.originalPrice.setText(localSpannableString);
      this.price.setText(PriceFormatUtils.formatPrice(this.dataModel.getDouble("Price")));
      if ((this.dataModel != null) && (this.dataModel.getObject("BuyConfig") != null) && (this.dataModel.getObject("BuyConfig").getBoolean("ButtonEnabled")))
        break label195;
      this.buy.setEnabled(false);
    }
    while (true)
    {
      if ((this.dataModel.getObject("BuyConfig") != null) && (!TextUtils.isEmpty(this.dataModel.getObject("BuyConfig").getString("ButtonText"))))
        this.buy.setText(this.dataModel.getObject("BuyConfig").getString("ButtonText"));
      return;
      label195: this.buy.setEnabled(true);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.widget.HotelProdBuyView
 * JD-Core Version:    0.6.0
 */