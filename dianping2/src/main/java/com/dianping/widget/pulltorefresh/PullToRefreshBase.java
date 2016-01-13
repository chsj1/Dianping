package com.dianping.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.v1.R.styleable;
import com.dianping.widget.pulltorefresh.internal.DperLoadingLayout;
import com.dianping.widget.pulltorefresh.internal.FlipLoadingLayout;
import com.dianping.widget.pulltorefresh.internal.LoadLoadingLayout;
import com.dianping.widget.pulltorefresh.internal.LoadingLayout;
import com.dianping.widget.pulltorefresh.internal.RotateLoadingLayout;
import com.dianping.widget.pulltorefresh.internal.Utils;
import com.dianping.widget.pulltorefresh.internal.ViewCompat;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout
  implements IPullToRefresh<T>
{
  static final boolean DEBUG = false;
  static final int DEMO_SCROLL_INTERVAL = 225;
  static final float FRICTION = 2.0F;
  static final String LOG_TAG = "PullToRefresh";
  public static final int SMOOTH_SCROLL_DURATION_MS = 200;
  public static final int SMOOTH_SCROLL_LONG_DURATION_MS = 325;
  static final String STATE_CURRENT_MODE = "ptr_current_mode";
  static final String STATE_MODE = "ptr_mode";
  static final String STATE_SCROLLING_REFRESHING_ENABLED = "ptr_disable_scrolling";
  static final String STATE_SHOW_REFRESHING_VIEW = "ptr_show_refreshing_view";
  static final String STATE_STATE = "ptr_state";
  static final String STATE_SUPER = "ptr_super";
  static final boolean USE_HW_LAYERS = false;
  private Mode mCurrentMode;
  private PullToRefreshBase<T>.SmoothScrollRunnable mCurrentSmoothScrollRunnable;
  private boolean mFilterTouchEvents = true;
  private LoadingLayout mFooterLayout;
  private LoadingLayout mHeaderLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private boolean mIsBeingDragged = false;
  private float mLastMotionX;
  private float mLastMotionY;
  private boolean mLayoutVisibilityChangesEnabled = true;
  private AnimationStyle mLoadingAnimationStyle = AnimationStyle.getDefault();
  private Mode mMode = Mode.getDefault();
  private OnPullEventListener<T> mOnPullEventListener;
  private OnPullScrollListener<T> mOnPullScrollListener;
  private OnRefreshListener<T> mOnRefreshListener;
  private OnRefreshListener2<T> mOnRefreshListener2;
  private boolean mOverScrollEnabled = true;
  T mRefreshableView;
  private FrameLayout mRefreshableViewWrapper;
  private Interpolator mScrollAnimationInterpolator;
  private boolean mScrollingWhileRefreshingEnabled = false;
  private boolean mShowViewWhileRefreshing = true;
  private State mState = State.RESET;
  private int mTouchSlop;

  public PullToRefreshBase(Context paramContext)
  {
    super(paramContext);
    init(paramContext, null);
  }

  public PullToRefreshBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }

  public PullToRefreshBase(Context paramContext, Mode paramMode)
  {
    super(paramContext);
    this.mMode = paramMode;
    init(paramContext, null);
  }

  public PullToRefreshBase(Context paramContext, Mode paramMode, AnimationStyle paramAnimationStyle)
  {
    super(paramContext);
    this.mMode = paramMode;
    this.mLoadingAnimationStyle = paramAnimationStyle;
    init(paramContext, null);
  }

  private void addRefreshableView(Context paramContext, T paramT)
  {
    this.mRefreshableViewWrapper = new FrameLayout(paramContext);
    this.mRefreshableViewWrapper.addView(paramT, -1, -1);
    addViewInternal(this.mRefreshableViewWrapper, new LinearLayout.LayoutParams(-1, -1));
  }

  private void callRefreshListener()
  {
    if (this.mOnRefreshListener != null)
      this.mOnRefreshListener.onRefresh(this);
    do
    {
      do
        return;
      while (this.mOnRefreshListener2 == null);
      if (this.mCurrentMode != Mode.PULL_FROM_START)
        continue;
      this.mOnRefreshListener2.onPullDownToRefresh(this);
      return;
    }
    while (this.mCurrentMode != Mode.PULL_FROM_END);
    this.mOnRefreshListener2.onPullUpToRefresh(this);
  }

  private LinearLayout.LayoutParams getLoadingLayoutLayoutParams()
  {
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      return new LinearLayout.LayoutParams(-1, -2);
    case 1:
    }
    return new LinearLayout.LayoutParams(-2, -1);
  }

  private int getMaximumPullScroll()
  {
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      return Math.round(getHeight() / 2.0F);
    case 1:
    }
    return Math.round(getWidth() / 2.0F);
  }

  private void init(Context paramContext, AttributeSet paramAttributeSet)
  {
    TypedArray localTypedArray;
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      setOrientation(1);
      setGravity(17);
      this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
      localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PullToRefresh);
      if (localTypedArray.hasValue(R.styleable.PullToRefresh_ptrMode))
        this.mMode = Mode.mapIntToValue(localTypedArray.getInteger(R.styleable.PullToRefresh_ptrMode, 0));
      if (localTypedArray.hasValue(R.styleable.PullToRefresh_ptrAnimationStyle))
        this.mLoadingAnimationStyle = AnimationStyle.mapIntToValue(localTypedArray.getInteger(R.styleable.PullToRefresh_ptrAnimationStyle, 0));
      this.mRefreshableView = createRefreshableView(paramContext, paramAttributeSet);
      addRefreshableView(paramContext, this.mRefreshableView);
      this.mHeaderLayout = createLoadingLayout(paramContext, Mode.PULL_FROM_START, localTypedArray);
      this.mFooterLayout = createLoadingLayout(paramContext, Mode.PULL_FROM_END, localTypedArray);
      if (localTypedArray.hasValue(R.styleable.PullToRefresh_ptrRefreshableViewBackground))
      {
        paramContext = localTypedArray.getDrawable(R.styleable.PullToRefresh_ptrRefreshableViewBackground);
        if (paramContext == null)
          break;
        this.mRefreshableView.setBackgroundDrawable(paramContext);
      }
    case 1:
    }
    while (true)
    {
      if (localTypedArray.hasValue(R.styleable.PullToRefresh_ptrOverScroll))
        this.mOverScrollEnabled = localTypedArray.getBoolean(R.styleable.PullToRefresh_ptrOverScroll, true);
      if (localTypedArray.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled))
        this.mScrollingWhileRefreshingEnabled = localTypedArray.getBoolean(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled, false);
      handleStyledAttributes(localTypedArray);
      localTypedArray.recycle();
      updateUIForMode();
      return;
      setOrientation(0);
      break;
      if (!localTypedArray.hasValue(R.styleable.PullToRefresh_ptrAdapterViewBackground))
        continue;
      Utils.warnDeprecation("ptrAdapterViewBackground", "ptrRefreshableViewBackground");
      paramContext = localTypedArray.getDrawable(R.styleable.PullToRefresh_ptrAdapterViewBackground);
      if (paramContext == null)
        continue;
      this.mRefreshableView.setBackgroundDrawable(paramContext);
    }
  }

  private boolean isReadyForPull()
  {
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mMode.ordinal()])
    {
    case 3:
    default:
    case 2:
    case 1:
    case 4:
    }
    do
    {
      return false;
      return isReadyForPullStart();
      return isReadyForPullEnd();
    }
    while ((!isReadyForPullEnd()) && (!isReadyForPullStart()));
    return true;
  }

  private void pullEvent()
  {
    float f1;
    float f2;
    int j;
    int i;
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      f1 = this.mInitialMotionY;
      f2 = this.mLastMotionY;
      switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()])
      {
      default:
        j = Math.round(Math.min(f1 - f2, 0.0F) / 2.0F);
        i = getHeaderSize();
        label87: setHeaderScroll(j);
        if ((j != 0) && (!isRefreshing()))
        {
          f1 = Math.abs(j) / i;
          switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()])
          {
          default:
            this.mHeaderLayout.onPull(f1);
            label152: if ((this.mState != State.PULL_TO_REFRESH) && (i >= Math.abs(j)))
              setState(State.PULL_TO_REFRESH, new boolean[0]);
          case 1:
          }
        }
      case 1:
      }
    case 1:
    }
    do
    {
      return;
      f1 = this.mInitialMotionX;
      f2 = this.mLastMotionX;
      break;
      j = Math.round(Math.max(f1 - f2, 0.0F) / 2.0F);
      i = getFooterSize();
      break label87;
      this.mFooterLayout.onPull(f1);
      break label152;
    }
    while ((this.mState != State.PULL_TO_REFRESH) || (i >= Math.abs(j)));
    setState(State.RELEASE_TO_REFRESH, new boolean[0]);
  }

  private final void smoothScrollTo(int paramInt, long paramLong)
  {
    smoothScrollTo(paramInt, paramLong, 0L, null);
  }

  private final void smoothScrollTo(int paramInt, long paramLong1, long paramLong2, OnSmoothScrollFinishedListener paramOnSmoothScrollFinishedListener)
  {
    if (this.mCurrentSmoothScrollRunnable != null)
      this.mCurrentSmoothScrollRunnable.stop();
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
    case 1:
    }
    for (int i = getScrollY(); ; i = getScrollX())
    {
      if (i != paramInt)
      {
        if (this.mScrollAnimationInterpolator == null)
          this.mScrollAnimationInterpolator = new DecelerateInterpolator();
        this.mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(i, paramInt, paramLong1, paramOnSmoothScrollFinishedListener);
        if (paramLong2 <= 0L)
          break;
        postDelayed(this.mCurrentSmoothScrollRunnable, paramLong2);
      }
      return;
    }
    post(this.mCurrentSmoothScrollRunnable);
  }

  private final void smoothScrollToAndBack(int paramInt)
  {
    smoothScrollTo(paramInt, 200L, 0L, new OnSmoothScrollFinishedListener()
    {
      public void onSmoothScrollFinished()
      {
        PullToRefreshBase.this.smoothScrollTo(0, 200L, 225L, null);
      }
    });
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    View localView = getRefreshableView();
    if ((localView instanceof ViewGroup))
    {
      ((ViewGroup)localView).addView(paramView, paramInt, paramLayoutParams);
      return;
    }
    throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
  }

  protected final void addViewInternal(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramInt, paramLayoutParams);
  }

  protected final void addViewInternal(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, -1, paramLayoutParams);
  }

  protected LoadingLayout createLoadingLayout(Context paramContext, Mode paramMode, TypedArray paramTypedArray)
  {
    paramContext = this.mLoadingAnimationStyle.createLoadingLayout(paramContext, paramMode, getPullToRefreshScrollDirection(), paramTypedArray);
    paramContext.setVisibility(4);
    return paramContext;
  }

  protected LoadingLayoutProxy createLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2)
  {
    LoadingLayoutProxy localLoadingLayoutProxy = new LoadingLayoutProxy();
    if ((paramBoolean1) && (this.mMode.showHeaderLoadingLayout()))
      localLoadingLayoutProxy.addLayout(this.mHeaderLayout);
    if ((paramBoolean2) && (this.mMode.showFooterLoadingLayout()))
      localLoadingLayoutProxy.addLayout(this.mFooterLayout);
    return localLoadingLayoutProxy;
  }

  protected abstract T createRefreshableView(Context paramContext, AttributeSet paramAttributeSet);

  public final boolean demo()
  {
    if ((this.mMode.showHeaderLoadingLayout()) && (isReadyForPullStart()))
    {
      smoothScrollToAndBack(-getHeaderSize() * 2);
      return true;
    }
    if ((this.mMode.showFooterLoadingLayout()) && (isReadyForPullEnd()))
    {
      smoothScrollToAndBack(getFooterSize() * 2);
      return true;
    }
    return false;
  }

  protected final void disableLoadingLayoutVisibilityChanges()
  {
    this.mLayoutVisibilityChangesEnabled = false;
  }

  public final Mode getCurrentMode()
  {
    return this.mCurrentMode;
  }

  public final boolean getFilterTouchEvents()
  {
    return this.mFilterTouchEvents;
  }

  protected final LoadingLayout getFooterLayout()
  {
    return this.mFooterLayout;
  }

  protected final int getFooterSize()
  {
    return this.mFooterLayout.getContentSize();
  }

  protected final LoadingLayout getHeaderLayout()
  {
    return this.mHeaderLayout;
  }

  protected final int getHeaderSize()
  {
    return this.mHeaderLayout.getContentSize();
  }

  public final ILoadingLayout getLoadingLayoutProxy()
  {
    return getLoadingLayoutProxy(true, true);
  }

  public final ILoadingLayout getLoadingLayoutProxy(boolean paramBoolean1, boolean paramBoolean2)
  {
    return createLoadingLayoutProxy(paramBoolean1, paramBoolean2);
  }

  public final Mode getMode()
  {
    return this.mMode;
  }

  public abstract Orientation getPullToRefreshScrollDirection();

  protected int getPullToRefreshScrollDuration()
  {
    return 200;
  }

  protected int getPullToRefreshScrollDurationLonger()
  {
    return 325;
  }

  public final T getRefreshableView()
  {
    return this.mRefreshableView;
  }

  protected FrameLayout getRefreshableViewWrapper()
  {
    return this.mRefreshableViewWrapper;
  }

  public final boolean getShowViewWhileRefreshing()
  {
    return this.mShowViewWhileRefreshing;
  }

  public final State getState()
  {
    return this.mState;
  }

  protected void handleStyledAttributes(TypedArray paramTypedArray)
  {
  }

  public final boolean isPullToRefreshEnabled()
  {
    return this.mMode.permitsPullToRefresh();
  }

  public final boolean isPullToRefreshOverScrollEnabled()
  {
    return (Build.VERSION.SDK_INT >= 9) && (this.mOverScrollEnabled) && (OverscrollHelper.isAndroidOverScrollEnabled(this.mRefreshableView));
  }

  protected abstract boolean isReadyForPullEnd();

  protected abstract boolean isReadyForPullStart();

  public final boolean isRefreshing()
  {
    return (this.mState == State.REFRESHING) || (this.mState == State.MANUAL_REFRESHING);
  }

  public final boolean isScrollingWhileRefreshingEnabled()
  {
    return this.mScrollingWhileRefreshingEnabled;
  }

  public final boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!isPullToRefreshEnabled())
      return false;
    int i = paramMotionEvent.getAction();
    if ((i == 3) || (i == 1))
    {
      this.mIsBeingDragged = false;
      return false;
    }
    if ((i != 0) && (this.mIsBeingDragged))
      return true;
    switch (i)
    {
    case 1:
    default:
    case 2:
    case 0:
    }
    while (true)
    {
      return this.mIsBeingDragged;
      if ((!this.mScrollingWhileRefreshingEnabled) && (isRefreshing()))
        return true;
      if (!isReadyForPull())
        continue;
      float f3 = paramMotionEvent.getY();
      float f4 = paramMotionEvent.getX();
      float f2;
      switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
      {
      default:
        f1 = f3 - this.mLastMotionY;
        f2 = f4 - this.mLastMotionX;
      case 1:
      }
      while (true)
      {
        float f5 = Math.abs(f1);
        if ((f5 <= this.mTouchSlop) || ((this.mFilterTouchEvents) && (f5 <= Math.abs(f2))))
          break;
        if ((!this.mMode.showHeaderLoadingLayout()) || (f1 < 1.0F) || (!isReadyForPullStart()))
          break label273;
        this.mLastMotionY = f3;
        this.mLastMotionX = f4;
        this.mIsBeingDragged = true;
        if (this.mMode != Mode.BOTH)
          break;
        this.mCurrentMode = Mode.PULL_FROM_START;
        break;
        f1 = f4 - this.mLastMotionX;
        f2 = f3 - this.mLastMotionY;
      }
      label273: if ((!this.mMode.showFooterLoadingLayout()) || (f1 > -1.0F) || (!isReadyForPullEnd()))
        continue;
      this.mLastMotionY = f3;
      this.mLastMotionX = f4;
      this.mIsBeingDragged = true;
      if (this.mMode != Mode.BOTH)
        continue;
      this.mCurrentMode = Mode.PULL_FROM_END;
      continue;
      if (!isReadyForPull())
        continue;
      float f1 = paramMotionEvent.getY();
      this.mInitialMotionY = f1;
      this.mLastMotionY = f1;
      f1 = paramMotionEvent.getX();
      this.mInitialMotionX = f1;
      this.mLastMotionX = f1;
      this.mIsBeingDragged = false;
    }
  }

  protected void onPtrRestoreInstanceState(Bundle paramBundle)
  {
  }

  protected void onPtrSaveInstanceState(Bundle paramBundle)
  {
  }

  protected void onPullToRefresh()
  {
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()])
    {
    default:
      return;
    case 1:
      this.mFooterLayout.pullToRefresh();
      return;
    case 2:
    }
    this.mHeaderLayout.pullToRefresh();
  }

  public final void onRefreshComplete()
  {
    if (isRefreshing())
      setState(State.RESET, new boolean[0]);
  }

  protected void onRefreshing(boolean paramBoolean)
  {
    if (this.mMode.showHeaderLoadingLayout())
      this.mHeaderLayout.refreshing();
    if (this.mMode.showFooterLoadingLayout())
      this.mFooterLayout.refreshing();
    if (paramBoolean)
    {
      if (this.mShowViewWhileRefreshing)
      {
        1 local1 = new OnSmoothScrollFinishedListener()
        {
          public void onSmoothScrollFinished()
          {
            PullToRefreshBase.this.callRefreshListener();
          }
        };
        switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()])
        {
        case 2:
        default:
          smoothScrollTo(-getHeaderSize(), local1);
          return;
        case 1:
        case 3:
        }
        smoothScrollTo(getFooterSize(), local1);
        return;
      }
      smoothScrollTo(0);
      return;
    }
    callRefreshListener();
  }

  protected void onReleaseToRefresh()
  {
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()])
    {
    default:
      return;
    case 1:
      this.mFooterLayout.releaseToRefresh();
      return;
    case 2:
    }
    this.mHeaderLayout.releaseToRefresh();
  }

  protected void onReset()
  {
    this.mIsBeingDragged = false;
    this.mLayoutVisibilityChangesEnabled = true;
    this.mHeaderLayout.reset();
    this.mFooterLayout.reset();
    smoothScrollTo(0);
  }

  protected final void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof Bundle))
    {
      paramParcelable = (Bundle)paramParcelable;
      setMode(Mode.mapIntToValue(paramParcelable.getInt("ptr_mode", 0)));
      this.mCurrentMode = Mode.mapIntToValue(paramParcelable.getInt("ptr_current_mode", 0));
      this.mScrollingWhileRefreshingEnabled = paramParcelable.getBoolean("ptr_disable_scrolling", false);
      this.mShowViewWhileRefreshing = paramParcelable.getBoolean("ptr_show_refreshing_view", true);
      super.onRestoreInstanceState(paramParcelable.getParcelable("ptr_super"));
      State localState = State.mapIntToValue(paramParcelable.getInt("ptr_state", 0));
      if ((localState == State.REFRESHING) || (localState == State.MANUAL_REFRESHING))
        setState(localState, new boolean[] { true });
      onPtrRestoreInstanceState(paramParcelable);
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }

  protected final Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    onPtrSaveInstanceState(localBundle);
    localBundle.putInt("ptr_state", this.mState.getIntValue());
    localBundle.putInt("ptr_mode", this.mMode.getIntValue());
    localBundle.putInt("ptr_current_mode", this.mCurrentMode.getIntValue());
    localBundle.putBoolean("ptr_disable_scrolling", this.mScrollingWhileRefreshingEnabled);
    localBundle.putBoolean("ptr_show_refreshing_view", this.mShowViewWhileRefreshing);
    localBundle.putParcelable("ptr_super", super.onSaveInstanceState());
    return localBundle;
  }

  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mOnPullScrollListener != null)
      this.mOnPullScrollListener.onPullScrollRefresh(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    refreshLoadingViewsSize();
    refreshRefreshableViewSize(paramInt1, paramInt2);
    post(new Runnable()
    {
      public void run()
      {
        PullToRefreshBase.this.requestLayout();
      }
    });
  }

  public final boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!isPullToRefreshEnabled());
    do
    {
      do
      {
        do
        {
          do
          {
            return false;
            if ((!this.mScrollingWhileRefreshingEnabled) && (isRefreshing()))
              return true;
          }
          while ((paramMotionEvent.getAction() == 0) && (paramMotionEvent.getEdgeFlags() != 0));
          switch (paramMotionEvent.getAction())
          {
          default:
            return false;
          case 0:
          case 2:
          case 1:
          case 3:
          }
        }
        while (!isReadyForPull());
        float f = paramMotionEvent.getY();
        this.mInitialMotionY = f;
        this.mLastMotionY = f;
        f = paramMotionEvent.getX();
        this.mInitialMotionX = f;
        this.mLastMotionX = f;
        return true;
      }
      while (!this.mIsBeingDragged);
      this.mLastMotionY = paramMotionEvent.getY();
      this.mLastMotionX = paramMotionEvent.getX();
      pullEvent();
      return true;
    }
    while (!this.mIsBeingDragged);
    this.mIsBeingDragged = false;
    if ((this.mState == State.RELEASE_TO_REFRESH) && ((this.mOnRefreshListener != null) || (this.mOnRefreshListener2 != null)))
    {
      setState(State.REFRESHING, new boolean[] { true });
      return true;
    }
    if (isRefreshing())
    {
      smoothScrollTo(0);
      return true;
    }
    setState(State.RESET, new boolean[0]);
    return true;
  }

  protected final void refreshLoadingViewsSize()
  {
    int i1 = (int)(getMaximumPullScroll() * 1.2F);
    int m = getPaddingLeft();
    int n = getPaddingTop();
    int k = getPaddingRight();
    int i = getPaddingBottom();
    int j;
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
      j = n;
    case 1:
    case 2:
    }
    while (true)
    {
      setPadding(m, j, k, i);
      return;
      if (this.mMode.showHeaderLoadingLayout())
        this.mHeaderLayout.setWidth(i1);
      for (j = -i1; ; j = 0)
      {
        if (!this.mMode.showFooterLoadingLayout())
          break label141;
        this.mFooterLayout.setWidth(i1);
        k = -i1;
        m = j;
        j = n;
        break;
      }
      label141: k = 0;
      m = j;
      j = n;
      continue;
      if (this.mMode.showHeaderLoadingLayout())
        this.mHeaderLayout.setHeight(i1);
      for (j = -i1; ; j = 0)
      {
        if (!this.mMode.showFooterLoadingLayout())
          break label206;
        this.mFooterLayout.setHeight(i1);
        i = -i1;
        break;
      }
      label206: i = 0;
    }
  }

  protected final void refreshRefreshableViewSize(int paramInt1, int paramInt2)
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mRefreshableViewWrapper.getLayoutParams();
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    do
    {
      do
        return;
      while (localLayoutParams.width == paramInt1);
      localLayoutParams.width = paramInt1;
      this.mRefreshableViewWrapper.requestLayout();
      return;
    }
    while (localLayoutParams.height == paramInt2);
    localLayoutParams.height = paramInt2;
    this.mRefreshableViewWrapper.requestLayout();
  }

  public final void setFilterTouchEvents(boolean paramBoolean)
  {
    this.mFilterTouchEvents = paramBoolean;
  }

  @TargetApi(11)
  protected final void setHeaderScroll(int paramInt)
  {
    int i = getMaximumPullScroll();
    paramInt = Math.min(i, Math.max(-i, paramInt));
    if (this.mLayoutVisibilityChangesEnabled)
    {
      if (paramInt >= 0)
        break label69;
      this.mHeaderLayout.setVisibility(0);
    }
    while (true)
      switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()])
      {
      default:
        return;
        label69: if (paramInt > 0)
        {
          this.mFooterLayout.setVisibility(0);
          continue;
        }
        this.mHeaderLayout.setVisibility(4);
        this.mFooterLayout.setVisibility(4);
      case 2:
      case 1:
      }
    scrollTo(0, paramInt);
    return;
    scrollTo(paramInt, 0);
  }

  @Deprecated
  public void setLastUpdatedLabel(CharSequence paramCharSequence)
  {
    getLoadingLayoutProxy().setLastUpdatedLabel(paramCharSequence);
  }

  public void setLongClickable(boolean paramBoolean)
  {
    getRefreshableView().setLongClickable(paramBoolean);
  }

  public final void setMode(Mode paramMode)
  {
    if (paramMode != this.mMode)
    {
      this.mMode = paramMode;
      updateUIForMode();
    }
  }

  public void setOnPullEventListener(OnPullEventListener<T> paramOnPullEventListener)
  {
    this.mOnPullEventListener = paramOnPullEventListener;
  }

  public final void setOnPullScrollListener(OnPullScrollListener<T> paramOnPullScrollListener)
  {
    this.mOnPullScrollListener = paramOnPullScrollListener;
  }

  public final void setOnRefreshListener(OnRefreshListener2<T> paramOnRefreshListener2)
  {
    this.mOnRefreshListener2 = paramOnRefreshListener2;
    this.mOnRefreshListener = null;
  }

  public final void setOnRefreshListener(OnRefreshListener<T> paramOnRefreshListener)
  {
    this.mOnRefreshListener = paramOnRefreshListener;
    this.mOnRefreshListener2 = null;
  }

  @Deprecated
  public final void setPullToRefreshEnabled(boolean paramBoolean)
  {
    if (paramBoolean);
    for (Mode localMode = Mode.getDefault(); ; localMode = Mode.DISABLED)
    {
      setMode(localMode);
      return;
    }
  }

  public final void setPullToRefreshOverScrollEnabled(boolean paramBoolean)
  {
    this.mOverScrollEnabled = paramBoolean;
  }

  public final void setRefreshing()
  {
    setRefreshing(true);
  }

  public final void setRefreshing(boolean paramBoolean)
  {
    if (!isRefreshing())
      setState(State.MANUAL_REFRESHING, new boolean[] { paramBoolean });
  }

  public void setScrollAnimationInterpolator(Interpolator paramInterpolator)
  {
    this.mScrollAnimationInterpolator = paramInterpolator;
  }

  public final void setScrollingWhileRefreshingEnabled(boolean paramBoolean)
  {
    this.mScrollingWhileRefreshingEnabled = paramBoolean;
  }

  public final void setShowViewWhileRefreshing(boolean paramBoolean)
  {
    this.mShowViewWhileRefreshing = paramBoolean;
  }

  final void setState(State paramState, boolean[] paramArrayOfBoolean)
  {
    this.mState = paramState;
    switch (4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$State[this.mState.ordinal()])
    {
    case 6:
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      if (this.mOnPullEventListener != null)
        this.mOnPullEventListener.onPullEvent(this, this.mState, this.mCurrentMode);
      return;
      onReset();
      continue;
      onPullToRefresh();
      continue;
      onReleaseToRefresh();
      continue;
      onRefreshing(paramArrayOfBoolean[0]);
    }
  }

  protected final void smoothScrollTo(int paramInt)
  {
    smoothScrollTo(paramInt, getPullToRefreshScrollDuration());
  }

  protected final void smoothScrollTo(int paramInt, OnSmoothScrollFinishedListener paramOnSmoothScrollFinishedListener)
  {
    smoothScrollTo(paramInt, getPullToRefreshScrollDuration(), 0L, paramOnSmoothScrollFinishedListener);
  }

  protected void updateUIForMode()
  {
    Object localObject = getLoadingLayoutLayoutParams();
    if (this == this.mHeaderLayout.getParent())
      removeView(this.mHeaderLayout);
    if (this.mMode.showHeaderLoadingLayout())
      addViewInternal(this.mHeaderLayout, 0, (ViewGroup.LayoutParams)localObject);
    if (this == this.mFooterLayout.getParent())
      removeView(this.mFooterLayout);
    if (this.mMode.showFooterLoadingLayout())
      addViewInternal(this.mFooterLayout, (ViewGroup.LayoutParams)localObject);
    refreshLoadingViewsSize();
    if (this.mMode != Mode.BOTH);
    for (localObject = this.mMode; ; localObject = Mode.PULL_FROM_START)
    {
      this.mCurrentMode = ((Mode)localObject);
      return;
    }
  }

  public static enum AnimationStyle
  {
    static
    {
      FLIP = new AnimationStyle("FLIP", 1);
      PEOPLE = new AnimationStyle("PEOPLE", 2);
      LOAD = new AnimationStyle("LOAD", 3);
      $VALUES = new AnimationStyle[] { ROTATE, FLIP, PEOPLE, LOAD };
    }

    static AnimationStyle getDefault()
    {
      return PEOPLE;
    }

    static AnimationStyle mapIntToValue(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return PEOPLE;
      case 1:
        return FLIP;
      case 2:
        return ROTATE;
      case 3:
      }
      return LOAD;
    }

    LoadingLayout createLoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray)
    {
      switch (PullToRefreshBase.4.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$AnimationStyle[ordinal()])
      {
      default:
        return new DperLoadingLayout(paramContext, paramMode, paramOrientation, paramTypedArray);
      case 2:
        return new FlipLoadingLayout(paramContext, paramMode, paramOrientation, paramTypedArray);
      case 3:
        return new RotateLoadingLayout(paramContext, paramMode, paramOrientation, paramTypedArray);
      case 4:
      }
      return new LoadLoadingLayout(paramContext, paramMode, paramOrientation, paramTypedArray);
    }
  }

  public static enum Mode
  {

    @Deprecated
    public static Mode PULL_DOWN_TO_REFRESH;

    @Deprecated
    public static Mode PULL_UP_TO_REFRESH;
    private int mIntValue;

    static
    {
      PULL_FROM_END = new Mode("PULL_FROM_END", 2, 2);
      BOTH = new Mode("BOTH", 3, 3);
      MANUAL_REFRESH_ONLY = new Mode("MANUAL_REFRESH_ONLY", 4, 4);
      $VALUES = new Mode[] { DISABLED, PULL_FROM_START, PULL_FROM_END, BOTH, MANUAL_REFRESH_ONLY };
      PULL_DOWN_TO_REFRESH = PULL_FROM_START;
      PULL_UP_TO_REFRESH = PULL_FROM_END;
    }

    private Mode(int paramInt)
    {
      this.mIntValue = paramInt;
    }

    static Mode getDefault()
    {
      return PULL_FROM_START;
    }

    static Mode mapIntToValue(int paramInt)
    {
      Mode[] arrayOfMode = values();
      int j = arrayOfMode.length;
      int i = 0;
      while (i < j)
      {
        Mode localMode = arrayOfMode[i];
        if (paramInt == localMode.getIntValue())
          return localMode;
        i += 1;
      }
      return getDefault();
    }

    int getIntValue()
    {
      return this.mIntValue;
    }

    boolean permitsPullToRefresh()
    {
      return (this != DISABLED) && (this != MANUAL_REFRESH_ONLY);
    }

    public boolean showFooterLoadingLayout()
    {
      return (this == PULL_FROM_END) || (this == BOTH) || (this == MANUAL_REFRESH_ONLY);
    }

    public boolean showHeaderLoadingLayout()
    {
      return (this == PULL_FROM_START) || (this == BOTH);
    }
  }

  public static abstract interface OnLastItemVisibleListener
  {
    public abstract void onLastItemVisible();
  }

  public static abstract interface OnPullEventListener<V extends View>
  {
    public abstract void onPullEvent(PullToRefreshBase<V> paramPullToRefreshBase, PullToRefreshBase.State paramState, PullToRefreshBase.Mode paramMode);
  }

  public static abstract interface OnPullScrollListener<V extends View>
  {
    public abstract void onPullScrollRefresh(PullToRefreshBase<V> paramPullToRefreshBase, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }

  public static abstract interface OnRefreshListener<V extends View>
  {
    public abstract void onRefresh(PullToRefreshBase<V> paramPullToRefreshBase);
  }

  public static abstract interface OnRefreshListener2<V extends View>
  {
    public abstract void onPullDownToRefresh(PullToRefreshBase<V> paramPullToRefreshBase);

    public abstract void onPullUpToRefresh(PullToRefreshBase<V> paramPullToRefreshBase);
  }

  static abstract interface OnSmoothScrollFinishedListener
  {
    public abstract void onSmoothScrollFinished();
  }

  public static enum Orientation
  {
    static
    {
      HORIZONTAL = new Orientation("HORIZONTAL", 1);
      $VALUES = new Orientation[] { VERTICAL, HORIZONTAL };
    }
  }

  final class SmoothScrollRunnable
    implements Runnable
  {
    private boolean mContinueRunning = true;
    private int mCurrentY = -1;
    private final long mDuration;
    private final Interpolator mInterpolator;
    private PullToRefreshBase.OnSmoothScrollFinishedListener mListener;
    private final int mScrollFromY;
    private final int mScrollToY;
    private long mStartTime = -1L;

    public SmoothScrollRunnable(int paramInt1, int paramLong, long arg4, PullToRefreshBase.OnSmoothScrollFinishedListener arg6)
    {
      this.mScrollFromY = paramInt1;
      this.mScrollToY = paramLong;
      this.mInterpolator = PullToRefreshBase.this.mScrollAnimationInterpolator;
      this.mDuration = ???;
      Object localObject;
      this.mListener = localObject;
    }

    public void run()
    {
      if (this.mStartTime == -1L)
      {
        this.mStartTime = System.currentTimeMillis();
        if ((!this.mContinueRunning) || (this.mScrollToY == this.mCurrentY))
          break label126;
        ViewCompat.postOnAnimation(PullToRefreshBase.this, this);
      }
      label126: 
      do
      {
        return;
        long l = Math.max(Math.min((System.currentTimeMillis() - this.mStartTime) * 1000L / this.mDuration, 1000L), 0L);
        int i = Math.round((this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation((float)l / 1000.0F));
        this.mCurrentY = (this.mScrollFromY - i);
        PullToRefreshBase.this.setHeaderScroll(this.mCurrentY);
        break;
      }
      while (this.mListener == null);
      this.mListener.onSmoothScrollFinished();
    }

    public void stop()
    {
      this.mContinueRunning = false;
      PullToRefreshBase.this.removeCallbacks(this);
    }
  }

  public static enum State
  {
    private int mIntValue;

    static
    {
      PULL_TO_REFRESH = new State("PULL_TO_REFRESH", 1, 1);
      RELEASE_TO_REFRESH = new State("RELEASE_TO_REFRESH", 2, 2);
      REFRESHING = new State("REFRESHING", 3, 8);
      MANUAL_REFRESHING = new State("MANUAL_REFRESHING", 4, 9);
      OVERSCROLLING = new State("OVERSCROLLING", 5, 16);
      $VALUES = new State[] { RESET, PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING, MANUAL_REFRESHING, OVERSCROLLING };
    }

    private State(int paramInt)
    {
      this.mIntValue = paramInt;
    }

    static State mapIntToValue(int paramInt)
    {
      State[] arrayOfState = values();
      int j = arrayOfState.length;
      int i = 0;
      while (i < j)
      {
        State localState = arrayOfState[i];
        if (paramInt == localState.getIntValue())
          return localState;
        i += 1;
      }
      return RESET;
    }

    int getIntValue()
    {
      return this.mIntValue;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.PullToRefreshBase
 * JD-Core Version:    0.6.0
 */