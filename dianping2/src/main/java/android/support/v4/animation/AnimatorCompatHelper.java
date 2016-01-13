package android.support.v4.animation;

import android.os.Build.VERSION;
import android.view.View;

public abstract class AnimatorCompatHelper
{
  static AnimatorProvider IMPL;

  static
  {
    if (Build.VERSION.SDK_INT >= 12)
    {
      IMPL = new HoneycombMr1AnimatorCompatProvider();
      return;
    }
    IMPL = new DonutAnimatorCompatProvider();
  }

  public static void clearInterpolator(View paramView)
  {
    IMPL.clearInterpolator(paramView);
  }

  public static ValueAnimatorCompat emptyValueAnimator()
  {
    return IMPL.emptyValueAnimator();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.animation.AnimatorCompatHelper
 * JD-Core Version:    0.6.0
 */