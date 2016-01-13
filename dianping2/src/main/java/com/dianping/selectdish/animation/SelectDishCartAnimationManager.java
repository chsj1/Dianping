package com.dianping.selectdish.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import com.dianping.selectdish.ui.SelectDishesDetailInfoActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.string;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import java.util.ArrayList;

public class SelectDishCartAnimationManager
{
  private AddDishAniListener addDishAniListener;
  ObjectAnimator animator;
  private WindowManager appWindowManager;
  private Context mContext;
  private View movingView;

  public SelectDishCartAnimationManager(Context paramContext)
  {
    this.mContext = paramContext;
    this.animator = new ObjectAnimator();
    this.animator.setTarget(this);
    this.animator.setPropertyName("evaluativePosition");
  }

  private WindowManager getApplicationWindowManager()
  {
    if (this.appWindowManager == null)
      this.appWindowManager = ((WindowManager)this.mContext.getSystemService("window"));
    return this.appWindowManager;
  }

  private void setEvaluativePosition(Point paramPoint)
  {
    if (this.addDishAniListener != null)
      this.addDishAniListener.onAnimationPositionChanged(this.animator, paramPoint);
  }

  public static void startAddRecommendAnimation(View paramView)
  {
    AnimationSet localAnimationSet = new AnimationSet(true);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, -0.5F);
    localAlphaAnimation.setDuration(1000L);
    localTranslateAnimation.setDuration(1000L);
    localAnimationSet.addAnimation(localAlphaAnimation);
    localAnimationSet.addAnimation(localTranslateAnimation);
    localAnimationSet.setInterpolator(new DecelerateInterpolator());
    localAnimationSet.setAnimationListener(new Animation.AnimationListener(paramView)
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        this.val$recommendAddView.setVisibility(8);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    paramView.setVisibility(0);
    paramView.startAnimation(localAnimationSet);
  }

  public static void startScaleAnimationOnView(View paramView)
  {
    ScaleAnimation localScaleAnimation = new ScaleAnimation(0.67F, 1.0F, 0.67F, 1.0F, 1, 0.5F, 1, 0.5F);
    localScaleAnimation.setDuration(50L);
    localScaleAnimation.setAnimationListener(new Animation.AnimationListener(paramView)
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        paramAnimation = new ScaleAnimation(1.1F, 1.0F, 1.1F, 1.0F, 1, 0.5F, 1, 0.5F);
        paramAnimation.setDuration(20L);
        this.val$view.startAnimation(paramAnimation);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    paramView.startAnimation(localScaleAnimation);
  }

  public void cancel()
  {
    this.animator.cancel();
  }

  public boolean isRunning()
  {
    return this.animator.isRunning();
  }

  public void setAddDishAniListener(AddDishAniListener paramAddDishAniListener)
  {
    this.addDishAniListener = paramAddDishAniListener;
  }

  public void start()
  {
    this.animator.start();
  }

  public void startAddDishAnimation(View paramView1, View paramView2, View paramView3, int paramInt1, int paramInt2)
  {
    this.movingView = paramView1;
    paramView1 = new Rect();
    ((Activity)this.mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(paramView1);
    int i = paramView1.top;
    if (this.mContext.getString(R.string.sd_detail_top_buy_tag).equals(paramView2.getTag()))
      paramInt2 = -ViewUtils.dip2px(this.mContext, 50.0F) - i;
    paramView1 = new int[2];
    paramView3.getLocationOnScreen(paramView1);
    paramView3 = new Point(paramView1[0], paramView1[1] - i);
    paramView2.getLocationInWindow(paramView1);
    paramView1[1] += paramInt2;
    if ((this.mContext instanceof SelectDishesDetailInfoActivity));
    double d1;
    double d2;
    for (paramView1 = new Point(paramView1[0], paramView1[1] + ViewUtils.dip2px(this.mContext, 50.0F)); ; paramView1 = new Point(paramView1[0], paramView1[1] - i))
    {
      paramView2 = new ArrayList(3);
      paramView2.add(paramView1);
      paramView2.add(paramView3);
      d1 = 0.5D * (paramView1.x + paramView3.x);
      d2 = 0.5D * (paramView1.y + paramView1.y);
      if (paramView1.x != paramView3.x)
        break;
      d1 -= ViewUtils.dip2px(this.mContext, 50.0F);
      this.animator.setObjectValues(paramView2.toArray());
      this.animator.setDuration(paramInt1);
      this.animator.setEvaluator(new PathEvaluator(d1, d2));
      this.animator.setInterpolator(new AccelerateDecelerateInterpolator());
      this.animator.addListener(this.addDishAniListener);
      this.animator.start();
      return;
    }
    double d3 = 1.0D * (paramView3.y - paramView1.y) / (paramView3.x - paramView1.x);
    double d4 = d1 / d3;
    if ((d3 < 0.0D) && (d3 > -3.0D))
      d1 -= ViewUtils.dip2px(this.mContext, (float)(80.0D * Math.abs(d3)));
    while (true)
    {
      d2 = d2 + d4 - d1 / d3;
      break;
      d1 -= ViewUtils.dip2px(this.mContext, 120.0F);
    }
  }

  public class AddDishAniListener
    implements SelectDishCartAnimationManager.ParabolicAnimatorListener
  {
    public AddDishAniListener()
    {
    }

    public void onAnimationCancel(Animator paramAnimator)
    {
    }

    public void onAnimationEnd(Animator paramAnimator)
    {
      SelectDishCartAnimationManager.this.movingView.clearAnimation();
    }

    public void onAnimationPositionChanged(Animator paramAnimator, Point paramPoint)
    {
      if (SelectDishCartAnimationManager.this.movingView != null)
      {
        SelectDishCartAnimationManager.this.movingView.setX(paramPoint.x);
        SelectDishCartAnimationManager.this.movingView.setY(paramPoint.y);
      }
    }

    public void onAnimationRepeat(Animator paramAnimator)
    {
    }

    public void onAnimationStart(Animator paramAnimator)
    {
    }
  }

  public static abstract interface ParabolicAnimatorListener extends Animator.AnimatorListener
  {
    public abstract void onAnimationPositionChanged(Animator paramAnimator, Point paramPoint);
  }

  private class PathEvaluator
    implements TypeEvaluator<Point>
  {
    private double thirdX;
    private double thirdY;

    public PathEvaluator(double arg2, double arg4)
    {
      this.thirdX = ???;
      Object localObject;
      this.thirdY = localObject;
    }

    public Point evaluate(float paramFloat, Point paramPoint1, Point paramPoint2)
    {
      paramFloat = 1.0F * paramFloat;
      float f = 1.0F - paramFloat;
      return new Point((int)(f * f * paramPoint1.x + 2.0F * f * paramFloat * this.thirdX + paramFloat * paramFloat * paramPoint2.x), (int)(f * f * paramPoint1.y + 2.0F * f * paramFloat * this.thirdY + paramFloat * paramFloat * paramPoint2.y));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.animation.SelectDishCartAnimationManager
 * JD-Core Version:    0.6.0
 */