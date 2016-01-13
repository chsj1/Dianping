package com.dianping.main.user.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.main.user.agent.app.UserAgent;
import com.dianping.model.SimpleMsg;

public class UserReceiverAgent extends UserAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final String ACTION_MY_PROFILE_EDIT = "com.dianping.action.PROFILE_EDIT";
  public static final String ACTION_MY_RESIDENCE_CHANGED = "com.dianping.action.RESIDENCE_CHANGE";
  public static final String ACTION_RED_ALERT_REFRESH = "com.dianping.action.RedAlerts";
  public static final String ACTION_UPDATE_AVATAR = "com.dianping.main.user.UPDATE_AVATAR";
  Broadcast[] broadcasts = { new Broadcast("com.dianping.action.PROFILE_EDIT")
  {
    public void onActionCome(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getStringExtra("newUserName");
      if (!TextUtils.isEmpty(paramContext))
      {
        Object localObject = new Bundle();
        ((Bundle)localObject).putString("newUserName", paramContext);
        UserReceiverAgent.this.dispatchAgentChanged("me/userinfoheader", (Bundle)localObject);
        if (UserReceiverAgent.this.token() != null)
        {
          localObject = UserReceiverAgent.this.accountService().profile();
          if (localObject == null)
            break label198;
          paramContext = ((DPObject)localObject).edit().putString("NickName", paramContext).generate();
          UserReceiverAgent.this.accountService().update(paramContext);
        }
      }
      while (true)
      {
        int i = paramIntent.getIntExtra("newUserGenderid", -1);
        if (i != -1)
        {
          paramContext = new Bundle();
          paramContext.putInt("newUserGenderid", i);
          UserReceiverAgent.this.dispatchAgentChanged("me/userinfoheader", paramContext);
          if (UserReceiverAgent.this.token() != null)
          {
            paramContext = UserReceiverAgent.this.accountService().profile();
            if (paramContext == null)
              break;
            paramContext = paramContext.edit().putInt("Gender", i).generate();
            UserReceiverAgent.this.accountService().update(paramContext);
          }
        }
        return;
        label198: UserReceiverAgent.this.refreshProfile(true);
      }
      UserReceiverAgent.this.refreshProfile(true);
    }
  }
  , new Broadcast("com.dianping.action.RESIDENCE_CHANGE")
  {
    public void onActionCome(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getStringExtra("cityName");
      if (paramContext != null)
      {
        Bundle localBundle = new Bundle();
        localBundle.putString("cityName", paramContext);
        UserReceiverAgent.this.dispatchAgentChanged("me/userinfoheader", localBundle);
        if (UserReceiverAgent.this.accountService().token() != null)
        {
          paramContext = UserReceiverAgent.this.accountService().profile().edit().putInt("CityID", paramIntent.getIntExtra("cityId", 0)).generate();
          UserReceiverAgent.this.accountService().update(paramContext);
        }
      }
    }
  }
  , new Broadcast("com.dianping.main.user.UPDATE_AVATAR")
  {
    public void onActionCome(Context paramContext, Intent paramIntent)
    {
      UserReceiverAgent.this.refreshProfile(true);
    }
  }
  , new Broadcast("com.dianping.action.RedAlerts")
  {
    public void onActionCome(Context paramContext, Intent paramIntent)
    {
      UserReceiverAgent.this.dispatchAgentChanged("me/userextrainfo", new Bundle());
    }
  }
   };
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      UserReceiverAgent.Broadcast[] arrayOfBroadcast = UserReceiverAgent.this.broadcasts;
      int j = arrayOfBroadcast.length;
      int i = 0;
      while (true)
      {
        if (i < j)
        {
          UserReceiverAgent.Broadcast localBroadcast = arrayOfBroadcast[i];
          if (localBroadcast.getAction().equals(paramIntent.getAction()))
            localBroadcast.onActionCome(paramContext, paramIntent);
        }
        else
        {
          return;
        }
        i += 1;
      }
    }
  };
  private MApiRequest userProfilereq;

  public UserReceiverAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void registerBroadcast(BroadcastReceiver paramBroadcastReceiver)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    Broadcast[] arrayOfBroadcast = this.broadcasts;
    int j = arrayOfBroadcast.length;
    int i = 0;
    while (i < j)
    {
      localIntentFilter.addAction(arrayOfBroadcast[i].getAction());
      i += 1;
    }
    getContext().registerReceiver(paramBroadcastReceiver, localIntentFilter);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    registerBroadcast(this.receiver);
    if (isLogin())
      refreshProfile(false);
  }

  public void onDestroy()
  {
    getContext().unregisterReceiver(this.receiver);
    if (this.userProfilereq != null)
    {
      mapiService().abort(this.userProfilereq, this, true);
      this.userProfilereq = null;
    }
    super.onDestroy();
  }

  public void onRefresh()
  {
    if (isLogin())
    {
      onRefreshRequestStart();
      refreshProfile(false);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.userProfilereq)
    {
      if (paramMApiResponse.message() != null)
        showToast(paramMApiResponse.message().toString());
      this.userProfilereq = null;
      onRefreshRequestFinish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.userProfilereq)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        accountService().update((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(true);
      }
      this.userProfilereq = null;
      onRefreshRequestFinish();
    }
  }

  void refreshProfile(boolean paramBoolean)
  {
    if (TextUtils.isEmpty(token()))
      return;
    if (this.userProfilereq != null)
      mapiService().abort(this.userProfilereq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/user.bin").buildUpon().appendQueryParameter("token", token()).appendQueryParameter("userid", "0");
    if (paramBoolean)
      localBuilder.appendQueryParameter("refresh", "true");
    this.userProfilereq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.userProfilereq, this);
  }

  class Broadcast
  {
    String mAction;

    public Broadcast(String arg2)
    {
      Object localObject;
      this.mAction = localObject;
    }

    public String getAction()
    {
      return this.mAction;
    }

    public void onActionCome(Context paramContext, Intent paramIntent)
    {
      UserReceiverAgent.this.refreshProfile(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserReceiverAgent
 * JD-Core Version:    0.6.0
 */