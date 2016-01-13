package com.dianping.hotel.deal.agent;

import android.os.Bundle;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.hotel.agent.HotelDealInfoOtherDealsAgent;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;

public class HotelProdOtherDealsAgent extends HotelDealInfoOtherDealsAgent
{
  protected int biztype;
  private DPObject dpHotelProdBase;
  protected int productId;

  public HotelProdOtherDealsAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void loadOtherDeals()
  {
    if ((this.otherDealsReq == null) && (!this.otherDealsLoaded))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://m.api.dianping.com/hoteltg/seeagaindealsgn.hoteltg");
      localStringBuilder.append("?cityid=").append(cityId());
      localStringBuilder.append("&dealid=").append(this.productId);
      localStringBuilder.append("&biztype=").append(this.biztype);
      String str = accountService().token();
      if (str != null)
        localStringBuilder.append("&token=").append(str);
      if (location() != null)
      {
        localStringBuilder.append("&lat=").append(location().latitude());
        localStringBuilder.append("&lng=").append(location().longitude());
      }
      this.otherDealsReq = mapiGet(this, localStringBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.otherDealsReq, this);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
    }
    while (this.dpHotelProdBase == null);
    this.productId = this.dpHotelProdBase.getInt("ProductId");
    this.biztype = this.dpHotelProdBase.getInt("BizType");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdOtherDealsAgent
 * JD-Core Version:    0.6.0
 */