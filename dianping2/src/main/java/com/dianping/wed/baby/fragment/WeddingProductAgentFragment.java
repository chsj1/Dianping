package com.dianping.wed.baby.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.configure.BabyProductInfoConfigure;
import com.dianping.wed.configure.DefaultProductInfoConfigure;
import com.dianping.wed.configure.WedProductInfoConfigure;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class WeddingProductAgentFragment extends WeddingProductBaseFragment
  implements PullToRefreshBase.OnRefreshListener
{
  protected void dispatchProductChanged()
  {
    super.dispatchProductChanged();
    if (this.dpProduct == null);
  }

  protected void dispatchShopRequest()
  {
    super.dispatchShopRequest();
    resetAgents(null);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BabyProductInfoConfigure(this.dpShop, this.dpProduct));
    localArrayList.add(new WedProductInfoConfigure(this.dpShop, this.dpProduct));
    localArrayList.add(new DefaultProductInfoConfigure());
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (getActivity() != null)
    {
      ((DPActivity)getActivity()).gaExtra.shop_id = Integer.valueOf(getShopId());
      ((DPActivity)getActivity()).gaExtra.biz_id = (getProductId() + "");
    }
    initTitleShare();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.wed_product_agent, paramViewGroup, false);
    this.rootView = ((FrameLayout)paramLayoutInflater.findViewById(R.id.root));
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.scroll));
    this.scrollView = ((MyScrollView)this.pullToRefreshScrollView.getRefreshableView());
    this.contentView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    this.bottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    paramViewGroup = this.pullToRefreshScrollView.getLoadingLayoutProxy(false, true);
    paramViewGroup.setLoadingDrawable(getResources().getDrawable(R.drawable.load_flip_arrow));
    paramViewGroup.setPullLabel("继续拖动，查看图文详情");
    paramViewGroup.setReleaseLabel("释放拖动，进入图文详情");
    paramViewGroup.setRefreshingLabel("正在加载图文详情...");
    this.pullToRefreshScrollView.setOnRefreshListener(this);
    this.scrollView.setOnScrollListener(new WeddingProductAgentFragment.1(this));
    this.topCellContainer = new WeddingProductBaseFragment.CellContainer(this, getActivity());
    this.topCellContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 48));
    this.topCellContainer.setVisibility(4);
    this.rootView.addView(this.topCellContainer);
    this.bottomCellContainer = new WeddingProductBaseFragment.CellContainer(this, getActivity());
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.setVisibility(8);
    this.bottomView.addView(this.bottomCellContainer);
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    return paramLayoutInflater;
  }

  public void onRefresh(PullToRefreshBase paramPullToRefreshBase)
  {
    paramPullToRefreshBase.postDelayed(new WeddingProductAgentFragment.2(this), 200L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingProductAgentFragment
 * JD-Core Version:    0.6.0
 */