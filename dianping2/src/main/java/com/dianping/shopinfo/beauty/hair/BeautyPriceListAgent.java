package com.dianping.shopinfo.beauty.hair;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TicketCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.string;
import java.net.URLEncoder;

public class BeautyPriceListAgent extends ShopCellAgent
{
  private static final String BEAUTY_SHOP_BASIC_INFO = "beautyShopBasicInfo";
  private static final String CELL_PRICELIST = "0510BeautyCustom.200PriceList";
  private DPObject beautyHairShop;
  TicketCell priceListCell;

  public BeautyPriceListAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      return;
      this.beautyHairShop = ((DPObject)super.getSharedObject("beautyShopBasicInfo"));
      if ((this.beautyHairShop != null) || (paramBundle == null))
        continue;
      this.beautyHairShop = ((DPObject)paramBundle.getParcelable("beautyShopBasicInfo"));
    }
    while ((this.beautyHairShop == null) || (!this.beautyHairShop.getBoolean("HasPriceList")));
    if (this.priceListCell == null)
      this.priceListCell = createTicketCell();
    this.priceListCell.setIcon(this.res.getDrawable(R.drawable.detail_beauty_icon_pricelist));
    if (this.beautyHairShop.getBoolean("HasOfficialPic"))
      this.priceListCell.setTitle(getResources().getString(R.string.shopinfo_pricelist));
    while (true)
    {
      this.priceListCell.setMark(null, 0);
      this.priceListCell.setGAString("beautypricelist");
      addCell("0510BeautyCustom.200PriceList", this.priceListCell, 257);
      return;
      this.priceListCell.setTitle(getResources().getString(R.string.shopinfo_uploadpricelist));
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    if ((getShop() == null) || (this.beautyHairShop == null));
    do
    {
      return;
      paramString = this.beautyHairShop.getString("PriceListUrl");
    }
    while (paramString == null);
    try
    {
      paramView = URLEncoder.encode(paramString, "utf-8");
      paramString = paramView;
      label38: paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
      getContext().startActivity(paramString);
      return;
    }
    catch (java.io.UnsupportedEncodingException paramView)
    {
      break label38;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    if (!isBeautyHairType())
      return;
    super.onCreate(paramBundle);
    getShop();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.beauty.hair.BeautyPriceListAgent
 * JD-Core Version:    0.6.0
 */