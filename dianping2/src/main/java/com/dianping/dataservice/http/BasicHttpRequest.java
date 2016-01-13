package com.dianping.dataservice.http;

import com.dianping.dataservice.BasicRequest;
import java.io.InputStream;
import java.net.Proxy;
import java.util.List;
import org.apache.http.NameValuePair;

public class BasicHttpRequest extends BasicRequest
  implements HttpRequest
{
  public static final String DELETE = "DELETE";
  public static final String GET = "GET";
  public static final String HEAD = "HEAD";
  public static final String POST = "POST";
  public static final String PUT = "PUT";
  private List<NameValuePair> headers;
  private InputStream input;
  private String method;
  private Proxy proxy;
  private long timeout;

  public BasicHttpRequest(String paramString1, String paramString2, InputStream paramInputStream)
  {
    this(paramString1, paramString2, paramInputStream, null, 0L);
  }

  public BasicHttpRequest(String paramString1, String paramString2, InputStream paramInputStream, List<NameValuePair> paramList)
  {
    this(paramString1, paramString2, paramInputStream, paramList, 0L);
  }

  public BasicHttpRequest(String paramString1, String paramString2, InputStream paramInputStream, List<NameValuePair> paramList, long paramLong)
  {
    this(paramString1, paramString2, paramInputStream, paramList, paramLong, null);
  }

  public BasicHttpRequest(String paramString1, String paramString2, InputStream paramInputStream, List<NameValuePair> paramList, long paramLong, Proxy paramProxy)
  {
    super(paramString1);
    this.method = paramString2;
    this.input = paramInputStream;
    this.headers = paramList;
    this.timeout = paramLong;
    this.proxy = paramProxy;
  }

  public static HttpRequest httpGet(String paramString)
  {
    return new BasicHttpRequest(paramString, "GET", null, null);
  }

  public static HttpRequest httpPost(String paramString, String[] paramArrayOfString)
  {
    return new BasicHttpRequest(paramString, "POST", new FormInputStream(paramArrayOfString), null);
  }

  public void addHeaders(List<NameValuePair> paramList)
  {
    if (paramList == null)
      return;
    if (this.headers != null)
    {
      this.headers.addAll(paramList);
      return;
    }
    this.headers = paramList;
  }

  public List<NameValuePair> headers()
  {
    return this.headers;
  }

  public InputStream input()
  {
    return this.input;
  }

  public String method()
  {
    return this.method;
  }

  public Proxy proxy()
  {
    return this.proxy;
  }

  public long timeout()
  {
    return this.timeout;
  }

  public String toString()
  {
    return this.method + ": " + super.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.BasicHttpRequest
 * JD-Core Version:    0.6.0
 */