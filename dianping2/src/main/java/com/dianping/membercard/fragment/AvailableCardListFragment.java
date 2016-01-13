package com.dianping.membercard.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractFilterFragment.OnFilterItemClickListener;
import com.dianping.base.widget.NovaListFragment;
import com.dianping.base.widget.ThreeFilterFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.model.JoinMCHandler;
import com.dianping.membercard.model.JoinMCHandler.OnJoinCardRequestHandlerListener;
import com.dianping.membercard.model.JoinMCHandler2;
import com.dianping.membercard.model.JoinMCHandler2.OnJoinThirdPartyCardHandlerListener;
import com.dianping.membercard.model.JoinMScoreCHandler;
import com.dianping.membercard.model.JoinMScoreCHandler.OnJoinScoreCardHandlerListener;
import com.dianping.membercard.utils.BasicCardAdapter;
import com.dianping.membercard.utils.MCUtils;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.membercard.view.AvailableCardListItem;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvailableCardListFragment extends NovaListFragment
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>, AbstractFilterFragment.OnFilterItemClickListener, AccountListener, JoinMCHandler.OnJoinCardRequestHandlerListener, LoginResultListener
{
  private static final String TITLE = "会员卡";
  private final String MESSAGE_DIALOG_ADD_CARD_WAITING = "正在提交请求，请稍候...";
  private final int SCORE_CARD_BIND_REQUEST_CODE = 10;
  private final int THIRD_PARTY_CARD_BIND_REQUEST_CODE_255 = 255;
  private CardAdapter adapter;
  private int categoryId = 0;
  private String categoryName;
  private DPObject[] categoryNavs;
  private ThreeFilterFragment filterFragment;
  private DPObject[] filterNavs;
  private MApiRequest getAvailableCardListRequest;
  private boolean isFirstPage = true;
  private int joinCardType = 0;
  private JoinMCHandler2 joinMCHandler2;
  private JoinMScoreCHandler joinMScoreCHandler;
  private String keyword = "";
  private ArrayList<DPObject> lists = new ArrayList();
  private Location location;
  private JoinMCHandler mJoinMCHandler;
  private MyReceiver mReceiver;
  private int membercardid;
  private int nextStartIndex;
  private JoinMScoreCHandler.OnJoinScoreCardHandlerListener onJoinScoreCardHandlerListener = new JoinMScoreCHandler.OnJoinScoreCardHandlerListener()
  {
    public void onJoinScoreCardFail(String paramString)
    {
      AvailableCardListFragment.this.dismissDialog();
      Toast.makeText(AvailableCardListFragment.this.getActivity(), paramString, 0).show();
    }

    public void onJoinScoreCardFailForNeedMemberInfo(String paramString1, String paramString2)
    {
      AvailableCardListFragment.this.dismissDialog();
      AvailableCardListFragment.this.openBindScoreCardWebview(paramString2);
    }

    public void onJoinScoreCardSuccess()
    {
      AvailableCardListFragment.this.dismissDialog();
      MCUtils.sendJoinScoreCardSuccessBroadcast(AvailableCardListFragment.this.getActivity(), String.valueOf(AvailableCardListFragment.this.membercardid));
    }
  };
  private JoinMCHandler2.OnJoinThirdPartyCardHandlerListener onJoinThirdPartyCardHandlerListener = new JoinMCHandler2.OnJoinThirdPartyCardHandlerListener()
  {
    public void onRequestJoinThirdPartyCardFailed(SimpleMsg paramSimpleMsg)
    {
      AvailableCardListFragment.this.dismissDialog();
      Toast.makeText(AvailableCardListFragment.this.getActivity(), paramSimpleMsg.content(), 0).show();
    }

    public void onRequestJoinThirdPartyCardSuccess(DPObject paramDPObject)
    {
      AvailableCardListFragment.this.dismissDialog();
      int i = paramDPObject.getInt("Code");
      String str = paramDPObject.getString("Msg");
      paramDPObject = paramDPObject.getString("RedirectUrl");
      if (i == 200)
      {
        MCUtils.sendJoinScoreCardSuccessBroadcast(AvailableCardListFragment.this.getActivity(), String.valueOf(AvailableCardListFragment.this.membercardid));
        MCUtils.sendUpdateMemberCardListBroadcast(AvailableCardListFragment.this.getActivity(), String.valueOf(AvailableCardListFragment.this.membercardid));
        return;
      }
      if (i == 201)
      {
        AvailableCardListFragment.this.openBindThirdPartyCardWebview(paramDPObject);
        return;
      }
      Toast.makeText(AvailableCardListFragment.this.getActivity(), str, 0).show();
    }
  };
  private int openSearch;
  private int regionId = 0;
  private String regionName;
  private DPObject[] regionNavs;
  private MApiRequest searchCardListRequest;
  private int sort = 0;
  private String sortName;
  private int source = 14;
  private int trigger = -1;

  private void addScoreCard(int paramInt1, int paramInt2)
  {
    if (!isLogin())
    {
      this.joinCardType = 1;
      accountService().login(this);
      return;
    }
    showProgressDialog("正在提交请求，请稍候...");
    this.joinMScoreCHandler.joinScoreCards(String.valueOf(paramInt1), paramInt2);
  }

  private void addThirdPartyCard(int paramInt1, int paramInt2)
  {
    if (!isLogin())
    {
      this.joinCardType = 2;
      accountService().login(this);
      return;
    }
    showProgressDialog("正在提交请求，请稍候...");
    this.joinMCHandler2.joinThirdPartyCards(String.valueOf(paramInt1), paramInt2);
  }

  private void appendCards(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
    int i = paramDPObject.getInt("StartIndex");
    if ((arrayOfDPObject != null) && (i == this.nextStartIndex))
    {
      if (this.nextStartIndex != 0)
        break label110;
      this.lists.clear();
      this.lists.addAll(Arrays.asList(arrayOfDPObject));
      this.adapter.replaceAll(Arrays.asList(arrayOfDPObject));
    }
    while (true)
    {
      this.adapter.updateEndStatus(paramDPObject.getBoolean("IsEnd"));
      this.adapter.setEmptyString(paramDPObject.getString("EmptyMsg"));
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.adapter.notifyDataSetChanged();
      return;
      label110: this.lists.addAll(Arrays.asList(arrayOfDPObject));
      this.adapter.addAll(Arrays.asList(arrayOfDPObject));
    }
  }

  private void buildFilterParameter()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    DPObject.Editor localEditor;
    if (this.categoryId > 0)
    {
      localEditor = new DPObject("Category").edit().putInt("ID", this.categoryId);
      if (TextUtils.isEmpty(this.categoryName))
      {
        localObject1 = "";
        localObject1 = localEditor.putString("Name", (String)localObject1).putInt("ParentID", -1).putInt("Distance", 500).generate();
      }
    }
    else
    {
      if (this.regionId > 0)
      {
        localEditor = new DPObject("Region").edit().putInt("ID", this.regionId);
        if (!TextUtils.isEmpty(this.regionName))
          break label249;
        localObject2 = "";
        label137: localObject2 = localEditor.putString("Name", (String)localObject2).putInt("ParentID", 0).generate();
      }
      if (this.sort > 0)
      {
        localEditor = new DPObject("Pair").edit().putString("ID", String.valueOf(this.sort));
        if (!TextUtils.isEmpty(this.sortName))
          break label257;
      }
    }
    label257: for (localObject3 = ""; ; localObject3 = this.sortName)
    {
      localObject3 = localEditor.putString("Name", (String)localObject3).generate();
      this.filterFragment.updateNavs((DPObject)localObject1, (DPObject)localObject2, (DPObject)localObject3);
      return;
      localObject1 = this.categoryName;
      break;
      label249: localObject2 = this.regionName;
      break label137;
    }
  }

  private void gotoChainCard(String paramString, int paramInt, boolean paramBoolean)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://chaincardlist?membercardgroupid=" + paramString + "&source=" + this.source + "&flag=" + paramInt + "&isscorecard=" + paramBoolean));
    startActivityForResult(localIntent, 10);
  }

  private void handlerResult(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      int i;
      do
      {
        return;
        setupFilterView(paramDPObject);
        DPObject localDPObject = paramDPObject.getObject("CardList");
        if (localDPObject != null)
          appendCards(localDPObject);
        i = paramDPObject.getInt("StartIndex");
      }
      while ((paramDPObject.getInt("RecordCount") != 0) || (i != 0));
      paramDPObject = paramDPObject.getArray("SearchTipList");
    }
    while (paramDPObject == null);
    this.adapter.replaceAll(Arrays.asList(paramDPObject));
  }

  private void initSearchParameters()
  {
    this.keyword = getArguments().getString("keyword");
    if (this.keyword == null)
      this.keyword = "";
    if (!TextUtils.isEmpty(getArguments().getString("trigger")))
      this.trigger = Integer.parseInt(getArguments().getString("trigger"));
    if (!TextUtils.isEmpty(getArguments().getString("sort")))
      this.sort = Integer.parseInt(getArguments().getString("sort"));
    if (!TextUtils.isEmpty(getArguments().getString("regionid")))
      this.regionId = Integer.parseInt(getArguments().getString("regionid"));
    if (!TextUtils.isEmpty(getArguments().getString("categoryid")))
      this.categoryId = Integer.parseInt(getArguments().getString("categoryid"));
    if (!TextUtils.isEmpty(getArguments().getString("categoryname")))
      this.categoryName = getArguments().getString("categoryname");
    if (!TextUtils.isEmpty(getArguments().getString("regionname")))
      this.regionName = getArguments().getString("regionname");
    if (!TextUtils.isEmpty(getArguments().getString("sortname")))
      this.sortName = getArguments().getString("sortname");
    if (!TextUtils.isEmpty(getArguments().getString("showsearch")))
      this.openSearch = Integer.parseInt(getArguments().getString("showsearch"));
    if (!TextUtils.isEmpty(getArguments().getString("source")))
      this.source = Integer.parseInt(getArguments().getString("source"));
  }

  private boolean isLogin()
  {
    return getAccount() != null;
  }

  private void openBindScoreCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", String.valueOf(this.membercardid));
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivity(paramString);
  }

  private void openBindThirdPartyCardWebview(String paramString)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("membercardids", String.valueOf(this.membercardid));
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
    paramString.putExtras(localBundle);
    startActivity(paramString);
  }

  private void openThirdPartyCardInfoView(String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse(paramString1));
    localIntent.putExtra("title", paramString2);
    localIntent.putExtra("source", 14);
    startActivity(localIntent);
  }

  private void searchCardListTask(String paramString, int paramInt)
  {
    paramString = new StringBuilder("http://mc.api.dianping.com/");
    paramString.append("searchcardlist.v2.mc?");
    paramString.append("cityid=").append(cityId());
    if (accountService().token() != null)
      paramString.append("&token=").append(accountService().token());
    paramString.append("&uuid=");
    paramString.append(Environment.uuid());
    Object localObject = getResources().getDisplayMetrics();
    paramString.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    paramString.append("&startindex=").append(paramInt);
    paramString.append("&kw=").append(this.keyword);
    paramString.append("&regionid=").append(this.regionId);
    paramString.append("&categoryid=").append(this.categoryId);
    if (this.sort > 0)
      paramString.append("&sort=").append(this.sort);
    if (this.trigger > -1)
      paramString.append("&trigger=").append(this.trigger);
    if (this.location != null)
    {
      localObject = Location.FMT;
      paramString.append("&lat=").append(((NumberFormat)localObject).format(this.location.latitude()));
      paramString.append("&lng=").append(((NumberFormat)localObject).format(this.location.longitude()));
    }
    while (true)
    {
      paramString.append("&source=").append(this.source);
      this.searchCardListRequest = BasicMApiRequest.mapiGet(paramString.toString(), CacheType.DISABLED);
      mapiService().exec(this.searchCardListRequest, this);
      return;
      if ((this.sortName == null) || (!this.sortName.contains("距离")))
        continue;
      Toast.makeText(getActivity(), "暂时无法获取您的定位信息，请确认定位已开启或稍后再试", 1).show();
    }
  }

  private void setupFilterView(DPObject paramDPObject)
  {
    if (this.isFirstPage);
    try
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("CategoryNavs");
      if (this.trigger != 1)
        if ((arrayOfDPObject == null) || (arrayOfDPObject.length <= 0))
          break label168;
      label168: for (this.categoryNavs = arrayOfDPObject; ; this.categoryNavs = null)
      {
        arrayOfDPObject = paramDPObject.getArray("FilterNavs");
        if (this.trigger != 0)
        {
          if (arrayOfDPObject == null)
            break;
          this.filterNavs = arrayOfDPObject;
        }
        arrayOfDPObject = paramDPObject.getArray("RegionNavs");
        if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
          this.regionNavs = arrayOfDPObject;
        if (paramDPObject.getObject("CurrentFilter") != null)
        {
          paramDPObject = paramDPObject.getObject("CurrentFilter");
          this.sort = Integer.valueOf(paramDPObject.getString("ID")).intValue();
          this.sortName = paramDPObject.getString("Name");
          this.filterFragment.updateNavs(null, null, paramDPObject);
        }
        updateSelectedFilter();
        this.filterFragment.setNavs(this.categoryNavs, this.regionNavs, this.filterNavs);
        this.isFirstPage = false;
        return;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e("mc", "parse filter failed", localException);
        continue;
        this.filterNavs = null;
      }
    }
  }

  private void setupListAdapter(List<DPObject> paramList)
  {
    if (this.adapter != null)
    {
      this.adapter.notifyDataSetChanged();
      return;
    }
    this.adapter = new CardAdapter(getActivity(), paramList, true);
    setListAdapter(this.adapter);
  }

  private void updateSelectedFilter()
  {
    if (!this.isFirstPage)
      return;
    Object localObject3 = null;
    Object localObject2 = null;
    Object localObject4 = null;
    Object localObject1 = localObject2;
    int i;
    if (this.regionId > 0)
    {
      localObject1 = localObject2;
      if (this.regionNavs != null)
      {
        i = 0;
        label36: localObject1 = localObject2;
        if (i < this.regionNavs.length)
        {
          if (this.regionId != this.regionNavs[i].getInt("ID"))
            break label230;
          localObject1 = this.regionNavs[i];
        }
      }
    }
    localObject2 = localObject3;
    if (this.categoryId > 0)
    {
      localObject2 = localObject3;
      if (this.categoryNavs != null)
      {
        i = 0;
        label97: localObject2 = localObject3;
        if (i < this.categoryNavs.length)
        {
          if (this.categoryId != this.categoryNavs[i].getInt("ID"))
            break label239;
          localObject2 = this.categoryNavs[i];
        }
      }
    }
    localObject3 = localObject4;
    if (this.sort > 0)
    {
      localObject3 = localObject4;
      if (this.filterNavs != null)
        i = 0;
    }
    while (true)
    {
      localObject3 = localObject4;
      if (i < this.filterNavs.length)
      {
        if (String.valueOf(this.sort).equals(this.filterNavs[i].getString("ID")))
          localObject3 = this.filterNavs[i];
      }
      else
      {
        if ((localObject2 == null) && (localObject1 == null) && (localObject3 == null))
          break;
        this.filterFragment.updateNavs((DPObject)localObject2, (DPObject)localObject1, (DPObject)localObject3);
        return;
        label230: i += 1;
        break label36;
        label239: i += 1;
        break label97;
      }
      i += 1;
    }
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

  public void gotoMyCard()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://membercardinfo?membercardid=" + this.membercardid)));
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    if (paramAccountService.profile() != null)
      reset();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mJoinMCHandler = new JoinMCHandler(getActivity());
    this.mJoinMCHandler.setOnJoinCardRequestHandlerListener(this);
    this.joinMScoreCHandler = new JoinMScoreCHandler((NovaActivity)getActivity());
    this.joinMScoreCHandler.setJoinScoreCardHandlerListener(this.onJoinScoreCardHandlerListener);
    this.joinMCHandler2 = new JoinMCHandler2((NovaActivity)getActivity());
    this.joinMCHandler2.setJoinThirdPartyCardHandlerListener(this.onJoinThirdPartyCardHandlerListener);
    initSearchParameters();
    this.location = location();
    accountService().addListener(this);
    setupListAdapter(null);
    getListView().setOnItemClickListener(this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mReceiver = new MyReceiver(null);
    paramBundle = new IntentFilter("com.dianping.action.QUIT_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.JOIN_MEMBER_CARD");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_LIST_DATA");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("Card:JoinSuccess");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.available_card_list, paramViewGroup, false);
    this.filterFragment = new ThreeFilterFragment();
    paramViewGroup = getFragmentManager().beginTransaction();
    paramViewGroup.add(R.id.fliter_fragment, this.filterFragment);
    paramViewGroup.commit();
    this.filterFragment.setOnFilterItemClickListener(this);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    accountService().removeListener(this);
    this.joinMScoreCHandler.setJoinScoreCardHandlerListener(null);
    this.joinMCHandler2.stopJoinThirdPartyCards();
    this.joinMCHandler2.setJoinThirdPartyCardHandlerListener(null);
    super.onDestroy();
  }

  public void onFilterItemClick(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3)
  {
    if (this.categoryId != paramDPObject1.getInt("ID"))
    {
      this.trigger = 1;
      statisticsEvent("availablecard5", "availablecard5_category", paramDPObject1.getString("Name"), 0);
    }
    this.categoryId = paramDPObject1.getInt("ID");
    if (this.regionId != paramDPObject2.getInt("ID"))
    {
      this.trigger = 0;
      statisticsEvent("availablecard5", "availablecard5_area", paramDPObject2.getString("Name"), 0);
    }
    this.regionId = paramDPObject2.getInt("ID");
    if (this.sort != Integer.valueOf(paramDPObject3.getString("ID")).intValue())
      statisticsEvent("availablecard5", "availablecard5_sort", paramDPObject3.getString("Name"), 0);
    this.sort = Integer.valueOf(paramDPObject3.getString("ID")).intValue();
    this.sortName = paramDPObject3.getString("Name");
    refresh();
  }

  public void onFinish()
  {
    if (this.getAvailableCardListRequest != null)
    {
      mapiService().abort(this.getAvailableCardListRequest, this, true);
      this.getAvailableCardListRequest = null;
    }
    if (this.searchCardListRequest != null)
    {
      mapiService().abort(this.searchCardListRequest, this, true);
      this.searchCardListRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!(paramView instanceof AvailableCardListItem));
    do
    {
      return;
      paramAdapterView = (DPObject)getListView().getItemAtPosition(paramInt);
    }
    while (!(paramAdapterView instanceof DPObject));
    this.membercardid = paramAdapterView.getInt("MemberCardID");
    paramView = paramAdapterView.getString("Title");
    if (paramAdapterView.getBoolean("Joined"))
    {
      if (MemberCard.isThirdPartyCard(paramAdapterView))
        openThirdPartyCardInfoView(paramAdapterView.getString("NavigateUrl"), paramView);
      while (true)
      {
        statisticsEvent("availablecard5", "availablecard5_item_view", this.membercardid + "|" + paramView, 0);
        return;
        gotoMyCard();
      }
    }
    String str = paramAdapterView.getString("MemberCardGroupID");
    if ((str != null) && (!TextUtils.isEmpty(str)))
      gotoChainCard(str, 0, MemberCard.isScoreCard(paramAdapterView));
    while (true)
    {
      statisticsEvent("availablecard5", "availablecard5_join", this.membercardid + "|" + paramView, 0);
      return;
      if (MemberCard.isThirdPartyCard(paramAdapterView))
      {
        openThirdPartyCardInfoView(paramAdapterView.getString("NavigateUrl"), paramView);
        continue;
      }
      paramInt = paramAdapterView.getInt("CardLevel");
      this.mJoinMCHandler.addCard(String.valueOf(this.membercardid), this.source, paramInt, paramAdapterView.getString("Title"));
    }
  }

  public void onJoinCardFinish(DPObject paramDPObject)
  {
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.joinCardType == 0)
      this.mJoinMCHandler.onLoginAddCard();
    do
    {
      return;
      if (this.joinCardType != 1)
        continue;
      addScoreCard(this.membercardid, this.source);
      return;
    }
    while (this.joinCardType != 2);
    addThirdPartyCard(this.membercardid, this.source);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message().toString();
    CardAdapter localCardAdapter = this.adapter;
    paramMApiRequest = paramMApiResponse;
    if (paramMApiResponse == null)
      paramMApiRequest = "出错了";
    localCardAdapter.setErrorString(paramMApiRequest);
    this.adapter.notifyDataSetChanged();
    this.getAvailableCardListRequest = null;
    this.searchCardListRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.getAvailableCardListRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      handlerResult((DPObject)paramMApiResponse.result());
      this.getAvailableCardListRequest = null;
      return;
    }
    if ((paramMApiRequest == this.searchCardListRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      handlerResult((DPObject)paramMApiResponse.result());
      this.searchCardListRequest = null;
      return;
    }
    paramMApiResponse = paramMApiResponse.message().toString();
    CardAdapter localCardAdapter = this.adapter;
    paramMApiRequest = paramMApiResponse;
    if (paramMApiResponse == null)
      paramMApiRequest = "出错了";
    localCardAdapter.setErrorString(paramMApiRequest);
    this.adapter.notifyDataSetChanged();
  }

  public void onStart()
  {
    super.onStart();
    buildFilterParameter();
  }

  public void refresh()
  {
    reset();
  }

  public void refreshList(String paramString, boolean paramBoolean)
  {
    if (paramString.contains(","))
      return;
    while (true)
    {
      int i;
      try
      {
        int j = Integer.parseInt(paramString);
        i = 0;
        if (i >= this.lists.size())
          break;
        int k = ((DPObject)this.lists.get(i)).getInt("MemberCardID");
        paramString = ((DPObject)this.lists.get(i)).getString("MemberCardGroupID");
        if (((paramString == null) || (TextUtils.isEmpty(paramString))) && (k == j))
        {
          paramString = ((DPObject)this.lists.get(i)).edit().putBoolean("Joined", paramBoolean).generate();
          this.lists.set(i, paramString);
          return;
        }
      }
      catch (java.lang.NumberFormatException paramString)
      {
        return;
      }
      i += 1;
    }
  }

  public void refreshListbygroupid(String paramString, boolean paramBoolean)
  {
    int i = 0;
    while (true)
    {
      if (i < this.lists.size())
      {
        String str = ((DPObject)this.lists.get(i)).getString("MemberCardGroupID");
        if ((str != null) && (!TextUtils.isEmpty(str)) && (str.equals(paramString)))
        {
          paramString = ((DPObject)this.lists.get(i)).edit().putBoolean("Joined", paramBoolean).generate();
          this.lists.set(i, paramString);
        }
      }
      else
      {
        return;
      }
      i += 1;
    }
  }

  public void reset()
  {
    this.lists.clear();
    this.trigger = -1;
    this.nextStartIndex = 0;
    this.isFirstPage = true;
    this.adapter.reset();
  }

  public void setKeyword(String paramString)
  {
    this.keyword = paramString;
  }

  class CardAdapter extends BasicCardAdapter<DPObject>
  {
    public CardAdapter(List<DPObject> paramBoolean, boolean arg3)
    {
      super(localList, bool);
    }

    public int getItemResource()
    {
      return R.layout.available_card_list_item;
    }

    public View getItemView(int paramInt, View paramView, BasicCardAdapter<DPObject>.ViewHolder paramBasicCardAdapter)
    {
      paramView = (AvailableCardListItem)paramView;
      paramBasicCardAdapter = (DPObject)getItem(paramInt);
      paramView.setAvailableCard(paramBasicCardAdapter, 0);
      paramInt = paramBasicCardAdapter.getInt("MemberCardID");
      String str1 = paramBasicCardAdapter.getString("MemberCardGroupID");
      String str2 = paramBasicCardAdapter.getString("Title");
      int i = AvailableCardListFragment.this.getProductID(paramBasicCardAdapter);
      int j = paramBasicCardAdapter.getInt("CardLevel");
      boolean bool1 = MemberCard.isScoreCard(paramBasicCardAdapter);
      boolean bool2 = MemberCard.isThirdPartyCard(paramBasicCardAdapter);
      String str3 = paramBasicCardAdapter.getString("NavigateUrl");
      if (!paramBasicCardAdapter.getBoolean("Joined"))
      {
        paramView.status.setOnClickListener(new View.OnClickListener(str1, bool1, paramInt, bool2, i, j, str2)
        {
          public void onClick(View paramView)
          {
            if ((this.val$groupid != null) && (!TextUtils.isEmpty(this.val$groupid)))
              AvailableCardListFragment.this.gotoChainCard(this.val$groupid, 1, this.val$isScoreCard);
            while (true)
            {
              AvailableCardListFragment.this.statisticsEvent("availablecard5", "availablecard5_directlyadd", AvailableCardListFragment.this.membercardid + "|" + this.val$title + "|" + AvailableCardListFragment.this.source, 0);
              return;
              AvailableCardListFragment.access$002(AvailableCardListFragment.this, this.val$cardid);
              if (this.val$isThirdPartyCard)
              {
                AvailableCardListFragment.this.addThirdPartyCard(AvailableCardListFragment.this.membercardid, AvailableCardListFragment.this.source);
                continue;
              }
              if (this.val$isScoreCard)
              {
                AvailableCardListFragment.this.addScoreCard(AvailableCardListFragment.this.membercardid, AvailableCardListFragment.this.source);
                continue;
              }
              AvailableCardListFragment.this.mJoinMCHandler.addCardQuick(String.valueOf(this.val$cardid), AvailableCardListFragment.this.source, this.val$productid, this.val$cardlevel);
            }
          }
        });
        return paramView;
      }
      paramView.status.setOnClickListener(new View.OnClickListener(bool2, str3, str2, paramInt)
      {
        public void onClick(View paramView)
        {
          if (this.val$isThirdPartyCard)
          {
            AvailableCardListFragment.this.openThirdPartyCardInfoView(this.val$thirdPartyCardUri, this.val$title);
            return;
          }
          AvailableCardListFragment.access$002(AvailableCardListFragment.this, this.val$cardid);
          AvailableCardListFragment.this.gotoMyCard();
        }
      });
      return paramView;
    }

    public void loadNextPage()
    {
      AvailableCardListFragment localAvailableCardListFragment;
      if (AvailableCardListFragment.this.searchCardListRequest == null)
      {
        localAvailableCardListFragment = AvailableCardListFragment.this;
        if (AvailableCardListFragment.this.getAccount() != null)
          break label77;
      }
      label77: for (String str = ""; ; str = AvailableCardListFragment.this.getAccount().token())
      {
        localAvailableCardListFragment.searchCardListTask(str, AvailableCardListFragment.this.nextStartIndex);
        notifyDataSetChanged();
        if (AvailableCardListFragment.this.nextStartIndex > 0)
          AvailableCardListFragment.this.statisticsEvent("availablecard5", "availablecard5_dropdown", Integer.toString(AvailableCardListFragment.this.nextStartIndex), 0);
        return;
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
      paramContext = paramIntent.getAction();
      if (MCUtils.isMemberCardRelativeBroadcast(paramIntent))
      {
        if ((!paramContext.equals("com.dianping.action.UPDATE_LIST_DATA")) && (!paramContext.equals("Card:JoinSuccess")))
          break label67;
        AvailableCardListFragment.this.refresh();
        if (paramContext.equals("Card:JoinSuccess"))
          MCUtils.sendUpdateMemberCardListBroadcast(AvailableCardListFragment.this.getActivity(), String.valueOf(AvailableCardListFragment.this.membercardid));
      }
      return;
      label67: String str = paramIntent.getStringExtra("membercardid");
      paramIntent = paramIntent.getStringExtra("membercardgroupid");
      if (paramContext.equals("com.dianping.action.JOIN_MEMBER_CARD"))
      {
        if (str != null)
          AvailableCardListFragment.this.refreshList(str, true);
        if (paramIntent != null)
          AvailableCardListFragment.this.refreshListbygroupid(paramIntent, true);
      }
      while (true)
      {
        AvailableCardListFragment.this.adapter.replaceAll(AvailableCardListFragment.this.lists);
        AvailableCardListFragment.this.setupListAdapter(null);
        return;
        if (!paramContext.equals("com.dianping.action.QUIT_MEMBER_CARD"))
          continue;
        if (str != null)
          AvailableCardListFragment.this.refreshList(str, false);
        if (paramIntent == null)
          continue;
        AvailableCardListFragment.this.refreshListbygroupid(paramIntent, false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.AvailableCardListFragment
 * JD-Core Version:    0.6.0
 */