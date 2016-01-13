package com.dianping.main.messagecenter.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.SubscribeItem;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class SubscribeListActivity extends MessageCenterListActivity
{
  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void itemClicked(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (this.adapter.getCount() >= paramInt)
    {
      paramAdapterView = this.adapter.getItem(paramInt);
      if ((paramAdapterView instanceof DPObject))
      {
        paramAdapterView = (DPObject)paramAdapterView;
        if ((paramAdapterView.getArray("Actions") == null) || (paramAdapterView.getArray("Actions").length != 1) || (paramAdapterView.getArray("Actions")[0] == null) || (paramAdapterView.getArray("Actions")[0].getString("Name") == null))
          break label176;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView.getArray("Actions")[0].getString("Name")));
        paramView.putExtra("title", paramAdapterView.getString("Name"));
        paramView.putExtra("desc", paramAdapterView.getString("Desc"));
        paramView.putExtra("imageurl", paramAdapterView.getString("Image"));
        paramView.putExtra("content", paramAdapterView.getString("Content"));
        paramView.putExtra("subtype", paramAdapterView.getInt("SubType"));
      }
    }
    try
    {
      startActivity(paramView);
      label176: 
      do
        return;
      while ((paramAdapterView.getArray("Actions") == null) || (paramAdapterView.getArray("Actions").length <= 1));
      paramView = new AlertDialog.Builder(this);
      paramView.setTitle("操作");
      String[] arrayOfString = new String[paramAdapterView.getArray("Actions").length];
      int i = 0;
      while (i < paramAdapterView.getArray("Actions").length)
      {
        arrayOfString[i] = paramAdapterView.getArray("Actions")[i].getString("ID");
        i += 1;
      }
      paramView.setItems(arrayOfString, new DialogInterface.OnClickListener(paramInt)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface = SubscribeListActivity.this.adapter.getItem(this.val$position2);
          if ((paramDialogInterface instanceof DPObject))
          {
            paramDialogInterface = (DPObject)paramDialogInterface;
            if ((paramDialogInterface.getArray("Actions") != null) && (paramDialogInterface.getArray("Actions").length > paramInt))
            {
              Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramDialogInterface.getArray("Actions")[paramInt].getString("Name")));
              localIntent.putExtra("title", paramDialogInterface.getString("Name"));
              localIntent.putExtra("desc", paramDialogInterface.getString("Desc"));
              localIntent.putExtra("imageurl", paramDialogInterface.getString("Image"));
              localIntent.putExtra("content", paramDialogInterface.getString("Content"));
              localIntent.putExtra("subtype", paramDialogInterface.getInt("SubType"));
              SubscribeListActivity.this.startActivity(localIntent);
            }
          }
        }
      });
      paramView.show();
      return;
    }
    catch (java.lang.Exception paramAdapterView)
    {
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("往期内容");
    this.mSubType = getIntParam("subtype", 0);
    setSubtitle(getStringParam("title"));
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramInt -= this.pullToRefreshListView.getHeaderViewsCount();
    if ((paramInt > -1) && (paramInt <= this.adapter.getCount()) && ((this.adapter.getItem(paramInt) instanceof DPObject)))
    {
      itemClicked(paramAdapterView, paramView, paramInt, paramLong);
      statisticsEvent("notify5", "notify5_broadcast_historyitem", ((DPObject)this.adapter.getItem(paramInt)).getString("Name"), 0);
    }
  }

  protected void setAdapter()
  {
    this.adapter = new SubscribeListAdapter(this);
  }

  class SubscribeListAdapter extends MessageCenterListActivity.MessageBasicAdapter
  {
    public SubscribeListAdapter(Context arg2)
    {
      super(localContext);
    }

    public void appendData(DPObject paramDPObject)
    {
      paramDPObject = paramDPObject.getObject("NotificationList");
      if ((paramDPObject instanceof DPObject))
        super.appendData(paramDPObject);
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/getnotificationlist.bin").buildUpon();
      localBuilder.appendQueryParameter("type", String.valueOf(3)).appendQueryParameter("start", String.valueOf(paramInt));
      if (SubscribeListActivity.this.mSubType > 0)
        localBuilder.appendQueryParameter("subtype", String.valueOf(SubscribeListActivity.this.mSubType));
      if (!TextUtils.isEmpty(SubscribeListActivity.this.accountService().token()))
        localBuilder.appendQueryParameter("token", SubscribeListActivity.this.accountService().token());
      return BasicMApiRequest.mapiGet(localBuilder.build().toString(), null);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof SubscribeItem));
      for (paramView = (SubscribeItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
        {
          localObject = (SubscribeItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.subscribe_item, paramViewGroup, false);
          ((SubscribeItem)localObject).setShowThumb(false);
        }
        ((SubscribeItem)localObject).setNotification(paramDPObject);
        return localObject;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.SubscribeListActivity
 * JD-Core Version:    0.6.0
 */