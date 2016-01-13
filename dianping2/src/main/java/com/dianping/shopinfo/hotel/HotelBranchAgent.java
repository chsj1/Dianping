package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;

public class HotelBranchAgent extends ShopCellAgent
{
  public HotelBranchAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupView()
  {
    Object localObject = getShop();
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    int i;
    if ((((DPObject)localObject).getInt("BranchCounts") > 0) || ((((DPObject)localObject).getString("BranchIDs") != null) && (((DPObject)localObject).getString("BranchIDs").length() > 0)))
    {
      if (((DPObject)localObject).getInt("BranchCounts") <= 0)
        break label153;
      i = ((DPObject)localObject).getInt("BranchCounts");
      if (i <= 0)
        break label215;
      localObject = ((DPObject)localObject).getString("Name");
      if ((localObject == null) || (((String)localObject).equals("")))
        break label183;
      localShopinfoCommonCell.setTitle((String)localObject + "-" + i + "家分店");
    }
    while (true)
    {
      localShopinfoCommonCell.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = HotelBranchAgent.this.getShop();
          if (paramView == null)
            return;
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + HotelBranchAgent.this.shopId()));
          localIntent.putExtra("showAddBranchShop", true);
          localIntent.putExtra("shop", paramView);
          HotelBranchAgent.this.getFragment().startActivity(localIntent);
          HotelBranchAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelgroup", "" + HotelBranchAgent.this.shopId(), 0);
        }
      });
      addCell("", localShopinfoCommonCell);
      return;
      label153: String str = ((DPObject)localObject).getString("BranchIDs");
      i = str.length() - str.replace(",", "").length() + 1;
      break;
      label183: localShopinfoCommonCell.setTitle("其他" + i + "家分店");
      continue;
      label215: localShopinfoCommonCell.setTitle("其他分店");
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (!isHotelType());
    do
    {
      return;
      super.onAgentChanged(paramBundle);
      removeAllCells();
    }
    while (getShop() == null);
    setupView();
  }

  public void onCreate(Bundle paramBundle)
  {
    if (!isHotelType())
      return;
    super.onCreate(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelBranchAgent
 * JD-Core Version:    0.6.0
 */