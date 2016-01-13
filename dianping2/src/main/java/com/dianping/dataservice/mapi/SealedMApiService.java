package com.dianping.dataservice.mapi;

import android.content.Context;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.util.Log;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SealedMApiService
  implements MApiService, FullRequestHandle<MApiRequest, MApiResponse>
{
  private Context host;
  private ConcurrentHashMap<MApiRequest, RequestHandler<MApiRequest, MApiResponse>> running;
  private MApiService service;

  public SealedMApiService(Context paramContext, MApiService paramMApiService)
  {
    this.host = paramContext;
    this.service = paramMApiService;
    this.running = new ConcurrentHashMap();
  }

  public void abort(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, boolean paramBoolean)
  {
    if (this.running.remove(paramMApiRequest, paramRequestHandler))
    {
      this.service.abort(paramMApiRequest, this, paramBoolean);
      return;
    }
    this.service.abort(paramMApiRequest, paramRequestHandler, paramBoolean);
  }

  public void exec(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    if (paramRequestHandler != null)
    {
      this.running.put(paramMApiRequest, paramRequestHandler);
      this.service.exec(paramMApiRequest, this);
      return;
    }
    this.service.exec(paramMApiRequest, paramRequestHandler);
  }

  public MApiResponse execSync(MApiRequest paramMApiRequest)
  {
    return (MApiResponse)this.service.execSync(paramMApiRequest);
  }

  public void onDestroy()
  {
    Iterator localIterator = this.running.keySet().iterator();
    while (localIterator.hasNext())
    {
      MApiRequest localMApiRequest = (MApiRequest)localIterator.next();
      this.service.abort(localMApiRequest, this, true);
      Log.i("mapi_seal", "Abort leak request " + localMApiRequest);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.remove(paramMApiRequest);
    if (localRequestHandler != null)
    {
      localRequestHandler.onRequestFailed(paramMApiRequest, paramMApiResponse);
      return;
    }
    Log.w("mapi_seal", "Sealed leak on " + paramMApiRequest);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.remove(paramMApiRequest);
    if (localRequestHandler != null)
    {
      localRequestHandler.onRequestFinish(paramMApiRequest, paramMApiResponse);
      return;
    }
    Log.w("mapi_seal", "Sealed leak on " + paramMApiRequest);
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.get(paramMApiRequest);
    if ((localRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)localRequestHandler).onRequestProgress(paramMApiRequest, paramInt1, paramInt2);
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    RequestHandler localRequestHandler = (RequestHandler)this.running.get(paramMApiRequest);
    if ((localRequestHandler instanceof FullRequestHandle))
      ((FullRequestHandle)localRequestHandler).onRequestStart(paramMApiRequest);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.SealedMApiService
 * JD-Core Version:    0.6.0
 */