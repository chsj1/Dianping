package com.dianping.base.widget;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Date;

public class NotificationItem extends NovaLinearLayout
{
  protected static final int SUBSCRIBE_TYPE = 3;
  protected TextView content;
  boolean isTo;
  protected TextView name;
  protected TextView quote;
  boolean showThumb = true;
  protected NetworkThumbView thumb;
  protected TextView time;

  public NotificationItem(Context paramContext)
  {
    super(paramContext);
  }

  public NotificationItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean getIsTo()
  {
    return this.isTo;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumb = ((NetworkThumbView)findViewById(16908294));
    this.name = ((TextView)findViewById(R.id.notification_name));
    this.content = ((TextView)findViewById(R.id.notification_content));
    this.quote = ((TextView)findViewById(R.id.notification_quote));
    this.time = ((TextView)findViewById(R.id.notification_time));
  }

  public void setIsTo(boolean paramBoolean)
  {
    this.isTo = paramBoolean;
  }

  public void setNotification(DPObject paramDPObject)
  {
    String str3 = paramDPObject.getString("Name");
    Object localObject = paramDPObject.getString("Content");
    int n = paramDPObject.getInt("Type");
    String str1 = paramDPObject.getString("Image");
    long l = paramDPObject.getTime("Time");
    String str2 = paramDPObject.getString("Quote");
    int m = paramDPObject.getInt("ContentStyle");
    boolean bool = paramDPObject.getBoolean("IsRead");
    int i;
    if ((0x8000000 & m) == 134217728)
      i = 1;
    while (true)
    {
      int j;
      label88: int k;
      if ((0x4000000 & m) == 67108864)
      {
        j = 1;
        if ((0x2000000 & m) != 33554432)
          break label453;
        k = 1;
        label101: if ((0x1000000 & m) != 16777216)
          break label459;
        m = 1;
        label114: if (!TextUtils.isEmpty((CharSequence)localObject))
          break label465;
        localObject = "";
        paramDPObject = (DPObject)localObject;
        if (i != 0)
          paramDPObject = "<b>" + (String)localObject + "</b>";
        localObject = paramDPObject;
        if (j != 0)
          localObject = "<i>" + paramDPObject + "</i>";
        paramDPObject = (DPObject)localObject;
        if (k != 0)
          paramDPObject = "<u>" + (String)localObject + "</u>";
        localObject = paramDPObject;
        if (m != 0)
          localObject = "<strike>" + paramDPObject + "</strike>";
        paramDPObject = ((String)localObject).replace("\r\n", "<br>");
        this.name.setText(str3);
      }
      try
      {
        this.content.setText(Html.fromHtml(paramDPObject));
        paramDPObject = new Date(l);
        this.time.setText(DateUtil.format2(paramDPObject));
        if (this.showThumb)
        {
          paramDPObject = findViewById(R.id.thumb);
          paramDPObject.setVisibility(0);
          if (n == 3)
          {
            localObject = (LinearLayout.LayoutParams)paramDPObject.getLayoutParams();
            ((LinearLayout.LayoutParams)localObject).width = ViewUtils.dip2px(getContext(), 54.0F);
            ((LinearLayout.LayoutParams)localObject).height = ViewUtils.dip2px(getContext(), 54.0F);
            paramDPObject.setLayoutParams((ViewGroup.LayoutParams)localObject);
          }
          this.thumb.setImage(str1);
          if ((str2 != null) && (!str2.equals("")))
            break label495;
          this.quote.setText("");
          this.quote.setVisibility(8);
          if (findViewById(R.id.notification) != null)
          {
            paramDPObject = findViewById(R.id.red_tag);
            if (bool)
              break label515;
            if (paramDPObject != null)
              paramDPObject.setVisibility(0);
          }
          return;
          i = 0;
          continue;
          j = 0;
          break label88;
          label453: k = 0;
          break label101;
          label459: m = 0;
          break label114;
        }
      }
      catch (Exception localException)
      {
        label465: label495: 
        do
          while (true)
          {
            this.content.setText(paramDPObject);
            continue;
            findViewById(R.id.thumb).setVisibility(8);
            continue;
            this.quote.setText(str2);
            this.quote.setVisibility(0);
          }
        while (paramDPObject == null);
        label515: paramDPObject.setVisibility(8);
      }
    }
  }

  public void setShowThumb(boolean paramBoolean)
  {
    this.showThumb = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NotificationItem
 * JD-Core Version:    0.6.0
 */