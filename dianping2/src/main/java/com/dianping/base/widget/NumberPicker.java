package com.dianping.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.dianping.v1.R.attr;
import com.dianping.v1.R.id;
import com.dianping.v1.R.styleable;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class NumberPicker extends LinearLayout
{
  private static final int DEFAULT_LAYOUT_RESOURCE_ID = 0;
  private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300L;
  private static final char[] DIGIT_CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785 };
  private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
  private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
  private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
  private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
  private static final int SIZE_UNSPECIFIED = -1;
  private static final int SNAP_SCROLL_DURATION = 300;
  private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9F;
  private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
  private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
  private SupportAccessibilityNodeProvider mAccessibilityNodeProvider;
  private final Scroller mAdjustScroller;
  private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
  private int mBottomSelectionDividerBottom;
  private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
  private final boolean mComputeMaxWidth;
  private int mCurrentScrollOffset;
  private final ImageButton mDecrementButton;
  private boolean mDecrementVirtualButtonPressed;
  private String[] mDisplayedValues;
  private final Scroller mFlingScroller;
  private Formatter mFormatter;
  private final boolean mHasSelectorWheel;
  private final ImageButton mIncrementButton;
  private boolean mIncrementVirtualButtonPressed;
  private boolean mIngonreMoveEvents;
  private int mInitialScrollOffset = -2147483648;
  private final EditText mInputText;
  private long mLastDownEventTime;
  private float mLastDownEventY;
  private float mLastDownOrMoveEventY;
  private int mLastHandledDownDpadKeyCode = -1;
  private int mLastHoveredChildVirtualViewId;
  private long mLongPressUpdateInterval = 300L;
  private final int mMaxHeight;
  private int mMaxValue;
  private int mMaxWidth;
  private int mMaximumFlingVelocity;
  private final int mMinHeight;
  private int mMinValue;
  private final int mMinWidth;
  private int mMinimumFlingVelocity;
  private OnScrollListener mOnScrollListener;
  private OnValueChangeListener mOnValueChangeListener;
  private final PressedStateHelper mPressedStateHelper;
  private int mPreviousScrollerY;
  private int mScrollState = 0;
  private final Drawable mSelectionDivider;
  private final int mSelectionDividerHeight;
  private final int mSelectionDividersDistance;
  private int mSelectorElementHeight;
  private final SparseArray<String> mSelectorIndexToStringCache = new SparseArray();
  private final int[] mSelectorIndices = new int[3];
  private int mSelectorTextGapHeight;
  private final Paint mSelectorWheelPaint;
  private SetSelectionCommand mSetSelectionCommand;
  private boolean mShowSoftInputOnTap;
  private final int mSolidColor;
  private final int mTextSize;
  private int mTopSelectionDividerTop;
  private int mTouchSlop;
  private int mValue;
  private VelocityTracker mVelocityTracker;
  private final Drawable mVirtualButtonPressedDrawable;
  private boolean mWrapSelectorWheel;

  public NumberPicker(Context paramContext)
  {
    this(paramContext, null);
  }

  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.numberPickerStyle);
  }

  @TargetApi(16)
  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NumberPicker, paramInt, 0);
    paramInt = paramAttributeSet.getResourceId(R.styleable.NumberPicker_internalLayout, 0);
    if (paramInt != 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mHasSelectorWheel = bool;
      this.mSolidColor = paramAttributeSet.getColor(R.styleable.NumberPicker_solidColor, 0);
      this.mSelectionDivider = paramAttributeSet.getDrawable(R.styleable.NumberPicker_selectionDivider);
      int i = (int)TypedValue.applyDimension(1, 2.0F, getResources().getDisplayMetrics());
      this.mSelectionDividerHeight = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_selectionDividerHeight, i);
      i = (int)TypedValue.applyDimension(1, 48.0F, getResources().getDisplayMetrics());
      this.mSelectionDividersDistance = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_selectionDividersDistance, i);
      this.mMinHeight = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_internalMinHeight, -1);
      this.mMaxHeight = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_internalMaxHeight, -1);
      if ((this.mMinHeight == -1) || (this.mMaxHeight == -1) || (this.mMinHeight <= this.mMaxHeight))
        break;
      throw new IllegalArgumentException("minHeight > maxHeight");
    }
    this.mMinWidth = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_internalMinWidth, -1);
    this.mMaxWidth = paramAttributeSet.getDimensionPixelSize(R.styleable.NumberPicker_internalMaxWidth, -1);
    if ((this.mMinWidth != -1) && (this.mMaxWidth != -1) && (this.mMinWidth > this.mMaxWidth))
      throw new IllegalArgumentException("minWidth > maxWidth");
    if (this.mMaxWidth == -1)
    {
      bool = true;
      this.mComputeMaxWidth = bool;
      this.mVirtualButtonPressedDrawable = paramAttributeSet.getDrawable(R.styleable.NumberPicker_virtualButtonPressedDrawable);
      paramAttributeSet.recycle();
      this.mPressedStateHelper = new PressedStateHelper();
      if (this.mHasSelectorWheel)
        break label701;
      bool = true;
      label345: setWillNotDraw(bool);
      ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(paramInt, this, true);
      paramAttributeSet = new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          NumberPicker.this.hideSoftInput();
          NumberPicker.this.mInputText.clearFocus();
          if (paramView.getId() == R.id.np__increment)
          {
            NumberPicker.this.changeValueByOne(true);
            return;
          }
          NumberPicker.this.changeValueByOne(false);
        }
      };
      2 local2 = new View.OnLongClickListener()
      {
        public boolean onLongClick(View paramView)
        {
          NumberPicker.this.hideSoftInput();
          NumberPicker.this.mInputText.clearFocus();
          if (paramView.getId() == R.id.np__increment)
          {
            NumberPicker.this.postChangeCurrentByOneFromLongPress(true, 0L);
            return true;
          }
          NumberPicker.this.postChangeCurrentByOneFromLongPress(false, 0L);
          return true;
        }
      };
      if (this.mHasSelectorWheel)
        break label707;
      this.mIncrementButton = ((ImageButton)findViewById(R.id.np__increment));
      this.mIncrementButton.setOnClickListener(paramAttributeSet);
      this.mIncrementButton.setOnLongClickListener(local2);
      label428: if (this.mHasSelectorWheel)
        break label715;
      this.mDecrementButton = ((ImageButton)findViewById(R.id.np__decrement));
      this.mDecrementButton.setOnClickListener(paramAttributeSet);
      this.mDecrementButton.setOnLongClickListener(local2);
    }
    while (true)
    {
      this.mInputText = ((EditText)findViewById(R.id.np__numberpicker_input));
      this.mInputText.setFilters(new InputFilter[] { new InputTextFilter() });
      this.mInputText.setRawInputType(2);
      this.mInputText.setImeOptions(6);
      paramContext = ViewConfiguration.get(paramContext);
      this.mTouchSlop = paramContext.getScaledTouchSlop();
      this.mMinimumFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
      this.mMaximumFlingVelocity = (paramContext.getScaledMaximumFlingVelocity() / 8);
      this.mTextSize = (int)this.mInputText.getTextSize();
      paramContext = new Paint();
      paramContext.setAntiAlias(true);
      paramContext.setTextAlign(Paint.Align.CENTER);
      paramContext.setTextSize(this.mTextSize);
      paramContext.setTypeface(this.mInputText.getTypeface());
      paramContext.setColor(this.mInputText.getTextColors().getColorForState(ENABLED_STATE_SET, -1));
      this.mSelectorWheelPaint = paramContext;
      this.mFlingScroller = new Scroller(getContext(), null, true);
      this.mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5F));
      updateInputTextView();
      if ((Build.VERSION.SDK_INT >= 16) && (getImportantForAccessibility() == 0))
        setImportantForAccessibility(1);
      return;
      bool = false;
      break;
      label701: bool = false;
      break label345;
      label707: this.mIncrementButton = null;
      break label428;
      label715: this.mDecrementButton = null;
    }
  }

  private void changeValueByOne(boolean paramBoolean)
  {
    if (this.mHasSelectorWheel)
    {
      this.mInputText.setVisibility(4);
      if (!moveToFinalScrollerPosition(this.mFlingScroller))
        moveToFinalScrollerPosition(this.mAdjustScroller);
      this.mPreviousScrollerY = 0;
      if (paramBoolean)
        this.mFlingScroller.startScroll(0, 0, 0, -this.mSelectorElementHeight, 300);
      while (true)
      {
        invalidate();
        return;
        this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight, 300);
      }
    }
    if (paramBoolean)
    {
      setValueInternal(this.mValue + 1, true);
      return;
    }
    setValueInternal(this.mValue - 1, true);
  }

  private void decrementSelectorIndices(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length - 1;
    while (i > 0)
    {
      paramArrayOfInt[i] = paramArrayOfInt[(i - 1)];
      i -= 1;
    }
    int j = paramArrayOfInt[1] - 1;
    i = j;
    if (this.mWrapSelectorWheel)
    {
      i = j;
      if (j < this.mMinValue)
        i = this.mMaxValue;
    }
    paramArrayOfInt[0] = i;
    ensureCachedScrollSelectorValue(i);
  }

  private void ensureCachedScrollSelectorValue(int paramInt)
  {
    SparseArray localSparseArray = this.mSelectorIndexToStringCache;
    if ((String)localSparseArray.get(paramInt) != null)
      return;
    String str;
    if ((paramInt < this.mMinValue) || (paramInt > this.mMaxValue))
      str = "";
    while (true)
    {
      localSparseArray.put(paramInt, str);
      return;
      if (this.mDisplayedValues != null)
      {
        int i = this.mMinValue;
        str = this.mDisplayedValues[(paramInt - i)];
        continue;
      }
      str = formatNumber(paramInt);
    }
  }

  private boolean ensureScrollWheelAdjusted()
  {
    int k = 0;
    int j = this.mInitialScrollOffset - this.mCurrentScrollOffset;
    if (j != 0)
    {
      this.mPreviousScrollerY = 0;
      i = j;
      if (Math.abs(j) > this.mSelectorElementHeight / 2)
        if (j <= 0)
          break label72;
    }
    label72: for (int i = -this.mSelectorElementHeight; ; i = this.mSelectorElementHeight)
    {
      i = j + i;
      this.mAdjustScroller.startScroll(0, 0, 0, i, 800);
      invalidate();
      k = 1;
      return k;
    }
  }

  private void fling(int paramInt)
  {
    this.mPreviousScrollerY = 0;
    if (paramInt > 0)
      this.mFlingScroller.fling(0, 0, 0, paramInt, 0, 0, 0, 2147483647);
    while (true)
    {
      invalidate();
      return;
      this.mFlingScroller.fling(0, 2147483647, 0, paramInt, 0, 0, 0, 2147483647);
    }
  }

  private String formatNumber(int paramInt)
  {
    if (this.mFormatter != null)
      return this.mFormatter.format(paramInt);
    return formatNumberWithLocale(paramInt);
  }

  private static String formatNumberWithLocale(int paramInt)
  {
    return String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt) });
  }

  // ERROR //
  private int getSelectedPos(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 592	com/dianping/base/widget/NumberPicker:mDisplayedValues	[Ljava/lang/String;
    //   4: ifnonnull +10 -> 14
    //   7: aload_1
    //   8: invokestatic 675	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   11: istore_2
    //   12: iload_2
    //   13: ireturn
    //   14: iconst_0
    //   15: istore_2
    //   16: iload_2
    //   17: aload_0
    //   18: getfield 592	com/dianping/base/widget/NumberPicker:mDisplayedValues	[Ljava/lang/String;
    //   21: arraylength
    //   22: if_icmpge +38 -> 60
    //   25: aload_1
    //   26: invokevirtual 679	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   29: astore_1
    //   30: aload_0
    //   31: getfield 592	com/dianping/base/widget/NumberPicker:mDisplayedValues	[Ljava/lang/String;
    //   34: iload_2
    //   35: aaload
    //   36: invokevirtual 679	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   39: aload_1
    //   40: invokevirtual 683	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   43: ifeq +10 -> 53
    //   46: aload_0
    //   47: getfield 574	com/dianping/base/widget/NumberPicker:mMinValue	I
    //   50: iload_2
    //   51: iadd
    //   52: ireturn
    //   53: iload_2
    //   54: iconst_1
    //   55: iadd
    //   56: istore_2
    //   57: goto -41 -> 16
    //   60: aload_1
    //   61: invokestatic 675	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   64: istore_2
    //   65: iload_2
    //   66: ireturn
    //   67: astore_1
    //   68: aload_0
    //   69: getfield 574	com/dianping/base/widget/NumberPicker:mMinValue	I
    //   72: ireturn
    //   73: astore_1
    //   74: goto -6 -> 68
    //
    // Exception table:
    //   from	to	target	type
    //   7	12	67	java/lang/NumberFormatException
    //   60	65	73	java/lang/NumberFormatException
  }

  private SupportAccessibilityNodeProvider getSupportAccessibilityNodeProvider()
  {
    return new SupportAccessibilityNodeProvider(null);
  }

  private int getWrappedSelectorIndex(int paramInt)
  {
    int i;
    if (paramInt > this.mMaxValue)
      i = this.mMinValue + (paramInt - this.mMaxValue) % (this.mMaxValue - this.mMinValue) - 1;
    do
    {
      return i;
      i = paramInt;
    }
    while (paramInt >= this.mMinValue);
    return this.mMaxValue - (this.mMinValue - paramInt) % (this.mMaxValue - this.mMinValue) + 1;
  }

  private void hideSoftInput()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if ((localInputMethodManager != null) && (localInputMethodManager.isActive(this.mInputText)))
    {
      localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
      if (this.mHasSelectorWheel)
        this.mInputText.setVisibility(4);
    }
  }

  private void incrementSelectorIndices(int[] paramArrayOfInt)
  {
    int i = 0;
    while (i < paramArrayOfInt.length - 1)
    {
      paramArrayOfInt[i] = paramArrayOfInt[(i + 1)];
      i += 1;
    }
    int j = paramArrayOfInt[(paramArrayOfInt.length - 2)] + 1;
    i = j;
    if (this.mWrapSelectorWheel)
    {
      i = j;
      if (j > this.mMaxValue)
        i = this.mMinValue;
    }
    paramArrayOfInt[(paramArrayOfInt.length - 1)] = i;
    ensureCachedScrollSelectorValue(i);
  }

  private void initializeFadingEdges()
  {
    setVerticalFadingEdgeEnabled(true);
    setFadingEdgeLength((getBottom() - getTop() - this.mTextSize) / 2);
  }

  private void initializeSelectorWheel()
  {
    initializeSelectorWheelIndices();
    int[] arrayOfInt = this.mSelectorIndices;
    int i = arrayOfInt.length;
    int j = this.mTextSize;
    this.mSelectorTextGapHeight = (int)((getBottom() - getTop() - i * j) / arrayOfInt.length + 0.5F);
    this.mSelectorElementHeight = (this.mTextSize + this.mSelectorTextGapHeight);
    this.mInitialScrollOffset = (this.mInputText.getBaseline() + this.mInputText.getTop() - this.mSelectorElementHeight * 1);
    this.mCurrentScrollOffset = this.mInitialScrollOffset;
    updateInputTextView();
  }

  private void initializeSelectorWheelIndices()
  {
    this.mSelectorIndexToStringCache.clear();
    int[] arrayOfInt = this.mSelectorIndices;
    int m = getValue();
    int i = 0;
    while (i < this.mSelectorIndices.length)
    {
      int k = m + (i - 1);
      int j = k;
      if (this.mWrapSelectorWheel)
        j = getWrappedSelectorIndex(k);
      arrayOfInt[i] = j;
      ensureCachedScrollSelectorValue(arrayOfInt[i]);
      i += 1;
    }
  }

  private int makeMeasureSpec(int paramInt1, int paramInt2)
  {
    if (paramInt2 == -1)
      return paramInt1;
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt1);
    switch (j)
    {
    case 1073741824:
    default:
      throw new IllegalArgumentException("Unknown measure mode: " + j);
    case -2147483648:
      return View.MeasureSpec.makeMeasureSpec(Math.min(i, paramInt2), 1073741824);
    case 0:
    }
    return View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
  }

  private boolean moveToFinalScrollerPosition(Scroller paramScroller)
  {
    paramScroller.forceFinished(true);
    int k = paramScroller.getFinalY() - paramScroller.getCurrY();
    int i = this.mCurrentScrollOffset;
    int j = this.mSelectorElementHeight;
    j = this.mInitialScrollOffset - (i + k) % j;
    if (j != 0)
    {
      i = j;
      if (Math.abs(j) > this.mSelectorElementHeight / 2)
        if (j <= 0)
          break label79;
      label79: for (i = j - this.mSelectorElementHeight; ; i = j + this.mSelectorElementHeight)
      {
        scrollBy(0, k + i);
        return true;
      }
    }
    return false;
  }

  private void notifyChange(int paramInt1, int paramInt2)
  {
    if (this.mOnValueChangeListener != null)
      this.mOnValueChangeListener.onValueChange(this, paramInt1, this.mValue);
  }

  private void onScrollStateChange(int paramInt)
  {
    if (this.mScrollState == paramInt);
    do
    {
      return;
      this.mScrollState = paramInt;
    }
    while (this.mOnScrollListener == null);
    this.mOnScrollListener.onScrollStateChange(this, paramInt);
  }

  private void onScrollerFinished(Scroller paramScroller)
  {
    if (paramScroller == this.mFlingScroller)
    {
      if (!ensureScrollWheelAdjusted())
        updateInputTextView();
      onScrollStateChange(0);
    }
    do
      return;
    while (this.mScrollState == 1);
    updateInputTextView();
  }

  private void postBeginSoftInputOnLongPressCommand()
  {
    if (this.mBeginSoftInputOnLongPressCommand == null)
      this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
    while (true)
    {
      postDelayed(this.mBeginSoftInputOnLongPressCommand, ViewConfiguration.getLongPressTimeout());
      return;
      removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
    }
  }

  private void postChangeCurrentByOneFromLongPress(boolean paramBoolean, long paramLong)
  {
    if (this.mChangeCurrentByOneFromLongPressCommand == null)
      this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
    while (true)
    {
      this.mChangeCurrentByOneFromLongPressCommand.setStep(paramBoolean);
      postDelayed(this.mChangeCurrentByOneFromLongPressCommand, paramLong);
      return;
      removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
    }
  }

  private void postSetSelectionCommand(int paramInt1, int paramInt2)
  {
    if (this.mSetSelectionCommand == null)
      this.mSetSelectionCommand = new SetSelectionCommand();
    while (true)
    {
      SetSelectionCommand.access$602(this.mSetSelectionCommand, paramInt1);
      SetSelectionCommand.access$702(this.mSetSelectionCommand, paramInt2);
      post(this.mSetSelectionCommand);
      return;
      removeCallbacks(this.mSetSelectionCommand);
    }
  }

  private void removeAllCallbacks()
  {
    if (this.mChangeCurrentByOneFromLongPressCommand != null)
      removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
    if (this.mSetSelectionCommand != null)
      removeCallbacks(this.mSetSelectionCommand);
    if (this.mBeginSoftInputOnLongPressCommand != null)
      removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
    this.mPressedStateHelper.cancel();
  }

  private void removeBeginSoftInputCommand()
  {
    if (this.mBeginSoftInputOnLongPressCommand != null)
      removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
  }

  private void removeChangeCurrentByOneFromLongPress()
  {
    if (this.mChangeCurrentByOneFromLongPressCommand != null)
      removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
  }

  public static int resolveSizeAndState(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1;
    int j = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    switch (j)
    {
    default:
      paramInt1 = i;
    case 0:
    case -2147483648:
    case 1073741824:
    }
    while (true)
    {
      return 0xFF000000 & paramInt3 | paramInt1;
      continue;
      if (paramInt2 < paramInt1)
      {
        paramInt1 = paramInt2 | 0x1000000;
        continue;
      }
      continue;
      paramInt1 = paramInt2;
    }
  }

  @TargetApi(11)
  private int resolveSizeAndStateRespectingMinSize(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt2;
    if (paramInt1 != -1)
      i = resolveSizeAndState(Math.max(paramInt1, paramInt2), paramInt3, 0);
    return i;
  }

  private void setValueInternal(int paramInt, boolean paramBoolean)
  {
    if (this.mValue == paramInt)
      return;
    if (this.mWrapSelectorWheel);
    for (paramInt = getWrappedSelectorIndex(paramInt); ; paramInt = Math.min(Math.max(paramInt, this.mMinValue), this.mMaxValue))
    {
      int i = this.mValue;
      this.mValue = paramInt;
      updateInputTextView();
      if (paramBoolean)
        notifyChange(i, paramInt);
      initializeSelectorWheelIndices();
      invalidate();
      return;
    }
  }

  private void showSoftInput()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if (localInputMethodManager != null)
    {
      if (this.mHasSelectorWheel)
        this.mInputText.setVisibility(0);
      this.mInputText.requestFocus();
      localInputMethodManager.showSoftInput(this.mInputText, 0);
    }
  }

  private void tryComputeMaxWidth()
  {
    if (!this.mComputeMaxWidth);
    int i;
    float f1;
    int j;
    int k;
    while (true)
    {
      return;
      i = 0;
      if (this.mDisplayedValues != null)
        break;
      f1 = 0.0F;
      i = 0;
      while (i <= 9)
      {
        float f3 = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(i));
        float f2 = f1;
        if (f3 > f1)
          f2 = f3;
        i += 1;
        f1 = f2;
      }
      j = 0;
      i = this.mMaxValue;
      while (i > 0)
      {
        j += 1;
        i /= 10;
      }
      k = (int)(j * f1);
      i = k + (this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight());
      if (this.mMaxWidth == i)
        continue;
      if (i <= this.mMinWidth)
        break label216;
    }
    label216: for (this.mMaxWidth = i; ; this.mMaxWidth = this.mMinWidth)
    {
      invalidate();
      return;
      int m = this.mDisplayedValues.length;
      j = 0;
      while (true)
      {
        k = i;
        if (j >= m)
          break;
        f1 = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[j]);
        k = i;
        if (f1 > i)
          k = (int)f1;
        j += 1;
        i = k;
      }
    }
  }

  private boolean updateInputTextView()
  {
    if (this.mDisplayedValues == null);
    for (String str = formatNumber(this.mValue); (!TextUtils.isEmpty(str)) && (!str.equals(this.mInputText.getText().toString())); str = this.mDisplayedValues[(this.mValue - this.mMinValue)])
    {
      this.mInputText.setText(str);
      return true;
    }
    return false;
  }

  public void computeScroll()
  {
    Scroller localScroller2 = this.mFlingScroller;
    Scroller localScroller1 = localScroller2;
    if (localScroller2.isFinished())
    {
      localScroller2 = this.mAdjustScroller;
      localScroller1 = localScroller2;
      if (localScroller2.isFinished())
        return;
    }
    localScroller1.computeScrollOffset();
    int i = localScroller1.getCurrY();
    if (this.mPreviousScrollerY == 0)
      this.mPreviousScrollerY = localScroller1.getStartY();
    scrollBy(0, i - this.mPreviousScrollerY);
    this.mPreviousScrollerY = i;
    if (localScroller1.isFinished())
    {
      onScrollerFinished(localScroller1);
      return;
    }
    invalidate();
  }

  @TargetApi(16)
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    if (!this.mHasSelectorWheel)
      return super.dispatchHoverEvent(paramMotionEvent);
    int i;
    if (((AccessibilityManager)getContext().getSystemService("accessibility")).isEnabled())
    {
      i = (int)paramMotionEvent.getY();
      if (i >= this.mTopSelectionDividerTop)
        break label94;
      i = 3;
      int j = paramMotionEvent.getAction();
      paramMotionEvent = getSupportAccessibilityNodeProvider();
      switch (j & 0xFF)
      {
      case 8:
      default:
      case 9:
      case 7:
      case 10:
      }
    }
    while (true)
    {
      return false;
      label94: if (i > this.mBottomSelectionDividerBottom)
      {
        i = 1;
        break;
      }
      i = 2;
      break;
      paramMotionEvent.sendAccessibilityEventForVirtualView(i, 128);
      this.mLastHoveredChildVirtualViewId = i;
      paramMotionEvent.performAction(i, 64, null);
      continue;
      if ((this.mLastHoveredChildVirtualViewId == i) || (this.mLastHoveredChildVirtualViewId == -1))
        continue;
      paramMotionEvent.sendAccessibilityEventForVirtualView(this.mLastHoveredChildVirtualViewId, 256);
      paramMotionEvent.sendAccessibilityEventForVirtualView(i, 128);
      this.mLastHoveredChildVirtualViewId = i;
      paramMotionEvent.performAction(i, 64, null);
      continue;
      paramMotionEvent.sendAccessibilityEventForVirtualView(i, 256);
      this.mLastHoveredChildVirtualViewId = -1;
    }
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    int i = paramKeyEvent.getKeyCode();
    switch (i)
    {
    default:
    case 23:
    case 66:
    case 19:
    case 20:
    }
    label127: 
    do
    {
      bool = super.dispatchKeyEvent(paramKeyEvent);
      do
      {
        return bool;
        removeAllCallbacks();
        break;
        if (!this.mHasSelectorWheel)
          break;
        switch (paramKeyEvent.getAction())
        {
        default:
          break;
        case 0:
          if ((!this.mWrapSelectorWheel) && (i != 20))
            break label166;
          if (getValue() >= getMaxValue())
            break;
          requestFocus();
          this.mLastHandledDownDpadKeyCode = i;
          removeAllCallbacks();
        case 1:
        }
      }
      while (!this.mFlingScroller.isFinished());
      if (i == 20);
      for (bool = true; ; bool = false)
      {
        changeValueByOne(bool);
        return true;
        if (getValue() <= getMinValue())
          break;
        break label127;
      }
    }
    while (this.mLastHandledDownDpadKeyCode != i);
    label166: this.mLastHandledDownDpadKeyCode = -1;
    return true;
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 2:
    default:
    case 1:
    case 3:
    }
    while (true)
    {
      return super.dispatchTouchEvent(paramMotionEvent);
      removeAllCallbacks();
    }
  }

  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 2:
    default:
    case 1:
    case 3:
    }
    while (true)
    {
      return super.dispatchTrackballEvent(paramMotionEvent);
      removeAllCallbacks();
    }
  }

  @TargetApi(16)
  public AccessibilityNodeProvider getAccessibilityNodeProvider()
  {
    if (!this.mHasSelectorWheel)
      return super.getAccessibilityNodeProvider();
    if (this.mAccessibilityNodeProvider == null)
      this.mAccessibilityNodeProvider = new SupportAccessibilityNodeProvider(null);
    return this.mAccessibilityNodeProvider.mProvider;
  }

  protected float getBottomFadingEdgeStrength()
  {
    return 0.9F;
  }

  public String[] getDisplayedValues()
  {
    return this.mDisplayedValues;
  }

  public int getMaxValue()
  {
    return this.mMaxValue;
  }

  public int getMinValue()
  {
    return this.mMinValue;
  }

  public int getSolidColor()
  {
    return this.mSolidColor;
  }

  protected float getTopFadingEdgeStrength()
  {
    return 0.9F;
  }

  public int getValue()
  {
    return this.mValue;
  }

  public boolean getWrapSelectorWheel()
  {
    return this.mWrapSelectorWheel;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeAllCallbacks();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (!this.mHasSelectorWheel)
      super.onDraw(paramCanvas);
    do
    {
      return;
      float f2 = (getRight() - getLeft()) / 2.0F;
      float f1 = this.mCurrentScrollOffset;
      if ((this.mVirtualButtonPressedDrawable != null) && (this.mScrollState == 0))
      {
        if (this.mDecrementVirtualButtonPressed)
        {
          this.mVirtualButtonPressedDrawable.setState(PRESSED_ENABLED_STATE_SET);
          this.mVirtualButtonPressedDrawable.setBounds(0, 0, getRight(), this.mTopSelectionDividerTop);
          this.mVirtualButtonPressedDrawable.draw(paramCanvas);
        }
        if (this.mIncrementVirtualButtonPressed)
        {
          this.mVirtualButtonPressedDrawable.setState(PRESSED_ENABLED_STATE_SET);
          this.mVirtualButtonPressedDrawable.setBounds(0, this.mBottomSelectionDividerBottom, getRight(), getBottom());
          this.mVirtualButtonPressedDrawable.draw(paramCanvas);
        }
      }
      int[] arrayOfInt = this.mSelectorIndices;
      i = 0;
      while (i < arrayOfInt.length)
      {
        j = arrayOfInt[i];
        String str = (String)this.mSelectorIndexToStringCache.get(j);
        if ((i != 1) || (this.mInputText.getVisibility() != 0))
          paramCanvas.drawText(str, f2, f1, this.mSelectorWheelPaint);
        f1 += this.mSelectorElementHeight;
        i += 1;
      }
    }
    while (this.mSelectionDivider == null);
    int i = this.mTopSelectionDividerTop;
    int j = this.mSelectionDividerHeight;
    this.mSelectionDivider.setBounds(0, i, getRight(), i + j);
    this.mSelectionDivider.draw(paramCanvas);
    i = this.mBottomSelectionDividerBottom;
    j = this.mSelectionDividerHeight;
    this.mSelectionDivider.setBounds(0, i - j, getRight(), i);
    this.mSelectionDivider.draw(paramCanvas);
  }

  @TargetApi(15)
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(NumberPicker.class.getName());
    paramAccessibilityEvent.setScrollable(true);
    paramAccessibilityEvent.setScrollY((this.mMinValue + this.mValue) * this.mSelectorElementHeight);
    paramAccessibilityEvent.setMaxScrollY((this.mMaxValue - this.mMinValue) * this.mSelectorElementHeight);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((!this.mHasSelectorWheel) || (!isEnabled()))
      return false;
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    default:
      return false;
    case 0:
    }
    removeAllCallbacks();
    this.mInputText.setVisibility(4);
    float f = paramMotionEvent.getY();
    this.mLastDownEventY = f;
    this.mLastDownOrMoveEventY = f;
    this.mLastDownEventTime = paramMotionEvent.getEventTime();
    this.mIngonreMoveEvents = false;
    this.mShowSoftInputOnTap = false;
    if (this.mLastDownEventY < this.mTopSelectionDividerTop)
      if (this.mScrollState == 0)
        this.mPressedStateHelper.buttonPressDelayed(2);
    while (true)
    {
      getParent().requestDisallowInterceptTouchEvent(true);
      if (this.mFlingScroller.isFinished())
        break;
      this.mFlingScroller.forceFinished(true);
      this.mAdjustScroller.forceFinished(true);
      onScrollStateChange(0);
      return true;
      if ((this.mLastDownEventY <= this.mBottomSelectionDividerBottom) || (this.mScrollState != 0))
        continue;
      this.mPressedStateHelper.buttonPressDelayed(1);
    }
    if (!this.mAdjustScroller.isFinished())
    {
      this.mFlingScroller.forceFinished(true);
      this.mAdjustScroller.forceFinished(true);
      return true;
    }
    if (this.mLastDownEventY < this.mTopSelectionDividerTop)
    {
      hideSoftInput();
      postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
      return true;
    }
    if (this.mLastDownEventY > this.mBottomSelectionDividerBottom)
    {
      hideSoftInput();
      postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
      return true;
    }
    this.mShowSoftInputOnTap = true;
    postBeginSoftInputOnLongPressCommand();
    return true;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!this.mHasSelectorWheel)
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    do
    {
      return;
      paramInt4 = getMeasuredWidth();
      paramInt3 = getMeasuredHeight();
      paramInt1 = this.mInputText.getMeasuredWidth();
      paramInt2 = this.mInputText.getMeasuredHeight();
      paramInt4 = (paramInt4 - paramInt1) / 2;
      paramInt3 = (paramInt3 - paramInt2) / 2;
      this.mInputText.layout(paramInt4, paramInt3, paramInt4 + paramInt1, paramInt3 + paramInt2);
    }
    while (!paramBoolean);
    initializeSelectorWheel();
    initializeFadingEdges();
    this.mTopSelectionDividerTop = ((getHeight() - this.mSelectionDividersDistance) / 2 - this.mSelectionDividerHeight);
    this.mBottomSelectionDividerBottom = (this.mTopSelectionDividerTop + this.mSelectionDividerHeight * 2 + this.mSelectionDividersDistance);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!this.mHasSelectorWheel)
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    super.onMeasure(makeMeasureSpec(paramInt1, this.mMaxWidth), makeMeasureSpec(paramInt2, this.mMaxHeight));
    setMeasuredDimension(resolveSizeAndStateRespectingMinSize(this.mMinWidth, getMeasuredWidth(), paramInt1), resolveSizeAndStateRespectingMinSize(this.mMinHeight, getMeasuredHeight(), paramInt2));
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int j = 1;
    if ((!isEnabled()) || (!this.mHasSelectorWheel))
      j = 0;
    while (true)
    {
      return j;
      if (this.mVelocityTracker == null)
        this.mVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker.addMovement(paramMotionEvent);
      switch (paramMotionEvent.getAction() & 0xFF)
      {
      default:
        return true;
      case 1:
        removeBeginSoftInputCommand();
        removeChangeCurrentByOneFromLongPress();
        this.mPressedStateHelper.cancel();
        VelocityTracker localVelocityTracker = this.mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
        i = (int)localVelocityTracker.getYVelocity();
        if (Math.abs(i) <= this.mMinimumFlingVelocity)
          break;
        fling(i);
        onScrollStateChange(2);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        return true;
      case 2:
        if (this.mIngonreMoveEvents)
          continue;
        float f = paramMotionEvent.getY();
        if (this.mScrollState != 1)
          if ((int)Math.abs(f - this.mLastDownEventY) > this.mTouchSlop)
          {
            removeAllCallbacks();
            onScrollStateChange(1);
          }
        while (true)
        {
          this.mLastDownOrMoveEventY = f;
          return true;
          scrollBy(0, (int)(f - this.mLastDownOrMoveEventY));
          invalidate();
        }
      }
    }
    int i = (int)paramMotionEvent.getY();
    if ((int)Math.abs(i - this.mLastDownEventY) <= this.mTouchSlop)
      if (this.mShowSoftInputOnTap)
      {
        this.mShowSoftInputOnTap = false;
        showSoftInput();
      }
    while (true)
    {
      onScrollStateChange(0);
      break;
      i = i / this.mSelectorElementHeight - 1;
      if (i > 0)
      {
        changeValueByOne(true);
        this.mPressedStateHelper.buttonTapped(1);
        continue;
      }
      if (i >= 0)
        continue;
      changeValueByOne(false);
      this.mPressedStateHelper.buttonTapped(2);
      continue;
      ensureScrollWheelAdjusted();
    }
  }

  public void scrollBy(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = this.mSelectorIndices;
    if ((!this.mWrapSelectorWheel) && (paramInt2 > 0) && (arrayOfInt[1] <= this.mMinValue))
      this.mCurrentScrollOffset = this.mInitialScrollOffset;
    while (true)
    {
      return;
      if ((!this.mWrapSelectorWheel) && (paramInt2 < 0) && (arrayOfInt[1] >= this.mMaxValue))
      {
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        return;
      }
      this.mCurrentScrollOffset += paramInt2;
      while (this.mCurrentScrollOffset - this.mInitialScrollOffset > this.mSelectorTextGapHeight)
      {
        this.mCurrentScrollOffset -= this.mSelectorElementHeight;
        decrementSelectorIndices(arrayOfInt);
        setValueInternal(arrayOfInt[1], true);
        if ((this.mWrapSelectorWheel) || (arrayOfInt[1] > this.mMinValue))
          continue;
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
      }
      while (this.mCurrentScrollOffset - this.mInitialScrollOffset < -this.mSelectorTextGapHeight)
      {
        this.mCurrentScrollOffset += this.mSelectorElementHeight;
        incrementSelectorIndices(arrayOfInt);
        setValueInternal(arrayOfInt[1], true);
        if ((this.mWrapSelectorWheel) || (arrayOfInt[1] < this.mMaxValue))
          continue;
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
      }
    }
  }

  public void setDisplayedValues(String[] paramArrayOfString)
  {
    if (this.mDisplayedValues == paramArrayOfString)
      return;
    this.mDisplayedValues = paramArrayOfString;
    if (this.mDisplayedValues != null)
      this.mInputText.setRawInputType(524289);
    while (true)
    {
      updateInputTextView();
      initializeSelectorWheelIndices();
      tryComputeMaxWidth();
      return;
      this.mInputText.setRawInputType(2);
    }
  }

  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (!this.mHasSelectorWheel)
      this.mIncrementButton.setEnabled(paramBoolean);
    if (!this.mHasSelectorWheel)
      this.mDecrementButton.setEnabled(paramBoolean);
    this.mInputText.setEnabled(paramBoolean);
  }

  public void setFormatter(Formatter paramFormatter)
  {
    if (paramFormatter == this.mFormatter)
      return;
    this.mFormatter = paramFormatter;
    initializeSelectorWheelIndices();
    updateInputTextView();
  }

  public void setInputEnabled(boolean paramBoolean)
  {
    if (this.mInputText != null)
    {
      this.mInputText.clearFocus();
      this.mInputText.setFocusable(paramBoolean);
      this.mInputText.setFocusableInTouchMode(paramBoolean);
    }
  }

  public void setInputFilterEnabled(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mInputText.setFilters(new InputFilter[] { new InputTextFilter() });
      return;
    }
    this.mInputText.setFilters(new InputFilter[0]);
  }

  public void setMaxValue(int paramInt)
  {
    if (this.mMaxValue == paramInt)
      return;
    if (paramInt < 0)
      throw new IllegalArgumentException("maxValue must be >= 0");
    this.mMaxValue = paramInt;
    if (this.mMaxValue < this.mValue)
      this.mValue = this.mMaxValue;
    if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
    for (boolean bool = true; ; bool = false)
    {
      setWrapSelectorWheel(bool);
      initializeSelectorWheelIndices();
      updateInputTextView();
      tryComputeMaxWidth();
      invalidate();
      return;
    }
  }

  public void setMinValue(int paramInt)
  {
    if (this.mMinValue == paramInt)
      return;
    if (paramInt < 0)
      throw new IllegalArgumentException("minValue must be >= 0");
    this.mMinValue = paramInt;
    if (this.mMinValue > this.mValue)
      this.mValue = this.mMinValue;
    if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length);
    for (boolean bool = true; ; bool = false)
    {
      setWrapSelectorWheel(bool);
      initializeSelectorWheelIndices();
      updateInputTextView();
      tryComputeMaxWidth();
      invalidate();
      return;
    }
  }

  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    this.mOnScrollListener = paramOnScrollListener;
  }

  public void setOnValueChangedListener(OnValueChangeListener paramOnValueChangeListener)
  {
    this.mOnValueChangeListener = paramOnValueChangeListener;
  }

  public void setValue(int paramInt)
  {
    setValueInternal(paramInt, false);
  }

  public void setWrapSelectorWheel(boolean paramBoolean)
  {
    if (this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length);
    for (int i = 1; ; i = 0)
    {
      if (((!paramBoolean) || (i != 0)) && (paramBoolean != this.mWrapSelectorWheel))
        this.mWrapSelectorWheel = paramBoolean;
      return;
    }
  }

  @TargetApi(16)
  class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider
  {
    private static final int UNDEFINED = -2147483648;
    private static final int VIRTUAL_VIEW_ID_DECREMENT = 3;
    private static final int VIRTUAL_VIEW_ID_INCREMENT = 1;
    private static final int VIRTUAL_VIEW_ID_INPUT = 2;
    private int mAccessibilityFocusedView = -2147483648;
    private final int[] mTempArray = new int[2];
    private final Rect mTempRect = new Rect();

    AccessibilityNodeProviderImpl()
    {
    }

    private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
      localAccessibilityNodeInfo.setClassName(NumberPicker.class.getName());
      localAccessibilityNodeInfo.setPackageName(NumberPicker.this.getContext().getPackageName());
      localAccessibilityNodeInfo.setSource(NumberPicker.this);
      if (hasVirtualDecrementButton())
        localAccessibilityNodeInfo.addChild(NumberPicker.this, 3);
      localAccessibilityNodeInfo.addChild(NumberPicker.this, 2);
      if (hasVirtualIncrementButton())
        localAccessibilityNodeInfo.addChild(NumberPicker.this, 1);
      localAccessibilityNodeInfo.setParent((View)NumberPicker.this.getParentForAccessibility());
      localAccessibilityNodeInfo.setEnabled(NumberPicker.this.isEnabled());
      localAccessibilityNodeInfo.setScrollable(true);
      if (this.mAccessibilityFocusedView != -1)
        localAccessibilityNodeInfo.addAction(64);
      if (this.mAccessibilityFocusedView == -1)
        localAccessibilityNodeInfo.addAction(128);
      if (NumberPicker.this.isEnabled())
      {
        if ((NumberPicker.this.getWrapSelectorWheel()) || (NumberPicker.this.getValue() < NumberPicker.this.getMaxValue()))
          localAccessibilityNodeInfo.addAction(4096);
        if ((NumberPicker.this.getWrapSelectorWheel()) || (NumberPicker.this.getValue() > NumberPicker.this.getMinValue()))
          localAccessibilityNodeInfo.addAction(8192);
      }
      return localAccessibilityNodeInfo;
    }

    private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
      localAccessibilityNodeInfo.setClassName(Button.class.getName());
      localAccessibilityNodeInfo.setPackageName(NumberPicker.this.getContext().getPackageName());
      localAccessibilityNodeInfo.setSource(NumberPicker.this, paramInt1);
      localAccessibilityNodeInfo.setParent(NumberPicker.this);
      localAccessibilityNodeInfo.setText(paramString);
      localAccessibilityNodeInfo.setClickable(true);
      localAccessibilityNodeInfo.setLongClickable(true);
      localAccessibilityNodeInfo.setEnabled(NumberPicker.this.isEnabled());
      paramString = this.mTempRect;
      paramString.set(paramInt2, paramInt3, paramInt4, paramInt5);
      localAccessibilityNodeInfo.setBoundsInParent(paramString);
      int[] arrayOfInt = this.mTempArray;
      NumberPicker.this.getLocationOnScreen(arrayOfInt);
      paramString.offset(arrayOfInt[0], arrayOfInt[1]);
      localAccessibilityNodeInfo.setBoundsInScreen(paramString);
      if (this.mAccessibilityFocusedView != paramInt1)
        localAccessibilityNodeInfo.addAction(64);
      if (this.mAccessibilityFocusedView == paramInt1)
        localAccessibilityNodeInfo.addAction(128);
      if (NumberPicker.this.isEnabled())
        localAccessibilityNodeInfo.addAction(16);
      return localAccessibilityNodeInfo;
    }

    private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText()
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = NumberPicker.this.mInputText.createAccessibilityNodeInfo();
      localAccessibilityNodeInfo.setSource(NumberPicker.this, 2);
      if (this.mAccessibilityFocusedView != 2)
        localAccessibilityNodeInfo.addAction(64);
      if (this.mAccessibilityFocusedView == 2)
        localAccessibilityNodeInfo.addAction(128);
      return localAccessibilityNodeInfo;
    }

    private void findAccessibilityNodeInfosByTextInChild(String paramString, int paramInt, List<AccessibilityNodeInfo> paramList)
    {
      switch (paramInt)
      {
      default:
      case 3:
      case 2:
      case 1:
      }
      Object localObject;
      do
      {
        do
        {
          do
          {
            return;
            localObject = getVirtualDecrementButtonText();
          }
          while ((TextUtils.isEmpty((CharSequence)localObject)) || (!((String)localObject).toString().toLowerCase().contains(paramString)));
          paramList.add(createAccessibilityNodeInfo(3));
          return;
          localObject = NumberPicker.this.mInputText.getText();
          if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((CharSequence)localObject).toString().toLowerCase().contains(paramString)))
          {
            paramList.add(createAccessibilityNodeInfo(2));
            return;
          }
          localObject = NumberPicker.this.mInputText.getText();
        }
        while ((TextUtils.isEmpty((CharSequence)localObject)) || (!((CharSequence)localObject).toString().toLowerCase().contains(paramString)));
        paramList.add(createAccessibilityNodeInfo(2));
        return;
        localObject = getVirtualIncrementButtonText();
      }
      while ((TextUtils.isEmpty((CharSequence)localObject)) || (!((String)localObject).toString().toLowerCase().contains(paramString)));
      paramList.add(createAccessibilityNodeInfo(1));
    }

    private String getVirtualDecrementButtonText()
    {
      int j = NumberPicker.this.mValue - 1;
      int i = j;
      if (NumberPicker.this.mWrapSelectorWheel)
        i = NumberPicker.this.getWrappedSelectorIndex(j);
      if (i >= NumberPicker.this.mMinValue)
      {
        if (NumberPicker.this.mDisplayedValues == null)
          return NumberPicker.this.formatNumber(i);
        return NumberPicker.this.mDisplayedValues[(i - NumberPicker.this.mMinValue)];
      }
      return null;
    }

    private String getVirtualIncrementButtonText()
    {
      int j = NumberPicker.this.mValue + 1;
      int i = j;
      if (NumberPicker.this.mWrapSelectorWheel)
        i = NumberPicker.this.getWrappedSelectorIndex(j);
      if (i <= NumberPicker.this.mMaxValue)
      {
        if (NumberPicker.this.mDisplayedValues == null)
          return NumberPicker.this.formatNumber(i);
        return NumberPicker.this.mDisplayedValues[(i - NumberPicker.this.mMinValue)];
      }
      return null;
    }

    private boolean hasVirtualDecrementButton()
    {
      return (NumberPicker.this.getWrapSelectorWheel()) || (NumberPicker.this.getValue() > NumberPicker.this.getMinValue());
    }

    private boolean hasVirtualIncrementButton()
    {
      return (NumberPicker.this.getWrapSelectorWheel()) || (NumberPicker.this.getValue() < NumberPicker.this.getMaxValue());
    }

    private void sendAccessibilityEventForVirtualButton(int paramInt1, int paramInt2, String paramString)
    {
      if (((AccessibilityManager)NumberPicker.this.getContext().getSystemService("accessibility")).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt2);
        localAccessibilityEvent.setClassName(Button.class.getName());
        localAccessibilityEvent.setPackageName(NumberPicker.this.getContext().getPackageName());
        localAccessibilityEvent.getText().add(paramString);
        localAccessibilityEvent.setEnabled(NumberPicker.this.isEnabled());
        localAccessibilityEvent.setSource(NumberPicker.this, paramInt1);
        NumberPicker.this.requestSendAccessibilityEvent(NumberPicker.this, localAccessibilityEvent);
      }
    }

    private void sendAccessibilityEventForVirtualText(int paramInt)
    {
      if (((AccessibilityManager)NumberPicker.this.getContext().getSystemService("accessibility")).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt);
        NumberPicker.this.mInputText.onInitializeAccessibilityEvent(localAccessibilityEvent);
        NumberPicker.this.mInputText.onPopulateAccessibilityEvent(localAccessibilityEvent);
        localAccessibilityEvent.setSource(NumberPicker.this, 2);
        NumberPicker.this.requestSendAccessibilityEvent(NumberPicker.this, localAccessibilityEvent);
      }
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfo(int paramInt)
    {
      switch (paramInt)
      {
      case 0:
      default:
        return super.createAccessibilityNodeInfo(paramInt);
      case -1:
        return createAccessibilityNodeInfoForNumberPicker(NumberPicker.this.getScrollX(), NumberPicker.this.getScrollY(), NumberPicker.this.getScrollX() + (NumberPicker.this.getRight() - NumberPicker.this.getLeft()), NumberPicker.this.getScrollY() + (NumberPicker.this.getBottom() - NumberPicker.this.getTop()));
      case 3:
        str = getVirtualDecrementButtonText();
        paramInt = NumberPicker.this.getScrollX();
        i = NumberPicker.this.getScrollY();
        j = NumberPicker.this.getScrollX();
        k = NumberPicker.this.getRight();
        m = NumberPicker.this.getLeft();
        n = NumberPicker.this.mTopSelectionDividerTop;
        return createAccessibilityNodeInfoForVirtualButton(3, str, paramInt, i, k - m + j, NumberPicker.this.mSelectionDividerHeight + n);
      case 2:
        return createAccessibiltyNodeInfoForInputText();
      case 1:
      }
      String str = getVirtualIncrementButtonText();
      paramInt = NumberPicker.this.getScrollX();
      int i = NumberPicker.this.mBottomSelectionDividerBottom;
      int j = NumberPicker.this.mSelectionDividerHeight;
      int k = NumberPicker.this.getScrollX();
      int m = NumberPicker.this.getRight();
      int n = NumberPicker.this.getLeft();
      int i1 = NumberPicker.this.getScrollY();
      return createAccessibilityNodeInfoForVirtualButton(1, str, paramInt, i - j, m - n + k, NumberPicker.this.getBottom() - NumberPicker.this.getTop() + i1);
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String paramString, int paramInt)
    {
      if (TextUtils.isEmpty(paramString))
        return Collections.emptyList();
      String str = paramString.toLowerCase();
      ArrayList localArrayList = new ArrayList();
      switch (paramInt)
      {
      case 0:
      default:
        return super.findAccessibilityNodeInfosByText(paramString, paramInt);
      case -1:
        findAccessibilityNodeInfosByTextInChild(str, 3, localArrayList);
        findAccessibilityNodeInfosByTextInChild(str, 2, localArrayList);
        findAccessibilityNodeInfosByTextInChild(str, 1, localArrayList);
        return localArrayList;
      case 1:
      case 2:
      case 3:
      }
      findAccessibilityNodeInfosByTextInChild(str, paramInt, localArrayList);
      return localArrayList;
    }

    public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      boolean bool3 = false;
      boolean bool2 = false;
      boolean bool1;
      switch (paramInt1)
      {
      case 0:
      default:
        bool1 = super.performAction(paramInt1, paramInt2, paramBundle);
      case -1:
      case 2:
      case 1:
      case 3:
      }
      label206: 
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                do
                {
                  do
                  {
                    do
                    {
                      do
                      {
                        do
                        {
                          do
                          {
                            do
                            {
                              do
                              {
                                do
                                {
                                  do
                                  {
                                    do
                                    {
                                      do
                                      {
                                        do
                                        {
                                          do
                                          {
                                            return bool1;
                                            switch (paramInt2)
                                            {
                                            default:
                                              break;
                                            case 64:
                                              bool1 = bool2;
                                            case 128:
                                            case 4096:
                                            case 8192:
                                            }
                                          }
                                          while (this.mAccessibilityFocusedView == paramInt1);
                                          this.mAccessibilityFocusedView = paramInt1;
                                          NumberPicker.this.performAccessibilityAction(64, null);
                                          return true;
                                          bool1 = bool2;
                                        }
                                        while (this.mAccessibilityFocusedView != paramInt1);
                                        this.mAccessibilityFocusedView = -2147483648;
                                        NumberPicker.this.performAccessibilityAction(128, null);
                                        return true;
                                        bool1 = bool2;
                                      }
                                      while (!NumberPicker.this.isEnabled());
                                      if (NumberPicker.this.getWrapSelectorWheel())
                                        break label206;
                                      bool1 = bool2;
                                    }
                                    while (NumberPicker.this.getValue() >= NumberPicker.this.getMaxValue());
                                    NumberPicker.this.changeValueByOne(true);
                                    return true;
                                    bool1 = bool2;
                                  }
                                  while (!NumberPicker.this.isEnabled());
                                  if (NumberPicker.this.getWrapSelectorWheel())
                                    break label261;
                                  bool1 = bool2;
                                }
                                while (NumberPicker.this.getValue() <= NumberPicker.this.getMinValue());
                                NumberPicker.this.changeValueByOne(false);
                                return true;
                                switch (paramInt2)
                                {
                                default:
                                  return NumberPicker.this.mInputText.performAccessibilityAction(paramInt2, paramBundle);
                                case 1:
                                  bool1 = bool2;
                                case 2:
                                case 16:
                                case 64:
                                case 128:
                                }
                              }
                              while (!NumberPicker.this.isEnabled());
                              bool1 = bool2;
                            }
                            while (NumberPicker.this.mInputText.isFocused());
                            return NumberPicker.this.mInputText.requestFocus();
                            bool1 = bool2;
                          }
                          while (!NumberPicker.this.isEnabled());
                          bool1 = bool2;
                        }
                        while (!NumberPicker.this.mInputText.isFocused());
                        NumberPicker.this.mInputText.clearFocus();
                        return true;
                        bool1 = bool2;
                      }
                      while (!NumberPicker.this.isEnabled());
                      NumberPicker.this.showSoftInput();
                      return true;
                      bool1 = bool2;
                    }
                    while (this.mAccessibilityFocusedView == paramInt1);
                    this.mAccessibilityFocusedView = paramInt1;
                    sendAccessibilityEventForVirtualView(paramInt1, 32768);
                    NumberPicker.this.mInputText.invalidate();
                    return true;
                    bool1 = bool2;
                  }
                  while (this.mAccessibilityFocusedView != paramInt1);
                  this.mAccessibilityFocusedView = -2147483648;
                  sendAccessibilityEventForVirtualView(paramInt1, 65536);
                  NumberPicker.this.mInputText.invalidate();
                  return true;
                  switch (paramInt2)
                  {
                  default:
                    return false;
                  case 16:
                    bool1 = bool2;
                  case 64:
                  case 128:
                  }
                }
                while (!NumberPicker.this.isEnabled());
                NumberPicker.this.changeValueByOne(true);
                sendAccessibilityEventForVirtualView(paramInt1, 1);
                return true;
                bool1 = bool2;
              }
              while (this.mAccessibilityFocusedView == paramInt1);
              this.mAccessibilityFocusedView = paramInt1;
              sendAccessibilityEventForVirtualView(paramInt1, 32768);
              NumberPicker.this.invalidate(0, NumberPicker.this.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
              return true;
              bool1 = bool2;
            }
            while (this.mAccessibilityFocusedView != paramInt1);
            this.mAccessibilityFocusedView = -2147483648;
            sendAccessibilityEventForVirtualView(paramInt1, 65536);
            NumberPicker.this.invalidate(0, NumberPicker.this.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
            return true;
            switch (paramInt2)
            {
            default:
              return false;
            case 16:
              bool1 = bool2;
            case 64:
            case 128:
            }
          }
          while (!NumberPicker.this.isEnabled());
          bool1 = bool3;
          if (paramInt1 == 1)
            bool1 = true;
          NumberPicker.this.changeValueByOne(bool1);
          sendAccessibilityEventForVirtualView(paramInt1, 1);
          return true;
          bool1 = bool2;
        }
        while (this.mAccessibilityFocusedView == paramInt1);
        this.mAccessibilityFocusedView = paramInt1;
        sendAccessibilityEventForVirtualView(paramInt1, 32768);
        NumberPicker.this.invalidate(0, 0, NumberPicker.this.getRight(), NumberPicker.this.mTopSelectionDividerTop);
        return true;
        bool1 = bool2;
      }
      while (this.mAccessibilityFocusedView != paramInt1);
      label261: this.mAccessibilityFocusedView = -2147483648;
      sendAccessibilityEventForVirtualView(paramInt1, 65536);
      NumberPicker.this.invalidate(0, 0, NumberPicker.this.getRight(), NumberPicker.this.mTopSelectionDividerTop);
      return true;
    }

    public void sendAccessibilityEventForVirtualView(int paramInt1, int paramInt2)
    {
      switch (paramInt1)
      {
      default:
      case 3:
      case 2:
      case 1:
      }
      do
      {
        do
          return;
        while (!hasVirtualDecrementButton());
        sendAccessibilityEventForVirtualButton(paramInt1, paramInt2, getVirtualDecrementButtonText());
        return;
        sendAccessibilityEventForVirtualText(paramInt2);
        return;
      }
      while (!hasVirtualIncrementButton());
      sendAccessibilityEventForVirtualButton(paramInt1, paramInt2, getVirtualIncrementButtonText());
    }
  }

  class BeginSoftInputOnLongPressCommand
    implements Runnable
  {
    BeginSoftInputOnLongPressCommand()
    {
    }

    public void run()
    {
      NumberPicker.this.showSoftInput();
      NumberPicker.access$1902(NumberPicker.this, true);
    }
  }

  class ChangeCurrentByOneFromLongPressCommand
    implements Runnable
  {
    private boolean mIncrement;

    ChangeCurrentByOneFromLongPressCommand()
    {
    }

    private void setStep(boolean paramBoolean)
    {
      this.mIncrement = paramBoolean;
    }

    public void run()
    {
      NumberPicker.this.changeValueByOne(this.mIncrement);
      NumberPicker.this.postDelayed(this, NumberPicker.this.mLongPressUpdateInterval);
    }
  }

  public static class CustomEditText extends EditText
  {
    public CustomEditText(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }

    public void onEditorAction(int paramInt)
    {
      super.onEditorAction(paramInt);
      if (paramInt == 6)
        clearFocus();
    }
  }

  public static abstract interface Formatter
  {
    public abstract String format(int paramInt);
  }

  class InputTextFilter extends NumberKeyListener
  {
    InputTextFilter()
    {
    }

    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      CharSequence localCharSequence;
      if (NumberPicker.this.mDisplayedValues == null)
      {
        localCharSequence = super.filter(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4);
        localObject = localCharSequence;
        if (localCharSequence == null)
          localObject = paramCharSequence.subSequence(paramInt1, paramInt2);
        paramCharSequence = String.valueOf(paramSpanned.subSequence(0, paramInt3)) + localObject + paramSpanned.subSequence(paramInt4, paramSpanned.length());
        if ("".equals(paramCharSequence))
          return paramCharSequence;
        if (NumberPicker.this.getSelectedPos(paramCharSequence) > NumberPicker.this.mMaxValue)
          return "";
        return localObject;
      }
      paramCharSequence = String.valueOf(paramCharSequence.subSequence(paramInt1, paramInt2));
      if (TextUtils.isEmpty(paramCharSequence))
        return "";
      paramCharSequence = String.valueOf(paramSpanned.subSequence(0, paramInt3)) + paramCharSequence + paramSpanned.subSequence(paramInt4, paramSpanned.length());
      paramSpanned = String.valueOf(paramCharSequence).toLowerCase();
      Object localObject = NumberPicker.this.mDisplayedValues;
      paramInt2 = localObject.length;
      paramInt1 = 0;
      while (paramInt1 < paramInt2)
      {
        localCharSequence = localObject[paramInt1];
        if (localCharSequence.toLowerCase().startsWith(paramSpanned))
        {
          NumberPicker.this.postSetSelectionCommand(paramCharSequence.length(), localCharSequence.length());
          return localCharSequence.subSequence(paramInt3, localCharSequence.length());
        }
        paramInt1 += 1;
      }
      return (CharSequence)"";
    }

    protected char[] getAcceptedChars()
    {
      return NumberPicker.DIGIT_CHARACTERS;
    }

    public int getInputType()
    {
      return 1;
    }
  }

  public static abstract interface OnScrollListener
  {
    public static final int SCROLL_STATE_FLING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

    public abstract void onScrollStateChange(NumberPicker paramNumberPicker, int paramInt);
  }

  public static abstract interface OnValueChangeListener
  {
    public abstract void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2);
  }

  class PressedStateHelper
    implements Runnable
  {
    public static final int BUTTON_DECREMENT = 2;
    public static final int BUTTON_INCREMENT = 1;
    private static final int MODE_PRESS = 1;
    private static final int MODE_TAPPED = 2;
    private int mManagedButton;
    private int mMode;

    PressedStateHelper()
    {
    }

    public void buttonPressDelayed(int paramInt)
    {
      cancel();
      this.mMode = 1;
      this.mManagedButton = paramInt;
      NumberPicker.this.postDelayed(this, ViewConfiguration.getTapTimeout());
    }

    public void buttonTapped(int paramInt)
    {
      cancel();
      this.mMode = 2;
      this.mManagedButton = paramInt;
      NumberPicker.this.post(this);
    }

    public void cancel()
    {
      this.mMode = 0;
      this.mManagedButton = 0;
      NumberPicker.this.removeCallbacks(this);
      if (NumberPicker.this.mIncrementVirtualButtonPressed)
      {
        NumberPicker.access$1302(NumberPicker.this, false);
        NumberPicker.this.invalidate(0, NumberPicker.this.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
      }
      NumberPicker.access$1502(NumberPicker.this, false);
      if (NumberPicker.this.mDecrementVirtualButtonPressed)
        NumberPicker.this.invalidate(0, 0, NumberPicker.this.getRight(), NumberPicker.this.mTopSelectionDividerTop);
    }

    public void run()
    {
      switch (this.mMode)
      {
      default:
        return;
      case 1:
        switch (this.mManagedButton)
        {
        default:
          return;
        case 1:
          NumberPicker.access$1302(NumberPicker.this, true);
          NumberPicker.this.invalidate(0, NumberPicker.this.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
          return;
        case 2:
        }
        NumberPicker.access$1502(NumberPicker.this, true);
        NumberPicker.this.invalidate(0, 0, NumberPicker.this.getRight(), NumberPicker.this.mTopSelectionDividerTop);
        return;
      case 2:
      }
      switch (this.mManagedButton)
      {
      default:
        return;
      case 1:
        if (!NumberPicker.this.mIncrementVirtualButtonPressed)
          NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
        NumberPicker.access$1380(NumberPicker.this, 1);
        NumberPicker.this.invalidate(0, NumberPicker.this.mBottomSelectionDividerBottom, NumberPicker.this.getRight(), NumberPicker.this.getBottom());
        return;
      case 2:
      }
      if (!NumberPicker.this.mDecrementVirtualButtonPressed)
        NumberPicker.this.postDelayed(this, ViewConfiguration.getPressedStateDuration());
      NumberPicker.access$1580(NumberPicker.this, 1);
      NumberPicker.this.invalidate(0, 0, NumberPicker.this.getRight(), NumberPicker.this.mTopSelectionDividerTop);
    }
  }

  class SetSelectionCommand
    implements Runnable
  {
    private int mSelectionEnd;
    private int mSelectionStart;

    SetSelectionCommand()
    {
    }

    public void run()
    {
      NumberPicker.this.mInputText.setSelection(this.mSelectionStart, this.mSelectionEnd);
    }
  }

  class SupportAccessibilityNodeProvider
  {
    NumberPicker.AccessibilityNodeProviderImpl mProvider;

    private SupportAccessibilityNodeProvider()
    {
      if (Build.VERSION.SDK_INT >= 16)
        this.mProvider = new NumberPicker.AccessibilityNodeProviderImpl(NumberPicker.this);
    }

    @TargetApi(16)
    public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      if (this.mProvider != null)
        return this.mProvider.performAction(paramInt1, paramInt2, paramBundle);
      return false;
    }

    public void sendAccessibilityEventForVirtualView(int paramInt1, int paramInt2)
    {
      if (this.mProvider != null)
        this.mProvider.sendAccessibilityEventForVirtualView(paramInt1, paramInt2);
    }
  }

  private static class TwoDigitFormatter
    implements NumberPicker.Formatter
  {
    final Object[] mArgs = new Object[1];
    final StringBuilder mBuilder = new StringBuilder();
    Formatter mFmt;
    char mZeroDigit;

    TwoDigitFormatter()
    {
      init(Locale.getDefault());
    }

    private Formatter createFormatter(Locale paramLocale)
    {
      return new Formatter(this.mBuilder, paramLocale);
    }

    private static char getZeroDigit(Locale paramLocale)
    {
      return new DecimalFormatSymbols(paramLocale).getZeroDigit();
    }

    private void init(Locale paramLocale)
    {
      this.mFmt = createFormatter(paramLocale);
      this.mZeroDigit = getZeroDigit(paramLocale);
    }

    public String format(int paramInt)
    {
      Locale localLocale = Locale.getDefault();
      if (this.mZeroDigit != getZeroDigit(localLocale))
        init(localLocale);
      this.mArgs[0] = Integer.valueOf(paramInt);
      this.mBuilder.delete(0, this.mBuilder.length());
      this.mFmt.format("%02d", this.mArgs);
      return this.mFmt.toString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NumberPicker
 * JD-Core Version:    0.6.0
 */