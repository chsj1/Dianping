package com.dianping.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.dianping.v1.R.id;
import com.dianping.widget.MyScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView>
{
  public PullToRefreshScrollView(Context paramContext)
  {
    super(paramContext);
  }

  public PullToRefreshScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public PullToRefreshScrollView(Context paramContext, PullToRefreshBase.Mode paramMode)
  {
    super(paramContext, paramMode);
  }

  public PullToRefreshScrollView(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.AnimationStyle paramAnimationStyle)
  {
    super(paramContext, paramMode, paramAnimationStyle);
  }

  protected ScrollView createRefreshableView(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramContext = new InternalScrollView(paramContext, paramAttributeSet);
    paramContext.setId(R.id.scrollview);
    return paramContext;
  }

  public final PullToRefreshBase.Orientation getPullToRefreshScrollDirection()
  {
    return PullToRefreshBase.Orientation.VERTICAL;
  }

  protected boolean isReadyForPullEnd()
  {
    View localView = ((ScrollView)this.mRefreshableView).getChildAt(0);
    if (localView != null)
      return ((ScrollView)this.mRefreshableView).getScrollY() >= localView.getHeight() - getHeight();
    return false;
  }

  protected boolean isReadyForPullStart()
  {
    return ((ScrollView)this.mRefreshableView).getScrollY() == 0;
  }

  @TargetApi(9)
  final class InternalScrollView extends MyScrollView
  {
    public InternalScrollView(Context paramAttributeSet, AttributeSet arg3)
    {
      super(localAttributeSet);
    }

    private int getScrollRange()
    {
      int i = 0;
      if (getChildCount() > 0)
        i = Math.max(0, getChildAt(0).getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
      return i;
    }

    protected boolean overScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean)
    {
      boolean bool = super.overScrollBy(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBoolean);
      OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, paramInt1, paramInt3, paramInt2, paramInt4, getScrollRange(), paramBoolean);
      return bool;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.PullToRefreshScrollView
 * JD-Core Version:    0.6.0
 */