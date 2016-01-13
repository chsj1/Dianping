package com.dianping.main.find.pictureplaza;

public abstract class LazyLoadAdapterCellAgent extends PlazaAdapterCellAgent
{
  public LazyLoadAdapterCellAgent(Object paramObject)
  {
    super(paramObject);
    if (!(this.fragment instanceof LazyLoadAdapterAgentFragment))
      throw new RuntimeException("this is not a LazyLoadAdapterAgentFragment");
  }

  public abstract void requestData();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.LazyLoadAdapterCellAgent
 * JD-Core Version:    0.6.0
 */