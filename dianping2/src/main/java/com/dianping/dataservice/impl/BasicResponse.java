package com.dianping.dataservice.impl;

import com.dianping.dataservice.Response;

public class BasicResponse
  implements Response
{
  private Object error;
  private Object result;

  public BasicResponse(Object paramObject1, Object paramObject2)
  {
    this.result = paramObject1;
    this.error = paramObject2;
  }

  public Object error()
  {
    return this.error;
  }

  public Object result()
  {
    return this.result;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.impl.BasicResponse
 * JD-Core Version:    0.6.0
 */