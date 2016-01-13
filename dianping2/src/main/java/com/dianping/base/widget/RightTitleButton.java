package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.v1.R.layout;

public class RightTitleButton extends FrameLayout
{
  private TextView textView;

  public RightTitleButton(Context paramContext)
  {
    this(paramContext, null);
  }

  public RightTitleButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.right_title_button, this, true);
    this.textView = ((TextView)findViewById(16908308));
  }

  public TextView getTextView()
  {
    return this.textView;
  }

  public void setText(int paramInt)
  {
    this.textView.setText(paramInt);
  }

  public void setText(String paramString)
  {
    this.textView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RightTitleButton
 * JD-Core Version:    0.6.0
 */