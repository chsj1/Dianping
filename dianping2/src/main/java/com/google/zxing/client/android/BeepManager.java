package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.Closeable;

final class BeepManager
  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Closeable
{
  private static final float BEEP_VOLUME = 0.1F;
  private static final String TAG = BeepManager.class.getSimpleName();
  private static final long VIBRATE_DURATION = 200L;
  private final Activity activity;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;
  private boolean vibrate;

  BeepManager(Activity paramActivity)
  {
    this.activity = paramActivity;
    this.mediaPlayer = null;
    updatePrefs();
  }

  private MediaPlayer buildMediaPlayer(Context paramContext)
  {
    MediaPlayer localMediaPlayer = new MediaPlayer();
    localMediaPlayer.setAudioStreamType(3);
    localMediaPlayer.setOnCompletionListener(this);
    localMediaPlayer.setOnErrorListener(this);
    try
    {
      paramContext = paramContext.getResources().openRawResourceFd(R.raw.beep);
      try
      {
        localMediaPlayer.setDataSource(paramContext.getFileDescriptor(), paramContext.getStartOffset(), paramContext.getLength());
        paramContext.close();
        localMediaPlayer.setVolume(0.1F, 0.1F);
        return localMediaPlayer;
      }
      finally
      {
        paramContext.close();
      }
    }
    catch (java.io.IOException paramContext)
    {
      Log.w(TAG, paramContext);
      localMediaPlayer.release();
    }
    return null;
  }

  private static boolean shouldBeep(SharedPreferences paramSharedPreferences, Context paramContext)
  {
    boolean bool2 = paramSharedPreferences.getBoolean("preferences_play_beep", true);
    boolean bool1 = bool2;
    if (bool2)
    {
      bool1 = bool2;
      if (((AudioManager)paramContext.getSystemService("audio")).getRingerMode() != 2)
        bool1 = false;
    }
    return bool1;
  }

  public void close()
  {
    monitorenter;
    try
    {
      if (this.mediaPlayer != null)
      {
        this.mediaPlayer.release();
        this.mediaPlayer = null;
      }
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

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    paramMediaPlayer.seekTo(0);
  }

  public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    monitorenter;
    if (paramInt1 == 100);
    try
    {
      this.activity.finish();
      while (true)
      {
        return true;
        paramMediaPlayer.release();
        this.mediaPlayer = null;
        updatePrefs();
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramMediaPlayer;
  }

  void playBeepSoundAndVibrate()
  {
    monitorenter;
    try
    {
      if ((this.playBeep) && (this.mediaPlayer != null))
        this.mediaPlayer.start();
      if (this.vibrate)
        ((Vibrator)this.activity.getSystemService("vibrator")).vibrate(200L);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void updatePrefs()
  {
    monitorenter;
    try
    {
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
      this.playBeep = shouldBeep(localSharedPreferences, this.activity);
      this.vibrate = localSharedPreferences.getBoolean("preferences_vibrate", false);
      if ((this.playBeep) && (this.mediaPlayer == null))
      {
        this.activity.setVolumeControlStream(3);
        this.mediaPlayer = buildMediaPlayer(this.activity);
      }
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.BeepManager
 * JD-Core Version:    0.6.0
 */