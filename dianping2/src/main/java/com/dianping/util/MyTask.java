package com.dianping.util;

import android.os.Message;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MyTask<Params, Progress, Result>
{
  private static final int MESSAGE_POST_PROGRESS = 2;
  private static final int MESSAGE_POST_RESULT = 1;
  private static final MyTask.InternalHandler sHandler = new MyTask.InternalHandler();
  private final FutureTask<Result> mFuture = new MyTask.2(this, this.mWorker);
  private volatile MyTask.Status mStatus = MyTask.Status.PENDING;
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  private final MyTask.WorkerRunnable<Params, Result> mWorker = new MyTask.1(this);

  private void finish(Result paramResult)
  {
    if (isCancelled())
      onCancelled(paramResult);
    while (true)
    {
      this.mStatus = MyTask.Status.FINISHED;
      return;
      onPostExecute(paramResult);
    }
  }

  private Result postResult(Result paramResult)
  {
    sHandler.obtainMessage(1, new MyTask.AsyncTaskResult(this, new Object[] { paramResult })).sendToTarget();
    return paramResult;
  }

  private void postResultIfNotInvoked(Result paramResult)
  {
    if (!this.mTaskInvoked.get())
      postResult(paramResult);
  }

  public final boolean cancel(boolean paramBoolean)
  {
    return this.mFuture.cancel(paramBoolean);
  }

  protected abstract Result doInBackground(Params[] paramArrayOfParams);

  public final MyTask<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params[] paramArrayOfParams)
  {
    if (this.mStatus != MyTask.Status.PENDING);
    switch (MyTask.3.$SwitchMap$com$dianping$util$MyTask$Status[this.mStatus.ordinal()])
    {
    default:
      this.mStatus = MyTask.Status.RUNNING;
      onPreExecute();
      this.mWorker.mParams = paramArrayOfParams;
      paramExecutor.execute(this.mFuture);
      return this;
    case 1:
      throw new IllegalStateException("Cannot execute task: the task is already running.");
    case 2:
    }
    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
  }

  public final Result get()
    throws InterruptedException, ExecutionException
  {
    return this.mFuture.get();
  }

  public final Result get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }

  public final MyTask.Status getStatus()
  {
    return this.mStatus;
  }

  public final boolean isCancelled()
  {
    return this.mFuture.isCancelled();
  }

  protected void onCancelled()
  {
  }

  protected void onCancelled(Result paramResult)
  {
    onCancelled();
  }

  protected void onPostExecute(Result paramResult)
  {
  }

  protected void onPreExecute()
  {
  }

  protected void onProgressUpdate(Progress[] paramArrayOfProgress)
  {
  }

  protected final void publishProgress(Progress[] paramArrayOfProgress)
  {
    if (!isCancelled())
      sHandler.obtainMessage(2, new MyTask.AsyncTaskResult(this, paramArrayOfProgress)).sendToTarget();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.MyTask
 * JD-Core Version:    0.6.0
 */