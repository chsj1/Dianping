package com.dianping.wed.baby.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class WeddingProductRecommandFragment extends WeddingProductBaseFragment
  implements PullToRefreshBase.OnRefreshListener
{
  LinearLayout group;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingProductRecommandFragment.1(this));
    return localArrayList;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.inflate(R.layout.wedding_content_scroll, paramViewGroup, false));
    this.group = ((LinearLayout)this.pullToRefreshScrollView.findViewById(R.id.content_layout));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.pullToRefreshScrollView.setOnRefreshListener(this);
    paramLayoutInflater = this.pullToRefreshScrollView.getLoadingLayoutProxy(true, false);
    paramLayoutInflater.setLoadingDrawable(getResources().getDrawable(R.drawable.wed_load_arrow));
    paramLayoutInflater.setPullLabel("继续拖动，返回产品详情");
    paramLayoutInflater.setReleaseLabel("释放拖动， 返回产品详情");
    paramLayoutInflater.setRefreshingLabel("正在加载产品详情...");
    setAgentContainerView(this.group);
    return this.pullToRefreshScrollView;
  }

  public void onRefresh(PullToRefreshBase paramPullToRefreshBase)
  {
    getActivity().onBackPressed();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingProductRecommandFragment
 * JD-Core Version:    0.6.0
 */