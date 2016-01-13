package com.dianping.dataservice;

public abstract interface Response
{
  public static final Object SUCCESS = null;

  public abstract Object error();

  public abstract Object result();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.Response
 * JD-Core Version:    0.6.0
 */