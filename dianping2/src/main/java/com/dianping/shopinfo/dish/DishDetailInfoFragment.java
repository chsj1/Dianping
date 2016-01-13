package com.dianping.shopinfo.dish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DishDetailInfoFragment extends GroupAgentFragment
{
  ViewGroup contentView;
  String dishshopid;
  ViewGroup mContainer;
  public View mFragmentView;

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
        localHashMap.put("dishdetail/top", DishDetailInfoAgent.class);
        localHashMap.put("dishdetail/rank", DishRankAgent.class);
        localHashMap.put("dishdetail/list", DishDetailInfoListAgent.class);
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
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.dish_detail_main_layout, null, false);
    this.contentView = ((ViewGroup)this.mFragmentView.findViewById(16908290));
    this.mContainer = ((ViewGroup)this.mFragmentView.findViewById(R.id.container));
    this.mContainer.setPadding(0, 0, 0, 0);
    setAgentContainerView(this.contentView);
    return this.mFragmentView;
  }

  public void setDishshopid(int paramInt)
  {
    this.dishshopid = (paramInt + "");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.dish.DishDetailInfoFragment
 * JD-Core Version:    0.6.0
 */