package com.dianping.widget.pulltorefresh.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class ListViewFooter extends LinearLayout
{
  public static final int STATE_LOADING = 2;
  public static final int STATE_NORMAL = 0;
  public static final int STATE_READY = 1;
  private View mContentView;
  private Context mContext;
  private TextView mHintView;
  private View mProgressBar;

  public ListViewFooter(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public ListViewFooter(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    this.mContext = paramContext;
    paramContext = (LinearLayout)LayoutInflater.from(this.mContext).inflate(R.layout.listview_footer, null);
    addView(paramContext);
    paramContext.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.mContentView = paramContext.findViewById(R.id.listview_footer_content);
    this.mProgressBar = paramContext.findViewById(R.id.listview_footer_progressbar);
    this.mHintView = ((TextView)paramContext.findViewById(R.id.listview_footer_hint_textview));
  }

  public int getBottomMargin()
  {
    return ((LinearLayout.LayoutParams)this.mContentView.getLayoutParams()).bottomMargin;
  }

  public void hide()
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
    localLayoutParams.height = 0;
    this.mContentView.setLayoutParams(localLayoutParams);
  }

  public void loading()
  {
    this.mHintView.setVisibility(8);
    this.mProgressBar.setVisibility(0);
  }

  public void normal()
  {
    this.mHintView.setVisibility(0);
    this.mProgressBar.setVisibility(8);
  }

  public void setBottomMargin(int paramInt)
  {
    if (paramInt < 0)
      return;
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
    localLayoutParams.bottomMargin = paramInt;
    this.mContentView.setLayoutParams(localLayoutParams);
  }

  public void setState(int paramInt)
  {
    this.mHintView.setVisibility(4);
    this.mProgressBar.setVisibility(4);
    this.mHintView.setVisibility(4);
    if (paramInt == 1)
    {
      this.mHintView.setVisibility(0);
      this.mHintView.setText(R.string.listview_footer_hint_ready);
      return;
    }
    if (paramInt == 2)
    {
      this.mProgressBar.setVisibility(0);
      return;
    }
    this.mHintView.setVisibility(0);
    this.mHintView.setText(R.string.listview_footer_hint_normal);
  }

  public void show()
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
    localLayoutParams.height = -2;
    this.mContentView.setLayoutParams(localLayoutParams);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.listview.ListViewFooter
 * JD-Core Version:    0.6.0
 */