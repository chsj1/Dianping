package com.dianping.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import com.dianping.v1.R.layout;

public class LabelIndicatorStrategy
{
  private Context mContext;
  private int mIndicatorView;
  private final CharSequence mLabel;

  public LabelIndicatorStrategy(Context paramContext, CharSequence paramCharSequence)
  {
    this.mContext = paramContext;
    this.mLabel = paramCharSequence;
  }

  public LabelIndicatorStrategy(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    this(paramContext, paramCharSequence);
    this.mIndicatorView = paramInt;
  }

  public View createIndicatorView(TabHost paramTabHost)
  {
    if (this.mIndicatorView == 0)
      this.mIndicatorView = R.layout.tab_indicator_holo;
    paramTabHost = ((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(this.mIndicatorView, paramTabHost.getTabWidget(), false);
    ((TextView)paramTabHost.findViewById(16908310)).setText(this.mLabel);
    return paramTabHost;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.LabelIndicatorStrategy
 * JD-Core Version:    0.6.0
 */