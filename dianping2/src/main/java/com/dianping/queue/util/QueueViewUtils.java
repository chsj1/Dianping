package com.dianping.queue.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class QueueViewUtils
{
  public static void setTextViewToLine(TextView paramTextView, String paramString)
  {
    if (paramTextView == null)
      return;
    if (TextUtils.isEmpty(paramString))
    {
      paramTextView.setText("--");
      return;
    }
    paramTextView.setText(paramString);
  }

  public static void updateViewVisibility(View paramView, int paramInt)
  {
    if (paramView == null)
      return;
    paramView.setVisibility(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.util.QueueViewUtils
 * JD-Core Version:    0.6.0
 */