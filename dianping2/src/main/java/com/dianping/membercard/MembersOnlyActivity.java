package com.dianping.membercard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.fragment.CardFragment;
import com.dianping.membercard.fragment.NoAddedGeneralCardFragment;
import com.dianping.membercard.fragment.NoAddedMallCardFragment;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.membercard.model.JoinMScoreCHandler;
import com.dianping.membercard.model.JoinMScoreCHandler.OnJoinScoreCardHandlerListener;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.model.Location;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MembersOnlyActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, JoinMCHandler.OnJoinCardRequestHandlerListener
{
  private static final String KEY_IS_MC_SHOP = "IsMCShop";
  private static final String KEY_IS_MC_USER = "IsMCUser";
  private static final String MESSAGE_CARD_NON_EXISTENT = "此卡不存在";
  private static final String MESSAGE_DIALOG_ADD_CARD_WAITING = "正在提交请求，请稍候...";
  private static final String MESSAGE_DIALOG_INFO_UPDATING = "正在更新信息，请稍候...";
  private static final String TITLE = "成为会员";
  String cardProductURL;
  int cardType;
  int cardlevel;
  DPObject cardobject;
  private MApiRequest getMCStatusRequestForBindScoreCardAfterLogin = null;
  String host;
  private CardFragment loadedFragment;
  private boolean loginForBindScoreCard = false;
  private MyReceiver mBroadcastReceiver;
  MApiRequest mGetCardInfoRequst = null;
  MApiRequest mGetMCStatusRequest = null;
  boolean mIsScan;
  JoinMCHandler mJoinMCHandler;
  JoinMScoreCHandler mJoinMScoreCHandler;
  String mTitle;
  String membercardid;
  private JoinMScoreCHandler.OnJoinScoreCardHandlerListener onJoinScoreCardHandlerListener = new JoinMScoreCHandler.OnJoinScoreCardHandlerListener()
  {
    public void onJoinScoreCardFail(String paramString)
    {
      MembersOnlyActivity.this.dismissDialog();
      Toast.makeText(MembersOnlyActivity.this, paramString, 0).show();
    }

    public void onJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2)
    {
      MembersOnlyActivity.this.dismissDialog();
      MembersOnlyActivity.this.openBindScoreCardWebview(paramString2);
    }

    public void onJoinScoreCardSuccess()
    {
      MembersOnlyActivity.this.dismissDialog();
      MCUtils.sendJoinScoreCardSuccessBroadcast(MembersOnlyActivity.this, MembersOnlyActivity.this.membercardid);
      MembersOnlyActivity.this.doWhenIsMemberCardUser();
    }
  };
  int productid;
  int shopid;
  int source;

  private void addScoreCard()
  {
    showProgressDialog("正在提交请求，请稍候...");
    this.mJoinMScoreCHandler.joinScoreCards(this.membercardid, this.source);
  }

  private void doWhenIsMemberCardUser()
  {
    gotoMyCard();
    setResult(-1);
    finish();
  }

  private void doWhenIsnotMemberCardShop()
  {
    Toast.makeText(this, "此卡不存在", 0).show();
    finish();
  }

  private void getMCStatusForBindScoreCardAfterLogin()
  {
    showProgressDialog("正在更新信息，请稍候...");
    this.getMCStatusRequestForBindScoreCardAfterLogin = BasicMApiRequest.mapiGet(getMCStatusRequestUrlString(), CacheType.DISABLED);
    mapiService().exec(this.getMCStatusRequestForBindScoreCardAfterLogin, this);
  }

  private String getMCStatusRequestUrlString()
  {
    String str2 = "http://mc.api.dianping.com/getmcstatus.v2.mc?&membercardid=" + this.membercardid;
    String str1 = str2;
    if (getAccount() != null)
    {
      str1 = str2;
      if (accountService().token() != null)
        str1 = str2 + "&token=" + accountService().token();
    }
    return str1 + "&uuid=" + Environment.uuid();
  }

  private void getMCStatusTask()
  {
    this.mGetMCStatusRequest = BasicMApiRequest.mapiGet(getMCStatusRequestUrlString(), CacheType.DISABLED);
    mapiService().exec(this.mGetMCStatusRequest, this);
  }

  private void gotoAddScoreCard()
  {
    if (getAccount() == null)
    {
      this.loginForBindScoreCard = true;
      gotoLogin();
      return;
    }
    addScoreCard();
  }

  private void inflateFragment(DPObject paramDPObject)
  {
    this.shopid = paramDPObject.getInt("ShopID");
    this.mTitle = paramDPObject.getString("Title");
    this.cardType = paramDPObject.getInt("CardType");
    this.productid = getProductID(paramDPObject);
    if (this.cardlevel == 0)
    {
      this.cardlevel = paramDPObject.getInt("CardLevel");
      if (this.cardlevel == 3)
        this.cardlevel = 2;
    }
    this.membercardid = String.valueOf(paramDPObject.getInt("MemberCardID"));
    if ((this.cardType == 2) || (this.cardType == 3))
      this.loadedFragment = new NoAddedMallCardFragment();
    while (true)
    {
      if (this.loadedFragment != null)
      {
        FragmentManager localFragmentManager = getSupportFragmentManager();
        FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("card", paramDPObject);
        localBundle.putInt("source", this.source);
        localBundle.putInt("cardlevel", this.cardlevel);
        this.loadedFragment.setArguments(localBundle);
        paramDPObject = localFragmentManager.findFragmentByTag("loadedFragment");
        if (paramDPObject != null)
          localFragmentTransaction.remove(paramDPObject);
        localFragmentTransaction.add(R.id.card_fragment_layout, this.loadedFragment, "loadedFragment");
        localFragmentTransaction.commitAllowingStateLoss();
      }
      return;
      if (this.cardType == 1)
      {
        this.loadedFragment = new NoAddedGeneralCardFragment();
        continue;
      }
      this.loadedFragment = new NoAddedMallCardFragment();
    }
  }

  private boolean isScoreCard()
  {
    return MemberCard.isScoreCard(this.cardobject);
  }

  private void openBindScoreCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", this.membercardid);
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivity(paramString);
  }

  private void setupView()
  {
    super.setContentView(R.layout.members_only);
    this.leftTitleButton.setOnClickListener(this);
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

  private void updateMCStatus(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    if (!paramDPObject.getBoolean("IsMCShop"))
      doWhenIsnotMemberCardShop();
    if (paramDPObject.getBoolean("IsMCUser"))
    {
      doWhenIsMemberCardUser();
      return;
    }
    if (this.mIsScan)
    {
      getCardInfoTask();
      return;
    }
    this.mJoinMCHandler.onLoginAddCard();
  }

  private void updateMCStatusForBindScoreCardAfterLogin(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    if (!paramDPObject.getBoolean("IsMCShop"))
      doWhenIsnotMemberCardShop();
    if (paramDPObject.getBoolean("IsMCUser"))
    {
      MCUtils.sendJoinScoreCardSuccessBroadcast(this, this.membercardid);
      doWhenIsMemberCardUser();
      return;
    }
    gotoAddScoreCard();
  }

  public void getCardInfoTask()
  {
    showProgressDialog("正在发起请求，请稍候...");
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/getcardinfo.v2.mc?membercardid=");
    localStringBuilder.append(this.membercardid);
    if (accountService().token() != null)
    {
      localStringBuilder.append("&token=");
      localStringBuilder.append(accountService().token());
    }
    Object localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(((Location)localObject).latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(((Location)localObject).longitude()));
    }
    localStringBuilder.append("&uuid=");
    localStringBuilder.append(Environment.uuid());
    localObject = getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    this.mGetCardInfoRequst = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mGetCardInfoRequst, this);
  }

  public String getPageName()
  {
    return "membersonly";
  }

  public int getProductID(DPObject paramDPObject)
  {
    int j = 0;
    paramDPObject = paramDPObject.getArray("ProductList");
    int i = j;
    if (paramDPObject != null)
    {
      i = j;
      if (paramDPObject.length > 0)
        i = paramDPObject[0].getInt("ProductID");
    }
    return i;
  }

  public void gotoCreateOrder()
  {
    this.mJoinMCHandler.gotoCreateOrder(this.membercardid, this.source, this.productid);
  }

  public void gotoMyCard()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://membercardinfo?membercardid=" + this.membercardid + "&shopid=" + this.shopid)));
  }

  public void joinTask()
  {
    this.mJoinMCHandler.joinTask(this.membercardid, this.source);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt2 == -1) || (paramInt2 == 10) || (paramInt2 == 20))
    {
      setResult(paramInt2);
      finish();
      gotoMyCard();
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.submit)
      if (this.source == 12)
      {
        statisticsEvent("cardinfo5", "cardinfo5_join_shopinfo", this.membercardid + "|" + this.mTitle, 0);
        if (!isScoreCard())
          break label187;
        gotoAddScoreCard();
      }
    label187: 
    do
    {
      return;
      if (this.source == 14)
      {
        statisticsEvent("cardinfo5", "cardinfo5_join_availablecard", this.membercardid + "|" + this.mTitle, 0);
        break;
      }
      if ((this.source < 30) || (this.source > 39))
        break;
      statisticsEvent("cardinfo5", "cardinfo5_join_landingpage", this.membercardid + "|" + this.mTitle, 0);
      break;
      if (this.cardlevel == 2)
      {
        gotoCreateOrder();
        return;
      }
      joinTask();
      return;
    }
    while (paramView.getId() != R.id.left_title_button);
    statisticsEvent("cardinfo5", "cardinfo5_back", this.membercardid + "|" + this.mTitle, 0);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("成为会员");
    parseIntent();
    setupView();
    this.mJoinMCHandler = new JoinMCHandler(this);
    this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    this.mJoinMScoreCHandler = new JoinMScoreCHandler(this);
    this.mJoinMScoreCHandler.setJoinScoreCardHandlerListener(this.onJoinScoreCardHandlerListener);
    if ((this.mIsScan) && (accountService() != null) && (accountService().token() != null))
    {
      getMCStatusTask();
      return;
    }
    GAUserInfo localGAUserInfo;
    if (this.cardobject == null)
    {
      getCardInfoTask();
      paramBundle = getStringParam("shopid");
      this.gaExtra.member_card_id = Integer.valueOf(this.membercardid);
      localGAUserInfo = this.gaExtra;
      if (!TextUtils.isEmpty(paramBundle))
        break label196;
    }
    label196: for (int i = 0; ; i = Integer.valueOf(paramBundle).intValue())
    {
      localGAUserInfo.shop_id = Integer.valueOf(i);
      this.mBroadcastReceiver = new MyReceiver(null);
      registerReceiver(this.mBroadcastReceiver, new IntentFilter("Card:JoinSuccess"));
      return;
      unloadFragment();
      inflateFragment(this.cardobject);
      break;
    }
  }

  protected void onDestroy()
  {
    if (this.mJoinMCHandler != null)
      this.mJoinMCHandler.removeListener();
    this.mJoinMScoreCHandler.setJoinScoreCardHandlerListener(null);
    if (this.mBroadcastReceiver != null)
      unregisterReceiver(this.mBroadcastReceiver);
    super.onDestroy();
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
    gotoMyCard();
    setResult(10);
    finish();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      statisticsEvent("cardinfo5", "cardinfo5_back", this.membercardid + "|" + this.mTitle, 0);
      finish();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (this.loginForBindScoreCard)
      {
        getMCStatusForBindScoreCardAfterLogin();
        this.loginForBindScoreCard = false;
      }
      while (true)
      {
        return true;
        getMCStatusTask();
      }
    }
    this.loginForBindScoreCard = false;
    return false;
  }

  public void onLoginCancel()
  {
    super.onLoginCancel();
    if (this.loginForBindScoreCard)
      this.loginForBindScoreCard = false;
  }

  public void onMessageConfirm()
  {
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGetCardInfoRequst)
    {
      dismissDialog();
      showMessageDialog(paramMApiResponse.message());
    }
    do
      return;
    while (paramMApiRequest != this.mGetMCStatusRequest);
    dismissDialog();
    this.mGetMCStatusRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mGetCardInfoRequst)
    {
      dismissDialog();
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.cardobject = ((DPObject)paramMApiResponse.result());
        inflateFragment(this.cardobject);
      }
    }
    do
    {
      return;
      if (paramMApiRequest != this.mGetMCStatusRequest)
        continue;
      updateMCStatus((DPObject)paramMApiResponse.result());
      this.mGetMCStatusRequest = null;
      return;
    }
    while (paramMApiRequest != this.getMCStatusRequestForBindScoreCardAfterLogin);
    updateMCStatusForBindScoreCardAfterLogin((DPObject)paramMApiResponse.result());
    this.getMCStatusRequestForBindScoreCardAfterLogin = null;
  }

  public void parseIntent()
  {
    Intent localIntent = getIntent();
    Uri localUri = localIntent.getData();
    this.host = localUri.getHost();
    boolean bool;
    if ("mcbarcode".equals(this.host))
      bool = true;
    while (true)
    {
      this.mIsScan = bool;
      String str;
      if (this.mIsScan)
      {
        this.source = 11;
        str = localUri.getQueryParameter("cardlevel");
      }
      try
      {
        while (true)
        {
          this.cardlevel = Integer.parseInt(str);
          this.membercardid = localUri.getQueryParameter("membercardid");
          if ((localIntent.getExtras() != null) && (localIntent.getExtras().getParcelable("cardObject") != null))
            this.cardobject = ((DPObject)localIntent.getExtras().getParcelable("cardObject"));
          return;
          bool = false;
          break;
          str = localUri.getQueryParameter("source");
          try
          {
            this.source = Integer.parseInt(str);
          }
          catch (NumberFormatException localNumberFormatException1)
          {
            this.source = 12;
          }
        }
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        while (true)
          this.cardlevel = 0;
      }
    }
  }

  private class MyReceiver extends BroadcastReceiver
  {
    private MyReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      MCUtils.sendJoinScoreCardSuccessBroadcast(MembersOnlyActivity.this, MembersOnlyActivity.this.membercardid);
      MembersOnlyActivity.this.finish();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.MembersOnlyActivity
 * JD-Core Version:    0.6.0
 */