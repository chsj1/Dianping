package com.dianping.shopinfo.hotel;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelFoodNearbyAgent extends ShopCellAgent
{
  public HotelFoodNearbyAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    setupView();
  }

  public void setupView()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setTitle("附近美食推荐", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        new ArrayList().add(new BasicNameValuePair("shopid", HotelFoodNearbyAgent.this.shopId() + ""));
        HotelFoodNearbyAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelnearby", "" + HotelFoodNearbyAgent.this.shopId(), 0);
        paramView = HotelFoodNearbyAgent.this.getShop();
        if (paramView != null)
        {
          double d1 = paramView.getDouble("Latitude");
          double d2 = paramView.getDouble("Longitude");
          if ((d1 != 0.0D) || (d2 != 0.0D));
        }
        else
        {
          return;
        }
        StringBuilder localStringBuilder = new StringBuilder("dianping://nearbyshoplist");
        localStringBuilder.append("?shopid=").append(HotelFoodNearbyAgent.this.shopId());
        localStringBuilder.append("&shopname=").append(paramView.getString("Name"));
        localStringBuilder.append("&cityid=").append(paramView.getInt("CityID"));
        localStringBuilder.append("&shoplatitude=").append(paramView.getDouble("Latitude"));
        localStringBuilder.append("&shoplongitude=").append(paramView.getDouble("Longitude"));
        localStringBuilder.append("&categoryid=").append(10);
        localStringBuilder.append("&category=").append("附近");
        HotelFoodNearbyAgent.this.getFragment().startActivity(localStringBuilder.toString());
      }
    });
    addCell("", localShopinfoCommonCell);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelFoodNearbyAgent
 * JD-Core Version:    0.6.0
 */