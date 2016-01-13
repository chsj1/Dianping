package com.dianping.widget;

import android.content.Context;
import android.util.AttributeSet;

public class NetworkFullImageView extends NetworkImageView
{
  public NetworkFullImageView(Context paramContext)
  {
    super(paramContext);
  }

  public NetworkFullImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NetworkFullImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void setPlaceHolder(int paramInt)
  {
    if (!this.currentPlaceholder)
      setScaleType(getScaleType());
    this.currentPlaceholder = true;
    super.setImageResource(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.NetworkFullImageView
 * JD-Core Version:    0.6.0
 */