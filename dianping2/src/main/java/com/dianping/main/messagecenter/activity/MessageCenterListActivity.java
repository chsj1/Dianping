package com.dianping.main.messagecenter.activity;

import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.MessageInfoItem;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;

public class MessageCenterListActivity extends NovaActivity
  implements AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener, AdapterView.OnItemLongClickListener
{
  protected static final String DEL_NOTIFICATION_URI = "http://m.api.dianping.com/delnotifications.bin";
  protected static final String GET_MESSAGE_LIST_URI = "http://m.api.dianping.com/getmessagelist.bin";
  protected static final String GET_NOTIFICATION_URI = "http://m.api.dianping.com/getnotificationlist.bin";
  protected static final String GET_SYSMESSAGE_LIST_URI = "http://m.api.dianping.com/getsysmsglist.bin";
  protected static final int NOTIFY_TYPE = 1;
  protected static final int REMINDER_TYPE = 2;
  protected static final int SUBSCRIBE_TYPE = 3;
  protected MessageBasicAdapter adapter;
  private DPObject mNotification;
  protected int mSubType;
  protected PullToRefreshListView pullToRefreshListView;

  void modifyUnreadMessageCount()
  {
    Object localObject = preferences();
    if (localObject != null)
    {
      localObject = ((SharedPreferences)localObject).edit();
      ((SharedPreferences.Editor)localObject).putInt("notification_count", 0);
      ((SharedPreferences.Editor)localObject).putInt("alert_count", 0);
      ((SharedPreferences.Editor)localObject).putInt("subscribe_count", 0);
      ((SharedPreferences.Editor)localObject).commit();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.table_content_for_msg);
    this.pullToRefreshListView = ((PullToRefreshListView)findViewById(16908298));
    this.pullToRefreshListView.setOnItemClickListener(this);
    this.pullToRefreshListView.setOnRefreshListener(this);
    this.pullToRefreshListView.setOnItemLongClickListener(this);
    setAdapter();
    this.pullToRefreshListView.setAdapter(this.adapter);
    if (paramBundle != null)
      this.adapter.onRestoreInstanceState(paramBundle);
    sendBroadcast(new Intent("com.dianping.action.NEW_MESSAGE"));
    ((NotificationManager)getSystemService("notification")).cancelAll();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramInt -= this.pullToRefreshListView.getHeaderViewsCount();
    if ((this.adapter.getCount() >= paramInt) && (paramInt > -1))
    {
      paramAdapterView = this.adapter.getItem(paramInt);
      if ((paramAdapterView instanceof DPObject))
      {
        paramAdapterView = (DPObject)paramAdapterView;
        this.adapter.setMessageRead(paramInt);
        paramView = paramAdapterView.getString("URL");
        if (!TextUtils.isEmpty(paramView))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          paramView.putExtra("title", paramAdapterView.getString("Title"));
          paramView.putExtra("imageurl", paramAdapterView.getString("Image"));
          paramView.putExtra("content", paramAdapterView.getString("Content"));
          paramView.putExtra("subtype", paramAdapterView.getInt("SubType"));
        }
      }
    }
    try
    {
      startActivity(paramView);
      return;
    }
    catch (java.lang.Exception paramAdapterView)
    {
    }
  }

  public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramInt -= this.pullToRefreshListView.getHeaderViewsCount();
    if ((this.adapter.getCount() >= paramInt) && (paramInt > -1))
    {
      paramAdapterView = this.adapter.getItem(paramInt);
      if ((paramAdapterView instanceof DPObject))
      {
        this.mNotification = ((DPObject)paramAdapterView);
        if (this.mNotification.getInt("SubType") == 2)
          return false;
        new AlertDialog.Builder(this).setTitle("提示").setMessage("是否删除该消息？").setPositiveButton("确定", new DialogInterface.OnClickListener(paramInt)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            MessageCenterListActivity.this.adapter.delNotificationTask(this.val$finalPosition);
            MessageCenterListActivity.this.showProgressDialog("正在删除...");
          }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            paramDialogInterface.cancel();
          }
        }).show();
      }
    }
    return true;
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    this.adapter.pullToReset(true);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.adapter.onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  protected void setAdapter()
  {
    this.adapter = new MessageBasicAdapter(this);
  }

  class MessageBasicAdapter extends BasicLoadAdapter
  {
    private MApiRequest deleteNotificationRequest;

    public MessageBasicAdapter(Context arg2)
    {
      super();
    }

    private DPObject[] resetUnreadCount(DPObject[] paramArrayOfDPObject)
    {
      DPObject[] arrayOfDPObject = paramArrayOfDPObject;
      if (paramArrayOfDPObject == null)
        arrayOfDPObject = new DPObject[0];
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        arrayOfDPObject[i] = arrayOfDPObject[i].edit().putInt("UnreadCount", 0).generate();
        i += 1;
      }
      return arrayOfDPObject;
    }

    public void appendData(DPObject paramDPObject)
    {
      if (this.mIsPullToRefresh)
      {
        this.mIsPullToRefresh = false;
        this.mData.clear();
      }
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        if (this.mNextStartIndex <= 0)
          break label71;
        appendData(resetUnreadCount(arrayOfDPObject));
      }
      while (true)
      {
        this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.mIsEnd = paramDPObject.getBoolean("IsEnd");
        notifyDataSetChanged();
        return;
        label71: appendData(arrayOfDPObject);
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/getmessagelist.bin").buildUpon();
      localBuilder.appendQueryParameter("start", "" + paramInt);
      return BasicMApiRequest.mapiGet(localBuilder.build().toString(), null);
    }

    protected void delNotificationTask(int paramInt)
    {
      Object localObject = MessageCenterListActivity.this.adapter.getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        localObject = (DPObject)localObject;
        if (this.deleteNotificationRequest != null)
          MessageCenterListActivity.this.mapiService().abort(this.deleteNotificationRequest, this, true);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(String.valueOf(((DPObject)localObject).getInt("Type")) + "|");
        localStringBuilder.append(String.valueOf(((DPObject)localObject).getInt("SubType")) + "|");
        localStringBuilder.append(String.valueOf(((DPObject)localObject).getInt("ID")));
        this.deleteNotificationRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/delnotifications.bin", new String[] { "token", MessageCenterListActivity.this.accountService().token(), "notificationidset", localStringBuilder.toString() });
        MessageCenterListActivity.this.mapiService().exec(this.deleteNotificationRequest, this);
      }
    }

    protected String emptyMessage()
    {
      return "暂无消息";
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public int getItemViewType(int paramInt)
    {
      int i = 1;
      Object localObject = getItem(paramInt);
      if (localObject == ERROR)
        paramInt = 0;
      do
      {
        return paramInt;
        paramInt = i;
      }
      while (localObject == LOADING);
      if (localObject == EMPTY)
        return 2;
      if ((localObject instanceof DPObject))
      {
        if (((DPObject)localObject).getInt("Type") == 1)
          return 3;
        return 4;
      }
      return 5;
    }

    public int getViewTypeCount()
    {
      return 6;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof MessageInfoItem))
      {
        paramView = (MessageInfoItem)paramView;
        localObject = paramView;
        if (paramView == null)
          if (paramDPObject.getInt("Type") != 1)
            break label120;
      }
      label120: for (Object localObject = (MessageInfoItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.notification_item, paramViewGroup, false); ; localObject = (MessageInfoItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.notification_item_broadcast, paramViewGroup, false))
      {
        ((MessageInfoItem)localObject).setGAString("item", paramDPObject.getString("Title"));
        ((MessageInfoItem)localObject).gaUserInfo.biz_id = ("" + paramDPObject.getInt("Type"));
        MessageCenterListActivity.this.addGAView((View)localObject, paramInt);
        ((MessageInfoItem)localObject).setNotification(paramDPObject);
        return localObject;
        paramView = null;
        break;
      }
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (MessageCenterListActivity.this.pullToRefreshListView != null)
        MessageCenterListActivity.this.pullToRefreshListView.onRefreshComplete();
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == this.mReq)
        super.onRequestFailed(paramMApiRequest, paramMApiResponse);
      do
        return;
      while (paramMApiRequest != this.deleteNotificationRequest);
      MessageCenterListActivity.this.dismissDialog();
      new AlertDialog.Builder(MessageCenterListActivity.this).setTitle("删除失败").setMessage(paramMApiResponse.message().content()).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.cancel();
        }
      }).show();
      this.deleteNotificationRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == this.mReq)
      {
        super.onRequestFinish(paramMApiRequest, paramMApiResponse);
        MessageCenterListActivity.this.modifyUnreadMessageCount();
        paramMApiRequest = new Intent("com.dianping.action.NEW_MESSAGE");
        MessageCenterListActivity.this.sendBroadcast(paramMApiRequest);
      }
      do
        return;
      while (paramMApiRequest != this.deleteNotificationRequest);
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      if ((localDPObject != null) && (localDPObject.isClass("SuccessMsg")))
      {
        remove(MessageCenterListActivity.this.mNotification);
        MessageCenterListActivity.this.dismissDialog();
      }
      while (true)
      {
        this.deleteNotificationRequest = null;
        return;
        onRequestFailed(paramMApiRequest, paramMApiResponse);
      }
    }

    public void setMessageRead(int paramInt)
    {
      if ((this.mData == null) || (this.mData.size() == 0));
      do
      {
        do
          return;
        while (!(getItem(paramInt) instanceof DPObject));
        localDPObject = (DPObject)getItem(paramInt);
      }
      while (localDPObject.getInt("UnreadCount") == 0);
      DPObject localDPObject = localDPObject.edit().putInt("UnreadCount", 0).generate();
      this.mData.remove(paramInt);
      this.mData.add(paramInt, localDPObject);
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.MessageCenterListActivity
 * JD-Core Version:    0.6.0
 */