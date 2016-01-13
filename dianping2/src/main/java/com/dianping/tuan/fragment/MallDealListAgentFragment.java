package com.dianping.tuan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.agent.TuanAdapterCellAgent.AgentTypeCountInterFace;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.tuan.config.DefaultMallDealListConfig;
import com.dianping.tuan.framework.TuanListAgentFragment;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class MallDealListAgentFragment extends TuanListAgentFragment
  implements AgentFragment.CellStable, TuanAdapterCellAgent.AgentTypeCountInterFace
{
  static HashMap<String, Integer> agentAdpaterTypeMap = new HashMap();
  protected String[] agentIndexName;
  protected boolean intentParsed = false;
  private PullToRefreshListView pullToRefreshListView;
  final PullToRefreshListView.OnRefreshListener refreshListener = new MallDealListAgentFragment.1(this);
  FrameLayout rootView;
  CellAgent topCellAgent;
  private int topCellAgentPosition = 2147483647;
  MallDealListAgentFragment.CellContainer topCellContainer;
  public int typeCount = 0;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DefaultMallDealListConfig(this.agentIndexName));
    return localArrayList;
  }

  public int getAdapterType()
  {
    return this.typeCount;
  }

  public int getDefaultType(String paramString)
  {
    return 0;
  }

  public View getTopCellContainer()
  {
    return this.topCellContainer;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    resetAgentHashMap();
    this.agentIndexName = new String[] { "mallinfo", "filter", "deallist" };
    parseIntent();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.tuan_mall_deallist_agent_fragment, paramViewGroup, false);
    this.rootView = ((FrameLayout)paramLayoutInflater.findViewById(R.id.content_root));
    this.pullToRefreshListView = ((PullToRefreshListView)paramLayoutInflater.findViewById(R.id.tuan_mall_deallist_main_listview));
    this.pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.pullToRefreshListView.setOnRefreshListener(this.refreshListener);
    setAgentContainerListView(this.pullToRefreshListView);
    this.pullToRefreshListView.setOnScrollListener(new MallDealListAgentFragment.2(this));
    this.topCellContainer = new MallDealListAgentFragment.CellContainer(this, getActivity());
    this.topCellContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 48));
    this.topCellContainer.setVisibility(4);
    this.rootView.addView(this.topCellContainer);
    setAgentContainerView(this.rootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  public void onRefreshComplete()
  {
    this.pullToRefreshListView.onRefreshComplete();
  }

  public void parseIntent()
  {
    if (this.intentParsed)
      return;
    String str2 = getStringParam("category");
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = getStringParam("categoryid");
    if (!TextUtils.isEmpty(str1))
      setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY.toString(), str1);
    setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_ENNAME.toString(), getStringParam("categoryenname"));
    str2 = getStringParam("region");
    str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = getStringParam("regionid");
    if (!TextUtils.isEmpty(str1))
      setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY.toString(), str1);
    setSharedObject(TuanSharedDataKey.CURRENT_REGION_ENNAME.toString(), getStringParam("regionenname"));
    str1 = getStringParam("sort");
    if (!TextUtils.isEmpty(str1))
      setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY.toString(), str1);
    this.intentParsed = true;
  }

  public void resetAgentHashMap()
  {
    Iterator localIterator = DefaultMallDealListConfig.agentClassMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      agentAdpaterTypeMap.put(localEntry.getKey(), Integer.valueOf(this.typeCount));
      try
      {
        int i = this.typeCount;
        this.typeCount = (((Class)localEntry.getValue()).getField("adapterTypeCount").getInt(null) + i);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
    this.topCellContainer.reset();
    this.topCellAgent = paramCellAgent;
    this.topCellContainer.set(paramView, paramCellAgent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.MallDealListAgentFragment
 * JD-Core Version:    0.6.0
 */