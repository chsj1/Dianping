package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopNearRecommendItem;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class NearbyAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_NEARBY = "8000Nearby.70Nearby";
  private MApiRequest getNearRecommendRequest;
  private boolean isRecommendRetrieved;
  protected DPObject[] recommendShops;

  public NearbyAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void getNearRecommendRequest()
  {
    if (this.getNearRecommendRequest != null)
      return;
    String str2 = "http://m.api.dianping.com/recommendhotel.bin?shopid=" + shopId() + "&start=0&limit=3";
    String str1 = str2;
    if (getFragment().accountService() != null)
      str1 = str2 + "&token=" + getFragment().accountService().token();
    this.getNearRecommendRequest = BasicMApiRequest.mapiGet(str1, CacheType.NORMAL);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (NearbyAgent.this.getNearRecommendRequest != null)
          NearbyAgent.this.getFragment().mapiService().exec(NearbyAgent.this.getNearRecommendRequest, NearbyAgent.this);
      }
    }
    , 100L);
  }

  protected void configRecommend()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
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
            NearbyAgent.this.getFragment().startActivity(localIntent);
            NearbyAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelreco_content", "", 0);
          }
        }
      });
      i += 1;
    }
    localShopinfoCommonCell.setTitle("周边酒店", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NearbyAgent.this.startNearbyActivity(60, "酒店");
        new ArrayList().add(new BasicNameValuePair("shopid", NearbyAgent.this.shopId() + ""));
        NearbyAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelreco_title", "", 0);
      }
    });
    addCell("8000Nearby.70Nearby", localShopinfoCommonCell, 0);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((getShopStatus() == 0) && ((isHotelType()) || (isTravelType())))
    {
      if (this.isRecommendRetrieved)
        break label47;
      this.isRecommendRetrieved = true;
      getNearRecommendRequest();
    }
    label47: 
    do
      return;
    while ((this.recommendShops == null) || (this.recommendShops.length <= 0));
    configRecommend();
  }

  public void onDestroy()
  {
    if (this.getNearRecommendRequest != null)
    {
      getFragment().mapiService().abort(this.getNearRecommendRequest, this, true);
      this.getNearRecommendRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getNearRecommendRequest)
    {
      this.getNearRecommendRequest = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getNearRecommendRequest)
    {
      this.getNearRecommendRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.recommendShops = ((DPObject)paramMApiResponse.result()).getArray("List");
        dispatchAgentChanged(false);
      }
    }
  }

  protected void startNearbyActivity(int paramInt, String paramString)
  {
    DPObject localDPObject = getShop();
    if (localDPObject != null)
    {
      double d1 = localDPObject.getDouble("Latitude");
      double d2 = localDPObject.getDouble("Longitude");
      if ((d1 != 0.0D) || (d2 != 0.0D));
    }
    else
    {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("dianping://localshoplist");
    localStringBuilder.append("?shopid=").append(shopId());
    localStringBuilder.append("&shopname=").append(localDPObject.getString("Name"));
    localStringBuilder.append("&cityid=").append(localDPObject.getInt("CityID"));
    localStringBuilder.append("&shoplatitude=").append(localDPObject.getDouble("Latitude"));
    localStringBuilder.append("&shoplongitude=").append(localDPObject.getDouble("Longitude"));
    localStringBuilder.append("&categoryid=").append(paramInt);
    localStringBuilder.append("&categoryname=").append(paramString);
    getFragment().startActivity(localStringBuilder.toString());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.NearbyAgent
 * JD-Core Version:    0.6.0
 */