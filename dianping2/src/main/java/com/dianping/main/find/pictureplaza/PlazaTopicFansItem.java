package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;

public class PlazaTopicFansItem extends NovaRelativeLayout
  implements View.OnClickListener
{
  private TextView feedCountTV;
  private int topicId;
  private NetworkImageView topicImageView;
  private TextView topicNameTV;
  private String url;

  public PlazaTopicFansItem(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaTopicFansItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (TextUtils.isEmpty(this.url))
      return;
    paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.url));
    getContext().startActivity(paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.topicImageView = ((NetworkImageView)findViewById(R.id.topic_image));
    this.topicNameTV = ((TextView)findViewById(16908308));
    this.feedCountTV = ((TextView)findViewById(16908309));
    setOnClickListener(this);
  }

  public void setTopicInfo(DPObject paramDPObject)
  {
    this.topicId = paramDPObject.getInt("TopicId");
    String str = paramDPObject.getString("PicUrl");
    if (!TextUtils.isEmpty(str))
      this.topicImageView.setImage(str);
    str = paramDPObject.getString("Title");
    if (!TextUtils.isEmpty(str))
      this.topicNameTV.setText(str);
    int i = paramDPObject.getInt("ContentCount");
    if (i != 0)
      this.feedCountTV.setText(i + "条内容");
    while (true)
    {
      this.url = paramDPObject.getString("Url");
      return;
      this.feedCountTV.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaTopicFansItem
 * JD-Core Version:    0.6.0
 */