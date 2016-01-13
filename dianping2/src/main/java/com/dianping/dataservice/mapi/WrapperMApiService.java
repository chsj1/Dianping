package com.dianping.dataservice.mapi;

import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract class WrapperMApiService
  implements MApiService, FullRequestHandle<MApiRequest, MApiResponse>
{
  private HashMap<MApiRequest, RequestHandler<MApiRequest, MApiResponse>> running;
  private MApiService service;

  public WrapperMApiService(MApiService paramMApiService)
  {
    this.service = paramMApiService;
    this.running = new HashMap();
  }

  public void abort(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, boolean paramBoolean)
  {
    paramRequestHandler = this.running.entrySet().iterator();
    while (paramRequestHandler.hasNext())
    {
      WrapperMApiRequest localWrapperMApiRequest = (WrapperMApiRequest)((Map.Entry)paramRequestHandler.next()).getKey();
      if (localWrapperMApiRequest.wrapped() != paramMApiRequest)
        continue;
      this.service.abort(localWrapperMApiRequest, this, paramBoolean);
      paramRequestHandler.remove();
    }
  }

  public void exec(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    paramMApiRequest = wrap(paramMApiRequest);
    this.running.put(paramMApiRequest, paramRequestHandler);
    this.service.exec(paramMApiRequest, this);
  }

  public MApiResponse execSync(MApiRequest paramMApiRequest)
  {
    return (MApiResponse)this.service.execSync(wrap(paramMApiRequest));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.remove(paramMApiRequest);
    if (localRequestHandler != null)
      localRequestHandler.onRequestFailed(((WrapperMApiRequest)paramMApiRequest).wrapped(), paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.remove(paramMApiRequest);
    if (localRequestHandler != null)
      localRequestHandler.onRequestFinish(((WrapperMApiRequest)paramMApiRequest).wrapped(), paramMApiResponse);
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.get(paramMApiRequest);
    if ((localRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)localRequestHandler).onRequestProgress(((WrapperMApiRequest)paramMApiRequest).wrapped(), paramInt1, paramInt2);
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.get(paramMApiRequest);
    if ((localRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)localRequestHandler).onRequestStart(((WrapperMApiRequest)paramMApiRequest).wrapped());
  }

  public abstract WrapperMApiRequest wrap(MApiRequest paramMApiRequest);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.WrapperMApiService
 * JD-Core Version:    0.6.0
 */