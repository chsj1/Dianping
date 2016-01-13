package com.dianping.dataservice.cache;

import com.dianping.dataservice.DataService;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;

public abstract interface CacheService extends DataService<HttpRequest, HttpResponse>
{
  public abstract void clear();

  public abstract boolean put(Request paramRequest, HttpResponse paramHttpResponse, long paramLong);

  public abstract void remove(Request paramRequest);

  public abstract boolean touch(Request paramRequest, long paramLong);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.cache.CacheService
 * JD-Core Version:    0.6.0
 */