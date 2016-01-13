package com.dianping.membercard;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.fragment.CardDetailRequestTask;
import com.dianping.membercard.fragment.CardDetailRequestTask.CardDetailRequestHandler;
import com.dianping.membercard.fragment.CardDetailRequestTask.ResponseDataType;
import com.dianping.membercard.fragment.CardFragment;
import com.dianping.membercard.fragment.MallCardFragment;
import com.dianping.membercard.fragment.MemberCardFragment;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnQuitThirdPartyCardHandlerListener;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;

public class MemberCardInfoActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, CardDetailRequestTask.CardDetailRequestHandler
{
  private static final String LOG_TAG = MemberCardInfoActivity.class.getSimpleName();
  private static final String MC_MEMBERCARDINFOACTIVITY_CHECK_LOGIN = "com.dianping.action.MEBMERCARDINFOACTIVITY_CHECK_LOGIN";
  private static final String MC_UPDATE_CARD_INFO = "com.dianping.action.UPDATE_CARD_INFO";
  private static final String MC_UPDATE_USER_INFO = "com.dianping.action.UPDATE_USER_INFO";
  private MApiRequest addUserCommentRequest;
  private CardDetailRequestTask cardDetailRequestTask = null;
  private String cardId;
  private DPObject cardObj;
  private String firstAddedBranchCardId;
  private boolean isFromScoreUrl = false;
  private CardFragment loadedFragment;
  private JoinMCHandler2 mJoinMCHandler2;
  BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if (paramContext.equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        paramContext = paramIntent.getStringExtra("username");
        MemberCardInfoActivity.this.updateUserInfo(paramContext);
      }
      do
      {
        return;
        if (!paramContext.equals("com.dianping.action.UPDATE_CARD_INFO"))
          continue;
        MemberCardInfoActivity.this.loadedFragment.refreshUI();
        return;
      }
      while (!paramContext.equals("com.dianping.action.MEBMERCARDINFOACTIVITY_CHECK_LOGIN"));
      MemberCardInfoActivity.access$102(MemberCardInfoActivity.this, true);
      MemberCardInfoActivity.this.onTitleButtonClick();
    }
  };
  MemberCard membercard;
  private boolean needLogin = false;
  private double productValue;
  Handler refreshMallCardHandler;
  private FrameLayout rootView;
  private int usercardlevel;

  private void addGa()
  {
    int j = 0;
    Object localObject = getStringParam("membercardid");
    String str = getStringParam("shopid");
    GAUserInfo localGAUserInfo = this.gaExtra;
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      i = 0;
      localGAUserInfo.member_card_id = Integer.valueOf(i);
      localObject = this.gaExtra;
      if (!TextUtils.isEmpty(str))
        break label79;
    }
    label79: for (int i = j; ; i = Integer.valueOf(str).intValue())
    {
      ((GAUserInfo)localObject).shop_id = Integer.valueOf(i);
      return;
      i = Integer.valueOf((String)localObject).intValue();
      break;
    }
  }

  private void addUserCommentTask(String paramString)
  {
    showProgressDialog("正在提交请求，请稍候...");
    if (this.addUserCommentRequest != null)
      mapiService().abort(this.addUserCommentRequest, this, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("membercardid");
    localArrayList.add(this.cardId);
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.cardObj.getInt("ShopID")));
    localArrayList.add("comment");
    localArrayList.add(paramString);
    this.addUserCommentRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/addusercomment.mc", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.addUserCommentRequest, this);
  }

  private void initView()
  {
    this.rootView.removeAllViews();
    this.membercard = new MemberCard(this.cardObj);
    if (TextUtils.isEmpty(this.cardObj.getString("UserName")))
      gotoMemberInfo();
    while (true)
    {
      return;
      this.cardId = String.valueOf(this.cardObj.getInt("MemberCardID"));
      this.usercardlevel = this.cardObj.getInt("UserCardLevel");
      updateRightButton();
      Object localObject1 = new MemberCard(this.cardObj).getVIPCard();
      if (localObject1 != null)
        this.productValue = ((DPObject)localObject1).getDouble("ProductValue");
      int i = this.cardObj.getInt("CardType");
      if ((i == 2) || (i == 3))
      {
        this.loadedFragment = new MallCardFragment();
        ((MallCardFragment)this.loadedFragment).setRefreshHander(this.refreshMallCardHandler);
      }
      while (this.loadedFragment != null)
      {
        Object localObject2 = getSupportFragmentManager();
        localObject1 = ((FragmentManager)localObject2).beginTransaction();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("card", this.cardObj);
        this.loadedFragment.setArguments(localBundle);
        localObject2 = ((FragmentManager)localObject2).findFragmentByTag("loadedFragment");
        if (localObject2 != null)
          ((FragmentTransaction)localObject1).remove((Fragment)localObject2);
        ((FragmentTransaction)localObject1).add(16908300, this.loadedFragment, "loadedFragment");
        ((FragmentTransaction)localObject1).commitAllowingStateLoss();
        return;
        if (i == 1)
        {
          this.loadedFragment = new MemberCardFragment();
          continue;
        }
        this.loadedFragment = new MallCardFragment();
        ((MallCardFragment)this.loadedFragment).setRefreshHander(this.refreshMallCardHandler);
      }
    }
  }

  private void requestCard(String paramString)
  {
    if (paramString == null)
    {
      Log.e(LOG_TAG, "card id is null, activity finish");
      finish();
    }
    setLoadingView();
    this.cardDetailRequestTask.doRequest(Integer.parseInt(paramString), this.usercardlevel);
  }

  private void setError(String paramString)
  {
    this.rootView.findViewById(R.id.progress).setVisibility(8);
    ((TextView)this.rootView.findViewById(R.id.text)).setText(paramString);
  }

  private void setLoadingView()
  {
    View localView = LayoutInflater.from(this).inflate(R.layout.loading_view, null);
    this.rootView.addView(localView);
  }

  private void showMoreFeature()
  {
    MCUtils.showMembercardMoreFeature(this, this.cardObj, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          paramDialogInterface = MemberCardInfoActivity.this.getAccount();
          MemberCardInfoActivity localMemberCardInfoActivity = MemberCardInfoActivity.this;
          String str = ConfigHelper.mcWeixinUrl;
          Location localLocation = MemberCardInfoActivity.this.location();
          paramInt = MemberCardInfoActivity.this.cityId();
          if (paramDialogInterface == null);
          for (paramDialogInterface = ""; ; paramDialogInterface = paramDialogInterface.token())
          {
            MCUtils.membercardShare2WeiXin(localMemberCardInfoActivity, false, str, localLocation, paramInt, paramDialogInterface, MemberCardInfoActivity.this.cardObj);
            return;
          }
        case 1:
        }
        MCUtils.membercardShare2ThirdParty(MemberCardInfoActivity.this, MemberCardInfoActivity.this.cardObj, MemberCardInfoActivity.this.getAccount().feedFlag());
      }
    }
    , new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          MemberCardInfoActivity.this.showConfirmDialog("商家不让用");
          return;
        case 1:
          MemberCardInfoActivity.this.showConfirmDialog("优惠与描述不符");
          return;
        case 2:
        }
        MCUtils.gotoMembercardFeedBack(MemberCardInfoActivity.this, MemberCardInfoActivity.this.cardObj);
      }
    }
    , new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MemberCardInfoActivity.this.showProgressDialog("正在提交请求，请稍候...");
        MemberCardInfoActivity.this.mJoinMCHandler2.quitThirdPartyCard(MemberCardInfoActivity.this.cardId);
      }
    });
  }

  private void unloadFragment()
  {
    if (this.loadedFragment != null)
    {
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.remove(this.loadedFragment);
      localFragmentTransaction.commit();
      this.loadedFragment = null;
    }
  }

  private void updateRightButton()
  {
    if (getAccount() == null)
    {
      this.rightTitleButton.setVisibility(8);
      this.titleButton.setText("登录");
      this.titleButton.setVisibility(0);
      this.titleButton.setOnClickListener(this);
      return;
    }
    setRightTitleButton(R.drawable.title_icon_more, this);
    this.titleButton.setVisibility(8);
    this.rightTitleButton.setVisibility(0);
    this.rightTitleButton.setOnClickListener(this);
  }

  public String getPageName()
  {
    return "membercardinfo";
  }

  public void gotoMemberInfo()
  {
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://memberinfo?membercardid=" + this.cardId + "&from=1")), 10);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    updateRightButton();
    if (paramInt2 == 10)
    {
      unloadFragment();
      initView();
    }
    do
    {
      return;
      if (paramInt2 == 20)
      {
        this.cardObj = ((DPObject)paramIntent.getParcelableExtra("cardobject"));
        unloadFragment();
        initView();
        return;
      }
      if (paramInt2 != 40)
        continue;
      updateRightButton();
      return;
    }
    while (paramInt2 != 50);
    paramInt1 = paramIntent.getIntExtra("membercardid", 0);
    this.loadedFragment.refreshByCardId(paramInt1);
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt)
  {
    setError(paramMApiResponse.message().content());
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, CardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    dismissDialog();
    this.cardObj = paramDPObject;
    if (TextUtils.isEmpty(this.firstAddedBranchCardId))
      this.firstAddedBranchCardId = String.valueOf(this.cardObj.getInt("MemberCardID"));
    initView();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.right_title_button)
      showMoreFeature();
    do
      return;
    while (paramView.getId() != R.id.title_button);
    onTitleButtonClick();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.cardDetailRequestTask = new CardDetailRequestTask(this);
    this.rootView = new FrameLayout(this);
    this.rootView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.rootView.setId(16908300);
    this.rootView.setBackgroundResource(R.drawable.main_background);
    super.setContentView(this.rootView);
    this.refreshMallCardHandler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        paramMessage = (DPObject)paramMessage.getData().get("card");
        if (paramMessage != null)
        {
          MemberCardInfoActivity.access$202(MemberCardInfoActivity.this, paramMessage);
          MemberCardInfoActivity.this.initView();
        }
      }
    };
    Intent localIntent = getIntent();
    Uri localUri = localIntent.getData();
    if (paramBundle == null)
    {
      this.cardObj = ((DPObject)localIntent.getParcelableExtra("card"));
      if (this.cardObj != null)
        break label264;
      this.cardId = localUri.getQueryParameter("membercardid");
      if (this.cardId.indexOf(',') >= 0)
        this.cardId = MCUtils.filterTextAfter(',', this.cardId);
      requestCard(this.cardId);
    }
    while (true)
    {
      paramBundle = new IntentFilter[3];
      paramBundle[0] = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
      paramBundle[1] = new IntentFilter("com.dianping.action.UPDATE_CARD_INFO");
      paramBundle[2] = new IntentFilter("com.dianping.action.MEBMERCARDINFOACTIVITY_CHECK_LOGIN");
      int i = 0;
      while (i < paramBundle.length)
      {
        registerReceiver(this.mReceiver, paramBundle[i]);
        i += 1;
      }
      this.cardObj = ((DPObject)paramBundle.getParcelable("cardObj"));
      this.cardId = paramBundle.getString("cardId");
      break;
      label264: this.cardId = String.valueOf(this.cardObj.getInt("MemberCardID"));
      this.firstAddedBranchCardId = this.cardId;
      initView();
    }
    this.mJoinMCHandler2 = new JoinMCHandler2(this);
    this.mJoinMCHandler2.setOnQuitThirdPartyCardHandlerListener(new JoinMCHandler2.OnQuitThirdPartyCardHandlerListener()
    {
      public void onRequestQuitThirdPartyCardResult(boolean paramBoolean, SimpleMsg paramSimpleMsg)
      {
        if ((paramSimpleMsg.flag() == 200) || (paramSimpleMsg.flag() == 201))
        {
          Toast.makeText(MemberCardInfoActivity.this, paramSimpleMsg.content(), 0).show();
          paramSimpleMsg = new Intent("com.dianping.action.QUIT_MEMBER_CARD");
          paramSimpleMsg.putExtra("membercardid", MemberCardInfoActivity.this.cardId);
          MemberCardInfoActivity.this.sendBroadcast(paramSimpleMsg);
          paramSimpleMsg = MemberCardInfoActivity.this.cardObj.getString("MemberCardGroupID");
          if ((!TextUtils.isEmpty(paramSimpleMsg)) && (MemberCardInfoActivity.this.firstAddedBranchCardId != null) && (MemberCardInfoActivity.this.firstAddedBranchCardId.equals(MemberCardInfoActivity.this.cardId)))
          {
            Intent localIntent = new Intent("com.dianping.action.QUIT_MEMBER_CARD");
            localIntent.putExtra("membercardgroupid", paramSimpleMsg);
            MemberCardInfoActivity.this.sendBroadcast(localIntent);
          }
          MemberCardInfoActivity.this.finish();
          return;
        }
        MemberCardInfoActivity.this.showMessageDialog(paramSimpleMsg);
      }
    });
    addGa();
  }

  protected void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.addUserCommentRequest != null)
      mapiService().abort(this.addUserCommentRequest, this, true);
    if (this.mJoinMCHandler2 != null)
    {
      this.mJoinMCHandler2.setOnQuitThirdPartyCardHandlerListener(null);
      this.mJoinMCHandler2 = null;
    }
    if (this.cardDetailRequestTask != null)
    {
      this.cardDetailRequestTask.abortRequest();
      this.cardDetailRequestTask = null;
    }
    super.onDestroy();
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if (super.onLogin(paramBoolean));
    while (true)
    {
      return true;
      if (!paramBoolean)
        break;
      this.needLogin = false;
      requestCard(this.cardId);
      updateRightButton();
      if (!this.isFromScoreUrl)
        continue;
      this.loadedFragment.gotoScoreUrl();
      this.isFromScoreUrl = false;
      return true;
    }
    return false;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.addUserCommentRequest)
      showMessageDialog(paramMApiResponse.message());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (!(paramMApiResponse.result() instanceof DPObject));
    do
      return;
    while (paramMApiRequest != this.addUserCommentRequest);
    Toast.makeText(this, ((DPObject)paramMApiResponse.result()).getString("Content"), 0).show();
    this.addUserCommentRequest = null;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("cardObj", this.cardObj);
    paramBundle.putString("cardId", this.cardId);
    super.onSaveInstanceState(paramBundle);
  }

  public void onTitleButtonClick()
  {
    if (getAccount() == null)
    {
      statisticsEvent("mycard5", "mycard5_login", "", 0);
      gotoLogin();
      this.needLogin = true;
    }
    do
      return;
    while (!this.isFromScoreUrl);
    this.loadedFragment.gotoScoreUrl();
    this.isFromScoreUrl = false;
  }

  public void showConfirmDialog(String paramString)
  {
    new AlertDialog.Builder(this).setTitle("确认投诉").setPositiveButton("确定", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MemberCardInfoActivity.this.addUserCommentTask(this.val$s);
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MemberCardInfoActivity.this.statisticsEvent("mycard5", "mycard5_complaint_cancel", this.val$s, 0);
      }
    }).show();
  }

  public void updateUserInfo(String paramString)
  {
    this.cardObj = this.cardObj.edit().putString("UserName", paramString).generate();
    DPObject[] arrayOfDPObject;
    if (!TextUtils.isEmpty(this.cardObj.getString("MemberCardGroupID")))
    {
      arrayOfDPObject = this.cardObj.getArray("SubCardList");
      if (arrayOfDPObject != null);
    }
    else
    {
      return;
    }
    int i = 0;
    while (i < arrayOfDPObject.length)
    {
      arrayOfDPObject[i] = arrayOfDPObject[i].edit().putString("UserName", paramString).generate();
      i += 1;
    }
    this.cardObj = this.cardObj.edit().putArray("SubCardList", arrayOfDPObject).generate();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MemberCardInfoActivity
 * JD-Core Version:    0.6.0
 */