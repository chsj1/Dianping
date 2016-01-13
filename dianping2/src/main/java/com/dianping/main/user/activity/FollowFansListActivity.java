package com.dianping.main.user.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.array;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;

public class FollowFansListActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
  public static final int FANS_LIST = 1;
  public static final int FOLLOW_LIST = 0;
  String emptyMsg;
  MApiRequest fansRequest;
  boolean isFansRequesting = false;
  boolean isRemoveRequesting = false;
  private int mFilter;
  private Adapter mFollowAdapter;
  int mUserId = 0;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.HONEY_CHANGED".equals(paramIntent.getAction()))
        FollowFansListActivity.this.refresh();
    }
  };
  MApiRequest removeRequest;
  UserProfile removeUser;
  int requestNum = 0;

  private void cancelRemoveRequest()
  {
    if (this.removeRequest == null)
      return;
    this.isRemoveRequesting = false;
    mapiService().abort(this.removeRequest, this, true);
  }

  private void initEmptyMsg()
  {
    if (this.mFilter == 0)
      if (this.mUserId > 0)
        setEmptyMsg("Ta还没有关注", false);
    do
    {
      return;
      setEmptyMsg("哎呀，还没有关注？看不到精彩的签到啦，点击“添加关注”试试吧！", false);
      return;
    }
    while (this.mFilter != 1);
    if (this.mUserId > 0)
    {
      setEmptyMsg("Ta还没有粉丝，真可惜……", false);
      return;
    }
    setEmptyMsg("哎呀，还没有人关注你？邀请好友成为你的粉丝一起来签到得徽章啦！", false);
  }

  void cancelFollowListRequest()
  {
    if (this.fansRequest == null)
      return;
    mapiService().abort(this.fansRequest, this, true);
    this.isFansRequesting = false;
  }

  public void clearListCache(int paramInt1, String paramString, int paramInt2)
  {
    if (this.mFilter == 1);
    for (paramString = "http://m.api.dianping.com/fanslist.bin?userid=" + paramInt1 + "&token=" + paramString + "&start=" + paramInt2; ; paramString = "http://m.api.dianping.com/followlist.bin?userid=" + paramInt1 + "&token=" + paramString + "&start=" + paramInt2)
    {
      mapiCacheService().remove(BasicMApiRequest.mapiGet(paramString, CacheType.NORMAL));
      return;
    }
  }

  void confirmRemoveFollow(UserProfile paramUserProfile)
  {
    new AlertDialog.Builder(this).setTitle("取消关注").setMessage("确认取消关注" + paramUserProfile.nickName()).setPositiveButton("确认", new DialogInterface.OnClickListener(paramUserProfile)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        FollowFansListActivity.this.showProgressDialog("取消关注");
        FollowFansListActivity localFollowFansListActivity = FollowFansListActivity.this;
        if (FollowFansListActivity.this.getAccount() == null);
        for (paramDialogInterface = ""; ; paramDialogInterface = FollowFansListActivity.this.accountService().token())
        {
          localFollowFansListActivity.sendRemoveRequest(paramDialogInterface, this.val$user);
          return;
        }
      }
    }).setNegativeButton("取消", null).create().show();
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (this.mFollowAdapter != null)
      this.mFollowAdapter.reset();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject;
    if (paramBundle == null)
    {
      paramBundle = getIntent();
      localObject = paramBundle.getData();
      if (localObject == null)
      {
        this.mUserId = paramBundle.getIntExtra("user_id", 0);
        this.mFilter = paramBundle.getIntExtra("filter", 0);
        if ((this.mUserId == 0) && (getAccount() == null))
          gotoLogin();
        if (this.mUserId <= 0)
          break label372;
        localObject = "Ta";
        label73: paramBundle = "关注";
        if (this.mFilter != 0)
          break label379;
        paramBundle = "关注";
      }
    }
    while (true)
    {
      while (true)
      {
        setTitle((String)localObject + "的" + paramBundle);
        initEmptyMsg();
        if ((this.mFilter == 0) && (this.mUserId == 0))
          setRightTitleButton(R.drawable.myadd_u, new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              FollowFansListActivity.this.statisticsEvent("profile5", "profile5_addfriend", "", 0);
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://addhoney"));
              FollowFansListActivity.this.startActivity(paramView);
            }
          });
        this.mFollowAdapter = new Adapter();
        this.listView.setAdapter(this.mFollowAdapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        paramBundle = new IntentFilter("com.dianping.action.HONEY_CHANGED");
        registerReceiver(this.receiver, paramBundle);
        return;
        try
        {
          this.mUserId = Integer.parseInt(((Uri)localObject).getQueryParameter("userid"));
        }
        catch (java.lang.NumberFormatException paramBundle)
        {
          try
          {
            while (true)
            {
              this.mFilter = Integer.parseInt(((Uri)localObject).getQueryParameter("filter"));
              if ("myfans".equals(((Uri)localObject).getHost()))
              {
                this.mFilter = 1;
                if (getAccount() != null)
                  break label327;
                paramBundle = "";
                clearListCache(0, paramBundle, 0);
              }
              if (!"myhoney".equals(((Uri)localObject).getHost()))
                break;
              this.mFilter = 0;
              if (getAccount() != null)
                break label338;
              paramBundle = "";
              clearListCache(0, paramBundle, 0);
              break;
              paramBundle = paramBundle;
              this.mUserId = 0;
            }
          }
          catch (java.lang.NumberFormatException paramBundle)
          {
            while (true)
            {
              this.mFilter = 0;
              continue;
              label327: paramBundle = getAccount().token();
              continue;
              label338: paramBundle = getAccount().token();
            }
          }
        }
      }
      this.mUserId = paramBundle.getInt("user_id");
      this.mFilter = paramBundle.getInt("filter");
      break;
      label372: localObject = "我";
      break label73;
      label379: if (this.mFilter != 1)
        continue;
      paramBundle = "粉丝";
    }
  }

  protected void onDestroy()
  {
    this.mFollowAdapter.onFinish();
    cancelRemoveRequest();
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.mFollowAdapter.getItem(paramInt);
    if ((paramAdapterView instanceof UserProfile))
      viewUser((UserProfile)paramAdapterView);
  }

  public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((this.mUserId > 0) || (this.mFilter == 1))
      return false;
    paramAdapterView = this.mFollowAdapter.getItem(paramInt);
    if ((paramAdapterView instanceof UserProfile))
    {
      paramAdapterView = (UserProfile)paramAdapterView;
      paramView = new AlertDialog.Builder(this);
      paramView.setTitle("关注");
      paramView.setItems(R.array.select_follow_items, new DialogInterface.OnClickListener(paramAdapterView)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          if (paramInt == 0)
            FollowFansListActivity.this.viewUser(this.val$user);
          do
            return;
          while (paramInt != 1);
          FollowFansListActivity.this.confirmRemoveFollow(this.val$user);
        }
      });
      paramView.show();
      return true;
    }
    return false;
  }

  public void onLoginCancel()
  {
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.fansRequest)
    {
      this.isFansRequesting = false;
      this.mFollowAdapter.setError(paramMApiResponse.message().content());
    }
    if (paramMApiRequest == this.removeRequest)
      this.isRemoveRequesting = false;
    dismissDialog();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.fansRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.isFansRequesting = false;
      Object localObject = (DPObject)paramMApiResponse.result();
      if (localObject == null)
        break label266;
      this.mFollowAdapter.appendUsers((DPObject)localObject);
      localObject = DPActivity.preferences(this).edit();
      ((SharedPreferences.Editor)localObject).putInt("fans_count", 0);
      ((SharedPreferences.Editor)localObject).commit();
      if (accountService().token() != null)
      {
        localObject = new Intent("com.dianping.action.NEW_MESSAGE");
        ((Intent)localObject).putExtra("fansCount", 0);
        sendBroadcast((Intent)localObject);
      }
    }
    int i;
    if ((paramMApiRequest == this.removeRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.isRemoveRequesting = false;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
      {
        dismissDialog();
        Toast.makeText(this, paramMApiRequest.getString("Title") + ":" + paramMApiRequest.getString("Content"), 0).show();
        paramMApiRequest = new Intent("com.dianping.action.HONEY_CHANGED");
        paramMApiRequest.putExtra("user", this.removeUser);
        paramMApiRequest.putExtra("isDelete", true);
        sendBroadcast(paramMApiRequest);
        dismissDialog();
        i = this.mUserId;
        if (getAccount() != null)
          break label285;
      }
    }
    label266: label285: for (paramMApiRequest = ""; ; paramMApiRequest = accountService().token())
    {
      clearListCache(i, paramMApiRequest, 0);
      return;
      this.mFollowAdapter.setError(paramMApiResponse.message().content());
      break;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.emptyMsg = paramBundle.getString("emptyMsg");
    this.mFollowAdapter.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mUserId > 0)
      paramBundle.putInt("user_id", this.mUserId);
    paramBundle.putInt("filter", this.mFilter);
    paramBundle.putString("emptyMsg", this.emptyMsg);
    this.mFollowAdapter.onSaveInstanceState(paramBundle);
  }

  void refresh()
  {
    cancelRemoveRequest();
    int i;
    if (this.mFilter == 0)
    {
      i = this.mUserId;
      if (getAccount() != null)
        break label42;
    }
    label42: for (String str = ""; ; str = getAccount().token())
    {
      clearListCache(i, str, 0);
      this.mFollowAdapter.reset();
      return;
    }
  }

  void sendFollowListRequest(int paramInt1, String paramString, int paramInt2)
  {
    if (this.mFilter == 1);
    for (paramString = "http://m.api.dianping.com/fanslist.bin?userid=" + paramInt1 + "&token=" + paramString + "&start=" + paramInt2; ; paramString = "http://m.api.dianping.com/followlist.bin?userid=" + paramInt1 + "&token=" + paramString + "&start=" + paramInt2)
    {
      this.fansRequest = BasicMApiRequest.mapiGet(paramString, CacheType.DISABLED);
      mapiService().exec(this.fansRequest, this);
      this.isFansRequesting = true;
      return;
    }
  }

  void sendRemoveRequest(String paramString, UserProfile paramUserProfile)
  {
    this.removeUser = paramUserProfile;
    this.removeRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/removefollow.bin", new String[] { "token", paramString, "userid", String.valueOf(paramUserProfile.id()) });
    mapiService().exec(this.removeRequest, this);
    this.isRemoveRequesting = true;
  }

  protected void setupView()
  {
    super.setContentView(R.layout.list_follow_fans);
  }

  void viewUser(UserProfile paramUserProfile)
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://user?userid=" + paramUserProfile.id())));
  }

  class Adapter extends BasicAdapter
  {
    protected String errorMsg;
    boolean isEnd;
    StringBuilder mStringBuilder = new StringBuilder();
    int nextStartIndex;
    int recordCount = -1;
    ArrayList<UserProfile> users = new ArrayList();

    Adapter()
    {
    }

    public void appendUsers(DPObject paramDPObject)
    {
      if ((paramDPObject.getInt("StartIndex") == this.nextStartIndex) && (paramDPObject.getArray("List") != null))
      {
        FollowFansListActivity.this.setEmptyMsg(paramDPObject.getString("EmptyMsg"), true);
        DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
        int j = arrayOfDPObject.length;
        int i = 0;
        while (true)
          if (i < j)
          {
            Object localObject = arrayOfDPObject[i];
            try
            {
              localObject = (UserProfile)((DPObject)localObject).decodeToObject(UserProfile.DECODER);
              this.users.add(localObject);
              i += 1;
            }
            catch (Exception localException)
            {
              while (true)
                localException.printStackTrace();
            }
          }
        this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.isEnd = paramDPObject.getBoolean("IsEnd");
        this.recordCount = paramDPObject.getInt("RecordCount");
        FollowFansListActivity.this.emptyMsg = paramDPObject.getString("EmptyMsg");
        notifyDataSetChanged();
      }
    }

    public boolean areAllItemsEnabled()
    {
      return true;
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.users.size();
      return this.users.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.users.size())
        return this.users.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof UserProfile))
        return ((UserProfile)localObject).id();
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof UserProfile))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof UserProfile))
      {
        localObject = (UserProfile)localObject;
        if ((paramView == null) || (paramView.getTag() != this))
        {
          paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.follow_fans_info, paramViewGroup, false);
          paramView.setTag(this);
        }
        while (true)
        {
          ((NetworkThumbView)paramView.findViewById(16908294)).setImage(((UserProfile)localObject).avatar());
          ((TextView)paramView.findViewById(R.id.nick_name)).setText(((UserProfile)localObject).nickName());
          paramViewGroup = this.mStringBuilder;
          paramViewGroup.delete(0, this.mStringBuilder.length());
          paramViewGroup.append("签到");
          paramViewGroup.append(((UserProfile)localObject).checkinCount());
          paramViewGroup.append("次");
          ((TextView)paramView.findViewById(R.id.description)).setText(paramViewGroup.toString());
          return paramView;
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
          FollowFansListActivity.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean hasStableIds()
    {
      return true;
    }

    public boolean loadNewPage()
    {
      if (this.isEnd)
        return false;
      FollowFansListActivity localFollowFansListActivity = FollowFansListActivity.this;
      int i = FollowFansListActivity.this.mUserId;
      if (FollowFansListActivity.this.getAccount() == null);
      for (String str = ""; ; str = FollowFansListActivity.this.accountService().token())
      {
        localFollowFansListActivity.sendFollowListRequest(i, str, this.nextStartIndex);
        return true;
      }
    }

    public void onFinish()
    {
      if (FollowFansListActivity.this.isFansRequesting)
        FollowFansListActivity.this.cancelFollowListRequest();
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      this.users = paramBundle.getParcelableArrayList("users");
      this.nextStartIndex = paramBundle.getInt("nextStartIndex");
      this.isEnd = paramBundle.getBoolean("isEnd");
      this.recordCount = paramBundle.getInt("recordCount");
      this.errorMsg = paramBundle.getString("error");
      notifyDataSetChanged();
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      paramBundle.putParcelableArrayList("users", this.users);
      paramBundle.putInt("nextStartIndex", this.nextStartIndex);
      paramBundle.putBoolean("isEnd", this.isEnd);
      paramBundle.putInt("recordCount", this.recordCount);
      paramBundle.putString("error", this.errorMsg);
    }

    public void reset()
    {
      onFinish();
      this.users = new ArrayList();
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
 * Qualified Name:     com.dianping.main.user.activity.FollowFansListActivity
 * JD-Core Version:    0.6.0
 */