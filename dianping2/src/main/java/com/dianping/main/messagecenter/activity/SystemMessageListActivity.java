package com.dianping.main.messagecenter.activity;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MessageInfoItem;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SystemMessageListActivity extends MessageCenterListActivity
{
  protected int mType;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mSubType = getIntParam("subtype", 0);
    this.mType = getIntParam("type", 2);
    setTitle(getStringParam("title"));
  }

  protected void setAdapter()
  {
    this.adapter = new SystemMessageAdapter(this);
  }

  class SystemMessageAdapter extends MessageCenterListActivity.MessageBasicAdapter
  {
    public SystemMessageAdapter(Context arg2)
    {
      super(localContext);
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/getsysmsglist.bin").buildUpon();
      localBuilder.appendQueryParameter("start", "" + paramInt);
      localBuilder.appendQueryParameter("subtype", "" + SystemMessageListActivity.this.mSubType);
      localBuilder.appendQueryParameter("type", "" + SystemMessageListActivity.this.mType);
      return BasicMApiRequest.mapiGet(localBuilder.build().toString(), null);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof MessageInfoItem));
      for (paramView = (MessageInfoItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (MessageInfoItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.notification_item_broadcast, paramViewGroup, false);
        paramView = (TextView)((MessageInfoItem)localObject).findViewById(R.id.notification_content);
        paramView.setSingleLine(false);
        paramView.setMaxLines(3);
        ((MessageInfoItem)localObject).setNotification(paramDPObject);
        ((MessageInfoItem)localObject).setGAString("item", paramDPObject.getString("Title"));
        ((MessageInfoItem)localObject).gaUserInfo.biz_id = ("" + paramDPObject.getInt("Type"));
        SystemMessageListActivity.this.addGAView((View)localObject, paramInt);
        return localObject;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.SystemMessageListActivity
 * JD-Core Version:    0.6.0
 */