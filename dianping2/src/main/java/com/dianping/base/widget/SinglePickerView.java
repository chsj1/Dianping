package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.wheel.adapter.WheelViewAdapter;
import com.dianping.base.widget.wheel.widget.OnWheelChangedListener;
import com.dianping.base.widget.wheel.widget.OnWheelClickedListener;
import com.dianping.base.widget.wheel.widget.OnWheelScrollListener;
import com.dianping.base.widget.wheel.widget.WheelView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SinglePickerView extends LinearLayout
  implements View.OnClickListener
{
  private Context mContext;
  public OnDataChangeListener onDataChangeListener;
  private TextView pickerOK;
  private TextView pickerTip;
  private WheelView pickerView;

  public SinglePickerView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SinglePickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(this.mContext).inflate(R.layout.single_picker_view, this, true);
    this.pickerView = ((WheelView)findViewById(R.id.picker));
    this.pickerOK = ((TextView)findViewById(R.id.picker_ok));
    this.pickerTip = ((TextView)findViewById(R.id.picker_tip));
    this.pickerOK.setOnClickListener(this);
    this.pickerView.addChangingListener(new OnWheelChangedListener()
    {
      public void onChanged(WheelView paramWheelView, int paramInt1, int paramInt2)
      {
      }
    });
    this.pickerView.addScrollingListener(new OnWheelScrollListener()
    {
      public void onScrollingFinished(WheelView paramWheelView)
      {
      }

      public void onScrollingStarted(WheelView paramWheelView)
      {
      }
    });
    this.pickerView.addClickingListener(new OnWheelClickedListener()
    {
      public void onItemClicked(WheelView paramWheelView, int paramInt)
      {
      }
    });
  }

  public int getCurrentItem()
  {
    return this.pickerView.getCurrentItem();
  }

  public void onClick(View paramView)
  {
    if ((paramView.getId() == R.id.picker_ok) && (this.onDataChangeListener != null))
      this.onDataChangeListener.onDataChange();
  }

  public void setAdapter(WheelViewAdapter paramWheelViewAdapter)
  {
    this.pickerView.setViewAdapter(paramWheelViewAdapter);
  }

  public void setCurrentItem(int paramInt)
  {
    this.pickerView.setCurrentItem(paramInt);
  }

  public void setOnDataChangeListener(OnDataChangeListener paramOnDataChangeListener)
  {
    this.onDataChangeListener = paramOnDataChangeListener;
  }

  public static abstract interface OnDataChangeListener
  {
    public abstract void onDataChange();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SinglePickerView
 * JD-Core Version:    0.6.0
 */