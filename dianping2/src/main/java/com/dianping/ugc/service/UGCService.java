package com.dianping.ugc.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UGCService
{
  private ExecutorService mUGCExecutor = Executors.newSingleThreadExecutor();

  public void submit(Runnable paramRunnable)
  {
    this.mUGCExecutor.submit(paramRunnable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.service.UGCService
 * JD-Core Version:    0.6.0
 */