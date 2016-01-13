package com.dianping.main.login.nativelogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.TitleBar;
import com.dianping.main.login.nativelogin.agent.FastLoginAgent;
import com.dianping.main.login.nativelogin.agent.LoginThirdAgent;
import com.dianping.main.login.nativelogin.agent.NormalLoginAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginAgentFragment extends AgentFragment
{
  public static final String HOST = "login";
  public static final String LOGIN_CELL_FAST = "login/loginfast";
  public static final String LOGIN_CELL_NORMAL = "login/loginnormal";
  public static final String LOGIN_CELL_THIRD = "login/loginthird";
  public static final int REQUEST_CODE_SIGNUP = 35;

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
        if (DPActivity.preferences().getInt("dianping.login.login_mode", 0) == 0)
        {
          localHashMap.put("login/loginfast", FastLoginAgent.class);
          localHashMap.put("login/loginthird", LoginThirdAgent.class);
          return localHashMap;
        }
        localHashMap.put("login/loginnormal", NormalLoginAgent.class);
        localHashMap.put("login/loginthird", LoginThirdAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    super.getTitleBar().addRightViewItem("注册", "register", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        LoginAgentFragment.this.startActivityForResult("dianping://signup?isFromNative=true", 35);
      }
    });
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 35) && (paramInt2 == 64033) && (getActivity() != null))
    {
      super.getActivity().setResult(64033);
      super.getActivity().finish();
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.activity_login, paramViewGroup, false);
    super.setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.root));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.LoginAgentFragment
 * JD-Core Version:    0.6.0
 */