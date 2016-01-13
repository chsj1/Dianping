package com.dianping.takeaway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TAToastView extends LinearLayout
{
  ProgressBar progressBar;
  TextView textView;

  public TAToastView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public TAToastView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    inflate(paramContext, R.layout.ta_toast_layout, this);
    this.textView = ((TextView)findViewById(R.id.toastText));
    this.progressBar = ((ProgressBar)findViewById(R.id.toastProcess));
  }

  public void hideToast()
  {
    setVisibility(8);
  }

  public void showToast(String paramString, boolean paramBoolean)
  {
    this.textView.setText(paramString);
    if (paramBoolean)
      this.progressBar.setVisibility(0);
    while (true)
    {
      setVisibility(0);
      return;
      this.progressBar.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAToastView
 * JD-Core Version:    0.6.0
 */