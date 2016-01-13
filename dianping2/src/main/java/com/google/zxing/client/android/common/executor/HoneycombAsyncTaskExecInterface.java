package com.google.zxing.client.android.common.executor;

import android.annotation.TargetApi;
import android.os.AsyncTask;

@TargetApi(11)
public final class HoneycombAsyncTaskExecInterface
  implements AsyncTaskExecInterface
{
  public <T> void execute(AsyncTask<T, ?, ?> paramAsyncTask, T[] paramArrayOfT)
  {
    paramAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paramArrayOfT);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.common.executor.HoneycombAsyncTaskExecInterface
 * JD-Core Version:    0.6.0
 */