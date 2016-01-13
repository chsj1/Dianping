package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CustomGridView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class ExploreHotFeedItem extends NovaLinearLayout
{
  private int feedType;
  private CustomGridView hotFeedGridView;
  private NovaTextView hotFeedShowAllView;
  private NovaTextView hotFeedTitleView;

  public ExploreHotFeedItem(Context paramContext)
  {
    super(paramContext);
  }

  public ExploreHotFeedItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ExploreHotFeedItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public CustomGridView getCustomGridView()
  {
    return this.hotFeedGridView;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.hotFeedTitleView = ((NovaTextView)findViewById(R.id.explore_hot_feed_title));
    this.hotFeedShowAllView = ((NovaTextView)findViewById(R.id.explore_hot_feed_show_all));
    this.hotFeedGridView = ((CustomGridView)findViewById(R.id.explore_hot_feed_gridview));
  }

  public void setTopicInfo(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      return;
      this.feedType = paramDPObject.getInt("Type");
      if (this.hotFeedTitleView != null)
        this.hotFeedTitleView.setText(paramDPObject.getString("ItemTitle"));
      if (this.hotFeedShowAllView == null)
        continue;
      this.hotFeedShowAllView.setText("查看全部");
    }
    while (this.hotFeedGridView == null);
    this.hotFeedGridView.setNeedHideDivider(true);
    this.hotFeedGridView.setStretchAllColumns(true);
    this.hotFeedGridView.setFocusable(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreHotFeedItem
 * JD-Core Version:    0.6.0
 */