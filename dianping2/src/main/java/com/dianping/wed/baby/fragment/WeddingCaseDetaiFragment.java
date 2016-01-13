package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.app.DPActivity;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;

public class WeddingCaseDetaiFragment extends WeddingProductBaseFragment
{
  PullToRefreshScrollView pullToRefreshScrollView;

  protected void dispatchShopRequest()
  {
    dispatchAgentChanged("casedetail/booking", null);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingCaseDetaiFragment.1(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (getActivity() != null)
    {
      ((DPActivity)getActivity()).gaExtra.shop_id = Integer.valueOf(getShopId());
      ((DPActivity)getActivity()).gaExtra.biz_id = (caseId() + "");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.needProductReuqest = false;
    if (getObjectParam("shop") != null)
      this.needShopRequest = false;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.wed_casedetail_fragment, paramViewGroup, false);
    this.pullToRefreshScrollView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.wed_casedetail_scrollview));
    this.pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.bottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.bottomCellContainer = new WeddingProductBaseFragment.CellContainer(this, getActivity());
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.setVisibility(8);
    this.bottomView.addView(this.bottomCellContainer);
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingCaseDetaiFragment
 * JD-Core Version:    0.6.0
 */