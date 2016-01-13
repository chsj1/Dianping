package com.dianping.base.widget.loading;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.OnLoadingListener;
import com.dianping.widget.ZoomNetworkImageView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class LoadingLayout extends RelativeLayout
  implements OnLoadingListener, View.OnClickListener, PhotoViewAttacher.OnViewTapListener
{
  private float DEFAULT_DENSITY = 2.0F;
  private int LOADING_IMAGE_HEIGHT = 150;
  private int LOADING_IMAGE_WIDTH = 150;
  ImageView backgroundView;
  ClipDrawable clipdrawable;
  GLRenderer gl;
  NetworkImageView imageView;
  boolean isClickable = false;
  ImageView loadingView;
  float scale;
  Timer timer;

  public LoadingLayout(Context paramContext)
  {
    super(paramContext);
  }

  public LoadingLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public LoadingLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void addBackgroundView()
  {
    this.backgroundView = new ImageView(getContext());
    this.backgroundView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    this.backgroundView.setScaleType(ImageView.ScaleType.FIT_XY);
    addView(this.backgroundView);
  }

  public void addLoadingBubbleView(RelativeLayout.LayoutParams paramLayoutParams)
  {
    this.gl = new GLRenderer(getContext());
    addView(this.gl, paramLayoutParams);
  }

  public void addLoadingImageView(RelativeLayout.LayoutParams paramLayoutParams)
  {
    this.clipdrawable = ((ClipDrawable)getContext().getResources().getDrawable(R.drawable.loading_large_drawable));
    this.clipdrawable.setLevel(1000);
    this.loadingView = new ImageView(getContext());
    this.loadingView.setImageDrawable(this.clipdrawable);
    addView(this.loadingView, paramLayoutParams);
  }

  public void addNetworkImageView(boolean paramBoolean)
  {
    this.imageView = ((NetworkImageView)LayoutInflater.from(getContext()).inflate(R.layout.fullscreen_photo_view, this, false));
    if ((this.imageView instanceof ZoomNetworkImageView))
    {
      ((ZoomNetworkImageView)this.imageView).setImageZoomable(paramBoolean);
      ((ZoomNetworkImageView)this.imageView).setOnViewTapListener(this);
      ((ZoomNetworkImageView)this.imageView).setLoadingListener(this);
    }
    this.imageView.isPhoto = true;
    addView(this.imageView);
  }

  public void creatLoadingLayout(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.isClickable = paramBoolean3;
    this.scale = (getContext().getResources().getDisplayMetrics().density / this.DEFAULT_DENSITY);
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams((int)(this.scale * this.LOADING_IMAGE_WIDTH), (int)(this.scale * this.LOADING_IMAGE_HEIGHT));
    localLayoutParams.addRule(13, -1);
    if (paramBoolean1)
      addBackgroundView();
    addNetworkImageView(paramBoolean2);
    addLoadingImageView(localLayoutParams);
    addLoadingBubbleView(localLayoutParams);
  }

  public NetworkImageView getImageView()
  {
    return this.imageView;
  }

  public void onClick(View paramView)
  {
    if ((paramView == this.imageView) && (this.isClickable))
      ((Activity)getContext()).finish();
  }

  public void onLoadingPause()
  {
    this.gl.setVisibility(8);
    this.gl.onPause();
    this.timer.cancel();
    this.loadingView.setVisibility(8);
    if (this.backgroundView != null)
      this.backgroundView.setVisibility(8);
  }

  public void onLoadingProgress(int paramInt1, int paramInt2)
  {
    this.timer.cancel();
    paramInt1 = paramInt1 / paramInt2 * 10000;
    if (this.clipdrawable.getLevel() < paramInt1)
      this.clipdrawable.setLevel(paramInt1);
  }

  public void onLoadingResume()
  {
    this.timer = new Timer();
    this.gl.setVisibility(0);
    this.gl.onResume();
    this.loadingView.setVisibility(0);
    if (this.backgroundView != null)
      this.backgroundView.setVisibility(0);
    int i = (int)(new Random().nextFloat() * 3000.0F + 4000.0F);
    1 local1 = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        if (paramMessage.what == 1)
          LoadingLayout.this.clipdrawable.setLevel(LoadingLayout.this.clipdrawable.getLevel() + 600);
      }
    };
    this.timer.schedule(new TimerTask(local1, i)
    {
      public void run()
      {
        Message localMessage = Message.obtain();
        localMessage.what = 1;
        this.val$handler.sendMessage(localMessage);
        if (LoadingLayout.this.clipdrawable.getLevel() >= this.val$max)
          LoadingLayout.this.timer.cancel();
      }
    }
    , 0L, 200L);
  }

  public void onViewTap(View paramView, float paramFloat1, float paramFloat2)
  {
    ((Activity)getContext()).finish();
  }

  public boolean sameAs(Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    if ((paramBitmap1.getWidth() != paramBitmap2.getWidth()) || (paramBitmap1.getHeight() != paramBitmap2.getHeight()))
      return false;
    int i = 0;
    while (true)
    {
      if (i >= paramBitmap1.getWidth())
        break label79;
      int j = 0;
      while (true)
      {
        if (j >= paramBitmap1.getHeight())
          break label72;
        if (paramBitmap1.getPixel(i, j) != paramBitmap2.getPixel(i, j))
          break;
        j += 1;
      }
      label72: i += 1;
    }
    label79: return true;
  }

  public void setImageUrl(String paramString)
  {
    this.imageView.setImage(paramString);
  }

  public void setLoadingBackgruond(Bitmap paramBitmap)
  {
    Object localObject = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_loading);
    Bitmap localBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_error);
    if ((paramBitmap != null) && (!sameAs(paramBitmap, (Bitmap)localObject)) && (!sameAs(paramBitmap, localBitmap)))
    {
      int j = paramBitmap.getWidth();
      int i = paramBitmap.getHeight();
      while ((j < this.LOADING_IMAGE_WIDTH) || (i < this.LOADING_IMAGE_HEIGHT))
      {
        j *= 2;
        i *= 2;
      }
      localObject = new RelativeLayout.LayoutParams((int)(this.scale * j), (int)(this.scale * i));
      ((RelativeLayout.LayoutParams)localObject).addRule(13, -1);
      this.backgroundView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.backgroundView.setImageBitmap(paramBitmap);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.loading.LoadingLayout
 * JD-Core Version:    0.6.0
 */