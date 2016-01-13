package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;

public class WeddingProductPicDetailFragment extends AdapterAgentFragment
  implements PullToRefreshListView.OnRefreshListener
{
  PullToRefreshListView pullToRefreshListView;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingProductPicDetailFragment.1(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.pullToRefreshListView = ((PullToRefreshListView)paramLayoutInflater.inflate(R.layout.wed_product_detail, paramViewGroup, false));
    this.pullToRefreshListView.setPullRefreshEnable(2);
    this.pullToRefreshListView.resetHeader(2);
    this.pullToRefreshListView.setOnRefreshListener(this);
    setAgentContainerListView(this.pullToRefreshListView);
    return this.pullToRefreshListView;
  }

  public void onDetach()
  {
    super.onDetach();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    paramPullToRefreshListView.onRefreshComplete();
    getActivity().onBackPressed();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingProductPicDetailFragment
 * JD-Core Version:    0.6.0
 */