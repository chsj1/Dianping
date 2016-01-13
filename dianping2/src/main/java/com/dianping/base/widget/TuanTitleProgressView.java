package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TuanTitleProgressView extends LinearLayout
{
  private TextView contentView;
  private ProgressBar progressBar;
  private TextView titleView;

  public TuanTitleProgressView(Context paramContext)
  {
    this(paramContext, null);
  }

  public TuanTitleProgressView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.tuan_title_progress, this, true);
    this.titleView = ((TextView)findViewById(R.id.title));
    this.contentView = ((TextView)findViewById(R.id.content));
    this.progressBar = ((ProgressBar)findViewById(R.id.progress));
  }

  public void setContent(String paramString)
  {
    this.contentView.setText(paramString);
  }

  public void setProgress(int paramInt1, int paramInt2)
  {
    this.progressBar.setMax(paramInt1);
    this.progressBar.setProgress(paramInt2);
  }

  public void setTitle(String paramString)
  {
    this.titleView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TuanTitleProgressView
 * JD-Core Version:    0.6.0
 */