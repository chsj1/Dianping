package com.dianping.widget.pulltorefresh;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public abstract interface ILoadingLayout
{
  public abstract void setBackgroundColor(int paramInt);

  public abstract void setLastUpdatedLabel(CharSequence paramCharSequence);

  public abstract void setLoadingDrawable(Drawable paramDrawable);

  public abstract void setLoadingLayoutBackground(Drawable paramDrawable);

  public abstract void setLoadingVisible(boolean paramBoolean);

  public abstract void setPullLabel(CharSequence paramCharSequence);

  public abstract void setRefreshingLabel(CharSequence paramCharSequence);

  public abstract void setReleaseLabel(CharSequence paramCharSequence);

  public abstract void setTextTypeface(Typeface paramTypeface);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.ILoadingLayout
 * JD-Core Version:    0.6.0
 */