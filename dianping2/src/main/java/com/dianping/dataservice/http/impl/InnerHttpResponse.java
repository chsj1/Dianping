package com.dianping.dataservice.http.impl;

import java.util.List;
import org.apache.http.NameValuePair;

public class InnerHttpResponse extends BasicHttpResponse
{
  public static final int SOURCE_HTTP = 0;
  public static final int SOURCE_HTTPS = 8;
  public static final int SOURCE_TUNNEL = 1;
  public static final int SOURCE_UTN = 2;
  public static final int SOURCE_WNS = 4;
  public static final int TUNNEL_HTTP = 0;
  public static final int TUNNEL_HTTPS = 8;
  public static final int TUNNEL_TCP = 1;
  public static final int TUNNEL_UTN = 2;
  public static final int TUNNEL_WNS = 4;
  public String ip;
  public int source;
  public int tunnel;

  public InnerHttpResponse(int paramInt, Object paramObject1, List<NameValuePair> paramList, Object paramObject2)
  {
    super(paramInt, paramObject1, paramList, paramObject2);
  }

  public String from()
  {
    switch (this.source)
    {
    case 3:
    case 5:
    case 6:
    case 7:
    default:
      return "?";
    case 0:
      return "http";
    case 1:
      return "tunnel";
    case 2:
      return "utn";
    case 4:
      return "wns";
    case 8:
    }
    return "https";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.impl.InnerHttpResponse
 * JD-Core Version:    0.6.0
 */