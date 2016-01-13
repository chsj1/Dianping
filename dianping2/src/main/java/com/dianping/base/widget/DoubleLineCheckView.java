package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;

public class DoubleLineCheckView extends LinearLayout
  implements View.OnClickListener
{
  private CompoundButton mCbSwitch;
  private CharSequence mLine1;
  private CharSequence mLine2;
  private View.OnClickListener mListener;
  private int mMode;
  private TextView mTvLine1;
  private TextView mTvLine2;

  public DoubleLineCheckView(Context paramContext)
  {
    this(paramContext, null);
  }

  public DoubleLineCheckView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.double_line_check_view, this);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DoubleLineCheckView);
    this.mLine1 = paramContext.getText(R.styleable.DoubleLineCheckView_line_1);
    this.mLine2 = paramContext.getText(R.styleable.DoubleLineCheckView_line_2);
    this.mMode = paramContext.getInt(R.styleable.DoubleLineCheckView_line_mode, 1);
    paramContext.recycle();
    initRes();
  }

  public void initRes()
  {
    this.mCbSwitch = ((CompoundButton)findViewById(R.id.cb_switch));
    this.mTvLine1 = ((TextView)findViewById(R.id.tv_line_1));
    this.mTvLine2 = ((TextView)findViewById(R.id.tv_line_2));
    if (this.mLine1 != null)
      this.mTvLine1.setText(this.mLine1);
    if (this.mLine2 != null)
      this.mTvLine2.setText(this.mLine2);
    if (this.mMode == 1)
      ViewUtils.hideView(this.mTvLine2, true);
    while (true)
    {
      setOnClickListener(this);
      return;
      if (this.mMode != 2)
        continue;
      ViewUtils.showView(this.mTvLine2);
    }
  }

  public boolean isChecked()
  {
    return this.mCbSwitch.isChecked();
  }

  public void onClick(View paramView)
  {
    if (this.mListener != null)
      this.mListener.onClick(paramView);
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
  }

  public void setChecked(boolean paramBoolean)
  {
    this.mCbSwitch.setChecked(paramBoolean);
  }

  public void setLine1Text(CharSequence paramCharSequence)
  {
    this.mLine1 = paramCharSequence;
    this.mTvLine1.setText(paramCharSequence);
  }

  public void setLine2Text(CharSequence paramCharSequence)
  {
    this.mLine2 = paramCharSequence;
    this.mTvLine2.setText(paramCharSequence);
  }

  public void setMyOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mListener = paramOnClickListener;
  }

  public void setTwoLineMode()
  {
    this.mMode = 2;
    ViewUtils.showView(this.mTvLine2);
  }

  public void toggle()
  {
    this.mCbSwitch.toggle();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.DoubleLineCheckView
 * JD-Core Version:    0.6.0
 */