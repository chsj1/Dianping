package com.dianping.base.widget;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.dianping.widget.view.NovaImageButton;

public class CustomImageButton extends NovaImageButton
  implements View.OnTouchListener
{
  public CustomImageButton(Context paramContext)
  {
    super(paramContext);
    setOnTouchListener(this);
  }

  public CustomImageButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOnTouchListener(this);
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
      setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
    if ((paramMotionEvent.getAction() == 1) || (paramMotionEvent.getAction() == 3))
      setColorFilter(null);
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CustomImageButton
 * JD-Core Version:    0.6.0
 */