package com.dianping.main.find.pictureplaza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExploreTopicListFragment extends AdapterAgentFragment
{
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
        localHashMap.put("explore/plazaexploretopiclist", ExploreTopicListAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getTitleBar().setTitle(getStringParam("categoryname"));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.find_plaza_explore_topic_list, paramViewGroup, false);
    setAgentContainerListView((ListView)paramLayoutInflater.findViewById(R.id.plaza_explore_topic_listview));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreTopicListFragment
 * JD-Core Version:    0.6.0
 */