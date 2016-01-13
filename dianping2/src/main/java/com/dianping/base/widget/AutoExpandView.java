package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class AutoExpandView extends NovaLinearLayout
  implements View.OnClickListener
{
  private int DEFAULT_MAX_LINE = 2;
  private TextView autoETextView;
  private ImageView expandArrow;
  private OnExpandLayoutChangeListener onExpandLayoutChangeListener;

  public AutoExpandView(Context paramContext)
  {
    super(paramContext);
  }

  public AutoExpandView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.auto_expand_arrow)
      this.expandArrow.postDelayed(new Runnable()
      {
        public void run()
        {
          boolean bool = ((Boolean)AutoExpandView.this.expandArrow.getTag()).booleanValue();
          ImageView localImageView;
          if (bool)
          {
            AutoExpandView.this.expandArrow.setImageResource(R.drawable.navibar_arrow_down);
            AutoExpandView.this.autoETextView.setMaxLines(AutoExpandView.this.DEFAULT_MAX_LINE);
            localImageView = AutoExpandView.this.expandArrow;
            if (bool)
              break label112;
          }
          label112: for (bool = true; ; bool = false)
          {
            localImageView.setTag(Boolean.valueOf(bool));
            AutoExpandView.this.autoETextView.requestLayout();
            return;
            AutoExpandView.this.expandArrow.setImageResource(R.drawable.navibar_arrow_up);
            AutoExpandView.this.autoETextView.setMaxLines(2147483647);
            break;
          }
        }
      }
      , 100L);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.autoETextView = ((TextView)findViewById(R.id.auto_expand_textview));
    this.expandArrow = ((ImageView)findViewById(R.id.auto_expand_arrow));
    this.expandArrow.setOnClickListener(this);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if ((paramBoolean) && (this.onExpandLayoutChangeListener != null))
      this.onExpandLayoutChangeListener.onExpandLayoutChanged(paramBoolean);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    Log.d("~~abc", "AutoExpandView 计算  onMeasure");
  }

  public void setDefaultMaxLines(int paramInt)
  {
    this.DEFAULT_MAX_LINE = paramInt;
  }

  public void setOnExpandLayoutChangeListener(OnExpandLayoutChangeListener paramOnExpandLayoutChangeListener)
  {
    this.onExpandLayoutChangeListener = paramOnExpandLayoutChangeListener;
  }

  public void setText(String paramString)
  {
    this.autoETextView.setText(paramString);
    this.autoETextView.postDelayed(new Runnable()
    {
      public void run()
      {
        if (AutoExpandView.this.autoETextView.getLineCount() > AutoExpandView.this.DEFAULT_MAX_LINE)
        {
          AutoExpandView.this.autoETextView.setMaxLines(AutoExpandView.this.DEFAULT_MAX_LINE);
          AutoExpandView.this.autoETextView.requestLayout();
          AutoExpandView.this.expandArrow.setVisibility(0);
          AutoExpandView.this.expandArrow.setTag(Boolean.valueOf(false));
          return;
        }
        AutoExpandView.this.expandArrow.setVisibility(8);
      }
    }
    , 50L);
  }

  public static abstract interface OnExpandLayoutChangeListener
  {
    public abstract void onExpandLayoutChanged(boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AutoExpandView
 * JD-Core Version:    0.6.0
 */