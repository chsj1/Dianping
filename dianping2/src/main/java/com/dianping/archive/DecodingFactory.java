package com.dianping.archive;

public abstract interface DecodingFactory<T>
{
  public abstract T[] createArray(int paramInt);

  public abstract T createInstance(int paramInt);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.archive.DecodingFactory
 * JD-Core Version:    0.6.0
 */