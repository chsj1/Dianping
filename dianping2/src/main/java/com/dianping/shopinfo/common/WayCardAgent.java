package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;

public class WayCardAgent extends ShopCellAgent
{
  public static final String CELL_WAY = "0300Basic_20way";
  private DPObject addressCard;
  private CommonCell wayCardCell;

  public WayCardAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    if ((paramBundle == null) || (getShopStatus() != 0) || (paramBundle.getObject("AddressCard") == null))
    {
      removeAllCells();
      return;
    }
    this.addressCard = paramBundle.getObject("AddressCard");
    if (this.wayCardCell == null)
      this.wayCardCell = createCommonCell();
    this.wayCardCell.setLeftIcon(R.drawable.icon_ask);
    this.wayCardCell.setTitleMaxLines(1);
    if (!TextUtils.isEmpty(this.addressCard.getString("Title")))
      this.wayCardCell.setTitle(this.addressCard.getString("Title"));
    while (true)
    {
      this.wayCardCell.setGAString("guardcard");
      addCell("0300Basic_20way", this.wayCardCell, 257);
      return;
      this.wayCardCell.setTitle("问路卡");
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://waycard"));
    paramString.putExtra("addressCard", this.addressCard);
    startActivity(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.WayCardAgent
 * JD-Core Version:    0.6.0
 */