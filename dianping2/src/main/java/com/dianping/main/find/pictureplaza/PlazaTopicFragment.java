package com.dianping.main.find.pictureplaza;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlazaTopicFragment extends PlazaAdapterAgentFragment
{
  private CustomImageButton mBackButton;
  private View mFragmentView;
  private CustomImageButton mJoinTopicView;
  private PullToRefreshListView mListView;
  private ViewGroup mTitleBarLayout;
  public String mTopicName = "";

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
        localHashMap.put("topic/TitleBar", TopicTitleBarAgent.class);
        localHashMap.put("topic/StarUser", TopicStarUserAgent.class);
        localHashMap.put("topic/FeedList", TopicFeedListAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public ListView getListView()
  {
    return this.mListView;
  }

  public ViewGroup getTitleBarLayout()
  {
    return this.mTitleBarLayout;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.find_plaza_topic, paramViewGroup, false);
    this.mTitleBarLayout = ((ViewGroup)this.mFragmentView.findViewById(R.id.topic_title_bar_layout));
    this.mBackButton = ((CustomImageButton)this.mFragmentView.findViewById(R.id.back));
    this.mBackButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PlazaTopicFragment.this.getActivity().finish();
      }
    });
    this.mJoinTopicView = ((CustomImageButton)this.mFragmentView.findViewById(R.id.publisher));
    this.mJoinTopicView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = PlazaTopicFragment.this.accountService();
        if (paramView.token() == null)
        {
          paramView.login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              PlazaTopicFragment.2.this.onClick(null);
            }
          });
          return;
        }
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://addcommunityphoto").buildUpon().appendQueryParameter("maxnum", String.valueOf(9)).appendQueryParameter("interesttag", PlazaTopicFragment.this.mTopicName).appendQueryParameter("topicid", String.valueOf(PlazaTopicFragment.this.getIntParam("topicid", 1))).build());
        PlazaTopicFragment.this.startActivity(paramView);
      }
    });
    this.mJoinTopicView.getGAUserInfo().biz_id = String.valueOf(getIntParam("topicid", 1));
    this.mListView = ((PullToRefreshListView)this.mFragmentView.findViewById(R.id.topic_list_view));
    this.mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        PlazaTopicFragment.this.startRefresh(paramPullToRefreshListView);
      }
    });
    setAgentContainerListView(this.mListView);
    return this.mFragmentView;
  }

  void onRefreshComplete()
  {
    this.mListView.onRefreshComplete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaTopicFragment
 * JD-Core Version:    0.6.0
 */