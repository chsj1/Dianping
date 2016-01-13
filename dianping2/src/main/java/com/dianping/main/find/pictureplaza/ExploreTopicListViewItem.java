package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class ExploreTopicListViewItem extends NovaLinearLayout
{
  private int categoryId;
  private NovaTextView topicContentCountTextView;
  private DPNetworkImageView topicImageView;
  private NovaTextView topicTitleTextView;
  private String url;

  public ExploreTopicListViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public ExploreTopicListViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ExploreTopicListViewItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public String getUrl()
  {
    return this.url;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.topicImageView = ((DPNetworkImageView)findViewById(R.id.explore_plaza_topic_image));
    this.topicTitleTextView = ((NovaTextView)findViewById(R.id.explore_plaza_topic_title));
    this.topicContentCountTextView = ((NovaTextView)findViewById(R.id.explore_plaza_topic_content_count));
  }

  public void setCategoryId(int paramInt)
  {
    this.categoryId = paramInt;
  }

  public void setPlazaTopicInfor(DPObject paramDPObject)
  {
    this.url = paramDPObject.getString("Url");
    this.topicImageView.setImage(paramDPObject.getString("PicUrl"));
    this.topicTitleTextView.setText(paramDPObject.getString("Title"));
    NovaTextView localNovaTextView = this.topicContentCountTextView;
    StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getInt("ContentCount"));
    if (this.categoryId == 1);
    for (paramDPObject = "人参与"; ; paramDPObject = "条内容")
    {
      localNovaTextView.setText(paramDPObject);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreTopicListViewItem
 * JD-Core Version:    0.6.0
 */