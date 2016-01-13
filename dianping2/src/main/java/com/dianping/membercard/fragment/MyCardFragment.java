package com.dianping.membercard.fragment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.widget.NovaFragment;
import com.dianping.cache.DPCache;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.MemberCardListAdapter;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnQuitThirdPartyCardHandlerListener;
import com.dianping.membercard.utils.CardDetailAbortStatus;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.CouponEntryView.OnCouponEntryViewClickListener;
import com.dianping.membercard.view.MemberCardListView;
import com.dianping.membercard.view.MemberCardListView.OnItemClickWithAnimListener;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCardFragment extends NovaFragment
  implements View.OnClickListener, MemberCardListView.OnItemClickWithAnimListener, CardDetailRequestTask.CardDetailRequestHandler, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String MC_MYCARDFRAGMENT_CHECK_LOGIN = "com.dianping.action.MYCARDFRAGMENT_CHECK_LOGIN";
  private static final String MC_UPDATE_CARD_INFO = "com.dianping.action.UPDATE_CARD_INFO";
  private static int MEMBER_INFO_REQ = 0;
  private static final String TAG = MyCardFragment.class.getName();
  private static final String TITLE = "我的会员卡";
  private static final String UPDATE_UI = "com.dianping.action.UPDATE_USER_INFO";
  CardDetailAbortStatus abortStatus = new CardDetailAbortStatus();
  private Adapter adapter;
  CardDetailInflateMode cardDetailInflateMode = CardDetailInflateMode.SIMPLE_MEMBER_CARD;
  private CardDetailRequestTask cardDetailRequestTask = null;
  private DPObject cardList;
  private DPObject cardObject;
  private int cardType;
  private String currentmembercardid;
  private int from = 0;
  private String host;
  private boolean isFromScoreUrl = false;
  private boolean isPreparedToRefresh = false;
  private boolean isPullToRefreshing;
  private CardFragment loadedFragment;
  private Object lockForLoadedFragment = new Object();
  private MApiRequest mAddUserCommentRequest = null;
  private DPObject mCardListHolder;
  private MApiRequest mCardListRequest = null;
  private DPObject mCouponEntryDo;
  private boolean mFirst = false;
  private boolean mIsCardBox = true;
  private JoinMCHandler2 mJoinMCHandler2;
  private MemberCardListView mListView;
  private int mPosition;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if ((paramContext.equals("com.dianping.action.QUIT_MEMBER_CARD")) || (paramContext.equals("Card:CardChanged")))
      {
        MyCardFragment.this.adapter.reset();
        MyCardFragment.this.adapter.notifyDataSetChanged();
        MyCardFragment.this.setmIsCardBox(true);
      }
      do
        while (true)
        {
          return;
          if (paramContext.equals("com.dianping.action.JOIN_MEMBER_CARD"))
          {
            MyCardFragment.this.prepareToRefresh();
            return;
          }
          if (paramContext.equals("com.dianping.action.UPDATE_USER_INFO"))
          {
            paramContext = paramIntent.getStringExtra("username");
            if (MyCardFragment.this.cardObject != null)
            {
              MyCardFragment.this.adapter.updateUserInfo(paramContext);
              MyCardFragment.this.adapter.notifyDataSetChanged();
              MyCardFragment.access$402(MyCardFragment.this, MyCardFragment.this.cardObject.edit().putString("UserName", paramContext).generate());
            }
            if (MyCardFragment.this.mListView == null)
              continue;
            MyCardFragment.this.mListView.updateCurrrentUserName(paramContext);
            return;
          }
          if (!paramContext.equals("com.dianping.action.UPDATE_CARD_INFO"))
            break;
          if (MyCardFragment.this.loadedFragment == null)
            continue;
          MyCardFragment.this.loadedFragment.refreshUI();
          return;
        }
      while (!paramContext.equals("com.dianping.action.MYCARDFRAGMENT_CHECK_LOGIN"));
      MyCardFragment.access$702(MyCardFragment.this, true);
      MyCardFragment.this.onTitleButtonClick();
    }
  };
  private MApiRequest mUserCouponRequest;
  private MemberCard membercard;
  private String membercardid;
  private JoinMCHandler2.OnQuitThirdPartyCardHandlerListener onQuitCardHandler = new JoinMCHandler2.OnQuitThirdPartyCardHandlerListener()
  {
    public void onRequestQuitThirdPartyCardResult(boolean paramBoolean, SimpleMsg paramSimpleMsg)
    {
      MyCardFragment.this.dismissDialog();
      if ((paramSimpleMsg.flag() == 200) || (paramSimpleMsg.flag() == 201))
      {
        Toast.makeText(MyCardFragment.this.getActivity(), paramSimpleMsg.content(), 0).show();
        paramSimpleMsg = new Intent("com.dianping.action.QUIT_MEMBER_CARD");
        paramSimpleMsg.putExtra("membercardid", MyCardFragment.this.membercardid);
        MyCardFragment.this.getActivity().sendBroadcast(paramSimpleMsg);
        MyCardFragment.this.onCardBoxButtonClick();
        return;
      }
      ((NovaActivity)MyCardFragment.this.getActivity()).showMessageDialog(paramSimpleMsg);
    }
  };
  PullToRefreshListView.OnRefreshListener onRefreshListener = new PullToRefreshListView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
    {
      MyCardFragment.access$102(MyCardFragment.this, true);
      MyCardFragment.this.refreshCardList();
      MyCardFragment.this.statisticsEvent("mycard5", "mycard5_refresh", null, 0);
    }
  };
  Handler refreshMallCardHandler;
  private int shopid;
  private int usercardlevel;

  static
  {
    MEMBER_INFO_REQ = 110;
  }

  private void _inflateFullMemberCard(DPObject paramDPObject)
  {
    if ((this.cardType == 2) || (this.cardType == 3))
    {
      this.loadedFragment = new MallCardFragment();
      ((MallCardFragment)this.loadedFragment).setRefreshHander(this.refreshMallCardHandler);
    }
    while (true)
    {
      if (this.loadedFragment != null)
      {
        FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("card", paramDPObject);
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
        this.loadedFragment = new MemberCardFragment();
        continue;
      }
      this.loadedFragment = new MallCardFragment();
      ((MallCardFragment)this.loadedFragment).setRefreshHander(this.refreshMallCardHandler);
    }
  }

  private void _inflateSimpleMemberCard(DPObject paramDPObject)
  {
    MemberCardRetryFragment localMemberCardRetryFragment = new MemberCardRetryFragment();
    if (localMemberCardRetryFragment != null)
    {
      this.loadedFragment = localMemberCardRetryFragment;
      FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
      FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("card", paramDPObject);
      localMemberCardRetryFragment.setArguments(localBundle);
      paramDPObject = localFragmentManager.findFragmentByTag("loadedFragment");
      if (paramDPObject != null)
        localFragmentTransaction.remove(paramDPObject);
      localFragmentTransaction.add(R.id.card_fragment_layout, localMemberCardRetryFragment, "loadedFragment");
      localFragmentTransaction.commitAllowingStateLoss();
    }
  }

  private DPObject cacheCardList()
  {
    DPObject localDPObject2 = (DPObject)DPCache.getInstance().getParcelable(couponEntryCacheKey(), "membercardlist", 3600000L, DPObject.CREATOR);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 != null)
      localDPObject1 = localDPObject2.edit().putBoolean("IsEnd", true).generate();
    return localDPObject1;
  }

  private String couponEntryCacheKey()
  {
    return String.format("%d%s", new Object[] { Integer.valueOf(accountService().id()), "CARD-PACKAGE-COUPON" });
  }

  private void handlerCardListData(DPObject paramDPObject)
  {
    unloadFragment();
    showCardBox();
    setmIsCardBox(true);
    onTitleBarChange();
    if (paramDPObject != null)
      this.adapter.appendCards(paramDPObject);
    this.mCardListRequest = null;
    this.mCardListHolder = null;
  }

  private void hideLoadedFragment()
  {
    try
    {
      synchronized (this.lockForLoadedFragment)
      {
        if (this.loadedFragment != null)
        {
          FragmentTransaction localFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
          localFragmentTransaction.hide(this.loadedFragment);
          localFragmentTransaction.commit();
          this.loadedFragment = null;
        }
        return;
      }
    }
    catch (Exception localException)
    {
      Log.e("Exception on removing loadedFragment!");
    }
  }

  private void hookHandleCardListData(DPObject paramDPObject)
  {
    if (this.adapter.getmNextStartIndex() == 0)
      DPCache.getInstance().put(couponEntryCacheKey(), "membercardlist", paramDPObject, 3600000L);
    if (this.abortStatus.isOpenningListEnable())
    {
      handlerCardListData(paramDPObject);
      return;
    }
    this.mCardListHolder = paramDPObject;
  }

  private void inflateFragment(DPObject paramDPObject, CardDetailInflateMode paramCardDetailInflateMode)
  {
    synchronized (this.lockForLoadedFragment)
    {
      this.cardDetailInflateMode = paramCardDetailInflateMode;
      updateSelectedCardStatus(paramDPObject);
      onTitleBarChange();
      if (paramCardDetailInflateMode == CardDetailInflateMode.FULL_MEMBER_CARD)
        _inflateFullMemberCard(paramDPObject);
      do
        return;
      while (paramCardDetailInflateMode != CardDetailInflateMode.SIMPLE_MEMBER_CARD);
      _inflateSimpleMemberCard(paramDPObject);
    }
  }

  private boolean isUserNameEmpty(DPObject paramDPObject)
  {
    return (paramDPObject == null) || (TextUtils.isEmpty(paramDPObject.getString("UserName")));
  }

  private void openCouponPackage()
  {
    getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.mCouponEntryDo.getString("Url"))));
  }

  private void openJoinedThirdPartyCard(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("NavigateUrl");
    if (TextUtils.isEmpty(str))
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.putExtra("title", paramDPObject.getString("Title"));
    localIntent.setData(Uri.parse(str));
    startActivity(localIntent);
  }

  private void queryUserCoupon()
  {
    if (this.mUserCouponRequest != null)
      mapiService().abort(this.mUserCouponRequest, this, true);
    this.mUserCouponRequest = queryUserCouponRequest();
    mapiService().exec(this.mUserCouponRequest, this);
  }

  private MApiRequest queryUserCouponRequest()
  {
    return BasicMApiRequest.mapiGet(Uri.parse("http://mc.api.dianping.com/couponentry.mc").buildUpon().appendQueryParameter("token", accountService().token()).toString(), CacheType.DISABLED);
  }

  private void refreshCardList()
  {
    this.adapter.reset();
    this.adapter.notifyDataSetChanged();
  }

  private void showMoreFeature()
  {
    MCUtils.showMembercardMoreFeature(getActivity(), this.cardObject, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          paramDialogInterface = MyCardFragment.this.getAccount();
          FragmentActivity localFragmentActivity = MyCardFragment.this.getActivity();
          String str = ConfigHelper.mcWeixinUrl;
          Location localLocation = MyCardFragment.this.location();
          paramInt = MyCardFragment.this.cityId();
          if (paramDialogInterface == null);
          for (paramDialogInterface = ""; ; paramDialogInterface = paramDialogInterface.token())
          {
            MCUtils.membercardShare2WeiXin(localFragmentActivity, true, str, localLocation, paramInt, paramDialogInterface, MyCardFragment.this.cardObject);
            return;
          }
        case 1:
        }
        MCUtils.membercardShare2ThirdParty(MyCardFragment.this.getActivity(), MyCardFragment.this.cardObject, MyCardFragment.this.getAccount().feedFlag());
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
          MyCardFragment.this.showConfirmDialog("商家不让用");
          return;
        case 1:
          MyCardFragment.this.showConfirmDialog("优惠与描述不符");
          return;
        case 2:
        }
        MCUtils.gotoMembercardFeedBack(MyCardFragment.this.getActivity(), MyCardFragment.this.cardObject);
      }
    }
    , new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MyCardFragment.this.showProgressDialog("正在提交请求，请稍候...");
        MyCardFragment.this.mJoinMCHandler2.quitThirdPartyCard(MyCardFragment.this.membercardid);
      }
    });
  }

  private void unloadFragment()
  {
    try
    {
      synchronized (this.lockForLoadedFragment)
      {
        if (this.loadedFragment != null)
        {
          FragmentTransaction localFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
          localFragmentTransaction.remove(this.loadedFragment);
          localFragmentTransaction.commit();
          this.loadedFragment = null;
        }
        return;
      }
    }
    catch (Exception localException)
    {
      Log.e("Exception on removing loadedFragment!");
    }
  }

  private void updateCouponInfo(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      this.mListView.updateCouponEntryHeaderViewVisibility(8);
      return;
    }
    this.mCouponEntryDo = paramDPObject;
    this.mListView.updateCouponEntryHeaderViewVisibility(0);
    this.mListView.updateCouponEntryHeaderViewContents(paramDPObject.getString("Text"), String.valueOf(paramDPObject.getInt("Count") + String.valueOf(paramDPObject.getString("Unit"))));
    this.mListView.setCouponEntryViewListener(new CouponEntryView.OnCouponEntryViewClickListener()
    {
      public void couponEntryViewClick()
      {
        MyCardFragment.this.openCouponPackage();
      }
    });
  }

  private void updateSelectedCardStatus(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    this.membercard = new MemberCard(this.cardObject);
    this.membercardid = String.valueOf(paramDPObject.getInt("MemberCardID"));
    this.shopid = paramDPObject.getInt("ShopID");
    Object localObject = paramDPObject.getString("Title");
    String str = paramDPObject.getString("SubTitle");
    localObject = new StringBuilder().append((String)localObject);
    if (TextUtils.isEmpty(str));
    for (str = ""; ; str = "(" + str + ")")
    {
      ((StringBuilder)localObject).append(str).toString();
      this.cardType = paramDPObject.getInt("CardType");
      paramDPObject.getString("CardProductURL");
      paramDPObject.getString("QRCode");
      this.usercardlevel = this.cardObject.getInt("UserCardLevel");
      paramDPObject = this.membercard.getVIPCard();
      if (paramDPObject != null)
        paramDPObject.getDouble("ProductValue");
      return;
    }
  }

  public void addCard(Intent paramIntent)
  {
    this.membercardid = paramIntent.getStringExtra("membercardid");
    this.currentmembercardid = this.membercardid;
    this.adapter.reset();
    this.mListView.setAdapter(this.adapter);
    this.adapter.notifyDataSetChanged();
    this.mListView.setSelection(0);
    setmIsCardBox(true);
  }

  public void addUserCommentTask(String paramString)
  {
    showProgressDialog("正在提交请求，请稍候...");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("membercardid");
    localArrayList.add(String.valueOf(this.membercardid));
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.shopid));
    localArrayList.add("comment");
    localArrayList.add(paramString);
    this.mAddUserCommentRequest = BasicMApiRequest.mapiPost("http://mc.api.dianping.com/addusercomment.mc", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.mAddUserCommentRequest, this);
  }

  public void getMyCardListTask(int paramInt)
  {
    if (paramInt == 0)
      queryUserCoupon();
    if (this.mCardListRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/");
    localStringBuilder.append("getmycardlist.v5.mc?");
    localStringBuilder.append("uuid=");
    localStringBuilder.append(Environment.uuid());
    if (accountService().token() != null)
      localStringBuilder.append("&token=").append(accountService().token());
    if (this.membercardid != null)
      localStringBuilder.append("&cardids=").append(this.membercardid);
    Object localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(((Location)localObject).latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(((Location)localObject).longitude()));
    }
    localObject = NovaApplication.instance().getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    localStringBuilder.append("&startindex=").append(paramInt);
    this.mCardListRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mCardListRequest, this);
  }

  public void goBackShopInfo()
  {
    getActivity().finish();
  }

  public void gotoAvailableCardList()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://availablecardlist?source=14")));
  }

  public void gotoMemberInfo()
  {
    hideLoadedFragment();
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://memberinfo?membercardid=" + this.membercardid + "&from=1")), MEMBER_INFO_REQ);
  }

  public void gotoShopInfo()
  {
    if (this.shopid == 0)
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.shopid));
    localIntent.putExtra("fromMC", 1);
    startActivity(localIntent);
    statisticsEvent("mycard5", "mycard5_detail_shop", this.cardType + "|" + this.shopid, 0);
  }

  public boolean ismIsCardBox()
  {
    return this.mIsCardBox;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    parseIntent();
    onTitleBarChange();
    ((TextView)getView().findViewById(16908310)).setText("我的会员卡");
    getView().findViewById(R.id.left_title_button).setOnClickListener(this);
    this.refreshMallCardHandler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        paramMessage = (DPObject)paramMessage.getData().get("card");
        if (paramMessage != null)
          MyCardFragment.this.inflateFragment(paramMessage, MyCardFragment.CardDetailInflateMode.FULL_MEMBER_CARD);
      }
    };
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    onTitleBarChange();
    if ((paramInt2 == 10) && (paramInt1 == MEMBER_INFO_REQ))
    {
      paramIntent = paramIntent.getStringExtra("username");
      this.adapter.updateUserInfo(paramIntent);
      this.mListView.updateCurrrentUserName(paramIntent);
      this.adapter.notifyDataSetChanged();
      if ((this.cardDetailInflateMode == CardDetailInflateMode.FULL_MEMBER_CARD) && (this.loadedFragment != null) && (!(this.loadedFragment instanceof MemberCardRetryFragment)))
      {
        unloadFragment();
        inflateFragment(this.cardObject, CardDetailInflateMode.FULL_MEMBER_CARD);
      }
    }
    do
    {
      return;
      onCardBoxButtonClick();
      return;
      if (paramInt2 == 20)
      {
        this.cardObject = ((DPObject)paramIntent.getParcelableExtra("cardobject"));
        this.adapter.replaceCard(this.cardObject);
        this.adapter.notifyDataSetChanged();
        if ((this.cardDetailInflateMode == CardDetailInflateMode.FULL_MEMBER_CARD) && (this.loadedFragment != null) && (!(this.loadedFragment instanceof MemberCardRetryFragment)))
        {
          unloadFragment();
          inflateFragment(this.cardObject, CardDetailInflateMode.FULL_MEMBER_CARD);
          return;
        }
        onCardBoxButtonClick();
        return;
      }
      if ((paramInt1 != MEMBER_INFO_REQ) || (paramInt2 == 20))
        continue;
      onCardBoxButtonClick();
      return;
    }
    while (paramInt2 != 50);
    paramInt1 = paramIntent.getIntExtra("membercardid", 0);
    this.loadedFragment.refreshByCardId(paramInt1);
  }

  public void onCardBoxButtonClick()
  {
    this.abortStatus.openAbortStatus();
    if (this.cardDetailRequestTask != null)
    {
      Log.v("CardRequest", "OnCardBoxButtonClick_really_abort");
      this.cardDetailRequestTask.abortRequest();
    }
    Log.v("CardRequest", "OnCardBoxButtonClick");
    unloadFragment();
    setmIsCardBox(true);
    this.shopid = 0;
    onTitleBarChange();
    this.mListView.openList(true);
    if (this.mCardListHolder != null)
      handlerCardListData(this.mCardListHolder);
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt)
  {
    if (this.abortStatus.isCardDetailAborted(paramInt))
      return;
    while (true)
    {
      synchronized (this.lockForLoadedFragment)
      {
        if ((this.loadedFragment == null) || (!(this.loadedFragment instanceof MemberCardRetryFragment)))
          continue;
        String str2 = "错误，服务暂时不可用，请稍候再试";
        str1 = str2;
        if (paramMApiResponse == null)
          continue;
        str1 = str2;
        if (paramMApiResponse.message() == null)
          continue;
        if ("".equals(paramMApiResponse.message()))
        {
          str1 = str2;
          ((MemberCardRetryFragment)this.loadedFragment).showFailedView(str1, new LoadingErrorView.LoadRetry(paramInt)
          {
            public void loadRetry(View paramView)
            {
              if (MyCardFragment.this.loadedFragment != null)
              {
                ((MemberCardRetryFragment)MyCardFragment.this.loadedFragment).showLoadingView();
                MyCardFragment.this.cardDetailRequestTask.doRequest(this.val$memberCardId, MyCardFragment.this.usercardlevel);
              }
            }
          });
          return;
        }
      }
      String str1 = paramMApiResponse.message().toString();
    }
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, CardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    if (this.abortStatus.isCardDetailAborted(paramDPObject.getInt("MemberCardID")))
      return;
    if (!isUserNameEmpty(paramDPObject))
    {
      inflateFragment(paramDPObject, CardDetailInflateMode.FULL_MEMBER_CARD);
      return;
    }
    gotoMemberInfo();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.left_title_button)
      onLeftButtonClick();
    do
    {
      return;
      if (paramView.getId() != R.id.right_title_button)
        continue;
      showMoreFeature();
      return;
    }
    while (paramView.getId() != R.id.title_button);
    onTitleButtonClick();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.QUIT_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.JOIN_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.MYCARDFRAGMENT_CHECK_LOGIN");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_CARD_INFO");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("Card:CardChanged");
    registerReceiver(this.mReceiver, paramBundle);
    this.mJoinMCHandler2 = new JoinMCHandler2((NovaActivity)getActivity());
    this.mJoinMCHandler2.setOnQuitThirdPartyCardHandlerListener(this.onQuitCardHandler);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.my_card_tab_layout, paramViewGroup, false);
    this.mListView = ((MemberCardListView)paramLayoutInflater.findViewById(R.id.card_listview));
    this.adapter = new Adapter(getActivity());
    this.mListView.setAdapter(this.adapter);
    this.mListView.setOnItemClickWithAnimListener(this);
    this.mListView.setOnCardInfoClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MyCardFragment.this.gotoShopInfo();
      }
    });
    this.mListView.setOnRefreshListener(this.onRefreshListener);
    paramViewGroup = cacheCardList();
    if (paramViewGroup != null)
      this.adapter.appendCards(paramViewGroup);
    paramLayoutInflater.findViewById(R.id.card_fragment_layout).setVisibility(8);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mAddUserCommentRequest != null)
    {
      mapiService().abort(this.mAddUserCommentRequest, this, true);
      this.mAddUserCommentRequest = null;
    }
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
    if (this.mUserCouponRequest != null)
    {
      mapiService().abort(this.mUserCouponRequest, this, true);
      this.mUserCouponRequest = null;
    }
    if (this.mCardListRequest != null)
    {
      mapiService().abort(this.mCardListRequest, this, true);
      this.mCardListRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClickAfterAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      if (!MemberCard.isThirdPartyCard(paramAdapterView))
        break label30;
      onCardBoxButtonClick();
    }
    label30: 
    do
    {
      return;
      getView().findViewById(R.id.card_fragment_layout).setVisibility(0);
    }
    while (this.adapter.getData().size() <= 0);
    this.mPosition = paramInt;
    if (isUserNameEmpty(paramAdapterView))
    {
      gotoMemberInfo();
      return;
    }
    paramView = paramAdapterView.getString("Title");
    this.cardDetailRequestTask.doRequest(paramAdapterView.getInt("MemberCardID"), this.usercardlevel);
    if (this.from == 4)
    {
      paramAdapterView = "1";
      statisticsEvent("mycard5", "mycard5_list_detail", paramAdapterView, 0);
      return;
    }
    paramView = new StringBuilder().append("0|").append(this.membercardid).append("|").append(paramView).append("|");
    if (NetworkUtils.isConnectingToInternet(getActivity().getApplication()));
    for (paramAdapterView = "联网"; ; paramAdapterView = "离线")
    {
      paramAdapterView = paramAdapterView;
      break;
    }
  }

  public void onItemClickBeforAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = (DPObject)paramAdapterView.getAdapter().getItem(paramInt);
    if (MemberCard.isThirdPartyCard(paramView))
    {
      openJoinedThirdPartyCard(paramView);
      return;
    }
    int i = -1;
    if (paramView != null)
      i = paramView.getInt("MemberCardID");
    this.abortStatus.initAbortStatus(i);
    setmIsCardBox(false);
    onTitleBarChange();
    getView().findViewById(R.id.card_fragment_layout).setVisibility(4);
    inflateFragment((DPObject)paramAdapterView.getAdapter().getItem(paramInt), CardDetailInflateMode.SIMPLE_MEMBER_CARD);
  }

  public void onLeftButtonClick()
  {
    if ("membercardinfo".equals(this.host))
    {
      goBackShopInfo();
      return;
    }
    if (ismIsCardBox())
    {
      getActivity().finish();
      return;
    }
    onCardBoxButtonClick();
    statisticsEvent("mycard5", "mycard5_detail_list", "", 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissProgressDialog();
    if (paramMApiRequest == this.mAddUserCommentRequest)
    {
      ((NovaActivity)getActivity()).showMessageDialog(paramMApiResponse.message());
      this.mAddUserCommentRequest = null;
    }
    do
    {
      return;
      if (paramMApiRequest != this.mUserCouponRequest)
        continue;
      updateCouponInfo((DPObject)DPCache.getInstance().getParcelable(couponEntryCacheKey(), "membercardcoupon", 31539600000L, DPObject.CREATOR));
      return;
    }
    while (paramMApiRequest != this.mCardListRequest);
    if (this.isPullToRefreshing)
    {
      this.isPullToRefreshing = false;
      this.mListView.onRefreshComplete();
      if (this.adapter.getmNextStartIndex() == 0)
        this.adapter.reset();
    }
    paramMApiResponse = paramMApiResponse.message().content();
    Adapter localAdapter = this.adapter;
    paramMApiRequest = paramMApiResponse;
    if (paramMApiResponse == null)
      paramMApiRequest = "错误，服务暂时不可用，请稍候再试";
    localAdapter.setErrorMsg(paramMApiRequest);
    this.adapter.notifyDataSetChanged();
    this.mCardListRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissProgressDialog();
    if ((paramMApiRequest == this.mAddUserCommentRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      Toast.makeText(getActivity(), paramMApiRequest.getString("Content"), 0).show();
      this.mAddUserCommentRequest = null;
    }
    do
      while (true)
      {
        return;
        if (paramMApiRequest != this.mUserCouponRequest)
          break;
        if (!(paramMApiResponse.result() instanceof DPObject))
          continue;
        updateCouponInfo((DPObject)paramMApiResponse.result());
        DPCache.getInstance().put(couponEntryCacheKey(), "membercardcoupon", (DPObject)paramMApiResponse.result(), 31539600000L);
        return;
      }
    while ((paramMApiRequest != this.mCardListRequest) || (!(paramMApiResponse.result() instanceof DPObject)));
    if (this.isPullToRefreshing)
    {
      this.isPullToRefreshing = false;
      this.mListView.onRefreshComplete();
      this.adapter.reset();
    }
    hookHandleCardListData((DPObject)paramMApiResponse.result());
  }

  public void onResume()
  {
    super.onResume();
    if (this.isPreparedToRefresh)
    {
      this.isPreparedToRefresh = false;
      trigerRefreshing();
    }
  }

  public void onStart()
  {
    super.onStart();
    this.cardDetailRequestTask = new CardDetailRequestTask(this);
  }

  public void onTitleBarChange()
  {
    Object localObject = getView();
    if (localObject == null)
      return;
    View localView = ((View)localObject).findViewById(R.id.right_title_button);
    localObject = (TextView)(TextView)((View)localObject).findViewById(R.id.title_button);
    if (localView != null)
      if (!ismIsCardBox())
        break label77;
    label77: for (int i = 8; ; i = 0)
    {
      localView.setVisibility(i);
      localView.setOnClickListener(this);
      ((ImageView)localView).setImageResource(R.drawable.title_icon_more);
      if (localObject == null)
        break;
      ((TextView)localObject).setVisibility(8);
      return;
    }
  }

  public void onTitleButtonClick()
  {
    if (ismIsCardBox())
    {
      statisticsEvent("mycard5", "mycard5_list_add", "", 0);
      gotoAvailableCardList();
    }
    do
      return;
    while (!this.isFromScoreUrl);
    this.loadedFragment.gotoScoreUrl();
    this.isFromScoreUrl = false;
  }

  public void parseIntent()
  {
    boolean bool2 = true;
    Object localObject = getActivity().getIntent().getData();
    this.host = ((Uri)localObject).getHost();
    boolean bool1;
    if ("membercardinfo".equals(this.host))
      bool1 = false;
    while (true)
    {
      setmIsCardBox(bool1);
      if (!ismIsCardBox())
      {
        bool1 = bool2;
        this.mFirst = bool1;
        this.membercardid = ((Uri)localObject).getQueryParameter("membercardid");
        localObject = ((Uri)localObject).getQueryParameter("from");
      }
      try
      {
        this.from = Integer.parseInt((String)localObject);
        label82: this.currentmembercardid = this.membercardid;
        return;
        bool1 = true;
        continue;
        bool1 = false;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label82;
      }
    }
  }

  public void prepareToRefresh()
  {
    this.isPreparedToRefresh = true;
  }

  public void setmIsCardBox(boolean paramBoolean)
  {
    this.mIsCardBox = paramBoolean;
  }

  public void showCardBox()
  {
    this.mListView.openList();
  }

  public void showConfirmDialog(String paramString)
  {
    new AlertDialog.Builder(getActivity()).setTitle("确认投诉").setPositiveButton("确定", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MyCardFragment.this.addUserCommentTask(this.val$s);
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MyCardFragment.this.statisticsEvent("mycard5", "mycard5_complaint_cancel", this.val$s, 0);
      }
    }).show();
  }

  public void trigerRefreshing()
  {
    this.mListView.postDelayed(new Runnable()
    {
      public void run()
      {
        MyCardFragment.this.mListView.trigerRefreshing();
      }
    }
    , 500L);
  }

  class Adapter extends MemberCardListAdapter
  {
    public Adapter(Activity arg2)
    {
      super();
    }

    public void appendCards(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      int i = paramDPObject.getInt("StartIndex");
      if ((arrayOfDPObject != null) && (i == getmNextStartIndex()))
      {
        if (getmNextStartIndex() != 0)
          break label101;
        getData().clear();
        getData().addAll(Arrays.asList(arrayOfDPObject));
      }
      while (true)
      {
        setmIsEnd(paramDPObject.getBoolean("IsEnd"));
        setmEmptyMsg(paramDPObject.getString("EmptyMsg"));
        setmNextStartIndex(paramDPObject.getInt("NextStartIndex"));
        setmRecordCount(paramDPObject.getInt("RecordCount"));
        notifyDataSetChanged();
        return;
        label101: getData().addAll(Arrays.asList(arrayOfDPObject));
      }
    }

    public int getCount()
    {
      if (MyCardFragment.this.isPullToRefreshing)
        return getData().size();
      return super.getCount();
    }

    protected boolean loadNewPage()
    {
      if (ismIsEnd());
      do
        return false;
      while (MyCardFragment.this.mCardListRequest != null);
      MyCardFragment.this.getMyCardListTask(getmNextStartIndex());
      return true;
    }

    public void replaceCard(DPObject paramDPObject)
    {
      int i = 0;
      while (true)
      {
        Object localObject;
        String str3;
        if (i < getData().size())
        {
          localObject = (DPObject)getData().get(i);
          String str1 = ((DPObject)localObject).getString("MemberCardGroupID");
          String str2 = paramDPObject.getString("MemberCardGroupID");
          localObject = ((DPObject)localObject).getString("MemberCardID");
          str3 = paramDPObject.getString("MemberCardID");
          if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)) && (str1.equals(str2)))
            getData().set(i, paramDPObject);
        }
        else
        {
          return;
        }
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (!TextUtils.isEmpty(str3)) && (((String)localObject).equals(str3)))
        {
          getData().set(i, paramDPObject);
          return;
        }
        i += 1;
      }
    }

    protected void reset()
    {
      if (MyCardFragment.this.isPullToRefreshing)
      {
        setmIsEnd(false);
        setmErrorMsg(null);
        setmEmptyMsg(null);
        setmNextStartIndex(0);
        setmRecordCount(0);
        loadNewPage();
        return;
      }
      super.reset();
    }

    public void updateUserInfo(String paramString)
    {
      int i = 0;
      if (i < getData().size())
      {
        DPObject localDPObject = ((DPObject)getData().get(i)).edit().putString("UserName", paramString).generate();
        getData().set(i, localDPObject);
        DPObject[] arrayOfDPObject;
        if (!TextUtils.isEmpty(localDPObject.getString("MemberCardGroupID")))
        {
          arrayOfDPObject = localDPObject.getArray("SubCardList");
          if (arrayOfDPObject != null)
            break label93;
        }
        while (true)
        {
          i += 1;
          break;
          label93: int j = 0;
          while (j < arrayOfDPObject.length)
          {
            arrayOfDPObject[j] = arrayOfDPObject[j].edit().putString("UserName", paramString).generate();
            j += 1;
          }
          localDPObject = localDPObject.edit().putArray("SubCardList", arrayOfDPObject).generate();
          getData().set(i, localDPObject);
        }
      }
    }
  }

  private static enum CardDetailInflateMode
  {
    static
    {
      FULL_MEMBER_CARD = new CardDetailInflateMode("FULL_MEMBER_CARD", 1);
      $VALUES = new CardDetailInflateMode[] { SIMPLE_MEMBER_CARD, FULL_MEMBER_CARD };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.MyCardFragment
 * JD-Core Version:    0.6.0
 */