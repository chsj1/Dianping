package com.dianping.membercard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class CardChainShopItem extends NovaLinearLayout
{
  private RelativeLayout cardChainShopLayer;
  private TextView chainShopName;

  public CardChainShopItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public CardChainShopItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(getContext()).inflate(R.layout.mc_card_chainshop_item, this, true);
    this.cardChainShopLayer = ((RelativeLayout)findViewById(R.id.chainshop_layer));
    this.chainShopName = ((TextView)findViewById(R.id.chainshop_name));
  }

  public void setChainShop(DPObject paramDPObject)
  {
    this.chainShopName.setText(paramDPObject.getString("SubTitle") + "特权");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.CardChainShopItem
 * JD-Core Version:    0.6.0
 */