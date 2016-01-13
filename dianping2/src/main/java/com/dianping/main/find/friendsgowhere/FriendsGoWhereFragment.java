package com.dianping.main.find.friendsgowhere;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsGoWhereFragment extends AdapterAgentFragment
{
  public static final String FRIENDS_SHOW_CONTACT_AGENT = "FriendsShowContactAgent";

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
        localHashMap.put("friend/FeedList", FriendsFeedAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public ListView getFragmentListView()
  {
    return this.listView;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.find_friendgowhere_main_layout, null, false);
    setAgentContainerListView((NovaListView)paramLayoutInflater.findViewById(R.id.friend_listview));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.friendsgowhere.FriendsGoWhereFragment
 * JD-Core Version:    0.6.0
 */