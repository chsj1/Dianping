package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CheckinItemFramelayout extends FrameLayout
{
  boolean isLoading = false;
  boolean isShow;
  int showHeightBottom;
  int showHeightTop;

  public CheckinItemFramelayout(Context paramContext)
  {
    super(paramContext);
  }

  public CheckinItemFramelayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public CheckinItemFramelayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public int getShowBottom()
  {
    return this.showHeightBottom;
  }

  public int getShowTop()
  {
    return this.showHeightTop;
  }

  public boolean isLoading()
  {
    return this.isLoading;
  }

  public boolean isShow()
  {
    return this.isShow;
  }

  public void setLoading(boolean paramBoolean)
  {
    this.isLoading = paramBoolean;
  }

  public void setShowBottom(int paramInt)
  {
    this.showHeightBottom = paramInt;
  }

  public void setShowTop(int paramInt)
  {
    this.showHeightTop = paramInt;
  }

  public void setShowable(boolean paramBoolean)
  {
    this.isShow = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CheckinItemFramelayout
 * JD-Core Version:    0.6.0
 */