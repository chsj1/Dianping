package com.dianping.shopinfo.oversea;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.hotel.senic.BaseScenicTicketAgent;

public class ForeignScenicTicketAgent extends BaseScenicTicketAgent
{
  public ForeignScenicTicketAgent(Object paramObject)
  {
    super(paramObject);
  }

  public DPObject getData(Bundle paramBundle)
  {
    return null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    sendRequest();
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
    {
      this.request = null;
      this.GeneralTicketGroupList = ((DPObject[])(DPObject[])paramResponse.result());
    }
    dispatchAgentChanged(false);
  }

  public void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/scenicticket.overseas").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    this.request = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.oversea.ForeignScenicTicketAgent
 * JD-Core Version:    0.6.0
 */