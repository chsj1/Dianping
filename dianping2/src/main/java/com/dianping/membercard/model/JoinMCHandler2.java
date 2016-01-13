package com.dianping.membercard.model;

import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class JoinMCHandler2
{
  private static final String API_NAME_JOIN_TRANSFER = "jointransfer.mc";
  private static final String API_NAME_QUIT_MC = "quitmc.mc";
  private MApiRequest joinThirdPartyCardRequest;
  private NovaActivity mContext;
  private OnJoinThirdPartyCardHandlerListener onJoinThirdPartyCardHandlerListener;
  private OnQuitThirdPartyCardHandlerListener onQuitThirdPartyCardHandlerListener;
  private MApiRequest quitThirdPartyCardRequest;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == JoinMCHandler2.this.joinThirdPartyCardRequest)
        JoinMCHandler2.this.onJoinThirdPartyCardRequestFailed(paramMApiResponse);
      do
        return;
      while (paramMApiRequest != JoinMCHandler2.this.quitThirdPartyCardRequest);
      JoinMCHandler2.this.onQuitThirdPartyCardRequestFailed(paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == JoinMCHandler2.this.joinThirdPartyCardRequest)
        JoinMCHandler2.this.onJoinThirdPartyCardRequestFinish(paramMApiResponse);
      do
        return;
      while (paramMApiRequest != JoinMCHandler2.this.quitThirdPartyCardRequest);
      JoinMCHandler2.this.onQuitThirdPartyCardRequestFinish(paramMApiResponse);
    }
  };

  public JoinMCHandler2(NovaActivity paramNovaActivity)
  {
    if (paramNovaActivity == null)
      throw new IllegalArgumentException("context cannot be null");
    this.mContext = paramNovaActivity;
  }

  private void callbackOnJoinThirdPartyCardFailed(SimpleMsg paramSimpleMsg)
  {
    if (this.onJoinThirdPartyCardHandlerListener != null)
      this.onJoinThirdPartyCardHandlerListener.onRequestJoinThirdPartyCardFailed(paramSimpleMsg);
  }

  private void callbackOnJoinThirdPartyCardSuccessful(DPObject paramDPObject)
  {
    if (this.onJoinThirdPartyCardHandlerListener != null)
      this.onJoinThirdPartyCardHandlerListener.onRequestJoinThirdPartyCardSuccess(paramDPObject);
  }

  private void callbackOnQuitThirdPartyCardResult(boolean paramBoolean, SimpleMsg paramSimpleMsg)
  {
    if (this.onQuitThirdPartyCardHandlerListener != null)
      this.onQuitThirdPartyCardHandlerListener.onRequestQuitThirdPartyCardResult(paramBoolean, paramSimpleMsg);
  }

  private void execJoinThirdPartyCardTask(ArrayList<String> paramArrayList)
  {
    if (this.joinThirdPartyCardRequest != null)
    {
      this.mContext.mapiService().abort(this.joinThirdPartyCardRequest, this.requestHandler, true);
      this.joinThirdPartyCardRequest = null;
    }
    this.joinThirdPartyCardRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/jointransfer.mc", (String[])paramArrayList.toArray(new String[0]));
    this.mContext.mapiService().exec(this.joinThirdPartyCardRequest, this.requestHandler);
  }

  private boolean isJoinResponseDataValid(MApiResponse paramMApiResponse)
  {
    return (paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)) && (((DPObject)paramMApiResponse.result()).isClass("JoinTransferResult"));
  }

  private boolean isQuitCardRespondDataValid(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof DPObject)) && (((DPObject)paramObject).isClass("SimpleMsg"));
  }

  private void onJoinThirdPartyCardRequestFailed(MApiResponse paramMApiResponse)
  {
    callbackOnJoinThirdPartyCardFailed(paramMApiResponse.message());
  }

  private void onJoinThirdPartyCardRequestFinish(MApiResponse paramMApiResponse)
  {
    if (!isJoinResponseDataValid(paramMApiResponse))
    {
      callbackOnJoinThirdPartyCardFailed(new SimpleMsg("错误", "错误：数据异常", 0, 0));
      return;
    }
    callbackOnJoinThirdPartyCardSuccessful((DPObject)paramMApiResponse.result());
  }

  private void onQuitThirdPartyCardRequestFailed(MApiResponse paramMApiResponse)
  {
    callbackOnQuitThirdPartyCardResult(false, paramMApiResponse.message());
  }

  private void onQuitThirdPartyCardRequestFinish(MApiResponse paramMApiResponse)
  {
    if (!isQuitCardRespondDataValid(paramMApiResponse.result()))
    {
      callbackOnQuitThirdPartyCardResult(false, new SimpleMsg("错误", "错误：数据异常", 0, 0));
      return;
    }
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    callbackOnQuitThirdPartyCardResult(true, new SimpleMsg(paramMApiResponse.getString("Title"), paramMApiResponse.getString("Content"), paramMApiResponse.getInt("Icon"), paramMApiResponse.getInt("Flag")));
  }

  public void joinThirdPartyCards(String paramString, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cardids");
    localArrayList.add(paramString);
    if (this.mContext.getAccount() != null)
    {
      localArrayList.add("token");
      localArrayList.add(this.mContext.accountService().token());
    }
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    localArrayList.add("cityid");
    if (DPApplication.instance().locationService().city() == null);
    for (int i = 0; ; i = DPApplication.instance().locationService().city().getInt("ID"))
    {
      localArrayList.add(String.valueOf(i));
      paramString = this.mContext.location();
      if (paramString != null)
      {
        DecimalFormat localDecimalFormat = Location.FMT;
        localArrayList.add("lat");
        localArrayList.add(localDecimalFormat.format(paramString.latitude()));
        localArrayList.add("lng");
        localArrayList.add(localDecimalFormat.format(paramString.longitude()));
      }
      localArrayList.add("source");
      localArrayList.add(String.valueOf(paramInt));
      execJoinThirdPartyCardTask(localArrayList);
      return;
    }
  }

  public void quitThirdPartyCard(String paramString)
  {
    if (this.quitThirdPartyCardRequest != null)
    {
      this.mContext.mapiService().abort(this.quitThirdPartyCardRequest, this.requestHandler, true);
      this.quitThirdPartyCardRequest = null;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("membercardid");
    localArrayList.add(String.valueOf(paramString));
    if (this.mContext.getAccount() != null)
    {
      localArrayList.add("token");
      localArrayList.add(this.mContext.getAccount().token());
    }
    this.quitThirdPartyCardRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/quitmc.mc", (String[])localArrayList.toArray(new String[0]));
    this.mContext.mapiService().exec(this.quitThirdPartyCardRequest, this.requestHandler);
  }

  public void setJoinThirdPartyCardHandlerListener(OnJoinThirdPartyCardHandlerListener paramOnJoinThirdPartyCardHandlerListener)
  {
    this.onJoinThirdPartyCardHandlerListener = paramOnJoinThirdPartyCardHandlerListener;
  }

  public void setOnQuitThirdPartyCardHandlerListener(OnQuitThirdPartyCardHandlerListener paramOnQuitThirdPartyCardHandlerListener)
  {
    this.onQuitThirdPartyCardHandlerListener = paramOnQuitThirdPartyCardHandlerListener;
  }

  public void stopAllRequest()
  {
    stopJoinThirdPartyCards();
    stopQuitThirdPartyCard();
  }

  public void stopJoinThirdPartyCards()
  {
    if (this.joinThirdPartyCardRequest != null)
    {
      this.mContext.mapiService().abort(this.joinThirdPartyCardRequest, this.requestHandler, true);
      this.joinThirdPartyCardRequest = null;
    }
  }

  public void stopQuitThirdPartyCard()
  {
    if (this.quitThirdPartyCardRequest != null)
    {
      this.mContext.mapiService().abort(this.quitThirdPartyCardRequest, this.requestHandler, true);
      this.quitThirdPartyCardRequest = null;
    }
  }

  public static abstract interface OnJoinThirdPartyCardHandlerListener
  {
    public abstract void onRequestJoinThirdPartyCardFailed(SimpleMsg paramSimpleMsg);

    public abstract void onRequestJoinThirdPartyCardSuccess(DPObject paramDPObject);
  }

  public static abstract interface OnQuitThirdPartyCardHandlerListener
  {
    public abstract void onRequestQuitThirdPartyCardResult(boolean paramBoolean, SimpleMsg paramSimpleMsg);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.model.JoinMCHandler2
 * JD-Core Version:    0.6.0
 */