package com.dianping.membercard.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import com.dianping.widget.NetworkImageView;

public class RecyclingNetworkImageView extends NetworkImageView
{
  public RecyclingNetworkImageView(Context paramContext)
  {
    super(paramContext);
  }

  public RecyclingNetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public RecyclingNetworkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private static void notifyDrawable(Drawable paramDrawable, boolean paramBoolean)
  {
    if ((paramDrawable instanceof RecyclingBitmapDrawable))
      ((RecyclingBitmapDrawable)paramDrawable).setIsDisplayed(paramBoolean);
    while (true)
    {
      return;
      if (!(paramDrawable instanceof LayerDrawable))
        continue;
      paramDrawable = (LayerDrawable)paramDrawable;
      int i = 0;
      int j = paramDrawable.getNumberOfLayers();
      while (i < j)
      {
        notifyDrawable(paramDrawable.getDrawable(i), paramBoolean);
        i += 1;
      }
    }
  }

  protected void onDetachedFromWindow()
  {
    setImageDrawable(null);
    super.onDetachedFromWindow();
  }

  public void setImageDrawable(Drawable paramDrawable)
  {
    Drawable localDrawable = getDrawable();
    super.setImageDrawable(paramDrawable);
    notifyDrawable(paramDrawable, true);
    notifyDrawable(localDrawable, false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.RecyclingNetworkImageView
 * JD-Core Version:    0.6.0
 */