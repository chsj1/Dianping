package com.dianping.main.login.nativelogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.main.login.nativelogin.agent.FastLoginAgent;
import com.dianping.main.login.nativelogin.agent.NormalLoginAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FastLoginFragment extends AgentFragment
{
  public static final String HOST = "sublogin";
  public static final String LOGIN_CELL_FAST = "sublogin/loginfast";
  public static final String LOGIN_CELL_NORMAL = "sublogin/loginnormal";

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
        if ((DPActivity.preferences().getInt("dianping.login.login_mode", 0) == 1) || ("fastlogin".equals(FastLoginFragment.this.getActivity().getIntent().getData().getHost())))
        {
          localHashMap.put("sublogin/loginfast", FastLoginAgent.class);
          return localHashMap;
        }
        localHashMap.put("sublogin/loginnormal", NormalLoginAgent.class);
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
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.activity_login, paramViewGroup, false);
    super.setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.root));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.FastLoginFragment
 * JD-Core Version:    0.6.0
 */