package com.dianping.dataservice;

public abstract class BasicRequest
  implements Request
{
  private String url;

  public BasicRequest(String paramString)
  {
    this.url = paramString;
  }

  public String toString()
  {
    return this.url;
  }

  public String url()
  {
    return this.url;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.BasicRequest
 * JD-Core Version:    0.6.0
 */