package com.dianping.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NovaTextView extends TextView
{
  public GAUserInfo gaUserInfo = new GAUserInfo();
  private String mGAElementString = null;
  private View.OnClickListener mListener;

  public NovaTextView(Context paramContext)
  {
    super(paramContext);
  }

  public NovaTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  @SuppressLint({"NewApi"})
  public NovaTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public String getGAString()
  {
    return this.mGAElementString;
  }

  public GAUserInfo getGAUserInfo()
  {
    return this.gaUserInfo;
  }

  public boolean performClick()
  {
    if (this.mListener != null)
      GAHelper.instance().statisticsEvent(this, "tap");
    return super.performClick();
  }

  public void setGAString(String paramString)
  {
    this.mGAElementString = paramString;
  }

  public void setGAString(String paramString, GAUserInfo paramGAUserInfo)
  {
    this.mGAElementString = paramString;
    this.gaUserInfo = paramGAUserInfo;
  }

  public void setGAString(String paramString1, String paramString2)
  {
    setGAString(paramString1, paramString2, 2147483647);
  }

  public void setGAString(String paramString1, String paramString2, int paramInt)
  {
    this.mGAElementString = paramString1;
    this.gaUserInfo.title = paramString2;
    this.gaUserInfo.index = Integer.valueOf(paramInt);
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    super.setOnClickListener(paramOnClickListener);
    this.mListener = paramOnClickListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.NovaTextView
 * JD-Core Version:    0.6.0
 */