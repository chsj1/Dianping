package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.os.TraceCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.recyclerview.R.styleable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup
  implements ScrollingView, NestedScrollingChild
{
  private static final boolean DEBUG = false;
  private static final boolean DISPATCH_TEMP_DETACH = false;
  private static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
  public static final int HORIZONTAL = 0;
  private static final int INVALID_POINTER = -1;
  public static final int INVALID_TYPE = -1;
  private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
  private static final int MAX_SCROLL_DURATION = 2000;
  public static final long NO_ID = -1L;
  public static final int NO_POSITION = -1;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "RecyclerView";
  public static final int TOUCH_SLOP_DEFAULT = 0;
  public static final int TOUCH_SLOP_PAGING = 1;
  private static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
  private static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
  private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
  private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
  private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
  private static final String TRACE_SCROLL_TAG = "RV Scroll";
  public static final int VERTICAL = 1;
  private static final Interpolator sQuinticInterpolator;
  private RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
  private final AccessibilityManager mAccessibilityManager;
  private OnItemTouchListener mActiveOnItemTouchListener;
  private Adapter mAdapter;
  AdapterHelper mAdapterHelper;
  private boolean mAdapterUpdateDuringMeasure;
  private EdgeEffectCompat mBottomGlow;
  private ChildDrawingOrderCallback mChildDrawingOrderCallback;
  ChildHelper mChildHelper;
  private boolean mClipToPadding;
  private boolean mDataSetHasChangedAfterLayout = false;
  private boolean mEatRequestLayout;
  private int mEatenAccessibilityChangeFlags;
  private boolean mFirstLayoutComplete;
  private boolean mHasFixedSize;
  private boolean mIgnoreMotionEventTillDown;
  private int mInitialTouchX;
  private int mInitialTouchY;
  private boolean mIsAttached;
  ItemAnimator mItemAnimator = new DefaultItemAnimator();
  private RecyclerView.ItemAnimator.ItemAnimatorListener mItemAnimatorListener = new ItemAnimatorRestoreListener(null);
  private Runnable mItemAnimatorRunner = new Runnable()
  {
    public void run()
    {
      if (RecyclerView.this.mItemAnimator != null)
        RecyclerView.this.mItemAnimator.runPendingAnimations();
      RecyclerView.access$502(RecyclerView.this, false);
    }
  };
  private final ArrayList<ItemDecoration> mItemDecorations = new ArrayList();
  boolean mItemsAddedOrRemoved = false;
  boolean mItemsChanged = false;
  private int mLastTouchX;
  private int mLastTouchY;
  private LayoutManager mLayout;
  private boolean mLayoutFrozen;
  private int mLayoutOrScrollCounter = 0;
  private boolean mLayoutRequestEaten;
  private EdgeEffectCompat mLeftGlow;
  private final int mMaxFlingVelocity;
  private final int mMinFlingVelocity;
  private final int[] mMinMaxLayoutPositions = new int[2];
  private final int[] mNestedOffsets = new int[2];
  private final RecyclerViewDataObserver mObserver = new RecyclerViewDataObserver(null);
  private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
  private final ArrayList<OnItemTouchListener> mOnItemTouchListeners = new ArrayList();
  private SavedState mPendingSavedState;
  private final boolean mPostUpdatesOnAnimation;
  private boolean mPostedAnimatorRunner = false;
  final Recycler mRecycler = new Recycler();
  private RecyclerListener mRecyclerListener;
  private EdgeEffectCompat mRightGlow;
  private final int[] mScrollConsumed = new int[2];
  private float mScrollFactor = 1.4E-45F;
  private OnScrollListener mScrollListener;
  private List<OnScrollListener> mScrollListeners;
  private final int[] mScrollOffset = new int[2];
  private int mScrollPointerId = -1;
  private int mScrollState = 0;
  private final NestedScrollingChildHelper mScrollingChildHelper;
  final State mState = new State();
  private final Rect mTempRect = new Rect();
  private EdgeEffectCompat mTopGlow;
  private int mTouchSlop;
  private final Runnable mUpdateChildViewsRunnable = new Runnable()
  {
    public void run()
    {
      if (!RecyclerView.this.mFirstLayoutComplete);
      do
      {
        return;
        if (!RecyclerView.this.mDataSetHasChangedAfterLayout)
          continue;
        TraceCompat.beginSection("RV FullInvalidate");
        RecyclerView.this.dispatchLayout();
        TraceCompat.endSection();
        return;
      }
      while (!RecyclerView.this.mAdapterHelper.hasPendingUpdates());
      TraceCompat.beginSection("RV PartialInvalidate");
      RecyclerView.this.eatRequestLayout();
      RecyclerView.this.mAdapterHelper.preProcess();
      if (!RecyclerView.this.mLayoutRequestEaten)
        RecyclerView.this.rebindUpdatedViewHolders();
      RecyclerView.this.resumeRequestLayout(true);
      TraceCompat.endSection();
    }
  };
  private VelocityTracker mVelocityTracker;
  private final ViewFlinger mViewFlinger = new ViewFlinger();

  static
  {
    if ((Build.VERSION.SDK_INT == 18) || (Build.VERSION.SDK_INT == 19) || (Build.VERSION.SDK_INT == 20));
    for (boolean bool = true; ; bool = false)
    {
      FORCE_INVALIDATE_DISPLAY_LIST = bool;
      LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE };
      sQuinticInterpolator = new Interpolator()
      {
        public float getInterpolation(float paramFloat)
        {
          paramFloat -= 1.0F;
          return paramFloat * paramFloat * paramFloat * paramFloat * paramFloat + 1.0F;
        }
      };
      return;
    }
  }

  public RecyclerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public RecyclerView(Context paramContext, @Nullable AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public RecyclerView(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setScrollContainer(true);
    setFocusableInTouchMode(true);
    if (Build.VERSION.SDK_INT >= 16);
    for (boolean bool1 = true; ; bool1 = false)
    {
      this.mPostUpdatesOnAnimation = bool1;
      Object localObject = ViewConfiguration.get(paramContext);
      this.mTouchSlop = ((ViewConfiguration)localObject).getScaledTouchSlop();
      this.mMinFlingVelocity = ((ViewConfiguration)localObject).getScaledMinimumFlingVelocity();
      this.mMaxFlingVelocity = ((ViewConfiguration)localObject).getScaledMaximumFlingVelocity();
      bool1 = bool2;
      if (ViewCompat.getOverScrollMode(this) == 2)
        bool1 = true;
      setWillNotDraw(bool1);
      this.mItemAnimator.setListener(this.mItemAnimatorListener);
      initAdapterManager();
      initChildrenHelper();
      if (ViewCompat.getImportantForAccessibility(this) == 0)
        ViewCompat.setImportantForAccessibility(this, 1);
      this.mAccessibilityManager = ((AccessibilityManager)getContext().getSystemService("accessibility"));
      setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
      if (paramAttributeSet != null)
      {
        localObject = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt, 0);
        String str = ((TypedArray)localObject).getString(R.styleable.RecyclerView_layoutManager);
        ((TypedArray)localObject).recycle();
        createLayoutManager(paramContext, str, paramAttributeSet, paramInt, 0);
      }
      this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
      setNestedScrollingEnabled(true);
      return;
    }
  }

  private void addAnimatingView(ViewHolder paramViewHolder)
  {
    View localView = paramViewHolder.itemView;
    if (localView.getParent() == this);
    for (int i = 1; ; i = 0)
    {
      this.mRecycler.unscrapView(getChildViewHolder(localView));
      if (!paramViewHolder.isTmpDetached())
        break;
      this.mChildHelper.attachViewToParent(localView, -1, localView.getLayoutParams(), true);
      return;
    }
    if (i == 0)
    {
      this.mChildHelper.addView(localView, true);
      return;
    }
    this.mChildHelper.hide(localView);
  }

  private void animateAppearance(ViewHolder paramViewHolder, Rect paramRect, int paramInt1, int paramInt2)
  {
    View localView = paramViewHolder.itemView;
    if ((paramRect != null) && ((paramRect.left != paramInt1) || (paramRect.top != paramInt2)))
    {
      paramViewHolder.setIsRecyclable(false);
      if (this.mItemAnimator.animateMove(paramViewHolder, paramRect.left, paramRect.top, paramInt1, paramInt2))
        postAnimationRunner();
    }
    do
    {
      return;
      paramViewHolder.setIsRecyclable(false);
    }
    while (!this.mItemAnimator.animateAdd(paramViewHolder));
    postAnimationRunner();
  }

  private void animateChange(ViewHolder paramViewHolder1, ViewHolder paramViewHolder2)
  {
    paramViewHolder1.setIsRecyclable(false);
    addAnimatingView(paramViewHolder1);
    paramViewHolder1.mShadowedHolder = paramViewHolder2;
    this.mRecycler.unscrapView(paramViewHolder1);
    int k = paramViewHolder1.itemView.getLeft();
    int m = paramViewHolder1.itemView.getTop();
    int i;
    int j;
    if ((paramViewHolder2 == null) || (paramViewHolder2.shouldIgnore()))
    {
      i = k;
      j = m;
    }
    while (true)
    {
      if (this.mItemAnimator.animateChange(paramViewHolder1, paramViewHolder2, k, m, i, j))
        postAnimationRunner();
      return;
      i = paramViewHolder2.itemView.getLeft();
      j = paramViewHolder2.itemView.getTop();
      paramViewHolder2.setIsRecyclable(false);
      paramViewHolder2.mShadowingHolder = paramViewHolder1;
    }
  }

  private void animateDisappearance(ItemHolderInfo paramItemHolderInfo)
  {
    View localView = paramItemHolderInfo.holder.itemView;
    addAnimatingView(paramItemHolderInfo.holder);
    int i = paramItemHolderInfo.left;
    int j = paramItemHolderInfo.top;
    int k = localView.getLeft();
    int m = localView.getTop();
    if ((!paramItemHolderInfo.holder.isRemoved()) && ((i != k) || (j != m)))
    {
      paramItemHolderInfo.holder.setIsRecyclable(false);
      localView.layout(k, m, localView.getWidth() + k, localView.getHeight() + m);
      if (this.mItemAnimator.animateMove(paramItemHolderInfo.holder, i, j, k, m))
        postAnimationRunner();
    }
    do
    {
      return;
      paramItemHolderInfo.holder.setIsRecyclable(false);
    }
    while (!this.mItemAnimator.animateRemove(paramItemHolderInfo.holder));
    postAnimationRunner();
  }

  private void cancelTouch()
  {
    resetTouch();
    setScrollState(0);
  }

  private void considerReleasingGlowsOnScroll(int paramInt1, int paramInt2)
  {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (this.mLeftGlow != null)
    {
      bool1 = bool2;
      if (!this.mLeftGlow.isFinished())
      {
        bool1 = bool2;
        if (paramInt1 > 0)
          bool1 = this.mLeftGlow.onRelease();
      }
    }
    bool2 = bool1;
    if (this.mRightGlow != null)
    {
      bool2 = bool1;
      if (!this.mRightGlow.isFinished())
      {
        bool2 = bool1;
        if (paramInt1 < 0)
          bool2 = bool1 | this.mRightGlow.onRelease();
      }
    }
    bool1 = bool2;
    if (this.mTopGlow != null)
    {
      bool1 = bool2;
      if (!this.mTopGlow.isFinished())
      {
        bool1 = bool2;
        if (paramInt2 > 0)
          bool1 = bool2 | this.mTopGlow.onRelease();
      }
    }
    bool2 = bool1;
    if (this.mBottomGlow != null)
    {
      bool2 = bool1;
      if (!this.mBottomGlow.isFinished())
      {
        bool2 = bool1;
        if (paramInt2 < 0)
          bool2 = bool1 | this.mBottomGlow.onRelease();
      }
    }
    if (bool2)
      ViewCompat.postInvalidateOnAnimation(this);
  }

  private void consumePendingUpdateOperations()
  {
    this.mUpdateChildViewsRunnable.run();
  }

  private void createLayoutManager(Context paramContext, String paramString, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    if (paramString != null)
    {
      paramString = paramString.trim();
      if (paramString.length() != 0)
      {
        String str = getFullClassName(paramContext, paramString);
        try
        {
          if (isInEditMode())
            paramString = getClass().getClassLoader();
          while (true)
          {
            Class localClass = paramString.loadClass(str).asSubclass(LayoutManager.class);
            paramString = null;
            try
            {
              Constructor localConstructor = localClass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
              paramString = new Object[] { paramContext, paramAttributeSet, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) };
              paramContext = localConstructor;
              paramContext.setAccessible(true);
              setLayoutManager((LayoutManager)paramContext.newInstance(paramString));
              return;
              paramString = paramContext.getClassLoader();
            }
            catch (NoSuchMethodException localNoSuchMethodException)
            {
              try
              {
                paramContext = localClass.getConstructor(new Class[0]);
              }
              catch (NoSuchMethodException paramContext)
              {
                paramContext.initCause(localNoSuchMethodException);
                throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Error creating LayoutManager " + str, paramContext);
              }
            }
          }
        }
        catch (java.lang.ClassNotFoundException paramContext)
        {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Unable to find LayoutManager " + str, paramContext);
        }
        catch (java.lang.reflect.InvocationTargetException paramContext)
        {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, paramContext);
        }
        catch (java.lang.InstantiationException paramContext)
        {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + str, paramContext);
        }
        catch (java.lang.IllegalAccessException paramContext)
        {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Cannot access non-public constructor " + str, paramContext);
        }
        catch (java.lang.ClassCastException paramContext)
        {
          throw new IllegalStateException(paramAttributeSet.getPositionDescription() + ": Class is not a LayoutManager " + str, paramContext);
        }
      }
    }
  }

  private void defaultOnMeasure(int paramInt1, int paramInt2)
  {
    int j = View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    switch (j)
    {
    default:
      paramInt1 = ViewCompat.getMinimumWidth(this);
      switch (i)
      {
      default:
        paramInt2 = ViewCompat.getMinimumHeight(this);
      case 1073741824:
      case -2147483648:
      }
    case 1073741824:
    case -2147483648:
    }
    while (true)
    {
      setMeasuredDimension(paramInt1, paramInt2);
      return;
      break;
    }
  }

  private boolean didChildRangeChange(int paramInt1, int paramInt2)
  {
    int n = 0;
    int j = this.mChildHelper.getChildCount();
    int m;
    if (j == 0)
      if (paramInt1 == 0)
      {
        m = n;
        if (paramInt2 == 0);
      }
      else
      {
        m = 1;
      }
    int i;
    do
    {
      return m;
      i = 0;
      m = n;
    }
    while (i >= j);
    ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
    if (localViewHolder.shouldIgnore());
    int k;
    do
    {
      i += 1;
      break;
      k = localViewHolder.getLayoutPosition();
    }
    while ((k >= paramInt1) && (k <= paramInt2));
    return true;
  }

  private void dispatchChildAttached(View paramView)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    onChildAttachedToWindow(paramView);
    if ((this.mAdapter != null) && (localViewHolder != null))
      this.mAdapter.onViewAttachedToWindow(localViewHolder);
    if (this.mOnChildAttachStateListeners != null)
    {
      int i = this.mOnChildAttachStateListeners.size() - 1;
      while (i >= 0)
      {
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(paramView);
        i -= 1;
      }
    }
  }

  private void dispatchChildDetached(View paramView)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    onChildDetachedFromWindow(paramView);
    if ((this.mAdapter != null) && (localViewHolder != null))
      this.mAdapter.onViewDetachedFromWindow(localViewHolder);
    if (this.mOnChildAttachStateListeners != null)
    {
      int i = this.mOnChildAttachStateListeners.size() - 1;
      while (i >= 0)
      {
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(paramView);
        i -= 1;
      }
    }
  }

  private void dispatchContentChangedIfNecessary()
  {
    int i = this.mEatenAccessibilityChangeFlags;
    this.mEatenAccessibilityChangeFlags = 0;
    if ((i != 0) && (isAccessibilityEnabled()))
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
      localAccessibilityEvent.setEventType(2048);
      AccessibilityEventCompat.setContentChangeTypes(localAccessibilityEvent, i);
      sendAccessibilityEventUnchecked(localAccessibilityEvent);
    }
  }

  private boolean dispatchOnItemTouch(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    int j;
    if (this.mActiveOnItemTouchListener != null)
    {
      if (i == 0)
        this.mActiveOnItemTouchListener = null;
    }
    else
    {
      if (i == 0)
        break label107;
      j = this.mOnItemTouchListeners.size();
      i = 0;
    }
    while (i < j)
    {
      OnItemTouchListener localOnItemTouchListener = (OnItemTouchListener)this.mOnItemTouchListeners.get(i);
      if (localOnItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent))
      {
        this.mActiveOnItemTouchListener = localOnItemTouchListener;
        do
        {
          return true;
          this.mActiveOnItemTouchListener.onTouchEvent(this, paramMotionEvent);
        }
        while ((i != 3) && (i != 1));
        this.mActiveOnItemTouchListener = null;
        return true;
      }
      i += 1;
    }
    label107: return false;
  }

  private boolean dispatchOnItemTouchIntercept(MotionEvent paramMotionEvent)
  {
    int j = paramMotionEvent.getAction();
    if ((j == 3) || (j == 0))
      this.mActiveOnItemTouchListener = null;
    int k = this.mOnItemTouchListeners.size();
    int i = 0;
    while (i < k)
    {
      OnItemTouchListener localOnItemTouchListener = (OnItemTouchListener)this.mOnItemTouchListeners.get(i);
      if ((localOnItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent)) && (j != 3))
      {
        this.mActiveOnItemTouchListener = localOnItemTouchListener;
        return true;
      }
      i += 1;
    }
    return false;
  }

  private void findMinMaxChildLayoutPositions(int[] paramArrayOfInt)
  {
    int i2 = this.mChildHelper.getChildCount();
    if (i2 == 0)
    {
      paramArrayOfInt[0] = 0;
      paramArrayOfInt[1] = 0;
      return;
    }
    int j = 2147483647;
    int m = -2147483648;
    int k = 0;
    if (k < i2)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(k));
      int i1;
      if (localViewHolder.shouldIgnore())
      {
        i1 = j;
        j = m;
      }
      while (true)
      {
        k += 1;
        m = j;
        j = i1;
        break;
        int n = localViewHolder.getLayoutPosition();
        int i = j;
        if (n < j)
          i = n;
        j = m;
        i1 = i;
        if (n <= m)
          continue;
        j = n;
        i1 = i;
      }
    }
    paramArrayOfInt[0] = j;
    paramArrayOfInt[1] = m;
  }

  private int getAdapterPositionFor(ViewHolder paramViewHolder)
  {
    if ((paramViewHolder.hasAnyOfTheFlags(524)) || (!paramViewHolder.isBound()))
      return -1;
    return this.mAdapterHelper.applyPendingUpdatesToPosition(paramViewHolder.mPosition);
  }

  static ViewHolder getChildViewHolderInt(View paramView)
  {
    if (paramView == null)
      return null;
    return ((LayoutParams)paramView.getLayoutParams()).mViewHolder;
  }

  private String getFullClassName(Context paramContext, String paramString)
  {
    if (paramString.charAt(0) == '.')
      paramContext = paramContext.getPackageName() + paramString;
    do
    {
      return paramContext;
      paramContext = paramString;
    }
    while (paramString.contains("."));
    return RecyclerView.class.getPackage().getName() + '.' + paramString;
  }

  private float getScrollFactor()
  {
    if (this.mScrollFactor == 1.4E-45F)
    {
      TypedValue localTypedValue = new TypedValue();
      if (getContext().getTheme().resolveAttribute(16842829, localTypedValue, true))
        this.mScrollFactor = localTypedValue.getDimension(getContext().getResources().getDisplayMetrics());
    }
    else
    {
      return this.mScrollFactor;
    }
    return 0.0F;
  }

  private void initChildrenHelper()
  {
    this.mChildHelper = new ChildHelper(new ChildHelper.Callback()
    {
      public void addView(View paramView, int paramInt)
      {
        RecyclerView.this.addView(paramView, paramInt);
        RecyclerView.this.dispatchChildAttached(paramView);
      }

      public void attachViewToParent(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
      {
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
        if (localViewHolder != null)
        {
          if ((!localViewHolder.isTmpDetached()) && (!localViewHolder.shouldIgnore()))
            throw new IllegalArgumentException("Called attach on a child which is not detached: " + localViewHolder);
          localViewHolder.clearTmpDetachFlag();
        }
        RecyclerView.this.attachViewToParent(paramView, paramInt, paramLayoutParams);
      }

      public void detachViewFromParent(int paramInt)
      {
        Object localObject = getChildAt(paramInt);
        if (localObject != null)
        {
          localObject = RecyclerView.getChildViewHolderInt((View)localObject);
          if (localObject != null)
          {
            if ((((RecyclerView.ViewHolder)localObject).isTmpDetached()) && (!((RecyclerView.ViewHolder)localObject).shouldIgnore()))
              throw new IllegalArgumentException("called detach on an already detached child " + localObject);
            ((RecyclerView.ViewHolder)localObject).addFlags(256);
          }
        }
        RecyclerView.this.detachViewFromParent(paramInt);
      }

      public View getChildAt(int paramInt)
      {
        return RecyclerView.this.getChildAt(paramInt);
      }

      public int getChildCount()
      {
        return RecyclerView.this.getChildCount();
      }

      public RecyclerView.ViewHolder getChildViewHolder(View paramView)
      {
        return RecyclerView.getChildViewHolderInt(paramView);
      }

      public int indexOfChild(View paramView)
      {
        return RecyclerView.this.indexOfChild(paramView);
      }

      public void onEnteredHiddenState(View paramView)
      {
        paramView = RecyclerView.getChildViewHolderInt(paramView);
        if (paramView != null)
          RecyclerView.ViewHolder.access$1000(paramView);
      }

      public void onLeftHiddenState(View paramView)
      {
        paramView = RecyclerView.getChildViewHolderInt(paramView);
        if (paramView != null)
          RecyclerView.ViewHolder.access$1100(paramView);
      }

      public void removeAllViews()
      {
        int j = getChildCount();
        int i = 0;
        while (i < j)
        {
          RecyclerView.this.dispatchChildDetached(getChildAt(i));
          i += 1;
        }
        RecyclerView.this.removeAllViews();
      }

      public void removeViewAt(int paramInt)
      {
        View localView = RecyclerView.this.getChildAt(paramInt);
        if (localView != null)
          RecyclerView.this.dispatchChildDetached(localView);
        RecyclerView.this.removeViewAt(paramInt);
      }
    });
  }

  private void jumpToPositionForSmoothScroller(int paramInt)
  {
    if (this.mLayout == null)
      return;
    this.mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }

  private void onEnterLayoutOrScroll()
  {
    this.mLayoutOrScrollCounter += 1;
  }

  private void onExitLayoutOrScroll()
  {
    this.mLayoutOrScrollCounter -= 1;
    if (this.mLayoutOrScrollCounter < 1)
    {
      this.mLayoutOrScrollCounter = 0;
      dispatchContentChangedIfNecessary();
    }
  }

  private void onPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == this.mScrollPointerId)
      if (i != 0)
        break label75;
    label75: for (i = 1; ; i = 0)
    {
      this.mScrollPointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      int j = (int)(MotionEventCompat.getX(paramMotionEvent, i) + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      i = (int)(MotionEventCompat.getY(paramMotionEvent, i) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
      return;
    }
  }

  private void postAnimationRunner()
  {
    if ((!this.mPostedAnimatorRunner) && (this.mIsAttached))
    {
      ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
      this.mPostedAnimatorRunner = true;
    }
  }

  private boolean predictiveItemAnimationsEnabled()
  {
    return (this.mItemAnimator != null) && (this.mLayout.supportsPredictiveItemAnimations());
  }

  private void processAdapterUpdatesAndSetAnimationFlags()
  {
    boolean bool2 = true;
    if (this.mDataSetHasChangedAfterLayout)
    {
      this.mAdapterHelper.reset();
      markKnownViewsInvalid();
      this.mLayout.onItemsChanged(this);
    }
    int i;
    label90: State localState;
    if ((this.mItemAnimator != null) && (this.mLayout.supportsPredictiveItemAnimations()))
    {
      this.mAdapterHelper.preProcess();
      if (((!this.mItemsAddedOrRemoved) || (this.mItemsChanged)) && (!this.mItemsAddedOrRemoved) && ((!this.mItemsChanged) || (!supportsChangeAnimations())))
        break label208;
      i = 1;
      localState = this.mState;
      if ((!this.mFirstLayoutComplete) || (this.mItemAnimator == null) || ((!this.mDataSetHasChangedAfterLayout) && (i == 0) && (!this.mLayout.mRequestedSimpleAnimations)) || ((this.mDataSetHasChangedAfterLayout) && (!this.mAdapter.hasStableIds())))
        break label213;
      bool1 = true;
      label149: State.access$1802(localState, bool1);
      localState = this.mState;
      if ((!this.mState.mRunSimpleAnimations) || (i == 0) || (this.mDataSetHasChangedAfterLayout) || (!predictiveItemAnimationsEnabled()))
        break label218;
    }
    label208: label213: label218: for (boolean bool1 = bool2; ; bool1 = false)
    {
      State.access$1602(localState, bool1);
      return;
      this.mAdapterHelper.consumeUpdatesInOnePass();
      break;
      i = 0;
      break label90;
      bool1 = false;
      break label149;
    }
  }

  private void processDisappearingList(ArrayMap<View, Rect> paramArrayMap)
  {
    List localList = this.mState.mDisappearingViewsInLayoutPass;
    int i = localList.size() - 1;
    if (i >= 0)
    {
      View localView = (View)localList.get(i);
      ViewHolder localViewHolder = getChildViewHolderInt(localView);
      ItemHolderInfo localItemHolderInfo = (ItemHolderInfo)this.mState.mPreLayoutHolderMap.remove(localViewHolder);
      if (!this.mState.isPreLayout())
        this.mState.mPostLayoutHolderMap.remove(localViewHolder);
      if (paramArrayMap.remove(localView) != null)
        this.mLayout.removeAndRecycleView(localView, this.mRecycler);
      while (true)
      {
        i -= 1;
        break;
        if (localItemHolderInfo != null)
        {
          animateDisappearance(localItemHolderInfo);
          continue;
        }
        animateDisappearance(new ItemHolderInfo(localViewHolder, localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom()));
      }
    }
    localList.clear();
  }

  private void pullGlows(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    int j = 0;
    int i;
    if (paramFloat2 < 0.0F)
    {
      ensureLeftGlow();
      i = j;
      if (this.mLeftGlow.onPull(-paramFloat2 / getWidth(), 1.0F - paramFloat3 / getHeight()))
        i = 1;
      if (paramFloat4 >= 0.0F)
        break label162;
      ensureTopGlow();
      j = i;
      if (this.mTopGlow.onPull(-paramFloat4 / getHeight(), paramFloat1 / getWidth()))
        j = 1;
    }
    while (true)
    {
      if ((j != 0) || (paramFloat2 != 0.0F) || (paramFloat4 != 0.0F))
        ViewCompat.postInvalidateOnAnimation(this);
      return;
      i = j;
      if (paramFloat2 <= 0.0F)
        break;
      ensureRightGlow();
      i = j;
      if (!this.mRightGlow.onPull(paramFloat2 / getWidth(), paramFloat3 / getHeight()))
        break;
      i = 1;
      break;
      label162: j = i;
      if (paramFloat4 <= 0.0F)
        continue;
      ensureBottomGlow();
      j = i;
      if (!this.mBottomGlow.onPull(paramFloat4 / getHeight(), 1.0F - paramFloat1 / getWidth()))
        continue;
      j = 1;
    }
  }

  private void releaseGlows()
  {
    boolean bool2 = false;
    if (this.mLeftGlow != null)
      bool2 = this.mLeftGlow.onRelease();
    boolean bool1 = bool2;
    if (this.mTopGlow != null)
      bool1 = bool2 | this.mTopGlow.onRelease();
    bool2 = bool1;
    if (this.mRightGlow != null)
      bool2 = bool1 | this.mRightGlow.onRelease();
    bool1 = bool2;
    if (this.mBottomGlow != null)
      bool1 = bool2 | this.mBottomGlow.onRelease();
    if (bool1)
      ViewCompat.postInvalidateOnAnimation(this);
  }

  private boolean removeAnimatingView(View paramView)
  {
    eatRequestLayout();
    boolean bool = this.mChildHelper.removeViewIfHidden(paramView);
    if (bool)
    {
      paramView = getChildViewHolderInt(paramView);
      this.mRecycler.unscrapView(paramView);
      this.mRecycler.recycleViewHolderInternal(paramView);
    }
    resumeRequestLayout(false);
    return bool;
  }

  private void resetTouch()
  {
    if (this.mVelocityTracker != null)
      this.mVelocityTracker.clear();
    stopNestedScroll();
    releaseGlows();
  }

  private void setAdapterInternal(Adapter paramAdapter, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mAdapter != null)
    {
      this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
      this.mAdapter.onDetachedFromRecyclerView(this);
    }
    if ((!paramBoolean1) || (paramBoolean2))
    {
      if (this.mItemAnimator != null)
        this.mItemAnimator.endAnimations();
      if (this.mLayout != null)
      {
        this.mLayout.removeAndRecycleAllViews(this.mRecycler);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      }
      this.mRecycler.clear();
    }
    this.mAdapterHelper.reset();
    Adapter localAdapter = this.mAdapter;
    this.mAdapter = paramAdapter;
    if (paramAdapter != null)
    {
      paramAdapter.registerAdapterDataObserver(this.mObserver);
      paramAdapter.onAttachedToRecyclerView(this);
    }
    if (this.mLayout != null)
      this.mLayout.onAdapterChanged(localAdapter, this.mAdapter);
    this.mRecycler.onAdapterChanged(localAdapter, this.mAdapter, paramBoolean1);
    State.access$1402(this.mState, true);
    markKnownViewsInvalid();
  }

  private void setDataSetChangedAfterLayout()
  {
    if (this.mDataSetHasChangedAfterLayout)
      return;
    this.mDataSetHasChangedAfterLayout = true;
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()))
        localViewHolder.addFlags(512);
      i += 1;
    }
    this.mRecycler.setAdapterPositionsAsUnknown();
  }

  private void setScrollState(int paramInt)
  {
    if (paramInt == this.mScrollState)
      return;
    this.mScrollState = paramInt;
    if (paramInt != 2)
      stopScrollersInternal();
    dispatchOnScrollStateChanged(paramInt);
  }

  private void stopScrollersInternal()
  {
    this.mViewFlinger.stop();
    if (this.mLayout != null)
      this.mLayout.stopSmoothScroller();
  }

  private boolean supportsChangeAnimations()
  {
    return (this.mItemAnimator != null) && (this.mItemAnimator.getSupportsChangeAnimations());
  }

  void absorbGlows(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
    {
      ensureLeftGlow();
      this.mLeftGlow.onAbsorb(-paramInt1);
      if (paramInt2 >= 0)
        break label69;
      ensureTopGlow();
      this.mTopGlow.onAbsorb(-paramInt2);
    }
    while (true)
    {
      if ((paramInt1 != 0) || (paramInt2 != 0))
        ViewCompat.postInvalidateOnAnimation(this);
      return;
      if (paramInt1 <= 0)
        break;
      ensureRightGlow();
      this.mRightGlow.onAbsorb(paramInt1);
      break;
      label69: if (paramInt2 <= 0)
        continue;
      ensureBottomGlow();
      this.mBottomGlow.onAbsorb(paramInt2);
    }
  }

  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    if ((this.mLayout == null) || (!this.mLayout.onAddFocusables(this, paramArrayList, paramInt1, paramInt2)))
      super.addFocusables(paramArrayList, paramInt1, paramInt2);
  }

  public void addItemDecoration(ItemDecoration paramItemDecoration)
  {
    addItemDecoration(paramItemDecoration, -1);
  }

  public void addItemDecoration(ItemDecoration paramItemDecoration, int paramInt)
  {
    if (this.mLayout != null)
      this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
    if (this.mItemDecorations.isEmpty())
      setWillNotDraw(false);
    if (paramInt < 0)
      this.mItemDecorations.add(paramItemDecoration);
    while (true)
    {
      markItemDecorInsetsDirty();
      requestLayout();
      return;
      this.mItemDecorations.add(paramInt, paramItemDecoration);
    }
  }

  public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener)
  {
    if (this.mOnChildAttachStateListeners == null)
      this.mOnChildAttachStateListeners = new ArrayList();
    this.mOnChildAttachStateListeners.add(paramOnChildAttachStateChangeListener);
  }

  public void addOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener)
  {
    this.mOnItemTouchListeners.add(paramOnItemTouchListener);
  }

  public void addOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    if (this.mScrollListeners == null)
      this.mScrollListeners = new ArrayList();
    this.mScrollListeners.add(paramOnScrollListener);
  }

  void assertInLayoutOrScroll(String paramString)
  {
    if (!isComputingLayout())
    {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling");
      throw new IllegalStateException(paramString);
    }
  }

  void assertNotInLayoutOrScroll(String paramString)
  {
    if (isComputingLayout())
    {
      if (paramString == null)
        throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
      throw new IllegalStateException(paramString);
    }
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return ((paramLayoutParams instanceof LayoutParams)) && (this.mLayout.checkLayoutParams((LayoutParams)paramLayoutParams));
  }

  void clearOldPositions()
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if (!localViewHolder.shouldIgnore())
        localViewHolder.clearOldPosition();
      i += 1;
    }
    this.mRecycler.clearOldPositions();
  }

  public void clearOnChildAttachStateChangeListeners()
  {
    if (this.mOnChildAttachStateListeners != null)
      this.mOnChildAttachStateListeners.clear();
  }

  public void clearOnScrollListeners()
  {
    if (this.mScrollListeners != null)
      this.mScrollListeners.clear();
  }

  public int computeHorizontalScrollExtent()
  {
    if (this.mLayout.canScrollHorizontally())
      return this.mLayout.computeHorizontalScrollExtent(this.mState);
    return 0;
  }

  public int computeHorizontalScrollOffset()
  {
    if (this.mLayout.canScrollHorizontally())
      return this.mLayout.computeHorizontalScrollOffset(this.mState);
    return 0;
  }

  public int computeHorizontalScrollRange()
  {
    if (this.mLayout.canScrollHorizontally())
      return this.mLayout.computeHorizontalScrollRange(this.mState);
    return 0;
  }

  public int computeVerticalScrollExtent()
  {
    if (this.mLayout.canScrollVertically())
      return this.mLayout.computeVerticalScrollExtent(this.mState);
    return 0;
  }

  public int computeVerticalScrollOffset()
  {
    if (this.mLayout.canScrollVertically())
      return this.mLayout.computeVerticalScrollOffset(this.mState);
    return 0;
  }

  public int computeVerticalScrollRange()
  {
    if (this.mLayout.canScrollVertically())
      return this.mLayout.computeVerticalScrollRange(this.mState);
    return 0;
  }

  void dispatchLayout()
  {
    if (this.mAdapter == null)
      Log.e("RecyclerView", "No adapter attached; skipping layout");
    label191: label249: label1275: 
    do
    {
      return;
      if (this.mLayout == null)
      {
        Log.e("RecyclerView", "No layout manager attached; skipping layout");
        return;
      }
      this.mState.mDisappearingViewsInLayoutPass.clear();
      eatRequestLayout();
      onEnterLayoutOrScroll();
      processAdapterUpdatesAndSetAnimationFlags();
      Object localObject2 = this.mState;
      Object localObject1;
      if ((this.mState.mRunSimpleAnimations) && (this.mItemsChanged) && (supportsChangeAnimations()))
      {
        localObject1 = new ArrayMap();
        ((State)localObject2).mOldChangedHolders = ((ArrayMap)localObject1);
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        localObject1 = null;
        State.access$1702(this.mState, this.mState.mRunPredictiveAnimations);
        this.mState.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (!this.mState.mRunSimpleAnimations)
          break label293;
        this.mState.mPreLayoutHolderMap.clear();
        this.mState.mPostLayoutHolderMap.clear();
        j = this.mChildHelper.getChildCount();
        i = 0;
        if (i >= j)
          break label293;
        localObject2 = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
        if ((!((ViewHolder)localObject2).shouldIgnore()) && ((!((ViewHolder)localObject2).isInvalid()) || (this.mAdapter.hasStableIds())))
          break label249;
      }
      Object localObject3;
      while (true)
      {
        i += 1;
        break label191;
        localObject1 = null;
        break;
        localObject3 = ((ViewHolder)localObject2).itemView;
        this.mState.mPreLayoutHolderMap.put(localObject2, new ItemHolderInfo((ViewHolder)localObject2, ((View)localObject3).getLeft(), ((View)localObject3).getTop(), ((View)localObject3).getRight(), ((View)localObject3).getBottom()));
      }
      long l;
      boolean bool;
      if (this.mState.mRunPredictiveAnimations)
      {
        saveOldPositions();
        if (this.mState.mOldChangedHolders != null)
        {
          j = this.mChildHelper.getChildCount();
          i = 0;
          while (i < j)
          {
            localObject1 = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if ((((ViewHolder)localObject1).isChanged()) && (!((ViewHolder)localObject1).isRemoved()) && (!((ViewHolder)localObject1).shouldIgnore()))
            {
              l = getChangedHolderKey((ViewHolder)localObject1);
              this.mState.mOldChangedHolders.put(Long.valueOf(l), localObject1);
              this.mState.mPreLayoutHolderMap.remove(localObject1);
            }
            i += 1;
          }
        }
        bool = this.mState.mStructureChanged;
        State.access$1402(this.mState, false);
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        State.access$1402(this.mState, bool);
        localObject2 = new ArrayMap();
        i = 0;
        while (i < this.mChildHelper.getChildCount())
        {
          int m = 0;
          localObject1 = this.mChildHelper.getChildAt(i);
          if (getChildViewHolderInt((View)localObject1).shouldIgnore())
          {
            i += 1;
            continue;
          }
          j = 0;
          while (true)
          {
            int k = m;
            if (j < this.mState.mPreLayoutHolderMap.size())
            {
              if (((ViewHolder)this.mState.mPreLayoutHolderMap.keyAt(j)).itemView == localObject1)
                k = 1;
            }
            else
            {
              if (k != 0)
                break;
              ((ArrayMap)localObject2).put(localObject1, new Rect(((View)localObject1).getLeft(), ((View)localObject1).getTop(), ((View)localObject1).getRight(), ((View)localObject1).getBottom()));
              break;
            }
            j += 1;
          }
        }
        clearOldPositions();
        this.mAdapterHelper.consumePostponedUpdates();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        State.access$1202(this.mState, 0);
        State.access$1702(this.mState, false);
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        State.access$1402(this.mState, false);
        this.mPendingSavedState = null;
        localObject1 = this.mState;
        if ((!this.mState.mRunSimpleAnimations) || (this.mItemAnimator == null))
          break label909;
        bool = true;
        State.access$1802((State)localObject1, bool);
        if (!this.mState.mRunSimpleAnimations)
          break label1573;
        if (this.mState.mOldChangedHolders == null)
          break label915;
        localObject1 = new ArrayMap();
        j = this.mChildHelper.getChildCount();
        i = 0;
        if (i >= j)
          break label1012;
        localObject3 = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
        if (!((ViewHolder)localObject3).shouldIgnore())
          break label920;
      }
      Object localObject4;
      while (true)
      {
        i += 1;
        break label750;
        clearOldPositions();
        this.mAdapterHelper.consumeUpdatesInOnePass();
        localObject2 = localObject1;
        if (this.mState.mOldChangedHolders == null)
          break;
        j = this.mChildHelper.getChildCount();
        i = 0;
        while (true)
        {
          localObject2 = localObject1;
          if (i >= j)
            break;
          localObject2 = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
          if ((((ViewHolder)localObject2).isChanged()) && (!((ViewHolder)localObject2).isRemoved()) && (!((ViewHolder)localObject2).shouldIgnore()))
          {
            l = getChangedHolderKey((ViewHolder)localObject2);
            this.mState.mOldChangedHolders.put(Long.valueOf(l), localObject2);
            this.mState.mPreLayoutHolderMap.remove(localObject2);
          }
          i += 1;
        }
        bool = false;
        break label703;
        localObject1 = null;
        break label738;
        localObject4 = ((ViewHolder)localObject3).itemView;
        l = getChangedHolderKey((ViewHolder)localObject3);
        if ((localObject1 != null) && (this.mState.mOldChangedHolders.get(Long.valueOf(l)) != null))
        {
          ((ArrayMap)localObject1).put(Long.valueOf(l), localObject3);
          continue;
        }
        this.mState.mPostLayoutHolderMap.put(localObject3, new ItemHolderInfo((ViewHolder)localObject3, ((View)localObject4).getLeft(), ((View)localObject4).getTop(), ((View)localObject4).getRight(), ((View)localObject4).getBottom()));
      }
      processDisappearingList((ArrayMap)localObject2);
      int i = this.mState.mPreLayoutHolderMap.size() - 1;
      while (i >= 0)
      {
        localObject3 = (ViewHolder)this.mState.mPreLayoutHolderMap.keyAt(i);
        if (!this.mState.mPostLayoutHolderMap.containsKey(localObject3))
        {
          localObject3 = (ItemHolderInfo)this.mState.mPreLayoutHolderMap.valueAt(i);
          this.mState.mPreLayoutHolderMap.removeAt(i);
          localObject4 = ((ItemHolderInfo)localObject3).holder.itemView;
          this.mRecycler.unscrapView(((ItemHolderInfo)localObject3).holder);
          animateDisappearance((ItemHolderInfo)localObject3);
        }
        i -= 1;
      }
      i = this.mState.mPostLayoutHolderMap.size();
      if (i > 0)
      {
        i -= 1;
        if (i >= 0)
        {
          localObject4 = (ViewHolder)this.mState.mPostLayoutHolderMap.keyAt(i);
          ItemHolderInfo localItemHolderInfo = (ItemHolderInfo)this.mState.mPostLayoutHolderMap.valueAt(i);
          if ((this.mState.mPreLayoutHolderMap.isEmpty()) || (!this.mState.mPreLayoutHolderMap.containsKey(localObject4)))
          {
            this.mState.mPostLayoutHolderMap.removeAt(i);
            if (localObject2 == null)
              break label1275;
          }
          for (localObject3 = (Rect)((ArrayMap)localObject2).get(((ViewHolder)localObject4).itemView); ; localObject3 = null)
          {
            animateAppearance((ViewHolder)localObject4, (Rect)localObject3, localItemHolderInfo.left, localItemHolderInfo.top);
            i -= 1;
            break;
          }
        }
      }
      int j = this.mState.mPostLayoutHolderMap.size();
      i = 0;
      while (i < j)
      {
        localObject2 = (ViewHolder)this.mState.mPostLayoutHolderMap.keyAt(i);
        localObject3 = (ItemHolderInfo)this.mState.mPostLayoutHolderMap.valueAt(i);
        localObject4 = (ItemHolderInfo)this.mState.mPreLayoutHolderMap.get(localObject2);
        if ((localObject4 != null) && (localObject3 != null) && ((((ItemHolderInfo)localObject4).left != ((ItemHolderInfo)localObject3).left) || (((ItemHolderInfo)localObject4).top != ((ItemHolderInfo)localObject3).top)))
        {
          ((ViewHolder)localObject2).setIsRecyclable(false);
          if (this.mItemAnimator.animateMove((ViewHolder)localObject2, ((ItemHolderInfo)localObject4).left, ((ItemHolderInfo)localObject4).top, ((ItemHolderInfo)localObject3).left, ((ItemHolderInfo)localObject3).top))
            postAnimationRunner();
        }
        i += 1;
      }
      if (this.mState.mOldChangedHolders != null)
      {
        i = this.mState.mOldChangedHolders.size();
        i -= 1;
        if (i < 0)
          break label1573;
        l = ((Long)this.mState.mOldChangedHolders.keyAt(i)).longValue();
        localObject2 = (ViewHolder)this.mState.mOldChangedHolders.get(Long.valueOf(l));
        localObject3 = ((ViewHolder)localObject2).itemView;
        if (!((ViewHolder)localObject2).shouldIgnore())
          break label1529;
      }
      while (true)
      {
        i -= 1;
        break label1458;
        i = 0;
        break;
        if ((this.mRecycler.mChangedScrap == null) || (!this.mRecycler.mChangedScrap.contains(localObject2)))
          continue;
        animateChange((ViewHolder)localObject2, (ViewHolder)((ArrayMap)localObject1).get(Long.valueOf(l)));
      }
      resumeRequestLayout(false);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      State.access$2102(this.mState, this.mState.mItemCount);
      this.mDataSetHasChangedAfterLayout = false;
      State.access$1802(this.mState, false);
      State.access$1602(this.mState, false);
      onExitLayoutOrScroll();
      LayoutManager.access$1902(this.mLayout, false);
      if (this.mRecycler.mChangedScrap != null)
        this.mRecycler.mChangedScrap.clear();
      this.mState.mOldChangedHolders = null;
    }
    while (!didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1]));
    label293: label703: label738: label750: label1012: label1529: dispatchOnScrolled(0, 0);
    label909: label915: label920: label1458: return;
  }

  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return this.mScrollingChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }

  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2)
  {
    return this.mScrollingChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }

  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    return this.mScrollingChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2);
  }

  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    return this.mScrollingChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
  }

  void dispatchOnScrollStateChanged(int paramInt)
  {
    if (this.mLayout != null)
      this.mLayout.onScrollStateChanged(paramInt);
    onScrollStateChanged(paramInt);
    if (this.mScrollListener != null)
      this.mScrollListener.onScrollStateChanged(this, paramInt);
    if (this.mScrollListeners != null)
    {
      int i = this.mScrollListeners.size() - 1;
      while (i >= 0)
      {
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrollStateChanged(this, paramInt);
        i -= 1;
      }
    }
  }

  void dispatchOnScrolled(int paramInt1, int paramInt2)
  {
    int i = getScrollX();
    int j = getScrollY();
    onScrollChanged(i, j, i, j);
    onScrolled(paramInt1, paramInt2);
    if (this.mScrollListener != null)
      this.mScrollListener.onScrolled(this, paramInt1, paramInt2);
    if (this.mScrollListeners != null)
    {
      i = this.mScrollListeners.size() - 1;
      while (i >= 0)
      {
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrolled(this, paramInt1, paramInt2);
        i -= 1;
      }
    }
  }

  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }

  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchFreezeSelfOnly(paramSparseArray);
  }

  public void draw(Canvas paramCanvas)
  {
    int k = 1;
    super.draw(paramCanvas);
    int j = this.mItemDecorations.size();
    int i = 0;
    while (i < j)
    {
      ((ItemDecoration)this.mItemDecorations.get(i)).onDrawOver(paramCanvas, this, this.mState);
      i += 1;
    }
    i = 0;
    j = i;
    int m;
    if (this.mLeftGlow != null)
    {
      j = i;
      if (!this.mLeftGlow.isFinished())
      {
        m = paramCanvas.save();
        if (!this.mClipToPadding)
          break label456;
        i = getPaddingBottom();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-getHeight() + i, 0.0F);
        if ((this.mLeftGlow == null) || (!this.mLeftGlow.draw(paramCanvas)))
          break label461;
        j = 1;
        label131: paramCanvas.restoreToCount(m);
      }
    }
    i = j;
    if (this.mTopGlow != null)
    {
      i = j;
      if (!this.mTopGlow.isFinished())
      {
        m = paramCanvas.save();
        if (this.mClipToPadding)
          paramCanvas.translate(getPaddingLeft(), getPaddingTop());
        if ((this.mTopGlow == null) || (!this.mTopGlow.draw(paramCanvas)))
          break label466;
        i = 1;
        label205: i = j | i;
        paramCanvas.restoreToCount(m);
      }
    }
    j = i;
    if (this.mRightGlow != null)
    {
      j = i;
      if (!this.mRightGlow.isFinished())
      {
        m = paramCanvas.save();
        int n = getWidth();
        if (!this.mClipToPadding)
          break label471;
        j = getPaddingTop();
        label260: paramCanvas.rotate(90.0F);
        paramCanvas.translate(-j, -n);
        if ((this.mRightGlow == null) || (!this.mRightGlow.draw(paramCanvas)))
          break label476;
        j = 1;
        label298: j = i | j;
        paramCanvas.restoreToCount(m);
      }
    }
    i = j;
    if (this.mBottomGlow != null)
    {
      i = j;
      if (!this.mBottomGlow.isFinished())
      {
        m = paramCanvas.save();
        paramCanvas.rotate(180.0F);
        if (!this.mClipToPadding)
          break label481;
        paramCanvas.translate(-getWidth() + getPaddingRight(), -getHeight() + getPaddingBottom());
        label375: if ((this.mBottomGlow == null) || (!this.mBottomGlow.draw(paramCanvas)))
          break label500;
      }
    }
    label456: label461: label466: label471: label476: label481: label500: for (i = k; ; i = 0)
    {
      i = j | i;
      paramCanvas.restoreToCount(m);
      j = i;
      if (i == 0)
      {
        j = i;
        if (this.mItemAnimator != null)
        {
          j = i;
          if (this.mItemDecorations.size() > 0)
          {
            j = i;
            if (this.mItemAnimator.isRunning())
              j = 1;
          }
        }
      }
      if (j != 0)
        ViewCompat.postInvalidateOnAnimation(this);
      return;
      i = 0;
      break;
      j = 0;
      break label131;
      i = 0;
      break label205;
      j = 0;
      break label260;
      j = 0;
      break label298;
      paramCanvas.translate(-getWidth(), -getHeight());
      break label375;
    }
  }

  public boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    return super.drawChild(paramCanvas, paramView, paramLong);
  }

  void eatRequestLayout()
  {
    if (!this.mEatRequestLayout)
    {
      this.mEatRequestLayout = true;
      if (!this.mLayoutFrozen)
        this.mLayoutRequestEaten = false;
    }
  }

  void ensureBottomGlow()
  {
    if (this.mBottomGlow != null)
      return;
    this.mBottomGlow = new EdgeEffectCompat(getContext());
    if (this.mClipToPadding)
    {
      this.mBottomGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
      return;
    }
    this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
  }

  void ensureLeftGlow()
  {
    if (this.mLeftGlow != null)
      return;
    this.mLeftGlow = new EdgeEffectCompat(getContext());
    if (this.mClipToPadding)
    {
      this.mLeftGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
      return;
    }
    this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
  }

  void ensureRightGlow()
  {
    if (this.mRightGlow != null)
      return;
    this.mRightGlow = new EdgeEffectCompat(getContext());
    if (this.mClipToPadding)
    {
      this.mRightGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
      return;
    }
    this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
  }

  void ensureTopGlow()
  {
    if (this.mTopGlow != null)
      return;
    this.mTopGlow = new EdgeEffectCompat(getContext());
    if (this.mClipToPadding)
    {
      this.mTopGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
      return;
    }
    this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
  }

  public View findChildViewUnder(float paramFloat1, float paramFloat2)
  {
    int i = this.mChildHelper.getChildCount() - 1;
    while (i >= 0)
    {
      View localView = this.mChildHelper.getChildAt(i);
      float f1 = ViewCompat.getTranslationX(localView);
      float f2 = ViewCompat.getTranslationY(localView);
      if ((paramFloat1 >= localView.getLeft() + f1) && (paramFloat1 <= localView.getRight() + f1) && (paramFloat2 >= localView.getTop() + f2) && (paramFloat2 <= localView.getBottom() + f2))
        return localView;
      i -= 1;
    }
    return null;
  }

  public ViewHolder findViewHolderForAdapterPosition(int paramInt)
  {
    Object localObject;
    if (this.mDataSetHasChangedAfterLayout)
    {
      localObject = null;
      return localObject;
    }
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (true)
    {
      if (i >= j)
        break label74;
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.isRemoved()))
      {
        localObject = localViewHolder;
        if (getAdapterPositionFor(localViewHolder) == paramInt)
          break;
      }
      i += 1;
    }
    label74: return null;
  }

  public ViewHolder findViewHolderForItemId(long paramLong)
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (localViewHolder.getItemId() == paramLong))
        return localViewHolder;
      i += 1;
    }
    return null;
  }

  public ViewHolder findViewHolderForLayoutPosition(int paramInt)
  {
    return findViewHolderForPosition(paramInt, false);
  }

  @Deprecated
  public ViewHolder findViewHolderForPosition(int paramInt)
  {
    return findViewHolderForPosition(paramInt, false);
  }

  ViewHolder findViewHolderForPosition(int paramInt, boolean paramBoolean)
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.isRemoved()))
        if (paramBoolean)
        {
          if (localViewHolder.mPosition != paramInt);
        }
        else
          do
            return localViewHolder;
          while (localViewHolder.getLayoutPosition() == paramInt);
      i += 1;
    }
    return null;
  }

  public boolean fling(int paramInt1, int paramInt2)
  {
    if (this.mLayout == null)
      Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
    boolean bool2;
    int i;
    do
    {
      do
        return false;
      while (this.mLayoutFrozen);
      bool1 = this.mLayout.canScrollHorizontally();
      bool2 = this.mLayout.canScrollVertically();
      if (bool1)
      {
        i = paramInt1;
        if (Math.abs(paramInt1) >= this.mMinFlingVelocity);
      }
      else
      {
        i = 0;
      }
      if (bool2)
      {
        paramInt1 = paramInt2;
        if (Math.abs(paramInt2) >= this.mMinFlingVelocity)
          continue;
      }
      paramInt1 = 0;
    }
    while (((i == 0) && (paramInt1 == 0)) || (dispatchNestedPreFling(i, paramInt1)));
    if ((bool1) || (bool2));
    for (boolean bool1 = true; ; bool1 = false)
    {
      dispatchNestedFling(i, paramInt1, bool1);
      if (!bool1)
        break;
      paramInt2 = Math.max(-this.mMaxFlingVelocity, Math.min(i, this.mMaxFlingVelocity));
      paramInt1 = Math.max(-this.mMaxFlingVelocity, Math.min(paramInt1, this.mMaxFlingVelocity));
      this.mViewFlinger.fling(paramInt2, paramInt1);
      return true;
    }
  }

  public View focusSearch(View paramView, int paramInt)
  {
    Object localObject = this.mLayout.onInterceptFocusSearch(paramView, paramInt);
    if (localObject != null)
      return localObject;
    View localView = FocusFinder.getInstance().findNextFocus(this, paramView, paramInt);
    localObject = localView;
    if (localView == null)
    {
      localObject = localView;
      if (this.mAdapter != null)
      {
        localObject = localView;
        if (this.mLayout != null)
        {
          localObject = localView;
          if (!isComputingLayout())
          {
            localObject = localView;
            if (!this.mLayoutFrozen)
            {
              eatRequestLayout();
              localObject = this.mLayout.onFocusSearchFailed(paramView, paramInt, this.mRecycler, this.mState);
              resumeRequestLayout(false);
            }
          }
        }
      }
    }
    if (localObject != null)
      return localObject;
    return (View)super.focusSearch(paramView, paramInt);
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager");
    return this.mLayout.generateDefaultLayoutParams();
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager");
    return this.mLayout.generateLayoutParams(getContext(), paramAttributeSet);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (this.mLayout == null)
      throw new IllegalStateException("RecyclerView has no LayoutManager");
    return this.mLayout.generateLayoutParams(paramLayoutParams);
  }

  public Adapter getAdapter()
  {
    return this.mAdapter;
  }

  public int getBaseline()
  {
    if (this.mLayout != null)
      return this.mLayout.getBaseline();
    return super.getBaseline();
  }

  long getChangedHolderKey(ViewHolder paramViewHolder)
  {
    if (this.mAdapter.hasStableIds())
      return paramViewHolder.getItemId();
    return paramViewHolder.mPosition;
  }

  public int getChildAdapterPosition(View paramView)
  {
    paramView = getChildViewHolderInt(paramView);
    if (paramView != null)
      return paramView.getAdapterPosition();
    return -1;
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mChildDrawingOrderCallback == null)
      return super.getChildDrawingOrder(paramInt1, paramInt2);
    return this.mChildDrawingOrderCallback.onGetChildDrawingOrder(paramInt1, paramInt2);
  }

  public long getChildItemId(View paramView)
  {
    if ((this.mAdapter == null) || (!this.mAdapter.hasStableIds()));
    do
    {
      return -1L;
      paramView = getChildViewHolderInt(paramView);
    }
    while (paramView == null);
    return paramView.getItemId();
  }

  public int getChildLayoutPosition(View paramView)
  {
    paramView = getChildViewHolderInt(paramView);
    if (paramView != null)
      return paramView.getLayoutPosition();
    return -1;
  }

  @Deprecated
  public int getChildPosition(View paramView)
  {
    return getChildAdapterPosition(paramView);
  }

  public ViewHolder getChildViewHolder(View paramView)
  {
    ViewParent localViewParent = paramView.getParent();
    if ((localViewParent != null) && (localViewParent != this))
      throw new IllegalArgumentException("View " + paramView + " is not a direct child of " + this);
    return getChildViewHolderInt(paramView);
  }

  public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate()
  {
    return this.mAccessibilityDelegate;
  }

  public ItemAnimator getItemAnimator()
  {
    return this.mItemAnimator;
  }

  Rect getItemDecorInsetsForChild(View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!localLayoutParams.mInsetsDirty)
      return localLayoutParams.mDecorInsets;
    Rect localRect = localLayoutParams.mDecorInsets;
    localRect.set(0, 0, 0, 0);
    int j = this.mItemDecorations.size();
    int i = 0;
    while (i < j)
    {
      this.mTempRect.set(0, 0, 0, 0);
      ((ItemDecoration)this.mItemDecorations.get(i)).getItemOffsets(this.mTempRect, paramView, this, this.mState);
      localRect.left += this.mTempRect.left;
      localRect.top += this.mTempRect.top;
      localRect.right += this.mTempRect.right;
      localRect.bottom += this.mTempRect.bottom;
      i += 1;
    }
    localLayoutParams.mInsetsDirty = false;
    return localRect;
  }

  public LayoutManager getLayoutManager()
  {
    return this.mLayout;
  }

  public int getMaxFlingVelocity()
  {
    return this.mMaxFlingVelocity;
  }

  public int getMinFlingVelocity()
  {
    return this.mMinFlingVelocity;
  }

  public RecycledViewPool getRecycledViewPool()
  {
    return this.mRecycler.getRecycledViewPool();
  }

  public int getScrollState()
  {
    return this.mScrollState;
  }

  public boolean hasFixedSize()
  {
    return this.mHasFixedSize;
  }

  public boolean hasNestedScrollingParent()
  {
    return this.mScrollingChildHelper.hasNestedScrollingParent();
  }

  public boolean hasPendingAdapterUpdates()
  {
    return (!this.mFirstLayoutComplete) || (this.mDataSetHasChangedAfterLayout) || (this.mAdapterHelper.hasPendingUpdates());
  }

  void initAdapterManager()
  {
    this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback()
    {
      void dispatchUpdate(AdapterHelper.UpdateOp paramUpdateOp)
      {
        switch (paramUpdateOp.cmd)
        {
        default:
          return;
        case 0:
          RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, paramUpdateOp.positionStart, paramUpdateOp.itemCount);
          return;
        case 1:
          RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, paramUpdateOp.positionStart, paramUpdateOp.itemCount);
          return;
        case 2:
          RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, paramUpdateOp.positionStart, paramUpdateOp.itemCount, paramUpdateOp.payload);
          return;
        case 3:
        }
        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, paramUpdateOp.positionStart, paramUpdateOp.itemCount, 1);
      }

      public RecyclerView.ViewHolder findViewHolder(int paramInt)
      {
        RecyclerView.ViewHolder localViewHolder2 = RecyclerView.this.findViewHolderForPosition(paramInt, true);
        RecyclerView.ViewHolder localViewHolder1;
        if (localViewHolder2 == null)
          localViewHolder1 = null;
        do
        {
          return localViewHolder1;
          localViewHolder1 = localViewHolder2;
        }
        while (!RecyclerView.this.mChildHelper.isHidden(localViewHolder2.itemView));
        return null;
      }

      public void markViewHoldersUpdated(int paramInt1, int paramInt2, Object paramObject)
      {
        RecyclerView.this.viewRangeUpdate(paramInt1, paramInt2, paramObject);
        RecyclerView.this.mItemsChanged = true;
      }

      public void offsetPositionsForAdd(int paramInt1, int paramInt2)
      {
        RecyclerView.this.offsetPositionRecordsForInsert(paramInt1, paramInt2);
        RecyclerView.this.mItemsAddedOrRemoved = true;
      }

      public void offsetPositionsForMove(int paramInt1, int paramInt2)
      {
        RecyclerView.this.offsetPositionRecordsForMove(paramInt1, paramInt2);
        RecyclerView.this.mItemsAddedOrRemoved = true;
      }

      public void offsetPositionsForRemovingInvisible(int paramInt1, int paramInt2)
      {
        RecyclerView.this.offsetPositionRecordsForRemove(paramInt1, paramInt2, true);
        RecyclerView.this.mItemsAddedOrRemoved = true;
        RecyclerView.State.access$1212(RecyclerView.this.mState, paramInt2);
      }

      public void offsetPositionsForRemovingLaidOutOrNewView(int paramInt1, int paramInt2)
      {
        RecyclerView.this.offsetPositionRecordsForRemove(paramInt1, paramInt2, false);
        RecyclerView.this.mItemsAddedOrRemoved = true;
      }

      public void onDispatchFirstPass(AdapterHelper.UpdateOp paramUpdateOp)
      {
        dispatchUpdate(paramUpdateOp);
      }

      public void onDispatchSecondPass(AdapterHelper.UpdateOp paramUpdateOp)
      {
        dispatchUpdate(paramUpdateOp);
      }
    });
  }

  void invalidateGlows()
  {
    this.mBottomGlow = null;
    this.mTopGlow = null;
    this.mRightGlow = null;
    this.mLeftGlow = null;
  }

  public void invalidateItemDecorations()
  {
    if (this.mItemDecorations.size() == 0)
      return;
    if (this.mLayout != null)
      this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
    markItemDecorInsetsDirty();
    requestLayout();
  }

  boolean isAccessibilityEnabled()
  {
    return (this.mAccessibilityManager != null) && (this.mAccessibilityManager.isEnabled());
  }

  public boolean isAnimating()
  {
    return (this.mItemAnimator != null) && (this.mItemAnimator.isRunning());
  }

  public boolean isAttachedToWindow()
  {
    return this.mIsAttached;
  }

  public boolean isComputingLayout()
  {
    return this.mLayoutOrScrollCounter > 0;
  }

  public boolean isLayoutFrozen()
  {
    return this.mLayoutFrozen;
  }

  public boolean isNestedScrollingEnabled()
  {
    return this.mScrollingChildHelper.isNestedScrollingEnabled();
  }

  void markItemDecorInsetsDirty()
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
      i += 1;
    }
    this.mRecycler.markItemDecorInsetsDirty();
  }

  void markKnownViewsInvalid()
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()))
        localViewHolder.addFlags(6);
      i += 1;
    }
    markItemDecorInsetsDirty();
    this.mRecycler.markKnownViewsInvalid();
  }

  public void offsetChildrenHorizontal(int paramInt)
  {
    int j = this.mChildHelper.getChildCount();
    int i = 0;
    while (i < j)
    {
      this.mChildHelper.getChildAt(i).offsetLeftAndRight(paramInt);
      i += 1;
    }
  }

  public void offsetChildrenVertical(int paramInt)
  {
    int j = this.mChildHelper.getChildCount();
    int i = 0;
    while (i < j)
    {
      this.mChildHelper.getChildAt(i).offsetTopAndBottom(paramInt);
      i += 1;
    }
  }

  void offsetPositionRecordsForInsert(int paramInt1, int paramInt2)
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()) && (localViewHolder.mPosition >= paramInt1))
      {
        localViewHolder.offsetPosition(paramInt2, false);
        State.access$1402(this.mState, true);
      }
      i += 1;
    }
    this.mRecycler.offsetPositionRecordsForInsert(paramInt1, paramInt2);
    requestLayout();
  }

  void offsetPositionRecordsForMove(int paramInt1, int paramInt2)
  {
    int n = this.mChildHelper.getUnfilteredChildCount();
    int k;
    int i;
    int j;
    if (paramInt1 < paramInt2)
    {
      k = paramInt1;
      i = paramInt2;
      j = -1;
    }
    while (true)
    {
      int m = 0;
      ViewHolder localViewHolder;
      while (true)
      {
        if (m >= n)
          break label127;
        localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(m));
        if ((localViewHolder == null) || (localViewHolder.mPosition < k) || (localViewHolder.mPosition > i))
        {
          m += 1;
          continue;
          k = paramInt2;
          i = paramInt1;
          j = 1;
          break;
        }
      }
      if (localViewHolder.mPosition == paramInt1)
        localViewHolder.offsetPosition(paramInt2 - paramInt1, false);
      while (true)
      {
        State.access$1402(this.mState, true);
        break;
        localViewHolder.offsetPosition(j, false);
      }
    }
    label127: this.mRecycler.offsetPositionRecordsForMove(paramInt1, paramInt2);
    requestLayout();
  }

  void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    if (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()))
      {
        if (localViewHolder.mPosition < paramInt1 + paramInt2)
          break label83;
        localViewHolder.offsetPosition(-paramInt2, paramBoolean);
        State.access$1402(this.mState, true);
      }
      while (true)
      {
        i += 1;
        break;
        label83: if (localViewHolder.mPosition < paramInt1)
          continue;
        localViewHolder.flagRemovedAndOffsetPosition(paramInt1 - 1, -paramInt2, paramBoolean);
        State.access$1402(this.mState, true);
      }
    }
    this.mRecycler.offsetPositionRecordsForRemove(paramInt1, paramInt2, paramBoolean);
    requestLayout();
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mLayoutOrScrollCounter = 0;
    this.mIsAttached = true;
    this.mFirstLayoutComplete = false;
    if (this.mLayout != null)
      this.mLayout.dispatchAttachedToWindow(this);
    this.mPostedAnimatorRunner = false;
  }

  public void onChildAttachedToWindow(View paramView)
  {
  }

  public void onChildDetachedFromWindow(View paramView)
  {
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mItemAnimator != null)
      this.mItemAnimator.endAnimations();
    this.mFirstLayoutComplete = false;
    stopScroll();
    this.mIsAttached = false;
    if (this.mLayout != null)
      this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
    removeCallbacks(this.mItemAnimatorRunner);
  }

  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int j = this.mItemDecorations.size();
    int i = 0;
    while (i < j)
    {
      ((ItemDecoration)this.mItemDecorations.get(i)).onDraw(paramCanvas, this, this.mState);
      i += 1;
    }
  }

  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (this.mLayout == null);
    label110: label113: 
    while (true)
    {
      return false;
      if ((this.mLayoutFrozen) || ((MotionEventCompat.getSource(paramMotionEvent) & 0x2) == 0) || (paramMotionEvent.getAction() != 8))
        continue;
      float f1;
      float f2;
      if (this.mLayout.canScrollVertically())
      {
        f1 = -MotionEventCompat.getAxisValue(paramMotionEvent, 9);
        if (!this.mLayout.canScrollHorizontally())
          break label110;
        f2 = MotionEventCompat.getAxisValue(paramMotionEvent, 10);
      }
      while (true)
      {
        if ((f1 == 0.0F) && (f2 == 0.0F))
          break label113;
        float f3 = getScrollFactor();
        scrollByInternal((int)(f2 * f3), (int)(f1 * f3), paramMotionEvent);
        return false;
        f1 = 0.0F;
        break;
        f2 = 0.0F;
      }
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mLayoutFrozen)
      return false;
    if (dispatchOnItemTouchIntercept(paramMotionEvent))
    {
      cancelTouch();
      return true;
    }
    if (this.mLayout == null)
      return false;
    boolean bool1 = this.mLayout.canScrollHorizontally();
    boolean bool2 = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain();
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int j = MotionEventCompat.getActionMasked(paramMotionEvent);
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    switch (j)
    {
    case 4:
    default:
    case 0:
    case 5:
    case 2:
    case 6:
    case 1:
    case 3:
    }
    while (this.mScrollState == 1)
    {
      return true;
      if (this.mIgnoreMotionEventTillDown)
        this.mIgnoreMotionEventTillDown = false;
      this.mScrollPointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      i = (int)(paramMotionEvent.getX() + 0.5F);
      this.mLastTouchX = i;
      this.mInitialTouchX = i;
      i = (int)(paramMotionEvent.getY() + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
      if (this.mScrollState == 2)
      {
        getParent().requestDisallowInterceptTouchEvent(true);
        setScrollState(1);
      }
      i = 0;
      if (bool1)
        i = 0x0 | 0x1;
      j = i;
      if (bool2)
        j = i | 0x2;
      startNestedScroll(j);
      continue;
      this.mScrollPointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
      j = (int)(MotionEventCompat.getX(paramMotionEvent, i) + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      i = (int)(MotionEventCompat.getY(paramMotionEvent, i) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
      continue;
      j = MotionEventCompat.findPointerIndex(paramMotionEvent, this.mScrollPointerId);
      if (j < 0)
      {
        Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
        return false;
      }
      i = (int)(MotionEventCompat.getX(paramMotionEvent, j) + 0.5F);
      j = (int)(MotionEventCompat.getY(paramMotionEvent, j) + 0.5F);
      if (this.mScrollState == 1)
        continue;
      int m = i - this.mInitialTouchX;
      int k = j - this.mInitialTouchY;
      j = 0;
      i = j;
      if (bool1)
      {
        i = j;
        if (Math.abs(m) > this.mTouchSlop)
        {
          j = this.mInitialTouchX;
          int n = this.mTouchSlop;
          if (m >= 0)
            break label531;
          i = -1;
          label440: this.mLastTouchX = (i * n + j);
          i = 1;
        }
      }
      j = i;
      if (bool2)
      {
        j = i;
        if (Math.abs(k) > this.mTouchSlop)
        {
          j = this.mInitialTouchY;
          m = this.mTouchSlop;
          if (k >= 0)
            break label536;
        }
      }
      label531: label536: for (i = -1; ; i = 1)
      {
        this.mLastTouchY = (i * m + j);
        j = 1;
        if (j == 0)
          break;
        paramMotionEvent = getParent();
        if (paramMotionEvent != null)
          paramMotionEvent.requestDisallowInterceptTouchEvent(true);
        setScrollState(1);
        break;
        i = 1;
        break label440;
      }
      onPointerUp(paramMotionEvent);
      continue;
      this.mVelocityTracker.clear();
      stopNestedScroll();
      continue;
      cancelTouch();
    }
    return false;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    eatRequestLayout();
    TraceCompat.beginSection("RV OnLayout");
    dispatchLayout();
    TraceCompat.endSection();
    resumeRequestLayout(false);
    this.mFirstLayoutComplete = true;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mAdapterUpdateDuringMeasure)
    {
      eatRequestLayout();
      processAdapterUpdatesAndSetAnimationFlags();
      if (this.mState.mRunPredictiveAnimations)
      {
        State.access$1702(this.mState, true);
        this.mAdapterUpdateDuringMeasure = false;
        resumeRequestLayout(false);
      }
    }
    else
    {
      if (this.mAdapter == null)
        break label107;
      this.mState.mItemCount = this.mAdapter.getItemCount();
      label65: if (this.mLayout != null)
        break label118;
      defaultOnMeasure(paramInt1, paramInt2);
    }
    while (true)
    {
      State.access$1702(this.mState, false);
      return;
      this.mAdapterHelper.consumeUpdatesInOnePass();
      State.access$1702(this.mState, false);
      break;
      label107: this.mState.mItemCount = 0;
      break label65;
      label118: this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
    }
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    this.mPendingSavedState = ((SavedState)paramParcelable);
    super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
    if ((this.mLayout != null) && (this.mPendingSavedState.mLayoutState != null))
      this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
  }

  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if (this.mPendingSavedState != null)
    {
      localSavedState.copyFrom(this.mPendingSavedState);
      return localSavedState;
    }
    if (this.mLayout != null)
    {
      localSavedState.mLayoutState = this.mLayout.onSaveInstanceState();
      return localSavedState;
    }
    localSavedState.mLayoutState = null;
    return localSavedState;
  }

  public void onScrollStateChanged(int paramInt)
  {
  }

  public void onScrolled(int paramInt1, int paramInt2)
  {
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if ((paramInt1 != paramInt3) || (paramInt2 != paramInt4))
      invalidateGlows();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mLayoutFrozen) || (this.mIgnoreMotionEventTillDown))
      return false;
    if (dispatchOnItemTouch(paramMotionEvent))
    {
      cancelTouch();
      return true;
    }
    if (this.mLayout == null)
      return false;
    boolean bool1 = this.mLayout.canScrollHorizontally();
    boolean bool2 = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain();
    int i2 = 0;
    MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
    int k = MotionEventCompat.getActionMasked(paramMotionEvent);
    int j = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (k == 0)
    {
      int[] arrayOfInt = this.mNestedOffsets;
      this.mNestedOffsets[1] = 0;
      arrayOfInt[0] = 0;
    }
    localMotionEvent.offsetLocation(this.mNestedOffsets[0], this.mNestedOffsets[1]);
    int i = i2;
    switch (k)
    {
    default:
      i = i2;
    case 4:
    case 0:
    case 5:
    case 2:
    case 6:
    case 1:
    case 3:
    }
    while (true)
    {
      if (i == 0)
        this.mVelocityTracker.addMovement(localMotionEvent);
      localMotionEvent.recycle();
      return true;
      this.mScrollPointerId = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      i = (int)(paramMotionEvent.getX() + 0.5F);
      this.mLastTouchX = i;
      this.mInitialTouchX = i;
      i = (int)(paramMotionEvent.getY() + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
      i = 0;
      if (bool1)
        i = 0x0 | 0x1;
      j = i;
      if (bool2)
        j = i | 0x2;
      startNestedScroll(j);
      i = i2;
      continue;
      this.mScrollPointerId = MotionEventCompat.getPointerId(paramMotionEvent, j);
      i = (int)(MotionEventCompat.getX(paramMotionEvent, j) + 0.5F);
      this.mLastTouchX = i;
      this.mInitialTouchX = i;
      i = (int)(MotionEventCompat.getY(paramMotionEvent, j) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
      i = i2;
      continue;
      i = MotionEventCompat.findPointerIndex(paramMotionEvent, this.mScrollPointerId);
      if (i < 0)
      {
        Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
        return false;
      }
      int i3 = (int)(MotionEventCompat.getX(paramMotionEvent, i) + 0.5F);
      int i4 = (int)(MotionEventCompat.getY(paramMotionEvent, i) + 0.5F);
      int m = this.mLastTouchX - i3;
      k = this.mLastTouchY - i4;
      j = m;
      i = k;
      if (dispatchNestedPreScroll(m, k, this.mScrollConsumed, this.mScrollOffset))
      {
        j = m - this.mScrollConsumed[0];
        i = k - this.mScrollConsumed[1];
        localMotionEvent.offsetLocation(this.mScrollOffset[0], this.mScrollOffset[1]);
        paramMotionEvent = this.mNestedOffsets;
        paramMotionEvent[0] += this.mScrollOffset[0];
        paramMotionEvent = this.mNestedOffsets;
        paramMotionEvent[1] += this.mScrollOffset[1];
      }
      int n = j;
      m = i;
      if (this.mScrollState != 1)
      {
        n = 0;
        k = j;
        m = n;
        if (bool1)
        {
          k = j;
          m = n;
          if (Math.abs(j) > this.mTouchSlop)
          {
            if (j <= 0)
              break label816;
            k = j - this.mTouchSlop;
            label639: m = 1;
          }
        }
        j = i;
        int i1 = m;
        if (bool2)
        {
          j = i;
          i1 = m;
          if (Math.abs(i) > this.mTouchSlop)
          {
            if (i <= 0)
              break label828;
            j = i - this.mTouchSlop;
            label689: i1 = 1;
          }
        }
        n = k;
        m = j;
        if (i1 != 0)
        {
          paramMotionEvent = getParent();
          if (paramMotionEvent != null)
            paramMotionEvent.requestDisallowInterceptTouchEvent(true);
          setScrollState(1);
          m = j;
          n = k;
        }
      }
      i = i2;
      if (this.mScrollState != 1)
        continue;
      this.mLastTouchX = (i3 - this.mScrollOffset[0]);
      this.mLastTouchY = (i4 - this.mScrollOffset[1]);
      if (bool1)
        label777: if (!bool2)
          break label846;
      while (true)
      {
        i = i2;
        if (!scrollByInternal(n, m, localMotionEvent))
          break;
        getParent().requestDisallowInterceptTouchEvent(true);
        i = i2;
        break;
        label816: k = j + this.mTouchSlop;
        break label639;
        label828: j = i + this.mTouchSlop;
        break label689;
        n = 0;
        break label777;
        label846: m = 0;
      }
      onPointerUp(paramMotionEvent);
      i = i2;
      continue;
      this.mVelocityTracker.addMovement(localMotionEvent);
      i = 1;
      this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxFlingVelocity);
      float f1;
      label909: float f2;
      if (bool1)
      {
        f1 = -VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mScrollPointerId);
        if (!bool2)
          break label967;
        f2 = -VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mScrollPointerId);
      }
      while (true)
      {
        if (((f1 == 0.0F) && (f2 == 0.0F)) || (!fling((int)f1, (int)f2)))
          setScrollState(0);
        resetTouch();
        break;
        f1 = 0.0F;
        break label909;
        label967: f2 = 0.0F;
      }
      cancelTouch();
      i = i2;
    }
  }

  void rebindUpdatedViewHolders()
  {
    int j = this.mChildHelper.getChildCount();
    int i = 0;
    if (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
      if ((localViewHolder == null) || (localViewHolder.shouldIgnore()));
      while (true)
      {
        i += 1;
        break;
        if ((localViewHolder.isRemoved()) || (localViewHolder.isInvalid()))
        {
          requestLayout();
          continue;
        }
        if (!localViewHolder.needsUpdate())
          continue;
        int k = this.mAdapter.getItemViewType(localViewHolder.mPosition);
        if (localViewHolder.getItemViewType() != k)
          break label131;
        if ((!localViewHolder.isChanged()) || (!supportsChangeAnimations()))
        {
          this.mAdapter.bindViewHolder(localViewHolder, localViewHolder.mPosition);
          continue;
        }
        requestLayout();
      }
      label131: requestLayout();
    }
  }

  protected void removeDetachedView(View paramView, boolean paramBoolean)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    if (localViewHolder != null)
    {
      if (!localViewHolder.isTmpDetached())
        break label32;
      localViewHolder.clearTmpDetachFlag();
    }
    label32: 
    do
    {
      dispatchChildDetached(paramView);
      super.removeDetachedView(paramView, paramBoolean);
      return;
    }
    while (localViewHolder.shouldIgnore());
    throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + localViewHolder);
  }

  public void removeItemDecoration(ItemDecoration paramItemDecoration)
  {
    if (this.mLayout != null)
      this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
    this.mItemDecorations.remove(paramItemDecoration);
    if (this.mItemDecorations.isEmpty())
      if (ViewCompat.getOverScrollMode(this) != 2)
        break label60;
    label60: for (boolean bool = true; ; bool = false)
    {
      setWillNotDraw(bool);
      markItemDecorInsetsDirty();
      requestLayout();
      return;
    }
  }

  public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener)
  {
    if (this.mOnChildAttachStateListeners == null)
      return;
    this.mOnChildAttachStateListeners.remove(paramOnChildAttachStateChangeListener);
  }

  public void removeOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener)
  {
    this.mOnItemTouchListeners.remove(paramOnItemTouchListener);
    if (this.mActiveOnItemTouchListener == paramOnItemTouchListener)
      this.mActiveOnItemTouchListener = null;
  }

  public void removeOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    if (this.mScrollListeners != null)
      this.mScrollListeners.remove(paramOnScrollListener);
  }

  public void requestChildFocus(View paramView1, View paramView2)
  {
    boolean bool = false;
    if ((!this.mLayout.onRequestChildFocus(this, this.mState, paramView1, paramView2)) && (paramView2 != null))
    {
      this.mTempRect.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
      Object localObject = paramView2.getLayoutParams();
      if ((localObject instanceof LayoutParams))
      {
        localObject = (LayoutParams)localObject;
        if (!((LayoutParams)localObject).mInsetsDirty)
        {
          localObject = ((LayoutParams)localObject).mDecorInsets;
          Rect localRect = this.mTempRect;
          localRect.left -= ((Rect)localObject).left;
          localRect = this.mTempRect;
          localRect.right += ((Rect)localObject).right;
          localRect = this.mTempRect;
          localRect.top -= ((Rect)localObject).top;
          localRect = this.mTempRect;
          localRect.bottom += ((Rect)localObject).bottom;
        }
      }
      offsetDescendantRectToMyCoords(paramView2, this.mTempRect);
      offsetRectIntoDescendantCoords(paramView1, this.mTempRect);
      localObject = this.mTempRect;
      if (!this.mFirstLayoutComplete)
        bool = true;
      requestChildRectangleOnScreen(paramView1, (Rect)localObject, bool);
    }
    super.requestChildFocus(paramView1, paramView2);
  }

  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    return this.mLayout.requestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean);
  }

  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    int j = this.mOnItemTouchListeners.size();
    int i = 0;
    while (i < j)
    {
      ((OnItemTouchListener)this.mOnItemTouchListeners.get(i)).onRequestDisallowInterceptTouchEvent(paramBoolean);
      i += 1;
    }
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }

  public void requestLayout()
  {
    if ((!this.mEatRequestLayout) && (!this.mLayoutFrozen))
    {
      super.requestLayout();
      return;
    }
    this.mLayoutRequestEaten = true;
  }

  void resumeRequestLayout(boolean paramBoolean)
  {
    if (this.mEatRequestLayout)
    {
      if ((paramBoolean) && (this.mLayoutRequestEaten) && (!this.mLayoutFrozen) && (this.mLayout != null) && (this.mAdapter != null))
        dispatchLayout();
      this.mEatRequestLayout = false;
      if (!this.mLayoutFrozen)
        this.mLayoutRequestEaten = false;
    }
  }

  void saveOldPositions()
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    while (i < j)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
      if (!localViewHolder.shouldIgnore())
        localViewHolder.saveOldPosition();
      i += 1;
    }
  }

  public void scrollBy(int paramInt1, int paramInt2)
  {
    if (this.mLayout == null);
    boolean bool1;
    boolean bool2;
    do
    {
      Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      do
        return;
      while (this.mLayoutFrozen);
      bool1 = this.mLayout.canScrollHorizontally();
      bool2 = this.mLayout.canScrollVertically();
    }
    while ((!bool1) && (!bool2));
    if (bool1)
      if (!bool2)
        break label73;
    while (true)
    {
      scrollByInternal(paramInt1, paramInt2, null);
      return;
      paramInt1 = 0;
      break;
      label73: paramInt2 = 0;
    }
  }

  boolean scrollByInternal(int paramInt1, int paramInt2, MotionEvent paramMotionEvent)
  {
    int j = 0;
    int i2 = 0;
    int m = 0;
    int n = 0;
    int i = 0;
    int i3 = 0;
    int k = 0;
    int i1 = 0;
    consumePendingUpdateOperations();
    if (this.mAdapter != null)
    {
      eatRequestLayout();
      onEnterLayoutOrScroll();
      TraceCompat.beginSection("RV Scroll");
      i = i3;
      j = i2;
      if (paramInt1 != 0)
      {
        i = this.mLayout.scrollHorizontallyBy(paramInt1, this.mRecycler, this.mState);
        j = paramInt1 - i;
      }
      k = i1;
      m = n;
      if (paramInt2 != 0)
      {
        k = this.mLayout.scrollVerticallyBy(paramInt2, this.mRecycler, this.mState);
        m = paramInt2 - k;
      }
      TraceCompat.endSection();
      if (supportsChangeAnimations())
      {
        i1 = this.mChildHelper.getChildCount();
        n = 0;
        if (n < i1)
        {
          View localView = this.mChildHelper.getChildAt(n);
          Object localObject = getChildViewHolder(localView);
          if ((localObject != null) && (((ViewHolder)localObject).mShadowingHolder != null))
          {
            localObject = ((ViewHolder)localObject).mShadowingHolder;
            if (localObject == null)
              break label273;
          }
          label273: for (localObject = ((ViewHolder)localObject).itemView; ; localObject = null)
          {
            if (localObject != null)
            {
              i2 = localView.getLeft();
              i3 = localView.getTop();
              if ((i2 != ((View)localObject).getLeft()) || (i3 != ((View)localObject).getTop()))
                ((View)localObject).layout(i2, i3, ((View)localObject).getWidth() + i2, ((View)localObject).getHeight() + i3);
            }
            n += 1;
            break;
          }
        }
      }
      onExitLayoutOrScroll();
      resumeRequestLayout(false);
    }
    if (!this.mItemDecorations.isEmpty())
      invalidate();
    if (dispatchNestedScroll(i, k, j, m, this.mScrollOffset))
    {
      this.mLastTouchX -= this.mScrollOffset[0];
      this.mLastTouchY -= this.mScrollOffset[1];
      if (paramMotionEvent != null)
        paramMotionEvent.offsetLocation(this.mScrollOffset[0], this.mScrollOffset[1]);
      paramMotionEvent = this.mNestedOffsets;
      paramMotionEvent[0] += this.mScrollOffset[0];
      paramMotionEvent = this.mNestedOffsets;
      paramMotionEvent[1] += this.mScrollOffset[1];
    }
    while (true)
    {
      if ((i != 0) || (k != 0))
        dispatchOnScrolled(i, k);
      if (!awakenScrollBars())
        invalidate();
      if ((i == 0) && (k == 0))
        break;
      return true;
      if (ViewCompat.getOverScrollMode(this) == 2)
        continue;
      if (paramMotionEvent != null)
        pullGlows(paramMotionEvent.getX(), j, paramMotionEvent.getY(), m);
      considerReleasingGlowsOnScroll(paramInt1, paramInt2);
    }
    return false;
  }

  public void scrollTo(int paramInt1, int paramInt2)
  {
    throw new UnsupportedOperationException("RecyclerView does not support scrolling to an absolute position.");
  }

  public void scrollToPosition(int paramInt)
  {
    if (this.mLayoutFrozen)
      return;
    stopScroll();
    if (this.mLayout == null)
    {
      Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    this.mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }

  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent)
  {
    if (shouldDeferAccessibilityEvent(paramAccessibilityEvent))
      return;
    super.sendAccessibilityEventUnchecked(paramAccessibilityEvent);
  }

  public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate paramRecyclerViewAccessibilityDelegate)
  {
    this.mAccessibilityDelegate = paramRecyclerViewAccessibilityDelegate;
    ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
  }

  public void setAdapter(Adapter paramAdapter)
  {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, false, true);
    requestLayout();
  }

  public void setChildDrawingOrderCallback(ChildDrawingOrderCallback paramChildDrawingOrderCallback)
  {
    if (paramChildDrawingOrderCallback == this.mChildDrawingOrderCallback)
      return;
    this.mChildDrawingOrderCallback = paramChildDrawingOrderCallback;
    if (this.mChildDrawingOrderCallback != null);
    for (boolean bool = true; ; bool = false)
    {
      setChildrenDrawingOrderEnabled(bool);
      return;
    }
  }

  public void setClipToPadding(boolean paramBoolean)
  {
    if (paramBoolean != this.mClipToPadding)
      invalidateGlows();
    this.mClipToPadding = paramBoolean;
    super.setClipToPadding(paramBoolean);
    if (this.mFirstLayoutComplete)
      requestLayout();
  }

  public void setHasFixedSize(boolean paramBoolean)
  {
    this.mHasFixedSize = paramBoolean;
  }

  public void setItemAnimator(ItemAnimator paramItemAnimator)
  {
    if (this.mItemAnimator != null)
    {
      this.mItemAnimator.endAnimations();
      this.mItemAnimator.setListener(null);
    }
    this.mItemAnimator = paramItemAnimator;
    if (this.mItemAnimator != null)
      this.mItemAnimator.setListener(this.mItemAnimatorListener);
  }

  public void setItemViewCacheSize(int paramInt)
  {
    this.mRecycler.setViewCacheSize(paramInt);
  }

  public void setLayoutFrozen(boolean paramBoolean)
  {
    if (paramBoolean != this.mLayoutFrozen)
    {
      assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
      if (!paramBoolean)
      {
        this.mLayoutFrozen = paramBoolean;
        if ((this.mLayoutRequestEaten) && (this.mLayout != null) && (this.mAdapter != null))
          requestLayout();
        this.mLayoutRequestEaten = false;
      }
    }
    else
    {
      return;
    }
    long l = SystemClock.uptimeMillis();
    onTouchEvent(MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0));
    this.mLayoutFrozen = paramBoolean;
    this.mIgnoreMotionEventTillDown = true;
    stopScroll();
  }

  public void setLayoutManager(LayoutManager paramLayoutManager)
  {
    if (paramLayoutManager == this.mLayout)
      return;
    if (this.mLayout != null)
    {
      if (this.mIsAttached)
        this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
      this.mLayout.setRecyclerView(null);
    }
    this.mRecycler.clear();
    this.mChildHelper.removeAllViewsUnfiltered();
    this.mLayout = paramLayoutManager;
    if (paramLayoutManager != null)
    {
      if (paramLayoutManager.mRecyclerView != null)
        throw new IllegalArgumentException("LayoutManager " + paramLayoutManager + " is already attached to a RecyclerView: " + paramLayoutManager.mRecyclerView);
      this.mLayout.setRecyclerView(this);
      if (this.mIsAttached)
        this.mLayout.dispatchAttachedToWindow(this);
    }
    requestLayout();
  }

  public void setNestedScrollingEnabled(boolean paramBoolean)
  {
    this.mScrollingChildHelper.setNestedScrollingEnabled(paramBoolean);
  }

  @Deprecated
  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    this.mScrollListener = paramOnScrollListener;
  }

  public void setRecycledViewPool(RecycledViewPool paramRecycledViewPool)
  {
    this.mRecycler.setRecycledViewPool(paramRecycledViewPool);
  }

  public void setRecyclerListener(RecyclerListener paramRecyclerListener)
  {
    this.mRecyclerListener = paramRecyclerListener;
  }

  public void setScrollingTouchSlop(int paramInt)
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
    switch (paramInt)
    {
    default:
      Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + paramInt + "; using default value");
    case 0:
      this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
      return;
    case 1:
    }
    this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(localViewConfiguration);
  }

  public void setViewCacheExtension(ViewCacheExtension paramViewCacheExtension)
  {
    this.mRecycler.setViewCacheExtension(paramViewCacheExtension);
  }

  boolean shouldDeferAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (isComputingLayout())
    {
      int i = 0;
      if (paramAccessibilityEvent != null)
        i = AccessibilityEventCompat.getContentChangeTypes(paramAccessibilityEvent);
      int j = i;
      if (i == 0)
        j = 0;
      this.mEatenAccessibilityChangeFlags |= j;
      return true;
    }
    return false;
  }

  public void smoothScrollBy(int paramInt1, int paramInt2)
  {
    if (this.mLayout == null);
    do
    {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      do
        return;
      while (this.mLayoutFrozen);
      if (!this.mLayout.canScrollHorizontally())
        paramInt1 = 0;
      if (this.mLayout.canScrollVertically())
        continue;
      paramInt2 = 0;
    }
    while ((paramInt1 == 0) && (paramInt2 == 0));
    this.mViewFlinger.smoothScrollBy(paramInt1, paramInt2);
  }

  public void smoothScrollToPosition(int paramInt)
  {
    if (this.mLayoutFrozen)
      return;
    if (this.mLayout == null)
    {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    this.mLayout.smoothScrollToPosition(this, this.mState, paramInt);
  }

  public boolean startNestedScroll(int paramInt)
  {
    return this.mScrollingChildHelper.startNestedScroll(paramInt);
  }

  public void stopNestedScroll()
  {
    this.mScrollingChildHelper.stopNestedScroll();
  }

  public void stopScroll()
  {
    setScrollState(0);
    stopScrollersInternal();
  }

  public void swapAdapter(Adapter paramAdapter, boolean paramBoolean)
  {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, true, paramBoolean);
    setDataSetChangedAfterLayout();
    requestLayout();
  }

  void viewRangeUpdate(int paramInt1, int paramInt2, Object paramObject)
  {
    int j = this.mChildHelper.getUnfilteredChildCount();
    int i = 0;
    if (i < j)
    {
      View localView = this.mChildHelper.getUnfilteredChildAt(i);
      ViewHolder localViewHolder = getChildViewHolderInt(localView);
      if ((localViewHolder == null) || (localViewHolder.shouldIgnore()));
      while (true)
      {
        i += 1;
        break;
        if ((localViewHolder.mPosition < paramInt1) || (localViewHolder.mPosition >= paramInt1 + paramInt2))
          continue;
        localViewHolder.addFlags(2);
        localViewHolder.addChangePayload(paramObject);
        if (supportsChangeAnimations())
          localViewHolder.addFlags(64);
        ((LayoutParams)localView.getLayoutParams()).mInsetsDirty = true;
      }
    }
    this.mRecycler.viewRangeUpdate(paramInt1, paramInt2);
  }

  public static abstract class Adapter<VH extends RecyclerView.ViewHolder>
  {
    private boolean mHasStableIds = false;
    private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();

    public final void bindViewHolder(VH paramVH, int paramInt)
    {
      paramVH.mPosition = paramInt;
      if (hasStableIds())
        paramVH.mItemId = getItemId(paramInt);
      paramVH.setFlags(1, 519);
      TraceCompat.beginSection("RV OnBindView");
      onBindViewHolder(paramVH, paramInt, paramVH.getUnmodifiedPayloads());
      paramVH.clearPayload();
      TraceCompat.endSection();
    }

    public final VH createViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      TraceCompat.beginSection("RV CreateView");
      paramViewGroup = onCreateViewHolder(paramViewGroup, paramInt);
      paramViewGroup.mItemViewType = paramInt;
      TraceCompat.endSection();
      return paramViewGroup;
    }

    public abstract int getItemCount();

    public long getItemId(int paramInt)
    {
      return -1L;
    }

    public int getItemViewType(int paramInt)
    {
      return 0;
    }

    public final boolean hasObservers()
    {
      return this.mObservable.hasObservers();
    }

    public final boolean hasStableIds()
    {
      return this.mHasStableIds;
    }

    public final void notifyDataSetChanged()
    {
      this.mObservable.notifyChanged();
    }

    public final void notifyItemChanged(int paramInt)
    {
      this.mObservable.notifyItemRangeChanged(paramInt, 1);
    }

    public final void notifyItemChanged(int paramInt, Object paramObject)
    {
      this.mObservable.notifyItemRangeChanged(paramInt, 1, paramObject);
    }

    public final void notifyItemInserted(int paramInt)
    {
      this.mObservable.notifyItemRangeInserted(paramInt, 1);
    }

    public final void notifyItemMoved(int paramInt1, int paramInt2)
    {
      this.mObservable.notifyItemMoved(paramInt1, paramInt2);
    }

    public final void notifyItemRangeChanged(int paramInt1, int paramInt2)
    {
      this.mObservable.notifyItemRangeChanged(paramInt1, paramInt2);
    }

    public final void notifyItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      this.mObservable.notifyItemRangeChanged(paramInt1, paramInt2, paramObject);
    }

    public final void notifyItemRangeInserted(int paramInt1, int paramInt2)
    {
      this.mObservable.notifyItemRangeInserted(paramInt1, paramInt2);
    }

    public final void notifyItemRangeRemoved(int paramInt1, int paramInt2)
    {
      this.mObservable.notifyItemRangeRemoved(paramInt1, paramInt2);
    }

    public final void notifyItemRemoved(int paramInt)
    {
      this.mObservable.notifyItemRangeRemoved(paramInt, 1);
    }

    public void onAttachedToRecyclerView(RecyclerView paramRecyclerView)
    {
    }

    public abstract void onBindViewHolder(VH paramVH, int paramInt);

    public void onBindViewHolder(VH paramVH, int paramInt, List<Object> paramList)
    {
      onBindViewHolder(paramVH, paramInt);
    }

    public abstract VH onCreateViewHolder(ViewGroup paramViewGroup, int paramInt);

    public void onDetachedFromRecyclerView(RecyclerView paramRecyclerView)
    {
    }

    public boolean onFailedToRecycleView(VH paramVH)
    {
      return false;
    }

    public void onViewAttachedToWindow(VH paramVH)
    {
    }

    public void onViewDetachedFromWindow(VH paramVH)
    {
    }

    public void onViewRecycled(VH paramVH)
    {
    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver paramAdapterDataObserver)
    {
      this.mObservable.registerObserver(paramAdapterDataObserver);
    }

    public void setHasStableIds(boolean paramBoolean)
    {
      if (hasObservers())
        throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
      this.mHasStableIds = paramBoolean;
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver paramAdapterDataObserver)
    {
      this.mObservable.unregisterObserver(paramAdapterDataObserver);
    }
  }

  static class AdapterDataObservable extends Observable<RecyclerView.AdapterDataObserver>
  {
    public boolean hasObservers()
    {
      return !this.mObservers.isEmpty();
    }

    public void notifyChanged()
    {
      int i = this.mObservers.size() - 1;
      while (i >= 0)
      {
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onChanged();
        i -= 1;
      }
    }

    public void notifyItemMoved(int paramInt1, int paramInt2)
    {
      int i = this.mObservers.size() - 1;
      while (i >= 0)
      {
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(paramInt1, paramInt2, 1);
        i -= 1;
      }
    }

    public void notifyItemRangeChanged(int paramInt1, int paramInt2)
    {
      notifyItemRangeChanged(paramInt1, paramInt2, null);
    }

    public void notifyItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      int i = this.mObservers.size() - 1;
      while (i >= 0)
      {
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(paramInt1, paramInt2, paramObject);
        i -= 1;
      }
    }

    public void notifyItemRangeInserted(int paramInt1, int paramInt2)
    {
      int i = this.mObservers.size() - 1;
      while (i >= 0)
      {
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(paramInt1, paramInt2);
        i -= 1;
      }
    }

    public void notifyItemRangeRemoved(int paramInt1, int paramInt2)
    {
      int i = this.mObservers.size() - 1;
      while (i >= 0)
      {
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(paramInt1, paramInt2);
        i -= 1;
      }
    }
  }

  public static abstract class AdapterDataObserver
  {
    public void onChanged()
    {
    }

    public void onItemRangeChanged(int paramInt1, int paramInt2)
    {
    }

    public void onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      onItemRangeChanged(paramInt1, paramInt2);
    }

    public void onItemRangeInserted(int paramInt1, int paramInt2)
    {
    }

    public void onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onItemRangeRemoved(int paramInt1, int paramInt2)
    {
    }
  }

  public static abstract interface ChildDrawingOrderCallback
  {
    public abstract int onGetChildDrawingOrder(int paramInt1, int paramInt2);
  }

  public static abstract class ItemAnimator
  {
    private long mAddDuration = 120L;
    private long mChangeDuration = 250L;
    private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList();
    private ItemAnimatorListener mListener = null;
    private long mMoveDuration = 250L;
    private long mRemoveDuration = 120L;
    private boolean mSupportsChangeAnimations = true;

    public abstract boolean animateAdd(RecyclerView.ViewHolder paramViewHolder);

    public abstract boolean animateChange(RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract boolean animateMove(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract boolean animateRemove(RecyclerView.ViewHolder paramViewHolder);

    public final void dispatchAddFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      onAddFinished(paramViewHolder);
      if (this.mListener != null)
        this.mListener.onAddFinished(paramViewHolder);
    }

    public final void dispatchAddStarting(RecyclerView.ViewHolder paramViewHolder)
    {
      onAddStarting(paramViewHolder);
    }

    public final void dispatchAnimationsFinished()
    {
      int j = this.mFinishedListeners.size();
      int i = 0;
      while (i < j)
      {
        ((ItemAnimatorFinishedListener)this.mFinishedListeners.get(i)).onAnimationsFinished();
        i += 1;
      }
      this.mFinishedListeners.clear();
    }

    public final void dispatchChangeFinished(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
    {
      onChangeFinished(paramViewHolder, paramBoolean);
      if (this.mListener != null)
        this.mListener.onChangeFinished(paramViewHolder);
    }

    public final void dispatchChangeStarting(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
    {
      onChangeStarting(paramViewHolder, paramBoolean);
    }

    public final void dispatchMoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      onMoveFinished(paramViewHolder);
      if (this.mListener != null)
        this.mListener.onMoveFinished(paramViewHolder);
    }

    public final void dispatchMoveStarting(RecyclerView.ViewHolder paramViewHolder)
    {
      onMoveStarting(paramViewHolder);
    }

    public final void dispatchRemoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      onRemoveFinished(paramViewHolder);
      if (this.mListener != null)
        this.mListener.onRemoveFinished(paramViewHolder);
    }

    public final void dispatchRemoveStarting(RecyclerView.ViewHolder paramViewHolder)
    {
      onRemoveStarting(paramViewHolder);
    }

    public abstract void endAnimation(RecyclerView.ViewHolder paramViewHolder);

    public abstract void endAnimations();

    public long getAddDuration()
    {
      return this.mAddDuration;
    }

    public long getChangeDuration()
    {
      return this.mChangeDuration;
    }

    public long getMoveDuration()
    {
      return this.mMoveDuration;
    }

    public long getRemoveDuration()
    {
      return this.mRemoveDuration;
    }

    public boolean getSupportsChangeAnimations()
    {
      return this.mSupportsChangeAnimations;
    }

    public abstract boolean isRunning();

    public final boolean isRunning(ItemAnimatorFinishedListener paramItemAnimatorFinishedListener)
    {
      boolean bool = isRunning();
      if (paramItemAnimatorFinishedListener != null)
      {
        if (!bool)
          paramItemAnimatorFinishedListener.onAnimationsFinished();
      }
      else
        return bool;
      this.mFinishedListeners.add(paramItemAnimatorFinishedListener);
      return bool;
    }

    public void onAddFinished(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public void onAddStarting(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public void onChangeFinished(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
    {
    }

    public void onChangeStarting(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
    {
    }

    public void onMoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public void onMoveStarting(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public void onRemoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public void onRemoveStarting(RecyclerView.ViewHolder paramViewHolder)
    {
    }

    public abstract void runPendingAnimations();

    public void setAddDuration(long paramLong)
    {
      this.mAddDuration = paramLong;
    }

    public void setChangeDuration(long paramLong)
    {
      this.mChangeDuration = paramLong;
    }

    void setListener(ItemAnimatorListener paramItemAnimatorListener)
    {
      this.mListener = paramItemAnimatorListener;
    }

    public void setMoveDuration(long paramLong)
    {
      this.mMoveDuration = paramLong;
    }

    public void setRemoveDuration(long paramLong)
    {
      this.mRemoveDuration = paramLong;
    }

    public void setSupportsChangeAnimations(boolean paramBoolean)
    {
      this.mSupportsChangeAnimations = paramBoolean;
    }

    public static abstract interface ItemAnimatorFinishedListener
    {
      public abstract void onAnimationsFinished();
    }

    static abstract interface ItemAnimatorListener
    {
      public abstract void onAddFinished(RecyclerView.ViewHolder paramViewHolder);

      public abstract void onChangeFinished(RecyclerView.ViewHolder paramViewHolder);

      public abstract void onMoveFinished(RecyclerView.ViewHolder paramViewHolder);

      public abstract void onRemoveFinished(RecyclerView.ViewHolder paramViewHolder);
    }
  }

  private class ItemAnimatorRestoreListener
    implements RecyclerView.ItemAnimator.ItemAnimatorListener
  {
    private ItemAnimatorRestoreListener()
    {
    }

    public void onAddFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      paramViewHolder.setIsRecyclable(true);
      if (!RecyclerView.ViewHolder.access$5400(paramViewHolder))
        RecyclerView.this.removeAnimatingView(paramViewHolder.itemView);
    }

    public void onChangeFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      paramViewHolder.setIsRecyclable(true);
      if ((paramViewHolder.mShadowedHolder != null) && (paramViewHolder.mShadowingHolder == null))
      {
        paramViewHolder.mShadowedHolder = null;
        paramViewHolder.setFlags(-65, RecyclerView.ViewHolder.access$5500(paramViewHolder));
      }
      paramViewHolder.mShadowingHolder = null;
      if (!RecyclerView.ViewHolder.access$5400(paramViewHolder))
        RecyclerView.this.removeAnimatingView(paramViewHolder.itemView);
    }

    public void onMoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      paramViewHolder.setIsRecyclable(true);
      if (!RecyclerView.ViewHolder.access$5400(paramViewHolder))
        RecyclerView.this.removeAnimatingView(paramViewHolder.itemView);
    }

    public void onRemoveFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      paramViewHolder.setIsRecyclable(true);
      if ((!RecyclerView.this.removeAnimatingView(paramViewHolder.itemView)) && (paramViewHolder.isTmpDetached()))
        RecyclerView.this.removeDetachedView(paramViewHolder.itemView, false);
    }
  }

  public static abstract class ItemDecoration
  {
    @Deprecated
    public void getItemOffsets(Rect paramRect, int paramInt, RecyclerView paramRecyclerView)
    {
      paramRect.set(0, 0, 0, 0);
    }

    public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      getItemOffsets(paramRect, ((RecyclerView.LayoutParams)paramView.getLayoutParams()).getViewLayoutPosition(), paramRecyclerView);
    }

    @Deprecated
    public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView)
    {
    }

    public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      onDraw(paramCanvas, paramRecyclerView);
    }

    @Deprecated
    public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView)
    {
    }

    public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      onDrawOver(paramCanvas, paramRecyclerView);
    }
  }

  private static class ItemHolderInfo
  {
    int bottom;
    RecyclerView.ViewHolder holder;
    int left;
    int right;
    int top;

    ItemHolderInfo(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.holder = paramViewHolder;
      this.left = paramInt1;
      this.top = paramInt2;
      this.right = paramInt3;
      this.bottom = paramInt4;
    }
  }

  public static abstract class LayoutManager
  {
    ChildHelper mChildHelper;
    private boolean mIsAttachedToWindow = false;
    RecyclerView mRecyclerView;
    private boolean mRequestedSimpleAnimations = false;

    @Nullable
    RecyclerView.SmoothScroller mSmoothScroller;

    private void addViewInt(View paramView, int paramInt, boolean paramBoolean)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      RecyclerView.LayoutParams localLayoutParams;
      if ((paramBoolean) || (localViewHolder.isRemoved()))
      {
        this.mRecyclerView.mState.addToDisappearingList(paramView);
        localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
        if ((!localViewHolder.wasReturnedFromScrap()) && (!localViewHolder.isScrap()))
          break label126;
        if (!localViewHolder.isScrap())
          break label118;
        localViewHolder.unScrap();
        label67: this.mChildHelper.attachViewToParent(paramView, paramInt, paramView.getLayoutParams(), false);
      }
      while (true)
      {
        if (localLayoutParams.mPendingInvalidate)
        {
          localViewHolder.itemView.invalidate();
          localLayoutParams.mPendingInvalidate = false;
        }
        return;
        this.mRecyclerView.mState.removeFromDisappearingList(paramView);
        break;
        label118: localViewHolder.clearReturnedFromScrapFlag();
        break label67;
        label126: if (paramView.getParent() == this.mRecyclerView)
        {
          int j = this.mChildHelper.indexOfChild(paramView);
          int i = paramInt;
          if (paramInt == -1)
            i = this.mChildHelper.getChildCount();
          if (j == -1)
            throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(paramView));
          if (j == i)
            continue;
          this.mRecyclerView.mLayout.moveView(j, i);
          continue;
        }
        this.mChildHelper.addView(paramView, paramInt, false);
        localLayoutParams.mInsetsDirty = true;
        if ((this.mSmoothScroller == null) || (!this.mSmoothScroller.isRunning()))
          continue;
        this.mSmoothScroller.onChildAttachedToWindow(paramView);
      }
    }

    private void detachViewInternal(int paramInt, View paramView)
    {
      this.mChildHelper.detachViewFromParent(paramInt);
    }

    public static int getChildMeasureSpec(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      int i = Math.max(0, paramInt1 - paramInt2);
      paramInt2 = 0;
      paramInt1 = 0;
      if (paramBoolean)
        if (paramInt3 >= 0)
        {
          paramInt2 = paramInt3;
          paramInt1 = 1073741824;
        }
      while (true)
      {
        return View.MeasureSpec.makeMeasureSpec(paramInt2, paramInt1);
        paramInt2 = 0;
        paramInt1 = 0;
        continue;
        if (paramInt3 >= 0)
        {
          paramInt2 = paramInt3;
          paramInt1 = 1073741824;
          continue;
        }
        if (paramInt3 == -1)
        {
          paramInt2 = i;
          paramInt1 = 1073741824;
          continue;
        }
        if (paramInt3 != -2)
          continue;
        paramInt2 = i;
        paramInt1 = -2147483648;
      }
    }

    public static Properties getProperties(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      Properties localProperties = new Properties();
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt1, paramInt2);
      localProperties.orientation = paramContext.getInt(R.styleable.RecyclerView_android_orientation, 1);
      localProperties.spanCount = paramContext.getInt(R.styleable.RecyclerView_spanCount, 1);
      localProperties.reverseLayout = paramContext.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
      localProperties.stackFromEnd = paramContext.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
      paramContext.recycle();
      return localProperties;
    }

    private void onSmoothScrollerStopped(RecyclerView.SmoothScroller paramSmoothScroller)
    {
      if (this.mSmoothScroller == paramSmoothScroller)
        this.mSmoothScroller = null;
    }

    private void scrapOrRecycleView(RecyclerView.Recycler paramRecycler, int paramInt, View paramView)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.shouldIgnore())
        return;
      if ((localViewHolder.isInvalid()) && (!localViewHolder.isRemoved()) && (!localViewHolder.isChanged()) && (!this.mRecyclerView.mAdapter.hasStableIds()))
      {
        removeViewAt(paramInt);
        paramRecycler.recycleViewHolderInternal(localViewHolder);
        return;
      }
      detachViewAt(paramInt);
      paramRecycler.scrapView(paramView);
    }

    public void addDisappearingView(View paramView)
    {
      addDisappearingView(paramView, -1);
    }

    public void addDisappearingView(View paramView, int paramInt)
    {
      addViewInt(paramView, paramInt, true);
    }

    public void addView(View paramView)
    {
      addView(paramView, -1);
    }

    public void addView(View paramView, int paramInt)
    {
      addViewInt(paramView, paramInt, false);
    }

    public void assertInLayoutOrScroll(String paramString)
    {
      if (this.mRecyclerView != null)
        this.mRecyclerView.assertInLayoutOrScroll(paramString);
    }

    public void assertNotInLayoutOrScroll(String paramString)
    {
      if (this.mRecyclerView != null)
        this.mRecyclerView.assertNotInLayoutOrScroll(paramString);
    }

    public void attachView(View paramView)
    {
      attachView(paramView, -1);
    }

    public void attachView(View paramView, int paramInt)
    {
      attachView(paramView, paramInt, (RecyclerView.LayoutParams)paramView.getLayoutParams());
    }

    public void attachView(View paramView, int paramInt, RecyclerView.LayoutParams paramLayoutParams)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.isRemoved())
        this.mRecyclerView.mState.addToDisappearingList(paramView);
      while (true)
      {
        this.mChildHelper.attachViewToParent(paramView, paramInt, paramLayoutParams, localViewHolder.isRemoved());
        return;
        this.mRecyclerView.mState.removeFromDisappearingList(paramView);
      }
    }

    public void calculateItemDecorationsForChild(View paramView, Rect paramRect)
    {
      if (this.mRecyclerView == null)
      {
        paramRect.set(0, 0, 0, 0);
        return;
      }
      paramRect.set(this.mRecyclerView.getItemDecorInsetsForChild(paramView));
    }

    public boolean canScrollHorizontally()
    {
      return false;
    }

    public boolean canScrollVertically()
    {
      return false;
    }

    public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams)
    {
      return paramLayoutParams != null;
    }

    public int computeHorizontalScrollExtent(RecyclerView.State paramState)
    {
      return 0;
    }

    public int computeHorizontalScrollOffset(RecyclerView.State paramState)
    {
      return 0;
    }

    public int computeHorizontalScrollRange(RecyclerView.State paramState)
    {
      return 0;
    }

    public int computeVerticalScrollExtent(RecyclerView.State paramState)
    {
      return 0;
    }

    public int computeVerticalScrollOffset(RecyclerView.State paramState)
    {
      return 0;
    }

    public int computeVerticalScrollRange(RecyclerView.State paramState)
    {
      return 0;
    }

    public void detachAndScrapAttachedViews(RecyclerView.Recycler paramRecycler)
    {
      int i = getChildCount() - 1;
      while (i >= 0)
      {
        scrapOrRecycleView(paramRecycler, i, getChildAt(i));
        i -= 1;
      }
    }

    public void detachAndScrapView(View paramView, RecyclerView.Recycler paramRecycler)
    {
      scrapOrRecycleView(paramRecycler, this.mChildHelper.indexOfChild(paramView), paramView);
    }

    public void detachAndScrapViewAt(int paramInt, RecyclerView.Recycler paramRecycler)
    {
      scrapOrRecycleView(paramRecycler, paramInt, getChildAt(paramInt));
    }

    public void detachView(View paramView)
    {
      int i = this.mChildHelper.indexOfChild(paramView);
      if (i >= 0)
        detachViewInternal(i, paramView);
    }

    public void detachViewAt(int paramInt)
    {
      detachViewInternal(paramInt, getChildAt(paramInt));
    }

    void dispatchAttachedToWindow(RecyclerView paramRecyclerView)
    {
      this.mIsAttachedToWindow = true;
      onAttachedToWindow(paramRecyclerView);
    }

    void dispatchDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler)
    {
      this.mIsAttachedToWindow = false;
      onDetachedFromWindow(paramRecyclerView, paramRecycler);
    }

    public void endAnimation(View paramView)
    {
      if (this.mRecyclerView.mItemAnimator != null)
        this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(paramView));
    }

    public View findViewByPosition(int paramInt)
    {
      int j = getChildCount();
      int i = 0;
      if (i < j)
      {
        View localView = getChildAt(i);
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(localView);
        if (localViewHolder == null);
        do
        {
          i += 1;
          break;
        }
        while ((localViewHolder.getLayoutPosition() != paramInt) || (localViewHolder.shouldIgnore()) || ((!this.mRecyclerView.mState.isPreLayout()) && (localViewHolder.isRemoved())));
        return localView;
      }
      return null;
    }

    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();

    public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      return new RecyclerView.LayoutParams(paramContext, paramAttributeSet);
    }

    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      if ((paramLayoutParams instanceof RecyclerView.LayoutParams))
        return new RecyclerView.LayoutParams((RecyclerView.LayoutParams)paramLayoutParams);
      if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams))
        return new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
      return new RecyclerView.LayoutParams(paramLayoutParams);
    }

    public int getBaseline()
    {
      return -1;
    }

    public int getBottomDecorationHeight(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets.bottom;
    }

    public View getChildAt(int paramInt)
    {
      if (this.mChildHelper != null)
        return this.mChildHelper.getChildAt(paramInt);
      return null;
    }

    public int getChildCount()
    {
      if (this.mChildHelper != null)
        return this.mChildHelper.getChildCount();
      return 0;
    }

    public boolean getClipToPadding()
    {
      return (this.mRecyclerView != null) && (this.mRecyclerView.mClipToPadding);
    }

    public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      if ((this.mRecyclerView == null) || (this.mRecyclerView.mAdapter == null));
      do
        return 1;
      while (!canScrollHorizontally());
      return this.mRecyclerView.mAdapter.getItemCount();
    }

    public int getDecoratedBottom(View paramView)
    {
      return paramView.getBottom() + getBottomDecorationHeight(paramView);
    }

    public int getDecoratedLeft(View paramView)
    {
      return paramView.getLeft() - getLeftDecorationWidth(paramView);
    }

    public int getDecoratedMeasuredHeight(View paramView)
    {
      Rect localRect = ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets;
      return paramView.getMeasuredHeight() + localRect.top + localRect.bottom;
    }

    public int getDecoratedMeasuredWidth(View paramView)
    {
      Rect localRect = ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets;
      return paramView.getMeasuredWidth() + localRect.left + localRect.right;
    }

    public int getDecoratedRight(View paramView)
    {
      return paramView.getRight() + getRightDecorationWidth(paramView);
    }

    public int getDecoratedTop(View paramView)
    {
      return paramView.getTop() - getTopDecorationHeight(paramView);
    }

    public View getFocusedChild()
    {
      Object localObject;
      if (this.mRecyclerView == null)
        localObject = null;
      View localView;
      do
      {
        return localObject;
        localView = this.mRecyclerView.getFocusedChild();
        if (localView == null)
          break;
        localObject = localView;
      }
      while (!this.mChildHelper.isHidden(localView));
      return null;
    }

    public int getHeight()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getHeight();
      return 0;
    }

    public int getItemCount()
    {
      if (this.mRecyclerView != null);
      for (RecyclerView.Adapter localAdapter = this.mRecyclerView.getAdapter(); localAdapter != null; localAdapter = null)
        return localAdapter.getItemCount();
      return 0;
    }

    public int getItemViewType(View paramView)
    {
      return RecyclerView.getChildViewHolderInt(paramView).getItemViewType();
    }

    public int getLayoutDirection()
    {
      return ViewCompat.getLayoutDirection(this.mRecyclerView);
    }

    public int getLeftDecorationWidth(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets.left;
    }

    public int getMinimumHeight()
    {
      return ViewCompat.getMinimumHeight(this.mRecyclerView);
    }

    public int getMinimumWidth()
    {
      return ViewCompat.getMinimumWidth(this.mRecyclerView);
    }

    public int getPaddingBottom()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getPaddingBottom();
      return 0;
    }

    public int getPaddingEnd()
    {
      if (this.mRecyclerView != null)
        return ViewCompat.getPaddingEnd(this.mRecyclerView);
      return 0;
    }

    public int getPaddingLeft()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getPaddingLeft();
      return 0;
    }

    public int getPaddingRight()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getPaddingRight();
      return 0;
    }

    public int getPaddingStart()
    {
      if (this.mRecyclerView != null)
        return ViewCompat.getPaddingStart(this.mRecyclerView);
      return 0;
    }

    public int getPaddingTop()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getPaddingTop();
      return 0;
    }

    public int getPosition(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).getViewLayoutPosition();
    }

    public int getRightDecorationWidth(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets.right;
    }

    public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      if ((this.mRecyclerView == null) || (this.mRecyclerView.mAdapter == null));
      do
        return 1;
      while (!canScrollVertically());
      return this.mRecyclerView.mAdapter.getItemCount();
    }

    public int getSelectionModeForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }

    public int getTopDecorationHeight(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets.top;
    }

    public int getWidth()
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.getWidth();
      return 0;
    }

    public boolean hasFocus()
    {
      return (this.mRecyclerView != null) && (this.mRecyclerView.hasFocus());
    }

    public void ignoreView(View paramView)
    {
      if ((paramView.getParent() != this.mRecyclerView) || (this.mRecyclerView.indexOfChild(paramView) == -1))
        throw new IllegalArgumentException("View should be fully attached to be ignored");
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      paramView.addFlags(128);
      this.mRecyclerView.mState.onViewIgnored(paramView);
    }

    public boolean isAttachedToWindow()
    {
      return this.mIsAttachedToWindow;
    }

    public boolean isFocused()
    {
      return (this.mRecyclerView != null) && (this.mRecyclerView.isFocused());
    }

    public boolean isLayoutHierarchical(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return false;
    }

    public boolean isSmoothScrolling()
    {
      return (this.mSmoothScroller != null) && (this.mSmoothScroller.isRunning());
    }

    public void layoutDecorated(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      Rect localRect = ((RecyclerView.LayoutParams)paramView.getLayoutParams()).mDecorInsets;
      paramView.layout(localRect.left + paramInt1, localRect.top + paramInt2, paramInt3 - localRect.right, paramInt4 - localRect.bottom);
    }

    public void measureChild(View paramView, int paramInt1, int paramInt2)
    {
      RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      Rect localRect = this.mRecyclerView.getItemDecorInsetsForChild(paramView);
      int i = localRect.left;
      int j = localRect.right;
      int k = localRect.top;
      int m = localRect.bottom;
      paramView.measure(getChildMeasureSpec(getWidth(), getPaddingLeft() + getPaddingRight() + (paramInt1 + (i + j)), localLayoutParams.width, canScrollHorizontally()), getChildMeasureSpec(getHeight(), getPaddingTop() + getPaddingBottom() + (paramInt2 + (k + m)), localLayoutParams.height, canScrollVertically()));
    }

    public void measureChildWithMargins(View paramView, int paramInt1, int paramInt2)
    {
      RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      Rect localRect = this.mRecyclerView.getItemDecorInsetsForChild(paramView);
      int i = localRect.left;
      int j = localRect.right;
      int k = localRect.top;
      int m = localRect.bottom;
      paramView.measure(getChildMeasureSpec(getWidth(), getPaddingLeft() + getPaddingRight() + localLayoutParams.leftMargin + localLayoutParams.rightMargin + (paramInt1 + (i + j)), localLayoutParams.width, canScrollHorizontally()), getChildMeasureSpec(getHeight(), getPaddingTop() + getPaddingBottom() + localLayoutParams.topMargin + localLayoutParams.bottomMargin + (paramInt2 + (k + m)), localLayoutParams.height, canScrollVertically()));
    }

    public void moveView(int paramInt1, int paramInt2)
    {
      View localView = getChildAt(paramInt1);
      if (localView == null)
        throw new IllegalArgumentException("Cannot move a child from non-existing index:" + paramInt1);
      detachViewAt(paramInt1);
      attachView(localView, paramInt2);
    }

    public void offsetChildrenHorizontal(int paramInt)
    {
      if (this.mRecyclerView != null)
        this.mRecyclerView.offsetChildrenHorizontal(paramInt);
    }

    public void offsetChildrenVertical(int paramInt)
    {
      if (this.mRecyclerView != null)
        this.mRecyclerView.offsetChildrenVertical(paramInt);
    }

    public void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2)
    {
    }

    public boolean onAddFocusables(RecyclerView paramRecyclerView, ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
    {
      return false;
    }

    @CallSuper
    public void onAttachedToWindow(RecyclerView paramRecyclerView)
    {
    }

    @Deprecated
    public void onDetachedFromWindow(RecyclerView paramRecyclerView)
    {
    }

    @CallSuper
    public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler)
    {
      onDetachedFromWindow(paramRecyclerView);
    }

    @Nullable
    public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return null;
    }

    public void onInitializeAccessibilityEvent(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AccessibilityEvent paramAccessibilityEvent)
    {
      boolean bool2 = true;
      paramRecycler = AccessibilityEventCompat.asRecord(paramAccessibilityEvent);
      if ((this.mRecyclerView == null) || (paramRecycler == null))
        return;
      boolean bool1 = bool2;
      if (!ViewCompat.canScrollVertically(this.mRecyclerView, 1))
      {
        bool1 = bool2;
        if (!ViewCompat.canScrollVertically(this.mRecyclerView, -1))
        {
          bool1 = bool2;
          if (!ViewCompat.canScrollHorizontally(this.mRecyclerView, -1))
            if (!ViewCompat.canScrollHorizontally(this.mRecyclerView, 1))
              break label111;
        }
      }
      label111: for (bool1 = bool2; ; bool1 = false)
      {
        paramRecycler.setScrollable(bool1);
        if (this.mRecyclerView.mAdapter == null)
          break;
        paramRecycler.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
        return;
      }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, paramAccessibilityEvent);
    }

    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, paramAccessibilityNodeInfoCompat);
    }

    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      if ((ViewCompat.canScrollVertically(this.mRecyclerView, -1)) || (ViewCompat.canScrollHorizontally(this.mRecyclerView, -1)))
      {
        paramAccessibilityNodeInfoCompat.addAction(8192);
        paramAccessibilityNodeInfoCompat.setScrollable(true);
      }
      if ((ViewCompat.canScrollVertically(this.mRecyclerView, 1)) || (ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)))
      {
        paramAccessibilityNodeInfoCompat.addAction(4096);
        paramAccessibilityNodeInfoCompat.setScrollable(true);
      }
      paramAccessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(paramRecycler, paramState), getColumnCountForAccessibility(paramRecycler, paramState), isLayoutHierarchical(paramRecycler, paramState), getSelectionModeForAccessibility(paramRecycler, paramState)));
    }

    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      int i;
      if (canScrollVertically())
      {
        i = getPosition(paramView);
        if (!canScrollHorizontally())
          break label51;
      }
      label51: for (int j = getPosition(paramView); ; j = 0)
      {
        paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, j, 1, false, false));
        return;
        i = 0;
        break;
      }
    }

    void onInitializeAccessibilityNodeInfoForItem(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if ((localViewHolder != null) && (!localViewHolder.isRemoved()) && (!this.mChildHelper.isHidden(localViewHolder.itemView)))
        onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, paramView, paramAccessibilityNodeInfoCompat);
    }

    public View onInterceptFocusSearch(View paramView, int paramInt)
    {
      return null;
    }

    public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
    {
    }

    public void onItemsChanged(RecyclerView paramRecyclerView)
    {
    }

    public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
    {
    }

    public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
    {
    }

    public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject)
    {
      onItemsUpdated(paramRecyclerView, paramInt1, paramInt2);
    }

    public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
    }

    public void onMeasure(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2)
    {
      this.mRecyclerView.defaultOnMeasure(paramInt1, paramInt2);
    }

    public boolean onRequestChildFocus(RecyclerView paramRecyclerView, RecyclerView.State paramState, View paramView1, View paramView2)
    {
      return onRequestChildFocus(paramRecyclerView, paramView1, paramView2);
    }

    @Deprecated
    public boolean onRequestChildFocus(RecyclerView paramRecyclerView, View paramView1, View paramView2)
    {
      return (isSmoothScrolling()) || (paramRecyclerView.isComputingLayout());
    }

    public void onRestoreInstanceState(Parcelable paramParcelable)
    {
    }

    public Parcelable onSaveInstanceState()
    {
      return null;
    }

    public void onScrollStateChanged(int paramInt)
    {
    }

    boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
    {
      return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, paramInt, paramBundle);
    }

    public boolean performAccessibilityAction(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt, Bundle paramBundle)
    {
      if (this.mRecyclerView == null)
        return false;
      int k = 0;
      int m = 0;
      int i = 0;
      int j = 0;
      switch (paramInt)
      {
      default:
        paramInt = i;
      case 8192:
      case 4096:
      }
      while ((paramInt != 0) || (j != 0))
      {
        this.mRecyclerView.scrollBy(j, paramInt);
        return true;
        i = k;
        if (ViewCompat.canScrollVertically(this.mRecyclerView, -1))
          i = -(getHeight() - getPaddingTop() - getPaddingBottom());
        paramInt = i;
        if (!ViewCompat.canScrollHorizontally(this.mRecyclerView, -1))
          continue;
        j = -(getWidth() - getPaddingLeft() - getPaddingRight());
        paramInt = i;
        continue;
        i = m;
        if (ViewCompat.canScrollVertically(this.mRecyclerView, 1))
          i = getHeight() - getPaddingTop() - getPaddingBottom();
        paramInt = i;
        if (!ViewCompat.canScrollHorizontally(this.mRecyclerView, 1))
          continue;
        j = getWidth() - getPaddingLeft() - getPaddingRight();
        paramInt = i;
      }
    }

    public boolean performAccessibilityActionForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, int paramInt, Bundle paramBundle)
    {
      return false;
    }

    boolean performAccessibilityActionForItem(View paramView, int paramInt, Bundle paramBundle)
    {
      return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, paramView, paramInt, paramBundle);
    }

    public void postOnAnimation(Runnable paramRunnable)
    {
      if (this.mRecyclerView != null)
        ViewCompat.postOnAnimation(this.mRecyclerView, paramRunnable);
    }

    public void removeAllViews()
    {
      int i = getChildCount() - 1;
      while (i >= 0)
      {
        this.mChildHelper.removeViewAt(i);
        i -= 1;
      }
    }

    public void removeAndRecycleAllViews(RecyclerView.Recycler paramRecycler)
    {
      int i = getChildCount() - 1;
      while (i >= 0)
      {
        if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore())
          removeAndRecycleViewAt(i, paramRecycler);
        i -= 1;
      }
    }

    void removeAndRecycleScrapInt(RecyclerView.Recycler paramRecycler)
    {
      int j = paramRecycler.getScrapCount();
      int i = j - 1;
      if (i >= 0)
      {
        View localView = paramRecycler.getScrapViewAt(i);
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(localView);
        if (localViewHolder.shouldIgnore());
        while (true)
        {
          i -= 1;
          break;
          localViewHolder.setIsRecyclable(false);
          if (localViewHolder.isTmpDetached())
            this.mRecyclerView.removeDetachedView(localView, false);
          if (this.mRecyclerView.mItemAnimator != null)
            this.mRecyclerView.mItemAnimator.endAnimation(localViewHolder);
          localViewHolder.setIsRecyclable(true);
          paramRecycler.quickRecycleScrapView(localView);
        }
      }
      paramRecycler.clearScrap();
      if (j > 0)
        this.mRecyclerView.invalidate();
    }

    public void removeAndRecycleView(View paramView, RecyclerView.Recycler paramRecycler)
    {
      removeView(paramView);
      paramRecycler.recycleView(paramView);
    }

    public void removeAndRecycleViewAt(int paramInt, RecyclerView.Recycler paramRecycler)
    {
      View localView = getChildAt(paramInt);
      removeViewAt(paramInt);
      paramRecycler.recycleView(localView);
    }

    public boolean removeCallbacks(Runnable paramRunnable)
    {
      if (this.mRecyclerView != null)
        return this.mRecyclerView.removeCallbacks(paramRunnable);
      return false;
    }

    public void removeDetachedView(View paramView)
    {
      this.mRecyclerView.removeDetachedView(paramView, false);
    }

    public void removeView(View paramView)
    {
      this.mChildHelper.removeView(paramView);
    }

    public void removeViewAt(int paramInt)
    {
      if (getChildAt(paramInt) != null)
        this.mChildHelper.removeViewAt(paramInt);
    }

    public boolean requestChildRectangleOnScreen(RecyclerView paramRecyclerView, View paramView, Rect paramRect, boolean paramBoolean)
    {
      int i2 = getPaddingLeft();
      int m = getPaddingTop();
      int i3 = getWidth() - getPaddingRight();
      int i1 = getHeight();
      int i6 = getPaddingBottom();
      int i4 = paramView.getLeft() + paramRect.left;
      int n = paramView.getTop() + paramRect.top;
      int i5 = i4 + paramRect.width();
      int i7 = paramRect.height();
      int i = Math.min(0, i4 - i2);
      int j = Math.min(0, n - m);
      int k = Math.max(0, i5 - i3);
      i1 = Math.max(0, n + i7 - (i1 - i6));
      if (getLayoutDirection() == 1)
        if (k != 0)
        {
          i = k;
          if (j == 0)
            break label207;
          label144: if ((i == 0) && (j == 0))
            break label233;
          if (!paramBoolean)
            break label222;
          paramRecyclerView.scrollBy(i, j);
        }
      while (true)
      {
        return true;
        i = Math.max(i, i5 - i3);
        break;
        if (i != 0)
          break;
        while (true)
          i = Math.min(i4 - i2, k);
        label207: j = Math.min(n - m, i1);
        break label144;
        label222: paramRecyclerView.smoothScrollBy(i, j);
      }
      label233: return false;
    }

    public void requestLayout()
    {
      if (this.mRecyclerView != null)
        this.mRecyclerView.requestLayout();
    }

    public void requestSimpleAnimationsInNextLayout()
    {
      this.mRequestedSimpleAnimations = true;
    }

    public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }

    public void scrollToPosition(int paramInt)
    {
    }

    public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }

    public void setMeasuredDimension(int paramInt1, int paramInt2)
    {
      this.mRecyclerView.setMeasuredDimension(paramInt1, paramInt2);
    }

    void setRecyclerView(RecyclerView paramRecyclerView)
    {
      if (paramRecyclerView == null)
      {
        this.mRecyclerView = null;
        this.mChildHelper = null;
        return;
      }
      this.mRecyclerView = paramRecyclerView;
      this.mChildHelper = paramRecyclerView.mChildHelper;
    }

    public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt)
    {
      Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
    }

    public void startSmoothScroll(RecyclerView.SmoothScroller paramSmoothScroller)
    {
      if ((this.mSmoothScroller != null) && (paramSmoothScroller != this.mSmoothScroller) && (this.mSmoothScroller.isRunning()))
        this.mSmoothScroller.stop();
      this.mSmoothScroller = paramSmoothScroller;
      this.mSmoothScroller.start(this.mRecyclerView, this);
    }

    public void stopIgnoringView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      paramView.stopIgnoring();
      paramView.resetInternal();
      paramView.addFlags(4);
    }

    void stopSmoothScroller()
    {
      if (this.mSmoothScroller != null)
        this.mSmoothScroller.stop();
    }

    public boolean supportsPredictiveItemAnimations()
    {
      return false;
    }

    public static class Properties
    {
      public int orientation;
      public boolean reverseLayout;
      public int spanCount;
      public boolean stackFromEnd;
    }
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    final Rect mDecorInsets = new Rect();
    boolean mInsetsDirty = true;
    boolean mPendingInvalidate = false;
    RecyclerView.ViewHolder mViewHolder;

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }

    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
    }

    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }

    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }

    public int getViewAdapterPosition()
    {
      return this.mViewHolder.getAdapterPosition();
    }

    public int getViewLayoutPosition()
    {
      return this.mViewHolder.getLayoutPosition();
    }

    public int getViewPosition()
    {
      return this.mViewHolder.getPosition();
    }

    public boolean isItemChanged()
    {
      return this.mViewHolder.isChanged();
    }

    public boolean isItemRemoved()
    {
      return this.mViewHolder.isRemoved();
    }

    public boolean isViewInvalid()
    {
      return this.mViewHolder.isInvalid();
    }

    public boolean viewNeedsUpdate()
    {
      return this.mViewHolder.needsUpdate();
    }
  }

  public static abstract interface OnChildAttachStateChangeListener
  {
    public abstract void onChildViewAttachedToWindow(View paramView);

    public abstract void onChildViewDetachedFromWindow(View paramView);
  }

  public static abstract interface OnItemTouchListener
  {
    public abstract boolean onInterceptTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent);

    public abstract void onRequestDisallowInterceptTouchEvent(boolean paramBoolean);

    public abstract void onTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent);
  }

  public static abstract class OnScrollListener
  {
    public void onScrollStateChanged(RecyclerView paramRecyclerView, int paramInt)
    {
    }

    public void onScrolled(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
    {
    }
  }

  public static class RecycledViewPool
  {
    private static final int DEFAULT_MAX_SCRAP = 5;
    private int mAttachCount = 0;
    private SparseIntArray mMaxScrap = new SparseIntArray();
    private SparseArray<ArrayList<RecyclerView.ViewHolder>> mScrap = new SparseArray();

    private ArrayList<RecyclerView.ViewHolder> getScrapHeapForType(int paramInt)
    {
      ArrayList localArrayList2 = (ArrayList)this.mScrap.get(paramInt);
      ArrayList localArrayList1 = localArrayList2;
      if (localArrayList2 == null)
      {
        localArrayList2 = new ArrayList();
        this.mScrap.put(paramInt, localArrayList2);
        localArrayList1 = localArrayList2;
        if (this.mMaxScrap.indexOfKey(paramInt) < 0)
        {
          this.mMaxScrap.put(paramInt, 5);
          localArrayList1 = localArrayList2;
        }
      }
      return localArrayList1;
    }

    void attach(RecyclerView.Adapter paramAdapter)
    {
      this.mAttachCount += 1;
    }

    public void clear()
    {
      this.mScrap.clear();
    }

    void detach()
    {
      this.mAttachCount -= 1;
    }

    public RecyclerView.ViewHolder getRecycledView(int paramInt)
    {
      ArrayList localArrayList = (ArrayList)this.mScrap.get(paramInt);
      if ((localArrayList != null) && (!localArrayList.isEmpty()))
      {
        paramInt = localArrayList.size() - 1;
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)localArrayList.get(paramInt);
        localArrayList.remove(paramInt);
        return localViewHolder;
      }
      return null;
    }

    void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2, boolean paramBoolean)
    {
      if (paramAdapter1 != null)
        detach();
      if ((!paramBoolean) && (this.mAttachCount == 0))
        clear();
      if (paramAdapter2 != null)
        attach(paramAdapter2);
    }

    public void putRecycledView(RecyclerView.ViewHolder paramViewHolder)
    {
      int i = paramViewHolder.getItemViewType();
      ArrayList localArrayList = getScrapHeapForType(i);
      if (this.mMaxScrap.get(i) <= localArrayList.size())
        return;
      paramViewHolder.resetInternal();
      localArrayList.add(paramViewHolder);
    }

    public void setMaxRecycledViews(int paramInt1, int paramInt2)
    {
      this.mMaxScrap.put(paramInt1, paramInt2);
      ArrayList localArrayList = (ArrayList)this.mScrap.get(paramInt1);
      if (localArrayList != null)
        while (localArrayList.size() > paramInt2)
          localArrayList.remove(localArrayList.size() - 1);
    }

    int size()
    {
      int j = 0;
      int i = 0;
      while (i < this.mScrap.size())
      {
        ArrayList localArrayList = (ArrayList)this.mScrap.valueAt(i);
        int k = j;
        if (localArrayList != null)
          k = j + localArrayList.size();
        i += 1;
        j = k;
      }
      return j;
    }
  }

  public final class Recycler
  {
    private static final int DEFAULT_CACHE_SIZE = 2;
    final ArrayList<RecyclerView.ViewHolder> mAttachedScrap = new ArrayList();
    final ArrayList<RecyclerView.ViewHolder> mCachedViews = new ArrayList();
    private ArrayList<RecyclerView.ViewHolder> mChangedScrap = null;
    private RecyclerView.RecycledViewPool mRecyclerPool;
    private final List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
    private RecyclerView.ViewCacheExtension mViewCacheExtension;
    private int mViewCacheMax = 2;

    public Recycler()
    {
    }

    private void attachAccessibilityDelegate(View paramView)
    {
      if (RecyclerView.this.isAccessibilityEnabled())
      {
        if (ViewCompat.getImportantForAccessibility(paramView) == 0)
          ViewCompat.setImportantForAccessibility(paramView, 1);
        if (!ViewCompat.hasAccessibilityDelegate(paramView))
          ViewCompat.setAccessibilityDelegate(paramView, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
      }
    }

    private void invalidateDisplayListInt(RecyclerView.ViewHolder paramViewHolder)
    {
      if ((paramViewHolder.itemView instanceof ViewGroup))
        invalidateDisplayListInt((ViewGroup)paramViewHolder.itemView, false);
    }

    private void invalidateDisplayListInt(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      int i = paramViewGroup.getChildCount() - 1;
      while (i >= 0)
      {
        View localView = paramViewGroup.getChildAt(i);
        if ((localView instanceof ViewGroup))
          invalidateDisplayListInt((ViewGroup)localView, true);
        i -= 1;
      }
      if (!paramBoolean)
        return;
      if (paramViewGroup.getVisibility() == 4)
      {
        paramViewGroup.setVisibility(0);
        paramViewGroup.setVisibility(4);
        return;
      }
      i = paramViewGroup.getVisibility();
      paramViewGroup.setVisibility(4);
      paramViewGroup.setVisibility(i);
    }

    void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder paramViewHolder)
    {
      ViewCompat.setAccessibilityDelegate(paramViewHolder.itemView, null);
      dispatchViewRecycled(paramViewHolder);
      paramViewHolder.mOwnerRecyclerView = null;
      getRecycledViewPool().putRecycledView(paramViewHolder);
    }

    public void bindViewToPosition(View paramView, int paramInt)
    {
      boolean bool = true;
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder == null)
        throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
      int i = RecyclerView.this.mAdapterHelper.findPositionOffset(paramInt);
      if ((i < 0) || (i >= RecyclerView.this.mAdapter.getItemCount()))
        throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + paramInt + "(offset:" + i + ")." + "state:" + RecyclerView.this.mState.getItemCount());
      localViewHolder.mOwnerRecyclerView = RecyclerView.this;
      RecyclerView.this.mAdapter.bindViewHolder(localViewHolder, i);
      attachAccessibilityDelegate(paramView);
      if (RecyclerView.this.mState.isPreLayout())
        localViewHolder.mPreLayoutPosition = paramInt;
      paramView = localViewHolder.itemView.getLayoutParams();
      if (paramView == null)
      {
        paramView = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
        localViewHolder.itemView.setLayoutParams(paramView);
        paramView.mInsetsDirty = true;
        paramView.mViewHolder = localViewHolder;
        if (localViewHolder.itemView.getParent() != null)
          break label259;
      }
      while (true)
      {
        paramView.mPendingInvalidate = bool;
        return;
        if (!RecyclerView.this.checkLayoutParams(paramView))
        {
          paramView = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams(paramView);
          localViewHolder.itemView.setLayoutParams(paramView);
          break;
        }
        paramView = (RecyclerView.LayoutParams)paramView;
        break;
        label259: bool = false;
      }
    }

    public void clear()
    {
      this.mAttachedScrap.clear();
      recycleAndClearCachedViews();
    }

    void clearOldPositions()
    {
      int j = this.mCachedViews.size();
      int i = 0;
      while (i < j)
      {
        ((RecyclerView.ViewHolder)this.mCachedViews.get(i)).clearOldPosition();
        i += 1;
      }
      j = this.mAttachedScrap.size();
      i = 0;
      while (i < j)
      {
        ((RecyclerView.ViewHolder)this.mAttachedScrap.get(i)).clearOldPosition();
        i += 1;
      }
      if (this.mChangedScrap != null)
      {
        j = this.mChangedScrap.size();
        i = 0;
        while (i < j)
        {
          ((RecyclerView.ViewHolder)this.mChangedScrap.get(i)).clearOldPosition();
          i += 1;
        }
      }
    }

    void clearScrap()
    {
      this.mAttachedScrap.clear();
    }

    public int convertPreLayoutPositionToPostLayout(int paramInt)
    {
      if ((paramInt < 0) || (paramInt >= RecyclerView.this.mState.getItemCount()))
        throw new IndexOutOfBoundsException("invalid position " + paramInt + ". State " + "item count is " + RecyclerView.this.mState.getItemCount());
      if (!RecyclerView.this.mState.isPreLayout())
        return paramInt;
      return RecyclerView.this.mAdapterHelper.findPositionOffset(paramInt);
    }

    void dispatchViewRecycled(RecyclerView.ViewHolder paramViewHolder)
    {
      if (RecyclerView.this.mRecyclerListener != null)
        RecyclerView.this.mRecyclerListener.onViewRecycled(paramViewHolder);
      if (RecyclerView.this.mAdapter != null)
        RecyclerView.this.mAdapter.onViewRecycled(paramViewHolder);
      if (RecyclerView.this.mState != null)
        RecyclerView.this.mState.onViewRecycled(paramViewHolder);
    }

    RecyclerView.ViewHolder getChangedScrapViewForPosition(int paramInt)
    {
      int j;
      if (this.mChangedScrap != null)
      {
        j = this.mChangedScrap.size();
        if (j != 0);
      }
      else
      {
        return null;
      }
      int i = 0;
      RecyclerView.ViewHolder localViewHolder;
      while (i < j)
      {
        localViewHolder = (RecyclerView.ViewHolder)this.mChangedScrap.get(i);
        if ((!localViewHolder.wasReturnedFromScrap()) && (localViewHolder.getLayoutPosition() == paramInt))
        {
          localViewHolder.addFlags(32);
          return localViewHolder;
        }
        i += 1;
      }
      if (RecyclerView.this.mAdapter.hasStableIds())
      {
        paramInt = RecyclerView.this.mAdapterHelper.findPositionOffset(paramInt);
        if ((paramInt > 0) && (paramInt < RecyclerView.this.mAdapter.getItemCount()))
        {
          long l = RecyclerView.this.mAdapter.getItemId(paramInt);
          paramInt = 0;
          while (paramInt < j)
          {
            localViewHolder = (RecyclerView.ViewHolder)this.mChangedScrap.get(paramInt);
            if ((!localViewHolder.wasReturnedFromScrap()) && (localViewHolder.getItemId() == l))
            {
              localViewHolder.addFlags(32);
              return localViewHolder;
            }
            paramInt += 1;
          }
        }
      }
      return null;
    }

    RecyclerView.RecycledViewPool getRecycledViewPool()
    {
      if (this.mRecyclerPool == null)
        this.mRecyclerPool = new RecyclerView.RecycledViewPool();
      return this.mRecyclerPool;
    }

    int getScrapCount()
    {
      return this.mAttachedScrap.size();
    }

    public List<RecyclerView.ViewHolder> getScrapList()
    {
      return this.mUnmodifiableAttachedScrap;
    }

    View getScrapViewAt(int paramInt)
    {
      return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(paramInt)).itemView;
    }

    RecyclerView.ViewHolder getScrapViewForId(long paramLong, int paramInt, boolean paramBoolean)
    {
      int i = this.mAttachedScrap.size() - 1;
      RecyclerView.ViewHolder localViewHolder2;
      RecyclerView.ViewHolder localViewHolder1;
      while (i >= 0)
      {
        localViewHolder2 = (RecyclerView.ViewHolder)this.mAttachedScrap.get(i);
        if ((localViewHolder2.getItemId() == paramLong) && (!localViewHolder2.wasReturnedFromScrap()))
        {
          if (paramInt == localViewHolder2.getItemViewType())
          {
            localViewHolder2.addFlags(32);
            localViewHolder1 = localViewHolder2;
            if (localViewHolder2.isRemoved())
            {
              localViewHolder1 = localViewHolder2;
              if (!RecyclerView.this.mState.isPreLayout())
              {
                localViewHolder2.setFlags(2, 14);
                localViewHolder1 = localViewHolder2;
              }
            }
            return localViewHolder1;
          }
          if (!paramBoolean)
          {
            this.mAttachedScrap.remove(i);
            RecyclerView.this.removeDetachedView(localViewHolder2.itemView, false);
            quickRecycleScrapView(localViewHolder2.itemView);
          }
        }
        i -= 1;
      }
      i = this.mCachedViews.size() - 1;
      while (true)
      {
        if (i < 0)
          break label245;
        localViewHolder2 = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if (localViewHolder2.getItemId() == paramLong)
        {
          if (paramInt == localViewHolder2.getItemViewType())
          {
            localViewHolder1 = localViewHolder2;
            if (paramBoolean)
              break;
            this.mCachedViews.remove(i);
            return localViewHolder2;
          }
          if (!paramBoolean)
            recycleCachedViewAt(i);
        }
        i -= 1;
      }
      label245: return null;
    }

    RecyclerView.ViewHolder getScrapViewForPosition(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int j = this.mAttachedScrap.size();
      int i = 0;
      Object localObject;
      if (i < j)
      {
        localObject = (RecyclerView.ViewHolder)this.mAttachedScrap.get(i);
        if ((((RecyclerView.ViewHolder)localObject).wasReturnedFromScrap()) || (((RecyclerView.ViewHolder)localObject).getLayoutPosition() != paramInt1) || (((RecyclerView.ViewHolder)localObject).isInvalid()) || ((!RecyclerView.State.access$1700(RecyclerView.this.mState)) && (((RecyclerView.ViewHolder)localObject).isRemoved())))
          break label271;
        if ((paramInt2 != -1) && (((RecyclerView.ViewHolder)localObject).getItemViewType() != paramInt2))
          Log.e("RecyclerView", "Scrap view for position " + paramInt1 + " isn't dirty but has" + " wrong view type! (found " + ((RecyclerView.ViewHolder)localObject).getItemViewType() + " but expected " + paramInt2 + ")");
      }
      else
      {
        if (!paramBoolean)
        {
          localObject = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(paramInt1, paramInt2);
          if (localObject != null)
            RecyclerView.this.mItemAnimator.endAnimation(RecyclerView.this.getChildViewHolder((View)localObject));
        }
        i = this.mCachedViews.size();
        paramInt2 = 0;
      }
      while (true)
      {
        if (paramInt2 >= i)
          break label287;
        localObject = (RecyclerView.ViewHolder)this.mCachedViews.get(paramInt2);
        if ((!((RecyclerView.ViewHolder)localObject).isInvalid()) && (((RecyclerView.ViewHolder)localObject).getLayoutPosition() == paramInt1))
        {
          if (!paramBoolean)
            this.mCachedViews.remove(paramInt2);
          return localObject;
          ((RecyclerView.ViewHolder)localObject).addFlags(32);
          return localObject;
          label271: i += 1;
          break;
        }
        paramInt2 += 1;
      }
      label287: return (RecyclerView.ViewHolder)null;
    }

    public View getViewForPosition(int paramInt)
    {
      return getViewForPosition(paramInt, false);
    }

    View getViewForPosition(int paramInt, boolean paramBoolean)
    {
      boolean bool = true;
      if ((paramInt < 0) || (paramInt >= RecyclerView.this.mState.getItemCount()))
        throw new IndexOutOfBoundsException("Invalid item position " + paramInt + "(" + paramInt + "). Item count:" + RecyclerView.this.mState.getItemCount());
      int j = 0;
      Object localObject2 = null;
      if (RecyclerView.this.mState.isPreLayout())
      {
        localObject2 = getChangedScrapViewForPosition(paramInt);
        if (localObject2 != null)
          j = 1;
      }
      else
      {
        i = j;
        localObject1 = localObject2;
        if (localObject2 == null)
        {
          localObject2 = getScrapViewForPosition(paramInt, -1, paramBoolean);
          i = j;
          localObject1 = localObject2;
          if (localObject2 != null)
          {
            if (validateViewHolderForOffsetPosition((RecyclerView.ViewHolder)localObject2))
              break label328;
            if (!paramBoolean)
            {
              ((RecyclerView.ViewHolder)localObject2).addFlags(4);
              if (!((RecyclerView.ViewHolder)localObject2).isScrap())
                break label312;
              RecyclerView.this.removeDetachedView(((RecyclerView.ViewHolder)localObject2).itemView, false);
              ((RecyclerView.ViewHolder)localObject2).unScrap();
              label190: recycleViewHolderInternal((RecyclerView.ViewHolder)localObject2);
            }
            localObject1 = null;
            i = j;
          }
        }
      }
      while (true)
      {
        k = i;
        localObject2 = localObject1;
        if (localObject1 != null)
          break label582;
        k = RecyclerView.this.mAdapterHelper.findPositionOffset(paramInt);
        if ((k >= 0) && (k < RecyclerView.this.mAdapter.getItemCount()))
          break label337;
        throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + paramInt + "(offset:" + k + ")." + "state:" + RecyclerView.this.mState.getItemCount());
        j = 0;
        break;
        label312: if (!((RecyclerView.ViewHolder)localObject2).wasReturnedFromScrap())
          break label190;
        ((RecyclerView.ViewHolder)localObject2).clearReturnedFromScrapFlag();
        break label190;
        label328: i = 1;
        localObject1 = localObject2;
      }
      label337: int m = RecyclerView.this.mAdapter.getItemViewType(k);
      j = i;
      localObject2 = localObject1;
      if (RecyclerView.this.mAdapter.hasStableIds())
      {
        localObject1 = getScrapViewForId(RecyclerView.this.mAdapter.getItemId(k), m, paramBoolean);
        j = i;
        localObject2 = localObject1;
        if (localObject1 != null)
        {
          ((RecyclerView.ViewHolder)localObject1).mPosition = k;
          j = 1;
          localObject2 = localObject1;
        }
      }
      Object localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = localObject2;
        if (this.mViewCacheExtension != null)
        {
          localObject3 = this.mViewCacheExtension.getViewForPositionAndType(this, paramInt, m);
          localObject1 = localObject2;
          if (localObject3 != null)
          {
            localObject2 = RecyclerView.this.getChildViewHolder((View)localObject3);
            if (localObject2 == null)
              throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
            localObject1 = localObject2;
            if (((RecyclerView.ViewHolder)localObject2).shouldIgnore())
              throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
          }
        }
      }
      Object localObject3 = localObject1;
      if (localObject1 == null)
      {
        localObject1 = getRecycledViewPool().getRecycledView(m);
        localObject3 = localObject1;
        if (localObject1 != null)
        {
          ((RecyclerView.ViewHolder)localObject1).resetInternal();
          localObject3 = localObject1;
          if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST)
          {
            invalidateDisplayListInt((RecyclerView.ViewHolder)localObject1);
            localObject3 = localObject1;
          }
        }
      }
      int k = j;
      localObject2 = localObject3;
      if (localObject3 == null)
      {
        localObject2 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, m);
        k = j;
      }
      label582: int i = 0;
      if ((RecyclerView.this.mState.isPreLayout()) && (((RecyclerView.ViewHolder)localObject2).isBound()))
      {
        ((RecyclerView.ViewHolder)localObject2).mPreLayoutPosition = paramInt;
        localObject1 = ((RecyclerView.ViewHolder)localObject2).itemView.getLayoutParams();
        if (localObject1 != null)
          break label777;
        localObject1 = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
        ((RecyclerView.ViewHolder)localObject2).itemView.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        label645: ((RecyclerView.LayoutParams)localObject1).mViewHolder = ((RecyclerView.ViewHolder)localObject2);
        if ((k == 0) || (i == 0))
          break label820;
      }
      label777: label820: for (paramBoolean = bool; ; paramBoolean = false)
      {
        ((RecyclerView.LayoutParams)localObject1).mPendingInvalidate = paramBoolean;
        return ((RecyclerView.ViewHolder)localObject2).itemView;
        if ((((RecyclerView.ViewHolder)localObject2).isBound()) && (!((RecyclerView.ViewHolder)localObject2).needsUpdate()) && (!((RecyclerView.ViewHolder)localObject2).isInvalid()))
          break;
        i = RecyclerView.this.mAdapterHelper.findPositionOffset(paramInt);
        ((RecyclerView.ViewHolder)localObject2).mOwnerRecyclerView = RecyclerView.this;
        RecyclerView.this.mAdapter.bindViewHolder((RecyclerView.ViewHolder)localObject2, i);
        attachAccessibilityDelegate(((RecyclerView.ViewHolder)localObject2).itemView);
        j = 1;
        i = j;
        if (!RecyclerView.this.mState.isPreLayout())
          break;
        ((RecyclerView.ViewHolder)localObject2).mPreLayoutPosition = paramInt;
        i = j;
        break;
        if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)localObject1))
        {
          localObject1 = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)localObject1);
          ((RecyclerView.ViewHolder)localObject2).itemView.setLayoutParams((ViewGroup.LayoutParams)localObject1);
          break label645;
        }
        localObject1 = (RecyclerView.LayoutParams)localObject1;
        break label645;
      }
    }

    void markItemDecorInsetsDirty()
    {
      int j = this.mCachedViews.size();
      int i = 0;
      while (i < j)
      {
        RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(i)).itemView.getLayoutParams();
        if (localLayoutParams != null)
          localLayoutParams.mInsetsDirty = true;
        i += 1;
      }
    }

    void markKnownViewsInvalid()
    {
      int j;
      int i;
      if ((RecyclerView.this.mAdapter != null) && (RecyclerView.this.mAdapter.hasStableIds()))
      {
        j = this.mCachedViews.size();
        i = 0;
      }
      while (i < j)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if (localViewHolder != null)
        {
          localViewHolder.addFlags(6);
          localViewHolder.addChangePayload(null);
        }
        i += 1;
        continue;
        recycleAndClearCachedViews();
      }
    }

    void offsetPositionRecordsForInsert(int paramInt1, int paramInt2)
    {
      int j = this.mCachedViews.size();
      int i = 0;
      while (i < j)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if ((localViewHolder != null) && (localViewHolder.getLayoutPosition() >= paramInt1))
          localViewHolder.offsetPosition(paramInt2, true);
        i += 1;
      }
    }

    void offsetPositionRecordsForMove(int paramInt1, int paramInt2)
    {
      int k;
      int i;
      int j;
      int m;
      label26: RecyclerView.ViewHolder localViewHolder;
      if (paramInt1 < paramInt2)
      {
        k = paramInt1;
        i = paramInt2;
        j = -1;
        int n = this.mCachedViews.size();
        m = 0;
        if (m >= n)
          return;
        localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(m);
        if ((localViewHolder != null) && (localViewHolder.mPosition >= k) && (localViewHolder.mPosition <= i))
          break label89;
      }
      while (true)
      {
        m += 1;
        break label26;
        k = paramInt2;
        i = paramInt1;
        j = 1;
        break;
        label89: if (localViewHolder.mPosition == paramInt1)
        {
          localViewHolder.offsetPosition(paramInt2 - paramInt1, false);
          continue;
        }
        localViewHolder.offsetPosition(j, false);
      }
    }

    void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      int i = this.mCachedViews.size() - 1;
      if (i >= 0)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if (localViewHolder != null)
        {
          if (localViewHolder.getLayoutPosition() < paramInt1 + paramInt2)
            break label63;
          localViewHolder.offsetPosition(-paramInt2, paramBoolean);
        }
        while (true)
        {
          i -= 1;
          break;
          label63: if (localViewHolder.getLayoutPosition() < paramInt1)
            continue;
          localViewHolder.addFlags(8);
          recycleCachedViewAt(i);
        }
      }
    }

    void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2, boolean paramBoolean)
    {
      clear();
      getRecycledViewPool().onAdapterChanged(paramAdapter1, paramAdapter2, paramBoolean);
    }

    void quickRecycleScrapView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      RecyclerView.ViewHolder.access$4202(paramView, null);
      paramView.clearReturnedFromScrapFlag();
      recycleViewHolderInternal(paramView);
    }

    void recycleAndClearCachedViews()
    {
      int i = this.mCachedViews.size() - 1;
      while (i >= 0)
      {
        recycleCachedViewAt(i);
        i -= 1;
      }
      this.mCachedViews.clear();
    }

    void recycleCachedViewAt(int paramInt)
    {
      addViewHolderToRecycledViewPool((RecyclerView.ViewHolder)this.mCachedViews.get(paramInt));
      this.mCachedViews.remove(paramInt);
    }

    public void recycleView(View paramView)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(paramView, false);
      if (localViewHolder.isScrap())
        localViewHolder.unScrap();
      while (true)
      {
        recycleViewHolderInternal(localViewHolder);
        return;
        if (!localViewHolder.wasReturnedFromScrap())
          continue;
        localViewHolder.clearReturnedFromScrapFlag();
      }
    }

    void recycleViewHolderInternal(RecyclerView.ViewHolder paramViewHolder)
    {
      boolean bool = true;
      if ((paramViewHolder.isScrap()) || (paramViewHolder.itemView.getParent() != null))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(paramViewHolder.isScrap()).append(" isAttached:");
        if (paramViewHolder.itemView.getParent() != null);
        while (true)
        {
          throw new IllegalArgumentException(bool);
          bool = false;
        }
      }
      if (paramViewHolder.isTmpDetached())
        throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + paramViewHolder);
      if (paramViewHolder.shouldIgnore())
        throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
      bool = RecyclerView.ViewHolder.access$4100(paramViewHolder);
      if ((RecyclerView.this.mAdapter != null) && (bool) && (RecyclerView.this.mAdapter.onFailedToRecycleView(paramViewHolder)));
      for (int i = 1; ; i = 0)
      {
        int j = 0;
        int n = 0;
        int m = 0;
        int k;
        if (i == 0)
        {
          k = m;
          if (!paramViewHolder.isRecyclable());
        }
        else
        {
          i = n;
          if (!paramViewHolder.hasAnyOfTheFlags(78))
          {
            j = this.mCachedViews.size();
            if ((j == this.mViewCacheMax) && (j > 0))
              recycleCachedViewAt(0);
            i = n;
            if (j < this.mViewCacheMax)
            {
              this.mCachedViews.add(paramViewHolder);
              i = 1;
            }
          }
          j = i;
          k = m;
          if (i == 0)
          {
            addViewHolderToRecycledViewPool(paramViewHolder);
            k = 1;
            j = i;
          }
        }
        RecyclerView.this.mState.onViewRecycled(paramViewHolder);
        if ((j == 0) && (k == 0) && (bool))
          paramViewHolder.mOwnerRecyclerView = null;
        return;
      }
    }

    void recycleViewInternal(View paramView)
    {
      recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(paramView));
    }

    void scrapView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      paramView.setScrapContainer(this);
      if ((!paramView.isChanged()) || (!RecyclerView.this.supportsChangeAnimations()))
      {
        if ((paramView.isInvalid()) && (!paramView.isRemoved()) && (!RecyclerView.this.mAdapter.hasStableIds()))
          throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
        this.mAttachedScrap.add(paramView);
        return;
      }
      if (this.mChangedScrap == null)
        this.mChangedScrap = new ArrayList();
      this.mChangedScrap.add(paramView);
    }

    void setAdapterPositionsAsUnknown()
    {
      int j = this.mCachedViews.size();
      int i = 0;
      while (i < j)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if (localViewHolder != null)
          localViewHolder.addFlags(512);
        i += 1;
      }
    }

    void setRecycledViewPool(RecyclerView.RecycledViewPool paramRecycledViewPool)
    {
      if (this.mRecyclerPool != null)
        this.mRecyclerPool.detach();
      this.mRecyclerPool = paramRecycledViewPool;
      if (paramRecycledViewPool != null)
        this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
    }

    void setViewCacheExtension(RecyclerView.ViewCacheExtension paramViewCacheExtension)
    {
      this.mViewCacheExtension = paramViewCacheExtension;
    }

    public void setViewCacheSize(int paramInt)
    {
      this.mViewCacheMax = paramInt;
      int i = this.mCachedViews.size() - 1;
      while ((i >= 0) && (this.mCachedViews.size() > paramInt))
      {
        recycleCachedViewAt(i);
        i -= 1;
      }
    }

    void unscrapView(RecyclerView.ViewHolder paramViewHolder)
    {
      if ((!paramViewHolder.isChanged()) || (!RecyclerView.this.supportsChangeAnimations()) || (this.mChangedScrap == null))
        this.mAttachedScrap.remove(paramViewHolder);
      while (true)
      {
        RecyclerView.ViewHolder.access$4202(paramViewHolder, null);
        paramViewHolder.clearReturnedFromScrapFlag();
        return;
        this.mChangedScrap.remove(paramViewHolder);
      }
    }

    boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder paramViewHolder)
    {
      if (paramViewHolder.isRemoved());
      do
      {
        return true;
        if ((paramViewHolder.mPosition < 0) || (paramViewHolder.mPosition >= RecyclerView.this.mAdapter.getItemCount()))
          throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + paramViewHolder);
        if ((!RecyclerView.this.mState.isPreLayout()) && (RecyclerView.this.mAdapter.getItemViewType(paramViewHolder.mPosition) != paramViewHolder.getItemViewType()))
          return false;
      }
      while ((!RecyclerView.this.mAdapter.hasStableIds()) || (paramViewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(paramViewHolder.mPosition)));
      return false;
    }

    void viewRangeUpdate(int paramInt1, int paramInt2)
    {
      int i = this.mCachedViews.size() - 1;
      if (i >= 0)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)this.mCachedViews.get(i);
        if (localViewHolder == null);
        while (true)
        {
          i -= 1;
          break;
          int j = localViewHolder.getLayoutPosition();
          if ((j < paramInt1) || (j >= paramInt1 + paramInt2))
            continue;
          localViewHolder.addFlags(2);
          recycleCachedViewAt(i);
        }
      }
    }
  }

  public static abstract interface RecyclerListener
  {
    public abstract void onViewRecycled(RecyclerView.ViewHolder paramViewHolder);
  }

  private class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver
  {
    private RecyclerViewDataObserver()
    {
    }

    public void onChanged()
    {
      RecyclerView.this.assertNotInLayoutOrScroll(null);
      if (RecyclerView.this.mAdapter.hasStableIds())
      {
        RecyclerView.State.access$1402(RecyclerView.this.mState, true);
        RecyclerView.this.setDataSetChangedAfterLayout();
      }
      while (true)
      {
        if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates())
          RecyclerView.this.requestLayout();
        return;
        RecyclerView.State.access$1402(RecyclerView.this.mState, true);
        RecyclerView.this.setDataSetChangedAfterLayout();
      }
    }

    public void onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      RecyclerView.this.assertNotInLayoutOrScroll(null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(paramInt1, paramInt2, paramObject))
        triggerUpdateProcessor();
    }

    public void onItemRangeInserted(int paramInt1, int paramInt2)
    {
      RecyclerView.this.assertNotInLayoutOrScroll(null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(paramInt1, paramInt2))
        triggerUpdateProcessor();
    }

    public void onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
    {
      RecyclerView.this.assertNotInLayoutOrScroll(null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(paramInt1, paramInt2, paramInt3))
        triggerUpdateProcessor();
    }

    public void onItemRangeRemoved(int paramInt1, int paramInt2)
    {
      RecyclerView.this.assertNotInLayoutOrScroll(null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(paramInt1, paramInt2))
        triggerUpdateProcessor();
    }

    void triggerUpdateProcessor()
    {
      if ((RecyclerView.this.mPostUpdatesOnAnimation) && (RecyclerView.this.mHasFixedSize) && (RecyclerView.this.mIsAttached))
      {
        ViewCompat.postOnAnimation(RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
        return;
      }
      RecyclerView.access$3802(RecyclerView.this, true);
      RecyclerView.this.requestLayout();
    }
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public RecyclerView.SavedState createFromParcel(Parcel paramParcel)
      {
        return new RecyclerView.SavedState(paramParcel);
      }

      public RecyclerView.SavedState[] newArray(int paramInt)
      {
        return new RecyclerView.SavedState[paramInt];
      }
    };
    Parcelable mLayoutState;

    SavedState(Parcel paramParcel)
    {
      super();
      this.mLayoutState = paramParcel.readParcelable(RecyclerView.LayoutManager.class.getClassLoader());
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    private void copyFrom(SavedState paramSavedState)
    {
      this.mLayoutState = paramSavedState.mLayoutState;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(this.mLayoutState, 0);
    }
  }

  public static class SimpleOnItemTouchListener
    implements RecyclerView.OnItemTouchListener
  {
    public boolean onInterceptTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent)
    {
      return false;
    }

    public void onRequestDisallowInterceptTouchEvent(boolean paramBoolean)
    {
    }

    public void onTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent)
    {
    }
  }

  public static abstract class SmoothScroller
  {
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mPendingInitialRun;
    private RecyclerView mRecyclerView;
    private final Action mRecyclingAction = new Action(0, 0);
    private boolean mRunning;
    private int mTargetPosition = -1;
    private View mTargetView;

    private void onAnimation(int paramInt1, int paramInt2)
    {
      RecyclerView localRecyclerView = this.mRecyclerView;
      if ((!this.mRunning) || (this.mTargetPosition == -1) || (localRecyclerView == null))
        stop();
      this.mPendingInitialRun = false;
      if (this.mTargetView != null)
      {
        if (getChildPosition(this.mTargetView) != this.mTargetPosition)
          break label146;
        onTargetFound(this.mTargetView, localRecyclerView.mState, this.mRecyclingAction);
        this.mRecyclingAction.runIfNecessary(localRecyclerView);
        stop();
      }
      while (true)
      {
        if (this.mRunning)
        {
          onSeekTargetStep(paramInt1, paramInt2, localRecyclerView.mState, this.mRecyclingAction);
          boolean bool = this.mRecyclingAction.hasJumpTarget();
          this.mRecyclingAction.runIfNecessary(localRecyclerView);
          if (bool)
          {
            if (!this.mRunning)
              break;
            this.mPendingInitialRun = true;
            localRecyclerView.mViewFlinger.postOnAnimation();
          }
        }
        return;
        label146: Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
        this.mTargetView = null;
      }
      stop();
    }

    public View findViewByPosition(int paramInt)
    {
      return this.mRecyclerView.mLayout.findViewByPosition(paramInt);
    }

    public int getChildCount()
    {
      return this.mRecyclerView.mLayout.getChildCount();
    }

    public int getChildPosition(View paramView)
    {
      return this.mRecyclerView.getChildLayoutPosition(paramView);
    }

    @Nullable
    public RecyclerView.LayoutManager getLayoutManager()
    {
      return this.mLayoutManager;
    }

    public int getTargetPosition()
    {
      return this.mTargetPosition;
    }

    @Deprecated
    public void instantScrollToPosition(int paramInt)
    {
      this.mRecyclerView.scrollToPosition(paramInt);
    }

    public boolean isPendingInitialRun()
    {
      return this.mPendingInitialRun;
    }

    public boolean isRunning()
    {
      return this.mRunning;
    }

    protected void normalize(PointF paramPointF)
    {
      double d = Math.sqrt(paramPointF.x * paramPointF.x + paramPointF.y * paramPointF.y);
      paramPointF.x = (float)(paramPointF.x / d);
      paramPointF.y = (float)(paramPointF.y / d);
    }

    protected void onChildAttachedToWindow(View paramView)
    {
      if (getChildPosition(paramView) == getTargetPosition())
        this.mTargetView = paramView;
    }

    protected abstract void onSeekTargetStep(int paramInt1, int paramInt2, RecyclerView.State paramState, Action paramAction);

    protected abstract void onStart();

    protected abstract void onStop();

    protected abstract void onTargetFound(View paramView, RecyclerView.State paramState, Action paramAction);

    public void setTargetPosition(int paramInt)
    {
      this.mTargetPosition = paramInt;
    }

    void start(RecyclerView paramRecyclerView, RecyclerView.LayoutManager paramLayoutManager)
    {
      this.mRecyclerView = paramRecyclerView;
      this.mLayoutManager = paramLayoutManager;
      if (this.mTargetPosition == -1)
        throw new IllegalArgumentException("Invalid target position");
      RecyclerView.State.access$4802(this.mRecyclerView.mState, this.mTargetPosition);
      this.mRunning = true;
      this.mPendingInitialRun = true;
      this.mTargetView = findViewByPosition(getTargetPosition());
      onStart();
      this.mRecyclerView.mViewFlinger.postOnAnimation();
    }

    protected final void stop()
    {
      if (!this.mRunning)
        return;
      onStop();
      RecyclerView.State.access$4802(this.mRecyclerView.mState, -1);
      this.mTargetView = null;
      this.mTargetPosition = -1;
      this.mPendingInitialRun = false;
      this.mRunning = false;
      this.mLayoutManager.onSmoothScrollerStopped(this);
      this.mLayoutManager = null;
      this.mRecyclerView = null;
    }

    public static class Action
    {
      public static final int UNDEFINED_DURATION = -2147483648;
      private boolean changed = false;
      private int consecutiveUpdates = 0;
      private int mDuration;
      private int mDx;
      private int mDy;
      private Interpolator mInterpolator;
      private int mJumpToPosition = -1;

      public Action(int paramInt1, int paramInt2)
      {
        this(paramInt1, paramInt2, -2147483648, null);
      }

      public Action(int paramInt1, int paramInt2, int paramInt3)
      {
        this(paramInt1, paramInt2, paramInt3, null);
      }

      public Action(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
      {
        this.mDx = paramInt1;
        this.mDy = paramInt2;
        this.mDuration = paramInt3;
        this.mInterpolator = paramInterpolator;
      }

      private void runIfNecessary(RecyclerView paramRecyclerView)
      {
        if (this.mJumpToPosition >= 0)
        {
          int i = this.mJumpToPosition;
          this.mJumpToPosition = -1;
          paramRecyclerView.jumpToPositionForSmoothScroller(i);
          this.changed = false;
          return;
        }
        if (this.changed)
        {
          validate();
          if (this.mInterpolator == null)
            if (this.mDuration == -2147483648)
              paramRecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
          while (true)
          {
            this.consecutiveUpdates += 1;
            if (this.consecutiveUpdates > 10)
              Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
            this.changed = false;
            return;
            paramRecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
            continue;
            paramRecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
          }
        }
        this.consecutiveUpdates = 0;
      }

      private void validate()
      {
        if ((this.mInterpolator != null) && (this.mDuration < 1))
          throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
        if (this.mDuration < 1)
          throw new IllegalStateException("Scroll duration must be a positive number");
      }

      public int getDuration()
      {
        return this.mDuration;
      }

      public int getDx()
      {
        return this.mDx;
      }

      public int getDy()
      {
        return this.mDy;
      }

      public Interpolator getInterpolator()
      {
        return this.mInterpolator;
      }

      boolean hasJumpTarget()
      {
        return this.mJumpToPosition >= 0;
      }

      public void jumpTo(int paramInt)
      {
        this.mJumpToPosition = paramInt;
      }

      public void setDuration(int paramInt)
      {
        this.changed = true;
        this.mDuration = paramInt;
      }

      public void setDx(int paramInt)
      {
        this.changed = true;
        this.mDx = paramInt;
      }

      public void setDy(int paramInt)
      {
        this.changed = true;
        this.mDy = paramInt;
      }

      public void setInterpolator(Interpolator paramInterpolator)
      {
        this.changed = true;
        this.mInterpolator = paramInterpolator;
      }

      public void update(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
      {
        this.mDx = paramInt1;
        this.mDy = paramInt2;
        this.mDuration = paramInt3;
        this.mInterpolator = paramInterpolator;
        this.changed = true;
      }
    }
  }

  public static class State
  {
    private SparseArray<Object> mData;
    private int mDeletedInvisibleItemCountSincePreviousLayout = 0;
    final List<View> mDisappearingViewsInLayoutPass = new ArrayList();
    private boolean mInPreLayout = false;
    int mItemCount = 0;
    ArrayMap<Long, RecyclerView.ViewHolder> mOldChangedHolders = new ArrayMap();
    ArrayMap<RecyclerView.ViewHolder, RecyclerView.ItemHolderInfo> mPostLayoutHolderMap = new ArrayMap();
    ArrayMap<RecyclerView.ViewHolder, RecyclerView.ItemHolderInfo> mPreLayoutHolderMap = new ArrayMap();
    private int mPreviousLayoutItemCount = 0;
    private boolean mRunPredictiveAnimations = false;
    private boolean mRunSimpleAnimations = false;
    private boolean mStructureChanged = false;
    private int mTargetPosition = -1;

    private void removeFrom(ArrayMap<Long, RecyclerView.ViewHolder> paramArrayMap, RecyclerView.ViewHolder paramViewHolder)
    {
      int i = paramArrayMap.size() - 1;
      while (true)
      {
        if (i >= 0)
        {
          if (paramViewHolder == paramArrayMap.valueAt(i))
            paramArrayMap.removeAt(i);
        }
        else
          return;
        i -= 1;
      }
    }

    void addToDisappearingList(View paramView)
    {
      if (!this.mDisappearingViewsInLayoutPass.contains(paramView))
        this.mDisappearingViewsInLayoutPass.add(paramView);
    }

    public boolean didStructureChange()
    {
      return this.mStructureChanged;
    }

    public <T> T get(int paramInt)
    {
      if (this.mData == null)
        return null;
      return this.mData.get(paramInt);
    }

    public int getItemCount()
    {
      if (this.mInPreLayout)
        return this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
      return this.mItemCount;
    }

    public int getTargetScrollPosition()
    {
      return this.mTargetPosition;
    }

    public boolean hasTargetScrollPosition()
    {
      return this.mTargetPosition != -1;
    }

    public boolean isPreLayout()
    {
      return this.mInPreLayout;
    }

    public void onViewIgnored(RecyclerView.ViewHolder paramViewHolder)
    {
      onViewRecycled(paramViewHolder);
    }

    void onViewRecycled(RecyclerView.ViewHolder paramViewHolder)
    {
      this.mPreLayoutHolderMap.remove(paramViewHolder);
      this.mPostLayoutHolderMap.remove(paramViewHolder);
      if (this.mOldChangedHolders != null)
        removeFrom(this.mOldChangedHolders, paramViewHolder);
      this.mDisappearingViewsInLayoutPass.remove(paramViewHolder.itemView);
    }

    public void put(int paramInt, Object paramObject)
    {
      if (this.mData == null)
        this.mData = new SparseArray();
      this.mData.put(paramInt, paramObject);
    }

    public void remove(int paramInt)
    {
      if (this.mData == null)
        return;
      this.mData.remove(paramInt);
    }

    void removeFromDisappearingList(View paramView)
    {
      this.mDisappearingViewsInLayoutPass.remove(paramView);
    }

    State reset()
    {
      this.mTargetPosition = -1;
      if (this.mData != null)
        this.mData.clear();
      this.mItemCount = 0;
      this.mStructureChanged = false;
      return this;
    }

    public String toString()
    {
      return "State{mTargetPosition=" + this.mTargetPosition + ", mPreLayoutHolderMap=" + this.mPreLayoutHolderMap + ", mPostLayoutHolderMap=" + this.mPostLayoutHolderMap + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
    }

    public boolean willRunPredictiveAnimations()
    {
      return this.mRunPredictiveAnimations;
    }

    public boolean willRunSimpleAnimations()
    {
      return this.mRunSimpleAnimations;
    }
  }

  public static abstract class ViewCacheExtension
  {
    public abstract View getViewForPositionAndType(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2);
  }

  private class ViewFlinger
    implements Runnable
  {
    private boolean mEatRunOnAnimationRequest = false;
    private Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
    private int mLastFlingX;
    private int mLastFlingY;
    private boolean mReSchedulePostAnimationCallback = false;
    private ScrollerCompat mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);

    public ViewFlinger()
    {
    }

    private int computeScrollDuration(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int j = Math.abs(paramInt1);
      int k = Math.abs(paramInt2);
      int i;
      if (j > k)
      {
        i = 1;
        paramInt3 = (int)Math.sqrt(paramInt3 * paramInt3 + paramInt4 * paramInt4);
        paramInt2 = (int)Math.sqrt(paramInt1 * paramInt1 + paramInt2 * paramInt2);
        if (i == 0)
          break label140;
      }
      label140: for (paramInt1 = RecyclerView.this.getWidth(); ; paramInt1 = RecyclerView.this.getHeight())
      {
        paramInt4 = paramInt1 / 2;
        float f3 = Math.min(1.0F, 1.0F * paramInt2 / paramInt1);
        float f1 = paramInt4;
        float f2 = paramInt4;
        f3 = distanceInfluenceForSnapDuration(f3);
        if (paramInt3 <= 0)
          break label151;
        paramInt1 = Math.round(1000.0F * Math.abs((f1 + f2 * f3) / paramInt3)) * 4;
        return Math.min(paramInt1, 2000);
        i = 0;
        break;
      }
      label151: if (i != 0);
      for (paramInt2 = j; ; paramInt2 = k)
      {
        paramInt1 = (int)((paramInt2 / paramInt1 + 1.0F) * 300.0F);
        break;
      }
    }

    private void disableRunOnAnimationRequests()
    {
      this.mReSchedulePostAnimationCallback = false;
      this.mEatRunOnAnimationRequest = true;
    }

    private float distanceInfluenceForSnapDuration(float paramFloat)
    {
      return (float)Math.sin((float)((paramFloat - 0.5F) * 0.47123891676382D));
    }

    private void enableRunOnAnimationRequests()
    {
      this.mEatRunOnAnimationRequest = false;
      if (this.mReSchedulePostAnimationCallback)
        postOnAnimation();
    }

    public void fling(int paramInt1, int paramInt2)
    {
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      this.mScroller.fling(0, 0, paramInt1, paramInt2, -2147483648, 2147483647, -2147483648, 2147483647);
      postOnAnimation();
    }

    void postOnAnimation()
    {
      if (this.mEatRunOnAnimationRequest)
      {
        this.mReSchedulePostAnimationCallback = true;
        return;
      }
      RecyclerView.this.removeCallbacks(this);
      ViewCompat.postOnAnimation(RecyclerView.this, this);
    }

    public void run()
    {
      disableRunOnAnimationRequests();
      RecyclerView.this.consumePendingUpdateOperations();
      ScrollerCompat localScrollerCompat = this.mScroller;
      RecyclerView.SmoothScroller localSmoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
      int i4;
      int i5;
      int n;
      int i;
      int i3;
      int m;
      int i1;
      int j;
      int i2;
      int k;
      if (localScrollerCompat.computeScrollOffset())
      {
        int i6 = localScrollerCompat.getCurrX();
        int i7 = localScrollerCompat.getCurrY();
        i4 = i6 - this.mLastFlingX;
        i5 = i7 - this.mLastFlingY;
        n = 0;
        i = 0;
        i3 = 0;
        m = 0;
        this.mLastFlingX = i6;
        this.mLastFlingY = i7;
        i1 = 0;
        j = 0;
        i2 = 0;
        k = 0;
        if (RecyclerView.this.mAdapter != null)
        {
          RecyclerView.this.eatRequestLayout();
          RecyclerView.this.onEnterLayoutOrScroll();
          TraceCompat.beginSection("RV Scroll");
          if (i4 != 0)
          {
            i = RecyclerView.this.mLayout.scrollHorizontallyBy(i4, RecyclerView.this.mRecycler, RecyclerView.this.mState);
            j = i4 - i;
          }
          if (i5 != 0)
          {
            m = RecyclerView.this.mLayout.scrollVerticallyBy(i5, RecyclerView.this.mRecycler, RecyclerView.this.mState);
            k = i5 - m;
          }
          TraceCompat.endSection();
          if (RecyclerView.this.supportsChangeAnimations())
          {
            i1 = RecyclerView.this.mChildHelper.getChildCount();
            n = 0;
            while (n < i1)
            {
              View localView = RecyclerView.this.mChildHelper.getChildAt(n);
              Object localObject = RecyclerView.this.getChildViewHolder(localView);
              if ((localObject != null) && (((RecyclerView.ViewHolder)localObject).mShadowingHolder != null))
              {
                localObject = ((RecyclerView.ViewHolder)localObject).mShadowingHolder.itemView;
                i2 = localView.getLeft();
                i3 = localView.getTop();
                if ((i2 != ((View)localObject).getLeft()) || (i3 != ((View)localObject).getTop()))
                  ((View)localObject).layout(i2, i3, ((View)localObject).getWidth() + i2, ((View)localObject).getHeight() + i3);
              }
              n += 1;
            }
          }
          RecyclerView.this.onExitLayoutOrScroll();
          RecyclerView.this.resumeRequestLayout(false);
          n = i;
          i1 = j;
          i2 = k;
          i3 = m;
          if (localSmoothScroller != null)
          {
            n = i;
            i1 = j;
            i2 = k;
            i3 = m;
            if (!localSmoothScroller.isPendingInitialRun())
            {
              n = i;
              i1 = j;
              i2 = k;
              i3 = m;
              if (localSmoothScroller.isRunning())
              {
                n = RecyclerView.this.mState.getItemCount();
                if (n != 0)
                  break label807;
                localSmoothScroller.stop();
                i3 = m;
                i2 = k;
                i1 = j;
                n = i;
              }
            }
          }
        }
        if (!RecyclerView.this.mItemDecorations.isEmpty())
          RecyclerView.this.invalidate();
        if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2)
          RecyclerView.this.considerReleasingGlowsOnScroll(i4, i5);
        if ((i1 != 0) || (i2 != 0))
        {
          k = (int)localScrollerCompat.getCurrVelocity();
          i = 0;
          if (i1 != i6)
          {
            if (i1 >= 0)
              break label890;
            i = -k;
          }
          label553: j = 0;
          if (i2 != i7)
          {
            if (i2 >= 0)
              break label908;
            j = -k;
          }
          label573: if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2)
            RecyclerView.this.absorbGlows(i, j);
          if (((i != 0) || (i1 == i6) || (localScrollerCompat.getFinalX() == 0)) && ((j != 0) || (i2 == i7) || (localScrollerCompat.getFinalY() == 0)))
            localScrollerCompat.abortAnimation();
        }
        if ((n != 0) || (i3 != 0))
          RecyclerView.this.dispatchOnScrolled(n, i3);
        if (!RecyclerView.this.awakenScrollBars())
          RecyclerView.this.invalidate();
        if ((i5 == 0) || (!RecyclerView.this.mLayout.canScrollVertically()) || (i3 != i5))
          break label926;
        i = 1;
        label703: if ((i4 == 0) || (!RecyclerView.this.mLayout.canScrollHorizontally()) || (n != i4))
          break label932;
        j = 1;
        label731: if (((i4 != 0) || (i5 != 0)) && (j == 0) && (i == 0))
          break label938;
        i = 1;
        label754: if ((!localScrollerCompat.isFinished()) && (i != 0))
          break label944;
        RecyclerView.this.setScrollState(0);
      }
      while (true)
      {
        if (localSmoothScroller != null)
        {
          if (localSmoothScroller.isPendingInitialRun())
            localSmoothScroller.onAnimation(0, 0);
          if (!this.mReSchedulePostAnimationCallback)
            localSmoothScroller.stop();
        }
        enableRunOnAnimationRequests();
        return;
        label807: if (localSmoothScroller.getTargetPosition() >= n)
        {
          localSmoothScroller.setTargetPosition(n - 1);
          localSmoothScroller.onAnimation(i4 - j, i5 - k);
          n = i;
          i1 = j;
          i2 = k;
          i3 = m;
          break;
        }
        localSmoothScroller.onAnimation(i4 - j, i5 - k);
        n = i;
        i1 = j;
        i2 = k;
        i3 = m;
        break;
        label890: if (i1 > 0)
        {
          i = k;
          break label553;
        }
        i = 0;
        break label553;
        label908: if (i2 > 0)
        {
          j = k;
          break label573;
        }
        j = 0;
        break label573;
        label926: i = 0;
        break label703;
        label932: j = 0;
        break label731;
        label938: i = 0;
        break label754;
        label944: postOnAnimation();
      }
    }

    public void smoothScrollBy(int paramInt1, int paramInt2)
    {
      smoothScrollBy(paramInt1, paramInt2, 0, 0);
    }

    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3)
    {
      smoothScrollBy(paramInt1, paramInt2, paramInt3, RecyclerView.sQuinticInterpolator);
    }

    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      smoothScrollBy(paramInt1, paramInt2, computeScrollDuration(paramInt1, paramInt2, paramInt3, paramInt4));
    }

    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
    {
      if (this.mInterpolator != paramInterpolator)
      {
        this.mInterpolator = paramInterpolator;
        this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), paramInterpolator);
      }
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      this.mScroller.startScroll(0, 0, paramInt1, paramInt2, paramInt3);
      postOnAnimation();
    }

    public void stop()
    {
      RecyclerView.this.removeCallbacks(this);
      this.mScroller.abortAnimation();
    }
  }

  public static abstract class ViewHolder
  {
    static final int FLAG_ADAPTER_FULLUPDATE = 1024;
    static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
    static final int FLAG_BOUND = 1;
    static final int FLAG_CHANGED = 64;
    static final int FLAG_IGNORE = 128;
    static final int FLAG_INVALID = 4;
    static final int FLAG_NOT_RECYCLABLE = 16;
    static final int FLAG_REMOVED = 8;
    static final int FLAG_RETURNED_FROM_SCRAP = 32;
    static final int FLAG_TMP_DETACHED = 256;
    static final int FLAG_UPDATE = 2;
    private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
    public final View itemView;
    private int mFlags;
    private int mIsRecyclableCount = 0;
    long mItemId = -1L;
    int mItemViewType = -1;
    int mOldPosition = -1;
    RecyclerView mOwnerRecyclerView;
    List<Object> mPayloads = null;
    int mPosition = -1;
    int mPreLayoutPosition = -1;
    private RecyclerView.Recycler mScrapContainer = null;
    ViewHolder mShadowedHolder = null;
    ViewHolder mShadowingHolder = null;
    List<Object> mUnmodifiedPayloads = null;
    private int mWasImportantForAccessibilityBeforeHidden = 0;

    public ViewHolder(View paramView)
    {
      if (paramView == null)
        throw new IllegalArgumentException("itemView may not be null");
      this.itemView = paramView;
    }

    private void createPayloadsIfNeeded()
    {
      if (this.mPayloads == null)
      {
        this.mPayloads = new ArrayList();
        this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
      }
    }

    private boolean doesTransientStatePreventRecycling()
    {
      return ((this.mFlags & 0x10) == 0) && (ViewCompat.hasTransientState(this.itemView));
    }

    private void onEnteredHiddenState()
    {
      this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
      ViewCompat.setImportantForAccessibility(this.itemView, 4);
    }

    private void onLeftHiddenState()
    {
      ViewCompat.setImportantForAccessibility(this.itemView, this.mWasImportantForAccessibilityBeforeHidden);
      this.mWasImportantForAccessibilityBeforeHidden = 0;
    }

    private boolean shouldBeKeptAsChild()
    {
      return (this.mFlags & 0x10) != 0;
    }

    void addChangePayload(Object paramObject)
    {
      if (paramObject == null)
        addFlags(1024);
      do
        return;
      while ((this.mFlags & 0x400) != 0);
      createPayloadsIfNeeded();
      this.mPayloads.add(paramObject);
    }

    void addFlags(int paramInt)
    {
      this.mFlags |= paramInt;
    }

    void clearOldPosition()
    {
      this.mOldPosition = -1;
      this.mPreLayoutPosition = -1;
    }

    void clearPayload()
    {
      if (this.mPayloads != null)
        this.mPayloads.clear();
      this.mFlags &= -1025;
    }

    void clearReturnedFromScrapFlag()
    {
      this.mFlags &= -33;
    }

    void clearTmpDetachFlag()
    {
      this.mFlags &= -257;
    }

    void flagRemovedAndOffsetPosition(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      addFlags(8);
      offsetPosition(paramInt2, paramBoolean);
      this.mPosition = paramInt1;
    }

    public final int getAdapterPosition()
    {
      if (this.mOwnerRecyclerView == null)
        return -1;
      return this.mOwnerRecyclerView.getAdapterPositionFor(this);
    }

    public final long getItemId()
    {
      return this.mItemId;
    }

    public final int getItemViewType()
    {
      return this.mItemViewType;
    }

    public final int getLayoutPosition()
    {
      if (this.mPreLayoutPosition == -1)
        return this.mPosition;
      return this.mPreLayoutPosition;
    }

    public final int getOldPosition()
    {
      return this.mOldPosition;
    }

    @Deprecated
    public final int getPosition()
    {
      if (this.mPreLayoutPosition == -1)
        return this.mPosition;
      return this.mPreLayoutPosition;
    }

    List<Object> getUnmodifiedPayloads()
    {
      if ((this.mFlags & 0x400) == 0)
      {
        if ((this.mPayloads == null) || (this.mPayloads.size() == 0))
          return FULLUPDATE_PAYLOADS;
        return this.mUnmodifiedPayloads;
      }
      return FULLUPDATE_PAYLOADS;
    }

    boolean hasAnyOfTheFlags(int paramInt)
    {
      return (this.mFlags & paramInt) != 0;
    }

    boolean isAdapterPositionUnknown()
    {
      return ((this.mFlags & 0x200) != 0) || (isInvalid());
    }

    boolean isBound()
    {
      return (this.mFlags & 0x1) != 0;
    }

    boolean isChanged()
    {
      return (this.mFlags & 0x40) != 0;
    }

    boolean isInvalid()
    {
      return (this.mFlags & 0x4) != 0;
    }

    public final boolean isRecyclable()
    {
      return ((this.mFlags & 0x10) == 0) && (!ViewCompat.hasTransientState(this.itemView));
    }

    boolean isRemoved()
    {
      return (this.mFlags & 0x8) != 0;
    }

    boolean isScrap()
    {
      return this.mScrapContainer != null;
    }

    boolean isTmpDetached()
    {
      return (this.mFlags & 0x100) != 0;
    }

    boolean needsUpdate()
    {
      return (this.mFlags & 0x2) != 0;
    }

    void offsetPosition(int paramInt, boolean paramBoolean)
    {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition;
      if (this.mPreLayoutPosition == -1)
        this.mPreLayoutPosition = this.mPosition;
      if (paramBoolean)
        this.mPreLayoutPosition += paramInt;
      this.mPosition += paramInt;
      if (this.itemView.getLayoutParams() != null)
        ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
    }

    void resetInternal()
    {
      this.mFlags = 0;
      this.mPosition = -1;
      this.mOldPosition = -1;
      this.mItemId = -1L;
      this.mPreLayoutPosition = -1;
      this.mIsRecyclableCount = 0;
      this.mShadowedHolder = null;
      this.mShadowingHolder = null;
      clearPayload();
      this.mWasImportantForAccessibilityBeforeHidden = 0;
    }

    void saveOldPosition()
    {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition;
    }

    void setFlags(int paramInt1, int paramInt2)
    {
      this.mFlags = (this.mFlags & (paramInt2 ^ 0xFFFFFFFF) | paramInt1 & paramInt2);
    }

    public final void setIsRecyclable(boolean paramBoolean)
    {
      int i;
      if (paramBoolean)
      {
        i = this.mIsRecyclableCount - 1;
        this.mIsRecyclableCount = i;
        if (this.mIsRecyclableCount >= 0)
          break label64;
        this.mIsRecyclableCount = 0;
        Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
      }
      label64: 
      do
      {
        return;
        i = this.mIsRecyclableCount + 1;
        break;
        if ((paramBoolean) || (this.mIsRecyclableCount != 1))
          continue;
        this.mFlags |= 16;
        return;
      }
      while ((!paramBoolean) || (this.mIsRecyclableCount != 0));
      this.mFlags &= -17;
    }

    void setScrapContainer(RecyclerView.Recycler paramRecycler)
    {
      this.mScrapContainer = paramRecycler;
    }

    boolean shouldIgnore()
    {
      return (this.mFlags & 0x80) != 0;
    }

    void stopIgnoring()
    {
      this.mFlags &= -129;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
      if (isScrap())
        localStringBuilder.append(" scrap");
      if (isInvalid())
        localStringBuilder.append(" invalid");
      if (!isBound())
        localStringBuilder.append(" unbound");
      if (needsUpdate())
        localStringBuilder.append(" update");
      if (isRemoved())
        localStringBuilder.append(" removed");
      if (shouldIgnore())
        localStringBuilder.append(" ignored");
      if (isChanged())
        localStringBuilder.append(" changed");
      if (isTmpDetached())
        localStringBuilder.append(" tmpDetached");
      if (!isRecyclable())
        localStringBuilder.append(" not recyclable(" + this.mIsRecyclableCount + ")");
      if (isAdapterPositionUnknown())
        localStringBuilder.append("undefined adapter position");
      if (this.itemView.getParent() == null)
        localStringBuilder.append(" no parent");
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }

    void unScrap()
    {
      this.mScrapContainer.unscrapView(this);
    }

    boolean wasReturnedFromScrap()
    {
      return (this.mFlags & 0x20) != 0;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.widget.RecyclerView
 * JD-Core Version:    0.6.0
 */