package com.dianping.takeaway.entity;

import android.graphics.Point;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import java.util.ArrayList;

public class ParabolicAnimator
{
  ObjectAnimator animator = new ObjectAnimator();
  private ParabolicAnimatorListener animatorListener;

  public ParabolicAnimator()
  {
    this.animator.setTarget(this);
    this.animator.setPropertyName("evaluativePosition");
  }

  private void setEvaluativePosition(Point paramPoint)
  {
    if (this.animatorListener != null)
      this.animatorListener.onAnimationPositionChanged(this.animator, paramPoint);
  }

  public void cancel()
  {
    this.animator.cancel();
  }

  public boolean isRunning()
  {
    return this.animator.isRunning();
  }

  public void setListener(ParabolicAnimatorListener paramParabolicAnimatorListener)
  {
    this.animator.removeAllListeners();
    this.animatorListener = paramParabolicAnimatorListener;
    this.animator.addListener(this.animatorListener);
  }

  public void setPathAndDuration(Point paramPoint1, Point paramPoint2, int paramInt)
  {
    ArrayList localArrayList = new ArrayList(3);
    localArrayList.add(paramPoint1);
    localArrayList.add(paramPoint2);
    int j = Math.min(100, Math.abs(paramPoint1.x - paramPoint2.x) / 4);
    if (paramPoint1.x > paramPoint2.x);
    for (int i = 1; ; i = -1)
    {
      double d2 = i * j;
      double d1 = paramPoint1.x - d2;
      d2 = paramPoint1.y - Math.abs(d2) * Math.tan(0.0174532925199433D * 25.0D);
      d1 = ((paramPoint1.y - d2) / (paramPoint1.x - d1) - (d2 - paramPoint2.y) / (d1 - paramPoint2.x)) / ((Math.pow(paramPoint1.x, 2.0D) - Math.pow(d1, 2.0D)) / (paramPoint1.x - d1) - (Math.pow(d1, 2.0D) - Math.pow(paramPoint2.x, 2.0D)) / (d1 - paramPoint2.x));
      d2 = (paramPoint1.y - paramPoint2.y - (Math.pow(paramPoint1.x, 2.0D) - Math.pow(paramPoint2.x, 2.0D)) * d1) / (paramPoint1.x - paramPoint2.x);
      double d3 = paramPoint1.y;
      double d4 = Math.pow(paramPoint1.x, 2.0D);
      double d5 = paramPoint1.x;
      this.animator.setObjectValues(localArrayList.toArray());
      this.animator.setDuration(paramInt);
      this.animator.setEvaluator(new PathEvaluator(d1, d2, d3 - d4 * d1 - d5 * d2));
      this.animator.setInterpolator(new AccelerateDecelerateInterpolator());
      return;
    }
  }

  public void start()
  {
    this.animator.start();
  }

  public static abstract interface ParabolicAnimatorListener extends Animator.AnimatorListener
  {
    public abstract void onAnimationPositionChanged(Animator paramAnimator, Point paramPoint);
  }

  private class PathEvaluator
    implements TypeEvaluator<Point>
  {
    private double firstVar;
    private double secondVar;
    private double thirdVar;

    PathEvaluator(double arg2, double arg4, double arg6)
    {
      this.firstVar = ???;
      Object localObject1;
      this.secondVar = localObject1;
      Object localObject2;
      this.thirdVar = localObject2;
    }

    public Point evaluate(float paramFloat, Point paramPoint1, Point paramPoint2)
    {
      double d1 = paramPoint1.x + (paramPoint2.x - paramPoint1.x) * paramFloat;
      double d2 = this.firstVar;
      double d3 = Math.pow(d1, 2.0D);
      double d4 = this.secondVar;
      double d5 = this.thirdVar;
      return new Point((int)d1, (int)(d2 * d3 + d4 * d1 + d5));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.ParabolicAnimator
 * JD-Core Version:    0.6.0
 */