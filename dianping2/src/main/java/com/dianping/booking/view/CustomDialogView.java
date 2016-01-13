package com.dianping.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.id;

public class CustomDialogView extends LinearLayout
{
  private Button button1;
  private Button button2;
  private View buttonLayout;
  private FrameLayout contentView;
  private TextView title;

  public CustomDialogView(Context paramContext)
  {
    super(paramContext);
  }

  public CustomDialogView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.contentView = ((FrameLayout)findViewById(R.id.content_view));
    this.buttonLayout = findViewById(R.id.button_layout);
    this.button1 = ((Button)findViewById(R.id.button1));
    this.button2 = ((Button)findViewById(R.id.button2));
  }

  public CustomDialogView setNegativeButton(String paramString, View.OnClickListener paramOnClickListener)
  {
    this.button2.setText(paramString);
    this.button2.setOnClickListener(paramOnClickListener);
    this.button2.setVisibility(0);
    this.buttonLayout.setVisibility(0);
    return this;
  }

  public CustomDialogView setPositiveButton(String paramString, View.OnClickListener paramOnClickListener)
  {
    this.button1.setText(paramString);
    this.button1.setOnClickListener(paramOnClickListener);
    this.button1.setVisibility(0);
    this.buttonLayout.setVisibility(0);
    return this;
  }

  public CustomDialogView setTitle(String paramString)
  {
    this.title.setText(paramString);
    return this;
  }

  public CustomDialogView setView(View paramView)
  {
    if (paramView != null)
    {
      this.contentView.addView(paramView, new LinearLayout.LayoutParams(-1, -1));
      this.contentView.setVisibility(0);
    }
    return this;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.CustomDialogView
 * JD-Core Version:    0.6.0
 */