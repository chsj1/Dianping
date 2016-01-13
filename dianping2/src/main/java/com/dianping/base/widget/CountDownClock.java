package com.dianping.base.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountDownClock extends LinearLayout
{
  public static final String BEGIN_COUNT_TIME = "mainhome_countdownclock_begincounttime";
  public static final String END_TIME = "mainhome_countdownclock_endtime";
  static final int FINISHED = 2;
  static final int NOT_STARTED = 0;
  static final int STARTED = 1;
  public static final String START_TIME = "mainhome_countdownclock_starttime";
  private boolean countFlag = false;
  private OnTimeListener mListener;
  private SharedPreferences mPrefs = DPActivity.preferences();
  public CountDownTimer mc;
  private SimpleDateFormat sdf_min = new SimpleDateFormat("mm");
  private SimpleDateFormat sdf_sec = new SimpleDateFormat("ss");
  private boolean singleFlag = true;
  private TextView textViewHour;
  private TextView textViewHour1;
  private TextView textViewMin;
  private TextView textViewMin1;
  private TextView textViewSec;
  private TextView textViewSec1;

  public CountDownClock(Context paramContext)
  {
    super(paramContext);
  }

  public CountDownClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CountDownClock);
    this.singleFlag = paramAttributeSet.getBoolean(R.styleable.CountDownClock_isSingle, true);
    paramAttributeSet.recycle();
    if (this.singleFlag)
      inflate(paramContext, R.layout.digital_clock_famousshop, this);
    while (true)
    {
      this.textViewHour = ((TextView)findViewById(R.id.clock_hour));
      this.textViewMin = ((TextView)findViewById(R.id.clock_min));
      this.textViewSec = ((TextView)findViewById(R.id.clock_sec));
      this.textViewHour1 = ((TextView)findViewById(R.id.clock_hour1));
      this.textViewMin1 = ((TextView)findViewById(R.id.clock_min1));
      this.textViewSec1 = ((TextView)findViewById(R.id.clock_sec1));
      return;
      inflate(paramContext, R.layout.digital_clock_trip, this);
    }
  }

  public void reStartCount()
  {
    if (!this.mPrefs.contains("mainhome_countdownclock_begincounttime"));
    label111: label114: 
    while (true)
    {
      return;
      long l1 = this.mPrefs.getLong("mainhome_countdownclock_starttime", 0L);
      long l2 = this.mPrefs.getLong("mainhome_countdownclock_endtime", 0L);
      long l3 = SystemClock.elapsedRealtime() - this.mPrefs.getLong("mainhome_countdownclock_begincounttime", 0L);
      if (l1 - l3 > 0L)
      {
        l1 -= l3;
        if (l2 - l3 <= 0L)
          break label111;
        l2 -= l3;
      }
      while (true)
      {
        if ((l1 <= 0L) && (l2 <= 0L))
          break label114;
        startCount(l1, l2);
        return;
        l1 = 0L;
        break;
        l2 = 0L;
      }
    }
  }

  public void resetCount()
  {
    this.mPrefs.edit().remove("mainhome_countdownclock_starttime").remove("mainhome_countdownclock_endtime").remove("mainhome_countdownclock_begincounttime").commit();
  }

  public void setOnTimeListener(OnTimeListener paramOnTimeListener)
  {
    this.mListener = paramOnTimeListener;
  }

  public void startCount(long paramLong1, long paramLong2)
  {
    if (paramLong1 > 0L)
      this.mPrefs.edit().putLong("mainhome_countdownclock_starttime", paramLong1).commit();
    if (paramLong2 > 0L)
      this.mPrefs.edit().putLong("mainhome_countdownclock_endtime", paramLong2).commit();
    if (this.mc != null)
      this.mc.cancel();
    if (paramLong1 > 0L)
    {
      this.countFlag = false;
      paramLong2 -= paramLong1;
      this.mc = new CountDownTimer(paramLong1, paramLong2, 1000L);
      this.mc.start();
    }
    while (true)
    {
      if (this.mListener != null)
      {
        if (paramLong1 <= 0L)
          break;
        this.mListener.onTimesUp(0);
      }
      return;
      this.countFlag = true;
      this.mc = new CountDownTimer(paramLong2, 1000L);
      this.mc.start();
    }
    if ((paramLong1 <= 0L) && (paramLong2 > 0L))
    {
      this.mListener.onTimesUp(1);
      return;
    }
    this.mListener.onTimesUp(2);
  }

  public void stopCount()
  {
    if (this.mc != null)
      this.mc.cancel();
  }

  public class CountDownTimer
  {
    private static final int MSG = 1;
    private final long mCountdownInterval;
    private Handler mHandler = new Handler()
    {
      public void handleMessage(Message arg1)
      {
        while (true)
        {
          synchronized (CountDownClock.CountDownTimer.this)
          {
            l1 = CountDownClock.CountDownTimer.this.mStopTimeInFuture - SystemClock.elapsedRealtime();
            if (l1 > 0L)
              continue;
            CountDownClock.CountDownTimer.this.onFinish();
            return;
            if (l1 < CountDownClock.CountDownTimer.this.mCountdownInterval)
              sendMessageDelayed(obtainMessage(1), l1);
          }
          long l2 = SystemClock.elapsedRealtime();
          CountDownClock.CountDownTimer.this.onTick(l1);
          long l1 = CountDownClock.CountDownTimer.this.mCountdownInterval + l2 - SystemClock.elapsedRealtime();
          while (l1 < 0L)
            l1 += CountDownClock.CountDownTimer.this.mCountdownInterval;
          sendMessageDelayed(obtainMessage(1), l1);
        }
      }
    };
    private final long mMillisInFuture;
    private long mStopTimeInFuture;
    private long nextTimeInFuture;

    public CountDownTimer(long arg2, long arg4)
    {
      this.mMillisInFuture = ???;
      Object localObject;
      this.mCountdownInterval = localObject;
    }

    public CountDownTimer(long arg2, long arg4, long arg6)
    {
      this.mMillisInFuture = ???;
      Object localObject2;
      this.mCountdownInterval = localObject2;
      Object localObject1;
      this.nextTimeInFuture = localObject1;
    }

    public final void cancel()
    {
      this.mHandler.removeMessages(1);
    }

    public void onFinish()
    {
      CountDownClock.this.textViewHour.setText("00");
      CountDownClock.this.textViewMin.setText("00");
      CountDownClock.this.textViewSec.setText("00");
      if (!CountDownClock.this.countFlag)
      {
        CountDownClock.access$1202(CountDownClock.this, true);
        cancel();
        reStart();
      }
      do
      {
        return;
        CountDownClock.this.resetCount();
      }
      while (CountDownClock.this.mListener == null);
      CountDownClock.this.mListener.onTimesUp(2);
    }

    public void onTick(long paramLong)
    {
      if (!CountDownClock.this.singleFlag)
      {
        i = (int)(paramLong / 1000L / 3600L);
        if (i < 10);
        for (str = "0" + i; ; str = i + "")
        {
          CountDownClock.this.textViewHour.setText(str.substring(0, 1));
          CountDownClock.this.textViewHour1.setText(str.substring(1));
          str = CountDownClock.this.sdf_min.format(new Date(paramLong));
          CountDownClock.this.textViewMin.setText(str.substring(0, 1));
          CountDownClock.this.textViewMin1.setText(str.substring(1));
          str = CountDownClock.this.sdf_sec.format(new Date(paramLong));
          CountDownClock.this.textViewSec.setText(str.substring(0, 1));
          CountDownClock.this.textViewSec1.setText(str.substring(1));
          return;
        }
      }
      int i = (int)(paramLong / 1000L / 3600L);
      if (i < 10);
      for (String str = "0" + i; ; str = i + "")
      {
        CountDownClock.this.textViewHour.setText(str);
        str = CountDownClock.this.sdf_min.format(new Date(paramLong));
        CountDownClock.this.textViewMin.setText(str);
        str = CountDownClock.this.sdf_sec.format(new Date(paramLong));
        CountDownClock.this.textViewSec.setText(str);
        return;
      }
    }

    public final CountDownTimer reStart()
    {
      monitorenter;
      try
      {
        if (this.nextTimeInFuture <= 0L)
          onFinish();
        while (true)
        {
          return this;
          CountDownClock.this.mPrefs.edit().putLong("mainhome_countdownclock_begincounttime", SystemClock.elapsedRealtime()).commit();
          this.mStopTimeInFuture = (SystemClock.elapsedRealtime() + this.nextTimeInFuture);
          this.mHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              CountDownClock.CountDownTimer.this.mHandler.sendMessage(CountDownClock.CountDownTimer.this.mHandler.obtainMessage(1));
              if (CountDownClock.this.mListener != null)
                CountDownClock.this.mListener.onTimesUp(1);
            }
          }
          , 500L);
        }
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }

    public final CountDownTimer start()
    {
      monitorenter;
      try
      {
        if (this.mMillisInFuture <= 0L)
          onFinish();
        while (true)
        {
          return this;
          CountDownClock.this.mPrefs.edit().putLong("mainhome_countdownclock_begincounttime", SystemClock.elapsedRealtime()).commit();
          this.mStopTimeInFuture = (SystemClock.elapsedRealtime() + this.mMillisInFuture);
          this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        }
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }
  }

  public static abstract interface OnTimeListener
  {
    public abstract void onTimesUp(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CountDownClock
 * JD-Core Version:    0.6.0
 */