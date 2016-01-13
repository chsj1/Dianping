package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.RejectedExecutionException;

final class AutoFocusManager
  implements Camera.AutoFocusCallback
{
  private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;
  private static final Collection<String> FOCUS_MODES_CALLING_AF;
  private static final String TAG = AutoFocusManager.class.getSimpleName();
  private final Camera camera;
  private boolean focusing;
  private AsyncTask<?, ?, ?> outstandingTask;
  private boolean stopped;
  private final AsyncTaskExecInterface taskExec;
  private final boolean useAutoFocus;

  static
  {
    FOCUS_MODES_CALLING_AF = new ArrayList(2);
    FOCUS_MODES_CALLING_AF.add("auto");
    FOCUS_MODES_CALLING_AF.add("macro");
  }

  AutoFocusManager(Context paramContext, Camera paramCamera)
  {
    this.camera = paramCamera;
    this.taskExec = ((AsyncTaskExecInterface)new AsyncTaskExecManager().build());
    paramContext = PreferenceManager.getDefaultSharedPreferences(paramContext);
    paramCamera = paramCamera.getParameters().getFocusMode();
    if ((paramContext.getBoolean("preferences_auto_focus", true)) && (FOCUS_MODES_CALLING_AF.contains(paramCamera)));
    for (boolean bool = true; ; bool = false)
    {
      this.useAutoFocus = bool;
      Log.i(TAG, "Current focus mode '" + paramCamera + "'; use auto focus? " + this.useAutoFocus);
      start();
      return;
    }
  }

  private void autoFocusAgainLater()
  {
    monitorenter;
    try
    {
      AutoFocusTask localAutoFocusTask;
      if ((!this.stopped) && (this.outstandingTask == null))
        localAutoFocusTask = new AutoFocusTask(null);
      try
      {
        this.taskExec.execute(localAutoFocusTask, new Object[0]);
        this.outstandingTask = localAutoFocusTask;
        monitorexit;
        return;
      }
      catch (RejectedExecutionException localRejectedExecutionException)
      {
        while (true)
          Log.w(TAG, "Could not request auto focus", localRejectedExecutionException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void cancelOutstandingTask()
  {
    monitorenter;
    try
    {
      if (this.outstandingTask != null)
      {
        if (this.outstandingTask.getStatus() != AsyncTask.Status.FINISHED)
          this.outstandingTask.cancel(true);
        this.outstandingTask = null;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void onAutoFocus(boolean paramBoolean, Camera paramCamera)
  {
    monitorenter;
    try
    {
      this.focusing = false;
      autoFocusAgainLater();
      monitorexit;
      return;
    }
    finally
    {
      paramCamera = finally;
      monitorexit;
    }
    throw paramCamera;
  }

  void start()
  {
    monitorenter;
    try
    {
      if (this.useAutoFocus)
      {
        this.outstandingTask = null;
        if (!this.stopped)
        {
          boolean bool = this.focusing;
          if (bool);
        }
      }
      try
      {
        this.camera.autoFocus(this);
        this.focusing = true;
        monitorexit;
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        while (true)
        {
          Log.w(TAG, "Unexpected exception while focusing", localRuntimeException);
          autoFocusAgainLater();
        }
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void stop()
  {
    monitorenter;
    try
    {
      this.stopped = true;
      if (this.useAutoFocus)
        cancelOutstandingTask();
      try
      {
        this.camera.cancelAutoFocus();
        monitorexit;
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        while (true)
          Log.w(TAG, "Unexpected exception while cancelling focusing", localRuntimeException);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private final class AutoFocusTask extends AsyncTask<Object, Object, Object>
  {
    private AutoFocusTask()
    {
    }

    protected Object doInBackground(Object[] paramArrayOfObject)
    {
      try
      {
        Thread.sleep(2000L);
        label6: AutoFocusManager.this.start();
        return null;
      }
      catch (java.lang.InterruptedException paramArrayOfObject)
      {
        break label6;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.camera.AutoFocusManager
 * JD-Core Version:    0.6.0
 */