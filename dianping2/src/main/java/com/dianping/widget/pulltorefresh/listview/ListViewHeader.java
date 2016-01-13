package com.dianping.widget.pulltorefresh.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class ListViewHeader extends LinearLayout
{
  public static final int STATE_NORMAL = 0;
  public static final int STATE_READY = 1;
  public static final int STATE_REFRESHING = 2;
  public int mState = 0;

  public ListViewHeader(Context paramContext)
  {
    super(paramContext);
  }

  public ListViewHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected abstract int getDefaultDrawableResId();

  public abstract int getVisiableHeight();

  public abstract void onPullImpl(float paramFloat);

  public abstract void reset();

  public void setLoadingDrawable(Drawable paramDrawable)
  {
  }

  public abstract void setState(int paramInt);

  public abstract void setVisiableHeight(int paramInt);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.listview.ListViewHeader
 * JD-Core Version:    0.6.0
 */