package com.github.mmin18.layoutcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.widget.TextView;

public class ResetActivity extends Activity
{
  private static final Handler HANDLER = new Handler(Looper.getMainLooper());
  private static final long RESET_WAIT = 2000L;
  private int back;
  private long createTime;
  private boolean ready;
  private final Runnable reset = new Runnable()
  {
    public void run()
    {
      Process.killProcess(Process.myPid());
    }
  };

  public void onBackPressed()
  {
    int i = this.back;
    this.back = (i + 1);
    if (i > 0)
    {
      if (this.ready)
        this.reset.run();
      super.onBackPressed();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new TextView(this);
    paramBundle.setGravity(17);
    paramBundle.setText("Cast DEX in 2 second..");
    setContentView(paramBundle);
    this.createTime = SystemClock.uptimeMillis();
    this.ready = getIntent().getBooleanExtra("reset", false);
    if (this.ready)
      reset();
  }

  protected void onDestroy()
  {
    HANDLER.removeCallbacks(this.reset);
    super.onDestroy();
  }

  public void reset()
  {
    this.ready = true;
    HANDLER.removeCallbacks(this.reset);
    long l = SystemClock.uptimeMillis() - this.createTime;
    if (l > 2000L)
    {
      HANDLER.postDelayed(this.reset, 100L);
      return;
    }
    HANDLER.postDelayed(this.reset, 2000L - l);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.ResetActivity
 * JD-Core Version:    0.6.0
 */