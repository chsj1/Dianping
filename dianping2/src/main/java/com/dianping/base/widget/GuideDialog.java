package com.dianping.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.style;

public class GuideDialog extends Dialog
{
  Context context;
  View layoutRes;
  int layoutResID;
  private boolean mTouch2Dismiss = true;

  public GuideDialog(Context paramContext, int paramInt)
  {
    super(paramContext, R.style.dialog_fullscreen);
    this.context = paramContext;
    this.layoutResID = paramInt;
  }

  public GuideDialog(Context paramContext, View paramView)
  {
    super(paramContext, R.style.dialog_fullscreen);
    this.context = paramContext;
    this.layoutRes = paramView;
  }

  public static int dip2px(Context paramContext, float paramFloat)
  {
    return (int)(paramFloat * paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.layoutRes != null)
    {
      super.setContentView(this.layoutRes);
      return;
    }
    super.setContentView(this.layoutResID);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
      return super.onKeyDown(paramInt, paramKeyEvent);
    if (paramInt == 82)
    {
      cancel();
      return super.onKeyDown(paramInt, paramKeyEvent);
    }
    return false;
  }

  protected void onStart()
  {
    if (findViewById(R.id.guideLay) != null)
      playAnimation();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mTouch2Dismiss)
      cancel();
    return super.onTouchEvent(paramMotionEvent);
  }

  protected void playAnimation()
  {
    new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        paramMessage = AnimationUtils.loadAnimation(GuideDialog.this.context, R.anim.gradient_enter);
        View localView = GuideDialog.this.findViewById(R.id.guideLay);
        if (localView != null)
          localView.startAnimation(paramMessage);
      }
    }
    .sendEmptyMessage(1);
  }

  public void setTouchToDismiss(boolean paramBoolean)
  {
    this.mTouch2Dismiss = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.GuideDialog
 * JD-Core Version:    0.6.0
 */