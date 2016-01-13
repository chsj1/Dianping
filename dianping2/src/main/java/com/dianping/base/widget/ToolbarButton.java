package com.dianping.base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.widget.view.NovaRelativeLayout;

public class ToolbarButton extends NovaRelativeLayout
{
  public ToolbarButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setIcon(Drawable paramDrawable)
  {
    ((ToolbarImageButton)findViewById(16908294)).setImageDrawable(paramDrawable);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    ((TextView)findViewById(16908308)).setText(paramCharSequence);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ToolbarButton
 * JD-Core Version:    0.6.0
 */