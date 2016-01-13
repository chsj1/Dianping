package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;

public class WedBanquetAgent extends ShopCellAgent
{
  private static final String CELL_WEDBANQUET = "0500Cash.80WedBanquet";
  private static final String CELL_WEDBANQUET_HOTEL = "7780Cash.80WedBanquet";
  CommonCell cell;

  public WedBanquetAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean isHotel(DPObject paramDPObject)
  {
    return (paramDPObject != null) && (paramDPObject.getInt("ShopType") == 60);
  }

  public CommonCell createBanquetCell()
  {
    CommonCell localCommonCell = super.createCommonCell();
    localCommonCell.setMinimumHeight((int)getResources().getDimension(R.dimen.shopinfo_common_cell_height));
    localCommonCell.setGAString("banquet", getGAExtra());
    return localCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if (paramBundle == null);
    DPObject localDPObject;
    do
    {
      return;
      localDPObject = paramBundle.getObject("FeastInfo");
    }
    while ((localDPObject == null) || (android.text.TextUtils.isEmpty(localDPObject.getString("Title"))));
    if (this.cell == null)
      this.cell = createBanquetCell();
    this.cell.setLeftIcon(R.drawable.shopinfo_detail_banquet);
    this.cell.setTitle(localDPObject.getString("Title"));
    this.cell.setRightText(com.dianping.util.TextUtils.jsonParseText(localDPObject.getString("SubTitle")));
    this.cell.setTag(localDPObject);
    if (isHotel(paramBundle))
    {
      addCell("7780Cash.80WedBanquet", this.cell, 257);
      return;
    }
    addCell("0500Cash.80WedBanquet", this.cell, 257);
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if (getShop() == null);
    do
    {
      do
        return;
      while (!(paramView.getTag() instanceof DPObject));
      paramString = (DPObject)paramView.getTag();
    }
    while (android.text.TextUtils.isEmpty(paramString.getString("Scheme")));
    getFragment().startActivity(paramString.getString("Scheme"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.WedBanquetAgent
 * JD-Core Version:    0.6.0
 */