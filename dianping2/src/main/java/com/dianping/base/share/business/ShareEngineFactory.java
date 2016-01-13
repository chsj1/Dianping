package com.dianping.base.share.business;

public class ShareEngineFactory
{
  public static IShareEngine createShareEngine(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return new SinaShareEngine();
    case 1:
      return new SinaShareEngine();
    case 32:
    }
    return new QQShareEngine();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.business.ShareEngineFactory
 * JD-Core Version:    0.6.0
 */