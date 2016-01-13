package com.dianping.movie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class MovieVideoActivity extends NovaActivity
  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
{
  public static final String VIDEO_URL = "VIDEO_URL";
  private boolean alertShowed = false;
  private View loadingLayout;
  private Handler mHandler = new Handler();
  private VideoView mVideoView;
  private MediaController mediaController;
  private String videoUrl = "";

  private boolean playUrl(String paramString)
  {
    if ((paramString == null) || (paramString.equals("null")))
    {
      stopPlaying();
      return false;
    }
    this.mediaController.requestFocus();
    this.mVideoView.setVideoURI(Uri.parse(paramString));
    return true;
  }

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.hideTitleBar();
    super.getWindow().setBackgroundDrawable(null);
    setRequestedOrientation(0);
    getWindow().setFlags(1024, 1024);
    getWindow().setFlags(128, 128);
    setContentView(R.layout.movie_video_layout);
    this.videoUrl = getIntent().getStringExtra("VIDEO_URL");
    this.mVideoView = ((VideoView)findViewById(R.id.myvideoview));
    this.loadingLayout = findViewById(R.id.loading_layout);
    this.mediaController = new MediaController(this);
    this.mVideoView.setMediaController(this.mediaController);
    this.mVideoView.setOnCompletionListener(this);
    this.mVideoView.setOnPreparedListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mVideoView.isPlaying())
      this.mVideoView.stopPlayback();
  }

  public void onPrepared(MediaPlayer paramMediaPlayer)
  {
    this.loadingLayout.setVisibility(8);
    this.mVideoView.start();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if ((paramBoolean) && (!this.mVideoView.isPlaying()))
    {
      if (!NetworkUtils.getNetworkType(this).equals("WIFI"))
        break label51;
      this.mHandler.postDelayed(new Runnable()
      {
        public void run()
        {
          if (!MovieVideoActivity.this.playUrl(MovieVideoActivity.this.videoUrl))
            Toast.makeText(MovieVideoActivity.this, "播放失败", 0).show();
        }
      }
      , 500L);
    }
    label51: 
    do
      return;
    while (this.alertShowed);
    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("您当前使用的不是WIFI网络，播放预告片将消耗较多流量，是否继续?").setPositiveButton("继续", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MovieVideoActivity.access$202(MovieVideoActivity.this, true);
        if (!MovieVideoActivity.this.playUrl(MovieVideoActivity.this.videoUrl))
          Toast.makeText(MovieVideoActivity.this, "播放失败", 0).show();
      }
    }).setNegativeButton("退出", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MovieVideoActivity.this.finish();
      }
    }).setCancelable(false).create().show();
  }

  public void stopPlaying()
  {
    this.mVideoView.stopPlayback();
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieVideoActivity
 * JD-Core Version:    0.6.0
 */