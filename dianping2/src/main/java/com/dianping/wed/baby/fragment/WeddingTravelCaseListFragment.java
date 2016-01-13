package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class WeddingTravelCaseListFragment extends WeddingProductBaseFragment
{
  int productCategoryId;

  void dispatchProductId()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("productcategoryid", this.productCategoryId);
    dispatchAgentChanged("caselist/filterbar", localBundle);
    dispatchAgentChanged("caselist/list", localBundle);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingTravelCaseListFragment.1(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    dispatchProductId();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getArguments() != null) && (getArguments().containsKey("productcategoryid")))
      this.productCategoryId = getArguments().getInt("productcategoryid");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.common_linearlayout_fragment, paramViewGroup, false);
    setAgentContainerView((ViewGroup)paramLayoutInflater);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingTravelCaseListFragment
 * JD-Core Version:    0.6.0
 */