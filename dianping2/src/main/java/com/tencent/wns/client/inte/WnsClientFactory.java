package com.tencent.wns.client.inte;

import com.tencent.base.util.f;

public class WnsClientFactory
{
  private static final f a = new WnsClientFactory.1();
  private static final f b = new WnsClientFactory.2();

  public static InternalWnsService getInternalWnsService()
  {
    return (InternalWnsService)b.get();
  }

  public static WnsService getThirdPartyWnsService()
  {
    return (WnsService)a.get();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.client.inte.WnsClientFactory
 * JD-Core Version:    0.6.0
 */