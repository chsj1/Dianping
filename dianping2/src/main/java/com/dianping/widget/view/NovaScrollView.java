package com.dianping.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import com.dianping.app.DPActivity;

public class NovaScrollView extends ScrollView
{
  private DPActivity dpActivity = (DPActivity)GAHelper.instance().getDpActivity(getContext());
  private boolean mIsFling;

  public NovaScrollView(Context paramContext)
  {
    super(paramContext);
  }

  public NovaScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NovaScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.dpActivity == null);
    do
      return;
    while ((!this.mIsFling) || ((Math.abs(paramInt2 - paramInt4) >= 2) && (paramInt2 < getMeasuredHeight()) && (paramInt2 != 0)));
    postDelayed(new Runnable()
    {
      public void run()
      {
        NovaScrollView.this.dpActivity.showGAView(NovaScrollView.this.dpActivity.getPageName());
      }
    }
    , 1000L);
    this.mIsFling = false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 1)
      this.mIsFling = true;
    return super.onTouchEvent(paramMotionEvent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.NovaScrollView
 * JD-Core Version:    0.6.0
 */