package com.dianping.widget.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Orientation;

public class LoadLoadingLayout extends FlipLoadingLayout
{
  public LoadLoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray)
  {
    super(paramContext, paramMode, paramOrientation, paramTypedArray);
  }

  protected int getDefaultVerticalLayout()
  {
    return R.layout.load_vertical;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.LoadLoadingLayout
 * JD-Core Version:    0.6.0
 */