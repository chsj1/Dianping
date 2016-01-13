package com.dianping.base.widget.wheel.adapter;

import android.content.Context;

public class NumericWheelAdapter extends AbstractWheelTextAdapter
{
  public static final int DEFAULT_MAX_VALUE = 9;
  private static final int DEFAULT_MIN_VALUE = 0;
  private String format;
  private int maxValue;
  private int minValue;
  private int step = 1;

  public NumericWheelAdapter(Context paramContext)
  {
    this(paramContext, 0, 9);
  }

  public NumericWheelAdapter(Context paramContext, int paramInt1, int paramInt2)
  {
    this(paramContext, paramInt1, paramInt2, null);
  }

  public NumericWheelAdapter(Context paramContext, int paramInt1, int paramInt2, String paramString)
  {
    this(paramContext, paramInt1, paramInt2, paramString, 1);
  }

  public NumericWheelAdapter(Context paramContext, int paramInt1, int paramInt2, String paramString, int paramInt3)
  {
    super(paramContext);
    this.minValue = paramInt1;
    this.maxValue = paramInt2;
    this.format = paramString;
    this.step = paramInt3;
  }

  public CharSequence getItemText(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getItemsCount()))
    {
      paramInt = this.minValue + this.step * paramInt;
      if (this.format != null)
        return String.format(this.format, new Object[] { Integer.valueOf(paramInt) });
      return Integer.toString(paramInt);
    }
    return null;
  }

  public int getItemsCount()
  {
    return (this.maxValue - this.minValue) / this.step + 1;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wheel.adapter.NumericWheelAdapter
 * JD-Core Version:    0.6.0
 */