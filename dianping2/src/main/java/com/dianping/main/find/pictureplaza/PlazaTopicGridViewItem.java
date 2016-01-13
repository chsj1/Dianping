package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class PlazaTopicGridViewItem extends NovaFrameLayout
{
  private DPNetworkImageView networkImageView;
  private TextView titleView;
  private int topicId;

  public PlazaTopicGridViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaTopicGridViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public PlazaTopicGridViewItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public int getTopicId()
  {
    return this.topicId;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.networkImageView = ((DPNetworkImageView)findViewById(16908294));
    this.titleView = ((TextView)findViewById(R.id.topic_title));
  }

  public void setTopicInfo(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.topicId = paramDPObject.getInt("TopicId");
    if (this.titleView != null)
      this.titleView.setText(paramDPObject.getString("Title"));
    if (this.networkImageView != null)
      this.networkImageView.setImage(paramDPObject.getString("PicUrl"));
    setTag(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaTopicGridViewItem
 * JD-Core Version:    0.6.0
 */