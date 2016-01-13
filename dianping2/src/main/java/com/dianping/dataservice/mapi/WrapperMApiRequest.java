package com.dianping.dataservice.mapi;

import java.io.InputStream;
import java.util.List;
import org.apache.http.NameValuePair;

public class WrapperMApiRequest
  implements MApiRequest
{
  private MApiRequest request;

  public WrapperMApiRequest(MApiRequest paramMApiRequest)
  {
    this.request = paramMApiRequest;
  }

  public void addHeaders(List<NameValuePair> paramList)
  {
    this.request.addHeaders(paramList);
  }

  public CacheType defaultCacheType()
  {
    return this.request.defaultCacheType();
  }

  public boolean disableStatistics()
  {
    return this.request.disableStatistics();
  }

  public List<NameValuePair> headers()
  {
    return this.request.headers();
  }

  public InputStream input()
  {
    return this.request.input();
  }

  public String method()
  {
    return this.request.method();
  }

  public long timeout()
  {
    return this.request.timeout();
  }

  public String toString()
  {
    return this.request.toString();
  }

  public String url()
  {
    return this.request.url();
  }

  public MApiRequest wrapped()
  {
    return this.request;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.WrapperMApiRequest
 * JD-Core Version:    0.6.0
 */