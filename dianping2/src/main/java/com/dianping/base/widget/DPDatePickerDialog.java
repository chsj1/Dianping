package com.dianping.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.base.widget.wheel.adapter.NumericWheelAdapter;
import com.dianping.base.widget.wheel.widget.OnWheelChangedListener;
import com.dianping.base.widget.wheel.widget.WheelView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.Calendar;

public class DPDatePickerDialog extends Dialog
  implements View.OnClickListener
{
  private Calendar mCurrentCalendar;
  WheelView mDayWheel;
  private TextView mDay_picker_ok;
  private OnDatePickerClickListener mListener;
  private int mMaxYear;
  private int mMinYear;
  WheelView mMonthWheel;
  WheelView mYearWheel;

  public DPDatePickerDialog(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public DPDatePickerDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
    init(paramContext);
  }

  protected DPDatePickerDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    super(paramContext, paramBoolean, paramOnCancelListener);
  }

  private void init(Context paramContext)
  {
    setCanceledOnTouchOutside(true);
    paramContext = getLayoutInflater().inflate(R.layout.day_picker_layout, null, false);
    this.mYearWheel = ((WheelView)paramContext.findViewById(R.id.year));
    this.mMonthWheel = ((WheelView)paramContext.findViewById(R.id.month));
    this.mDayWheel = ((WheelView)paramContext.findViewById(R.id.day));
    this.mMinYear = 1900;
    this.mMaxYear = 2013;
    this.mCurrentCalendar = Calendar.getInstance();
    this.mDay_picker_ok = ((TextView)paramContext.findViewById(R.id.day_picker_ok));
    this.mDay_picker_ok.setOnClickListener(this);
    super.setContentView(paramContext);
  }

  private void setupView()
  {
    Calendar localCalendar = this.mCurrentCalendar;
    1 local1 = new OnWheelChangedListener()
    {
      public void onChanged(WheelView paramWheelView, int paramInt1, int paramInt2)
      {
        DPDatePickerDialog.this.updateDays(DPDatePickerDialog.this.mYearWheel, DPDatePickerDialog.this.mMonthWheel, DPDatePickerDialog.this.mDayWheel);
      }
    };
    int i = localCalendar.get(2);
    this.mMonthWheel.setViewAdapter(new DateNumericAdapter(getContext(), 1, 12, i));
    this.mMonthWheel.setCurrentItem(i);
    this.mMonthWheel.addChangingListener(local1);
    i = localCalendar.get(1);
    this.mYearWheel.setViewAdapter(new DateNumericAdapter(getContext(), this.mMinYear, this.mMaxYear, i - this.mMinYear));
    this.mYearWheel.setCurrentItem(i - this.mMinYear);
    this.mYearWheel.addChangingListener(local1);
    updateDays(this.mYearWheel, this.mMonthWheel, this.mDayWheel);
    this.mDayWheel.setCurrentItem(localCalendar.get(5) - 1);
  }

  public Calendar getCurrentCalendar()
  {
    return this.mCurrentCalendar;
  }

  public void onClick(View paramView)
  {
    paramView = Calendar.getInstance();
    paramView.set(1, this.mMinYear + this.mYearWheel.getCurrentItem());
    paramView.set(2, this.mMonthWheel.getCurrentItem());
    paramView.set(5, this.mDayWheel.getCurrentItem() + 1);
    this.mCurrentCalendar = paramView;
    dismiss();
    if (this.mListener != null)
      this.mListener.onDatePickerClick(this);
  }

  public DPDatePickerDialog setCurrentCalendar(Calendar paramCalendar)
  {
    this.mCurrentCalendar = paramCalendar;
    return this;
  }

  public DPDatePickerDialog setPositiveButton(String paramString, OnDatePickerClickListener paramOnDatePickerClickListener)
  {
    this.mListener = paramOnDatePickerClickListener;
    return this;
  }

  public DPDatePickerDialog setYearRange(int paramInt1, int paramInt2)
  {
    return this;
  }

  public void show()
  {
    setupView();
    super.show();
  }

  void updateDays(WheelView paramWheelView1, WheelView paramWheelView2, WheelView paramWheelView3)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(1, this.mMinYear + paramWheelView1.getCurrentItem());
    localCalendar.set(2, paramWheelView2.getCurrentItem());
    int i = localCalendar.getActualMaximum(5);
    paramWheelView3.setViewAdapter(new DateNumericAdapter(getContext(), 1, i, localCalendar.get(5) - 1));
    paramWheelView3.setCurrentItem(Math.min(i, paramWheelView3.getCurrentItem() + 1) - 1, true);
  }

  private class DateNumericAdapter extends NumericWheelAdapter
  {
    public DateNumericAdapter(Context paramInt1, int paramInt2, int paramInt3, int arg5)
    {
      super(paramInt2, paramInt3);
      setItemResource(R.layout.date_item);
      setItemTextResource(R.id.text1);
    }
  }

  public static abstract interface OnDatePickerClickListener
  {
    public abstract void onDatePickerClick(Dialog paramDialog);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DPDatePickerDialog
 * JD-Core Version:    0.6.0
 */