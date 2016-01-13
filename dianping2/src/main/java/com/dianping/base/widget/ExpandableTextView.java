package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class ExpandableTextView extends LinearLayout
{
  private ImageView arrow;
  private boolean isExpand;
  private TextView textView;

  public ExpandableTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setClickable(true);
  }

  public void expand()
  {
    if (this.isExpand)
    {
      this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_black));
      this.textView.setMaxLines(4);
      this.isExpand = false;
      return;
    }
    this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_black));
    this.textView.setMaxLines(9999);
    this.isExpand = true;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.arrow = ((ImageView)findViewById(R.id.desc_arrow));
    this.textView = ((TextView)findViewById(R.id.desc_text));
    this.isExpand = false;
  }

  public void setText(String paramString)
  {
    this.textView.setText(paramString);
    this.textView.post(new Runnable()
    {
      public void run()
      {
        if (ExpandableTextView.this.textView.getLineCount() >= 4)
          ExpandableTextView.this.arrow.setVisibility(0);
      }
    });
  }

  public void setTextClickEvent()
  {
    this.textView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ExpandableTextView.this.expand();
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ExpandableTextView
 * JD-Core Version:    0.6.0
 */