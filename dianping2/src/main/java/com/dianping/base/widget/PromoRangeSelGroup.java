package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class PromoRangeSelGroup extends RadioGroup
  implements View.OnClickListener
{
  public static final int RANGE1K = 2;
  public static final int RANGE2K = 3;
  public static final int RANGE5K = 4;
  private int mCurRangeType;
  private RadioButton mLastSel;
  private OnRangeSelChangeListener mListener;
  private Drawable mNormal;
  private Drawable mSelected;
  private RadioButton range1k;
  private RadioButton range2k;
  private RadioButton range5k;

  public PromoRangeSelGroup(Context paramContext)
  {
    super(paramContext);
  }

  public PromoRangeSelGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public static DPObject rangeTypeToPair(int paramInt)
  {
    if (paramInt == 2)
      return new DPObject("Pair").edit().putString("ID", "1000").putString("Name", "1000米").generate();
    if (paramInt == 3)
      return new DPObject("Pair").edit().putString("ID", "2000").putString("Name", "2000米").generate();
    if (paramInt == 4)
      return new DPObject("Pair").edit().putString("ID", "5000").putString("Name", "5000米").generate();
    return null;
  }

  public int getRangeType()
  {
    return this.mCurRangeType;
  }

  public void onClick(View paramView)
  {
    if (paramView == this.range1k)
      setRangeType(2);
    while (true)
    {
      if (this.mListener != null)
        this.mListener.onRangeSelChanged(this.mCurRangeType);
      return;
      if (paramView == this.range2k)
      {
        setRangeType(3);
        continue;
      }
      if (paramView != this.range5k)
        continue;
      setRangeType(4);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.range1k = ((RadioButton)findViewById(R.id.range_1k));
    this.range2k = ((RadioButton)findViewById(R.id.range_2k));
    this.range5k = ((RadioButton)findViewById(R.id.range_5k));
    this.range1k.setOnClickListener(this);
    this.range2k.setOnClickListener(this);
    this.range5k.setOnClickListener(this);
    this.mNormal = getContext().getResources().getDrawable(R.drawable.radio_normal);
    this.mNormal.setBounds(0, 0, this.mNormal.getIntrinsicWidth(), this.mNormal.getIntrinsicHeight());
    this.mSelected = getContext().getResources().getDrawable(R.drawable.radio_pressed);
    this.mSelected.setBounds(0, 0, this.mSelected.getIntrinsicWidth(), this.mSelected.getIntrinsicHeight());
    setRangeType(this.mCurRangeType);
  }

  public void setOnRangeSelChangeListener(OnRangeSelChangeListener paramOnRangeSelChangeListener)
  {
    this.mListener = paramOnRangeSelChangeListener;
  }

  public void setRangeType(int paramInt)
  {
    this.mCurRangeType = paramInt;
    if (this.mLastSel != null)
      this.mLastSel.setCompoundDrawables(null, this.mNormal, null, null);
    if (this.mCurRangeType == 2)
    {
      this.range1k.setChecked(true);
      this.mLastSel = this.range1k;
    }
    while (true)
    {
      if (this.mLastSel != null)
        this.mLastSel.setCompoundDrawables(null, this.mSelected, null, null);
      do
      {
        return;
        if (this.mCurRangeType != 3)
          continue;
        this.range2k.setChecked(true);
        this.mLastSel = this.range2k;
        break;
      }
      while (this.mCurRangeType != 4);
      this.range5k.setChecked(true);
      this.mLastSel = this.range5k;
    }
  }

  public static abstract interface OnRangeSelChangeListener
  {
    public abstract void onRangeSelChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PromoRangeSelGroup
 * JD-Core Version:    0.6.0
 */