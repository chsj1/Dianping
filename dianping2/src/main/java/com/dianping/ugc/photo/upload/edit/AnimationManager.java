package com.dianping.ugc.photo.upload.edit;

import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationManager
{
  private AnimationListener animationListener;
  private AnimationSet animationSet;
  private View appearView = null;
  private View clickView;
  private View disappearView = null;
  private int duration = 1000;

  public AnimationManager()
  {
  }

  public AnimationManager(View paramView, int paramInt)
  {
    this.disappearView = paramView;
    this.duration = paramInt;
  }

  public static void generateAlphaAnimation(View paramView, float paramFloat1, float paramFloat2, int paramInt)
  {
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    localAlphaAnimation.setDuration(paramInt);
    paramView.startAnimation(localAlphaAnimation);
  }

  public void selectCategoryAnimation(View paramView1, View paramView2, View paramView3, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.clickView = paramView1;
    this.animationSet = new AnimationSet(true);
    this.animationSet.setInterpolator(new AccelerateInterpolator());
    this.animationSet.setAnimationListener(this.animationListener);
    this.animationSet.setFillAfter(true);
    int i = paramView1.getMeasuredWidth();
    float f = 1.0F * paramView3.getMeasuredWidth() / i;
    paramView1 = new TranslateAnimation(0, 0.0F, 0, paramArrayOfInt2[0] - paramArrayOfInt1[0], 0, 0.0F, 0, paramArrayOfInt2[1] - paramArrayOfInt1[1]);
    paramView1.setDuration(this.duration);
    this.animationSet.addAnimation(paramView1);
    if (Build.VERSION.SDK_INT <= 10);
    for (paramView1 = new ScaleAnimation(1.0F, f, 1.0F, f, 0, paramArrayOfInt2[0] - paramArrayOfInt1[0], 0, paramArrayOfInt2[1] - paramArrayOfInt1[1]); ; paramView1 = new ScaleAnimation(1.0F, f, 1.0F, f, 0, paramArrayOfInt2[0], 0, paramArrayOfInt2[1]))
    {
      paramView1.setDuration(this.duration);
      this.animationSet.addAnimation(paramView1);
      paramView2.startAnimation(this.animationSet);
      return;
    }
  }

  public void setAnimationListener(AnimationListener paramAnimationListener)
  {
    this.animationListener = paramAnimationListener;
  }

  public void setDuration(int paramInt)
  {
    this.duration = paramInt;
  }

  class AnimationListener
    implements Animation.AnimationListener
  {
    AnimationListener()
    {
    }

    public void onAnimationEnd(Animation paramAnimation)
    {
      if (AnimationManager.this.appearView != null)
      {
        paramAnimation = new ScaleAnimation(1.1F, 1.0F, 1.1F, 1.0F, 1, 0.5F, 1, 0.5F);
        paramAnimation.setDuration(20L);
        AnimationManager.this.appearView.startAnimation(paramAnimation);
      }
    }

    public void onAnimationRepeat(Animation paramAnimation)
    {
    }

    public void onAnimationStart(Animation paramAnimation)
    {
      if (AnimationManager.this.disappearView != null)
      {
        AnimationManager.this.clickView.setVisibility(4);
        paramAnimation = new AlphaAnimation(1.0F, 0.0F);
        paramAnimation.setDuration(AnimationManager.this.duration);
        paramAnimation.setAnimationListener(new AnimationManager.AnimationListener.1(this));
        AnimationManager.this.disappearView.startAnimation(paramAnimation);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.upload.edit.AnimationManager
 * JD-Core Version:    0.6.0
 */