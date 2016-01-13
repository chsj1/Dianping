package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.AnimationStyle;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.pulltorefresh.internal.LoadingLayout;

public class DPPullToRefreshScrollView extends PullToRefreshScrollView
{
  public DPPullToRefreshScrollView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public DPPullToRefreshScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public DPPullToRefreshScrollView(Context paramContext, PullToRefreshBase.Mode paramMode)
  {
    super(paramContext, paramMode);
    init();
  }

  public DPPullToRefreshScrollView(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.AnimationStyle paramAnimationStyle)
  {
    super(paramContext, paramMode, paramAnimationStyle);
    init();
  }

  private void init()
  {
    setMode(PullToRefreshBase.Mode.BOTH);
    ILoadingLayout localILoadingLayout = getLoadingLayoutProxy(true, false);
    localILoadingLayout.setLoadingLayoutBackground(getResources().getDrawable(R.drawable.common_bkg_dropdown));
    localILoadingLayout.setLoadingDrawable(getResources().getDrawable(R.drawable.transparent));
    localILoadingLayout.setBackgroundColor(getResources().getColor(R.color.gray_light_background));
    getFooterLayout().setLoadingDrawable(getResources().getDrawable(R.drawable.transparent));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DPPullToRefreshScrollView
 * JD-Core Version:    0.6.0
 */