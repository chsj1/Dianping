package com.dianping.shopinfo.hotel;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.shopinfo.common.FavorShopAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.DeviceUtils;

public class HotelFavorShopAgent extends FavorShopAgent
{
  public HotelFavorShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cpmAddsRequest)
    {
      Object localObject = (DPObject)paramMApiResponse.result();
      if ((((DPObject)localObject).getArray("SpecialAdList") != null) && (((DPObject)localObject).getArray("SpecialAdList") != null) && (((DPObject)localObject).getArray("SpecialAdList").length > 0))
      {
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("SpecialAd", localObject.getArray("SpecialAdList")[0]);
        localObject = new AgentMessage("com.dianping.shopinfo.hotel.HotelFavorShopAgent.HOTEL_FAVOR_SHOP_LOAD_DATA_NEW_CPMADS");
        ((AgentMessage)localObject).body = localBundle;
        dispatchMessage((AgentMessage)localObject);
      }
    }
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
  }

  protected void reqCpmAds()
  {
    if (this.cpmAddsRequest != null)
      getFragment().mapiService().abort(this.cpmAddsRequest, this, true);
    if ((getFragment() == null) || (getShop() == null))
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/hotel/newcpmads.hotel").buildUpon().appendQueryParameter("cityId", String.valueOf(cityId())).appendQueryParameter("viewShopId", String.valueOf(shopId())).appendQueryParameter("shopType", String.valueOf(getShop().getInt("ShopType"))).appendQueryParameter("categoryId", String.valueOf(getShop().getInt("CategoryID"))).appendQueryParameter("categoryName", getShop().getString("CategoryName")).appendQueryParameter("slotId", String.valueOf(10004)).appendQueryParameter("dpId", DeviceUtils.dpid()).appendQueryParameter("shopCityId", String.valueOf(getShop().getInt("CityID")));
    if (location() != null)
      localBuilder.appendQueryParameter("longitude", String.valueOf(location().longitude())).appendQueryParameter("latitude", String.valueOf(location().latitude()));
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localBuilder.appendQueryParameter("token", str);
    this.cpmAddsRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.cpmAddsRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelFavorShopAgent
 * JD-Core Version:    0.6.0
 */