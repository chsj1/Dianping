package com.dianping.base.widget.tagflow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.v1.R.styleable;

public class TagFlowLayout extends FlowLayout
  implements TagAdapter.OnDataChangedListener
{
  public static final int CHOICE_MODE_MULTIPLE = 2;
  public static final int CHOICE_MODE_NONE = 0;
  public static final int CHOICE_MODE_SINGLE = 1;
  private static final int INVALID_POSITION = -1;
  private static final String KEY_CHECKED_COUNT = "key_checked_count";
  private static final String KEY_CHECKED_POSITIONS = "key_checked_positions";
  private static final String KEY_DEFAULT = "key_default";
  private static final String TAG = "TagFlowLayout";
  private SparseBooleanArray mCheckStates = new SparseBooleanArray();
  private int mCheckedItemCount = 0;
  private int mChoiceMode = 0;
  private MotionEvent mMotionEvent;
  private OnItemCheckedStateChangedListener mOnItemCheckedStateChangedListener;
  private OnTagClickListener mOnTagClickListener;
  private TagAdapter mTagAdapter;

  public TagFlowLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public TagFlowLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public TagFlowLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TagFlowLayout).recycle();
  }

  private void changeAdapter()
  {
    removeAllViews();
    TagAdapter localTagAdapter = this.mTagAdapter;
    if (localTagAdapter != null)
    {
      int i = 0;
      if (i < localTagAdapter.getCount())
      {
        View localView = localTagAdapter.getView(this, i, localTagAdapter.getItem(i));
        TagView localTagView = new TagView(getContext());
        localView.setDuplicateParentStateEnabled(true);
        localTagView.setLayoutParams(localView.getLayoutParams());
        localTagView.addView(localView);
        addView(localTagView);
        if ((this.mChoiceMode == 1) && (i == 0))
        {
          this.mCheckStates.put(i, true);
          this.mCheckedItemCount = 1;
          localTagView.setChecked(true);
        }
        while (true)
        {
          i += 1;
          break;
          if (!this.mCheckStates.get(i))
            continue;
          this.mCheckedItemCount += 1;
          localTagView.setChecked(true);
        }
      }
    }
  }

  private void doCheckAction(TagView paramTagView, int paramInt)
  {
    if (!paramTagView.isChecked())
      if ((this.mChoiceMode == 1) && (this.mCheckStates.size() == 1))
      {
        ((TagView)getChildAt(this.mCheckStates.keyAt(0))).setChecked(false);
        this.mCheckStates.delete(this.mCheckStates.keyAt(0));
        paramTagView.setChecked(true);
        this.mCheckStates.put(paramInt, true);
        this.mCheckedItemCount = 1;
      }
    do
    {
      do
        return;
      while ((this.mChoiceMode != 2) && (this.mChoiceMode != 0));
      paramTagView.setChecked(true);
      this.mCheckStates.put(paramInt, true);
      this.mCheckedItemCount += 1;
      return;
    }
    while ((this.mChoiceMode == 1) && (this.mCheckStates.size() == 1));
    paramTagView.setChecked(false);
    this.mCheckStates.delete(paramInt);
    this.mCheckedItemCount -= 1;
  }

  private TagView findChild(int paramInt1, int paramInt2)
  {
    int j = getChildCount();
    int i = 0;
    if (i < j)
    {
      TagView localTagView = (TagView)getChildAt(i);
      if (localTagView.getVisibility() == 8);
      Rect localRect;
      do
      {
        i += 1;
        break;
        localRect = new Rect();
        localTagView.getHitRect(localRect);
      }
      while (!localRect.contains(paramInt1, paramInt2));
      return localTagView;
    }
    return null;
  }

  private int findPosByView(View paramView)
  {
    int j = getChildCount();
    int i = 0;
    while (i < j)
    {
      if (getChildAt(i) == paramView)
        return i;
      i += 1;
    }
    return -1;
  }

  public void clearChoices()
  {
    if ((this.mChoiceMode == 1) && (this.mCheckStates.size() == 1))
      return;
    int i = 0;
    while (i < this.mCheckStates.size())
    {
      ((TagView)getChildAt(this.mCheckStates.keyAt(i))).setChecked(false);
      i += 1;
    }
    this.mCheckStates.clear();
    this.mCheckedItemCount = 0;
  }

  public int getCheckedItemCount()
  {
    return this.mCheckedItemCount;
  }

  public int getCheckedItemPosition()
  {
    if ((this.mChoiceMode == 1) && (this.mCheckStates != null) && (this.mCheckStates.size() == 1))
      return this.mCheckStates.keyAt(0);
    return -1;
  }

  public SparseBooleanArray getCheckedItemPositions()
  {
    return this.mCheckStates;
  }

  public int getChoiceMode()
  {
    return this.mChoiceMode;
  }

  public boolean isItemChecked(int paramInt)
  {
    if ((this.mChoiceMode != 0) && (this.mCheckStates != null))
      return this.mCheckStates.get(paramInt);
    return false;
  }

  public void onChanged()
  {
    changeAdapter();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int j = getChildCount();
    int i = 0;
    if (i < j)
    {
      TagView localTagView = (TagView)getChildAt(i);
      if (localTagView.getVisibility() == 8);
      while (true)
      {
        i += 1;
        break;
        if (localTagView.getTagView().getVisibility() != 8)
          continue;
        localTagView.setVisibility(8);
      }
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof Bundle))
    {
      paramParcelable = (Bundle)paramParcelable;
      this.mCheckedItemCount = paramParcelable.getInt("key_checked_count");
      Object localObject = paramParcelable.getString("key_checked_positions");
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localObject = ((String)localObject).split("\\|");
        int j = localObject.length;
        int i = 0;
        while (i < j)
        {
          int k = Integer.parseInt(localObject[i]);
          if ((this.mChoiceMode == 1) && (this.mCheckStates != null) && (this.mCheckStates.size() == 1))
          {
            ((TagView)getChildAt(this.mCheckStates.keyAt(0))).setChecked(false);
            this.mCheckStates.clear();
          }
          this.mCheckStates.put(k, true);
          ((TagView)getChildAt(k)).setChecked(true);
          i += 1;
        }
      }
      super.onRestoreInstanceState(paramParcelable.getParcelable("key_default"));
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }

  protected Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("key_default", super.onSaveInstanceState());
    String str1 = "";
    String str2 = str1;
    if (this.mCheckStates.size() > 0)
    {
      int i = 0;
      while (i < this.mCheckStates.size())
      {
        str1 = str1 + this.mCheckStates.keyAt(i) + "|";
        i += 1;
      }
      str2 = str1.substring(0, str1.length() - 1);
    }
    localBundle.putString("key_checked_positions", str2);
    localBundle.putInt("key_checked_count", this.mCheckedItemCount);
    return localBundle;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 1)
      this.mMotionEvent = MotionEvent.obtain(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }

  public boolean performClick()
  {
    if (this.mMotionEvent == null)
      return super.performClick();
    int i = (int)this.mMotionEvent.getX();
    int j = (int)this.mMotionEvent.getY();
    this.mMotionEvent = null;
    TagView localTagView = findChild(i, j);
    i = findPosByView(localTagView);
    if (localTagView != null)
    {
      doCheckAction(localTagView, i);
      if (this.mOnItemCheckedStateChangedListener != null)
        this.mOnItemCheckedStateChangedListener.onItemCheckedStateChanged(localTagView.getTagView(), i, this.mCheckStates.get(i, false));
      if (this.mOnTagClickListener != null)
        return this.mOnTagClickListener.onTagClick(localTagView.getTagView(), i, this);
    }
    return super.performClick();
  }

  public void setAdapter(TagAdapter paramTagAdapter)
  {
    if (paramTagAdapter == null)
      return;
    if (this.mCheckStates != null)
      this.mCheckStates.clear();
    this.mTagAdapter = paramTagAdapter;
    this.mTagAdapter.setOnDataChangedListener(this);
    changeAdapter();
  }

  public void setCheckedList(SparseBooleanArray paramSparseBooleanArray)
  {
    if (paramSparseBooleanArray == null);
    while (true)
    {
      return;
      if (this.mChoiceMode == 1)
        continue;
      this.mCheckedItemCount = 0;
      SparseBooleanArray localSparseBooleanArray = this.mCheckStates.clone();
      int i = 0;
      while (i < localSparseBooleanArray.size())
      {
        ((TagView)getChildAt(localSparseBooleanArray.keyAt(i))).setChecked(false);
        this.mCheckStates.delete(localSparseBooleanArray.keyAt(i));
        i += 1;
      }
      i = 0;
      while (i < paramSparseBooleanArray.size())
      {
        if ((paramSparseBooleanArray.keyAt(i) < this.mTagAdapter.getCount() - 1) && (paramSparseBooleanArray.keyAt(i) > -1) && (paramSparseBooleanArray.valueAt(i)))
          this.mCheckStates.put(paramSparseBooleanArray.keyAt(i), paramSparseBooleanArray.valueAt(i));
        i += 1;
      }
      i = 0;
      while (i < this.mCheckStates.size())
      {
        ((TagView)getChildAt(this.mCheckStates.keyAt(i))).setChecked(this.mCheckStates.valueAt(i));
        this.mCheckedItemCount += 1;
        i += 1;
      }
    }
  }

  public void setChoiceMode(int paramInt)
  {
    this.mChoiceMode = paramInt;
  }

  public void setItemChecked(int paramInt)
  {
    if ((paramInt >= this.mTagAdapter.getCount()) || (paramInt < 0));
    TagView localTagView;
    do
    {
      return;
      localTagView = (TagView)getChildAt(paramInt);
    }
    while (localTagView == null);
    doCheckAction(localTagView, paramInt);
  }

  public void setNumLine(int paramInt)
  {
    super.setNumLine(paramInt);
  }

  public void setOnItemCheckedStateChangedListener(OnItemCheckedStateChangedListener paramOnItemCheckedStateChangedListener)
  {
    this.mOnItemCheckedStateChangedListener = paramOnItemCheckedStateChangedListener;
    if (this.mOnItemCheckedStateChangedListener != null)
      setClickable(true);
  }

  public void setOnTagClickListener(OnTagClickListener paramOnTagClickListener)
  {
    this.mOnTagClickListener = paramOnTagClickListener;
    if (paramOnTagClickListener != null)
      setClickable(true);
  }

  public static abstract interface OnItemCheckedStateChangedListener
  {
    public abstract void onItemCheckedStateChanged(View paramView, int paramInt, boolean paramBoolean);
  }

  public static abstract interface OnTagClickListener
  {
    public abstract boolean onTagClick(View paramView, int paramInt, FlowLayout paramFlowLayout);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.tagflow.TagFlowLayout
 * JD-Core Version:    0.6.0
 */