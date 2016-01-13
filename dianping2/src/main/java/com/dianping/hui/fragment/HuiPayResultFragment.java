package com.dianping.hui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.dianping.advertisement.agent.AdContainer;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.hui.activity.HuiPayResultActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HuiPayResultFragment extends AgentFragment
  implements AdContainer
{
  private static final String TAG = HuiPayResultFragment.class.getSimpleName();
  private LinearLayout containerView;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new HuiPayResultFragment.1(this));
    return localArrayList;
  }

  public ScrollView getScrollView()
  {
    return ((HuiPayResultActivity)getActivity()).getScrollView();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    setHost("hui_payresult");
    super.onActivityCreated(paramBundle);
    dispatchAgentChanged(null, new Bundle());
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.hui_pay_result_fragment, paramViewGroup, false);
    this.containerView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.hui_pay_result_container));
    setAgentContainerView(this.containerView);
    return paramLayoutInflater;
  }

  public void requestAdvertisementAgent(int paramInt, String paramString)
  {
    AgentMessage localAgentMessage = new AgentMessage("com.dianping.advertisement.agent.AdAgent.UPDATE_CONTEXT");
    Bundle localBundle = new Bundle();
    localBundle.putInt("shopId", paramInt);
    localBundle.putInt("slotId", 10015);
    localAgentMessage.body = localBundle;
    dispatchMessage(localAgentMessage);
    localAgentMessage = new AgentMessage("com.dianping.advertisement.agent.NcpmAdAgent.UPDATE_CONTEXT");
    localBundle = new Bundle();
    localBundle.putInt("shopId", paramInt);
    localBundle.putInt("slotId", 10021);
    localBundle.putString("mobileno", paramString);
    localAgentMessage.body = localBundle;
    dispatchMessage(localAgentMessage);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.fragment.HuiPayResultFragment
 * JD-Core Version:    0.6.0
 */