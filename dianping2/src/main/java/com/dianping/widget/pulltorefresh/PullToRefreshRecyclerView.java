package com.dianping.widget.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.styleable;
import com.dianping.widget.pulltorefresh.listview.DperListViewHeader;
import com.dianping.widget.pulltorefresh.listview.FlipListViewHeader;
import com.dianping.widget.pulltorefresh.listview.ListViewFooter;
import com.dianping.widget.pulltorefresh.listview.ListViewHeader;
import com.dianping.widget.pulltorefresh.listview.RotateListViewHeader;
import com.dianping.widget.recyclerview.WrapRecyclerView;

public class PullToRefreshRecyclerView extends WrapRecyclerView
{
  public static final int DISABLE = 0;
  public static final int MODE_LOAD_FLIP = 2;
  public static final int MODE_NORAL_DPER = 0;
  public static final int MODE_SKIN_ROTATE = 1;
  private static final float OFFSET_RADIO = 1.8F;
  private static final int PULL_LOAD_MORE_DELTA = 50;
  public static final int PULL_NO_REFRESH = 1;
  public static final int PULL_REFRESH = 2;
  private static final int SCROLLBACK_FOOTER = 1;
  private static final int SCROLLBACK_HEADER = 0;
  private static final int SCROLL_DURATION = 400;
  private boolean isHeaderRefresh = false;
  private boolean isScrool = false;
  private ListViewFooter mFooterView;
  private Handler mHandler;
  private TextView mHeaderTimeView;
  private ListViewHeader mHeaderView;
  private LinearLayout mHeaderViewContent;
  private int mHeaderViewHeight;
  private boolean mIsFooterReady = false;
  private float mLastY = -1.0F;
  private OnLoadMoreListener mLoadMoreListener;
  private int mMode = 0;
  private int mPullLoadState = 1;
  private boolean mPullLoading;
  private int mPullRefreshState = 2;
  private boolean mPullRefreshing = false;
  private OnPullScrollListener mPullScrollListener;
  private OnRefreshListener mRefreshListener;
  private int mScrollBack;
  private AbsListView.OnScrollListener mScrollListener;
  private Scroller mScroller;
  private LinearLayoutManager manager;
  private OnScrollChangedListener onScrollChangedListener;

  public PullToRefreshRecyclerView(Context paramContext)
  {
    super(paramContext);
    initWithContext(paramContext, null);
  }

  public PullToRefreshRecyclerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initWithContext(paramContext, paramAttributeSet);
  }

  public PullToRefreshRecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initWithContext(paramContext, paramAttributeSet);
  }

  private void initWithContext(Context paramContext, AttributeSet paramAttributeSet)
  {
    setLayoutManager(new LinearLayoutManager(paramContext));
    setOverScrollMode(2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PullToRefreshListView);
    if (paramAttributeSet.hasValue(R.styleable.PullToRefreshListView_headerMode))
      this.mMode = paramAttributeSet.getInteger(R.styleable.PullToRefreshListView_headerMode, 0);
    this.mScroller = new Scroller(paramContext, new DecelerateInterpolator());
    this.mHandler = new Handler();
    this.mFooterView = new ListViewFooter(paramContext);
    resetHeader(this.mMode);
    setPullLoadEnable(0);
  }

  private void invokeOnScrolling()
  {
    if ((this.mScrollListener instanceof OnRefreshScrollListener))
      ((OnRefreshScrollListener)this.mScrollListener).onScrolling(this);
  }

  private void resetFooterHeight()
  {
    int i = this.mFooterView.getBottomMargin();
    if (i > 0)
    {
      this.mScrollBack = 1;
      this.mScroller.startScroll(0, i, 0, -i, 400);
      invalidate();
    }
  }

  private void resetHeaderHeight()
  {
    int k = this.mHeaderView.getVisiableHeight();
    if (k == 0);
    do
      return;
    while ((this.mPullRefreshing) && (k <= this.mHeaderViewHeight));
    int j = 0;
    int i = j;
    if (this.mPullRefreshing)
    {
      i = j;
      if (k > this.mHeaderViewHeight)
        i = this.mHeaderViewHeight;
    }
    this.mScrollBack = 0;
    this.mScroller.startScroll(0, k, 0, i - k, 400);
    this.isScrool = true;
    invalidate();
  }

  private void startLoadMore()
  {
    this.mPullLoading = true;
    this.mFooterView.setState(2);
    if (this.mLoadMoreListener != null)
      this.mLoadMoreListener.onLoadMore(this);
  }

  private void updateFooterHeight(float paramFloat)
  {
    int i = this.mFooterView.getBottomMargin() + (int)paramFloat;
    if ((this.mPullLoadState == 2) && (!this.mPullLoading))
    {
      if (i <= 50)
        break label49;
      this.mFooterView.setState(1);
    }
    while (true)
    {
      this.mFooterView.setBottomMargin(i);
      return;
      label49: this.mFooterView.setState(0);
    }
  }

  private void updateHeaderHeight(float paramFloat)
  {
    this.mHeaderView.setVisiableHeight((int)paramFloat + this.mHeaderView.getVisiableHeight());
    if ((this.mPullRefreshState == 2) && (!this.mPullRefreshing))
    {
      if (this.mHeaderView.getVisiableHeight() <= this.mHeaderViewHeight)
        break label80;
      if (this.mPullScrollListener != null)
        this.mPullScrollListener.onPullScrollRefresh(1.0F);
      this.mHeaderView.setState(1);
    }
    while (true)
    {
      this.manager.scrollToPosition(0);
      return;
      label80: paramFloat = this.mHeaderView.getVisiableHeight() / this.mHeaderViewHeight;
      this.mHeaderView.onPullImpl(paramFloat);
      this.mHeaderView.setState(0);
      if (this.mPullScrollListener == null)
        continue;
      this.mPullScrollListener.onPullScrollRefresh(paramFloat);
    }
  }

  public void computeScroll()
  {
    if (this.mScroller.computeScrollOffset())
      if (this.mScrollBack == 0)
      {
        this.mHeaderView.setVisiableHeight(this.mScroller.getCurrY());
        postInvalidate();
        invokeOnScrolling();
      }
    while (true)
    {
      super.computeScroll();
      return;
      this.mFooterView.setBottomMargin(this.mScroller.getCurrY());
      break;
      if ((this.isHeaderRefresh) && (this.mRefreshListener != null))
      {
        this.mRefreshListener.onRefresh(this);
        this.isHeaderRefresh = false;
        continue;
      }
      if ((!this.isScrool) || (this.mPullRefreshing))
        continue;
      this.mHeaderView.reset();
      this.isScrool = false;
    }
  }

  public boolean isPullToRefreshing()
  {
    return this.mPullRefreshing;
  }

  public void onLoadMoreComplete()
  {
    if (this.mPullLoading == true)
    {
      this.mPullLoading = false;
      this.mFooterView.setState(0);
    }
  }

  public void onRefreshComplete()
  {
    this.mHandler.postDelayed(new Runnable()
    {
      public void run()
      {
        if (PullToRefreshRecyclerView.this.mPullRefreshing == true)
        {
          PullToRefreshRecyclerView.access$202(PullToRefreshRecyclerView.this, false);
          PullToRefreshRecyclerView.this.resetHeaderHeight();
        }
      }
    }
    , 500L);
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.onScrollChangedListener != null)
      this.onScrollChangedListener.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mLastY == -1.0F)
      this.mLastY = paramMotionEvent.getRawY();
    switch (paramMotionEvent.getAction())
    {
    case 1:
    default:
      this.mLastY = -1.0F;
      if ((this.mPullRefreshState == 0) || ((this.manager.findFirstCompletelyVisibleItemPosition() != 1) && (this.manager.findFirstCompletelyVisibleItemPosition() != 0)))
        break;
      if ((this.mPullRefreshState == 2) && (this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) && (!this.mPullRefreshing))
      {
        this.mPullRefreshing = true;
        this.mHeaderView.setState(2);
        this.isHeaderRefresh = true;
      }
      resetHeaderHeight();
    case 0:
    case 2:
    }
    while (true)
    {
      return super.onTouchEvent(paramMotionEvent);
      this.mLastY = paramMotionEvent.getRawY();
      continue;
      float f = paramMotionEvent.getRawY() - this.mLastY;
      this.mLastY = paramMotionEvent.getRawY();
      if ((this.mPullRefreshState != 0) && ((this.manager.findFirstCompletelyVisibleItemPosition() == 1) || (this.manager.findFirstCompletelyVisibleItemPosition() == 0)) && ((f > 0.0F) || (this.mHeaderView.getVisiableHeight() > 0)))
      {
        updateHeaderHeight(f / 1.8F);
        invokeOnScrolling();
        continue;
      }
      if ((this.mPullLoadState == 0) || ((this.manager.findLastCompletelyVisibleItemPosition() != getAdapter().getItemCount() - 1) && (this.manager.findLastCompletelyVisibleItemPosition() != getAdapter().getItemCount() - 2)) || ((this.mFooterView.getBottomMargin() <= 0) && (f >= 0.0F)))
        continue;
      updateFooterHeight(-f / 1.8F);
      continue;
      if ((this.mPullLoadState == 0) || ((this.manager.findLastCompletelyVisibleItemPosition() != getAdapter().getItemCount() - 1) && (this.manager.findLastCompletelyVisibleItemPosition() != getAdapter().getItemCount() - 2)))
        continue;
      if ((this.mPullLoadState == 2) && (this.mFooterView.getBottomMargin() > 50) && (!this.mPullLoading))
        startLoadMore();
      resetFooterHeight();
    }
  }

  public void resetHeader(int paramInt)
  {
    if (this.mHeaderView != null);
    try
    {
      removeHeaderView(this.mHeaderView);
      if (paramInt == 0)
      {
        this.mHeaderView = new DperListViewHeader(getContext());
        this.mHeaderViewContent = ((LinearLayout)this.mHeaderView.findViewById(R.id.listview_header_content));
        this.mHeaderTimeView = ((TextView)this.mHeaderView.findViewById(R.id.listview_header_time));
        addHeaderView(this.mHeaderView);
        this.mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
          public void onGlobalLayout()
          {
            PullToRefreshRecyclerView.access$002(PullToRefreshRecyclerView.this, ViewUtils.dip2px(PullToRefreshRecyclerView.this.getContext(), 80.0F));
            PullToRefreshRecyclerView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          }
        });
        return;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        continue;
        if (paramInt == 2)
        {
          this.mHeaderView = new FlipListViewHeader(getContext());
          continue;
        }
        this.mHeaderView = new RotateListViewHeader(getContext());
      }
    }
  }

  public void setAdapter(RecyclerView.Adapter paramAdapter)
  {
    if (!this.mIsFooterReady)
      this.mIsFooterReady = true;
    super.setAdapter(paramAdapter);
  }

  public void setLayoutManager(RecyclerView.LayoutManager paramLayoutManager)
  {
    if (!(paramLayoutManager instanceof LinearLayoutManager))
      throw new RuntimeException("PullToRefreshRecyclerView must set LinearLayoutManager");
    super.setLayoutManager(paramLayoutManager);
    this.manager = ((LinearLayoutManager)paramLayoutManager);
  }

  public void setLoadingDrawable(Drawable paramDrawable)
  {
    this.mHeaderView.setLoadingDrawable(paramDrawable);
  }

  public void setMode(PullToRefreshBase.Mode paramMode)
  {
    if (paramMode == PullToRefreshBase.Mode.DISABLED)
    {
      setPullRefreshEnable(0);
      setPullLoadEnable(0);
    }
    do
      return;
    while (paramMode != PullToRefreshBase.Mode.PULL_FROM_START);
    setPullRefreshEnable(2);
    setPullLoadEnable(0);
  }

  public void setOnLoadMoreListener(OnLoadMoreListener paramOnLoadMoreListener)
  {
    this.mLoadMoreListener = paramOnLoadMoreListener;
  }

  public void setOnPullScrollListener(OnPullScrollListener paramOnPullScrollListener)
  {
    this.mPullScrollListener = paramOnPullScrollListener;
  }

  public void setOnRefreshListener(OnRefreshListener paramOnRefreshListener)
  {
    this.mRefreshListener = paramOnRefreshListener;
  }

  public void setOnScrollChangedListener(OnScrollChangedListener paramOnScrollChangedListener)
  {
    this.onScrollChangedListener = paramOnScrollChangedListener;
  }

  public void setPullLoadEnable(int paramInt)
  {
    this.mPullLoadState = paramInt;
    if (this.mPullLoadState == 2)
    {
      this.mPullLoading = false;
      this.mFooterView.show();
      this.mFooterView.setState(0);
      this.mFooterView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          PullToRefreshRecyclerView.this.startLoadMore();
        }
      });
      return;
    }
    this.mFooterView.hide();
    this.mFooterView.setOnClickListener(null);
  }

  public void setPullRefreshEnable(int paramInt)
  {
    this.mPullRefreshState = paramInt;
    if (paramInt == 2)
    {
      this.mHeaderViewContent.setVisibility(0);
      return;
    }
    this.mHeaderViewContent.setVisibility(4);
  }

  public void setRefreshTime(String paramString)
  {
    this.mHeaderTimeView.setText(paramString);
  }

  public void setRefreshing()
  {
    if (this.mPullRefreshState == 2)
    {
      this.mPullRefreshing = true;
      this.mHeaderView.setState(2);
      if (this.mRefreshListener != null)
        this.mRefreshListener.onRefresh(this);
      this.mScrollBack = 0;
      this.mScroller.startScroll(0, 0, 0, this.mHeaderViewHeight, 400);
      invalidate();
    }
  }

  public static abstract interface OnLoadMoreListener
  {
    public abstract void onLoadMore(PullToRefreshRecyclerView paramPullToRefreshRecyclerView);
  }

  public static abstract interface OnPullScrollListener
  {
    public abstract void onPullScrollRefresh(float paramFloat);
  }

  public static abstract interface OnRefreshListener
  {
    public abstract void onRefresh(PullToRefreshRecyclerView paramPullToRefreshRecyclerView);
  }

  public static abstract interface OnRefreshScrollListener extends AbsListView.OnScrollListener
  {
    public abstract void onScrolling(View paramView);
  }

  public static abstract interface OnScrollChangedListener
  {
    public abstract void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.PullToRefreshRecyclerView
 * JD-Core Version:    0.6.0
 */