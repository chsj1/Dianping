package com.dianping.shopinfo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class ExpandView extends NovaLinearLayout
{
  private ImageView arrow;
  private TextView expandTextView;
  private String moreHint = "更多";

  public ExpandView(Context paramContext)
  {
    super(paramContext);
  }

  public ExpandView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.expandTextView = ((TextView)findViewById(R.id.expand_hint));
    this.arrow = ((ImageView)findViewById(R.id.expand_arrow));
  }

  public void setExpandTextTitle(String paramString)
  {
    this.moreHint = paramString;
    if (TextUtils.isEmpty(this.moreHint))
      this.moreHint = "更多";
  }

  public void setExpandViewSpread(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.arrow.setImageResource(R.drawable.navibar_arrow_up);
      this.expandTextView.setText("收起");
      return;
    }
    this.arrow.setImageResource(R.drawable.navibar_arrow_down);
    this.expandTextView.setText(this.moreHint);
  }

  public void setTextColor(int paramInt)
  {
    this.expandTextView.setTextColor(paramInt);
  }

  public void setTextSize(float paramFloat)
  {
    this.expandTextView.setTextSize(0, paramFloat);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ExpandView
 * JD-Core Version:    0.6.0
 */