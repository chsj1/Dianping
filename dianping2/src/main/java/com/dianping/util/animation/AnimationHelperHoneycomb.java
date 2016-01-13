package com.dianping.util.animation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

public class AnimationHelperHoneycomb extends AnimationHelper
{
  protected AnimationHelperHoneycomb(FragmentActivity paramFragmentActivity)
  {
    super(paramFragmentActivity);
  }

  public void rotate3dFragment(Fragment paramFragment1, Fragment paramFragment2, AnimatorListener paramAnimatorListener)
  {
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(paramFragment1.getView(), "rotationY", new float[] { 0.0F, 90.0F });
    localObjectAnimator1.setDuration(500L);
    localObjectAnimator1.setInterpolator(accelerator);
    localObjectAnimator1.start();
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(paramFragment2.getView(), "rotationY", new float[] { -90.0F, 0.0F });
    localObjectAnimator2.setDuration(500L);
    localObjectAnimator2.setInterpolator(decelerator);
    localObjectAnimator1.addListener(new AnimatorListenerAdapter(localObjectAnimator2, paramFragment1, paramFragment2)
    {
      public void onAnimationEnd(Animator paramAnimator)
      {
        this.val$invisToVis.start();
        AnimationHelperHoneycomb.this.mActivity.getSupportFragmentManager().beginTransaction().hide(this.val$visibleFragment).show(this.val$invisibleFragment).commit();
      }
    });
    localObjectAnimator2.addListener(new Animator.AnimatorListener(paramAnimatorListener)
    {
      public void onAnimationCancel(Animator paramAnimator)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationCancel();
      }

      public void onAnimationEnd(Animator paramAnimator)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationEnd();
      }

      public void onAnimationRepeat(Animator paramAnimator)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationRepeat();
      }

      public void onAnimationStart(Animator paramAnimator)
      {
        if (this.val$animatorListener != null)
          this.val$animatorListener.onAnimationStart();
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.animation.AnimationHelperHoneycomb
 * JD-Core Version:    0.6.0
 */