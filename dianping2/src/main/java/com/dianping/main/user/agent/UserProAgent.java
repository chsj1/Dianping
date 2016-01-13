package com.dianping.main.user.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.user.UserProLayout;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.layout;

public class UserProAgent extends AdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String USER_PRO_TAG = "30UserPro.";
  private Adapter adapter;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      UserProAgent.this.requestUserPro();
    }
  };
  private UserProLayout userProLayout;
  private MApiRequest userProRequest;
  private DPObject[] userPros;

  public UserProAgent(Object paramObject)
  {
    super(paramObject);
  }

  private MApiRequest createRequest()
  {
    return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/daren/getuserinfo.bin").buildUpon().appendQueryParameter("userid", String.valueOf(accountService().id())).appendQueryParameter("memberid", String.valueOf(getSharedObject("memberId"))).toString(), CacheType.DISABLED);
  }

  private void requestUserPro()
  {
    if (getFragment() == null)
      return;
    if (this.userProRequest != null)
      getFragment().mapiService().abort(this.userProRequest, this, true);
    this.userProRequest = createRequest();
    getFragment().mapiService().exec(this.userProRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestUserPro();
    this.adapter = new Adapter(null);
    addCell("30UserPro.", this.adapter);
    paramBundle = new IntentFilter("daren:badgeupdate");
    getContext().registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.userProRequest != null)
    {
      getFragment().mapiService().abort(this.userProRequest, this, true);
      this.userProRequest = null;
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
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        this.userPros = paramMApiRequest.getArray("DarenIcons");
      this.adapter.notifyDataSetChanged();
    }
  }

  private class Adapter extends BaseAdapter
  {
    private Adapter()
    {
    }

    private boolean isMyself()
    {
      UserProfile localUserProfile = UserProAgent.this.getAccount();
      return (localUserProfile != null) && (localUserProfile.id() == userId());
    }

    private int userId()
    {
      if ((UserProAgent.this.getSharedObject("user") instanceof DPObject))
        return ((DPObject)UserProAgent.this.getSharedObject("user")).getInt("UserID");
      return ((Integer)UserProAgent.this.getSharedObject("memberId")).intValue();
    }

    public int getCount()
    {
      if ((UserProAgent.this.userPros != null) && (UserProAgent.this.userPros.length > 0))
        return 1;
      return 0;
    }

    public DPObject[] getItem(int paramInt)
    {
      return UserProAgent.this.userPros;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (!(paramView instanceof UserProLayout))
        localView = UserProAgent.this.res.inflate(UserProAgent.this.getContext(), R.layout.user_pro_layout, paramViewGroup, false);
      UserProAgent.access$302(UserProAgent.this, (UserProLayout)localView);
      UserProAgent.this.userProLayout.setUserPro(UserProAgent.this.userPros, isMyself());
      return UserProAgent.this.userProLayout;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.agent.UserProAgent
 * JD-Core Version:    0.6.0
 */