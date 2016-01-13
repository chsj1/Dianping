package com.dianping.widget.pulltorefresh;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import com.dianping.widget.pulltorefresh.internal.LoadingLayout;
import java.util.HashSet;
import java.util.Iterator;

public class LoadingLayoutProxy
  implements ILoadingLayout
{
  private final HashSet<LoadingLayout> mLoadingLayouts = new HashSet();

  public void addLayout(LoadingLayout paramLoadingLayout)
  {
    if (paramLoadingLayout != null)
      this.mLoadingLayouts.add(paramLoadingLayout);
  }

  public void setBackgroundColor(int paramInt)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setBackgroundColor(paramInt);
  }

  public void setLastUpdatedLabel(CharSequence paramCharSequence)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setLastUpdatedLabel(paramCharSequence);
  }

  public void setLoadingDrawable(Drawable paramDrawable)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setLoadingDrawable(paramDrawable);
  }

  public void setLoadingLayoutBackground(Drawable paramDrawable)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setLoadingLayoutBackground(paramDrawable);
  }

  public void setLoadingVisible(boolean paramBoolean)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setLoadingVisible(paramBoolean);
  }

  public void setPullLabel(CharSequence paramCharSequence)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setPullLabel(paramCharSequence);
  }

  public void setRefreshingLabel(CharSequence paramCharSequence)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setRefreshingLabel(paramCharSequence);
  }

  public void setReleaseLabel(CharSequence paramCharSequence)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setReleaseLabel(paramCharSequence);
  }

  public void setTextTypeface(Typeface paramTypeface)
  {
    Iterator localIterator = this.mLoadingLayouts.iterator();
    while (localIterator.hasNext())
      ((LoadingLayout)localIterator.next()).setTextTypeface(paramTypeface);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.LoadingLayoutProxy
 * JD-Core Version:    0.6.0
 */