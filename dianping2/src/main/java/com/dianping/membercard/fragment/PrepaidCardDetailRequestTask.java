package com.dianping.membercard.fragment;

import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.utils.ContextWrapper;
import com.dianping.util.Log;

public class PrepaidCardDetailRequestTask
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private int accountId = -1;
  private ContextWrapper context;
  private volatile Boolean dataIsHandled = Boolean.valueOf(false);
  private volatile MApiRequest mGetCardInfoRequst;
  private int memberCardId = -1;
  Handler timeHandler;
  Runnable timeRunnable;

  public PrepaidCardDetailRequestTask(Object paramObject)
  {
    this.context = new ContextWrapper(paramObject);
  }

  private boolean containsRequestHandler()
  {
    return (this.context.getOrignalContext() != null) && ((this.context.getOrignalContext() instanceof PrepaidCardDetailRequestHandler));
  }

  private PrepaidCardDetailRequestHandler getRequestHandler()
  {
    return (PrepaidCardDetailRequestHandler)this.context.getOrignalContext();
  }

  private boolean hasCardDetailCache(int paramInt)
  {
    return DPCache.getInstance().getParcelable("mc/card/detail/" + paramInt, null, 31539600000L, DPObject.CREATOR) != null;
  }

  private void useCardDetailCache(int paramInt)
  {
    if (!this.dataIsHandled.booleanValue())
    {
      DPObject localDPObject = (DPObject)DPCache.getInstance().getParcelable("mc/card/detail/" + paramInt, null, 31539600000L, DPObject.CREATOR);
      if ((localDPObject != null) && (containsRequestHandler()))
      {
        getRequestHandler().onCardDetailRequestFinish(localDPObject, ResponseDataType.CACHE_CARD_INFO);
        this.dataIsHandled = Boolean.valueOf(true);
      }
    }
  }

  public void abortRequest()
  {
    if (this.mGetCardInfoRequst != null)
    {
      Log.v("CardRequest", "really_abort");
      this.context.getActivity().mapiService().abort(this.mGetCardInfoRequst, this, true);
      this.mGetCardInfoRequst = null;
    }
    if ((this.timeHandler != null) && (this.timeRunnable != null))
    {
      Log.v("CardRequest", "really_time");
      this.timeHandler.removeCallbacks(this.timeRunnable);
      this.timeHandler = null;
      this.timeRunnable = null;
    }
  }

  public void doRequest(int paramInt1, int paramInt2)
  {
    abortRequest();
    this.dataIsHandled = Boolean.valueOf(false);
    this.memberCardId = paramInt1;
    this.accountId = paramInt2;
    StringBuilder localStringBuilder = new StringBuilder("http://app.t.dianping.com/myprepaidcarddetailgn.bin?prepaidcardid=");
    localStringBuilder.append(paramInt1);
    if (this.context.getActivity().accountService().token() != null)
    {
      localStringBuilder.append("&token=");
      localStringBuilder.append(this.context.getActivity().accountService().token());
    }
    localStringBuilder.append("&accountid=");
    localStringBuilder.append(paramInt2);
    DisplayMetrics localDisplayMetrics = this.context.getActivity().getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(localDisplayMetrics.widthPixels);
    this.mGetCardInfoRequst = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    this.context.getActivity().mapiService().exec(this.mGetCardInfoRequst, this);
    this.timeHandler = new Handler();
    this.timeRunnable = new Runnable(paramInt1)
    {
      public void run()
      {
        synchronized (PrepaidCardDetailRequestTask.this.dataIsHandled)
        {
          PrepaidCardDetailRequestTask.this.useCardDetailCache(this.val$memberCardId);
          return;
        }
      }
    };
    this.timeHandler.postDelayed(this.timeRunnable, 5000L);
  }

  public void load(Object paramObject)
  {
    if ((this.context != null) && (this.mGetCardInfoRequst != null))
      abortRequest();
    this.context = new ContextWrapper(paramObject);
  }

  public void onRequestFailed(MApiRequest arg1, MApiResponse paramMApiResponse)
  {
    this.mGetCardInfoRequst = null;
    synchronized (this.dataIsHandled)
    {
      if ((!this.dataIsHandled.booleanValue()) && (hasCardDetailCache(this.memberCardId)))
        useCardDetailCache(this.memberCardId);
      do
      {
        this.dataIsHandled = Boolean.valueOf(true);
        return;
      }
      while (!containsRequestHandler());
      getRequestHandler().onCardDetailRequestFailed(paramMApiResponse, this.memberCardId, this.accountId);
    }
  }

  public void onRequestFinish(MApiRequest arg1, MApiResponse paramMApiResponse)
  {
    synchronized (this.dataIsHandled)
    {
      this.mGetCardInfoRequst = null;
      this.dataIsHandled = Boolean.valueOf(true);
      if ((paramMApiResponse == null) || (paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject)))
        return;
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      DPCache.getInstance().put("mc/card/detail/" + paramMApiResponse.getInt("MemberCardID"), null, paramMApiResponse, 31539600000L);
      if ((paramMApiResponse != null) && (containsRequestHandler()))
        getRequestHandler().onCardDetailRequestFinish(paramMApiResponse, ResponseDataType.CURRENT_CARD_INFO);
      return;
    }
  }

  public static abstract interface PrepaidCardDetailRequestHandler
  {
    public abstract void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt1, int paramInt2);

    public abstract void onCardDetailRequestFinish(DPObject paramDPObject, PrepaidCardDetailRequestTask.ResponseDataType paramResponseDataType);
  }

  public static enum ResponseDataType
  {
    static
    {
      $VALUES = new ResponseDataType[] { CACHE_CARD_INFO, CURRENT_CARD_INFO };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.PrepaidCardDetailRequestTask
 * JD-Core Version:    0.6.0
 */