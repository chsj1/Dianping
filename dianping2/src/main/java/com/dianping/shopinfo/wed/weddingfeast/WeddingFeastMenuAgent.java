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

public class WeddingFeastMenuAgent extends ShopCellAgent
  implements View.OnClickListener
{
  private static final String CELL_WEDDING_MENU = "0400Menu.0001";
  DPObject extra;

  public WeddingFeastMenuAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingType());
    int i;
    do
    {
      do
      {
        return;
        this.extra = ((DPObject)getSharedObject("WeddingHotelExtra"));
      }
      while (this.extra == null);
      i = this.extra.getInt("MenuCount");
    }
    while (i <= 0);
    paramBundle = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_weddingfeast_menu_cell, getParentView(), false);
    paramBundle.setGAString("wedmenu");
    ((TextView)paramBundle.findViewById(R.id.menu_count)).setText("共" + i + "套");
    paramBundle.setOnClickListener(this);
    addCell("0400Menu.0001", paramBundle);
  }

  public void onClick(View paramView)
  {
    paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelmenu").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(getShop())).build().toString()));
    paramView.putExtra("objShop", getShop());
    paramView.putExtra("shopId", shopId());
    paramView.putExtra("extra", this.extra);
    startActivity(paramView);
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastMenuAgent
 * JD-Core Version:    0.6.0
 */