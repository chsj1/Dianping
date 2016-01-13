package com.dianping.main.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.base.widget.RegisterDialog;
import com.dianping.main.guide.MainActivity;
import com.dianping.main.user.agent.UserExtraInfoAgent;
import com.dianping.main.user.agent.UserInfoHeaderAgent;
import com.dianping.main.user.agent.UserReceiverAgent;
import com.dianping.main.user.agent.app.UserAgent;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserFragment extends AgentFragment
{
  public static final String HOST = "me";
  private static final String TAG = UserFragment.class.getSimpleName();
  public static final String USER_CELL_EXTRA_INFO = "me/userextrainfo";
  public static final String USER_CELL_INFO_HEADER = "me/userinfoheader";
  public static final String USER_CELL_RECEIVER = "me/userreceiver";
  public static final int USER_CELL_WEEK_TIME = 604800000;
  private FrameLayout mContentView;
  private boolean mIsRefreshing = false;
  private FrameLayout mLayNews;
  private PullToRefreshScrollView mPullToRefreshScrollView;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (("com.dianping.action.NEW_MESSAGE".equals(paramIntent.getAction())) && ((UserFragment.this.getActivity() instanceof MainActivity)))
        UserFragment.this.updateMessageMailCount(((MainActivity)UserFragment.this.getActivity()).getMailCount());
    }
  };
  private RegisterDialog mRegisterDialog;
  private int mRequestCount = 0;
  private LinearLayout mRootView;
  private MeasuredTextView mTvMsgCount;
  final PullToRefreshBase.OnRefreshListener refreshListener = new PullToRefreshBase.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
    {
      UserFragment.this.onRefresh();
    }
  };
  private SharedPreferences sharedPreferences;

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    this.mRootView.addView(paramCell.view);
  }

  public void checkShouldSuggestRegister()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences();
    int i = localSharedPreferences.getInt("user_mypage_register_lastVersion", 0);
    int j = Environment.versionCode();
    if (i < j)
    {
      localSharedPreferences.edit().putInt("user_mypage_register_lastVersion", j).putLong("user_mypage_register_firstOpentime", System.currentTimeMillis()).commit();
      if (accountService().token() != null)
        localSharedPreferences.edit().putBoolean("user_mypage_register_hasLogin", true).commit();
    }
    long l = localSharedPreferences.getLong("user_mypage_register_firstOpentime", 0L);
    if (localSharedPreferences.getLong("user_mypage_register_lastOpentime", 0L) > 0L)
    {
      l = System.currentTimeMillis() - localSharedPreferences.getLong("user_mypage_register_lastOpentime", System.currentTimeMillis());
      System.currentTimeMillis();
      if ((localSharedPreferences.getBoolean("user_mypage_register_hasLogin", false)) || (accountService().token() != null))
        break label225;
      if (l >= 604800000L)
      {
        GAHelper.instance().contextStatisticsEvent(getActivity(), "signup_view", null, "view");
        showRegisterSuggestDialog();
        localSharedPreferences.edit().putLong("user_mypage_register_lastOpentime", System.currentTimeMillis()).putBoolean("user_mypage_register_firstLogin", false).commit();
      }
    }
    label225: 
    do
    {
      return;
      l = System.currentTimeMillis() - l;
      break;
    }
    while ((localSharedPreferences.getBoolean("user_mypage_register_hasLogin", false)) || (accountService().token() == null));
    localSharedPreferences.edit().putBoolean("user_mypage_register_hasLogin", true).commit();
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("me/userreceiver", UserReceiverAgent.class);
        localHashMap.put("me/userinfoheader", UserInfoHeaderAgent.class);
        localHashMap.put("me/userextrainfo", UserExtraInfoAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  void gotoOnlineService()
  {
    String str;
    if (accountService().token() != null)
      str = "http://kf.dianping.com/csCenter?cityId=" + cityId() + "&token=" + accountService().token();
    try
    {
      while (true)
      {
        str = URLEncoder.encode(str, "utf-8");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + str)));
        return;
        str = "http://kf.dianping.com/csCenter?cityId=" + cityId();
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
  }

  public void onAgentRequestFinish()
  {
    if (this.mIsRefreshing)
    {
      this.mRequestCount -= 1;
      if (this.mRequestCount < 1)
      {
        this.mIsRefreshing = false;
        this.mPullToRefreshScrollView.onRefreshComplete();
      }
    }
  }

  public void onAgentRequestStart()
  {
    if (this.mIsRefreshing)
      this.mRequestCount += 1;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.sharedPreferences == null)
      this.sharedPreferences = preferences(getActivity());
    setHost("me");
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.NEW_MESSAGE");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mContentView = ((FrameLayout)paramLayoutInflater.inflate(R.layout.my_zone, paramViewGroup, false));
    this.mPullToRefreshScrollView = ((PullToRefreshScrollView)this.mContentView.findViewById(R.id.scroll_view));
    this.mPullToRefreshScrollView.setOnRefreshListener(this.refreshListener);
    this.mRootView = ((LinearLayout)this.mContentView.findViewById(R.id.root));
    this.mContentView.findViewById(R.id.help).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UserFragment.this.gotoOnlineService();
      }
    });
    this.mLayNews = ((FrameLayout)this.mContentView.findViewById(R.id.news));
    if (this.mLayNews != null)
      this.mLayNews.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (UserFragment.this.getActivity() == null)
            return;
          paramView = "dianping://notification";
          SharedPreferences localSharedPreferences = UserFragment.this.getActivity().getSharedPreferences(UserFragment.this.getActivity().getPackageName(), 32768);
          if (localSharedPreferences != null)
          {
            if (localSharedPreferences.getInt("notification_count", 0) <= 0)
              break label98;
            paramView = "dianping://notification" + "?tab=0";
          }
          while (true)
          {
            UserFragment.this.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
            return;
            label98: if (localSharedPreferences.getInt("alert_count", 0) > 0)
            {
              paramView = "dianping://notification" + "?tab=1";
              continue;
            }
            paramView = "dianping://notification" + "?tab=2";
          }
        }
      });
    this.mTvMsgCount = ((MeasuredTextView)this.mContentView.findViewById(R.id.tv_msg_count));
    if ((getActivity() instanceof MainActivity))
      updateMessageMailCount(((MainActivity)getActivity()).getMailCount());
    checkShouldSuggestRegister();
    return this.mContentView;
  }

  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public final void onRefresh()
  {
    if (getActivity() == null);
    while (true)
    {
      return;
      this.mIsRefreshing = true;
      Iterator localIterator = this.agentList.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = (CellAgent)this.agents.get(localObject);
        if (!(localObject instanceof UserAgent))
          continue;
        ((UserAgent)localObject).onRefresh();
      }
    }
  }

  protected void resetAgentContainerView()
  {
    this.mRootView.removeAllViews();
  }

  public void showRegisterSuggestDialog()
  {
    this.mRegisterDialog = new RegisterDialog(getActivity());
    this.mRegisterDialog.show();
    this.mRegisterDialog.setLogin(new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        UserFragment.access$002(UserFragment.this, null);
        paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://login"));
        UserFragment.this.getActivity().startActivity(paramDialogInterface);
        GAHelper.instance().contextStatisticsEvent(UserFragment.this.getActivity(), "signup_login", null, "tap");
      }
    }).setPositiveButton("快速注册", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        UserFragment.access$002(UserFragment.this, null);
        GAHelper.instance().contextStatisticsEvent(UserFragment.this.getActivity(), "signup_mobile", null, "tap");
        paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://signup?isFromNative=true"));
        UserFragment.this.getActivity().startActivity(paramDialogInterface);
      }
    }).setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        GAHelper.instance().contextStatisticsEvent(UserFragment.this.getActivity(), "signup_skip", null, "tap");
        UserFragment.access$002(UserFragment.this, null);
      }
    });
  }

  public void updateDraftRedMark()
  {
    dispatchAgentChanged("home/userinfoheader", null);
  }

  public void updateMessageMailCount(int paramInt)
  {
    if ((this.mTvMsgCount == null) || (getActivity() == null))
      return;
    if (paramInt <= 0)
    {
      this.mTvMsgCount.setVisibility(8);
      this.mLayNews.setPadding(0, 0, ViewUtils.dip2px(getActivity(), 6.0F), 0);
      return;
    }
    this.mTvMsgCount.setVisibility(0);
    MeasuredTextView localMeasuredTextView = this.mTvMsgCount;
    if (paramInt > 99);
    for (String str = "99"; ; str = paramInt + "")
    {
      localMeasuredTextView.setText(str);
      if (paramInt >= 10)
        break;
      this.mTvMsgCount.setFlag(true);
      this.mLayNews.setPadding(0, 0, ViewUtils.dip2px(getActivity(), 12.0F), 0);
      this.mTvMsgCount.setBackgroundResource(R.drawable.home_navibar_tips_red_b);
      return;
    }
    this.mLayNews.setPadding(0, 0, ViewUtils.dip2px(getActivity(), 15.0F), 0);
    this.mTvMsgCount.setBackgroundResource(R.drawable.home_navibar_tips_reddigit);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserFragment
 * JD-Core Version:    0.6.0
 */