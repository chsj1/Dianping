package com.dianping.base.widget.dialogfilter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.layout;

public class RangeFilterDialog extends FilterDialog
{
  private static final int ID_START = 100000;
  RadioGroup group = (RadioGroup)getLayoutInflater().inflate(R.layout.range_filter, getFilterViewParent(), false);
  final RadioGroup.OnCheckedChangeListener handler = new RadioGroup.OnCheckedChangeListener()
  {
    public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
    {
      int j;
      int i;
      if (RangeFilterDialog.this.listener != null)
      {
        if (RangeFilterDialog.this.pairs != null)
        {
          paramRadioGroup = RangeFilterDialog.this.pairs;
          j = paramRadioGroup.length;
          i = 0;
        }
      }
      else
        while (i < j)
        {
          Object localObject = paramRadioGroup[i];
          try
          {
            if (Integer.parseInt(localObject.getString("ID")) == paramInt - 100000)
            {
              RangeFilterDialog.this.listener.onFilter(RangeFilterDialog.this, localObject);
              return;
            }
          }
          catch (Exception localException)
          {
            i += 1;
          }
        }
      RangeFilterDialog.this.listener.onFilter(RangeFilterDialog.this, Integer.valueOf(paramInt - 100000));
    }
  };
  DPObject[] pairs;

  public RangeFilterDialog(Activity paramActivity)
  {
    super(paramActivity);
    this.group.setOnCheckedChangeListener(this.handler);
    setFilterView(this.group);
  }

  public void addRange(int paramInt)
  {
    if (this.group.getChildCount() > 0)
      getLayoutInflater().inflate(R.layout.range_filter_dashline, this.group, true);
    RadioButton localRadioButton = (RadioButton)getLayoutInflater().inflate(R.layout.range_filter_btn, this.group, false);
    if (paramInt > 0)
      localRadioButton.setText(paramInt + "m");
    while (true)
    {
      localRadioButton.setId(100000 + paramInt);
      this.group.addView(localRadioButton);
      return;
      localRadioButton.setText("附近");
    }
  }

  public void removeAllRanges()
  {
    this.group.removeAllViews();
    this.pairs = null;
  }

  public void setRanges(DPObject[] paramArrayOfDPObject)
  {
    removeAllRanges();
    this.pairs = paramArrayOfDPObject;
    int m = paramArrayOfDPObject.length;
    int i = 0;
    while (true)
      if (i < m)
      {
        String str = paramArrayOfDPObject[i].getString("ID");
        int j;
        if (str != null)
          j = 0;
        try
        {
          int k = Integer.parseInt(str);
          j = k;
          label47: addRange(j);
          i += 1;
        }
        catch (Exception localException)
        {
          break label47;
        }
      }
  }

  public void setSelectedRange(int paramInt)
  {
    this.group.check(100000 + paramInt);
  }

  public void setSelectedRange(DPObject paramDPObject)
  {
    int i = 0;
    try
    {
      int j = Integer.parseInt(paramDPObject.getString("ID"));
      i = j;
      label14: setSelectedRange(i);
      return;
    }
    catch (Exception paramDPObject)
    {
      break label14;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.dialogfilter.RangeFilterDialog
 * JD-Core Version:    0.6.0
 */