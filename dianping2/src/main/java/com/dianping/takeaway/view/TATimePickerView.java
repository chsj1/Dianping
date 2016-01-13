package com.dianping.takeaway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.base.widget.NumberPicker;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class TATimePickerView extends LinearLayout
{
  private int interval = 1;
  private int maxValue;
  private int minValue;
  private ArrayList<String> numList;
  private NumberPicker numberPicker;

  public TATimePickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    paramContext = LayoutInflater.from(paramContext).inflate(R.layout.takeaway_time_picker_view, null);
    addView(paramContext, new LinearLayout.LayoutParams(-2, -2));
    this.numberPicker = ((NumberPicker)paramContext.findViewById(R.id.numberPicker));
    this.numberPicker.requestFocus();
    this.numberPicker.setInputEnabled(false);
    this.numberPicker.setWrapSelectorWheel(false);
  }

  public int getValue()
  {
    if (this.numberPicker != null)
      return Integer.parseInt((String)this.numList.get(this.numberPicker.getValue()));
    return 0;
  }

  public void setInterval(int paramInt)
  {
    this.interval = paramInt;
  }

  public void setNumRange(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2)
      throw new IllegalArgumentException("minValue can't be less than maxValue.");
    this.minValue = paramInt1;
    this.maxValue = paramInt2;
    if (this.numberPicker != null)
    {
      this.numList = new ArrayList();
      while (paramInt1 < this.interval + paramInt2)
      {
        this.numList.add(String.valueOf(paramInt1));
        paramInt1 += this.interval;
      }
      this.numberPicker.setDisplayedValues((String[])this.numList.toArray(new String[0]));
      this.numberPicker.setMinValue(0);
      this.numberPicker.setMaxValue(this.numList.size() - 1);
      this.numberPicker.setWrapSelectorWheel(false);
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
 * Qualified Name:     com.dianping.takeaway.view.TATimePickerView
 * JD-Core Version:    0.6.0
 */