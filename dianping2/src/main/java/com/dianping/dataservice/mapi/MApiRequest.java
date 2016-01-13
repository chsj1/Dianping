package com.dianping.dataservice.mapi;

import com.dianping.dataservice.http.HttpRequest;
import java.io.InputStream;

public abstract interface MApiRequest extends HttpRequest
{
  public abstract CacheType defaultCacheType();

  public abstract boolean disableStatistics();

  public abstract InputStream input();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.MApiRequest
 * JD-Core Version:    0.6.0
 */