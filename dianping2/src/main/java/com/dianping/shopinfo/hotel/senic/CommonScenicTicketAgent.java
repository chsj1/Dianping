package com.dianping.shopinfo.hotel.senic;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class CommonScenicTicketAgent extends BaseScenicTicketAgent
{
  public CommonScenicTicketAgent(Object paramObject)
  {
    super(paramObject);
  }

  public DPObject getData(Bundle paramBundle)
  {
    return this.shopTicket;
  }

  public void onCreate(Bundle paramBundle)
  {
    sendRequest();
  }

  public void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/scenic/ticket.scenic").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.request = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.CommonScenicTicketAgent
 * JD-Core Version:    0.6.0
 */