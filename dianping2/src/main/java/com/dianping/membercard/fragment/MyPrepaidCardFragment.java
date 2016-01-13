package com.dianping.membercard.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.membercard.MemberCardListAdapter;
import com.dianping.membercard.business.PrepaidCardListApiHelper;
import com.dianping.membercard.business.PrepaidCardListApiHelper.PrepaidCardListApiResult;
import com.dianping.membercard.utils.CardDetailAbortStatus;
import com.dianping.membercard.view.MemberCardListItem;
import com.dianping.membercard.view.PrepaidCardListView;
import com.dianping.membercard.view.PrepaidCardListView.OnItemClickWithAnimListener;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPrepaidCardFragment extends NovaFragment
  implements PrepaidCardListView.OnItemClickWithAnimListener, View.OnClickListener, PrepaidCardDetailRequestTask.PrepaidCardDetailRequestHandler
{
  private static final String MC_RS = "com.dianping.action.PREPAID_CARD_BUY_SUCCESS";
  private static final int REQUEST_CODE_ADD_PREPAIDCARD = 0;
  private static final String TITLE = "我的储值卡";
  private static final String UPDATE_UI = "com.dianping.action.UPDATE_USER_INFO";
  CardDetailAbortStatus abortStatus = new CardDetailAbortStatus();
  private Adapter adapter;
  CardDetailInflateMode cardDetailInflateMode = CardDetailInflateMode.SIMPLE_MEMBER_CARD;
  private PrepaidCardDetailRequestTask cardDetailRequestTask = null;
  private DPObject cardObject;
  private String host;
  private boolean isPreparedToRefresh = false;
  private boolean isPullToRefreshing;
  private CardFragment loadedFragment;
  private Object lockForLoadedFragment = new Object();
  private boolean mFirst = false;
  private boolean mIsCardBox = true;
  private PrepaidCardListView mListView;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        paramContext = paramIntent.getStringExtra("username");
        if (MyPrepaidCardFragment.this.cardObject != null)
        {
          MyPrepaidCardFragment.this.adapter.updateUserInfo(paramContext);
          MyPrepaidCardFragment.this.adapter.notifyDataSetChanged();
          MyPrepaidCardFragment.access$202(MyPrepaidCardFragment.this, MyPrepaidCardFragment.this.cardObject.edit().putString("UserName", paramContext).generate());
        }
        if (MyPrepaidCardFragment.this.mListView != null)
          MyPrepaidCardFragment.this.mListView.updateCurrrentUserName(paramContext);
        if (MyPrepaidCardFragment.this.task != null)
          MyPrepaidCardFragment.this.task.updateAsynPostUserName(paramContext);
      }
      do
        return;
      while (!paramIntent.getAction().equals("com.dianping.action.PREPAID_CARD_BUY_SUCCESS"));
      MyPrepaidCardFragment.this.adapter.reset();
    }
  };
  PullToRefreshListView.OnRefreshListener onRefreshListener = new PullToRefreshListView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
    {
      MyPrepaidCardFragment.access$002(MyPrepaidCardFragment.this, true);
      MyPrepaidCardFragment.this.refreshCardList();
    }
  };
  private MyCardListTask task;

  private void _inflateFullPrepaidCard(DPObject paramDPObject)
  {
    this.loadedFragment = new PrepaidCardFragment();
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
      localFragmentTransaction.add(R.id.prepaid_card_fragment_layout, this.loadedFragment, "loadedFragment");
      localFragmentTransaction.commitAllowingStateLoss();
    }
  }

  private void _inflateSimplePrepaidCard(DPObject paramDPObject)
  {
    PrepaidCardRetryFragment localPrepaidCardRetryFragment = new PrepaidCardRetryFragment();
    if (localPrepaidCardRetryFragment != null)
    {
      this.loadedFragment = localPrepaidCardRetryFragment;
      FragmentManager localFragmentManager = getActivity().getSupportFragmentManager();
      FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("card", paramDPObject);
      this.loadedFragment.setArguments(localBundle);
      paramDPObject = localFragmentManager.findFragmentByTag("loadedFragment");
      if (paramDPObject != null)
        localFragmentTransaction.remove(paramDPObject);
      localFragmentTransaction.add(R.id.prepaid_card_fragment_layout, localPrepaidCardRetryFragment, "loadedFragment");
      localFragmentTransaction.commitAllowingStateLoss();
    }
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

  private void inflateFragment(DPObject paramDPObject, CardDetailInflateMode paramCardDetailInflateMode)
  {
    synchronized (this.lockForLoadedFragment)
    {
      this.cardDetailInflateMode = paramCardDetailInflateMode;
      updateSelectedCardStatus(paramDPObject);
      onTitleBarChange();
      if (paramCardDetailInflateMode == CardDetailInflateMode.FULL_MEMBER_CARD)
        _inflateFullPrepaidCard(paramDPObject);
      do
        return;
      while (paramCardDetailInflateMode != CardDetailInflateMode.SIMPLE_MEMBER_CARD);
      _inflateSimplePrepaidCard(paramDPObject);
    }
  }

  private void refreshCardList()
  {
    if (this.task != null)
    {
      this.task.cancel(true);
      this.task = null;
    }
    this.adapter.reset();
    this.adapter.notifyDataSetChanged();
  }

  private void showListDialog(ArrayList<String> paramArrayList, String paramString, DialogInterface.OnClickListener paramOnClickListener)
  {
    paramArrayList = new ArrayAdapter(getActivity(), 17367043, paramArrayList);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setAdapter(paramArrayList, paramOnClickListener);
    paramArrayList = localBuilder.create();
    if (paramString != null)
      paramArrayList.setTitle(paramString);
    paramArrayList.show();
    paramArrayList.setCanceledOnTouchOutside(true);
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

  private void updateSelectedCardStatus(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
  }

  public void getMyCardListTask(int paramInt)
  {
    if (this.task != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder("http://app.t.dianping.com/");
    localStringBuilder.append("myprepaidcardlistgn.bin?");
    localStringBuilder.append("uuid=");
    localStringBuilder.append(Environment.uuid());
    if (accountService().token() != null)
      localStringBuilder.append("&token=").append(accountService().token());
    Object localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(((Location)localObject).latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(((Location)localObject).longitude()));
    }
    localStringBuilder.append("&startindex=").append(paramInt);
    localStringBuilder.append("&from=").append("cardbag");
    localStringBuilder.append("&cityid=").append(cityId());
    localObject = getActivity().getResources().getDisplayMetrics();
    localStringBuilder.append("&pixel=").append(((DisplayMetrics)localObject).widthPixels);
    this.task = new MyCardListTask();
    this.task.execute(new String[] { localStringBuilder.toString() });
  }

  public void gotoHelp()
  {
    Object localObject = (MApiDebugAgent)getService("mapi_debug");
    if (!TextUtils.isEmpty(((MApiDebugAgent)localObject).membercardDebugDomain()))
      localObject = ((MApiDebugAgent)localObject).membercardDebugDomain();
    while (true)
    {
      localObject = (String)localObject + "help.html";
      try
      {
        localObject = URLEncoder.encode((String)localObject, "utf-8");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + (String)localObject)));
        ((NovaActivity)getActivity()).statisticsEvent("paidcardinfo5", "paidcardinfo5_help", "", 0);
        return;
        localObject = "http://mc.api.dianping.com/";
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        localUnsupportedEncodingException.printStackTrace();
      }
    }
  }

  public boolean ismIsCardBox()
  {
    return this.mIsCardBox;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    ((TextView)getView().findViewById(16908310)).setText("我的储值卡");
    getView().findViewById(R.id.left_title_button).setOnClickListener(this);
    parseIntent();
    onTitleBarChange();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 0)
      this.adapter.reset();
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
    onTitleBarChange();
    this.mListView.openList(true);
    if (this.task != null)
      this.task.doCardDetailAsynPost();
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt1, int paramInt2)
  {
    if (this.abortStatus.isCardDetailAborted(paramInt1))
      return;
    while (true)
    {
      synchronized (this.lockForLoadedFragment)
      {
        if ((this.loadedFragment == null) || (!(this.loadedFragment instanceof PrepaidCardRetryFragment)))
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
          ((PrepaidCardRetryFragment)this.loadedFragment).showFailedView(str1, new LoadingErrorView.LoadRetry(paramInt1, paramInt2)
          {
            public void loadRetry(View paramView)
            {
              if (MyPrepaidCardFragment.this.loadedFragment != null)
              {
                ((PrepaidCardRetryFragment)MyPrepaidCardFragment.this.loadedFragment).showLoadingView();
                MyPrepaidCardFragment.this.cardDetailRequestTask.doRequest(this.val$prepaidCardId, this.val$accountId);
              }
            }
          });
          return;
        }
      }
      String str1 = paramMApiResponse.message().toString();
    }
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, PrepaidCardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    if (this.abortStatus.isCardDetailAborted(paramDPObject.getInt("PrepaidCardID")))
      return;
    inflateFragment(paramDPObject, CardDetailInflateMode.FULL_MEMBER_CARD);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.left_title_button)
      onLeftButtonClick();
    do
      return;
    while (paramView.getId() != R.id.right_title_button);
    onRightButtonClick();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
    registerReceiver(this.mReceiver, paramBundle);
    paramBundle = new IntentFilter("com.dianping.action.PREPAID_CARD_BUY_SUCCESS");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.prepaid_card_tab_layout, paramViewGroup, false);
    this.mListView = ((PrepaidCardListView)paramLayoutInflater.findViewById(R.id.card_listview));
    this.adapter = new Adapter(getActivity());
    this.mListView.setAdapter(this.adapter);
    this.mListView.setOnItemClickWithAnimListener(this);
    this.mListView.setOnRefreshListener(this.onRefreshListener);
    paramViewGroup = PrepaidCardListApiHelper.getCacheCardList(getActivity());
    if (paramViewGroup != null)
      this.adapter.appendCards(paramViewGroup.cardList);
    paramLayoutInflater.findViewById(R.id.prepaid_card_fragment_layout).setVisibility(8);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.task != null)
    {
      this.task.cancel(true);
      this.task = null;
    }
    if (this.cardDetailRequestTask != null)
    {
      this.cardDetailRequestTask.abortRequest();
      this.cardDetailRequestTask = null;
    }
    super.onDestroy();
  }

  public void onItemClickAfterAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    getView().findViewById(R.id.prepaid_card_fragment_layout).setVisibility(0);
    if (this.adapter.getData().size() <= 0)
      return;
    paramAdapterView = (DPObject)paramAdapterView.getAdapter().getItem(paramInt);
    ((NovaActivity)getActivity()).statisticsEvent("mycard5", "mycard5_paidcard_item", "" + paramAdapterView.getInt("PrepaidCardID"), 0);
    this.cardDetailRequestTask.doRequest(paramAdapterView.getInt("PrepaidCardID"), paramAdapterView.getInt("AccountID"));
  }

  public void onItemClickBeforAnim(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = (DPObject)paramAdapterView.getAdapter().getItem(paramInt);
    int i = -1;
    if (paramView != null)
      i = paramView.getInt("PrepaidCardID");
    this.abortStatus.initAbortStatus(i);
    setmIsCardBox(false);
    onTitleBarChange();
    getView().findViewById(R.id.prepaid_card_fragment_layout).setVisibility(4);
    inflateFragment((DPObject)paramAdapterView.getAdapter().getItem(paramInt), CardDetailInflateMode.SIMPLE_MEMBER_CARD);
  }

  public void onLeftButtonClick()
  {
    if (ismIsCardBox())
    {
      getActivity().finish();
      return;
    }
    onCardBoxButtonClick();
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

  public void onRightButtonClick()
  {
    if (!ismIsCardBox())
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("意见反馈");
      localArrayList.add("使用帮助");
      localArrayList.add("取消");
      showListDialog(localArrayList, "更多", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          switch (paramInt)
          {
          default:
            return;
          case 0:
            MyPrepaidCardFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://feedback?flag=7")));
            ((NovaActivity)MyPrepaidCardFragment.this.getActivity()).statisticsEvent("paidcardinfo5", "paidcardinfo5_feedback", "", 0);
            return;
          case 1:
          }
          MyPrepaidCardFragment.this.gotoHelp();
        }
      });
    }
  }

  public void onStart()
  {
    super.onStart();
    this.cardDetailRequestTask = new PrepaidCardDetailRequestTask(this);
  }

  public void onTitleBarChange()
  {
    View localView = getView().findViewById(R.id.right_title_button);
    if (localView != null)
      if (!ismIsCardBox())
        break label46;
    label46: for (int i = 8; ; i = 0)
    {
      localView.setVisibility(i);
      localView.setOnClickListener(this);
      ((ImageView)localView).setImageResource(R.drawable.title_icon_more);
      return;
    }
  }

  public void parseIntent()
  {
    boolean bool = true;
    this.host = getActivity().getIntent().getData().getHost();
    setmIsCardBox(true);
    if (!ismIsCardBox());
    while (true)
    {
      this.mFirst = bool;
      return;
      bool = false;
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

  public void trigerRefreshing()
  {
    this.mListView.postDelayed(new Runnable()
    {
      public void run()
      {
        MyPrepaidCardFragment.this.mListView.trigerRefreshing();
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
      if (MyPrepaidCardFragment.this.isPullToRefreshing)
        return getData().size();
      return super.getCount();
    }

    protected View getEmptyMsgView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      paramString1 = null;
      if (paramView == null)
      {
        paramString2 = paramString1;
        if (paramString1 == null)
        {
          paramString2 = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.my_prepaidcard_empty_view, paramViewGroup, false);
          paramString2.setTag(EMPTY);
        }
        paramString1 = (TextView)paramString2.findViewById(R.id.prepaid_card_not_open);
        paramView = paramString2.findViewById(R.id.list_empty);
        if (!MyPrepaidCardFragment.this.city().isPrepaidCardCity())
          break label108;
        paramView.setVisibility(0);
        paramString1.setVisibility(8);
      }
      while (true)
      {
        paramString2.setMinimumHeight(paramViewGroup.getHeight());
        return paramString2;
        if (paramView.getTag() != EMPTY)
          break;
        paramString1 = paramView;
        break;
        label108: paramView.setVisibility(8);
        paramString1.setVisibility(0);
      }
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      LayoutInflater localLayoutInflater = LayoutInflater.from(paramViewGroup.getContext());
      if ((localObject instanceof DPObject))
      {
        View localView = paramView;
        if (!(paramView instanceof MemberCardListItem))
          localView = localLayoutInflater.inflate(R.layout.card_list_item, paramViewGroup, false);
        ((MemberCardListItem)localView).setData((DPObject)localObject, true);
        return localView;
      }
      if (localObject == LOADING)
      {
        loadNewPage();
        return (MemberCardListItem)getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == EMPTY)
        return (FrameLayout)getEmptyMsgView(this.mEmptyMsg, "", paramViewGroup, paramView);
      return (MemberCardListItem)getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MyPrepaidCardFragment.Adapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    protected boolean loadNewPage()
    {
      if (ismIsEnd());
      do
        return false;
      while (MyPrepaidCardFragment.this.task != null);
      MyPrepaidCardFragment.this.getMyCardListTask(getmNextStartIndex());
      return true;
    }

    public void replaceCard(DPObject paramDPObject)
    {
      int i = 0;
      while (true)
      {
        if (i < getData().size())
        {
          String str1 = ((DPObject)getData().get(i)).getString("MemberCardID");
          String str2 = paramDPObject.getString("MemberCardID");
          if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)) && (str1.equals(str2)))
            getData().set(i, paramDPObject);
        }
        else
        {
          return;
        }
        i += 1;
      }
    }

    protected void reset()
    {
      if (MyPrepaidCardFragment.this.isPullToRefreshing)
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
      while (i < getData().size())
      {
        DPObject localDPObject = ((DPObject)getData().get(i)).edit().putString("UserName", paramString).generate();
        getData().set(i, localDPObject);
        i += 1;
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

  class MyCardListTask extends AsyncTask<String, Void, PrepaidCardListApiHelper.PrepaidCardListApiResult>
  {
    private boolean hasAsynResult = false;
    PrepaidCardListApiHelper helper = new PrepaidCardListApiHelper(MyPrepaidCardFragment.this.getActivity());
    private PrepaidCardListApiHelper.PrepaidCardListApiResult resultHolder;

    public MyCardListTask()
    {
    }

    private void doWithPost(PrepaidCardListApiHelper.PrepaidCardListApiResult paramPrepaidCardListApiResult)
    {
      if (MyPrepaidCardFragment.this.isPullToRefreshing)
      {
        MyPrepaidCardFragment.access$002(MyPrepaidCardFragment.this, false);
        MyPrepaidCardFragment.this.mListView.onRefreshComplete();
        MyPrepaidCardFragment.this.adapter.reset();
      }
      if (paramPrepaidCardListApiResult == null)
        if (this.helper.errorMsg() == null)
        {
          paramPrepaidCardListApiResult = "错误，服务暂时不可用，请稍候再试";
          MyPrepaidCardFragment.this.adapter.setErrorMsg(paramPrepaidCardListApiResult);
          MyPrepaidCardFragment.this.adapter.notifyDataSetChanged();
        }
      while (true)
      {
        MyPrepaidCardFragment.access$502(MyPrepaidCardFragment.this, null);
        return;
        paramPrepaidCardListApiResult = this.helper.errorMsg();
        break;
        MyPrepaidCardFragment.this.adapter.appendCards(paramPrepaidCardListApiResult.cardList);
        MyPrepaidCardFragment.this.unloadFragment();
        MyPrepaidCardFragment.this.showCardBox();
        MyPrepaidCardFragment.this.setmIsCardBox(true);
        MyPrepaidCardFragment.this.onTitleBarChange();
      }
    }

    public void doCardDetailAsynPost()
    {
      monitorenter;
      try
      {
        if (this.hasAsynResult)
        {
          this.hasAsynResult = false;
          doWithPost(this.resultHolder);
        }
        monitorexit;
        return;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }

    public PrepaidCardListApiHelper.PrepaidCardListApiResult doInBackground(String[] paramArrayOfString)
    {
      paramArrayOfString = paramArrayOfString[0];
      return this.helper.getMyCards(paramArrayOfString);
    }

    public void onCancelled()
    {
      monitorenter;
      try
      {
        if (this.helper != null)
          this.helper.abort();
        monitorexit;
        return;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }

    public void onPostExecute(PrepaidCardListApiHelper.PrepaidCardListApiResult paramPrepaidCardListApiResult)
    {
      monitorenter;
      while (true)
      {
        try
        {
          MyCardListTask localMyCardListTask = MyPrepaidCardFragment.this.task;
          if (localMyCardListTask != this)
            return;
          if (MyPrepaidCardFragment.this.abortStatus.isOpenningListEnable())
          {
            this.hasAsynResult = false;
            this.resultHolder = null;
            doWithPost(paramPrepaidCardListApiResult);
            continue;
          }
        }
        finally
        {
          monitorexit;
        }
        this.hasAsynResult = true;
        this.resultHolder = paramPrepaidCardListApiResult;
      }
    }

    public void updateAsynPostUserName(String paramString)
    {
      if ((!this.hasAsynResult) || (this.resultHolder == null) || (this.resultHolder.cardList == null));
      DPObject[] arrayOfDPObject;
      do
      {
        return;
        arrayOfDPObject = this.resultHolder.cardList.getArray("List");
      }
      while ((arrayOfDPObject == null) || (arrayOfDPObject.length <= 0));
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        arrayOfDPObject[i] = arrayOfDPObject[i].edit().putString("UserName", paramString).generate();
        i += 1;
      }
      this.resultHolder.cardList = this.resultHolder.cardList.edit().putArray("List", arrayOfDPObject).generate();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.MyPrepaidCardFragment
 * JD-Core Version:    0.6.0
 */