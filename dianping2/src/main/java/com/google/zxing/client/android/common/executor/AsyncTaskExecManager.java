package com.google.zxing.client.android.common.executor;

import com.google.zxing.client.android.common.PlatformSupportManager;

public final class AsyncTaskExecManager extends PlatformSupportManager<AsyncTaskExecInterface>
{
  public AsyncTaskExecManager()
  {
    super(AsyncTaskExecInterface.class, new DefaultAsyncTaskExecInterface());
    addImplementationClass(11, "com.google.zxing.client.android.common.executor.HoneycombAsyncTaskExecInterface");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.common.executor.AsyncTaskExecManager
 * JD-Core Version:    0.6.0
 */