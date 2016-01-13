package com.google.zxing.client.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;

final class InactivityTimer
{
  private static final long INACTIVITY_DELAY_MS = 300000L;
  private static final String TAG = InactivityTimer.class.getSimpleName();
  private final Activity activity;
  private AsyncTask<Object, Object, Object> inactivityTask;
  private final BroadcastReceiver powerStatusReceiver;
  private boolean registered;
  private final AsyncTaskExecInterface taskExec;

  InactivityTimer(Activity paramActivity)
  {
    this.activity = paramActivity;
    this.taskExec = ((AsyncTaskExecInterface)new AsyncTaskExecManager().build());
    this.powerStatusReceiver = new PowerStatusReceiver(null);
    this.registered = false;
    onActivity();
  }

  private void cancel()
  {
    monitorenter;
    try
    {
      AsyncTask localAsyncTask = this.inactivityTask;
      if (localAsyncTask != null)
      {
        localAsyncTask.cancel(true);
        this.inactivityTask = null;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void onActivity()
  {
    monitorenter;
    try
    {
      cancel();
      this.inactivityTask = new InactivityAsyncTask(null);
      this.taskExec.execute(this.inactivityTask, new Object[0]);
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void onPause()
  {
    monitorenter;
    try
    {
      cancel();
      if (this.registered)
      {
        this.activity.unregisterReceiver(this.powerStatusReceiver);
        this.registered = false;
      }
      while (true)
      {
        return;
        Log.w(TAG, "PowerStatusReceiver was never registered?");
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void onResume()
  {
    monitorenter;
    try
    {
      if (this.registered)
        Log.w(TAG, "PowerStatusReceiver was already registered?");
      while (true)
      {
        onActivity();
        return;
        this.activity.registerReceiver(this.powerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.registered = true;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void shutdown()
  {
    cancel();
  }

  private final class InactivityAsyncTask extends AsyncTask<Object, Object, Object>
  {
    private InactivityAsyncTask()
    {
    }

    protected Object doInBackground(Object[] paramArrayOfObject)
    {
      try
      {
        Thread.sleep(300000L);
        Log.i(InactivityTimer.TAG, "Finishing activity due to inactivity");
        InactivityTimer.this.activity.finish();
        label25: return null;
      }
      catch (java.lang.InterruptedException paramArrayOfObject)
      {
        break label25;
      }
    }
  }

  private final class PowerStatusReceiver extends BroadcastReceiver
  {
    private PowerStatusReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("android.intent.action.BATTERY_CHANGED".equals(paramIntent.getAction()))
        if (paramIntent.getIntExtra("plugged", -1) > 0)
          break label36;
      label36: for (int i = 1; i != 0; i = 0)
      {
        InactivityTimer.this.onActivity();
        return;
      }
      InactivityTimer.this.cancel();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.InactivityTimer
 * JD-Core Version:    0.6.0
 */