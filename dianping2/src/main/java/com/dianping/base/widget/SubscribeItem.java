package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import java.util.Date;

public class SubscribeItem extends NotificationItem
{
  public SubscribeItem(Context paramContext)
  {
    super(paramContext);
  }

  public SubscribeItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setNotification(DPObject paramDPObject)
  {
    paramDPObject.getString("Name");
    Object localObject = paramDPObject.getString("Content");
    int n = paramDPObject.getInt("Type");
    String str1 = paramDPObject.getString("Image");
    long l = paramDPObject.getTime("Time");
    String str2 = paramDPObject.getString("Quote");
    int i1 = paramDPObject.getInt("ContentStyle");
    boolean bool = paramDPObject.getBoolean("IsRead");
    int i;
    if ((0x8000000 & i1) == 134217728)
      i = 1;
    while (true)
    {
      int j;
      label87: int k;
      label100: int m;
      if ((0x4000000 & i1) == 67108864)
      {
        j = 1;
        if ((0x2000000 & i1) != 33554432)
          break label444;
        k = 1;
        if ((0x1000000 & i1) != 16777216)
          break label450;
        m = 1;
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
        this.content.setTextColor(0xFFFFFF & i1 | 0xFF000000);
      }
      try
      {
        this.content.setText(Html.fromHtml(paramDPObject));
        paramDPObject = new Date(l);
        this.name.setText(DateUtil.format2(paramDPObject));
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
            break label483;
          this.quote.setText("");
          this.quote.setVisibility(8);
          if (findViewById(R.id.notification) != null)
          {
            if (bool)
              break label503;
            findViewById(R.id.notification).setBackgroundResource(R.drawable.notification_unread);
          }
          return;
          i = 0;
          continue;
          j = 0;
          break label87;
          label444: k = 0;
          break label100;
          label450: m = 0;
        }
      }
      catch (Exception localException)
      {
        label483: label503: 
        do
        {
          while (true)
          {
            this.content.setText(paramDPObject);
            continue;
            findViewById(R.id.thumb).setVisibility(8);
            continue;
            this.quote.setText(str2);
            this.quote.setVisibility(0);
          }
          findViewById(R.id.notification).setBackgroundResource(R.drawable.list_item);
        }
        while (n != 3);
        this.name.setTextColor(getResources().getColor(R.color.light_gray));
        this.time.setTextColor(getResources().getColor(R.color.light_gray));
        this.content.setTextColor(getResources().getColor(R.color.light_gray));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SubscribeItem
 * JD-Core Version:    0.6.0
 */