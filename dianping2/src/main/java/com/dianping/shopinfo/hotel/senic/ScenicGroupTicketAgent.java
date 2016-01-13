package com.dianping.shopinfo.hotel.senic;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.common.ShopInfoTuanAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class ScenicGroupTicketAgent extends ShopInfoTuanAgent
{
  protected MApiRequest mDealsRequest;
  protected DPObject scenicDealList;

  public ScenicGroupTicketAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected DPObject getDeals()
  {
    return this.scenicDealList;
  }

  protected int getDisplayCount()
  {
    return 3;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (!this.hasRequested)
    {
      paramBundle = Uri.parse("http://m.api.dianping.com/scenic/ticketcollection.scenic").buildUpon();
      paramBundle.appendQueryParameter("shopid", String.valueOf(shopId()));
      paramBundle.appendQueryParameter("cityid", String.valueOf(cityId()));
      this.mDealsRequest = mapiGet(this, paramBundle.toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.mDealsRequest, new ScenicGroupTicketRequestHandler(null));
      return;
    }
    setupView();
  }

  public void onDestroy()
  {
    if (this.mDealsRequest != null)
    {
      getFragment().mapiService().abort(this.mDealsRequest, this, true);
      this.mDealsRequest = null;
    }
    super.onDestroy();
  }

  private class ScenicGroupTicketRequestHandler
    implements RequestHandler<MApiRequest, MApiResponse>
  {
    private ScenicGroupTicketRequestHandler()
    {
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiRequest != null) && (paramMApiRequest == ScenicGroupTicketAgent.this.mDealsRequest))
      {
        ScenicGroupTicketAgent.access$1102(ScenicGroupTicketAgent.this, true);
        ScenicGroupTicketAgent.this.mDealsRequest = null;
        ScenicGroupTicketAgent.this.getParentView().post(new Runnable()
        {
          public void run()
          {
            ScenicGroupTicketAgent.this.dispatchAgentChanged(false);
          }
        });
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == ScenicGroupTicketAgent.this.mPromoTagRequest)
      {
        ScenicGroupTicketAgent.access$202(ScenicGroupTicketAgent.this, null);
        ScenicGroupTicketAgent.access$302(ScenicGroupTicketAgent.this, true);
        ScenicGroupTicketAgent.access$402(ScenicGroupTicketAgent.this, (DPObject)paramMApiResponse.result());
        ScenicGroupTicketAgent.this.updateTuanDealTags();
      }
      do
        return;
      while (paramMApiRequest != ScenicGroupTicketAgent.this.mDealsRequest);
      ScenicGroupTicketAgent.access$602(ScenicGroupTicketAgent.this, true);
      ScenicGroupTicketAgent.this.mDealsRequest = null;
      ScenicGroupTicketAgent.this.scenicDealList = ((DPObject)paramMApiResponse.result());
      ScenicGroupTicketAgent.this.getParentView().post(new Runnable()
      {
        public void run()
        {
          ScenicGroupTicketAgent.this.dispatchAgentChanged(false);
          if (!ScenicGroupTicketAgent.this.hasPromoRequested)
          {
            if (ScenicGroupTicketAgent.this.mPromoTagRequest != null)
              ScenicGroupTicketAgent.this.getFragment().mapiService().abort(ScenicGroupTicketAgent.this.mPromoTagRequest, ScenicGroupTicketAgent.this, true);
            ScenicGroupTicketAgent.this.requestPromoTag();
          }
        }
      });
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.ScenicGroupTicketAgent
 * JD-Core Version:    0.6.0
 */