package com.dianping.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.view.View;

@TargetApi(9)
public final class OverscrollHelper
{
  static final float DEFAULT_OVERSCROLL_SCALE = 1.0F;
  static final String LOG_TAG = "OverscrollHelper";

  static boolean isAndroidOverScrollEnabled(View paramView)
  {
    return paramView.getOverScrollMode() != 2;
  }

  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, float paramFloat, boolean paramBoolean)
  {
    PullToRefreshBase.Mode localMode;
    switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[paramPullToRefreshBase.getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      paramInt2 = paramInt4;
      paramInt1 = paramPullToRefreshBase.getScrollY();
      if ((!paramPullToRefreshBase.isPullToRefreshOverScrollEnabled()) || (paramPullToRefreshBase.isRefreshing()))
        break;
      localMode = paramPullToRefreshBase.getMode();
      if ((localMode.permitsPullToRefresh()) && (!paramBoolean) && (paramInt3 != 0))
      {
        paramInt2 = paramInt3 + paramInt2;
        if (paramInt2 < 0 - paramInt6)
        {
          if (!localMode.showHeaderLoadingLayout())
            break;
          if (paramInt1 == 0)
            paramPullToRefreshBase.setState(PullToRefreshBase.State.OVERSCROLLING, new boolean[0]);
          paramPullToRefreshBase.setHeaderScroll((int)((paramInt1 + paramInt2) * paramFloat));
        }
      }
    case 1:
    }
    label177: 
    do
    {
      do
      {
        do
        {
          return;
          paramInt3 = paramInt1;
          paramInt1 = paramPullToRefreshBase.getScrollX();
          break;
          if (paramInt2 <= paramInt5 + paramInt6)
            break label177;
        }
        while (!localMode.showFooterLoadingLayout());
        if (paramInt1 == 0)
          paramPullToRefreshBase.setState(PullToRefreshBase.State.OVERSCROLLING, new boolean[0]);
        paramPullToRefreshBase.setHeaderScroll((int)((paramInt1 + paramInt2 - paramInt5) * paramFloat));
        return;
      }
      while ((Math.abs(paramInt2) > paramInt6) && (Math.abs(paramInt2 - paramInt5) > paramInt6));
      paramPullToRefreshBase.setState(PullToRefreshBase.State.RESET, new boolean[0]);
      return;
    }
    while ((!paramBoolean) || (PullToRefreshBase.State.OVERSCROLLING != paramPullToRefreshBase.getState()));
    paramPullToRefreshBase.setState(PullToRefreshBase.State.RESET, new boolean[0]);
  }

  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    overScrollBy(paramPullToRefreshBase, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, 1.0F, paramBoolean);
  }

  public static void overScrollBy(PullToRefreshBase<?> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    overScrollBy(paramPullToRefreshBase, paramInt1, paramInt2, paramInt3, paramInt4, 0, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.OverscrollHelper
 * JD-Core Version:    0.6.0
 */