package com.dianping.base.widget.wheel.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.dianping.base.widget.wheel.adapter.WheelViewAdapter;
import com.dianping.v1.R.drawable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WheelView extends View
{
  private static final int DEF_VISIBLE_ITEMS = 5;
  private static final int[] GROUPBOOKING_SHADOWS_COLORS;
  private static final int ITEM_OFFSET_PERCENT = 10;
  private static final int PADDING = 10;
  private static final int[] SHADOWS_COLORS = { -15658735, 11184810, 11184810 };
  private GradientDrawable bottomShadow;
  private Drawable centerDrawable;
  private List<OnWheelChangedListener> changingListeners = new LinkedList();
  private List<OnWheelClickedListener> clickingListeners = new LinkedList();
  private int currentItem = 0;
  private DataSetObserver dataObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      WheelView.this.invalidateWheel(false);
    }

    public void onInvalidated()
    {
      WheelView.this.invalidateWheel(true);
    }
  };
  private int firstItem;
  boolean isCyclic = false;
  boolean isScrollingPerformed;
  boolean isTopBottomShadowed = false;
  private int itemHeight = 0;
  private LinearLayout itemsLayout;
  private WheelRecycle recycle = new WheelRecycle(this);
  WheelScroller scroller;
  WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener()
  {
    public void onFinished()
    {
      if (WheelView.this.isScrollingPerformed)
      {
        WheelView.this.notifyScrollingListenersAboutEnd();
        WheelView.this.isScrollingPerformed = false;
      }
      WheelView.this.scrollingOffset = 0;
      WheelView.this.invalidate();
    }

    public void onJustify()
    {
      if (Math.abs(WheelView.this.scrollingOffset) > 1)
        WheelView.this.scroller.scroll(WheelView.this.scrollingOffset, 0);
    }

    public void onScroll(int paramInt)
    {
      WheelView.this.doScroll(paramInt);
      if (WheelView.this.isTopBottomShadowed)
        if (WheelView.this.scrollingOffset < -(WheelView.this.viewAdapter.getItemsCount() - WheelView.this.currentItem) * WheelView.this.getItemHeight())
        {
          WheelView.this.scrollingOffset = (-(WheelView.this.viewAdapter.getItemsCount() - WheelView.this.currentItem) * WheelView.this.getItemHeight());
          WheelView.this.scroller.stopScrolling();
        }
      do
      {
        do
          return;
        while (WheelView.this.scrollingOffset <= (WheelView.this.currentItem + 1) * WheelView.this.getItemHeight());
        WheelView.this.scrollingOffset = ((WheelView.this.currentItem + 1) * WheelView.this.getItemHeight());
        WheelView.this.scroller.stopScrolling();
        return;
        paramInt = WheelView.this.getHeight();
        if (WheelView.this.scrollingOffset <= paramInt)
          continue;
        WheelView.this.scrollingOffset = paramInt;
        WheelView.this.scroller.stopScrolling();
        return;
      }
      while (WheelView.this.scrollingOffset >= -paramInt);
      WheelView.this.scrollingOffset = (-paramInt);
      WheelView.this.scroller.stopScrolling();
    }

    public void onStarted()
    {
      WheelView.this.isScrollingPerformed = true;
      WheelView.this.notifyScrollingListenersAboutStart();
    }
  };
  private List<OnWheelScrollListener> scrollingListeners = new LinkedList();
  int scrollingOffset;
  private GradientDrawable topShadow;
  private WheelViewAdapter viewAdapter;
  private int visibleItems = 5;

  static
  {
    GROUPBOOKING_SHADOWS_COLORS = new int[] { -1, -1426063361, 16777215 };
  }

  public WheelView(Context paramContext)
  {
    super(paramContext);
    initData(paramContext);
  }

  public WheelView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initData(paramContext);
  }

  public WheelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initData(paramContext);
  }

  private boolean addViewItem(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    View localView = getItemView(paramInt);
    if (localView != null)
    {
      if (!paramBoolean)
        break label32;
      this.itemsLayout.addView(localView, 0);
    }
    while (true)
    {
      i = 1;
      return i;
      label32: this.itemsLayout.addView(localView);
    }
  }

  private void buildViewForMeasuring()
  {
    if (this.itemsLayout != null)
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    while (true)
    {
      int j = this.visibleItems / 2;
      int i = this.currentItem + j;
      while (i >= this.currentItem - j)
      {
        if (addViewItem(i, true))
          this.firstItem = i;
        i -= 1;
      }
      createItemsLayout();
    }
  }

  private int calculateLayoutWidth(int paramInt1, int paramInt2)
  {
    initResourcesIfNecessary();
    this.itemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    int i = this.itemsLayout.getMeasuredWidth();
    if (paramInt2 == 1073741824)
      i = paramInt1;
    while (true)
    {
      this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(i - 20, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
      return i;
      int j = Math.max(i + 20, getSuggestedMinimumWidth());
      i = j;
      if (paramInt2 != -2147483648)
        continue;
      i = j;
      if (paramInt1 >= j)
        continue;
      i = paramInt1;
    }
  }

  private void createItemsLayout()
  {
    if (this.itemsLayout == null)
    {
      this.itemsLayout = new LinearLayout(getContext());
      this.itemsLayout.setOrientation(1);
    }
  }

  private void drawCenterRect(Canvas paramCanvas)
  {
    int i = getHeight() / 2;
    int j = (int)(getItemHeight() / 2 * 1.2D);
    this.centerDrawable.setBounds(0, i - j, getWidth(), i + j);
    this.centerDrawable.draw(paramCanvas);
  }

  private void drawItems(Canvas paramCanvas)
  {
    paramCanvas.save();
    paramCanvas.translate(10.0F, -((this.currentItem - this.firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2) + this.scrollingOffset);
    this.itemsLayout.draw(paramCanvas);
    paramCanvas.restore();
  }

  private void drawShadows(Canvas paramCanvas)
  {
    int i = getHeight() / 2;
    int j = (int)(getItemHeight() / 2 * 1.2D);
    this.topShadow.setBounds(0, 0, getWidth(), i - j);
    this.topShadow.draw(paramCanvas);
    this.bottomShadow.setBounds(0, i + j, getWidth(), getHeight());
    this.bottomShadow.draw(paramCanvas);
  }

  private int getDesiredHeight(LinearLayout paramLinearLayout)
  {
    if ((paramLinearLayout != null) && (paramLinearLayout.getChildAt(0) != null))
      this.itemHeight = paramLinearLayout.getChildAt(0).getMeasuredHeight();
    return Math.max(this.itemHeight * this.visibleItems - this.itemHeight * 10 / 50, getSuggestedMinimumHeight());
  }

  private int getItemHeight()
  {
    if (this.itemHeight != 0)
      return this.itemHeight;
    if ((this.itemsLayout != null) && (this.itemsLayout.getChildAt(0) != null))
    {
      this.itemHeight = this.itemsLayout.getChildAt(0).getHeight();
      return this.itemHeight;
    }
    return getHeight() / this.visibleItems;
  }

  private View getItemView(int paramInt)
  {
    if ((this.viewAdapter == null) || (this.viewAdapter.getItemsCount() == 0))
      return null;
    int j = this.viewAdapter.getItemsCount();
    int i = paramInt;
    if (!isValidItemIndex(paramInt))
      return this.viewAdapter.getEmptyItem(this.recycle.getEmptyItem(), this.itemsLayout);
    while (i < 0)
      i += j;
    return this.viewAdapter.getItem(i % j, this.recycle.getItem(), this.itemsLayout);
  }

  private ItemsRange getItemsRange()
  {
    if (getItemHeight() == 0)
      return null;
    int i = this.currentItem;
    int j = 1;
    while (getItemHeight() * j < getHeight())
    {
      i -= 1;
      j += 2;
    }
    int i1 = j;
    int k = i;
    int n;
    if (this.scrollingOffset != 0)
    {
      k = i;
      int m;
      if (this.scrollingOffset > 0)
        m = i - 1;
      i = this.scrollingOffset / getItemHeight();
      m -= i;
      i1 = (int)(j + 1 + Math.asin(i));
    }
    return new ItemsRange(n, i1);
  }

  private void initData(Context paramContext)
  {
    this.scroller = new WheelScroller(getContext(), this.scrollingListener);
  }

  private void initResourcesIfNecessary()
  {
    if (this.centerDrawable == null)
      this.centerDrawable = getContext().getResources().getDrawable(R.drawable.wheel_val);
    if (this.topShadow == null)
      this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS);
    if (this.bottomShadow == null)
      this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS);
    if (this.isTopBottomShadowed)
    {
      this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, GROUPBOOKING_SHADOWS_COLORS);
      this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, GROUPBOOKING_SHADOWS_COLORS);
    }
  }

  private boolean isValidItemIndex(int paramInt)
  {
    return (this.viewAdapter != null) && (this.viewAdapter.getItemsCount() > 0) && ((this.isCyclic) || ((paramInt >= 0) && (paramInt < this.viewAdapter.getItemsCount())));
  }

  private void layout(int paramInt1, int paramInt2)
  {
    this.itemsLayout.layout(0, 0, paramInt1 - 20, paramInt2);
  }

  private boolean rebuildItems()
  {
    ItemsRange localItemsRange = getItemsRange();
    int i;
    int m;
    label45: int n;
    if (this.itemsLayout != null)
    {
      i = this.recycle.recycleItems(this.itemsLayout, this.firstItem, localItemsRange);
      if (this.firstItem != i)
      {
        m = 1;
        this.firstItem = i;
        n = m;
        if (m == 0)
        {
          if ((this.firstItem == localItemsRange.getFirst()) && (this.itemsLayout.getChildCount() == localItemsRange.getCount()))
            break label210;
          n = 1;
        }
        label82: if ((this.firstItem <= localItemsRange.getFirst()) || (this.firstItem > localItemsRange.getLast()))
          break label228;
        i = this.firstItem - 1;
        label111: if ((i >= localItemsRange.getFirst()) && (addViewItem(i, true)))
          break label216;
      }
    }
    int j;
    while (true)
    {
      j = this.firstItem;
      i = this.itemsLayout.getChildCount();
      while (i < localItemsRange.getCount())
      {
        int k = j;
        if (!addViewItem(this.firstItem + i, false))
        {
          k = j;
          if (this.itemsLayout.getChildCount() == 0)
            k = j + 1;
        }
        i += 1;
        j = k;
      }
      m = 0;
      break;
      createItemsLayout();
      m = 1;
      break label45;
      label210: n = 0;
      break label82;
      label216: this.firstItem = i;
      i -= 1;
      break label111;
      label228: this.firstItem = localItemsRange.getFirst();
    }
    this.firstItem = j;
    return n;
  }

  private void updateView()
  {
    if (rebuildItems())
    {
      calculateLayoutWidth(getWidth(), 1073741824);
      layout(getWidth(), getHeight());
    }
  }

  public void addChangingListener(OnWheelChangedListener paramOnWheelChangedListener)
  {
    this.changingListeners.add(paramOnWheelChangedListener);
  }

  public void addClickingListener(OnWheelClickedListener paramOnWheelClickedListener)
  {
    this.clickingListeners.add(paramOnWheelClickedListener);
  }

  public void addScrollingListener(OnWheelScrollListener paramOnWheelScrollListener)
  {
    this.scrollingListeners.add(paramOnWheelScrollListener);
  }

  void doScroll(int paramInt)
  {
    this.scrollingOffset += paramInt;
    int n = getItemHeight();
    int j = this.scrollingOffset / n;
    int k = this.currentItem - j;
    int i1 = this.viewAdapter.getItemsCount();
    paramInt = this.scrollingOffset % n;
    int m = paramInt;
    if (Math.abs(paramInt) <= n / 2)
      m = 0;
    int i;
    if ((this.isCyclic) && (i1 > 0))
    {
      if (m > 0)
      {
        i = k - 1;
        paramInt = j + 1;
      }
      while (i < 0)
      {
        i += i1;
        continue;
        paramInt = j;
        i = k;
        if (m >= 0)
          continue;
        i = k + 1;
        paramInt = j - 1;
      }
      i %= i1;
      j = this.scrollingOffset;
      if (i == this.currentItem)
        break label290;
      setCurrentItem(i, false);
    }
    while (true)
    {
      this.scrollingOffset = (j - paramInt * n);
      if (this.scrollingOffset > getHeight())
        this.scrollingOffset = (this.scrollingOffset % getHeight() + getHeight());
      return;
      if (k < 0)
      {
        paramInt = this.currentItem;
        i = 0;
        break;
      }
      if (k >= i1)
      {
        paramInt = this.currentItem - i1 + 1;
        i = i1 - 1;
        break;
      }
      if ((k > 0) && (m > 0))
      {
        i = k - 1;
        paramInt = j + 1;
        break;
      }
      paramInt = j;
      i = k;
      if (k >= i1 - 1)
        break;
      paramInt = j;
      i = k;
      if (m >= 0)
        break;
      i = k + 1;
      paramInt = j - 1;
      break;
      label290: invalidate();
    }
  }

  public int getCurrentItem()
  {
    return this.currentItem;
  }

  public WheelViewAdapter getViewAdapter()
  {
    return this.viewAdapter;
  }

  public int getVisibleItems()
  {
    return this.visibleItems;
  }

  public void invalidateWheel(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.recycle.clearAll();
      if (this.itemsLayout != null)
        this.itemsLayout.removeAllViews();
      this.scrollingOffset = 0;
    }
    while (true)
    {
      invalidate();
      return;
      if (this.itemsLayout == null)
        continue;
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    }
  }

  public boolean isCyclic()
  {
    return this.isCyclic;
  }

  protected void notifyChangingListeners(int paramInt1, int paramInt2)
  {
    Iterator localIterator = this.changingListeners.iterator();
    while (localIterator.hasNext())
      ((OnWheelChangedListener)localIterator.next()).onChanged(this, paramInt1, paramInt2);
  }

  protected void notifyClickListenersAboutClick(int paramInt)
  {
    Iterator localIterator = this.clickingListeners.iterator();
    while (localIterator.hasNext())
      ((OnWheelClickedListener)localIterator.next()).onItemClicked(this, paramInt);
  }

  protected void notifyScrollingListenersAboutEnd()
  {
    Iterator localIterator = this.scrollingListeners.iterator();
    while (localIterator.hasNext())
      ((OnWheelScrollListener)localIterator.next()).onScrollingFinished(this);
  }

  protected void notifyScrollingListenersAboutStart()
  {
    Iterator localIterator = this.scrollingListeners.iterator();
    while (localIterator.hasNext())
      ((OnWheelScrollListener)localIterator.next()).onScrollingStarted(this);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if ((this.viewAdapter != null) && (this.viewAdapter.getItemsCount() > 0))
    {
      updateView();
      drawItems(paramCanvas);
      drawCenterRect(paramCanvas);
    }
    if (this.isTopBottomShadowed)
      drawShadows(paramCanvas);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    layout(paramInt3 - paramInt1, paramInt4 - paramInt2);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    buildViewForMeasuring();
    int k = calculateLayoutWidth(paramInt1, i);
    if (j == 1073741824)
      paramInt1 = paramInt2;
    while (true)
    {
      setMeasuredDimension(k, paramInt1);
      return;
      i = getDesiredHeight(this.itemsLayout);
      paramInt1 = i;
      if (j != -2147483648)
        continue;
      paramInt1 = Math.min(i, paramInt2);
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((!isEnabled()) || (getViewAdapter() == null))
      return true;
    switch (paramMotionEvent.getAction())
    {
    default:
    case 2:
    case 1:
    }
    do
      while (true)
      {
        return this.scroller.onTouchEvent(paramMotionEvent);
        if (getParent() == null)
          continue;
        getParent().requestDisallowInterceptTouchEvent(true);
      }
    while (this.isScrollingPerformed);
    int i = (int)paramMotionEvent.getY() - getHeight() / 2;
    if (i > 0)
      i += getItemHeight() / 2;
    while (true)
    {
      i /= getItemHeight();
      if ((i == 0) || (!isValidItemIndex(this.currentItem + i)))
        break;
      notifyClickListenersAboutClick(this.currentItem + i);
      break;
      i -= getItemHeight() / 2;
    }
  }

  public void scroll(int paramInt1, int paramInt2)
  {
    int i = getItemHeight();
    int j = this.scrollingOffset;
    this.scroller.scroll(i * paramInt1 - j, paramInt2);
  }

  public void setCenterDrawable(int paramInt)
  {
    this.centerDrawable = getContext().getResources().getDrawable(paramInt);
  }

  public void setCurrentItem(int paramInt)
  {
    setCurrentItem(paramInt, false);
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    if ((this.viewAdapter == null) || (this.viewAdapter.getItemsCount() == 0));
    int k;
    int i;
    do
      while (true)
      {
        return;
        k = this.viewAdapter.getItemsCount();
        if (paramInt >= 0)
        {
          i = paramInt;
          if (paramInt < k)
            break;
        }
        else
        {
          if (!this.isCyclic)
            continue;
          while (paramInt < 0)
            paramInt += k;
          i = paramInt % k;
        }
      }
    while (i == this.currentItem);
    if (paramBoolean)
    {
      int j = i - this.currentItem;
      paramInt = j;
      if (this.isCyclic)
      {
        i = Math.min(i, this.currentItem) + k - Math.max(i, this.currentItem);
        paramInt = j;
        if (i < Math.abs(j))
          if (j >= 0)
            break label144;
      }
      label144: for (paramInt = i; ; paramInt = -i)
      {
        scroll(paramInt, 0);
        return;
      }
    }
    this.scrollingOffset = 0;
    paramInt = this.currentItem;
    this.currentItem = i;
    notifyChangingListeners(paramInt, this.currentItem);
    invalidate();
  }

  public void setCyclic(boolean paramBoolean)
  {
    this.isCyclic = paramBoolean;
    invalidateWheel(false);
  }

  public void setInterpolator(Interpolator paramInterpolator)
  {
    this.scroller.setInterpolator(paramInterpolator);
  }

  public void setViewAdapter(WheelViewAdapter paramWheelViewAdapter)
  {
    if (this.viewAdapter != null)
      this.viewAdapter.unregisterDataSetObserver(this.dataObserver);
    this.viewAdapter = paramWheelViewAdapter;
    if (this.viewAdapter != null)
      this.viewAdapter.registerDataSetObserver(this.dataObserver);
    invalidateWheel(true);
  }

  public void setVisibleItems(int paramInt)
  {
    this.visibleItems = paramInt;
  }

  public void showTopBottomShadows()
  {
    this.isTopBottomShadowed = true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wheel.widget.WheelView
 * JD-Core Version:    0.6.0
 */