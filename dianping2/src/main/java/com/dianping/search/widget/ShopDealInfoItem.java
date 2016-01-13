package com.dianping.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.search.shoplist.data.model.DealModel;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaLinearLayout;

public class ShopDealInfoItem extends NovaLinearLayout
{
  private TextView mDealTitleTv;
  private TextView mOriginalPriceTv;
  private TextView mPriceTv;

  public ShopDealInfoItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopDealInfoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ShopDealInfoItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mPriceTv = ((TextView)findViewById(R.id.price));
    this.mOriginalPriceTv = ((TextView)findViewById(R.id.original_price));
    this.mDealTitleTv = ((TextView)findViewById(R.id.deal_title));
  }

  public void setShopDealInfo(DealModel paramDealModel)
  {
    this.mPriceTv.setText(getResources().getString(R.string.search_rmb) + PriceFormatUtils.formatPrice(paramDealModel.price));
    this.mOriginalPriceTv.setText(TextUtils.jsonParseText(String.format("{\"text\": \"%s\", \"strikethrough\": true}", new Object[] { getResources().getString(R.string.search_rmb) + PriceFormatUtils.formatPrice(paramDealModel.originalPrice) })));
    this.mDealTitleTv.setText(paramDealModel.dealTitle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.ShopDealInfoItem
 * JD-Core Version:    0.6.0
 */