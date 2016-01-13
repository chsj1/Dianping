package com.dianping.travel.abroad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.model.City;
import com.dianping.travel.abroad.agent.HomeTripGuideAgent;
import com.dianping.travel.abroad.agent.TravelRecommendListAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TravelGuideFragment extends AgentFragment
  implements CityConfig.SwitchListener
{
  private View agentContainerView;
  private View loadingView;

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
        localHashMap.put("guide", HomeTripGuideAgent.class);
        localHashMap.put("recommend", TravelRecommendListAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void hideLoadingView()
  {
    this.loadingView.setVisibility(8);
    this.agentContainerView.setVisibility(0);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    showLoadingView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHost("overseaguide");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.travel_guide_layout, paramViewGroup, false);
    this.loadingView = paramLayoutInflater.findViewById(R.id.loading_view);
    this.agentContainerView = paramLayoutInflater.findViewById(R.id.agent_container_layout);
    setAgentContainerView((ViewGroup)this.agentContainerView);
    showLoadingView();
    return paramLayoutInflater;
  }

  public void showLoadingView()
  {
    this.agentContainerView.setVisibility(8);
    this.loadingView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.abroad.fragment.TravelGuideFragment
 * JD-Core Version:    0.6.0
 */