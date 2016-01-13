package com.dianping.dataservice;

public abstract interface DataService<T extends Request, R extends Response>
{
  public abstract void abort(T paramT, RequestHandler<T, R> paramRequestHandler, boolean paramBoolean);

  public abstract void exec(T paramT, RequestHandler<T, R> paramRequestHandler);

  public abstract R execSync(T paramT);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.DataService
 * JD-Core Version:    0.6.0
 */