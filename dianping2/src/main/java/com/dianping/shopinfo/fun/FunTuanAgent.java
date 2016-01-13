package com.dianping.shopinfo.fun;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.shopinfo.common.ShopInfoTuanAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class FunTuanAgent extends ShopInfoTuanAgent
{
  protected DPObject funDeals;
  protected MApiRequest mDealsRequest;

  public FunTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected DPObject getDeals()
  {
    return this.funDeals;
  }

  protected int getDisplayCount()
  {
    return 3;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (!this.hasRequested)
    {
      if (this.mDealsRequest != null)
        getFragment().mapiService().abort(this.mDealsRequest, this, true);
      paramBundle = new StringBuilder();
      paramBundle.append("http://m.api.dianping.com/fun/getshopdeallist.bin");
      paramBundle.append("?shopid=").append(shopId());
      paramBundle.append("&cityid=").append(cityId());
      Object localObject = accountService().token();
      if (!TextUtils.isEmpty((CharSequence)localObject))
        paramBundle.append("&token=").append((String)localObject);
      localObject = location();
      if (localObject != null)
      {
        paramBundle.append("&lat=").append(((Location)localObject).latitude());
        paramBundle.append("&lng=").append(((Location)localObject).longitude());
      }
      this.mDealsRequest = BasicMApiRequest.mapiGet(paramBundle.toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.mDealsRequest, new FunDealRequestHandler(null));
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

  private class FunDealRequestHandler
    implements RequestHandler<MApiRequest, MApiResponse>
  {
    private FunDealRequestHandler()
    {
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiRequest != null) && (paramMApiRequest == FunTuanAgent.this.mDealsRequest))
      {
        FunTuanAgent.access$1102(FunTuanAgent.this, true);
        FunTuanAgent.this.mDealsRequest = null;
        FunTuanAgent.this.getParentView().post(new Runnable()
        {
          public void run()
          {
            FunTuanAgent.this.dispatchAgentChanged(false);
          }
        });
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == FunTuanAgent.this.mPromoTagRequest)
      {
        FunTuanAgent.access$202(FunTuanAgent.this, null);
        FunTuanAgent.access$302(FunTuanAgent.this, true);
        FunTuanAgent.access$402(FunTuanAgent.this, (DPObject)paramMApiResponse.result());
        FunTuanAgent.this.updateTuanDealTags();
      }
      do
        return;
      while (paramMApiRequest != FunTuanAgent.this.mDealsRequest);
      FunTuanAgent.access$602(FunTuanAgent.this, true);
      FunTuanAgent.this.mDealsRequest = null;
      FunTuanAgent.this.funDeals = ((DPObject)paramMApiResponse.result());
      FunTuanAgent.this.getParentView().post(new Runnable()
      {
        public void run()
        {
          FunTuanAgent.this.dispatchAgentChanged(false);
          if (!FunTuanAgent.this.hasPromoRequested)
          {
            if (FunTuanAgent.this.mPromoTagRequest != null)
              FunTuanAgent.this.getFragment().mapiService().abort(FunTuanAgent.this.mPromoTagRequest, FunTuanAgent.this, true);
            FunTuanAgent.this.requestPromoTag();
          }
        }
      });
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.FunTuanAgent
 * JD-Core Version:    0.6.0
 */