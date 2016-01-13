package com.dianping.base.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.util.Log;
import com.dianping.v1.R.raw;
import java.io.IOException;
import java.util.HashMap;

public class SoundPlayer
{
  private static final int MAX_STREAMS = 3;
  private static final String TAG = "SoundPlayer";
  private static HashMap<String, Integer> sSoundMaps = new HashMap();
  private static SoundPool sSoundPool;

  private static SoundPool getSoundPool()
  {
    if (sSoundPool == null)
      sSoundPool = new SoundPool(3, 1, 0);
    return sSoundPool;
  }

  public static void play(int paramInt)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    int i = ((AudioManager)localContext.getSystemService("audio")).getStreamVolume(1);
    int j = ((AudioManager)localContext.getSystemService("audio")).getStreamMaxVolume(1);
    play(paramInt, i / j, 0);
  }

  public static void play(int paramInt1, float paramFloat, int paramInt2)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    SoundPool localSoundPool;
    Integer localInteger;
    try
    {
      localSoundPool = getSoundPool();
      if (paramInt1 <= 0)
      {
        playDefault(paramFloat, paramInt2);
        return;
      }
      localInteger = (Integer)sSoundMaps.get("resource:" + paramInt1);
      if (localInteger == null)
      {
        localSoundPool.load(localContext, paramInt1, 1);
        localSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(paramInt1, paramFloat, paramInt2)
        {
          public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2)
          {
            SoundPlayer.sSoundMaps.put("resource:" + this.val$resId, Integer.valueOf(paramInt1));
            paramSoundPool.play(paramInt1, this.val$vol, this.val$vol, 0, this.val$loop, 1.0F);
          }
        });
        return;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Log.e("SoundPlayer", "play error");
      return;
    }
    localSoundPool.play(localInteger.intValue(), paramFloat, paramFloat, 0, paramInt2, 1.0F);
  }

  public static void play(AssetFileDescriptor paramAssetFileDescriptor)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    int i = ((AudioManager)localContext.getSystemService("audio")).getStreamVolume(1);
    int j = ((AudioManager)localContext.getSystemService("audio")).getStreamMaxVolume(1);
    play(paramAssetFileDescriptor, i / j, 0);
  }

  public static void play(AssetFileDescriptor paramAssetFileDescriptor, float paramFloat, int paramInt)
  {
    SoundPool localSoundPool;
    Integer localInteger;
    try
    {
      localSoundPool = getSoundPool();
      if (paramAssetFileDescriptor == null)
      {
        playDefault(paramFloat, paramInt);
        return;
      }
      localInteger = (Integer)sSoundMaps.get(paramAssetFileDescriptor.toString());
      if (localInteger == null)
      {
        localSoundPool.load(paramAssetFileDescriptor, 1);
        localSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(paramAssetFileDescriptor, paramFloat, paramInt)
        {
          public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2)
          {
            SoundPlayer.sSoundMaps.put(this.val$ad.toString(), Integer.valueOf(paramInt1));
            paramSoundPool.play(paramInt1, this.val$vol, this.val$vol, 0, this.val$loop, 1.0F);
          }
        });
        return;
      }
    }
    catch (Exception paramAssetFileDescriptor)
    {
      paramAssetFileDescriptor.printStackTrace();
      Log.e("SoundPlayer", "play error");
      return;
    }
    localSoundPool.play(localInteger.intValue(), paramFloat, paramFloat, 0, paramInt, 1.0F);
  }

  public static void play(String paramString)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    int i = ((AudioManager)localContext.getSystemService("audio")).getStreamVolume(1);
    int j = ((AudioManager)localContext.getSystemService("audio")).getStreamMaxVolume(1);
    play(paramString, i / j, 0);
  }

  public static void play(String paramString, float paramFloat, int paramInt)
  {
    SoundPool localSoundPool;
    Integer localInteger;
    try
    {
      localSoundPool = getSoundPool();
      if (TextUtils.isEmpty(paramString))
      {
        playDefault(paramFloat, paramInt);
        return;
      }
      localInteger = (Integer)sSoundMaps.get(paramString);
      if (localInteger == null)
      {
        localSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(paramString, paramFloat, paramInt)
        {
          public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2)
          {
            SoundPlayer.sSoundMaps.put(this.val$path, Integer.valueOf(paramInt1));
            paramSoundPool.play(paramInt1, this.val$vol, this.val$vol, 0, this.val$loop, 1.0F);
          }
        });
        localSoundPool.load(paramString, 1);
        return;
      }
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
      Log.e("SoundPlayer", "play error");
      return;
    }
    localSoundPool.play(localInteger.intValue(), paramFloat, paramFloat, 0, paramInt, 1.0F);
  }

  public static void playDefault()
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    int i = ((AudioManager)localContext.getSystemService("audio")).getStreamVolume(1);
    int j = ((AudioManager)localContext.getSystemService("audio")).getStreamMaxVolume(1);
    playDefault(i / j, 0);
  }

  public static void playDefault(float paramFloat, int paramInt)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    try
    {
      SoundPool localSoundPool = getSoundPool();
      Integer localInteger = (Integer)sSoundMaps.get("default");
      if (localInteger == null)
      {
        localSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(paramFloat, paramInt)
        {
          public void onLoadComplete(SoundPool paramSoundPool, int paramInt1, int paramInt2)
          {
            SoundPlayer.sSoundMaps.put("default", Integer.valueOf(paramInt1));
            paramSoundPool.play(paramInt1, this.val$vol, this.val$vol, 0, this.val$loop, 1.0F);
          }
        });
        localSoundPool.load(localContext, R.raw.audio_default, 1);
        return;
      }
      localSoundPool.play(localInteger.intValue(), paramFloat, paramFloat, 0, paramInt, 1.0F);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Log.e("SoundPlayer", "play error");
    }
  }

  @Deprecated
  public static void playWithCallback(String paramString, PlayCallBack paramPlayCallBack)
  {
    Context localContext = DPApplication.instance().getApplicationContext();
    MediaPlayer localMediaPlayer = new MediaPlayer();
    localMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(paramPlayCallBack, localMediaPlayer)
    {
      public void onCompletion(MediaPlayer paramMediaPlayer)
      {
        this.val$callback.onPlayCompleted();
        this.val$player.release();
      }
    });
    try
    {
      if (TextUtils.isEmpty(paramString))
        localMediaPlayer.setDataSource(localContext, Uri.parse("android.resource://" + localContext.getApplicationContext().getPackageName() + "/" + R.raw.audio_default));
      while (true)
      {
        localMediaPlayer.prepareAsync();
        localMediaPlayer.setAudioStreamType(1);
        localMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(localMediaPlayer)
        {
          public void onPrepared(MediaPlayer paramMediaPlayer)
          {
            this.val$player.start();
          }
        });
        return;
        localMediaPlayer.setDataSource(localContext, Uri.parse(paramString));
      }
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
    }
  }

  public static abstract interface PlayCallBack
  {
    public abstract void onPlayCompleted();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.SoundPlayer
 * JD-Core Version:    0.6.0
 */