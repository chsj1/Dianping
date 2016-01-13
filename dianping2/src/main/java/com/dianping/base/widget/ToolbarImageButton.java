package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.ImageButton;

public class ToolbarImageButton extends ImageButton
  implements View.OnTouchListener
{
  public ToolbarImageButton(Context paramContext)
  {
    super(paramContext);
    setOnTouchListener(this);
  }

  public ToolbarImageButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOnTouchListener(this);
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
      setAlpha(123);
    if ((paramMotionEvent.getAction() == 1) || (paramMotionEvent.getAction() == 3))
    {
      setAlpha(255);
      if ((getParent() != null) && (getParent().getParent() != null))
        ((View)getParent().getParent()).performClick();
    }
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ToolbarImageButton
 * JD-Core Version:    0.6.0
 */