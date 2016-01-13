package com.dianping.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.dianping.base.widget.NumberPicker;
import com.dianping.v1.R.id;
import java.util.ArrayList;

public class PeoplePickerView extends LinearLayout
{
  private int maxValue;
  private int minValue;
  private NumberPicker numberPicker;

  public PeoplePickerView(Context paramContext)
  {
    super(paramContext);
  }

  public PeoplePickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public int getMaxValue()
  {
    return this.maxValue;
  }

  public int getMinValue()
  {
    return this.minValue;
  }

  public int getValue()
  {
    if (this.numberPicker != null)
      return this.numberPicker.getValue();
    return 0;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.numberPicker = ((NumberPicker)findViewById(R.id.numberPicker));
    this.numberPicker.requestFocus();
    this.numberPicker.setInputEnabled(false);
    this.numberPicker.setWrapSelectorWheel(false);
  }

  public void setMaxValue(int paramInt)
  {
    this.maxValue = paramInt;
    if (this.numberPicker != null)
    {
      this.numberPicker.setMaxValue(this.maxValue);
      this.numberPicker.setWrapSelectorWheel(false);
    }
  }

  public void setMinValue(int paramInt)
  {
    this.minValue = paramInt;
    if (this.numberPicker != null)
    {
      this.numberPicker.setMinValue(this.minValue);
      this.numberPicker.setWrapSelectorWheel(false);
    }
  }

  public void setNumViewWithTip(int paramInt1, int paramInt2)
  {
    this.minValue = paramInt1;
    this.maxValue = paramInt2;
    if (this.numberPicker != null)
    {
      ArrayList localArrayList = new ArrayList();
      if (paramInt1 > 1)
      {
        this.numberPicker.setMinValue(paramInt1 - 1);
        localArrayList.add("本店只接受" + (paramInt1 - 1) + "人以上预订");
      }
      while (true)
      {
        this.numberPicker.setMaxValue(paramInt2);
        int i = paramInt1;
        while (i <= paramInt2)
        {
          localArrayList.add(i + "");
          i += 1;
        }
        this.numberPicker.setMinValue(paramInt1);
      }
      this.numberPicker.setDisplayedValues((String[])localArrayList.toArray(new String[0]));
      this.numberPicker.setWrapSelectorWheel(false);
      this.numberPicker.setOnValueChangedListener(new PeoplePickerView.1(this, paramInt1));
    }
  }

  public void setValue(int paramInt)
  {
    if (this.numberPicker != null)
    {
      NumberPicker localNumberPicker = this.numberPicker;
      int i = paramInt;
      if (paramInt < this.minValue)
        i = this.minValue;
      localNumberPicker.setValue(i);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.PeoplePickerView
 * JD-Core Version:    0.6.0
 */