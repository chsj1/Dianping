package com.dianping.main.find.pictureplaza;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedDetailFragment extends PlazaAdapterAgentFragment
{
  private View mFragmentView;
  private PullToRefreshListView mListView;

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
        if (FeedDetailFragment.this.getIntParam("isfromnotificationcenter", 0) == 1)
        {
          localHashMap.put("feed/Content", FeedContentAgent.class);
          FeedDetailFragment.this.mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
        localHashMap.put("feed/LikesUser", FeedLikesUserAgent.class);
        localHashMap.put("feed/CommentList", FeedCommentListAgent.class);
        localHashMap.put("feed/EmptyView", FeedEmptyViewAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public View getFragmentView()
  {
    return this.mFragmentView;
  }

  public ListView getListView()
  {
    return this.mListView;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.find_plaza_feed_detail, paramViewGroup, false);
    this.mListView = ((PullToRefreshListView)this.mFragmentView.findViewById(R.id.detail_list_view));
    this.mListView.setBackgroundColor(getResources().getColor(R.color.white));
    this.mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        FeedDetailFragment.this.startRefresh(paramPullToRefreshListView);
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
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedDetailFragment
 * JD-Core Version:    0.6.0
 */