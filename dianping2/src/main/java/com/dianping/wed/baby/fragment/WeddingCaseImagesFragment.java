package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;

public class WeddingCaseImagesFragment extends AdapterAgentFragment
{
  PullToRefreshListView pullToRefreshListView;

  public int caseId()
  {
    return getIntParam("caseid");
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingCaseImagesFragment.1(this));
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    this.pullToRefreshListView = ((PullToRefreshListView)paramLayoutInflater.inflate(R.layout.wed_product_detail, paramViewGroup, false));
    this.pullToRefreshListView.setPullRefreshEnable(0);
    setAgentContainerListView(this.pullToRefreshListView);
    return this.pullToRefreshListView;
  }

  public int shopId()
  {
    return getIntParam("shopid");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingCaseImagesFragment
 * JD-Core Version:    0.6.0
 */