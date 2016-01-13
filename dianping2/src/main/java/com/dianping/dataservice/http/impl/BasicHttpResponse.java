package com.dianping.dataservice.http.impl;

import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.impl.BasicResponse;
import java.util.List;
import org.apache.http.NameValuePair;

public class BasicHttpResponse extends BasicResponse
  implements HttpResponse
{
  private List<NameValuePair> headers;
  private int statusCode;

  public BasicHttpResponse(int paramInt, Object paramObject1, List<NameValuePair> paramList, Object paramObject2)
  {
    super(paramObject1, paramObject2);
    this.statusCode = paramInt;
    this.headers = paramList;
  }

  public List<NameValuePair> headers()
  {
    return this.headers;
  }

  public int statusCode()
  {
    return this.statusCode;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.impl.BasicHttpResponse
 * JD-Core Version:    0.6.0
 */