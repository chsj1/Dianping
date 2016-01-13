package com.dianping.dataservice;

public abstract interface RequestHandler<T extends Request, R extends Response>
{
  public abstract void onRequestFailed(T paramT, R paramR);

  public abstract void onRequestFinish(T paramT, R paramR);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.RequestHandler
 * JD-Core Version:    0.6.0
 */