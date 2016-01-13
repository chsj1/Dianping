package com.dianping.dataservice;

public abstract interface FullRequestHandle<T extends Request, R extends Response> extends RequestHandler<T, R>
{
  public abstract void onRequestProgress(T paramT, int paramInt1, int paramInt2);

  public abstract void onRequestStart(T paramT);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.FullRequestHandle
 * JD-Core Version:    0.6.0
 */