package com.dianping.dataservice.http;

import com.dianping.dataservice.Response;
import java.util.List;
import org.apache.http.NameValuePair;

public abstract interface HttpResponse extends Response
{
  public abstract List<NameValuePair> headers();

  public abstract int statusCode();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.HttpResponse
 * JD-Core Version:    0.6.0
 */