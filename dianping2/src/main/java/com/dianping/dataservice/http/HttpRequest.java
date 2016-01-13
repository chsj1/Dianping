package com.dianping.dataservice.http;

import com.dianping.dataservice.Request;
import java.io.InputStream;
import java.util.List;
import org.apache.http.NameValuePair;

public abstract interface HttpRequest extends Request
{
  public abstract void addHeaders(List<NameValuePair> paramList);

  public abstract List<NameValuePair> headers();

  public abstract InputStream input();

  public abstract String method();

  public abstract long timeout();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.HttpRequest
 * JD-Core Version:    0.6.0
 */