package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExploreHomeFragment extends LazyLoadAdapterAgentFragment
  implements PullToRefreshListView.OnRefreshListener
{
  private PullToRefreshListView listView;

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
        localHashMap.put("explore/hotfeed", ExploreHotFeedAgent.class);
        localHashMap.put("explore/category", ExploreCategoryAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.find_plaza_explore_home, paramViewGroup, false);
    this.listView = ((PullToRefreshListView)paramLayoutInflater.findViewById(R.id.plaza_explore_home_list));
    this.listView.setOnRefreshListener(this);
    setAgentContainerListView(this.listView);
    return paramLayoutInflater;
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    startRefresh(paramPullToRefreshListView);
  }

  void onRefreshComplete()
  {
    this.listView.onRefreshComplete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreHomeFragment
 * JD-Core Version:    0.6.0
 */