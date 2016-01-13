package com.dianping.dataservice.mapi.impl;

import com.dianping.dataservice.http.HttpRequest;
import java.io.ByteArrayOutputStream;

public class HttpExceptionLogger extends BaseLogger
{
  private Exception e;
  private int errorCode;
  private HttpRequest req;

  public HttpExceptionLogger(String paramString1, HttpRequest paramHttpRequest, String paramString2, int paramInt, Exception paramException)
  {
    super(paramString1, getCommand(paramHttpRequest.url()), paramString2);
    this.e = paramException;
    this.errorCode = paramInt;
    this.req = paramHttpRequest;
  }

  public byte[] buildLogInfo()
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[[[HTTPEXCEPTION - ").append(this.errorCode);
      localStringBuilder.append(": ").append(this.command).append('\n');
      localStringBuilder.append("Dpid: ").append(this.dpid).append('\n');
      localStringBuilder.append("Network: ").append(this.netInfo).append('\n');
      localStringBuilder.append("Url:").append(this.req.url()).append('\n');
      localStringBuilder.append("Method:").append(this.req.method()).append('\n');
      localStringBuilder.append("Exception:").append(this.e.getClass()).append('\n');
      localStringBuilder.append('\n').append(this.e.getMessage());
      localStringBuilder.append("\n]]]\n\n");
      localByteArrayOutputStream.write(localStringBuilder.toString().getBytes("US-ASCII"));
      label194: return localByteArrayOutputStream.toByteArray();
    }
    catch (Exception localException)
    {
      break label194;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.HttpExceptionLogger
 * JD-Core Version:    0.6.0
 */