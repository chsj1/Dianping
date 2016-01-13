package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopNearRecommendItem;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.common.NearbyAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelNearbyAgent extends NearbyAgent
{
  public HotelNearbyAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void configRecommend()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setTitle("附近同类型酒店", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelNearbyAgent.this.startNearbyActivity(60, "酒店");
        new ArrayList().add(new BasicNameValuePair("shopid", HotelNearbyAgent.this.shopId() + ""));
        HotelNearbyAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelreco_title", "", 0);
      }
    });
    if ((this.recommendShops != null) && (this.recommendShops.length > 0))
    {
      int i = 0;
      while (i < this.recommendShops.length)
      {
        DPObject localDPObject = this.recommendShops[i];
        ShopNearRecommendItem localShopNearRecommendItem = (ShopNearRecommendItem)this.res.inflate(getContext(), R.layout.item_of_shoptravel_near, null, false);
        localShopNearRecommendItem.setGroupon(localDPObject, String.valueOf(shopId()));
        localShopNearRecommendItem.setTag(2147483647, localDPObject);
        localShopinfoCommonCell.addContent(localShopNearRecommendItem, true, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = (DPObject)paramView.getTag(2147483647);
            if (paramView != null)
            {
              Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + paramView.getInt("ID")));
              localIntent.putExtra("shop", paramView);
              HotelNearbyAgent.this.getFragment().startActivity(localIntent);
              HotelNearbyAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelreco_content", "", 0);
            }
          }
        });
        i += 1;
      }
    }
    addCell("8000Nearby.70Nearby", localShopinfoCommonCell, 0);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShopStatus() == 0) && ((isHotelType()) || (isTravelType())))
      configRecommend();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelNearbyAgent
 * JD-Core Version:    0.6.0
 */