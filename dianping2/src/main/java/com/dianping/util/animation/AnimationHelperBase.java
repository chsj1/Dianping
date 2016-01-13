package com.dianping.util.animation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimationHelperBase extends AnimationHelper
{
  protected AnimationHelperBase(FragmentActivity paramFragmentActivity)
  {
    super(paramFragmentActivity);
  }

  public void rotate3dFragment(Fragment paramFragment1, Fragment paramFragment2, AnimatorListener paramAnimatorListener)
  {
    View localView = paramFragment1.getView();
    float f1 = localView.getWidth() / 2.0F;
    float f2 = localView.getHeight() / 2.0F;
    Rotate3dAnimation localRotate3dAnimation1 = new Rotate3dAnimation(0.0F, 90.0F, f1, f2, 0.0F, true);
    Rotate3dAnimation localRotate3dAnimation2 = new Rotate3dAnimation(-90.0F, 0.0F, f1, f2, 0.0F, false);
    localRotate3dAnimation1.setDuration(500L);
    localRotate3dAnimation1.setFillAfter(true);
    localRotate3dAnimation1.setInterpolator(accelerator);
    localRotate3dAnimation2.setDuration(500L);
    localRotate3dAnimation2.setFillAfter(true);
    localRotate3dAnimation2.setInterpolator(decelerator);
    localRotate3dAnimation1.setAnimationListener(new Animation.AnimationListener(paramAnimatorListener, paramFragment1, paramFragment2, localRotate3dAnimation2)
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        AnimationHelperBase.this.mActivity.getSupportFragmentManager().beginTransaction().hide(this.val$visibleFragment).show(this.val$invisibleFragment).commit();
        this.val$invisibleFragment.getView().startAnimation(this.val$deceleratorRotation);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationRepeat();
      }

      public void onAnimationStart(Animation paramAnimation)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationStart();
      }
    });
    localRotate3dAnimation2.setAnimationListener(new Animation.AnimationListener(paramAnimatorListener)
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationEnd();
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    localView.startAnimation(localRotate3dAnimation1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.animation.AnimationHelperBase
 * JD-Core Version:    0.6.0
 */