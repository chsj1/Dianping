package com.dianping.takeaway.entity;

import android.graphics.Point;
import android.view.View;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TakeawayAddDishAnimations
{
  private final HashMap<Animator, TakeawayAddDishAnimationInfo> animationHashMap = new HashMap(5);
  private TakeawayAddDishAnimationsListener animationListener;
  public ParabolicAnimator.ParabolicAnimatorListener parabolicListener = new ParabolicAnimator.ParabolicAnimatorListener()
  {
    private TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo animationInfoForAnimator(Animator paramAnimator)
    {
      if (paramAnimator != null)
        return (TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo)TakeawayAddDishAnimations.this.animationHashMap.get(paramAnimator);
      return null;
    }

    public void onAnimationCancel(Animator paramAnimator)
    {
    }

    public void onAnimationEnd(Animator paramAnimator)
    {
      TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo localTakeawayAddDishAnimationInfo = animationInfoForAnimator(paramAnimator);
      TakeawayAddDishAnimations.this.animationHashMap.remove(paramAnimator);
      if (TakeawayAddDishAnimations.this.animationListener != null)
        TakeawayAddDishAnimations.this.animationListener.onAnimationEnd(localTakeawayAddDishAnimationInfo);
    }

    public void onAnimationPositionChanged(Animator paramAnimator, Point paramPoint)
    {
      if (TakeawayAddDishAnimations.this.animationListener != null)
        TakeawayAddDishAnimations.this.animationListener.onAnimationPositionChanged(animationInfoForAnimator(paramAnimator), paramPoint);
    }

    public void onAnimationRepeat(Animator paramAnimator)
    {
    }

    public void onAnimationStart(Animator paramAnimator)
    {
      if (TakeawayAddDishAnimations.this.animationListener != null)
        TakeawayAddDishAnimations.this.animationListener.onAnimationStart(animationInfoForAnimator(paramAnimator));
    }
  };

  public TakeawayAddDishAnimations(TakeawayAddDishAnimationsListener paramTakeawayAddDishAnimationsListener)
  {
    this.animationListener = paramTakeawayAddDishAnimationsListener;
  }

  public void cancelAllActiveAddDishAnimations()
  {
    monitorenter;
    try
    {
      Iterator localIterator = new ArrayList(this.animationHashMap.keySet()).iterator();
      while (localIterator.hasNext())
        ((Animator)localIterator.next()).cancel();
    }
    finally
    {
      monitorexit;
    }
    monitorexit;
  }

  public void startAddDishAnimation(TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo)
  {
    if (paramTakeawayAddDishAnimationInfo == null)
      return;
    ParabolicAnimator localParabolicAnimator = new ParabolicAnimator();
    localParabolicAnimator.setListener(this.parabolicListener);
    localParabolicAnimator.setPathAndDuration(paramTakeawayAddDishAnimationInfo.startPoint, paramTakeawayAddDishAnimationInfo.endPoint, paramTakeawayAddDishAnimationInfo.duration);
    this.animationHashMap.put(localParabolicAnimator.animator, paramTakeawayAddDishAnimationInfo);
    localParabolicAnimator.start();
  }

  public static class TakeawayAddDishAnimationInfo
  {
    public DishOperation dishOperation;
    public int duration;
    public Point endPoint;
    public View movingView;
    public Point startPoint;

    public TakeawayAddDishAnimationInfo(Point paramPoint1, Point paramPoint2, int paramInt)
    {
      this.startPoint = paramPoint1;
      this.endPoint = paramPoint2;
      this.duration = paramInt;
    }
  }

  public static abstract interface TakeawayAddDishAnimationsListener
  {
    public abstract void onAnimationCancel(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo);

    public abstract void onAnimationEnd(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo);

    public abstract void onAnimationPositionChanged(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo, Point paramPoint);

    public abstract void onAnimationStart(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayAddDishAnimations
 * JD-Core Version:    0.6.0
 */