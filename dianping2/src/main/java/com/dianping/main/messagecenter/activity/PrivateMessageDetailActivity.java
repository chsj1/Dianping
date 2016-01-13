package com.dianping.main.messagecenter.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.NotificationItem;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class PrivateMessageDetailActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  Adapter adapter;
  EditText inputBox;
  MApiService mapi;
  private String name = null;
  MApiRequest notificationRequest;
  MApiRequest sendRequest;
  private int targetid;
  String token = null;

  private void endTask(DPObject paramDPObject)
  {
    this.adapter.appendNotification(paramDPObject);
    paramDPObject = preferences().edit();
    paramDPObject.putInt("my_notification_count", 0);
    paramDPObject.commit();
  }

  void notificationTask(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
    {
      endTask(new DPObject("ResultList").edit().putInt("RecordCount", 0).putInt("StartIndex", 0).putBoolean("IsEnd", true).putArray("List", new DPObject[0]).generate());
      return;
    }
    this.mapi = ((MApiService)getService("mapi"));
    this.notificationRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/getmessages.bin", new String[] { "type", String.valueOf(3), "token", paramString, "start", String.valueOf(paramInt), "targetid", String.valueOf(this.targetid) });
    this.mapi.exec(this.notificationRequest, this);
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    super.onAccountSwitched(paramUserProfile);
    if (paramUserProfile != null)
    {
      this.token = paramUserProfile.token();
      this.adapter.reset();
      return;
    }
    finish();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.send_btn)
      sendMsg();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if ((paramBundle != null) && (paramBundle.getQueryParameter("targetid") != null))
    {
      this.targetid = Integer.parseInt(paramBundle.getQueryParameter("targetid"));
      this.name = paramBundle.getQueryParameter("name");
      setTitle(this.name);
      if (accountService().token() == null)
        break label154;
      this.token = accountService().token();
    }
    while (true)
    {
      this.inputBox = ((EditText)findViewById(R.id.input_text));
      findViewById(R.id.send_btn).setOnClickListener(this);
      this.adapter = new Adapter();
      this.listView.setAdapter(this.adapter);
      this.listView.setOnItemClickListener(this);
      setEmptyMsg("没有私信记录！", false);
      return;
      finish();
      break;
      label154: gotoLogin();
    }
  }

  protected void onDestroy()
  {
    if (this.adapter != null)
      this.adapter.onFinish();
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (this.adapter.getCount() >= paramInt)
    {
      paramAdapterView = this.adapter.getItem(paramInt);
      if ((paramAdapterView instanceof DPObject))
      {
        paramAdapterView = (DPObject)paramAdapterView;
        if ((paramAdapterView.getArray("Actions") != null) && (paramAdapterView.getArray("Actions").length > 0))
        {
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
              paramDialogInterface = PrivateMessageDetailActivity.this.adapter.getItem(this.val$position);
              if ((paramDialogInterface instanceof DPObject))
              {
                paramDialogInterface = (DPObject)paramDialogInterface;
                if ((paramDialogInterface.getArray("Actions") == null) || (paramDialogInterface.getArray("Actions").length <= paramInt));
              }
              try
              {
                paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse(paramDialogInterface.getArray("Actions")[paramInt].getString("Name")));
                PrivateMessageDetailActivity.this.startActivity(paramDialogInterface);
                return;
              }
              catch (Exception paramDialogInterface)
              {
                paramDialogInterface.printStackTrace();
              }
            }
          });
          paramView.show();
        }
      }
    }
  }

  public void onLoginCancel()
  {
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.notificationRequest)
    {
      this.adapter.setError(paramMApiResponse.message().content());
      this.notificationRequest = null;
    }
    if (paramMApiRequest == this.sendRequest)
    {
      this.sendRequest = null;
      new AlertDialog.Builder(this).setMessage("发送失败！").setPositiveButton("重发", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PrivateMessageDetailActivity.this.sendMsg();
        }
      }).setNegativeButton("取消 ", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PrivateMessageDetailActivity.this.inputBox.setText("");
        }
      }).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    DPObject localDPObject;
    if (paramMApiRequest == this.notificationRequest)
    {
      localDPObject = (DPObject)paramMApiResponse.result();
      if (localDPObject == null)
        onRequestFailed(paramMApiRequest, paramMApiResponse);
    }
    do
    {
      return;
      this.adapter.appendNotification(localDPObject);
      paramMApiResponse = preferences().edit();
      paramMApiResponse.putInt("my_notification_count", 0);
      paramMApiResponse.commit();
      this.notificationRequest = null;
    }
    while (paramMApiRequest != this.sendRequest);
    long l = new Date().getTime();
    paramMApiRequest = new DPObject("Notification").edit().putInt("Uid", 0).putInt("UserId", getUserId()).putString("Name", getAccount().nickName()).putString("Image", getAccount().avatar()).putString("Content", "").putInt("ContentStyle", 0).putString("Quote", this.inputBox.getText().toString()).putTime("Time", l).generate();
    this.adapter.notifications.add(paramMApiRequest);
    this.adapter.nextStartIndex = this.adapter.notifications.size();
    this.adapter.notifyDataSetChanged();
    if (this.adapter.notifications.size() > 0)
      this.listView.setSelection(this.adapter.notifications.size() - 1);
    this.inputBox.setText("");
    this.sendRequest = null;
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.adapter.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.adapter.onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  void sendMsg()
  {
    if (this.sendRequest != null);
    String str;
    do
    {
      return;
      str = this.inputBox.getText().toString();
    }
    while (str.length() == 0);
    this.mapi = ((MApiService)getService("mapi"));
    this.sendRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/sendprivatemsg.bin", new String[] { "targetid", String.valueOf(this.targetid), "token", this.token, "content", str });
    this.mapi.exec(this.sendRequest, this);
  }

  protected void setEmptyView()
  {
    super.setEmptyView();
    this.emptyView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1, 48));
  }

  protected void setupView()
  {
    super.setContentView(R.layout.list_frame_for_msg_detail);
  }

  class Adapter extends BasicAdapter
  {
    protected String errorMsg;
    boolean isEnd;
    int nextStartIndex;
    ArrayList<DPObject> notifications = new ArrayList();
    int recordCount = -1;

    Adapter()
    {
    }

    public void appendNotification(DPObject paramDPObject)
    {
      if (paramDPObject.getInt("StartIndex") == this.nextStartIndex)
      {
        PrivateMessageDetailActivity.this.setEmptyMsg(paramDPObject.getString("EmptyMsg"), true);
        if ((paramDPObject.getArray("List") != null) && (paramDPObject.getArray("List").length > 0))
          this.notifications.addAll(Arrays.asList(paramDPObject.getArray("List")));
        this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.isEnd = paramDPObject.getBoolean("IsEnd");
        this.recordCount = paramDPObject.getInt("RecordCount");
        notifyDataSetChanged();
        if (PrivateMessageDetailActivity.this.adapter.notifications.size() > 0)
          PrivateMessageDetailActivity.this.listView.setSelection(PrivateMessageDetailActivity.this.adapter.notifications.size() - 1);
      }
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.notifications.size();
      return this.notifications.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.notifications.size())
        return this.notifications.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
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
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        DPObject localDPObject = (DPObject)localObject;
        if ((paramView instanceof NotificationItem))
        {
          paramView = (NotificationItem)paramView;
          if ((PrivateMessageDetailActivity.this.getAccount() == null) || (localDPObject.getInt("UserId") != PrivateMessageDetailActivity.this.getUserId()))
            break label116;
          if (paramView != null)
          {
            localObject = paramView;
            if (paramView.getIsTo());
          }
          else
          {
            localObject = (NotificationItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.private_message_item_r, paramViewGroup, false);
            ((NotificationItem)localObject).setIsTo(true);
          }
        }
        while (true)
        {
          ((NotificationItem)localObject).setNotification(localDPObject);
          return localObject;
          paramView = null;
          break;
          label116: localObject = paramView;
          if (PrivateMessageDetailActivity.this.getAccount() == null)
            continue;
          localObject = paramView;
          if (localDPObject.getInt("UserId") == PrivateMessageDetailActivity.this.getUserId())
            continue;
          if (paramView != null)
          {
            localObject = paramView;
            if (!paramView.getIsTo())
              continue;
          }
          localObject = (NotificationItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.private_message_item, paramViewGroup, false);
          ((NotificationItem)localObject).setIsTo(false);
        }
      }
      if (localObject == LOADING)
      {
        if (this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          PrivateMessageDetailActivity.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public boolean loadNewPage()
    {
      if (this.isEnd);
      do
        return false;
      while (PrivateMessageDetailActivity.this.notificationRequest != null);
      this.errorMsg = null;
      PrivateMessageDetailActivity.this.notificationTask(PrivateMessageDetailActivity.this.token, this.nextStartIndex);
      notifyDataSetChanged();
      return true;
    }

    public void onFinish()
    {
      if (PrivateMessageDetailActivity.this.notificationRequest != null)
        PrivateMessageDetailActivity.this.mapi.abort(PrivateMessageDetailActivity.this.notificationRequest, null, true);
      if (PrivateMessageDetailActivity.this.sendRequest != null)
        PrivateMessageDetailActivity.this.mapi.abort(PrivateMessageDetailActivity.this.sendRequest, null, true);
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      this.notifications = paramBundle.getParcelableArrayList("notifications");
      if (this.notifications == null)
        this.notifications = new ArrayList();
      this.nextStartIndex = paramBundle.getInt("nextStartIndex");
      this.isEnd = paramBundle.getBoolean("isEnd");
      this.recordCount = paramBundle.getInt("recordCount");
      this.errorMsg = paramBundle.getString("error");
      notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      paramBundle.putParcelableArrayList("notifications", this.notifications);
      paramBundle.putInt("nextStartIndex", this.nextStartIndex);
      paramBundle.putBoolean("isEnd", this.isEnd);
      paramBundle.putInt("recordCount", this.recordCount);
      paramBundle.putString("error", this.errorMsg);
    }

    public void reset()
    {
      onFinish();
      this.notifications = new ArrayList();
      this.nextStartIndex = 0;
      this.isEnd = false;
      this.recordCount = -1;
      this.errorMsg = null;
      notifyDataSetChanged();
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.PrivateMessageDetailActivity
 * JD-Core Version:    0.6.0
 */