package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class MessageInfoItem extends NotificationItem
{
  protected TextView msgCount;
  protected NetworkImageView plazaImageView;
  FrameLayout tagView;

  public MessageInfoItem(Context paramContext)
  {
    super(paramContext);
  }

  public MessageInfoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.plazaImageView = ((NetworkImageView)findViewById(R.id.plaza_message_img));
    this.msgCount = ((TextView)findViewById(R.id.msg_count));
    this.tagView = ((FrameLayout)findViewById(R.id.red_tag));
  }

  public void setNotification(DPObject paramDPObject)
  {
    String str4 = paramDPObject.getString("Title");
    String str1 = paramDPObject.getString("Content");
    paramDPObject.getInt("Type");
    String str2 = paramDPObject.getString("Image");
    String str3 = paramDPObject.getString("Time");
    int i = paramDPObject.getInt("UnreadCount");
    String str5 = paramDPObject.getString("PlazaImage");
    if (TextUtils.isEmpty(str1))
      paramDPObject = "";
    while (true)
    {
      this.name.setText(str4);
      try
      {
        this.content.setText(TextUtils.jsonParseText(paramDPObject));
        this.time.setText(str3);
        if (this.showThumb)
        {
          findViewById(R.id.thumb).setVisibility(0);
          this.thumb.setImage(str2);
          if (findViewById(R.id.notification) != null)
          {
            if (i != 0)
              break label208;
            this.tagView.setVisibility(8);
            this.msgCount.setVisibility(8);
          }
          if (this.plazaImageView != null)
          {
            if (!TextUtils.isEmpty(str5))
              break label281;
            this.plazaImageView.setVisibility(8);
          }
          return;
          paramDPObject = str1;
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          this.content.setText(paramDPObject);
          continue;
          findViewById(R.id.thumb).setVisibility(8);
          continue;
          label208: if (i == 1)
          {
            this.tagView.setVisibility(0);
            this.msgCount.setVisibility(8);
            continue;
          }
          this.tagView.setVisibility(8);
          this.msgCount.setVisibility(0);
          this.msgCount.setText("" + i);
        }
        label281: this.plazaImageView.setVisibility(0);
        this.plazaImageView.setImage(str5);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MessageInfoItem
 * JD-Core Version:    0.6.0
 */