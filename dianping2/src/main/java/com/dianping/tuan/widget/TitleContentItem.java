package com.dianping.tuan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TitleContentItem extends LinearLayout
{
  private TextView contentView;
  private TextView titleView;

  public TitleContentItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public TitleContentItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.title_content_view, this, true);
    this.titleView = ((TextView)findViewById(R.id.title));
    this.contentView = ((TextView)findViewById(R.id.content));
  }

  public void setContent(String paramString)
  {
    this.contentView.setText(paramString);
  }

  public void setTitle(String paramString)
  {
    this.titleView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.widget.TitleContentItem
 * JD-Core Version:    0.6.0
 */