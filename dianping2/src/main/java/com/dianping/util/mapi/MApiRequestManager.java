package com.dianping.util.mapi;

import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MApiRequestManager
{
  private static final String TAG = "MApiRequestManager";
  private Map<String, MApiRequestInfo> mapiRequests = new HashMap();
  private final MApiService mapiService;

  public MApiRequestManager(MApiService paramMApiService)
  {
    this.mapiService = paramMApiService;
  }

  private void removeOldRequest(MApiRequest paramMApiRequest)
  {
    Object localObject = paramMApiRequest.url();
    Iterator localIterator = this.mapiRequests.keySet().iterator();
    while (localIterator.hasNext())
    {
      paramMApiRequest = (String)localIterator.next();
      if (!paramMApiRequest.startsWith((String)localObject))
        continue;
      localObject = (MApiRequestInfo)this.mapiRequests.get(paramMApiRequest);
      if (localObject != null)
      {
        this.mapiService.abort(((MApiRequestInfo)localObject).request, ((MApiRequestInfo)localObject).handler, true);
        Log.i("MApiRequestManager", "new request is coming. abort the old one from the map with url: " + ((MApiRequestInfo)localObject).request.url());
      }
      this.mapiRequests.remove(paramMApiRequest);
    }
  }

  public void addRequest(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    removeOldRequest(paramMApiRequest);
    this.mapiRequests.put(paramMApiRequest.url(), new MApiRequestInfo(paramMApiRequest, paramRequestHandler));
  }

  public void clearAll()
  {
    Iterator localIterator = this.mapiRequests.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (String)localIterator.next();
      localObject = (MApiRequestInfo)this.mapiRequests.get(localObject);
      this.mapiService.abort(((MApiRequestInfo)localObject).request, ((MApiRequestInfo)localObject).handler, true);
      Log.i("MApiRequestManager", "abort a request from the map with url: " + ((MApiRequestInfo)localObject).request.url());
      localIterator.remove();
    }
  }

  private static class MApiRequestInfo
  {
    public final RequestHandler<MApiRequest, MApiResponse> handler;
    public final MApiRequest request;

    public MApiRequestInfo(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
    {
      this.request = paramMApiRequest;
      this.handler = paramRequestHandler;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.mapi.MApiRequestManager
 * JD-Core Version:    0.6.0
 */