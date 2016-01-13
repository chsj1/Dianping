package com.dianping.booking.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.NumberPicker;
import com.dianping.booking.BookingDateItem;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.Calendar;

public class TimePickerView extends LinearLayout
{
  private static final int HOUR_CHANGE = 0;
  private static final int MINUTE_CHANGE = 1;
  private Button button1;
  private Button button2;
  private View buttonView;
  private ArrayList<BookingDateItem> hourList;
  private NumberPicker hourPicker;
  private Handler mHandler = new TimePickerView.1(this);
  private OnButtonClickListener mListener;
  private SparseArray<ArrayList<BookingDateItem>> minuteMap;
  private NumberPicker minutePicker;
  private TextView textView;
  private int timeInterval;

  public TimePickerView(Context paramContext)
  {
    super(paramContext);
  }

  public TimePickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setHourTipView(int paramInt1, int paramInt2)
  {
    if ((this.hourList == null) || (this.hourList.size() <= 0));
    while (true)
    {
      return;
      if (((BookingDateItem)this.hourList.get(paramInt1)).status != 1)
        break;
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status == 1)
      {
        this.textView.setVisibility(8);
        this.buttonView.setVisibility(0);
        return;
      }
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status == -1)
      {
        this.textView.setVisibility(0);
        this.buttonView.setVisibility(8);
        this.textView.setText("该时段不接受订位");
        return;
      }
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status != -2)
        continue;
      this.textView.setVisibility(0);
      this.buttonView.setVisibility(8);
      this.textView.setText("座位已满，请另选时段");
      return;
    }
    if (((BookingDateItem)this.hourList.get(paramInt1)).status == -1)
    {
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status == -1)
      {
        this.textView.setVisibility(0);
        this.buttonView.setVisibility(8);
        this.textView.setText("该时段不接受订位");
        return;
      }
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status == -2)
      {
        this.textView.setVisibility(0);
        this.buttonView.setVisibility(8);
        this.textView.setText("座位已满，请另选时段");
        return;
      }
      this.textView.setVisibility(0);
      this.buttonView.setVisibility(8);
      this.textView.setText("该时段不接受订位");
      return;
    }
    if (((BookingDateItem)this.hourList.get(paramInt1)).status == -2)
    {
      this.textView.setVisibility(0);
      this.buttonView.setVisibility(8);
      this.textView.setText("座位已满，请另选时段");
      return;
    }
    this.textView.setVisibility(8);
    this.buttonView.setVisibility(0);
  }

  private void setMinuteTipView(int paramInt1, int paramInt2)
  {
    if ((this.minuteMap == null) || (this.minuteMap.get(paramInt1) == null) || (((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2) == null));
    do
    {
      return;
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status == 1)
      {
        this.textView.setVisibility(8);
        this.buttonView.setVisibility(0);
        return;
      }
      if (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status != -1)
        continue;
      this.textView.setVisibility(0);
      this.buttonView.setVisibility(8);
      this.textView.setText("该时段不接受订位");
      return;
    }
    while (((BookingDateItem)((ArrayList)this.minuteMap.get(paramInt1)).get(paramInt2)).status != -2);
    this.textView.setVisibility(0);
    this.buttonView.setVisibility(8);
    this.textView.setText("座位已满，请另选时段");
  }

  private void setViewDate()
  {
    this.hourPicker.setMaxValue(23);
    this.hourPicker.setMinValue(0);
    this.hourPicker.setWrapSelectorWheel(false);
    this.minutePicker.setMinValue(0);
    if (this.timeInterval == 0)
      this.timeInterval = 30;
    this.minutePicker.setMaxValue(60 / this.timeInterval - 1);
    this.minutePicker.setWrapSelectorWheel(false);
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < 60 / this.timeInterval)
    {
      localArrayList.add(this.timeInterval * i + "");
      i += 1;
    }
    this.minutePicker.setDisplayedValues((String[])localArrayList.toArray(new String[0]));
    this.hourPicker.setOnValueChangedListener(new TimePickerView.4(this));
    this.minutePicker.setOnValueChangedListener(new TimePickerView.5(this));
  }

  public int getSelectHour()
  {
    if (this.hourPicker != null)
      return this.hourPicker.getValue();
    return 0;
  }

  public int getSelectMinute()
  {
    if (this.minutePicker != null)
      return this.minutePicker.getValue() * this.timeInterval;
    return 0;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.hourPicker = ((NumberPicker)findViewById(R.id.hourPicker));
    this.minutePicker = ((NumberPicker)findViewById(R.id.minutePicker));
    this.textView = ((TextView)findViewById(R.id.text));
    this.buttonView = findViewById(R.id.button_layout);
    this.button1 = ((Button)findViewById(R.id.button1));
    this.button2 = ((Button)findViewById(R.id.button2));
    this.hourPicker.setWrapSelectorWheel(false);
    this.minutePicker.setWrapSelectorWheel(false);
    this.hourPicker.setInputEnabled(false);
    this.minutePicker.setInputEnabled(false);
    this.button1.setOnClickListener(new TimePickerView.2(this));
    this.button2.setOnClickListener(new TimePickerView.3(this));
  }

  public void setDate(ArrayList<BookingDateItem> paramArrayList, SparseArray<ArrayList<BookingDateItem>> paramSparseArray, int paramInt)
  {
    this.hourList = paramArrayList;
    this.minuteMap = paramSparseArray;
    this.timeInterval = paramInt;
    if ((this.hourList == null) || (this.minuteMap == null))
      return;
    setViewDate();
  }

  public void setOnButtonClickListener(OnButtonClickListener paramOnButtonClickListener)
  {
    this.mListener = paramOnButtonClickListener;
  }

  public void setSelectDate(Calendar paramCalendar)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
    this.hourPicker.setValue(localCalendar.get(11));
    this.minutePicker.setValue(localCalendar.get(12) / this.timeInterval);
    if ((this.hourList == null) || (this.hourList.size() <= 0) || (this.minuteMap == null))
      return;
    setHourTipView(this.hourPicker.getValue(), this.minutePicker.getValue());
  }

  public static abstract interface OnButtonClickListener
  {
    public abstract void onNegativeButtonClick();

    public abstract void onPositiveButtonClick();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.TimePickerView
 * JD-Core Version:    0.6.0
 */