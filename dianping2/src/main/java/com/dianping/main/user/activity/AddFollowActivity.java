package com.dianping.main.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.EditSearchBar;
import com.dianping.base.widget.EditSearchBar.OnKeywordChangeListner;
import com.dianping.base.widget.MyDPFriendItem;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.main.user.UserListAdapter;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.KeyboardUtils.SoftKeyboardController;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddFollowActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AdapterView.OnItemClickListener, EditSearchBar.OnKeywordChangeListner
{
  private static final String TAG = AddFollowActivity.class.getSimpleName();
  SearchAdapter adapter;
  private MApiRequest addFollowRequest;
  EditSearchBar edit;
  String keyword;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      int i;
      if ("com.dianping.action.HONEY_CHANGED".equals(paramIntent.getAction()))
      {
        i = paramIntent.getIntExtra("userid", -1);
        if (i != -1)
          break label26;
      }
      while (true)
      {
        return;
        label26: boolean bool1 = paramIntent.getBooleanExtra("isAdd", false);
        boolean bool2 = paramIntent.getBooleanExtra("isDelete", false);
        int j = 0;
        while (j < AddFollowActivity.this.adapter.users.size())
        {
          if (i == ((UserProfile)AddFollowActivity.this.adapter.users.get(j)).id())
          {
            paramContext = AddFollowActivity.this.adapter.confirms;
            if (bool1)
              i = 2;
            while (true)
            {
              paramContext.set(j, Integer.valueOf(i));
              AddFollowActivity.this.adapter.notifyDataSetChanged();
              return;
              if (bool2)
              {
                i = 0;
                continue;
              }
              i = ((Integer)AddFollowActivity.this.adapter.confirms.get(j)).intValue();
            }
          }
          j += 1;
        }
      }
    }
  };
  MApiRequest searchUserRequest;
  UserProfile selectUser;
  String token;

  public void addFollow(String paramString, UserProfile paramUserProfile)
  {
    if (this.addFollowRequest != null)
    {
      Log.i(TAG, "already requesting");
      return;
    }
    this.selectUser = paramUserProfile;
    this.addFollowRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/addfollow.bin", new String[] { "token", accountService().token(), "userid", String.valueOf(paramUserProfile.id()) });
    mapiService().exec(this.addFollowRequest, this);
  }

  public void onAccountInfoChanged(UserProfile paramUserProfile)
  {
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    this.token = accountService().token();
    if (getAccount() == null)
    {
      finish();
      return;
    }
    refresh();
  }

  public void onClick(View paramView)
  {
    if ((paramView instanceof MyDPFriendItem))
    {
      paramView = ((MyDPFriendItem)paramView).user();
      if (paramView != null);
    }
    else
    {
      return;
    }
    showProgressDialog("添加关注");
    addFollow(this.token, paramView);
    statisticsEvent("profile5", "profile5_addsearch_one", "", 0, null);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject1 = getAccount();
    if (localObject1 == null)
      gotoLogin();
    Object localObject2 = new IntentFilter("com.dianping.action.HONEY_CHANGED");
    registerReceiver(this.receiver, (IntentFilter)localObject2);
    this.edit = ((EditSearchBar)findViewById(R.id.search_bar));
    this.edit.setHint("输入昵称");
    this.edit.setOnKeywordChangeListner(this);
    if (paramBundle == null)
    {
      localObject2 = getIntent().getData();
      if (localObject2 != null)
      {
        localObject2 = ((Uri)localObject2).getQueryParameter("q");
        this.edit.setKeyword((String)localObject2);
      }
    }
    if (localObject1 == null);
    for (localObject1 = null; ; localObject1 = ((UserProfile)localObject1).token())
    {
      this.token = ((String)localObject1);
      if (paramBundle != null)
        this.keyword = paramBundle.getString("keyword");
      updateTitle();
      this.adapter = new SearchAdapter();
      this.listView.setAdapter(this.adapter);
      this.listView.setOnItemClickListener(this);
      return;
    }
  }

  protected void onDestroy()
  {
    this.adapter.onFinish();
    unregisterReceiver(this.receiver);
    if (this.addFollowRequest != null)
      mapiService().abort(this.addFollowRequest, this, true);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.adapter.getItem(paramInt);
    if ((paramAdapterView instanceof UserProfile))
    {
      paramAdapterView = (UserProfile)paramAdapterView;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user"));
      paramView.putExtra("user", paramAdapterView);
      startActivity(paramView);
    }
  }

  public void onKeywordChanged(String paramString)
  {
    findViewById(16908308).setVisibility(0);
    if (this.emptyView.getChildCount() == 0)
      setEmptyMsg("未找到符合条件的用户", false);
    if ((this.keyword != null) && (this.keyword.equals(paramString)))
      return;
    searchHoney(paramString);
    statisticsEvent("profile5", "profile5_addfriend_search", "", 0);
  }

  public void onLoginCancel()
  {
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    showMessageDialog(paramMApiResponse.message());
    this.addFollowRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.addFollowRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      if ((DPObject)paramMApiResponse.result() != null)
      {
        paramMApiRequest = new Intent("com.dianping.action.HONEY_CHANGED");
        paramMApiRequest.putExtra("isAdd", true);
        paramMApiRequest.putExtra("userid", this.selectUser.id());
        sendBroadcast(paramMApiRequest);
        dismissDialog();
      }
      this.addFollowRequest = null;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.adapter.onRestoreInstanceState(paramBundle);
    updateTitle();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.adapter.onSaveInstanceState(paramBundle);
    paramBundle.putString("keyword", this.keyword);
    super.onSaveInstanceState(paramBundle);
  }

  public void refresh()
  {
    this.adapter.reset();
    updateTitle();
  }

  public void searchHoney(String paramString)
  {
    String str;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.trim().length() != 0);
    }
    else
    {
      str = null;
    }
    if (str != null)
      this.edit.setKeyword(str);
    this.keyword = str;
    this.adapter.reset();
    updateTitle();
    KeyboardUtils.getSoftKeyboardController(this.edit).hide();
  }

  protected void setEmptyView()
  {
    super.setEmptyView();
    this.emptyView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1, 48));
  }

  protected void setupView()
  {
    super.setContentView(R.layout.add_follow_frame);
  }

  public void updateTitle()
  {
    if ((this.keyword == null) || (this.keyword.length() == 0))
    {
      setTitle("添加关注");
      return;
    }
    if ((this.adapter != null) && (this.adapter.recordCount() > 0))
    {
      setTitle("共找到" + this.adapter.recordCount() + "个用户");
      return;
    }
    setTitle("添加关注");
  }

  class SearchAdapter extends UserListAdapter
  {
    public SearchAdapter()
    {
      super();
    }

    public void appendUsers(DPObject paramDPObject)
    {
      super.appendUsers(paramDPObject);
      AddFollowActivity.this.updateTitle();
      AddFollowActivity.this.setEmptyMsg(paramDPObject.getString("EmptyMsg"), true);
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
      localStringBuilder.append("searchuser.bin?");
      localStringBuilder.append("cityid=").append(AddFollowActivity.this.cityId());
      if (AddFollowActivity.this.accountService().token() != null)
        localStringBuilder.append("&token=").append(URLEncoder.encode(AddFollowActivity.this.accountService().token()));
      if (AddFollowActivity.this.keyword != null)
        localStringBuilder.append("&keyword=").append(URLEncoder.encode(AddFollowActivity.this.keyword));
      localStringBuilder.append("&start=").append(paramInt);
      AddFollowActivity.this.searchUserRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
      return AddFollowActivity.this.searchUserRequest;
    }

    public int getCount()
    {
      if ((AddFollowActivity.this.keyword == null) || (TextUtils.isEmpty(AddFollowActivity.this.keyword)))
        return 0;
      return super.getCount();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof UserProfile))
      {
        UserProfile localUserProfile = (UserProfile)localObject;
        if ((paramView instanceof MyDPFriendItem));
        for (paramView = (MyDPFriendItem)paramView; ; paramView = null)
        {
          localObject = paramView;
          if (paramView == null)
            localObject = (MyDPFriendItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.my_dp_friend_item, paramViewGroup, false);
          ((MyDPFriendItem)localObject).resetStatus();
          ((MyDPFriendItem)localObject).setButtonOnClickListener(AddFollowActivity.this);
          ((MyDPFriendItem)localObject).setMyHoney(localUserProfile, paramInt, (Integer)this.confirms.get(paramInt));
          paramView = CityUtils.getCityById(localUserProfile.cityId());
          if (paramView != null)
            ((MyDPFriendItem)localObject).setCity(paramView.name());
          return localObject;
        }
      }
      return (View)super.getView(paramInt, paramView, paramViewGroup);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.AddFollowActivity
 * JD-Core Version:    0.6.0
 */