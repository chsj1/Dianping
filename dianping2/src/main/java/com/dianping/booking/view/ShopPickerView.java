package com.dianping.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.dianping.base.widget.wheel.adapter.WheelViewAdapter;
import com.dianping.base.widget.wheel.widget.WheelView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class ShopPickerView extends LinearLayout
{
  public WheelView pickerView;

  public ShopPickerView(Context paramContext)
  {
    super(paramContext);
  }

  public ShopPickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public int getCurrentItem()
  {
    return this.pickerView.getCurrentItem();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.pickerView = ((WheelView)findViewById(R.id.picker));
    this.pickerView.setCenterDrawable(R.drawable.booking_grouponshop_pick);
    this.pickerView.requestFocus();
    this.pickerView.showTopBottomShadows();
  }

  public void setAdapter(WheelViewAdapter paramWheelViewAdapter)
  {
    this.pickerView.setViewAdapter(paramWheelViewAdapter);
  }

  public void setCurrentItem(int paramInt)
  {
    this.pickerView.setCurrentItem(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.ShopPickerView
 * JD-Core Version:    0.6.0
 */