package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class WeddingFeastPromoAgent extends ShopCellAgent
  implements View.OnClickListener
{
  private static final String CELL_WEDDING_PROMO = "0200Basic.09WeddingPromo";
  DPObject mWeddingHotelExtra;
  DPObject[] summary;

  public WeddingFeastPromoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBookingAgent()
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_info_wedding_feast_promo, getParentView(), false);
    localNovaLinearLayout.setGAString("wedcoupon");
    Object localObject1 = localNovaLinearLayout.findViewById(R.id.promo_hezuo);
    Object localObject2 = localNovaLinearLayout.findViewById(R.id.promo_feihezuo);
    if (this.mWeddingHotelExtra.getBoolean("Commision"))
    {
      ((View)localObject1).setVisibility(0);
      ((View)localObject2).setVisibility(8);
      if (this.summary.length == 3)
      {
        localObject2 = (TextView)((View)localObject1).findViewById(R.id.first_promo_summary);
        TextView localTextView1 = (TextView)((View)localObject1).findViewById(R.id.first_promo_title);
        TextView localTextView2 = (TextView)((View)localObject1).findViewById(R.id.second_promo_summary);
        TextView localTextView3 = (TextView)((View)localObject1).findViewById(R.id.second_promo_title);
        TextView localTextView4 = (TextView)((View)localObject1).findViewById(R.id.third_promo_summary);
        localObject1 = (TextView)((View)localObject1).findViewById(R.id.third_promo_title);
        ((TextView)localObject2).setText(this.summary[0].getString("Summary"));
        localTextView1.setText(this.summary[0].getString("Title"));
        localTextView2.setText(this.summary[1].getString("Summary"));
        localTextView3.setText(this.summary[1].getString("Title"));
        localTextView4.setText(this.summary[2].getString("Summary"));
        ((TextView)localObject1).setText(this.summary[2].getString("Title"));
      }
    }
    do
    {
      return localNovaLinearLayout;
      ((View)localObject1).setVisibility(8);
      ((View)localObject2).setVisibility(0);
    }
    while (this.summary.length != 1);
    localObject1 = (TextView)((View)localObject2).findViewById(R.id.promo_summary);
    localObject2 = (TextView)((View)localObject2).findViewById(R.id.promo_title);
    ((TextView)localObject1).setText(this.summary[0].getString("Summary"));
    ((TextView)localObject2).setText(this.summary[0].getString("Title"));
    return (View)(View)localNovaLinearLayout;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingType());
    do
    {
      do
      {
        return;
        this.mWeddingHotelExtra = ((DPObject)getSharedObject("WeddingHotelExtra"));
      }
      while ((this.mWeddingHotelExtra == null) || (this.mWeddingHotelExtra.getInt("CooperateType") != 1));
      this.summary = this.mWeddingHotelExtra.getArray("PromoSummaries");
    }
    while ((this.summary == null) || ((this.summary.length != 1) && (this.summary.length != 3)));
    paramBundle = createBookingAgent();
    paramBundle.setOnClickListener(this);
    addCell("0200Basic.09WeddingPromo", paramBundle);
  }

  public void onClick(View paramView)
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelpromo").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(getShop())).build().toString())));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (shopId() <= 0);
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastPromoAgent
 * JD-Core Version:    0.6.0
 */