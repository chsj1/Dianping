package com.dianping.base.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TitleListItem extends LinearLayout
{
  private FrameLayout contentView;
  private TextView messageView;
  private TextView titleView;

  public TitleListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public TitleListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.list_item_with_title, this, true);
    this.titleView = ((TextView)findViewById(R.id.title));
    this.messageView = ((TextView)findViewById(R.id.message));
    this.contentView = ((FrameLayout)findViewById(R.id.content_view));
  }

  public void setContentView(View paramView)
  {
    this.messageView.setVisibility(8);
    this.contentView.setVisibility(0);
    this.contentView.removeAllViews();
    this.contentView.addView(paramView);
  }

  public void setMessage(String paramString)
  {
    this.messageView.setVisibility(0);
    this.contentView.setVisibility(8);
    this.messageView.setText(Html.fromHtml(paramString));
  }

  public void setTitle(String paramString)
  {
    this.titleView.setText(Html.fromHtml(paramString));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TitleListItem
 * JD-Core Version:    0.6.0
 */