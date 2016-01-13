package com.dianping.main.user.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.user.UserProfileHeadLayout;
import com.dianping.main.user.UserProfileHeadLayout.OnFollowListener;
import com.dianping.main.user.UserProfileHeadLayout.OnFollowedListener;
import com.dianping.main.user.UserProfileHeadLayout.onMessageListener;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.layout;

public class UserProfileAgent extends AdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String USER_PROFILE_TAG = "10UserProfile.";
  private Adapter adapter;
  UserProfileHeadLayout.OnFollowListener followClickListener = new UserProfileHeadLayout.OnFollowListener()
  {
    public void onFollowClickListener()
    {
      AccountService localAccountService = UserProfileAgent.this.getFragment().accountService();
      if (localAccountService.token() == null)
      {
        localAccountService.login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            if (UserProfileAgent.this.adapter != null)
              UserProfileAgent.this.adapter.notifyDataSetChanged();
            UserProfileAgent.2.this.onFollowClickListener();
          }
        });
        return;
      }
      UserProfileAgent.access$302(UserProfileAgent.this, BasicMApiRequest.mapiPost("http://m.api.dianping.com/addfollow.bin", new String[] { "token", localAccountService.token(), "userid", String.valueOf(UserProfileAgent.this.getSharedObject("memberId")) }));
      UserProfileAgent.this.mapiService().exec(UserProfileAgent.this.followRequest, UserProfileAgent.this);
    }
  };
  private MApiRequest followRequest;
  UserProfileHeadLayout.OnFollowedListener followedClickListener = new UserProfileHeadLayout.OnFollowedListener()
  {
    public void onFollowedClickListener()
    {
      AccountService localAccountService = UserProfileAgent.this.getFragment().accountService();
      if (localAccountService.token() == null)
      {
        localAccountService.login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            if (UserProfileAgent.this.adapter != null)
              UserProfileAgent.this.adapter.notifyDataSetChanged();
            UserProfileAgent.3.this.onFollowedClickListener();
          }
        });
        return;
      }
      UserProfileAgent.this.showSimpleAlertDialog("提示", "确定要解除关注吗", "确定", new DialogInterface.OnClickListener(localAccountService)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          UserProfileAgent.access$402(UserProfileAgent.this, BasicMApiRequest.mapiPost("http://m.api.dianping.com/removefollow.bin", new String[] { "token", this.val$accountService.token(), "userid", String.valueOf(UserProfileAgent.this.getSharedObject("memberId")) }));
          UserProfileAgent.this.mapiService().exec(UserProfileAgent.this.followedRequest, UserProfileAgent.this);
        }
      }
      , "取消", null);
    }
  };
  private MApiRequest followedRequest;
  private UserProfileHeadLayout.onMessageListener messageClickListener = new UserProfileHeadLayout.onMessageListener()
  {
    public void onMessageClickListener()
    {
      Object localObject = UserProfileAgent.this.getFragment().accountService();
      if (((AccountService)localObject).token() == null)
        ((AccountService)localObject).login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            if (UserProfileAgent.this.adapter != null)
              UserProfileAgent.this.adapter.notifyDataSetChanged();
            UserProfileAgent.1.this.onMessageClickListener();
          }
        });
      do
        return;
      while (UserProfileAgent.this.isMyself());
      Uri.Builder localBuilder = Uri.parse("dianping://privatemsgdetail").buildUpon().appendQueryParameter("targetid", String.valueOf(UserProfileAgent.this.userId()));
      if ((UserProfileAgent.this.getSharedObject("user") instanceof DPObject));
      String str;
      for (localObject = ((DPObject)UserProfileAgent.this.getSharedObject("user")).getString("Nick"); ; str = "")
      {
        localObject = localBuilder.appendQueryParameter("name", (String)localObject).build();
        try
        {
          UserProfileAgent.this.startActivity(((Uri)localObject).toString());
          return;
        }
        catch (Exception localException)
        {
          return;
        }
      }
    }
  };
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.USER_EDIT".equals(paramIntent.getAction()))
        UserProfileAgent.this.requestUserProfile();
    }
  };
  private DPObject userObject;
  private UserProfileHeadLayout userProfileHeadLayout;
  private MApiRequest userProfileRequest;

  public UserProfileAgent(Object paramObject)
  {
    super(paramObject);
  }

  private MApiRequest createRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/getprofileuser.bin").buildUpon();
    if (getAccount() == null);
    for (String str = null; ; str = accountService().token())
      return BasicMApiRequest.mapiGet(localBuilder.appendQueryParameter("token", str).appendQueryParameter("userid", String.valueOf(getSharedObject("memberId"))).toString(), CacheType.DISABLED);
  }

  private boolean isMyself()
  {
    UserProfile localUserProfile = getAccount();
    return (localUserProfile != null) && (localUserProfile.id() == userId());
  }

  private void requestUserProfile()
  {
    if (getFragment() == null)
      return;
    if (this.userProfileRequest != null)
      getFragment().mapiService().abort(this.userProfileRequest, this, true);
    this.userProfileRequest = createRequest();
    getFragment().mapiService().exec(this.userProfileRequest, this);
  }

  private int userId()
  {
    if ((getSharedObject("user") instanceof UserProfile))
      return ((UserProfile)getSharedObject("user")).id();
    return ((Integer)getSharedObject("memberId")).intValue();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestUserProfile();
    this.adapter = new Adapter(null);
    addCell("10UserProfile.", this.adapter);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.USER_EDIT");
    getContext().registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.userProfileRequest != null)
    {
      getFragment().mapiService().abort(this.userProfileRequest, this, true);
      this.userProfileRequest = null;
    }
    getContext().unregisterReceiver(this.receiver);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((getContext() != null) && (paramMApiResponse.message() != null))
      Toast.makeText(getContext(), paramMApiResponse.message().toString(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.userProfileRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.userObject = ((DPObject)paramMApiResponse.result());
      if (this.userProfileHeadLayout != null)
        this.userProfileHeadLayout.setUser(this.userObject, accountService().id());
      getFragment().setSharedObject("user", this.userObject);
      paramMApiRequest = new Intent("com.dianping.user.UPDATE_COUNT");
      getContext().sendBroadcast(paramMApiRequest);
    }
    do
    {
      return;
      if ((paramMApiRequest != this.followRequest) || (!(paramMApiResponse.result() instanceof DPObject)))
        continue;
      paramMApiRequest = new Intent("com.dianping.action.HONEY_CHANGED");
      paramMApiRequest.putExtra("userid", userId());
      paramMApiRequest.putExtra("isAdd", true);
      getContext().sendBroadcast(paramMApiRequest);
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        Toast.makeText(getContext(), paramMApiRequest.getString("Content"), 0).show();
      this.userProfileHeadLayout.setFollowedStatus();
      this.userObject = this.userObject.edit().putInt("FansCount", this.userObject.getInt("FansCount") + 1).generate();
      this.userProfileHeadLayout.setFansCount(this.userObject.getInt("FansCount"));
      return;
    }
    while ((paramMApiRequest != this.followedRequest) || (!(paramMApiResponse.result() instanceof DPObject)));
    paramMApiRequest = new Intent("com.dianping.action.HONEY_CHANGED");
    paramMApiRequest.putExtra("userid", userId());
    paramMApiRequest.putExtra("isDelete", true);
    getContext().sendBroadcast(paramMApiRequest);
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      Toast.makeText(getContext(), paramMApiRequest.getString("Content"), 0).show();
    this.userProfileHeadLayout.setFollowStatus();
    this.userObject = this.userObject.edit().putInt("FansCount", Math.max(0, this.userObject.getInt("FansCount") - 1)).generate();
    this.userProfileHeadLayout.setFansCount(this.userObject.getInt("FansCount"));
  }

  private class Adapter extends BaseAdapter
  {
    private Adapter()
    {
    }

    public int getCount()
    {
      return 1;
    }

    public DPObject getItem(int paramInt)
    {
      return UserProfileAgent.this.userObject;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (UserProfileAgent.this.userProfileHeadLayout == null)
      {
        UserProfileAgent.access$802(UserProfileAgent.this, (UserProfileHeadLayout)UserProfileAgent.this.res.inflate(UserProfileAgent.this.getContext(), R.layout.user_profile_head_layout, paramViewGroup, false));
        UserProfileAgent.this.userProfileHeadLayout.setUser(getItem(paramInt), UserProfileAgent.this.accountService().id());
        UserProfileAgent.this.userProfileHeadLayout.setOnMessageListener(UserProfileAgent.this.messageClickListener);
        UserProfileAgent.this.userProfileHeadLayout.setOnFollowListener(UserProfileAgent.this.followClickListener);
        UserProfileAgent.this.userProfileHeadLayout.setOnFollowedListener(UserProfileAgent.this.followedClickListener);
      }
      return UserProfileAgent.this.userProfileHeadLayout;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserProfileAgent
 * JD-Core Version:    0.6.0
 */