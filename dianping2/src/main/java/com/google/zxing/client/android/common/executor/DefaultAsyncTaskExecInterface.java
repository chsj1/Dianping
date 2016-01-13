package com.google.zxing.client.android.common.executor;

import android.os.AsyncTask;

public final class DefaultAsyncTaskExecInterface
  implements AsyncTaskExecInterface
{
  public <T> void execute(AsyncTask<T, ?, ?> paramAsyncTask, T[] paramArrayOfT)
  {
    paramAsyncTask.execute(paramArrayOfT);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.common.executor.DefaultAsyncTaskExecInterface
 * JD-Core Version:    0.6.0
 */