package com.tonicartos.widget.stickygridheaders;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StickyGridHeadersGridView extends GridView
  implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener
{
  private static final String ERROR_PLATFORM = "Error supporting platform " + Build.VERSION.SDK_INT + ".";
  private static final int MATCHED_STICKIED_HEADER = -2;
  private static final int NO_MATCHED_HEADER = -1;
  static final String TAG = StickyGridHeadersGridView.class.getSimpleName();
  protected static final int TOUCH_MODE_DONE_WAITING = 2;
  protected static final int TOUCH_MODE_DOWN = 0;
  protected static final int TOUCH_MODE_FINISHED_LONG_PRESS = -2;
  protected static final int TOUCH_MODE_REST = -1;
  protected static final int TOUCH_MODE_TAP = 1;
  protected StickyGridHeadersBaseAdapterWrapper mAdapter;
  private boolean mAreHeadersSticky = true;
  private boolean mClipToPaddingHasBeenSet;
  private final Rect mClippingRect = new Rect();
  private boolean mClippingToPadding;
  private int mColumnWidth;
  private long mCurrentHeaderId = -1L;
  protected boolean mDataChanged;
  private DataSetObserver mDataSetObserver = new StickyGridHeadersGridView.1(this);
  private int mHeaderBottomPosition;
  boolean mHeaderChildBeingPressed = false;
  private boolean mHeadersIgnorePadding;
  private int mHorizontalSpacing;
  private boolean mMaskStickyHeaderRegion = true;
  protected int mMotionHeaderPosition;
  private float mMotionY;
  private int mNumColumns;
  private boolean mNumColumnsSet;
  private int mNumMeasuredColumns = 1;
  private OnHeaderClickListener mOnHeaderClickListener;
  private StickyGridHeadersGridView.OnHeaderLongClickListener mOnHeaderLongClickListener;
  private AdapterView.OnItemClickListener mOnItemClickListener;
  private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
  private AdapterView.OnItemSelectedListener mOnItemSelectedListener;
  public StickyGridHeadersGridView.CheckForHeaderLongPress mPendingCheckForLongPress;
  public StickyGridHeadersGridView.CheckForHeaderTap mPendingCheckForTap;
  private StickyGridHeadersGridView.PerformHeaderClick mPerformHeaderClick;
  private AbsListView.OnScrollListener mScrollListener;
  private int mScrollState = 0;
  private View mStickiedHeader;
  protected int mTouchMode;
  private Runnable mTouchModeReset;
  private int mTouchSlop;
  private int mVerticalSpacing;

  public StickyGridHeadersGridView(Context paramContext)
  {
    this(paramContext, null);
  }

  public StickyGridHeadersGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842865);
  }

  public StickyGridHeadersGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    super.setOnScrollListener(this);
    setVerticalFadingEdgeEnabled(false);
    if (!this.mNumColumnsSet)
      this.mNumColumns = -1;
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
  }

  private int findMotionHeader(float paramFloat)
  {
    int k;
    if ((this.mStickiedHeader != null) && (paramFloat <= this.mHeaderBottomPosition))
    {
      k = -2;
      return k;
    }
    int i = 0;
    int j = getFirstVisiblePosition();
    while (true)
    {
      if (j > getLastVisiblePosition())
        break label110;
      if (getItemIdAtPosition(j) == -1L)
      {
        View localView = getChildAt(i);
        k = localView.getBottom();
        int m = localView.getTop();
        if (paramFloat <= k)
        {
          k = i;
          if (paramFloat >= m)
            break;
        }
      }
      j += this.mNumMeasuredColumns;
      i += this.mNumMeasuredColumns;
    }
    label110: return -1;
  }

  private int getHeaderHeight()
  {
    if (this.mStickiedHeader != null)
      return this.mStickiedHeader.getMeasuredHeight();
    return 0;
  }

  private static MotionEvent.PointerCoords[] getPointerCoords(MotionEvent paramMotionEvent)
  {
    int j = paramMotionEvent.getPointerCount();
    MotionEvent.PointerCoords[] arrayOfPointerCoords = new MotionEvent.PointerCoords[j];
    int i = 0;
    while (i < j)
    {
      arrayOfPointerCoords[i] = new MotionEvent.PointerCoords();
      paramMotionEvent.getPointerCoords(i, arrayOfPointerCoords[i]);
      i += 1;
    }
    return arrayOfPointerCoords;
  }

  private static int[] getPointerIds(MotionEvent paramMotionEvent)
  {
    int j = paramMotionEvent.getPointerCount();
    int[] arrayOfInt = new int[j];
    int i = 0;
    while (i < j)
    {
      arrayOfInt[i] = paramMotionEvent.getPointerId(i);
      i += 1;
    }
    return arrayOfInt;
  }

  private long headerViewPositionToId(int paramInt)
  {
    if (paramInt == -2)
      return this.mCurrentHeaderId;
    return this.mAdapter.getHeaderId(getFirstVisiblePosition() + paramInt);
  }

  private void measureHeader()
  {
    if (this.mStickiedHeader == null)
      return;
    int i;
    ViewGroup.LayoutParams localLayoutParams;
    if (this.mHeadersIgnorePadding)
    {
      i = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
      localLayoutParams = this.mStickiedHeader.getLayoutParams();
      if ((localLayoutParams == null) || (localLayoutParams.height <= 0))
        break label137;
    }
    label137: for (int j = View.MeasureSpec.makeMeasureSpec(localLayoutParams.height, 1073741824); ; j = View.MeasureSpec.makeMeasureSpec(0, 0))
    {
      this.mStickiedHeader.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
      this.mStickiedHeader.measure(i, j);
      if (!this.mHeadersIgnorePadding)
        break label146;
      this.mStickiedHeader.layout(getLeft(), 0, getRight(), this.mStickiedHeader.getMeasuredHeight());
      return;
      i = View.MeasureSpec.makeMeasureSpec(getWidth() - getPaddingLeft() - getPaddingRight(), 1073741824);
      break;
    }
    label146: this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), this.mStickiedHeader.getMeasuredHeight());
  }

  private void reset()
  {
    this.mHeaderBottomPosition = 0;
    swapStickiedHeader(null);
    this.mCurrentHeaderId = -9223372036854775808L;
  }

  private void scrollChanged(int paramInt)
  {
    if ((this.mAdapter == null) || (this.mAdapter.getCount() == 0) || (!this.mAreHeadersSticky));
    label163: label197: label346: label356: 
    do
    {
      int k;
      int j;
      int m;
      long l;
      Object localObject1;
      View localView;
      Object localObject2;
      while (true)
      {
        do
          return;
        while (getChildAt(0) == null);
        k = paramInt;
        j = paramInt - this.mNumMeasuredColumns;
        i = j;
        if (j < 0)
          i = paramInt;
        m = paramInt + this.mNumMeasuredColumns;
        j = m;
        if (m >= this.mAdapter.getCount())
          j = paramInt;
        if (this.mVerticalSpacing != 0)
          break;
        l = this.mAdapter.getHeaderId(paramInt);
        i = k;
        if (this.mCurrentHeaderId != l)
        {
          swapStickiedHeader(this.mAdapter.getHeaderView(i, this.mStickiedHeader, this));
          measureHeader();
          this.mCurrentHeaderId = l;
        }
        int n = getChildCount();
        if (n == 0)
          continue;
        localObject1 = null;
        k = 99999;
        j = 0;
        if (j >= n)
          break label405;
        localView = super.getChildAt(j);
        if (!this.mClippingToPadding)
          break label346;
        i = localView.getTop() - getPaddingTop();
        if (i >= 0)
          break label356;
        m = k;
        localObject2 = localObject1;
      }
      while (true)
      {
        j += this.mNumMeasuredColumns;
        localObject1 = localObject2;
        k = m;
        break label163;
        if (this.mVerticalSpacing < 0)
        {
          this.mAdapter.getHeaderId(paramInt);
          if (getChildAt(this.mNumMeasuredColumns).getTop() <= 0)
          {
            l = this.mAdapter.getHeaderId(j);
            i = j;
            break;
          }
          l = this.mAdapter.getHeaderId(paramInt);
          i = k;
          break;
        }
        j = getChildAt(0).getTop();
        if ((j > 0) && (j < this.mVerticalSpacing))
        {
          l = this.mAdapter.getHeaderId(i);
          break;
        }
        l = this.mAdapter.getHeaderId(paramInt);
        i = k;
        break;
        i = localView.getTop();
        break label197;
        localObject2 = localObject1;
        m = k;
        if (this.mAdapter.getItemId(getPositionForView(localView)) != -1L)
          continue;
        localObject2 = localObject1;
        m = k;
        if (i >= k)
          continue;
        localObject2 = localView;
        m = i;
      }
      int i = getHeaderHeight();
      if (localObject1 != null)
      {
        if ((paramInt == 0) && (super.getChildAt(0).getTop() > 0) && (!this.mClippingToPadding))
        {
          this.mHeaderBottomPosition = 0;
          return;
        }
        if (this.mClippingToPadding)
        {
          this.mHeaderBottomPosition = Math.min(localObject1.getTop(), getPaddingTop() + i);
          if (this.mHeaderBottomPosition < getPaddingTop());
          for (paramInt = getPaddingTop() + i; ; paramInt = this.mHeaderBottomPosition)
          {
            this.mHeaderBottomPosition = paramInt;
            return;
          }
        }
        this.mHeaderBottomPosition = Math.min(localObject1.getTop(), i);
        if (this.mHeaderBottomPosition < 0);
        for (paramInt = i; ; paramInt = this.mHeaderBottomPosition)
        {
          this.mHeaderBottomPosition = paramInt;
          return;
        }
      }
      this.mHeaderBottomPosition = i;
    }
    while (!this.mClippingToPadding);
    label405: this.mHeaderBottomPosition += getPaddingTop();
  }

  private void swapStickiedHeader(View paramView)
  {
    detachHeader(this.mStickiedHeader);
    attachHeader(paramView);
    this.mStickiedHeader = paramView;
  }

  private MotionEvent transformEvent(MotionEvent paramMotionEvent, int paramInt)
  {
    if (paramInt == -2)
      return paramMotionEvent;
    long l1 = paramMotionEvent.getDownTime();
    long l2 = paramMotionEvent.getEventTime();
    int i = paramMotionEvent.getAction();
    int j = paramMotionEvent.getPointerCount();
    int[] arrayOfInt = getPointerIds(paramMotionEvent);
    MotionEvent.PointerCoords[] arrayOfPointerCoords = getPointerCoords(paramMotionEvent);
    int k = paramMotionEvent.getMetaState();
    float f1 = paramMotionEvent.getXPrecision();
    float f2 = paramMotionEvent.getYPrecision();
    int m = paramMotionEvent.getDeviceId();
    int n = paramMotionEvent.getEdgeFlags();
    int i1 = paramMotionEvent.getSource();
    int i2 = paramMotionEvent.getFlags();
    paramMotionEvent = getChildAt(paramInt);
    paramInt = 0;
    while (paramInt < j)
    {
      MotionEvent.PointerCoords localPointerCoords = arrayOfPointerCoords[paramInt];
      localPointerCoords.y -= paramMotionEvent.getTop();
      paramInt += 1;
    }
    return MotionEvent.obtain(l1, l2, i, j, arrayOfInt, arrayOfPointerCoords, k, f1, f2, m, n, i1, i2);
  }

  public boolean areHeadersSticky()
  {
    return this.mAreHeadersSticky;
  }

  void attachHeader(View paramView)
  {
    if (paramView == null)
      return;
    try
    {
      Field localField = View.class.getDeclaredField("mAttachInfo");
      localField.setAccessible(true);
      Method localMethod = View.class.getDeclaredMethod("dispatchAttachedToWindow", new Class[] { Class.forName("android.view.View$AttachInfo"), Integer.TYPE });
      localMethod.setAccessible(true);
      localMethod.invoke(paramView, new Object[] { localField.get(this), Integer.valueOf(8) });
      return;
    }
    catch (java.lang.NoSuchMethodException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.ClassNotFoundException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.IllegalArgumentException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.IllegalAccessException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.reflect.InvocationTargetException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.NoSuchFieldException paramView)
    {
    }
    throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
  }

  void detachHeader(View paramView)
  {
    if (paramView == null)
      return;
    try
    {
      Method localMethod = View.class.getDeclaredMethod("dispatchDetachedFromWindow", new Class[0]);
      localMethod.setAccessible(true);
      localMethod.invoke(paramView, new Object[0]);
      return;
    }
    catch (java.lang.NoSuchMethodException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.IllegalArgumentException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.IllegalAccessException paramView)
    {
      throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
    }
    catch (java.lang.reflect.InvocationTargetException paramView)
    {
    }
    throw new StickyGridHeadersGridView.RuntimePlatformSupportException(this, paramView);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    if (Build.VERSION.SDK_INT < 8)
      scrollChanged(getFirstVisiblePosition());
    int i;
    int m;
    int n;
    if ((this.mStickiedHeader != null) && (this.mAreHeadersSticky) && (this.mStickiedHeader.getVisibility() == 0))
    {
      i = 1;
      m = getHeaderHeight();
      n = this.mHeaderBottomPosition - m;
      if ((i != 0) && (this.mMaskStickyHeaderRegion))
      {
        if (!this.mHeadersIgnorePadding)
          break label215;
        this.mClippingRect.left = 0;
      }
    }
    ArrayList localArrayList;
    int k;
    for (this.mClippingRect.right = getWidth(); ; this.mClippingRect.right = (getWidth() - getPaddingRight()))
    {
      this.mClippingRect.top = this.mHeaderBottomPosition;
      this.mClippingRect.bottom = getHeight();
      paramCanvas.save();
      paramCanvas.clipRect(this.mClippingRect);
      super.dispatchDraw(paramCanvas);
      localArrayList = new ArrayList();
      j = 0;
      k = getFirstVisiblePosition();
      while (k <= getLastVisiblePosition())
      {
        if (getItemIdAtPosition(k) == -1L)
          localArrayList.add(Integer.valueOf(j));
        k += this.mNumMeasuredColumns;
        j += this.mNumMeasuredColumns;
      }
      i = 0;
      break;
      label215: this.mClippingRect.left = getPaddingLeft();
    }
    int j = 0;
    if (j < localArrayList.size())
    {
      View localView1 = getChildAt(((Integer)localArrayList.get(j)).intValue());
      View localView2;
      while (true)
      {
        try
        {
          localView2 = (View)localView1.getTag();
          if ((((StickyGridHeadersBaseAdapterWrapper.HeaderFillerView)localView1).getHeaderId() == this.mCurrentHeaderId) && (localView1.getTop() < 0) && (this.mAreHeadersSticky))
          {
            k = 1;
            if ((localView2.getVisibility() == 0) && (k == 0))
              break label350;
            j += 1;
          }
        }
        catch (Exception paramCanvas)
        {
          return;
        }
        k = 0;
      }
      label350: if (this.mHeadersIgnorePadding)
      {
        k = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
        label369: int i1 = View.MeasureSpec.makeMeasureSpec(0, 0);
        localView2.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        localView2.measure(k, i1);
        if (!this.mHeadersIgnorePadding)
          break label542;
        localView2.layout(getLeft(), 0, getRight(), localView1.getHeight());
        if (!this.mHeadersIgnorePadding)
          break label573;
        this.mClippingRect.left = 0;
        this.mClippingRect.right = getWidth();
        this.mClippingRect.bottom = localView1.getBottom();
        this.mClippingRect.top = localView1.getTop();
        paramCanvas.save();
        paramCanvas.clipRect(this.mClippingRect);
        if (!this.mHeadersIgnorePadding)
          break label603;
        paramCanvas.translate(0.0F, localView1.getTop());
      }
      while (true)
      {
        label425: label451: localView2.draw(paramCanvas);
        paramCanvas.restore();
        break;
        k = View.MeasureSpec.makeMeasureSpec(getWidth() - getPaddingLeft() - getPaddingRight(), 1073741824);
        break label369;
        label542: localView2.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), localView1.getHeight());
        break label425;
        label573: this.mClippingRect.left = getPaddingLeft();
        this.mClippingRect.right = (getWidth() - getPaddingRight());
        break label451;
        label603: paramCanvas.translate(getPaddingLeft(), localView1.getTop());
      }
    }
    if ((i != 0) && (this.mMaskStickyHeaderRegion))
    {
      paramCanvas.restore();
      if (!this.mHeadersIgnorePadding)
        break label901;
      i = getWidth();
      label649: if (this.mStickiedHeader.getWidth() != i)
      {
        if (!this.mHeadersIgnorePadding)
          break label920;
        i = View.MeasureSpec.makeMeasureSpec(getWidth(), 1073741824);
        label680: j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mStickiedHeader.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        this.mStickiedHeader.measure(i, j);
        if (!this.mHeadersIgnorePadding)
          break label945;
        this.mStickiedHeader.layout(getLeft(), 0, getRight(), this.mStickiedHeader.getHeight());
      }
      label745: if (!this.mHeadersIgnorePadding)
        break label981;
      this.mClippingRect.left = 0;
      this.mClippingRect.right = getWidth();
      label771: this.mClippingRect.bottom = (n + m);
      if (!this.mClippingToPadding)
        break label1011;
      this.mClippingRect.top = getPaddingTop();
      label801: paramCanvas.save();
      paramCanvas.clipRect(this.mClippingRect);
      if (!this.mHeadersIgnorePadding)
        break label1022;
      paramCanvas.translate(0.0F, n);
    }
    while (true)
    {
      if (this.mHeaderBottomPosition != m)
        paramCanvas.saveLayerAlpha(0.0F, 0.0F, paramCanvas.getWidth(), paramCanvas.getHeight(), this.mHeaderBottomPosition * 255 / m, 31);
      this.mStickiedHeader.draw(paramCanvas);
      if (this.mHeaderBottomPosition != m)
        paramCanvas.restore();
      paramCanvas.restore();
      return;
      if (i != 0)
        break;
      return;
      label901: i = getWidth() - getPaddingLeft() - getPaddingRight();
      break label649;
      label920: i = View.MeasureSpec.makeMeasureSpec(getWidth() - getPaddingLeft() - getPaddingRight(), 1073741824);
      break label680;
      label945: this.mStickiedHeader.layout(getLeft() + getPaddingLeft(), 0, getRight() - getPaddingRight(), this.mStickiedHeader.getHeight());
      break label745;
      label981: this.mClippingRect.left = getPaddingLeft();
      this.mClippingRect.right = (getWidth() - getPaddingRight());
      break label771;
      label1011: this.mClippingRect.top = 0;
      break label801;
      label1022: paramCanvas.translate(getPaddingLeft(), n);
    }
  }

  public View getHeaderAt(int paramInt)
  {
    if (paramInt == -2)
      return this.mStickiedHeader;
    try
    {
      View localView = (View)getChildAt(paramInt).getTag();
      return localView;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public View getStickiedHeader()
  {
    return this.mStickiedHeader;
  }

  public boolean getStickyHeaderIsTranscluent()
  {
    return !this.mMaskStickyHeaderRegion;
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    this.mOnItemClickListener.onItemClick(paramAdapterView, paramView, this.mAdapter.translatePosition(paramInt).mPosition, paramLong);
  }

  public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    return this.mOnItemLongClickListener.onItemLongClick(paramAdapterView, paramView, this.mAdapter.translatePosition(paramInt).mPosition, paramLong);
  }

  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    this.mOnItemSelectedListener.onItemSelected(paramAdapterView, paramView, this.mAdapter.translatePosition(paramInt).mPosition, paramLong);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int j;
    if (this.mNumColumns == -1)
      if (this.mColumnWidth > 0)
      {
        int k = Math.max(View.MeasureSpec.getSize(paramInt1) - getPaddingLeft() - getPaddingRight(), 0);
        int i = k / this.mColumnWidth;
        if (i > 0)
          while (true)
          {
            j = i;
            if (i == 1)
              break;
            j = i;
            if (this.mColumnWidth * i + (i - 1) * this.mHorizontalSpacing <= k)
              break;
            i -= 1;
          }
        j = 1;
      }
    for (this.mNumMeasuredColumns = j; ; this.mNumMeasuredColumns = this.mNumColumns)
    {
      if (this.mAdapter != null)
        this.mAdapter.setNumColumns(this.mNumMeasuredColumns);
      measureHeader();
      super.onMeasure(paramInt1, paramInt2);
      return;
      j = 2;
      break;
    }
  }

  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
    this.mOnItemSelectedListener.onNothingSelected(paramAdapterView);
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (StickyGridHeadersGridView.SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    this.mAreHeadersSticky = paramParcelable.areHeadersSticky;
    requestLayout();
  }

  public Parcelable onSaveInstanceState()
  {
    StickyGridHeadersGridView.SavedState localSavedState = new StickyGridHeadersGridView.SavedState(super.onSaveInstanceState());
    localSavedState.areHeadersSticky = this.mAreHeadersSticky;
    return localSavedState;
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.mScrollListener != null)
      this.mScrollListener.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
    if (Build.VERSION.SDK_INT >= 8)
      scrollChanged(paramInt1);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    if (this.mScrollListener != null)
      this.mScrollListener.onScrollStateChanged(paramAbsListView, paramInt);
    this.mScrollState = paramInt;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    boolean bool = this.mHeaderChildBeingPressed;
    Object localObject2;
    if (this.mHeaderChildBeingPressed)
    {
      localObject2 = getHeaderAt(this.mMotionHeaderPosition);
      if (this.mMotionHeaderPosition != -2)
        break label158;
      localObject1 = localObject2;
      if ((i == 1) || (i == 3))
        this.mHeaderChildBeingPressed = false;
      if (localObject2 != null)
      {
        ((View)localObject2).dispatchTouchEvent(transformEvent(paramMotionEvent, this.mMotionHeaderPosition));
        ((View)localObject2).invalidate();
        ((View)localObject2).postDelayed(new StickyGridHeadersGridView.2(this, (View)localObject1), ViewConfiguration.getPressedStateDuration());
        invalidate(0, ((View)localObject1).getTop(), getWidth(), ((View)localObject1).getTop() + ((View)localObject1).getHeight());
      }
    }
    switch (i & 0xFF)
    {
    default:
    case 0:
    case 2:
    case 1:
    }
    label158: 
    do
    {
      while (true)
      {
        return super.onTouchEvent(paramMotionEvent);
        localObject1 = getChildAt(this.mMotionHeaderPosition);
        break;
        if (this.mPendingCheckForTap == null)
          this.mPendingCheckForTap = new StickyGridHeadersGridView.CheckForHeaderTap(this);
        postDelayed(this.mPendingCheckForTap, ViewConfiguration.getTapTimeout());
        i = (int)paramMotionEvent.getY();
        this.mMotionY = i;
        this.mMotionHeaderPosition = findMotionHeader(i);
        if ((this.mMotionHeaderPosition == -1) || (this.mScrollState == 2))
          continue;
        localObject1 = getHeaderAt(this.mMotionHeaderPosition);
        if (localObject1 != null)
        {
          if (((View)localObject1).dispatchTouchEvent(transformEvent(paramMotionEvent, this.mMotionHeaderPosition)))
          {
            this.mHeaderChildBeingPressed = true;
            ((View)localObject1).setPressed(true);
          }
          ((View)localObject1).invalidate();
          paramMotionEvent = (MotionEvent)localObject1;
          if (this.mMotionHeaderPosition != -2)
            paramMotionEvent = getChildAt(this.mMotionHeaderPosition);
          invalidate(0, paramMotionEvent.getTop(), getWidth(), paramMotionEvent.getTop() + paramMotionEvent.getHeight());
        }
        this.mTouchMode = 0;
        return true;
        if ((this.mMotionHeaderPosition == -1) || (Math.abs(paramMotionEvent.getY() - this.mMotionY) <= this.mTouchSlop))
          continue;
        this.mTouchMode = -1;
        localObject1 = getHeaderAt(this.mMotionHeaderPosition);
        if (localObject1 != null)
        {
          ((View)localObject1).setPressed(false);
          ((View)localObject1).invalidate();
        }
        localObject1 = getHandler();
        if (localObject1 != null)
          ((Handler)localObject1).removeCallbacks(this.mPendingCheckForLongPress);
        this.mMotionHeaderPosition = -1;
      }
      if (this.mTouchMode != -2)
        continue;
      this.mTouchMode = -1;
      return true;
    }
    while ((this.mTouchMode == -1) || (this.mMotionHeaderPosition == -1));
    Object localObject1 = getHeaderAt(this.mMotionHeaderPosition);
    if ((!bool) && (localObject1 != null))
    {
      if (this.mTouchMode != 0)
        ((View)localObject1).setPressed(false);
      if (this.mPerformHeaderClick == null)
        this.mPerformHeaderClick = new StickyGridHeadersGridView.PerformHeaderClick(this, null);
      localObject2 = this.mPerformHeaderClick;
      ((StickyGridHeadersGridView.PerformHeaderClick)localObject2).mClickMotionPosition = this.mMotionHeaderPosition;
      ((StickyGridHeadersGridView.PerformHeaderClick)localObject2).rememberWindowAttachCount();
      if ((this.mTouchMode != 0) && (this.mTouchMode != 1))
        break label647;
      Handler localHandler = getHandler();
      if (localHandler != null)
      {
        if (this.mTouchMode != 0)
          break label631;
        paramMotionEvent = this.mPendingCheckForTap;
        localHandler.removeCallbacks(paramMotionEvent);
      }
      if (this.mDataChanged)
        break label639;
      this.mTouchMode = 1;
      ((View)localObject1).setPressed(true);
      setPressed(true);
      if (this.mTouchModeReset != null)
        removeCallbacks(this.mTouchModeReset);
      this.mTouchModeReset = new StickyGridHeadersGridView.3(this, (View)localObject1, (StickyGridHeadersGridView.PerformHeaderClick)localObject2);
      postDelayed(this.mTouchModeReset, ViewConfiguration.getPressedStateDuration());
    }
    while (true)
    {
      this.mTouchMode = -1;
      return true;
      label631: paramMotionEvent = this.mPendingCheckForLongPress;
      break;
      label639: this.mTouchMode = -1;
      continue;
      label647: if (this.mDataChanged)
        continue;
      ((StickyGridHeadersGridView.PerformHeaderClick)localObject2).run();
    }
  }

  public boolean performHeaderClick(View paramView, long paramLong)
  {
    if (this.mOnHeaderClickListener != null)
    {
      playSoundEffect(0);
      if (paramView != null)
        paramView.sendAccessibilityEvent(1);
      this.mOnHeaderClickListener.onHeaderClick(this, paramView, paramLong);
      return true;
    }
    return false;
  }

  public boolean performHeaderLongPress(View paramView, long paramLong)
  {
    boolean bool = false;
    if (this.mOnHeaderLongClickListener != null)
      bool = this.mOnHeaderLongClickListener.onHeaderLongClick(this, paramView, paramLong);
    if (bool)
    {
      if (paramView != null)
        paramView.sendAccessibilityEvent(2);
      performHapticFeedback(0);
    }
    return bool;
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    if ((this.mAdapter != null) && (this.mDataSetObserver != null))
      this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
    if (!this.mClipToPaddingHasBeenSet)
      this.mClippingToPadding = true;
    if ((paramListAdapter instanceof StickyGridHeadersBaseAdapter))
      paramListAdapter = (StickyGridHeadersBaseAdapter)paramListAdapter;
    while (true)
    {
      this.mAdapter = new StickyGridHeadersBaseAdapterWrapper(getContext(), this, paramListAdapter);
      this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
      reset();
      super.setAdapter(this.mAdapter);
      return;
      if ((paramListAdapter instanceof StickyGridHeadersSimpleAdapter))
      {
        paramListAdapter = new StickyGridHeadersSimpleAdapterWrapper((StickyGridHeadersSimpleAdapter)paramListAdapter);
        continue;
      }
      paramListAdapter = new StickyGridHeadersListAdapterWrapper(paramListAdapter);
    }
  }

  public void setAreHeadersSticky(boolean paramBoolean)
  {
    if (paramBoolean != this.mAreHeadersSticky)
    {
      this.mAreHeadersSticky = paramBoolean;
      requestLayout();
    }
  }

  public void setClipToPadding(boolean paramBoolean)
  {
    super.setClipToPadding(paramBoolean);
    this.mClippingToPadding = paramBoolean;
    this.mClipToPaddingHasBeenSet = true;
  }

  public void setColumnWidth(int paramInt)
  {
    super.setColumnWidth(paramInt);
    this.mColumnWidth = paramInt;
  }

  public void setHeadersIgnorePadding(boolean paramBoolean)
  {
    this.mHeadersIgnorePadding = paramBoolean;
  }

  public void setHorizontalSpacing(int paramInt)
  {
    super.setHorizontalSpacing(paramInt);
    this.mHorizontalSpacing = paramInt;
  }

  public void setNumColumns(int paramInt)
  {
    super.setNumColumns(paramInt);
    this.mNumColumnsSet = true;
    this.mNumColumns = paramInt;
    if ((paramInt != -1) && (this.mAdapter != null))
      this.mAdapter.setNumColumns(paramInt);
  }

  public void setOnHeaderClickListener(OnHeaderClickListener paramOnHeaderClickListener)
  {
    this.mOnHeaderClickListener = paramOnHeaderClickListener;
  }

  public void setOnHeaderLongClickListener(StickyGridHeadersGridView.OnHeaderLongClickListener paramOnHeaderLongClickListener)
  {
    if (!isLongClickable())
      setLongClickable(true);
    this.mOnHeaderLongClickListener = paramOnHeaderLongClickListener;
  }

  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mOnItemClickListener = paramOnItemClickListener;
    super.setOnItemClickListener(this);
  }

  public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener paramOnItemLongClickListener)
  {
    this.mOnItemLongClickListener = paramOnItemLongClickListener;
    super.setOnItemLongClickListener(this);
  }

  public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
  {
    this.mOnItemSelectedListener = paramOnItemSelectedListener;
    super.setOnItemSelectedListener(this);
  }

  public void setOnScrollListener(AbsListView.OnScrollListener paramOnScrollListener)
  {
    this.mScrollListener = paramOnScrollListener;
  }

  public void setStickyHeaderIsTranscluent(boolean paramBoolean)
  {
    if (!paramBoolean);
    for (paramBoolean = true; ; paramBoolean = false)
    {
      this.mMaskStickyHeaderRegion = paramBoolean;
      return;
    }
  }

  public void setVerticalSpacing(int paramInt)
  {
    super.setVerticalSpacing(paramInt);
    this.mVerticalSpacing = paramInt;
  }

  public static abstract interface OnHeaderClickListener
  {
    public abstract void onHeaderClick(AdapterView<?> paramAdapterView, View paramView, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView
 * JD-Core Version:    0.6.0
 */