package com.dianping.base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class NumOperateButton extends LinearLayout
  implements View.OnClickListener
{
  private ImageView addBtn;
  private int changeInterval = 1;
  private int currentValue = this.minValue;
  private ImageView emptyAddBtn;
  private boolean enabled = true;
  private NumOperateListener listener;
  private int maxValue = 2147483647;
  private int minValue = 0;
  private TextView numView;
  private ImageView subtractBtn;

  public NumOperateButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void dealLogic()
  {
    this.subtractBtn.setOnClickListener(this);
    this.addBtn.setOnClickListener(this);
    this.emptyAddBtn.setOnClickListener(this);
  }

  private void initView(Context paramContext)
  {
    inflate(paramContext, R.layout.dish_order_button, this);
    this.numView = ((TextView)findViewById(R.id.num));
    this.subtractBtn = ((ImageView)findViewById(R.id.subtract_num));
    this.addBtn = ((ImageView)findViewById(R.id.add_num));
    this.emptyAddBtn = ((ImageView)findViewById(R.id.empty_add_num));
    dealLogic();
    updateView();
  }

  private void updateCurrentNum()
  {
    this.numView.setText(String.valueOf(this.currentValue));
  }

  private void updateView()
  {
    if (!this.enabled)
    {
      this.numView.setVisibility(4);
      this.subtractBtn.setVisibility(4);
      this.addBtn.setVisibility(8);
      this.emptyAddBtn.setVisibility(0);
      this.emptyAddBtn.setBackgroundResource(R.drawable.empty_add_num_btn_normal);
      this.emptyAddBtn.getBackground().setAlpha(100);
      return;
    }
    updateCurrentNum();
    if (this.currentValue == this.minValue)
    {
      this.numView.setVisibility(4);
      this.subtractBtn.setVisibility(4);
      this.addBtn.setVisibility(8);
      this.emptyAddBtn.setVisibility(0);
      return;
    }
    this.numView.setVisibility(0);
    this.subtractBtn.setVisibility(0);
    this.addBtn.setVisibility(0);
    this.emptyAddBtn.setVisibility(8);
  }

  public boolean addMoreNum()
  {
    if (this.currentValue + this.changeInterval > this.maxValue)
    {
      if (this.listener != null)
        this.listener.addResult(false, this.currentValue);
      return false;
    }
    this.currentValue += this.changeInterval;
    updateView();
    if (this.listener != null)
      this.listener.addResult(true, this.currentValue);
    return true;
  }

  public int getCurrentValue()
  {
    return this.currentValue;
  }

  public void onClick(View paramView)
  {
    if (!this.enabled);
    int i;
    do
    {
      return;
      i = paramView.getId();
      if (i != R.id.subtract_num)
        continue;
      subtractNum();
      return;
    }
    while ((i != R.id.add_num) && (i != R.id.empty_add_num));
    addMoreNum();
  }

  public void setCurrentValue(int paramInt)
  {
    if ((paramInt < this.minValue) || (paramInt > this.maxValue))
      throw new IllegalArgumentException("The value must be in [minValue, maxValue]");
    this.currentValue = paramInt;
    updateView();
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.enabled = paramBoolean;
    updateView();
  }

  public void setNumOperateListener(NumOperateListener paramNumOperateListener)
  {
    this.listener = paramNumOperateListener;
  }

  public void setNumValueRange(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2)
      throw new IllegalArgumentException("minValue > maxValue");
    this.minValue = paramInt1;
    this.maxValue = paramInt2;
    this.currentValue = this.minValue;
    this.numView.setText(this.currentValue + "");
  }

  public boolean subtractNum()
  {
    return subtractNum(true);
  }

  public boolean subtractNum(boolean paramBoolean)
  {
    if (this.currentValue - this.changeInterval < this.minValue)
    {
      if ((this.listener != null) && (paramBoolean))
        this.listener.subtractResult(false, this.currentValue);
      return false;
    }
    this.currentValue -= this.changeInterval;
    updateView();
    if ((this.listener != null) && (paramBoolean))
      this.listener.subtractResult(true, this.currentValue);
    return true;
  }

  public static abstract interface NumOperateListener
  {
    public abstract void addResult(boolean paramBoolean, int paramInt);

    public abstract void subtractResult(boolean paramBoolean, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NumOperateButton
 * JD-Core Version:    0.6.0
 */