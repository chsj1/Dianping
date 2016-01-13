package com.dianping.main.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CircleImageView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.UserProfileItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserLevel;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class UserZoneActivity extends NovaActivity
  implements View.OnClickListener, TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>, AccountListener
{
  public static final String ACTION_COMMENT = "com.dianping.action.COMMENT";
  boolean addHoney = false;
  TableView infoTab;
  boolean isReceiverRegistered = false;
  UserProfileItem mDianPingLay;
  View mFansLay;
  TextView mFansNumText;
  UserProfileItem mFavoriteLay;
  View mHoneyLay;
  TextView mmHoneyNumText;
  MApiRequest operateReq;
  int preUserId;
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
      label26: 
      do
        return;
      while (i != UserZoneActivity.this.userId());
      UserZoneActivity.this.refresh(false);
    }
  };
  boolean retrieved;
  View.OnClickListener sendMsgBtnListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (UserZoneActivity.this.userId() == 0);
      do
      {
        return;
        if (UserZoneActivity.this.accountService().token() != null)
          continue;
        UserZoneActivity.this.gotoLogin();
        return;
      }
      while (UserZoneActivity.this.isMyself());
      Uri.Builder localBuilder = Uri.parse("dianping://privatemsgdetail").buildUpon().appendQueryParameter("targetid", String.valueOf(UserZoneActivity.this.userId()));
      if (UserZoneActivity.this.user == null);
      for (paramView = ""; ; paramView = UserZoneActivity.this.user.nickName())
      {
        paramView = localBuilder.appendQueryParameter("name", paramView).build();
        try
        {
          UserZoneActivity.this.startActivity(paramView.toString());
          return;
        }
        catch (java.lang.Exception paramView)
        {
          return;
        }
      }
    }
  };
  View.OnClickListener titleBtnChangeListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      int k = UserZoneActivity.this.userId();
      if (k == 0)
        return;
      if (UserZoneActivity.this.accountService().token() == null)
      {
        UserZoneActivity.this.gotoLogin();
        UserZoneActivity.this.addHoney = true;
        return;
      }
      int i;
      if ((UserZoneActivity.this.retrieved) && (UserZoneActivity.this.user != null) && (!UserZoneActivity.this.user.isHoney()))
      {
        i = 1;
        label81: if ((!UserZoneActivity.this.retrieved) || (UserZoneActivity.this.user == null) || (!UserZoneActivity.this.user.isHoney()))
          break label282;
      }
      label282: for (int j = 1; ; j = 0)
      {
        if (i != 0)
        {
          UserZoneActivity.this.operateReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/addfollow.bin", new String[] { "token", UserZoneActivity.this.token, "userid", String.valueOf(k) });
          UserZoneActivity.this.mapiService().exec(UserZoneActivity.this.operateReq, UserZoneActivity.this);
          UserZoneActivity.this.showProgressDialog("正在关注...");
        }
        if (j == 0)
          break;
        UserZoneActivity.this.operateReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/removefollow.bin", new String[] { "token", UserZoneActivity.this.token, "userid", String.valueOf(k) });
        UserZoneActivity.this.mapiService().exec(UserZoneActivity.this.operateReq, UserZoneActivity.this);
        UserZoneActivity.this.showProgressDialog("正在取消关注...");
        return;
        i = 0;
        break label81;
      }
    }
  };
  String token;
  UserProfile user;
  boolean userChanged;
  MApiRequest userprofileReq;

  private int getParamId()
  {
    UserProfile localUserProfile = getAccount();
    int i = userId();
    if ((localUserProfile != null) && (localUserProfile.id() == i))
      return 0;
    return i;
  }

  private void notifyDataSetChanged()
  {
    if (this.user.favoCount() > 0)
    {
      this.mFavoriteLay.setSubtitle("" + this.user.favoCount());
      if (this.user.honeyCount() <= 0)
        break label139;
      this.mmHoneyNumText.setText("" + this.user.honeyCount());
    }
    while (true)
    {
      if (this.user.fansCount() <= 0)
        break label151;
      this.mFansNumText.setText("" + this.user.fansCount());
      return;
      this.mFavoriteLay.setSubtitle("");
      break;
      label139: this.mmHoneyNumText.setText("");
    }
    label151: this.mFansNumText.setText("");
  }

  private void setupView()
  {
    this.infoTab = ((TableView)findViewById(R.id.user_tableview));
    this.infoTab.setOnItemClickListener(this);
    findViewById(R.id.follow).setOnClickListener(this.titleBtnChangeListener);
    findViewById(R.id.message).setOnClickListener(this.sendMsgBtnListener);
    updateProfile();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public boolean isConfirm()
  {
    return getIntent().getBooleanExtra("isConfirm", false);
  }

  boolean isMyself()
  {
    UserProfile localUserProfile = getAccount();
    return (localUserProfile != null) && (localUserProfile.id() == userId());
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    paramAccountService = getAccount();
    if (paramAccountService == null);
    do
    {
      return;
      this.token = paramAccountService.token();
      refresh(false);
    }
    while (!this.addHoney);
    this.operateReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/addfollow.bin?token=" + this.token + "&userid=" + this.user.id(), CacheType.NORMAL);
    mapiService().exec(this.operateReq, this);
    showProgressDialog("正在关注...");
    this.addHoney = false;
  }

  public void onClick(View paramView)
  {
    int i = getParamId();
    int j = paramView.getId();
    if (j == R.id.followers)
      startActivity("dianping://myhoney?userid=" + i + "&filter=0");
    do
      return;
    while (j != R.id.fans);
    startActivity("dianping://myfans?userid=" + i + "&filter=1");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.user_zone);
    this.mFavoriteLay = ((UserProfileItem)findViewById(R.id.favour));
    this.mFavoriteLay.setClickable(true);
    this.mDianPingLay = ((UserProfileItem)findViewById(R.id.review));
    this.mDianPingLay.setClickable(true);
    this.mFansLay = findViewById(R.id.fans);
    this.mFansNumText = ((TextView)findViewById(R.id.my_fans_num_txt));
    this.mFansLay.setOnClickListener(this);
    this.mHoneyLay = findViewById(R.id.followers);
    this.mHoneyLay.setOnClickListener(this);
    this.mmHoneyNumText = ((TextView)findViewById(R.id.my_honey_num_txt));
    this.preUserId = getIntent().getIntExtra("userId", 0);
    if (getIntent().getData() != null);
    try
    {
      this.preUserId = Integer.parseInt(getIntent().getData().getQueryParameter("userid"));
      if (paramBundle != null);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        try
        {
          if (!(getIntent().getParcelableExtra("user") instanceof DPObject))
            continue;
          if (getIntent().getParcelableExtra("user") == null)
            continue;
          this.user = ((UserProfile)((DPObject)getIntent().getParcelableExtra("user")).decodeToObject(UserProfile.DECODER));
          this.token = accountService().token();
          setupView();
          accountService().addListener(this);
          paramBundle = new IntentFilter("com.dianping.action.HONEY_CHANGED");
          registerReceiver(this.receiver, paramBundle);
          paramBundle = new IntentFilter("com.dianping.action.COMMENT");
          registerReceiver(this.receiver, paramBundle);
          this.isReceiverRegistered = true;
          StringBuilder localStringBuilder = new StringBuilder().append("http://m.api.dianping.com/user.bin?token=");
          if (getAccount() != null)
            break label486;
          paramBundle = null;
          this.userprofileReq = BasicMApiRequest.mapiGet(paramBundle + "&userid=" + userId(), CacheType.DISABLED);
          mapiService().exec(this.userprofileReq, this);
          showProgressDialog("载入中...");
          return;
          localNumberFormatException = localNumberFormatException;
          if (getAccount() == null)
            continue;
          int i = getAccount().id();
          this.preUserId = i;
          continue;
          i = 0;
          continue;
          if (!(getIntent().getParcelableExtra("user") instanceof UserProfile))
            continue;
          this.user = ((UserProfile)getIntent().getParcelableExtra("user"));
          continue;
        }
        catch (ArchiveException paramBundle)
        {
          paramBundle.printStackTrace();
          continue;
        }
        this.user = ((UserProfile)paramBundle.getParcelable("user"));
        this.retrieved = paramBundle.getBoolean("retrieved");
        this.userChanged = paramBundle.getBoolean("userChanged");
        continue;
        label486: paramBundle = accountService().token();
      }
    }
  }

  protected void onDestroy()
  {
    if (this.isReceiverRegistered)
      unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    paramInt = getParamId();
    int i = paramView.getId();
    if (i == R.id.favour)
      startActivity("dianping://userfavorite?userid=" + paramInt);
    do
      return;
    while (i != R.id.review);
    startActivity("dianping://userrecord?userid=" + paramInt);
  }

  public void onLoginCancel()
  {
    this.addHoney = false;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == "refresh".hashCode())
    {
      refresh(true);
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onProgressDialogCancel()
  {
    if (this.operateReq != null)
      mapiService().abort(this.operateReq, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if ((paramMApiRequest == this.operateReq) && (paramMApiResponse.message() != null))
      Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
    do
      return;
    while ((paramMApiRequest != this.userprofileReq) || (paramMApiResponse.message() == null));
    Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
    findViewById(R.id.follow).setVisibility(8);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    Intent localIntent;
    if ((paramMApiRequest == this.operateReq) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      mapiCacheService().remove(BasicMApiRequest.mapiGet("http://m.api.dianping.com/followlist.bin?userid=0&token=" + this.token + "&start=0", CacheType.NORMAL));
      this.retrieved = true;
      this.userChanged = true;
      updateProfile();
      localIntent = new Intent("com.dianping.action.HONEY_CHANGED");
      localIntent.putExtra("userid", this.user.id());
      if (paramMApiRequest.url().contains("removefollow"))
      {
        localIntent.putExtra("isDelete", true);
        sendBroadcast(localIntent);
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest != null)
          Toast.makeText(this, paramMApiRequest.getString("Content"), 0).show();
      }
    }
    do
    {
      return;
      localIntent.putExtra("isAdd", true);
      break;
    }
    while ((paramMApiRequest != this.userprofileReq) || (!(paramMApiResponse.result() instanceof DPObject)));
    paramMApiRequest = null;
    try
    {
      paramMApiResponse = (UserProfile)((DPObject)paramMApiResponse.result()).decodeToObject(UserProfile.DECODER);
      paramMApiRequest = paramMApiResponse;
      label222: parseProfile(paramMApiRequest);
      notifyDataSetChanged();
      return;
    }
    catch (ArchiveException paramMApiResponse)
    {
      break label222;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.retrieved = paramBundle.getBoolean("retrieved");
  }

  protected void onResume()
  {
    super.onResume();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("user", this.user);
    paramBundle.putBoolean("retrieved", this.retrieved);
    paramBundle.putBoolean("userChanged", this.userChanged);
    super.onSaveInstanceState(paramBundle);
  }

  public void parseProfile(UserProfile paramUserProfile)
  {
    this.user = paramUserProfile;
    this.retrieved = true;
    updateProfile();
  }

  void refresh(boolean paramBoolean)
  {
    this.retrieved = false;
    StringBuilder localStringBuilder = new StringBuilder().append("http://m.api.dianping.com/user.bin?token=");
    if (getAccount() == null);
    for (String str = null; ; str = accountService().token())
    {
      this.userprofileReq = BasicMApiRequest.mapiGet(str + "&userid=" + userId(), CacheType.DISABLED);
      mapiService().exec(this.userprofileReq, this);
      if (paramBoolean)
        showProgressDialog("正在刷新...");
      return;
    }
  }

  public void updateProfile()
  {
    Object localObject2 = (CircleImageView)findViewById(16908294);
    Object localObject3 = (TextView)findViewById(16908308);
    Object localObject1 = (NetworkThumbView)findViewById(R.id.user_level_logo);
    if (this.user == null)
    {
      ((CircleImageView)localObject2).setLocalBitmap(((BitmapDrawable)getResources().getDrawable(R.drawable.portrait_def)).getBitmap());
      ((TextView)localObject3).setText(null);
      localObject2 = (Button)findViewById(R.id.message);
      localObject3 = (NovaButton)findViewById(R.id.follow);
      if (isMyself())
      {
        ((NovaButton)localObject3).setVisibility(8);
        ((Button)localObject2).setVisibility(8);
        return;
      }
    }
    else
    {
      ((CircleImageView)localObject2).setImage(this.user.avatarBig());
      if (this.user.nickName().length() >= 12)
        ((TextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_18));
      while (true)
      {
        ((TextView)localObject3).setText(this.user.nickName());
        localObject2 = this.user.getUserLevel();
        if ((localObject2 == null) || (TextUtils.isEmpty(((UserLevel)localObject2).getPic())))
          break;
        ((NetworkThumbView)localObject1).setImage(((UserLevel)localObject2).getPic());
        break;
        ((TextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_20));
      }
    }
    if (isConfirm())
    {
      ((NovaButton)localObject3).setVisibility(0);
      ((Button)localObject2).setVisibility(0);
      return;
    }
    if (this.retrieved)
    {
      ((NovaButton)localObject3).setVisibility(0);
      ((Button)localObject2).setVisibility(0);
      if ((this.user.isHoney()) && (!this.userChanged))
        ((NovaButton)localObject3).setGAString("follow", "取消关注");
      for (localObject1 = "取消关注"; ; localObject1 = "添加关注")
      {
        this.userChanged = false;
        ((NovaButton)localObject3).setText((CharSequence)localObject1);
        ((Button)localObject2).setText("发私信");
        return;
        ((NovaButton)localObject3).setGAString("follow", "关注");
      }
    }
    ((NovaButton)localObject3).setVisibility(8);
    ((Button)localObject2).setVisibility(8);
  }

  public int userId()
  {
    if (this.user == null)
      return this.preUserId;
    return this.user.id();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.UserZoneActivity
 * JD-Core Version:    0.6.0
 */