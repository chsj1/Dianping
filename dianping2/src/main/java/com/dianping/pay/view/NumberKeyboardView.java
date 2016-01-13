package com.dianping.pay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class NumberKeyboardView extends LinearLayout
  implements View.OnClickListener
{
  public static final int KEYCODE_DEFAULT = -1;
  public static final int KEYCODE_DEL = 10;
  public static final int KEYCODE_EIGHT = 8;
  public static final int KEYCODE_FIVE = 5;
  public static final int KEYCODE_FOUR = 4;
  public static final int KEYCODE_NINE = 9;
  public static final int KEYCODE_ONE = 1;
  public static final int KEYCODE_SEVEN = 7;
  public static final int KEYCODE_SIX = 6;
  public static final int KEYCODE_THREE = 3;
  public static final int KEYCODE_TWO = 2;
  public static final int KEYCODE_ZERO = 0;
  private int keyValue = -1;
  private LinearLayout mDelete;
  private TextView mNum0;
  private TextView mNum1;
  private TextView mNum2;
  private TextView mNum3;
  private TextView mNum4;
  private TextView mNum5;
  private TextView mNum6;
  private TextView mNum7;
  private TextView mNum8;
  private TextView mNum9;
  private OnNumKeyboardListener mNumKeyboardListener;

  public NumberKeyboardView(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public NumberKeyboardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(R.layout.pay_num_keyboard, this);
    this.mNum1 = ((TextView)findViewById(R.id.pay_keyboard_one));
    this.mNum2 = ((TextView)findViewById(R.id.pay_keyboard_two));
    this.mNum3 = ((TextView)findViewById(R.id.pay_keyboard_three));
    this.mNum4 = ((TextView)findViewById(R.id.pay_keyboard_four));
    this.mNum5 = ((TextView)findViewById(R.id.pay_keyboard_five));
    this.mNum6 = ((TextView)findViewById(R.id.pay_keyboard_six));
    this.mNum7 = ((TextView)findViewById(R.id.pay_keyboard_seven));
    this.mNum8 = ((TextView)findViewById(R.id.pay_keyboard_eight));
    this.mNum9 = ((TextView)findViewById(R.id.pay_keyboard_nine));
    this.mNum0 = ((TextView)findViewById(R.id.pay_keyboard_zero));
    this.mDelete = ((LinearLayout)findViewById(R.id.pay_keyboard_del));
    this.mNum1.setOnClickListener(this);
    this.mNum2.setOnClickListener(this);
    this.mNum3.setOnClickListener(this);
    this.mNum4.setOnClickListener(this);
    this.mNum5.setOnClickListener(this);
    this.mNum6.setOnClickListener(this);
    this.mNum7.setOnClickListener(this);
    this.mNum8.setOnClickListener(this);
    this.mNum9.setOnClickListener(this);
    this.mNum0.setOnClickListener(this);
    this.mDelete.setOnClickListener(this);
  }

  public void onClick(View paramView)
  {
    this.keyValue = -1;
    if (paramView.getId() == R.id.pay_keyboard_one)
      this.keyValue = 1;
    while (true)
    {
      if (this.mNumKeyboardListener != null)
        this.mNumKeyboardListener.onKeyChanged(this.keyValue);
      return;
      if (paramView.getId() == R.id.pay_keyboard_two)
      {
        this.keyValue = 2;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_three)
      {
        this.keyValue = 3;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_four)
      {
        this.keyValue = 4;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_five)
      {
        this.keyValue = 5;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_six)
      {
        this.keyValue = 6;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_seven)
      {
        this.keyValue = 7;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_eight)
      {
        this.keyValue = 8;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_nine)
      {
        this.keyValue = 9;
        continue;
      }
      if (paramView.getId() == R.id.pay_keyboard_zero)
      {
        this.keyValue = 0;
        continue;
      }
      if (paramView.getId() != R.id.pay_keyboard_del)
        continue;
      this.keyValue = 10;
    }
  }

  public void setKeyboardInputListener(OnNumKeyboardListener paramOnNumKeyboardListener)
  {
    this.mNumKeyboardListener = paramOnNumKeyboardListener;
  }

  public static abstract interface OnNumKeyboardListener
  {
    public abstract void onKeyChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.NumberKeyboardView
 * JD-Core Version:    0.6.0
 */