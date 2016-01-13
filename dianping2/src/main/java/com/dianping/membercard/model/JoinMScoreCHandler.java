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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class JoinMScoreCHandler
{
  private static final String API_NAME_JOIN_TRANSFER = "jointransfer.mc";
  private static final int RESPOND_CODE_FAILED = 500;
  private static final int RESPOND_CODE_NEED_OPEN_WEBPAGE = 201;
  private static final int RESPOND_CODE_SUCCEED = 200;
  private NovaActivity mContext;
  private MApiRequest mJoinRequest;
  private OnJoinScoreCardHandlerListener onJoinScoreCardHandlerListener;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      JoinMScoreCHandler.this.onJoinMSCRequestFailed(paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      JoinMScoreCHandler.this.onJoinMSCRequestFinish(paramMApiRequest, paramMApiResponse);
    }
  };

  public JoinMScoreCHandler(NovaActivity paramNovaActivity)
  {
    if (paramNovaActivity == null)
      throw new IllegalArgumentException("context cannot be null");
    this.mContext = paramNovaActivity;
  }

  private void callbackOnJoinScoreCardFail(String paramString)
  {
    if (this.onJoinScoreCardHandlerListener != null)
      this.onJoinScoreCardHandlerListener.onJoinScoreCardFail(paramString);
  }

  private void callbackOnJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2)
  {
    if (this.onJoinScoreCardHandlerListener != null)
      this.onJoinScoreCardHandlerListener.onJoinScoreCardFailForNeedMemberInfo(paramString1, paramString2);
  }

  private void callbackOnJoinScoreCardSuccess()
  {
    if (this.onJoinScoreCardHandlerListener != null)
      this.onJoinScoreCardHandlerListener.onJoinScoreCardSuccess();
  }

  private void execJoinMSCTask(ArrayList<String> paramArrayList)
  {
    if (this.mJoinRequest != null)
    {
      this.mContext.mapiService().abort(this.mJoinRequest, this.requestHandler, true);
      this.mJoinRequest = null;
    }
    this.mJoinRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/jointransfer.mc", (String[])paramArrayList.toArray(new String[0]));
    this.mContext.mapiService().exec(this.mJoinRequest, this.requestHandler);
  }

  private boolean isJoinResponseDataValid(MApiResponse paramMApiResponse)
  {
    return (paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)) && (((DPObject)paramMApiResponse.result()).isClass("JoinTransferResult"));
  }

  private void onJoinMSCRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    callbackOnJoinScoreCardFail(paramMApiResponse.message().toString());
  }

  private void onJoinMSCRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (!isJoinResponseDataValid(paramMApiResponse))
    {
      callbackOnJoinScoreCardFail("错误：数据异常");
      return;
    }
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    int i = paramMApiRequest.getInt("Code");
    paramMApiResponse = paramMApiRequest.getString("Msg");
    if (i == 200)
    {
      callbackOnJoinScoreCardSuccess();
      return;
    }
    if (i == 201)
    {
      callbackOnJoinScoreCardFailForNeedMemberInfo(paramMApiResponse, paramMApiRequest.getString("RedirectUrl"));
      return;
    }
    callbackOnJoinScoreCardFail(paramMApiResponse);
  }

  public void joinScoreCards(String paramString, int paramInt)
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
      execJoinMSCTask(localArrayList);
      return;
    }
  }

  public void setJoinScoreCardHandlerListener(OnJoinScoreCardHandlerListener paramOnJoinScoreCardHandlerListener)
  {
    this.onJoinScoreCardHandlerListener = paramOnJoinScoreCardHandlerListener;
  }

  public static abstract interface OnJoinScoreCardHandlerListener
  {
    public abstract void onJoinScoreCardFail(String paramString);

    public abstract void onJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2);

    public abstract void onJoinScoreCardSuccess();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.model.JoinMScoreCHandler
 * JD-Core Version:    0.6.0
 */