package com.dianping.util.animation;

import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class AnimationHelper
{
  protected static final Interpolator accelerator = new AccelerateInterpolator();
  protected static final Interpolator decelerator = new DecelerateInterpolator();
  protected FragmentActivity mActivity;

  protected AnimationHelper(FragmentActivity paramFragmentActivity)
  {
    this.mActivity = paramFragmentActivity;
  }

  public static AnimationHelper createInstance(FragmentActivity paramFragmentActivity)
  {
    if (Build.VERSION.SDK_INT >= 14)
      return new AnimationHelperICS(paramFragmentActivity);
    if (Build.VERSION.SDK_INT >= 11)
      return new AnimationHelperHoneycomb(paramFragmentActivity);
    return new AnimationHelperBase(paramFragmentActivity);
  }

  public abstract void rotate3dFragment(Fragment paramFragment1, Fragment paramFragment2, AnimatorListener paramAnimatorListener);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.animation.AnimationHelper
 * JD-Core Version:    0.6.0
 */