package com.dianping.wed.baby.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class BabyTuanListFragment extends WeddingProductBaseFragment
{
  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BabyTuanListFragment.1(this));
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.needProductReuqest = false;
    this.needShopRequest = false;
    this.shopid = getIntParam("shopid");
  }

  @Nullable
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.common_linearlayout_fragment, paramViewGroup, false);
    paramLayoutInflater.setBackgroundColor(getResources().getColor(R.color.white));
    setAgentContainerView((ViewGroup)paramLayoutInflater);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.BabyTuanListFragment
 * JD-Core Version:    0.6.0
 */