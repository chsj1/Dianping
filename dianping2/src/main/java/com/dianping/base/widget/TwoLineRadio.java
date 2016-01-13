package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;

public class TwoLineRadio extends LinearLayout
  implements View.OnClickListener
{
  private View.OnClickListener listener;
  private int mode;
  private RadioButton radioButton;
  private Drawable radioDrawable;
  private CharSequence text1;
  private CharSequence text2;

  public TwoLineRadio(Context paramContext)
  {
    this(paramContext, null);
  }

  public TwoLineRadio(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.two_line_radio, this);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TwoLineRadio);
    this.text1 = paramContext.getText(R.styleable.TwoLineRadio_text);
    this.text2 = paramContext.getText(R.styleable.TwoLineRadio_desc);
    this.radioDrawable = paramContext.getDrawable(R.styleable.TwoLineRadio_button_drawable);
    this.mode = paramContext.getInt(R.styleable.TwoLineRadio_mode, 1);
    paramContext.recycle();
    initializeRes();
  }

  private void initializeRes()
  {
    if (this.mode == 1)
    {
      this.radioButton = ((RadioButton)findViewById(R.id.radioBtn1));
      findViewById(R.id.radioBtn2).setVisibility(8);
      if (this.text1 != null)
        ((TextView)findViewById(R.id.radioText)).setText(this.text1);
      if (this.text2 == null)
        break label161;
      ((TextView)findViewById(R.id.radioDesc)).setText(this.text2);
    }
    while (true)
    {
      if (this.radioDrawable != null)
      {
        this.radioDrawable.setBounds(0, 0, this.radioDrawable.getIntrinsicWidth(), this.radioDrawable.getIntrinsicHeight());
        this.radioButton.setCompoundDrawables(null, null, this.radioDrawable, null);
      }
      setOnClickListener(this);
      return;
      this.radioButton = ((RadioButton)findViewById(R.id.radioBtn2));
      findViewById(R.id.radioBtn1).setVisibility(8);
      break;
      label161: ((TextView)findViewById(R.id.radioDesc)).setVisibility(8);
    }
  }

  public boolean isChecked()
  {
    return this.radioButton.isChecked();
  }

  public void onClick(View paramView)
  {
    if (this.listener != null)
      this.listener.onClick(paramView);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.radioButton.setChecked(paramBoolean);
  }

  public void setDesc(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      this.text2 = paramCharSequence;
      ((TextView)findViewById(R.id.radioDesc)).setText(this.text2);
      ((TextView)findViewById(R.id.radioDesc)).setVisibility(0);
      return;
    }
    ((TextView)findViewById(R.id.radioDesc)).setVisibility(8);
  }

  public void setMyOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.listener = paramOnClickListener;
  }

  public void setRadioButtonDrawable(int paramInt)
  {
    if (paramInt > 0)
    {
      this.radioDrawable = getResources().getDrawable(paramInt);
      this.radioDrawable.setBounds(0, 0, this.radioDrawable.getIntrinsicWidth(), this.radioDrawable.getIntrinsicHeight());
      this.radioButton.setCompoundDrawables(null, null, this.radioDrawable, null);
    }
  }

  public void setText(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      this.text1 = paramCharSequence;
      ((TextView)findViewById(R.id.radioText)).setText(this.text1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TwoLineRadio
 * JD-Core Version:    0.6.0
 */