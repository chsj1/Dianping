package com.dianping.shopinfo.community;

import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.ShopInfoServiceView;
import com.dianping.shopinfo.widget.ShopInfoServiceView.ShopServiceItemInfo;
import com.dianping.v1.R.drawable;
import java.util.ArrayList;

public class SurroundingSeviceAgent extends ShopCellAgent
{
  private String CELL_INDEX = "0200Basic.50Sevice";

  public SurroundingSeviceAgent(Object paramObject)
  {
    super(paramObject);
  }

  public int getPicResByType(int paramInt)
  {
    switch (paramInt)
    {
    case 3:
    case 4:
    case 5:
    default:
      return 0;
    case 8:
      return R.drawable.detail_service_icon_housework;
    case 7:
      return R.drawable.detail_service_icon_secondhandhouse;
    case 6:
      return R.drawable.detail_service_icon_renthouse;
    case 2:
    }
    return R.drawable.detail_service_icon_takeaway;
  }

  public ArrayList<ShopInfoServiceView.ShopServiceItemInfo> initializeDate(DPObject paramDPObject, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    paramDPObject = paramDPObject.getArray(paramString);
    if (paramDPObject != null)
    {
      int i = 0;
      while (i < paramDPObject.length)
      {
        paramString = new ShopInfoServiceView.ShopServiceItemInfo();
        Object localObject = paramDPObject[i];
        if (localObject != null)
        {
          paramString.type = localObject.getInt("Type");
          paramString.promoInfo = localObject.getString("PromoInfo");
          paramString.title = localObject.getString("Title");
          paramString.scheme = localObject.getString("Scheme");
          paramString.extraInfo = localObject.getString("ExtraInfo");
          paramString.picResId = getPicResByType(localObject.getInt("Type"));
          localArrayList.add(paramString);
        }
        i += 1;
      }
    }
    return localArrayList;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (super.getShop() == null);
    ShopInfoServiceView localShopInfoServiceView;
    do
    {
      do
        return;
      while (super.getShopStatus() != 0);
      paramBundle = initializeDate(getShop(), "CommuntiyService");
      localShopInfoServiceView = new ShopInfoServiceView(getContext());
      localShopInfoServiceView.setData(paramBundle);
    }
    while (localShopInfoServiceView.getItemCount() <= 0);
    super.addCell(this.CELL_INDEX, localShopInfoServiceView, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.community.SurroundingSeviceAgent
 * JD-Core Version:    0.6.0
 */